package com.android.phone.settings;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.custompreferences.CustomListPreference;

/* JADX INFO: loaded from: classes.dex */
public class CdmaSubscriptionListPreference extends CustomListPreference {
    private CdmaSubscriptionButtonHandler mHandler;
    private Phone mPhone;

    public CdmaSubscriptionListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPhone = PhoneFactory.getDefaultPhone();
        this.mHandler = new CdmaSubscriptionButtonHandler();
        setCurrentCdmaSubscriptionModeValue();
    }

    private void setCurrentCdmaSubscriptionModeValue() {
        setValue(Integer.toString(Settings.Global.getInt(this.mPhone.getContext().getContentResolver(), "subscription_mode", 1)));
    }

    @Override // com.android.phone.custompreferences.CustomListPreference
    protected void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        if (z) {
            Log.d("CdmaSubscriptionListPreference", "Setting new value " + Integer.valueOf(getValue()).intValue());
        }
    }

    class CdmaSubscriptionButtonHandler extends Handler {
        private CdmaSubscriptionButtonHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 0) {
                return;
            }
            handleSetCdmaSubscriptionMode(message);
        }

        private void handleSetCdmaSubscriptionMode(Message message) {
            CdmaSubscriptionListPreference.this.mPhone = PhoneFactory.getDefaultPhone();
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception == null) {
                Settings.Global.putInt(CdmaSubscriptionListPreference.this.mPhone.getContext().getContentResolver(), "subscription_mode", Integer.valueOf((String) asyncResult.userObj).intValue());
            } else {
                Log.e("CdmaSubscriptionListPreference", "Setting Cdma subscription source failed");
            }
        }
    }
}
