package com.android.services.telephony.rcs.validator;

import android.telephony.ims.SipMessage;
import android.util.Pair;
import com.android.internal.telephony.SipMessageParsingUtils;
import com.ot.pubsub.b.a;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class RestrictedOutgoingSubscribeValidator implements SipMessageValidator {
    private static final String[] RESTRICTED_EVENTS = {"presence"};

    @Override // com.android.services.telephony.rcs.validator.SipMessageValidator
    public ValidationResult validate(SipMessage sipMessage) {
        if (!SipMessageParsingUtils.isSipRequest(sipMessage.getStartLine())) {
            return ValidationResult.SUCCESS;
        }
        String[] strArrSplitStartLineAndVerify = SipMessageParsingUtils.splitStartLineAndVerify(sipMessage.getStartLine());
        if (strArrSplitStartLineAndVerify == null) {
            return new ValidationResult(3, "malformed start line: " + sipMessage.getStartLine());
        }
        if (!strArrSplitStartLineAndVerify[0].equalsIgnoreCase("subscribe")) {
            return ValidationResult.SUCCESS;
        }
        List headers = SipMessageParsingUtils.parseHeaders(sipMessage.getHeaderSection(), true, new String[]{a.b});
        if (headers.size() == 0) {
            return ValidationResult.SUCCESS;
        }
        if (headers.stream().map(new Function() { // from class: com.android.services.telephony.rcs.validator.RestrictedOutgoingSubscribeValidator$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return RestrictedOutgoingSubscribeValidator.m4504$r8$lambda$WasndJ4xcYpJKORplsTSrP0xNk((Pair) obj);
            }
        }).anyMatch(new Predicate() { // from class: com.android.services.telephony.rcs.validator.RestrictedOutgoingSubscribeValidator$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Arrays.asList(RestrictedOutgoingSubscribeValidator.RESTRICTED_EVENTS).contains(((String) obj).trim().toLowerCase(Locale.ROOT));
            }
        })) {
            return new ValidationResult(4, "matched a restricted header field: " + headers.stream().map(new Function() { // from class: com.android.services.telephony.rcs.validator.RestrictedOutgoingSubscribeValidator$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return RestrictedOutgoingSubscribeValidator.m4503$r8$lambda$EZPWOzHqGnH7yXWKA899xx400((Pair) obj);
                }
            }).collect(Collectors.toSet()));
        }
        return ValidationResult.SUCCESS;
    }

    /* JADX INFO: renamed from: $r8$lambda$WasndJ4xcYpJKORplsTSrP0x-Nk, reason: not valid java name */
    public static /* synthetic */ String m4504$r8$lambda$WasndJ4xcYpJKORplsTSrP0xNk(Pair pair) {
        return (String) pair.second;
    }

    /* JADX INFO: renamed from: $r8$lambda$EZPWOzHqGnH7yXWKA899xx4--00, reason: not valid java name */
    public static /* synthetic */ String m4503$r8$lambda$EZPWOzHqGnH7yXWKA899xx400(Pair pair) {
        return (String) pair.second;
    }
}
