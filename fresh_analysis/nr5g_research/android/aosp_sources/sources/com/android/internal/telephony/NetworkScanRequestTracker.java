package com.android.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.CellInfo;
import android.telephony.LocationAccessPolicy;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessSpecifier;
import android.telephony.SubscriptionInfo;
import android.util.Log;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.util.LinkPropertiesUtils;
import com.android.internal.util.ArrayUtils;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public final class NetworkScanRequestTracker {
    private IHyperUiccManager mIHyperUiccManager;
    private final Handler mHandler = new Handler() { // from class: com.android.internal.telephony.NetworkScanRequestTracker.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d("ScanRequestTracker", "Received Event :" + message.what);
            switch (message.what) {
                case 1:
                    NetworkScanRequestTracker.this.mScheduler.doStartScan((NetworkScanRequestInfo) message.obj);
                    break;
                case 2:
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    NetworkScanRequestTracker.this.mScheduler.startScanDone(asyncResult);
                    ((NetworkScanRequestInfo) asyncResult.userObj).mPhone.setNetworkScanStarted(true);
                    break;
                case 3:
                    NetworkScanRequestTracker.this.mScheduler.receiveResult((AsyncResult) message.obj);
                    break;
                case 4:
                    NetworkScanRequestTracker.this.mScheduler.doStopScan(message.arg1, message.arg2);
                    break;
                case 5:
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    NetworkScanRequestTracker.this.mScheduler.stopScanDone(asyncResult2);
                    ((NetworkScanRequestInfo) asyncResult2.userObj).mPhone.setNetworkScanStarted(false);
                    break;
                case 6:
                    NetworkScanRequestTracker.this.mScheduler.doInterruptScan(message.arg1);
                    break;
                case 7:
                    AsyncResult asyncResult3 = (AsyncResult) message.obj;
                    NetworkScanRequestTracker.this.mScheduler.interruptScanDone(asyncResult3);
                    ((NetworkScanRequestInfo) asyncResult3.userObj).mPhone.setNetworkScanStarted(false);
                    break;
                case 8:
                case 9:
                    AsyncResult asyncResult4 = (AsyncResult) message.obj;
                    NetworkScanRequestTracker.this.mScheduler.deleteScanAndMayNotify((NetworkScanRequestInfo) asyncResult4.userObj, 1, true);
                    ((NetworkScanRequestInfo) asyncResult4.userObj).mPhone.setNetworkScanStarted(false);
                    break;
            }
        }
    };
    private final AtomicInteger mNextNetworkScanRequestId = new AtomicInteger(1);
    private final NetworkScanRequestScheduler mScheduler = new NetworkScanRequestScheduler();

    /* JADX INFO: Access modifiers changed from: private */
    public void logEmptyResultOrException(AsyncResult asyncResult) {
        if (asyncResult.result == null) {
            Log.e("ScanRequestTracker", "NetworkScanResult: Empty result");
            return;
        }
        Log.e("ScanRequestTracker", "NetworkScanResult: Exception: " + asyncResult.exception);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidScan(NetworkScanRequestInfo networkScanRequestInfo) {
        if (networkScanRequestInfo.mRequest == null || ArrayUtils.isEmpty(networkScanRequestInfo.mRequest.getSpecifiers()) || networkScanRequestInfo.mRequest.getSpecifiers().length > 8) {
            return false;
        }
        for (RadioAccessSpecifier radioAccessSpecifier : networkScanRequestInfo.mRequest.getSpecifiers()) {
            if (radioAccessSpecifier.getRadioAccessNetwork() != 1 && radioAccessSpecifier.getRadioAccessNetwork() != 2 && radioAccessSpecifier.getRadioAccessNetwork() != 3 && radioAccessSpecifier.getRadioAccessNetwork() != 6) {
                return false;
            }
            if (radioAccessSpecifier.getBands() != null && radioAccessSpecifier.getBands().length > 8) {
                return false;
            }
            if (radioAccessSpecifier.getChannels() != null && radioAccessSpecifier.getChannels().length > 32) {
                return false;
            }
        }
        if (networkScanRequestInfo.mRequest.getSearchPeriodicity() >= 5 && networkScanRequestInfo.mRequest.getSearchPeriodicity() <= 300 && networkScanRequestInfo.mRequest.getMaxSearchTime() >= 60 && networkScanRequestInfo.mRequest.getMaxSearchTime() <= 3600 && networkScanRequestInfo.mRequest.getIncrementalResultsPeriodicity() >= 1 && networkScanRequestInfo.mRequest.getIncrementalResultsPeriodicity() <= 10 && networkScanRequestInfo.mRequest.getSearchPeriodicity() <= networkScanRequestInfo.mRequest.getMaxSearchTime() && networkScanRequestInfo.mRequest.getIncrementalResultsPeriodicity() <= networkScanRequestInfo.mRequest.getMaxSearchTime()) {
            return networkScanRequestInfo.mRequest.getPlmns() == null || networkScanRequestInfo.mRequest.getPlmns().size() <= 20;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean doesCellInfoCorrespondToKnownMccMnc(CellInfo cellInfo, Collection collection) {
        return collection.contains(cellInfo.getCellIdentity().getMccString() + cellInfo.getCellIdentity().getMncString());
    }

    public static Set<String> getAllowedMccMncsForLocationRestrictedScan(Context context) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            return (Set) SubscriptionManagerService.getInstance().getAvailableSubscriptionInfoList(context.getOpPackageName(), context.getAttributionTag()).stream().flatMap(new Function() { // from class: com.android.internal.telephony.NetworkScanRequestTracker$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return NetworkScanRequestTracker.getAllowableMccMncsFromSubscriptionInfo((SubscriptionInfo) obj);
                }
            }).collect(Collectors.toSet());
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Stream getAllowableMccMncsFromSubscriptionInfo(SubscriptionInfo subscriptionInfo) {
        Stream streamFlatMap = Stream.of((Object[]) new List[]{subscriptionInfo.getEhplmns(), subscriptionInfo.getHplmns()}).flatMap(new NetworkScanRequestTracker$$ExternalSyntheticLambda1());
        if (subscriptionInfo.getMccString() == null || subscriptionInfo.getMncString() == null) {
            return streamFlatMap;
        }
        return Stream.concat(streamFlatMap, Stream.of(subscriptionInfo.getMccString() + subscriptionInfo.getMncString()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyMessenger(NetworkScanRequestInfo networkScanRequestInfo, int i, int i2, List list) {
        Messenger messenger = networkScanRequestInfo.mMessenger;
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.arg1 = i2;
        messageObtain.arg2 = networkScanRequestInfo.mScanId;
        if (list != null) {
            if (i == 4) {
                final Set<String> allowedMccMncsForLocationRestrictedScan = getAllowedMccMncsForLocationRestrictedScan(networkScanRequestInfo.mPhone.getContext());
                list = (List) list.stream().map(new Function() { // from class: com.android.internal.telephony.NetworkScanRequestTracker$$ExternalSyntheticLambda2
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        return ((CellInfo) obj).sanitizeLocationInfo();
                    }
                }).filter(new Predicate() { // from class: com.android.internal.telephony.NetworkScanRequestTracker$$ExternalSyntheticLambda3
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return NetworkScanRequestTracker.doesCellInfoCorrespondToKnownMccMnc((CellInfo) obj, allowedMccMncsForLocationRestrictedScan);
                    }
                }).collect(Collectors.toList());
            }
            CellInfo[] cellInfoArr = (CellInfo[]) list.toArray(new CellInfo[list.size()]);
            Bundle bundle = new Bundle();
            bundle.putParcelableArray("scanResult", cellInfoArr);
            messageObtain.setData(bundle);
        } else {
            messageObtain.obj = null;
        }
        try {
            messenger.send(messageObtain);
        } catch (RemoteException e) {
            Log.e("ScanRequestTracker", "Exception in notifyMessenger: " + e);
        }
    }

    public class NetworkScanRequestInfo implements IBinder.DeathRecipient {
        private final IBinder mBinder;
        private final String mCallingPackage;
        private boolean mIsBinderDead = false;
        private final Messenger mMessenger;
        public final Phone mPhone;
        private final int mPid;
        private boolean mRenounceFineLocationAccess;
        private final NetworkScanRequest mRequest;
        private final int mScanId;
        private final int mUid;

        public NetworkScanRequestInfo(NetworkScanRequest networkScanRequest, Messenger messenger, IBinder iBinder, int i, Phone phone, int i2, int i3, String str, boolean z) {
            this.mRequest = networkScanRequest;
            this.mMessenger = messenger;
            this.mBinder = iBinder;
            this.mScanId = i;
            this.mPhone = phone;
            this.mUid = i2;
            this.mPid = i3;
            this.mCallingPackage = str;
            this.mRenounceFineLocationAccess = z;
            NetworkScanRequestTracker.this.mIHyperUiccManager = (IHyperUiccManager) HyperTelephonyFactory.getInstance().initModule(IHyperUiccManager.DEFAULT, new Object[0]);
            try {
                iBinder.linkToDeath(this, 0);
            } catch (RemoteException unused) {
                binderDied();
            }
        }

        synchronized void setIsBinderDead(boolean z) {
            this.mIsBinderDead = z;
        }

        synchronized boolean getIsBinderDead() {
            return this.mIsBinderDead;
        }

        NetworkScanRequest getRequest() {
            return this.mRequest;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.e("ScanRequestTracker", "PhoneInterfaceManager NetworkScanRequestInfo binderDied(" + this.mRequest + ", " + this.mBinder + ")");
            NetworkScanRequestTracker.this.mIHyperUiccManager.setInternalDataEnabled(PhoneFactory.getPhones());
            setIsBinderDead(true);
            NetworkScanRequestTracker.this.interruptNetworkScan(this.mScanId);
        }
    }

    private class NetworkScanRequestScheduler {
        private NetworkScanRequestInfo mLiveRequestInfo;
        private NetworkScanRequestInfo mPendingRequestInfo;

        private boolean cacheScan(NetworkScanRequestInfo networkScanRequestInfo) {
            return false;
        }

        private NetworkScanRequestScheduler() {
        }

        private int rilErrorToScanError(int i) {
            if (i == 0) {
                return 0;
            }
            if (i == 1) {
                Log.e("ScanRequestTracker", "rilErrorToScanError: RADIO_NOT_AVAILABLE");
                return 1;
            }
            if (i == 6) {
                Log.e("ScanRequestTracker", "rilErrorToScanError: REQUEST_NOT_SUPPORTED");
                return 4;
            }
            if (i == 40) {
                Log.e("ScanRequestTracker", "rilErrorToScanError: MODEM_ERR");
                return 1;
            }
            if (i == 44) {
                Log.e("ScanRequestTracker", "rilErrorToScanError: INVALID_ARGUMENTS");
                return 2;
            }
            if (i == 54) {
                Log.e("ScanRequestTracker", "rilErrorToScanError: OPERATION_NOT_ALLOWED");
                return 1;
            }
            if (i == 64) {
                Log.e("ScanRequestTracker", "rilErrorToScanError: DEVICE_IN_USE");
                return 3;
            }
            if (i == 37) {
                Log.e("ScanRequestTracker", "rilErrorToScanError: NO_MEMORY");
                return 1;
            }
            if (i == 38) {
                Log.e("ScanRequestTracker", "rilErrorToScanError: INTERNAL_ERR");
                return 1;
            }
            Log.e("ScanRequestTracker", "rilErrorToScanError: Unexpected RadioError " + i);
            return LinkPropertiesUtils.MAX_MTU;
        }

        private int commandExceptionErrorToScanError(CommandException.Error error) {
            switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$CommandException$Error[error.ordinal()]) {
                case 1:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: RADIO_NOT_AVAILABLE");
                    return 1;
                case 2:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: REQUEST_NOT_SUPPORTED");
                    return 4;
                case 3:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: NO_MEMORY");
                    return 1;
                case 4:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: INTERNAL_ERR");
                    return 1;
                case 5:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: MODEM_ERR");
                    return 1;
                case 6:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: OPERATION_NOT_ALLOWED");
                    return 1;
                case 7:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: INVALID_ARGUMENTS");
                    return 2;
                case 8:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: DEVICE_IN_USE");
                    return 3;
                default:
                    Log.e("ScanRequestTracker", "commandExceptionErrorToScanError: Unexpected CommandExceptionError " + error);
                    return LinkPropertiesUtils.MAX_MTU;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void doStartScan(NetworkScanRequestInfo networkScanRequestInfo) {
            if (networkScanRequestInfo == null) {
                Log.e("ScanRequestTracker", "CMD_START_NETWORK_SCAN: nsri is null");
                return;
            }
            if (!NetworkScanRequestTracker.this.isValidScan(networkScanRequestInfo)) {
                NetworkScanRequestTracker.this.notifyMessenger(networkScanRequestInfo, 2, 2, null);
                return;
            }
            if (networkScanRequestInfo.getIsBinderDead()) {
                Log.e("ScanRequestTracker", "CMD_START_NETWORK_SCAN: Binder has died");
            } else {
                if (startNewScan(networkScanRequestInfo) || interruptLiveScan(networkScanRequestInfo) || cacheScan(networkScanRequestInfo)) {
                    return;
                }
                NetworkScanRequestTracker.this.notifyMessenger(networkScanRequestInfo, 2, 3, null);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void startScanDone(AsyncResult asyncResult) {
            NetworkScanRequestInfo networkScanRequestInfo = (NetworkScanRequestInfo) asyncResult.userObj;
            if (networkScanRequestInfo == null) {
                Log.e("ScanRequestTracker", "EVENT_START_NETWORK_SCAN_DONE: nsri is null");
                return;
            }
            if (this.mLiveRequestInfo != null && networkScanRequestInfo.mScanId == this.mLiveRequestInfo.mScanId) {
                if (asyncResult.exception == null && asyncResult.result != null) {
                    NetworkScanRequestTracker.this.mIHyperUiccManager.registerForNetworkScanResult(networkScanRequestInfo, NetworkScanRequestTracker.this.mHandler, 3, 1);
                } else {
                    NetworkScanRequestTracker.this.mIHyperUiccManager.unregisterForNetworkScanResult(networkScanRequestInfo, NetworkScanRequestTracker.this.mHandler);
                    NetworkScanRequestTracker.this.logEmptyResultOrException(asyncResult);
                    Throwable th = asyncResult.exception;
                    if (th != null) {
                        deleteScanAndMayNotify(networkScanRequestInfo, commandExceptionErrorToScanError(((CommandException) th).getCommandError()), true);
                    } else {
                        Log.wtf("ScanRequestTracker", "EVENT_START_NETWORK_SCAN_DONE: ar.exception can not be null!");
                    }
                }
                return;
            }
            Log.e("ScanRequestTracker", "EVENT_START_NETWORK_SCAN_DONE: nsri does not match mLiveRequestInfo");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void receiveResult(AsyncResult asyncResult) {
            Object obj;
            NetworkScanRequestInfo networkScanRequestInfo = (NetworkScanRequestInfo) asyncResult.userObj;
            if (networkScanRequestInfo == null) {
                Log.e("ScanRequestTracker", "EVENT_RECEIVE_NETWORK_SCAN_RESULT: nsri is null");
                return;
            }
            if (networkScanRequestInfo != this.mLiveRequestInfo) {
                Log.e("ScanRequestTracker", "EVENT_RECEIVE_NETWORK_SCAN_RESULT received for inactive scan");
                return;
            }
            LocationAccessPolicy.LocationPermissionQuery locationPermissionQueryBuild = new LocationAccessPolicy.LocationPermissionQuery.Builder().setCallingPackage(networkScanRequestInfo.mCallingPackage).setCallingPid(networkScanRequestInfo.mPid).setCallingUid(networkScanRequestInfo.mUid).setCallingFeatureId(networkScanRequestInfo.mPhone.getContext().getAttributionTag()).setMinSdkVersionForFine(29).setMinSdkVersionForCoarse(29).setMinSdkVersionForEnforcement(29).setMethod("NetworkScanTracker#onResult").build();
            if (asyncResult.exception == null && (obj = asyncResult.result) != null) {
                NetworkScanResult networkScanResult = (NetworkScanResult) obj;
                int i = (networkScanRequestInfo.mRenounceFineLocationAccess || LocationAccessPolicy.checkLocationPermission(networkScanRequestInfo.mPhone.getContext(), locationPermissionQueryBuild) != LocationAccessPolicy.LocationPermissionResult.ALLOWED) ? 4 : 1;
                int i2 = networkScanResult.scanError;
                if (i2 == 0) {
                    if (networkScanRequestInfo.mPhone.getServiceStateTracker() != null) {
                        networkScanRequestInfo.mPhone.getServiceStateTracker().updateOperatorNameForCellInfo(networkScanResult.networkInfos);
                    }
                    NetworkScanRequestTracker.this.notifyMessenger(networkScanRequestInfo, i, rilErrorToScanError(networkScanResult.scanError), networkScanResult.networkInfos);
                    if (networkScanResult.scanStatus == 2) {
                        networkScanRequestInfo.mPhone.mCi.unregisterForNetworkScanResult(NetworkScanRequestTracker.this.mHandler);
                        deleteScanAndMayNotify(networkScanRequestInfo, 0, true);
                        return;
                    }
                    return;
                }
                if (networkScanResult.networkInfos != null) {
                    NetworkScanRequestTracker.this.notifyMessenger(networkScanRequestInfo, i, rilErrorToScanError(i2), networkScanResult.networkInfos);
                }
                networkScanRequestInfo.mPhone.mCi.unregisterForNetworkScanResult(NetworkScanRequestTracker.this.mHandler);
                deleteScanAndMayNotify(networkScanRequestInfo, rilErrorToScanError(networkScanResult.scanError), true);
                return;
            }
            NetworkScanRequestTracker.this.logEmptyResultOrException(asyncResult);
            networkScanRequestInfo.mPhone.mCi.unregisterForNetworkScanResult(NetworkScanRequestTracker.this.mHandler);
            deleteScanAndMayNotify(networkScanRequestInfo, LinkPropertiesUtils.MAX_MTU, true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void doStopScan(int i, int i2) {
            try {
                NetworkScanRequestInfo networkScanRequestInfo = this.mLiveRequestInfo;
                if (networkScanRequestInfo != null && i == networkScanRequestInfo.mScanId && i2 == this.mLiveRequestInfo.mUid) {
                    this.mLiveRequestInfo.mPhone.stopNetworkScan(NetworkScanRequestTracker.this.mHandler.obtainMessage(5, this.mLiveRequestInfo));
                } else {
                    NetworkScanRequestInfo networkScanRequestInfo2 = this.mPendingRequestInfo;
                    if (networkScanRequestInfo2 != null && i == networkScanRequestInfo2.mScanId && i2 == NetworkScanRequestTracker.this.mScheduler.mPendingRequestInfo.mUid) {
                        NetworkScanRequestTracker.this.notifyMessenger(this.mPendingRequestInfo, 3, 0, null);
                        this.mPendingRequestInfo = null;
                    } else {
                        Log.e("ScanRequestTracker", "stopScan: scan " + i + " does not exist!");
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void stopScanDone(AsyncResult asyncResult) {
            NetworkScanRequestInfo networkScanRequestInfo = (NetworkScanRequestInfo) asyncResult.userObj;
            if (networkScanRequestInfo == null) {
                Log.e("ScanRequestTracker", "EVENT_STOP_NETWORK_SCAN_DONE: nsri is null");
                return;
            }
            networkScanRequestInfo.mPhone.mCi.unregisterForNetworkScanResult(NetworkScanRequestTracker.this.mHandler);
            if (asyncResult.exception == null && asyncResult.result != null) {
                deleteScanAndMayNotify(networkScanRequestInfo, 0, true);
                return;
            }
            NetworkScanRequestTracker.this.logEmptyResultOrException(asyncResult);
            Throwable th = asyncResult.exception;
            if (th == null) {
                Log.wtf("ScanRequestTracker", "EVENT_STOP_NETWORK_SCAN_DONE: ar.exception can not be null!");
            } else {
                deleteScanAndMayNotify(networkScanRequestInfo, commandExceptionErrorToScanError(((CommandException) th).getCommandError()), true);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void doInterruptScan(int i) {
            try {
                NetworkScanRequestInfo networkScanRequestInfo = this.mLiveRequestInfo;
                if (networkScanRequestInfo != null && i == networkScanRequestInfo.mScanId) {
                    this.mLiveRequestInfo.mPhone.stopNetworkScan(NetworkScanRequestTracker.this.mHandler.obtainMessage(7, this.mLiveRequestInfo));
                } else {
                    Log.e("ScanRequestTracker", "doInterruptScan: scan " + i + " does not exist!");
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void interruptScanDone(AsyncResult asyncResult) {
            NetworkScanRequestInfo networkScanRequestInfo = (NetworkScanRequestInfo) asyncResult.userObj;
            if (networkScanRequestInfo == null) {
                Log.e("ScanRequestTracker", "EVENT_INTERRUPT_NETWORK_SCAN_DONE: nsri is null");
            } else {
                networkScanRequestInfo.mPhone.mCi.unregisterForNetworkScanResult(NetworkScanRequestTracker.this.mHandler);
                deleteScanAndMayNotify(networkScanRequestInfo, 0, false);
            }
        }

        private synchronized boolean interruptLiveScan(NetworkScanRequestInfo networkScanRequestInfo) {
            if (this.mLiveRequestInfo == null || this.mPendingRequestInfo != null || !UserHandle.isSameApp(networkScanRequestInfo.mUid, 1000) || UserHandle.isSameApp(this.mLiveRequestInfo.mUid, 1000)) {
                return false;
            }
            doInterruptScan(this.mLiveRequestInfo.mScanId);
            this.mPendingRequestInfo = networkScanRequestInfo;
            NetworkScanRequestTracker.this.notifyMessenger(this.mLiveRequestInfo, 2, 10002, null);
            return true;
        }

        private synchronized boolean startNewScan(NetworkScanRequestInfo networkScanRequestInfo) {
            if (this.mLiveRequestInfo != null) {
                return false;
            }
            this.mLiveRequestInfo = networkScanRequestInfo;
            networkScanRequestInfo.mPhone.startNetworkScan(networkScanRequestInfo.getRequest(), NetworkScanRequestTracker.this.mHandler.obtainMessage(2, networkScanRequestInfo));
            networkScanRequestInfo.mPhone.mCi.registerForModemReset(NetworkScanRequestTracker.this.mHandler, 8, networkScanRequestInfo);
            networkScanRequestInfo.mPhone.mCi.registerForNotAvailable(NetworkScanRequestTracker.this.mHandler, 9, networkScanRequestInfo);
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public synchronized void deleteScanAndMayNotify(NetworkScanRequestInfo networkScanRequestInfo, int i, boolean z) {
            try {
                if (this.mLiveRequestInfo != null && networkScanRequestInfo.mScanId == this.mLiveRequestInfo.mScanId) {
                    if (z) {
                        NetworkScanRequestTracker networkScanRequestTracker = NetworkScanRequestTracker.this;
                        if (i == 0) {
                            networkScanRequestTracker.notifyMessenger(networkScanRequestInfo, 3, i, null);
                        } else {
                            networkScanRequestTracker.notifyMessenger(networkScanRequestInfo, 2, i, null);
                        }
                    }
                    this.mLiveRequestInfo.mPhone.mCi.unregisterForModemReset(NetworkScanRequestTracker.this.mHandler);
                    this.mLiveRequestInfo.mPhone.mCi.unregisterForNotAvailable(NetworkScanRequestTracker.this.mHandler);
                    this.mLiveRequestInfo = null;
                    NetworkScanRequestInfo networkScanRequestInfo2 = this.mPendingRequestInfo;
                    if (networkScanRequestInfo2 != null) {
                        startNewScan(networkScanRequestInfo2);
                        this.mPendingRequestInfo = null;
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.NetworkScanRequestTracker$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error;

        static {
            int[] iArr = new int[CommandException.Error.values().length];
            $SwitchMap$com$android$internal$telephony$CommandException$Error = iArr;
            try {
                iArr[CommandException.Error.RADIO_NOT_AVAILABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.REQUEST_NOT_SUPPORTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.NO_MEMORY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INTERNAL_ERR.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.MODEM_ERR.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.OPERATION_NOT_ALLOWED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.INVALID_ARGUMENTS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.DEVICE_IN_USE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void interruptNetworkScan(int i) {
        this.mHandler.obtainMessage(6, i, 0).sendToTarget();
    }

    public int startNetworkScan(boolean z, NetworkScanRequest networkScanRequest, Messenger messenger, IBinder iBinder, Phone phone, int i, int i2, String str) {
        int andIncrement = this.mNextNetworkScanRequestId.getAndIncrement();
        this.mHandler.obtainMessage(1, new NetworkScanRequestInfo(networkScanRequest, messenger, iBinder, andIncrement, phone, i, i2, str, z)).sendToTarget();
        return andIncrement;
    }

    public void stopNetworkScan(int i, int i2) {
        this.mHandler.obtainMessage(4, i, i2).sendToTarget();
    }
}
