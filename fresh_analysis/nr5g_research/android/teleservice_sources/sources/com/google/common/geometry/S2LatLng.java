package com.google.common.geometry;

/* JADX INFO: loaded from: classes.dex */
public class S2LatLng {
    public static final S2LatLng CENTER = new S2LatLng(0.0d, 0.0d);
    private final double latRadians;
    private final double lngRadians;

    public static S2LatLng fromDegrees(double d, double d2) {
        return new S2LatLng(S1Angle.degrees(d), S1Angle.degrees(d2));
    }

    private S2LatLng(double d, double d2) {
        this.latRadians = d;
        this.lngRadians = d2;
    }

    public S2LatLng(S1Angle s1Angle, S1Angle s1Angle2) {
        this(s1Angle.radians(), s1Angle2.radians());
    }

    public S1Angle lat() {
        return S1Angle.radians(this.latRadians);
    }

    public S1Angle lng() {
        return S1Angle.radians(this.lngRadians);
    }

    public S2Point toPoint() {
        double dRadians = lat().radians();
        double dRadians2 = lng().radians();
        double dCos = Math.cos(dRadians);
        return new S2Point(Math.cos(dRadians2) * dCos, Math.sin(dRadians2) * dCos, Math.sin(dRadians));
    }

    public boolean equals(Object obj) {
        if (obj instanceof S2LatLng) {
            S2LatLng s2LatLng = (S2LatLng) obj;
            if (this.latRadians == s2LatLng.latRadians && this.lngRadians == s2LatLng.lngRadians) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(this.latRadians) + 646;
        long jDoubleToLongBits2 = jDoubleToLongBits + (37 * jDoubleToLongBits) + Double.doubleToLongBits(this.lngRadians);
        return (int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32));
    }

    public String toString() {
        return "(" + this.latRadians + ", " + this.lngRadians + ")";
    }
}
