package com.android.telephony.sats2range.read;

/* JADX INFO: loaded from: classes.dex */
final class UnpopulatedSuffixTableBlock implements SuffixTableBlock.SuffixTableBlockDelegate {
    private final int mPrefix;

    @Override // com.android.telephony.sats2range.read.SuffixTableBlock.SuffixTableBlockDelegate
    public SuffixTableBlock.Entry findEntryByCellId(long j) {
        return null;
    }

    UnpopulatedSuffixTableBlock(int i) {
        this.mPrefix = i;
    }

    @Override // com.android.telephony.sats2range.read.SuffixTableBlock.SuffixTableBlockDelegate
    public int getPrefix() {
        return this.mPrefix;
    }
}
