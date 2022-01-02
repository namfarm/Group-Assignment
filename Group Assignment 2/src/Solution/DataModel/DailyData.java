package Solution.DataModel;

import java.util.Date;

public class DailyData {
    private final Date date;
    private int newCases;
    private int newDeaths;
    private int Vaccinated;

    public DailyData(Date date, int newCases, int newDeaths, int peopleVaccinated) {
        this.date = date;
        this.newCases = newCases;
        this.newDeaths = newDeaths;
        this.Vaccinated = peopleVaccinated;
    }

    public Date getDate() {
        return date;
    }

    public int getNewDeaths() {
        return newDeaths;
    }

    public int getNewCases() {
        return newCases;
    }

    public int getPeopleVaccinated() {
        return Vaccinated;
    }

    public void add(DailyData other) {
        this.newCases += other.getNewCases();
        this.newDeaths += other.getNewDeaths();
        this.Vaccinated += other.getPeopleVaccinated();
    }
}
