package com.android.services.telephony.rcs.validator;

import android.telephony.ims.SipMessage;
import com.android.internal.telephony.SipMessageParsingUtils;

/* JADX INFO: loaded from: classes.dex */
public class MalformedSipMessageValidator implements SipMessageValidator {
    @Override // com.android.services.telephony.rcs.validator.SipMessageValidator
    public ValidationResult validate(SipMessage sipMessage) {
        if (!SipMessageParsingUtils.isSipRequest(sipMessage.getStartLine()) && !SipMessageParsingUtils.isSipResponse(sipMessage.getStartLine())) {
            return new ValidationResult(3, "malformed start line: " + sipMessage.getStartLine());
        }
        return ValidationResult.SUCCESS;
    }
}
