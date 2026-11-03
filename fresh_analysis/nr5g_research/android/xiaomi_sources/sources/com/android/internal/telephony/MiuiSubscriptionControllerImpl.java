package com.android.internal.telephony;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SubscriptionInfo;
import android.text.TextUtils;
import com.android.internal.telephony.subscription.SubscriptionInfoInternal;
import com.android.telephony.Rlog;
import java.io.PrintWriter;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSubscriptionControllerImpl implements MiuiSubscriptionControllerStub.IMiuiSubscriptionController {
    private static final String LOG_TAG = "MiuiSubscriptionControllerImpl";

    public MiuiSubscriptionControllerImpl() {
        MiuiTelephony.getInstance().registerTelephonyPlugins(this, Integer.MAX_VALUE);
    }

    public void clearActiveSubInfosCache() {
        MiuiSubscriptionControllerBase.clearActiveSubInfosCache();
    }

    public void removeActiveSubInfosCache(int slotId) {
        MiuiSubscriptionControllerBase.removeActiveSubInfosCache(slotId);
    }

    public List<SubscriptionInfo> getActiveSubInfoListCache() {
        return MiuiSubscriptionControllerBase.getActiveSubInfoListCache();
    }

    public List<SubscriptionInfo> getOpportunisticSubInfoListCache() {
        return MiuiSubscriptionControllerBase.getOpportunisticSubInfoListCache();
    }

    public boolean carrierNameEqualsCache(int subId, String carrierName) {
        return MiuiSubscriptionControllerBase.carrierNameEqualsCache(subId, carrierName);
    }

    public boolean mccMncEqualsCache(int subId, int mcc, int mnc) {
        return MiuiSubscriptionControllerBase.mccMncEqualsCache(subId, mcc, mnc);
    }

    public boolean displayNameUsingSrcEqualsCache(int subId, String displayName, long nameSource) {
        return MiuiSubscriptionControllerBase.displayNameUsingSrcEqualsCache(subId, displayName, nameSource);
    }

    public void dumpDatabaseSubInfoList(PrintWriter pw, boolean isSubInfoReady, List<SubscriptionInfo> sirl) {
        MiuiSubscriptionControllerBase.dumpDatabaseSubInfoList(pw, isSubInfoReady, sirl);
    }

    public void updateMccMnc(String mccMnc, int subId) {
        MiuiSubscriptionControllerBase.updateMccMnc(mccMnc, subId);
    }

    public void updateCarrierText(String carrier, int subId) {
        MiuiSubscriptionControllerBase.updateCarrierText(carrier, subId);
    }

    public void updateSubInfoListCache(Context context, int subId, ContentValues value) {
        MiuiSubscriptionController.getInstance().updateSubInfoListCache(context, subId, value);
    }

    public void setUiccApplicationsEnabled(Context context, boolean enabled, int subId) {
        MiuiSubscriptionController.getInstance().setUiccApplicationsEnabled(context, enabled, subId);
    }

    public void addActiveSubInfosCache(SubscriptionInfo subInfo) {
        MiuiSubscriptionController.getInstance().addActiveSubInfosCache(subInfo);
    }

    public void addPhoneNumberToSubInfo(Context context, int phoneId) {
        MiuiSubscriptionControllerBase.addPhoneNumberToSubInfo(context, phoneId);
    }

    public boolean isUpdatedPhoneNumberByUser(Context context, int subId) {
        if (context == null) {
            return false;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean isUpdatedByUser = sp.getBoolean("update_phone_number_by_user" + subId, false);
        Rlog.d(LOG_TAG, "isUpdatedPhoneNumberByUser, subId = " + subId + ", isUpdatedByUser = " + isUpdatedByUser);
        return isUpdatedByUser;
    }

    public void notifyFeatureResolveSimReady(SubscriptionInfoInternal subInfo) {
        if (!TelephonyManager.isCustFeatureResolveEnabled() || subInfo == null) {
            Rlog.d(LOG_TAG, "notifyFeatureResolveSimReady return");
            return;
        }
        String mcc = subInfo.getMcc();
        String mnc = subInfo.getMnc();
        int phoneId = subInfo.getSimSlotIndex();
        Rlog.d(LOG_TAG, "notifyFeatureResolveSimReady phoneId " + phoneId + " mcc= " + mcc + " mnc= " + mnc);
        if (phoneId > -1 && !TextUtils.isEmpty(mcc) && !TextUtils.isEmpty(mnc)) {
            Bundle simInfo = new Bundle();
            simInfo.putInt("phoneId", phoneId);
            simInfo.putString("mcc", mcc);
            simInfo.putString("mnc", mnc);
            String iccid = subInfo.getIccId();
            String imsi = subInfo.getImsi();
            simInfo.putString("iccid", iccid);
            simInfo.putString("imsi", imsi);
            Phone phone = PhoneFactory.getPhone(phoneId);
            String gid1 = "";
            String gid2 = "";
            if (phone != null) {
                gid1 = phone.getGroupIdLevel1();
                gid2 = phone.getGroupIdLevel2();
                simInfo.putString("gid1", gid1);
                simInfo.putString("gid2", gid2);
            }
            String spn = TelephonyManagerEx.getDefault().getSimOperatorNameForSlot(phoneId);
            simInfo.putString("spn", spn);
            Rlog.d(LOG_TAG, "notifyFeatureResolveSimReady phoneId " + phoneId + " gid1= " + gid1 + " gid2= " + gid2 + " spn= " + spn);
            HyperOSCustFeatureResolve.notifyCustFeatureResolve("simReady", simInfo);
        }
    }
}
