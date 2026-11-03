package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class CallSetupParams extends CommandParams {
    public String mAddress;
    public TextMessage mCallMsg;
    public TextMessage mConfirmMsg;
    public Duration mDuration;

    public CallSetupParams(CommandDetails commandDetails, TextMessage textMessage, TextMessage textMessage2) {
        super(commandDetails);
        this.mConfirmMsg = textMessage;
        this.mCallMsg = textMessage2;
    }

    public CallSetupParams(CommandDetails commandDetails, TextMessage textMessage, TextMessage textMessage2, String str, Duration duration) {
        super(commandDetails);
        this.mConfirmMsg = textMessage;
        this.mCallMsg = textMessage2;
        this.mAddress = str;
        this.mDuration = duration;
    }

    @Override // com.android.internal.telephony.cat.CommandParams
    boolean setIcon(Bitmap bitmap) {
        if (bitmap == null) {
            return false;
        }
        TextMessage textMessage = this.mConfirmMsg;
        if (textMessage != null && textMessage.icon == null) {
            textMessage.icon = bitmap;
            return true;
        }
        TextMessage textMessage2 = this.mCallMsg;
        if (textMessage2 == null || textMessage2.icon != null) {
            return false;
        }
        textMessage2.icon = bitmap;
        return true;
    }
}
