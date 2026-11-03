package com.android.internal.telephony;

import android.R;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccAccessRule;
import android.text.TextUtils;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.UiccController;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiSubscriptionControllerBase {
    private static final int EVENT_UPDATE_CARRIER_NAME = 0;
    private static final int EVENT_UPDATE_MCC_MNC = 1;
    private static final int EVENT_UPDATE_NUMBER = 2;
    private static final String LOG_TAG = "MiuiSubscriptionControllerBase";
    private static final String PREF_CARRIERS_NUMBER_PREFIX = "carrier_number_";
    protected static List<SubscriptionInfo> sActiveSubscriptionInfoList = new ArrayList();
    private static DatabaseHandler sDatabaseHandler;

    protected abstract SubscriptionInfo createSubscriptionInfo(int i, String str, int i2, CharSequence charSequence, CharSequence charSequence2, int i3, int i4, String str2, int i5, Bitmap bitmap, String str3, String str4, String str5, boolean z, UiccAccessRule[] uiccAccessRuleArr, String str6, int i6, boolean z2, String str7, boolean z3, int i7, int i8, int i9, String str8, UiccAccessRule[] uiccAccessRuleArr2, boolean z4, int i10, int i11);

    protected abstract UiccAccessRule[] getAccessRules(SubscriptionInfo subscriptionInfo);

    protected abstract String getAccessRulesTag();

    protected abstract UiccAccessRule[] getCarrierConfigAccessRules(SubscriptionInfo subscriptionInfo);

    protected abstract String getIsEmbeddedTag();

    protected abstract int getSimProvisioningStatus(SubscriptionInfo subscriptionInfo);

    protected abstract String getSimProvisioningStatusTag();

    protected abstract boolean isEmbedded(SubscriptionInfo subscriptionInfo);

    protected abstract void logdl(String str);

    static {
        HandlerThread handlerThread = new HandlerThread("MiuiSubscriptionControllerBase.Database");
        handlerThread.start();
        sDatabaseHandler = new DatabaseHandler(handlerThread.getLooper());
    }

    protected void addActiveSubInfosCache(SubscriptionInfo subInfo) {
        synchronized (MiuiSubscriptionControllerBase.class) {
            logdl("addActiveSubInfosCache subInfo: " + subInfo);
            sActiveSubscriptionInfoList.add(subInfo);
        }
    }

    protected static synchronized void clearActiveSubInfosCache() {
        sActiveSubscriptionInfoList.clear();
    }

    protected static synchronized void removeActiveSubInfosCache(int slotId) {
        int index = -1;
        int foundIndex = -1;
        for (SubscriptionInfo info : sActiveSubscriptionInfoList) {
            index++;
            if (info.getSimSlotIndex() == slotId) {
                foundIndex = index;
                break;
            }
        }
        logd("removeActiveSubInfosCache: slotId " + slotId + " foundIndex " + foundIndex);
        if (foundIndex >= 0 && foundIndex < sActiveSubscriptionInfoList.size()) {
            sActiveSubscriptionInfoList.remove(foundIndex);
        }
    }

    public void setUiccApplicationsEnabled(Context context, boolean z, int i) throws Throwable {
        ContentValues contentValues = new ContentValues(1);
        contentValues.put("uicc_applications_enabled", Integer.valueOf(z ? 1 : 0));
        updateSubInfoListCache(context, i, contentValues);
    }

    public void updateSubInfoListCache(Context context, int subId, ContentValues value) throws Throwable {
        String number;
        boolean isEmbedded;
        synchronized (MiuiSubscriptionControllerBase.class) {
            try {
                if (value != null) {
                    try {
                        if (value.size() != 0) {
                            SubscriptionInfo subInfo = null;
                            int index = -1;
                            for (SubscriptionInfo info : sActiveSubscriptionInfoList) {
                                index++;
                                if (info.getSubscriptionId() == subId) {
                                    subInfo = info;
                                    break;
                                }
                            }
                            if (subInfo != null) {
                                CharSequence displyName = subInfo.getDisplayName();
                                CharSequence carrierName = subInfo.getCarrierName();
                                String number2 = subInfo.getNumber();
                                int nameSource = subInfo.getDisplayNameSource();
                                int simSlotIndex = subInfo.getSimSlotIndex();
                                int iconTint = subInfo.getIconTint();
                                String mcc = subInfo.getMccString();
                                String mnc = subInfo.getMncString();
                                int dataRoaming = subInfo.getDataRoaming();
                                getSimProvisioningStatus(subInfo);
                                int index2 = index;
                                Bitmap iconBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_zen_mode_icon_hiking);
                                boolean isEmbedded2 = isEmbedded(subInfo);
                                UiccAccessRule[] accessRules = getAccessRules(subInfo);
                                UiccAccessRule[] carrierConfigAccessRules = getCarrierConfigAccessRules(subInfo);
                                String cardString = subInfo.getCardString();
                                int carrierId = subInfo.getCarrierId();
                                boolean isOpportunistic = subInfo.isOpportunistic();
                                String groupUUID = subInfo.getGroupUuid() != null ? subInfo.getGroupUuid().toString() : null;
                                int profileClass = subInfo.getProfileClass();
                                int subType = subInfo.getSubscriptionType();
                                SubscriptionInfo subInfo2 = subInfo;
                                String[] ehplmns = (String[]) subInfo2.getEhplmns().toArray(new String[subInfo2.getEhplmns().size()]);
                                String[] hplmns = (String[]) subInfo2.getHplmns().toArray(new String[subInfo2.getHplmns().size()]);
                                String groupOwner = subInfo2.getGroupOwner();
                                String countryIso = subInfo2.getCountryIso();
                                boolean areUiccApplicationsEnabled = subInfo2.areUiccApplicationsEnabled();
                                int portIndex = subInfo2.getPortIndex();
                                int usageSetting = subInfo2.getUsageSetting();
                                String[] hplmns2 = hplmns;
                                String[] hplmns3 = ehplmns;
                                boolean areUiccApplicationsEnabled2 = areUiccApplicationsEnabled;
                                int portIndex2 = usageSetting;
                                String mnc2 = mnc;
                                String mnc3 = cardString;
                                String cardString2 = countryIso;
                                String countryIso2 = groupOwner;
                                CharSequence displyName2 = displyName;
                                String key = number2;
                                CharSequence carrierName2 = carrierName;
                                String mcc2 = mcc;
                                int dataRoaming2 = dataRoaming;
                                for (String key2 : value.keySet()) {
                                    String[] hplmns4 = hplmns2;
                                    String number3 = key;
                                    if ("display_name".equals(key2)) {
                                        displyName2 = (String) value.get(key2);
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if ("carrier_name".equals(key2)) {
                                        carrierName2 = (String) value.get(key2);
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if (MiuiIccProvider.STR_NUMBER.equals(key2)) {
                                        key = (String) value.get(key2);
                                        hplmns2 = hplmns4;
                                    } else if ("name_source".equals(key2)) {
                                        nameSource = ((Integer) value.get(key2)).intValue();
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if ("sim_id".equals(key2)) {
                                        simSlotIndex = ((Integer) value.get(key2)).intValue();
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if ("color".equals(key2)) {
                                        iconTint = ((Integer) value.get(key2)).intValue();
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if ("mcc_string".equals(key2)) {
                                        mcc2 = (String) value.get(key2);
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if ("mnc_string".equals(key2)) {
                                        mnc2 = (String) value.get(key2);
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if ("data_roaming".equals(key2)) {
                                        dataRoaming2 = ((Integer) value.get(key2)).intValue();
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if (getSimProvisioningStatusTag().equals(key2)) {
                                        ((Integer) value.get(key2)).intValue();
                                        hplmns2 = hplmns4;
                                        key = number3;
                                    } else if (getIsEmbeddedTag().equals(key2)) {
                                        CharSequence displyName3 = displyName2;
                                        isEmbedded = ((Integer) value.get(key2)).intValue() == 1;
                                        if (isEmbedded) {
                                            hplmns2 = hplmns4;
                                            isEmbedded2 = isEmbedded;
                                            key = number3;
                                            displyName2 = displyName3;
                                        } else {
                                            accessRules = null;
                                            hplmns2 = hplmns4;
                                            isEmbedded2 = isEmbedded;
                                            key = number3;
                                            displyName2 = displyName3;
                                        }
                                    } else {
                                        CharSequence displyName4 = displyName2;
                                        if (getAccessRulesTag().equals(key2)) {
                                            accessRules = value.get(key2) == null ? null : UiccAccessRule.decodeRules((byte[]) value.get(key2));
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("card_id".equals(key2)) {
                                            mnc3 = (String) value.get(key2);
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("carrier_id".equals(key2)) {
                                            carrierId = ((Integer) value.get(key2)).intValue();
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("is_opportunistic".equals(key2)) {
                                            isEmbedded = ((Integer) value.get(key2)).intValue() == 1;
                                            hplmns2 = hplmns4;
                                            isOpportunistic = isEmbedded;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("group_uuid".equals(key2)) {
                                            groupUUID = (String) value.get(key2);
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("profile_class".equals(key2)) {
                                            profileClass = ((Integer) value.get(key2)).intValue();
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("subscription_type".equals(key2)) {
                                            subType = ((Integer) value.get(key2)).intValue();
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("hplmns".equals(key2)) {
                                            String hplmnsRaw = (String) value.get(key2);
                                            String[] hplmns5 = hplmnsRaw == null ? null : hplmnsRaw.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                                            hplmns2 = hplmns5;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("ehplmns".equals(key2)) {
                                            String ehplmnsRaw = (String) value.get(key2);
                                            String[] ehplmns2 = ehplmnsRaw == null ? null : ehplmnsRaw.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
                                            hplmns3 = ehplmns2;
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("group_owner".equals(key2)) {
                                            countryIso2 = (String) value.get(key2);
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("iso_country_code".equals(key2)) {
                                            cardString2 = (String) value.get(key2);
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("access_rules_from_carrier_configs".equals(key2)) {
                                            carrierConfigAccessRules = value.get(key2) == null ? null : UiccAccessRule.decodeRules((byte[]) value.get(key2));
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("uicc_applications_enabled".equals(key2)) {
                                            isEmbedded = ((Integer) value.get(key2)).intValue() == 1;
                                            hplmns2 = hplmns4;
                                            areUiccApplicationsEnabled2 = isEmbedded;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else if ("usage_setting".equals(key2)) {
                                            portIndex2 = ((Integer) value.get(key2)).intValue();
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        } else {
                                            hplmns2 = hplmns4;
                                            key = number3;
                                            displyName2 = displyName4;
                                        }
                                    }
                                }
                                String[] hplmns6 = hplmns2;
                                String number4 = key;
                                CharSequence displyName5 = displyName2;
                                int publicCardId = UiccController.getInstance().convertToPublicCardId(mnc3);
                                try {
                                    if (TextUtils.isEmpty(number4)) {
                                        try {
                                            number = getLine1Number(context, simSlotIndex, subInfo2.getIccId());
                                            if (!TextUtils.isEmpty(number)) {
                                            }
                                            boolean isEmbedded3 = isEmbedded2;
                                            String countryIso3 = cardString2;
                                            int index3 = carrierId;
                                            UiccAccessRule[] carrierConfigAccessRules2 = carrierConfigAccessRules;
                                            String cardString3 = mnc3;
                                            SubscriptionInfo newSubInfo = new SubscriptionInfo.Builder(createSubscriptionInfo(subInfo2.getSubscriptionId(), subInfo2.getIccId(), simSlotIndex, displyName5, carrierName2, nameSource, iconTint, number, dataRoaming2, iconBitmap, mcc2, mnc2, countryIso3, isEmbedded3, accessRules, cardString3, publicCardId, isOpportunistic, groupUUID, false, index3, profileClass, subType, countryIso2, carrierConfigAccessRules2, areUiccApplicationsEnabled2, portIndex, portIndex2)).setEhplmns(hplmns3).setHplmns(hplmns6).build();
                                            sActiveSubscriptionInfoList.set(index2, newSubInfo);
                                            logdl("updateActiveSubInfosCache subId: " + subId + ", subInfo: " + newSubInfo);
                                        } catch (Throwable th) {
                                            th = th;
                                        }
                                    }
                                    SubscriptionInfo newSubInfo2 = new SubscriptionInfo.Builder(createSubscriptionInfo(subInfo2.getSubscriptionId(), subInfo2.getIccId(), simSlotIndex, displyName5, carrierName2, nameSource, iconTint, number, dataRoaming2, iconBitmap, mcc2, mnc2, countryIso3, isEmbedded3, accessRules, cardString3, publicCardId, isOpportunistic, groupUUID, false, index3, profileClass, subType, countryIso2, carrierConfigAccessRules2, areUiccApplicationsEnabled2, portIndex, portIndex2)).setEhplmns(hplmns3).setHplmns(hplmns6).build();
                                    sActiveSubscriptionInfoList.set(index2, newSubInfo2);
                                    logdl("updateActiveSubInfosCache subId: " + subId + ", subInfo: " + newSubInfo2);
                                } catch (Throwable th2) {
                                    th = th2;
                                }
                                number = number4;
                                boolean isEmbedded4 = isEmbedded2;
                                String countryIso4 = cardString2;
                                int index4 = carrierId;
                                UiccAccessRule[] carrierConfigAccessRules3 = carrierConfigAccessRules;
                                String cardString4 = mnc3;
                            }
                            return;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
            }
        }
    }

    protected String getLine1Number(Context context, int phoneId, String iccId) {
        String number = null;
        if (!TextUtils.isEmpty(iccId)) {
            String numberPrefKey = PREF_CARRIERS_NUMBER_PREFIX + iccId;
            number = PreferenceManager.getDefaultSharedPreferences(context).getString(numberPrefKey, null);
        }
        if (number != null) {
            return number;
        }
        Phone phone = PhoneFactory.getPhone(phoneId);
        if (phone != null) {
            return phone.getLine1Number();
        }
        logd("getLine1Number phone is null for phoneId:" + phoneId);
        return null;
    }

    protected static synchronized List<SubscriptionInfo> getActiveSubInfoListCache() {
        if (sActiveSubscriptionInfoList.isEmpty()) {
            return null;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = new ArrayList<>();
        for (SubscriptionInfo info : sActiveSubscriptionInfoList) {
            if (info.getSimSlotIndex() >= 0 || info.getSubscriptionType() == 1) {
                activeSubscriptionInfoList.add(info);
            }
        }
        return activeSubscriptionInfoList.isEmpty() ? null : activeSubscriptionInfoList;
    }

    protected static synchronized List<SubscriptionInfo> getOpportunisticSubInfoListCache() {
        if (sActiveSubscriptionInfoList.isEmpty()) {
            return null;
        }
        List<SubscriptionInfo> activeSubscriptionInfoList = new ArrayList<>();
        for (SubscriptionInfo info : sActiveSubscriptionInfoList) {
            if (info.isOpportunistic() && (info.getSimSlotIndex() >= 0 || info.isEmbedded())) {
                activeSubscriptionInfoList.add(info);
            }
        }
        return activeSubscriptionInfoList.isEmpty() ? null : activeSubscriptionInfoList;
    }

    public static synchronized boolean carrierNameEqualsCache(int subId, String carrierName) {
        SubscriptionInfo info = getSubscriptionInfoForSubId(subId);
        if (info == null || !TextUtils.equals(carrierName, info.getCarrierName())) {
            return false;
        }
        logd("ignore setCarrierText which equals with cache, text:" + carrierName + ", subId:" + subId);
        return true;
    }

    protected static synchronized boolean mccMncEqualsCache(int subId, int mcc, int mnc) {
        SubscriptionInfo info = getSubscriptionInfoForSubId(subId);
        if (info == null || mcc != info.getMcc() || mnc != info.getMnc()) {
            return false;
        }
        logd("ignore setMccMnc which equals with cache, mcc/mnc:" + mcc + "/" + mnc + " subId:" + subId);
        return true;
    }

    protected static synchronized boolean displayNameUsingSrcEqualsCache(int subId, String displayName, long nameSource) {
        SubscriptionInfo info = getSubscriptionInfoForSubId(subId);
        if (info == null || !TextUtils.equals(displayName, info.getDisplayName()) || nameSource != info.getDisplayNameSource()) {
            return false;
        }
        logd("ignore setDisplayName which equals with cache, displayName:" + displayName + ", nameSource:" + nameSource + " subId:" + subId);
        return true;
    }

    private static synchronized SubscriptionInfo getSubscriptionInfoForSubId(int subId) {
        List<SubscriptionInfo> activeInfos = getActiveSubInfoListCache();
        if (activeInfos == null) {
            return null;
        }
        for (SubscriptionInfo info : activeInfos) {
            if (info != null && info.getSubscriptionId() == subId) {
                return info;
            }
        }
        return null;
    }

    protected static void dumpDatabaseSubInfoList(PrintWriter pw, boolean isSubInfoReady, List<SubscriptionInfo> sirl) {
        if (isSubInfoReady && sirl != null) {
            pw.println(" ActiveSubInfoList in the DB:");
            for (SubscriptionInfo entry : sirl) {
                pw.println("  " + entry.toString());
            }
        } else {
            pw.println(" ActiveSubInfoList in the DB: null");
        }
        pw.flush();
    }

    private static class DatabaseHandler extends Handler {
        public DatabaseHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            SubscriptionManagerService subService = SubscriptionManagerService.getInstance();
            if (subService == null) {
            }
            switch (msg.what) {
                case 0:
                    MiuiSubscriptionControllerBase.logd("EVENT_UPDATE_CARRIER_NAME handled ");
                    subService.setCarrierName(msg.arg1, (String) msg.obj);
                    break;
                case 1:
                    MiuiSubscriptionControllerBase.logd("EVENT_UPDATE_MCC_MNC handled ");
                    subService.setMccMnc(msg.arg1, (String) msg.obj);
                    break;
                case 2:
                    MiuiSubscriptionControllerBase.logd("EVENT_UPDATE_NUMBER handled ");
                    subService.setDisplayNumber((String) msg.obj, msg.arg1);
                    break;
            }
        }
    }

    protected static void updateMccMnc(String mccMnc, int subId) {
        sDatabaseHandler.obtainMessage(1, subId, -1, mccMnc).sendToTarget();
    }

    protected static void updateCarrierText(String carrier, int subId) {
        sDatabaseHandler.obtainMessage(0, subId, -1, carrier).sendToTarget();
    }

    protected static void addPhoneNumberToSubInfo(Context context, int phoneId) {
        int subId = SubscriptionManager.getSubscriptionId(phoneId);
        if (SubscriptionManager.isValidSubscriptionId(subId)) {
            SubscriptionManager subscriptionManager = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
            SubscriptionInfo subInfo = subscriptionManager.getActiveSubscriptionInfo(subId);
            if (subInfo != null && !TextUtils.isEmpty(subInfo.getNumber())) {
                return;
            }
            TelephonyManager tm = ((TelephonyManager) context.getSystemService("phone")).createForSubscriptionId(subId);
            String line1Number = tm.getLine1Number(subId);
            if (line1Number != null) {
                sDatabaseHandler.obtainMessage(2, subId, -1, line1Number).sendToTarget();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }
}
