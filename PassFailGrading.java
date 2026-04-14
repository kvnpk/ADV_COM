public class PassFailGrading implements GradingStrategy{
    @Override
    public String calculateGrade(double rawScore) {
        if (rawScore >= 50) {
            return "S";
        }
        else {
            return "F";
        }
    }
}
