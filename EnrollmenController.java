public class EnrollmenController {
    public void enrollStudent(Student student, Course course) {
        if (student != null && course != null) {
            course.addStudent(student);
            System.out.println(student.getName() + " has been enrolled in " + course.getCourseName());
        }
    }
}
