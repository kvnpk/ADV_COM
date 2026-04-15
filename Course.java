import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Course implements Subject, Serializable {
    private String courseName;
    private String courseID;
    private List<Student> enrolledStudents;
    private Map<Student, String> grades;
    private List<Observer> observers;
    private GradingStrategy gradingStrategy;

    public Course(String courseName,String courseID,GradingStrategy gradingStrategy) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.gradingStrategy = gradingStrategy;
        this.enrolledStudents = new ArrayList<>();
        this.grades = new HashMap<>();
        this.observers = new ArrayList<>();
    }

    public String getCourseName() { return courseName; }
    public String getCourseID() { return courseID; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public Map<Student, String> getgrades() {return grades;};

    public void addStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            this.enrolledStudents.add(student);
            this.registerObserver(student);
        }
    }

    public boolean gradeStudent(Student student, double rawScore) {
        if (enrolledStudents.contains(student)) {
            String finalGrade = gradingStrategy.calculateGrade(rawScore);
            grades.put(student, finalGrade);
            notifyObservers("final grade out");
            return true;
        }
        else {
            System.out.println("Error: Student not enrolled.");
            return false;
        }
    }
    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
}
}