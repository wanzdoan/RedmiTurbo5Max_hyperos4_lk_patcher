package com.android.services.telephony.rcs.validator;

import android.telephony.ims.SipDelegateManager;
import android.telephony.ims.SipMessage;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.SipMessageParsingUtils;
import com.android.services.telephony.rcs.SipSessionTracker;
import com.android.services.telephony.rcs.SipSessionTracker$$ExternalSyntheticLambda0;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class OutgoingTransportStateValidator implements SipMessageValidator {
    private static final ArrayMap ENUM_TO_STRING_MAP;
    private Set mAllowedTags;
    private Set mDeniedTags;
    private Set mRestrictedFeatureTags;
    private final SipSessionTracker mSipSessionTracker;
    private int mState = 0;
    private int mReason = 2;

    static {
        ArrayMap arrayMap = new ArrayMap(3);
        ENUM_TO_STRING_MAP = arrayMap;
        arrayMap.append(0, "CLOSED");
        arrayMap.append(1, "RESTRICTED");
        arrayMap.append(2, "OPEN");
    }

    public OutgoingTransportStateValidator(SipSessionTracker sipSessionTracker) {
        Set set = Collections.EMPTY_SET;
        this.mAllowedTags = set;
        this.mDeniedTags = set;
        this.mSipSessionTracker = sipSessionTracker;
    }

    public void open(Set set, Set set2) {
        this.mState = 2;
        this.mReason = -1;
        this.mAllowedTags = (Set) set.stream().map(new OutgoingTransportStateValidator$$ExternalSyntheticLambda0()).map(new OutgoingTransportStateValidator$$ExternalSyntheticLambda1()).collect(Collectors.toSet());
        this.mDeniedTags = (Set) set2.stream().map(new OutgoingTransportStateValidator$$ExternalSyntheticLambda0()).map(new OutgoingTransportStateValidator$$ExternalSyntheticLambda1()).collect(Collectors.toSet());
        this.mRestrictedFeatureTags = null;
    }

    public void restrictFeatureTags(Set set) {
        this.mRestrictedFeatureTags = (Set) set.stream().map(new OutgoingTransportStateValidator$$ExternalSyntheticLambda0()).map(new OutgoingTransportStateValidator$$ExternalSyntheticLambda1()).collect(Collectors.toSet());
    }

    public void restrict(int i) {
        this.mState = 1;
        this.mReason = i;
    }

    public void close(int i) {
        this.mState = 0;
        this.mReason = i;
        this.mAllowedTags = Collections.EMPTY_SET;
    }

    @Override // com.android.services.telephony.rcs.validator.SipMessageValidator
    public ValidationResult validate(SipMessage sipMessage) {
        int i = this.mState;
        if (i == 0) {
            return new ValidationResult(this.mReason, "outgoing transport closed.");
        }
        if (i == 1) {
            return verifyRestrictedMessage(sipMessage);
        }
        if (i == 2) {
            return verifyOpenMessage(sipMessage);
        }
        Log.w("SipTransportC", "OutgoingTSV - warning, unexpected state");
        return ValidationResult.SUCCESS;
    }

    public Set getAllowedCallIds() {
        return (Set) Stream.concat(this.mSipSessionTracker.getEarlyDialogs().stream(), this.mSipSessionTracker.getConfirmedDialogs().stream()).map(new SipSessionTracker$$ExternalSyntheticLambda0()).collect(Collectors.toSet());
    }

    public String toString() {
        return "Outgoing Transport State: " + ((String) ENUM_TO_STRING_MAP.getOrDefault(Integer.valueOf(this.mState), String.valueOf(this.mState))) + ", reason: " + ((String) SipDelegateManager.MESSAGE_FAILURE_REASON_STRING_MAP.getOrDefault(Integer.valueOf(this.mReason), String.valueOf(this.mReason))) + ", allowed tags: " + this.mAllowedTags + ", restricted tags: " + this.mRestrictedFeatureTags + ", denied tags: " + this.mDeniedTags;
    }

    private ValidationResult verifyOpenMessage(SipMessage sipMessage) {
        if (!SipMessageParsingUtils.isSipRequest(sipMessage.getStartLine())) {
            return ValidationResult.SUCCESS;
        }
        if (this.mRestrictedFeatureTags == null) {
            return new ValidationResult(9, "no reg state from vendor");
        }
        final String[] strArrSplitStartLineAndVerify = SipMessageParsingUtils.splitStartLineAndVerify(sipMessage.getStartLine());
        if (strArrSplitStartLineAndVerify == null) {
            return new ValidationResult(3, "couldn't parse start line: " + sipMessage.getStartLine());
        }
        if (Arrays.stream(SipSessionTracker.SIP_REQUEST_DIALOG_START_METHODS).anyMatch(new Predicate() { // from class: com.android.services.telephony.rcs.validator.OutgoingTransportStateValidator$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return ((String) obj).equals(strArrSplitStartLineAndVerify[0].trim().toLowerCase(Locale.ROOT));
            }
        }) && !getAllowedCallIds().contains(sipMessage.getCallIdParameter())) {
            return validateMessageFeatureTag(sipMessage);
        }
        return ValidationResult.SUCCESS;
    }

    private ValidationResult validateMessageFeatureTag(SipMessage sipMessage) {
        Set set = (Set) SipMessageParsingUtils.getAcceptContactFeatureTags(sipMessage.getHeaderSection()).stream().map(new OutgoingTransportStateValidator$$ExternalSyntheticLambda1()).map(new OutgoingTransportStateValidator$$ExternalSyntheticLambda0()).collect(Collectors.toSet());
        long jCount = set.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.validator.OutgoingTransportStateValidator$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$validateMessageFeatureTag$1((String) obj);
            }
        }).count();
        long jCount2 = set.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.validator.OutgoingTransportStateValidator$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$validateMessageFeatureTag$2((String) obj);
            }
        }).count();
        long jCount3 = set.stream().filter(new Predicate() { // from class: com.android.services.telephony.rcs.validator.OutgoingTransportStateValidator$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$validateMessageFeatureTag$3((String) obj);
            }
        }).count();
        if (jCount2 > 0) {
            return new ValidationResult(6, "contains denied tags in Accept-Contact: " + set);
        }
        if (jCount3 > 0) {
            return new ValidationResult(6, "contains restricted tags in Accept-Contact: " + set);
        }
        if (jCount == 0) {
            return new ValidationResult(6, "No Accept-Contact feature tags are in accepted feature tag list: " + set);
        }
        return ValidationResult.SUCCESS;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$validateMessageFeatureTag$1(String str) {
        return this.mAllowedTags.contains(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$validateMessageFeatureTag$2(String str) {
        return this.mDeniedTags.contains(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$validateMessageFeatureTag$3(String str) {
        return this.mRestrictedFeatureTags.contains(str);
    }

    private ValidationResult verifyRestrictedMessage(SipMessage sipMessage) {
        if (!SipMessageParsingUtils.isSipRequest(sipMessage.getStartLine())) {
            return ValidationResult.SUCCESS;
        }
        String callIdParameter = sipMessage.getCallIdParameter();
        if (TextUtils.isEmpty(callIdParameter)) {
            return new ValidationResult(this.mReason, "empty call id");
        }
        if (!getAllowedCallIds().contains(callIdParameter)) {
            return new ValidationResult(this.mReason, "call id " + callIdParameter + " is not associated with any active sessions");
        }
        return ValidationResult.SUCCESS;
    }
}
