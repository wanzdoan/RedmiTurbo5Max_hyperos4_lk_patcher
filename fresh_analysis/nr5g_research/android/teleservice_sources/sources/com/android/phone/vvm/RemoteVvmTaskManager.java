package com.android.phone.vvm;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ComponentInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.CarrierConfigManager;
import android.telephony.VisualVoicemailSms;
import android.text.TextUtils;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.phone.Assert;
import com.android.phone.R;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
public class RemoteVvmTaskManager extends Service {
    private RemoteServiceConnection mConnection;
    private Messenger mMessenger;
    private int mTaskReferenceCount;

    /* JADX INFO: Access modifiers changed from: private */
    public int getTaskId() {
        return 1;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    static void startCellServiceConnected(Context context, PhoneAccountHandle phoneAccountHandle) {
        Intent intent = new Intent("ACTION_START_CELL_SERVICE_CONNECTED", null, context, RemoteVvmTaskManager.class);
        intent.putExtra("data_phone_account_handle", phoneAccountHandle);
        context.startService(intent);
    }

    static void startSmsReceived(Context context, VisualVoicemailSms visualVoicemailSms, String str) {
        Intent intent = new Intent("ACTION_START_SMS_RECEIVED", null, context, RemoteVvmTaskManager.class);
        intent.putExtra("data_phone_account_handle", visualVoicemailSms.getPhoneAccountHandle());
        intent.putExtra("data_sms", visualVoicemailSms);
        intent.putExtra("target_package", str);
        context.startService(intent);
    }

    static void startSimRemoved(Context context, PhoneAccountHandle phoneAccountHandle) {
        Intent intent = new Intent("ACTION_START_SIM_REMOVED", null, context, RemoteVvmTaskManager.class);
        intent.putExtra("data_phone_account_handle", phoneAccountHandle);
        context.startService(intent);
    }

    static boolean hasRemoteService(Context context, int i, String str) {
        return getRemotePackage(context, i, str) != null;
    }

    public static ComponentName getRemotePackage(Context context, int i) {
        return getRemotePackage(context, i, null);
    }

    public static ComponentName getRemotePackage(Context context, int i, String str) {
        ComponentName broadcastPackage = getBroadcastPackage(context);
        if (broadcastPackage != null) {
            return broadcastPackage;
        }
        Intent intentNewBindIntent = newBindIntent(context);
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(TelecomManager.class);
        ArrayList arrayList = new ArrayList();
        arrayList.add(telecomManager.getDefaultDialerPackage());
        PersistableBundle configForSubId = ((CarrierConfigManager) context.getSystemService(CarrierConfigManager.class)).getConfigForSubId(i);
        arrayList.add(configForSubId.getString("carrier_vvm_package_name_string"));
        String[] stringArray = configForSubId.getStringArray("carrier_vvm_package_name_string_array");
        if (stringArray != null && stringArray.length > 0) {
            for (String str2 : stringArray) {
                arrayList.add(str2);
            }
        }
        arrayList.add(context.getResources().getString(R.string.system_visual_voicemail_client));
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            String str3 = (String) obj;
            if (!TextUtils.isEmpty(str3)) {
                intentNewBindIntent.setPackage(str3);
                ResolveInfo resolveInfoResolveService = context.getPackageManager().resolveService(intentNewBindIntent, 0);
                if (resolveInfoResolveService != null) {
                    ServiceInfo serviceInfo = resolveInfoResolveService.serviceInfo;
                    if (serviceInfo == null) {
                        VvmLog.w("RemoteVvmTaskManager", "getRemotePackage: Component " + TelephonyUtils.getComponentInfo(resolveInfoResolveService) + " is not a service, ignoring");
                    } else if (!"android.permission.BIND_VISUAL_VOICEMAIL_SERVICE".equals(serviceInfo.permission)) {
                        VvmLog.w("RemoteVvmTaskManager", "getRemotePackage: package " + resolveInfoResolveService.serviceInfo.packageName + " does not enforce BIND_VISUAL_VOICEMAIL_SERVICE, ignoring");
                    } else {
                        if (str == null || TextUtils.equals(str3, str)) {
                            ComponentInfo componentInfo = TelephonyUtils.getComponentInfo(resolveInfoResolveService);
                            VvmLog.i("RemoteVvmTaskManager", "getRemotePackage: found package " + str);
                            return new ComponentName(componentInfo.packageName, componentInfo.name);
                        }
                        VvmLog.w("RemoteVvmTaskManager", "getRemotePackage: target package " + str + " is no longer the active VisualVoicemailService, ignoring");
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    private static ComponentName getBroadcastPackage(Context context) {
        Intent intent = new Intent("com.android.phone.vvm.ACTION_VISUAL_VOICEMAIL_SERVICE_EVENT");
        intent.setPackage(((TelecomManager) context.getSystemService(TelecomManager.class)).getDefaultDialerPackage());
        List<ResolveInfo> listQueryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 131072);
        if (listQueryBroadcastReceivers == null || listQueryBroadcastReceivers.isEmpty()) {
            return null;
        }
        ComponentInfo componentInfo = TelephonyUtils.getComponentInfo(listQueryBroadcastReceivers.get(0));
        VvmLog.i("RemoteVvmTaskManager", "getBroadcastPackage: found package " + componentInfo.packageName);
        return new ComponentName(componentInfo.packageName, componentInfo.name);
    }

    @Override // android.app.Service
    public void onCreate() {
        Assert.isMainThread();
        this.mMessenger = new Messenger(new Handler() { // from class: com.android.phone.vvm.RemoteVvmTaskManager.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                Assert.isMainThread();
                if (message.what == 4) {
                    RemoteVvmTaskManager.this.mTaskReferenceCount--;
                    RemoteVvmTaskManager.this.checkReference();
                } else {
                    VvmLog.wtf("RemoteVvmTaskManager", "unexpected message " + message.what);
                }
            }
        });
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        Assert.isMainThread();
        this.mTaskReferenceCount++;
        if (intent == null) {
            VvmLog.i("RemoteVvmTaskManager", "onStartCommand: received intent is null");
            checkReference();
            return 2;
        }
        PhoneAccountHandle phoneAccountHandle = (PhoneAccountHandle) intent.getExtras().getParcelable("data_phone_account_handle");
        int subId = PhoneAccountHandleConverter.toSubId(phoneAccountHandle);
        UserHandle userHandle = phoneAccountHandle.getUserHandle();
        ComponentName remotePackage = getRemotePackage(this, subId, intent.getStringExtra("target_package"));
        if (remotePackage == null) {
            VvmLog.i("RemoteVvmTaskManager", "onStartCommand: No service to handle " + intent.getAction() + ", ignoring");
            checkReference();
            return 2;
        }
        String action = intent.getAction();
        action.hashCode();
        switch (action) {
            case "ACTION_START_SIM_REMOVED":
                send(remotePackage, 3, intent.getExtras(), userHandle);
                return 2;
            case "ACTION_START_SMS_RECEIVED":
                send(remotePackage, 2, intent.getExtras(), userHandle);
                return 2;
            case "ACTION_START_CELL_SERVICE_CONNECTED":
                send(remotePackage, 1, intent.getExtras(), userHandle);
                return 2;
            default:
                Assert.fail("Unexpected action +" + intent.getAction());
                return 2;
        }
    }

    class RemoteServiceConnection implements ServiceConnection {
        private boolean mConnected;
        private Messenger mRemoteMessenger;
        private final Queue mTaskQueue;

        private RemoteServiceConnection() {
            this.mTaskQueue = new LinkedList();
        }

        public void enqueue(Message message) {
            this.mTaskQueue.add(message);
            if (this.mConnected) {
                runQueue();
            }
        }

        public boolean isConnected() {
            return this.mConnected;
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            VvmLog.i("RemoteVvmTaskManager", "onServiceConnected: " + componentName);
            this.mRemoteMessenger = new Messenger(iBinder);
            this.mConnected = true;
            runQueue();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            RemoteVvmTaskManager.this.mConnection = null;
            this.mConnected = false;
            this.mRemoteMessenger = null;
            VvmLog.e("RemoteVvmTaskManager", "onServiceDisconnected: remoteService disconnected, " + RemoteVvmTaskManager.this.mTaskReferenceCount + " tasks dropped.");
            RemoteVvmTaskManager.this.mTaskReferenceCount = 0;
            RemoteVvmTaskManager.this.checkReference();
        }

        private void runQueue() {
            Assert.isMainThread();
            Message message = (Message) this.mTaskQueue.poll();
            while (message != null) {
                message.replyTo = RemoteVvmTaskManager.this.mMessenger;
                message.arg1 = RemoteVvmTaskManager.this.getTaskId();
                try {
                    this.mRemoteMessenger.send(message);
                } catch (RemoteException e) {
                    VvmLog.e("RemoteVvmTaskManager", "runQueue: Error sending message to remote service", e);
                }
                message = (Message) this.mTaskQueue.poll();
            }
        }
    }

    private void send(ComponentName componentName, int i, Bundle bundle, UserHandle userHandle) {
        Assert.isMainThread();
        if (getBroadcastPackage(this) != null) {
            VvmLog.i("RemoteVvmTaskManager", "send: sending broadcast " + i + " to " + componentName);
            Intent intent = new Intent("com.android.phone.vvm.ACTION_VISUAL_VOICEMAIL_SERVICE_EVENT");
            intent.putExtras(bundle);
            intent.putExtra("what", i);
            intent.setComponent(componentName);
            sendBroadcastAsUser(intent, userHandle);
            return;
        }
        Message messageObtain = Message.obtain();
        messageObtain.what = i;
        messageObtain.setData(new Bundle(bundle));
        if (this.mConnection == null) {
            this.mConnection = new RemoteServiceConnection();
        }
        this.mConnection.enqueue(messageObtain);
        if (this.mConnection.isConnected()) {
            return;
        }
        Intent intentNewBindIntent = newBindIntent(this);
        intentNewBindIntent.setComponent(componentName);
        VvmLog.i("RemoteVvmTaskManager", "send: Binding to " + intentNewBindIntent.getComponent());
        bindServiceAsUser(intentNewBindIntent, this.mConnection, 1, userHandle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkReference() {
        RemoteServiceConnection remoteServiceConnection = this.mConnection;
        if (remoteServiceConnection != null && this.mTaskReferenceCount == 0) {
            unbindService(remoteServiceConnection);
            this.mConnection = null;
        }
    }

    private static Intent newBindIntent(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.telephony.VisualVoicemailService");
        return intent;
    }
}
