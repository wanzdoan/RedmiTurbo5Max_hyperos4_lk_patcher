package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class DisplayTextParams extends CommandParams {
    public TextMessage mTextMsg;

    public DisplayTextParams(CommandDetails commandDetails, TextMessage textMessage) {
        super(commandDetails);
        this.mTextMsg = textMessage;
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

    @Override // com.android.internal.telephony.cat.CommandParams
    public String toString() {
        return "TextMessage=" + this.mTextMsg + " " + super.toString();
    }
}
