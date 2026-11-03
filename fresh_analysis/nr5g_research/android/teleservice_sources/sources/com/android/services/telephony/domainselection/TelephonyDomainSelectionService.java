package com.android.services.telephony.domainselection;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.telephony.BarringInfo;
import android.telephony.DomainSelectionService;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.TransportSelectorCallback;
import android.util.Log;
import android.util.SparseArray;
import androidx.emoji2.text.ConcurrencyHelpers$$ExternalSyntheticLambda0;
import com.android.internal.telephony.AndroidUtilIndentingPrintWriter;
import com.android.internal.telephony.LocalLog;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class TelephonyDomainSelectionService extends DomainSelectionService {
    private static final String TAG = TelephonyDomainSelectionService.class.getSimpleName();
    private static final LocalLog sEventLog = new LocalLog(20);
    private Context mContext;
    private CrossSimRedialingController mCrossSimRedialingController;
    private DataConnectionStateHelper mDataConnectionStateHelper;
    private final DomainSelectorBase.DestroyListener mDestroyListener;
    private final List mDomainSelectorContainers;
    private final DomainSelectorFactory mDomainSelectorFactory;
    private final ImsStateTrackerFactory mImsStateTrackerFactory;
    private final SparseArray mImsStateTrackers;
    private Handler mServiceHandler;
    private final SubscriptionManager.OnSubscriptionsChangedListener mSubscriptionsChangedListener;

    public interface DomainSelectorFactory {
        DomainSelectorBase create(Context context, int i, int i2, int i3, boolean z, Looper looper, ImsStateTracker imsStateTracker, DomainSelectorBase.DestroyListener destroyListener, CrossSimRedialingController crossSimRedialingController, DataConnectionStateHelper dataConnectionStateHelper);
    }

    public interface ImsStateTrackerFactory {
        ImsStateTracker create(Context context, int i, Looper looper);
    }

    final class DefaultDomainSelectorFactory implements DomainSelectorFactory {
        private DefaultDomainSelectorFactory() {
        }

        @Override // com.android.services.telephony.domainselection.TelephonyDomainSelectionService.DomainSelectorFactory
        public DomainSelectorBase create(Context context, int i, int i2, int i3, boolean z, Looper looper, ImsStateTracker imsStateTracker, DomainSelectorBase.DestroyListener destroyListener, CrossSimRedialingController crossSimRedialingController, DataConnectionStateHelper dataConnectionStateHelper) {
            TelephonyDomainSelectionService.logi("create-DomainSelector: slotId=" + i + ", subId=" + i2 + ", selectorType=" + TelephonyDomainSelectionService.selectorTypeToString(i3) + ", emergency=" + z);
            if (i3 == 1) {
                if (z) {
                    return new EmergencyCallDomainSelector(context, i, i2, looper, imsStateTracker, destroyListener, crossSimRedialingController, dataConnectionStateHelper);
                }
                return new NormalCallDomainSelector(context, i, i2, looper, imsStateTracker, destroyListener);
            }
            if (i3 != 2) {
                return null;
            }
            if (z) {
                return new EmergencySmsDomainSelector(context, i, i2, looper, imsStateTracker, destroyListener);
            }
            return new SmsDomainSelector(context, i, i2, looper, imsStateTracker, destroyListener);
        }
    }

    final class DomainSelectorContainer {
        private final boolean mIsEmergency;
        private final DomainSelectorBase mSelector;
        private final int mSelectorType;
        private final int mSlotId;

        DomainSelectorContainer(int i, int i2, boolean z, DomainSelectorBase domainSelectorBase) {
            this.mSlotId = i;
            this.mSelectorType = i2;
            this.mIsEmergency = z;
            this.mSelector = domainSelectorBase;
        }

        public int getSlotId() {
            return this.mSlotId;
        }

        public int getSelectorType() {
            return this.mSelectorType;
        }

        public DomainSelectorBase getDomainSelector() {
            return this.mSelector;
        }

        public boolean isEmergency() {
            return this.mIsEmergency;
        }

        public String toString() {
            return "{ slotId=" + this.mSlotId + ", selectorType=" + TelephonyDomainSelectionService.selectorTypeToString(this.mSelectorType) + ", isEmergency=" + this.mIsEmergency + ", selector=" + this.mSelector + " }";
        }
    }

    public TelephonyDomainSelectionService() {
        this(new ImsStateTrackerFactory() { // from class: com.android.services.telephony.domainselection.TelephonyDomainSelectionService$$ExternalSyntheticLambda0
            @Override // com.android.services.telephony.domainselection.TelephonyDomainSelectionService.ImsStateTrackerFactory
            public final ImsStateTracker create(Context context, int i, Looper looper) {
                return new ImsStateTracker(context, i, looper);
            }
        }, new DefaultDomainSelectorFactory(), null);
    }

    protected TelephonyDomainSelectionService(ImsStateTrackerFactory imsStateTrackerFactory, DomainSelectorFactory domainSelectorFactory, DataConnectionStateHelper dataConnectionStateHelper) {
        this.mDestroyListener = new DomainSelectorBase.DestroyListener() { // from class: com.android.services.telephony.domainselection.TelephonyDomainSelectionService.1
            @Override // com.android.services.telephony.domainselection.DomainSelectorBase.DestroyListener
            public void onDomainSelectorDestroyed(DomainSelectorBase domainSelectorBase) {
                TelephonyDomainSelectionService.logd("DomainSelector destroyed: " + domainSelectorBase);
                TelephonyDomainSelectionService.this.removeDomainSelector(domainSelectorBase);
            }
        };
        this.mSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.services.telephony.domainselection.TelephonyDomainSelectionService.2
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                TelephonyDomainSelectionService.this.handleSubscriptionsChanged();
            }
        };
        this.mImsStateTrackers = new SparseArray(2);
        this.mDomainSelectorContainers = new ArrayList();
        this.mImsStateTrackerFactory = imsStateTrackerFactory;
        this.mDomainSelectorFactory = domainSelectorFactory;
        this.mDataConnectionStateHelper = dataConnectionStateHelper;
    }

    public void onCreate() {
        logd("onCreate");
        this.mContext = getApplicationContext();
        getCreateExecutor();
        TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
        int activeModemCount = telephonyManager != null ? telephonyManager.getActiveModemCount() : 1;
        for (int i = 0; i < activeModemCount; i++) {
            this.mImsStateTrackers.put(i, this.mImsStateTrackerFactory.create(this.mContext, i, getLooper()));
        }
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        if (subscriptionManager != null) {
            subscriptionManager.addOnSubscriptionsChangedListener(getExecutor(), this.mSubscriptionsChangedListener);
        } else {
            loge("Adding OnSubscriptionChangedListener failed");
        }
        this.mCrossSimRedialingController = new CrossSimRedialingController(this.mContext, getLooper());
        if (this.mDataConnectionStateHelper == null) {
            this.mDataConnectionStateHelper = new DataConnectionStateHelper(this.mContext, getLooper());
        }
        logi("TelephonyDomainSelectionService created");
    }

    public void onDestroy() {
        ArrayList arrayList;
        SparseArray sparseArray;
        logd("onDestroy");
        synchronized (this.mDomainSelectorContainers) {
            arrayList = new ArrayList(this.mDomainSelectorContainers);
            this.mDomainSelectorContainers.clear();
        }
        int size = arrayList.size();
        int i = 0;
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            DomainSelectorBase domainSelector = ((DomainSelectorContainer) obj).getDomainSelector();
            if (domainSelector != null) {
                domainSelector.destroy();
            }
        }
        arrayList.clear();
        synchronized (this.mImsStateTrackers) {
            while (true) {
                try {
                    int size2 = this.mImsStateTrackers.size();
                    sparseArray = this.mImsStateTrackers;
                    if (i >= size2) {
                        break;
                    }
                    ImsStateTracker imsStateTracker = (ImsStateTracker) sparseArray.get(i);
                    if (imsStateTracker != null) {
                        imsStateTracker.destroy();
                    }
                    i++;
                } catch (Throwable th) {
                    throw th;
                }
            }
            sparseArray.clear();
        }
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        if (subscriptionManager != null) {
            subscriptionManager.removeOnSubscriptionsChangedListener(this.mSubscriptionsChangedListener);
        }
        CrossSimRedialingController crossSimRedialingController = this.mCrossSimRedialingController;
        if (crossSimRedialingController != null) {
            crossSimRedialingController.destroy();
            this.mCrossSimRedialingController = null;
        }
        DataConnectionStateHelper dataConnectionStateHelper = this.mDataConnectionStateHelper;
        if (dataConnectionStateHelper != null) {
            dataConnectionStateHelper.destroy();
            this.mDataConnectionStateHelper = null;
        }
        Handler handler = this.mServiceHandler;
        if (handler != null) {
            handler.getLooper().quit();
            this.mServiceHandler = null;
        }
    }

    public void onDomainSelection(final DomainSelectionService.SelectionAttributes selectionAttributes, final TransportSelectorCallback transportSelectorCallback) {
        int slotIndex = selectionAttributes.getSlotIndex();
        int subscriptionId = selectionAttributes.getSubscriptionId();
        int selectorType = selectionAttributes.getSelectorType();
        boolean zIsEmergency = selectionAttributes.isEmergency();
        ImsStateTracker imsStateTracker = getImsStateTracker(slotIndex);
        final DomainSelectorBase domainSelectorBaseCreate = this.mDomainSelectorFactory.create(this.mContext, slotIndex, subscriptionId, selectorType, zIsEmergency, getLooper(), imsStateTracker, this.mDestroyListener, this.mCrossSimRedialingController, this.mDataConnectionStateHelper);
        if (domainSelectorBaseCreate != null) {
            imsStateTracker.start(subscriptionId);
            addDomainSelector(slotIndex, selectorType, zIsEmergency, domainSelectorBaseCreate);
            this.mServiceHandler.post(new Runnable() { // from class: com.android.services.telephony.domainselection.TelephonyDomainSelectionService$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    TelephonyDomainSelectionService.m4441$r8$lambda$0tqTfmnmrGOMdi1r2VrKSCSn84(transportSelectorCallback, domainSelectorBaseCreate, selectionAttributes);
                }
            });
        } else {
            loge("No proper domain selector: " + selectorTypeToString(selectorType));
            this.mServiceHandler.post(new Runnable() { // from class: com.android.services.telephony.domainselection.TelephonyDomainSelectionService$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    transportSelectorCallback.onSelectionTerminated(36);
                }
            });
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$0t-qTfmnmrGOMdi1r2VrKSCSn84, reason: not valid java name */
    public static /* synthetic */ void m4441$r8$lambda$0tqTfmnmrGOMdi1r2VrKSCSn84(TransportSelectorCallback transportSelectorCallback, DomainSelectorBase domainSelectorBase, DomainSelectionService.SelectionAttributes selectionAttributes) {
        transportSelectorCallback.onCreated(domainSelectorBase);
        domainSelectorBase.selectDomain(selectionAttributes, transportSelectorCallback);
    }

    public void onServiceStateUpdated(int i, int i2, ServiceState serviceState) {
        ImsStateTracker imsStateTracker = getImsStateTracker(i);
        if (imsStateTracker != null) {
            imsStateTracker.updateServiceState(serviceState);
        }
    }

    public void onBarringInfoUpdated(int i, int i2, BarringInfo barringInfo) {
        ImsStateTracker imsStateTracker = getImsStateTracker(i);
        if (imsStateTracker != null) {
            imsStateTracker.updateBarringInfo(barringInfo);
        }
    }

    public Executor getCreateExecutor() {
        return getExecutor();
    }

    public Executor getExecutor() {
        if (this.mServiceHandler == null) {
            HandlerThread handlerThread = new HandlerThread(TAG);
            handlerThread.start();
            this.mServiceHandler = new Handler(handlerThread.getLooper());
        }
        Handler handler = this.mServiceHandler;
        Objects.requireNonNull(handler);
        return new ConcurrencyHelpers$$ExternalSyntheticLambda0(handler);
    }

    public Looper getLooper() {
        getExecutor();
        return this.mServiceHandler.getLooper();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSubscriptionsChanged() {
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService(SubscriptionManager.class);
        List<SubscriptionInfo> activeSubscriptionInfoList = subscriptionManager != null ? subscriptionManager.getActiveSubscriptionInfoList() : null;
        if (activeSubscriptionInfoList == null || activeSubscriptionInfoList.isEmpty()) {
            logd("handleSubscriptionsChanged: No valid SubscriptionInfo");
            return;
        }
        for (int i = 0; i < activeSubscriptionInfoList.size(); i++) {
            SubscriptionInfo subscriptionInfo = activeSubscriptionInfoList.get(i);
            int simSlotIndex = subscriptionInfo.getSimSlotIndex();
            if (simSlotIndex != -1) {
                logd("handleSubscriptionsChanged: slotId=" + simSlotIndex);
                getImsStateTracker(simSlotIndex).start(subscriptionInfo.getSubscriptionId());
            }
        }
    }

    private void addDomainSelector(int i, int i2, boolean z, DomainSelectorBase domainSelectorBase) {
        synchronized (this.mDomainSelectorContainers) {
            for (int i3 = 0; i3 < this.mDomainSelectorContainers.size(); i3++) {
                try {
                    DomainSelectorContainer domainSelectorContainer = (DomainSelectorContainer) this.mDomainSelectorContainers.get(i3);
                    if (domainSelectorContainer.getSlotId() == i && domainSelectorContainer.getSelectorType() == i2 && domainSelectorContainer.isEmergency() == z) {
                        this.mDomainSelectorContainers.remove(i3);
                        DomainSelectorBase domainSelector = domainSelectorContainer.getDomainSelector();
                        if (domainSelector == null) {
                            break;
                        }
                        logw("DomainSelector destroyed by new domain selection request: " + domainSelectorContainer);
                        domainSelector.destroy();
                        break;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            DomainSelectorContainer domainSelectorContainer2 = new DomainSelectorContainer(i, i2, z, domainSelectorBase);
            this.mDomainSelectorContainers.add(domainSelectorContainer2);
            logi("DomainSelector added: " + domainSelectorContainer2 + ", count=" + this.mDomainSelectorContainers.size());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeDomainSelector(DomainSelectorBase domainSelectorBase) {
        synchronized (this.mDomainSelectorContainers) {
            for (int i = 0; i < this.mDomainSelectorContainers.size(); i++) {
                try {
                    DomainSelectorContainer domainSelectorContainer = (DomainSelectorContainer) this.mDomainSelectorContainers.get(i);
                    if (domainSelectorContainer.getDomainSelector() == domainSelectorBase) {
                        this.mDomainSelectorContainers.remove(i);
                        logi("DomainSelector removed: " + domainSelectorContainer + ", count=" + this.mDomainSelectorContainers.size());
                        break;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    private ImsStateTracker getImsStateTracker(int i) {
        ImsStateTracker imsStateTrackerCreate;
        synchronized (this.mImsStateTrackers) {
            try {
                imsStateTrackerCreate = (ImsStateTracker) this.mImsStateTrackers.get(i);
                if (imsStateTrackerCreate == null) {
                    imsStateTrackerCreate = this.mImsStateTrackerFactory.create(this.mContext, i, getLooper());
                    this.mImsStateTrackers.put(i, imsStateTrackerCreate);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return imsStateTrackerCreate;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String selectorTypeToString(int i) {
        if (i == 1) {
            return "CALLING";
        }
        if (i == 2) {
            return "SMS";
        }
        return Integer.toString(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logi(String str) {
        Log.i(TAG, str);
        sEventLog.log(str);
    }

    private static void loge(String str) {
        Log.e(TAG, str);
        sEventLog.log(str);
    }

    private static void logw(String str) {
        Log.w(TAG, str);
        sEventLog.log(str);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int i;
        AndroidUtilIndentingPrintWriter androidUtilIndentingPrintWriter = new AndroidUtilIndentingPrintWriter(printWriter, "  ");
        androidUtilIndentingPrintWriter.println("TelephonyDomainSelectionService:");
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("ImsStateTrackers:");
        synchronized (this.mImsStateTrackers) {
            for (int i2 = 0; i2 < this.mImsStateTrackers.size(); i2++) {
                try {
                    ((ImsStateTracker) this.mImsStateTrackers.valueAt(i2)).dump(androidUtilIndentingPrintWriter);
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.increaseIndent();
        synchronized (this.mDomainSelectorContainers) {
            for (i = 0; i < this.mDomainSelectorContainers.size(); i++) {
                try {
                    DomainSelectorContainer domainSelectorContainer = (DomainSelectorContainer) this.mDomainSelectorContainers.get(i);
                    androidUtilIndentingPrintWriter.println("DomainSelector: " + domainSelectorContainer.toString());
                    androidUtilIndentingPrintWriter.increaseIndent();
                    DomainSelectorBase domainSelector = domainSelectorContainer.getDomainSelector();
                    if (domainSelector != null) {
                        domainSelector.dump(androidUtilIndentingPrintWriter);
                    }
                    androidUtilIndentingPrintWriter.decreaseIndent();
                } catch (Throwable th2) {
                    throw th2;
                }
            }
        }
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.increaseIndent();
        androidUtilIndentingPrintWriter.println("Event Log:");
        androidUtilIndentingPrintWriter.increaseIndent();
        sEventLog.dump(androidUtilIndentingPrintWriter);
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.decreaseIndent();
        androidUtilIndentingPrintWriter.println("________________________________");
    }
}
