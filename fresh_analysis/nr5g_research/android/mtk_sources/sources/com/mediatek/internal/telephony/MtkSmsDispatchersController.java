package com.mediatek.internal.telephony;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.os.PowerManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.SmsDispatchersController;
import com.android.internal.telephony.SmsStorageMonitor;
import com.android.internal.telephony.SmsUsageMonitor;
import com.android.internal.telephony.satellite.SatelliteController;
import java.util.ArrayList;
import java.util.List;
import mediatek.telephony.MtkTelephony;

/* JADX INFO: loaded from: classes.dex */
public class MtkSmsDispatchersController extends SmsDispatchersController {
    protected static final int EVENT_SMS_READY = 0;
    private static final String TAG = "MtkSmsDispatchersController";
    private static final int WAKE_LOCK_TIMEOUT = 500;
    private boolean mSmsReady;
    private PowerManager.WakeLock mWakeLock;

    /* JADX WARN: Multi-variable type inference failed */
    public MtkSmsDispatchersController(Phone phone, SmsStorageMonitor storageMonitor, SmsUsageMonitor usageMonitor, FeatureFlags featureFlags) {
        super(phone, storageMonitor, usageMonitor, featureFlags);
        this.mSmsReady = false;
        createWakelock();
        MtkRIL ci = this.mCi;
        ci.registerForSmsReady(this, 0, null);
        Rlog.d(TAG, "MtkSmsDispatchersController created");
    }

    protected void sendData(String callingPackage, int callingUser, String destAddr, String scAddr, int destPort, int originalPort, byte[] data, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        String scAddr2;
        if (!TextUtils.isEmpty(scAddr)) {
            scAddr2 = scAddr;
        } else {
            scAddr2 = getSmscAddressFromUSIMWithPhoneIdentity(callingPackage);
        }
        if (this.mImsSmsDispatcher.isAvailable()) {
            ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).sendData(callingPackage, callingUser, destAddr, scAddr2, destPort, originalPort, data, sentIntent, deliveryIntent);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendData(callingPackage, callingUser, destAddr, scAddr2, destPort, originalPort, data, sentIntent, deliveryIntent);
        } else {
            this.mGsmDispatcher.sendData(callingPackage, callingUser, destAddr, scAddr2, destPort, originalPort, data, sentIntent, deliveryIntent);
        }
    }

    public int copyTextMessageToIccCard(String scAddress, String address, List<String> text, int status, long timestamp) {
        if (this.mPhone.getPhoneType() == 2) {
            return this.mCdmaDispatcher.copyTextMessageToIccCard(scAddress, address, text, status, timestamp);
        }
        return this.mGsmDispatcher.copyTextMessageToIccCard(scAddress, address, text, status, timestamp);
    }

    protected void sendTextWithEncodingType(String destAddr, String scAddr, String text, int encodingType, PendingIntent sentIntent, PendingIntent deliveryIntent, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        String str;
        String scAddr2;
        if (!TextUtils.isEmpty(scAddr)) {
            str = callingPkg;
            scAddr2 = scAddr;
        } else {
            str = callingPkg;
            scAddr2 = getSmscAddressFromUSIMWithPhoneIdentity(str);
        }
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (this.mImsSmsDispatcher.isAvailable() || (satelliteController != null && satelliteController.isSatelliteEnabledOrBeingEnabled())) {
            ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).sendTextWithEncodingType(destAddr, scAddr2, text, encodingType, sentIntent, deliveryIntent, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendTextWithEncodingType(destAddr, scAddr2, text, encodingType, sentIntent, deliveryIntent, messageUri, str, callingUser, persistMessage, priority, expectMore, validityPeriod);
        } else {
            this.mGsmDispatcher.sendTextWithEncodingType(destAddr, scAddr2, text, encodingType, sentIntent, deliveryIntent, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod);
        }
    }

    protected void sendMultipartTextWithEncodingType(String destAddr, String scAddr, ArrayList<String> parts, int encodingType, ArrayList<PendingIntent> sentIntents, ArrayList<PendingIntent> deliveryIntents, Uri messageUri, String callingPkg, int callingUser, boolean persistMessage, int priority, boolean expectMore, int validityPeriod) {
        String str;
        String scAddr2;
        if (!TextUtils.isEmpty(scAddr)) {
            str = callingPkg;
            scAddr2 = scAddr;
        } else {
            str = callingPkg;
            scAddr2 = getSmscAddressFromUSIMWithPhoneIdentity(str);
        }
        SatelliteController satelliteController = SatelliteController.getInstance();
        if (this.mImsSmsDispatcher.isAvailable() || (satelliteController != null && satelliteController.isSatelliteEnabledOrBeingEnabled())) {
            ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).sendMultipartTextWithEncodingType(destAddr, scAddr2, parts, encodingType, sentIntents, deliveryIntents, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod);
        } else if (isCdmaMo()) {
            this.mCdmaDispatcher.sendMultipartTextWithEncodingType(destAddr, scAddr2, parts, encodingType, sentIntents, deliveryIntents, messageUri, str, callingUser, persistMessage, priority, expectMore, validityPeriod);
        } else {
            this.mGsmDispatcher.sendMultipartTextWithEncodingType(destAddr, scAddr2, parts, encodingType, sentIntents, deliveryIntents, messageUri, callingPkg, callingUser, persistMessage, priority, expectMore, validityPeriod);
        }
    }

    protected void handleIccFull() {
        if (!isCdmaMo()) {
            this.mGsmDispatcher.handleIccFull();
        }
    }

    protected void setSmsMemoryStatus(boolean status) {
        if (!isCdmaMo()) {
            this.mGsmDispatcher.setSmsMemoryStatus(status);
        }
    }

    public boolean isSmsReady() {
        return this.mSmsReady;
    }

    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                Rlog.d(TAG, "SMS is ready, Phone: " + this.mPhone.getPhoneId());
                this.mSmsReady = true;
                notifySmsReady(this.mSmsReady);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }

    private void createWakelock() {
        PowerManager pm = (PowerManager) this.mPhone.getContext().getSystemService("power");
        this.mWakeLock = pm.newWakeLock(1, "SmsCommonEventHelp");
        this.mWakeLock.setReferenceCounted(true);
    }

    private void notifySmsReady(boolean isReady) {
        Intent intent = new Intent(MtkTelephony.SMS_STATE_CHANGED_ACTION);
        intent.putExtra("ready", isReady);
        SubscriptionManager.putPhoneIdAndSubIdExtra(intent, this.mPhone.getPhoneId());
        intent.addFlags(DynamicSarService.WIFI_DATA_STATE_MASK_AP);
        intent.setComponent(null);
        this.mWakeLock.acquire(500L);
        this.mPhone.getContext().sendBroadcast(intent);
    }

    public boolean isImsEx() {
        return ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).isAvailable();
    }

    public boolean isSmsCapable() {
        return ((MtkImsSmsDispatcher) this.mImsSmsDispatcher).isSmsCapable();
    }
}
