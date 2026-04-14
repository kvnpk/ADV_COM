import java.util.List;

public class ProfessorController {
    private List<Course> masterCourseList;

    public ProfessorController(List<Course> masterCourseList) {
        this.masterCourseList = masterCourseList;
    }

    public void inputGrade(Course course, Student student, double rawScore) {
        System.out.println("System: Professor is submitting a grade...");
        course.gradeStudent(student, rawScore);
        DataManager.saveCourses(masterCourseList);
    }

    public void createCourse(String name, String id, GradingStrategy strategy) {
        Course newCourse = new Course(name, id, strategy);
        masterCourseList.add(newCourse);
        DataManager.saveCourses(masterCourseList);
    }

}
