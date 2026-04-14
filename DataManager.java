import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String COURSE_FILE = "course_data.ser";
    private static final String STUDENT_FILE = "students_data.ser";
    private static final String PROF_FILE = "professors_data.ser";


    public static void saveCourses(List<Course> courses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COURSE_FILE))) {
            oos.writeObject(courses);
            System.out.println("System: Course data saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Course> loadCourses() {
        File file = new File(COURSE_FILE);
        if (!file.exists()) {
            return new ArrayList<>(); 
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COURSE_FILE))) {
            return (List<Course>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading courses: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STUDENT_FILE))) {
            oos.writeObject(students);
            System.out.println("System: Student data saved successfully!");
        } catch (IOException e) {
            System.err.println("Error saving students: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Student> loadStudents() {
        File file = new File(STUDENT_FILE);
        if (!file.exists()) {
            return new ArrayList<>(); 
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STUDENT_FILE))) {
            return (List<Student>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading students: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public static void saveProfessors(List<Professor> professors) {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROF_FILE))) {
        oos.writeObject(professors);
    } catch (IOException e) { e.printStackTrace(); }
}

    @SuppressWarnings("unchecked")
    public static List<Professor> loadProfessors() {
        File file = new File(PROF_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PROF_FILE))) {
            return (List<Professor>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) { return new ArrayList<>(); }
    }
}