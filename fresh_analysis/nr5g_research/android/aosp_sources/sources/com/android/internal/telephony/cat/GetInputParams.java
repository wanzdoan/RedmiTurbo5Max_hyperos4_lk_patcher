package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class GetInputParams extends CommandParams {
    Input mInput;

    public GetInputParams(CommandDetails commandDetails, Input input) {
        super(commandDetails);
        this.mInput = input;
    }

    @Override // com.android.internal.telephony.cat.CommandParams
    boolean setIcon(Bitmap bitmap) {
        Input input;
        if (bitmap == null || (input = this.mInput) == null) {
            return true;
        }
        input.icon = bitmap;
        return true;
    }
}
