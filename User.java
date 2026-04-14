import java.io.Serializable;
import java.util.Objects; // Don't forget this new import!

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String id;

    public User(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // --- NEW: Teach Java how to compare Users ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Two users are the exact same if they have the same ID
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}