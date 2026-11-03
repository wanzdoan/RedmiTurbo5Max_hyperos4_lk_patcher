package com.hyper.internal.telephony;

import android.app.timezonedetector.TelephonyTimeZoneSuggestion;
import android.content.Intent;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.IHyperTimeZoneDetector;
import com.android.internal.telephony.NitzSignal;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.nitz.TimeServiceHelper;
import com.android.internal.telephony.nitz.TimeZoneSuggester;
import com.xiaomi.internal.telephony.TelephonyCommonProxyFactory;

/* JADX INFO: loaded from: classes.dex */
public class HyperTimeZoneDetectorImpl implements IHyperTimeZoneDetector {
    private static final String ACTION_TIMEZONE_DETECTION_WITH_NULL_NITZ_STATISTIC = "miui.intent.action.TIMEZONE_DETECTION_WITH_NULL_NITZ_STATISTIC";
    private static final String EXTRA_COUNTRY_ISO = "countryIso";
    private static final String EXTRA_SUGGESTION_ZONEID = "suggestion_zoneid";
    private static final String LOG_TAG = "HyperTimeZoneDetectorImpl";
    private static final String REASON_HANDLE_COUNTRY_DETECTED_WITH_NULL_NITZ = "handleCountryDetectedWithNullNitz";
    private static int numPhones = TelephonyCommonProxyFactory.PHONE_COUNT;
    private final HyperCountryCodeResolver.LocationCountryCodeListener mListener;
    private final Object mLock = new Object();
    private String mNetworkCountryCode = null;
    private final Phone mPhone;
    private final int mSlotIndex;
    private final TimeServiceHelper mTimeServiceHelper;
    private final TimeZoneSuggester mTimeZoneSuggester;

    public HyperTimeZoneDetectorImpl(Phone phone, TimeZoneSuggester timeZoneSuggester, TimeServiceHelper timeServiceHelper) {
        if (phone == null) {
            throw new IllegalArgumentException("Phone cannot be null");
        }
        this.mPhone = phone;
        this.mSlotIndex = phone.getPhoneId();
        this.mTimeZoneSuggester = timeZoneSuggester;
        this.mTimeServiceHelper = timeServiceHelper;
        this.mListener = new HyperCountryCodeResolver.LocationCountryCodeListener() { // from class: com.hyper.internal.telephony.HyperTimeZoneDetectorImpl.1
            @Override // com.hyper.internal.telephony.HyperCountryCodeResolver.LocationCountryCodeListener
            public void onLocationCountryCodeChanged(String countryCode) {
                HyperTimeZoneDetectorImpl.this.logd("onLocationCountryCodeChanged: " + countryCode);
                synchronized (HyperTimeZoneDetectorImpl.this.mLock) {
                    if (TextUtils.isEmpty(HyperTimeZoneDetectorImpl.this.mNetworkCountryCode)) {
                        HyperTimeZoneDetectorImpl.this.doTimeZoneDetectionWithNullNitz(countryCode);
                    }
                }
            }
        };
    }

    public void onNetworkCountryCodeChanged(String currentCountryCode) {
        logd("onNetworkCountryCodeChanged: " + currentCountryCode);
        synchronized (this.mLock) {
            this.mNetworkCountryCode = currentCountryCode;
            if (this.mPhone != null) {
                HyperCountryCodeResolver resolver = HyperCountryCodeResolver.getInstance(this.mPhone.getContext());
                boolean zIsEmpty = TextUtils.isEmpty(this.mNetworkCountryCode);
                HyperCountryCodeResolver.LocationCountryCodeListener locationCountryCodeListener = this.mListener;
                if (zIsEmpty) {
                    resolver.addLocationCountryCodeListener(locationCountryCodeListener);
                } else {
                    resolver.removeLocationCountryCodeListener(locationCountryCodeListener);
                }
                resolver.onNetworkCountryCodeChanged();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doTimeZoneDetectionWithNullNitz(String countryIsoCode) {
        try {
            TelephonyTimeZoneSuggestion suggestion = this.mTimeZoneSuggester.getTimeZoneSuggestion(this.mSlotIndex, countryIsoCode, (NitzSignal) null);
            suggestion.addDebugInfo("Detection reason=handleCountryDetectedWithNullNitz");
            logd("doTimeZoneDetectionWithNullNitz: countryIsoCode=" + countryIsoCode + ", suggestion=" + suggestion);
            this.mTimeServiceHelper.maybeSuggestDeviceTimeZone(suggestion);
            if (!TextUtils.isEmpty(countryIsoCode)) {
                trackDoTimeZoneDetectionWithNullNitz(countryIsoCode, suggestion);
            }
        } catch (RuntimeException ex) {
            loge("doTimeZoneDetectionWithNullNitz: Exception thrown mSlotIndex=" + this.mSlotIndex + ", countryIsoCode=" + countryIsoCode + ", ex=" + ex);
        }
    }

    private void trackDoTimeZoneDetectionWithNullNitz(String countryIso, TelephonyTimeZoneSuggestion suggestion) {
        String suggestionZoneId = "";
        if (suggestion != null) {
            suggestionZoneId = suggestion.getZoneId();
        }
        Intent intent = new Intent(ACTION_TIMEZONE_DETECTION_WITH_NULL_NITZ_STATISTIC);
        intent.putExtra(EXTRA_COUNTRY_ISO, countryIso);
        intent.putExtra(EXTRA_SUGGESTION_ZONEID, suggestionZoneId);
        if (this.mPhone != null && this.mPhone.getContext() != null) {
            this.mPhone.getContext().sendBroadcast(intent);
            logd("recordDoTimeZoneDetectionWithNullNitz: countryIso = " + countryIso + ", suggestionZoneId = " + suggestionZoneId);
        }
    }

    private static boolean isValidPhoneId(int phoneId) {
        return phoneId >= 0 && phoneId < numPhones;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String log) {
        Rlog.d("HyperTimeZoneDetectorImpl[" + this.mSlotIndex + "]", log);
    }

    private void logw(String log) {
        Rlog.w("HyperTimeZoneDetectorImpl[" + this.mSlotIndex + "]", log);
    }

    private void loge(String log) {
        Rlog.e("HyperTimeZoneDetectorImpl[" + this.mSlotIndex + "]", log);
    }
}
