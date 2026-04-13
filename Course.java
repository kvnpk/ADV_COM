import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseName;
    private List<Student> enrolledStudents;

    public Course(String courseName) {
        this.courseName = courseName;
        this.enrolledStudents = new ArrayList<>();
    }

    public String getCourseName() { return courseName; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }

    public void addStudent(Student student) {
        this.enrolledStudents.add(student);
    }
}