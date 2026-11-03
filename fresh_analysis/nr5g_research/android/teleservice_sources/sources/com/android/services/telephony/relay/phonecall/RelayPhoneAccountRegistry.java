package com.android.services.telephony.relay.phonecall;

import android.content.ComponentName;
import android.content.Context;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import gov.nist.javax.sip.address.GenericURI;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class RelayPhoneAccountRegistry {
    public static final Context CONTEXT;
    public static final PhoneAccountHandle PHONE_ACCOUNT_HANDLE;
    private static final ComponentName RELAY_CONNECTION_SERVICE_COMPONENT;
    private static final String TAG = "RelayPhoneAccountRegistry";
    private boolean mIsPhoneAccountEnabled;
    private PhoneAccount mPhoneAccount;

    static {
        Context context = RelayUtils.System.getContext();
        CONTEXT = context;
        ComponentName componentName = new ComponentName(context.getPackageName(), RelayConnectionService.class.getName());
        RELAY_CONNECTION_SERVICE_COMPONENT = componentName;
        PHONE_ACCOUNT_HANDLE = new PhoneAccountHandle(componentName, "RL-PHAh");
    }

    RelayPhoneAccountRegistry() {
    }

    public ComponentName getRelayConnectionServiceName() {
        return RELAY_CONNECTION_SERVICE_COMPONENT;
    }

    public PhoneAccount getPhoneAccount() {
        return this.mPhoneAccount;
    }

    public void createRelayPhoneAccount() {
        if (RelayUtils.Relay.isRelaySide() || RelayUtils.Relay.supportDualPhoneRelayCallFeature(CONTEXT)) {
            PhoneAccountHandle phoneAccountHandle = PHONE_ACCOUNT_HANDLE;
            new ArrayList().add(GenericURI.TEL);
            this.mPhoneAccount = new PhoneAccount.Builder(phoneAccountHandle, "RL-PHAh").setShortDescription("Relay account").build();
            RelayLog.logi(TAG, "createRelayPhoneAccount phoneAccount:" + this.mPhoneAccount);
        }
    }

    public void enablePhoneAccount(boolean z) {
        if ((!RelayUtils.Relay.isRelaySide() && !RelayUtils.Relay.supportDualPhoneRelayCallFeature(CONTEXT)) || z == this.mIsPhoneAccountEnabled || this.mPhoneAccount == null) {
            return;
        }
        RelayLog.logi(TAG, "enablePhoneAccount enabled:" + z + " mIsPhoneAccountEnabled:" + this.mIsPhoneAccountEnabled);
        TelecomManager telecomManagerFrom = TelecomManager.from(CONTEXT);
        telecomManagerFrom.registerPhoneAccount(this.mPhoneAccount);
        telecomManagerFrom.enablePhoneAccount(PHONE_ACCOUNT_HANDLE, z);
        this.mIsPhoneAccountEnabled = z;
    }
}
