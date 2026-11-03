package com.android.internal.telephony.euicc;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.euicc.DownloadSubscriptionResult;
import android.service.euicc.GetDefaultDownloadableSubscriptionListResult;
import android.service.euicc.GetDownloadableSubscriptionMetadataResult;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.service.euicc.IDeleteSubscriptionCallback;
import android.service.euicc.IDownloadSubscriptionCallback;
import android.service.euicc.IEraseSubscriptionsCallback;
import android.service.euicc.IEuiccService;
import android.service.euicc.IEuiccServiceDumpResultCallback;
import android.service.euicc.IGetAvailableMemoryInBytesCallback;
import android.service.euicc.IGetDefaultDownloadableSubscriptionListCallback;
import android.service.euicc.IGetDownloadableSubscriptionMetadataCallback;
import android.service.euicc.IGetEidCallback;
import android.service.euicc.IGetEuiccInfoCallback;
import android.service.euicc.IGetEuiccProfileInfoListCallback;
import android.service.euicc.IGetOtaStatusCallback;
import android.service.euicc.IOtaStatusChangedCallback;
import android.service.euicc.IRetainSubscriptionsForFactoryResetCallback;
import android.service.euicc.ISwitchToSubscriptionCallback;
import android.service.euicc.IUpdateSubscriptionNicknameCallback;
import android.telephony.AnomalyReporter;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccSlotInfo;
import android.telephony.euicc.DownloadableSubscription;
import android.telephony.euicc.EuiccInfo;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.telephony.IState;
import com.android.internal.telephony.MiuiPhoneStub;
import com.android.internal.telephony.PackageChangeReceiver;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.ntnphone.NtnRILConstants;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.util.NetworkStackConstants;
import com.android.internal.telephony.util.TelephonyUtils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class EuiccConnector extends StateMachine implements ServiceConnection {
    static final int BIND_TIMEOUT_MILLIS = 30000;
    static final int LINGER_TIMEOUT_MILLIS = 60000;
    private Set mActiveCommandCallbacks;
    public AvailableState mAvailableState;
    public BindingState mBindingState;
    public ConnectedState mConnectedState;
    private Context mContext;
    public DisconnectedState mDisconnectedState;
    private IEuiccService mEuiccService;
    private final PackageChangeReceiver mPackageMonitor;
    private PackageManager mPm;
    private ServiceInfo mSelectedComponent;
    private SubscriptionManager mSm;
    private TelephonyManager mTm;
    public UnavailableState mUnavailableState;
    private final BroadcastReceiver mUserUnlockedReceiver;

    public interface BaseEuiccCommandCallback {
        void onEuiccServiceUnavailable();
    }

    public interface DeleteCommandCallback extends BaseEuiccCommandCallback {
        void onDeleteComplete(int i);
    }

    public interface DownloadCommandCallback extends BaseEuiccCommandCallback {
        void onDownloadComplete(DownloadSubscriptionResult downloadSubscriptionResult);
    }

    public interface DumpEuiccServiceCommandCallback extends BaseEuiccCommandCallback {
        void onDumpEuiccServiceComplete(String str);
    }

    public interface EraseCommandCallback extends BaseEuiccCommandCallback {
        void onEraseComplete(int i);
    }

    public interface GetAvailableMemoryInBytesCommandCallback extends BaseEuiccCommandCallback {
        void onGetAvailableMemoryInBytesComplete(long j);

        void onUnsupportedOperationExceptionComplete(String str);
    }

    public interface GetDefaultListCommandCallback extends BaseEuiccCommandCallback {
        void onGetDefaultListComplete(int i, GetDefaultDownloadableSubscriptionListResult getDefaultDownloadableSubscriptionListResult);
    }

    public interface GetEidCommandCallback extends BaseEuiccCommandCallback {
        void onGetEidComplete(String str);
    }

    public interface GetEuiccInfoCommandCallback extends BaseEuiccCommandCallback {
        void onGetEuiccInfoComplete(EuiccInfo euiccInfo);
    }

    interface GetEuiccProfileInfoListCommandCallback extends BaseEuiccCommandCallback {
        void onListComplete(GetEuiccProfileInfoListResult getEuiccProfileInfoListResult);
    }

    public interface GetMetadataCommandCallback extends BaseEuiccCommandCallback {
        void onGetMetadataComplete(int i, GetDownloadableSubscriptionMetadataResult getDownloadableSubscriptionMetadataResult);
    }

    public interface GetOtaStatusCommandCallback extends BaseEuiccCommandCallback {
        void onGetOtaStatusComplete(int i);
    }

    public interface OtaStatusChangedCallback extends BaseEuiccCommandCallback {
        void onOtaStatusChanged(int i);
    }

    public interface RetainSubscriptionsCommandCallback extends BaseEuiccCommandCallback {
        void onRetainSubscriptionsComplete(int i);
    }

    public interface SwitchCommandCallback extends BaseEuiccCommandCallback {
        void onSwitchComplete(int i);
    }

    public interface UpdateNicknameCommandCallback extends BaseEuiccCommandCallback {
        void onUpdateNicknameComplete(int i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isEuiccCommand(int i) {
        return i >= 100;
    }

    public static ActivityInfo findBestActivity(PackageManager packageManager, Intent intent) {
        List<ResolveInfo> listQueryIntentActivities = packageManager.queryIntentActivities(intent, 269484096);
        ActivityInfo activityInfo = (ActivityInfo) findBestComponent(packageManager, listQueryIntentActivities);
        MiuiPhoneStub.IMiuiPhone iMiuiPhone = MiuiPhoneStub.get(0);
        ActivityInfo activityInfoOverrideBestEuiccUiActivity = iMiuiPhone != null ? iMiuiPhone.overrideBestEuiccUiActivity(packageManager, intent, activityInfo, listQueryIntentActivities) : null;
        if (activityInfoOverrideBestEuiccUiActivity != null) {
            activityInfo = activityInfoOverrideBestEuiccUiActivity;
        }
        if (activityInfo == null) {
            Log.w("EuiccConnector", "No valid component found for intent: " + intent);
        }
        return activityInfo;
    }

    public static ComponentInfo findBestComponent(PackageManager packageManager) {
        if (!MiuiTelephonyManagerStub.get().isEsimEnabled()) {
            Log.i("EuiccConnector", "eSIM is not supported");
            return null;
        }
        ComponentInfo componentInfoFindBestComponent = findBestComponent(packageManager, packageManager.queryIntentServices(new Intent("android.service.euicc.EuiccService"), 269484096));
        if (componentInfoFindBestComponent == null) {
            Log.w("EuiccConnector", "No valid EuiccService implementation found");
        }
        return componentInfoFindBestComponent;
    }

    static class GetMetadataRequest {
        GetMetadataCommandCallback mCallback;
        boolean mForceDeactivateSim;
        int mPortIndex;
        DownloadableSubscription mSubscription;
        boolean mSwitchAfterDownload;

        GetMetadataRequest() {
        }
    }

    static class DownloadRequest {
        DownloadCommandCallback mCallback;
        boolean mForceDeactivateSim;
        int mPortIndex;
        Bundle mResolvedBundle;
        DownloadableSubscription mSubscription;
        boolean mSwitchAfterDownload;

        DownloadRequest() {
        }
    }

    static class GetDefaultListRequest {
        GetDefaultListCommandCallback mCallback;
        boolean mForceDeactivateSim;

        GetDefaultListRequest() {
        }
    }

    static class DeleteRequest {
        DeleteCommandCallback mCallback;
        String mIccid;

        DeleteRequest() {
        }
    }

    static class SwitchRequest {
        SwitchCommandCallback mCallback;
        boolean mForceDeactivateSim;
        String mIccid;
        boolean mUsePortIndex;

        SwitchRequest() {
        }
    }

    static class UpdateNicknameRequest {
        UpdateNicknameCommandCallback mCallback;
        String mIccid;
        String mNickname;

        UpdateNicknameRequest() {
        }
    }

    EuiccConnector(Context context) {
        super("EuiccConnector");
        this.mPackageMonitor = new EuiccPackageMonitor();
        this.mUserUnlockedReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.euicc.EuiccConnector.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) || "android.intent.action.LOCALE_CHANGED".equals(intent.getAction()) || "miui.intent.action.MIUI_REGION_CHANGED".equals(intent.getAction())) {
                    EuiccConnector.this.sendMessage(1);
                }
            }
        };
        this.mActiveCommandCallbacks = new ArraySet();
        init(context);
    }

    public EuiccConnector(Context context, Looper looper) {
        super("EuiccConnector", looper);
        this.mPackageMonitor = new EuiccPackageMonitor();
        this.mUserUnlockedReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.euicc.EuiccConnector.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) || "android.intent.action.LOCALE_CHANGED".equals(intent.getAction()) || "miui.intent.action.MIUI_REGION_CHANGED".equals(intent.getAction())) {
                    EuiccConnector.this.sendMessage(1);
                }
            }
        };
        this.mActiveCommandCallbacks = new ArraySet();
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mPm = context.getPackageManager();
        this.mTm = (TelephonyManager) context.getSystemService("phone");
        this.mSm = (SubscriptionManager) context.getSystemService("telephony_subscription_service");
        setDbg(true);
        UnavailableState unavailableState = new UnavailableState();
        this.mUnavailableState = unavailableState;
        addState(unavailableState);
        AvailableState availableState = new AvailableState();
        this.mAvailableState = availableState;
        addState(availableState, this.mUnavailableState);
        BindingState bindingState = new BindingState();
        this.mBindingState = bindingState;
        addState(bindingState);
        DisconnectedState disconnectedState = new DisconnectedState();
        this.mDisconnectedState = disconnectedState;
        addState(disconnectedState);
        ConnectedState connectedState = new ConnectedState();
        this.mConnectedState = connectedState;
        addState(connectedState, this.mDisconnectedState);
        ServiceInfo serviceInfoFindBestComponent = findBestComponent();
        this.mSelectedComponent = serviceInfoFindBestComponent;
        setInitialState(serviceInfoFindBestComponent != null ? this.mAvailableState : this.mUnavailableState);
        start();
        this.mPackageMonitor.register(this.mContext, (Looper) null, (UserHandle) null);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.USER_UNLOCKED");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter.addAction("miui.intent.action.MIUI_REGION_CHANGED");
        this.mContext.registerReceiver(this.mUserUnlockedReceiver, intentFilter, 4);
    }

    @Override // com.android.internal.telephony.StateMachine
    public void onHalting() {
        this.mPackageMonitor.unregister();
        this.mContext.unregisterReceiver(this.mUserUnlockedReceiver);
    }

    public void getEid(int i, GetEidCommandCallback getEidCommandCallback) {
        sendMessage(100, i, 0, getEidCommandCallback);
    }

    public void getAvailableMemoryInBytes(int i, GetAvailableMemoryInBytesCommandCallback getAvailableMemoryInBytesCommandCallback) {
        sendMessage(115, i, 0, getAvailableMemoryInBytesCommandCallback);
    }

    public void getOtaStatus(int i, GetOtaStatusCommandCallback getOtaStatusCommandCallback) {
        sendMessage(111, i, 0, getOtaStatusCommandCallback);
    }

    public void startOtaIfNecessary(int i, OtaStatusChangedCallback otaStatusChangedCallback) {
        sendMessage(112, i, 0, otaStatusChangedCallback);
    }

    public void getDownloadableSubscriptionMetadata(int i, int i2, DownloadableSubscription downloadableSubscription, boolean z, boolean z2, GetMetadataCommandCallback getMetadataCommandCallback) {
        GetMetadataRequest getMetadataRequest = new GetMetadataRequest();
        getMetadataRequest.mSubscription = downloadableSubscription;
        getMetadataRequest.mForceDeactivateSim = z2;
        getMetadataRequest.mSwitchAfterDownload = z;
        getMetadataRequest.mPortIndex = i2;
        getMetadataRequest.mCallback = getMetadataCommandCallback;
        sendMessage(101, i, 0, getMetadataRequest);
    }

    public void downloadSubscription(int i, int i2, DownloadableSubscription downloadableSubscription, boolean z, boolean z2, Bundle bundle, DownloadCommandCallback downloadCommandCallback) {
        DownloadRequest downloadRequest = new DownloadRequest();
        downloadRequest.mSubscription = downloadableSubscription;
        downloadRequest.mSwitchAfterDownload = z;
        downloadRequest.mForceDeactivateSim = z2;
        downloadRequest.mResolvedBundle = bundle;
        downloadRequest.mCallback = downloadCommandCallback;
        downloadRequest.mPortIndex = i2;
        sendMessage(102, i, 0, downloadRequest);
    }

    void getEuiccProfileInfoList(int i, GetEuiccProfileInfoListCommandCallback getEuiccProfileInfoListCommandCallback) {
        sendMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING, i, 0, getEuiccProfileInfoListCommandCallback);
    }

    public void getDefaultDownloadableSubscriptionList(int i, boolean z, GetDefaultListCommandCallback getDefaultListCommandCallback) {
        GetDefaultListRequest getDefaultListRequest = new GetDefaultListRequest();
        getDefaultListRequest.mForceDeactivateSim = z;
        getDefaultListRequest.mCallback = getDefaultListCommandCallback;
        sendMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE, i, 0, getDefaultListRequest);
    }

    public void getEuiccInfo(int i, GetEuiccInfoCommandCallback getEuiccInfoCommandCallback) {
        sendMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION, i, 0, getEuiccInfoCommandCallback);
    }

    public void deleteSubscription(int i, String str, DeleteCommandCallback deleteCommandCallback) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.mIccid = str;
        deleteRequest.mCallback = deleteCommandCallback;
        sendMessage(106, i, 0, deleteRequest);
    }

    public void switchToSubscription(int i, int i2, String str, boolean z, SwitchCommandCallback switchCommandCallback, boolean z2) {
        SwitchRequest switchRequest = new SwitchRequest();
        switchRequest.mIccid = str;
        switchRequest.mForceDeactivateSim = z;
        switchRequest.mCallback = switchCommandCallback;
        switchRequest.mUsePortIndex = z2;
        sendMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS, i, i2, switchRequest);
    }

    public void updateSubscriptionNickname(int i, String str, String str2, UpdateNicknameCommandCallback updateNicknameCommandCallback) {
        UpdateNicknameRequest updateNicknameRequest = new UpdateNicknameRequest();
        updateNicknameRequest.mIccid = str;
        updateNicknameRequest.mNickname = str2;
        updateNicknameRequest.mCallback = updateNicknameCommandCallback;
        sendMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH, i, 0, updateNicknameRequest);
    }

    public void eraseSubscriptions(int i, EraseCommandCallback eraseCommandCallback) {
        sendMessage(109, i, 0, eraseCommandCallback);
    }

    public void eraseSubscriptionsWithOptions(int i, int i2, EraseCommandCallback eraseCommandCallback) {
        sendMessage(113, i, i2, eraseCommandCallback);
    }

    public void retainSubscriptions(int i, RetainSubscriptionsCommandCallback retainSubscriptionsCommandCallback) {
        sendMessage(NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE, i, 0, retainSubscriptionsCommandCallback);
    }

    public void dumpEuiccService(DumpEuiccServiceCommandCallback dumpEuiccServiceCommandCallback) {
        sendMessage(114, -1, 0, dumpEuiccServiceCommandCallback);
    }

    public final IEuiccService getBinder() {
        return this.mEuiccService;
    }

    private class UnavailableState extends State {
        private UnavailableState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                euiccConnector.mSelectedComponent = euiccConnector.findBestComponent();
                ServiceInfo serviceInfo = EuiccConnector.this.mSelectedComponent;
                EuiccConnector euiccConnector2 = EuiccConnector.this;
                if (serviceInfo != null) {
                    euiccConnector2.transitionTo(euiccConnector2.mAvailableState);
                    EuiccConnector.this.updateSubscriptionInfoListForAllAccessibleEuiccs();
                } else {
                    IState currentState = euiccConnector2.getCurrentState();
                    EuiccConnector euiccConnector3 = EuiccConnector.this;
                    UnavailableState unavailableState = euiccConnector3.mUnavailableState;
                    if (currentState != unavailableState) {
                        euiccConnector3.transitionTo(unavailableState);
                    }
                }
                return true;
            }
            if (!EuiccConnector.isEuiccCommand(i)) {
                return false;
            }
            EuiccConnector.getCallback(message).onEuiccServiceUnavailable();
            return true;
        }
    }

    private class AvailableState extends State {
        private AvailableState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (!EuiccConnector.isEuiccCommand(message.what)) {
                return false;
            }
            EuiccConnector.this.deferMessage(message);
            EuiccConnector euiccConnector = EuiccConnector.this;
            euiccConnector.transitionTo(euiccConnector.mBindingState);
            return true;
        }
    }

    private class BindingState extends State {
        private BindingState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            boolean zCreateBinding = EuiccConnector.this.createBinding();
            EuiccConnector euiccConnector = EuiccConnector.this;
            if (zCreateBinding) {
                euiccConnector.transitionTo(euiccConnector.mDisconnectedState);
            } else {
                euiccConnector.transitionTo(euiccConnector.mAvailableState);
            }
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            EuiccConnector.this.deferMessage(message);
            return true;
        }
    }

    private class DisconnectedState extends State {
        private DisconnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            EuiccConnector.this.sendMessageDelayed(2, SatelliteSessionController.DEFAULT_SATELLITE_STAY_AT_LISTENING_FROM_RECEIVING_MILLIS);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int i = message.what;
            if (i == 4) {
                EuiccConnector.this.mEuiccService = (IEuiccService) message.obj;
                EuiccConnector euiccConnector = EuiccConnector.this;
                euiccConnector.transitionTo(euiccConnector.mConnectedState);
                return true;
            }
            boolean z = false;
            if (i != 1) {
                if (i == 2) {
                    EuiccConnector.this.unbind();
                    EuiccConnector euiccConnector2 = EuiccConnector.this;
                    euiccConnector2.transitionTo(euiccConnector2.mAvailableState);
                    return true;
                }
                if (!EuiccConnector.isEuiccCommand(i)) {
                    return false;
                }
                EuiccConnector.this.deferMessage(message);
                return true;
            }
            ServiceInfo serviceInfoFindBestComponent = EuiccConnector.this.findBestComponent();
            String str = (String) message.obj;
            EuiccConnector euiccConnector3 = EuiccConnector.this;
            boolean z2 = serviceInfoFindBestComponent != null ? euiccConnector3.mSelectedComponent == null || new ComponentName(serviceInfoFindBestComponent.packageName, serviceInfoFindBestComponent.name).equals(new ComponentName(EuiccConnector.this.mSelectedComponent.packageName, EuiccConnector.this.mSelectedComponent.name)) : euiccConnector3.mSelectedComponent != null;
            if (serviceInfoFindBestComponent != null && Objects.equals(serviceInfoFindBestComponent.packageName, str)) {
                z = true;
            }
            if (!z2 || z) {
                EuiccConnector.this.unbind();
                EuiccConnector.this.mSelectedComponent = serviceInfoFindBestComponent;
                ServiceInfo serviceInfo = EuiccConnector.this.mSelectedComponent;
                EuiccConnector euiccConnector4 = EuiccConnector.this;
                if (serviceInfo == null) {
                    euiccConnector4.transitionTo(euiccConnector4.mUnavailableState);
                } else {
                    euiccConnector4.transitionTo(euiccConnector4.mBindingState);
                }
                EuiccConnector.this.updateSubscriptionInfoListForAllAccessibleEuiccs();
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ConnectedState extends State {
        private ConnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            EuiccConnector.this.removeMessages(2);
            EuiccConnector.this.sendMessageDelayed(3, 60000L);
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            int i = message.what;
            if (i == 5) {
                EuiccSession.get(EuiccConnector.this.mContext).endAllSessions();
                EuiccConnector.this.mEuiccService = null;
                EuiccConnector euiccConnector = EuiccConnector.this;
                euiccConnector.transitionTo(euiccConnector.mDisconnectedState);
                return true;
            }
            if (i == 3) {
                EuiccSession.get(EuiccConnector.this.mContext).endAllSessions();
                EuiccConnector.this.unbind();
                EuiccConnector euiccConnector2 = EuiccConnector.this;
                euiccConnector2.transitionTo(euiccConnector2.mAvailableState);
                return true;
            }
            if (i == 6) {
                ((Runnable) message.obj).run();
                return true;
            }
            if (!EuiccConnector.isEuiccCommand(i)) {
                return false;
            }
            BaseEuiccCommandCallback callback = EuiccConnector.getCallback(message);
            EuiccConnector.this.onCommandStart(callback);
            int i2 = message.arg1;
            int slotIdFromCardId = EuiccConnector.this.getSlotIdFromCardId(i2);
            try {
                switch (message.what) {
                    case 100:
                        EuiccConnector.this.mEuiccService.getEid(slotIdFromCardId, new AnonymousClass1(callback));
                        return true;
                    case 101:
                        GetMetadataRequest getMetadataRequest = (GetMetadataRequest) message.obj;
                        EuiccConnector.this.mEuiccService.getDownloadableSubscriptionMetadata(slotIdFromCardId, getMetadataRequest.mPortIndex, getMetadataRequest.mSubscription, getMetadataRequest.mSwitchAfterDownload, getMetadataRequest.mForceDeactivateSim, new AnonymousClass3(callback, i2));
                        return true;
                    case 102:
                        DownloadRequest downloadRequest = (DownloadRequest) message.obj;
                        EuiccSession.get(EuiccConnector.this.mContext).startSession(EuiccSession.DOWNLOAD);
                        EuiccConnector.this.mEuiccService.downloadSubscription(slotIdFromCardId, downloadRequest.mPortIndex, downloadRequest.mSubscription, downloadRequest.mSwitchAfterDownload, downloadRequest.mForceDeactivateSim, downloadRequest.mResolvedBundle, new AnonymousClass4(callback));
                        return true;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
                        EuiccConnector.this.mEuiccService.getEuiccProfileInfoList(slotIdFromCardId, new AnonymousClass5(callback));
                        return true;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                        EuiccConnector.this.mEuiccService.getDefaultDownloadableSubscriptionList(slotIdFromCardId, ((GetDefaultListRequest) message.obj).mForceDeactivateSim, new AnonymousClass6(callback, i2));
                        return true;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION /* 105 */:
                        EuiccConnector.this.mEuiccService.getEuiccInfo(slotIdFromCardId, new AnonymousClass7(callback));
                        return true;
                    case 106:
                        EuiccConnector.this.mEuiccService.deleteSubscription(slotIdFromCardId, ((DeleteRequest) message.obj).mIccid, new AnonymousClass8(callback));
                        return true;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS /* 107 */:
                        SwitchRequest switchRequest = (SwitchRequest) message.obj;
                        EuiccConnector.this.mEuiccService.switchToSubscription(slotIdFromCardId, message.arg2, switchRequest.mIccid, switchRequest.mForceDeactivateSim, new AnonymousClass9(callback), switchRequest.mUsePortIndex);
                        return true;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH /* 108 */:
                        UpdateNicknameRequest updateNicknameRequest = (UpdateNicknameRequest) message.obj;
                        EuiccConnector.this.mEuiccService.updateSubscriptionNickname(slotIdFromCardId, updateNicknameRequest.mIccid, updateNicknameRequest.mNickname, new AnonymousClass10(callback));
                        return true;
                    case 109:
                        EuiccConnector.this.mEuiccService.eraseSubscriptions(slotIdFromCardId, new AnonymousClass11(callback));
                        return true;
                    case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE /* 110 */:
                        EuiccConnector.this.mEuiccService.retainSubscriptionsForFactoryReset(slotIdFromCardId, new AnonymousClass13(callback));
                        return true;
                    case 111:
                        EuiccConnector.this.mEuiccService.getOtaStatus(slotIdFromCardId, new AnonymousClass14(callback));
                        return true;
                    case 112:
                        EuiccConnector.this.mEuiccService.startOtaIfNecessary(slotIdFromCardId, new AnonymousClass15(callback));
                        return true;
                    case 113:
                        EuiccConnector.this.mEuiccService.eraseSubscriptionsWithOptions(slotIdFromCardId, message.arg2, new AnonymousClass12(callback));
                        return true;
                    case 114:
                        EuiccConnector.this.mEuiccService.dump(new AnonymousClass16(callback));
                        return true;
                    case 115:
                        EuiccConnector.this.mEuiccService.getAvailableMemoryInBytes(slotIdFromCardId, new AnonymousClass2(callback));
                        return true;
                    default:
                        Log.wtf("EuiccConnector", "Unimplemented eUICC command: " + message.what);
                        callback.onEuiccServiceUnavailable();
                        EuiccConnector.this.onCommandEnd(callback);
                        return true;
                }
            } catch (Exception e) {
                Log.w("EuiccConnector", "Exception making binder call to EuiccService", e);
                callback.onEuiccServiceUnavailable();
                EuiccConnector.this.onCommandEnd(callback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$1, reason: invalid class name */
        class AnonymousClass1 extends IGetEidCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass1(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onSuccess(final String str) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onSuccess$0(baseEuiccCommandCallback, str);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onSuccess$0(BaseEuiccCommandCallback baseEuiccCommandCallback, String str) {
                ((GetEidCommandCallback) baseEuiccCommandCallback).onGetEidComplete(str);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$2, reason: invalid class name */
        class AnonymousClass2 extends IGetAvailableMemoryInBytesCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass2(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onSuccess(final long j) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$2$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onSuccess$0(baseEuiccCommandCallback, j);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onSuccess$0(BaseEuiccCommandCallback baseEuiccCommandCallback, long j) {
                ((GetAvailableMemoryInBytesCommandCallback) baseEuiccCommandCallback).onGetAvailableMemoryInBytesComplete(j);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }

            public void onUnsupportedOperationException(final String str) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onUnsupportedOperationException$1(baseEuiccCommandCallback, str);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onUnsupportedOperationException$1(BaseEuiccCommandCallback baseEuiccCommandCallback, String str) {
                ((GetAvailableMemoryInBytesCommandCallback) baseEuiccCommandCallback).onUnsupportedOperationExceptionComplete(str);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$3, reason: invalid class name */
        class AnonymousClass3 extends IGetDownloadableSubscriptionMetadataCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;
            final /* synthetic */ int val$cardId;

            AnonymousClass3(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                this.val$callback = baseEuiccCommandCallback;
                this.val$cardId = i;
            }

            public void onComplete(final GetDownloadableSubscriptionMetadataResult getDownloadableSubscriptionMetadataResult) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                final int i = this.val$cardId;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, i, getDownloadableSubscriptionMetadataResult);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i, GetDownloadableSubscriptionMetadataResult getDownloadableSubscriptionMetadataResult) {
                ((GetMetadataCommandCallback) baseEuiccCommandCallback).onGetMetadataComplete(i, getDownloadableSubscriptionMetadataResult);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$4, reason: invalid class name */
        class AnonymousClass4 extends IDownloadSubscriptionCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass4(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final DownloadSubscriptionResult downloadSubscriptionResult) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, downloadSubscriptionResult);
                    }
                });
                EuiccSession.get(EuiccConnector.this.mContext).endSession(EuiccSession.DOWNLOAD);
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, DownloadSubscriptionResult downloadSubscriptionResult) {
                ((DownloadCommandCallback) baseEuiccCommandCallback).onDownloadComplete(downloadSubscriptionResult);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$5, reason: invalid class name */
        class AnonymousClass5 extends IGetEuiccProfileInfoListCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass5(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final GetEuiccProfileInfoListResult getEuiccProfileInfoListResult) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$5$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, getEuiccProfileInfoListResult);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, GetEuiccProfileInfoListResult getEuiccProfileInfoListResult) {
                ((GetEuiccProfileInfoListCommandCallback) baseEuiccCommandCallback).onListComplete(getEuiccProfileInfoListResult);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$6, reason: invalid class name */
        class AnonymousClass6 extends IGetDefaultDownloadableSubscriptionListCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;
            final /* synthetic */ int val$cardId;

            AnonymousClass6(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                this.val$callback = baseEuiccCommandCallback;
                this.val$cardId = i;
            }

            public void onComplete(final GetDefaultDownloadableSubscriptionListResult getDefaultDownloadableSubscriptionListResult) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                final int i = this.val$cardId;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$6$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, i, getDefaultDownloadableSubscriptionListResult);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i, GetDefaultDownloadableSubscriptionListResult getDefaultDownloadableSubscriptionListResult) {
                ((GetDefaultListCommandCallback) baseEuiccCommandCallback).onGetDefaultListComplete(i, getDefaultDownloadableSubscriptionListResult);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$7, reason: invalid class name */
        class AnonymousClass7 extends IGetEuiccInfoCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass7(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onSuccess(final EuiccInfo euiccInfo) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$7$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onSuccess$0(baseEuiccCommandCallback, euiccInfo);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onSuccess$0(BaseEuiccCommandCallback baseEuiccCommandCallback, EuiccInfo euiccInfo) {
                ((GetEuiccInfoCommandCallback) baseEuiccCommandCallback).onGetEuiccInfoComplete(euiccInfo);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$8, reason: invalid class name */
        class AnonymousClass8 extends IDeleteSubscriptionCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass8(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final int i) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$8$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                ((DeleteCommandCallback) baseEuiccCommandCallback).onDeleteComplete(i);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$9, reason: invalid class name */
        class AnonymousClass9 extends ISwitchToSubscriptionCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass9(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final int i) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$9$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                ((SwitchCommandCallback) baseEuiccCommandCallback).onSwitchComplete(i);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$10, reason: invalid class name */
        class AnonymousClass10 extends IUpdateSubscriptionNicknameCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass10(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final int i) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$10$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                ((UpdateNicknameCommandCallback) baseEuiccCommandCallback).onUpdateNicknameComplete(i);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$11, reason: invalid class name */
        class AnonymousClass11 extends IEraseSubscriptionsCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass11(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final int i) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$11$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                ((EraseCommandCallback) baseEuiccCommandCallback).onEraseComplete(i);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$12, reason: invalid class name */
        class AnonymousClass12 extends IEraseSubscriptionsCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass12(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final int i) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$12$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                ((EraseCommandCallback) baseEuiccCommandCallback).onEraseComplete(i);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$13, reason: invalid class name */
        class AnonymousClass13 extends IRetainSubscriptionsForFactoryResetCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass13(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final int i) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$13$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                ((RetainSubscriptionsCommandCallback) baseEuiccCommandCallback).onRetainSubscriptionsComplete(i);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$14, reason: invalid class name */
        class AnonymousClass14 extends IGetOtaStatusCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass14(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onSuccess(final int i) {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$14$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onSuccess$0(baseEuiccCommandCallback, i);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onSuccess$0(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                ((GetOtaStatusCommandCallback) baseEuiccCommandCallback).onGetOtaStatusComplete(i);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$15, reason: invalid class name */
        class AnonymousClass15 extends IOtaStatusChangedCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass15(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onOtaStatusChanged(final int i) throws RemoteException {
                ConnectedState connectedState = ConnectedState.this;
                if (i == 1) {
                    EuiccConnector euiccConnector = EuiccConnector.this;
                    final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                    euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$15$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            ((EuiccConnector.OtaStatusChangedCallback) baseEuiccCommandCallback).onOtaStatusChanged(i);
                        }
                    });
                } else {
                    EuiccConnector euiccConnector2 = EuiccConnector.this;
                    final BaseEuiccCommandCallback baseEuiccCommandCallback2 = this.val$callback;
                    euiccConnector2.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$15$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onOtaStatusChanged$1(baseEuiccCommandCallback2, i);
                        }
                    });
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onOtaStatusChanged$1(BaseEuiccCommandCallback baseEuiccCommandCallback, int i) {
                ((OtaStatusChangedCallback) baseEuiccCommandCallback).onOtaStatusChanged(i);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        /* JADX INFO: renamed from: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$16, reason: invalid class name */
        class AnonymousClass16 extends IEuiccServiceDumpResultCallback.Stub {
            final /* synthetic */ BaseEuiccCommandCallback val$callback;

            AnonymousClass16(BaseEuiccCommandCallback baseEuiccCommandCallback) {
                this.val$callback = baseEuiccCommandCallback;
            }

            public void onComplete(final String str) throws RemoteException {
                EuiccConnector euiccConnector = EuiccConnector.this;
                final BaseEuiccCommandCallback baseEuiccCommandCallback = this.val$callback;
                euiccConnector.sendMessage(6, new Runnable() { // from class: com.android.internal.telephony.euicc.EuiccConnector$ConnectedState$16$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onComplete$0(baseEuiccCommandCallback, str);
                    }
                });
            }

            /* JADX INFO: Access modifiers changed from: private */
            public /* synthetic */ void lambda$onComplete$0(BaseEuiccCommandCallback baseEuiccCommandCallback, String str) {
                ((DumpEuiccServiceCommandCallback) baseEuiccCommandCallback).onDumpEuiccServiceComplete(str);
                EuiccConnector.this.onCommandEnd(baseEuiccCommandCallback);
            }
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            EuiccConnector.this.removeMessages(3);
            Iterator it = EuiccConnector.this.mActiveCommandCallbacks.iterator();
            while (it.hasNext()) {
                ((BaseEuiccCommandCallback) it.next()).onEuiccServiceUnavailable();
            }
            EuiccConnector.this.mActiveCommandCallbacks.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static BaseEuiccCommandCallback getCallback(Message message) {
        switch (message.what) {
            case 100:
            case NtnRILConstants.MI_NTN_RIL_REQUEST_REPORT_STK_SERVICE_IS_RUNNING /* 103 */:
            case NtnRILConstants.MI_NTN_RIL_REQUEST_ISIM_AUTHENTICATION /* 105 */:
            case 109:
            case NtnRILConstants.MI_NTN_RIL_REQUEST_SET_UNSOL_CELL_INFO_LIST_RATE /* 110 */:
            case 111:
            case 112:
            case 113:
            case 114:
            case 115:
                return (BaseEuiccCommandCallback) message.obj;
            case 101:
                return ((GetMetadataRequest) message.obj).mCallback;
            case 102:
                return ((DownloadRequest) message.obj).mCallback;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_CDMA_GET_SUBSCRIPTION_SOURCE /* 104 */:
                return ((GetDefaultListRequest) message.obj).mCallback;
            case 106:
                return ((DeleteRequest) message.obj).mCallback;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_STK_SEND_ENVELOPE_WITH_STATUS /* 107 */:
                return ((SwitchRequest) message.obj).mCallback;
            case NtnRILConstants.MI_NTN_RIL_REQUEST_VOICE_RADIO_TECH /* 108 */:
                return ((UpdateNicknameRequest) message.obj).mCallback;
            default:
                EuiccConnector$$ExternalSyntheticBUOutline0.m("Unsupported message: ", message.what);
                return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSlotIdFromCardId(int i) {
        if (i != -1 && i != -2) {
            UiccSlotInfo[] uiccSlotsInfo = ((TelephonyManager) this.mContext.getSystemService("phone")).getUiccSlotsInfo();
            if (uiccSlotsInfo == null || uiccSlotsInfo.length == 0) {
                Log.e("EuiccConnector", "UiccSlotInfo is null or empty");
            } else {
                String strConvertToCardString = UiccController.getInstance().convertToCardString(i);
                for (int i2 = 0; i2 < uiccSlotsInfo.length; i2++) {
                    UiccSlotInfo uiccSlotInfo = uiccSlotsInfo[i2];
                    if (uiccSlotInfo == null) {
                        Log.i("EuiccConnector", "No UiccSlotInfo found for slotIndex: " + i2);
                        return -1;
                    }
                    if (IccUtils.compareIgnoreTrailingFs(strConvertToCardString, uiccSlotInfo != null ? uiccSlotInfo.getCardId() : null)) {
                        return i2;
                    }
                }
                Log.i("EuiccConnector", "No UiccSlotInfo found for cardId: " + i);
                return -1;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCommandStart(BaseEuiccCommandCallback baseEuiccCommandCallback) {
        this.mActiveCommandCallbacks.add(baseEuiccCommandCallback);
        removeMessages(3);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCommandEnd(BaseEuiccCommandCallback baseEuiccCommandCallback) {
        if (!this.mActiveCommandCallbacks.remove(baseEuiccCommandCallback)) {
            Log.wtf("EuiccConnector", "Callback already removed from mActiveCommandCallbacks");
        }
        if (this.mActiveCommandCallbacks.isEmpty()) {
            sendMessageDelayed(3, 60000L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ServiceInfo findBestComponent() {
        return (ServiceInfo) findBestComponent(this.mPm);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean createBinding() {
        if (this.mSelectedComponent == null) {
            Log.wtf("EuiccConnector", "Attempting to create binding but no component is selected");
            return false;
        }
        Intent intent = new Intent("android.service.euicc.EuiccService");
        ServiceInfo serviceInfo = this.mSelectedComponent;
        intent.setComponent(new ComponentName(serviceInfo.packageName, serviceInfo.name));
        return this.mContext.bindService(intent, this, 67108865);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbind() {
        this.mEuiccService = null;
        this.mContext.unbindService(this);
    }

    private static ComponentInfo findBestComponent(PackageManager packageManager, List list) {
        ComponentInfo componentInfo = null;
        if (list != null) {
            Iterator it = list.iterator();
            int priority = NetworkStackConstants.NEIGHBOR_ADVERTISEMENT_FLAG_ROUTER;
            while (it.hasNext()) {
                ResolveInfo resolveInfo = (ResolveInfo) it.next();
                if (isValidEuiccComponent(packageManager, resolveInfo) && resolveInfo.filter.getPriority() > priority) {
                    priority = resolveInfo.filter.getPriority();
                    componentInfo = TelephonyUtils.getComponentInfo(resolveInfo);
                }
            }
        }
        return componentInfo;
    }

    private static boolean isValidEuiccComponent(PackageManager packageManager, ResolveInfo resolveInfo) {
        String str;
        ComponentInfo componentInfo = TelephonyUtils.getComponentInfo(resolveInfo);
        String packageName = new ComponentName(componentInfo.packageName, componentInfo.name).getPackageName();
        if (packageManager.checkPermission("android.permission.WRITE_EMBEDDED_SUBSCRIPTIONS", packageName) != 0) {
            Log.wtf("EuiccConnector", "Package " + packageName + " does not declare WRITE_EMBEDDED_SUBSCRIPTIONS");
            return false;
        }
        if (componentInfo instanceof ServiceInfo) {
            str = ((ServiceInfo) componentInfo).permission;
        } else if (componentInfo instanceof ActivityInfo) {
            str = ((ActivityInfo) componentInfo).permission;
        } else {
            Phone$$ExternalSyntheticBUOutline0.m("Can only verify services/activities");
            return false;
        }
        if (!TextUtils.equals(str, "android.permission.BIND_EUICC_SERVICE")) {
            Log.wtf("EuiccConnector", "Package " + packageName + " does not require the BIND_EUICC_SERVICE permission");
            return false;
        }
        IntentFilter intentFilter = resolveInfo.filter;
        if (intentFilter != null && intentFilter.getPriority() != 0) {
            return true;
        }
        Log.wtf("EuiccConnector", "Package " + packageName + " does not specify a priority");
        return false;
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        sendMessage(4, IEuiccService.Stub.asInterface(iBinder));
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        sendMessage(5);
    }

    private class EuiccPackageMonitor extends PackageChangeReceiver {
        private EuiccPackageMonitor() {
        }

        public void onPackageAdded(String str) {
            sendPackageChange(str, true);
        }

        public void onPackageRemoved(String str) {
            sendPackageChange(str, true);
        }

        public void onPackageUpdateFinished(String str) {
            sendPackageChange(str, true);
        }

        public void onPackageModified(String str) {
            sendPackageChange(str, false);
        }

        public void onHandleForceStop(String[] strArr, boolean z) {
            if (z) {
                for (String str : strArr) {
                    sendPackageChange(str, true);
                }
            }
        }

        private void sendPackageChange(String str, boolean z) {
            EuiccConnector euiccConnector = EuiccConnector.this;
            if (!z) {
                str = null;
            }
            euiccConnector.sendMessage(1, str);
        }
    }

    @Override // com.android.internal.telephony.StateMachine
    protected void unhandledMessage(Message message) {
        IState currentState = getCurrentState();
        StringBuilder sb = new StringBuilder("Unhandled message ");
        sb.append(message.what);
        sb.append(" in state ");
        sb.append(currentState == null ? "null" : currentState.getName());
        Log.wtf("EuiccConnector", sb.toString());
        AnomalyReporter.reportAnomaly(UUID.fromString("0db20514-5fa1-4e62-a7b7-2acf5f92c957"), "EuiccConnector: Found unhandledMessage " + String.valueOf(message.what));
    }

    @Override // com.android.internal.telephony.StateMachine
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.println("mSelectedComponent=" + this.mSelectedComponent);
        printWriter.println("mEuiccService=" + this.mEuiccService);
        printWriter.println("mActiveCommandCount=" + this.mActiveCommandCallbacks.size());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSubscriptionInfoListForAllAccessibleEuiccs() {
        if (this.mTm.getCardIdForDefaultEuicc() == -1) {
            this.mSm.requestEmbeddedSubscriptionInfoListRefresh();
            return;
        }
        for (UiccCardInfo uiccCardInfo : this.mTm.getUiccCardsInfo()) {
            if (uiccCardInfo.isEuicc()) {
                this.mSm.requestEmbeddedSubscriptionInfoListRefresh(uiccCardInfo.getCardId());
            }
        }
    }
}
