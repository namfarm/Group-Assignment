package Solution.DataModel;

import Solution.dataAnalysis.DateAna;

import java.util.HashMap;
import java.util.Map;

public class Region {
    private static final String fileName = "covid-data.csv";
    private final String name;
    private final Map<String, DailyData> dailyDataMap = new HashMap<>();

    public void addDailyData(DailyData dailyData) {
        String dateString = DateAna.format(dailyData.getDate());
        if (dailyDataMap.containsKey(dateString)) {
            dailyDataMap.get(dateString).add(dailyData);
        } else {
            dailyDataMap.put(dateString, dailyData);
        }
    }

    public Region(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Map<String, DailyData> getDailyDataMap() {
        return dailyDataMap;
    }

    public static String getFileName() {
        return fileName;
    }
}
