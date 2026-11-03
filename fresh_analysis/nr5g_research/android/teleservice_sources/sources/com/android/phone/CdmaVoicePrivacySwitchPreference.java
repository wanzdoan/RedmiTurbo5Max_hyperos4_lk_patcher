package com.android.phone;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class CdmaVoicePrivacySwitchPreference extends SwitchPreference {
    private final boolean DBG;
    private MyHandler mHandler;
    Phone phone;

    public CdmaVoicePrivacySwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.DBG = true;
        this.mHandler = new MyHandler();
    }

    public CdmaVoicePrivacySwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, android.R.attr.switchPreferenceStyle);
    }

    public CdmaVoicePrivacySwitchPreference(Context context) {
        this(context, null);
    }

    @Override // android.preference.TwoStatePreference, android.preference.Preference
    protected void onClick() {
        super.onClick();
        this.phone.enableEnhancedVoicePrivacy(isChecked(), this.mHandler.obtainMessage(1));
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
            if (asyncResult.exception != null) {
                Log.d("CdmaVoicePrivacySwitchPreference", "handleGetVPResponse: ar.exception=" + asyncResult.exception);
                CdmaVoicePrivacySwitchPreference.this.setEnabled(false);
                return;
            }
            Log.d("CdmaVoicePrivacySwitchPreference", "handleGetVPResponse: VP state successfully queried.");
            int i = ((int[]) asyncResult.result)[0];
            Log.d("CdmaVoicePrivacySwitchPreference", "Voice privacy value returned:" + i);
            CdmaVoicePrivacySwitchPreference.this.setChecked(i != 0);
            Settings.Secure.putInt(CdmaVoicePrivacySwitchPreference.this.getContext().getContentResolver(), "enhanced_voice_privacy_enabled", i);
        }

        private void handleSetVPResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                Log.d("CdmaVoicePrivacySwitchPreference", "handleSetVPResponse: ar.exception=" + asyncResult.exception);
                CdmaVoicePrivacySwitchPreference cdmaVoicePrivacySwitchPreference = CdmaVoicePrivacySwitchPreference.this;
                cdmaVoicePrivacySwitchPreference.setChecked(cdmaVoicePrivacySwitchPreference.isChecked() ^ true);
            }
            Log.d("CdmaVoicePrivacySwitchPreference", "handleSetVPResponse: re get");
            CdmaVoicePrivacySwitchPreference.this.phone.getEnhancedVoicePrivacy(obtainMessage(0));
        }
    }
}
