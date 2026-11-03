package com.android.phone.settings;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;

/* JADX INFO: loaded from: classes.dex */
public class CdmaVoicePrivacyCheckBoxPreference extends CheckBoxTitleIconPreference {
    private boolean DBG;
    private MyHandler mHandler;
    Phone phone;

    public CdmaVoicePrivacyCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
    }

    public void init(Phone phone) {
        this.phone = phone;
        phone.getEnhancedVoicePrivacy(this.mHandler.obtainMessage(0));
    }

    public CdmaVoicePrivacyCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
    }

    public CdmaVoicePrivacyCheckBoxPreference(Context context) {
        super(context);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
    }

    @Override // androidx.preference.TwoStatePreference, androidx.preference.Preference
    protected void onClick() {
        super.onClick();
        Phone phone = this.phone;
        if (phone != null) {
            phone.enableEnhancedVoicePrivacy(isChecked(), this.mHandler.obtainMessage(1));
        }
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                handleGetVPResponse(message);
            } else {
                if (i != 1) {
                    return;
                }
                handleSetVPResponse(message);
            }
        }

        private void handleGetVPResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            Throwable th = asyncResult.exception;
            CdmaVoicePrivacyCheckBoxPreference cdmaVoicePrivacyCheckBoxPreference = CdmaVoicePrivacyCheckBoxPreference.this;
            if (th != null) {
                if (cdmaVoicePrivacyCheckBoxPreference.DBG) {
                    Log.d("CdmaVoicePrivacyCheckBoxPreference", "handleGetVPResponse: ar.exception=" + asyncResult.exception);
                }
                CdmaVoicePrivacyCheckBoxPreference.this.setEnabled(false);
                return;
            }
            if (cdmaVoicePrivacyCheckBoxPreference.DBG) {
                Log.d("CdmaVoicePrivacyCheckBoxPreference", "handleGetVPResponse: VP state successfully queried.");
            }
            int i = ((int[]) asyncResult.result)[0];
            CdmaVoicePrivacyCheckBoxPreference.this.setChecked(i != 0);
            Settings.Secure.putInt(CdmaVoicePrivacyCheckBoxPreference.this.getContext().getContentResolver(), "enhanced_voice_privacy_enabled", i);
        }

        private void handleSetVPResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null && CdmaVoicePrivacyCheckBoxPreference.this.DBG) {
                Log.d("CdmaVoicePrivacyCheckBoxPreference", "handleSetVPResponse: ar.exception=" + asyncResult.exception);
            }
            if (CdmaVoicePrivacyCheckBoxPreference.this.DBG) {
                Log.d("CdmaVoicePrivacyCheckBoxPreference", "handleSetVPResponse: re get");
            }
            CdmaVoicePrivacyCheckBoxPreference.this.phone.getEnhancedVoicePrivacy(obtainMessage(0));
        }
    }
}
