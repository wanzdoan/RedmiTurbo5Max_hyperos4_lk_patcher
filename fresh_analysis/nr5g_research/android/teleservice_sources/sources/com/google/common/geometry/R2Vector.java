package com.google.common.geometry;

/* JADX INFO: loaded from: classes.dex */
public final class R2Vector {
    private final double x;
    private final double y;

    public R2Vector(double d, double d2) {
        this.x = d;
        this.y = d2;
    }

    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof R2Vector)) {
            return false;
        }
        R2Vector r2Vector = (R2Vector) obj;
        return this.x == r2Vector.x && this.y == r2Vector.y;
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(Math.abs(this.x)) + 646;
        long jDoubleToLongBits2 = jDoubleToLongBits + (37 * jDoubleToLongBits) + Double.doubleToLongBits(Math.abs(this.y));
        return (int) (jDoubleToLongBits2 ^ (jDoubleToLongBits2 >>> 32));
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
