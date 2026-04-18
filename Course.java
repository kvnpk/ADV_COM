import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Course implements Subject, Serializable {
    private String courseName;
    private String courseID;
    private List<Student> enrolledStudents;
    private Map<Student, String> grades;
    private Map<Student, Double> rawScores; 
    private List<Observer> observers;
    private GradingStrategy gradingStrategy;

    public Course(String courseName, String courseID, GradingStrategy gradingStrategy) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.gradingStrategy = gradingStrategy;
        this.enrolledStudents = new ArrayList<>();
        this.grades = new HashMap<>();
        this.rawScores = new HashMap<>(); 
        this.observers = new ArrayList<>();
    }

    public String getCourseName() { return courseName; }
    public String getCourseID() { return courseID; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public Map<Student, String> getgrades() { return grades; }

    public void addStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            this.enrolledStudents.add(student);
            this.registerObserver(student);
        }
    }

    public void gradeStudent(Student student, double rawScore) {
        if (enrolledStudents.contains(student)) {
            this.rawScores.put(student, rawScore);
            this.grades = gradingStrategy.calculateGrades(this.rawScores); 
            notifyObservers("A new grade was posted in " + courseName+".");
        } else {
            System.out.println("Error: Student not enrolled.");
        }
    }

    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) { observers.remove(o); }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) o.update(message);
    }
}