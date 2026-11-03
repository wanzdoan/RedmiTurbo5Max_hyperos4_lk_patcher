package miuix.animation.function;

/* JADX INFO: loaded from: classes2.dex */
public class Logarithmic implements Differentiable {
    private final double a;
    private Function derivative;
    private final boolean isLn;
    private final double k;

    public Logarithmic(double d) {
        this(1.0d, d);
    }

    public Logarithmic(double d, double d2) {
        this.k = d;
        this.a = d2;
        this.isLn = d2 == 2.718281828459045d;
    }

    @Override // miuix.animation.function.Differentiable, miuix.animation.function.Function
    public double apply(double d) {
        boolean z = this.isLn;
        double d2 = this.k;
        return z ? d2 * Math.log(d) : (d2 * Math.log(d)) / Math.log(this.a);
    }

    @Override // miuix.animation.function.Differentiable
    public Function derivative() {
        if (this.derivative == null) {
            boolean z = this.isLn;
            double dLog = this.k;
            if (!z) {
                dLog /= Math.log(this.a);
            }
            this.derivative = new InverseProportional(dLog);
        }
        return this.derivative;
    }
}
