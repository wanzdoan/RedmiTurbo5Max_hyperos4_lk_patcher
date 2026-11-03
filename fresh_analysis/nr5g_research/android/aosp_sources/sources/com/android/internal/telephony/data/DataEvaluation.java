package com.android.internal.telephony.data;

import android.telephony.data.DataProfile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class DataEvaluation {
    private final DataEvaluationReason mDataEvaluationReason;
    private final Set mDataDisallowedReasons = new HashSet();
    private DataAllowedReason mDataAllowedReason = DataAllowedReason.NONE;
    private DataProfile mCandidateDataProfile = null;
    private long mEvaluatedTime = 0;

    public enum DataAllowedReason {
        NONE,
        NORMAL,
        IN_VOICE_CALL,
        UNMETERED_USAGE,
        MMS_REQUEST,
        RESTRICTED_REQUEST,
        EMERGENCY_SUPL,
        EMERGENCY_REQUEST
    }

    public DataEvaluation(DataEvaluationReason dataEvaluationReason) {
        this.mDataEvaluationReason = dataEvaluationReason;
    }

    public void addDataDisallowedReason(DataDisallowedReason dataDisallowedReason) {
        this.mDataAllowedReason = DataAllowedReason.NONE;
        this.mDataDisallowedReasons.add(dataDisallowedReason);
        this.mEvaluatedTime = System.currentTimeMillis();
    }

    public void removeDataDisallowedReason(DataDisallowedReason dataDisallowedReason) {
        this.mDataDisallowedReasons.remove(dataDisallowedReason);
        this.mEvaluatedTime = System.currentTimeMillis();
    }

    /* JADX INFO: renamed from: $r8$lambda$AWCdpx4_UhK-iVruQBhDv06yclo, reason: not valid java name */
    public static /* synthetic */ boolean m693$r8$lambda$AWCdpx4_UhKiVruQBhDv06yclo(DataDisallowedReason dataDisallowedReason) {
        return !dataDisallowedReason.isHardReason();
    }

    public void removeSoftDisallowedReasons() {
        this.mDataDisallowedReasons.removeIf(new Predicate() { // from class: com.android.internal.telephony.data.DataEvaluation$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return DataEvaluation.m693$r8$lambda$AWCdpx4_UhKiVruQBhDv06yclo((DataEvaluation.DataDisallowedReason) obj);
            }
        });
        this.mEvaluatedTime = System.currentTimeMillis();
    }

    public void addDataAllowedReason(DataAllowedReason dataAllowedReason) {
        this.mDataDisallowedReasons.clear();
        if (dataAllowedReason.ordinal() > this.mDataAllowedReason.ordinal()) {
            this.mDataAllowedReason = dataAllowedReason;
        }
        this.mEvaluatedTime = System.currentTimeMillis();
    }

    public List<DataDisallowedReason> getDataDisallowedReasons() {
        return new ArrayList(this.mDataDisallowedReasons);
    }

    public DataAllowedReason getDataAllowedReason() {
        return this.mDataAllowedReason;
    }

    public void setCandidateDataProfile(DataProfile dataProfile) {
        this.mCandidateDataProfile = dataProfile;
    }

    public DataProfile getCandidateDataProfile() {
        return this.mCandidateDataProfile;
    }

    public boolean containsDisallowedReasons() {
        return !this.mDataDisallowedReasons.isEmpty();
    }

    public boolean contains(DataDisallowedReason dataDisallowedReason) {
        return this.mDataDisallowedReasons.contains(dataDisallowedReason);
    }

    public boolean containsOnly(DataDisallowedReason dataDisallowedReason) {
        return this.mDataDisallowedReasons.size() == 1 && contains(dataDisallowedReason);
    }

    public boolean isSubsetOf(DataDisallowedReason... dataDisallowedReasonArr) {
        int i = 0;
        for (DataDisallowedReason dataDisallowedReason : dataDisallowedReasonArr) {
            if (this.mDataDisallowedReasons.contains(dataDisallowedReason)) {
                i++;
            }
        }
        return i == this.mDataDisallowedReasons.size();
    }

    public boolean containsAny(DataDisallowedReason... dataDisallowedReasonArr) {
        for (DataDisallowedReason dataDisallowedReason : dataDisallowedReasonArr) {
            if (this.mDataDisallowedReasons.contains(dataDisallowedReason)) {
                return true;
            }
        }
        return false;
    }

    public boolean contains(DataAllowedReason dataAllowedReason) {
        return dataAllowedReason == this.mDataAllowedReason;
    }

    public boolean containsHardDisallowedReasons() {
        Iterator it = this.mDataDisallowedReasons.iterator();
        while (it.hasNext()) {
            if (((DataDisallowedReason) it.next()).isHardReason()) {
                return true;
            }
        }
        return false;
    }

    public enum DataEvaluationReason {
        NEW_REQUEST(false),
        DATA_CONFIG_CHANGED(true),
        SIM_LOADED(true),
        SIM_REMOVAL(true),
        DATA_PROFILES_CHANGED(true),
        DATA_SERVICE_STATE_CHANGED(true),
        DATA_ENABLED_CHANGED(true),
        DATA_ENABLED_OVERRIDE_CHANGED(true),
        ROAMING_ENABLED_CHANGED(true),
        VOICE_CALL_ENDED(true),
        DATA_RESTRICTED_CHANGED(true),
        DATA_NETWORK_CAPABILITIES_CHANGED(true),
        EMERGENCY_CALL_CHANGED(true),
        RETRY_AFTER_DISCONNECTED(true),
        DATA_RETRY(false),
        DATA_HANDOVER(true),
        PREFERRED_TRANSPORT_CHANGED(true),
        SLICE_CONFIG_CHANGED(true),
        SRVCC_STATE_CHANGED(true),
        SINGLE_DATA_NETWORK_ARBITRATION(true),
        EXTERNAL_QUERY(false),
        TAC_CHANGED(true),
        UNSATISFIED_REQUEST_DETACHED(true),
        CHECK_DATA_USAGE(false),
        DATA_DYNAMIC_CONFIG_CHANGED(true),
        SECURE_MODE_STATE_CHANGED(true),
        DDS_SWITCHED(true),
        VENDOR_SPECIFIC(true);

        private final boolean mIsConditionBased;

        public boolean isConditionBased() {
            return this.mIsConditionBased;
        }

        DataEvaluationReason(boolean z) {
            this.mIsConditionBased = z;
        }
    }

    public enum DataDisallowedReason {
        DATA_DISABLED(false),
        ROAMING_DISABLED(false),
        DEFAULT_DATA_UNSELECTED(false),
        MTK_FDN_ENABLED(true),
        NOT_IN_SERVICE(true),
        DATA_CONFIG_NOT_READY(true),
        SIM_NOT_READY(true),
        CONCURRENT_VOICE_DATA_NOT_ALLOWED(true),
        SERVICE_OPTION_NOT_SUPPORTED(true),
        DATA_RESTRICTED_BY_NETWORK(true),
        RADIO_POWER_OFF(true),
        PENDING_TEAR_DOWN_ALL(true),
        RADIO_DISABLED_BY_CARRIER(true),
        DATA_SERVICE_NOT_READY(true),
        NO_SUITABLE_DATA_PROFILE(true),
        DATA_NETWORK_TYPE_NOT_ALLOWED(true),
        CDMA_EMERGENCY_CALLBACK_MODE(true),
        RETRY_SCHEDULED(true),
        DATA_THROTTLED(true),
        DATA_PROFILE_INVALID(true),
        DATA_PROFILE_NOT_PREFERRED(true),
        NOT_ALLOWED_BY_POLICY(true),
        ILLEGAL_STATE(true),
        VOPS_NOT_SUPPORTED(true),
        ONLY_ALLOWED_SINGLE_NETWORK(true),
        DATA_SETTINGS_NOT_READY(true),
        DATA_RESTRICTED_BY_SECURE_MODE(true),
        DATA_RESTRICTED_BY_NETWORK_TYPE(true),
        HANDOVER_RETRY_STOPPED(true),
        DATA_LIMIT_REACHED(true),
        DATA_NETWORK_TRANSPORT_NOT_ALLOWED(true),
        UNSUPPORTED_NETWORK_CAPABILITIES(true),
        VENDOR_SPECIFIC(true);

        private final boolean mIsHardReason;

        public boolean isHardReason() {
            return this.mIsHardReason;
        }

        DataDisallowedReason(boolean z) {
            this.mIsHardReason = z;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Data evaluation: evaluation reason:");
        sb.append(this.mDataEvaluationReason);
        sb.append(", ");
        if (!this.mDataDisallowedReasons.isEmpty()) {
            sb.append("Data disallowed reasons:");
            for (DataDisallowedReason dataDisallowedReason : this.mDataDisallowedReasons) {
                sb.append(" ");
                sb.append(dataDisallowedReason);
            }
        } else {
            sb.append("Data allowed reason: ");
            sb.append(this.mDataAllowedReason);
        }
        sb.append(", candidate profile=");
        sb.append(this.mCandidateDataProfile);
        sb.append(", time=");
        sb.append(DataUtils.systemTimeToString(this.mEvaluatedTime));
        return sb.toString();
    }
}
