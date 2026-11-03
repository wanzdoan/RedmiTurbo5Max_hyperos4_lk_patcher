package miuix.animation.function;

/* JADX INFO: loaded from: classes2.dex */
public class FreeDamping implements Differentiable {
    private final double c;
    private final double d;
    private Function derivative;
    private final double g;
    private final double p;

    public FreeDamping(double d, double d2, double d3, double d4) {
        this.c = d;
        this.d = d2;
        this.p = d3;
        this.g = d4;
    }

    @Override // miuix.animation.function.Differentiable, miuix.animation.function.Function
    public double apply(double d) {
        double d2 = this.c;
        double d3 = this.p;
        return ((-(d2 / d3)) * Math.exp((-d3) * d)) + ((this.g / this.p) * d) + this.d;
    }

    @Override // miuix.animation.function.Differentiable
    public Function derivative() {
        if (this.derivative == null) {
            this.derivative = new Function() { // from class: miuix.animation.function.FreeDamping$$ExternalSyntheticLambda0
                @Override // miuix.animation.function.Function
                public final double apply(double d) {
                    return FreeDamping.m5431$r8$lambda$exqCQmpweX8Ba5QrxpNzudoqJo(this.f$0, d);
                }
            };
        }
        return this.derivative;
    }

    /* JADX INFO: renamed from: $r8$lambda$exqCQmpweX8Ba-5QrxpNzudoqJo, reason: not valid java name */
    public static /* synthetic */ double m5431$r8$lambda$exqCQmpweX8Ba5QrxpNzudoqJo(FreeDamping freeDamping, double d) {
        return (freeDamping.c * Math.exp((-freeDamping.p) * d)) + (freeDamping.g / freeDamping.p);
    }
}
