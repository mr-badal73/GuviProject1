package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

public class main_class extends JFrame implements ActionListener {
    private static final Logger LOGGER = Logger.getLogger(main_class.class.getName());
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    
    public main_class() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("University Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupBackground();
        setupMenuBar();
        setSize(1540, 850);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupBackground() {
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/third.jpg"));
            Image i2 = i1.getImage().getScaledInstance(1540, 750, Image.SCALE_DEFAULT);
            ImageIcon i3 = new ImageIcon(i2);
            JLabel img = new JLabel(i3);
            add(img);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to load background image", e);
            setBackground(Color.WHITE);
        }
    }

    private void setupMenuBar() {
        JMenuBar mb = new JMenuBar();
        
        // New Information Menu
        JMenu newInfo = createMenu("New Information", Color.BLACK);
        addMenuItem(newInfo, "New Faculty Information", this);
        addMenuItem(newInfo, "New Student Information", this);
        mb.add(newInfo);

        // View Details Menu
        JMenu details = createMenu("View Details", Color.BLACK);
        addMenuItem(details, "View Faculty Details", this);
        addMenuItem(details, "View Student Details", this);
        mb.add(details);

        // Leave Menu
        JMenu leave = createMenu("Apply Leave", Color.BLACK);
        addMenuItem(leave, "Faculty Leave", this);
        addMenuItem(leave, "Student Leave", this);
        mb.add(leave);

        // Leave Details Menu
        JMenu leaveDetails = createMenu("Leave Details", Color.BLACK);
        addMenuItem(leaveDetails, "Faculty Leave Details", this);
        addMenuItem(leaveDetails, "Student Leave Details", this);
        mb.add(leaveDetails);

        // Examination Menu
        JMenu exam = createMenu("Examination", Color.BLACK);
        addMenuItem(exam, "Examination Results", this);
        addMenuItem(exam, "Enter Marks", this);
        mb.add(exam);

        // Update Details Menu
        JMenu updateInfo = createMenu("Update Details", Color.BLACK);
        addMenuItem(updateInfo, "Update Faculty Details", this);
        addMenuItem(updateInfo, "Update Student Details", this);
        mb.add(updateInfo);

        // Fee Details Menu
        JMenu fee = createMenu("Fee Details", Color.BLACK);
        addMenuItem(fee, "Fee Structure", this);
        addMenuItem(fee, "Student Fee Form", this);
        mb.add(fee);

        // Utility Menu
        JMenu utility = createMenu("Utility", Color.BLACK);
        addMenuItem(utility, "Calculator", this);
        addMenuItem(utility, "Notepad", this);
        mb.add(utility);

        // About Menu
        JMenu about = createMenu("About", Color.BLACK);
        addMenuItem(about, "About", this);
        mb.add(about);

        // Exit Menu
        JMenu exit = createMenu("Exit", Color.BLACK);
        addMenuItem(exit, "Exit", this);
        mb.add(exit);

        setJMenuBar(mb);
    }

    private JMenu createMenu(String name, Color foreground) {
        JMenu menu = new JMenu(name);
        menu.setForeground(foreground);
        return menu;
    }

    private void addMenuItem(JMenu menu, String name, ActionListener listener) {
        JMenuItem item = new JMenuItem(name);
        item.setBackground(Color.WHITE);
        item.addActionListener(listener);
        menu.add(item);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        try {
            handleMenuAction(command);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error handling menu action: " + command, ex);
            JOptionPane.showMessageDialog(this, 
                "An error occurred while processing your request. Please try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleMenuAction(String command) {
        switch (command) {
            case "Exit":
                handleExit();
                break;
            case "Calculator":
                launchCalculator();
                break;
            case "Notepad":
                launchNotepad();
                break;
            case "New Faculty Information":
                new AddFaculty();
                break;
            case "New Student Information":
                new AddStudent();
                break;
            case "View Faculty Details":
                new TeacherDetails();
                break;
            case "View Student Details":
                new StudentDetails();
                break;
            case "Faculty Leave":
                new TeacherLeave();
                break;
            case "Student Leave":
                new StudentLeave();
                break;
            case "Faculty Leave Details":
                new TeacherLeaveDetails();
                break;
            case "Student Leave Details":
                new StudentLeaveDetails();
                break;
            case "Examination Results":
                new ExaminationDetails();
                break;
            case "Enter Marks":
                new EnterMarks();
                break;
            case "Update Faculty Details":
                new UpdateTeacher();
                break;
            case "Update Student Details":
                new updateStudent();
                break;
            case "Fee Structure":
                new FreeStructure();
                break;
            case "Student Fee Form":
                new StudentFeeForm();
                break;
            case "About":
                new About();
                break;
            default:
                LOGGER.warning("Unknown menu command: " + command);
        }
    }

    private void handleExit() {
        int response = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void launchCalculator() {
        try {
            String command = OS_NAME.contains("windows") ? "calc.exe" : "gnome-calculator";
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to launch calculator", e);
            JOptionPane.showMessageDialog(this, 
                "Failed to launch calculator. Please try using your system's calculator.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void launchNotepad() {
        try {
            String command = OS_NAME.contains("windows") ? "notepad.exe" : "gedit";
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to launch notepad", e);
            JOptionPane.showMessageDialog(this, 
                "Failed to launch notepad. Please try using your system's text editor.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to set system look and feel", e);
        }
        SwingUtilities.invokeLater(() -> new main_class());
    }
}
