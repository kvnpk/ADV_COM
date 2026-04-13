import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerializationManager {
    private static final String STUDENT_FILE = "students.ser";
    private static final String ENROLLMENT_FILE = "enrollments.ser";
    private static final String PROFESSOR_FILE = "professors.ser"; // NEW

    // --- STUDENT DATA ---
    public static void saveStudents(List<Student> students) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STUDENT_FILE))) {
            oos.writeObject(students);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static List<Student> loadStudents() {
        File file = new File(STUDENT_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Student>) ois.readObject();
        } catch (Exception e) { return new ArrayList<>(); }
    }

    // --- PROFESSOR DATA (NEW) ---
    public static void saveProfessors(List<Professor> professors) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PROFESSOR_FILE))) {
            oos.writeObject(professors);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static List<Professor> loadProfessors() {
        File file = new File(PROFESSOR_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Professor>) ois.readObject();
        } catch (Exception e) { return new ArrayList<>(); }
    }

    // --- ENROLLMENT DATA ---
    public static void saveEnrollment(String studentId, String courseName) {
        List<String> enrollments = loadEnrollments();
        enrollments.add(studentId + "," + courseName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ENROLLMENT_FILE))) {
            oos.writeObject(enrollments);
        } catch (IOException e) { e.printStackTrace(); }
    }

    @SuppressWarnings("unchecked")
    public static List<String> loadEnrollments() {
        File file = new File(ENROLLMENT_FILE);
        if (!file.exists()) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<String>) ois.readObject();
        } catch (Exception e) { return new ArrayList<>(); }
    }
}