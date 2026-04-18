import java.util.HashMap;
import java.util.Map;

public class CurveGrading implements GradingStrategy {
    public CurveGrading() {} 

    @Override
    public Map<Student, String> calculateGrades(Map<Student, Double> rawScores) {
        Map<Student, String> finalGrades = new HashMap<>();
        if (rawScores.isEmpty()) return finalGrades;

        double sum = 0;
        for (double s : rawScores.values()) sum += s;
        double classAvg = sum / rawScores.size();
        
        double sdSum = 0;
        for (double s : rawScores.values()) sdSum += Math.pow(s - classAvg, 2);
        double sd = Math.sqrt(sdSum / rawScores.size());
        if (sd == 0) sd = 1; 
        
        for (Map.Entry<Student, Double> entry : rawScores.entrySet()) {
            double z = (entry.getValue() - classAvg) / sd;
            if (z >= 1.0) finalGrades.put(entry.getKey(), "A");
            else if (z >= 0.0) finalGrades.put(entry.getKey(), "B");
            else if (z >= -1.0) finalGrades.put(entry.getKey(), "C");
            else if (z >= -2.0) finalGrades.put(entry.getKey(), "D");
            else finalGrades.put(entry.getKey(), "F");
        }
        return finalGrades;
    }
}