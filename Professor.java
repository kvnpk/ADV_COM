public class Professor extends User {
    private static final long serialVersionUID = 1L;

    private String department;
    
    public Professor(String name, String id, String department) {
        super(name, id);
        this.department = department;
    }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
