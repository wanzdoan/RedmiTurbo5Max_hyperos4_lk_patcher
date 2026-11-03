package com.android.phone;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.utils.MiuiThreadUtil;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;

/* JADX INFO: loaded from: classes.dex */
public class MtkModemBaseModelInitializer {
    private static MtkModemBaseModelInitializer mInstance;
    private boolean mIsInit = false;
    private final Handler mWorkHandler;

    public static MtkModemBaseModelInitializer make() {
        if (mInstance == null) {
            mInstance = new MtkModemBaseModelInitializer();
        }
        return mInstance;
    }

    private MtkModemBaseModelInitializer() {
        Handler handler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.MtkModemBaseModelInitializer.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    MtkModemBaseModelInitializer.this.sendInitCmd();
                } else {
                    if (i != 2) {
                        return;
                    }
                    MtkModemBaseModelInitializer.this.mIsInit = false;
                    removeMessages(1);
                    sendEmptyMessageDelayed(1, MirrorDesktopHelper.TIMEOUT_MILLIS);
                }
            }
        };
        this.mWorkHandler = handler;
        log("init");
        handler.sendEmptyMessageDelayed(1, MirrorDesktopHelper.TIMEOUT_MILLIS);
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone != null) {
                phone.mCi.registerForModemReset(this.mWorkHandler, 2, (Object) null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendInitCmd() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        if (activeDataPhone == null) {
            log("sendInitCmd: dataPhone is null, skip");
            return;
        }
        if (this.mIsInit) {
            return;
        }
        this.mIsInit = true;
        FiveGManagerBase.getInstance().sendAtCommand(activeDataPhone.getPhoneId(), new String[]{"AT+EDMFAPP=20006,400,1,0", "+EDMFAPP"}, (Message) null);
        log("sendInitCmd: AT+EDMFAPP=20006,400,1,0 phoneId=" + activeDataPhone.getPhoneId());
    }

    private void log(String str) {
        Log.d("MtkModemBaseModelInitializer", str);
    }
}
