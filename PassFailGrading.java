import java.util.HashMap;
import java.util.Map;

public class PassFailGrading implements GradingStrategy {
    @Override
    public Map<Student, String> calculateGrades(Map<Student, Double> rawScores) {
        Map<Student, String> finalGrades = new HashMap<>();
        for (Map.Entry<Student, Double> entry : rawScores.entrySet()) {
            if (entry.getValue() >= 50) finalGrades.put(entry.getKey(), "S");
            else finalGrades.put(entry.getKey(), "F");
        }
        return finalGrades;
    }
}