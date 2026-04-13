import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelDataManager {

    // This file will appear in your project folder and can be opened with Excel
    private static final String FILE_NAME = "students_data.csv";

    // --- 1. EXPORT TO EXCEL (SAVE) ---
    public static void saveStudentsToExcel(List<Student> students) {
        // FileWriter automatically creates the file if it doesn't exist
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            
            // Write the Header Row (These become your Excel column names)
            writer.println("Name,ID,Major");

            // Loop through students and write their data separated by commas
            for (Student student : students) {
                writer.println(
                    student.getName() + "," + 
                    student.getId() + "," + 
                    student.getMajor() + ","
                );
            }
            System.out.println("Data successfully saved to Excel (CSV)!");
            
        } catch (IOException e) {
            System.err.println("Error saving to Excel: " + e.getMessage());
        }
    }

    // --- 2. EXTRACT FROM EXCEL (LOAD) ---
    public static List<Student> loadStudentsFromExcel() {
        List<Student> loadedStudents = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        // If the file doesn't exist yet, just return an empty list
        if (!file.exists()) {
            return loadedStudents;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstRow = true; // We use this to skip the header row
            
            while ((line = reader.readLine()) != null) {
                // Skip the "Name,ID,Major..." header row
                if (isFirstRow) {
                    isFirstRow = false;
                    continue;
                }

                // Split the row by commas to get the individual cells
                String[] cells = line.split(",");
                
                // Ensure the row has exactly 4 columns before trying to read it
                if (cells.length == 4) {
                    String name = cells[0];
                    String id = cells[1];
                    String major = cells[2];

                    // USE YOUR BUILDER to recreate the Student object!
                    Student student = new Student.StudentBuilder(name, id)
                            .setMajor(major)
                            .build();
                    
                    loadedStudents.add(student);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading from Excel: " + e.getMessage());
        }
        
        return loadedStudents;
    }
}