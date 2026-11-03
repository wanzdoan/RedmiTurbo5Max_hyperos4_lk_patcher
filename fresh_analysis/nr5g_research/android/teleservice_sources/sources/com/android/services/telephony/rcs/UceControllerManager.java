package com.android.services.telephony.rcs;

import android.content.Context;
import android.net.Uri;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.RcsContactUceCapability;
import android.telephony.ims.aidl.IRcsUceControllerCallback;
import android.telephony.ims.aidl.IRcsUcePublishStateCallback;
import android.util.Log;
import com.android.ims.RcsFeatureManager;
import com.android.ims.rcs.uce.UceController;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class UceControllerManager implements RcsFeatureController.Feature {
    private final Context mContext;
    private final ExecutorService mExecutorService;
    private final FeatureFlags mFeatureFlags;
    private volatile RcsFeatureManager mRcsFeatureManager;
    private final int mSlotId;
    private volatile UceController mUceController;

    public UceControllerManager(Context context, int i, int i2, FeatureFlags featureFlags) {
        Log.d("UceControllerManager", "create: slotId=" + i + ", subId=" + i2);
        this.mSlotId = i;
        this.mContext = context;
        this.mExecutorService = Executors.newSingleThreadExecutor();
        this.mFeatureFlags = featureFlags;
        initUceController(i2);
    }

    public UceControllerManager(Context context, int i, ExecutorService executorService, UceController uceController, FeatureFlags featureFlags) {
        this.mSlotId = i;
        this.mContext = context;
        this.mExecutorService = executorService;
        this.mUceController = uceController;
        this.mFeatureFlags = featureFlags;
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onRcsConnected(final RcsFeatureManager rcsFeatureManager) {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onRcsConnected$0(rcsFeatureManager);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRcsConnected$0(RcsFeatureManager rcsFeatureManager) {
        this.mRcsFeatureManager = rcsFeatureManager;
        if (this.mUceController != null) {
            this.mUceController.onRcsConnected(rcsFeatureManager);
        } else {
            Log.d("UceControllerManager", "onRcsConnected: UceController is null");
        }
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onRcsDisconnected() {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onRcsDisconnected$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onRcsDisconnected$1() {
        this.mRcsFeatureManager = null;
        if (this.mUceController != null) {
            this.mUceController.onRcsDisconnected();
        } else {
            Log.d("UceControllerManager", "onRcsDisconnected: UceController is null");
        }
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onDestroy() {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onDestroy$2();
            }
        });
        this.mExecutorService.shutdown();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onDestroy$2() {
        Log.d("UceControllerManager", "onDestroy");
        if (this.mUceController != null) {
            this.mUceController.onDestroy();
        }
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onAssociatedSubscriptionUpdated(final int i) {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda16
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAssociatedSubscriptionUpdated$3(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAssociatedSubscriptionUpdated$3(int i) {
        Log.i("UceControllerManager", "onAssociatedSubscriptionUpdated: slotId=" + this.mSlotId + ", newSubId=" + i);
        initUceController(i);
        if (this.mUceController == null || this.mRcsFeatureManager == null) {
            return;
        }
        this.mUceController.onRcsConnected(this.mRcsFeatureManager);
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void onCarrierConfigChanged() {
        this.mExecutorService.submit(new Runnable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda15
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onCarrierConfigChanged$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCarrierConfigChanged$4() {
        Log.i("UceControllerManager", "onCarrierConfigChanged");
        if (this.mUceController != null) {
            this.mUceController.onCarrierConfigChanged();
        } else {
            Log.d("UceControllerManager", "onCarrierConfigChanged: UceController is null");
        }
    }

    public void requestCapabilities(final List list, final IRcsUceControllerCallback iRcsUceControllerCallback) throws ImsException {
        try {
            this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda12
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$requestCapabilities$5(list, iRcsUceControllerCallback);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "requestCapabilities: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$requestCapabilities$5(List list, IRcsUceControllerCallback iRcsUceControllerCallback) throws ImsException {
        checkUceControllerState();
        this.mUceController.requestCapabilities(list, iRcsUceControllerCallback);
        return Boolean.TRUE;
    }

    public void requestNetworkAvailability(final Uri uri, final IRcsUceControllerCallback iRcsUceControllerCallback) throws ImsException {
        try {
            this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda10
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$requestNetworkAvailability$6(uri, iRcsUceControllerCallback);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "requestNetworkAvailability exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$requestNetworkAvailability$6(Uri uri, IRcsUceControllerCallback iRcsUceControllerCallback) throws ImsException {
        checkUceControllerState();
        this.mUceController.requestAvailability(uri, iRcsUceControllerCallback);
        return Boolean.TRUE;
    }

    public int getUcePublishState(final boolean z) throws ImsException {
        try {
            return ((Integer) this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda3
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$getUcePublishState$7(z);
                }
            }).get()).intValue();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "getUcePublishState exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            return 6;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Integer lambda$getUcePublishState$7(boolean z) throws ImsException {
        checkUceControllerState();
        return Integer.valueOf(this.mUceController.getUcePublishState(z));
    }

    public RcsContactUceCapability addUceRegistrationOverride(final Set set) throws ImsException {
        try {
            return (RcsContactUceCapability) this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda14
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$addUceRegistrationOverride$8(set);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "addUceRegistrationOverride exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ RcsContactUceCapability lambda$addUceRegistrationOverride$8(Set set) throws ImsException {
        checkUceControllerState();
        return this.mUceController.addRegistrationOverrideCapabilities(set);
    }

    public RcsContactUceCapability removeUceRegistrationOverride(final Set set) throws ImsException {
        try {
            return (RcsContactUceCapability) this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda8
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$removeUceRegistrationOverride$9(set);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "removeUceRegistrationOverride exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ RcsContactUceCapability lambda$removeUceRegistrationOverride$9(Set set) throws ImsException {
        checkUceControllerState();
        return this.mUceController.removeRegistrationOverrideCapabilities(set);
    }

    public RcsContactUceCapability clearUceRegistrationOverride() throws ImsException {
        try {
            return (RcsContactUceCapability) this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda6
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$clearUceRegistrationOverride$10();
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "clearUceRegistrationOverride exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ RcsContactUceCapability lambda$clearUceRegistrationOverride$10() throws ImsException {
        checkUceControllerState();
        return this.mUceController.clearRegistrationOverrideCapabilities();
    }

    public RcsContactUceCapability getLatestRcsContactUceCapability() throws ImsException {
        try {
            return (RcsContactUceCapability) this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$getLatestRcsContactUceCapability$11();
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "getLatestRcsContactUceCapability exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ RcsContactUceCapability lambda$getLatestRcsContactUceCapability$11() throws ImsException {
        checkUceControllerState();
        return this.mUceController.getLatestRcsContactUceCapability();
    }

    public String getLastPidfXml() throws ImsException {
        try {
            return (String) this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda11
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$getLastPidfXml$12();
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "getLastPidfXml exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$getLastPidfXml$12() throws ImsException {
        checkUceControllerState();
        return this.mUceController.getLastPidfXml();
    }

    public boolean removeUceRequestDisallowedStatus() throws ImsException {
        try {
            return ((Boolean) this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda1
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$removeUceRequestDisallowedStatus$13();
                }
            }).get()).booleanValue();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "removeUceRequestDisallowedStatus exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$removeUceRequestDisallowedStatus$13() throws ImsException {
        if (this.mUceController == null) {
            throw new ImsException("UCE controller is null", 1);
        }
        this.mUceController.removeRequestDisallowedStatus();
        return Boolean.TRUE;
    }

    public boolean setCapabilitiesRequestTimeout(final long j) throws ImsException {
        try {
            return ((Boolean) this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda13
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$setCapabilitiesRequestTimeout$14(j);
                }
            }).get()).booleanValue();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "setCapabilitiesRequestTimeout exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$setCapabilitiesRequestTimeout$14(long j) throws ImsException {
        if (this.mUceController == null) {
            throw new ImsException("UCE controller is null", 1);
        }
        this.mUceController.setCapabilitiesRequestTimeout(j);
        return Boolean.TRUE;
    }

    public void registerPublishStateCallback(final IRcsUcePublishStateCallback iRcsUcePublishStateCallback, final boolean z) throws ImsException {
        try {
            this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda5
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$registerPublishStateCallback$15(iRcsUcePublishStateCallback, z);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "registerPublishStateCallback exception: " + e);
            Throwable cause = e.getCause();
            if (cause instanceof ImsException) {
                throw ((ImsException) cause);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$registerPublishStateCallback$15(IRcsUcePublishStateCallback iRcsUcePublishStateCallback, boolean z) throws ImsException {
        checkUceControllerState();
        this.mUceController.registerPublishStateCallback(iRcsUcePublishStateCallback, z);
        return Boolean.TRUE;
    }

    public void unregisterPublishStateCallback(final IRcsUcePublishStateCallback iRcsUcePublishStateCallback) {
        try {
            this.mExecutorService.submit(new Callable() { // from class: com.android.services.telephony.rcs.UceControllerManager$$ExternalSyntheticLambda7
                @Override // java.util.concurrent.Callable
                public final Object call() {
                    return this.f$0.lambda$unregisterPublishStateCallback$16(iRcsUcePublishStateCallback);
                }
            }).get();
        } catch (InterruptedException | ExecutionException e) {
            Log.w("UceControllerManager", "unregisterPublishStateCallback exception: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$unregisterPublishStateCallback$16(IRcsUcePublishStateCallback iRcsUcePublishStateCallback) {
        if (checkUceControllerState()) {
            this.mUceController.unregisterPublishStateCallback(iRcsUcePublishStateCallback);
        }
        return Boolean.TRUE;
    }

    private void initUceController(int i) {
        StringBuilder sb = new StringBuilder("initUceController: newSubId=");
        sb.append(i);
        sb.append(", current UceController subId=");
        sb.append(this.mUceController == null ? "null" : Integer.valueOf(this.mUceController.getSubId()));
        Log.d("UceControllerManager", sb.toString());
        if (this.mUceController == null) {
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                this.mUceController = new UceController(this.mContext, i, this.mFeatureFlags);
            }
        } else if (this.mUceController.getSubId() != i) {
            this.mUceController.onDestroy();
            this.mUceController = null;
            if (SubscriptionManager.isValidSubscriptionId(i)) {
                this.mUceController = new UceController(this.mContext, i, this.mFeatureFlags);
            }
        }
    }

    private boolean checkUceControllerState() throws ImsException {
        if (this.mUceController == null || this.mUceController.isUnavailable()) {
            throw new ImsException("UCE controller is unavailable", 1);
        }
        return true;
    }

    public UceController getUceController() {
        return this.mUceController;
    }

    @Override // com.android.services.telephony.rcs.RcsFeatureController.Feature
    public void dump(PrintWriter printWriter) {
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("UceControllerManager[" + this.mSlotId + "]:");
        androidUtilIndentingPrintWriter.increaseIndent();
        if (this.mUceController != null) {
            this.mUceController.dump(androidUtilIndentingPrintWriter);
        } else {
            androidUtilIndentingPrintWriter.println("UceController is null.");
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
    }
}
