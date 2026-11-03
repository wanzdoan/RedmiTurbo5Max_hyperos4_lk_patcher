package com.android.phone.roaming;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.util.ArrayUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.roaming.utils.RoamingUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class CallCapabilityManager {
    private static final Context sContext = PhoneGlobals.getInstance();
    private static volatile CallCapabilityManager sInstance;
    private final HashMap mLastNetworkNumeric = new HashMap();
    private final HashMap mRoamingVtDisabledState = new HashMap();
    private final Handler mHandler = new CallCapabilityHandler();

    private CallCapabilityManager() {
    }

    public static CallCapabilityManager getInstance() {
        if (sInstance == null) {
            synchronized (CallCapabilityManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new CallCapabilityManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public void processServiceState(int i, ServiceState serviceState) {
        if (SubscriptionManager.isValidPhoneId(i) && serviceState != null && serviceState.getState() == 0) {
            String operatorNumeric = serviceState.getOperatorNumeric();
            if (TextUtils.equals((CharSequence) this.mLastNetworkNumeric.get(Integer.valueOf(i)), operatorNumeric)) {
                return;
            }
            int subId = RoamingUtils.getSubId(i);
            if (this.mRoamingVtDisabledState.get(Integer.valueOf(subId)) == null) {
                Log.d("CallCapabilityManager", "The stored state has not been init yet！");
                return;
            }
            Log.d("CallCapabilityManager", "Updating vt capabilities for sim: " + i);
            this.mLastNetworkNumeric.put(Integer.valueOf(i), operatorNumeric);
            if (serviceState.getVoiceRoaming() && shouldDisableVt(operatorNumeric, i)) {
                if (((Boolean) this.mRoamingVtDisabledState.get(Integer.valueOf(subId))).booleanValue() || this.mHandler.hasMessages(i)) {
                    return;
                }
                ImsManager imsManager = ImsManager.getInstance(sContext, i);
                boolean zIsVtEnabledByUser = imsManager.isVtEnabledByUser();
                boolean zIsVtEnabledByPlatform = imsManager.isVtEnabledByPlatform();
                if (zIsVtEnabledByPlatform && zIsVtEnabledByUser) {
                    Message messageObtainMessage = this.mHandler.obtainMessage(i);
                    messageObtainMessage.arg1 = i;
                    messageObtainMessage.obj = operatorNumeric;
                    this.mHandler.sendMessageDelayed(messageObtainMessage, 120000L);
                    Log.d("CallCapabilityManager", "Delay to check the voice roaming status.");
                    return;
                }
                Log.d("CallCapabilityManager", "Do nothing when operatorNumeric is :" + operatorNumeric + ",isVtEnabledByPlatform: " + zIsVtEnabledByPlatform + ", isVtEnabledByUser: " + zIsVtEnabledByUser);
                return;
            }
            if (this.mHandler.hasMessages(i)) {
                this.mHandler.removeMessages(i);
            }
            if (((Boolean) this.mRoamingVtDisabledState.get(Integer.valueOf(subId))).booleanValue()) {
                setVtCapability(i, true, operatorNumeric);
            }
        }
    }

    private boolean shouldDisableVt(String str, int i) {
        String[] stringArray = sContext.getResources().getStringArray(R.array.disable_vt_mcc_mnc);
        if (ArrayUtils.isEmpty(stringArray) || TextUtils.isEmpty(str)) {
            Log.d("CallCapabilityManager", "numeric array is empty");
            return false;
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        String strSubstring = null;
        String operatorNumeric = phone == null ? null : phone.getOperatorNumeric();
        if (operatorNumeric != null && operatorNumeric.length() >= 3) {
            strSubstring = operatorNumeric.substring(0, 3);
        }
        if ("460".equals(strSubstring)) {
            return true;
        }
        for (String str2 : stringArray) {
            if (!TextUtils.isEmpty(str2) && str.startsWith(str2)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVtCapability(int i, boolean z, String str) {
        int subId = RoamingUtils.getSubId(i);
        ImsManager imsManager = ImsManager.getInstance(sContext, i);
        if (z) {
            if (imsManager.isVtEnabledByPlatform() && !imsManager.isVtEnabledByUser() && imsManager.isEnhanced4gLteModeSettingEnabledByUser()) {
                updateVtCapability(imsManager, subId, true, str);
                Log.i("CallCapabilityManager", "Enable local video capability for sim: " + i);
                return;
            }
            return;
        }
        if (imsManager.isVtEnabledByPlatform() && imsManager.isVtEnabledByUser()) {
            updateVtCapability(imsManager, subId, false, str);
            Log.i("CallCapabilityManager", "Disable local video capability for sim: " + i);
        }
    }

    private void updateVtCapability(ImsManager imsManager, int i, boolean z, String str) {
        imsManager.setVtSetting(z);
        this.mRoamingVtDisabledState.put(Integer.valueOf(i), Boolean.valueOf(!z));
        RoamingUtils.saveRoamingVtDisabledState(i, !z);
        recordUpdateVtAction(str, !z);
    }

    public void onIccStatusChanged(int i, String str) {
        if ("LOADED".equals(str)) {
            int subId = RoamingUtils.getSubId(i);
            this.mLastNetworkNumeric.remove(Integer.valueOf(i));
            this.mRoamingVtDisabledState.put(Integer.valueOf(subId), RoamingUtils.getRoamingVtDisabledState(subId));
            Phone phone = MiuiPhoneUtils.getPhone(i);
            if (phone != null) {
                processServiceState(i, phone.getServiceState());
            }
        }
    }

    private void recordUpdateVtAction(String str, boolean z) {
        HashMap map = new HashMap();
        map.put("operactor", str);
        map.put("disable_vt", Boolean.valueOf(z));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_update_roaming_call_capability", map);
    }

    class CallCapabilityHandler extends Handler {
        public CallCapabilityHandler() {
            super(Looper.getMainLooper());
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d("CallCapabilityManager", "handleMessage: " + message.what);
            int i = message.what;
            if (i == 0 || i == 1) {
                int i2 = message.arg1;
                Phone phone = MiuiPhoneUtils.getPhone(i2);
                Object obj = message.obj;
                String str = obj == null ? null : (String) obj;
                if (phone == null || !phone.getServiceState().getVoiceRoaming()) {
                    return;
                }
                CallCapabilityManager.this.setVtCapability(i2, false, str);
            }
        }
    }
}
