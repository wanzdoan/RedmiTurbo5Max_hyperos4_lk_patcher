package com.android.internal.telephony.nitz;

import android.icu.util.TimeZone;
import android.text.TextUtils;
import android.timezone.CountryTimeZones;
import android.timezone.MobileCountries;
import android.timezone.TimeZoneFinder;
import com.android.internal.telephony.NitzData;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public final class TimeZoneLookupHelper {
    private CountryTimeZones mLastCountryTimeZones;

    public static final class CountryResult {
        public static final int QUALITY_DEFAULT_BOOSTED = 2;
        public static final int QUALITY_MULTIPLE_ZONES_DIFFERENT_OFFSETS = 4;
        public static final int QUALITY_MULTIPLE_ZONES_SAME_OFFSET = 3;
        public static final int QUALITY_SINGLE_ZONE = 1;
        public final String countryIsoCode;
        private final String mDebugInfo;
        public final int quality;
        public final String zoneId;

        public CountryResult(String str, String str2, int i, String str3) {
            Objects.requireNonNull(str);
            this.zoneId = str;
            Objects.requireNonNull(str2);
            this.countryIsoCode = str2;
            this.quality = i;
            this.mDebugInfo = str3;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && CountryResult.class == obj.getClass()) {
                CountryResult countryResult = (CountryResult) obj;
                if (this.quality == countryResult.quality && this.zoneId.equals(countryResult.zoneId) && this.countryIsoCode.equals(countryResult.countryIsoCode)) {
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(this.zoneId, this.countryIsoCode, Integer.valueOf(this.quality));
        }

        public String toString() {
            return "CountryResult{zoneId='" + this.zoneId + "', countryIsoCode=" + this.countryIsoCode + ", quality=" + this.quality + ", mDebugInfo=" + this.mDebugInfo + '}';
        }
    }

    /* JADX INFO: renamed from: lookupByNitzCountry, reason: merged with bridge method [inline-methods] */
    public CountryTimeZones.OffsetResult lambda$lookupByNitzMobileCountries$0(NitzData nitzData, String str) {
        CountryTimeZones countryTimeZones = getCountryTimeZones(str);
        if (countryTimeZones == null) {
            return null;
        }
        TimeZone timeZone = TimeZone.getDefault();
        Integer dstAdjustmentMillis = nitzData.getDstAdjustmentMillis();
        if (dstAdjustmentMillis == null) {
            return countryTimeZones.lookupByOffsetWithBias(nitzData.getCurrentTimeInMillis(), timeZone, nitzData.getLocalOffsetMillis());
        }
        return countryTimeZones.lookupByOffsetWithBias(nitzData.getCurrentTimeInMillis(), timeZone, nitzData.getLocalOffsetMillis(), dstAdjustmentMillis.intValue() != 0);
    }

    public CountryTimeZones.OffsetResult lookupByNitz(NitzData nitzData) {
        Boolean boolValueOf;
        int localOffsetMillis = nitzData.getLocalOffsetMillis();
        long currentTimeInMillis = nitzData.getCurrentTimeInMillis();
        Integer dstAdjustmentMillis = nitzData.getDstAdjustmentMillis();
        if (dstAdjustmentMillis == null) {
            boolValueOf = null;
        } else {
            boolValueOf = Boolean.valueOf(dstAdjustmentMillis.intValue() != 0);
        }
        CountryTimeZones.OffsetResult offsetResultLookupByInstantOffsetDst = lookupByInstantOffsetDst(currentTimeInMillis, localOffsetMillis, boolValueOf);
        return (offsetResultLookupByInstantOffsetDst != null || boolValueOf == null) ? offsetResultLookupByInstantOffsetDst : lookupByInstantOffsetDst(currentTimeInMillis, localOffsetMillis, null);
    }

    public CountryTimeZones.OffsetResult lookupByNitzMobileCountries(final NitzData nitzData, MobileCountries mobileCountries) {
        List list = (List) mobileCountries.getCountryIsoCodes().stream().map(new Function() { // from class: com.android.internal.telephony.nitz.TimeZoneLookupHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$lookupByNitzMobileCountries$0(nitzData, (String) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.nitz.TimeZoneLookupHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((CountryTimeZones.OffsetResult) obj);
            }
        }).distinct().collect(Collectors.toList());
        if (list.size() <= 1) {
            if (list.isEmpty()) {
                return null;
            }
            return (CountryTimeZones.OffsetResult) list.get(0);
        }
        if (list.stream().map(new Function() { // from class: com.android.internal.telephony.nitz.TimeZoneLookupHelper$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(((CountryTimeZones.OffsetResult) obj).getTimeZone().getOffset(nitzData.getCurrentTimeInMillis()));
            }
        }).distinct().count() == 1) {
            return lambda$lookupByNitzMobileCountries$0(nitzData, mobileCountries.getDefaultCountryIsoCode());
        }
        return null;
    }

    public CountryResult lookupByMobileCountries(MobileCountries mobileCountries, final long j) {
        List list = (List) mobileCountries.getCountryIsoCodes().stream().map(new Function() { // from class: com.android.internal.telephony.nitz.TimeZoneLookupHelper$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return this.f$0.lambda$lookupByMobileCountries$2(j, (String) obj);
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.nitz.TimeZoneLookupHelper$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return Objects.nonNull((TimeZoneLookupHelper.CountryResult) obj);
            }
        }).distinct().collect(Collectors.toList());
        if (list.size() <= 1) {
            if (list.isEmpty()) {
                return null;
            }
            return (CountryResult) list.get(0);
        }
        if (list.stream().map(new Function() { // from class: com.android.internal.telephony.nitz.TimeZoneLookupHelper$$ExternalSyntheticLambda5
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return Integer.valueOf(TimeZone.getFrozenTimeZone(((TimeZoneLookupHelper.CountryResult) obj).zoneId).getOffset(j));
            }
        }).distinct().count() == 1) {
            return lookupByCountry(mobileCountries.getDefaultCountryIsoCode(), j, 3);
        }
        return null;
    }

    /* JADX INFO: renamed from: lookupByCountry, reason: merged with bridge method [inline-methods] */
    public CountryResult lambda$lookupByMobileCountries$2(String str, long j) {
        return lookupByCountry(str, j, null);
    }

    private CountryResult lookupByCountry(String str, long j, Integer num) {
        TimeZone defaultTimeZone;
        String str2;
        int iIntValue;
        CountryTimeZones countryTimeZones = getCountryTimeZones(str);
        if (countryTimeZones == null || (defaultTimeZone = countryTimeZones.getDefaultTimeZone()) == null) {
            return null;
        }
        if (countryTimeZones.isDefaultTimeZoneBoosted()) {
            iIntValue = 2;
            str2 = "Country default is boosted";
        } else {
            List effectiveTimeZoneMappingsAt = countryTimeZones.getEffectiveTimeZoneMappingsAt(j);
            if (effectiveTimeZoneMappingsAt.isEmpty()) {
                str2 = "No effective time zones found at whenMillis=" + j;
                iIntValue = 4;
            } else if (effectiveTimeZoneMappingsAt.size() == 1) {
                str2 = "One effective time zone found at whenMillis=" + j;
                iIntValue = 1;
            } else {
                boolean zCountryUsesDifferentOffsets = countryUsesDifferentOffsets(j, effectiveTimeZoneMappingsAt, defaultTimeZone);
                str2 = "countryUsesDifferentOffsets=" + zCountryUsesDifferentOffsets + " at whenMillis=" + j;
                iIntValue = zCountryUsesDifferentOffsets ? 4 : 3;
            }
        }
        String id = defaultTimeZone.getID();
        if (num != null) {
            iIntValue = num.intValue();
        }
        return new CountryResult(id, str, iIntValue, str2);
    }

    private static boolean countryUsesDifferentOffsets(long j, List list, TimeZone timeZone) {
        String id = timeZone.getID();
        int offset = timeZone.getOffset(j);
        Iterator it = list.iterator();
        while (it.hasNext()) {
            CountryTimeZones.TimeZoneMapping timeZoneMapping = (CountryTimeZones.TimeZoneMapping) it.next();
            if (!timeZoneMapping.getTimeZoneId().equals(id) && offset != timeZoneMapping.getTimeZone().getOffset(j)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:11:0x0020 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:12:0x0021  */
    private static CountryTimeZones.OffsetResult lookupByInstantOffsetDst(long j, int i, Boolean bool) {
        boolean z = false;
        TimeZone timeZone = null;
        for (String str : java.util.TimeZone.getAvailableIDs()) {
            TimeZone frozenTimeZone = TimeZone.getFrozenTimeZone(str);
            if (offsetMatchesAtTime(frozenTimeZone, i, bool, j)) {
                if (timeZone != null) {
                    if (timeZone == null) {
                        return null;
                    }
                    return new CountryTimeZones.OffsetResult(timeZone, (String) null, z);
                }
                timeZone = frozenTimeZone;
            }
        }
        z = true;
        if (timeZone == null) {
            return null;
        }
        return new CountryTimeZones.OffsetResult(timeZone, (String) null, z);
    }

    private static boolean offsetMatchesAtTime(TimeZone timeZone, int i, Boolean bool, long j) {
        int[] iArr = new int[2];
        timeZone.getOffset(j, false, iArr);
        if (i != iArr[0] + iArr[1]) {
            return false;
        }
        if (bool != null) {
            if (bool.booleanValue() != (iArr[1] != 0)) {
                return false;
            }
        }
        return true;
    }

    public boolean countryUsesUtc(String str, long j) {
        CountryTimeZones countryTimeZones;
        return (TextUtils.isEmpty(str) || (countryTimeZones = getCountryTimeZones(str)) == null || !countryTimeZones.hasUtcZone(j)) ? false : true;
    }

    private CountryTimeZones getCountryTimeZones(String str) {
        Objects.requireNonNull(str);
        synchronized (this) {
            try {
                CountryTimeZones countryTimeZones = this.mLastCountryTimeZones;
                if (countryTimeZones != null && countryTimeZones.getCountryIso().equals(str.toLowerCase(Locale.US))) {
                    return this.mLastCountryTimeZones;
                }
                CountryTimeZones countryTimeZonesLookupCountryTimeZones = TimeZoneFinder.getInstance().lookupCountryTimeZones(str);
                if (countryTimeZonesLookupCountryTimeZones != null) {
                    this.mLastCountryTimeZones = countryTimeZonesLookupCountryTimeZones;
                }
                return countryTimeZonesLookupCountryTimeZones;
            } catch (Throwable th) {
                throw th;
            }
        }
    }
}
