package com.google.common.geometry;

/* JADX INFO: loaded from: classes.dex */
public final class S1Angle implements Comparable {
    private final double radians;

    public double radians() {
        return this.radians;
    }

    public double degrees() {
        return this.radians * 57.29577951308232d;
    }

    private S1Angle(double d) {
        this.radians = d;
    }

    public boolean equals(Object obj) {
        return (obj instanceof S1Angle) && radians() == ((S1Angle) obj).radians();
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.radians);
        return (int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32));
    }

    public static S1Angle radians(double d) {
        return new S1Angle(d);
    }

    public static S1Angle degrees(double d) {
        return new S1Angle(d * 0.017453292519943295d);
    }

    public String toString() {
        return degrees() + "d";
    }

    @Override // java.lang.Comparable
    public int compareTo(S1Angle s1Angle) {
        double d = this.radians;
        double d2 = s1Angle.radians;
        if (d < d2) {
            return -1;
        }
        return d > d2 ? 1 : 0;
    }
}
