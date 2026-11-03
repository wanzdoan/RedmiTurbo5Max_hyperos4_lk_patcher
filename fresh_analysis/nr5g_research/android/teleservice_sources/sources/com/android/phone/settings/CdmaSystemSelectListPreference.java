package com.android.phone.settings;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.custompreferences.CustomListPreference;

/* JADX INFO: loaded from: classes.dex */
public class CdmaSystemSelectListPreference extends CustomListPreference {
    private MyHandler mHandler;
    private Phone mPhone;

    public CdmaSystemSelectListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHandler = new MyHandler();
        this.mPhone = MiuiPhoneUtils.getPhone(0);
        this.mHandler = new MyHandler();
    }

    public CdmaSystemSelectListPreference(Context context) {
        super(context);
        this.mHandler = new MyHandler();
    }

    @Override // com.android.phone.custompreferences.CustomListPreference
    protected void showDialog(Bundle bundle) {
        if (Boolean.parseBoolean(SystemProperties.get("ril.cdma.inecmmode"))) {
            return;
        }
        super.showDialog(bundle);
    }

    @Override // com.android.phone.custompreferences.CustomListPreference
    protected void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        if (z && getValue() != null) {
            int iIntValue = Integer.valueOf(getValue()).intValue();
            if (iIntValue != Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "roaming_settings", 0)) {
                Settings.Global.putInt(this.mPhone.getContext().getContentResolver(), "roaming_settings", iIntValue);
                return;
            }
            return;
        }
        Log.d("CdmaRoamingListPreference", String.format("onDialogClosed: positiveResult=%b value=%s -- do nothing", Boolean.valueOf(z), getValue()));
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                handleQueryCdmaRoamingPreference(message);
            } else {
                if (i != 1) {
                    return;
                }
                handleSetCdmaRoamingPreference(message);
            }
        }

        private void handleQueryCdmaRoamingPreference(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception == null) {
                int i = ((int[]) asyncResult.result)[0];
                int i2 = Settings.Global.getInt(CdmaSystemSelectListPreference.this.mPhone.getContext().getContentResolver(), "roaming_settings", 0);
                if (i == 0 || i == 2) {
                    if (i != i2) {
                        Settings.Global.putInt(CdmaSystemSelectListPreference.this.mPhone.getContext().getContentResolver(), "roaming_settings", i);
                    }
                    CdmaSystemSelectListPreference.this.setValue(Integer.toString(i));
                    return;
                }
                resetCdmaRoamingModeToDefault();
            }
        }

        private void handleSetCdmaRoamingPreference(Message message) {
            if (((AsyncResult) message.obj).exception != null || CdmaSystemSelectListPreference.this.getValue() == null) {
                return;
            }
            Settings.Global.putInt(CdmaSystemSelectListPreference.this.mPhone.getContext().getContentResolver(), "roaming_settings", Integer.valueOf(CdmaSystemSelectListPreference.this.getValue()).intValue());
        }

        private void resetCdmaRoamingModeToDefault() {
            CdmaSystemSelectListPreference.this.setValue(Integer.toString(0));
            Settings.Global.putInt(CdmaSystemSelectListPreference.this.mPhone.getContext().getContentResolver(), "roaming_settings", 0);
        }
    }
}
