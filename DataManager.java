import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String FILE_NAME = "course_data.ser";

    // --- SAVE DATA TO FILE ---
    public static void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(courses);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // --- LOAD DATA FROM FILE ---
    @SuppressWarnings("unchecked")
    public static List<Course> loadCourses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>(); // Return empty list if no save file exists yet
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
