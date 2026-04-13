        createUserBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 1. Grab what the user typed in the UI
                String name = nameField.getText();
                String id = idField.getText();
                String major = (String) majorCombo.getSelectedItem();
                boolean hasScholarship = scholarshipBox.isSelected();

                if(name.isEmpty() || id.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter Name and ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 2. USE YOUR BUILDER HERE to create the actual Java Object
                Student newStudent = new Student.StudentBuilder(name, id)
                                                .setMajor(major)
                                                .setScholarship(hasScholarship)
                                                .build();

                // 3. For the UI visual, we just create a string to show in the dropdown
                String studentDisplayInfo = newStudent.getName() + " (" + newStudent.getId() + ") - " + newStudent.getMajor();
                
                // 4. Add it to our visual lists
                registeredStudents.add(studentDisplayInfo);
                studentDropdown.addItem(studentDisplayInfo);

                // Clear fields
                nameField.setText("");
                idField.setText("");
                scholarshipBox.setSelected(false);

                JOptionPane.showMessageDialog(frame, "Student Created Successfully!\nNow available for enrollment.");
            }
        });