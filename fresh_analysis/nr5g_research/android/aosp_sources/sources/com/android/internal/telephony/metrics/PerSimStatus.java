package com.android.internal.telephony.metrics;

import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.AnomalyReporter;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.telephony.data.ApnSetting;
import android.telephony.ims.ImsManager;
import android.telephony.ims.ImsMmTelManager;
import android.text.TextUtils;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.telephony.Rlog;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class PerSimStatus {
    private static final UUID CROSS_SIM_CALLING_STATUS_ANOMALY_UUID = UUID.fromString("377e1a33-d4ac-4039-9cc0-f0d8396757f3");
    public final boolean advancedCallingSettingEnabled;
    public final int carrierId;
    public final boolean crossSimCallingEnabled;
    public final boolean dataRoamingEnabled;
    public final boolean disabled2g;
    public final int minimumVoltageClass;
    public final int phoneNumberSourceCarrier;
    public final int phoneNumberSourceIms;
    public final int phoneNumberSourceUicc;
    public final boolean pin1Enabled;
    public final long preferredNetworkType;
    public final long unmeteredNetworks;
    public final int userModifiedApnTypes;
    public final int voWiFiModeSetting;
    public final int voWiFiRoamingModeSetting;
    public final boolean voWiFiSettingEnabled;
    public final boolean vonrEnabled;
    public final boolean vtSettingEnabled;

    private static int wifiCallingModeToProtoEnum(int i) {
        if (i == 0) {
            return 1;
        }
        if (i != 1) {
            return i != 2 ? 0 : 3;
        }
        return 2;
    }

    public static PerSimStatus getCurrentState(Phone phone) {
        int[] numberIds = getNumberIds(phone);
        if (numberIds == null) {
            return null;
        }
        int carrierId = phone.getCarrierId();
        ImsMmTelManager imsMmTelManager = getImsMmTelManager(phone);
        IccCard iccCard = phone.getIccCard();
        PersistAtomsStorage atomsStorage = PhoneFactory.getMetricsCollector().getAtomsStorage();
        return new PerSimStatus(carrierId, numberIds[0], numberIds[1], numberIds[2], imsMmTelManager == null ? false : imsMmTelManager.isAdvancedCallingSettingEnabled(), imsMmTelManager == null ? false : imsMmTelManager.isVoWiFiSettingEnabled(), imsMmTelManager == null ? 0 : wifiCallingModeToProtoEnum(imsMmTelManager.getVoWiFiModeSetting()), imsMmTelManager == null ? 0 : wifiCallingModeToProtoEnum(imsMmTelManager.getVoWiFiRoamingModeSetting()), imsMmTelManager == null ? false : imsMmTelManager.isVtSettingEnabled(), phone.getDataRoamingEnabled(), phone.getAllowedNetworkTypes(0), is2gDisabled(phone), iccCard != null ? iccCard.getIccLockEnabled() : false, getMinimumVoltageClass(phone), getUserModifiedApnTypes(phone), atomsStorage.getUnmeteredNetworks(phone.getPhoneId(), carrierId), isVonrEnabled(phone), isCrossSimCallingEnabled(imsMmTelManager));
    }

    private PerSimStatus(int i, int i2, int i3, int i4, boolean z, boolean z2, int i5, int i6, boolean z3, boolean z4, long j, boolean z5, boolean z6, int i7, int i8, long j2, boolean z7, boolean z8) {
        this.carrierId = i;
        this.phoneNumberSourceUicc = i2;
        this.phoneNumberSourceCarrier = i3;
        this.phoneNumberSourceIms = i4;
        this.advancedCallingSettingEnabled = z;
        this.voWiFiSettingEnabled = z2;
        this.voWiFiModeSetting = i5;
        this.voWiFiRoamingModeSetting = i6;
        this.vtSettingEnabled = z3;
        this.dataRoamingEnabled = z4;
        this.preferredNetworkType = j;
        this.disabled2g = z5;
        this.pin1Enabled = z6;
        this.minimumVoltageClass = i7;
        this.userModifiedApnTypes = i8;
        this.unmeteredNetworks = j2;
        this.vonrEnabled = z7;
        this.crossSimCallingEnabled = z8;
    }

    private static boolean isCrossSimCallingEnabled(ImsMmTelManager imsMmTelManager) {
        if (imsMmTelManager != null) {
            try {
                if (imsMmTelManager.isCrossSimCallingEnabled()) {
                    return true;
                }
            } catch (Exception e) {
                AnomalyReporter.reportAnomaly(CROSS_SIM_CALLING_STATUS_ANOMALY_UUID, "Failed to query ImsMmTelManager for cross-SIM calling status!");
                Rlog.e("PerSimStatus", e.getMessage());
            }
        }
        return false;
    }

    private static ImsMmTelManager getImsMmTelManager(Phone phone) {
        ImsManager imsManager = (ImsManager) phone.getContext().getSystemService(ImsManager.class);
        if (imsManager == null) {
            return null;
        }
        try {
            return imsManager.getImsMmTelManager(phone.getSubId());
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    private static int[] getNumberIds(Phone phone) {
        String countryIso;
        if (SubscriptionManagerService.getInstance() == null) {
            return null;
        }
        SubscriptionInfoInternal subscriptionInfoInternal = SubscriptionManagerService.getInstance().getSubscriptionInfoInternal(phone.getSubId());
        if (subscriptionInfoInternal == null) {
            countryIso = "";
        } else {
            countryIso = subscriptionInfoInternal.getCountryIso();
        }
        int i = 1;
        String[] strArr = {SubscriptionManagerService.getInstance().getPhoneNumber(phone.getSubId(), 1, null, null), SubscriptionManagerService.getInstance().getPhoneNumber(phone.getSubId(), 2, null, null), SubscriptionManagerService.getInstance().getPhoneNumber(phone.getSubId(), 3, null, null)};
        int[] iArr = new int[3];
        for (int i2 = 0; i2 < 3; i2++) {
            if (!TextUtils.isEmpty(strArr[i2])) {
                for (int i3 = 0; i3 < i2; i3++) {
                    if (!TextUtils.isEmpty(strArr[i3]) && PhoneNumberUtils.areSamePhoneNumber(strArr[i2], strArr[i3], countryIso)) {
                        iArr[i2] = iArr[i3];
                    }
                }
                if (iArr[i2] == 0) {
                    iArr[i2] = i;
                    i++;
                }
            }
        }
        return iArr;
    }

    private static boolean is2gDisabled(Phone phone) {
        return (phone.getAllowedNetworkTypes(3) & 32843) == 0;
    }

    private static int getMinimumVoltageClass(Phone phone) {
        UiccSlot uiccSlotForPhone = UiccController.getInstance().getUiccSlotForPhone(phone.getPhoneId());
        if (uiccSlotForPhone == null) {
            return 0;
        }
        int minimumVoltageClass = uiccSlotForPhone.getMinimumVoltageClass();
        int i = 1;
        if (minimumVoltageClass != 1) {
            i = 2;
            if (minimumVoltageClass != 2) {
                i = 3;
                if (minimumVoltageClass != 3) {
                    return 0;
                }
            }
        }
        return i;
    }

    private static int getUserModifiedApnTypes(Phone phone) {
        String[] strArr = {Integer.toString(1)};
        Cursor cursorQuery = phone.getContext().getContentResolver().query(Uri.withAppendedPath(Telephony.Carriers.CONTENT_URI, "subId/" + phone.getSubId()), new String[]{"type"}, "edited=?", strArr, null);
        int apnTypesBitmaskFromString = 0;
        while (cursorQuery != null) {
            try {
                if (!cursorQuery.moveToNext()) {
                    break;
                }
                apnTypesBitmaskFromString |= ApnSetting.getApnTypesBitmaskFromString(cursorQuery.getString(0));
            } catch (Throwable th) {
                try {
                    cursorQuery.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        }
        if (cursorQuery != null) {
            cursorQuery.close();
        }
        return apnTypesBitmaskFromString;
    }

    static boolean isVonrEnabled(Phone phone) {
        TelephonyManager telephonyManager = (TelephonyManager) phone.getContext().getSystemService(TelephonyManager.class);
        if (telephonyManager == null) {
            return false;
        }
        return telephonyManager.createForSubscriptionId(phone.getSubId()).isVoNrEnabled();
    }
}
