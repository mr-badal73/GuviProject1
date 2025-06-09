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

public class FreeStructure extends JFrame implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(FreeStructure.class.getName());
    private static final Color BACKGROUND_COLOR = new Color(210, 252, 218);
    private static final int WINDOW_WIDTH = 1100;
    private static final int WINDOW_HEIGHT = 700;
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton print, cancel;
    private JLabel statusLabel;

    public FreeStructure() {
        initializeUI();
        loadFeeData();
    }

    private void initializeUI() {
        setTitle("Fee Structure");
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(null);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocation(300, 100);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Heading
        JLabel heading = new JLabel("Fee Structure");
        heading.setBounds(50, 20, 400, 50);
        heading.setFont(new Font("Tahoma", Font.BOLD, 24));
        add(heading);

        // Table
        setupTable();
        
        // Buttons
        setupButtons();
        
        // Status label
        setupStatusLabel();

        setVisible(true);
    }

    private void setupTable() {
        // Create table model with column names
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Course");
        columnNames.add("Semester");
        columnNames.add("Amount");

        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        JScrollPane js = new JScrollPane(table);
        js.setBounds(0, 100, WINDOW_WIDTH - 20, WINDOW_HEIGHT - 200);
        add(js);
    }

    private void setupButtons() {
        print = createButton("Print", 50, WINDOW_HEIGHT - 100);
        cancel = createButton("Cancel", 150, WINDOW_HEIGHT - 100);
    }

    private JButton createButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 100, 30);
        button.addActionListener(this);
        add(button);
        return button;
    }

    private void setupStatusLabel() {
        statusLabel = new JLabel("");
        statusLabel.setBounds(50, WINDOW_HEIGHT - 50, WINDOW_WIDTH - 100, 20);
        statusLabel.setForeground(Color.RED);
        add(statusLabel);
    }

    private void loadFeeData() {
        try {
            Conn conn = Conn.getInstance();
            ResultSet resultSet = conn.getStatement().executeQuery("SELECT * FROM fee ORDER BY course, semester");
            
            // Clear existing data
            tableModel.setRowCount(0);
            
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(resultSet.getString("course"));
                row.add(resultSet.getString("semester"));
                row.add(resultSet.getString("amount"));
                tableModel.addRow(row);
            }
            showStatus("Fee data loaded successfully", false);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading fee data", e);
            showStatus("Error loading fee data: " + e.getMessage(), true);
        }
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setForeground(isError ? Color.RED : Color.GREEN);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == print) {
                handlePrint();
            } else if (e.getSource() == cancel) {
                handleCancel();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error handling action", ex);
            showStatus("An error occurred: " + ex.getMessage(), true);
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
        SwingUtilities.invokeLater(() -> new FreeStructure());
    }
}
