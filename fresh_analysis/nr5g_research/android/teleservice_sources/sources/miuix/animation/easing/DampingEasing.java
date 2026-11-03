package miuix.animation.easing;

import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.motion.FreeDampedMotion;
import miuix.animation.motion.Motion;

/* JADX INFO: loaded from: classes2.dex */
public class DampingEasing implements PhysicalEasing {
    private final double acceleration;
    private final double damping;

    public DampingEasing(double d, double d2) {
        if (d < 0.0d) {
            Folme$$ExternalSyntheticBUOutline0.m("damping must not be negative");
            throw null;
        }
        this.damping = d;
        this.acceleration = d2;
    }

    public final double getDamping() {
        return this.damping;
    }

    public final double getAcceleration() {
        return this.acceleration;
    }

    @Override // miuix.animation.FolmeEase
    public Motion newMotion() {
        return new FreeDampedMotion(this.damping, this.acceleration);
    }

    public String toString() {
        return "Damping(" + this.damping + ", " + this.acceleration + ")";
    }
}
