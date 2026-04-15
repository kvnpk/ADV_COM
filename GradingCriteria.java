import java.io.Serializable;

public class GradingCriteria implements Serializable {
    private double minA;
    private double minBplus;
    private double minB;
    private double minCplus;
    private double minC;
    private double minDplus;
    private double minD;

    public GradingCriteria(double minA, double minBplus, double minB, double minCplus, double minC, double minDplus, double minD) {
        this.minA = minA;
        this.minBplus = minBplus;
        this.minB = minB;
        this.minCplus = minCplus;
        this.minC = minC;
        this.minDplus = minDplus;
        this.minD = minD;
    }

    public double getminA() { return minA; }
    public double getminBplus() { return minBplus; }
    public double getminB() { return minB; }
    public double getminCplus() { return minCplus; }
    public double getminC() { return minC; }
    public double getminDplus() { return minDplus; }
    public double getminD() { return minD; }
}
