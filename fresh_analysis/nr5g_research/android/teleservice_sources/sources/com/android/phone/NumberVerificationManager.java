package com.android.phone;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.telephony.PhoneNumberRange;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.INumberVerificationCallback;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class NumberVerificationManager {
    private static String sAuthorizedPackageOverride;
    private static NumberVerificationManager sInstance;
    private INumberVerificationCallback mCallback;
    private PhoneNumberRange mCurrentRange;
    private Handler mHandler;
    private final PhoneListSupplier mPhoneListSupplier;

    interface PhoneListSupplier {
        Phone[] getPhones();
    }

    NumberVerificationManager(PhoneListSupplier phoneListSupplier) {
        this.mPhoneListSupplier = phoneListSupplier;
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    private NumberVerificationManager() {
        this(new PhoneListSupplier() { // from class: com.android.phone.NumberVerificationManager$$ExternalSyntheticLambda1
            @Override // com.android.phone.NumberVerificationManager.PhoneListSupplier
            public final Phone[] getPhones() {
                return PhoneFactory.getPhones();
            }
        });
    }

    public synchronized boolean checkIncomingCall(String str, String str2) {
        if (this.mCurrentRange != null && this.mCallback != null) {
            Log.i("NumberVerification", "checkIncomingCall: number=" + Rlog.piiHandle(str) + ", country=" + str2);
            String numberToE164 = PhoneNumberUtils.formatNumberToE164(str, str2);
            if (TextUtils.isEmpty(numberToE164)) {
                numberToE164 = str;
            }
            if (!this.mCurrentRange.matches(numberToE164)) {
                return false;
            }
            this.mCurrentRange = null;
            INumberVerificationCallback iNumberVerificationCallback = this.mCallback;
            this.mCallback = null;
            try {
                iNumberVerificationCallback.onCallReceived(str);
                return true;
            } catch (RemoteException unused) {
                Log.w(NumberVerificationManager.class.getSimpleName(), "Remote exception calling verification complete callback");
                return true;
            }
        }
        return false;
    }

    synchronized void requestVerification(PhoneNumberRange phoneNumberRange, final INumberVerificationCallback iNumberVerificationCallback, long j) {
        if (checkNumberVerificationFeasibility(iNumberVerificationCallback)) {
            this.mCallback = iNumberVerificationCallback;
            this.mCurrentRange = phoneNumberRange;
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.NumberVerificationManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$requestVerification$0(iNumberVerificationCallback);
                }
            }, j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$requestVerification$0(INumberVerificationCallback iNumberVerificationCallback) {
        synchronized (this) {
            if (this.mCallback != null && this.mCurrentRange != null) {
                this.mCurrentRange = null;
                this.mCallback = null;
                try {
                    iNumberVerificationCallback.onVerificationFailed(1);
                } catch (RemoteException unused) {
                    Log.w(NumberVerificationManager.class.getSimpleName(), "Remote exception calling verification error callback");
                }
            }
        }
    }

    private boolean checkNumberVerificationFeasibility(INumberVerificationCallback iNumberVerificationCallback) {
        try {
            if (this.mCurrentRange != null || this.mCallback != null) {
                iNumberVerificationCallback.onVerificationFailed(4);
                return false;
            }
            boolean z = false;
            boolean z2 = false;
            for (Phone phone : this.mPhoneListSupplier.getPhones()) {
                if (!phone.isInEmergencyCall()) {
                    if (!phone.isInEcm()) {
                        if (phone.getServiceState().getState() == 0) {
                            z = true;
                        }
                        Call.State state = phone.getRingingCall().getState();
                        Call.State state2 = Call.State.IDLE;
                        if (state == state2 && (phone.getForegroundCall().getState() == state2 || phone.getBackgroundCall().getState() == state2)) {
                            z2 = true;
                        }
                    } else {
                        iNumberVerificationCallback.onVerificationFailed(5);
                        return false;
                    }
                } else {
                    iNumberVerificationCallback.onVerificationFailed(6);
                    return false;
                }
            }
            if (!z) {
                iNumberVerificationCallback.onVerificationFailed(2);
                return false;
            }
            if (z2) {
                return true;
            }
            iNumberVerificationCallback.onVerificationFailed(3);
            return false;
        } catch (RemoteException unused) {
            Log.w(NumberVerificationManager.class.getSimpleName(), "Remote exception calling verification error callback");
            return false;
        }
    }

    public static NumberVerificationManager getInstance() {
        if (sInstance == null) {
            sInstance = new NumberVerificationManager();
        }
        return sInstance;
    }

    static String getAuthorizedPackage(Context context) {
        return !TextUtils.isEmpty(sAuthorizedPackageOverride) ? sAuthorizedPackageOverride : context.getResources().getString(R.string.platform_number_verification_package);
    }

    static void overrideAuthorizedPackage(String str) {
        getInstance().mCallback = null;
        getInstance().mCurrentRange = null;
        getInstance().mHandler.removeMessages(0);
        sAuthorizedPackageOverride = str;
    }
}
