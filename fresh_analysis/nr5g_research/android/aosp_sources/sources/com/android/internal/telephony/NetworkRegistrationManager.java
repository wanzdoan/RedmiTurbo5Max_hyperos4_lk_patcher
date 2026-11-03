package com.android.internal.telephony;

import android.R;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.AccessNetworkConstants;
import android.telephony.CarrierConfigManager;
import android.telephony.INetworkService;
import android.telephony.INetworkServiceCallback;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import com.android.telephony.Rlog;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class NetworkRegistrationManager extends Handler {
    private RegManagerDeathRecipient mDeathRecipient;
    private INetworkService mINetworkService;
    private final Phone mPhone;
    private NetworkServiceConnection mServiceConnection;
    private final String mTag;
    private String mTargetBindingPackageName;
    private final int mTransportType;
    private final RegistrantList mRegStateChangeRegistrants = new RegistrantList();
    private final Map mCallbackTable = new Hashtable();

    public NetworkRegistrationManager(int i, final Phone phone) {
        this.mTransportType = i;
        this.mPhone = phone;
        StringBuilder sb = new StringBuilder("-");
        sb.append(i == 1 ? "C" : "I");
        sb.append("-");
        sb.append(phone.getPhoneId());
        this.mTag = "NRM".concat(sb.toString());
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            carrierConfigManager.registerCarrierConfigChangeListener(new Executor() { // from class: com.android.internal.telephony.NetworkRegistrationManager$$ExternalSyntheticLambda0
                @Override // java.util.concurrent.Executor
                public final void execute(Runnable runnable) {
                    this.f$0.post(runnable);
                }
            }, new CarrierConfigManager.CarrierConfigChangeListener() { // from class: com.android.internal.telephony.NetworkRegistrationManager$$ExternalSyntheticLambda1
                @Override // android.telephony.CarrierConfigManager.CarrierConfigChangeListener
                public final void onCarrierConfigChanged(int i2, int i3, int i4, int i5) {
                    this.f$0.lambda$new$0(phone, i2, i3, i4, i5);
                }
            });
        }
        PhoneConfigurationManager.registerForMultiSimConfigChange(this, 1, null);
        sendEmptyMessage(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Phone phone, int i, int i2, int i3, int i4) {
        if (i == phone.getPhoneId()) {
            logd("Carrier config changed. Try to bind network service.");
            rebindService();
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == 1) {
            rebindService();
            return;
        }
        loge("Unhandled event " + message.what);
    }

    public boolean isServiceConnected() {
        INetworkService iNetworkService = this.mINetworkService;
        return iNetworkService != null && iNetworkService.asBinder().isBinderAlive();
    }

    public void unregisterForNetworkRegistrationInfoChanged(Handler handler) {
        this.mRegStateChangeRegistrants.remove(handler);
    }

    public void registerForNetworkRegistrationInfoChanged(Handler handler, int i, Object obj) {
        logd("registerForNetworkRegistrationInfoChanged");
        this.mRegStateChangeRegistrants.addUnique(handler, i, obj);
    }

    public void requestNetworkRegistrationInfo(int i, Message message) {
        if (message == null) {
            return;
        }
        if (!isServiceConnected()) {
            loge("service not connected. Domain = ".concat(i == 1 ? "CS" : "PS"));
            message.obj = new AsyncResult(message.obj, (Object) null, new IllegalStateException("Service not connected."));
            message.sendToTarget();
            return;
        }
        NetworkRegStateCallback networkRegStateCallback = new NetworkRegStateCallback();
        try {
            this.mCallbackTable.put(networkRegStateCallback, message);
            this.mINetworkService.requestNetworkRegistrationInfo(this.mPhone.getPhoneId(), i, networkRegStateCallback);
        } catch (RemoteException e) {
            loge("requestNetworkRegistrationInfo RemoteException " + e);
            this.mCallbackTable.remove(networkRegStateCallback);
            message.obj = new AsyncResult(message.obj, (Object) null, e);
            message.sendToTarget();
        }
    }

    private class RegManagerDeathRecipient implements IBinder.DeathRecipient {
        private final ComponentName mComponentName;

        RegManagerDeathRecipient(ComponentName componentName) {
            this.mComponentName = componentName;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            NetworkRegistrationManager.this.logd("Network service " + this.mComponentName + " for transport type " + AccessNetworkConstants.transportTypeToString(NetworkRegistrationManager.this.mTransportType) + " died.");
        }
    }

    private class NetworkServiceConnection implements ServiceConnection {
        private NetworkServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            NetworkRegistrationManager.this.logd("service " + componentName + " for transport " + AccessNetworkConstants.transportTypeToString(NetworkRegistrationManager.this.mTransportType) + " is now connected.");
            NetworkRegistrationManager.this.mINetworkService = INetworkService.Stub.asInterface(iBinder);
            NetworkRegistrationManager networkRegistrationManager = NetworkRegistrationManager.this;
            networkRegistrationManager.mDeathRecipient = networkRegistrationManager.new RegManagerDeathRecipient(componentName);
            try {
                iBinder.linkToDeath(NetworkRegistrationManager.this.mDeathRecipient, 0);
                NetworkRegistrationManager.this.mINetworkService.createNetworkServiceProvider(NetworkRegistrationManager.this.mPhone.getPhoneId());
                NetworkRegistrationManager.this.mINetworkService.registerForNetworkRegistrationInfoChanged(NetworkRegistrationManager.this.mPhone.getPhoneId(), new NetworkRegStateCallback());
            } catch (RemoteException e) {
                NetworkRegistrationManager.this.logd("RemoteException " + e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            NetworkRegistrationManager.this.logd("service " + componentName + " for transport " + AccessNetworkConstants.transportTypeToString(NetworkRegistrationManager.this.mTransportType) + " is now disconnected.");
            NetworkRegistrationManager.this.mTargetBindingPackageName = null;
        }
    }

    private class NetworkRegStateCallback extends INetworkServiceCallback.Stub {
        private NetworkRegStateCallback() {
        }

        public void onRequestNetworkRegistrationInfoComplete(int i, NetworkRegistrationInfo networkRegistrationInfo) {
            NetworkRegistrationManager.this.logd("onRequestNetworkRegistrationInfoComplete result: " + i + ", info: " + networkRegistrationInfo);
            Message message = (Message) NetworkRegistrationManager.this.mCallbackTable.remove(this);
            if (message != null) {
                message.arg1 = i;
                message.obj = new AsyncResult(message.obj, new NetworkRegistrationInfo(networkRegistrationInfo), (Throwable) null);
                message.sendToTarget();
                return;
            }
            NetworkRegistrationManager.this.loge("onCompleteMessage is null");
        }

        public void onNetworkStateChanged() {
            NetworkRegistrationManager.this.logd("onNetworkStateChanged");
            NetworkRegistrationManager.this.mRegStateChangeRegistrants.notifyRegistrants();
        }
    }

    private void unbindService() {
        INetworkService iNetworkService = this.mINetworkService;
        if (iNetworkService != null && iNetworkService.asBinder().isBinderAlive()) {
            logd("unbinding service");
            try {
                this.mINetworkService.removeNetworkServiceProvider(this.mPhone.getPhoneId());
            } catch (RemoteException e) {
                loge("Cannot remove data service provider. " + e);
            }
        }
        if (this.mServiceConnection != null) {
            this.mPhone.getContext().unbindService(this.mServiceConnection);
        }
        this.mINetworkService = null;
        this.mServiceConnection = null;
        this.mTargetBindingPackageName = null;
    }

    private void bindService(String str) {
        Intent component;
        Phone phone = this.mPhone;
        if (phone == null || !SubscriptionManager.isValidPhoneId(phone.getPhoneId())) {
            loge("can't bindService with invalid phone or phoneId.");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            loge("Can't find the binding package");
            return;
        }
        String className = getClassName();
        if (TextUtils.isEmpty(className)) {
            component = new Intent("android.telephony.NetworkService");
            component.setPackage(str);
        } else {
            component = new Intent("android.telephony.NetworkService").setComponent(new ComponentName(str, className));
        }
        try {
            logd("Trying to bind " + getPackageName() + " for transport " + AccessNetworkConstants.transportTypeToString(this.mTransportType));
            this.mServiceConnection = new NetworkServiceConnection();
            if (!this.mPhone.getContext().bindService(component, this.mServiceConnection, 1)) {
                loge("Cannot bind to the data service.");
            } else {
                this.mTargetBindingPackageName = str;
            }
        } catch (SecurityException e) {
            loge("bindService failed " + e);
        }
    }

    private void rebindService() {
        String packageName = getPackageName();
        if (SubscriptionManager.isValidPhoneId(this.mPhone.getPhoneId()) && TextUtils.equals(packageName, this.mTargetBindingPackageName)) {
            logd("Service " + packageName + " already bound or being bound.");
            return;
        }
        unbindService();
        bindService(packageName);
    }

    private String getPackageName() {
        int i;
        String str;
        int i2 = this.mTransportType;
        if (i2 == 1) {
            i = R.string.eventTypeCustom;
            str = "carrier_network_service_wwan_package_override_string";
        } else if (i2 == 2) {
            i = R.string.etws_primary_default_message_others;
            str = "carrier_network_service_wlan_package_override_string";
        } else {
            LocaleTracker$$ExternalSyntheticBUOutline0.m("Transport type not WWAN or WLAN. type=", this.mTransportType);
            return null;
        }
        String string = this.mPhone.getContext().getResources().getString(i);
        PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mPhone.getContext(), this.mPhone.getSubId(), new String[]{str});
        return (carrierConfigSubset.isEmpty() || TextUtils.isEmpty(carrierConfigSubset.getString(str))) ? string : carrierConfigSubset.getString(str, string);
    }

    private String getClassName() {
        int i;
        String str;
        int i2 = this.mTransportType;
        if (i2 == 1) {
            i = R.string.eventTypeBirthday;
            str = "carrier_network_service_wwan_class_override_string";
        } else if (i2 == 2) {
            i = R.string.etws_primary_default_message_earthquake_and_tsunami;
            str = "carrier_network_service_wlan_class_override_string";
        } else {
            LocaleTracker$$ExternalSyntheticBUOutline0.m("Transport type not WWAN or WLAN. type=", this.mTransportType);
            return null;
        }
        String string = this.mPhone.getContext().getResources().getString(i);
        PersistableBundle carrierConfigSubset = CarrierConfigManager.getCarrierConfigSubset(this.mPhone.getContext(), this.mPhone.getSubId(), new String[]{str});
        return (carrierConfigSubset.isEmpty() || TextUtils.isEmpty(carrierConfigSubset.getString(str))) ? string : carrierConfigSubset.getString(str, string);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logd(String str) {
        Rlog.d(this.mTag, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loge(String str) {
        Rlog.e(this.mTag, str);
    }
}
