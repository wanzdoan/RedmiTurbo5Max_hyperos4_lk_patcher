package com.android.services.telephony.rcs.validator;

import android.telephony.ims.SipMessage;
import com.android.internal.telephony.SipMessageParsingUtils;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class RestrictedOutgoingSipRequestValidator implements SipMessageValidator {
    private static final String[] IMS_SERVICE_HANDLED_REQUEST_METHODS = {"register", "options", "publish"};

    @Override // com.android.services.telephony.rcs.validator.SipMessageValidator
    public ValidationResult validate(SipMessage sipMessage) {
        String startLine = sipMessage.getStartLine();
        if (SipMessageParsingUtils.isSipRequest(startLine)) {
            final String[] strArrSplitStartLineAndVerify = SipMessageParsingUtils.splitStartLineAndVerify(startLine);
            if (strArrSplitStartLineAndVerify == null) {
                return new ValidationResult(3, "malformed start line: " + sipMessage.getStartLine());
            }
            if (Arrays.stream(IMS_SERVICE_HANDLED_REQUEST_METHODS).anyMatch(new Predicate() { // from class: com.android.services.telephony.rcs.validator.RestrictedOutgoingSipRequestValidator$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return strArrSplitStartLineAndVerify[0].toLowerCase(Locale.ROOT).contains((String) obj);
                }
            })) {
                return new ValidationResult(3, "restricted method: " + strArrSplitStartLineAndVerify[0]);
            }
        }
        return ValidationResult.SUCCESS;
    }
}
