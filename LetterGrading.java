import java.util.HashMap;
import java.util.Map;

public class LetterGrading implements GradingStrategy {
    private GradingCriteria criteria;

    public LetterGrading(GradingCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Map<Student, String> calculateGrades(Map<Student, Double> rawScores) {
        Map<Student, String> finalGrades = new HashMap<>();
        for (Map.Entry<Student, Double> entry : rawScores.entrySet()) {
            double score = entry.getValue();
            if (score >= this.criteria.getminA()) finalGrades.put(entry.getKey(), "A");
            else if (score >= this.criteria.getminB()) finalGrades.put(entry.getKey(), "B");
            else if (score >= this.criteria.getminC()) finalGrades.put(entry.getKey(), "C");
            else if (score >= this.criteria.getminD()) finalGrades.put(entry.getKey(), "D");
            else finalGrades.put(entry.getKey(), "F");
        }
        return finalGrades;
    }
}