package com.android.services.telephony.rcs.validator;

import android.telephony.ims.SipMessage;

/* JADX INFO: loaded from: classes.dex */
public interface SipMessageValidator {
    ValidationResult validate(SipMessage sipMessage);

    default SipMessageValidator andThen(final SipMessageValidator sipMessageValidator) {
        return new SipMessageValidator() { // from class: com.android.services.telephony.rcs.validator.SipMessageValidator$$ExternalSyntheticLambda0
            @Override // com.android.services.telephony.rcs.validator.SipMessageValidator
            public final ValidationResult validate(SipMessage sipMessage) {
                return this.f$0.lambda$andThen$0(sipMessageValidator, sipMessage);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* synthetic */ default ValidationResult lambda$andThen$0(SipMessageValidator sipMessageValidator, SipMessage sipMessage) {
        ValidationResult validationResultValidate = validate(sipMessage);
        return !validationResultValidate.isValidated ? validationResultValidate : sipMessageValidator.validate(sipMessage);
    }
}
