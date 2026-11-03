package com.android.internal.telephony;

import android.app.timezonedetector.TelephonyTimeZoneSuggestion;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.icu.util.TimeZone;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.sysprop.TelephonyProperties;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.timezone.CountryTimeZones;
import android.timezone.TimeZoneFinder;
import android.util.Log;
import android.util.TimeUtils;
import com.android.internal.telephony.nitz.TimeZoneSuggesterImpl;
import com.xiaomi.modem.ModemUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import miui.telephony.MiuiTelephony;

/* JADX INFO: loaded from: classes.dex */
public class MiuiTimeZoneOptimization {
    private static final String ACTION_NITZ_TIMEZONE_PREFERRED_OPTIMIZATION = "miui.intent.action.NITZ_TIMEZONE_PREFERRED_OPTIMIZATION";
    private static final int EVENT_GET_OPERATOR = 1;
    private static final String EXTRA_CACHE_ZONEID = "cache_zoneid";
    private static final String EXTRA_COUNTRY_ISO = "countryIso";
    private static final String EXTRA_CURRENT_ZONEID = "current_zoneid";
    private static final String EXTRA_NITZ_OFFSET = "nitz_offset";
    private static final String EXTRA_OPERATOR = "operator";
    private static final String EXTRA_REASON = "reason";
    private static final String EXTRA_SUGGESTION_OFFSET = "suggestion_offset";
    private static final String EXTRA_SUGGESTION_ZONEID = "suggestion_zoneid";
    private static final long GEO_LOCATION_VALID_DAY = 86400000;
    private static final long GEO_LOCATION_VALID_TIME = 1800000;
    private static final int INVALID_NITZ_OFFSET = -1;
    private static final String KEY_COUNTRY_ISO = "key_countryIso";
    private static final String KEY_NITZ_DATA = "key_nitzData";
    private static final String LAST_TIME_ZONE = "last_time_zone";
    private static final String LOG_TAG = "MiuiTimeZoneOptimization";
    private static final int MS_PER_HOUR = 3600000;
    private static final Pattern NITZ_SPLIT_PATTERN = Pattern.compile("[/:,+-]");
    private static final String NITZ_VALID_COUNTRY = "nitz_valid_country";
    private static final String SEPARATOR = "-";
    private static final String SEPARATOR_COMMA = ",";
    private static final String TZDATA_VERSION = "tzdata_version";
    private static final String UNKNOWN_COUNTRY_ISO = "unknown_countryIso";
    private Context mContext;
    private String mCountryIso;
    private MiuiNetworkControllerImpl mNetworkControllerImpl;
    private Phone mPhone;
    private SharedPreferences mSharedPreferences;
    private SubscriptionManager mSubscriptionManager;
    private MiuiTimeZoneOptimization sInstance;
    private boolean mIsSupportFeature = false;
    private HashMap<String, Object> mCountryIsoNitzData = new HashMap<>();
    private Handler mHandler = new Handler() { // from class: com.android.internal.telephony.MiuiTimeZoneOptimization.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    MiuiTimeZoneOptimization.this.log("EVENT_GET_OPERATOR");
                    AsyncResult ar = (AsyncResult) msg.obj;
                    String[] opNames = (String[]) ar.result;
                    NitzData nitzData = (NitzData) ar.userObj;
                    MiuiTimeZoneOptimization.this.updateNitzDataAndCountry(opNames, nitzData);
                    break;
            }
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.MiuiTimeZoneOptimization.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null && "android.telephony.action.NETWORK_COUNTRY_CHANGED".equals(intent.getAction())) {
                List<String> newProp = new ArrayList<>(TelephonyProperties.operator_iso_country());
                String currentCountryIso = "";
                for (int index = 0; index < newProp.size(); index++) {
                    if (!TextUtils.isEmpty(newProp.get(index))) {
                        String currentCountryIso2 = newProp.get(index);
                        currentCountryIso = currentCountryIso2;
                        break;
                    }
                }
                MiuiTimeZoneOptimization.this.mCountryIso = currentCountryIso;
            }
        }
    };

    public MiuiTimeZoneOptimization(Phone phone) {
        this.mCountryIso = "";
        this.mPhone = phone;
        this.mContext = phone.getContext();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.telephony.action.NETWORK_COUNTRY_CHANGED");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        this.mNetworkControllerImpl = (MiuiNetworkControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiNetworkControllerImpl.class, this.mPhone.getPhoneId());
        this.mSubscriptionManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        List<String> newProp = new ArrayList<>(TelephonyProperties.operator_iso_country());
        for (int index = 0; index < newProp.size(); index++) {
            if (!TextUtils.isEmpty(newProp.get(index))) {
                this.mCountryIso = newProp.get(index);
                break;
            }
        }
        checkTzdataVersion();
    }

    private void checkTzdataVersion() {
        String oldVersion = this.mSharedPreferences.getString(TZDATA_VERSION, "");
        String newVersion = TimeUtils.getTimeZoneDatabaseVersion();
        log("oldVersion = " + oldVersion + ", newVersion = " + newVersion);
        if (!oldVersion.equals(newVersion) && !TextUtils.isEmpty(newVersion)) {
            clearTimeZoneSp();
            SharedPreferences.Editor spEditor = this.mSharedPreferences.edit();
            spEditor.putString(TZDATA_VERSION, newVersion).commit();
        }
    }

    public void notifyNitzTimeChange(NitzData nitzData) {
        if (nitzData == null) {
            return;
        }
        NitzData nitzDataRecord = this.mCountryIsoNitzData.isEmpty() ? null : (NitzData) this.mCountryIsoNitzData.get(KEY_NITZ_DATA);
        log("notifyNitzTimeChange, nitzData = " + nitzData + ", nitzDataRecord = " + nitzDataRecord);
        if (nitzDataRecord == null || nitzDataRecord.getLocalOffsetMillis() != nitzData.getLocalOffsetMillis()) {
            this.mPhone.mCi.getOperator(this.mHandler.obtainMessage(1, nitzData));
        }
    }

    public TelephonyTimeZoneSuggestion updateTimeZoneSuggesionFromNetworkCountryCode(TelephonyTimeZoneSuggestion suggestion, String countryIso) {
        log("updateTimeZoneSuggesionFromNetworkCountryCode");
        if (isInChina(countryIso) || suggestion == null || TextUtils.isEmpty(countryIso)) {
            return suggestion;
        }
        return updateTimeZoneByNitzCache(suggestion, countryIso);
    }

    private TelephonyTimeZoneSuggestion updateTimeZoneByNitzCache(TelephonyTimeZoneSuggestion suggestion, String countryIso) {
        TelephonyTimeZoneSuggestion suggestion2;
        if (!isValidTimeZoneSuggestion(suggestion)) {
            suggestion2 = suggestion;
        } else {
            setTelephonyVaildCountrySp(countryIso);
            String timeZoneCache = getCacheTimeZone(countryIso);
            if (TextUtils.isEmpty(timeZoneCache) || suggestion.getZoneId() == null) {
                suggestion2 = suggestion;
                trackOptimization(countryIso, -1, timeZoneCache, suggestion2, "country-only timezone");
            } else {
                TimeZone suggestionTimeZone = TimeZone.getTimeZone(suggestion.getZoneId());
                TimeZone cacheTimeZone = TimeZone.getTimeZone(timeZoneCache);
                long currentTime = System.currentTimeMillis();
                if (suggestionTimeZone == null || cacheTimeZone == null || suggestionTimeZone.getOffset(currentTime) == cacheTimeZone.getOffset(currentTime)) {
                    suggestion2 = suggestion;
                } else {
                    TelephonyTimeZoneSuggestion newSuggestion = findTimeZoneFromNitz(suggestion.getSlotIndex(), timeZoneCache, "Telephony NITZ priority: use cache NITZ timezone");
                    trackOptimization(countryIso, -1, timeZoneCache, suggestion, "use cache NITZ timezone");
                    return newSuggestion;
                }
            }
        }
        log("updateTimeZoneByNitzCache");
        return suggestion2;
    }

    public TelephonyTimeZoneSuggestion updateTimeZoneSuggesionFromCountryAndNitz(TimeZoneSuggesterImpl timeZoneSuggesterImpl, TelephonyTimeZoneSuggestion suggestion, NitzSignal nitzSignal, String countryIso) {
        if (isInChina(countryIso) || timeZoneSuggesterImpl == null || suggestion == null || nitzSignal == null || TextUtils.isEmpty(countryIso)) {
            return suggestion;
        }
        setTelephonyVaildCountrySp(countryIso);
        log("updateTimeZoneSuggesionFromCountryAndNitz, suggestion = " + suggestion + ", nitzSignal = " + nitzSignal + ", countryIso = " + countryIso);
        if (shouldUpdateTimeZoneByOffsetDifferent(timeZoneSuggesterImpl, suggestion, nitzSignal, countryIso)) {
            NitzData newDstNitzData = createNitzDataChangeDst(nitzSignal);
            NitzSignal newNitzSignal = new NitzSignal(nitzSignal.getReceiptElapsedRealtimeMillis(), newDstNitzData, nitzSignal.getAgeMillis());
            TelephonyTimeZoneSuggestion newSuggestion = timeZoneSuggesterImpl.findTimeZoneFromCountryAndNitz(this.mPhone.getPhoneId(), countryIso, newNitzSignal);
            if (isValidTimeZoneSuggestion(newSuggestion) && isMatchOffset(newSuggestion, newNitzSignal)) {
                trackOptimization(countryIso, nitzSignal.getNitzData().getLocalOffsetMillis(), getCacheTimeZone(countryIso), newSuggestion, "change DST match");
                clearTimeZoneSp(countryIso);
                return newSuggestion;
            }
            int nitzOffset = nitzSignal.getNitzData().getLocalOffsetMillis();
            if (nitzOffset % MS_PER_HOUR == 0) {
                String newTimezoneId = getNewTimeZoneId(nitzOffset);
                TelephonyTimeZoneSuggestion newSuggestion2 = findTimeZoneFromNitz(suggestion.getSlotIndex(), newTimezoneId, "Telephony NITZ priority");
                setCacheTimeZone(countryIso, newTimezoneId);
                trackOptimization(countryIso, nitzSignal.getNitzData().getLocalOffsetMillis(), newTimezoneId, suggestion, "NITZ priority");
                return newSuggestion2;
            }
            if (isAutoTimeSwitchOn()) {
                log("not match time zone");
                clearTimeZoneSp(countryIso);
            }
        } else {
            boolean isSuggestionValid = isValidTimeZoneSuggestion(suggestion);
            boolean isNitzValid = isValidNitz(countryIso, nitzSignal);
            boolean isMatchOffset = isMatchOffset(suggestion, nitzSignal);
            String cacheTimeZone = getCacheTimeZone(countryIso);
            boolean isUseAbnormalNitzDataAndCountry = isUseAbnormalNitzDataAndCountry(countryIso, nitzSignal);
            log("isSuggestionValid = " + isSuggestionValid + ", isNitzValid = " + isNitzValid + ", cacheTimeZone = " + cacheTimeZone + ", isMatchOffset = " + isMatchOffset + ", isUseAbnormalNitzDataAndCountry = " + isUseAbnormalNitzDataAndCountry);
            if (isUseAbnormalNitzDataAndCountry) {
                String msg = "NITZ IGNORE and telephony suggestion is " + (isSuggestionValid ? "valid" : "inValid");
                trackOptimization(countryIso, nitzSignal.getNitzData().getLocalOffsetMillis(), getCacheTimeZone(countryIso), suggestion, msg);
            } else if (!isNitzValid || isSuggestionValid) {
                trackOptimization(countryIso, nitzSignal.getNitzData().getLocalOffsetMillis(), getCacheTimeZone(countryIso), suggestion, !isNitzValid ? "NITZ INVALID" : "telephony suggestion is valid");
            }
            if (isSuggestionValid && isMatchOffset && !TextUtils.isEmpty(cacheTimeZone)) {
                clearTimeZoneSp(countryIso);
            }
        }
        return suggestion;
    }

    private NitzData createNitzDataChangeDst(NitzSignal nitzSignal) {
        log("createNitzDataChangeDst");
        String originalString = nitzSignal.getNitzData().getOriginalString();
        String[] nitzSubs = NITZ_SPLIT_PATTERN.split(originalString);
        if (nitzSubs != null && nitzSubs.length == 8) {
            Integer dstAdjustmentHours = Integer.valueOf(Integer.parseInt(nitzSubs[7]));
            nitzSubs[7] = dstAdjustmentHours.intValue() == 0 ? "01" : "00";
            boolean sign = originalString.indexOf(45) == -1;
            String newNitz = String.join("/", nitzSubs[0], nitzSubs[1], nitzSubs[2]) + "," + String.join(":", nitzSubs[3], nitzSubs[4], nitzSubs[5]) + (sign ? "+" : SEPARATOR) + nitzSubs[6] + "," + nitzSubs[7];
            log("createNitzDataChangeDst, newNitz = " + newNitz);
            nitzSignal.getNitzData();
            return NitzData.parse(newNitz);
        }
        return nitzSignal.getNitzData();
    }

    private String getNewTimeZoneId(int nitzOffset) {
        if (nitzOffset > 0) {
            String prefix = "Etc/GMT" + SEPARATOR;
            String newTimezoneId = prefix + (Math.abs(nitzOffset) / MS_PER_HOUR);
            return newTimezoneId;
        }
        if (nitzOffset < 0) {
            String prefix2 = "Etc/GMT+";
            String newTimezoneId2 = prefix2 + (Math.abs(nitzOffset) / MS_PER_HOUR);
            return newTimezoneId2;
        }
        return "Etc/UTC";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateNitzDataAndCountry(String[] opNames, NitzData nitzData) {
        String countryIsoRecord = this.mCountryIsoNitzData.isEmpty() ? UNKNOWN_COUNTRY_ISO : (String) this.mCountryIsoNitzData.get(KEY_COUNTRY_ISO);
        NitzData nitzDataRecord = this.mCountryIsoNitzData.isEmpty() ? null : (NitzData) this.mCountryIsoNitzData.get(KEY_NITZ_DATA);
        boolean isOffsetChange = false;
        if (nitzDataRecord == null || nitzDataRecord.getLocalOffsetMillis() != nitzData.getLocalOffsetMillis()) {
            isOffsetChange = true;
        }
        String newCountryIso = UNKNOWN_COUNTRY_ISO;
        if (opNames != null && opNames.length >= 3) {
            String operator = opNames[2] == null ? "" : opNames[2];
            MccTable.MccMnc mccMnc = MccTable.MccMnc.fromOperatorNumeric(operator);
            if (mccMnc != null) {
                newCountryIso = MccTable.geoCountryCodeForMccMnc(mccMnc);
            }
        }
        log("updateNitzDataAndCountry, oldCountryIsoNitzData = " + this.mCountryIsoNitzData);
        this.mCountryIsoNitzData.put(KEY_COUNTRY_ISO, newCountryIso);
        this.mCountryIsoNitzData.put(KEY_NITZ_DATA, nitzData);
        log("updateNitzDataAndCountry, newCountryIsoNitzData = " + this.mCountryIsoNitzData);
        if (isInChina(newCountryIso)) {
            return;
        }
        if (!countryIsoRecord.equalsIgnoreCase(newCountryIso) || isOffsetChange) {
            this.mPhone.getServiceStateTracker().getNitzStateMachine().handleCountryAgain();
        }
    }

    private TelephonyTimeZoneSuggestion findTimeZoneFromNitz(int slotId, String timeZone, String reason) {
        return new TelephonyTimeZoneSuggestion.Builder(slotId).setZoneId(timeZone).setQuality(1).setMatchType(3).addDebugInfo(reason).build();
    }

    private boolean shouldUpdateTimeZoneByOffsetDifferent(TimeZoneSuggesterImpl timeZoneSuggesterImpl, TelephonyTimeZoneSuggestion suggestion, NitzSignal nitzSignal, String countryIso) {
        boolean result = (isUseAbnormalNitzDataAndCountry(countryIso, nitzSignal) || isMatchOffset(suggestion, nitzSignal) || !isValidNitz(countryIso, nitzSignal)) ? false : true;
        log("shouldUpdateTimeZoneByOffsetDifferent, result = " + result);
        return result;
    }

    private boolean isUseAbnormalNitzDataAndCountry(String countryIso, NitzSignal nitzSignal) {
        boolean reuslt = false;
        String countryIsoRecord = (String) this.mCountryIsoNitzData.get(KEY_COUNTRY_ISO);
        NitzData nitzDataRecord = (NitzData) this.mCountryIsoNitzData.get(KEY_NITZ_DATA);
        NitzData nitzData = nitzSignal.getNitzData();
        log("isUseAbnormalNitzDataAndCountry, countryIsoRecord = " + countryIsoRecord + ", countryIso = " + countryIso + ", nitzDataRecord = " + nitzDataRecord + ", nitzData = " + nitzData);
        if (TextUtils.isEmpty(countryIsoRecord) || !countryIsoRecord.equalsIgnoreCase(countryIso) || nitzDataRecord == null || !nitzDataRecord.equals(nitzData)) {
            reuslt = true;
        }
        String timeZoneCache = getCacheTimeZone(countryIso);
        if (!TextUtils.isEmpty(timeZoneCache)) {
            TimeZone cacheTimeZone = TimeZone.getTimeZone(timeZoneCache);
            System.currentTimeMillis();
            if (cacheTimeZone != null) {
                reuslt = false;
            }
        }
        log("isUseAbnormalNitzDataAndCountry, reuslt = " + reuslt);
        return reuslt;
    }

    private boolean isMatchOffset(TelephonyTimeZoneSuggestion suggestion, NitzSignal nitzSignal) {
        TimeZone timeZone;
        if (TextUtils.isEmpty(getCurretTimeZone()) || (timeZone = TimeZone.getTimeZone(getCurretTimeZone())) == null) {
            return false;
        }
        int currentOffset = timeZone.getOffset(System.currentTimeMillis());
        int suggestionOffset = 0;
        int nitzOffset = nitzSignal.getNitzData().getLocalOffsetMillis();
        boolean result = true;
        if (isValidTimeZoneSuggestion(suggestion) && TimeZone.getTimeZone(suggestion.getZoneId()) != null) {
            suggestionOffset = TimeZone.getTimeZone(suggestion.getZoneId()).getOffset(System.currentTimeMillis());
            if (nitzOffset != suggestionOffset) {
                result = false;
            }
        } else if (nitzOffset != currentOffset) {
            result = false;
        }
        log("isMatchOffset, currentOffset = " + currentOffset + ", newOffset = " + nitzOffset + ", suggestionOffset = " + suggestionOffset);
        return result;
    }

    private boolean isValidTimeZoneSuggestion(TelephonyTimeZoneSuggestion suggestion) {
        boolean result = (suggestion.getZoneId() == null || suggestion.getQuality() == 0 || suggestion.getQuality() == 3) ? false : true;
        log("isValidTimeZoneSuggestion, result = " + result);
        return result;
    }

    private boolean isValidNitz(String countryIso, NitzSignal nitzSignal) {
        List<CountryTimeZones.TimeZoneMapping> effectiveTimeZoneMappings;
        boolean result = false;
        if (nitzSignal == null || nitzSignal.getNitzData() == null) {
            log("isValidNitz false");
            return false;
        }
        int offset = nitzSignal.getNitzData().getLocalOffsetMillis();
        int minOffset = 0;
        int maxOffset = 0;
        CountryTimeZones countryTimeZones = TimeZoneFinder.getInstance().lookupCountryTimeZones(countryIso);
        if (countryTimeZones != null && (effectiveTimeZoneMappings = countryTimeZones.getEffectiveTimeZoneMappingsAt(System.currentTimeMillis())) != null) {
            for (CountryTimeZones.TimeZoneMapping timeMap : effectiveTimeZoneMappings) {
                int tmpOffset = timeMap.getTimeZone().getOffset(System.currentTimeMillis());
                minOffset = minOffset == 0 ? tmpOffset : Math.min(minOffset, tmpOffset);
                maxOffset = maxOffset == 0 ? tmpOffset : Math.max(maxOffset, tmpOffset);
            }
        }
        if (offset <= maxOffset + MS_PER_HOUR && offset >= minOffset - MS_PER_HOUR) {
            result = true;
        }
        log("isValidNitz, countryIso = " + countryIso + ", minOffset = " + minOffset + ", maxOffset = " + maxOffset + ", offset = " + offset + ", result = " + result);
        return result;
    }

    private boolean isAutoTimeSwitchOn() {
        boolean result = Settings.Global.getInt(this.mContext.getContentResolver(), "auto_time_zone", 1) == 1;
        log("isAutoTimeSwitchOn, result = " + result);
        return result;
    }

    private ArrayList<String> getMatchTimeZoneList(String countryIso, NitzData nitzData) {
        List<CountryTimeZones.TimeZoneMapping> effectiveTimeZoneMappings;
        ArrayList<String> timeZoneList = new ArrayList<>();
        if (nitzData == null) {
            log("isValidNitz false");
            return timeZoneList;
        }
        CountryTimeZones countryTimeZones = TimeZoneFinder.getInstance().lookupCountryTimeZones(countryIso);
        if (countryTimeZones != null && (effectiveTimeZoneMappings = countryTimeZones.getEffectiveTimeZoneMappingsAt(System.currentTimeMillis())) != null) {
            for (CountryTimeZones.TimeZoneMapping timeZoneMapping : effectiveTimeZoneMappings) {
                TimeZone match = timeZoneMapping.getTimeZone();
                Integer dstAdjustmentMillis = nitzData.getDstAdjustmentMillis();
                if (match != null) {
                    if (offsetMatchesAtTime(System.currentTimeMillis(), match, nitzData.getLocalOffsetMillis(), dstAdjustmentMillis != null ? Boolean.valueOf(dstAdjustmentMillis.intValue() != 0) : null) && !timeZoneList.contains(match.getID())) {
                        timeZoneList.add(match.getID());
                    }
                }
            }
        }
        log("timeZoneList is " + timeZoneList);
        return timeZoneList;
    }

    private static boolean offsetMatchesAtTime(long whenMillis, TimeZone timeZone, int totalOffsetMillis, Boolean isDst) {
        int[] offsets = new int[2];
        timeZone.getOffset(whenMillis, false, offsets);
        if (totalOffsetMillis != offsets[0] + offsets[1]) {
            return false;
        }
        if (isDst != null) {
            if (isDst.booleanValue() != (offsets[1] != 0)) {
                return false;
            }
        }
        return true;
    }

    private void setCacheTimeZone(String countryIso, String timeZone) {
        SharedPreferences.Editor spEditor = this.mSharedPreferences.edit();
        spEditor.putString("last_time_zone-" + countryIso, timeZone).commit();
    }

    private String getCacheTimeZone(String countryIso) {
        String timezone = "";
        String key = "last_time_zone-" + countryIso;
        String value = this.mSharedPreferences.getString(key, "");
        String[] keys = key.split(SEPARATOR);
        if (keys.length == 2 && keys[1].equalsIgnoreCase(countryIso) && !TextUtils.isEmpty(value) && TimeZone.getTimeZone(value) != null) {
            timezone = value;
        }
        log("getCacheTimeZone, countryIso = " + countryIso + ", timezone = " + timezone);
        return timezone;
    }

    private void clearTimeZoneSp(String countryIso) {
        String timeZoneKey = "last_time_zone-" + countryIso;
        SharedPreferences.Editor spEditor = this.mSharedPreferences.edit();
        Map<String, ?> info = this.mSharedPreferences.getAll();
        for (String key : info.keySet()) {
            if (key.indexOf(timeZoneKey) != -1) {
                spEditor.remove(key);
            }
        }
        spEditor.commit();
    }

    private void clearTimeZoneSp() {
        clearTimeZoneSp("");
    }

    private void setTelephonyVaildCountrySp(String countryIso) {
        int subId = this.mPhone.getSubId();
        log("setTelephonyVaildCountrySp, countryIso = " + countryIso);
        if (SubscriptionManager.isValidSubscriptionId(subId) && !TextUtils.isEmpty(countryIso)) {
            String telphonyVaildCountrySp = getTelephonyVaildCountrySp(subId);
            if (TextUtils.isEmpty(telphonyVaildCountrySp) || !telphonyVaildCountrySp.contains(countryIso)) {
                SharedPreferences.Editor spEditor = this.mSharedPreferences.edit();
                spEditor.putString("nitz_valid_country-" + this.mPhone.getSubId(), (TextUtils.isEmpty(telphonyVaildCountrySp) ? ModemUtils.PROP_MIUI_BUILD_REGION_CN : telphonyVaildCountrySp) + "," + countryIso.toLowerCase()).commit();
            }
        }
    }

    private String getTelephonyVaildCountrySp(int subId) {
        if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            return "";
        }
        String key = "nitz_valid_country-" + subId;
        String value = this.mSharedPreferences.getString(key, "");
        return value;
    }

    private static String getCurretTimeZone() {
        return SystemProperties.get("persist.sys.timezone", "");
    }

    private boolean isInChina(String countryIso) {
        return ModemUtils.PROP_MIUI_BUILD_REGION_CN.equalsIgnoreCase(countryIso);
    }

    public void stopNitzTimeZonePreferred() {
        clearTimeZoneSp("");
    }

    public boolean isAllowGeolocationTimeZoneDetection(List<String> zoneIds, long effectiveFromElapsedMillis) {
        log("isAllowGeolocationTimeZoneDetection zoneIds = " + zoneIds + ", effectiveFromElapsedMillis = " + effectiveFromElapsedMillis);
        boolean result = false;
        if (zoneIds != null && !zoneIds.isEmpty() && !zoneIds.contains(getCurretTimeZone()) && !TextUtils.isEmpty(this.mCountryIso) && !isInChina(this.mCountryIso)) {
            String zoneId = zoneIds.get(0);
            if (!TextUtils.isEmpty(zoneId)) {
                long currentTime = SystemClock.elapsedRealtime();
                if (currentTime - effectiveFromElapsedMillis < GEO_LOCATION_VALID_TIME && currentTime > effectiveFromElapsedMillis) {
                    String timeZoneCountryIso = getCountryIsoByTimeZone(zoneId);
                    if (this.mSubscriptionManager != null && !TextUtils.isEmpty(timeZoneCountryIso) && !isInChina(timeZoneCountryIso)) {
                        int[] subIds = this.mSubscriptionManager.getActiveSubscriptionIdList(false);
                        boolean isUpdateTimeZoneByGeo = true;
                        if (subIds != null && subIds.length > 0) {
                            for (int subId : subIds) {
                                if (getTelephonyVaildCountrySp(subId).contains(timeZoneCountryIso.toLowerCase())) {
                                    isUpdateTimeZoneByGeo = false;
                                    break;
                                }
                            }
                        }
                        result = isUpdateTimeZoneByGeo;
                    }
                }
            }
        }
        log("isAllowGeolocationTimeZoneDetection result = " + result);
        return result;
    }

    public String findBestTZInMultiZones(String defaultZoneId, List<String> zoneIds, long effectiveFromElapsedMillis) {
        log("findBestTZInMultiZones defaultZoneId = " + defaultZoneId + ", zoneIds = " + zoneIds + ", effectiveFromElapsedMillis = " + effectiveFromElapsedMillis);
        String zoneId = defaultZoneId;
        if (!TextUtils.isEmpty(defaultZoneId)) {
            long currentTime = SystemClock.elapsedRealtime();
            if (currentTime - effectiveFromElapsedMillis < 86400000 && currentTime > effectiveFromElapsedMillis && this.mPhone != null && this.mPhone.getServiceStateTracker() != null && this.mPhone.getServiceStateTracker().getNitzStateMachine() != null) {
                NitzData nitzData = this.mPhone.getServiceStateTracker().getNitzStateMachine().getLatestNitzData();
                String countryIso = getCountryIsoByTimeZone(defaultZoneId);
                ArrayList<String> matchTimeZoneList = getMatchTimeZoneList(countryIso, nitzData);
                if (matchTimeZoneList != null && matchTimeZoneList.size() > 1 && matchTimeZoneList.contains(defaultZoneId)) {
                    String geoZoneId = zoneIds.size() > 0 ? zoneIds.get(0) : "";
                    if (!TextUtils.isEmpty(geoZoneId) && matchTimeZoneList.contains(geoZoneId) && !geoZoneId.equals(defaultZoneId)) {
                        zoneId = geoZoneId;
                    }
                }
            }
        }
        log("findBestTZInMultiZones zoneId = " + zoneId + ", defaultZoneId = " + defaultZoneId);
        return zoneId;
    }

    private static String getCountryIsoByTimeZone(String timeZone) {
        List<CountryTimeZones.TimeZoneMapping> effectiveTimeZoneMappings;
        String[] isoCountries = Locale.getISOCountries();
        System.currentTimeMillis();
        for (String iso : isoCountries) {
            CountryTimeZones countryTimeZones = TimeZoneFinder.getInstance().lookupCountryTimeZones(iso);
            if (countryTimeZones != null && (effectiveTimeZoneMappings = countryTimeZones.getEffectiveTimeZoneMappingsAt(System.currentTimeMillis())) != null) {
                for (CountryTimeZones.TimeZoneMapping timeMap : effectiveTimeZoneMappings) {
                    if (timeZone.equals(timeMap.getTimeZoneId())) {
                        return iso;
                    }
                }
            }
        }
        return "";
    }

    private void trackOptimization(String countryIso, int nitzOffset, String cacheZoneId, TelephonyTimeZoneSuggestion suggestion, String reason) {
        TimeZone timezone;
        String operator = this.mPhone.getServiceState().getOperatorNumeric();
        String suggestionZoneId = "";
        int suggestionOffset = 0;
        if (suggestion != null) {
            suggestionZoneId = suggestion.getZoneId();
            if (suggestion.getZoneId() != null && (timezone = TimeZone.getTimeZone(suggestion.getZoneId())) != null) {
                suggestionOffset = timezone.getOffset(System.currentTimeMillis());
            }
        }
        Intent intent = new Intent(ACTION_NITZ_TIMEZONE_PREFERRED_OPTIMIZATION);
        intent.putExtra(EXTRA_COUNTRY_ISO, countryIso);
        intent.putExtra(EXTRA_OPERATOR, operator);
        intent.putExtra(EXTRA_NITZ_OFFSET, String.valueOf(nitzOffset));
        intent.putExtra(EXTRA_CACHE_ZONEID, cacheZoneId);
        intent.putExtra(EXTRA_SUGGESTION_ZONEID, suggestionZoneId);
        intent.putExtra(EXTRA_SUGGESTION_OFFSET, String.valueOf(suggestionOffset));
        intent.putExtra(EXTRA_CURRENT_ZONEID, getCurretTimeZone());
        intent.putExtra(EXTRA_REASON, reason);
        this.mPhone.getContext().sendBroadcast(intent);
        log("trackOptimization: countryIso = " + countryIso + ", operator = " + operator + ", NitzOffset = " + nitzOffset + ", cacheZoneId = " + cacheZoneId + ", suggestionZoneId = " + suggestionZoneId + ", suggestionOffset = " + suggestionOffset + ", getCurretTimeZone = " + getCurretTimeZone() + ", reason = " + reason);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String msg) {
        Log.d("MiuiTimeZoneOptimization[" + this.mPhone.getPhoneId() + "]", msg);
    }
}
