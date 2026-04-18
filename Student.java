import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Observer {
    private static final long serialVersionUID = 1L;
    private String major;
    private List<String> inbox; 

    private Student(StudentBuilder builder) {
        super(builder.name, builder.id);
        this.major = builder.major;
        this.inbox = new ArrayList<>(); 
    }

    @Override
    public void update(String message) {
        if (this.inbox == null) this.inbox = new ArrayList<>();
        this.inbox.add(message);
        System.out.println("System: Notified " + this.getName() + " -> " + message);
    }

    public String getMajor() { return major; }
    public List<String> getInbox() { return inbox; } 

    public static class StudentBuilder {
        protected String name;
        protected String id;
        private String major = "Undeclared";

        public StudentBuilder(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public StudentBuilder setMajor(String major) {
            this.major = major;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }
}