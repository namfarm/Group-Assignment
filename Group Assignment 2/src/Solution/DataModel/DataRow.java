package Solution.DataModel;

import Solution.dataAnalysis.DateAna;
import Solution.dataAnalysis.NumberAna;

import java.text.ParseException;
import java.util.Date;

public class DataRow {
    private final String isoCode;
    private final String continent;
    private final String location;
    private final Date date;
    private final int newCases;
    private final int newDeaths;
    private final int peopleVaccinated;
    private final int population;

    public DataRow(String isoCode, String continent, String location, Date date, int newCases, int newDeaths, int peopleVaccinated, int population) {
        this.isoCode = isoCode;
        this.continent = continent;
        this.location = location;
        this.date = date;
        this.newCases = newCases;
        this.newDeaths = newDeaths;
        this.peopleVaccinated = peopleVaccinated;
        this.population = population;
    }

    public Date getDate() {
        return date;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getContinent() {
        return continent;
    }

    public String getLocation() {
        return location;
    }

    public int getNewCases() {
        return newCases;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public int getPeopleVaccinated() {
        return peopleVaccinated;
    }

    public int getPopulation() {
        return population;
    }

    public static DataRow fromCsv(String csv) {
        String[] fields = csv.split(",");
        if (fields.length < 8) return null;
        String isoCode = fields[0];
        String continent = fields[1];
        if (continent.isEmpty()) return null;
        String location = fields[2];
        if (location.isEmpty()) return null;
        Date date;
        try {
            date = DateAna.parse(fields[3]);
        } catch (ParseException e) {
            return null;
        }
        int newCases = NumberAna.parseInt(fields[4], 0);
        int newDeaths = NumberAna.parseInt(fields[5], 0);
        int peopleVaccinated = NumberAna.parseInt(fields[6], 0);
        int population = NumberAna.parseInt(fields[7], 0);
        return new DataRow(isoCode, continent, location, date, newCases, newDeaths, peopleVaccinated, population);
    }
}
