package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.logging.Level;

public class EnterMarks extends JFrame implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(EnterMarks.class.getName());
    private static final Color BACKGROUND_COLOR = new Color(210, 252, 218);
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 600;
    
    private Choice rollNoChoice;
    private JTextField subjectField, marksField;
    private JButton submit, cancel;
    private JLabel statusLabel;

    public EnterMarks() {
        initializeUI();
        loadStudentData();
    }

    private void initializeUI() {
        setTitle("Enter Marks");
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(400, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Roll Number Selection
        JLabel rollNoLabel = new JLabel("Select Roll Number");
        rollNoLabel.setBounds(50, 50, 150, 20);
        add(rollNoLabel);

        rollNoChoice = new Choice();
        rollNoChoice.setBounds(200, 50, 150, 20);
        add(rollNoChoice);

        // Subject Input
        JLabel subjectLabel = new JLabel("Subject");
        subjectLabel.setBounds(50, 100, 150, 20);
        add(subjectLabel);

        subjectField = new JTextField();
        subjectField.setBounds(200, 100, 150, 20);
        add(subjectField);

        // Marks Input
        JLabel marksLabel = new JLabel("Marks");
        marksLabel.setBounds(50, 150, 150, 20);
        add(marksLabel);

        marksField = new JTextField();
        marksField.setBounds(200, 150, 150, 20);
        add(marksField);

        // Buttons
        submit = createButton("Submit", 100, 200);
        cancel = createButton("Cancel", 250, 200);

        // Status Label
        statusLabel = new JLabel("");
        statusLabel.setBounds(50, WINDOW_HEIGHT - 100, WINDOW_WIDTH - 100, 20);
        statusLabel.setForeground(Color.RED);
        add(statusLabel);

        setVisible(true);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 100, 30);
        button.addActionListener(this);
        add(button);
        return button;
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
            if (e.getSource() == submit) {
                handleSubmit();
            } else if (e.getSource() == cancel) {
                handleCancel();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error handling action", ex);
            showStatus("An error occurred: " + ex.getMessage(), true);
        }
    }

    private void handleSubmit() {
        String rollNo = rollNoChoice.getSelectedItem();
        String subject = subjectField.getText().trim();
        String marksStr = marksField.getText().trim();

        // Input validation
        if (rollNo == null || rollNo.isEmpty()) {
            showStatus("Please select a roll number", true);
            return;
        }
        if (subject.isEmpty()) {
            showStatus("Please enter a subject", true);
            return;
        }
        if (marksStr.isEmpty()) {
            showStatus("Please enter marks", true);
            return;
        }

        try {
            int marks = Integer.parseInt(marksStr);
            if (marks < 0 || marks > 100) {
                showStatus("Marks must be between 0 and 100", true);
                return;
            }

            Conn conn = Conn.getInstance();
            String query = "INSERT INTO marks (rollno, subject, marks) VALUES (?, ?, ?) " +
                          "ON DUPLICATE KEY UPDATE marks = ?";
            
            PreparedStatement pstmt = conn.getConnection().prepareStatement(query);
            pstmt.setString(1, rollNo);
            pstmt.setString(2, subject);
            pstmt.setInt(3, marks);
            pstmt.setInt(4, marks);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                showStatus("Marks saved successfully", false);
                clearFields();
            } else {
                showStatus("Failed to save marks", true);
            }
            pstmt.close();
        } catch (NumberFormatException e) {
            showStatus("Please enter valid marks", true);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving marks", e);
            showStatus("Error saving marks: " + e.getMessage(), true);
        }
    }

    private void clearFields() {
        subjectField.setText("");
        marksField.setText("");
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
        SwingUtilities.invokeLater(() -> new EnterMarks());
    }
}
