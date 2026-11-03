package com.android.internal.telephony.uicc;

import android.R;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.WorkSource;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyStatsLog;
import com.android.internal.telephony.nano.StoredPinProto$EncryptedPin;
import com.android.internal.telephony.nano.StoredPinProto$EncryptedPlatformManagedPins;
import com.android.internal.telephony.nano.StoredPinProto$PlatformManagedPin;
import com.android.internal.telephony.nano.StoredPinProto$PlatformManagedPins;
import com.android.internal.telephony.nano.StoredPinProto$StoredPin;
import com.android.internal.telephony.protobuf.nano.MessageNano;
import com.android.internal.telephony.util.WorkerThread;
import com.android.internal.util.ArrayUtils;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/* JADX INFO: loaded from: classes.dex */
public class PinStorage extends Handler {
    private static final boolean VDBG = Rlog.isLoggable("PinStorage", 2);
    private final UUID mAnomalyUUID;
    private final int mBootCount;
    private final BroadcastReceiver mBroadcastReceiver;
    protected final Context mContext;
    private FeatureFlags mFeatureFlags;
    private boolean mIsDeviceLocked;
    private boolean mIsDeviceSecure;
    private KeyStore mKeyStore;
    private boolean mLastCommitResult;
    private final KeyguardManager.DeviceLockedStateListener mLockStateListener;
    protected SecretKey mLongTermSecretKey;
    private Set mPlatformManagedIccids;
    private SparseArray mRamStorage;
    private SecretKey mRebootPersistentSecretKey;
    private boolean mRegisteredKeyguardListener;
    private SecretKey mShortTermSecretKey;
    public int mShortTermSecretKeyDurationMinutes;

