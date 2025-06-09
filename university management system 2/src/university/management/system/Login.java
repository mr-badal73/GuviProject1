package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class Login extends JFrame implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(Login.class.getName());
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final int MAX_LOGIN_ATTEMPTS = 3;
    
    private JTextField textFieldName;
    private JPasswordField passwordField;
    private JButton login, back;
    private int loginAttempts = 0;
    private JLabel statusLabel;

    public Login() {
        initializeUI();
    }

    private void initializeUI() {
        setSize(600, 300);
        setLocation(500, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("University Management System - Login");

        // Set background image
        ImageIcon bgIcon = new ImageIcon(ClassLoader.getSystemResource("icon/loginback.png"));
        Image bgImg = bgIcon.getImage().getScaledInstance(600, 300, Image.SCALE_DEFAULT);
        JLabel background = new JLabel(new ImageIcon(bgImg));
        background.setBounds(0, 0, 600, 300);
        add(background);

        // Add components
        addComponents(background);

        // Status label for feedback
        statusLabel = new JLabel("");
        statusLabel.setBounds(40, 180, 300, 20);
        statusLabel.setForeground(Color.RED);
        background.add(statusLabel);

        setVisible(true);
    }

    private void addComponents(JLabel background) {
        // Username components
        JLabel labelName = new JLabel("Username");
        labelName.setBounds(40, 20, 100, 20);
        background.add(labelName);

        textFieldName = new JTextField();
        textFieldName.setBounds(150, 20, 150, 20);
        background.add(textFieldName);

        // Password components
        JLabel labelpass = new JLabel("Password");
        labelpass.setBounds(40, 70, 100, 20);
        background.add(labelpass);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 70, 150, 20);
        background.add(passwordField);

        // Buttons
        login = new JButton("Login");
        login.setBounds(40, 140, 120, 30);
        login.setBackground(Color.black);
        login.setForeground(Color.red);
        login.addActionListener(this);
        background.add(login);

        back = new JButton("Back");
        back.setBounds(180, 140, 120, 30);
        back.setBackground(Color.black);
        back.setForeground(Color.red);
        back.addActionListener(this);
        background.add(back);

        // Logo
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("icon/second.png"));
        Image img = icon.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        imgLabel.setBounds(350, 20, 200, 200);
        background.add(imgLabel);
    }

    private boolean validateInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            showError("Username cannot be empty");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            showError("Password cannot be empty");
            return false;
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            showError("Invalid username format");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.RED);
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.GREEN);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            handleLogin();
        } else if (e.getSource() == back) {
            handleBack();
        }
    }

    private void handleLogin() {
        String username = textFieldName.getText().trim();
        String password = new String(passwordField.getPassword());

        if (!validateInput(username, password)) {
            return;
        }

        if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
            showError("Too many login attempts. Please try again later.");
            login.setEnabled(false);
            return;
        }

        try {
            Conn conn = Conn.getInstance();
            String query = "SELECT * FROM login WHERE username = ? AND password = ?";
            java.sql.PreparedStatement pstmt = conn.getConnection().prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password); // In production, use proper password hashing

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                showSuccess("Login successful!");
                loginAttempts = 0;
                setVisible(false);
                new main_class();
            } else {
                loginAttempts++;
                showError("Invalid username or password. Attempts remaining: " + (MAX_LOGIN_ATTEMPTS - loginAttempts));
            }
            pstmt.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Database error during login", ex);
            showError("System error. Please try again later.");
        }
    }

    private void handleBack() {
        setVisible(false);
        new Splash();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to set system look and feel", e);
        }
        SwingUtilities.invokeLater(() -> new Splash());
    }
}