package Solution.DataModel;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import Solution.UI.Input;
import Solution.UI.Table;
import Solution.dataAnalysis.DateAna;
import Solution.Menu.DisplayRow;
import Solution.dataAnalysis.NumberAna;
import Solution.dataAnalysis.StringAna;
public class Summary {
    private final Region region;

    public Summary(Region region) {
        this.region = region;
    }

    public int ValueBefore(Date dateBefore, String metric) {
        List<DailyData> dailyDataList = new ArrayList<>(region.getDailyDataMap().values());
        dailyDataList.sort(Comparator.comparing(DailyData::getDate));
        int value = 0;
        for (var dailyData : dailyDataList) {
            if (!dailyData.getDate().before(dateBefore)) break;
            if (metric.equals("1")) value += dailyData.getNewCases();
            else if (metric.equals("2")) value += dailyData.getNewDeaths();
            else value = dailyData.getPeopleVaccinated();
        }
        return value;
    }

    public void compute(Date startDate, Date endDate, String groupBy, String metric, String calculationType, String displayFormat) {
        List<DailyData> dailyDataList = new ArrayList<>(region.getDailyDataMap().values());
        dailyDataList = dailyDataList.stream().filter(dailyData -> DateAna.isBetween(dailyData.getDate(), startDate, endDate)).collect(Collectors.toList());
        dailyDataList.sort(Comparator.comparing(DailyData::getDate));
        if (dailyDataList.size() == 0) {
            System.out.println("There is no daily data available in your provided time range");
            return;
        }
        System.out.println("Start date: " + DateAna.format(dailyDataList.get(0).getDate()));
        System.out.println("End date: " + DateAna.format(dailyDataList.get(dailyDataList.size() - 1).getDate()));
        System.out.println("Total number of days: " + dailyDataList.size());
        List<List<DailyData>> groups = groupBy.equals("1") ? divideIntoSeparateGroup(dailyDataList)
                : groupBy.equals("2") ? divideByNumberOfGroups(dailyDataList) : divideByNumberOfDays(dailyDataList);

        if (displayFormat.equals("1")) {
            displayTable(groups, metric, calculationType);
        } else {
            displayChart(groups, metric, calculationType);
        }
    }

    public void displayTable(List<List<DailyData>> groups, String metric, String calculationType) {
        Table table = new Table(new String[]{"Range", "Value"});
        var displayRows = computeDisplayRows(groups, metric, calculationType);
        displayRows.forEach(row -> table.addRow(new String[]{row.getRange(), String.valueOf(row.getValue())}));
        table.display();
    }

    public List<DisplayRow> computeDisplayRows(List<List<DailyData>> groups, String metric, String calculationType) {
        List<DisplayRow> displayRows = new ArrayList<>();
        AtomicInteger accumulateValue = new AtomicInteger(ValueBefore(groups.get(0).get(0).getDate(), metric));
        groups.forEach(group -> {
            AtomicInteger groupValue = new AtomicInteger(0);
            group.forEach(dailyData -> {
                int metricValue = metric.equals("1") ? dailyData.getNewCases()
                        : metric.equals("2") ? dailyData.getNewDeaths()
                        : dailyData.getPeopleVaccinated();
                if (!metric.equals("3")) {
                    accumulateValue.addAndGet(metricValue);
                    groupValue.addAndGet(metricValue);
                }
            });
            if (metric.equals("3")) {
                int lastDayVaccinated = group.get(group.size() - 1).getPeopleVaccinated();
                groupValue.set(Math.max(lastDayVaccinated - accumulateValue.get(), 0));
                if (lastDayVaccinated >= accumulateValue.get()) accumulateValue.set(lastDayVaccinated);
            }

            String rangeDisplay = "";
            // Display differently for 1 day vs multiple days
            if (group.size() == 1) {
                rangeDisplay = DateAna.format(group.get(0).getDate());
            } else if (group.size() > 1) {
                rangeDisplay = DateAna.format(group.get(0).getDate()) + " - " + DateAna.format(group.get(group.size() - 1).getDate());
            }
            displayRows.add(new DisplayRow(rangeDisplay, calculationType.equals("1") ? groupValue.get() : accumulateValue.get()));
        });
        return displayRows;
    }


