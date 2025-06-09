package university.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateTeacher extends JFrame implements ActionListener {

    JTextField textAddress, textPhone, textemail, textAadhar, textcourse, textbranch;
    JTextField empText, textName, textfather, dobdob, textM10, textM12;
    JButton submit, cancel;
    Choice cEMPID;

    public UpdateTeacher() {
        getContentPane().setBackground(new Color(230, 210, 252));
        setLayout(null);

        JLabel heading = new JLabel("Update Teacher Details");
        heading.setBounds(50, 10, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 35));
        add(heading);

        JLabel empID = new JLabel("Select Employee ID");
        empID.setBounds(50, 100, 200, 20);
        empID.setFont(new Font("serif", Font.PLAIN, 20));
        add(empID);

        cEMPID = new Choice();
        cEMPID.setBounds(250, 100, 200, 20);
        add(cEMPID);

        // Load Employee IDs into Choice dropdown
        try {
            Conn c = Conn.getInstance();
            ResultSet rs = c.getStatement().executeQuery("SELECT empId FROM teacher");
            while (rs.next()) {
                cEMPID.add(rs.getString("empId"));
            }
            if (cEMPID.getItemCount() > 0) {
                cEMPID.select(0);
                loadTeacherDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Name
        JLabel name = new JLabel("Name");
        name.setBounds(50, 150, 100, 30);
        name.setFont(new Font("serif", Font.BOLD, 20));
        add(name);

        textName = new JTextField();
        textName.setBounds(200, 150, 150, 30);
        textName.setEditable(false);
        add(textName);

        // Father Name
        JLabel fname = new JLabel("Father Name");
        fname.setBounds(400, 150, 200, 30);
        fname.setFont(new Font("serif", Font.BOLD, 20));
        add(fname);

        textfather = new JTextField();
        textfather.setBounds(600, 150, 150, 30);
        textfather.setEditable(false);
        add(textfather);

        // Employee ID
        JLabel EMPIDD = new JLabel("Employee ID");
        EMPIDD.setBounds(50, 200, 200, 30);
        EMPIDD.setFont(new Font("serif", Font.BOLD, 20));
        add(EMPIDD);

        empText = new JTextField();
        empText.setBounds(200, 200, 150, 30);
        empText.setEditable(false);
        add(empText);

        // DOB
        JLabel dob = new JLabel("Date of Birth");
        dob.setBounds(400, 200, 200, 30);
        dob.setFont(new Font("serif", Font.BOLD, 20));
        add(dob);

        dobdob = new JTextField();
        dobdob.setBounds(600, 200, 150, 30);
        dobdob.setEditable(false);
        add(dobdob);

        // Address
        JLabel address = new JLabel("Address");
        address.setBounds(50, 250, 200, 30);
        address.setFont(new Font("serif", Font.BOLD, 20));
        add(address);

        textAddress = new JTextField();
        textAddress.setBounds(200, 250, 150, 30);
        add(textAddress);

        // Phone
        JLabel phone = new JLabel("Phone");
        phone.setBounds(400, 250, 200, 30);
        phone.setFont(new Font("serif", Font.BOLD, 20));
        add(phone);

        textPhone = new JTextField();
        textPhone.setBounds(600, 250, 150, 30);
        add(textPhone);

        // Email
        JLabel email = new JLabel("Email");
        email.setBounds(50, 300, 200, 30);
        email.setFont(new Font("serif", Font.BOLD, 20));
        add(email);

        textemail = new JTextField();
        textemail.setBounds(200, 300, 150, 30);
        add(textemail);

        // Class X (disabled)
        JLabel M10 = new JLabel("Class X (%)");
        M10.setBounds(400, 300, 200, 30);
        M10.setFont(new Font("serif", Font.BOLD, 20));
        add(M10);

        textM10 = new JTextField();
        textM10.setBounds(600, 300, 150, 30);
        textM10.setEditable(false);
        add(textM10);

        // Class XII (disabled)
        JLabel M12 = new JLabel("Class XII (%)");
        M12.setBounds(50, 350, 200, 30);
        M12.setFont(new Font("serif", Font.BOLD, 20));
        add(M12);

        textM12 = new JTextField();
        textM12.setBounds(200, 350, 150, 30);
        textM12.setEditable(false);
        add(textM12);

        // Aadhar Number
        JLabel AadharNo = new JLabel("Aadhar Number");
        AadharNo.setBounds(400, 350, 200, 30);
        AadharNo.setFont(new Font("serif", Font.BOLD, 20));
        add(AadharNo);

        textAadhar = new JTextField();
        textAadhar.setBounds(600, 350, 150, 30);
        add(textAadhar);

        // Qualification (education)
        JLabel Qualification = new JLabel("Qualification");
        Qualification.setBounds(50, 400, 200, 30);
        Qualification.setFont(new Font("serif", Font.BOLD, 20));
        add(Qualification);

        textcourse = new JTextField();
        textcourse.setBounds(200, 400, 150, 30);
        add(textcourse);

        // Department
        JLabel Department = new JLabel("Department");
        Department.setBounds(400, 400, 200, 30);
        Department.setFont(new Font("serif", Font.BOLD, 20));
        add(Department);

        textbranch = new JTextField();
        textbranch.setBounds(600, 400, 150, 30);
        add(textbranch);

        // Add listener to update details when Employee ID changes
        cEMPID.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadTeacherDetails();
                }
            }
        });

        // Buttons
        submit = new JButton("Update");
        submit.setBounds(250, 550, 120, 30);
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.red);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(450, 550, 120, 30);
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.red);
        cancel.addActionListener(this);
        add(cancel);

        setSize(900, 700);
        setLocation(350, 50);
        setVisible(true);
    }

    private void loadTeacherDetails() {
        String empId = cEMPID.getSelectedItem();
        if (empId == null || empId.isEmpty()) return;

        try {
            Conn c = Conn.getInstance();
            String query = "SELECT * FROM teacher WHERE empId = ?";
            PreparedStatement pst = c.getConnection().prepareStatement(query);
            pst.setString(1, empId);
            ResultSet resultSet = pst.executeQuery();

            if (resultSet.next()) {
                textName.setText(resultSet.getString("name"));
                textfather.setText(resultSet.getString("fname"));
                dobdob.setText(resultSet.getString("dob"));
                textAddress.setText(resultSet.getString("address"));
                textPhone.setText(resultSet.getString("phone"));
                textemail.setText(resultSet.getString("email"));
                textM10.setText(resultSet.getString("class_x"));
                textM12.setText(resultSet.getString("class_xii"));
                textAadhar.setText(resultSet.getString("aadhar"));
                empText.setText(resultSet.getString("empId"));
                textcourse.setText(resultSet.getString("education"));
                textbranch.setText(resultSet.getString("department"));
            }

            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            String empid = empText.getText();
            String address = textAddress.getText();
            String phone = textPhone.getText();
            String email = textemail.getText();
            String aadhar = textAadhar.getText();
            String course = textcourse.getText();
            String branch = textbranch.getText();

            try {
                Conn c = Conn.getInstance();
                String updateQuery = "UPDATE teacher SET address=?, phone=?, email=?, aadhar=?, education=?, department=? WHERE empId=?";
                PreparedStatement pst = c.getConnection().prepareStatement(updateQuery);

                pst.setString(1, address);
                pst.setString(2, phone);
                pst.setString(3, email);
                pst.setString(4, aadhar);
                pst.setString(5, course);
                pst.setString(6, branch);
                pst.setString(7, empid);

                int result = pst.executeUpdate();

                if (result > 0) {
                    JOptionPane.showMessageDialog(null, "Details Updated Successfully");
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Update Failed. Please try again.");
                }

                pst.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error Occurred: " + ex.getMessage());
            }

        } else if (e.getSource() == cancel) {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        new UpdateTeacher();
    }
}