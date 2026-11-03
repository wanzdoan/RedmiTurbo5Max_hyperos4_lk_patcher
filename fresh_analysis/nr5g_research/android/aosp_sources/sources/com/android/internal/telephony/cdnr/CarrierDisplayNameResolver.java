package com.android.internal.telephony.cdnr;

import android.R;
import android.content.Context;
import android.content.res.Configuration;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.internal.telephony.GsmCdmaPhone;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.RuimRecords;
import com.android.internal.telephony.uicc.SIMRecords;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class CarrierDisplayNameResolver {
    private static final CarrierDisplayNameConditionRule DEFAULT_CARRIER_DISPLAY_NAME_RULE = new CarrierDisplayNameConditionRule(0);
    private static final List EF_SOURCE_PRIORITY = Arrays.asList(2, 1, 10, 3, 4, 5, 6, 7, 8, 9);
    private final CarrierConfigManager mCCManager;
    private CarrierDisplayNameData mCarrierDisplayNameData;
    private final Context mContext;
    private final SparseArray mEf = new SparseArray();
    private final LocalLog mLocalLog = new LocalLog(32);
    private final GsmCdmaPhone mPhone;

    public CarrierDisplayNameResolver(GsmCdmaPhone gsmCdmaPhone) {
        Context context = gsmCdmaPhone.getContext();
        this.mContext = context;
        this.mPhone = gsmCdmaPhone;
        this.mCCManager = (CarrierConfigManager) context.getSystemService("carrier_config");
    }

    public void updateEfFromRuim(RuimRecords ruimRecords) {
        int sourcePriority = getSourcePriority(6);
        SparseArray sparseArray = this.mEf;
        if (ruimRecords == null) {
            sparseArray.remove(sourcePriority);
        } else {
            sparseArray.put(sourcePriority, new RuimEfData(ruimRecords));
        }
    }

    public void updateEfFromUsim(SIMRecords sIMRecords) {
        int sourcePriority = getSourcePriority(3);
        SparseArray sparseArray = this.mEf;
        if (sIMRecords == null) {
            sparseArray.remove(sourcePriority);
        } else {
            sparseArray.put(sourcePriority, new UsimEfData(sIMRecords));
        }
    }

    public void updateEfFromCarrierConfig(PersistableBundle persistableBundle) {
        int sourcePriority = getSourcePriority(1);
        SparseArray sparseArray = this.mEf;
        if (persistableBundle == null) {
            sparseArray.remove(sourcePriority);
        } else {
            sparseArray.put(sourcePriority, new CarrierConfigEfData(persistableBundle));
        }
    }

    public void updateEfForBrandOverride(String str) {
        int sourcePriority = getSourcePriority(2);
        boolean zIsEmpty = TextUtils.isEmpty(str);
        SparseArray sparseArray = this.mEf;
        if (zIsEmpty) {
            sparseArray.remove(sourcePriority);
        } else {
            sparseArray.put(sourcePriority, new BrandOverrideEfData(str, getServiceState().getOperatorNumeric()));
        }
    }

    public CarrierDisplayNameData getCarrierDisplayNameData() {
        resolveCarrierDisplayName();
        return this.mCarrierDisplayNameData;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.mEf.size(); i++) {
            EfData efData = (EfData) this.mEf.valueAt(i);
            sb.append("{spnDisplayCondition = " + efData.getServiceProviderNameDisplayCondition(isRoaming()) + ", spn = " + efData.getServiceProviderName() + ", spdiList = " + efData.getServiceProviderDisplayInformation() + ", pnnList = " + efData.getPlmnNetworkNameList() + ", oplList = " + efData.getOperatorPlmnList() + ", ehplmn = " + efData.getEhplmnList() + "}, ");
        }
        sb.append(", roamingFromSS = " + getServiceState().getRoaming());
        sb.append(", registeredPLMN = " + getServiceState().getOperatorNumeric());
        return sb.toString();
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        indentingPrintWriter.println("CDNR:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("fields = " + toString());
        indentingPrintWriter.println("carrierDisplayNameData = " + this.mCarrierDisplayNameData);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("CDNR local log:");
        indentingPrintWriter.increaseIndent();
        this.mLocalLog.dump(indentingPrintWriter);
        indentingPrintWriter.decreaseIndent();
    }

    private PersistableBundle getCarrierConfig() {
        PersistableBundle configForSubId = this.mCCManager.getConfigForSubId(this.mPhone.getSubId());
        return configForSubId == null ? CarrierConfigManager.getDefaultConfig() : configForSubId;
    }

    private CarrierDisplayNameConditionRule getDisplayRule() {
        boolean zIsRoaming = isRoaming();
        for (int i = 0; i < this.mEf.size(); i++) {
            if (((EfData) this.mEf.valueAt(i)).getServiceProviderNameDisplayCondition(zIsRoaming) != -1) {
                return new CarrierDisplayNameConditionRule(((EfData) this.mEf.valueAt(i)).getServiceProviderNameDisplayCondition(zIsRoaming));
            }
        }
        return DEFAULT_CARRIER_DISPLAY_NAME_RULE;
    }

    private List getEfSpdi() {
        for (int i = 0; i < this.mEf.size(); i++) {
            if (((EfData) this.mEf.valueAt(i)).getServiceProviderDisplayInformation() != null) {
                return ((EfData) this.mEf.valueAt(i)).getServiceProviderDisplayInformation();
            }
        }
        return Collections.EMPTY_LIST;
    }

    private String getEfSpn() {
        for (int i = 0; i < this.mEf.size(); i++) {
            if (!TextUtils.isEmpty(((EfData) this.mEf.valueAt(i)).getServiceProviderName())) {
                return ((EfData) this.mEf.valueAt(i)).getServiceProviderName();
            }
        }
        return "";
    }

    private List getEfOpl() {
        for (int i = 0; i < this.mEf.size(); i++) {
            if (((EfData) this.mEf.valueAt(i)).getOperatorPlmnList() != null) {
                return ((EfData) this.mEf.valueAt(i)).getOperatorPlmnList();
            }
        }
        return Collections.EMPTY_LIST;
    }

    private List getEfPnn() {
        for (int i = 0; i < this.mEf.size(); i++) {
            if (((EfData) this.mEf.valueAt(i)).getPlmnNetworkNameList() != null) {
                return ((EfData) this.mEf.valueAt(i)).getPlmnNetworkNameList();
            }
        }
        return Collections.EMPTY_LIST;
    }

    private boolean isRoaming() {
        return getServiceState().getRoaming() && !getEfSpdi().contains(getServiceState().getOperatorNumeric());
    }

    private CarrierDisplayNameData getCarrierDisplayNameFromEf() {
        String plmnNetworkName;
        CarrierDisplayNameConditionRule displayRule = getDisplayRule();
        String operatorAlpha = getServiceState().getOperatorAlpha();
        String operatorNumeric = getServiceState().getOperatorNumeric();
        String efSpn = getEfSpn();
        List efOpl = getEfOpl();
        List efPnn = getEfPnn();
        if (isRoaming()) {
            plmnNetworkName = operatorAlpha;
        } else if (efOpl.isEmpty()) {
            plmnNetworkName = efPnn.isEmpty() ? "" : getPlmnNetworkName((IccRecords.PlmnNetworkName) efPnn.get(0));
        } else {
            plmnNetworkName = null;
        }
        if (TextUtils.isEmpty(plmnNetworkName)) {
            if (TextUtils.isEmpty(operatorAlpha)) {
                operatorAlpha = operatorNumeric;
            }
            plmnNetworkName = operatorAlpha;
        }
        return new CarrierDisplayNameData.Builder().setSpn(efSpn).setDataSpn(efSpn).setShowSpn(displayRule.shouldShowSpn(efSpn)).setPlmn(plmnNetworkName).setShowPlmn(TextUtils.isEmpty(efSpn) || displayRule.shouldShowPlmn(plmnNetworkName)).build();
    }

    private CarrierDisplayNameData getCarrierDisplayNameFromWifiCallingOverride(CarrierDisplayNameData carrierDisplayNameData) {
        PersistableBundle carrierConfig = getCarrierConfig();
        boolean z = carrierConfig.getBoolean("wfc_spn_use_root_locale");
        Context contextCreateConfigurationContext = this.mContext;
        if (z) {
            Configuration configuration = contextCreateConfigurationContext.getResources().getConfiguration();
            configuration.setLocale(Locale.ROOT);
            contextCreateConfigurationContext = this.mContext.createConfigurationContext(configuration);
        }
        WfcCarrierNameFormatter wfcCarrierNameFormatter = new WfcCarrierNameFormatter(carrierConfig, contextCreateConfigurationContext.getResources().getStringArray(17236327), getServiceState().getState() == 3);
        String voiceName = wfcCarrierNameFormatter.formatVoiceName(carrierDisplayNameData.getSpn());
        String dataName = wfcCarrierNameFormatter.formatDataName(carrierDisplayNameData.getSpn());
        List efPnn = getEfPnn();
        String plmnNetworkName = efPnn.isEmpty() ? "" : getPlmnNetworkName((IccRecords.PlmnNetworkName) efPnn.get(0));
        if (TextUtils.isEmpty(plmnNetworkName)) {
            plmnNetworkName = carrierDisplayNameData.getPlmn();
        }
        String voiceName2 = wfcCarrierNameFormatter.formatVoiceName(plmnNetworkName);
        if (TextUtils.isEmpty(voiceName) || TextUtils.isEmpty(dataName)) {
            return !TextUtils.isEmpty(voiceName2) ? new CarrierDisplayNameData.Builder().setPlmn(voiceName2).setShowPlmn(true).build() : carrierDisplayNameData;
        }
        return new CarrierDisplayNameData.Builder().setSpn(voiceName).setDataSpn(dataName).setShowSpn(true).build();
    }

    private CarrierDisplayNameData getCarrierDisplayNameFromCrossSimCallingOverride(CarrierDisplayNameData carrierDisplayNameData) {
        PersistableBundle carrierConfig = getCarrierConfig();
        int i = carrierConfig.getInt("cross_sim_spn_format_int");
        String[] stringArray = SubscriptionManager.getResourcesForSubId(this.mPhone.getContext(), this.mPhone.getSubId(), carrierConfig.getBoolean("wfc_spn_use_root_locale")).getStringArray(17236261);
        if (i < 0 || i >= stringArray.length) {
            Rlog.e("CDNR", "updateSpnDisplay: KEY_CROSS_SIM_SPN_FORMAT_INT out of bounds: " + i);
            i = 0;
        }
        String str = stringArray[i];
        List efPnn = getEfPnn();
        String plmnNetworkName = efPnn.isEmpty() ? "" : getPlmnNetworkName((IccRecords.PlmnNetworkName) efPnn.get(0));
        String spn = carrierDisplayNameData.getSpn();
        String plmn = TextUtils.isEmpty(plmnNetworkName) ? carrierDisplayNameData.getPlmn() : plmnNetworkName;
        String str2 = String.format(str, spn);
        String str3 = String.format(str, plmnNetworkName);
        if (TextUtils.isEmpty(spn) || TextUtils.isEmpty(str2)) {
            return (TextUtils.isEmpty(plmn) || TextUtils.isEmpty(str3)) ? carrierDisplayNameData : new CarrierDisplayNameData.Builder().setPlmn(str3).setShowPlmn(true).build();
        }
        return new CarrierDisplayNameData.Builder().setSpn(str2).setDataSpn(str2).setShowSpn(true).build();
    }

    private CarrierDisplayNameData getOutOfServiceDisplayName(CarrierDisplayNameData carrierDisplayNameData) {
        String string;
        boolean z = this.mPhone.getServiceStateTracker().shouldForceDisplayNoService() && !(this.mPhone.getUiccCardApplication() != null && this.mPhone.getUiccCardApplication().getState() == IccCardApplicationStatus.AppState.APPSTATE_READY);
        ServiceState serviceState = getServiceState();
        boolean z2 = serviceState.isEmergencyOnly() || Phone.isEmergencyCallOnly();
        if (serviceState.getState() == 3 && !z && !z2) {
            string = null;
        } else if (z || !z2) {
            string = this.mContext.getResources().getString(R.string.network_partial_connectivity);
        } else {
            string = this.mContext.getResources().getString(R.string.global_action_settings);
        }
        return new CarrierDisplayNameData.Builder().setSpn(carrierDisplayNameData.getSpn()).setDataSpn(carrierDisplayNameData.getDataSpn()).setShowSpn(carrierDisplayNameData.shouldShowSpn()).setPlmn(string).setShowPlmn(true).build();
    }

    private void resolveCarrierDisplayName() {
        CarrierDisplayNameData carrierDisplayNameFromEf = getCarrierDisplayNameFromEf();
        ServiceState serviceState = getServiceState();
        int combinedRegState = this.mPhone.getServiceStateTracker().getCombinedRegState(serviceState);
        boolean zIsWifiCallingEnabled = this.mPhone.isWifiCallingEnabled();
        Rlog.d("CDNR", "CarrierName from EF: " + carrierDisplayNameFromEf + " combinedRegState: " + ServiceState.rilServiceStateToString(combinedRegState) + " isWifiCallingEnabled: " + zIsWifiCallingEnabled + serviceState);
        if (this.mPhone.getImsPhone() != null && this.mPhone.getImsPhone().getImsRegistrationTech() == 2 && this.mPhone.isImsRegistered()) {
            carrierDisplayNameFromEf = getCarrierDisplayNameFromCrossSimCallingOverride(carrierDisplayNameFromEf);
            Rlog.d("CDNR", "CarrierName override by Cross-SIM Calling " + carrierDisplayNameFromEf);
        } else if (combinedRegState == 0) {
            if (zIsWifiCallingEnabled && this.mPhone.isImsRegistered()) {
                carrierDisplayNameFromEf = getCarrierDisplayNameFromWifiCallingOverride(carrierDisplayNameFromEf);
                Rlog.d("CDNR", "CarrierName override by wifi-calling " + carrierDisplayNameFromEf);
            } else if (getServiceState().getState() == 3) {
                carrierDisplayNameFromEf = getOutOfServiceDisplayName(carrierDisplayNameFromEf);
                Rlog.d("CDNR", "Out of service carrierName (APM) " + carrierDisplayNameFromEf);
            }
        } else {
            carrierDisplayNameFromEf = getOutOfServiceDisplayName(carrierDisplayNameFromEf);
            Rlog.d("CDNR", "Out of service carrierName " + carrierDisplayNameFromEf);
        }
        if (!Objects.equals(this.mCarrierDisplayNameData, carrierDisplayNameFromEf)) {
            this.mLocalLog.log(String.format("ResolveCarrierDisplayName: %s", carrierDisplayNameFromEf.toString()));
        }
        this.mCarrierDisplayNameData = carrierDisplayNameFromEf;
    }

    private static String getPlmnNetworkName(IccRecords.PlmnNetworkName plmnNetworkName) {
        if (plmnNetworkName == null) {
            return "";
        }
        if (TextUtils.isEmpty(plmnNetworkName.fullName)) {
            return !TextUtils.isEmpty(plmnNetworkName.shortName) ? plmnNetworkName.shortName : "";
        }
        return plmnNetworkName.fullName;
    }

    private static int getSourcePriority(int i) {
        int iIndexOf = EF_SOURCE_PRIORITY.indexOf(Integer.valueOf(i));
        if (iIndexOf == -1) {
            return Integer.MAX_VALUE;
        }
        return iIndexOf;
    }

    private static final class CarrierDisplayNameConditionRule {
        private int mDisplayConditionBitmask;

        CarrierDisplayNameConditionRule(int i) {
            this.mDisplayConditionBitmask = i;
        }

        boolean shouldShowSpn(String str) {
            return !TextUtils.isEmpty(str) && ((this.mDisplayConditionBitmask & 2) == 2);
        }

        boolean shouldShowPlmn(String str) {
            return !TextUtils.isEmpty(str) && ((this.mDisplayConditionBitmask & 1) == 1);
        }

        public String toString() {
            return String.format("{ SPN_bit = %d, PLMN_bit = %d }", Integer.valueOf(this.mDisplayConditionBitmask & 2), Integer.valueOf(this.mDisplayConditionBitmask & 1));
        }
    }

    private ServiceState getServiceState() {
        return this.mPhone.getServiceStateTracker().getServiceState();
    }

    private static final class WfcCarrierNameFormatter {
        final String mDataFormat;
        final String mVoiceFormat;

        WfcCarrierNameFormatter(PersistableBundle persistableBundle, String[] strArr, boolean z) {
            int i = persistableBundle.getInt("wfc_spn_format_idx_int");
            int i2 = persistableBundle.getInt("wfc_data_spn_format_idx_int");
            int i3 = persistableBundle.getInt("wfc_flight_mode_spn_format_idx_int");
            if (i < 0 || i >= strArr.length) {
                Rlog.e("CDNR", "updateSpnDisplay: KEY_WFC_SPN_FORMAT_IDX_INT out of bounds: " + i);
                i = 0;
            }
            if (i2 < 0 || i2 >= strArr.length) {
                Rlog.e("CDNR", "updateSpnDisplay: KEY_WFC_DATA_SPN_FORMAT_IDX_INT out of bounds: " + i2);
                i2 = 0;
            }
            i = z ? (i3 < 0 || i3 >= strArr.length) ? i : i3 : i;
            this.mVoiceFormat = i != -1 ? strArr[i] : "";
            this.mDataFormat = i2 != -1 ? strArr[i2] : "";
        }

        public String formatVoiceName(String str) {
            return TextUtils.isEmpty(str) ? str : String.format(this.mVoiceFormat, str.trim());
        }

        public String formatDataName(String str) {
            return TextUtils.isEmpty(str) ? str : String.format(this.mDataFormat, str.trim());
        }
    }
}
