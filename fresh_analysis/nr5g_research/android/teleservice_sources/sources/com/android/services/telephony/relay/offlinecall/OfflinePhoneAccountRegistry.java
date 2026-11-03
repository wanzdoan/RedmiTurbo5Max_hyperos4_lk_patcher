package com.android.services.telephony.relay.offlinecall;

import android.content.ComponentName;
import android.content.Context;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import gov.nist.javax.sip.address.GenericURI;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class OfflinePhoneAccountRegistry {
    public static final Context CONTEXT;
    private static final ComponentName OFFLINE_CONNECTION_SERVICE_COMPONENT;
    private static final String OFFLINE_PHONE_ACCOUNT_HANDLE_NAME = "OFF_PHAh";
    public static final PhoneAccountHandle PHONE_ACCOUNT_HANDLE;
    private static final String TAG = "OfflinePhoneAccountRegistry";
    private boolean mIsPhoneAccountEnabled;
    private final PhoneAccount mPhoneAccount = createOfflinePhoneAccount();

    static {
        Context context = OfflineUtils.System.getContext();
        CONTEXT = context;
        ComponentName componentName = new ComponentName(context.getPackageName(), OfflineConnectionService.class.getName());
        OFFLINE_CONNECTION_SERVICE_COMPONENT = componentName;
        PHONE_ACCOUNT_HANDLE = new PhoneAccountHandle(componentName, OFFLINE_PHONE_ACCOUNT_HANDLE_NAME);
    }

    OfflinePhoneAccountRegistry() {
    }

    public ComponentName getOfflineConnectionServiceName() {
        return OFFLINE_CONNECTION_SERVICE_COMPONENT;
    }

    public PhoneAccount getPhoneAccount() {
        return this.mPhoneAccount;
    }

    private PhoneAccount createOfflinePhoneAccount() {
        PhoneAccountHandle phoneAccountHandle = PHONE_ACCOUNT_HANDLE;
        new ArrayList().add(GenericURI.TEL);
        PhoneAccount phoneAccountBuild = new PhoneAccount.Builder(phoneAccountHandle, OFFLINE_PHONE_ACCOUNT_HANDLE_NAME).setShortDescription("Offline account").build();
        OfflineCallLog.logi(TAG, "createOfflinePhoneAccount phoneAccount:" + phoneAccountBuild);
        return phoneAccountBuild;
    }

    public void enablePhoneAccount(boolean z) {
        OfflineCallLog.logi(TAG, "enablePhoneAccount enabled:" + z + " mIsPhoneAccountEnabled:" + this.mIsPhoneAccountEnabled);
        TelecomManager telecomManagerFrom = TelecomManager.from(CONTEXT);
        telecomManagerFrom.registerPhoneAccount(this.mPhoneAccount);
        telecomManagerFrom.enablePhoneAccount(PHONE_ACCOUNT_HANDLE, z);
        this.mIsPhoneAccountEnabled = z;
    }
}
