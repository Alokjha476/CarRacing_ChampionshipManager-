import java.util.List;

interface ChampionshipManager {
    void createDriver(String driverName, String teamName);
    void deleteDriver(String driverName);
    void changeDriverForTeam(String teamName, String newDriverName);
    Formula1Driver getDriverStatistics(String driverName);
    List<Formula1Driver> getDriverTable();

    List<Race> getAllRaces();
}
