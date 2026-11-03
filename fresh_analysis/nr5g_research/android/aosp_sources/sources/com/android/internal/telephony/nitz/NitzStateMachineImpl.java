package com.android.internal.telephony.nitz;

import android.app.timedetector.TelephonyTimeSuggestion;
import android.app.timezonedetector.TelephonyTimeZoneSuggestion;
import android.os.TimestampedValue;
import android.timezone.MobileCountries;
import com.android.internal.telephony.CountryDetectionListener;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperTimeZoneDetector;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.MiuiNetworkControllerStub;
import com.android.internal.telephony.NitzData;
import com.android.internal.telephony.NitzSignal;
import com.android.internal.telephony.NitzStateMachine;
import com.android.internal.telephony.Phone;
import com.android.telephony.Rlog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public final class NitzStateMachineImpl implements NitzStateMachine {
    private final Set mCountryDetectionListeners = new HashSet();
    private String mCountryIsoCode;
    private final NitzStateMachine.DeviceState mDeviceState;
    private TimestampedValue mLastNitzSignalCleared;
    private NitzSignal mLatestNitzSignal;
    private MobileCountries mMobileCountries;
    private final NitzSignalInputFilterPredicate mNitzSignalInputFilter;
    private final int mSlotIndex;
    private final TimeServiceHelper mTimeServiceHelper;
    private final TimeZoneSuggester mTimeZoneSuggester;

    @FunctionalInterface
    public interface NitzSignalInputFilterPredicate {
        boolean mustProcessNitzSignal(NitzSignal nitzSignal, NitzSignal nitzSignal2);
    }

    public static NitzStateMachineImpl createInstance(Phone phone) {
        Objects.requireNonNull(phone);
        int phoneId = phone.getPhoneId();
        NitzStateMachine.DeviceStateImpl deviceStateImpl = new NitzStateMachine.DeviceStateImpl(phone);
        TimeZoneSuggesterImpl timeZoneSuggesterImpl = new TimeZoneSuggesterImpl(deviceStateImpl, new TimeZoneLookupHelper());
        TimeServiceHelperImpl timeServiceHelperImpl = new TimeServiceHelperImpl(phone);
        NitzSignalInputFilterPredicate nitzSignalInputFilterPredicateCreate = NitzSignalInputFilterPredicateFactory.create(phone.getContext(), deviceStateImpl);
        HyperTelephonyFactory.getInstance().initModule(IHyperTimeZoneDetector.DEFAULT, phone, timeZoneSuggesterImpl, timeServiceHelperImpl);
        return new NitzStateMachineImpl(phoneId, deviceStateImpl, nitzSignalInputFilterPredicateCreate, timeZoneSuggesterImpl, timeServiceHelperImpl);
    }

    public NitzStateMachineImpl(int i, NitzStateMachine.DeviceState deviceState, NitzSignalInputFilterPredicate nitzSignalInputFilterPredicate, TimeZoneSuggester timeZoneSuggester, TimeServiceHelper timeServiceHelper) {
        this.mSlotIndex = i;
        Objects.requireNonNull(deviceState);
        this.mDeviceState = deviceState;
        Objects.requireNonNull(timeZoneSuggester);
        this.mTimeZoneSuggester = timeZoneSuggester;
        Objects.requireNonNull(timeServiceHelper);
        this.mTimeServiceHelper = timeServiceHelper;
        Objects.requireNonNull(nitzSignalInputFilterPredicate);
        this.mNitzSignalInputFilter = nitzSignalInputFilterPredicate;
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void registerCountryDetection(CountryDetectionListener countryDetectionListener) {
        if (countryDetectionListener == null) {
            return;
        }
        this.mCountryDetectionListeners.add(countryDetectionListener);
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void unregisterCountryDetection(CountryDetectionListener countryDetectionListener) {
        if (countryDetectionListener == null || !this.mCountryDetectionListeners.contains(countryDetectionListener)) {
            return;
        }
        this.mCountryDetectionListeners.remove(countryDetectionListener);
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void handleNetworkAvailable() {
        restoreNetworkStateAndRerunDetection("handleNetworkAvailable");
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void handleNetworkUnavailable() {
        if (clearNetworkState(false)) {
            runDetection("handleNetworkUnavailable");
        }
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void handleCountryDetected(String str) {
        Rlog.d("NitzStateMachineImpl", "handleCountryDetected: countryIsoCode=" + str + ", mLatestNitzSignal=" + this.mLatestNitzSignal);
        String str2 = this.mCountryIsoCode;
        Objects.requireNonNull(str);
        this.mCountryIsoCode = str;
        if (Objects.equals(str2, str)) {
            return;
        }
        doTimeZoneDetection(str, this.mLatestNitzSignal, "handleCountryDetected(\"" + str + "\")");
    }

    public void handleCountryAgain() {
        doTimeZoneDetection(this.mCountryIsoCode, this.mLatestNitzSignal, "handleCountryAgain(\"" + this.mCountryIsoCode + "\")" + this.mLatestNitzSignal);
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void handleMobileCountriesDetected(MobileCountries mobileCountries) {
        synchronized (this) {
            try {
                Rlog.d("NitzStateMachineImpl", "handleMobileCountriesDetected: mobileCountries=" + mobileCountries + ", mLatestNitzSignal=" + this.mLatestNitzSignal);
                MobileCountries mobileCountries2 = this.mMobileCountries;
                Objects.requireNonNull(mobileCountries);
                this.mMobileCountries = mobileCountries;
                if (!Objects.equals(mobileCountries2, mobileCountries)) {
                    if (this.mMobileCountries.getCountryIsoCodes().size() == 1) {
                        this.mCountryIsoCode = this.mMobileCountries.getDefaultCountryIsoCode();
                    }
                    doTimeZoneDetection(mobileCountries, this.mLatestNitzSignal, "handleMobileCountriesDetected(\"" + mobileCountries + "\")");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void handleCountryUnavailable() {
        Rlog.d("NitzStateMachineImpl", "handleCountryUnavailable: mLatestNitzSignal=" + this.mLatestNitzSignal);
        this.mCountryIsoCode = null;
        this.mMobileCountries = null;
        doTimeZoneDetection((String) null, this.mLatestNitzSignal, "handleCountryUnavailable");
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void handleNitzReceived(NitzSignal nitzSignal) {
        Objects.requireNonNull(nitzSignal);
        NitzSignal nitzSignal2 = this.mLatestNitzSignal;
        if (!this.mNitzSignalInputFilter.mustProcessNitzSignal(nitzSignal2, nitzSignal)) {
            Rlog.d("NitzStateMachineImpl", "handleNitzReceived: previousNitzSignal=" + nitzSignal2 + ", nitzSignal=" + nitzSignal + ": NITZ filtered");
            return;
        }
        this.mLatestNitzSignal = nitzSignal;
        this.mLastNitzSignalCleared = null;
        runDetection("handleNitzReceived(" + nitzSignal + ")");
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void handleAirplaneModeChanged(boolean z) {
        boolean z2 = (this.mCountryIsoCode == null && this.mMobileCountries == null) ? false : true;
        this.mCountryIsoCode = null;
        this.mMobileCountries = null;
        boolean zClearNetworkState = clearNetworkState(true);
        if (z2 || zClearNetworkState) {
            runDetection("handleAirplaneModeChanged(" + z + ")");
        }
    }

    private void restoreNetworkStateAndRerunDetection(String str) {
        if (this.mLastNitzSignalCleared == null) {
            Rlog.d("NitzStateMachineImpl", str + ": mLastNitzSignalCleared is null.");
            return;
        }
        if (this.mDeviceState.elapsedRealtimeMillis() - this.mLastNitzSignalCleared.getReferenceTimeMillis() < this.mDeviceState.getNitzNetworkDisconnectRetentionMillis()) {
            String str2 = str + ", mLatestNitzSignal restored from mLastNitzSignalCleared=" + this.mLastNitzSignalCleared.getValue();
            this.mLatestNitzSignal = (NitzSignal) this.mLastNitzSignalCleared.getValue();
            this.mLastNitzSignalCleared = null;
            runDetection(str2);
            return;
        }
        Rlog.d("NitzStateMachineImpl", str + ": mLastNitzSignalCleared is too old.");
    }

    private boolean clearNetworkState(boolean z) {
        if (z) {
            this.mLastNitzSignalCleared = null;
        } else {
            this.mLastNitzSignalCleared = new TimestampedValue(this.mDeviceState.elapsedRealtimeMillis(), this.mLatestNitzSignal);
        }
        boolean z2 = this.mLatestNitzSignal != null;
        this.mLatestNitzSignal = null;
        return z2;
    }

    private void runDetection(String str) {
        NitzSignal nitzSignal = this.mLatestNitzSignal;
        MobileCountries mobileCountries = this.mMobileCountries;
        Rlog.d("NitzStateMachineImpl", "runDetection: reason=" + str + ", mobileCountries=" + mobileCountries + ", nitzSignal=" + nitzSignal);
        doTimeZoneDetection(mobileCountries, nitzSignal, str);
        doTimeDetection(nitzSignal, str);
    }

    private void doTimeZoneDetection(String str, NitzSignal nitzSignal, String str2) {
        try {
            Objects.requireNonNull(str2);
            TelephonyTimeZoneSuggestion timeZoneSuggestion = this.mTimeZoneSuggester.getTimeZoneSuggestion(this.mSlotIndex, str, nitzSignal);
            timeZoneSuggestion.addDebugInfo("Detection reason=".concat(str2));
            MiuiNetworkControllerStub.get(this.mSlotIndex).storeAndCheckTimeZone(this.mSlotIndex, timeZoneSuggestion, nitzSignal);
            Rlog.d("NitzStateMachineImpl", "doTimeZoneDetection: countryIsoCode=" + str + ", nitzSignal=" + nitzSignal + ", suggestion=" + timeZoneSuggestion + ", reason=" + str2);
            this.mTimeServiceHelper.maybeSuggestDeviceTimeZone(timeZoneSuggestion);
        } catch (RuntimeException e) {
            Rlog.e("NitzStateMachineImpl", "doTimeZoneDetection: Exception thrown mSlotIndex=" + this.mSlotIndex + ", countryIsoCode=" + str + ", nitzSignal=" + nitzSignal + ", reason=" + str2 + ", ex=" + e, e);
        }
    }

    private void doTimeZoneDetection(MobileCountries mobileCountries, NitzSignal nitzSignal, String str) {
        try {
            Objects.requireNonNull(str);
            final TelephonyTimeZoneSuggestion timeZoneSuggestion = this.mTimeZoneSuggester.getTimeZoneSuggestion(this.mSlotIndex, mobileCountries, nitzSignal);
            timeZoneSuggestion.addDebugInfo("Detection reason=".concat(str));
            Rlog.d("NitzStateMachineImpl", "doTimeZoneDetection: mobileCountries=" + mobileCountries + ", nitzSignal=" + nitzSignal + ", suggestion=" + timeZoneSuggestion + ", reason=" + str);
            this.mTimeServiceHelper.maybeSuggestDeviceTimeZone(timeZoneSuggestion);
            if (timeZoneSuggestion.getCountryIsoCode() != null) {
                this.mCountryIsoCode = timeZoneSuggestion.getCountryIsoCode();
                this.mCountryDetectionListeners.forEach(new Consumer() { // from class: com.android.internal.telephony.nitz.NitzStateMachineImpl$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((CountryDetectionListener) obj).onCountryDetected(timeZoneSuggestion.getCountryIsoCode());
                    }
                });
            }
        } catch (RuntimeException e) {
            Rlog.e("NitzStateMachineImpl", "doTimeZoneDetection: Exception thrown mSlotIndex=" + this.mSlotIndex + ", mobileCountries=" + mobileCountries + ", nitzSignal=" + nitzSignal + ", reason=" + str + ", ex=" + e, e);
        }
    }

    private void doTimeDetection(NitzSignal nitzSignal, String str) {
        if (!MiuiNetworkControllerStub.get(this.mSlotIndex).isValidNitz(this.mSlotIndex, nitzSignal)) {
            Rlog.d("NitzStateMachineImpl", "doTimeDetection: invalid nitz!!!");
            return;
        }
        try {
            Objects.requireNonNull(str);
            TelephonyTimeSuggestion.Builder builder = new TelephonyTimeSuggestion.Builder(this.mSlotIndex);
            if (nitzSignal == null) {
                builder.addDebugInfo("Clearing time suggestion reason=".concat(str));
            } else {
                builder.setUnixEpochTime(nitzSignal.createTimeSignal());
                builder.addDebugInfo("Sending new time suggestion nitzSignal=" + nitzSignal + ", reason=" + str);
            }
            this.mTimeServiceHelper.suggestDeviceTime(builder.build());
        } catch (RuntimeException e) {
            Rlog.e("NitzStateMachineImpl", "doTimeDetection: Exception thrown mSlotIndex=" + this.mSlotIndex + ", nitzSignal=" + nitzSignal + ", reason=" + str + ", ex=" + e, e);
        }
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void dumpState(PrintWriter printWriter) {
        printWriter.println(" NitzStateMachineImpl.mLatestNitzSignal=" + this.mLatestNitzSignal);
        printWriter.println(" NitzStateMachineImpl.mCountryIsoCode=" + this.mCountryIsoCode);
        printWriter.println(" NitzStateMachineImpl.mMobileCountries=" + this.mMobileCountries);
        this.mTimeServiceHelper.dumpState(printWriter);
        printWriter.flush();
    }

    @Override // com.android.internal.telephony.NitzStateMachine
    public void dumpLogs(FileDescriptor fileDescriptor, IndentingPrintWriter indentingPrintWriter, String[] strArr) {
        this.mTimeServiceHelper.dumpLogs(indentingPrintWriter);
    }

    public NitzData getLatestNitzData() {
        NitzSignal nitzSignal = this.mLatestNitzSignal;
        if (nitzSignal != null) {
            return nitzSignal.getNitzData();
        }
        return null;
    }

    public NitzData getLastNitzDataCleared() {
        TimestampedValue timestampedValue = this.mLastNitzSignalCleared;
        if (timestampedValue != null) {
            return ((NitzSignal) timestampedValue.getValue()).getNitzData();
        }
        return null;
    }
}
