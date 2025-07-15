import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Vehicle {
    String id, type;
    int capacity;
    public Vehicle(String id, String type, int capacity) {
        this.id = id; this.type = type; this.capacity = capacity;
    }
    @Override public String toString() {
        return "ID: " + id + ", Type: " + type + ", Capacity: " + capacity;
    }
}

class Schedule {
    String id, vehicleId, route, time, date;
    public Schedule(String id, String vehicleId, String route, String time, String date) {
        this.id = id; this.vehicleId = vehicleId; this.route = route; this.time = time; this.date = date;
    }
    @Override public String toString() {
        return "ID: " + id + ", Vehicle: " + vehicleId + ", Route: " + route + ", Time: " + time + ", Date: " + date;
    }
}

class Booking {
    String id, passengerName, vehicleId, date;
    int seats;
    public Booking(String id, String passengerName, String vehicleId, String date, int seats) {
        this.id = id; this.passengerName = passengerName; this.vehicleId = vehicleId; this.date = date; this.seats = seats;
    }
    @Override public String toString() {
        return "ID: " + id + ", Passenger: " + passengerName + ", Vehicle: " + vehicleId + ", Date: " + date + ", Seats: " + seats;
    }
}

public class TransportManagementGUI extends JFrame {

    private List<Vehicle> vehicles = new ArrayList<>();
    private List<Schedule> schedules = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    private JPanel mainPanel, adminPanel, passengerPanel;
    private JPanel viewVehiclesPanel, viewSchedulesPanel, viewBookingsPanel;

    private CardLayout cardLayout = new CardLayout();
    private JPanel cardPanel;

