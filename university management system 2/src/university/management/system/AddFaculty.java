package university.management.system;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddFaculty extends JFrame implements ActionListener {

    JTextField textName, textFather, textAddress, textPhone, textEmail,
            textM10, textM12, textAadhar, textEmpId;
    JDateChooser cdob;
    JComboBox<String> courseBox, departmentBox;
    JButton submit, cancel;

    public AddFaculty() {
        // Frame settings
        getContentPane().setBackground(new Color(166, 164, 252));
        setLayout(null);
        setSize(900, 700);
        setLocation(350, 50);

        // Heading
        JLabel heading = new JLabel("New Teacher Details");
        heading.setBounds(310, 30, 500, 50);
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        add(heading);

        // Name
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(50, 150, 100, 30);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(nameLabel);

        textName = new JTextField();
        textName.setBounds(200, 150, 150, 30);
        add(textName);

        // Father Name
        JLabel fatherLabel = new JLabel("Father Name");
        fatherLabel.setBounds(400, 150, 200, 30);
        fatherLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(fatherLabel);

        textFather = new JTextField();
        textFather.setBounds(600, 150, 150, 30);
        add(textFather);

        // Employee ID (user‐entered)
        JLabel empIdLabel = new JLabel("Employee ID");
        empIdLabel.setBounds(50, 200, 200, 30);
        empIdLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(empIdLabel);

        textEmpId = new JTextField();
        textEmpId.setBounds(200, 200, 150, 30);
        add(textEmpId);

        // Date of Birth
        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setBounds(400, 200, 200, 30);
        dobLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(dobLabel);

        cdob = new JDateChooser();
        cdob.setBounds(600, 200, 150, 30);
        add(cdob);

        // Address
        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(50, 250, 200, 30);
        addressLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(addressLabel);

        textAddress = new JTextField();
        textAddress.setBounds(200, 250, 150, 30);
        add(textAddress);

        // Phone
        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(400, 250, 200, 30);
        phoneLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(phoneLabel);

        textPhone = new JTextField();
        textPhone.setBounds(600, 250, 150, 30);
        add(textPhone);

        // Email
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(50, 300, 200, 30);
        emailLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(emailLabel);

        textEmail = new JTextField();
        textEmail.setBounds(200, 300, 150, 30);
        add(textEmail);

        // Class X %
        JLabel m10Label = new JLabel("Class X (%)");
        m10Label.setBounds(400, 300, 200, 30);
        m10Label.setFont(new Font("Serif", Font.BOLD, 20));
        add(m10Label);

        textM10 = new JTextField();
        textM10.setBounds(600, 300, 150, 30);
        add(textM10);

        // Class XII %
        JLabel m12Label = new JLabel("Class XII (%)");
        m12Label.setBounds(50, 350, 200, 30);
        m12Label.setFont(new Font("Serif", Font.BOLD, 20));
        add(m12Label);

        textM12 = new JTextField();
        textM12.setBounds(200, 350, 150, 30);
        add(textM12);

        // Aadhar Number
        JLabel aadharLabel = new JLabel("Aadhar Number");
        aadharLabel.setBounds(400, 350, 200, 30);
        aadharLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(aadharLabel);

        textAadhar = new JTextField();
        textAadhar.setBounds(600, 350, 150, 30);
        add(textAadhar);

        // Qualification
        JLabel qualLabel = new JLabel("Qualification");
        qualLabel.setBounds(50, 400, 200, 30);
        qualLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(qualLabel);

        String[] courses = {"B.Tech", "BBA", "BCA", "BSC", "MSC", "MBA", "MCA", "MCom", "MA", "BA"};
        courseBox = new JComboBox<>(courses);
        courseBox.setBounds(200, 400, 150, 30);
        add(courseBox);

        // Department
        JLabel deptLabel = new JLabel("Department");
        deptLabel.setBounds(400, 400, 200, 30);
        deptLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(deptLabel);

        String[] depts = {"Computer Science", "Electronics", "Mechanical", "Civil", "IT"};
        departmentBox = new JComboBox<>(depts);
        departmentBox.setBounds(600, 400, 150, 30);
        add(departmentBox);

        // Submit button
        submit = new JButton("Submit");
        submit.setBounds(250, 550, 120, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.RED);
        submit.addActionListener(this);
        add(submit);

        // Cancel button
        cancel = new JButton("Cancel");
        cancel.setBounds(450, 550, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.RED);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            // Gather input values
            String name       = textName.getText();
            String fatherName = textFather.getText();
            String empId      = textEmpId.getText();
            String dob        = ((JTextField) cdob.getDateEditor().getUiComponent()).getText();
            String address    = textAddress.getText();
            String phone      = textPhone.getText();
            String email      = textEmail.getText();
            String m10        = textM10.getText();
            String m12        = textM12.getText();
            String aadhar     = textAadhar.getText();
            String course     = (String) courseBox.getSelectedItem();
            String dept       = (String) departmentBox.getSelectedItem();

            // Build and execute INSERT query
            try {
                Conn c = Conn.getInstance();
                String q = "INSERT INTO teacher " +
                        "(name, fname, empid, dob, address, phone, email, class_x, class_xii, aadhar, course, department) " +
                        "VALUES ('" + name + "', '" + fatherName + "', '" + empId + "', '" + dob + "', '" +
                        address + "', '" + phone + "', '" + email + "', '" + m10 + "', '" +
                        m12 + "', '" + aadhar + "', '" + course + "', '" + dept + "')";
                c.getStatement().executeUpdate(q);
                JOptionPane.showMessageDialog(null, "Details Inserted Successfully");
                setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
                ex.printStackTrace();
            }

        } else if (e.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new AddFaculty();
    }
}