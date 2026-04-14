import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentController {
    private List<Course> masterCourseList;

    public StudentController(List<Course> masterCourseList) {
        this.masterCourseList = masterCourseList;
    }

    public void enrollInCourse(Student student, Course course) {
        course.addStudent(student);
        DataManager.saveCourses(masterCourseList);
    }

    public Map<String, String> getMyTranscript(Student student) {
        Map<String, String> myGrades = new HashMap<>();
        
        for (Course c : masterCourseList) {
            if (c.getEnrolledStudents().contains(student)) {
                String grade = c.getgrades().getOrDefault(student, "Pending");
                myGrades.put(c.getCourseName(), grade);
            }
        }
        return myGrades;
    }
}