    public TransportManagementGUI() {
        setTitle("Transport Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeMockData();

        cardPanel = new JPanel(cardLayout);
        add(cardPanel);

        createMainPanel();
        createAdminPanel();
        createPassengerPanel();
        viewVehiclesPanel = createViewPanel("All Vehicles", vehicles, "Admin");
        viewSchedulesPanel = createViewPanel("All Schedules", schedules, "Admin");
        viewBookingsPanel = createViewPanel("All Bookings", bookings, "Main"); // Re-used for passenger view

        cardPanel.add(mainPanel, "Main");
        cardPanel.add(adminPanel, "Admin");
        cardPanel.add(passengerPanel, "Passenger");
        cardPanel.add(viewVehiclesPanel, "ViewVehicles");
        cardPanel.add(viewSchedulesPanel, "ViewSchedules");
        cardPanel.add(viewBookingsPanel, "ViewBookings");

        cardLayout.show(cardPanel, "Main");
    }

    private void initializeMockData() {
        vehicles.add(new Vehicle("V001", "Bus", 50));
        vehicles.add(new Vehicle("V002", "Train", 200));
        schedules.add(new Schedule("S001", "V001", "City A-B", "08:00", "2025-06-01"));
        bookings.add(new Booking("B001", "Alice", "V001", "2025-06-01", 2));
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(220, 230, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Welcome to Transport Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 70, 90));

        JButton adminButton = createStyledButton("Admin Login");
        adminButton.addActionListener(e -> cardLayout.show(cardPanel, "Admin"));
        JButton passengerButton = createStyledButton("Passenger Login");
        passengerButton.addActionListener(e -> cardLayout.show(cardPanel, "Passenger"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; mainPanel.add(titleLabel, gbc);
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL; mainPanel.add(adminButton, gbc);
        gbc.gridx = 1; mainPanel.add(passengerButton, gbc);
    }

    private void createAdminPanel() {
        adminPanel = new JPanel(new GridBagLayout());
        adminPanel.setBackground(new Color(230, 240, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Admin Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 90, 70));

        JButton addVehicleButton = createStyledButton("Add Vehicle");
        addVehicleButton.addActionListener(e -> showAddVehicleDialog());
        JButton viewVehiclesButton = createStyledButton("View Vehicles");
        viewVehiclesButton.addActionListener(e -> { updateViewPanel(viewVehiclesPanel, vehicles); cardLayout.show(cardPanel, "ViewVehicles"); });
        JButton addScheduleButton = createStyledButton("Add Schedule (Simulated)");
        addScheduleButton.addActionListener(e -> {
            String newSchedId = "S" + String.format("%03d", schedules.size() + 1);
            schedules.add(new Schedule(newSchedId, "V001", "New Route", "12:00", "2025-06-03"));
            JOptionPane.showMessageDialog(this, "Schedule " + newSchedId + " added!", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateAllViewPanels();
        });
        JButton viewSchedulesButton = createStyledButton("View Schedules");
        viewSchedulesButton.addActionListener(e -> { updateViewPanel(viewSchedulesPanel, schedules); cardLayout.show(cardPanel, "ViewSchedules"); });
        JButton viewAllBookingsButton = createStyledButton("View All Bookings");
        viewAllBookingsButton.addActionListener(e -> { updateViewPanel(viewBookingsPanel, bookings); cardLayout.show(cardPanel, "ViewBookings"); });
        JButton backButton = createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; adminPanel.add(titleLabel, gbc);
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL; adminPanel.add(addVehicleButton, gbc);
        gbc.gridx = 1; adminPanel.add(viewVehiclesButton, gbc);
        gbc.gridx = 0; gbc.gridy = 2; adminPanel.add(addScheduleButton, gbc);
        gbc.gridx = 1; adminPanel.add(viewSchedulesButton, gbc);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; adminPanel.add(viewAllBookingsButton, gbc);
        gbc.gridy = 4; adminPanel.add(backButton, gbc);
    }

    private void showAddVehicleDialog() {
        JTextField idField = new JTextField(10);
        JTextField typeField = new JTextField(10);
        JTextField capacityField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Vehicle ID:")); panel.add(idField);
        panel.add(new JLabel("Type:")); panel.add(typeField);
        panel.add(new JLabel("Capacity:")); panel.add(capacityField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Enter New Vehicle Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String type = typeField.getText().trim();
                int capacity = Integer.parseInt(capacityField.getText().trim());
                if (id.isEmpty() || type.isEmpty() || capacity <= 0) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled and capacity positive.", "Input Error", JOptionPane.ERROR_MESSAGE); return;
                }
                if (vehicles.stream().anyMatch(v -> v.id.equalsIgnoreCase(id))) {
                    JOptionPane.showMessageDialog(this, "Vehicle ID exists. Use unique ID.", "Input Error", JOptionPane.ERROR_MESSAGE); return;
                }
                vehicles.add(new Vehicle(id, type, capacity));
                JOptionPane.showMessageDialog(this, "Vehicle " + id + " added!", "Success", JOptionPane.INFORMATION_MESSAGE);
                updateAllViewPanels();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid capacity. Enter a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void createPassengerPanel() {
        passengerPanel = new JPanel(new GridBagLayout());
        passengerPanel.setBackground(new Color(240, 220, 230));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Passenger Portal");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(90, 50, 70));

        JButton bookTicketButton = createStyledButton("Book a Ticket (Simulated)");
        bookTicketButton.addActionListener(e -> {
            String newBookingId = "B" + String.format("%03d", bookings.size() + 1);
            bookings.add(new Booking(newBookingId, "New Passenger", "V003", "2025-06-05", 1));
            JOptionPane.showMessageDialog(this, "Ticket " + newBookingId + " booked!", "Success", JOptionPane.INFORMATION_MESSAGE);
            updateAllViewPanels();
        });
        JButton viewMyBookingsButton = createStyledButton("View My Bookings");
        viewMyBookingsButton.addActionListener(e -> { updateViewPanel(viewBookingsPanel, bookings); cardLayout.show(cardPanel, "ViewBookings"); });
        JButton backButton = createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Main"));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; passengerPanel.add(titleLabel, gbc);
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL; passengerPanel.add(bookTicketButton, gbc);
        gbc.gridx = 1; passengerPanel.add(viewMyBookingsButton, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; passengerPanel.add(backButton, gbc);
    }

    private JPanel createViewPanel(String title, List<?> dataList, String backToCard) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(70, 70, 70));
        panel.add(titleLabel, BorderLayout.NORTH);

        JTextArea listArea = new JTextArea();
        listArea.setEditable(false);
        listArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        listArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.add(new JScrollPane(listArea), BorderLayout.CENTER);

        JButton backButton = createStyledButton("Back to " + backToCard + " Dashboard");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, backToCard));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void updateViewPanel(JPanel panel, List<?> dataList) {
        JTextArea listArea = (JTextArea) ((JScrollPane) panel.getComponent(1)).getViewport().getView();
        StringBuilder sb = new StringBuilder();
        if (dataList.isEmpty()) {
            sb.append("No items added yet.");
        } else {
            for (Object item : dataList) {
                sb.append(item.toString()).append("\n");
            }
        }
        listArea.setText(sb.toString());
    }

    private void updateAllViewPanels() {
        updateViewPanel(viewVehiclesPanel, vehicles);
        updateViewPanel(viewSchedulesPanel, schedules);
        updateViewPanel(viewBookingsPanel, bookings);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.putClientProperty("JButton.buttonType", "roundRect");
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TransportManagementGUI().setVisible(true);
        });
    }
}