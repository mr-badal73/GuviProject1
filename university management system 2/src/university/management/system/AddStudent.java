package university.management.system;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddStudent extends JFrame implements ActionListener {

    JTextField textName, textFather, textAddress, textPhone, textEmail,
            textM10, textM12, textAadhar, textRoll;
    JDateChooser cdob;
    JComboBox<String> courseBox, branchBox;
    JButton submit, cancel;

    public AddStudent() {
        getContentPane().setBackground(new Color(210, 252, 218));
        setLayout(null);
        setSize(900, 700);
        setLocation(350, 50);

        JLabel heading = new JLabel("New Student Details");
        heading.setBounds(310, 30, 500, 50);
        heading.setFont(new Font("Serif", Font.BOLD, 30));
        add(heading);

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setBounds(50, 150, 100, 30);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(nameLabel);

        textName = new JTextField();
        textName.setBounds(200, 150, 150, 30);
        add(textName);

        JLabel fatherLabel = new JLabel("Father Name");
        fatherLabel.setBounds(400, 150, 200, 30);
        fatherLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(fatherLabel);

        textFather = new JTextField();
        textFather.setBounds(600, 150, 150, 30);
        add(textFather);

        JLabel rollLabel = new JLabel("Roll Number");
        rollLabel.setBounds(50, 200, 200, 30);
        rollLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(rollLabel);

        textRoll = new JTextField();
        textRoll.setBounds(200, 200, 150, 30);
        add(textRoll);

        JLabel dobLabel = new JLabel("Date of Birth");
        dobLabel.setBounds(400, 200, 200, 30);
        dobLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(dobLabel);

        cdob = new JDateChooser();
        cdob.setBounds(600, 200, 150, 30);
        add(cdob);

        JLabel addressLabel = new JLabel("Address");
        addressLabel.setBounds(50, 250, 200, 30);
        addressLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(addressLabel);

        textAddress = new JTextField();
        textAddress.setBounds(200, 250, 150, 30);
        add(textAddress);

        JLabel phoneLabel = new JLabel("Phone");
        phoneLabel.setBounds(400, 250, 200, 30);
        phoneLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(phoneLabel);

        textPhone = new JTextField();
        textPhone.setBounds(600, 250, 150, 30);
        add(textPhone);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(50, 300, 200, 30);
        emailLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(emailLabel);

        textEmail = new JTextField();
        textEmail.setBounds(200, 300, 150, 30);
        add(textEmail);

        JLabel m10Label = new JLabel("Class X (%)");
        m10Label.setBounds(400, 300, 200, 30);
        m10Label.setFont(new Font("Serif", Font.BOLD, 20));
        add(m10Label);

        textM10 = new JTextField();
        textM10.setBounds(600, 300, 150, 30);
        add(textM10);

        JLabel m12Label = new JLabel("Class XII (%)");
        m12Label.setBounds(50, 350, 200, 30);
        m12Label.setFont(new Font("Serif", Font.BOLD, 20));
        add(m12Label);

        textM12 = new JTextField();
        textM12.setBounds(200, 350, 150, 30);
        add(textM12);

        JLabel aadharLabel = new JLabel("Aadhar Number");
        aadharLabel.setBounds(400, 350, 200, 30);
        aadharLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(aadharLabel);

        textAadhar = new JTextField();
        textAadhar.setBounds(600, 350, 150, 30);
        add(textAadhar);

        JLabel courseLabel = new JLabel("Course");
        courseLabel.setBounds(50, 400, 200, 30);
        courseLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(courseLabel);

        String[] courses = {"B.Tech", "BBA", "BCA", "BSC", "BA", "BCOM"};
        courseBox = new JComboBox<>(courses);
        courseBox.setBounds(200, 400, 150, 30);
        add(courseBox);

        JLabel branchLabel = new JLabel("Branch");
        branchLabel.setBounds(400, 400, 200, 30);
        branchLabel.setFont(new Font("Serif", Font.BOLD, 20));
        add(branchLabel);

        String[] branches = {"CSE", "IT", "ECE", "ME", "CE", "EEE"};
        branchBox = new JComboBox<>(branches);
        branchBox.setBounds(600, 400, 150, 30);
        add(branchBox);

        submit = new JButton("Submit");
        submit.setBounds(250, 550, 120, 30);
        submit.setBackground(Color.RED);
        submit.setForeground(Color.red);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBounds(450, 550, 120, 30);
        cancel.setBackground(Color.RED);
        cancel.setForeground(Color.red);
        cancel.addActionListener(this);
        add(cancel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            String name     = textName.getText();
            String father   = textFather.getText();
            String roll     = textRoll.getText();
            String dob      = ((JTextField) cdob.getDateEditor().getUiComponent()).getText();
            String address  = textAddress.getText();
            String phone    = textPhone.getText();
            String email    = textEmail.getText();
            String m10      = textM10.getText();
            String m12      = textM12.getText();
            String aadhar   = textAadhar.getText();
            String course   = (String) courseBox.getSelectedItem();
            String branch   = (String) branchBox.getSelectedItem();

            try {
                Conn c = Conn.getInstance();
                String q = "INSERT INTO student (name, fname, rollno, dob, address, phone, email, class_x, class_xii, aadhar, course, branch) VALUES ('" + name + "', '" + father + "', '" + roll + "', '" + dob + "', '" + address + "', '" + phone + "', '" + email + "', '" + m10 + "', '" + m12 + "', '" + aadhar + "', '" + course + "', '" + branch + "')";
                c.getStatement().executeUpdate(q);
                JOptionPane.showMessageDialog(null, "Student Added Successfully");
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
        new AddStudent();
    }
}