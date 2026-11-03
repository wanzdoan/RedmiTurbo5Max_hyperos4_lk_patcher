package miuix.animation.easing;

import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.motion.DurationMotion;
import miuix.animation.motion.Motion;
import miuix.animation.motion.UniformlyAcceleratedMotion;

/* JADX INFO: loaded from: classes2.dex */
public class QuadInEasing implements SimpleEasing {
    private final double duration;

    @Override // miuix.animation.easing.SimpleEasing
    public double startSpeed() {
        return 0.0d;
    }

    public QuadInEasing() {
        this(1.0d);
    }

    public QuadInEasing(double d) {
        if (d <= 0.0d) {
            Folme$$ExternalSyntheticBUOutline0.m("duration must be positive");
            throw null;
        }
        this.duration = d;
    }

    @Override // miuix.animation.FolmeEase
    public Motion newMotion() {
        double d = this.duration;
        return new DurationMotion(new UniformlyAcceleratedMotion(2.0d / (d * d)), this.duration, true);
    }

    @Override // miuix.animation.easing.SimpleEasing
    public final double duration() {
        return this.duration;
    }

    public String toString() {
        return "QuadIn(" + this.duration + ")";
    }
}
