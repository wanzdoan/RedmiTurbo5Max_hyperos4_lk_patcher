package com.android.internal.telephony.phonenumbers;

import com.android.internal.telephony.phonenumbers.internal.MatcherApi;
import com.android.internal.telephony.phonenumbers.internal.RegexBasedMatcher;
import com.android.internal.telephony.phonenumbers.metadata.DefaultMetadataDependenciesProvider;
import com.android.internal.telephony.phonenumbers.metadata.source.RegionMetadataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
public class ShortNumberInfo {
    private static final Set REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT;
    private final Map countryCallingCodeToRegionCodeMap = CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap();
    private final MatcherApi matcherApi;
    private final RegionMetadataSource shortNumberMetadataSource;
    private static final Logger logger = Logger.getLogger(ShortNumberInfo.class.getName());
    private static final ShortNumberInfo INSTANCE = new ShortNumberInfo(RegexBasedMatcher.create(), DefaultMetadataDependenciesProvider.getInstance().getShortNumberMetadataSource());

    public enum ShortNumberCost {
        TOLL_FREE,
        STANDARD_RATE,
        PREMIUM_RATE,
        UNKNOWN_COST
    }

    static {
        HashSet hashSet = new HashSet();
        REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT = hashSet;
        hashSet.add("BR");
        hashSet.add("CL");
        hashSet.add("NI");
    }

    public static ShortNumberInfo getInstance() {
        return INSTANCE;
    }

    ShortNumberInfo(MatcherApi matcherApi, RegionMetadataSource regionMetadataSource) {
        this.matcherApi = matcherApi;
        this.shortNumberMetadataSource = regionMetadataSource;
    }

