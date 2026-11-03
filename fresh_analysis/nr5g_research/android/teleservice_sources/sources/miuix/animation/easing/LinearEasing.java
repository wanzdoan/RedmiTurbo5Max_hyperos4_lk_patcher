package miuix.animation.easing;

import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.motion.DurationMotion;
import miuix.animation.motion.Motion;
import miuix.animation.motion.UniformLinearMotion;

/* JADX INFO: loaded from: classes2.dex */
public class LinearEasing implements SimpleEasing {
    private final double duration;

    public LinearEasing() {
        this(1.0d);
    }

    public LinearEasing(double d) {
        if (d <= 0.0d) {
            Folme$$ExternalSyntheticBUOutline0.m("duration must be positive");
            throw null;
        }
        this.duration = d;
    }

    @Override // miuix.animation.FolmeEase
    public Motion newMotion() {
        return new DurationMotion(new UniformLinearMotion(), this.duration, true);
    }

    @Override // miuix.animation.easing.SimpleEasing
    public final double duration() {
        return this.duration;
    }

    @Override // miuix.animation.easing.SimpleEasing
    public double startSpeed() {
        return 1.0d / this.duration;
    }

    public String toString() {
        return "Linear(" + this.duration + ")";
    }
}
