package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class LaunchBrowserParams extends CommandParams {
    public TextMessage mConfirmMsg;
    public LaunchBrowserMode mMode;
    public String mUrl;

    public LaunchBrowserParams(CommandDetails commandDetails, TextMessage textMessage, String str, LaunchBrowserMode launchBrowserMode) {
        super(commandDetails);
        this.mConfirmMsg = textMessage;
        this.mMode = launchBrowserMode;
        this.mUrl = str;
    }

    @Override // com.android.internal.telephony.cat.CommandParams
    boolean setIcon(Bitmap bitmap) {
        TextMessage textMessage;
        if (bitmap == null || (textMessage = this.mConfirmMsg) == null) {
            return false;
        }
        textMessage.icon = bitmap;
        return true;
    }

    @Override // com.android.internal.telephony.cat.CommandParams
    public String toString() {
        return "TextMessage=" + this.mConfirmMsg + " " + super.toString();
    }
}
