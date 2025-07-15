import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserAuthApp extends JFrame {
    private JTextField usernameField = new JTextField(15); // Adjusted initial size
    private JPasswordField passwordField = new JPasswordField(15); // Adjusted initial size
    private JRadioButton passengerRadio = new JRadioButton("Passenger",true);
    private JRadioButton adminRadio = new JRadioButton("Admin");
    private JButton actionButton = new JButton("Login");
    private JButton switchButton = new JButton("Don't have an account? Sign Up");
    private JLabel titleLabel = new JLabel("Travel Management System");
    private JLabel formLabel = new JLabel("Login to Your Account");

    private boolean isLoginMode = true;

    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=LoginSystemDB;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "waleed_user";
    private static final String DB_PASS = "Need4Speed";

    public UserAuthApp() {
        setupWindow();
        setupComponents();
        setupLayout();
        setupEvents();
        setVisible(true);
    }

    private void setupWindow() {
        setTitle("User Authentication");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 530); // Adjusted height slightly
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(Color.WHITE);
    }

    private void setupComponents() {
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));

        formLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formLabel.setForeground(new Color(70, 70, 70));

        styleTextField(usernameField);
        styleTextField(passwordField);

        actionButton.setBackground(new Color(0, 120, 215));
        actionButton.setForeground(Color.black);
        actionButton.setFocusPainted(false);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        actionButton.setPreferredSize(new Dimension(120, 40));

        styleLinkButton(switchButton);

        ButtonGroup userTypeGroup = new ButtonGroup();
        userTypeGroup.add(passengerRadio);
        userTypeGroup.add(adminRadio);

        passengerRadio.setBackground(Color.WHITE);
        passengerRadio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passengerRadio.setFocusPainted(false);
        adminRadio.setBackground(Color.WHITE);
        adminRadio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        adminRadio.setFocusPainted(false);
    }

    private void styleTextField(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 5, 5, 5)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height + 2)); // Allow slight height increase
    }

    private void styleLinkButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(new Color(0, 102, 204));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30)); // Adjusted padding
        mainPanel.setBackground(Color.white);

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        formLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(formLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        radioPanel.setBackground(Color.WHITE);
        JLabel userTypePromptLabel = new JLabel("I am a:");
        userTypePromptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        radioPanel.add(userTypePromptLabel);
        radioPanel.add(passengerRadio);
        radioPanel.add(adminRadio);
        radioPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        radioPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, radioPanel.getPreferredSize().height));
        mainPanel.add(radioPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel for Username Label and Field
        JPanel usernamePanel = new JPanel(new BorderLayout(5,0)); // BorderLayout for better label-field alignment
        usernamePanel.setBackground(Color.WHITE);
        JLabel usernamePromptLabel = new JLabel("Username:");
        usernamePromptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernamePanel.add(usernamePromptLabel, BorderLayout.WEST);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        usernamePanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the whole username panel
        usernamePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, usernameField.getPreferredSize().height + 5));
        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Panel for Password Label and Field
        JPanel passwordPanel = new JPanel(new BorderLayout(5,0)); // BorderLayout for better label-field alignment
        passwordPanel.setBackground(Color.WHITE);
        JLabel passwordPromptLabel = new JLabel("Password:");
        passwordPromptLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordPanel.add(passwordPromptLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the whole password panel
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, passwordField.getPreferredSize().height + 5));
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        actionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(actionButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        switchButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(switchButton);

        add(mainPanel);
    }

    private void setupEvents() {
        actionButton.addActionListener(e -> handleAction());
        switchButton.addActionListener(e -> toggleMode());
    }

    private void handleAction() {
        if (isLoginMode) {
            login();
        } else {
            register();
        }
    }

    private void toggleMode() {
        isLoginMode = !isLoginMode;
        if (isLoginMode) {
            formLabel.setText("Login to Your Account");
            actionButton.setText("Login");
            switchButton.setText("Don't have an account? Sign Up");
        } else {
            formLabel.setText("Create New Account");
            actionButton.setText("Sign Up");
            switchButton.setText("Already have an account? Login");
        }
        usernameField.setText("");
        passwordField.setText("");
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String userType = adminRadio.isSelected() ? "admin" : "passenger";

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password.", "Input Error", true);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM AppUsers WHERE username = ? AND password = ? AND user_type = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, userType);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    showMessage("Login Successful! Welcome, " + username + " (" + userType + ")!", "Success", false);
                } else {
                    showMessage("Invalid username, password, or user type.", "Login Failed", true);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error during login: " + ex.getMessage(), "Database Error", true);
        }
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String userType = adminRadio.isSelected() ? "admin" : "passenger";

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Please enter both username and password for registration.", "Input Error", true);
            return;
        }
        if (password.length() < 6) {
            showMessage("Password must be at least 6 characters long.", "Input Error", true);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String checkUserQuery = "SELECT COUNT(*) FROM AppUsers WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkUserQuery)) {
                checkStmt.setString(1, username);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        showMessage("Username already exists. Please choose a different one.", "Registration Failed", true);
                        return;
                    }
                }
            }

            String insertQuery = "INSERT INTO AppUsers (username, password, user_type) VALUES (?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.setString(3, userType);

                int rowsAffected = insertStmt.executeUpdate();
                if (rowsAffected > 0) {
                    showMessage("User '" + username + "' (" + userType + ") registered successfully!", "Registration Successful", false);
                    toggleMode();
                } else {
                    showMessage("User registration failed. Please try again.", "Registration Failed", true);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            showMessage("Database error during registration: " + ex.getMessage(), "Database Error", true);
        }
    }

    private void showMessage(String messageText, String messageTitle, boolean isErrorMessage) {
        JOptionPane.showMessageDialog(this, messageText, messageTitle,
                isErrorMessage ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new UserAuthApp());
    }
}
