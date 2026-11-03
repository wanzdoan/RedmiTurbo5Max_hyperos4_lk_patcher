package com.android.phone.beidou;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.SubscriptionInfo;

/* JADX INFO: loaded from: classes.dex */
public abstract class BdSmsUtils {
    public static String getPhoneNumber(Context context, int i) {
        int subId;
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(i);
        if (availableSubscriptionInfoBySlot == null) {
            return "";
        }
        String string = availableSubscriptionInfoBySlot.getDisplayNumber().toString();
        Log.d("BdSmsUtils", "getPhoneNumber: simInfo phoneNumber is null = " + TextUtils.isEmpty(string));
        String strStripSeparatorsAndCountryCode = PhoneNumberUtils.stripSeparatorsAndCountryCode(string);
        Log.d("BdSmsUtils", "getPhoneNumber: simInfo stripSeparatorsAndCountryCode phoneNumber is null = " + TextUtils.isEmpty(strStripSeparatorsAndCountryCode));
        if (!TextUtils.isEmpty(strStripSeparatorsAndCountryCode)) {
            return strStripSeparatorsAndCountryCode;
        }
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null || (subId = phone.getSubId()) == -1) {
            return "";
        }
        String phoneNumber = ((SubscriptionManager) context.getSystemService(SubscriptionManager.class)).getPhoneNumber(subId);
        Log.d("BdSmsUtils", "getPhoneNumber: getPhoneNumber is null = " + TextUtils.isEmpty(phoneNumber));
        String strStripSeparatorsAndCountryCode2 = PhoneNumberUtils.stripSeparatorsAndCountryCode(phoneNumber);
        Log.d("BdSmsUtils", "getPhoneNumber: getPhoneNumber stripSeparatorsAndCountryCode phoneNumber is null = " + TextUtils.isEmpty(strStripSeparatorsAndCountryCode2));
        return strStripSeparatorsAndCountryCode2;
    }

    public static void showPrivacyDialogOrNextCheck(Context context, int i) {
        if (isBdPrivacyStatementAgree(context)) {
            return;
        }
        startDialogActivity(i, 0, context);
    }

    public static boolean isBdPrivacyStatementAgree(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(BdDialogActivity.AGREE_BEIDOU_PRIVACY_STATEMENT, false);
    }

    private static void startDialogActivity(int i, int i2, Context context) {
        Intent intent = new Intent(context, (Class<?>) BdDialogActivity.class);
        intent.setFlags(402653184);
        intent.putExtra("flag_id", i);
        intent.putExtra("type", i2);
        context.startActivity(intent);
    }

    public static void setBeiDouEnableByUser(Context context, boolean z) {
        if (z) {
            showPrivacyDialogOrNextCheck(context, 0);
        } else {
            setBdSwitchState(false);
        }
    }

    public static boolean getBdSwitchState() {
        boolean z = Settings.System.getInt(PhoneGlobals.getInstance().getContentResolver(), "bd_switch_state", 0) == 1;
        Log.i("BdSmsUtils", "isBDEnabledByUser : state = " + z);
        return z;
    }

    public static void setBdSwitchState(boolean z) {
        boolean bdSwitchState = getBdSwitchState();
        Log.i("BdSmsUtils", "setBdSwitchState : newState = " + z + ", oldState = " + bdSwitchState);
        if (bdSwitchState == z) {
            return;
        }
        if (BdSmsController.getInstance() != null) {
            BdSmsController.getInstance().setBdEnabled(z);
            Settings.System.putInt(PhoneGlobals.getInstance().getContentResolver(), "bd_switch_state", z ? 1 : 0);
        } else {
            Log.e("BdSmsUtils", "setBdSwitchState : BdSmsController not init");
        }
    }

    public static boolean isSupportBeiDouByCarrier(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return false;
        }
        boolean booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "config_beidou_support", false);
        Log.d("BdSmsUtils", "isSupportBeiDouByCarrier : slotId = " + i + ", isSupportBeiDou = " + booleanConfigForSubId);
        return booleanConfigForSubId;
    }

    public static boolean isBluetoothEnable(Context context) {
        BluetoothAdapter adapter;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService("bluetooth");
        if (bluetoothManager == null || (adapter = bluetoothManager.getAdapter()) == null) {
            return false;
        }
        if (SystemProperties.get("persist.sys.bluetooth_restricte_state", "false").equals("true")) {
            Log.d("BdSmsUtils", "isBluetoothEnable: bluetooth is half close");
            return true;
        }
        boolean zIsEnabled = adapter.isEnabled();
        Log.d("BdSmsUtils", "isBluetoothEnabled : " + zIsEnabled);
        return zIsEnabled;
    }

    public static boolean setBluetoothStatus(Context context, boolean z) {
        BluetoothAdapter adapter;
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService("bluetooth");
        if (bluetoothManager == null || (adapter = bluetoothManager.getAdapter()) == null) {
            return false;
        }
        if (z) {
            boolean zEnable = adapter.enable();
            Log.d("BdSmsUtils", "setBluetooth : enableBluetooth, result = " + zEnable);
            return zEnable;
        }
        boolean zDisable = adapter.disable(false);
        Log.d("BdSmsUtils", "setBluetooth : disableBluetooth, result = " + zDisable);
        return zDisable;
    }

    public static boolean isNfcEnabled(Context context) {
        NfcAdapter defaultAdapter;
        NfcManager nfcManager = (NfcManager) context.getSystemService("nfc");
        if (nfcManager == null || (defaultAdapter = nfcManager.getDefaultAdapter()) == null) {
            return false;
        }
        boolean zIsEnabled = defaultAdapter.isEnabled();
        Log.d("BdSmsUtils", "isNfcEnabled : " + zIsEnabled);
        return zIsEnabled;
    }

    public static boolean setNfcStatus(Context context, boolean z) {
        NfcAdapter defaultAdapter;
        NfcManager nfcManager = (NfcManager) context.getSystemService("nfc");
        if (nfcManager == null || (defaultAdapter = nfcManager.getDefaultAdapter()) == null) {
            return false;
        }
        if (z) {
            boolean zEnable = defaultAdapter.enable();
            Log.d("BdSmsUtils", "setNfcStatus: enableNfc, result = " + zEnable);
            return zEnable;
        }
        boolean zDisable = defaultAdapter.disable();
        Log.d("BdSmsUtils", "setNfcStatus: disableNfc, result = " + zDisable);
        return zDisable;
    }
}
