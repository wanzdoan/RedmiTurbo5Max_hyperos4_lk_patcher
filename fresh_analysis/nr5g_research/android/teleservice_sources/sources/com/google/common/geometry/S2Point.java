package com.google.common.geometry;

/* JADX INFO: loaded from: classes.dex */
public class S2Point implements Comparable {
    final double x;
    final double y;
    final double z;

    public S2Point(double d, double d2, double d3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
    }

    public int largestAbsComponent() {
        S2Point s2PointFabs = fabs(this);
        double d = s2PointFabs.x;
        double d2 = s2PointFabs.y;
        double d3 = s2PointFabs.z;
        if (d > d2) {
            return d > d3 ? 0 : 2;
        }
        return d2 > d3 ? 1 : 2;
    }

    public static S2Point fabs(S2Point s2Point) {
        return new S2Point(Math.abs(s2Point.x), Math.abs(s2Point.y), Math.abs(s2Point.z));
    }

    public double get(int i) {
        if (i == 0) {
            return this.x;
        }
        return i == 1 ? this.y : this.z;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof S2Point)) {
            return false;
        }
        S2Point s2Point = (S2Point) obj;
        return this.x == s2Point.x && this.y == s2Point.y && this.z == s2Point.z;
    }

    public boolean lessThan(S2Point s2Point) {
        double d = this.x;
        double d2 = s2Point.x;
        if (d < d2) {
            return true;
        }
        if (d2 < d) {
            return false;
        }
        double d3 = this.y;
        double d4 = s2Point.y;
        if (d3 < d4) {
            return true;
        }
        return d4 >= d3 && this.z < s2Point.z;
    }

    @Override // java.lang.Comparable
    public int compareTo(S2Point s2Point) {
        if (lessThan(s2Point)) {
            return -1;
        }
        return equals(s2Point) ? 0 : 1;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public int hashCode() {
        long jDoubleToLongBits = Double.doubleToLongBits(Math.abs(this.x)) + 646;
        long jDoubleToLongBits2 = jDoubleToLongBits + (jDoubleToLongBits * 37) + Double.doubleToLongBits(Math.abs(this.y));
        long jDoubleToLongBits3 = jDoubleToLongBits2 + (37 * jDoubleToLongBits2) + Double.doubleToLongBits(Math.abs(this.z));
        return (int) (jDoubleToLongBits3 ^ (jDoubleToLongBits3 >>> 32));
    }
}