    public void displayChart(List<List<DailyData>> groups, String metric, String calculationType) {
        int ROWS = 23;
        int COLS = 79;
        var displayRows = computeDisplayRows(groups, metric, calculationType);
        int rowsCount = displayRows.size();
        if (rowsCount == 0) {
            System.out.println("No group to display!");
            return;
        }
        if (rowsCount > 79) {
            System.out.println("Too much groups to display! Number of groups should not be more than " + COLS);
            return;
        }

        int maxValue = displayRows.stream().mapToInt(DisplayRow::getValue).max().orElse(0);
        int minValue = displayRows.stream().mapToInt(DisplayRow::getValue).min().orElse(0);
        int yScale = (maxValue - minValue) / ROWS + 1;
        int xScale = COLS / rowsCount;

        Map<Integer, List<Integer>> yToXPositions = new HashMap<>();

        for (int i = 0; i < rowsCount; i++) {
            int value = displayRows.get(i).getValue();
            int xPosition = i * xScale;

            int yPosition = Math.min(ROWS - (value - minValue) / yScale, ROWS - 1);
            yToXPositions.computeIfAbsent(yPosition, position -> new ArrayList<>());
            yToXPositions.get(yPosition).add(xPosition);
        }
        for (int i = 0; i < ROWS; i++) {
            System.out.print("|");
            String col = StringAna.multiply(" ", COLS);
            if (yToXPositions.containsKey(i))
                col = StringAna.replace(col, yToXPositions.get(i).stream().mapToInt(x -> x).toArray(), '*');
            System.out.println(col);
        }
        System.out.println(StringAna.multiply("_", COLS + 1));
    }


    public List<List<DailyData>> divideIntoSeparateGroup(List<DailyData> dailyDataList) {
        return dailyDataList.stream().map(Collections::singletonList).collect(Collectors.toList());
    }

    public List<List<DailyData>> divideByNumberOfGroups(List<DailyData> dailyDataList) {
        int numberOfGroups = Integer.parseInt(new Input("Number of groups: ").setValid(s -> {
            if (!NumberAna.isInt(s)) return false;
            int value = Integer.parseInt(s);
            return value > 0 && value <= dailyDataList.size();
        }).setErr("Invalid number of groups.").getInput());
        List<List<DailyData>> groups = new ArrayList<>();
        int daysCount = dailyDataList.size();
        int daysPerGroup = daysCount / numberOfGroups;
        int from = 0;
        for (int i = 0; i < numberOfGroups - 1; i++) {
            int to = from + daysPerGroup;
            groups.add(dailyDataList.subList(from, to));
            from = to;
        }
        int to = daysCount;
        groups.add(dailyDataList.subList(from, to));
        return groups;
    }

    public List<List<DailyData>> divideByNumberOfDays(List<DailyData> dailyDataList) {
        int daysCount = dailyDataList.size();

        int daysPerGroup = Integer.parseInt(new Input("Number of days per group: ").setValid(s -> {
            if (!NumberAna.isInt(s)) return false;
            int value = Integer.parseInt(s);
            if (value <= 0 || value > daysCount) return false;
            if (daysCount % value != 0) {
                System.out.println("Cannot divide list into groups with equal number of days.");
                return false;
            }
            return true;
        }).setErr("Invalid number of days.").getInput());
        List<List<DailyData>> groups = new ArrayList<>();
        int from = 0;
        int numberOfGroups = daysCount / daysPerGroup;
        for (int i = 0; i < numberOfGroups; i++) {
            int to = from + daysPerGroup;
            groups.add(dailyDataList.subList(from, to));
            from = to;
        }
        return groups;
    }
}