    public PinStorage(Context context, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mAnomalyUUID = UUID.fromString("aaa85c00-7a31-4a46-b42a-753256664784");
        this.mLastCommitResult = true;
        this.mPlatformManagedIccids = new HashSet();
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.uicc.PinStorage.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if ("android.telephony.action.SIM_CARD_STATE_CHANGED".equals(action) || "android.telephony.action.SIM_APPLICATION_STATE_CHANGED".equals(action)) {
                    int intExtra = intent.getIntExtra("phone", -1);
                    int intExtra2 = intent.getIntExtra("android.telephony.extra.SIM_STATE", 0);
                    if (PinStorage.this.validateSlotId(intExtra)) {
                        PinStorage pinStorage = PinStorage.this;
                        pinStorage.sendMessage(pinStorage.obtainMessage(1, intExtra, intExtra2));
                        return;
                    }
                    return;
                }
                if ("android.intent.action.USER_UNLOCKED".equals(action)) {
                    PinStorage pinStorage2 = PinStorage.this;
                    pinStorage2.sendMessage(pinStorage2.obtainMessage(4));
                }
            }
        };
        this.mBroadcastReceiver = broadcastReceiver;
        this.mLockStateListener = new KeyguardManager.DeviceLockedStateListener() { // from class: com.android.internal.telephony.uicc.PinStorage.2
            public void onDeviceLockedStateChanged(boolean z) {
                PinStorage.logd("Device lock state changed, locked now? " + z, new Object[0]);
                PinStorage pinStorage = PinStorage.this;
                if (z) {
                    pinStorage.sendMessage(pinStorage.obtainMessage(6));
                } else {
                    pinStorage.sendMessage(pinStorage.obtainMessage(7));
                }
            }
        };
        this.mRegisteredKeyguardListener = false;
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        this.mBootCount = getBootCount();
        this.mShortTermSecretKeyDurationMinutes = 15;
        boolean zIsDeviceSecure = isDeviceSecure();
        this.mIsDeviceSecure = zIsDeviceSecure;
        this.mIsDeviceLocked = zIsDeviceSecure ? isDeviceLocked() : false;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.telephony.action.SIM_CARD_STATE_CHANGED");
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        intentFilter.addAction("android.intent.action.USER_UNLOCKED");
        context.registerReceiver(broadcastReceiver, intentFilter);
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.uicc.PinStorage$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.uicc.PinStorage$$ExternalSyntheticLambda1
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i, int i2, int i3, int i4) {
                    this.f$0.lambda$new$0(i, i2, i3, i4);
                }
            });
        }
        final String str = (!this.mIsDeviceSecure || this.mIsDeviceLocked) ? "PinStorage_longTerm_always_key" : "PinStorage_longTerm_ua_key";
        post(new Runnable() { // from class: com.android.internal.telephony.uicc.PinStorage$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$1(str);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, int i4) {
        onCarrierConfigurationChanged(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(String str) {
        this.mKeyStore = initializeKeyStore();
        this.mLongTermSecretKey = initializeSecretKey(str, true);
        this.mRebootPersistentSecretKey = initializeSecretKey("PinStorage_crossReboot_key", true);
        if (!this.mIsDeviceSecure || !this.mIsDeviceLocked) {
            this.mRamStorage = null;
            onDeviceReady();
        } else {
            logd("Device is locked - Postponing initialization", new Object[0]);
            this.mRamStorage = new SparseArray();
            loadPlatformManagedIccids();
            changeKeyguardListenerIfNecessary();
        }
    }

    private void changeKeyguardListenerIfNecessary() {
        synchronized (this) {
            try {
                KeyguardManager keyguardManager = (KeyguardManager) this.mContext.getSystemService(KeyguardManager.class);
                boolean zIsEmpty = this.mPlatformManagedIccids.isEmpty();
                boolean z = this.mRegisteredKeyguardListener;
                if (zIsEmpty) {
                    if (z) {
                        keyguardManager.removeDeviceLockedStateListener(this.mLockStateListener);
                        this.mRegisteredKeyguardListener = false;
                    }
                } else if (!z) {
                    keyguardManager.addDeviceLockedStateListener(WorkerThread.getExecutor(), this.mLockStateListener);
                    this.mRegisteredKeyguardListener = true;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private void loadPlatformManagedIccids() {
        synchronized (this) {
            this.mPlatformManagedIccids.addAll(deserializeIccids(this.mContext.getSharedPreferences("pinstorage_prefs", 0).getString("platform_managed_pin_identities", "")));
        }
    }

    private KeyStore getKeyStore() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            AnomalyReporter.reportAnomaly(this.mAnomalyUUID, "PinStorage keyStore invoked on main thread");
        }
        return this.mKeyStore;
    }

    public synchronized void storePin(String str, int i) {
        String iccid = getIccid(i);
        if (validatePin(str) && validateIccid(iccid) && validateSlotId(i)) {
            if (!isCacheAllowed(i)) {
                logd("storePin[%d]: caching it not allowed", Integer.valueOf(i));
                return;
            }
            logd("storePin[%d]", Integer.valueOf(i));
            StoredPinProto$StoredPin storedPinProto$StoredPin = new StoredPinProto$StoredPin();
            storedPinProto$StoredPin.iccid = iccid;
            storedPinProto$StoredPin.pin = str;
            storedPinProto$StoredPin.slotId = i;
            storedPinProto$StoredPin.status = 1;
            savePinInformation(i, storedPinProto$StoredPin);
            return;
        }
        loge("storePin[%d] - Invalid PIN, slotId or ICCID", Integer.valueOf(i));
        clearPin(i);
    }

    public synchronized void clearPin(int i) {
        logd("clearPin[%d]", Integer.valueOf(i));
        if (validateSlotId(i)) {
            savePinInformation(i, null);
        }
    }

    private String handleStoredPin(StoredPinProto$StoredPin storedPinProto$StoredPin, String str, int i) {
        if (storedPinProto$StoredPin == null) {
            return null;
        }
        if (!storedPinProto$StoredPin.iccid.equals(str)) {
            savePinInformation(i, null);
            TelephonyStatsLog.write(336, 6, 1, "");
        } else if (storedPinProto$StoredPin.status == 3) {
            logd("getPin[%d] - Found PIN ready for verification", Integer.valueOf(i));
            storedPinProto$StoredPin.status = 1;
            savePinInformation(i, storedPinProto$StoredPin);
            return storedPinProto$StoredPin.pin;
        }
        return null;
    }

    public synchronized String getPin(int i, String str) {
        if (validateSlotId(i) && validateIccid(str)) {
            String strHandleStoredPin = handleStoredPin(loadPinInformation(i), str, i);
            if (strHandleStoredPin != null) {
                return strHandleStoredPin;
            }
            StoredPinProto$PlatformManagedPin platformManagedPin = readPlatformManagedPin(str);
            if (platformManagedPin != null) {
                return platformManagedPin.pin;
            }
            return "";
        }
        return "";
    }

    public synchronized boolean isPinPlatformManaged(String str) {
        if (!validateIccid(str)) {
            return false;
        }
        return this.mPlatformManagedIccids.contains(str);
    }

    public synchronized boolean storePlatformManagedPin(int i, String str, String str2) {
        String iccid = getIccid(i);
        if (validateSlotId(i) && validateIccid(iccid) && validatePin(str) && validatePin(str2)) {
            StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin = new StoredPinProto$PlatformManagedPin();
            storedPinProto$PlatformManagedPin.iccid = iccid;
            storedPinProto$PlatformManagedPin.pin = str;
            storedPinProto$PlatformManagedPin.oldPin = str2;
            SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("pinstorage_prefs", 0).edit();
            if (!savePlatformManagedPins(editorEdit, iccid, storedPinProto$PlatformManagedPin)) {
                return false;
            }
            changeKeyguardListenerIfNecessary();
            storePlatformPinAsRebootReady(editorEdit, i, storedPinProto$PlatformManagedPin);
            return editorEdit.commit();
        }
        return false;
    }

    public synchronized boolean clearPlatformManagedPin(int i) {
        String iccid = getIccid(i);
        boolean z = false;
        if (!validateSlotId(i) || !validateIccid(iccid)) {
            return false;
        }
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("pinstorage_prefs", 0).edit();
        editorEdit.remove("encrypted_pin_reboot_" + i);
        if (savePlatformManagedPins(editorEdit, iccid, null) && editorEdit.commit()) {
            z = true;
        }
        return z;
    }

    public synchronized String getOldPin(String str) {
        if (!validateIccid(str)) {
            return "";
        }
        StoredPinProto$PlatformManagedPin platformManagedPin = readPlatformManagedPin(str);
        if (platformManagedPin != null && platformManagedPin.iccid.equals(str)) {
            return platformManagedPin.oldPin;
        }
        return "";
    }

    public synchronized byte[] getPlatformManagedPinsForBackup() {
        return MessageNano.toByteArray(readPlatformManagedPins());
    }

    public void restorePlatformManagedPinsFromBackup(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            loge("Restoring of platform-managed PINs backup failed: data is null or empty.", new Object[0]);
            return;
        }
        try {
            StoredPinProto$PlatformManagedPins from = StoredPinProto$PlatformManagedPins.parseFrom(bArr);
            StoredPinProto$PlatformManagedPins platformManagedPins = readPlatformManagedPins();
            List<StoredPinProto$PlatformManagedPin> listMergeExistingAndBackupPins = mergeExistingAndBackupPins(platformManagedPins, from);
            SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("pinstorage_prefs", 0).edit();
            savePlatformManagedPins(editorEdit, listMergeExistingAndBackupPins);
            editorEdit.commit();
            logi("Restored %d pins", Integer.valueOf(listMergeExistingAndBackupPins.size() - platformManagedPins.pins.length));
        } catch (IOException e) {
            loge("Failed parsing platform-managed PINs backup: %s", e);
        }
    }

    public static List<StoredPinProto$PlatformManagedPin> mergeExistingAndBackupPins(StoredPinProto$PlatformManagedPins storedPinProto$PlatformManagedPins, StoredPinProto$PlatformManagedPins storedPinProto$PlatformManagedPins2) {
        ArrayList arrayList = new ArrayList(storedPinProto$PlatformManagedPins.pins.length);
        arrayList.addAll(Arrays.asList(storedPinProto$PlatformManagedPins.pins));
        for (StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin : storedPinProto$PlatformManagedPins2.pins) {
            if (findPinByIccid(arrayList, storedPinProto$PlatformManagedPin.iccid) >= 0) {
                logw("Pin for ICCID %s already exists, not restoring", storedPinProto$PlatformManagedPin.iccid);
            } else {
                arrayList.add(storedPinProto$PlatformManagedPin);
            }
        }
        return arrayList;
    }

    public synchronized int prepareUnattendedReboot(WorkSource workSource) {
        int i = 2;
        if (this.mIsDeviceLocked) {
            loge("prepareUnattendedReboot - Device is locked", new Object[0]);
            return 2;
        }
        if (!startTimer(20000)) {
            return 2;
        }
        int slotCount = getSlotCount();
        SparseArray sparseArrayLoadPinInformation = loadPinInformation();
        deleteSecretKey("PinStorage_shortTerm_key");
        this.mShortTermSecretKey = null;
        if (sparseArrayLoadPinInformation.size() > 0) {
            this.mShortTermSecretKey = initializeSecretKey("PinStorage_shortTerm_key", true);
        }
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (true) {
            if (i2 >= slotCount) {
                i = i4;
                break;
            }
            StoredPinProto$StoredPin storedPinProto$StoredPin = (StoredPinProto$StoredPin) sparseArrayLoadPinInformation.get(i2);
            if (storedPinProto$StoredPin != null) {
                storedPinProto$StoredPin.status = 2;
                if (!savePinInformation(i2, storedPinProto$StoredPin)) {
                    break;
                }
                i3++;
            } else if (isPinState(i2, IccCardStatus.PinState.PINSTATE_ENABLED_VERIFIED)) {
                loge("Slot %d requires PIN and is not cached", Integer.valueOf(i2));
                i5++;
                i4 = 1;
            }
            i2++;
        }
        String packageName = (workSource == null || workSource.size() == 0) ? "" : workSource.getPackageName(0);
        if (i == 0) {
            logd("prepareUnattendedReboot - Stored %d PINs", Integer.valueOf(i3));
            TelephonyStatsLog.write(336, 4, i3, packageName);
        } else if (i == 1) {
            logd("prepareUnattendedReboot - Required %d PINs after reboot", Integer.valueOf(i5));
            TelephonyStatsLog.write(336, 5, i5, packageName);
        }
        saveNumberOfCachedPins(i3);
        return i;
    }

    private void onUserUnlocked() {
        synchronized (this) {
            try {
                if (this.mIsDeviceLocked) {
                    logd("onUserUnlocked - Device is unlocked", new Object[0]);
                    SparseArray sparseArrayLoadPinInformation = loadPinInformation();
                    cleanRamStorage();
                    this.mIsDeviceLocked = false;
                    this.mLongTermSecretKey = initializeSecretKey("PinStorage_longTerm_ua_key", true);
                    for (int i = 0; i < sparseArrayLoadPinInformation.size(); i++) {
                        savePinInformation(sparseArrayLoadPinInformation.keyAt(i), (StoredPinProto$StoredPin) sparseArrayLoadPinInformation.valueAt(i));
                    }
                    onDeviceReady();
                    verifyPendingPins();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private synchronized void makePlatformManagedPinsAvailableAndUsablePostReboot() {
        try {
            if (!isDeviceLocked() && !this.mIsDeviceLocked) {
                SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("pinstorage_prefs", 0).edit();
                int slotCount = getSlotCount();
                StoredPinProto$PlatformManagedPins platformManagedPins = readPlatformManagedPins();
                HashMap map = new HashMap();
                for (StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin : platformManagedPins.pins) {
                    map.put(storedPinProto$PlatformManagedPin.iccid, storedPinProto$PlatformManagedPin);
                }
                for (int i = 0; i < slotCount; i++) {
                    String iccid = getIccid(i);
                    StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin2 = (StoredPinProto$PlatformManagedPin) map.get(iccid);
                    if (storedPinProto$PlatformManagedPin2 == null) {
                        logd("PIN for SIM " + iccid + " is not platform-managed, skipping.", new Object[0]);
                    } else {
                        logd("PIN for SIM " + iccid + " IS platform-managed, processing.", new Object[0]);
                        storePlatformPinAsRebootReady(editorEdit, i, storedPinProto$PlatformManagedPin2);
                    }
                }
                logd("Result of storing platform-managed PINs as reboot-ready? " + editorEdit.commit(), new Object[0]);
                return;
            }
            logw("Cannot make platform-managed PINs available when device is locked.", new Object[0]);
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX WARN: Code duplicated, block: B:15:0x004e  */
    /* JADX WARN: Code duplicated, block: B:27:0x0056 A[SYNTHETIC] */
    private void onDeviceReady() {
        logd("onDeviceReady", new Object[0]);
        this.mShortTermSecretKey = initializeSecretKey("PinStorage_shortTerm_key", false);
        int slotCount = getSlotCount();
        int i = 0;
        for (int i2 = 0; i2 < slotCount; i2++) {
            StoredPinProto$StoredPin storedPinProto$StoredPinLoadPinInformation = loadPinInformation(i2);
            if (storedPinProto$StoredPinLoadPinInformation != null) {
                if (storedPinProto$StoredPinLoadPinInformation.status == 1) {
                    if (storedPinProto$StoredPinLoadPinInformation.bootCount != this.mBootCount) {
                        logd("Boot count [%d] does not match - remove PIN", Integer.valueOf(i2));
                        savePinInformation(i2, null);
                    } else {
                        logd("Boot count [%d] matches - keep stored PIN", Integer.valueOf(i2));
                        if (storedPinProto$StoredPinLoadPinInformation.status == 2) {
                            storedPinProto$StoredPinLoadPinInformation.status = 3;
                            savePinInformation(i2, storedPinProto$StoredPinLoadPinInformation);
                            i++;
                        }
                    }
                } else if (storedPinProto$StoredPinLoadPinInformation.status == 2) {
                    storedPinProto$StoredPinLoadPinInformation.status = 3;
                    savePinInformation(i2, storedPinProto$StoredPinLoadPinInformation);
                    i++;
                }
            }
        }
        if (i > 0) {
            startTimer(20000);
        }
        int iSaveNumberOfCachedPins = saveNumberOfCachedPins(0);
        if (iSaveNumberOfCachedPins > i) {
            TelephonyStatsLog.write(336, 7, iSaveNumberOfCachedPins - i, "");
        }
        loadPlatformManagedIccids();
        changeKeyguardListenerIfNecessary();
        logd("Have %d platform-managed PINs.", Integer.valueOf(this.mPlatformManagedIccids.size()));
    }

    private synchronized void onTimerExpiration() {
        try {
            logd("onTimerExpiration", new Object[0]);
            int slotCount = getSlotCount();
            int i = 0;
            for (int i2 = 0; i2 < slotCount; i2++) {
                StoredPinProto$StoredPin storedPinProto$StoredPinLoadPinInformation = loadPinInformation(i2);
                if (storedPinProto$StoredPinLoadPinInformation != null) {
                    int i3 = storedPinProto$StoredPinLoadPinInformation.status;
                    if (i3 == 3) {
                        logd("onTimerExpiration - Discarding PIN in slot %d", Integer.valueOf(i2));
                        savePinInformation(i2, null);
                        i++;
                    } else if (i3 == 2) {
                        logd("onTimerExpiration - Moving PIN in slot %d back to AVAILABLE", Integer.valueOf(i2));
                        storedPinProto$StoredPinLoadPinInformation.status = 1;
                        savePinInformation(i2, storedPinProto$StoredPinLoadPinInformation);
                    }
                }
            }
            deleteSecretKey("PinStorage_shortTerm_key");
            this.mShortTermSecretKey = null;
            saveNumberOfCachedPins(0);
            if (i > 0) {
                TelephonyStatsLog.write(336, 3, i, "");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected synchronized void onSimStatusChange(int i, int i2) {
        try {
            logd("SIM card/application changed[%d]: %s", Integer.valueOf(i), TelephonyManager.simStateToString(i2));
            switch (i2) {
                case 1:
                case 2:
                    StoredPinProto$StoredPin storedPinProto$StoredPinLoadPinInformation = loadPinInformation(i);
                    if (storedPinProto$StoredPinLoadPinInformation != null && storedPinProto$StoredPinLoadPinInformation.status != 3) {
                        savePinInformation(i, null);
                    }
                    break;
                case 3:
                    clearPlatformManagedPin(i);
                    clearPin(i);
                    break;
                case 4:
                case 5:
                case 9:
                case 10:
                    StoredPinProto$StoredPin storedPinProto$StoredPinLoadPinInformation2 = loadPinInformation(i);
                    if (storedPinProto$StoredPinLoadPinInformation2 != null && storedPinProto$StoredPinLoadPinInformation2.status != 1) {
                        savePinInformation(i, null);
                    }
                    break;
                case 7:
                case 8:
                    clearPin(i);
                    break;
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private void onCarrierConfigurationChanged(int i) {
        logv("onCarrierConfigChanged[%d]", Integer.valueOf(i));
        if (isCacheAllowed(i)) {
            return;
        }
        logd("onCarrierConfigChanged[%d] - PIN caching not allowed", Integer.valueOf(i));
        clearPin(i);
    }

    protected void onSupplyPinComplete(int i, boolean z) {
        logd("onSupplyPinComplete[%d] - success: %s", Integer.valueOf(i), Boolean.valueOf(z));
        if (!z) {
            clearPin(i);
        }
        TelephonyStatsLog.write(336, z ? 1 : 2, 1, "");
    }

    private synchronized void onDeviceLocked() {
        if (this.mIsDeviceLocked) {
            logd("Device was never unlocked, bailing.", new Object[0]);
            return;
        }
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("pinstorage_prefs", 0).edit();
        int slotCount = getSlotCount();
        for (int i = 0; i < slotCount; i++) {
            String iccid = getIccid(i);
            if (isPinPlatformManaged(iccid)) {
                logd("Removed post-reboot PIN for SIM " + iccid, new Object[0]);
                editorEdit.remove("encrypted_pin_reboot_" + i);
            }
        }
        editorEdit.commit();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 1) {
            onSimStatusChange(message.arg1, message.arg2);
            return;
        }
        if (i == 3) {
            onTimerExpiration();
            return;
        }
        if (i == 4) {
            onUserUnlocked();
            return;
        }
        if (i == 5) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            onSupplyPinComplete(message.arg2, asyncResult != null && asyncResult.exception == null);
        } else if (i == 6) {
            onDeviceLocked();
        } else {
            if (i != 7) {
                return;
            }
            makePlatformManagedPinsAvailableAndUsablePostReboot();
        }
    }

    private boolean isDeviceSecure() {
        KeyguardManager keyguardManager = (KeyguardManager) this.mContext.getSystemService(KeyguardManager.class);
        if (keyguardManager != null) {
            return keyguardManager.isDeviceSecure();
        }
        return false;
    }

    private boolean isDeviceLocked() {
        KeyguardManager keyguardManager = (KeyguardManager) this.mContext.getSystemService(KeyguardManager.class);
        return keyguardManager != null && keyguardManager.isDeviceSecure() && keyguardManager.isDeviceLocked();
    }

    private SparseArray loadPinInformation() {
        SparseArray sparseArray = new SparseArray();
        int slotCount = getSlotCount();
        for (int i = 0; i < slotCount; i++) {
            StoredPinProto$StoredPin storedPinProto$StoredPinLoadPinInformation = loadPinInformation(i);
            if (storedPinProto$StoredPinLoadPinInformation != null) {
                sparseArray.put(i, storedPinProto$StoredPinLoadPinInformation);
            }
        }
        return sparseArray;
    }

    public StoredPinProto$StoredPin loadPinInformation(int i) {
        StoredPinProto$StoredPin storedPinProto$StoredPinLoadPinInformationFromDisk;
        int i2;
        StoredPinProto$StoredPin storedPinProto$StoredPin = null;
        if (!this.mLastCommitResult) {
            loge("Last commit failed - returning empty values", new Object[0]);
            return null;
        }
        if (this.mIsDeviceLocked) {
            SparseArray sparseArray = this.mRamStorage;
            storedPinProto$StoredPinLoadPinInformationFromDisk = (sparseArray == null || sparseArray.get(i) == null) ? null : decryptStoredPin((byte[]) this.mRamStorage.get(i), this.mLongTermSecretKey);
            if (storedPinProto$StoredPinLoadPinInformationFromDisk == null) {
                logd("Device is locked, reading from reboot pin base key", new Object[0]);
                StoredPinProto$StoredPin storedPinProto$StoredPinLoadPinInformationFromDisk2 = loadPinInformationFromDisk(i, "encrypted_pin_reboot_", this.mRebootPersistentSecretKey);
                StringBuilder sb = new StringBuilder("Found post-reboot pin? ");
                sb.append(storedPinProto$StoredPinLoadPinInformationFromDisk2 != null);
                logd(sb.toString(), new Object[0]);
                if (storedPinProto$StoredPinLoadPinInformationFromDisk2 != null) {
                    deleteRebootPersistentPin(i);
                    storedPinProto$StoredPinLoadPinInformationFromDisk = storedPinProto$StoredPinLoadPinInformationFromDisk2;
                }
            }
        } else {
            storedPinProto$StoredPinLoadPinInformationFromDisk = loadPinInformationFromDisk(i, "encrypted_pin_available_", this.mLongTermSecretKey);
            StoredPinProto$StoredPin storedPinProto$StoredPinLoadPinInformationFromDisk3 = loadPinInformationFromDisk(i, "encrypted_pin_reboot_", this.mShortTermSecretKey);
            if (storedPinProto$StoredPinLoadPinInformationFromDisk == null || storedPinProto$StoredPinLoadPinInformationFromDisk3 != null) {
                storedPinProto$StoredPinLoadPinInformationFromDisk = (storedPinProto$StoredPinLoadPinInformationFromDisk != null || storedPinProto$StoredPinLoadPinInformationFromDisk3 == null) ? null : storedPinProto$StoredPinLoadPinInformationFromDisk3;
            }
        }
        if (storedPinProto$StoredPinLoadPinInformationFromDisk == null || (i2 = storedPinProto$StoredPinLoadPinInformationFromDisk.slotId) == i) {
            storedPinProto$StoredPin = storedPinProto$StoredPinLoadPinInformationFromDisk;
        } else {
            loge("Load PIN: slot ID does not match (%d != %d)", Integer.valueOf(i2), Integer.valueOf(i));
        }
        if (storedPinProto$StoredPin != null) {
            logv("Load PIN: %s", storedPinProto$StoredPin.toString());
            return storedPinProto$StoredPin;
        }
        logv("Load PIN for slot %d: null", Integer.valueOf(i));
        return storedPinProto$StoredPin;
    }

    private void deleteRebootPersistentPin(int i) {
        SharedPreferences.Editor editorEdit = this.mContext.getSharedPreferences("pinstorage_prefs", 0).edit();
        editorEdit.remove("encrypted_pin_reboot_" + i);
        editorEdit.commit();
    }

    private StoredPinProto$StoredPin loadPinInformationFromDisk(int i, String str, SecretKey secretKey) {
        String string = this.mContext.getSharedPreferences("pinstorage_prefs", 0).getString(str + i, "");
        if (string.isEmpty()) {
            return null;
        }
        try {
            return decryptStoredPin(Base64.decode(string, 0), secretKey);
        } catch (Exception unused) {
            return null;
        }
    }

    private StoredPinProto$StoredPin decryptStoredPin(byte[] bArr, SecretKey secretKey) {
        if (secretKey == null) {
            TelephonyStatsLog.write(336, 10, 1, "");
            return null;
        }
        try {
            byte[] bArrDecrypt = decrypt(secretKey, bArr);
            if (bArrDecrypt == null || bArrDecrypt.length <= 0) {
                return null;
            }
            return StoredPinProto$StoredPin.parseFrom(bArrDecrypt);
        } catch (Exception e) {
            loge("cannot decrypt/parse PIN information", e);
            return null;
        }
    }

    public boolean savePinInformation(int i, StoredPinProto$StoredPin storedPinProto$StoredPin) {
        PinStorage pinStorage;
        boolean zSavePinInformation;
        if (storedPinProto$StoredPin != null) {
            storedPinProto$StoredPin.bootCount = this.mBootCount;
        }
        if (this.mIsDeviceLocked) {
            return savePinInformationToRam(i, storedPinProto$StoredPin);
        }
        boolean z = false;
        SharedPreferences.Editor editorRemove = this.mContext.getSharedPreferences("pinstorage_prefs", 0).edit().remove("encrypted_pin_available_" + i).remove("encrypted_pin_reboot_" + i);
        if (storedPinProto$StoredPin != null) {
            logd("Saving PIN for slot %d", Integer.valueOf(i));
            if (storedPinProto$StoredPin.status == 1) {
                pinStorage = this;
                zSavePinInformation = pinStorage.savePinInformation(editorRemove, i, storedPinProto$StoredPin, "encrypted_pin_available_", this.mLongTermSecretKey);
            } else {
                pinStorage = this;
                zSavePinInformation = pinStorage.savePinInformation(editorRemove, i, storedPinProto$StoredPin, "encrypted_pin_reboot_", pinStorage.mShortTermSecretKey);
            }
        } else {
            pinStorage = this;
            logv("Deleting PIN for slot %d (if existed)", Integer.valueOf(i));
            zSavePinInformation = true;
        }
        if (editorRemove.commit() && zSavePinInformation) {
            z = true;
        }
        pinStorage.mLastCommitResult = z;
        return z;
    }

    private boolean savePlatformManagedPins(SharedPreferences.Editor editor, String str, StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin) {
        if (storedPinProto$PlatformManagedPin != null && !str.equals(storedPinProto$PlatformManagedPin.iccid)) {
            throw new IllegalArgumentException("Supplied iccid (" + str + ") must match the value in the Platform-managed PIN (" + storedPinProto$PlatformManagedPin.iccid + ")");
        }
        StoredPinProto$PlatformManagedPins platformManagedPins = readPlatformManagedPins();
        ArrayList arrayList = new ArrayList(platformManagedPins.pins.length);
        arrayList.addAll(Arrays.asList(platformManagedPins.pins));
        int iFindPinByIccid = findPinByIccid(arrayList, str);
        if (storedPinProto$PlatformManagedPin != null) {
            if (iFindPinByIccid == -1) {
                arrayList.add(storedPinProto$PlatformManagedPin);
            } else {
                arrayList.set(iFindPinByIccid, storedPinProto$PlatformManagedPin);
            }
        } else {
            if (iFindPinByIccid < 0) {
                logw("Requested to remove PIN for ICCID %s but is not present.", str);
                return false;
            }
            arrayList.remove(iFindPinByIccid);
        }
        Set hashSet = new HashSet();
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            hashSet.add(((StoredPinProto$PlatformManagedPin) obj).iccid);
        }
        if (!savePlatformManagedPins(editor, arrayList)) {
            return false;
        }
        savePlatformManagedIccids(editor, hashSet);
        return true;
    }

    private void savePlatformManagedIccids(SharedPreferences.Editor editor, Set set) {
        this.mPlatformManagedIccids = set;
        editor.remove("platform_managed_pin_identities");
        editor.putString("platform_managed_pin_identities", serializeIccidsSet(this.mPlatformManagedIccids));
        logd("Saved %d platform-managed PINs, identities: %s", Integer.valueOf(this.mPlatformManagedIccids.size()), this.mPlatformManagedIccids);
    }

    private boolean savePlatformManagedPins(SharedPreferences.Editor editor, List list) {
        StoredPinProto$PlatformManagedPins storedPinProto$PlatformManagedPins = new StoredPinProto$PlatformManagedPins();
        storedPinProto$PlatformManagedPins.pins = (StoredPinProto$PlatformManagedPin[]) list.toArray(new StoredPinProto$PlatformManagedPin[0]);
        byte[] byteArray = MessageNano.toByteArray(storedPinProto$PlatformManagedPins);
        byte[] bArrEncryptPlatformManagedPins = encryptPlatformManagedPins(this.mLongTermSecretKey, byteArray);
        if (byteArray.length > 0 && bArrEncryptPlatformManagedPins == null) {
            loge("Failed encrypting platform-managed pins, not changing stored ones.", new Object[0]);
            return false;
        }
        editor.remove("encrypted_platform_managed_pins");
        editor.putString("encrypted_platform_managed_pins", Base64.encodeToString(bArrEncryptPlatformManagedPins, 0));
        return true;
    }

    private static int findPinByIccid(List list, String str) {
        for (int i = 0; i < list.size(); i++) {
            StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin = (StoredPinProto$PlatformManagedPin) list.get(i);
            if (storedPinProto$PlatformManagedPin != null && storedPinProto$PlatformManagedPin.iccid.equals(str)) {
                return i;
            }
        }
        return -1;
    }

    private void storePlatformPinAsRebootReady(SharedPreferences.Editor editor, int i, StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin) {
        editor.remove("encrypted_pin_reboot_" + i);
        StoredPinProto$StoredPin storedPinProto$StoredPin = new StoredPinProto$StoredPin();
        storedPinProto$StoredPin.iccid = storedPinProto$PlatformManagedPin.iccid;
        storedPinProto$StoredPin.slotId = i;
        storedPinProto$StoredPin.pin = storedPinProto$PlatformManagedPin.pin;
        storedPinProto$StoredPin.status = 3;
        savePinInformation(editor, i, storedPinProto$StoredPin, "encrypted_pin_reboot_", this.mRebootPersistentSecretKey);
    }

    private StoredPinProto$PlatformManagedPins readPlatformManagedPins() {
        String string = this.mContext.getSharedPreferences("pinstorage_prefs", 0).getString("encrypted_platform_managed_pins", "");
        if (string.isEmpty()) {
            logw("Read platform-managed PINs preference is empty.", new Object[0]);
            return new StoredPinProto$PlatformManagedPins();
        }
        byte[] bArrDecode = Base64.decode(string, 0);
        StoredPinProto$PlatformManagedPins storedPinProto$PlatformManagedPins = new StoredPinProto$PlatformManagedPins();
        try {
            byte[] bArrDecryptPlatformManagedPins = decryptPlatformManagedPins(this.mLongTermSecretKey, bArrDecode);
            if (bArrDecryptPlatformManagedPins != null) {
                storedPinProto$PlatformManagedPins = StoredPinProto$PlatformManagedPins.parseFrom(bArrDecryptPlatformManagedPins);
            } else {
                logw("Could not decrypt platform-managed PINs.", new Object[0]);
            }
        } catch (IOException e) {
            loge("Failed parsing platform-managed PINs: %s", e);
        }
        logd("Returning %d platform-managed pins", Integer.valueOf(storedPinProto$PlatformManagedPins.pins.length));
        return storedPinProto$PlatformManagedPins;
    }

    private StoredPinProto$PlatformManagedPin readPlatformManagedPin(String str) {
        if (this.mIsDeviceLocked) {
            logw("Platform-managed PIN requested before device was unlocked for the first time, refusing.", new Object[0]);
            return null;
        }
        for (StoredPinProto$PlatformManagedPin storedPinProto$PlatformManagedPin : readPlatformManagedPins().pins) {
            if (storedPinProto$PlatformManagedPin.iccid.equals(str)) {
                return storedPinProto$PlatformManagedPin;
            }
        }
        return null;
    }

    private boolean savePinInformation(SharedPreferences.Editor editor, int i, StoredPinProto$StoredPin storedPinProto$StoredPin, String str, SecretKey secretKey) {
        if (secretKey == null) {
            return false;
        }
        if (i != storedPinProto$StoredPin.slotId) {
            loge("Save PIN: the slotId does not match (%d != %d)", Integer.valueOf(i), Integer.valueOf(storedPinProto$StoredPin.slotId));
            return false;
        }
        logv("Save PIN: %s", storedPinProto$StoredPin.toString());
        byte[] bArrEncrypt = encrypt(secretKey, MessageNano.toByteArray(storedPinProto$StoredPin));
        if (bArrEncrypt == null || bArrEncrypt.length <= 0) {
            return false;
        }
        editor.putString(str + i, Base64.encodeToString(bArrEncrypt, 0));
        return true;
    }

    private boolean savePinInformationToRam(int i, StoredPinProto$StoredPin storedPinProto$StoredPin) {
        byte[] bArrEncrypt;
        cleanRamStorage(i);
        if (storedPinProto$StoredPin == null) {
            return true;
        }
        if (storedPinProto$StoredPin.status != 1 || (bArrEncrypt = encrypt(this.mLongTermSecretKey, MessageNano.toByteArray(storedPinProto$StoredPin))) == null || bArrEncrypt.length <= 0) {
            return false;
        }
        logd("Saving PIN for slot %d in RAM", Integer.valueOf(i));
        this.mRamStorage.put(i, bArrEncrypt);
        return true;
    }

    private void cleanRamStorage() {
        int slotCount = getSlotCount();
        for (int i = 0; i < slotCount; i++) {
            cleanRamStorage(i);
        }
    }

    private void cleanRamStorage(int i) {
        SparseArray sparseArray = this.mRamStorage;
        if (sparseArray != null) {
            byte[] bArr = (byte[]) sparseArray.get(i);
            if (bArr != null) {
                Arrays.fill(bArr, (byte) 0);
            }
            this.mRamStorage.delete(i);
        }
    }

    private void verifyPendingPins() {
        int slotCount = getSlotCount();
        for (int i = 0; i < slotCount; i++) {
            if (isPinState(i, IccCardStatus.PinState.PINSTATE_ENABLED_NOT_VERIFIED)) {
                verifyPendingPin(i);
            }
        }
    }

    private void verifyPendingPin(int i) {
        String pin = getPin(i, getIccid(i));
        if (pin.isEmpty()) {
            return;
        }
        logd("Perform automatic verification of PIN in slot %d", Integer.valueOf(i));
        UiccProfile uiccProfileForPhone = UiccController.getInstance().getUiccProfileForPhone(i);
        if (uiccProfileForPhone != null) {
            Message messageObtainMessage = obtainMessage(5);
            messageObtainMessage.arg2 = i;
            uiccProfileForPhone.supplyPin(pin, messageObtainMessage);
            return;
        }
        logd("Perform automatic verification of PIN in slot %d not possible", Integer.valueOf(i));
    }

    private int getBootCount() {
        return Settings.Global.getInt(this.mContext.getContentResolver(), "boot_count", -1);
    }

    private int getSlotCount() {
        try {
            return PhoneFactory.getPhones().length;
        } catch (Exception unused) {
            return TelephonyManager.getDefault().getActiveModemCount();
        }
    }

    private int saveNumberOfCachedPins(int i) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("pinstorage_prefs", 0);
        int i2 = sharedPreferences.getInt("stored_pins", 0);
        sharedPreferences.edit().putInt("stored_pins", i).commit();
        return i2;
    }

    private boolean startTimer(int i) {
        removeMessages(3);
        if (i > 0) {
            return sendEmptyMessageDelayed(3, i);
        }
        return true;
    }

    private String getIccid(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        return phone != null ? phone.getFullIccSerialNumber() : "";
    }

    private boolean validatePin(String str) {
        return str != null && str.length() >= 4 && str.length() <= 8;
    }

    private boolean validateIccid(String str) {
        return str != null && str.length() >= 12;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean validateSlotId(int i) {
        return i >= 0 && i < getSlotCount();
    }

    private boolean isPinState(int i, IccCardStatus.PinState pinState) {
        UiccProfile uiccProfileForPhone = UiccController.getInstance().getUiccProfileForPhone(i);
        if (uiccProfileForPhone != null) {
            for (int i2 = 0; i2 < 3; i2++) {
                UiccCardApplication application = uiccProfileForPhone.getApplication(i2);
                if (application != null) {
                    return application.getPin1State() == pinState;
                }
            }
        }
        return false;
    }

    private boolean isCacheAllowed(int i) {
        return isCacheAllowedByDevice();
    }

    private boolean isCacheAllowedByDevice() {
        if (this.mContext.getResources().getBoolean(R.bool.config_allowTheaterModeWakeFromPowerKey)) {
            return true;
        }
        logv("Pin caching disabled in resources", new Object[0]);
        return false;
    }

    private boolean isCacheAllowedByCarrier(int i) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        Phone phone = PhoneFactory.getPhone(i);
        PersistableBundle carrierConfigSubset = (carrierConfigManager == null || phone == null) ? null : CarrierConfigManager.getCarrierConfigSubset(this.mContext, phone.getSubId(), new String[]{"store_sim_pin_for_unattended_reboot_bool"});
        if (carrierConfigSubset == null || carrierConfigSubset.isEmpty()) {
            carrierConfigSubset = CarrierConfigManager.getDefaultConfig();
        }
        return carrierConfigSubset.getBoolean("store_sim_pin_for_unattended_reboot_bool", true);
    }

    private static KeyStore initializeKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            logv("KeyStore ready", new Object[0]);
            return keyStore;
        } catch (Exception e) {
            loge("Error loading KeyStore", e);
            return null;
        }
    }

    private SecretKey initializeSecretKey(String str, boolean z) {
        if (getKeyStore() == null) {
            return null;
        }
        SecretKey secretKey = getSecretKey(str);
        if (secretKey != null) {
            logd("KeyStore: alias %s exists", str);
            return secretKey;
        }
        if (z) {
            Date shortLivedKeyValidityEnd = "PinStorage_shortTerm_key".equals(str) ? getShortLivedKeyValidityEnd() : null;
            boolean z2 = !"PinStorage_longTerm_always_key".equals(str) && isDeviceSecure();
            logd("KeyStore: alias %s does not exist - Creating (exp=%s, auth=%s)", str, shortLivedKeyValidityEnd != null ? shortLivedKeyValidityEnd.toString() : "", Boolean.valueOf(z2));
            return createSecretKey(str, shortLivedKeyValidityEnd, z2);
        }
        logd("KeyStore: alias %s does not exist - Nothing to do", str);
        return null;
    }

    private SecretKey getSecretKey(String str) {
        try {
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) getKeyStore().getEntry(str, null);
            if (secretKeyEntry != null) {
                return secretKeyEntry.getSecretKey();
            }
        } catch (Exception e) {
            loge("Exception with getting the key " + str, e);
            deleteSecretKey(str);
        }
        return null;
    }

    private SecretKey createSecretKey(String str, Date date, boolean z) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
            KeyGenParameterSpec.Builder encryptionPaddings = new KeyGenParameterSpec.Builder(str, 3).setBlockModes("GCM").setEncryptionPaddings("NoPadding");
            if (date != null) {
                encryptionPaddings = encryptionPaddings.setKeyValidityEnd(date);
            }
            if (z) {
                encryptionPaddings = encryptionPaddings.setUserAuthenticationRequired(true).setUserAuthenticationParameters(Integer.MAX_VALUE, 1);
            }
            keyGenerator.init(encryptionPaddings.build());
            return keyGenerator.generateKey();
        } catch (Exception e) {
            loge("Create key exception", e);
            return null;
        }
    }

    private Date getShortLivedKeyValidityEnd() {
        if (this.mShortTermSecretKeyDurationMinutes <= 0) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(12, this.mShortTermSecretKeyDurationMinutes);
        return calendar.getTime();
    }

    private void deleteSecretKey(String str) {
        if (getKeyStore() != null) {
            logd("Delete key: %s", str);
            try {
                getKeyStore().deleteEntry(str);
            } catch (Exception unused) {
                loge("Delete key exception", new Object[0]);
            }
        }
    }

    private static final class EncryptedBlob {
        private final byte[] mBytes;
        private final byte[] mInitializationVector;

        EncryptedBlob() {
            this.mBytes = null;
            this.mInitializationVector = null;
        }

        EncryptedBlob(byte[] bArr, byte[] bArr2) {
            this.mBytes = bArr;
            this.mInitializationVector = bArr2;
        }

        byte[] getBytes() {
            return this.mBytes;
        }

        byte[] getInitializationVector() {
            return this.mInitializationVector;
        }

        boolean isEmpty() {
            return this.mBytes == null && this.mInitializationVector == null;
        }
    }

    private static EncryptedBlob encryptToBlob(SecretKey secretKey, byte[] bArr) {
        if (secretKey == null) {
            loge("Encrypt: Secret key is null", new Object[0]);
            return new EncryptedBlob();
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(1, secretKey);
            return new EncryptedBlob(cipher.doFinal(bArr), cipher.getIV());
        } catch (Exception e) {
            loge("Encrypt exception", e);
            TelephonyStatsLog.write(336, 9, 1, "");
            return new EncryptedBlob();
        }
    }

    private static byte[] decryptBlob(SecretKey secretKey, EncryptedBlob encryptedBlob) {
        if (secretKey == null) {
            loge("Decrypt: Secret key is null", new Object[0]);
            return null;
        }
        if (encryptedBlob.isEmpty()) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, secretKey, new GCMParameterSpec(128, encryptedBlob.getInitializationVector()));
            return cipher.doFinal(encryptedBlob.getBytes());
        } catch (Exception e) {
            loge("Decrypt exception", e);
            TelephonyStatsLog.write(336, 8, 1, "");
            return null;
        }
    }

    private byte[] encrypt(SecretKey secretKey, byte[] bArr) {
        EncryptedBlob encryptedBlobEncryptToBlob = encryptToBlob(secretKey, bArr);
        if (encryptedBlobEncryptToBlob.isEmpty()) {
            return null;
        }
        StoredPinProto$EncryptedPin storedPinProto$EncryptedPin = new StoredPinProto$EncryptedPin();
        storedPinProto$EncryptedPin.iv = encryptedBlobEncryptToBlob.getInitializationVector();
        storedPinProto$EncryptedPin.encryptedStoredPin = encryptedBlobEncryptToBlob.getBytes();
        return MessageNano.toByteArray(storedPinProto$EncryptedPin);
    }

    private byte[] decrypt(SecretKey secretKey, byte[] bArr) {
        try {
            StoredPinProto$EncryptedPin from = StoredPinProto$EncryptedPin.parseFrom(bArr);
            if (ArrayUtils.isEmpty(from.encryptedStoredPin) || ArrayUtils.isEmpty(from.iv)) {
                return null;
            }
            return decryptBlob(secretKey, new EncryptedBlob(from.encryptedStoredPin, from.iv));
        } catch (Exception e) {
            loge("Parsing exception", e);
            return null;
        }
    }

    public static byte[] encryptPlatformManagedPins(SecretKey secretKey, byte[] bArr) {
        EncryptedBlob encryptedBlobEncryptToBlob = encryptToBlob(secretKey, bArr);
        if (encryptedBlobEncryptToBlob.isEmpty()) {
            logw("Encrypted platform-managed pins blob is empty.", new Object[0]);
            return null;
        }
        StoredPinProto$EncryptedPlatformManagedPins storedPinProto$EncryptedPlatformManagedPins = new StoredPinProto$EncryptedPlatformManagedPins();
        storedPinProto$EncryptedPlatformManagedPins.iv = encryptedBlobEncryptToBlob.getInitializationVector();
        storedPinProto$EncryptedPlatformManagedPins.encryptedPlatformPins = encryptedBlobEncryptToBlob.getBytes();
        return MessageNano.toByteArray(storedPinProto$EncryptedPlatformManagedPins);
    }

    public static byte[] decryptPlatformManagedPins(SecretKey secretKey, byte[] bArr) {
        try {
            StoredPinProto$EncryptedPlatformManagedPins from = StoredPinProto$EncryptedPlatformManagedPins.parseFrom(bArr);
            return decryptBlob(secretKey, new EncryptedBlob(from.encryptedPlatformPins, from.iv));
        } catch (IOException e) {
            loge("Failed parsing encrypted platform-managed PINs: %s", e);
            return null;
        }
    }

    private static void logv(String str, Object... objArr) {
        if (VDBG) {
            Rlog.d("PinStorage", String.format(str, objArr));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str, Object... objArr) {
        Rlog.d("PinStorage", String.format(str, objArr));
    }

    private static void loge(String str, Object... objArr) {
        Rlog.e("PinStorage", String.format(str, objArr));
    }

    private static void loge(String str, Throwable th) {
        Rlog.e("PinStorage", str, th);
    }

    private static void logw(String str, Object... objArr) {
        Rlog.w("PinStorage", String.format(str, objArr));
    }

    private static void logi(String str, Object... objArr) {
        Rlog.i("PinStorage", String.format(str, objArr));
    }

    void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("PinStorage:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("mIsDeviceSecure=" + this.mIsDeviceSecure);
        androidUtilIndentingPrintWriter.println("mIsDeviceLocked=" + this.mIsDeviceLocked);
        StringBuilder sb = new StringBuilder("isLongTermSecretKey=");
        sb.append(this.mLongTermSecretKey != null);
        androidUtilIndentingPrintWriter.println(sb.toString());
        StringBuilder sb2 = new StringBuilder("isShortTermSecretKey=");
        sb2.append(this.mShortTermSecretKey != null);
        androidUtilIndentingPrintWriter.println(sb2.toString());
        androidUtilIndentingPrintWriter.println("isCacheAllowedByDevice=" + isCacheAllowedByDevice());
        int slotCount = getSlotCount();
        for (int i = 0; i < slotCount; i++) {
            androidUtilIndentingPrintWriter.println("isCacheAllowedByCarrier[" + i + "]=" + isCacheAllowedByCarrier(i));
        }
        if (VDBG) {
            SparseArray sparseArrayLoadPinInformation = loadPinInformation();
            for (int i2 = 0; i2 < sparseArrayLoadPinInformation.size(); i2++) {
                androidUtilIndentingPrintWriter.println(" pin=" + ((StoredPinProto$StoredPin) sparseArrayLoadPinInformation.valueAt(i2)).toString());
            }
        }
        androidUtilIndentingPrintWriter.println("mPlatformManagedIccids=" + Rlog.pii(VDBG, this.mPlatformManagedIccids));
        androidUtilIndentingPrintWriter.decreaseIndent();
    }

    public static String serializeIccidsSet(Set<String> set) {
        if (set.isEmpty()) {
            return "";
        }
        return String.join(",", set);
    }

    public static Set<String> deserializeIccids(String str) {
        if (str.isEmpty()) {
            return new HashSet();
        }
        return new HashSet(Arrays.asList(str.split(",")));
    }
}
