import java.io.Serializable;

public interface GradingStrategy extends Serializable{
    String calculateGrade(double rawScore);
}
