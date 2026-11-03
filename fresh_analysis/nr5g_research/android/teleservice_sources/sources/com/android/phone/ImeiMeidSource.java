package com.android.phone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.radio.modem.ImeiInfo;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyPermissions;
import com.android.internal.telephony.utils.NtnUtils;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class ImeiMeidSource {
    private static String LOG_TAG = "ImeiMeidSource";
    private Context mContext;
    private Handler mHandler = new Handler() { // from class: com.android.phone.ImeiMeidSource.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            int i = message.what;
            if (i == 100 || i == 101) {
                int i2 = i - 100;
                ImeiMeidSource.this.mImeis[i2] = null;
                ImeiMeidSource.this.mMeids[i2] = null;
                if (ImeiMeidSource.this.mReadImeiException[i2] == null) {
                    ImeiMeidSource.this.mReadImeiException[i2] = new Throwable("init");
                }
                if (ImeiMeidSource.this.mReadMeidException[i2] == null) {
                    ImeiMeidSource.this.mReadMeidException[i2] = new Throwable("init");
                }
                CommandsInterface commandsInterface = ImeiMeidSource.getCommandsInterface(PhoneFactory.getPhone(i2));
                commandsInterface.getImei(obtainMessage(i + 100, 0, 0));
                commandsInterface.getDeviceIdentity(obtainMessage(i + 200, 0, 0));
                return;
            }
            if (i == 200 || i == 201) {
                int i3 = i - 200;
                Throwable[] thArr = ImeiMeidSource.this.mReadImeiException;
                Throwable th = asyncResult.exception;
                thArr[i3] = th;
                Object obj = asyncResult.result;
                ImeiInfo imeiInfo = (ImeiInfo) obj;
                if (th != null || obj == null || TextUtils.isEmpty(imeiInfo.imei)) {
                    ImeiMeidSource.this.mImeis[i3] = null;
                    int i4 = message.arg1;
                    if (i4 > 10) {
                        return;
                    }
                    int i5 = i + 200;
                    ImeiMeidSource.this.mHandler.removeMessages(i5);
                    ImeiMeidSource.this.mHandler.sendMessageDelayed(obtainMessage(i5, i4 + 1, 0), 2000L);
                    return;
                }
                ImeiMeidSource.this.mImeis[i3] = imeiInfo.imei;
                if (!TelephonyManager.isCustSingleSimDevice() || i3 != 1) {
                    Rlog.d(ImeiMeidSource.LOG_TAG, "slot=" + i3 + " imei=" + PhoneNumberUtils.toLogSafePhoneNumber(ImeiMeidSource.this.mImeis[i3], 3));
                }
                ImeiMeidSource.this.onDeviceIdLoaded();
                return;
            }
            if (i != 300 && i != 301) {
                if (i == 400 || i == 401) {
                    int i6 = i - 400;
                    if (ImeiMeidSource.this.mReadImeiException[i6] == null) {
                        ImeiMeidSource.this.mReadImeiException[i6] = new Throwable();
                    }
                    ImeiMeidSource.getCommandsInterface(PhoneFactory.getPhone(i6)).getImei(obtainMessage(i - 200, message.arg1, 0));
                    return;
                }
                if (i == 500 || i == 501) {
                    int i7 = i - 500;
                    if (ImeiMeidSource.this.mReadMeidException[i7] == null) {
                        ImeiMeidSource.this.mReadMeidException[i7] = new Throwable();
                    }
                    ImeiMeidSource.getCommandsInterface(PhoneFactory.getPhone(i7)).getDeviceIdentity(obtainMessage(i - 200, message.arg1, 0));
                    return;
                }
                return;
            }
            int i8 = i - 300;
            Throwable[] thArr2 = ImeiMeidSource.this.mReadMeidException;
            Throwable th2 = asyncResult.exception;
            thArr2[i8] = th2;
            if (th2 != null) {
                ImeiMeidSource.this.mMeids[i8] = null;
                int i9 = message.arg1;
                if (i9 > 10) {
                    return;
                }
                int i10 = i + 200;
                ImeiMeidSource.this.mHandler.removeMessages(i10);
                ImeiMeidSource.this.mHandler.sendMessageDelayed(obtainMessage(i10, i9 + 1, 0), 2000L);
                return;
            }
            String[] strArr = (String[]) asyncResult.result;
            if (strArr != null && strArr.length > 0 && !TextUtils.isEmpty(strArr[0])) {
                ImeiMeidSource.this.mImeis[i8] = strArr[0];
                ImeiMeidSource.this.mReadImeiException[i8] = asyncResult.exception;
            }
            ImeiMeidSource.this.mMeids[i8] = strArr[3];
            if (ImeiMeidSource.this.mMeids[i8] != null && ImeiMeidSource.this.mMeids[i8].matches("^0*$")) {
                if (!TelephonyManager.isCustSingleSimDevice() || i8 != 1) {
                    Rlog.d(ImeiMeidSource.LOG_TAG, "invalid meid=" + ImeiMeidSource.this.mMeids[i8] + " slot=" + i8);
                }
                ImeiMeidSource.this.mMeids[i8] = null;
            }
            if (!TelephonyManager.isCustSingleSimDevice() || i8 != 1) {
                Rlog.d(ImeiMeidSource.LOG_TAG, "slot=" + i8 + " imei=" + PhoneNumberUtils.toLogSafePhoneNumber(ImeiMeidSource.this.mImeis[i8], 3) + " meid=" + PhoneNumberUtils.toLogSafePhoneNumber(ImeiMeidSource.this.mMeids[i8], 3));
            }
            ImeiMeidSource.this.onDeviceIdLoaded();
        }
    };
    private String[] mImeis;
    private String[] mMeids;
    private NtnUtils mNtnUtils;
    private Throwable[] mReadImeiException;
    private Throwable[] mReadMeidException;

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code duplicated, block: B:39:0x00e5  */
    public void onDeviceIdLoaded() {
        String str;
        int i = 0;
        int i2 = 0;
        while (true) {
            String[] strArr = this.mImeis;
            if (i2 < strArr.length) {
                if (this.mReadImeiException[i2] != null || TextUtils.isEmpty(strArr[i2])) {
                    return;
                } else {
                    i2++;
                }
            } else {
                SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
                SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
                for (int i3 = 0; i3 < this.mImeis.length; i3++) {
                    editorEdit.putString("key_imei_slot" + i3, this.mImeis[i3]);
                    if (TextUtils.isEmpty(SystemProperties.get("ro.ril.miui.imei" + i3)) && (!TelephonyManager.isCustSingleSimDevice() || i3 != 1)) {
                        SystemProperties.set("ro.ril.miui.imei" + i3, this.mImeis[i3]);
                    }
                    if (!TextUtils.isEmpty(this.mMeids[i3])) {
                        editorEdit.putString("key_meid_slot" + i3, this.mMeids[i3]);
                    }
                }
                String string = defaultSharedPreferences.getString("device_id_key", "");
                String string2 = defaultSharedPreferences.getString("small_device_id_key", "");
                if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
                    String[] strArr2 = this.mImeis;
                    String str2 = strArr2[0];
                    if (strArr2.length == 2) {
                        int iCompareTo = str2.compareTo(strArr2[1]);
                        String[] strArr3 = this.mImeis;
                        if (iCompareTo < 0) {
                            str = strArr3[1];
                            if (strArr3[0].matches("^0*$")) {
                                str2 = this.mImeis[1];
                            }
                            i = 1;
                        } else if (strArr3[1].matches("^0*$")) {
                            str = str2;
                        } else {
                            str2 = this.mImeis[1];
                            str = str2;
                        }
                    } else {
                        str = str2;
                    }
                    editorEdit.putString("small_device_id_key", str2.matches("^0*$") ? "" : str2);
                    editorEdit.putString("device_id_key", str);
                    sendDeviceIdReadyBroadcast(str, i);
                }
                if (editorEdit.commit()) {
                    return;
                }
                Rlog.d(LOG_TAG, "failed to commit preference");
                return;
            }
        }
    }

    private void sendDeviceIdReadyBroadcast(String str, int i) {
        Intent intent = new Intent("android.intent.action.DEVICE_ID_READY");
        intent.putExtra("device_id", str);
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, i);
        this.mContext.sendBroadcast(intent, "android.permission.READ_PHONE_STATE");
    }

    public ImeiMeidSource(Context context) {
        this.mContext = context;
        Phone[] phones = PhoneFactory.getPhones();
        this.mImeis = new String[phones.length];
        this.mMeids = new String[phones.length];
        this.mReadImeiException = new Throwable[phones.length];
        this.mReadMeidException = new Throwable[phones.length];
        for (int i = 0; i < phones.length; i++) {
            this.mReadImeiException[i] = new Throwable("init");
            this.mReadMeidException[i] = new Throwable("init");
            this.mImeis[i] = null;
            this.mMeids[i] = null;
            getCommandsInterface(phones[i]).registerForAvailable(this.mHandler, i + 100, (Object) null);
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (!TextUtils.isEmpty(defaultSharedPreferences.getString("device_id_key", "")) || TextUtils.isEmpty(defaultSharedPreferences.getString("key_imei_slot0", ""))) {
            return;
        }
        SharedPreferences.Editor editorEdit = defaultSharedPreferences.edit();
        editorEdit.putString("device_id_key", defaultSharedPreferences.getString("key_imei_slot0", ""));
        if (editorEdit.commit()) {
            return;
        }
        Rlog.d(LOG_TAG, "failed to commit preference when init");
    }

    public String getDeviceId(String str) {
        Context context = this.mContext;
        if (TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, str, context.getAttributionTag(), "getDeviceId")) {
            return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("device_id_key", "");
        }
        return null;
    }

    public String getSmallDeviceId(String str) {
        Context context = this.mContext;
        if (TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, str, context.getAttributionTag(), "getSmallDeviceId")) {
            return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("small_device_id_key", "");
        }
        return null;
    }

    public List getDeviceIdList(String str) {
        Context context = this.mContext;
        boolean zCheckCallingOrSelfReadDeviceIdentifiers = TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, str, context.getAttributionTag(), "getDeviceIdList");
        if (!zCheckCallingOrSelfReadDeviceIdentifiers) {
            return new ArrayList(0);
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        ArrayList arrayList = new ArrayList(this.mImeis.length * 2);
        for (int i = 0; i < this.mImeis.length; i++) {
            String string = defaultSharedPreferences.getString("key_imei_slot" + i, "");
            if (!TextUtils.isEmpty(string) && !arrayList.contains(string)) {
                arrayList.add(string);
            }
            String string2 = defaultSharedPreferences.getString("key_meid_slot" + i, "");
            if (!TextUtils.isEmpty(string2) && !arrayList.contains(string2)) {
                arrayList.add(string2);
            }
        }
        return arrayList;
    }

    public List getImeiList(String str) {
        Context context = this.mContext;
        boolean zCheckCallingOrSelfReadDeviceIdentifiers = TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, str, context.getAttributionTag(), "getSortedImeiList");
        if (!zCheckCallingOrSelfReadDeviceIdentifiers) {
            return new ArrayList(0);
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        ArrayList arrayList = new ArrayList(MiuiPhoneUtils.PHONE_COUNT);
        boolean z = false;
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            String string = defaultSharedPreferences.getString("key_imei_slot" + i, "");
            if (getNtnImei(this.mContext, i) != null) {
                string = getNtnImei(this.mContext, i);
                Rlog.d(LOG_TAG, "get ntn imei is:" + PhoneNumberUtils.toLogSafePhoneNumber(string, 3) + " slotid:" + i);
                z = true;
            }
            if (string != null && !string.isEmpty()) {
                arrayList.add(string);
            }
        }
        if (arrayList.size() > 1 && !z) {
            Collections.sort(arrayList);
        }
        return arrayList;
    }

    public List getMeidList(String str) {
        Context context = this.mContext;
        boolean zCheckCallingOrSelfReadDeviceIdentifiers = TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, str, context.getAttributionTag(), "getSortedMeidList");
        if (!zCheckCallingOrSelfReadDeviceIdentifiers) {
            return new ArrayList(0);
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        ArrayList arrayList = new ArrayList(MiuiPhoneUtils.PHONE_COUNT);
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            String string = defaultSharedPreferences.getString("key_meid_slot" + i, "");
            if (string != null && !string.isEmpty() && !arrayList.contains(string)) {
                arrayList.add(string);
            }
        }
        if (arrayList.size() > 1) {
            Collections.sort(arrayList);
        }
        return arrayList;
    }

    public String getImei(int i, String str) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return null;
        }
        int subId = phone.getSubId();
        Context context = this.mContext;
        if (TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, subId, str, context.getAttributionTag(), "getImei")) {
            return getImei(i);
        }
        return null;
    }

    public String getNtnImei(Context context, int i) {
        if (this.mNtnUtils == null) {
            this.mNtnUtils = new NtnUtils();
        }
        return this.mNtnUtils.getNtnImei(context, i);
    }

    public String getMeid(int i, String str) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return null;
        }
        int subId = phone.getSubId();
        Context context = this.mContext;
        if (TelephonyPermissions.checkCallingOrSelfReadDeviceIdentifiers(context, subId, str, context.getAttributionTag(), "getMeid")) {
            return getMeid(i);
        }
        return null;
    }

    public String getImei(int i) {
        if (SystemProperties.getBoolean("ro.radio.noril", false)) {
            return null;
        }
        if (i < 0 || i >= this.mImeis.length) {
            i = 0;
        }
        if (TextUtils.isEmpty(this.mImeis[i])) {
            return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("key_imei_slot" + i, "");
        }
        return this.mImeis[i];
    }

    public String getMeid(int i) {
        if (SystemProperties.getBoolean("ro.radio.noril", false)) {
            return null;
        }
        if (i < 0 || i >= this.mMeids.length) {
            i = 0;
        }
        if (TextUtils.isEmpty(this.mMeids[i])) {
            return PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("key_meid_slot" + i, "");
        }
        return this.mMeids[i];
    }

    public void dump(PrintWriter printWriter) {
        StringBuilder sb = new StringBuilder(DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER);
        String string = PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("device_id_key", "");
        sb.append(" deviceId=");
        sb.append(PhoneNumberUtils.toLogSafePhoneNumber(string, 3));
        String string2 = PreferenceManager.getDefaultSharedPreferences(this.mContext).getString("small_device_id_key", "");
        sb.append(" smallDeviceId=");
        sb.append(PhoneNumberUtils.toLogSafePhoneNumber(string2, 3));
        int length = TelephonyManager.isCustSingleSimDevice() ? 1 : this.mImeis.length;
        for (int i = 0; i < length; i++) {
            sb.append(" slot");
            sb.append(i);
            sb.append(" imei=");
            sb.append(PhoneNumberUtils.toLogSafePhoneNumber(this.mImeis[i], 3));
            sb.append(" mReadImeiException=");
            sb.append(this.mReadImeiException[i]);
            sb.append(" meid=");
            sb.append(PhoneNumberUtils.toLogSafePhoneNumber(this.mMeids[i], 3));
            sb.append(" mReadMeidException=");
            sb.append(this.mReadMeidException[i]);
        }
        printWriter.println("ImeiMeidSource: ".concat(sb.toString()));
    }

    public static CommandsInterface getCommandsInterface(Phone phone) {
        return phone.mCi;
    }
}
