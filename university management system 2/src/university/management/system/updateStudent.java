package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class updateStudent extends JFrame implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(updateStudent.class.getName());
    private static final Color BACKGROUND_COLOR = new Color(210, 252, 218);
    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 700;
    
    private Choice rollNoChoice;
    private JTextField nameField, fatherField, addressField, phoneField, emailField;
    private JComboBox<String> courseChoice, branchChoice;
    private JButton search, update, cancel;
    private JLabel statusLabel;

    public updateStudent() {
        initializeUI();
        loadStudentData();
    }

    private void initializeUI() {
        setTitle("Update Student Details");
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(300, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Search components
        setupSearchComponents();
        
        // Input fields
        setupInputFields();
        
        // Buttons
        setupButtons();
        
        // Status label
        setupStatusLabel();

        setVisible(true);
    }

    private void setupSearchComponents() {
        JLabel heading = new JLabel("Search by Roll Number");
        heading.setBounds(20, 20, 150, 20);
        add(heading);

        rollNoChoice = new Choice();
        rollNoChoice.setBounds(180, 20, 150, 20);
        add(rollNoChoice);
    }

    private void setupInputFields() {
        // Name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(20, 60, 150, 20);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(180, 60, 150, 20);
        add(nameField);

        // Father's Name
        JLabel fatherLabel = new JLabel("Father's Name");
        fatherLabel.setBounds(20, 100, 150, 20);
        add(fatherLabel);

        fatherField = new JTextField();
        fatherField.setBounds(180, 100, 150, 20);
        add(fatherField);

        // Course
        JLabel courseLabel = new JLabel("Course");
        courseLabel.setBounds(20, 140, 150, 20);
        add(courseLabel);

        String[] courses = {"B.Tech", "M.Tech", "BBA", "MBA", "BCA", "MCA"};
        courseChoice = new JComboBox<>(courses);
        courseChoice.setBounds(180, 140, 150, 20);
        add(courseChoice);

        // Branch
        JLabel branchLabel = new JLabel("Branch");
        branchLabel.setBounds(20, 180, 150, 20);
        add(branchLabel);

        String[] branches = {"Computer Science", "Information Technology", "Electronics", "Mechanical", "Civil", "Electrical"};
        branchChoice = new JComboBox<>(branches);
        branchChoice.setBounds(180, 180, 150, 20);
        add(branchChoice);

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(20, 220, 150, 20);
        add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(180, 220, 150, 20);
        add(addressField);

        // Phone
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(20, 260, 150, 20);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(180, 260, 150, 20);
        add(phoneField);

        // Email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(20, 300, 150, 20);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(180, 300, 150, 20);
        add(emailField);
    }

    private void setupButtons() {
        search = createButton("Search", 20, 340);
        update = createButton("Update", 120, 340);
        cancel = createButton("Cancel", 220, 340);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 80, 20);
        button.addActionListener(this);
        add(button);
        return button;
    }

    private void setupStatusLabel() {
        statusLabel = new JLabel("");
        statusLabel.setBounds(20, WINDOW_HEIGHT - 80, WINDOW_WIDTH - 40, 20);
        statusLabel.setForeground(Color.RED);
        add(statusLabel);
    }

    private void loadStudentData() {
        try {
            Conn conn = Conn.getInstance();
            ResultSet resultSet = conn.getStatement().executeQuery("SELECT rollno FROM student ORDER BY rollno");
            
            rollNoChoice.removeAll();
            while (resultSet.next()) {
                rollNoChoice.add(resultSet.getString("rollno"));
            }
            showStatus("Student data loaded successfully", false);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading student data", e);
            showStatus("Error loading student data: " + e.getMessage(), true);
        }
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : Color.GREEN);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == search) {
                handleSearch();
            } else if (e.getSource() == update) {
                handleUpdate();
            } else if (e.getSource() == cancel) {
                handleCancel();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error handling action", ex);
            showStatus("An error occurred: " + ex.getMessage(), true);
        }
    }

    private void handleSearch() {
        String selectedRollNo = rollNoChoice.getSelectedItem();
        if (selectedRollNo == null || selectedRollNo.trim().isEmpty()) {
            showStatus("Please select a roll number", true);
            return;
        }

        try {
            Conn conn = Conn.getInstance();
            String query = "SELECT * FROM student WHERE rollno = ?";
            
            PreparedStatement pstmt = conn.getConnection().prepareStatement(query);
            pstmt.setString(1, selectedRollNo);
            ResultSet resultSet = pstmt.executeQuery();
            
            if (resultSet.next()) {
                nameField.setText(resultSet.getString("name"));
                fatherField.setText(resultSet.getString("father"));
                courseChoice.setSelectedItem(resultSet.getString("course"));
                branchChoice.setSelectedItem(resultSet.getString("branch"));
                addressField.setText(resultSet.getString("address"));
                phoneField.setText(resultSet.getString("phone"));
                emailField.setText(resultSet.getString("email"));
                showStatus("Student details found", false);
            } else {
                showStatus("No student found with roll number: " + selectedRollNo, true);
            }
            pstmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching student details", e);
            showStatus("Error searching student details: " + e.getMessage(), true);
        }
    }

    private void handleUpdate() {
        String selectedRollNo = rollNoChoice.getSelectedItem();
        if (selectedRollNo == null || selectedRollNo.trim().isEmpty()) {
            showStatus("Please select a roll number", true);
            return;
        }

        // Input validation
        if (nameField.getText().trim().isEmpty()) {
            showStatus("Please enter name", true);
            return;
        }
        if (fatherField.getText().trim().isEmpty()) {
            showStatus("Please enter father's name", true);
            return;
        }
        if (addressField.getText().trim().isEmpty()) {
            showStatus("Please enter address", true);
            return;
        }
        if (phoneField.getText().trim().isEmpty()) {
            showStatus("Please enter phone number", true);
            return;
        }
        if (emailField.getText().trim().isEmpty()) {
            showStatus("Please enter email", true);
            return;
        }

        try {
            Conn conn = Conn.getInstance();
            String query = "UPDATE student SET name = ?, father = ?, course = ?, branch = ?, " +
                          "address = ?, phone = ?, email = ? WHERE rollno = ?";
            
            PreparedStatement pstmt = conn.getConnection().prepareStatement(query);
            pstmt.setString(1, nameField.getText().trim());
            pstmt.setString(2, fatherField.getText().trim());
            pstmt.setString(3, courseChoice.getSelectedItem().toString());
            pstmt.setString(4, branchChoice.getSelectedItem().toString());
            pstmt.setString(5, addressField.getText().trim());
            pstmt.setString(6, phoneField.getText().trim());
            pstmt.setString(7, emailField.getText().trim());
            pstmt.setString(8, selectedRollNo);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showStatus("Student details updated successfully", false);
                clearFields();
            } else {
                showStatus("Failed to update student details", true);
            }
            pstmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating student details", e);
            showStatus("Error updating student details: " + e.getMessage(), true);
        }
    }

    private void clearFields() {
        nameField.setText("");
        fatherField.setText("");
        courseChoice.setSelectedIndex(0);
        branchChoice.setSelectedIndex(0);
        addressField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    private void handleCancel() {
        int response = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to close this window?",
            "Confirm Close",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to set system look and feel", e);
        }
        SwingUtilities.invokeLater(() -> new updateStudent());
    }
}