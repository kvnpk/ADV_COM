import java.io.Serializable;
import java.util.Map;

public interface GradingStrategy extends Serializable {
    Map<Student, String> calculateGrades(Map<Student, Double> rawScores);
}
