package com.android.phone.signalMap;

/* JADX INFO: loaded from: classes.dex */
public class Point2D {
    Double x;
    Double y;

    public Point2D(double d, double d2) {
        this.x = Double.valueOf(d);
        this.y = Double.valueOf(d2);
    }

    public double getX() {
        return this.x.doubleValue();
    }

    public double getY() {
        return this.y.doubleValue();
    }

    public String toString() {
        return "Point2D[" + this.x + ", " + this.y + "]";
    }
}
