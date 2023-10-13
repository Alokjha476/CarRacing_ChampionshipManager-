import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static javax.swing.SwingUtilities.*;

public class Formula1ChampionshipGUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable driversTable;
    private JButton sortPointsButton;
    private JButton sortFirstPositionsButton;
    private JButton randomRaceButton;
    private JButton probabilisticRaceButton;
    private JButton viewRacesButton;
    private JTextField searchDriverField;
    private JButton searchDriverButton;

    private Formula1ChampionshipManager manager; // Your existing manager instance
    private Iterable<? extends Formula1Driver> driver1;

    public Formula1ChampionshipGUI(Formula1ChampionshipManager manager) {
        this.manager = manager;

        setTitle("Formula 1 Championship Manager");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize the table with driver statistics
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Driver");
        tableModel.addColumn("Team");
        tableModel.addColumn("Points");
        tableModel.addColumn("1st Positions");
        tableModel.addColumn("2nd Positions");
        tableModel.addColumn("3rd Positions");
        driversTable = new JTable(tableModel);

        // Create a scroll pane to hold the table
        JScrollPane scrollPane = new JScrollPane(driversTable);

        // Create buttons
        sortPointsButton = new JButton("Sort by Points");
        sortFirstPositionsButton = new JButton("Sort by 1st Positions");
        randomRaceButton = new JButton("Random Race");
        probabilisticRaceButton = new JButton("Probabilistic Race");
        viewRacesButton = new JButton("View Races");
        searchDriverField = new JTextField(20);
        searchDriverButton = new JButton("Search Driver's Races");

        // Set up the layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sortPointsButton);
        buttonPanel.add(sortFirstPositionsButton);
        buttonPanel.add(randomRaceButton);
        buttonPanel.add(probabilisticRaceButton);
        buttonPanel.add(viewRacesButton);
        buttonPanel.add(searchDriverField);
        buttonPanel.add(searchDriverButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners for buttons
        sortPointsButton.addActionListener(e -> sortTableByPoints());
        sortFirstPositionsButton.addActionListener(e -> sortTableByFirstPositions());
        randomRaceButton.addActionListener(e -> simulateRandomRace());
        probabilisticRaceButton.addActionListener(e -> simulateProbabilisticRace());
        viewRacesButton.addActionListener(e -> viewAllRaces());
        searchDriverButton.addActionListener(e -> searchDriverRaces());

        // Make the frame visible
        setVisible(true);
    }

    // Method to update the table with driver data
    private void updateDriversTable() {
        DefaultTableModel model = (DefaultTableModel) driversTable.getModel();
        model.setRowCount(0); // Clear the existing rows in the table

        // Iterate through your list of drivers and add each driver to the table
        for (Formula1Driver driver : driver1) {
            Object[] rowData = {
                    driver.getName(),
                    driver.getLocation(),
                    driver.getTeam(),
                    driver.getPoints(),
                    driver.getRacesParticipated()
            };
            model.addRow(rowData);// Add the data for each driver to the table
        }
    }


    // Method to sort the table by driver points
    private void sortTableByPoints() {
        List<Formula1Driver> drivers = manager.getDriverTable();
        tableModel.setRowCount(0);
        drivers.forEach(driver -> {
            tableModel.addRow(new Object[]{
                    driver.getName(),
                    driver.getTeam(),
                    driver.getPoints(),
                    driver.getFirstPositions(),
                    driver.getSecondPositions(),
                    driver.getThirdPositions()
            });
        });

    }

    // Method to sort the table by the largest number of 1st positions
    private void sortTableByFirstPositions() {
        List<Formula1Driver> drivers = manager.getDriverTable();
        drivers.sort(Comparator.comparingInt(Formula1Driver::getFirstPositions).reversed());
        tableModel.setRowCount(0);
        drivers.forEach(driver -> {
            tableModel.addRow(new Object[]{
                    driver.getName(),
                    driver.getTeam(),
                    driver.getPoints(),
                    driver.getFirstPositions(),
                    driver.getSecondPositions(),
                    driver.getThirdPositions()
            });
        });
    }

    // Method to simulate a random race and update the table
    private AtomicReference<List<String>> simulateRandomRace() {
        List<String> driverPositions = generateRandomDriverPositions();
        manager.addRace(new Date(), driverPositions);
        updateDriversTable();
        return null;
    }

    // Method to simulate a probabilistic race and update the table
    private void simulateProbabilisticRace() {
        AtomicReference<List<String>> driverPositions = simulateRandomRace();
        manager.addRace(new Date(), driverPositions.get());
        updateDriversTable();
    }

    private void viewAllRaces() {
        // Create a new dialog to display race details
        JDialog raceDialog = new JDialog(this, "All Completed Races", true);
        raceDialog.setLayout(new BorderLayout());

        // Create a table model for the races
        DefaultTableModel raceTableModel = new DefaultTableModel();
        raceTableModel.addColumn("Date");
        raceTableModel.addColumn("Driver Positions");

        JTable raceTable = new JTable(raceTableModel);
        JScrollPane scrollPane = new JScrollPane(raceTable);

        // Populate the race table with completed race details
        List<Race> allRaces = manager.getAllRaces(); // Implement this method in Formula1ChampionshipManager
        for (Race race : allRaces) {
            String date = String.valueOf(race.toString().equals(allRaces));
            String positions = String.join(", ", race.getDriverPositions());
            raceTableModel.addRow(new Object[]{date, positions});
        }

        raceDialog.add(scrollPane, BorderLayout.CENTER);
        raceDialog.setSize(600, 400);
        raceDialog.setVisible(true);
    }
    private void searchDriverRaces() {
        String driverName = searchDriverField.getText();
        Formula1Driver driver = findDriver(driverName);

        if (driver != null) {
            // Create a new dialog to display the driver's race details
            JDialog driverRacesDialog = new JDialog(this, "Races for " + driverName, true);
            driverRacesDialog.setSize(600, 400);
            driverRacesDialog.setLayout(new BorderLayout());

            DefaultTableModel driverRacesTableModel = new DefaultTableModel();
            driverRacesTableModel.addColumn("Date");
            driverRacesTableModel.addColumn("Position");

            JTable driverRacesTable = new JTable(driverRacesTableModel);
            JScrollPane scrollPane = new JScrollPane(driverRacesTable);

            // Fetch and populate the table with the driver's race details
            List<Race> driverRaces = driver.getRaces();
            for (Race race : driverRaces) {
                String date = race.getDate().toString();
                List<String> position = (List<String>) race.getDriverPositions();
                driverRacesTableModel.addRow(new Object[]{date, position});
            }

            driverRacesDialog.add(scrollPane, BorderLayout.CENTER);
            driverRacesDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Driver not found!", "Driver Not Found", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Helper method to find a driver by name
    private Formula1Driver findDriver(String driverName) {
        for (Formula1Driver driver : manager.getDrivers()) {
            if (driver.getName().equals(driverName)) {
                return driver;
            }
        }
        return null;
    }


    // Method to generate random driver positions for a race
    public List<String> generateRandomDriverPositions() {
        List<String> driverNames = new ArrayList<>();
        driverNames.add("Driver1");
        driverNames.add("Driver2");
        driverNames.add("Driver3");
        Collections.shuffle(driverNames);
        List<String> randomPositions = driverNames.subList(0,2);

        return randomPositions;
    }
    public static void main(String[] args) {
        invokeLater(() -> {
            Formula1ChampionshipManager manager = new Formula1ChampionshipManager();
            new Formula1ChampionshipGUI(manager);
        });
    }
}

