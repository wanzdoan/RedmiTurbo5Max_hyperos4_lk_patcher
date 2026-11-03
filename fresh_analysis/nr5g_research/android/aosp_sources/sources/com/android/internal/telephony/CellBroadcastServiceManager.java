package com.android.internal.telephony;

import android.R;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.telephony.ICellBroadcastService;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.android.cellbroadcastservice.CellBroadcastStatsLog;
import com.android.internal.telephony.cdma.SmsMessage;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CellBroadcastServiceManager {
    private static CellBroadcastServiceConnection sServiceConnection;
    private String mCellBroadcastServicePackage;
    private Context mContext;
    private Phone mPhone;
    private Handler mModuleCellBroadcastHandler = null;
    private final LocalLog mLocalLog = new LocalLog(64);
    private boolean mEnabled = false;

    public CellBroadcastServiceManager(Context context, Phone phone) {
        Log.d("CellBroadcastServiceManager", "CellBroadcastServiceManager created for phone " + phone.getPhoneId());
        this.mContext = context;
        this.mPhone = phone;
    }

    private boolean cbMessagesDisabledByOem() {
        Context context = this.mContext;
        if (context == null || context.getResources() == null) {
            return false;
        }
        return this.mContext.getResources().getBoolean(R.bool.config_dontPreferApn);
    }

    public void sendGsmMessageToHandler(Message message) {
        if (cbMessagesDisabledByOem()) {
            Log.d("CellBroadcastServiceManager", "GSM CB message ignored - CB messages disabled by OEM.");
            CellBroadcastStatsLog.write(278, 1, 4);
        } else {
            message.what = 0;
            this.mModuleCellBroadcastHandler.sendMessage(message);
        }
    }

    public void sendCdmaMessageToHandler(SmsMessage smsMessage) {
        if (cbMessagesDisabledByOem()) {
            Log.d("CellBroadcastServiceManager", "CDMA CB message ignored - CB messages disabled by OEM.");
            CellBroadcastStatsLog.write(278, 2, 4);
        } else {
            Message messageObtain = Message.obtain();
            messageObtain.what = 1;
            messageObtain.obj = smsMessage;
            this.mModuleCellBroadcastHandler.sendMessage(messageObtain);
        }
    }

    public void sendCdmaScpMessageToHandler(SmsMessage smsMessage, RemoteCallback remoteCallback) {
        if (cbMessagesDisabledByOem()) {
            Log.d("CellBroadcastServiceManager", "CDMA SCP CB message ignored - CB messages disabled by OEM.");
            CellBroadcastStatsLog.write(278, 3, 4);
        } else {
            Message messageObtain = Message.obtain();
            messageObtain.what = 2;
            messageObtain.obj = Pair.create(smsMessage, remoteCallback);
            this.mModuleCellBroadcastHandler.sendMessage(messageObtain);
        }
    }

    public void enable() {
        initCellBroadcastServiceModule();
    }

    public void disable() {
        if (this.mEnabled) {
            this.mEnabled = false;
            this.mPhone.mCi.unSetOnNewGsmBroadcastSms(this.mModuleCellBroadcastHandler);
            CellBroadcastServiceConnection cellBroadcastServiceConnection = sServiceConnection;
            if (cellBroadcastServiceConnection.mService != null) {
                this.mContext.unbindService(cellBroadcastServiceConnection);
            }
        }
    }

    private void initCellBroadcastServiceModule() {
        this.mEnabled = true;
        if (sServiceConnection == null) {
            sServiceConnection = new CellBroadcastServiceConnection();
        }
        String cellBroadcastServicePackage = getCellBroadcastServicePackage();
        this.mCellBroadcastServicePackage = cellBroadcastServicePackage;
        if (cellBroadcastServicePackage == null) {
            Log.e("CellBroadcastServiceManager", "Unable to bind service; no cell broadcast service found");
            this.mLocalLog.log("Unable to bind service; no cell broadcast service found");
            CellBroadcastStatsLog.write(CallFailCause.RADIO_INTERNAL_ERROR, 15, "Unable to bind service; no cell broadcast service found");
            return;
        }
        this.mModuleCellBroadcastHandler = new Handler() { // from class: com.android.internal.telephony.CellBroadcastServiceManager.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (!CellBroadcastServiceManager.this.mEnabled) {
                    Log.d("CellBroadcastServiceManager", "CB module is disabled.");
                    return;
                }
                if (CellBroadcastServiceManager.sServiceConnection.mService == null) {
                    Log.d("CellBroadcastServiceManager", "sServiceConnection.mService is null, ignoring message.");
                    CellBroadcastStatsLog.write(CallFailCause.RADIO_INTERNAL_ERROR, 15, "sServiceConnection.mService is null, ignoring message.");
                    return;
                }
                try {
                    ICellBroadcastService iCellBroadcastServiceAsInterface = ICellBroadcastService.Stub.asInterface(CellBroadcastServiceManager.sServiceConnection.mService);
                    int i = message.what;
                    if (i == 0) {
                        CellBroadcastServiceManager.this.mLocalLog.log("GSM SMS CB for phone " + CellBroadcastServiceManager.this.mPhone.getPhoneId());
                        CellBroadcastStatsLog.write(CallFailCause.NO_VALID_SIM, 1, 1);
                        iCellBroadcastServiceAsInterface.handleGsmCellBroadcastSms(CellBroadcastServiceManager.this.mPhone.getPhoneId(), (byte[]) ((AsyncResult) message.obj).result);
                        return;
                    }
                    if (i == 1) {
                        CellBroadcastServiceManager.this.mLocalLog.log("CDMA SMS CB for phone " + CellBroadcastServiceManager.this.mPhone.getPhoneId());
                        SmsMessage smsMessage = (SmsMessage) message.obj;
                        CellBroadcastStatsLog.write(CallFailCause.NO_VALID_SIM, 2, 1);
                        iCellBroadcastServiceAsInterface.handleCdmaCellBroadcastSms(CellBroadcastServiceManager.this.mPhone.getPhoneId(), smsMessage.getEnvelopeBearerData(), smsMessage.getEnvelopeServiceCategory());
                        return;
                    }
                    if (i == 2) {
                        CellBroadcastServiceManager.this.mLocalLog.log("CDMA SCP message for phone " + CellBroadcastServiceManager.this.mPhone.getPhoneId());
                        CellBroadcastStatsLog.write(CallFailCause.NO_VALID_SIM, 3, 1);
                        Pair pair = (Pair) message.obj;
                        SmsMessage smsMessage2 = (SmsMessage) pair.first;
                        iCellBroadcastServiceAsInterface.handleCdmaScpMessage(CellBroadcastServiceManager.this.mPhone.getPhoneId(), smsMessage2.getSmsCbProgramData(), smsMessage2.getOriginatingAddress(), (RemoteCallback) pair.second);
                    }
                } catch (RemoteException e) {
                    String str = "Failed to connect to default app: " + CellBroadcastServiceManager.this.mCellBroadcastServicePackage + " err: " + e.toString();
                    Log.e("CellBroadcastServiceManager", str);
                    CellBroadcastServiceManager.this.mLocalLog.log(str);
                    CellBroadcastStatsLog.write(CallFailCause.RADIO_INTERNAL_ERROR, 15, str);
                    CellBroadcastServiceManager.this.mContext.unbindService(CellBroadcastServiceManager.sServiceConnection);
                    CellBroadcastServiceManager.sServiceConnection = null;
                }
            }
        };
        Intent intent = new Intent("android.telephony.CellBroadcastService");
        intent.setPackage(this.mCellBroadcastServicePackage);
        CellBroadcastServiceConnection cellBroadcastServiceConnection = sServiceConnection;
        if (cellBroadcastServiceConnection.mService != null) {
            Log.d("CellBroadcastServiceManager", "skipping bindService because connection already exists");
        } else {
            boolean zBindService = this.mContext.bindService(intent, cellBroadcastServiceConnection, 1);
            Log.d("CellBroadcastServiceManager", "serviceWasBound=" + zBindService);
            if (!zBindService) {
                Log.e("CellBroadcastServiceManager", "Unable to bind to service");
                this.mLocalLog.log("Unable to bind to service");
                CellBroadcastStatsLog.write(CallFailCause.RADIO_INTERNAL_ERROR, 15, "Unable to bind to service");
                return;
            }
        }
        this.mPhone.mCi.setOnNewGsmBroadcastSms(this.mModuleCellBroadcastHandler, 0, null);
    }

    private String getCellBroadcastServicePackage() {
        PackageManager packageManager = this.mContext.getPackageManager();
        List<ResolveInfo> listQueryIntentServices = packageManager.queryIntentServices(new Intent("android.telephony.CellBroadcastService"), 1048576);
        if (listQueryIntentServices.size() != 1) {
            Log.e("CellBroadcastServiceManager", "getCellBroadcastServicePackageName: found " + listQueryIntentServices.size() + " CBS packages");
        }
        Iterator<ResolveInfo> it = listQueryIntentServices.iterator();
        while (it.hasNext()) {
            ServiceInfo serviceInfo = it.next().serviceInfo;
            if (serviceInfo != null) {
                String str = serviceInfo.packageName;
                if (!TextUtils.isEmpty(str)) {
                    if (packageManager.checkPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", str) == 0) {
                        Log.d("CellBroadcastServiceManager", "getCellBroadcastServicePackageName: " + str);
                        return str;
                    }
                    Log.e("CellBroadcastServiceManager", "getCellBroadcastServicePackageName: " + str + " does not have READ_PRIVILEGED_PHONE_STATE permission");
                } else {
                    Log.e("CellBroadcastServiceManager", "getCellBroadcastServicePackageName: found a CBS package but packageName is null/empty");
                }
            }
        }
        Log.e("CellBroadcastServiceManager", "getCellBroadcastServicePackageName: package name not found");
        return null;
    }

    private class CellBroadcastServiceConnection implements ServiceConnection {
        IBinder mService;

        private CellBroadcastServiceConnection(CellBroadcastServiceManager cellBroadcastServiceManager) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("CellBroadcastServiceManager", "connected to CellBroadcastService");
            this.mService = iBinder;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("CellBroadcastServiceManager", "mICellBroadcastService has disconnected unexpectedly");
            this.mService = null;
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            Log.d("CellBroadcastServiceManager", "Binding died");
        }

        @Override // android.content.ServiceConnection
        public void onNullBinding(ComponentName componentName) {
            Log.d("CellBroadcastServiceManager", "Null binding");
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IBinder iBinder;
        printWriter.println("CellBroadcastServiceManager:");
        printWriter.println(" mEnabled=" + this.mEnabled);
        printWriter.println(" mCellBroadcastServicePackage=" + this.mCellBroadcastServicePackage);
        if (this.mEnabled) {
            try {
                CellBroadcastServiceConnection cellBroadcastServiceConnection = sServiceConnection;
                if (cellBroadcastServiceConnection != null && (iBinder = cellBroadcastServiceConnection.mService) != null) {
                    iBinder.dump(fileDescriptor, strArr);
                } else {
                    printWriter.println(" sServiceConnection is null");
                }
            } catch (RemoteException e) {
                printWriter.println(" mService.dump() threw RemoteException e: " + e.toString());
            }
        }
        this.mLocalLog.dump(fileDescriptor, printWriter, strArr);
        printWriter.flush();
    }
}
