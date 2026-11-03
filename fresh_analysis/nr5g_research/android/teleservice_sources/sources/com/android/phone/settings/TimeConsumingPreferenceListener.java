package com.android.phone.settings;

import androidx.preference.Preference;
import com.android.internal.telephony.CommandException;

/* JADX INFO: loaded from: classes.dex */
public interface TimeConsumingPreferenceListener {
    void onError(Preference preference, int i);

    void onException(Preference preference, CommandException commandException);

    void onFinished(Preference preference, boolean z);

    void onStarted(Preference preference, boolean z);
}
