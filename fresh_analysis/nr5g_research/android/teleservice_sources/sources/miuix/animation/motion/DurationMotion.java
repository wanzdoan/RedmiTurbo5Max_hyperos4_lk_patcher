package miuix.animation.motion;

import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.function.Differentiable;

/* JADX INFO: loaded from: classes2.dex */
public final class DurationMotion implements Motion {
    public static final double DURATION_FOREVER = Double.POSITIVE_INFINITY;
    private final double duration;
    private final Motion origin;
    private final boolean stopAtEnd;

    public DurationMotion(Motion motion, double d) {
        this(motion, d, true);
    }

    public DurationMotion(Motion motion, double d, boolean z) {
        if (d <= 0.0d) {
            Folme$$ExternalSyntheticBUOutline0.m("duration must be positive");
            throw null;
        }
        this.origin = motion;
        this.duration = d;
        this.stopAtEnd = z;
    }

    @Override // miuix.animation.motion.Motion
    public void setInitialX(double d) {
        this.origin.setInitialX(d);
    }

    @Override // miuix.animation.motion.Motion
    public void setInitialV(double d) {
        this.origin.setInitialV(d);
    }

    @Override // miuix.animation.motion.Motion
    public double getInitialX() {
        return this.origin.getInitialX();
    }

    @Override // miuix.animation.motion.Motion
    public double getInitialV() {
        return this.origin.getInitialV();
    }

    @Override // miuix.animation.motion.Motion
    public Differentiable solve() {
        return this.origin.solve();
    }

    @Override // miuix.animation.motion.Motion
    public double finishTime() {
        return this.duration;
    }

    @Override // miuix.animation.motion.Motion
    public double stopPosition() {
        double dFinishTime = this.origin.finishTime();
        double d = this.duration;
        Motion motion = this.origin;
        return dFinishTime < d ? motion.stopPosition() : motion.solve().apply(this.duration);
    }

    @Override // miuix.animation.motion.Motion
    public double stopSpeed() {
        if (this.stopAtEnd) {
            return 0.0d;
        }
        double dFinishTime = this.origin.finishTime();
        double d = this.duration;
        Motion motion = this.origin;
        return dFinishTime < d ? motion.stopSpeed() : motion.solve().derivative().apply(this.duration);
    }
}
