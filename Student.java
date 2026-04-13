public class Student extends User {
    private static final long serialVersionUID = 1L;

    private String major;

    private Student(StudentBuilder builder) {
        super(builder.name, builder.id);
        this.major = builder.major;
    }

    public String getMajor() { return major; }

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
