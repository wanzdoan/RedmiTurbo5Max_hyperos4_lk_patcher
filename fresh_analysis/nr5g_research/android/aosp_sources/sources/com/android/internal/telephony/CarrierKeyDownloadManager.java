package com.android.internal.telephony;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.os.UserManager;
import android.telephony.CarrierConfigManager;
import android.telephony.ImsiEncryptionInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CarrierKeyDownloadManager extends Handler {
    private static final int[] CARRIER_KEY_TYPES = {1, 2};
    private boolean mAllowedOverMeteredNetwork;
    private final BroadcastReceiver mBroadcastReceiver;
    public volatile int mCarrierId;
    private ConnectivityManager mConnectivityManager;
    private final Context mContext;
    private DefaultNetworkCallback mDefaultNetworkCallback;
    private boolean mDeleteOldKeyAfterDownload;
    public volatile long mDownloadId;
    public final DownloadManager mDownloadManager;
    private final BroadcastReceiver mDownloadReceiver;
    private boolean mIsKeySent;
    private boolean mIsRequiredToHandleUnlock;
    public int mKeyAvailability;
    private KeyguardManager mKeyguardManager;
    public volatile String mMccMncForDownload;
    private final Phone mPhone;
    private volatile TelephonyManager mTelephonyManager;
    private String mURL;
    private UserManager mUserManager;

    public static boolean isKeyEnabled(int i, int i2) {
        return ((i2 >> (i - 1)) & 1) == 1;
    }

    public CarrierKeyDownloadManager(Phone phone, Looper looper) {
        super(looper);
        this.mKeyAvailability = 0;
        this.mAllowedOverMeteredNetwork = false;
        this.mDeleteOldKeyAfterDownload = false;
        this.mMccMncForDownload = "";
        this.mCarrierId = -1;
        this.mIsKeySent = false;
        this.mDownloadReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.CarrierKeyDownloadManager.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.DOWNLOAD_COMPLETE")) {
                    CarrierKeyDownloadManager.this.logd("Download Complete");
                    CarrierKeyDownloadManager carrierKeyDownloadManager = CarrierKeyDownloadManager.this;
                    carrierKeyDownloadManager.sendMessage(carrierKeyDownloadManager.obtainMessage(1, Long.valueOf(intent.getLongExtra("extra_download_id", 0L))));
                }
            }
        };
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.CarrierKeyDownloadManager.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                int slotIndex = SubscriptionManager.getSlotIndex(CarrierKeyDownloadManager.this.mPhone.getSubId());
                int phoneId = CarrierKeyDownloadManager.this.mPhone.getPhoneId();
                action.hashCode();
                switch (action) {
                    case "com.android.internal.telephony.carrier_key_download_alarm":
                        if (intent.getIntExtra("android.telephony.extra.SLOT_INDEX", -1) == slotIndex) {
                            CarrierKeyDownloadManager.this.logd("Handling key renewal alarm: ".concat(action));
                            CarrierKeyDownloadManager.this.updateSimOperator();
                            CarrierKeyDownloadManager.this.sendEmptyMessage(0);
                            break;
                        }
                        break;
                    case "android.intent.action.USER_UNLOCKED":
                        CarrierKeyDownloadManager carrierKeyDownloadManager = CarrierKeyDownloadManager.this;
                        StringBuilder sb = new StringBuilder("device fully unlocked, isRequiredToHandleUnlock = ");
                        sb.append(CarrierKeyDownloadManager.this.mIsRequiredToHandleUnlock);
                        sb.append(", slotIndex = ");
                        sb.append(slotIndex);
                        sb.append("  hasActiveDataNetwork = ");
                        sb.append(CarrierKeyDownloadManager.this.mConnectivityManager.getActiveNetwork() != null);
                        carrierKeyDownloadManager.logd(sb.toString());
                        if (CarrierKeyDownloadManager.this.mIsRequiredToHandleUnlock) {
                            CarrierKeyDownloadManager.this.mIsRequiredToHandleUnlock = false;
                            CarrierKeyDownloadManager.this.sendEmptyMessage(3);
                            break;
                        }
                        break;
                    case "com.android.internal.telephony.ACTION_CARRIER_CERTIFICATE_DOWNLOAD":
                        if (phoneId == intent.getIntExtra("phone", -1)) {
                            CarrierKeyDownloadManager.this.logd("Handling reset intent: ".concat(action));
                            CarrierKeyDownloadManager.this.sendEmptyMessage(0);
                            break;
                        }
                        break;
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mPhone = phone;
        Context context = phone.getContext();
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.internal.telephony.carrier_key_download_alarm");
        intentFilter.addAction("com.android.internal.telephony.ACTION_CARRIER_CERTIFICATE_DOWNLOAD");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        this.mDownloadManager = (DownloadManager) context.getSystemService("download");
        this.mTelephonyManager = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(phone.getSubId());
        this.mKeyguardManager = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService(ConnectivityManager.class);
        context.registerReceiver(broadcastReceiver, intentFilter, null, this);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.CarrierKeyDownloadManager$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.CarrierKeyDownloadManager$$ExternalSyntheticLambda1
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        if (i == this.mPhone.getPhoneId() && !SubscriptionManager.isValidSubscriptionId(i2)) {
            this.mIsKeySent = false;
        }
        logd("CarrierConfig changed slotIndex = " + i + " subId = " + i2 + " CarrierId = " + i3 + " phoneId = " + this.mPhone.getPhoneId());
        if (i == this.mPhone.getPhoneId()) {
            if (i3 > 0 || !TextUtils.isEmpty(this.mMccMncForDownload)) {
                if (this.mTelephonyManager == null || this.mTelephonyManager.getSubscriptionId() != i2) {
                    logd("recreating TelManager with SubId = " + i2);
                    this.mTelephonyManager = ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i2);
                }
                if (i3 > 0) {
                    this.mCarrierId = i3;
                }
                updateSimOperator();
                printDeviceLockStatus();
                if (!this.mUserManager.isUserUnlocked()) {
                    this.mIsRequiredToHandleUnlock = true;
                    return;
                }
                logd("Carrier Config changed: slotIndex=" + i);
                sendEmptyMessage(0);
            }
        }
    }

    private void printDeviceLockStatus() {
        logd(" Device Status: isDeviceLocked = " + this.mKeyguardManager.isDeviceLocked() + "  iss User unlocked = " + this.mUserManager.isUserUnlocked());
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i != 0) {
            if (i == 1) {
                long jLongValue = ((Long) message.obj).longValue();
                String simOperator = this.mTelephonyManager.getSimOperator(this.mPhone.getSubId());
                int simCarrierId = this.mTelephonyManager.getSimCarrierId();
                if (isValidDownload(simOperator, jLongValue, simCarrierId)) {
                    onDownloadComplete(jLongValue, simOperator, simCarrierId);
                    onPostDownloadProcessing();
                    return;
                }
                return;
            }
            if (i != 2 && i != 3) {
                return;
            }
        }
        handleAlarmOrConfigChange();
    }

    private void onPostDownloadProcessing() {
        resetRenewalAlarm();
        this.mDownloadId = -1L;
        this.mContext.unregisterReceiver(this.mDownloadReceiver);
    }

    private void handleAlarmOrConfigChange() {
        ImsiEncryptionInfo existingKey;
        boolean z = false;
        if (carrierUsesKeys()) {
            if (areCarrierKeysAbsentOrExpiring()) {
                boolean z2 = this.mConnectivityManager.getActiveNetwork() != null;
                if (z2 && downloadKey()) {
                    z = true;
                }
                logd("handleAlarmOrConfigChange :: downloadStartedSuccessfully " + z);
                int slotIndex = SubscriptionManager.getSlotIndex(this.mPhone.getSubId());
                if (z) {
                    unregisterDefaultNetworkCb(slotIndex);
                    return;
                }
                this.mIsRequiredToHandleUnlock = !this.mUserManager.isUserUnlocked();
                loge("hasActiveDataConnection = " + z2 + "    isDeviceUserLocked = " + this.mIsRequiredToHandleUnlock);
                if (!z2) {
                    registerDefaultNetworkCb(slotIndex);
                }
                resetRenewalAlarm();
                return;
            }
            logd("handleAlarmOrConfigChange :: mIsKeySent " + this.mIsKeySent);
            if (this.mIsKeySent || (existingKey = getExistingKey()) == null) {
                return;
            }
            logd("handleAlarmOrConfigChange :: saving public key");
            savePublicKey(existingKey);
            return;
        }
        cleanupRenewalAlarms();
        if (!isOtherSlotHasCarrier()) {
            this.mPhone.deleteCarrierInfoForImsiEncryption(getSimCarrierId(), getSimOperator());
            this.mIsKeySent = false;
        }
        cleanupDownloadInfo();
    }

    private ImsiEncryptionInfo getExistingKey() {
        for (int i : CARRIER_KEY_TYPES) {
            if (isKeyEnabled(i)) {
                return this.mPhone.getCarrierInfoForImsiEncryption(i, false);
            }
        }
        return null;
    }

    private boolean isOtherSlotHasCarrier() {
        List<SubscriptionInfo> activeSubscriptionInfoList = ((SubscriptionManager) this.mPhone.getContext().getSystemService(SubscriptionManager.class)).getActiveSubscriptionInfoList();
        StringBuilder sb = new StringBuilder("handleAlarmOrConfigChange ActiveSubscriptionInfoList = ");
        sb.append(activeSubscriptionInfoList != null ? Integer.valueOf(activeSubscriptionInfoList.size()) : null);
        loge(sb.toString());
        for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
            if (this.mPhone.getSubId() != subscriptionInfo.getSubscriptionId() && subscriptionInfo.getCarrierId() == this.mPhone.getCarrierId()) {
                loge("handleAlarmOrConfigChange same operator sim in another slot");
                return true;
            }
        }
        return false;
    }

    private void cleanupDownloadInfo() {
        logd("Cleaning up download info");
        this.mDownloadId = -1L;
        this.mMccMncForDownload = "";
        this.mCarrierId = -1;
    }

    private void cleanupRenewalAlarms() {
        logd("Cleaning up existing renewal alarms");
        int slotIndex = SubscriptionManager.getSlotIndex(this.mPhone.getSubId());
        Intent intent = new Intent("com.android.internal.telephony.carrier_key_download_alarm");
        intent.putExtra("android.telephony.extra.SLOT_INDEX", slotIndex);
        ((AlarmManager) this.mContext.getSystemService(AlarmManager.class)).cancel(PendingIntent.getBroadcast(this.mContext, 0, intent, 201326592));
    }

    public long getExpirationDate() {
        ImsiEncryptionInfo carrierInfoForImsiEncryption;
        long time = Long.MAX_VALUE;
        for (int i : CARRIER_KEY_TYPES) {
            if (isKeyEnabled(i) && (carrierInfoForImsiEncryption = this.mPhone.getCarrierInfoForImsiEncryption(i, false)) != null && carrierInfoForImsiEncryption.getExpirationTime() != null && time > carrierInfoForImsiEncryption.getExpirationTime().getTime()) {
                time = carrierInfoForImsiEncryption.getExpirationTime().getTime();
            }
        }
        if (time == Long.MAX_VALUE || time < System.currentTimeMillis() + 604800000) {
            return System.currentTimeMillis() + 86400000;
        }
        return time - ((long) (new Random().nextInt(1209600000) + 604800000));
    }

    public void resetRenewalAlarm() {
        cleanupRenewalAlarms();
        int slotIndex = SubscriptionManager.getSlotIndex(this.mPhone.getSubId());
        long expirationDate = getExpirationDate();
        logd("minExpirationDate: " + new Date(expirationDate));
        AlarmManager alarmManager = (AlarmManager) this.mContext.getSystemService("alarm");
        Intent intent = new Intent("com.android.internal.telephony.carrier_key_download_alarm");
        intent.putExtra("android.telephony.extra.SLOT_INDEX", slotIndex);
        alarmManager.set(0, expirationDate, PendingIntent.getBroadcast(this.mContext, 0, intent, 201326592));
        logd("setRenewalAlarm: action=" + intent.getAction() + " time=" + new Date(expirationDate));
    }

    public void updateSimOperator() {
        String operatorNumeric = this.mPhone.getOperatorNumeric();
        if (TextUtils.isEmpty(operatorNumeric) || operatorNumeric.equals(this.mMccMncForDownload)) {
            return;
        }
        this.mMccMncForDownload = operatorNumeric;
        logd("updateSimOperator, Initialized mMccMncForDownload = " + this.mMccMncForDownload);
    }

    public String getSimOperator() {
        updateSimOperator();
        return this.mMccMncForDownload;
    }

    public int getSimCarrierId() {
        return this.mCarrierId > 0 ? this.mCarrierId : this.mPhone.getCarrierId();
    }

    public boolean isValidDownload(String str, long j, int i) {
        if (j != this.mDownloadId) {
            loge("download ID=" + j + " for completed download does not match stored id=" + this.mDownloadId);
            return false;
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(this.mMccMncForDownload) || !TextUtils.equals(str, this.mMccMncForDownload) || this.mCarrierId != i) {
            loge("currentMccMnc=" + str + " storedMccMnc =" + this.mMccMncForDownload + "currentCarrierId = " + i + "  storedCarrierId = " + this.mCarrierId);
            return false;
        }
        logd("Matched MccMnc =  " + str + ", carrierId = " + i + ", downloadId: " + j);
        return true;
    }

    private void onDownloadComplete(long j, String str, int i) {
        logd("onDownloadComplete: " + j);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(j);
        Cursor cursorQuery = this.mDownloadManager.query(query);
        if (cursorQuery == null) {
            return;
        }
        if (cursorQuery.moveToFirst()) {
            int columnIndex = cursorQuery.getColumnIndex("status");
            try {
                if (8 == cursorQuery.getInt(columnIndex)) {
                    try {
                        String strConvertToString = convertToString(this.mDownloadManager, j);
                        if (TextUtils.isEmpty(strConvertToString)) {
                            logd("fallback to no gzip");
                            strConvertToString = convertToStringNoGZip(this.mDownloadManager, j);
                        }
                        parseJsonAndPersistKey(strConvertToString, str, i);
                        logd("Completed downloading keys");
                        this.mDownloadManager.remove(j);
                    } catch (Exception e) {
                        loge("Error in download:" + j + ". " + e);
                        this.mDownloadManager.remove(j);
                    }
                } else {
                    loge("Download Failed reason = " + cursorQuery.getInt(columnIndex) + "Failed Status reason" + cursorQuery.getInt(cursorQuery.getColumnIndex("reason")));
                    printDeviceLockStatus();
                }
            } catch (Throwable th) {
                this.mDownloadManager.remove(j);
                throw th;
            }
        }
        cursorQuery.close();
    }

    private boolean carrierUsesKeys() {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        if (carrierConfigManager == null) {
            return false;
        }
        try {
            configForSubId = carrierConfigManager.getConfigForSubId(this.mPhone.getSubId(), "imsi_key_availability_int", "imsi_key_download_url_string", "allow_metered_network_for_cert_download_bool");
        } catch (RuntimeException unused) {
            loge("CarrierConfigLoader is not available.");
            configForSubId = null;
        }
        if (configForSubId != null && !configForSubId.isEmpty()) {
            this.mKeyAvailability = configForSubId.getInt("imsi_key_availability_int");
            this.mURL = configForSubId.getString("imsi_key_download_url_string");
            this.mAllowedOverMeteredNetwork = configForSubId.getBoolean("allow_metered_network_for_cert_download_bool");
            if (this.mKeyAvailability != 0 && !TextUtils.isEmpty(this.mURL)) {
                for (int i : CARRIER_KEY_TYPES) {
                    if (isKeyEnabled(i)) {
                        return true;
                    }
                }
                return false;
            }
            logd("Carrier not enabled or invalid values. mKeyAvailability=" + this.mKeyAvailability + " mURL=" + this.mURL);
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:36:0x0047 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private static String convertToStringNoGZip(DownloadManager downloadManager, long j) {
        StringBuilder sb = new StringBuilder();
        try {
            ParcelFileDescriptor parcelFileDescriptorOpenDownloadedFile = downloadManager.openDownloadedFile(j);
            try {
                FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptorOpenDownloadedFile.getFileDescriptor());
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
                    while (true) {
                        String line = bufferedReader.readLine();
                        if (line != null) {
                            sb.append(line);
                            sb.append('\n');
                        } else {
                            fileInputStream.close();
                            parcelFileDescriptorOpenDownloadedFile.close();
                            return sb.toString();
                        }
                        if (parcelFileDescriptorOpenDownloadedFile != null) {
                            try {
                                parcelFileDescriptorOpenDownloadedFile.close();
                            } catch (Throwable th) {
                                th.addSuppressed(th);
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th3) {
                        th2.addSuppressed(th3);
                    }
                    throw th2;
                }
            } catch (Throwable th4) {
                if (parcelFileDescriptorOpenDownloadedFile != null) {
                    parcelFileDescriptorOpenDownloadedFile.close();
                }
                throw th4;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String convertToString(DownloadManager downloadManager, long j) {
        try {
            ParcelFileDescriptor parcelFileDescriptorOpenDownloadedFile = downloadManager.openDownloadedFile(j);
            try {
                FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptorOpenDownloadedFile.getFileDescriptor());
                try {
                    GZIPInputStream gZIPInputStream = new GZIPInputStream(fileInputStream);
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(gZIPInputStream, StandardCharsets.UTF_8));
                        StringBuilder sb = new StringBuilder();
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line != null) {
                                sb.append(line);
                                sb.append('\n');
                            } else {
                                String string = sb.toString();
                                gZIPInputStream.close();
                                fileInputStream.close();
                                parcelFileDescriptorOpenDownloadedFile.close();
                                return string;
                            }
                            try {
                                fileInputStream.close();
                            } catch (Throwable th) {
                                th.addSuppressed(th);
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        try {
                            gZIPInputStream.close();
                        } catch (Throwable th3) {
                            th2.addSuppressed(th3);
                        }
                        throw th2;
                    }
                } catch (Throwable th4) {
                    fileInputStream.close();
                    throw th4;
                }
            } catch (Throwable th5) {
                if (parcelFileDescriptorOpenDownloadedFile != null) {
                    try {
                        parcelFileDescriptorOpenDownloadedFile.close();
                    } catch (Throwable th6) {
                        th5.addSuppressed(th6);
                    }
                }
                throw th5;
            }
        } catch (ZipException e) {
            Log.d("CarrierKeyDownloadManager", "Stream is not gzipped e=" + e);
            return null;
        } catch (IOException e2) {
            Log.e("CarrierKeyDownloadManager", "Unexpected exception in convertToString e=" + e2);
            return null;
        } catch (Exception e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public void parseJsonAndPersistKey(String str, String str2, int i) {
        int i2;
        String string;
        int i3;
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || (i2 = i) == -1) {
            loge("jsonStr or mcc, mnc: is empty or carrierId is UNKNOWN_CARRIER_ID");
            return;
        }
        try {
            String strSubstring = str2.substring(0, 3);
            String strSubstring2 = str2.substring(3);
            JSONArray jSONArray = new JSONObject(str).getJSONArray("carrier-keys");
            int i4 = 0;
            while (i4 < jSONArray.length()) {
                JSONObject jSONObject = jSONArray.getJSONObject(i4);
                if (jSONObject.has("certificate")) {
                    string = jSONObject.getString("certificate");
                } else {
                    string = jSONObject.getString("public-key");
                }
                if (jSONObject.has("key-type")) {
                    String string2 = jSONObject.getString("key-type");
                    if (string2.equals("EPDG")) {
                        i3 = 1;
                    } else {
                        if (!string2.equals("WLAN")) {
                            loge("Invalid key-type specified: " + string2);
                        }
                        i3 = 2;
                    }
                } else {
                    i3 = 2;
                }
                String string3 = jSONObject.getString("key-identifier");
                Pair<PublicKey, Long> keyInformation = getKeyInformation(cleanCertString(string).getBytes());
                if (this.mDeleteOldKeyAfterDownload) {
                    this.mPhone.deleteCarrierInfoForImsiEncryption(-1, null);
                    this.mDeleteOldKeyAfterDownload = false;
                }
                savePublicKey((PublicKey) keyInformation.first, i3, string3, ((Long) keyInformation.second).longValue(), strSubstring, strSubstring2, i2);
                i4++;
                i2 = i;
            }
        } catch (JSONException e) {
            loge("Json parsing error: " + e.getMessage());
        } catch (Exception e2) {
            loge("Exception getting certificate: " + e2);
        }
    }

    public boolean isKeyEnabled(int i) {
        return isKeyEnabled(i, this.mKeyAvailability);
    }

    public boolean areCarrierKeysAbsentOrExpiring() {
        for (int i : CARRIER_KEY_TYPES) {
            if (isKeyEnabled(i)) {
                ImsiEncryptionInfo carrierInfoForImsiEncryption = this.mPhone.getCarrierInfoForImsiEncryption(i, false);
                if (carrierInfoForImsiEncryption == null) {
                    logd("Key not found for: " + i);
                    return true;
                }
                if (carrierInfoForImsiEncryption.getCarrierId() == -1) {
                    logd("carrier key is unknown carrier, so prefer to reDownload");
                    this.mDeleteOldKeyAfterDownload = true;
                    return true;
                }
                if (carrierInfoForImsiEncryption.getExpirationTime().getTime() - System.currentTimeMillis() < 1814400000) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean downloadKey() {
        CarrierKeyDownloadManager carrierKeyDownloadManager;
        logd("starting download from: " + this.mURL);
        if (TextUtils.isEmpty(this.mMccMncForDownload) || this.mCarrierId == -1) {
            loge("mccmnc or carrierId is UnKnown");
            return false;
        }
        try {
            carrierKeyDownloadManager = this;
            try {
                this.mContext.registerReceiver(this.mDownloadReceiver, new IntentFilter("android.intent.action.DOWNLOAD_COMPLETE"), null, carrierKeyDownloadManager, 2);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(carrierKeyDownloadManager.mURL));
                request.setAllowedOverMetered(carrierKeyDownloadManager.mAllowedOverMeteredNetwork);
                request.setNotificationVisibility(2);
                request.addRequestHeader("Accept-Encoding", "gzip");
                long jEnqueue = carrierKeyDownloadManager.mDownloadManager.enqueue(request);
                carrierKeyDownloadManager.mDownloadId = jEnqueue;
                carrierKeyDownloadManager.logd("saving values mccmnc: " + carrierKeyDownloadManager.mMccMncForDownload + ", downloadId: " + jEnqueue + ", carrierId: " + carrierKeyDownloadManager.mCarrierId);
                return true;
            } catch (Exception e) {
                e = e;
                carrierKeyDownloadManager.loge("exception trying to download key from url: " + carrierKeyDownloadManager.mURL + ",  Exception = " + e.getMessage());
                carrierKeyDownloadManager.printDeviceLockStatus();
                return false;
            }
        } catch (Exception e2) {
            e = e2;
            carrierKeyDownloadManager = this;
        }
    }

    public static Pair<PublicKey, Long> getKeyInformation(byte[] bArr) throws Exception {
        X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(bArr));
        return new Pair<>(x509Certificate.getPublicKey(), Long.valueOf(x509Certificate.getNotAfter().getTime()));
    }

    public void savePublicKey(PublicKey publicKey, int i, String str, long j, String str2, String str3, int i2) {
        this.mPhone.setCarrierInfoForImsiEncryption(new ImsiEncryptionInfo(str2, str3, i, str, publicKey, new Date(j), i2), true);
        this.mIsKeySent = true;
    }

    public void savePublicKey(ImsiEncryptionInfo imsiEncryptionInfo) {
        this.mPhone.setCarrierInfoForImsiEncryption(imsiEncryptionInfo, false);
        this.mIsKeySent = true;
    }

    public static String cleanCertString(String str) {
        return str.substring(str.indexOf("-----BEGIN CERTIFICATE-----"), str.indexOf("-----END CERTIFICATE-----") + 25);
    }

    private void registerDefaultNetworkCb(int i) {
        logd("RegisterDefaultNetworkCb for slotId = " + i);
        if (this.mDefaultNetworkCallback != null || i == -1) {
            return;
        }
        DefaultNetworkCallback defaultNetworkCallback = new DefaultNetworkCallback(i);
        this.mDefaultNetworkCallback = defaultNetworkCallback;
        this.mConnectivityManager.registerDefaultNetworkCallback(defaultNetworkCallback, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unregisterDefaultNetworkCb(int i) {
        logd("unregisterDefaultNetworkCb for slotId = " + i);
        DefaultNetworkCallback defaultNetworkCallback = this.mDefaultNetworkCallback;
        if (defaultNetworkCallback != null) {
            this.mConnectivityManager.unregisterNetworkCallback(defaultNetworkCallback);
            this.mDefaultNetworkCallback = null;
        }
    }

    final class DefaultNetworkCallback extends ConnectivityManager.NetworkCallback {
        final int mSlotIndex;

        public DefaultNetworkCallback(int i) {
            this.mSlotIndex = i;
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            CarrierKeyDownloadManager.this.logd("Data network connected, slotId = " + this.mSlotIndex);
            if (CarrierKeyDownloadManager.this.mConnectivityManager.getActiveNetwork() == null || SubscriptionManager.getSlotIndex(CarrierKeyDownloadManager.this.mPhone.getSubId()) != this.mSlotIndex) {
                return;
            }
            CarrierKeyDownloadManager.this.mIsRequiredToHandleUnlock = false;
            CarrierKeyDownloadManager.this.unregisterDefaultNetworkCb(this.mSlotIndex);
            CarrierKeyDownloadManager.this.sendEmptyMessage(2);
        }
    }

    private void loge(String str) {
        Log.e("CarrierKeyDownloadManager [" + this.mPhone.getPhoneId() + "]", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Log.d("CarrierKeyDownloadManager [" + this.mPhone.getPhoneId() + "]", str);
    }
}
