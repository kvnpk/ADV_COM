public class CurveGrading implements GradingStrategy {
    private double classAvg;
    private double sd;
    
    public CurveGrading(double classAvg, double sd) {
        this.classAvg = classAvg;
        this.sd= sd;
    }

    @Override
    public String calculateGrade(double rawScore) {
        double z = (rawScore - classAvg) / sd;
        if (z >= 1.0) {
            return "A";
        } else if (z >= 0.0) {
            return "B"; 
        } else if (z >= -1.0) {
            return "C"; 
        } else if (z >= -2.0) {
            return "D"; 
        } else {
            return "F";
    }
}
}