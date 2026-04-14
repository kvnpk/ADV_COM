public class LetterGrading implements GradingStrategy{
    private GradingCriteria criteria;

    public LetterGrading(GradingCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public String calculateGrade(double rawScore) {
        if (rawScore >= this.criteria.getminA()) {
            return "A";
        } else if (rawScore >= this.criteria.getminB()) {
            return "B";
        } else if (rawScore >= this.criteria.getminC()) {
            return "C";
        } else if (rawScore >= this.criteria.getminD()) {
            return "D";
        } else {
            return "F";
        }
    }
}
