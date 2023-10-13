import java.util.List;

public abstract class Driver {
    private String name;
    private String team;
    private int date;
    private List<Race> races;

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
    }

    public int getDate() {
        return date;
    }

    public Driver(String name, String team) {
        this.name = name;
        this.team = team;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    // Abstract method to be implemented by subclasses
    public abstract int getPoints();
    public  String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

class Formula1Driver extends Driver {
    private int firstPositions;
    private int secondPositions;
    private int thirdPositions;
    private int totalPoints;
    private int racesParticipated;

    public Formula1Driver(String name, String team) {
        super(name, team);
        this.firstPositions = 0;
        this.secondPositions = 0;
        this.thirdPositions = 0;
        this.totalPoints = 0;
        this.racesParticipated = 0;
    }

    public int getFirstPositions() {
        return firstPositions;
    }

    public int getSecondPositions() {
        return secondPositions;
    }

    public int getThirdPositions() {
        return thirdPositions;
    }

    @Override
    public int getPoints() {
        return totalPoints;
    }

    public int getRacesParticipated() {
        return racesParticipated;
    }

    public void participateInRace(int position) {
        if (position >= 1 && position <= 10) {
            racesParticipated++;

            if (position == 1) {
                firstPositions++;
                totalPoints += 25;
            } else if (position == 2) {
                secondPositions++;
                totalPoints += 18;
            } else if (position == 3) {
                thirdPositions++;
                totalPoints += 15;
            } else if (position == 4) {
                totalPoints += 12;
            } else if (position == 5) {
                totalPoints += 10;
            } else if (position == 6) {
                totalPoints += 8;
            } else if (position == 7) {
                totalPoints += 6;
            } else if (position == 8) {
                totalPoints += 4;
            } else if (position == 9) {
                totalPoints += 2;
            } else if (position == 10) {
                totalPoints += 1;
            }
        }
    }



}
