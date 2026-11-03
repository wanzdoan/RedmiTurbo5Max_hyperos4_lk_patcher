package com.google.common.geometry;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class S2Projections {
    public static final S2.Metric AVG_ANGLE_SPAN;
    public static final S2.Metric AVG_AREA;
    public static final S2.Metric AVG_DIAG;
    public static final S2.Metric AVG_EDGE;
    public static final S2.Metric AVG_WIDTH;
    public static final S2.Metric MAX_ANGLE_SPAN;
    public static final S2.Metric MAX_AREA;
    public static final S2.Metric MAX_DIAG;
    public static final double MAX_DIAG_ASPECT;
    public static final S2.Metric MAX_EDGE;
    public static final double MAX_EDGE_ASPECT;
    public static final S2.Metric MAX_WIDTH;
    public static final S2.Metric MIN_ANGLE_SPAN;
    public static final S2.Metric MIN_AREA;
    public static final S2.Metric MIN_DIAG;
    public static final S2.Metric MIN_EDGE;
    public static final S2.Metric MIN_WIDTH;
    private static final Projections S2_PROJECTION;

    public enum Projections {
        S2_LINEAR_PROJECTION,
        S2_TAN_PROJECTION,
        S2_QUADRATIC_PROJECTION
    }

    static {
        double dSqrt;
        double d;
        double d2;
        double d3;
        double dSqrt2;
        double d4;
        double d5;
        double d6;
        double d7;
        double dSqrt3;
        double d8;
        Projections projections = Projections.S2_QUADRATIC_PROJECTION;
        S2_PROJECTION = projections;
        Projections projections2 = Projections.S2_LINEAR_PROJECTION;
        if (projections == projections2) {
            dSqrt = 1.0d / (Math.sqrt(3.0d) * 3.0d);
        } else if (projections == Projections.S2_TAN_PROJECTION) {
            dSqrt = 9.869604401089358d / (S2.M_SQRT2 * 16.0d);
        } else {
            dSqrt = (S2.M_SQRT2 * 2.0d) / 9.0d;
        }
        MIN_AREA = new S2.Metric(2, dSqrt);
        if (projections == projections2) {
            d = 1.0d;
        } else {
            d = projections == Projections.S2_TAN_PROJECTION ? 0.6168502750680849d : 0.6589498142407904d;
        }
        MAX_AREA = new S2.Metric(2, d);
        AVG_AREA = new S2.Metric(2, 0.5235987755982988d);
        if (projections == projections2) {
            d2 = 0.5d;
        } else {
            d2 = projections == Projections.S2_TAN_PROJECTION ? 0.7853981633974483d : 0.6666666666666666d;
        }
        MIN_ANGLE_SPAN = new S2.Metric(1, d2);
        if (projections == projections2) {
            d3 = 1.0d;
        } else {
            d3 = projections == Projections.S2_TAN_PROJECTION ? 0.7853981633974483d : 0.8524485895996092d;
        }
        S2.Metric metric = new S2.Metric(1, d3);
        MAX_ANGLE_SPAN = metric;
        AVG_ANGLE_SPAN = new S2.Metric(1, 0.7853981633974483d);
        if (projections == projections2) {
            dSqrt2 = 1.0d / Math.sqrt(6.0d);
        } else {
            dSqrt2 = projections == Projections.S2_TAN_PROJECTION ? 3.141592653589793d / (S2.M_SQRT2 * 4.0d) : S2.M_SQRT2 / 3.0d;
        }
        MIN_WIDTH = new S2.Metric(1, dSqrt2);
        MAX_WIDTH = new S2.Metric(1, metric.deriv());
        if (projections == projections2) {
            d4 = 0.7057296729222284d;
        } else {
            d4 = projections == Projections.S2_TAN_PROJECTION ? 0.7186593194625804d : 0.7172618364430496d;
        }
        AVG_WIDTH = new S2.Metric(1, d4);
        if (projections != projections2 && projections == Projections.S2_TAN_PROJECTION) {
            d5 = 3.141592653589793d / (S2.M_SQRT2 * 4.0d);
        } else {
            double d9 = S2.M_SQRT2;
            d5 = d9 / 3.0d;
        }
        MIN_EDGE = new S2.Metric(1, d5);
        MAX_EDGE = new S2.Metric(1, metric.deriv());
        if (projections == projections2) {
            d6 = 0.7200170964778018d;
        } else {
            d6 = projections == Projections.S2_TAN_PROJECTION ? 0.7308335162733697d : 0.7296068731930531d;
        }
        AVG_EDGE = new S2.Metric(1, d6);
        if (projections == projections2) {
            d7 = S2.M_SQRT2 / 3.0d;
        } else {
            d7 = projections == Projections.S2_TAN_PROJECTION ? 3.141592653589793d / (S2.M_SQRT2 * 3.0d) : (S2.M_SQRT2 * 4.0d) / 9.0d;
        }
        MIN_DIAG = new S2.Metric(1, d7);
        if (projections == projections2) {
            dSqrt3 = S2.M_SQRT2;
        } else {
            dSqrt3 = projections == Projections.S2_TAN_PROJECTION ? 3.141592653589793d / Math.sqrt(6.0d) : 1.2193272972170106d;
        }
        MAX_DIAG = new S2.Metric(1, dSqrt3);
        if (projections == projections2) {
            d8 = 1.0159089332094062d;
        } else {
            d8 = projections == Projections.S2_TAN_PROJECTION ? 1.0318115985978178d : 1.0302113694992359d;
        }
        AVG_DIAG = new S2.Metric(1, d8);
        double d10 = (projections == projections2 || projections == Projections.S2_TAN_PROJECTION) ? S2.M_SQRT2 : 1.442615274452683d;
        MAX_EDGE_ASPECT = d10;
        MAX_DIAG_ASPECT = Math.sqrt(3.0d);
    }

    public static double uvToST(double d) {
        int iOrdinal = S2_PROJECTION.ordinal();
        if (iOrdinal == 0) {
            return d;
        }
        if (iOrdinal == 1) {
            return Math.atan(d) * 1.2732395447351628d;
        }
        if (iOrdinal != 2) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Invalid value for S2_PROJECTION");
            return 0.0d;
        }
        if (d >= 0.0d) {
            return Math.sqrt((d * 3.0d) + 1.0d) - 1.0d;
        }
        return 1.0d - Math.sqrt(1.0d - (d * 3.0d));
    }

    public static R2Vector validFaceXyzToUv(int i, S2Point s2Point) {
        double d;
        double d2;
        double d3;
        double d4;
        if (i == 0) {
            double d5 = s2Point.y;
            d = s2Point.x;
            d2 = d5 / d;
            d3 = s2Point.z;
        } else if (i != 1) {
            if (i == 2) {
                double d6 = -s2Point.x;
                d = s2Point.z;
                d2 = d6 / d;
                d4 = s2Point.y;
            } else if (i == 3) {
                double d7 = s2Point.z;
                d = s2Point.x;
                d2 = d7 / d;
                d3 = s2Point.y;
            } else if (i == 4) {
                double d8 = s2Point.z;
                d = s2Point.y;
                d2 = d8 / d;
                d4 = s2Point.x;
            } else {
                double d9 = -s2Point.y;
                d = s2Point.z;
                d2 = d9 / d;
                d4 = s2Point.x;
            }
            d3 = -d4;
        } else {
            double d10 = -s2Point.x;
            d = s2Point.y;
            d2 = d10 / d;
            d3 = s2Point.z;
        }
        return new R2Vector(d2, d3 / d);
    }

    public static int xyzToFace(S2Point s2Point) {
        int iLargestAbsComponent = s2Point.largestAbsComponent();
        return s2Point.get(iLargestAbsComponent) < 0.0d ? iLargestAbsComponent + 3 : iLargestAbsComponent;
    }
}
