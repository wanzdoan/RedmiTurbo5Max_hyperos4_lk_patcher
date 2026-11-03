package com.android.internal.telephony.nitz;

import android.app.timezonedetector.TelephonySignal;
import android.app.timezonedetector.TelephonyTimeZoneSuggestion;
import android.text.TextUtils;
import android.timezone.CountryTimeZones;
import android.timezone.MobileCountries;
import com.android.internal.telephony.MiuiNetworkControllerStub;
import com.android.internal.telephony.NitzData;
import com.android.internal.telephony.NitzSignal;
import com.android.internal.telephony.NitzStateMachine;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.telephony.Rlog;
import java.util.Objects;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class TimeZoneSuggesterImpl implements TimeZoneSuggester {
    private final NitzStateMachine.DeviceState mDeviceState;
    private final TimeZoneLookupHelper mTimeZoneLookupHelper;

    public TimeZoneSuggesterImpl(NitzStateMachine.DeviceState deviceState, TimeZoneLookupHelper timeZoneLookupHelper) {
        Objects.requireNonNull(deviceState);
        this.mDeviceState = deviceState;
        Objects.requireNonNull(timeZoneLookupHelper);
        this.mTimeZoneLookupHelper = timeZoneLookupHelper;
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0042  */
    @Override // com.android.internal.telephony.nitz.TimeZoneSuggester
    public TelephonyTimeZoneSuggestion getTimeZoneSuggestion(int i, MobileCountries mobileCountries, NitzSignal nitzSignal) {
        TelephonyTimeZoneSuggestion telephonyTimeZoneSuggestionBuild;
        if (nitzSignal != null) {
            try {
                NitzData nitzData = nitzSignal.getNitzData();
                if (nitzData.getEmulatorHostTimeZone() != null) {
                    telephonyTimeZoneSuggestionBuild = new TelephonyTimeZoneSuggestion.Builder(i).setZoneId(nitzData.getEmulatorHostTimeZone().getID()).setMatchType(4).setQuality(1).addDebugInfo("Emulator time zone override: " + nitzData).build();
                } else {
                    telephonyTimeZoneSuggestionBuild = null;
                }
            } catch (RuntimeException e) {
                String str = "getTimeZoneSuggestion: Error during lookup:  mobileCountries=" + mobileCountries + ", nitzSignal=" + nitzSignal + ", e=" + e.getMessage();
                TelephonyTimeZoneSuggestion telephonyTimeZoneSuggestionCreateEmptySuggestion = TelephonyTimeZoneSuggestion.createEmptySuggestion(i, str);
                Rlog.w("NitzStateMachineImpl", str, e);
                return telephonyTimeZoneSuggestionCreateEmptySuggestion;
            }
        } else {
            telephonyTimeZoneSuggestionBuild = null;
        }
        if (telephonyTimeZoneSuggestionBuild == null) {
            if (mobileCountries == null) {
                if (nitzSignal == null) {
                    telephonyTimeZoneSuggestionBuild = TelephonyTimeZoneSuggestion.createEmptySuggestion(i, "getTimeZoneSuggestion: nitzSignal=null, mobileCountries=null");
                } else {
                    telephonyTimeZoneSuggestionBuild = TelephonyTimeZoneSuggestion.createEmptySuggestion(i, "getTimeZoneSuggestion: nitzSignal=" + nitzSignal + ", countryIsoCode=null");
                }
            } else if (nitzSignal == null) {
                if (mobileCountries.getDefaultCountryIsoCode().isEmpty()) {
                    telephonyTimeZoneSuggestionBuild = TelephonyTimeZoneSuggestion.createEmptySuggestion(i, "getTimeZoneSuggestion: nitzSignal=null, defaultCountryIsoCode=\"\"");
                } else {
                    telephonyTimeZoneSuggestionBuild = findTimeZoneFromMobileCountries(i, mobileCountries, this.mDeviceState.currentTimeMillis());
                }
            } else if (mobileCountries.getDefaultCountryIsoCode().isEmpty()) {
                telephonyTimeZoneSuggestionBuild = findTimeZoneForTestNetwork(i, nitzSignal);
            } else {
                telephonyTimeZoneSuggestionBuild = findTimeZoneFromMobileCountriesAndNitz(i, mobileCountries, nitzSignal);
            }
        }
        Objects.requireNonNull(telephonyTimeZoneSuggestionBuild);
        return telephonyTimeZoneSuggestionBuild;
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0042  */
    @Override // com.android.internal.telephony.nitz.TimeZoneSuggester
    public TelephonyTimeZoneSuggestion getTimeZoneSuggestion(int i, String str, NitzSignal nitzSignal) {
        TelephonyTimeZoneSuggestion telephonyTimeZoneSuggestionBuild;
        if (nitzSignal != null) {
            try {
                NitzData nitzData = nitzSignal.getNitzData();
                if (nitzData.getEmulatorHostTimeZone() != null) {
                    telephonyTimeZoneSuggestionBuild = new TelephonyTimeZoneSuggestion.Builder(i).setZoneId(nitzData.getEmulatorHostTimeZone().getID()).setMatchType(4).setQuality(1).addDebugInfo("Emulator time zone override: " + nitzData).build();
                } else {
                    telephonyTimeZoneSuggestionBuild = null;
                }
            } catch (RuntimeException e) {
                String str2 = "getTimeZoneSuggestion: Error during lookup:  countryIsoCode=" + str + ", nitzSignal=" + nitzSignal + ", e=" + e.getMessage();
                TelephonyTimeZoneSuggestion telephonyTimeZoneSuggestionCreateEmptySuggestion = TelephonyTimeZoneSuggestion.createEmptySuggestion(i, str2);
                Rlog.w("NitzStateMachineImpl", str2, e);
                return telephonyTimeZoneSuggestionCreateEmptySuggestion;
            }
        } else {
            telephonyTimeZoneSuggestionBuild = null;
        }
        if (telephonyTimeZoneSuggestionBuild == null) {
            if (str == null) {
                if (nitzSignal == null) {
                    telephonyTimeZoneSuggestionBuild = TelephonyTimeZoneSuggestion.createEmptySuggestion(i, "getTimeZoneSuggestion: nitzSignal=null, countryIsoCode=null");
                } else {
                    telephonyTimeZoneSuggestionBuild = TelephonyTimeZoneSuggestion.createEmptySuggestion(i, "getTimeZoneSuggestion: nitzSignal=" + nitzSignal + ", countryIsoCode=null");
                }
            } else if (nitzSignal == null) {
                if (str.isEmpty()) {
                    telephonyTimeZoneSuggestionBuild = TelephonyTimeZoneSuggestion.createEmptySuggestion(i, "getTimeZoneSuggestion: nitzSignal=null, countryIsoCode=\"\"");
                } else {
                    telephonyTimeZoneSuggestionBuild = MiuiNetworkControllerStub.get(i).updateTimeZoneSuggesionFromNetworkCountryCode(findTimeZoneFromNetworkCountryCode(i, str, this.mDeviceState.currentTimeMillis()), str);
                }
            } else if (str.isEmpty()) {
                telephonyTimeZoneSuggestionBuild = findTimeZoneForTestNetwork(i, nitzSignal);
            } else {
                telephonyTimeZoneSuggestionBuild = MiuiNetworkControllerStub.get(i).updateTimeZoneSuggesionFromCountryAndNitz(this, findTimeZoneFromCountryAndNitz(i, str, nitzSignal), nitzSignal, str);
            }
        }
        Objects.requireNonNull(telephonyTimeZoneSuggestionBuild);
        return telephonyTimeZoneSuggestionBuild;
    }

    private TelephonyTimeZoneSuggestion findTimeZoneForTestNetwork(int i, NitzSignal nitzSignal) {
        Objects.requireNonNull(nitzSignal);
        NitzData nitzData = nitzSignal.getNitzData();
        Objects.requireNonNull(nitzData);
        TelephonyTimeZoneSuggestion.Builder builder = new TelephonyTimeZoneSuggestion.Builder(i);
        builder.addDebugInfo("findTimeZoneForTestNetwork: nitzSignal=" + nitzSignal);
        CountryTimeZones.OffsetResult offsetResultLookupByNitz = this.mTimeZoneLookupHelper.lookupByNitz(nitzData);
        if (offsetResultLookupByNitz == null) {
            builder.addDebugInfo("findTimeZoneForTestNetwork: No zone found");
        } else {
            builder.setZoneId(offsetResultLookupByNitz.getTimeZone().getID());
            builder.setMatchType(5);
            builder.setQuality(offsetResultLookupByNitz.isOnlyMatch() ? 1 : 2);
            builder.addDebugInfo("findTimeZoneForTestNetwork: lookupResult=" + offsetResultLookupByNitz);
        }
        return builder.build();
    }

    private TelephonyTimeZoneSuggestion findTimeZoneFromMobileCountriesAndNitz(int i, MobileCountries mobileCountries, NitzSignal nitzSignal) {
        Objects.requireNonNull(mobileCountries);
        Objects.requireNonNull(nitzSignal);
        TelephonyTimeZoneSuggestion.Builder builder = new TelephonyTimeZoneSuggestion.Builder(i);
        builder.addDebugInfo("findTimeZoneFromMobileCountriesAndNitz: mobileCountries=" + mobileCountries + ", nitzSignal=" + nitzSignal);
        NitzData nitzData = nitzSignal.getNitzData();
        Objects.requireNonNull(nitzData);
        if (isNitzSignalOffsetInfoBogus(mobileCountries, nitzData)) {
            builder.addDebugInfo("findTimeZoneFromMobileCountriesAndNitz: NITZ signal looks bogus");
            return builder.build();
        }
        CountryTimeZones.OffsetResult offsetResultLookupByNitzMobileCountries = this.mTimeZoneLookupHelper.lookupByNitzMobileCountries(nitzData, mobileCountries);
        if (offsetResultLookupByNitzMobileCountries != null) {
            return builder.setZoneId(offsetResultLookupByNitzMobileCountries.getTimeZone().getID()).setCountryIsoCode(offsetResultLookupByNitzMobileCountries.getCountryIsoCode()).setMatchType(3).setQuality(offsetResultLookupByNitzMobileCountries.isOnlyMatch() ? 1 : 2).setTelephonySignal((TelephonySignal) null).addDebugInfo("findTimeZoneFromMobileCountriesAndNitz: lookupResult=" + offsetResultLookupByNitzMobileCountries).build();
        }
        TimeZoneLookupHelper.CountryResult countryResultLookupByMobileCountries = this.mTimeZoneLookupHelper.lookupByMobileCountries(mobileCountries, nitzData.getCurrentTimeInMillis());
        if (countryResultLookupByMobileCountries == null) {
            builder.addDebugInfo("findTimeZoneFromMobileCountriesAndNitz: lookupByCountry() country not recognized");
            return builder.build();
        }
        int i2 = countryResultLookupByMobileCountries.quality;
        if (i2 == 1 || i2 == 2) {
            return builder.setZoneId(countryResultLookupByMobileCountries.zoneId).setCountryIsoCode(countryResultLookupByMobileCountries.countryIsoCode).setMatchType(2).setQuality(1).setTelephonySignal((TelephonySignal) null).addDebugInfo("findTimeZoneFromMobileCountriesAndNitz: high quality country-only suggestion: countryResult=" + countryResultLookupByMobileCountries).build();
        }
        builder.addDebugInfo("findTimeZoneFromMobileCountriesAndNitz: country-only suggestion quality not high enough. countryResult=" + countryResultLookupByMobileCountries);
        return builder.build();
    }

    public TelephonyTimeZoneSuggestion findTimeZoneFromCountryAndNitz(int i, String str, NitzSignal nitzSignal) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(nitzSignal);
        TelephonyTimeZoneSuggestion.Builder builder = new TelephonyTimeZoneSuggestion.Builder(i);
        builder.addDebugInfo("findTimeZoneFromCountryAndNitz: countryIsoCode=" + str + ", nitzSignal=" + nitzSignal);
        NitzData nitzData = nitzSignal.getNitzData();
        Objects.requireNonNull(nitzData);
        if (lambda$isNitzSignalOffsetInfoBogus$0(str, nitzData)) {
            builder.addDebugInfo("findTimeZoneFromCountryAndNitz: NITZ signal looks bogus");
            return builder.build();
        }
        CountryTimeZones.OffsetResult offsetResultLambda$lookupByNitzMobileCountries$0 = this.mTimeZoneLookupHelper.lambda$lookupByNitzMobileCountries$0(nitzData, str);
        if (offsetResultLambda$lookupByNitzMobileCountries$0 != null) {
            builder.setZoneId(offsetResultLambda$lookupByNitzMobileCountries$0.getTimeZone().getID());
            builder.setMatchType(3);
            builder.setQuality(offsetResultLambda$lookupByNitzMobileCountries$0.isOnlyMatch() ? 1 : 2);
            builder.addDebugInfo("findTimeZoneFromCountryAndNitz: lookupResult=" + offsetResultLambda$lookupByNitzMobileCountries$0);
            return builder.build();
        }
        TimeZoneLookupHelper.CountryResult countryResultLambda$lookupByMobileCountries$2 = this.mTimeZoneLookupHelper.lambda$lookupByMobileCountries$2(str, nitzData.getCurrentTimeInMillis());
        if (countryResultLambda$lookupByMobileCountries$2 == null) {
            builder.addDebugInfo("findTimeZoneFromCountryAndNitz: lookupByCountry() country not recognized");
            return builder.build();
        }
        int i2 = countryResultLambda$lookupByMobileCountries$2.quality;
        if (i2 == 1 || i2 == 2) {
            builder.setZoneId(countryResultLambda$lookupByMobileCountries$2.zoneId);
            builder.setMatchType(2);
            builder.setQuality(1);
            builder.addDebugInfo("findTimeZoneFromCountryAndNitz: high quality country-only suggestion: countryResult=" + countryResultLambda$lookupByMobileCountries$2);
            return builder.build();
        }
        builder.addDebugInfo("findTimeZoneFromCountryAndNitz: country-only suggestion quality not high enough. countryResult=" + countryResultLambda$lookupByMobileCountries$2);
        return builder.build();
    }

    private TelephonyTimeZoneSuggestion findTimeZoneFromMobileCountries(int i, MobileCountries mobileCountries, long j) {
        Objects.requireNonNull(mobileCountries);
        if (TextUtils.isEmpty(mobileCountries.getDefaultCountryIsoCode())) {
            Phone$$ExternalSyntheticBUOutline0.m("defaultCountryIsoCode must not be empty");
            return null;
        }
        TelephonyTimeZoneSuggestion.Builder builder = new TelephonyTimeZoneSuggestion.Builder(i);
        builder.addDebugInfo("findTimeZoneFromMobileCountries: whenMillis=" + j + ", mobileCountries=" + mobileCountries);
        TimeZoneLookupHelper.CountryResult countryResultLookupByMobileCountries = this.mTimeZoneLookupHelper.lookupByMobileCountries(mobileCountries, j);
        if (countryResultLookupByMobileCountries != null) {
            int i2 = countryResultLookupByMobileCountries.quality;
            int i3 = 1;
            if (i2 != 1 && i2 != 2) {
                i3 = 3;
                if (i2 == 3) {
                    i3 = 2;
                } else if (i2 != 4) {
                    throw new IllegalArgumentException("lookupResult.quality not recognized: mobileCountries=" + mobileCountries + ", whenMillis=" + j + ", lookupResult=" + countryResultLookupByMobileCountries);
                }
            }
            builder.setZoneId(countryResultLookupByMobileCountries.zoneId).setCountryIsoCode(countryResultLookupByMobileCountries.countryIsoCode).setMatchType(2).setQuality(i3).setTelephonySignal((TelephonySignal) null).addDebugInfo("findTimeZoneFromMobileCountries: lookupResult=" + countryResultLookupByMobileCountries);
        } else {
            builder.addDebugInfo("findTimeZoneFromMobileCountries: Country not recognized?");
        }
        return builder.build();
    }

    private TelephonyTimeZoneSuggestion findTimeZoneFromNetworkCountryCode(int i, String str, long j) {
        Objects.requireNonNull(str);
        if (TextUtils.isEmpty(str)) {
            Phone$$ExternalSyntheticBUOutline0.m("countryIsoCode must not be empty");
            return null;
        }
        TelephonyTimeZoneSuggestion.Builder builder = new TelephonyTimeZoneSuggestion.Builder(i);
        builder.addDebugInfo("findTimeZoneFromNetworkCountryCode: whenMillis=" + j + ", countryIsoCode=" + str);
        TimeZoneLookupHelper.CountryResult countryResultLambda$lookupByMobileCountries$2 = this.mTimeZoneLookupHelper.lambda$lookupByMobileCountries$2(str, j);
        if (countryResultLambda$lookupByMobileCountries$2 != null) {
            builder.setZoneId(countryResultLambda$lookupByMobileCountries$2.zoneId);
            int i2 = 2;
            builder.setMatchType(2);
            int i3 = countryResultLambda$lookupByMobileCountries$2.quality;
            int i4 = 1;
            if (i3 == 1 || i3 == 2) {
                i2 = i4;
            } else {
                i4 = 3;
                if (i3 != 3) {
                    if (i3 != 4) {
                        throw new IllegalArgumentException("lookupResult.quality not recognized: countryIsoCode=" + str + ", whenMillis=" + j + ", lookupResult=" + countryResultLambda$lookupByMobileCountries$2);
                    }
                    i2 = i4;
                }
            }
            builder.setQuality(i2);
            builder.addDebugInfo("findTimeZoneFromNetworkCountryCode: lookupResult=" + countryResultLambda$lookupByMobileCountries$2);
        } else {
            builder.addDebugInfo("findTimeZoneFromNetworkCountryCode: Country not recognized?");
        }
        return builder.build();
    }

    private boolean isNitzSignalOffsetInfoBogus(MobileCountries mobileCountries, final NitzData nitzData) {
        return mobileCountries.getCountryIsoCodes().stream().allMatch(new Predicate() { // from class: com.android.internal.telephony.nitz.TimeZoneSuggesterImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return this.f$0.lambda$isNitzSignalOffsetInfoBogus$0(nitzData, (String) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: isNitzSignalOffsetInfoBogus, reason: merged with bridge method [inline-methods] */
    public boolean lambda$isNitzSignalOffsetInfoBogus$0(String str, NitzData nitzData) {
        return (TextUtils.isEmpty(str) || nitzData.getLocalOffsetMillis() != 0 || countryUsesUtc(str, nitzData)) ? false : true;
    }

    private boolean countryUsesUtc(String str, NitzData nitzData) {
        return this.mTimeZoneLookupHelper.countryUsesUtc(str, nitzData.getCurrentTimeInMillis());
    }
}
