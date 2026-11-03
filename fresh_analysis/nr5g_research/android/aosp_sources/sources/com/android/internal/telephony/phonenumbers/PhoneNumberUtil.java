package com.android.internal.telephony.phonenumbers;

import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.RILUtils$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.d2d.DtmfTransport;
import com.android.internal.telephony.phonenumbers.internal.MatcherApi;
import com.android.internal.telephony.phonenumbers.internal.RegexBasedMatcher;
import com.android.internal.telephony.phonenumbers.internal.RegexCache;
import com.android.internal.telephony.phonenumbers.metadata.DefaultMetadataDependenciesProvider;
import com.android.internal.telephony.phonenumbers.metadata.source.MetadataSource;
import com.android.internal.telephony.phonenumbers.metadata.source.MetadataSourceImpl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class PhoneNumberUtil {
    private static final Map ALL_PLUS_NUMBER_GROUPING_SYMBOLS;
    private static final String ALPHANUM;
    private static final Map ALPHA_MAPPINGS;
    private static final Map ALPHA_PHONE_MAPPINGS;
    private static final Pattern CAPTURING_DIGIT_PATTERN;
    private static final Set COUNTRIES_WITHOUT_NATIONAL_PREFIX_WITH_AREA_CODES;
    private static final Map DIALLABLE_CHAR_MAPPINGS;
    private static final Pattern EXTN_PATTERN;
    static final String EXTN_PATTERNS_FOR_MATCHING;
    private static final String EXTN_PATTERNS_FOR_PARSING;
    private static final Pattern FIRST_GROUP_ONLY_PREFIX_PATTERN;
    private static final Pattern FIRST_GROUP_PATTERN;
    private static final Set GEO_MOBILE_COUNTRIES;
    private static final Set GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES;
    private static final Map MOBILE_TOKEN_MAPPINGS;
    static final Pattern NON_DIGITS_PATTERN;
    static final Pattern PLUS_CHARS_PATTERN;
    public static final String REGION_CODE_FOR_NON_GEO_ENTITY = "001";
    private static final String RFC3966_DOMAINLABEL;
    private static final String RFC3966_DOMAINNAME;
    static final Pattern RFC3966_DOMAINNAME_PATTERN;
    static final Pattern RFC3966_GLOBAL_NUMBER_DIGITS_PATTERN;
    private static final String RFC3966_TOPLABEL;
    static final Pattern SECOND_NUMBER_START_PATTERN;
    private static final Pattern SEPARATOR_PATTERN;
    private static final Pattern SINGLE_INTERNATIONAL_PREFIX;
    static final Pattern UNWANTED_END_CHAR_PATTERN;
    private static final String VALID_ALPHA;
    private static final Pattern VALID_ALPHA_PHONE_PATTERN;
    private static final String VALID_PHONE_NUMBER;
    private static final Pattern VALID_PHONE_NUMBER_PATTERN;
    private static final Pattern VALID_START_CHAR_PATTERN;
    private static PhoneNumberUtil instance;
    private static final Logger logger = Logger.getLogger(PhoneNumberUtil.class.getName());
    private final Map countryCallingCodeToRegionCodeMap;
    private final MetadataSource metadataSource;
    private final MatcherApi matcherApi = RegexBasedMatcher.create();
    private final Set nanpaRegions = new HashSet(35);
    private final RegexCache regexCache = new RegexCache(100);
    private final Set supportedRegions = new HashSet(320);
    private final Set countryCodesForNonGeographicalRegion = new HashSet();

    public enum Leniency {
        POSSIBLE { // from class: com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency.1
            @Override // com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency
            boolean verify(Phonenumber$PhoneNumber phonenumber$PhoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil, PhoneNumberMatcher phoneNumberMatcher) {
                return phoneNumberUtil.isPossibleNumber(phonenumber$PhoneNumber);
            }
        },
        VALID { // from class: com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency.2
            @Override // com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency
            boolean verify(Phonenumber$PhoneNumber phonenumber$PhoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil, PhoneNumberMatcher phoneNumberMatcher) {
                if (phoneNumberUtil.isValidNumber(phonenumber$PhoneNumber) && PhoneNumberMatcher.containsOnlyValidXChars(phonenumber$PhoneNumber, charSequence.toString(), phoneNumberUtil)) {
                    return PhoneNumberMatcher.isNationalPrefixPresentIfRequired(phonenumber$PhoneNumber, phoneNumberUtil);
                }
                return false;
            }
        },
        STRICT_GROUPING { // from class: com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency.3
            @Override // com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency
            boolean verify(Phonenumber$PhoneNumber phonenumber$PhoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil, PhoneNumberMatcher phoneNumberMatcher) {
                String string = charSequence.toString();
                if (phoneNumberUtil.isValidNumber(phonenumber$PhoneNumber) && PhoneNumberMatcher.containsOnlyValidXChars(phonenumber$PhoneNumber, string, phoneNumberUtil) && !PhoneNumberMatcher.containsMoreThanOneSlashInNationalNumber(phonenumber$PhoneNumber, string) && PhoneNumberMatcher.isNationalPrefixPresentIfRequired(phonenumber$PhoneNumber, phoneNumberUtil)) {
                    return phoneNumberMatcher.checkNumberGroupingIsValid(phonenumber$PhoneNumber, charSequence, phoneNumberUtil, new PhoneNumberMatcher.NumberGroupingChecker() { // from class: com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency.3.1
                        @Override // com.android.internal.telephony.phonenumbers.PhoneNumberMatcher.NumberGroupingChecker
                        public boolean checkGroups(PhoneNumberUtil phoneNumberUtil2, Phonenumber$PhoneNumber phonenumber$PhoneNumber2, StringBuilder sb, String[] strArr) {
                            return PhoneNumberMatcher.allNumberGroupsRemainGrouped(phoneNumberUtil2, phonenumber$PhoneNumber2, sb, strArr);
                        }
                    });
                }
                return false;
            }
        },
        EXACT_GROUPING { // from class: com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency.4
            @Override // com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency
            boolean verify(Phonenumber$PhoneNumber phonenumber$PhoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil, PhoneNumberMatcher phoneNumberMatcher) {
                String string = charSequence.toString();
                if (phoneNumberUtil.isValidNumber(phonenumber$PhoneNumber) && PhoneNumberMatcher.containsOnlyValidXChars(phonenumber$PhoneNumber, string, phoneNumberUtil) && !PhoneNumberMatcher.containsMoreThanOneSlashInNationalNumber(phonenumber$PhoneNumber, string) && PhoneNumberMatcher.isNationalPrefixPresentIfRequired(phonenumber$PhoneNumber, phoneNumberUtil)) {
                    return phoneNumberMatcher.checkNumberGroupingIsValid(phonenumber$PhoneNumber, charSequence, phoneNumberUtil, new PhoneNumberMatcher.NumberGroupingChecker() { // from class: com.android.internal.telephony.phonenumbers.PhoneNumberUtil.Leniency.4.1
                        @Override // com.android.internal.telephony.phonenumbers.PhoneNumberMatcher.NumberGroupingChecker
                        public boolean checkGroups(PhoneNumberUtil phoneNumberUtil2, Phonenumber$PhoneNumber phonenumber$PhoneNumber2, StringBuilder sb, String[] strArr) {
                            return PhoneNumberMatcher.allNumberGroupsAreExactlyPresent(phoneNumberUtil2, phonenumber$PhoneNumber2, sb, strArr);
                        }
                    });
                }
                return false;
            }
        };

        abstract boolean verify(Phonenumber$PhoneNumber phonenumber$PhoneNumber, CharSequence charSequence, PhoneNumberUtil phoneNumberUtil, PhoneNumberMatcher phoneNumberMatcher);
    }

    public enum MatchType {
        NOT_A_NUMBER,
        NO_MATCH,
        SHORT_NSN_MATCH,
        NSN_MATCH,
        EXACT_MATCH
    }

    public enum PhoneNumberFormat {
        E164,
        INTERNATIONAL,
        NATIONAL,
        RFC3966
    }

    public enum PhoneNumberType {
        FIXED_LINE,
        MOBILE,
        FIXED_LINE_OR_MOBILE,
        TOLL_FREE,
        PREMIUM_RATE,
        SHARED_COST,
        VOIP,
        PERSONAL_NUMBER,
        PAGER,
        UAN,
        VOICEMAIL,
        UNKNOWN
    }

    public enum ValidationResult {
        IS_POSSIBLE,
        IS_POSSIBLE_LOCAL_ONLY,
        INVALID_COUNTRY_CODE,
        TOO_SHORT,
        INVALID_LENGTH,
        TOO_LONG
    }

    static {
        HashMap map = new HashMap();
        map.put(54, "9");
        MOBILE_TOKEN_MAPPINGS = Collections.unmodifiableMap(map);
        HashSet hashSet = new HashSet();
        hashSet.add(86);
        GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES = Collections.unmodifiableSet(hashSet);
        HashSet hashSet2 = new HashSet();
        hashSet2.add(52);
        COUNTRIES_WITHOUT_NATIONAL_PREFIX_WITH_AREA_CODES = Collections.unmodifiableSet(hashSet2);
        HashSet hashSet3 = new HashSet();
        hashSet3.add(52);
        hashSet3.add(54);
        hashSet3.add(55);
        hashSet3.add(62);
        hashSet3.addAll(hashSet);
        GEO_MOBILE_COUNTRIES = Collections.unmodifiableSet(hashSet3);
        HashMap map2 = new HashMap();
        map2.put('0', '0');
        map2.put('1', '1');
        map2.put('2', '2');
        map2.put('3', '3');
        map2.put('4', '4');
        map2.put('5', '5');
        map2.put('6', '6');
        map2.put('7', '7');
        map2.put('8', '8');
        map2.put('9', '9');
        HashMap map3 = new HashMap(40);
        map3.put(Character.valueOf(DtmfTransport.DTMF_MESSAGE_START), '2');
        map3.put('B', '2');
        map3.put('C', '2');
        map3.put(Character.valueOf(DtmfTransport.DTMF_MESSAGE_DELIMITER), '3');
        map3.put('E', '3');
        map3.put('F', '3');
        map3.put('G', '4');
        map3.put('H', '4');
        map3.put('I', '4');
        map3.put('J', '5');
        map3.put('K', '5');
        map3.put('L', '5');
        map3.put('M', '6');
        map3.put('N', '6');
        map3.put('O', '6');
        map3.put('P', '7');
        map3.put('Q', '7');
        map3.put('R', '7');
        map3.put('S', '7');
        map3.put('T', '8');
        map3.put('U', '8');
        map3.put('V', '8');
        map3.put('W', '9');
        map3.put('X', '9');
        map3.put('Y', '9');
        map3.put('Z', '9');
        Map mapUnmodifiableMap = Collections.unmodifiableMap(map3);
        ALPHA_MAPPINGS = mapUnmodifiableMap;
        HashMap map4 = new HashMap(100);
        map4.putAll(mapUnmodifiableMap);
        map4.putAll(map2);
        ALPHA_PHONE_MAPPINGS = Collections.unmodifiableMap(map4);
        HashMap map5 = new HashMap();
        map5.putAll(map2);
        map5.put('+', '+');
        map5.put('*', '*');
        map5.put('#', '#');
        DIALLABLE_CHAR_MAPPINGS = Collections.unmodifiableMap(map5);
        HashMap map6 = new HashMap();
        for (Character ch : mapUnmodifiableMap.keySet()) {
            map6.put(Character.valueOf(Character.toLowerCase(ch.charValue())), ch);
            map6.put(ch, ch);
        }
        map6.putAll(map2);
        map6.put('-', '-');
        map6.put((char) 65293, '-');
        map6.put((char) 8208, '-');
        map6.put((char) 8209, '-');
        map6.put((char) 8210, '-');
        map6.put((char) 8211, '-');
        map6.put((char) 8212, '-');
        map6.put((char) 8213, '-');
        map6.put((char) 8722, '-');
        map6.put('/', '/');
        map6.put((char) 65295, '/');
        map6.put(' ', ' ');
        map6.put((char) 12288, ' ');
        map6.put((char) 8288, ' ');
        map6.put('.', '.');
        map6.put((char) 65294, '.');
        ALL_PLUS_NUMBER_GROUPING_SYMBOLS = Collections.unmodifiableMap(map6);
        SINGLE_INTERNATIONAL_PREFIX = Pattern.compile("[\\d]+(?:[~⁓∼～][\\d]+)?");
        StringBuilder sb = new StringBuilder();
        Map map7 = ALPHA_MAPPINGS;
        sb.append(Arrays.toString(map7.keySet().toArray()).replaceAll("[, \\[\\]]", ""));
        sb.append(Arrays.toString(map7.keySet().toArray()).toLowerCase().replaceAll("[, \\[\\]]", ""));
        String string = sb.toString();
        VALID_ALPHA = string;
        PLUS_CHARS_PATTERN = Pattern.compile("[+＋]+");
        SEPARATOR_PATTERN = Pattern.compile("[-x‐-―−ー－-／  \u00ad\u200b\u2060\u3000()（）［］.\\[\\]/~⁓∼～]+");
        CAPTURING_DIGIT_PATTERN = Pattern.compile("(\\p{Nd})");
        VALID_START_CHAR_PATTERN = Pattern.compile("[+＋\\p{Nd}]");
        SECOND_NUMBER_START_PATTERN = Pattern.compile("[\\\\/] *x");
        UNWANTED_END_CHAR_PATTERN = Pattern.compile("[[\\P{N}&&\\P{L}]&&[^#]]+$");
        VALID_ALPHA_PHONE_PATTERN = Pattern.compile("(?:.*?[A-Za-z]){3}.*");
        String str = "\\p{Nd}{2}|[+＋]*+(?:[-x‐-―−ー－-／  \u00ad\u200b\u2060\u3000()（）［］.\\[\\]/~⁓∼～*]*\\p{Nd}){3,}[-x‐-―−ー－-／  \u00ad\u200b\u2060\u3000()（）［］.\\[\\]/~⁓∼～*" + string + "\\p{Nd}]*";
        VALID_PHONE_NUMBER = str;
        String strCreateExtnPattern = createExtnPattern(true);
        EXTN_PATTERNS_FOR_PARSING = strCreateExtnPattern;
        EXTN_PATTERNS_FOR_MATCHING = createExtnPattern(false);
        RFC3966_GLOBAL_NUMBER_DIGITS_PATTERN = Pattern.compile("^\\+(\\p{Nd}|[\\-\\.\\(\\)]?)*\\p{Nd}(\\p{Nd}|[\\-\\.\\(\\)]?)*$");
        String str2 = string + "\\p{Nd}";
        ALPHANUM = str2;
        String str3 = "[" + str2 + "]+((\\-)*[" + str2 + "])*";
        RFC3966_DOMAINLABEL = str3;
        String str4 = "[" + string + "]+((\\-)*[" + str2 + "])*";
        RFC3966_TOPLABEL = str4;
        String str5 = "^(" + str3 + "\\.)*" + str4 + "\\.?$";
        RFC3966_DOMAINNAME = str5;
        RFC3966_DOMAINNAME_PATTERN = Pattern.compile(str5);
        EXTN_PATTERN = Pattern.compile("(?:" + strCreateExtnPattern + ")$", 66);
        VALID_PHONE_NUMBER_PATTERN = Pattern.compile(str + "(?:" + strCreateExtnPattern + ")?", 66);
        NON_DIGITS_PATTERN = Pattern.compile("(\\D+)");
        FIRST_GROUP_PATTERN = Pattern.compile("(\\$\\d)");
        FIRST_GROUP_ONLY_PREFIX_PATTERN = Pattern.compile("\\(?\\$1\\)?");
        instance = null;
    }

    private static String extnDigits(int i) {
        return "(\\p{Nd}{1," + i + "})";
    }

    private static String createExtnPattern(boolean z) {
        String str = (";ext=" + extnDigits(20)) + "|" + ("[  \\t,]*(?:e?xt(?:ensi(?:ó?|ó))?n?|ｅ?ｘｔｎ?|доб|anexo)[:\\.．]?[  \\t,-]*" + extnDigits(20) + "#?") + "|" + ("[  \\t,]*(?:[xｘ#＃~～]|int|ｉｎｔ)[:\\.．]?[  \\t,-]*" + extnDigits(9) + "#?") + "|" + ("[- ]+" + extnDigits(6) + "#");
        if (!z) {
            return str;
        }
        return str + "|" + ("[  \\t]*(?:,{2}|;)[:\\.．]?[  \\t,-]*" + extnDigits(15) + "#?") + "|" + ("[  \\t]*(?:,)+[:\\.．]?[  \\t,-]*" + extnDigits(9) + "#?");
    }

    PhoneNumberUtil(MetadataSource metadataSource, Map map) {
        this.metadataSource = metadataSource;
        this.countryCallingCodeToRegionCodeMap = map;
        for (Map.Entry entry : map.entrySet()) {
            List list = (List) entry.getValue();
            if (list.size() == 1 && "001".equals(list.get(0))) {
                this.countryCodesForNonGeographicalRegion.add((Integer) entry.getKey());
            } else {
                this.supportedRegions.addAll(list);
            }
        }
        if (this.supportedRegions.remove("001")) {
            logger.log(Level.WARNING, "invalid metadata (country calling code was mapped to the non-geo entity as well as specific region(s))");
        }
        this.nanpaRegions.addAll((Collection) map.get(1));
    }

    static CharSequence extractPossibleNumber(CharSequence charSequence) {
        Matcher matcher = VALID_START_CHAR_PATTERN.matcher(charSequence);
        if (matcher.find()) {
            CharSequence charSequenceSubSequence = charSequence.subSequence(matcher.start(), charSequence.length());
            Matcher matcher2 = UNWANTED_END_CHAR_PATTERN.matcher(charSequenceSubSequence);
            if (matcher2.find()) {
                charSequenceSubSequence = charSequenceSubSequence.subSequence(0, matcher2.start());
            }
            Matcher matcher3 = SECOND_NUMBER_START_PATTERN.matcher(charSequenceSubSequence);
            return matcher3.find() ? charSequenceSubSequence.subSequence(0, matcher3.start()) : charSequenceSubSequence;
        }
        return "";
    }

    static boolean isViablePhoneNumber(CharSequence charSequence) {
        if (charSequence.length() < 2) {
            return false;
        }
        return VALID_PHONE_NUMBER_PATTERN.matcher(charSequence).matches();
    }

    static StringBuilder normalize(StringBuilder sb) {
        if (VALID_ALPHA_PHONE_PATTERN.matcher(sb).matches()) {
            sb.replace(0, sb.length(), normalizeHelper(sb, ALPHA_PHONE_MAPPINGS, true));
            return sb;
        }
        sb.replace(0, sb.length(), normalizeDigitsOnly(sb));
        return sb;
    }

    public static String normalizeDigitsOnly(CharSequence charSequence) {
        return normalizeDigits(charSequence, false).toString();
    }

    static StringBuilder normalizeDigits(CharSequence charSequence, boolean z) {
        StringBuilder sb = new StringBuilder(charSequence.length());
        for (int i = 0; i < charSequence.length(); i++) {
            char cCharAt = charSequence.charAt(i);
            int iDigit = Character.digit(cCharAt, 10);
            if (iDigit != -1) {
                sb.append(iDigit);
            } else if (z) {
                sb.append(cCharAt);
            }
        }
        return sb;
    }

    public static String normalizeDiallableCharsOnly(CharSequence charSequence) {
        return normalizeHelper(charSequence, DIALLABLE_CHAR_MAPPINGS, true);
    }

    public static String convertAlphaCharactersInNumber(CharSequence charSequence) {
        return normalizeHelper(charSequence, ALPHA_PHONE_MAPPINGS, false);
    }

    public int getLengthOfGeographicalAreaCode(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(getRegionCodeForNumber(phonenumber$PhoneNumber));
        if (metadataForRegion == null) {
            return 0;
        }
        PhoneNumberType numberType = getNumberType(phonenumber$PhoneNumber);
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        if (!metadataForRegion.hasNationalPrefix() && !phonenumber$PhoneNumber.isItalianLeadingZero() && !COUNTRIES_WITHOUT_NATIONAL_PREFIX_WITH_AREA_CODES.contains(Integer.valueOf(countryCode))) {
            return 0;
        }
        if (!(numberType == PhoneNumberType.MOBILE && GEO_MOBILE_COUNTRIES_WITHOUT_MOBILE_AREA_CODES.contains(Integer.valueOf(countryCode))) && isNumberGeographical(numberType, countryCode)) {
            return getLengthOfNationalDestinationCode(phonenumber$PhoneNumber);
        }
        return 0;
    }

    public int getLengthOfNationalDestinationCode(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        Phonenumber$PhoneNumber phonenumber$PhoneNumber2;
        if (phonenumber$PhoneNumber.hasExtension()) {
            phonenumber$PhoneNumber2 = new Phonenumber$PhoneNumber();
            phonenumber$PhoneNumber2.mergeFrom(phonenumber$PhoneNumber);
            phonenumber$PhoneNumber2.clearExtension();
        } else {
            phonenumber$PhoneNumber2 = phonenumber$PhoneNumber;
        }
        String[] strArrSplit = NON_DIGITS_PATTERN.split(format(phonenumber$PhoneNumber2, PhoneNumberFormat.INTERNATIONAL));
        if (strArrSplit.length <= 3) {
            return 0;
        }
        if (getNumberType(phonenumber$PhoneNumber) == PhoneNumberType.MOBILE && !getCountryMobileToken(phonenumber$PhoneNumber.getCountryCode()).equals("")) {
            return strArrSplit[2].length() + strArrSplit[3].length();
        }
        return strArrSplit[2].length();
    }

    public static String getCountryMobileToken(int i) {
        Map map = MOBILE_TOKEN_MAPPINGS;
        if (map.containsKey(Integer.valueOf(i))) {
            return (String) map.get(Integer.valueOf(i));
        }
        return "";
    }

    private static String normalizeHelper(CharSequence charSequence, Map map, boolean z) {
        StringBuilder sb = new StringBuilder(charSequence.length());
        for (int i = 0; i < charSequence.length(); i++) {
            char cCharAt = charSequence.charAt(i);
            Character ch = (Character) map.get(Character.valueOf(Character.toUpperCase(cCharAt)));
            if (ch != null) {
                sb.append(ch);
            } else if (!z) {
                sb.append(cCharAt);
            }
        }
        return sb.toString();
    }

    static synchronized void setInstance(PhoneNumberUtil phoneNumberUtil) {
        instance = phoneNumberUtil;
    }

    public Set<String> getSupportedRegions() {
        return Collections.unmodifiableSet(this.supportedRegions);
    }

    public Set<Integer> getSupportedGlobalNetworkCallingCodes() {
        return Collections.unmodifiableSet(this.countryCodesForNonGeographicalRegion);
    }

    public Set<Integer> getSupportedCallingCodes() {
        return Collections.unmodifiableSet(this.countryCallingCodeToRegionCodeMap.keySet());
    }

    private static boolean descHasPossibleNumberData(Phonemetadata$PhoneNumberDesc phonemetadata$PhoneNumberDesc) {
        return (phonemetadata$PhoneNumberDesc.getPossibleLengthCount() == 1 && phonemetadata$PhoneNumberDesc.getPossibleLength(0) == -1) ? false : true;
    }

    private static boolean descHasData(Phonemetadata$PhoneNumberDesc phonemetadata$PhoneNumberDesc) {
        return phonemetadata$PhoneNumberDesc.hasExampleNumber() || descHasPossibleNumberData(phonemetadata$PhoneNumberDesc) || phonemetadata$PhoneNumberDesc.hasNationalNumberPattern();
    }

    private Set getSupportedTypesForMetadata(Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata) {
        TreeSet treeSet = new TreeSet();
        for (PhoneNumberType phoneNumberType : PhoneNumberType.values()) {
            if (phoneNumberType != PhoneNumberType.FIXED_LINE_OR_MOBILE && phoneNumberType != PhoneNumberType.UNKNOWN && descHasData(getNumberDescByType(phonemetadata$PhoneMetadata, phoneNumberType))) {
                treeSet.add(phoneNumberType);
            }
        }
        return Collections.unmodifiableSet(treeSet);
    }

    public Set<PhoneNumberType> getSupportedTypesForRegion(String str) {
        if (!isValidRegionCode(str)) {
            logger.log(Level.WARNING, "Invalid or unknown region code provided: " + str);
            return Collections.unmodifiableSet(new TreeSet());
        }
        return getSupportedTypesForMetadata(getMetadataForRegion(str));
    }

    public Set<PhoneNumberType> getSupportedTypesForNonGeoEntity(int i) {
        Phonemetadata$PhoneMetadata metadataForNonGeographicalRegion = getMetadataForNonGeographicalRegion(i);
        if (metadataForNonGeographicalRegion == null) {
            logger.log(Level.WARNING, "Unknown country calling code for a non-geographical entity provided: " + i);
            return Collections.unmodifiableSet(new TreeSet());
        }
        return getSupportedTypesForMetadata(metadataForNonGeographicalRegion);
    }

    public static synchronized PhoneNumberUtil getInstance() {
        try {
            if (instance == null) {
                setInstance(createInstance(DefaultMetadataDependenciesProvider.getInstance().getMetadataLoader()));
            }
        } catch (Throwable th) {
            throw th;
        }
        return instance;
    }

    public static PhoneNumberUtil createInstance(MetadataLoader metadataLoader) {
        if (metadataLoader == null) {
            Phone$$ExternalSyntheticBUOutline0.m("metadataLoader could not be null.");
            return null;
        }
        return createInstance(new MetadataSourceImpl(DefaultMetadataDependenciesProvider.getInstance().getPhoneNumberMetadataFileNameProvider(), metadataLoader, DefaultMetadataDependenciesProvider.getInstance().getMetadataParser()));
    }

    private static PhoneNumberUtil createInstance(MetadataSource metadataSource) {
        if (metadataSource == null) {
            Phone$$ExternalSyntheticBUOutline0.m("metadataSource could not be null.");
            return null;
        }
        return new PhoneNumberUtil(metadataSource, CountryCodeToRegionCodeMap.getCountryCodeToRegionCodeMap());
    }

    static boolean formattingRuleHasFirstGroupOnly(String str) {
        return str.length() == 0 || FIRST_GROUP_ONLY_PREFIX_PATTERN.matcher(str).matches();
    }

    public boolean isNumberGeographical(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        return isNumberGeographical(getNumberType(phonenumber$PhoneNumber), phonenumber$PhoneNumber.getCountryCode());
    }

    public boolean isNumberGeographical(PhoneNumberType phoneNumberType, int i) {
        if (phoneNumberType == PhoneNumberType.FIXED_LINE || phoneNumberType == PhoneNumberType.FIXED_LINE_OR_MOBILE) {
            return true;
        }
        return GEO_MOBILE_COUNTRIES.contains(Integer.valueOf(i)) && phoneNumberType == PhoneNumberType.MOBILE;
    }

    private boolean isValidRegionCode(String str) {
        return str != null && this.supportedRegions.contains(str);
    }

    private boolean hasValidCountryCallingCode(int i) {
        return this.countryCallingCodeToRegionCodeMap.containsKey(Integer.valueOf(i));
    }

    public String format(Phonenumber$PhoneNumber phonenumber$PhoneNumber, PhoneNumberFormat phoneNumberFormat) {
        if (phonenumber$PhoneNumber.getNationalNumber() == 0) {
            String rawInput = phonenumber$PhoneNumber.getRawInput();
            if (rawInput.length() > 0 || !phonenumber$PhoneNumber.hasCountryCode()) {
                return rawInput;
            }
        }
        StringBuilder sb = new StringBuilder(20);
        format(phonenumber$PhoneNumber, phoneNumberFormat, sb);
        return sb.toString();
    }

    public void format(Phonenumber$PhoneNumber phonenumber$PhoneNumber, PhoneNumberFormat phoneNumberFormat, StringBuilder sb) {
        sb.setLength(0);
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        PhoneNumberFormat phoneNumberFormat2 = PhoneNumberFormat.E164;
        if (phoneNumberFormat == phoneNumberFormat2) {
            sb.append(nationalSignificantNumber);
            prefixNumberWithCountryCallingCode(countryCode, phoneNumberFormat2, sb);
        } else {
            if (!hasValidCountryCallingCode(countryCode)) {
                sb.append(nationalSignificantNumber);
                return;
            }
            Phonemetadata$PhoneMetadata metadataForRegionOrCallingCode = getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode));
            sb.append(formatNsn(nationalSignificantNumber, metadataForRegionOrCallingCode, phoneNumberFormat));
            maybeAppendFormattedExtension(phonenumber$PhoneNumber, metadataForRegionOrCallingCode, phoneNumberFormat, sb);
            prefixNumberWithCountryCallingCode(countryCode, phoneNumberFormat, sb);
        }
    }

    public String formatByPattern(Phonenumber$PhoneNumber phonenumber$PhoneNumber, PhoneNumberFormat phoneNumberFormat, List<Phonemetadata$NumberFormat> list) {
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        if (!hasValidCountryCallingCode(countryCode)) {
            return nationalSignificantNumber;
        }
        Phonemetadata$PhoneMetadata metadataForRegionOrCallingCode = getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode));
        StringBuilder sb = new StringBuilder(20);
        Phonemetadata$NumberFormat phonemetadata$NumberFormatChooseFormattingPatternForNumber = chooseFormattingPatternForNumber(list, nationalSignificantNumber);
        if (phonemetadata$NumberFormatChooseFormattingPatternForNumber == null) {
            sb.append(nationalSignificantNumber);
        } else {
            Phonemetadata$NumberFormat.Builder builderNewBuilder = Phonemetadata$NumberFormat.newBuilder();
            builderNewBuilder.mergeFrom(phonemetadata$NumberFormatChooseFormattingPatternForNumber);
            String nationalPrefixFormattingRule = phonemetadata$NumberFormatChooseFormattingPatternForNumber.getNationalPrefixFormattingRule();
            if (nationalPrefixFormattingRule.length() > 0) {
                String nationalPrefix = metadataForRegionOrCallingCode.getNationalPrefix();
                if (nationalPrefix.length() > 0) {
                    builderNewBuilder.setNationalPrefixFormattingRule(nationalPrefixFormattingRule.replace("$NP", nationalPrefix).replace("$FG", "$1"));
                } else {
                    builderNewBuilder.clearNationalPrefixFormattingRule();
                }
            }
            sb.append(formatNsnUsingPattern(nationalSignificantNumber, builderNewBuilder.build(), phoneNumberFormat));
        }
        maybeAppendFormattedExtension(phonenumber$PhoneNumber, metadataForRegionOrCallingCode, phoneNumberFormat, sb);
        prefixNumberWithCountryCallingCode(countryCode, phoneNumberFormat, sb);
        return sb.toString();
    }

    public String formatNationalNumberWithCarrierCode(Phonenumber$PhoneNumber phonenumber$PhoneNumber, CharSequence charSequence) {
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        if (!hasValidCountryCallingCode(countryCode)) {
            return nationalSignificantNumber;
        }
        Phonemetadata$PhoneMetadata metadataForRegionOrCallingCode = getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode));
        StringBuilder sb = new StringBuilder(20);
        PhoneNumberFormat phoneNumberFormat = PhoneNumberFormat.NATIONAL;
        sb.append(formatNsn(nationalSignificantNumber, metadataForRegionOrCallingCode, phoneNumberFormat, charSequence));
        maybeAppendFormattedExtension(phonenumber$PhoneNumber, metadataForRegionOrCallingCode, phoneNumberFormat, sb);
        prefixNumberWithCountryCallingCode(countryCode, phoneNumberFormat, sb);
        return sb.toString();
    }

    private Phonemetadata$PhoneMetadata getMetadataForRegionOrCallingCode(int i, String str) {
        if ("001".equals(str)) {
            return getMetadataForNonGeographicalRegion(i);
        }
        return getMetadataForRegion(str);
    }

    public String formatNationalNumberWithPreferredCarrierCode(Phonenumber$PhoneNumber phonenumber$PhoneNumber, CharSequence charSequence) {
        if (phonenumber$PhoneNumber.getPreferredDomesticCarrierCode().length() > 0) {
            charSequence = phonenumber$PhoneNumber.getPreferredDomesticCarrierCode();
        }
        return formatNationalNumberWithCarrierCode(phonenumber$PhoneNumber, charSequence);
    }

    public String formatNumberForMobileDialing(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str, boolean z) {
        String str2;
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        String nationalNumberWithPreferredCarrierCode = "";
        if (!hasValidCountryCallingCode(countryCode)) {
            return phonenumber$PhoneNumber.hasRawInput() ? phonenumber$PhoneNumber.getRawInput() : "";
        }
        Phonenumber$PhoneNumber phonenumber$PhoneNumberClearExtension = new Phonenumber$PhoneNumber().mergeFrom(phonenumber$PhoneNumber).clearExtension();
        String regionCodeForCountryCode = getRegionCodeForCountryCode(countryCode);
        PhoneNumberType numberType = getNumberType(phonenumber$PhoneNumberClearExtension);
        boolean z2 = numberType != PhoneNumberType.UNKNOWN;
        if (str.equals(regionCodeForCountryCode)) {
            boolean z3 = numberType == PhoneNumberType.FIXED_LINE || numberType == PhoneNumberType.MOBILE || numberType == PhoneNumberType.FIXED_LINE_OR_MOBILE;
            if (!regionCodeForCountryCode.equals("BR") || !z3) {
                if (countryCode == 1) {
                    Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(str);
                    if (canBeInternationallyDialled(phonenumber$PhoneNumberClearExtension) && testNumberLength(getNationalSignificantNumber(phonenumber$PhoneNumberClearExtension), metadataForRegion) != ValidationResult.TOO_SHORT) {
                        str2 = format(phonenumber$PhoneNumberClearExtension, PhoneNumberFormat.INTERNATIONAL);
                    } else {
                        str2 = format(phonenumber$PhoneNumberClearExtension, PhoneNumberFormat.NATIONAL);
                    }
                } else if ((regionCodeForCountryCode.equals("001") || ((regionCodeForCountryCode.equals("MX") || regionCodeForCountryCode.equals("CL") || regionCodeForCountryCode.equals("UZ")) && z3)) && canBeInternationallyDialled(phonenumber$PhoneNumberClearExtension)) {
                    str2 = format(phonenumber$PhoneNumberClearExtension, PhoneNumberFormat.INTERNATIONAL);
                } else {
                    str2 = format(phonenumber$PhoneNumberClearExtension, PhoneNumberFormat.NATIONAL);
                }
                nationalNumberWithPreferredCarrierCode = str2;
            } else if (phonenumber$PhoneNumberClearExtension.getPreferredDomesticCarrierCode().length() > 0) {
                nationalNumberWithPreferredCarrierCode = formatNationalNumberWithPreferredCarrierCode(phonenumber$PhoneNumberClearExtension, "");
            }
        } else if (z2 && canBeInternationallyDialled(phonenumber$PhoneNumberClearExtension)) {
            if (z) {
                return format(phonenumber$PhoneNumberClearExtension, PhoneNumberFormat.INTERNATIONAL);
            }
            return format(phonenumber$PhoneNumberClearExtension, PhoneNumberFormat.E164);
        }
        return z ? nationalNumberWithPreferredCarrierCode : normalizeDiallableCharsOnly(nationalNumberWithPreferredCarrierCode);
    }

    public String formatOutOfCountryCallingNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        if (!isValidRegionCode(str)) {
            logger.log(Level.WARNING, "Trying to format number from invalid region " + str + ". International formatting applied.");
            return format(phonenumber$PhoneNumber, PhoneNumberFormat.INTERNATIONAL);
        }
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        if (!hasValidCountryCallingCode(countryCode)) {
            return nationalSignificantNumber;
        }
        if (countryCode == 1) {
            if (isNANPACountry(str)) {
                return countryCode + " " + format(phonenumber$PhoneNumber, PhoneNumberFormat.NATIONAL);
            }
        } else if (countryCode == getCountryCodeForValidRegion(str)) {
            return format(phonenumber$PhoneNumber, PhoneNumberFormat.NATIONAL);
        }
        Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(str);
        String internationalPrefix = metadataForRegion.getInternationalPrefix();
        if (metadataForRegion.hasPreferredInternationalPrefix()) {
            internationalPrefix = metadataForRegion.getPreferredInternationalPrefix();
        } else if (!SINGLE_INTERNATIONAL_PREFIX.matcher(internationalPrefix).matches()) {
            internationalPrefix = "";
        }
        Phonemetadata$PhoneMetadata metadataForRegionOrCallingCode = getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode));
        PhoneNumberFormat phoneNumberFormat = PhoneNumberFormat.INTERNATIONAL;
        StringBuilder sb = new StringBuilder(formatNsn(nationalSignificantNumber, metadataForRegionOrCallingCode, phoneNumberFormat));
        maybeAppendFormattedExtension(phonenumber$PhoneNumber, metadataForRegionOrCallingCode, phoneNumberFormat, sb);
        if (internationalPrefix.length() > 0) {
            sb.insert(0, " ").insert(0, countryCode).insert(0, " ").insert(0, internationalPrefix);
        } else {
            prefixNumberWithCountryCallingCode(countryCode, phoneNumberFormat, sb);
        }
        return sb.toString();
    }

    public String formatInOriginalFormat(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        String outOfCountryCallingNumber;
        String nationalPrefixFormattingRule;
        int iIndexOf;
        if (phonenumber$PhoneNumber.hasRawInput() && !hasFormattingPatternForNumber(phonenumber$PhoneNumber)) {
            return phonenumber$PhoneNumber.getRawInput();
        }
        if (!phonenumber$PhoneNumber.hasCountryCodeSource()) {
            return format(phonenumber$PhoneNumber, PhoneNumberFormat.NATIONAL);
        }
        int i = AnonymousClass2.$SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[phonenumber$PhoneNumber.getCountryCodeSource().ordinal()];
        if (i == 1) {
            outOfCountryCallingNumber = format(phonenumber$PhoneNumber, PhoneNumberFormat.INTERNATIONAL);
        } else if (i == 2) {
            outOfCountryCallingNumber = formatOutOfCountryCallingNumber(phonenumber$PhoneNumber, str);
        } else if (i == 3) {
            outOfCountryCallingNumber = format(phonenumber$PhoneNumber, PhoneNumberFormat.INTERNATIONAL).substring(1);
        } else {
            String regionCodeForCountryCode = getRegionCodeForCountryCode(phonenumber$PhoneNumber.getCountryCode());
            String nddPrefixForRegion = getNddPrefixForRegion(regionCodeForCountryCode, true);
            PhoneNumberFormat phoneNumberFormat = PhoneNumberFormat.NATIONAL;
            outOfCountryCallingNumber = format(phonenumber$PhoneNumber, phoneNumberFormat);
            if (nddPrefixForRegion != null && nddPrefixForRegion.length() != 0 && !rawInputContainsNationalPrefix(phonenumber$PhoneNumber.getRawInput(), nddPrefixForRegion, regionCodeForCountryCode)) {
                Phonemetadata$NumberFormat phonemetadata$NumberFormatChooseFormattingPatternForNumber = chooseFormattingPatternForNumber(getMetadataForRegion(regionCodeForCountryCode).getNumberFormatList(), getNationalSignificantNumber(phonenumber$PhoneNumber));
                if (phonemetadata$NumberFormatChooseFormattingPatternForNumber != null && (iIndexOf = (nationalPrefixFormattingRule = phonemetadata$NumberFormatChooseFormattingPatternForNumber.getNationalPrefixFormattingRule()).indexOf("$1")) > 0 && normalizeDigitsOnly(nationalPrefixFormattingRule.substring(0, iIndexOf)).length() != 0) {
                    Phonemetadata$NumberFormat.Builder builderNewBuilder = Phonemetadata$NumberFormat.newBuilder();
                    builderNewBuilder.mergeFrom(phonemetadata$NumberFormatChooseFormattingPatternForNumber);
                    builderNewBuilder.clearNationalPrefixFormattingRule();
                    ArrayList arrayList = new ArrayList(1);
                    arrayList.add(builderNewBuilder.build());
                    outOfCountryCallingNumber = formatByPattern(phonenumber$PhoneNumber, phoneNumberFormat, arrayList);
                }
            }
        }
        String rawInput = phonenumber$PhoneNumber.getRawInput();
        return (outOfCountryCallingNumber == null || rawInput.length() <= 0 || normalizeDiallableCharsOnly(outOfCountryCallingNumber).equals(normalizeDiallableCharsOnly(rawInput))) ? outOfCountryCallingNumber : rawInput;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.phonenumbers.PhoneNumberUtil$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource;

        static {
            int[] iArr = new int[Phonenumber$PhoneNumber.CountryCodeSource.values().length];
            $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource = iArr;
            try {
                iArr[Phonenumber$PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[Phonenumber$PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_IDD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[Phonenumber$PhoneNumber.CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$i18n$phonenumbers$Phonenumber$PhoneNumber$CountryCodeSource[Phonenumber$PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private boolean rawInputContainsNationalPrefix(String str, String str2, String str3) {
        String strNormalizeDigitsOnly = normalizeDigitsOnly(str);
        if (strNormalizeDigitsOnly.startsWith(str2)) {
            try {
                return isValidNumber(parse(strNormalizeDigitsOnly.substring(str2.length()), str3));
            } catch (NumberParseException unused) {
            }
        }
        return false;
    }

    private boolean hasFormattingPatternForNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        Phonemetadata$PhoneMetadata metadataForRegionOrCallingCode = getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode));
        if (metadataForRegionOrCallingCode == null) {
            return false;
        }
        return chooseFormattingPatternForNumber(metadataForRegionOrCallingCode.getNumberFormatList(), getNationalSignificantNumber(phonenumber$PhoneNumber)) != null;
    }

    public String formatOutOfCountryKeepingAlphaChars(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        String internationalPrefix;
        int iIndexOf;
        String rawInput = phonenumber$PhoneNumber.getRawInput();
        if (rawInput.length() == 0) {
            return formatOutOfCountryCallingNumber(phonenumber$PhoneNumber, str);
        }
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        if (!hasValidCountryCallingCode(countryCode)) {
            return rawInput;
        }
        String strNormalizeHelper = normalizeHelper(rawInput, ALL_PLUS_NUMBER_GROUPING_SYMBOLS, true);
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        if (nationalSignificantNumber.length() > 3 && (iIndexOf = strNormalizeHelper.indexOf(nationalSignificantNumber.substring(0, 3))) != -1) {
            strNormalizeHelper = strNormalizeHelper.substring(iIndexOf);
        }
        Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(str);
        if (countryCode == 1) {
            if (isNANPACountry(str)) {
                return countryCode + " " + strNormalizeHelper;
            }
        } else if (metadataForRegion != null && countryCode == getCountryCodeForValidRegion(str)) {
            Phonemetadata$NumberFormat phonemetadata$NumberFormatChooseFormattingPatternForNumber = chooseFormattingPatternForNumber(metadataForRegion.getNumberFormatList(), nationalSignificantNumber);
            if (phonemetadata$NumberFormatChooseFormattingPatternForNumber == null) {
                return strNormalizeHelper;
            }
            Phonemetadata$NumberFormat.Builder builderNewBuilder = Phonemetadata$NumberFormat.newBuilder();
            builderNewBuilder.mergeFrom(phonemetadata$NumberFormatChooseFormattingPatternForNumber);
            builderNewBuilder.setPattern("(\\d+)(.*)");
            builderNewBuilder.setFormat("$1$2");
            return formatNsnUsingPattern(strNormalizeHelper, builderNewBuilder.build(), PhoneNumberFormat.NATIONAL);
        }
        if (metadataForRegion == null) {
            internationalPrefix = "";
        } else {
            internationalPrefix = metadataForRegion.getInternationalPrefix();
            if (!SINGLE_INTERNATIONAL_PREFIX.matcher(internationalPrefix).matches()) {
                internationalPrefix = metadataForRegion.getPreferredInternationalPrefix();
            }
        }
        StringBuilder sb = new StringBuilder(strNormalizeHelper);
        Phonemetadata$PhoneMetadata metadataForRegionOrCallingCode = getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode));
        maybeStripExtension(sb);
        PhoneNumberFormat phoneNumberFormat = PhoneNumberFormat.INTERNATIONAL;
        maybeAppendFormattedExtension(phonenumber$PhoneNumber, metadataForRegionOrCallingCode, phoneNumberFormat, sb);
        if (internationalPrefix.length() > 0) {
            sb.insert(0, " ").insert(0, countryCode).insert(0, " ").insert(0, internationalPrefix);
        } else {
            if (!isValidRegionCode(str)) {
                logger.log(Level.WARNING, "Trying to format number from invalid region " + str + ". International formatting applied.");
            }
            prefixNumberWithCountryCallingCode(countryCode, phoneNumberFormat, sb);
        }
        return sb.toString();
    }

    public String getNationalSignificantNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        StringBuilder sb = new StringBuilder();
        if (phonenumber$PhoneNumber.isItalianLeadingZero() && phonenumber$PhoneNumber.getNumberOfLeadingZeros() > 0) {
            char[] cArr = new char[phonenumber$PhoneNumber.getNumberOfLeadingZeros()];
            Arrays.fill(cArr, '0');
            sb.append(new String(cArr));
        }
        sb.append(phonenumber$PhoneNumber.getNationalNumber());
        return sb.toString();
    }

    private void prefixNumberWithCountryCallingCode(int i, PhoneNumberFormat phoneNumberFormat, StringBuilder sb) {
        int iOrdinal = phoneNumberFormat.ordinal();
        if (iOrdinal == 0) {
            sb.insert(0, i).insert(0, '+');
        } else if (iOrdinal == 1) {
            sb.insert(0, " ").insert(0, i).insert(0, '+');
        } else {
            if (iOrdinal != 3) {
                return;
            }
            sb.insert(0, "-").insert(0, i).insert(0, '+').insert(0, "tel:");
        }
    }

    private String formatNsn(String str, Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata, PhoneNumberFormat phoneNumberFormat) {
        return formatNsn(str, phonemetadata$PhoneMetadata, phoneNumberFormat, null);
    }

    private String formatNsn(String str, Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata, PhoneNumberFormat phoneNumberFormat, CharSequence charSequence) {
        List<Phonemetadata$NumberFormat> numberFormatList;
        if (phonemetadata$PhoneMetadata.getIntlNumberFormatList().size() == 0 || phoneNumberFormat == PhoneNumberFormat.NATIONAL) {
            numberFormatList = phonemetadata$PhoneMetadata.getNumberFormatList();
        } else {
            numberFormatList = phonemetadata$PhoneMetadata.getIntlNumberFormatList();
        }
        Phonemetadata$NumberFormat phonemetadata$NumberFormatChooseFormattingPatternForNumber = chooseFormattingPatternForNumber(numberFormatList, str);
        return phonemetadata$NumberFormatChooseFormattingPatternForNumber == null ? str : formatNsnUsingPattern(str, phonemetadata$NumberFormatChooseFormattingPatternForNumber, phoneNumberFormat, charSequence);
    }

    Phonemetadata$NumberFormat chooseFormattingPatternForNumber(List list, String str) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Phonemetadata$NumberFormat phonemetadata$NumberFormat = (Phonemetadata$NumberFormat) it.next();
            int leadingDigitsPatternCount = phonemetadata$NumberFormat.getLeadingDigitsPatternCount();
            if (leadingDigitsPatternCount == 0 || this.regexCache.getPatternForRegex(phonemetadata$NumberFormat.getLeadingDigitsPattern(leadingDigitsPatternCount - 1)).matcher(str).lookingAt()) {
                if (this.regexCache.getPatternForRegex(phonemetadata$NumberFormat.getPattern()).matcher(str).matches()) {
                    return phonemetadata$NumberFormat;
                }
            }
        }
        return null;
    }

    String formatNsnUsingPattern(String str, Phonemetadata$NumberFormat phonemetadata$NumberFormat, PhoneNumberFormat phoneNumberFormat) {
        return formatNsnUsingPattern(str, phonemetadata$NumberFormat, phoneNumberFormat, null);
    }

    private String formatNsnUsingPattern(String str, Phonemetadata$NumberFormat phonemetadata$NumberFormat, PhoneNumberFormat phoneNumberFormat, CharSequence charSequence) {
        String strReplaceAll;
        String format = phonemetadata$NumberFormat.getFormat();
        Matcher matcher = this.regexCache.getPatternForRegex(phonemetadata$NumberFormat.getPattern()).matcher(str);
        PhoneNumberFormat phoneNumberFormat2 = PhoneNumberFormat.NATIONAL;
        if (phoneNumberFormat == phoneNumberFormat2 && charSequence != null && charSequence.length() > 0 && phonemetadata$NumberFormat.getDomesticCarrierCodeFormattingRule().length() > 0) {
            strReplaceAll = matcher.replaceAll(FIRST_GROUP_PATTERN.matcher(format).replaceFirst(phonemetadata$NumberFormat.getDomesticCarrierCodeFormattingRule().replace("$CC", charSequence)));
        } else {
            String nationalPrefixFormattingRule = phonemetadata$NumberFormat.getNationalPrefixFormattingRule();
            if (phoneNumberFormat == phoneNumberFormat2 && nationalPrefixFormattingRule != null && nationalPrefixFormattingRule.length() > 0) {
                strReplaceAll = matcher.replaceAll(FIRST_GROUP_PATTERN.matcher(format).replaceFirst(nationalPrefixFormattingRule));
            } else {
                strReplaceAll = matcher.replaceAll(format);
            }
        }
        if (phoneNumberFormat != PhoneNumberFormat.RFC3966) {
            return strReplaceAll;
        }
        Matcher matcher2 = SEPARATOR_PATTERN.matcher(strReplaceAll);
        if (matcher2.lookingAt()) {
            strReplaceAll = matcher2.replaceFirst("");
        }
        return matcher2.reset(strReplaceAll).replaceAll("-");
    }

    public Phonenumber$PhoneNumber getExampleNumber(String str) {
        return getExampleNumberForType(str, PhoneNumberType.FIXED_LINE);
    }

    /*  JADX ERROR: NullPointerException in pass: BlockProcessor
        java.lang.NullPointerException: Cannot invoke "jadx.core.dex.nodes.BlockNode.getPredecessors()" because "to" is null
        	at jadx.core.dex.visitors.blocks.BlockSplitter.connect(BlockSplitter.java:159)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.connectSplittersAndHandlers(BlockExceptionHandler.java:501)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.wrapBlocksWithTryCatch(BlockExceptionHandler.java:382)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.connectExcHandlers(BlockExceptionHandler.java:91)
        	at jadx.core.dex.visitors.blocks.BlockExceptionHandler.process(BlockExceptionHandler.java:62)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.independentBlockTreeMod(BlockProcessor.java:422)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.processBlocksTree(BlockProcessor.java:57)
        	at jadx.core.dex.visitors.blocks.BlockProcessor.visit(BlockProcessor.java:50)
        */
    public com.android.internal.telephony.phonenumbers.Phonenumber$PhoneNumber getInvalidExampleNumber(java.lang.String r6) {
        /*
            r5 = this;
            boolean r0 = r5.isValidRegionCode(r6)
            r1 = 0
            if (r0 != 0) goto L1d
            java.util.logging.Logger r5 = com.android.internal.telephony.phonenumbers.PhoneNumberUtil.logger
            java.util.logging.Level r0 = java.util.logging.Level.WARNING
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "Invalid or unknown region code provided: "
            r2.<init>(r3)
            r2.append(r6)
            java.lang.String r6 = r2.toString()
            r5.log(r0, r6)
            return r1
        L1d:
            com.android.internal.telephony.phonenumbers.Phonemetadata$PhoneMetadata r0 = r5.getMetadataForRegion(r6)
            com.android.internal.telephony.phonenumbers.PhoneNumberUtil$PhoneNumberType r2 = com.android.internal.telephony.phonenumbers.PhoneNumberUtil.PhoneNumberType.FIXED_LINE
            com.android.internal.telephony.phonenumbers.Phonemetadata$PhoneNumberDesc r0 = r5.getNumberDescByType(r0, r2)
            boolean r2 = r0.hasExampleNumber()
            if (r2 != 0) goto L2e
            return r1
        L2e:
            java.lang.String r0 = r0.getExampleNumber()
            int r2 = r0.length()
            int r2 = r2 + (-1)
        L38:
            r3 = 2
            if (r2 < r3) goto L4e
            r3 = 0
            java.lang.String r3 = r0.substring(r3, r2)
            com.android.internal.telephony.phonenumbers.Phonenumber$PhoneNumber r3 = r5.parse(r3, r6)     // Catch: com.android.internal.telephony.phonenumbers.NumberParseException -> L4b
            boolean r4 = r5.isValidNumber(r3)     // Catch: com.android.internal.telephony.phonenumbers.NumberParseException -> L4b
            if (r4 != 0) goto L4b
            return r3
        L4b:
            int r2 = r2 + (-1)
            goto L38
        L4e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.phonenumbers.PhoneNumberUtil.getInvalidExampleNumber(java.lang.String):com.android.internal.telephony.phonenumbers.Phonenumber$PhoneNumber");
    }

    public Phonenumber$PhoneNumber getExampleNumberForType(String str, PhoneNumberType phoneNumberType) {
        if (!isValidRegionCode(str)) {
            logger.log(Level.WARNING, "Invalid or unknown region code provided: " + str);
            return null;
        }
        Phonemetadata$PhoneNumberDesc numberDescByType = getNumberDescByType(getMetadataForRegion(str), phoneNumberType);
        try {
            if (numberDescByType.hasExampleNumber()) {
                return parse(numberDescByType.getExampleNumber(), str);
            }
        } catch (NumberParseException e) {
            logger.log(Level.SEVERE, e.toString());
        }
        return null;
    }

    public Phonenumber$PhoneNumber getExampleNumberForType(PhoneNumberType phoneNumberType) {
        Iterator<String> it = getSupportedRegions().iterator();
        while (it.hasNext()) {
            Phonenumber$PhoneNumber exampleNumberForType = getExampleNumberForType(it.next(), phoneNumberType);
            if (exampleNumberForType != null) {
                return exampleNumberForType;
            }
        }
        Iterator<Integer> it2 = getSupportedGlobalNetworkCallingCodes().iterator();
        while (it2.hasNext()) {
            int iIntValue = it2.next().intValue();
            Phonemetadata$PhoneNumberDesc numberDescByType = this.getNumberDescByType(this.getMetadataForNonGeographicalRegion(iIntValue), phoneNumberType);
            try {
                if (numberDescByType.hasExampleNumber()) {
                    return this.parse("+" + iIntValue + numberDescByType.getExampleNumber(), "ZZ");
                }
                continue;
            } catch (NumberParseException e) {
                logger.log(Level.SEVERE, e.toString());
            }
        }
        return null;
    }

    public Phonenumber$PhoneNumber getExampleNumberForNonGeoEntity(int i) {
        Phonemetadata$PhoneMetadata metadataForNonGeographicalRegion = getMetadataForNonGeographicalRegion(i);
        if (metadataForNonGeographicalRegion != null) {
            for (Phonemetadata$PhoneNumberDesc phonemetadata$PhoneNumberDesc : Arrays.asList(metadataForNonGeographicalRegion.getMobile(), metadataForNonGeographicalRegion.getTollFree(), metadataForNonGeographicalRegion.getSharedCost(), metadataForNonGeographicalRegion.getVoip(), metadataForNonGeographicalRegion.getVoicemail(), metadataForNonGeographicalRegion.getUan(), metadataForNonGeographicalRegion.getPremiumRate())) {
                if (phonemetadata$PhoneNumberDesc != null) {
                    try {
                        if (phonemetadata$PhoneNumberDesc.hasExampleNumber()) {
                            return this.parse("+" + i + phonemetadata$PhoneNumberDesc.getExampleNumber(), "ZZ");
                        }
                        continue;
                    } catch (NumberParseException e) {
                        logger.log(Level.SEVERE, e.toString());
                    }
                }
            }
            return null;
        }
        logger.log(Level.WARNING, "Invalid or unknown country calling code provided: " + i);
        return null;
    }

    private void maybeAppendFormattedExtension(Phonenumber$PhoneNumber phonenumber$PhoneNumber, Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata, PhoneNumberFormat phoneNumberFormat, StringBuilder sb) {
        if (!phonenumber$PhoneNumber.hasExtension() || phonenumber$PhoneNumber.getExtension().length() <= 0) {
            return;
        }
        if (phoneNumberFormat == PhoneNumberFormat.RFC3966) {
            sb.append(";ext=");
            sb.append(phonenumber$PhoneNumber.getExtension());
        } else if (phonemetadata$PhoneMetadata.hasPreferredExtnPrefix()) {
            sb.append(phonemetadata$PhoneMetadata.getPreferredExtnPrefix());
            sb.append(phonenumber$PhoneNumber.getExtension());
        } else {
            sb.append(" ext. ");
            sb.append(phonenumber$PhoneNumber.getExtension());
        }
    }

    Phonemetadata$PhoneNumberDesc getNumberDescByType(Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata, PhoneNumberType phoneNumberType) {
        switch (phoneNumberType) {
            case FIXED_LINE:
            case FIXED_LINE_OR_MOBILE:
                return phonemetadata$PhoneMetadata.getFixedLine();
            case MOBILE:
                return phonemetadata$PhoneMetadata.getMobile();
            case TOLL_FREE:
                return phonemetadata$PhoneMetadata.getTollFree();
            case PREMIUM_RATE:
                return phonemetadata$PhoneMetadata.getPremiumRate();
            case SHARED_COST:
                return phonemetadata$PhoneMetadata.getSharedCost();
            case VOIP:
                return phonemetadata$PhoneMetadata.getVoip();
            case PERSONAL_NUMBER:
                return phonemetadata$PhoneMetadata.getPersonalNumber();
            case PAGER:
                return phonemetadata$PhoneMetadata.getPager();
            case UAN:
                return phonemetadata$PhoneMetadata.getUan();
            case VOICEMAIL:
                return phonemetadata$PhoneMetadata.getVoicemail();
            default:
                return phonemetadata$PhoneMetadata.getGeneralDesc();
        }
    }

    public PhoneNumberType getNumberType(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        Phonemetadata$PhoneMetadata metadataForRegionOrCallingCode = getMetadataForRegionOrCallingCode(phonenumber$PhoneNumber.getCountryCode(), getRegionCodeForNumber(phonenumber$PhoneNumber));
        if (metadataForRegionOrCallingCode == null) {
            return PhoneNumberType.UNKNOWN;
        }
        return getNumberTypeHelper(getNationalSignificantNumber(phonenumber$PhoneNumber), metadataForRegionOrCallingCode);
    }

    private PhoneNumberType getNumberTypeHelper(String str, Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata) {
        if (!isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getGeneralDesc())) {
            return PhoneNumberType.UNKNOWN;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getPremiumRate())) {
            return PhoneNumberType.PREMIUM_RATE;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getTollFree())) {
            return PhoneNumberType.TOLL_FREE;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getSharedCost())) {
            return PhoneNumberType.SHARED_COST;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getVoip())) {
            return PhoneNumberType.VOIP;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getPersonalNumber())) {
            return PhoneNumberType.PERSONAL_NUMBER;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getPager())) {
            return PhoneNumberType.PAGER;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getUan())) {
            return PhoneNumberType.UAN;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getVoicemail())) {
            return PhoneNumberType.VOICEMAIL;
        }
        if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getFixedLine())) {
            if (phonemetadata$PhoneMetadata.getSameMobileAndFixedLinePattern()) {
                return PhoneNumberType.FIXED_LINE_OR_MOBILE;
            }
            if (isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getMobile())) {
                return PhoneNumberType.FIXED_LINE_OR_MOBILE;
            }
            return PhoneNumberType.FIXED_LINE;
        }
        if (!phonemetadata$PhoneMetadata.getSameMobileAndFixedLinePattern() && isNumberMatchingDesc(str, phonemetadata$PhoneMetadata.getMobile())) {
            return PhoneNumberType.MOBILE;
        }
        return PhoneNumberType.UNKNOWN;
    }

    Phonemetadata$PhoneMetadata getMetadataForRegion(String str) {
        if (!isValidRegionCode(str)) {
            return null;
        }
        Phonemetadata$PhoneMetadata metadataForRegion = this.metadataSource.getMetadataForRegion(str);
        ensureMetadataIsNonNull(metadataForRegion, "Missing metadata for region code " + str);
        return metadataForRegion;
    }

    Phonemetadata$PhoneMetadata getMetadataForNonGeographicalRegion(int i) {
        if (!this.countryCodesForNonGeographicalRegion.contains(Integer.valueOf(i))) {
            return null;
        }
        Phonemetadata$PhoneMetadata metadataForNonGeographicalRegion = this.metadataSource.getMetadataForNonGeographicalRegion(i);
        ensureMetadataIsNonNull(metadataForNonGeographicalRegion, "Missing metadata for country code " + i);
        return metadataForNonGeographicalRegion;
    }

    private static void ensureMetadataIsNonNull(Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata, String str) {
        if (phonemetadata$PhoneMetadata == null) {
            throw new MissingMetadataException(str);
        }
    }

    boolean isNumberMatchingDesc(String str, Phonemetadata$PhoneNumberDesc phonemetadata$PhoneNumberDesc) {
        int length = str.length();
        List<Integer> possibleLengthList = phonemetadata$PhoneNumberDesc.getPossibleLengthList();
        if (possibleLengthList.size() <= 0 || possibleLengthList.contains(Integer.valueOf(length))) {
            return this.matcherApi.matchNationalNumber(str, phonemetadata$PhoneNumberDesc, false);
        }
        return false;
    }

    public boolean isValidNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        return isValidNumberForRegion(phonenumber$PhoneNumber, getRegionCodeForNumber(phonenumber$PhoneNumber));
    }

    public boolean isValidNumberForRegion(Phonenumber$PhoneNumber phonenumber$PhoneNumber, String str) {
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        Phonemetadata$PhoneMetadata metadataForRegionOrCallingCode = getMetadataForRegionOrCallingCode(countryCode, str);
        return metadataForRegionOrCallingCode != null && ("001".equals(str) || countryCode == getCountryCodeForValidRegion(str)) && getNumberTypeHelper(getNationalSignificantNumber(phonenumber$PhoneNumber), metadataForRegionOrCallingCode) != PhoneNumberType.UNKNOWN;
    }

    public String getRegionCodeForNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        List list = (List) this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(countryCode));
        if (list == null) {
            logger.log(Level.INFO, "Missing/invalid country_code (" + countryCode + ")");
            return null;
        }
        if (list.size() == 1) {
            return (String) list.get(0);
        }
        return getRegionCodeForNumberFromRegionList(phonenumber$PhoneNumber, list);
    }

    private String getRegionCodeForNumberFromRegionList(Phonenumber$PhoneNumber phonenumber$PhoneNumber, List list) {
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(str);
            if (metadataForRegion.hasLeadingDigits()) {
                if (this.regexCache.getPatternForRegex(metadataForRegion.getLeadingDigits()).matcher(nationalSignificantNumber).lookingAt()) {
                    return str;
                }
            } else if (getNumberTypeHelper(nationalSignificantNumber, metadataForRegion) != PhoneNumberType.UNKNOWN) {
                return str;
            }
        }
        return null;
    }

    public String getRegionCodeForCountryCode(int i) {
        List list = (List) this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(i));
        return list == null ? "ZZ" : (String) list.get(0);
    }

    public List<String> getRegionCodesForCountryCode(int i) {
        List arrayList = (List) this.countryCallingCodeToRegionCodeMap.get(Integer.valueOf(i));
        if (arrayList == null) {
            arrayList = new ArrayList(0);
        }
        return Collections.unmodifiableList(arrayList);
    }

    public int getCountryCodeForRegion(String str) {
        if (!isValidRegionCode(str)) {
            Logger logger2 = logger;
            Level level = Level.WARNING;
            StringBuilder sb = new StringBuilder("Invalid or missing region code (");
            if (str == null) {
                str = "null";
            }
            sb.append(str);
            sb.append(") provided.");
            logger2.log(level, sb.toString());
            return 0;
        }
        return getCountryCodeForValidRegion(str);
    }

    private int getCountryCodeForValidRegion(String str) {
        Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(str);
        if (metadataForRegion == null) {
            RILUtils$$ExternalSyntheticBUOutline1.m("Invalid region code: ", str);
            return 0;
        }
        return metadataForRegion.getCountryCode();
    }

    public String getNddPrefixForRegion(String str, boolean z) {
        Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(str);
        if (metadataForRegion == null) {
            Logger logger2 = logger;
            Level level = Level.WARNING;
            StringBuilder sb = new StringBuilder("Invalid or missing region code (");
            if (str == null) {
                str = "null";
            }
            sb.append(str);
            sb.append(") provided.");
            logger2.log(level, sb.toString());
            return null;
        }
        String nationalPrefix = metadataForRegion.getNationalPrefix();
        if (nationalPrefix.length() == 0) {
            return null;
        }
        return z ? nationalPrefix.replace("~", "") : nationalPrefix;
    }

    public boolean isNANPACountry(String str) {
        return this.nanpaRegions.contains(str);
    }

    public boolean isAlphaNumber(CharSequence charSequence) {
        if (!isViablePhoneNumber(charSequence)) {
            return false;
        }
        StringBuilder sb = new StringBuilder(charSequence);
        maybeStripExtension(sb);
        return VALID_ALPHA_PHONE_PATTERN.matcher(sb).matches();
    }

    public boolean isPossibleNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        ValidationResult validationResultIsPossibleNumberWithReason = isPossibleNumberWithReason(phonenumber$PhoneNumber);
        return validationResultIsPossibleNumberWithReason == ValidationResult.IS_POSSIBLE || validationResultIsPossibleNumberWithReason == ValidationResult.IS_POSSIBLE_LOCAL_ONLY;
    }

    public boolean isPossibleNumberForType(Phonenumber$PhoneNumber phonenumber$PhoneNumber, PhoneNumberType phoneNumberType) {
        ValidationResult validationResultIsPossibleNumberForTypeWithReason = isPossibleNumberForTypeWithReason(phonenumber$PhoneNumber, phoneNumberType);
        return validationResultIsPossibleNumberForTypeWithReason == ValidationResult.IS_POSSIBLE || validationResultIsPossibleNumberForTypeWithReason == ValidationResult.IS_POSSIBLE_LOCAL_ONLY;
    }

    private ValidationResult testNumberLength(CharSequence charSequence, Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata) {
        return testNumberLength(charSequence, phonemetadata$PhoneMetadata, PhoneNumberType.UNKNOWN);
    }

    private ValidationResult testNumberLength(CharSequence charSequence, Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata, PhoneNumberType phoneNumberType) {
        List<Integer> possibleLengthList;
        Phonemetadata$PhoneNumberDesc numberDescByType = getNumberDescByType(phonemetadata$PhoneMetadata, phoneNumberType);
        List<Integer> possibleLengthList2 = numberDescByType.getPossibleLengthList().isEmpty() ? phonemetadata$PhoneMetadata.getGeneralDesc().getPossibleLengthList() : numberDescByType.getPossibleLengthList();
        List<Integer> possibleLengthLocalOnlyList = numberDescByType.getPossibleLengthLocalOnlyList();
        if (phoneNumberType == PhoneNumberType.FIXED_LINE_OR_MOBILE) {
            if (!descHasPossibleNumberData(getNumberDescByType(phonemetadata$PhoneMetadata, PhoneNumberType.FIXED_LINE))) {
                return testNumberLength(charSequence, phonemetadata$PhoneMetadata, PhoneNumberType.MOBILE);
            }
            Phonemetadata$PhoneNumberDesc numberDescByType2 = getNumberDescByType(phonemetadata$PhoneMetadata, PhoneNumberType.MOBILE);
            if (descHasPossibleNumberData(numberDescByType2)) {
                ArrayList arrayList = new ArrayList(possibleLengthList2);
                if (numberDescByType2.getPossibleLengthCount() == 0) {
                    possibleLengthList = phonemetadata$PhoneMetadata.getGeneralDesc().getPossibleLengthList();
                } else {
                    possibleLengthList = numberDescByType2.getPossibleLengthList();
                }
                arrayList.addAll(possibleLengthList);
                Collections.sort(arrayList);
                if (possibleLengthLocalOnlyList.isEmpty()) {
                    possibleLengthLocalOnlyList = numberDescByType2.getPossibleLengthLocalOnlyList();
                } else {
                    ArrayList arrayList2 = new ArrayList(possibleLengthLocalOnlyList);
                    arrayList2.addAll(numberDescByType2.getPossibleLengthLocalOnlyList());
                    Collections.sort(arrayList2);
                    possibleLengthLocalOnlyList = arrayList2;
                }
                possibleLengthList2 = arrayList;
            }
        }
        if (possibleLengthList2.get(0).intValue() == -1) {
            return ValidationResult.INVALID_LENGTH;
        }
        int length = charSequence.length();
        if (possibleLengthLocalOnlyList.contains(Integer.valueOf(length))) {
            return ValidationResult.IS_POSSIBLE_LOCAL_ONLY;
        }
        int iIntValue = possibleLengthList2.get(0).intValue();
        if (iIntValue == length) {
            return ValidationResult.IS_POSSIBLE;
        }
        if (iIntValue > length) {
            return ValidationResult.TOO_SHORT;
        }
        if (possibleLengthList2.get(possibleLengthList2.size() - 1).intValue() < length) {
            return ValidationResult.TOO_LONG;
        }
        return possibleLengthList2.subList(1, possibleLengthList2.size()).contains(Integer.valueOf(length)) ? ValidationResult.IS_POSSIBLE : ValidationResult.INVALID_LENGTH;
    }

    public ValidationResult isPossibleNumberWithReason(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        return isPossibleNumberForTypeWithReason(phonenumber$PhoneNumber, PhoneNumberType.UNKNOWN);
    }

    public ValidationResult isPossibleNumberForTypeWithReason(Phonenumber$PhoneNumber phonenumber$PhoneNumber, PhoneNumberType phoneNumberType) {
        String nationalSignificantNumber = getNationalSignificantNumber(phonenumber$PhoneNumber);
        int countryCode = phonenumber$PhoneNumber.getCountryCode();
        if (!hasValidCountryCallingCode(countryCode)) {
            return ValidationResult.INVALID_COUNTRY_CODE;
        }
        return testNumberLength(nationalSignificantNumber, getMetadataForRegionOrCallingCode(countryCode, getRegionCodeForCountryCode(countryCode)), phoneNumberType);
    }

    public boolean isPossibleNumber(CharSequence charSequence, String str) {
        try {
            return isPossibleNumber(parse(charSequence, str));
        } catch (NumberParseException unused) {
            return false;
        }
    }

    public boolean truncateTooLongNumber(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        if (isValidNumber(phonenumber$PhoneNumber)) {
            return true;
        }
        Phonenumber$PhoneNumber phonenumber$PhoneNumber2 = new Phonenumber$PhoneNumber();
        phonenumber$PhoneNumber2.mergeFrom(phonenumber$PhoneNumber);
        long nationalNumber = phonenumber$PhoneNumber.getNationalNumber();
        do {
            nationalNumber /= 10;
            phonenumber$PhoneNumber2.setNationalNumber(nationalNumber);
            if (isPossibleNumberWithReason(phonenumber$PhoneNumber2) == ValidationResult.TOO_SHORT || nationalNumber == 0) {
                return false;
            }
        } while (!isValidNumber(phonenumber$PhoneNumber2));
        phonenumber$PhoneNumber.setNationalNumber(nationalNumber);
        return true;
    }

    public AsYouTypeFormatter getAsYouTypeFormatter(String str) {
        return new AsYouTypeFormatter(str);
    }

    int extractCountryCode(StringBuilder sb, StringBuilder sb2) {
        if (sb.length() != 0 && sb.charAt(0) != '0') {
            int length = sb.length();
            for (int i = 1; i <= 3 && i <= length; i++) {
                int i2 = Integer.parseInt(sb.substring(0, i));
                if (this.countryCallingCodeToRegionCodeMap.containsKey(Integer.valueOf(i2))) {
                    sb2.append(sb.substring(i));
                    return i2;
                }
            }
        }
        return 0;
    }

    int maybeExtractCountryCode(CharSequence charSequence, Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata, StringBuilder sb, boolean z, Phonenumber$PhoneNumber phonenumber$PhoneNumber) throws NumberParseException {
        String internationalPrefix;
        if (charSequence.length() == 0) {
            return 0;
        }
        StringBuilder sb2 = new StringBuilder(charSequence);
        if (phonemetadata$PhoneMetadata == null) {
            internationalPrefix = "NonMatch";
        } else {
            internationalPrefix = phonemetadata$PhoneMetadata.getInternationalPrefix();
        }
        Phonenumber$PhoneNumber.CountryCodeSource countryCodeSourceMaybeStripInternationalPrefixAndNormalize = maybeStripInternationalPrefixAndNormalize(sb2, internationalPrefix);
        if (z) {
            phonenumber$PhoneNumber.setCountryCodeSource(countryCodeSourceMaybeStripInternationalPrefixAndNormalize);
        }
        if (countryCodeSourceMaybeStripInternationalPrefixAndNormalize != Phonenumber$PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
            if (sb2.length() <= 2) {
                throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_AFTER_IDD, "Phone number had an IDD, but after this was not long enough to be a viable phone number.");
            }
            int iExtractCountryCode = extractCountryCode(sb2, sb);
            if (iExtractCountryCode != 0) {
                phonenumber$PhoneNumber.setCountryCode(iExtractCountryCode);
                return iExtractCountryCode;
            }
            throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Country calling code supplied was not recognised.");
        }
        if (phonemetadata$PhoneMetadata != null) {
            int countryCode = phonemetadata$PhoneMetadata.getCountryCode();
            String strValueOf = String.valueOf(countryCode);
            String string = sb2.toString();
            if (string.startsWith(strValueOf)) {
                StringBuilder sb3 = new StringBuilder(string.substring(strValueOf.length()));
                Phonemetadata$PhoneNumberDesc generalDesc = phonemetadata$PhoneMetadata.getGeneralDesc();
                maybeStripNationalPrefixAndCarrierCode(sb3, phonemetadata$PhoneMetadata, null);
                if ((!this.matcherApi.matchNationalNumber(sb2, generalDesc, false) && this.matcherApi.matchNationalNumber(sb3, generalDesc, false)) || testNumberLength(sb2, phonemetadata$PhoneMetadata) == ValidationResult.TOO_LONG) {
                    sb.append((CharSequence) sb3);
                    if (z) {
                        phonenumber$PhoneNumber.setCountryCodeSource(Phonenumber$PhoneNumber.CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN);
                    }
                    phonenumber$PhoneNumber.setCountryCode(countryCode);
                    return countryCode;
                }
            }
        }
        phonenumber$PhoneNumber.setCountryCode(0);
        return 0;
    }

    private boolean parsePrefixAsIdd(Pattern pattern, StringBuilder sb) {
        Matcher matcher = pattern.matcher(sb);
        if (!matcher.lookingAt()) {
            return false;
        }
        int iEnd = matcher.end();
        Matcher matcher2 = CAPTURING_DIGIT_PATTERN.matcher(sb.substring(iEnd));
        if (matcher2.find() && normalizeDigitsOnly(matcher2.group(1)).equals("0")) {
            return false;
        }
        sb.delete(0, iEnd);
        return true;
    }

    Phonenumber$PhoneNumber.CountryCodeSource maybeStripInternationalPrefixAndNormalize(StringBuilder sb, String str) {
        if (sb.length() == 0) {
            return Phonenumber$PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY;
        }
        Matcher matcher = PLUS_CHARS_PATTERN.matcher(sb);
        if (matcher.lookingAt()) {
            sb.delete(0, matcher.end());
            normalize(sb);
            return Phonenumber$PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN;
        }
        Pattern patternForRegex = this.regexCache.getPatternForRegex(str);
        normalize(sb);
        if (parsePrefixAsIdd(patternForRegex, sb)) {
            return Phonenumber$PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_IDD;
        }
        return Phonenumber$PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY;
    }

    boolean maybeStripNationalPrefixAndCarrierCode(StringBuilder sb, Phonemetadata$PhoneMetadata phonemetadata$PhoneMetadata, StringBuilder sb2) {
        int length = sb.length();
        String nationalPrefixForParsing = phonemetadata$PhoneMetadata.getNationalPrefixForParsing();
        if (length != 0 && nationalPrefixForParsing.length() != 0) {
            Matcher matcher = this.regexCache.getPatternForRegex(nationalPrefixForParsing).matcher(sb);
            if (matcher.lookingAt()) {
                Phonemetadata$PhoneNumberDesc generalDesc = phonemetadata$PhoneMetadata.getGeneralDesc();
                boolean zMatchNationalNumber = this.matcherApi.matchNationalNumber(sb, generalDesc, false);
                int iGroupCount = matcher.groupCount();
                String nationalPrefixTransformRule = phonemetadata$PhoneMetadata.getNationalPrefixTransformRule();
                if (nationalPrefixTransformRule == null || nationalPrefixTransformRule.length() == 0 || matcher.group(iGroupCount) == null) {
                    if (zMatchNationalNumber && !this.matcherApi.matchNationalNumber(sb.substring(matcher.end()), generalDesc, false)) {
                        return false;
                    }
                    if (sb2 != null && iGroupCount > 0 && matcher.group(iGroupCount) != null) {
                        sb2.append(matcher.group(1));
                    }
                    sb.delete(0, matcher.end());
                    return true;
                }
                StringBuilder sb3 = new StringBuilder(sb);
                sb3.replace(0, length, matcher.replaceFirst(nationalPrefixTransformRule));
                if (zMatchNationalNumber && !this.matcherApi.matchNationalNumber(sb3.toString(), generalDesc, false)) {
                    return false;
                }
                if (sb2 != null && iGroupCount > 1) {
                    sb2.append(matcher.group(1));
                }
                sb.replace(0, sb.length(), sb3.toString());
                return true;
            }
        }
        return false;
    }

    String maybeStripExtension(StringBuilder sb) {
        Matcher matcher = EXTN_PATTERN.matcher(sb);
        if (matcher.find() && isViablePhoneNumber(sb.substring(0, matcher.start()))) {
            int iGroupCount = matcher.groupCount();
            for (int i = 1; i <= iGroupCount; i++) {
                if (matcher.group(i) != null) {
                    String strGroup = matcher.group(i);
                    sb.delete(matcher.start(), sb.length());
                    return strGroup;
                }
            }
            return "";
        }
        return "";
    }

    private boolean checkRegionForParsing(CharSequence charSequence, String str) {
        if (isValidRegionCode(str)) {
            return true;
        }
        return (charSequence == null || charSequence.length() == 0 || !PLUS_CHARS_PATTERN.matcher(charSequence).lookingAt()) ? false : true;
    }

    public Phonenumber$PhoneNumber parse(CharSequence charSequence, String str) throws NumberParseException {
        Phonenumber$PhoneNumber phonenumber$PhoneNumber = new Phonenumber$PhoneNumber();
        parse(charSequence, str, phonenumber$PhoneNumber);
        return phonenumber$PhoneNumber;
    }

    public void parse(CharSequence charSequence, String str, Phonenumber$PhoneNumber phonenumber$PhoneNumber) throws NumberParseException {
        parseHelper(charSequence, str, false, true, phonenumber$PhoneNumber);
    }

    public Phonenumber$PhoneNumber parseAndKeepRawInput(CharSequence charSequence, String str) throws NumberParseException {
        Phonenumber$PhoneNumber phonenumber$PhoneNumber = new Phonenumber$PhoneNumber();
        parseAndKeepRawInput(charSequence, str, phonenumber$PhoneNumber);
        return phonenumber$PhoneNumber;
    }

    public void parseAndKeepRawInput(CharSequence charSequence, String str, Phonenumber$PhoneNumber phonenumber$PhoneNumber) throws NumberParseException {
        parseHelper(charSequence, str, true, true, phonenumber$PhoneNumber);
    }

    public Iterable<PhoneNumberMatch> findNumbers(CharSequence charSequence, String str) {
        return findNumbers(charSequence, str, Leniency.VALID, Long.MAX_VALUE);
    }

    public Iterable<PhoneNumberMatch> findNumbers(final CharSequence charSequence, final String str, final Leniency leniency, final long j) {
        return new Iterable<PhoneNumberMatch>() { // from class: com.android.internal.telephony.phonenumbers.PhoneNumberUtil.1
            @Override // java.lang.Iterable
            public Iterator<PhoneNumberMatch> iterator() {
                return new PhoneNumberMatcher(PhoneNumberUtil.this, charSequence, str, leniency, j);
            }
        };
    }

    static void setItalianLeadingZerosForPhoneNumber(CharSequence charSequence, Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        if (charSequence.length() <= 1 || charSequence.charAt(0) != '0') {
            return;
        }
        phonenumber$PhoneNumber.setItalianLeadingZero(true);
        int i = 1;
        while (i < charSequence.length() - 1 && charSequence.charAt(i) == '0') {
            i++;
        }
        if (i != 1) {
            phonenumber$PhoneNumber.setNumberOfLeadingZeros(i);
        }
    }

    private void parseHelper(CharSequence charSequence, String str, boolean z, boolean z2, Phonenumber$PhoneNumber phonenumber$PhoneNumber) throws NumberParseException {
        int iMaybeExtractCountryCode;
        if (charSequence == null) {
            throw new NumberParseException(NumberParseException.ErrorType.NOT_A_NUMBER, "The phone number supplied was null.");
        }
        if (charSequence.length() > 250) {
            throw new NumberParseException(NumberParseException.ErrorType.TOO_LONG, "The string supplied was too long to parse.");
        }
        StringBuilder sb = new StringBuilder();
        String string = charSequence.toString();
        buildNationalNumberForParsing(string, sb);
        if (!isViablePhoneNumber(sb)) {
            throw new NumberParseException(NumberParseException.ErrorType.NOT_A_NUMBER, "The string supplied did not seem to be a phone number.");
        }
        if (z2 && !checkRegionForParsing(sb, str)) {
            throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Missing or invalid default region.");
        }
        if (z) {
            phonenumber$PhoneNumber.setRawInput(string);
        }
        String strMaybeStripExtension = maybeStripExtension(sb);
        if (strMaybeStripExtension.length() > 0) {
            phonenumber$PhoneNumber.setExtension(strMaybeStripExtension);
        }
        Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(str);
        StringBuilder sb2 = new StringBuilder();
        boolean z3 = z;
        Phonenumber$PhoneNumber phonenumber$PhoneNumber2 = phonenumber$PhoneNumber;
        try {
            iMaybeExtractCountryCode = maybeExtractCountryCode(sb, metadataForRegion, sb2, z3, phonenumber$PhoneNumber2);
        } catch (NumberParseException e) {
            Matcher matcher = PLUS_CHARS_PATTERN.matcher(sb);
            if (e.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE && matcher.lookingAt()) {
                iMaybeExtractCountryCode = maybeExtractCountryCode(sb.substring(matcher.end()), metadataForRegion, sb2, z3, phonenumber$PhoneNumber2);
                if (iMaybeExtractCountryCode == 0) {
                    metadataForRegion = metadataForRegion;
                    sb2 = sb2;
                    z3 = z3;
                    phonenumber$PhoneNumber2 = phonenumber$PhoneNumber2;
                    throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE, "Could not interpret numbers after plus-sign.");
                }
            } else {
                throw new NumberParseException(e.getErrorType(), e.getMessage());
            }
        }
        if (iMaybeExtractCountryCode != 0) {
            String regionCodeForCountryCode = getRegionCodeForCountryCode(iMaybeExtractCountryCode);
            if (!regionCodeForCountryCode.equals(str)) {
                metadataForRegion = getMetadataForRegionOrCallingCode(iMaybeExtractCountryCode, regionCodeForCountryCode);
            }
        } else {
            sb2.append((CharSequence) normalize(sb));
            if (str != null) {
                phonenumber$PhoneNumber2.setCountryCode(metadataForRegion.getCountryCode());
            } else if (z3) {
                phonenumber$PhoneNumber2.clearCountryCodeSource();
            }
        }
        if (sb2.length() < 2) {
            throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_NSN, "The string supplied is too short to be a phone number.");
        }
        if (metadataForRegion != null) {
            StringBuilder sb3 = new StringBuilder();
            StringBuilder sb4 = new StringBuilder(sb2);
            maybeStripNationalPrefixAndCarrierCode(sb4, metadataForRegion, sb3);
            ValidationResult validationResultTestNumberLength = testNumberLength(sb4, metadataForRegion);
            if (validationResultTestNumberLength != ValidationResult.TOO_SHORT && validationResultTestNumberLength != ValidationResult.IS_POSSIBLE_LOCAL_ONLY && validationResultTestNumberLength != ValidationResult.INVALID_LENGTH) {
                if (z3 && sb3.length() > 0) {
                    phonenumber$PhoneNumber2.setPreferredDomesticCarrierCode(sb3.toString());
                }
                sb2 = sb4;
            }
        }
        int length = sb2.length();
        if (length < 2) {
            throw new NumberParseException(NumberParseException.ErrorType.TOO_SHORT_NSN, "The string supplied is too short to be a phone number.");
        }
        if (length > 17) {
            throw new NumberParseException(NumberParseException.ErrorType.TOO_LONG, "The string supplied is too long to be a phone number.");
        }
        setItalianLeadingZerosForPhoneNumber(sb2, phonenumber$PhoneNumber2);
        phonenumber$PhoneNumber2.setNationalNumber(Long.parseLong(sb2.toString()));
    }

    private String extractPhoneContext(String str, int i) {
        if (i == -1) {
            return null;
        }
        int i2 = i + 15;
        if (i2 >= str.length()) {
            return "";
        }
        int iIndexOf = str.indexOf(59, i2);
        if (iIndexOf != -1) {
            return str.substring(i2, iIndexOf);
        }
        return str.substring(i2);
    }

    private boolean isPhoneContextValid(String str) {
        if (str == null) {
            return true;
        }
        if (str.length() == 0) {
            return false;
        }
        return RFC3966_GLOBAL_NUMBER_DIGITS_PATTERN.matcher(str).matches() || RFC3966_DOMAINNAME_PATTERN.matcher(str).matches();
    }

    private void buildNationalNumberForParsing(String str, StringBuilder sb) throws NumberParseException {
        int iIndexOf = str.indexOf(";phone-context=");
        String strExtractPhoneContext = extractPhoneContext(str, iIndexOf);
        if (!isPhoneContextValid(strExtractPhoneContext)) {
            throw new NumberParseException(NumberParseException.ErrorType.NOT_A_NUMBER, "The phone-context value is invalid.");
        }
        if (strExtractPhoneContext != null) {
            if (strExtractPhoneContext.charAt(0) == '+') {
                sb.append(strExtractPhoneContext);
            }
            int iIndexOf2 = str.indexOf("tel:");
            sb.append(str.substring(iIndexOf2 >= 0 ? iIndexOf2 + 4 : 0, iIndexOf));
        } else {
            sb.append(extractPossibleNumber(str));
        }
        int iIndexOf3 = sb.indexOf(";isub=");
        if (iIndexOf3 > 0) {
            sb.delete(iIndexOf3, sb.length());
        }
    }

    private static Phonenumber$PhoneNumber copyCoreFieldsOnly(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        Phonenumber$PhoneNumber phonenumber$PhoneNumber2 = new Phonenumber$PhoneNumber();
        phonenumber$PhoneNumber2.setCountryCode(phonenumber$PhoneNumber.getCountryCode());
        phonenumber$PhoneNumber2.setNationalNumber(phonenumber$PhoneNumber.getNationalNumber());
        if (phonenumber$PhoneNumber.getExtension().length() > 0) {
            phonenumber$PhoneNumber2.setExtension(phonenumber$PhoneNumber.getExtension());
        }
        if (phonenumber$PhoneNumber.isItalianLeadingZero()) {
            phonenumber$PhoneNumber2.setItalianLeadingZero(true);
            phonenumber$PhoneNumber2.setNumberOfLeadingZeros(phonenumber$PhoneNumber.getNumberOfLeadingZeros());
        }
        return phonenumber$PhoneNumber2;
    }

    public MatchType isNumberMatch(Phonenumber$PhoneNumber phonenumber$PhoneNumber, Phonenumber$PhoneNumber phonenumber$PhoneNumber2) {
        Phonenumber$PhoneNumber phonenumber$PhoneNumberCopyCoreFieldsOnly = copyCoreFieldsOnly(phonenumber$PhoneNumber);
        Phonenumber$PhoneNumber phonenumber$PhoneNumberCopyCoreFieldsOnly2 = copyCoreFieldsOnly(phonenumber$PhoneNumber2);
        if (phonenumber$PhoneNumberCopyCoreFieldsOnly.hasExtension() && phonenumber$PhoneNumberCopyCoreFieldsOnly2.hasExtension() && !phonenumber$PhoneNumberCopyCoreFieldsOnly.getExtension().equals(phonenumber$PhoneNumberCopyCoreFieldsOnly2.getExtension())) {
            return MatchType.NO_MATCH;
        }
        int countryCode = phonenumber$PhoneNumberCopyCoreFieldsOnly.getCountryCode();
        int countryCode2 = phonenumber$PhoneNumberCopyCoreFieldsOnly2.getCountryCode();
        if (countryCode != 0 && countryCode2 != 0) {
            if (phonenumber$PhoneNumberCopyCoreFieldsOnly.exactlySameAs(phonenumber$PhoneNumberCopyCoreFieldsOnly2)) {
                return MatchType.EXACT_MATCH;
            }
            if (countryCode == countryCode2 && isNationalNumberSuffixOfTheOther(phonenumber$PhoneNumberCopyCoreFieldsOnly, phonenumber$PhoneNumberCopyCoreFieldsOnly2)) {
                return MatchType.SHORT_NSN_MATCH;
            }
            return MatchType.NO_MATCH;
        }
        phonenumber$PhoneNumberCopyCoreFieldsOnly.setCountryCode(countryCode2);
        if (phonenumber$PhoneNumberCopyCoreFieldsOnly.exactlySameAs(phonenumber$PhoneNumberCopyCoreFieldsOnly2)) {
            return MatchType.NSN_MATCH;
        }
        if (isNationalNumberSuffixOfTheOther(phonenumber$PhoneNumberCopyCoreFieldsOnly, phonenumber$PhoneNumberCopyCoreFieldsOnly2)) {
            return MatchType.SHORT_NSN_MATCH;
        }
        return MatchType.NO_MATCH;
    }

    private boolean isNationalNumberSuffixOfTheOther(Phonenumber$PhoneNumber phonenumber$PhoneNumber, Phonenumber$PhoneNumber phonenumber$PhoneNumber2) {
        String strValueOf = String.valueOf(phonenumber$PhoneNumber.getNationalNumber());
        String strValueOf2 = String.valueOf(phonenumber$PhoneNumber2.getNationalNumber());
        return strValueOf.endsWith(strValueOf2) || strValueOf2.endsWith(strValueOf);
    }

    public MatchType isNumberMatch(CharSequence charSequence, CharSequence charSequence2) {
        try {
            return isNumberMatch(parse(charSequence, "ZZ"), charSequence2);
        } catch (NumberParseException e) {
            if (e.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                try {
                    return isNumberMatch(parse(charSequence2, "ZZ"), charSequence);
                } catch (NumberParseException e2) {
                    if (e2.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                        try {
                            Phonenumber$PhoneNumber phonenumber$PhoneNumber = new Phonenumber$PhoneNumber();
                            Phonenumber$PhoneNumber phonenumber$PhoneNumber2 = new Phonenumber$PhoneNumber();
                            parseHelper(charSequence, null, false, false, phonenumber$PhoneNumber);
                            parseHelper(charSequence2, null, false, false, phonenumber$PhoneNumber2);
                            return isNumberMatch(phonenumber$PhoneNumber, phonenumber$PhoneNumber2);
                        } catch (NumberParseException unused) {
                            return MatchType.NOT_A_NUMBER;
                        }
                    }
                    return MatchType.NOT_A_NUMBER;
                }
            }
            return MatchType.NOT_A_NUMBER;
        }
    }

    public MatchType isNumberMatch(Phonenumber$PhoneNumber phonenumber$PhoneNumber, CharSequence charSequence) {
        try {
            return isNumberMatch(phonenumber$PhoneNumber, parse(charSequence, "ZZ"));
        } catch (NumberParseException e) {
            if (e.getErrorType() == NumberParseException.ErrorType.INVALID_COUNTRY_CODE) {
                String regionCodeForCountryCode = getRegionCodeForCountryCode(phonenumber$PhoneNumber.getCountryCode());
                try {
                    if (!regionCodeForCountryCode.equals("ZZ")) {
                        MatchType matchTypeIsNumberMatch = isNumberMatch(phonenumber$PhoneNumber, parse(charSequence, regionCodeForCountryCode));
                        return matchTypeIsNumberMatch == MatchType.EXACT_MATCH ? MatchType.NSN_MATCH : matchTypeIsNumberMatch;
                    }
                    Phonenumber$PhoneNumber phonenumber$PhoneNumber2 = new Phonenumber$PhoneNumber();
                    parseHelper(charSequence, null, false, false, phonenumber$PhoneNumber2);
                    return isNumberMatch(phonenumber$PhoneNumber, phonenumber$PhoneNumber2);
                } catch (NumberParseException unused) {
                    return MatchType.NOT_A_NUMBER;
                }
            }
            return MatchType.NOT_A_NUMBER;
        }
    }

    public boolean canBeInternationallyDialled(Phonenumber$PhoneNumber phonenumber$PhoneNumber) {
        Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(getRegionCodeForNumber(phonenumber$PhoneNumber));
        if (metadataForRegion == null) {
            return true;
        }
        return !isNumberMatchingDesc(getNationalSignificantNumber(phonenumber$PhoneNumber), metadataForRegion.getNoInternationalDialling());
    }

    public boolean isMobileNumberPortableRegion(String str) {
        Phonemetadata$PhoneMetadata metadataForRegion = getMetadataForRegion(str);
        if (metadataForRegion == null) {
            logger.log(Level.WARNING, "Invalid or unknown region code provided: " + str);
            return false;
        }
        return metadataForRegion.getMobileNumberPortableRegion();
    }
}
