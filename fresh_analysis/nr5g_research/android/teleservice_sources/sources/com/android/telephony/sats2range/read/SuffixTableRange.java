package com.android.telephony.sats2range.read;

import com.android.storage.s2.S2LevelRange;
import com.android.storage.s2.S2Support;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class SuffixTableRange extends S2LevelRange {
    private final int mEntryValue;

    public SuffixTableRange(long j, long j2, int i) {
        super(j, j2);
        this.mEntryValue = i;
    }

    public int getEntryValue() {
        return this.mEntryValue;
    }

    @Override // com.android.storage.s2.S2LevelRange
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && SuffixTableRange.class == obj.getClass() && super.equals(obj)) {
            if (this.mEntryValue == ((SuffixTableRange) obj).mEntryValue) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(Long.valueOf(this.mStartCellId), Long.valueOf(this.mEndCellId), Integer.valueOf(this.mEntryValue));
    }

    public String toString() {
        return "SuffixTableRange{mS2Level=" + this.mS2Level + ", mStartCellId=" + S2Support.cellIdToString(this.mStartCellId) + ", mEndCellId=" + S2Support.cellIdToString(this.mEndCellId) + ", mEntryValue=" + this.mEntryValue + '}';
    }
}
