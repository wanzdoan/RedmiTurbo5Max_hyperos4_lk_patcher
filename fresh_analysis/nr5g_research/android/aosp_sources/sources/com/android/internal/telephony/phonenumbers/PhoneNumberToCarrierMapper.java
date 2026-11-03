package com.android.internal.telephony.phonenumbers;

import com.android.internal.telephony.phonenumbers.metadata.DefaultMetadataDependenciesProvider;
import com.android.internal.telephony.phonenumbers.prefixmapper.PrefixFileReader;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class PhoneNumberToCarrierMapper {
    private static PhoneNumberToCarrierMapper instance;
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    private final PrefixFileReader prefixFileReader;

    PhoneNumberToCarrierMapper(String str) {
        this.prefixFileReader = new PrefixFileReader(str);
    }

    public static synchronized PhoneNumberToCarrierMapper getInstance() {
        try {
            if (instance == null) {
                instance = new PhoneNumberToCarrierMapper(DefaultMetadataDependenciesProvider.getInstance().getCarrierDataDirectory());
            }
        } catch (Throwable th) {
            throw th;
        }
        return instance;
    }

    public String getNameForValidNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber, Locale locale) {
        return this.prefixFileReader.getDescriptionForNumber(phonenumber$PhoneNumber, locale.getLanguage(), "", locale.getCountry());
    }

    public String getNameForNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber, Locale locale) {
        if (isMobile(this.phoneUtil.getNumberType(phonenumber$PhoneNumber))) {
            return getNameForValidNumber(phonenumber$PhoneNumber, locale);
        }
        return "";
    }

    public String getSafeDisplayName(Phonenumber$PhoneNumber phonenumber$PhoneNumber, Locale locale) {
        PhoneNumberUtil phoneNumberUtil = this.phoneUtil;
        if (phoneNumberUtil.isMobileNumberPortableRegion(phoneNumberUtil.getRegionCodeForNumber(phonenumber$PhoneNumber))) {
            return "";
        }
        return getNameForNumber(phonenumber$PhoneNumber, locale);
    }

    private boolean isMobile(PhoneNumberUtil.PhoneNumberType phoneNumberType) {
        return phoneNumberType == PhoneNumberUtil.PhoneNumberType.MOBILE || phoneNumberType == PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE || phoneNumberType == PhoneNumberUtil.PhoneNumberType.PAGER;
    }
}
