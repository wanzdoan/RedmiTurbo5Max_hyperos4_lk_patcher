package miuix.animation.function;

import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class OverDamping implements Damping {
    private final double c1;
    private final double c2;
    private Function derivative;
    private final double r1;
    private final double r2;
    private final double xStar;

    public OverDamping(double d, double d2, double d3, double d4, double d5) {
        this.c1 = d;
        this.c2 = d2;
        this.r1 = d3;
        this.r2 = d4;
        this.xStar = d5;
    }

    @Override // miuix.animation.function.Damping, miuix.animation.function.Differentiable, miuix.animation.function.Function
    public double apply(double d) {
        return (this.c1 * Math.exp(this.r1 * d)) + (this.c2 * Math.exp(this.r2 * d)) + this.xStar;
    }

    @Override // miuix.animation.function.Damping, miuix.animation.function.Differentiable
    public Function derivative() {
        if (this.derivative == null) {
            this.derivative = new Function() { // from class: miuix.animation.function.OverDamping$$ExternalSyntheticLambda0
                @Override // miuix.animation.function.Function
                public final double apply(double d) {
                    return OverDamping.m5432$r8$lambda$nV3NbAy1seyngLjUER8EAdvQ(this.f$0, d);
                }
            };
        }
        return this.derivative;
    }

    /* JADX INFO: renamed from: $r8$lambda$nV3NbA-y1se-yng-LjUER8EAdvQ, reason: not valid java name */
    public static /* synthetic */ double m5432$r8$lambda$nV3NbAy1seyngLjUER8EAdvQ(OverDamping overDamping, double d) {
        double d2 = overDamping.c1;
        double d3 = overDamping.r1;
        double dExp = d2 * d3 * Math.exp(d3 * d);
        double d4 = overDamping.c2;
        double d5 = overDamping.r2;
        return dExp + (d4 * d5 * Math.exp(d5 * d));
    }

    public String toString() {
        return String.format(Locale.getDefault(), "OverDamping{c1=%.3f c2=%.3f r1=%.3f r2=%.3f, x*=%.3f}", Double.valueOf(this.c1), Double.valueOf(this.c2), Double.valueOf(this.r1), Double.valueOf(this.r2), Double.valueOf(this.xStar));
    }
}
