import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

public class RegistrarUI {

    // Main memory list loaded from Excel
    private List<Student> allStudents;
    private JComboBox<String> studentDropdown; 

    public RegistrarUI() {
        // 1. Load existing data right when the app opens
        allStudents = ExcelDataManager.loadStudentsFromExcel();

        // 2. Setup the Main Frame
        JFrame frame = new JFrame("MyCourseVille - Registrar Portal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new GridLayout(1, 2, 10, 10));

        // --- LEFT SIDE: REGISTRATION PANEL ---
        JPanel registrationPanel = new JPanel();
        registrationPanel.setLayout(new BoxLayout(registrationPanel, BoxLayout.Y_AXIS));
        registrationPanel.setBorder(BorderFactory.createTitledBorder("Student Registration (Creation)"));

        JTextField nameField = new JTextField(15);
        JTextField idField = new JTextField(15);
        String[] majors = {"Automotive Design and Manufacturing Engineering", "Aerospace Engineering", "Information and Communication Engineering", "Nano Engineering", "Robotics and Artificial Intelligence Engineering", "Semiconductor Engineering"};
        JComboBox<String> majorCombo = new JComboBox<>(majors);
        JCheckBox scholarshipBox = new JCheckBox("Has Scholarship?");
        JButton createUserBtn = new JButton("Create User");

        registrationPanel.add(new JLabel("Full Name:"));
        registrationPanel.add(nameField);
        registrationPanel.add(Box.createVerticalStrut(10));
        registrationPanel.add(new JLabel("Student ID:"));
        registrationPanel.add(idField);
        registrationPanel.add(Box.createVerticalStrut(10));
        registrationPanel.add(new JLabel("Major:"));
        registrationPanel.add(majorCombo);
        registrationPanel.add(Box.createVerticalStrut(10));
        registrationPanel.add(scholarshipBox);
        registrationPanel.add(Box.createVerticalStrut(20));
        registrationPanel.add(createUserBtn);


        // --- RIGHT SIDE: ENROLLMENT PANEL ---
        JPanel enrollmentPanel = new JPanel();
        enrollmentPanel.setLayout(new BoxLayout(enrollmentPanel, BoxLayout.Y_AXIS));
        enrollmentPanel.setBorder(BorderFactory.createTitledBorder("Course Enrollment"));

        studentDropdown = new JComboBox<>();
        studentDropdown.addItem("Select a student..."); 
        
        // Populate dropdown with previously saved students
        for(Student s : allStudents) {
            studentDropdown.addItem(s.getName() + " (" + s.getId() + ")");
        }

        String[] courses = {"Calculus", "Physics", "Stats"};
        JComboBox<String> courseDropdown = new JComboBox<>(courses);
        JButton enrollBtn = new JButton("Enroll in Course");

        enrollmentPanel.add(new JLabel("Select Student:"));
        enrollmentPanel.add(studentDropdown);
        enrollmentPanel.add(Box.createVerticalStrut(20));
        enrollmentPanel.add(new JLabel("Select Course:"));
        enrollmentPanel.add(courseDropdown);
        enrollmentPanel.add(Box.createVerticalStrut(20));
        enrollmentPanel.add(enrollBtn);


        // --- ACTION: CREATE USER ---
        createUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String major = (String) majorCombo.getSelectedItem();
                boolean hasScholarship = scholarshipBox.isSelected();

                if(name.isEmpty() || id.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter Name and ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 1. Build the Object
                Student newStudent = new Student.StudentBuilder(name, id)
                                                .setMajor(major)
                                                .setScholarship(hasScholarship)
                                                .build();

                // 2. Add to list and Save to Excel
                allStudents.add(newStudent);
                ExcelDataManager.saveStudentsToExcel(allStudents);

                // 3. Update UI Dropdown
                studentDropdown.addItem(newStudent.getName() + " (" + newStudent.getId() + ")");

                // 4. Clear form
                nameField.setText("");
                idField.setText("");
                scholarshipBox.setSelected(false);

                JOptionPane.showMessageDialog(frame, "Student Created & Saved to Excel!");
            }
        });

        // --- ACTION: ENROLL ---
        enrollBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedStudent = (String) studentDropdown.getSelectedItem();
                String selectedCourse = (String) courseDropdown.getSelectedItem();

                if(selectedStudent == null || selectedStudent.startsWith("Select")) {
                    JOptionPane.showMessageDialog(frame, "Please select a valid student.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JOptionPane.showMessageDialog(frame, selectedStudent + "\nhas been enrolled in " + selectedCourse + "!");
            }
        });

        // Assemble and show
        frame.add(registrationPanel);
        frame.add(enrollmentPanel);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrarUI());
    }
}