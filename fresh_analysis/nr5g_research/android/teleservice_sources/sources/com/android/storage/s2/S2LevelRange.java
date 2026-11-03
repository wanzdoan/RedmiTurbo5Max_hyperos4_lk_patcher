package com.android.storage.s2;

import java.util.Comparator;

/* JADX INFO: loaded from: classes.dex */
public abstract class S2LevelRange {
    public static Comparator COMPARATOR = new Comparator() { // from class: com.android.storage.s2.S2LevelRange$$ExternalSyntheticLambda0
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            return S2LevelRange.$r8$lambda$0hlfjB2d64AoZSlc5_7SXGpxxZk((S2LevelRange) obj, (S2LevelRange) obj2);
        }
    };
    protected final long mEndCellId;
    protected final long mEndCellIdNumeric;
    protected final int mS2Level;
    protected final long mStartCellId;
    protected final long mStartCellIdNumeric;

    public static /* synthetic */ int $r8$lambda$0hlfjB2d64AoZSlc5_7SXGpxxZk(S2LevelRange s2LevelRange, S2LevelRange s2LevelRange2) {
        long j = s2LevelRange2.mStartCellIdNumeric - s2LevelRange.mStartCellIdNumeric;
        if (j < 0) {
            return 1;
        }
        if (j > 0) {
            return -1;
        }
        long j2 = s2LevelRange2.mEndCellIdNumeric - s2LevelRange.mEndCellIdNumeric;
        if (j2 < 0) {
            return 1;
        }
        return j2 > 0 ? -1 : 0;
    }

    public S2LevelRange(long j, long j2) {
        S2Support.validateCellId(j);
        S2Support.validateCellId(j2);
        int s2Level = S2Support.getS2Level(j);
        int s2Level2 = S2Support.getS2Level(j2);
        if (s2Level != s2Level2) {
            throw new IllegalArgumentException("startCellId " + S2Support.cellIdToString(j) + " level=" + s2Level + " != endCellId " + S2Support.cellIdToString(j2) + " level=" + s2Level2);
        }
        if (j == j2) {
            throw new IllegalArgumentException("A range cannot be zero length, startCellId=" + j + ", endCellId=" + j2);
        }
        long jAsUnsignedNumeric = S2CellOrdering.asUnsignedNumeric(j);
        long jAsUnsignedNumeric2 = S2CellOrdering.asUnsignedNumeric(j2);
        this.mStartCellId = j;
        this.mStartCellIdNumeric = jAsUnsignedNumeric;
        this.mEndCellId = j2;
        this.mEndCellIdNumeric = jAsUnsignedNumeric2;
        this.mS2Level = s2Level;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            S2LevelRange s2LevelRange = (S2LevelRange) obj;
            if (this.mStartCellId == s2LevelRange.mStartCellId && this.mEndCellId == s2LevelRange.mEndCellId) {
                return true;
            }
        }
        return false;
    }
}
