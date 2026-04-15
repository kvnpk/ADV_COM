import java.io.Serializable;

public class GradingCriteria implements Serializable {
    private double minA;
    private double minB;
    private double minC;
    private double minD;

    public GradingCriteria(double minA, double minB, double minC, double minD) {
        this.minA = minA;
        this.minB = minB;
        this.minC = minC;
        this.minD = minD;
    }

    public double getminA() { return minA; }
    public double getminB() { return minB; }
    public double getminC() { return minC; }
    public double getminD() { return minD; }
}
