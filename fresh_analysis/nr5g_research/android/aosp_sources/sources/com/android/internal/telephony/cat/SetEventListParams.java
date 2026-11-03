package com.android.internal.telephony.cat;

/* JADX INFO: loaded from: classes.dex */
public class SetEventListParams extends CommandParams {
    public int[] mEventInfo;

    public SetEventListParams(CommandDetails commandDetails, int[] iArr) {
        super(commandDetails);
        this.mEventInfo = iArr;
    }
}
