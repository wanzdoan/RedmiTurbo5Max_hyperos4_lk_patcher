package com.google.common.geometry;

import com.google.common.base.Preconditions;

/* JADX INFO: loaded from: classes.dex */
public abstract class S2 {
    public static final double M_SQRT2 = Math.sqrt(2.0d);
    private static final int[] POS_TO_ORIENTATION = {1, 0, 0, 3};
    private static final int[][] POS_TO_IJ = {new int[]{0, 1, 3, 2}, new int[]{0, 2, 3, 1}, new int[]{3, 2, 0, 1}, new int[]{3, 1, 0, 2}};
    private static final int[][] IJ_TO_POS = {new int[]{0, 1, 3, 2}, new int[]{0, 3, 1, 2}, new int[]{2, 3, 1, 0}, new int[]{2, 1, 3, 0}};

    static int exp(double d) {
        if (d == 0.0d) {
            return 0;
        }
        return ((int) ((Double.doubleToLongBits(d) & 9218868437227405312L) >> 52)) - 1022;
    }

    public static int posToOrientation(int i) {
        Preconditions.checkArgument(i >= 0 && i < 4);
        return POS_TO_ORIENTATION[i];
    }

    public static int posToIJ(int i, int i2) {
        boolean z = false;
        Preconditions.checkArgument(i >= 0 && i < 4);
        if (i2 >= 0 && i2 < 4) {
            z = true;
        }
        Preconditions.checkArgument(z);
        return POS_TO_IJ[i][i2];
    }

    public class Metric {
        private final double deriv;
        private final int dim;

        public Metric(int i, double d) {
            this.deriv = d;
            this.dim = i;
        }

        public double deriv() {
            return this.deriv;
        }
    }
}
