package com.android.internal.telephony.nitz;

import android.app.timezonedetector.TelephonyTimeZoneSuggestion;
import android.timezone.MobileCountries;
import com.android.internal.telephony.NitzSignal;

/* JADX INFO: loaded from: classes.dex */
public interface TimeZoneSuggester {
    TelephonyTimeZoneSuggestion getTimeZoneSuggestion(int i, MobileCountries mobileCountries, NitzSignal nitzSignal);

    TelephonyTimeZoneSuggestion getTimeZoneSuggestion(int i, String str, NitzSignal nitzSignal);
}
