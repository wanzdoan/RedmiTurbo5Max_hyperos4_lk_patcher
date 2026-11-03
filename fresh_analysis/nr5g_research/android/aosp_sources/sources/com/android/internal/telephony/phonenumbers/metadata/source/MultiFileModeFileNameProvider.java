package com.android.internal.telephony.phonenumbers.metadata.source;

import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline1;

/* JADX INFO: loaded from: classes.dex */
public final class MultiFileModeFileNameProvider implements PhoneMetadataFileNameProvider {
    private final String phoneMetadataFileNamePrefix;

    public MultiFileModeFileNameProvider(String str) {
        this.phoneMetadataFileNamePrefix = str + "_";
    }

    @Override // com.android.internal.telephony.phonenumbers.metadata.source.PhoneMetadataFileNameProvider
    public String getFor(Object obj) {
        String string = obj.toString();
        if (!isAlphanumeric(string)) {
            RILUtils$$ExternalSyntheticBUOutline1.m("Invalid key: ", string);
            return null;
        }
        return this.phoneMetadataFileNamePrefix + obj;
    }

    private boolean isAlphanumeric(String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        int length = str.length();
        int iCharCount = 0;
        while (iCharCount < length) {
            int iCodePointAt = str.codePointAt(iCharCount);
            if (!Character.isLetterOrDigit(iCodePointAt)) {
                return false;
            }
            iCharCount += Character.charCount(iCodePointAt);
        }
        return true;
    }
}
