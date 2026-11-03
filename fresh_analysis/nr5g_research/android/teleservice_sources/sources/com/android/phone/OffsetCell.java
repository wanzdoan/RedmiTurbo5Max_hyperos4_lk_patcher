package com.android.phone;

import android.text.TextUtils;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class OffsetCell {
    public int freq;
    public String globalCellId;
    public int offset;
    public long optimizationTime;
    public int pci;
    public String plmn;
    public int rat;
    public int score = 0;
    public boolean isResidentCell = false;

    public OffsetCell(int i, int i2, int i3, String str, int i4, String str2) {
        this.freq = i;
        this.pci = i2;
        this.offset = i3;
        this.plmn = str;
        this.rat = i4;
        this.globalCellId = str2;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int i) {
        this.score = i;
    }

    public boolean isResidentCell() {
        return this.isResidentCell;
    }

    public void setResidentCell(boolean z) {
        this.isResidentCell = z;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        OffsetCell offsetCell = (OffsetCell) obj;
        if (TextUtils.isEmpty(this.globalCellId)) {
            return false;
        }
        return this.globalCellId.equals(offsetCell.globalCellId);
    }

    public int hashCode() {
        return Objects.hash(this.globalCellId);
    }
}
