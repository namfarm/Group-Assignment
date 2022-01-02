package Solution.Menu;

import Solution.FileReader.CsvReader;
import Solution.UI.Input;
import Solution.UI.Option;
import Solution.UI.Table;
import Solution.DataModel.DailyData;
import Solution.DataModel.DataRow;
import Solution.DataModel.Region;
import Solution.dataAnalysis.DateAna;
import Solution.DataModel.Summary;
import java.util.*;

public class MainMenu extends Menu {
    private final Table groupOptionsTable = new Table(new String[]{"Option", "Group by"});
    private final Table metricOptionsTable = new Table(new String[]{"Option", "Metric"});
    private final Table calculationOptionsTable = new Table(new String[]{"Option", "Calculation type"});
    private final Table displayOptionsTable = new Table(new String[]{"Option", "Display format"});

    public Map<String, Region> regions = new HashMap<>();

    public MainMenu() {
        loadData();
        groupOptionsTable.addRow(new String[]{"1", "No grouping"});
        groupOptionsTable.addRow(new String[]{"2", "Number of groups"});
        groupOptionsTable.addRow(new String[]{"3", "Number of days"});

        metricOptionsTable.addRow(new String[]{"1", "Positive cases"});
        metricOptionsTable.addRow(new String[]{"2", "Deaths"});
        metricOptionsTable.addRow(new String[]{"3", "People vaccinated"});

        calculationOptionsTable.addRow(new String[]{"1", "New total"});
        calculationOptionsTable.addRow(new String[]{"2", "Up to total"});


        displayOptionsTable.addRow(new String[]{"1", "Table"});
        displayOptionsTable.addRow(new String[]{"2", "Chart"});

        System.out.println("Welcome to COVID-19 data analytics program");
        System.out.println("Pham Duy Nam-s3905273");
        System.out.println("Trần Minh Nhật-s3877063");
        System.out.println("La Uyên Nhi-s3878435");
        System.out.println("Bùi Đức Phát-s3914615");



        addOption(new Option("1", "Data analytics", () -> {
            dataAnalytics();
            Input.waitForEnter();
            execute();
        }));
        addOption(new Option("2", "Exit", this::exit));
    }

    private void loadData() {
        CsvReader csvReader = new CsvReader(Region.getFileName());
        List<DataRow> dataRows = new ArrayList<>();
        csvReader.getAll().forEach(row -> {
            dataRows.add(DataRow.fromCsv(row));
        });
        dataRows.forEach(row -> {
            if (row == null) return;
            String continent = row.getContinent().toLowerCase();
            String country = row.getLocation().toLowerCase();
            Date date = row.getDate();
            regions.computeIfAbsent(country, region -> new Region(country));
            regions.computeIfAbsent(continent, region -> new Region(continent));
            regions.get(country).addDailyData(new DailyData(date, row.getNewCases(), row.getNewDeaths(), row.getPeopleVaccinated()));
            regions.get(continent).addDailyData(new DailyData(date, row.getNewCases(), row.getNewDeaths(), row.getPeopleVaccinated()));
        });
    }

    public void dataAnalytics() {
        String regionName = new Input("Region: ")
                .setValid(s -> regions.containsKey(s.toLowerCase()))
                .setErr("Region not found.")
                .getInput().toLowerCase();
        Region region = regions.get(regionName);

        Date startDate = DateAna.parseNullable(new Input("Start date (MM/dd/yyyy): ")
                .setValid(s -> DateAna.parseNullable(s) != null)
                .setErr("Invalid date format.")
                .getInput());

        Date endDate = DateAna.parseNullable(new Input("End date (MM/dd/yyyy): ")
                .setValid(s -> DateAna.parseNullable(s) != null)
                .setErr("Invalid date format.")
                .getInput());

        groupOptionsTable.display();
        String groupBy = new Input("Enter an option: ")
                .setValid(s -> s.equals("1") || s.equals("2") || s.equals("3"))
                .getInput();

        metricOptionsTable.display();
        String metric = new Input("Enter an option: ")
                .setValid(s -> s.equals("1") || s.equals("2") || s.equals("3"))
                .getInput();

        calculationOptionsTable.display();
        String calculationType = new Input("Enter an option: ")
                .setValid(s -> s.equals("1") || s.equals("2"))
                .getInput();

        displayOptionsTable.display();
        String displayFormat = new Input("Enter an option: ")
                .setValid(s -> s.equals("1") || s.equals("2"))
                .getInput();
        Summary summary = new Summary(region);
        summary.compute(startDate, endDate, groupBy, metric, calculationType, displayFormat);
    }

    public void exit() {
        System.out.println("Program exits.");
    }
}
