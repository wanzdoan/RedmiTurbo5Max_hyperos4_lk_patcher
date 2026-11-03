package com.android.services.telephony.rcs;

import android.content.Context;
import android.net.Uri;
import android.telephony.ims.ImsReasonInfo;
import android.telephony.ims.ImsRegistrationAttributes;
import android.telephony.ims.aidl.IImsCapabilityCallback;
import android.telephony.ims.aidl.IImsRegistrationCallback;
import android.util.ArrayMap;
import android.util.Log;
import com.android.ims.FeatureConnector;
import com.android.ims.ImsException;
import com.android.ims.RcsFeatureManager;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.imsphone.ImsRegistrationCallbackHelper;
import com.android.phone.ImsStateCallbackController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class RcsFeatureController {
    private int mAssociatedSubId;
    private final Context mContext;
    private FeatureConnector mFeatureConnector;
    private FeatureConnector.Listener mFeatureConnectorListener;
    private FeatureConnectorFactory mFeatureFactory;
    private RcsFeatureManager mFeatureManager;
    private final Map mFeatures;
    private final ImsRegistrationCallbackHelper mImsRcsRegistrationHelper;
    private final Object mLock;
    private ImsRegistrationCallbackHelper.ImsRegistrationUpdate mRcsRegistrationUpdate;
    private RegistrationHelperFactory mRegistrationHelperFactory;
    private final int mSlotId;

    public interface Feature {
        void dump(PrintWriter printWriter);

        void onAssociatedSubscriptionUpdated(int i);

        void onCarrierConfigChanged();

        void onDestroy();

        void onRcsConnected(RcsFeatureManager rcsFeatureManager);

        void onRcsDisconnected();
    }

    public interface FeatureConnectorFactory {
        FeatureConnector create(Context context, int i, FeatureConnector.Listener listener, Executor executor, String str);
    }

    public interface RegistrationHelperFactory {
        ImsRegistrationCallbackHelper create(ImsRegistrationCallbackHelper.ImsRegistrationUpdate imsRegistrationUpdate, Executor executor);
    }

    public RcsFeatureController(Context context, int i, int i2) {
        this.mFeatureFactory = new FeatureConnectorFactory() { // from class: com.android.services.telephony.rcs.RcsFeatureController$$ExternalSyntheticLambda0
            @Override // com.android.services.telephony.rcs.RcsFeatureController.FeatureConnectorFactory
            public final FeatureConnector create(Context context2, int i3, FeatureConnector.Listener listener, Executor executor, String str) {
                return RcsFeatureManager.getConnector(context2, i3, listener, executor, str);
            }
        };
        this.mRegistrationHelperFactory = new RegistrationHelperFactory() { // from class: com.android.services.telephony.rcs.RcsFeatureController$$ExternalSyntheticLambda1
            @Override // com.android.services.telephony.rcs.RcsFeatureController.RegistrationHelperFactory
            public final ImsRegistrationCallbackHelper create(ImsRegistrationCallbackHelper.ImsRegistrationUpdate imsRegistrationUpdate, Executor executor) {
                return new ImsRegistrationCallbackHelper(imsRegistrationUpdate, executor);
            }
        };
        this.mFeatures = new ArrayMap();
        this.mLock = new Object();
        this.mFeatureConnectorListener = new FeatureConnector.Listener() { // from class: com.android.services.telephony.rcs.RcsFeatureController.1
            /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
            public void connectionReady(RcsFeatureManager rcsFeatureManager, int i3) throws ImsException {
                RcsFeatureController rcsFeatureController = RcsFeatureController.this;
                if (rcsFeatureManager == null) {
                    rcsFeatureController.logw("connectionReady returned null RcsFeatureManager");
                    return;
                }
                rcsFeatureController.logd("connectionReady");
                try {
                    RcsFeatureController.this.updateConnectionStatus(rcsFeatureManager);
                    RcsFeatureController.this.setupConnectionToService(rcsFeatureManager);
                    ImsStateCallbackController.getInstance().notifyExternalRcsStateChanged(RcsFeatureController.this.mSlotId, true, true);
                } catch (android.telephony.ims.ImsException e) {
                    RcsFeatureController.this.updateConnectionStatus(null);
                    throw new ImsException(e.getMessage(), 106);
                }
            }

            public void connectionUnavailable(int i3) {
                if (i3 == 3) {
                    RcsFeatureController.this.loge("unexpected - connectionUnavailable due to server unavailable");
                }
                RcsFeatureController.this.logd("connectionUnavailable");
                RcsFeatureController.this.removeConnectionToService();
                RcsFeatureController.this.updateConnectionStatus(null);
            }
        };
        ImsRegistrationCallbackHelper.ImsRegistrationUpdate imsRegistrationUpdate = new ImsRegistrationCallbackHelper.ImsRegistrationUpdate(this) { // from class: com.android.services.telephony.rcs.RcsFeatureController.2
            public void handleImsRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
            }

            public void handleImsRegistering(int i3) {
            }

            public void handleImsSubscriberAssociatedUriChanged(Uri[] uriArr) {
            }

            public void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i3, int i4) {
            }

            public void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i3, int i4, int i5) {
            }
        };
        this.mRcsRegistrationUpdate = imsRegistrationUpdate;
        this.mContext = context;
        this.mSlotId = i;
        this.mAssociatedSubId = i2;
        this.mImsRcsRegistrationHelper = this.mRegistrationHelperFactory.create(imsRegistrationUpdate, context.getMainExecutor());
    }

    public RcsFeatureController(Context context, int i, int i2, RegistrationHelperFactory registrationHelperFactory) {
        this.mFeatureFactory = new FeatureConnectorFactory() { // from class: com.android.services.telephony.rcs.RcsFeatureController$$ExternalSyntheticLambda0
            @Override // com.android.services.telephony.rcs.RcsFeatureController.FeatureConnectorFactory
            public final FeatureConnector create(Context context2, int i3, FeatureConnector.Listener listener, Executor executor, String str) {
                return RcsFeatureManager.getConnector(context2, i3, listener, executor, str);
            }
        };
        this.mRegistrationHelperFactory = new RegistrationHelperFactory() { // from class: com.android.services.telephony.rcs.RcsFeatureController$$ExternalSyntheticLambda1
            @Override // com.android.services.telephony.rcs.RcsFeatureController.RegistrationHelperFactory
            public final ImsRegistrationCallbackHelper create(ImsRegistrationCallbackHelper.ImsRegistrationUpdate imsRegistrationUpdate, Executor executor) {
                return new ImsRegistrationCallbackHelper(imsRegistrationUpdate, executor);
            }
        };
        this.mFeatures = new ArrayMap();
        this.mLock = new Object();
        this.mFeatureConnectorListener = new FeatureConnector.Listener() { // from class: com.android.services.telephony.rcs.RcsFeatureController.1
            /* JADX INFO: Thrown type has an unknown type hierarchy: com.android.ims.ImsException */
            public void connectionReady(RcsFeatureManager rcsFeatureManager, int i3) throws ImsException {
                RcsFeatureController rcsFeatureController = RcsFeatureController.this;
                if (rcsFeatureManager == null) {
                    rcsFeatureController.logw("connectionReady returned null RcsFeatureManager");
                    return;
                }
                rcsFeatureController.logd("connectionReady");
                try {
                    RcsFeatureController.this.updateConnectionStatus(rcsFeatureManager);
                    RcsFeatureController.this.setupConnectionToService(rcsFeatureManager);
                    ImsStateCallbackController.getInstance().notifyExternalRcsStateChanged(RcsFeatureController.this.mSlotId, true, true);
                } catch (android.telephony.ims.ImsException e) {
                    RcsFeatureController.this.updateConnectionStatus(null);
                    throw new ImsException(e.getMessage(), 106);
                }
            }

            public void connectionUnavailable(int i3) {
                if (i3 == 3) {
                    RcsFeatureController.this.loge("unexpected - connectionUnavailable due to server unavailable");
                }
                RcsFeatureController.this.logd("connectionUnavailable");
                RcsFeatureController.this.removeConnectionToService();
                RcsFeatureController.this.updateConnectionStatus(null);
            }
        };
        ImsRegistrationCallbackHelper.ImsRegistrationUpdate imsRegistrationUpdate = new ImsRegistrationCallbackHelper.ImsRegistrationUpdate(this) { // from class: com.android.services.telephony.rcs.RcsFeatureController.2
            public void handleImsRegistered(ImsRegistrationAttributes imsRegistrationAttributes) {
            }

            public void handleImsRegistering(int i3) {
            }

            public void handleImsSubscriberAssociatedUriChanged(Uri[] uriArr) {
            }

            public void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i3, int i4) {
            }

            public void handleImsUnregistered(ImsReasonInfo imsReasonInfo, int i3, int i4, int i5) {
            }
        };
        this.mRcsRegistrationUpdate = imsRegistrationUpdate;
        this.mContext = context;
        this.mSlotId = i;
        this.mAssociatedSubId = i2;
        this.mRegistrationHelperFactory = registrationHelperFactory;
        this.mImsRcsRegistrationHelper = registrationHelperFactory.create(imsRegistrationUpdate, context.getMainExecutor());
    }

    public void connect() {
        synchronized (this.mLock) {
            try {
                if (this.mFeatureConnector != null) {
                    return;
                }
                FeatureConnectorFactory featureConnectorFactory = this.mFeatureFactory;
                Context context = this.mContext;
                FeatureConnector featureConnectorCreate = featureConnectorFactory.create(context, this.mSlotId, this.mFeatureConnectorListener, context.getMainExecutor(), "RcsFeatureController");
                this.mFeatureConnector = featureConnectorCreate;
                featureConnectorCreate.connect();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void addFeature(Feature feature, Class cls) {
        synchronized (this.mLock) {
            this.mFeatures.put(cls, feature);
        }
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager != null) {
            feature.onRcsConnected(featureManager);
        } else {
            feature.onRcsDisconnected();
        }
    }

    public Object getFeature(Class cls) {
        Object obj;
        synchronized (this.mLock) {
            obj = this.mFeatures.get(cls);
        }
        return obj;
    }

    public void removeFeature(Class cls) {
        synchronized (this.mLock) {
            ((Feature) this.mFeatures.remove(cls)).onDestroy();
        }
    }

    public boolean hasActiveFeatures() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mFeatures.size() > 0;
        }
        return z;
    }

    public void updateAssociatedSubscription(int i) {
        this.mAssociatedSubId = i;
        updateCapabilities();
        synchronized (this.mLock) {
            try {
                Iterator it = this.mFeatures.values().iterator();
                while (it.hasNext()) {
                    ((Feature) it.next()).onAssociatedSubscriptionUpdated(i);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void onCarrierConfigChangedForSubscription() {
        updateCapabilities();
        synchronized (this.mLock) {
            try {
                Iterator it = this.mFeatures.values().iterator();
                while (it.hasNext()) {
                    ((Feature) it.next()).onCarrierConfigChanged();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void destroy() {
        synchronized (this.mLock) {
            try {
                Log.i("RcsFeatureController", "destroy: slotId=" + this.mSlotId);
                FeatureConnector featureConnector = this.mFeatureConnector;
                if (featureConnector != null) {
                    featureConnector.disconnect();
                }
                for (Feature feature : this.mFeatures.values()) {
                    feature.onRcsDisconnected();
                    feature.onDestroy();
                }
                this.mFeatures.clear();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setFeatureConnectorFactory(FeatureConnectorFactory featureConnectorFactory) {
        this.mFeatureFactory = featureConnectorFactory;
    }

    public void registerImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) throws android.telephony.ims.ImsException {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager == null) {
            throw new android.telephony.ims.ImsException("Service is not available", 1);
        }
        featureManager.registerImsRegistrationCallback(i, iImsRegistrationCallback);
    }

    public void unregisterImsRegistrationCallback(int i, IImsRegistrationCallback iImsRegistrationCallback) {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager != null) {
            featureManager.unregisterImsRegistrationCallback(i, iImsRegistrationCallback);
        }
    }

    public void registerRcsAvailabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) throws android.telephony.ims.ImsException {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager == null) {
            throw new android.telephony.ims.ImsException("Service is not available", 1);
        }
        featureManager.registerRcsAvailabilityCallback(i, iImsCapabilityCallback);
    }

    public void unregisterRcsAvailabilityCallback(int i, IImsCapabilityCallback iImsCapabilityCallback) {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager != null) {
            featureManager.unregisterRcsAvailabilityCallback(i, iImsCapabilityCallback);
        }
    }

    public boolean isCapable(int i, int i2) throws android.telephony.ims.ImsException {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager == null) {
            throw new android.telephony.ims.ImsException("Service is not available", 1);
        }
        return featureManager.isCapable(i, i2);
    }

    public boolean isAvailable(int i, int i2) throws android.telephony.ims.ImsException {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager == null) {
            throw new android.telephony.ims.ImsException("Service is not available", 1);
        }
        return featureManager.isAvailable(i, i2);
    }

    public void getRegistrationTech(Consumer consumer) {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager != null) {
            featureManager.getImsRegistrationTech(consumer);
        }
        consumer.accept(-1);
    }

    public void getRegistrationState(Consumer consumer) {
        consumer.accept(Integer.valueOf(this.mImsRcsRegistrationHelper.getImsRegistrationState()));
    }

    public int getAssociatedSubId() {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager != null) {
            return featureManager.getSubId();
        }
        return -1;
    }

    private void updateCapabilities() {
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager != null) {
            try {
                featureManager.updateCapabilities(this.mAssociatedSubId);
            } catch (android.telephony.ims.ImsException e) {
                Log.w("RcsFeatureController", "updateCapabilities failed:" + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupConnectionToService(RcsFeatureManager rcsFeatureManager) {
        logd("setupConnectionToService");
        rcsFeatureManager.openConnection();
        rcsFeatureManager.updateCapabilities(this.mAssociatedSubId);
        rcsFeatureManager.registerImsRegistrationCallback(this.mImsRcsRegistrationHelper.getCallbackBinder());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeConnectionToService() {
        logd("removeConnectionToService");
        RcsFeatureManager featureManager = getFeatureManager();
        if (featureManager != null) {
            featureManager.unregisterImsRegistrationCallback(this.mImsRcsRegistrationHelper.getCallbackBinder());
            featureManager.releaseConnection();
        }
        this.mImsRcsRegistrationHelper.reset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConnectionStatus(RcsFeatureManager rcsFeatureManager) {
        synchronized (this.mLock) {
            try {
                this.mFeatureManager = rcsFeatureManager;
                Map map = this.mFeatures;
                if (rcsFeatureManager != null) {
                    Iterator it = map.values().iterator();
                    while (it.hasNext()) {
                        ((Feature) it.next()).onRcsConnected(rcsFeatureManager);
                    }
                } else {
                    Iterator it2 = map.values().iterator();
                    while (it2.hasNext()) {
                        ((Feature) it2.next()).onRcsDisconnected();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private RcsFeatureManager getFeatureManager() {
        RcsFeatureManager rcsFeatureManager;
        synchronized (this.mLock) {
            rcsFeatureManager = this.mFeatureManager;
        }
        return rcsFeatureManager;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.print("slotId=");
        indentingPrintWriter.println(this.mSlotId);
        indentingPrintWriter.print("RegistrationState=");
        indentingPrintWriter.println(this.mImsRcsRegistrationHelper.getImsRegistrationState());
        indentingPrintWriter.print("connected=");
        synchronized (this.mLock) {
            try {
                indentingPrintWriter.println(this.mFeatureManager != null);
                indentingPrintWriter.println();
                indentingPrintWriter.println("RcsFeatureControllers:");
                indentingPrintWriter.increaseIndent();
                Iterator it = this.mFeatures.values().iterator();
                while (it.hasNext()) {
                    ((Feature) it.next()).dump(indentingPrintWriter);
                    indentingPrintWriter.println();
                }
                indentingPrintWriter.decreaseIndent();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        StringBuilder logPrefix = getLogPrefix();
        logPrefix.append(str);
        Log.d("RcsFeatureController", logPrefix.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logw(String str) {
        StringBuilder logPrefix = getLogPrefix();
        logPrefix.append(str);
        Log.w("RcsFeatureController", logPrefix.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        StringBuilder logPrefix = getLogPrefix();
        logPrefix.append(str);
        Log.e("RcsFeatureController", logPrefix.toString());
    }

    private StringBuilder getLogPrefix() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(this.mSlotId);
        sb.append("] ");
        return sb;
    }
}
