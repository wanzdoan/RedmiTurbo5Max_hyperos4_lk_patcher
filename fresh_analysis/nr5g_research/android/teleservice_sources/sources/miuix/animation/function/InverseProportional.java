package miuix.animation.function;

/* JADX INFO: loaded from: classes2.dex */
public class InverseProportional implements Differentiable {
    private Function derivative;
    private final double k;

    public InverseProportional(double d) {
        this.k = d;
    }

    @Override // miuix.animation.function.Differentiable, miuix.animation.function.Function
    public double apply(double d) {
        return this.k / d;
    }

    @Override // miuix.animation.function.Differentiable
    public Function derivative() {
        if (this.derivative == null) {
            this.derivative = new Function() { // from class: miuix.animation.function.InverseProportional$$ExternalSyntheticLambda0
                @Override // miuix.animation.function.Function
                public final double apply(double d) {
                    return InverseProportional.$r8$lambda$JMtYRsHZJX7ZwpntedCp3rKerNM(this.f$0, d);
                }
            };
        }
        return this.derivative;
    }

    public static /* synthetic */ double $r8$lambda$JMtYRsHZJX7ZwpntedCp3rKerNM(InverseProportional inverseProportional, double d) {
        return ((-inverseProportional.k) / d) / d;
    }
}
