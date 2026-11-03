package com.android.phone.vvm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.ArraySet;
import com.ot.pubsub.g.f;
import java.util.Collections;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class CarrierVvmPackageInstalledReceiver extends BroadcastReceiver {
    public void register(Context context) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme(f.a.e);
        context.registerReceiver(this, intentFilter);
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String schemeSpecificPart;
        if (intent.getData() == null || (schemeSpecificPart = intent.getData().getSchemeSpecificPart()) == null) {
            return;
        }
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(TelecomManager.class);
        String systemDialerPackage = telecomManager.getSystemDialerPackage();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        for (PhoneAccountHandle phoneAccountHandle : telecomManager.getCallCapablePhoneAccounts()) {
            TelephonyManager telephonyManagerCreateForPhoneAccountHandle = telephonyManager.createForPhoneAccountHandle(phoneAccountHandle);
            if (telephonyManagerCreateForPhoneAccountHandle == null) {
                VvmLog.e("VvmPkgInstalledRcvr", "carrierVvmPkgAdded: cannot create TelephonyManager from " + phoneAccountHandle);
            } else if (!getCarrierVvmPackages(telephonyManager).contains(schemeSpecificPart)) {
                VvmLog.w("VvmPkgInstalledRcvr", "carrierVvmPkgAdded: carrier vvm packages doesn't contain ".concat(schemeSpecificPart));
            } else {
                VvmLog.i("VvmPkgInstalledRcvr", "carrierVvmPkgAdded: Carrier VVM app " + schemeSpecificPart + " installed");
                String visualVoicemailPackageName = telephonyManagerCreateForPhoneAccountHandle.getVisualVoicemailPackageName();
                if (!TextUtils.equals(visualVoicemailPackageName, systemDialerPackage)) {
                    VvmLog.i("VvmPkgInstalledRcvr", "carrierVvmPkgAdded: non system dialer " + visualVoicemailPackageName + " ignored");
                } else {
                    VvmLog.i("VvmPkgInstalledRcvr", "carrierVvmPkgAdded: sending vvm package installed broadcast to " + visualVoicemailPackageName);
                    Intent intent2 = new Intent("com.android.internal.telephony.CARRIER_VVM_PACKAGE_INSTALLED");
                    intent2.putExtra("android.intent.extra.PACKAGE_NAME", schemeSpecificPart);
                    intent2.setPackage(visualVoicemailPackageName);
                    context.sendBroadcastAsUser(intent2, UserHandle.ALL);
                }
            }
        }
    }

    private static Set getCarrierVvmPackages(TelephonyManager telephonyManager) {
        ArraySet arraySet = new ArraySet();
        PersistableBundle carrierConfig = telephonyManager.getCarrierConfig();
        String string = carrierConfig.getString("carrier_vvm_package_name_string");
        if (!TextUtils.isEmpty(string)) {
            arraySet.add(string);
        }
        String[] stringArray = carrierConfig.getStringArray("carrier_vvm_package_name_string_array");
        if (stringArray != null) {
            Collections.addAll(arraySet, stringArray);
        }
        return arraySet;
    }
}
