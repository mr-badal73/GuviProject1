package university.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ExaminationDetails extends JFrame implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(ExaminationDetails.class.getName());
    private static final Color BACKGROUND_COLOR = new Color(210, 252, 218);
    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 700;
    
    private Choice choice;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton search, print, add, cancel;
    private JLabel statusLabel;

    public ExaminationDetails() {
        initializeUI();
        loadStudentData();
    }

    private void initializeUI() {
        setTitle("Examination Details");
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(300, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Search components
        setupSearchComponents();
        
        // Table
        setupTable();
        
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

        choice = new Choice();
        choice.setBounds(180, 20, 150, 20);
        add(choice);
    }

    private void setupTable() {
        // Create table model with column names
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Roll No");
        columnNames.add("Name");
        columnNames.add("Course");
        columnNames.add("Branch");
        columnNames.add("Subject");
        columnNames.add("Marks");
        columnNames.add("Grade");

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane js = new JScrollPane(table);
        js.setBounds(0, 100, WINDOW_WIDTH - 20, WINDOW_HEIGHT - 200);
        add(js);
    }

    private void setupButtons() {
        search = createButton("Search", 20, 70);
        print = createButton("Print", 120, 70);
        add = createButton("Add", 220, 70);
        cancel = createButton("Cancel", 320, 70);
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
            ResultSet resultSet = conn.getStatement().executeQuery("SELECT * FROM student ORDER BY rollno");
            
            // Clear existing data
            tableModel.setRowCount(0);
            choice.removeAll();
            
            while (resultSet.next()) {
                choice.add(resultSet.getString("rollno"));
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
            } else if (e.getSource() == print) {
                handlePrint();
            } else if (e.getSource() == add) {
                handleAdd();
            } else if (e.getSource() == cancel) {
                handleCancel();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error handling action", ex);
            showStatus("An error occurred: " + ex.getMessage(), true);
        }
    }

    private void handleSearch() {
        String selectedRollNo = choice.getSelectedItem();
        if (selectedRollNo == null || selectedRollNo.trim().isEmpty()) {
            showStatus("Please select a roll number", true);
            return;
        }

        try {
            Conn conn = Conn.getInstance();
            String query = "SELECT s.rollno, s.name, s.course, s.branch, m.subject, m.marks, " +
                          "CASE " +
                          "  WHEN m.marks >= 90 THEN 'A+' " +
                          "  WHEN m.marks >= 80 THEN 'A' " +
                          "  WHEN m.marks >= 70 THEN 'B' " +
                          "  WHEN m.marks >= 60 THEN 'C' " +
                          "  WHEN m.marks >= 50 THEN 'D' " +
                          "  ELSE 'F' " +
                          "END as grade " +
                          "FROM student s " +
                          "LEFT JOIN marks m ON s.rollno = m.rollno " +
                          "WHERE s.rollno = ?";
            
            java.sql.PreparedStatement pstmt = conn.getConnection().prepareStatement(query);
            pstmt.setString(1, selectedRollNo);
            ResultSet resultSet = pstmt.executeQuery();
            
            // Clear existing data
            tableModel.setRowCount(0);
            
            boolean found = false;
            while (resultSet.next()) {
                found = true;
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getString("rollno"));
                row.add(resultSet.getString("name"));
                row.add(resultSet.getString("course"));
                row.add(resultSet.getString("branch"));
                row.add(resultSet.getString("subject"));
                row.add(resultSet.getString("marks"));
                row.add(resultSet.getString("grade"));
                tableModel.addRow(row);
            }
            
            if (found) {
                showStatus("Student marks found", false);
            } else {
                showStatus("No marks found for roll number: " + selectedRollNo, true);
            }
            pstmt.close();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching student marks", e);
            showStatus("Error searching student marks: " + e.getMessage(), true);
        }
    }

    private void handlePrint() {
        try {
            boolean complete = table.print();
            if (complete) {
                showStatus("Printing completed successfully", false);
            } else {
                showStatus("Printing was cancelled", true);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error printing table", e);
            showStatus("Error printing: " + e.getMessage(), true);
        }
    }

    private void handleAdd() {
        setVisible(false);
        new EnterMarks();
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
        SwingUtilities.invokeLater(() -> new ExaminationDetails());
    }
}
