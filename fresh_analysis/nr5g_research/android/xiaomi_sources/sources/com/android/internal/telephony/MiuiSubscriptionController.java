package com.android.internal.telephony;

import android.graphics.Bitmap;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.UiccAccessRule;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSubscriptionController extends MiuiSubscriptionControllerBase {
    private static MiuiSubscriptionController sInstance;

    public static synchronized MiuiSubscriptionController getInstance() {
        if (sInstance == null) {
            sInstance = new MiuiSubscriptionController();
        }
        return sInstance;
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    protected int getSimProvisioningStatus(SubscriptionInfo subInfo) {
        return -1;
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    protected String getSimProvisioningStatusTag() {
        return "";
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    protected boolean isEmbedded(SubscriptionInfo subInfo) {
        return subInfo.isEmbedded();
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    protected String getIsEmbeddedTag() {
        return "is_embedded";
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    protected UiccAccessRule[] getAccessRules(SubscriptionInfo subInfo) {
        if (!subInfo.isEmbedded() || subInfo.getAccessRules() == null || subInfo.getAccessRules().size() == 0) {
            return null;
        }
        UiccAccessRule[] rules = new UiccAccessRule[subInfo.getAccessRules().size()];
        return (UiccAccessRule[]) subInfo.getAccessRules().toArray(rules);
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    protected String getAccessRulesTag() {
        return "access_rules";
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    protected UiccAccessRule[] getCarrierConfigAccessRules(SubscriptionInfo subInfo) {
        List<UiccAccessRule> carrierConfigAccessRules = subInfo.getCarrierConfigAccessRules();
        if (carrierConfigAccessRules == null || carrierConfigAccessRules.size() == 0) {
            return null;
        }
        UiccAccessRule[] rules = new UiccAccessRule[carrierConfigAccessRules.size()];
        return (UiccAccessRule[]) carrierConfigAccessRules.toArray(rules);
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    public SubscriptionInfo createSubscriptionInfo(int id, String iccId, int simSlotIndex, CharSequence displayName, CharSequence carrierName, int nameSource, int iconTint, String number, int roaming, Bitmap icon, String mcc, String mnc, String countryIso, boolean isEmbedded, UiccAccessRule[] accessRules, String cardString, int cardId, boolean isOpportunistic, String groupUUID, boolean isGroupDisabled, int carrierId, int profileClass, int subType, String groupOwner, UiccAccessRule[] carrierConfigAccessRules, boolean areUiccApplicationsEnabled, int portIndex, int usageSetting) {
        return new SubscriptionInfo.Builder().setId(id).setIccId(iccId).setSimSlotIndex(simSlotIndex).setDisplayName(displayName).setCarrierName(carrierName).setDisplayNameSource(nameSource).setIconTint(iconTint).setNumber(number).setDataRoaming(roaming).setIcon(icon).setMcc(mcc).setMnc(mnc).setCountryIso(countryIso).setEmbedded(isEmbedded).setNativeAccessRules(accessRules).setCardString(cardString).setCardId(cardId).setOpportunistic(isOpportunistic).setGroupUuid(groupUUID).setGroupDisabled(isGroupDisabled).setCarrierId(carrierId).setProfileClass(profileClass).setType(subType).setGroupOwner(groupOwner).setCarrierConfigAccessRules(carrierConfigAccessRules).setUiccApplicationsEnabled(areUiccApplicationsEnabled).setPortIndex(portIndex).setUsageSetting(usageSetting).build();
    }

    @Override // com.android.internal.telephony.MiuiSubscriptionControllerBase
    protected void logdl(String msg) {
        Rlog.d("SubscriptionController", msg);
    }
}
