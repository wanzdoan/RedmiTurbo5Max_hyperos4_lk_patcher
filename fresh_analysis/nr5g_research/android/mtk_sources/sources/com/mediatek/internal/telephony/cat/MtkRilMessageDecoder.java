package com.mediatek.internal.telephony.cat;

import android.content.Context;
import android.os.Handler;
import com.android.internal.telephony.cat.RilMessage;
import com.android.internal.telephony.cat.RilMessageDecoder;
import com.android.internal.telephony.uicc.IccFileHandler;

/* JADX INFO: loaded from: classes.dex */
public class MtkRilMessageDecoder extends RilMessageDecoder {
    private int mSlotId;

    public MtkRilMessageDecoder(Handler caller, IccFileHandler fh, Context context, int slotId) {
        super(caller, fh, context);
        this.mSlotId = slotId;
        MtkCatLog.d(this, "mCaller is " + this.mCaller.getClass().getName());
    }

    public MtkRilMessageDecoder() {
    }

    protected void sendSenssionEndWhenException(RilMessage mCurrentRilMessage) {
        mCurrentRilMessage.mId = 1;
        sendCmdForExecution(mCurrentRilMessage);
    }

    protected boolean needResetAllInstance() {
        if (mInstance == null) {
            return false;
        }
        if (mInstance[this.mSlotId] != null) {
            mInstance[this.mSlotId] = null;
        }
        int i = 0;
        while (i < mSimCount && mInstance[i] == null) {
            i++;
        }
        if (i != mSimCount) {
            return false;
        }
        mInstance = null;
        return true;
    }
}
