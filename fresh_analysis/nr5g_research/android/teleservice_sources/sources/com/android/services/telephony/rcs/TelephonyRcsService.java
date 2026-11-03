package com.android.services.telephony.rcs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.IndentingPrintWriter;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.metrics.RcsStats;
import com.android.phone.ImsStateCallbackController;
import com.android.phone.R;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyRcsService {
    private static ResourceProxy sResourceProxy = new ResourceProxy() { // from class: com.android.services.telephony.rcs.TelephonyRcsService$$ExternalSyntheticLambda0
        @Override // com.android.services.telephony.rcs.TelephonyRcsService.ResourceProxy
        public final boolean getDeviceUceEnabled(Context context) {
            return context.getResources().getBoolean(R.bool.config_rcs_user_capability_exchange_enabled);
        }
    };
    private final Context mContext;
    private SparseArray mFeatureControllers;
    private final FeatureFlags mFeatureFlags;
    private int mNumSlots;
    private boolean mRcsUceEnabled;
    private SparseArray mSlotToAssociatedSubIds;
    private FeatureFactory mFeatureFactory = new FeatureFactory(this) { // from class: com.android.services.telephony.rcs.TelephonyRcsService.1
        @Override // com.android.services.telephony.rcs.TelephonyRcsService.FeatureFactory
        public RcsFeatureController createController(Context context, int i, int i2) {
            return new RcsFeatureController(context, i, i2);
        }

        @Override // com.android.services.telephony.rcs.TelephonyRcsService.FeatureFactory
        public UceControllerManager createUceControllerManager(Context context, int i, int i2, FeatureFlags featureFlags) {
            return new UceControllerManager(context, i, i2, featureFlags);
        }

        @Override // com.android.services.telephony.rcs.TelephonyRcsService.FeatureFactory
        public SipTransportController createSipTransportController(Context context, int i, int i2) {
            return new SipTransportController(context, i, i2);
        }
    };
    private final Object mLock = new Object();
    private BroadcastReceiver mCarrierConfigChangedReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.rcs.TelephonyRcsService.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            Bundle extras;
            if (intent == null || !"android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction()) || (extras = intent.getExtras()) == null) {
                return;
            }
            TelephonyRcsService.this.onCarrierConfigChangedForSlot(extras.getInt("android.telephony.extra.SLOT_INDEX", -1), extras.getInt("android.telephony.extra.SUBSCRIPTION_INDEX", -1));
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() { // from class: com.android.services.telephony.rcs.TelephonyRcsService$$ExternalSyntheticLambda1
        @Override // android.os.Handler.Callback
        public final boolean handleMessage(Message message) {
            return this.f$0.lambda$new$1(message);
        }
    });

    public interface FeatureFactory {
        RcsFeatureController createController(Context context, int i, int i2);

        SipTransportController createSipTransportController(Context context, int i, int i2);

        UceControllerManager createUceControllerManager(Context context, int i, int i2, FeatureFlags featureFlags);
    }

    public interface ResourceProxy {
        boolean getDeviceUceEnabled(Context context);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$1(Message message) {
        if (message.what != 1) {
            return false;
        }
        Integer num = (Integer) ((AsyncResult) message.obj).result;
        if (num == null) {
            Log.w("TelephonyRcsService", "msim config change with null num slots.");
        } else {
            updateFeatureControllerSize(num.intValue());
        }
        return true;
    }

    public TelephonyRcsService(Context context, int i, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mNumSlots = i;
        this.mFeatureControllers = new SparseArray(i);
        this.mSlotToAssociatedSubIds = new SparseArray(i);
        this.mRcsUceEnabled = sResourceProxy.getDeviceUceEnabled(context);
        this.mFeatureFlags = featureFlags;
        RcsStats.getInstance().registerUceCallback();
    }

    public TelephonyRcsService(Context context, int i, ResourceProxy resourceProxy, FeatureFlags featureFlags) {
        this.mContext = context;
        this.mNumSlots = i;
        this.mFeatureControllers = new SparseArray(i);
        this.mSlotToAssociatedSubIds = new SparseArray(i);
        sResourceProxy = resourceProxy;
        this.mRcsUceEnabled = resourceProxy.getDeviceUceEnabled(context);
        this.mFeatureFlags = featureFlags;
        RcsStats.getInstance().registerUceCallback();
    }

    public RcsFeatureController getFeatureController(int i) {
        RcsFeatureController rcsFeatureController;
        synchronized (this.mLock) {
            rcsFeatureController = (RcsFeatureController) this.mFeatureControllers.get(i);
        }
        return rcsFeatureController;
    }

    public void initialize() {
        updateFeatureControllerSize(this.mNumSlots);
        PhoneConfigurationManager.registerForMultiSimConfigChange(this.mHandler, 1, (Object) null);
        this.mContext.registerReceiver(this.mCarrierConfigChangedReceiver, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    }

    public void setFeatureFactory(FeatureFactory featureFactory) {
        this.mFeatureFactory = featureFactory;
    }

    public void updateFeatureControllerSize(int i) {
        synchronized (this.mLock) {
            try {
                int size = this.mFeatureControllers.size();
                if (size == i) {
                    return;
                }
                Log.i("TelephonyRcsService", "updateFeatureControllers: oldSlots=" + size + ", newNumSlots=" + i);
                this.mNumSlots = i;
                if (size < i) {
                    while (size < i) {
                        RcsFeatureController rcsFeatureControllerConstructFeatureController = constructFeatureController(size);
                        if (rcsFeatureControllerConstructFeatureController.hasActiveFeatures()) {
                            this.mFeatureControllers.put(size, rcsFeatureControllerConstructFeatureController);
                        }
                        size++;
                    }
                } else {
                    for (int i2 = size - 1; i2 > i - 1; i2--) {
                        RcsFeatureController rcsFeatureController = (RcsFeatureController) this.mFeatureControllers.get(i2);
                        if (rcsFeatureController != null) {
                            this.mFeatureControllers.remove(i2);
                            this.mSlotToAssociatedSubIds.remove(i2);
                            rcsFeatureController.destroy();
                        }
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public boolean verifyActiveSubId(int i, int i2) {
        boolean z;
        synchronized (this.mLock) {
            z = i2 == ((Integer) this.mSlotToAssociatedSubIds.get(i, -1)).intValue();
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCarrierConfigChangedForSlot(int i, int i2) {
        synchronized (this.mLock) {
            try {
                RcsFeatureController rcsFeatureControllerCreateController = (RcsFeatureController) this.mFeatureControllers.get(i);
                int iIntValue = ((Integer) this.mSlotToAssociatedSubIds.get(i, -1)).intValue();
                this.mSlotToAssociatedSubIds.put(i, Integer.valueOf(i2));
                StringBuilder sb = new StringBuilder("updateFeatureControllerSubscription: slotId=");
                sb.append(i);
                sb.append(", oldSubId= ");
                sb.append(iIntValue);
                sb.append(", subId=");
                sb.append(i2);
                sb.append(", existing feature=");
                sb.append(rcsFeatureControllerCreateController != null);
                Log.i("TelephonyRcsService", sb.toString());
                if (SubscriptionManager.isValidSubscriptionId(i2)) {
                    if (rcsFeatureControllerCreateController == null) {
                        rcsFeatureControllerCreateController = this.mFeatureFactory.createController(this.mContext, i, i2);
                        updateSupportedFeatures(rcsFeatureControllerCreateController, i, i2);
                        if (rcsFeatureControllerCreateController.hasActiveFeatures()) {
                            this.mFeatureControllers.put(i, rcsFeatureControllerCreateController);
                        }
                    } else {
                        updateSupportedFeatures(rcsFeatureControllerCreateController, i, i2);
                        if (!rcsFeatureControllerCreateController.hasActiveFeatures()) {
                            rcsFeatureControllerCreateController.destroy();
                            this.mFeatureControllers.remove(i);
                        }
                    }
                }
                if (rcsFeatureControllerCreateController != null) {
                    if (iIntValue == i2) {
                        rcsFeatureControllerCreateController.onCarrierConfigChangedForSubscription();
                    } else {
                        rcsFeatureControllerCreateController.updateAssociatedSubscription(i2);
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private RcsFeatureController constructFeatureController(int i) {
        int subscriptionFromSlot = getSubscriptionFromSlot(i);
        RcsFeatureController rcsFeatureControllerCreateController = this.mFeatureFactory.createController(this.mContext, i, subscriptionFromSlot);
        updateSupportedFeatures(rcsFeatureControllerCreateController, i, subscriptionFromSlot);
        return rcsFeatureControllerCreateController;
    }

    private void updateSupportedFeatures(RcsFeatureController rcsFeatureController, int i, int i2) {
        if (isDeviceUceEnabled() && doesSubscriptionSupportPresence(i2)) {
            if (rcsFeatureController.getFeature(UceControllerManager.class) == null) {
                rcsFeatureController.addFeature(this.mFeatureFactory.createUceControllerManager(this.mContext, i, i2, this.mFeatureFlags), UceControllerManager.class);
            }
        } else if (rcsFeatureController.getFeature(UceControllerManager.class) != null) {
            rcsFeatureController.removeFeature(UceControllerManager.class);
        }
        if (doesSubscriptionSupportSingleRegistration(i2)) {
            if (rcsFeatureController.getFeature(SipTransportController.class) == null) {
                rcsFeatureController.addFeature(this.mFeatureFactory.createSipTransportController(this.mContext, i, i2), SipTransportController.class);
            }
        } else if (rcsFeatureController.getFeature(SipTransportController.class) != null) {
            rcsFeatureController.removeFeature(SipTransportController.class);
        }
        if (rcsFeatureController.hasActiveFeatures()) {
            rcsFeatureController.connect();
        }
        ImsStateCallbackController.getInstance().notifyExternalRcsStateChanged(i, false, rcsFeatureController.hasActiveFeatures());
    }

    public boolean isDeviceUceEnabled() {
        return this.mRcsUceEnabled;
    }

    public void setDeviceUceEnabled(boolean z) {
        this.mRcsUceEnabled = z;
    }

    private boolean doesSubscriptionSupportPresence(int i) {
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            return false;
        }
        return getConfig(i, "use_rcs_sip_options_bool", false) | getConfig(i, "ims.enable_presence_publish_bool", false);
    }

    private boolean doesSubscriptionSupportSingleRegistration(int i) {
        if (SubscriptionManager.isValidSubscriptionId(i)) {
            return getConfig(i, "ims.ims_single_registration_required_bool", false);
        }
        return false;
    }

    private int getSubscriptionFromSlot(int i) {
        return SubscriptionManager.getSubscriptionId(i);
    }

    private boolean getConfig(int i, String str, boolean z) {
        PersistableBundle configForSubId;
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService(CarrierConfigManager.class);
        return (carrierConfigManager == null || (configForSubId = carrierConfigManager.getConfigForSubId(i, str)) == null) ? z : configForSubId.getBoolean(str, z);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("RcsFeatureControllers:");
        indentingPrintWriter.increaseIndent();
        synchronized (this.mLock) {
            for (int i = 0; i < this.mNumSlots; i++) {
                try {
                    RcsFeatureController rcsFeatureController = (RcsFeatureController) this.mFeatureControllers.get(i);
                    if (rcsFeatureController != null) {
                        indentingPrintWriter.increaseIndent();
                        rcsFeatureController.dump(fileDescriptor, printWriter, strArr);
                        indentingPrintWriter.decreaseIndent();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        indentingPrintWriter.decreaseIndent();
    }
}
