package com.android.phone;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import java.util.ArrayList;
import java.util.List;
import miui.telephony.FeatureConfiguration;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;

/* JADX INFO: loaded from: classes.dex */
public abstract class SmartCallForwardUtils {
    public static boolean isSmartCallForwardSupported() {
        if (!isSupportSmartCallForwardByDevice()) {
            return false;
        }
        for (Phone phone : PhoneFactory.getPhones()) {
            if ((phone.getPhoneType() == 2 && !MiuiPhoneUtils.useUtForCdmaCallForward(phone)) || MiPhoneAppStub.get().isDataOnlySlot(phone.getContext(), phone)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSupportSmartCallForwardByDevice() {
        return FeatureConfiguration.isFeatureSupported(FeatureConfiguration.FeatureType.FEATURE_SMART_CALL_FORWARD);
    }

    public static ArrayList getAndUpdateOldSimSlotIds(Context context) {
        ArrayList arrayList = new ArrayList();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String string = defaultSharedPreferences.getString("key_telephony_last_stored_iccid", "");
        if (TextUtils.isEmpty(string)) {
            updateLastStoredIccids(null, defaultSharedPreferences);
            return arrayList;
        }
        String[] strArrSplit = string.split(",");
        String[] strArr = new String[MiuiPhoneUtils.PHONE_COUNT];
        List<SubscriptionInfo> availableSubscriptionInfoList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
        for (String str : strArrSplit) {
            for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
                int slotId = subscriptionInfo.getSlotId();
                String iccId = subscriptionInfo.getIccId() == null ? "<null>" : subscriptionInfo.getIccId();
                strArr[slotId] = iccId;
                if (str.equalsIgnoreCase(iccId)) {
                    arrayList.add(Integer.valueOf(slotId));
                }
            }
        }
        if (arrayList.size() != availableSubscriptionInfoList.size()) {
            updateLastStoredIccids(strArr, defaultSharedPreferences);
        }
        return arrayList;
    }

    public static void updateLastStoredIccids(String[] strArr, SharedPreferences sharedPreferences) {
        if (strArr == null) {
            List<SubscriptionInfo> availableSubscriptionInfoList = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList();
            String[] strArr2 = new String[MiuiPhoneUtils.PHONE_COUNT];
            for (int i = 0; i < availableSubscriptionInfoList.size(); i++) {
                for (SubscriptionInfo subscriptionInfo : availableSubscriptionInfoList) {
                    strArr2[subscriptionInfo.getSlotId()] = subscriptionInfo.getIccId() == null ? "<null>" : subscriptionInfo.getIccId();
                }
            }
            strArr = strArr2;
        }
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (i2 != 0) {
                sb.append(',');
            }
            String str = strArr[i2];
            if (str == null) {
                str = "<null>";
            }
            sb.append(str);
        }
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putString("key_telephony_last_stored_iccid", sb.toString());
        editorEdit.apply();
        Log.d("SmartCallForwardUtils", "update last iccid since sim changed.");
    }

    public static boolean isSmartCallForwardedCall(String str) {
        if (!isSupportSmartCallForwardByDevice() || !MiuiSettings.Telephony.isSmartCallForwardEnabled(PhoneGlobals.getInstance().getContentResolver())) {
            return false;
        }
        String string = PreferenceManager.getDefaultSharedPreferences(PhoneGlobals.getInstance()).getString("key_telephony_last_stored_number", "");
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(str)) {
            String[] strArrSplit = string.split(",");
            for (int i = 0; i < strArrSplit.length; i++) {
                if (!"<null>".equals(strArrSplit[i]) && strArrSplit[i].equals(str)) {
                    Log.d("SmartCallForwardUtils", "smart forward call.");
                    return true;
                }
            }
        }
        return false;
    }
}
