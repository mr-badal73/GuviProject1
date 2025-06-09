package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class StudentFeeForm extends JFrame implements ActionListener {
    Choice CrollNumber;
    JComboBox<String> courseBox, departmentBox, semesterBox;
    JTextField textName, textfName, totalAmount;
    JButton pay, update, back;

    StudentFeeForm() {
        getContentPane().setBackground(new Color(210, 252, 251));
        setLayout(null);

        JLabel roolNumber = new JLabel("Select Roll Number");
        roolNumber.setBounds(40, 60, 150, 20);
        roolNumber.setFont(new Font("Tahoma", Font.BOLD, 12));
        add(roolNumber);

        CrollNumber = new Choice();
        CrollNumber.setBounds(200, 60, 150, 20);
        add(CrollNumber);

        try {
            Conn c = Conn.getInstance();
            ResultSet rs = c.getStatement().executeQuery("select * from student");
            while (rs.next()) {
                CrollNumber.add(rs.getString("rollno"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel name = new JLabel("Name");
        name.setBounds(40, 100, 150, 20);
        add(name);

        textName = new JTextField();
        textName.setBounds(200, 100, 150, 20);
        add(textName);

        JLabel fname = new JLabel("Father Name");
        fname.setBounds(40, 140, 150, 20);
        add(fname);

        textfName = new JTextField();
        textfName.setBounds(200, 140, 150, 20);
        add(textfName);

        // Fetch initial name and fname
        try {
            Conn c = Conn.getInstance();
            String query = "select * from student where rollno='" + CrollNumber.getSelectedItem() + "'";
            ResultSet rs = c.getStatement().executeQuery(query);
            while (rs.next()) {
                textName.setText(rs.getString("name"));
                textfName.setText(rs.getString("fname"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CrollNumber.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ie) {
                try {
                    Conn c = Conn.getInstance();
                    String query = "select * from student where rollno='" + CrollNumber.getSelectedItem() + "'";
                    ResultSet rs = c.getStatement().executeQuery(query);
                    while (rs.next()) {
                        textName.setText(rs.getString("name"));
                        textfName.setText(rs.getString("fname"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        JLabel Qualification = new JLabel("Course");
        Qualification.setBounds(40, 180, 150, 20);
        add(Qualification);

        String course[] = {"BTech", "BBA", "BCA", "BSC", "MSC", "MBA", "MCA", "MCom", "MA", "BA"};
        courseBox = new JComboBox<>(course);
        courseBox.setBounds(200, 180, 150, 20);
        courseBox.setBackground(Color.WHITE);
        add(courseBox);

        JLabel Department = new JLabel("Branch");
        Department.setBounds(40, 220, 150, 20);
        add(Department);

        String department[] = {"Computer Science", "Electronics", "Mechanical", "Civil", "IT"};
        departmentBox = new JComboBox<>(department);
        departmentBox.setBounds(200, 220, 150, 20);
        departmentBox.setBackground(Color.WHITE);
        add(departmentBox);

        JLabel textsemester = new JLabel("Semester");
        textsemester.setBounds(40, 260, 150, 20);
        add(textsemester);

        String semester[] = {"semester1", "semester2", "semester3", "semester4", "semester5", "semester6", "semester7", "semester8"};
        semesterBox = new JComboBox<>(semester);
        semesterBox.setBounds(200, 260, 150, 20);
        semesterBox.setBackground(Color.WHITE);
        add(semesterBox);

        JLabel total = new JLabel("Total Payable");
        total.setBounds(40, 300, 150, 20);
        add(total);

        totalAmount = new JTextField();
        totalAmount.setBounds(200, 300, 150, 20);
        totalAmount.setEditable(false);
        add(totalAmount);

        update = new JButton("Update");
        update.setBounds(30, 380, 100, 25);
        update.addActionListener(this);
        add(update);

        pay = new JButton("Pay");
        pay.setBounds(150, 380, 100, 25);
        pay.addActionListener(this);
        add(pay);

        back = new JButton("Back");
        back.setBounds(270, 380, 100, 25);
        back.addActionListener(this);
        add(back);

        // Move image to the right side
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icon/fee.png"));
        Image i2 = i1.getImage().getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel img = new JLabel(i3);
        img.setBounds(400, 80, 400, 300);
        add(img);

        setSize(900, 500);
        setLocation(300, 100);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == update) {
            String course = (String) courseBox.getSelectedItem();
            String semester = (String) semesterBox.getSelectedItem();
            try {
                Conn c = Conn.getInstance();
                ResultSet rs = c.getStatement().executeQuery("select * from fee where course = '" + course + "'");
                while (rs.next()) {
                    totalAmount.setText(rs.getString(semester));
                }
            } catch (Exception E) {
                E.printStackTrace();
            }
        } else if (e.getSource() == pay) {
            String rollno = CrollNumber.getSelectedItem();
            String course = (String) courseBox.getSelectedItem();
            String semester = (String) semesterBox.getSelectedItem();
            String branch = (String) departmentBox.getSelectedItem();
            String total = totalAmount.getText();

            try {
                Conn c = Conn.getInstance();
                String Q = "insert into feecollege values('" + rollno + "','" + course + "','" + branch + "','" + semester + "','" + total + "')";
                c.getStatement().executeUpdate(Q);
                JOptionPane.showMessageDialog(null, "Fee Submitted successfully");
            } catch (Exception E) {
                E.printStackTrace();
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new StudentFeeForm();
    }
}