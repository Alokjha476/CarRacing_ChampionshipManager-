import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class Formula1ChampionshipManager implements ChampionshipManager {
    private List<Formula1Driver> drivers; // List of drivers in the championship
    private Map<String, String> constructorTeams; // Mapping of constructor team names to driver names
    private List<Race> races; // List to store race results

    public List<Formula1Driver> getDrivers() {
        return drivers;
    }

    public void addDriver(Formula1Driver driver) {
        drivers.add(driver);
    }


    public Formula1ChampionshipManager() {
        drivers = new ArrayList<>();
        constructorTeams = new HashMap<>();
        races = new ArrayList<>();
    }

    // Method to create a new driver and associate them with a unique team
    public void createDriver(String driverName, String teamName) {
        Formula1Driver newDriver = new Formula1Driver(driverName, teamName);
        drivers.add(newDriver);
        constructorTeams.put(teamName, driverName);
    }

    // Method to delete a driver and the team that the driver belongs to
    public void deleteDriver(String driverName) {
        Formula1Driver driver = findDriver(driverName);
        if (driver != null) {
            drivers.remove(driver);
            constructorTeams.remove(driver.getTeam());
        }
    }

    // Method to change the driver for an existing constructor team
    public void changeDriverForTeam(String teamName, String newDriverName) {
        Formula1Driver existingDriver = findDriver(constructorTeams.get(teamName));
        if (existingDriver != null) {
            constructorTeams.put(teamName, newDriverName);
            existingDriver.setName(newDriverName);
        }
    }

    @Override
    public Formula1Driver getDriverStatistics(String driverName) {
        return null;
    }

    @Override
    public List<Formula1Driver> getDriverTable() {
        return null;
    }

    @Override
    public List<Race> getAllRaces() {
        return races;
    }

    // Method to display the various statistics for a selected existing driver
    public void displayDriverStatistics(String driverName) {
        Formula1Driver driver = findDriver(driverName);
        if (driver != null) {
            System.out.println("Driver: " + driver.getName());
            System.out.println("Team: " + driver.getTeam());
            System.out.println("Points: " + driver.getPoints());
            System.out.println("First Positions: " + driver.getFirstPositions());
            System.out.println("Second Positions: " + driver.getSecondPositions());
            System.out.println("Third Positions: " + driver.getThirdPositions());
        }
    }

    // Method to display the Formula 1 Driver Table
    public void displayDriverTable() {
        List<Formula1Driver> sortedDrivers = drivers.stream()
                .sorted(Comparator.comparing(Formula1Driver::getPoints).reversed()
                        .thenComparing(Formula1Driver::getFirstPositions).reversed())
                .collect(Collectors.toList());

        System.out.println("Driver Table:");
        for (int i = 0; i < sortedDrivers.size(); i++) {
            Formula1Driver driver = sortedDrivers.get(i);
            System.out.println((i + 1) + ". " + driver.getName() + " (" + driver.getTeam() + ")");
            System.out.println("Points: " + driver.getPoints());
            System.out.println("First Positions: " + driver.getFirstPositions());
            System.out.println("Second Positions: " + driver.getSecondPositions());
            System.out.println("Third Positions: " + driver.getThirdPositions());
        }
    }

    // Method to add a race completed with its date and driver positions
    public void addRace(Date date, List<String> driverPositions) {
        Race race = new Race(date, driverPositions);
        races.add(race);

        for (int i = 0; i < driverPositions.size(); i++) {
            Formula1Driver driver = findDriver(driverPositions.get(i));
            if (driver != null && i < 10) {
                driver.participateInRace(i + 1);
            }
        }
    }

    // Method to save information to a file
    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(drivers);
            oos.writeObject(constructorTeams);
            oos.writeObject(races);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFromFile(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            // Read the previously saved data
            List<Formula1Driver> drivers = (List<Formula1Driver>) inputStream.readObject();
            List<Race> races = (List<Race>) inputStream.readObject();

            // Update the state of the application with the loaded data
            this.drivers = drivers;
            this.races = races;

            System.out.println("Previous state loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading previous state: " + e.getMessage());
        }
    }

    private Formula1Driver findDriver(String driverName) {
        for (Formula1Driver driver : drivers) {
            if (driver.getName().equals(driverName)) {
                return driver;
            }
        }
        return null;
    }


}


// Definition of Race class to store race results
class Race implements Serializable {
    private Date date;
    private List<String> driverPositions;

    public Race(Date date, List<String> driverPositions) {
        this.date = date;
        this.driverPositions = driverPositions;
    }

    public CharSequence getDriverPositions() {
        return this.getDriverPositions();

    }

    public Date getDate() {
        return this.date = date;
    }


    public int getDriverPosition(String driverName, Race race) {
        List<String> driverPositions = (List<String>) race.getDriverPositions();
        for (int i = 0; i < driverPositions.size(); i++) {
            if (driverPositions.get(i).equals(driverName)) {
                return i + 1; // Positions are typically 1-based in racing, so add 1 to the index
            }
        }
        // Return -1 to indicate that the driver was not found in the race positions
        return -1;
    }

}
