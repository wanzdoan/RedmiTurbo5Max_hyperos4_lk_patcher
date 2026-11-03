package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class BIPClientParams extends CommandParams {
    public boolean mHasAlphaId;
    public TextMessage mTextMsg;

    BIPClientParams(CommandDetails commandDetails, TextMessage textMessage, boolean z) {
        super(commandDetails);
        this.mTextMsg = textMessage;
        this.mHasAlphaId = z;
    }

    @Override // com.android.internal.telephony.cat.CommandParams
    boolean setIcon(Bitmap bitmap) {
        TextMessage textMessage;
        if (bitmap == null || (textMessage = this.mTextMsg) == null) {
            return false;
        }
        textMessage.icon = bitmap;
        return true;
    }
}
