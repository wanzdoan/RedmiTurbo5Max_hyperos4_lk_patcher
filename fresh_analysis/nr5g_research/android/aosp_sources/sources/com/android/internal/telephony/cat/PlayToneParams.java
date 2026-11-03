package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class PlayToneParams extends CommandParams {
    ToneSettings mSettings;
    TextMessage mTextMsg;

    public PlayToneParams(CommandDetails commandDetails, TextMessage textMessage, Tone tone, Duration duration, boolean z) {
        super(commandDetails);
        this.mTextMsg = textMessage;
        this.mSettings = new ToneSettings(duration, tone, z);
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
