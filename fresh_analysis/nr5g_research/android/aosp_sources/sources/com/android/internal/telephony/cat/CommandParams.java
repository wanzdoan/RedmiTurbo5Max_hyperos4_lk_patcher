package com.android.internal.telephony.cat;

import android.graphics.Bitmap;

/* JADX INFO: loaded from: classes.dex */
public class CommandParams {
    public CommandDetails mCmdDet;
    public boolean mLoadIconFailed = false;

    boolean setIcon(Bitmap bitmap) {
        return true;
    }

    public CommandParams(CommandDetails commandDetails) {
        this.mCmdDet = commandDetails;
    }

    public AppInterface.CommandType getCommandType() {
        return AppInterface.CommandType.fromInt(this.mCmdDet.typeOfCommand);
    }

    public String toString() {
        return this.mCmdDet.toString();
    }
}
