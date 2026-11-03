package com.android.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.service.carrier.CarrierMessagingServiceWrapper;
import android.service.carrier.MessagePdu;
import android.telephony.AnomalyReporter;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class CarrierServicesSmsFilter {
    protected static final boolean DBG = true;
    public static final int EVENT_ON_FILTER_COMPLETE_NOT_CALLED = 1;
    public static final int FILTER_COMPLETE_TIMEOUT_MS = 720000;
    private static final UUID sAnomalyNoResponseFromCarrierMessagingService = UUID.fromString("94095e8e-b516-4065-a8be-e05b84071002");
    private final CallbackTimeoutHandler mCallbackTimeoutHandler = new CallbackTimeoutHandler();
    private final CarrierServicesSmsFilterCallbackInterface mCarrierServicesSmsFilterCallback;
    private final Context mContext;
    private final int mDestPort;
    private FilterAggregator mFilterAggregator;
    private final LocalLog mLocalLog;
    private final String mLogTag;
    private final long mMessageId;
    private final String mPduFormat;
    private final byte[][] mPdus;
    private final Phone mPhone;

    public interface CarrierServicesSmsFilterCallbackInterface {
        void onFilterComplete(int i);
    }

    public CarrierServicesSmsFilter(Context context, Phone phone, byte[][] bArr, int i, String str, CarrierServicesSmsFilterCallbackInterface carrierServicesSmsFilterCallbackInterface, String str2, LocalLog localLog, long j) {
        this.mContext = context;
        this.mPhone = phone;
        this.mPdus = bArr;
        this.mDestPort = i;
        this.mPduFormat = str;
        this.mCarrierServicesSmsFilterCallback = carrierServicesSmsFilterCallbackInterface;
        this.mLogTag = str2;
        this.mLocalLog = localLog;
        this.mMessageId = j;
    }

    public boolean filter() {
        Optional carrierAppPackageForFiltering = getCarrierAppPackageForFiltering();
        ArrayList arrayList = new ArrayList();
        if (carrierAppPackageForFiltering.isPresent()) {
            arrayList.add((String) carrierAppPackageForFiltering.get());
        }
        String imsRcsPackageForIntent = CarrierSmsUtils.getImsRcsPackageForIntent(this.mContext, this.mPhone, new Intent("android.service.carrier.CarrierMessagingService"));
        if (imsRcsPackageForIntent != null) {
            arrayList.add(imsRcsPackageForIntent);
        }
        int i = 0;
        if (this.mFilterAggregator != null) {
            loge("filter: Cannot reuse the same CarrierServiceSmsFilter object for filtering");
            Phone$$ExternalSyntheticBUOutline1.m("filter: Cannot reuse the same CarrierServiceSmsFilter object for filtering");
            return false;
        }
        int size = arrayList.size();
        if (size <= 0) {
            return false;
        }
        FilterAggregator filterAggregator = new FilterAggregator(size);
        this.mFilterAggregator = filterAggregator;
        CallbackTimeoutHandler callbackTimeoutHandler = this.mCallbackTimeoutHandler;
        callbackTimeoutHandler.sendMessageDelayed(callbackTimeoutHandler.obtainMessage(1, filterAggregator), 720000L);
        int size2 = arrayList.size();
        while (i < size2) {
            Object obj = arrayList.get(i);
            i++;
            filterWithPackage((String) obj, this.mFilterAggregator);
        }
        return true;
    }

    private Optional getCarrierAppPackageForFiltering() {
        List<String> carrierPackageNamesForIntent;
        CarrierPrivilegesTracker carrierPrivilegesTracker = this.mPhone.getCarrierPrivilegesTracker();
        if (carrierPrivilegesTracker != null) {
            carrierPackageNamesForIntent = carrierPrivilegesTracker.getCarrierPackageNamesForIntent(new Intent("android.service.carrier.CarrierMessagingService"));
        } else {
            loge("getCarrierAppPackageForFiltering: UiccCard not initialized");
            carrierPackageNamesForIntent = null;
        }
        if (carrierPackageNamesForIntent != null && carrierPackageNamesForIntent.size() == 1) {
            log("getCarrierAppPackageForFiltering: Found carrier package: " + carrierPackageNamesForIntent.get(0));
            return Optional.of(carrierPackageNamesForIntent.get(0));
        }
        List systemAppForIntent = getSystemAppForIntent(new Intent("android.service.carrier.CarrierMessagingService"));
        if (systemAppForIntent != null && systemAppForIntent.size() == 1) {
            log("getCarrierAppPackageForFiltering: Found system package: " + ((String) systemAppForIntent.get(0)));
            return Optional.of((String) systemAppForIntent.get(0));
        }
        logv("getCarrierAppPackageForFiltering: Unable to find carrierPackages: " + carrierPackageNamesForIntent + " or systemPackages: " + systemAppForIntent);
        return Optional.empty();
    }

    private void filterWithPackage(String str, FilterAggregator filterAggregator) {
        CarrierSmsFilter carrierSmsFilter = new CarrierSmsFilter(this.mPdus, this.mDestPort, this.mPduFormat, str);
        CarrierSmsFilterCallback carrierSmsFilterCallback = new CarrierSmsFilterCallback(filterAggregator, carrierSmsFilter.mCarrierMessagingServiceWrapper, str);
        filterAggregator.addToCallbacks(carrierSmsFilterCallback);
        carrierSmsFilter.filterSms(carrierSmsFilterCallback);
    }

    private List getSystemAppForIntent(Intent intent) {
        ArrayList arrayList = new ArrayList();
        PackageManager packageManager = this.mContext.getPackageManager();
        for (ResolveInfo resolveInfo : packageManager.queryIntentServices(intent, 0)) {
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo == null) {
                loge("getSystemAppForIntent: Can't get service information from " + resolveInfo);
            } else {
                String str = serviceInfo.packageName;
                if (packageManager.checkPermission("android.permission.CARRIER_FILTER_SMS", str) == 0) {
                    arrayList.add(str);
                    log("getSystemAppForIntent: added package " + str);
                }
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Rlog.d(this.mLogTag, str + ", id: " + this.mMessageId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Rlog.e(this.mLogTag, str + ", id: " + this.mMessageId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logv(String str) {
        Rlog.v(this.mLogTag, str + ", id: " + this.mMessageId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class CarrierSmsFilter {
        protected final CarrierMessagingServiceWrapper mCarrierMessagingServiceWrapper = new CarrierMessagingServiceWrapper();
        private final int mDestPort;
        private final String mPackageName;
        private final byte[][] mPdus;
        private volatile CarrierSmsFilterCallback mSmsFilterCallback;
        private final String mSmsFormat;

        CarrierSmsFilter(byte[][] bArr, int i, String str, String str2) {
            this.mPdus = bArr;
            this.mDestPort = i;
            this.mSmsFormat = str;
            this.mPackageName = str2;
        }

        void filterSms(CarrierSmsFilterCallback carrierSmsFilterCallback) {
            this.mSmsFilterCallback = carrierSmsFilterCallback;
            boolean zBindToCarrierMessagingService = this.mCarrierMessagingServiceWrapper.bindToCarrierMessagingService(CarrierServicesSmsFilter.this.mContext, this.mPackageName, new Executor() { // from class: com.android.internal.telephony.CarrierServicesSmsFilter$CarrierSmsFilter$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    runnable.run();
                }
            }, new Runnable() { // from class: com.android.internal.telephony.CarrierServicesSmsFilter$CarrierSmsFilter$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$filterSms$1();
                }
            });
            CarrierServicesSmsFilter carrierServicesSmsFilter = CarrierServicesSmsFilter.this;
            if (!zBindToCarrierMessagingService) {
                carrierServicesSmsFilter.loge("CarrierSmsFilter::filterSms: bindService() failed for " + this.mPackageName);
                carrierSmsFilterCallback.onReceiveSmsComplete(0);
                return;
            }
            carrierServicesSmsFilter.logv("CarrierSmsFilter::filterSms: bindService() succeeded for " + this.mPackageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: onServiceReady, reason: merged with bridge method [inline-methods] */
        public void lambda$filterSms$1() {
            try {
                CarrierServicesSmsFilter.this.log("onServiceReady: calling filterSms on " + this.mPackageName);
                this.mCarrierMessagingServiceWrapper.receiveSms(new MessagePdu(Arrays.asList(this.mPdus)), this.mSmsFormat, this.mDestPort, CarrierServicesSmsFilter.this.mPhone.getSubId(), new Executor() { // from class: com.android.internal.telephony.CarrierServicesSmsFilter$CarrierSmsFilter$$ExternalSyntheticLambda2
                    @Override // java.util.concurrent.Executor
                    public final void execute(Runnable runnable) {
                        runnable.run();
                    }
                }, this.mSmsFilterCallback);
            } catch (RuntimeException e) {
                CarrierServicesSmsFilter.this.loge("Exception filtering the SMS with " + this.mPackageName + ": " + e);
                this.mSmsFilterCallback.onReceiveSmsComplete(0);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class CarrierSmsFilterCallback implements CarrierMessagingServiceWrapper.CarrierMessagingCallback {
        private final CarrierMessagingServiceWrapper mCarrierMessagingServiceWrapper;
        private final FilterAggregator mFilterAggregator;
        private boolean mIsOnFilterCompleteCalled = false;
        private final String mPackageName;

        CarrierSmsFilterCallback(FilterAggregator filterAggregator, CarrierMessagingServiceWrapper carrierMessagingServiceWrapper, String str) {
            this.mFilterAggregator = filterAggregator;
            this.mCarrierMessagingServiceWrapper = carrierMessagingServiceWrapper;
            this.mPackageName = str;
        }

        public void onReceiveSmsComplete(int i) {
            CarrierServicesSmsFilter.this.log("CarrierSmsFilterCallback::onFilterComplete: Called from " + this.mPackageName + " with result: " + i);
            if (this.mIsOnFilterCompleteCalled) {
                return;
            }
            this.mIsOnFilterCompleteCalled = true;
            this.mCarrierMessagingServiceWrapper.disconnect();
            this.mFilterAggregator.onFilterComplete(i, this);
        }

        public void onSendSmsComplete(int i, int i2) {
            CarrierServicesSmsFilter.this.loge("onSendSmsComplete: Unexpected call from " + this.mPackageName + " with result: " + i);
        }

        public void onSendMultipartSmsComplete(int i, int[] iArr) {
            CarrierServicesSmsFilter.this.loge("onSendMultipartSmsComplete: Unexpected call from " + this.mPackageName + " with result: " + i);
        }

        public void onSendMmsComplete(int i, byte[] bArr) {
            CarrierServicesSmsFilter.this.loge("onSendMmsComplete: Unexpected call from " + this.mPackageName + " with result: " + i);
        }

        public void onDownloadMmsComplete(int i) {
            CarrierServicesSmsFilter.this.loge("onDownloadMmsComplete: Unexpected call from " + this.mPackageName + " with result: " + i);
        }
    }

    private final class FilterAggregator {
        private int mNumPendingFilters;
        private final Object mFilterLock = new Object();
        private final Set mCallbacks = new HashSet();
        private int mFilterResult = 0;

        FilterAggregator(int i) {
            this.mNumPendingFilters = i;
        }

        void onFilterComplete(int i, CarrierSmsFilterCallback carrierSmsFilterCallback) {
            synchronized (this.mFilterLock) {
                try {
                    this.mNumPendingFilters--;
                    this.mCallbacks.remove(carrierSmsFilterCallback);
                    combine(i);
                    if (this.mNumPendingFilters == 0) {
                        long jClearCallingIdentity = Binder.clearCallingIdentity();
                        try {
                            CarrierServicesSmsFilter.this.mCarrierServicesSmsFilterCallback.onFilterComplete(this.mFilterResult);
                            Binder.restoreCallingIdentity(jClearCallingIdentity);
                            CarrierServicesSmsFilter.this.log("FilterAggregator::onFilterComplete: called successfully with result = " + i);
                            CarrierServicesSmsFilter.this.mCallbackTimeoutHandler.removeMessages(1);
                        } catch (Throwable th) {
                            Binder.restoreCallingIdentity(jClearCallingIdentity);
                            throw th;
                        }
                    } else {
                        CarrierServicesSmsFilter.this.log("FilterAggregator::onFilterComplete: waiting for pending filters " + this.mNumPendingFilters);
                    }
                } catch (Throwable th2) {
                    throw th2;
                }
            }
        }

        private void combine(int i) {
            this.mFilterResult = i | this.mFilterResult;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addToCallbacks(CarrierSmsFilterCallback carrierSmsFilterCallback) {
            synchronized (this.mFilterLock) {
                this.mCallbacks.add(carrierSmsFilterCallback);
            }
        }
    }

    protected final class CallbackTimeoutHandler extends Handler {
        protected CallbackTimeoutHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            CarrierServicesSmsFilter.this.log("CallbackTimeoutHandler: handleMessage(" + message.what + ")");
            if (message.what != 1) {
                return;
            }
            CarrierServicesSmsFilter.this.mLocalLog.log("CarrierServicesSmsFilter: onFilterComplete timeout: not called before 720000 milliseconds.");
            String str = (String) ((FilterAggregator) message.obj).mCallbacks.stream().map(new Function() { // from class: com.android.internal.telephony.CarrierServicesSmsFilter$CallbackTimeoutHandler$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return ((CarrierServicesSmsFilter.CarrierSmsFilterCallback) obj).mPackageName;
                }
            }).collect(Collectors.joining(", "));
            AnomalyReporter.reportAnomaly(CarrierServicesSmsFilter.sAnomalyNoResponseFromCarrierMessagingService, "No response from " + str, CarrierServicesSmsFilter.this.mPhone.getCarrierId());
            handleFilterCallbacksTimeout();
        }

        private void handleFilterCallbacksTimeout() {
            for (CarrierSmsFilterCallback carrierSmsFilterCallback : CarrierServicesSmsFilter.this.mFilterAggregator.mCallbacks) {
                CarrierServicesSmsFilter.this.log("handleFilterCallbacksTimeout: calling onFilterComplete");
                carrierSmsFilterCallback.onReceiveSmsComplete(0);
            }
        }
    }
}