    private List getRegionCodesForCountryCode(int i) {
        List arrayList = (List) this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(i));
        if (arrayList == null) {
            arrayList = new ArrayList(0);
        }
        return Collections.unmodifiableList(arrayList);
    }

    private boolean regionDialingFromMatchesNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        return getRegionCodesForCountryCode(phonenumber$PhoneNumber.getCountryCode()).contains(str);
    }

    private Phonemetadata$PhoneMetadata getShortNumberMetadataForRegion(String str) {
        if (str == null) {
            return null;
        }
        try {
            return this.shortNumberMetadataSource.getMetadataForRegion(str);
        } catch (IllegalArgumentException unused) {
            return null;
        }
    }

    public boolean isPossibleShortNumberForRegion(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        Phonemetadata$PhoneMetadata shortNumberMetadataForRegion;
        if (regionDialingFromMatchesNumber(phonenumber$PhoneNumber, str) && (shortNumberMetadataForRegion = getShortNumberMetadataForRegion(str)) != null) {
            return shortNumberMetadataForRegion.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(getNationalSignificantNumber(phonenumber$PhoneNumber).length()));
        }
        return false;
    }

    public boolean isPossibleShortNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        List regionCodesForCountryCode = getRegionCodesForCountryCode(phonenumber$PhoneNumber.getCountryCode());
        int length = getNationalSignificantNumber(phonenumber$PhoneNumber).length();
        Iterator it = regionCodesForCountryCode.iterator();
        while (it.hasNext()) {
            Phonemetadata$PhoneMetadata shortNumberMetadataForRegion = getShortNumberMetadataForRegion((String) it.next());
            if (shortNumberMetadataForRegion != null && shortNumberMetadataForRegion.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(length))) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidShortNumberForRegion(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        Phonemetadata$PhoneMetadata shortNumberMetadataForRegion;
        if (!regionDialingFromMatchesNumber(phonenumber$PhoneNumber, str) || (shortNumberMetadataForRegion = getShortNumberMetadataForRegion(str)) == null) {
            return false;
        }
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        if (matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getGeneralDesc())) {
            return matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getShortCode());
        }
        return false;
    }

    public boolean isValidShortNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        List regionCodesForCountryCode = getRegionCodesForCountryCode(phonenumber$PhoneNumber.getCountryCode());
        String regionCodeForShortNumberFromRegionList = getRegionCodeForShortNumberFromRegionList(phonenumber$PhoneNumber, regionCodesForCountryCode);
        if (regionCodesForCountryCode.size() <= 1 || regionCodeForShortNumberFromRegionList == null) {
            return isValidShortNumberForRegion(phonenumber$PhoneNumber, regionCodeForShortNumberFromRegionList);
        }
        return true;
    }

    public ShortNumberCost getExpectedCostForRegion(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        if (!regionDialingFromMatchesNumber(phonenumber$PhoneNumber, str)) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        Phonemetadata$PhoneMetadata shortNumberMetadataForRegion = getShortNumberMetadataForRegion(str);
        if (shortNumberMetadataForRegion == null) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        if (!shortNumberMetadataForRegion.getGeneralDesc().getPossibleLengthList().contains(Integer.valueOf(nationalSignificantNumber.length()))) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        if (matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getPremiumRate())) {
            return ShortNumberCost.PREMIUM_RATE;
        }
        if (matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getStandardRate())) {
            return ShortNumberCost.STANDARD_RATE;
        }
        if (matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getTollFree())) {
            return ShortNumberCost.TOLL_FREE;
        }
        if (isEmergencyNumber(nationalSignificantNumber, str)) {
            return ShortNumberCost.TOLL_FREE;
        }
        return ShortNumberCost.UNKNOWN_COST;
    }

    public ShortNumberCost getExpectedCost(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        List regionCodesForCountryCode = getRegionCodesForCountryCode(phonenumber$PhoneNumber.getCountryCode());
        if (regionCodesForCountryCode.size() == 0) {
            return ShortNumberCost.UNKNOWN_COST;
        }
        if (regionCodesForCountryCode.size() == 1) {
            return getExpectedCostForRegion(phonenumber$PhoneNumber, (String) regionCodesForCountryCode.get(0));
        }
        ShortNumberCost shortNumberCost = ShortNumberCost.TOLL_FREE;
        Iterator it = regionCodesForCountryCode.iterator();
        while (it.hasNext()) {
            ShortNumberCost expectedCostForRegion = getExpectedCostForRegion(phonenumber$PhoneNumber, (String) it.next());
            int iOrdinal = expectedCostForRegion.ordinal();
            if (iOrdinal != 0) {
                if (iOrdinal != 1) {
                    if (iOrdinal == 2) {
                        return ShortNumberCost.PREMIUM_RATE;
                    }
                    if (iOrdinal == 3) {
                        shortNumberCost = ShortNumberCost.UNKNOWN_COST;
                    } else {
                        logger.log(Level.SEVERE, "Unrecognised cost for region: " + expectedCostForRegion);
                    }
                } else if (shortNumberCost != ShortNumberCost.UNKNOWN_COST) {
                    shortNumberCost = ShortNumberCost.STANDARD_RATE;
                }
            }
        }
        return shortNumberCost;
    }

    private String getRegionCodeForShortNumberFromRegionList(Phonenumber$PhoneNumber phonenumber$PhoneNumber, List list) {
        if (list.size() == 0) {
            return null;
        }
        if (list.size() == 1) {
            return (String) list.get(0);
        }
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Phonemetadata$PhoneMetadata shortNumberMetadataForRegion = getShortNumberMetadataForRegion(str);
            if (shortNumberMetadataForRegion != null && matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getShortCode())) {
                return str;
            }
        }
        return null;
    }

    public boolean connectsToEmergencyNumber(String str, String str2) {
        return matchesEmergencyNumberHelper(str, str2, true);
    }

    public boolean isEmergencyNumber(CharSequence charSequence, String str) {
        return matchesEmergencyNumberHelper(charSequence, str, false);
    }

    private boolean matchesEmergencyNumberHelper(CharSequence charSequence, String str, boolean z) {
        Phonemetadata$PhoneMetadata shortNumberMetadataForRegion;
        CharSequence charSequenceExtractPossibleNumber = PhoneNumberUtil.extractPossibleNumber(charSequence);
        boolean z2 = false;
        if (PhoneNumberUtil.PLUS_CHARS_PATTERN.matcher(charSequenceExtractPossibleNumber).lookingAt() || (shortNumberMetadataForRegion = getShortNumberMetadataForRegion(str)) == null || !shortNumberMetadataForRegion.hasEmergency()) {
            return false;
        }
        String strNormalizeDigitsOnly = PhoneNumberUtil.normalizeDigitsOnly(charSequenceExtractPossibleNumber);
        if (z && !REGIONS_WHERE_EMERGENCY_NUMBERS_MUST_BE_EXACT.contains(str)) {
            z2 = true;
        }
        return this.matcherApi.matchNationalNumber(strNormalizeDigitsOnly, shortNumberMetadataForRegion.getEmergency(), z2);
    }

    public boolean isCarrierSpecific(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        String regionCodeForShortNumberFromRegionList = getRegionCodeForShortNumberFromRegionList(phonenumber$PhoneNumber, getRegionCodesForCountryCode(phonenumber$PhoneNumber.getCountryCode()));
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        Phonemetadata$PhoneMetadata shortNumberMetadataForRegion = getShortNumberMetadataForRegion(regionCodeForShortNumberFromRegionList);
        return shortNumberMetadataForRegion != null && matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getCarrierSpecific());
    }

    public boolean isCarrierSpecificForRegion(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        if (!regionDialingFromMatchesNumber(phonenumber$PhoneNumber, str)) {
            return false;
        }
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        Phonemetadata$PhoneMetadata shortNumberMetadataForRegion = getShortNumberMetadataForRegion(str);
        return shortNumberMetadataForRegion != null && matchesPossibleNumberAndNationalNumber(nationalSignificantNumber, shortNumberMetadataForRegion.getCarrierSpecific());
    }

    public boolean isSmsServiceForRegion(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        Phonemetadata$PhoneMetadata shortNumberMetadataForRegion;
        return regionDialingFromMatchesNumber(phonenumber$PhoneNumber, str) && (shortNumberMetadataForRegion = getShortNumberMetadataForRegion(str)) != null && matchesPossibleNumberAndNationalNumber(getNationalSignificantNumber(phonenumber$PhoneNumber), shortNumberMetadataForRegion.getSmsServices());
    }

    private static String getNationalSignificantNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        StringBuilder sb = new StringBuilder();
        if (phonenumber$PhoneNumber.isItalianLeadingZero()) {
            char[] cArr = new char[phonenumber$PhoneNumber.getNumberOfLeadingZeros()];
            Arrays.fill(cArr, '0');
            sb.append(new String(cArr));
        }
        sb.append(phonenumber$PhoneNumber.getNationalNumber());
        return sb.toString();
    }

    private boolean matchesPossibleNumberAndNationalNumber(String str, Phonemetadata$PhoneNumberDesc phonemetadata$PhoneNumberDesc) {
        if (phonemetadata$PhoneNumberDesc.getPossibleLengthCount() <= 0 || phonemetadata$PhoneNumberDesc.getPossibleLengthList().contains(Integer.valueOf(str.length()))) {
            return this.matcherApi.matchNationalNumber(str, phonemetadata$PhoneNumberDesc, false);
        }
        return false;
    }
}
