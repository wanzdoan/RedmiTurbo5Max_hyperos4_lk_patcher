package com.android.phone;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MiuiSettings;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellLocation;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.util.Pair;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.CloudController.CloudModemControllerBase;
import com.android.phone.module.INetworkModule;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.MiuiThreadUtil;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import com.ot.pubsub.util.v;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyStatAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class CloudModemController extends CloudModemControllerBase implements TelephonyStatAdapter.ICallBack {
    private CloudCmdOpt mCloudCmdOpt;
    private JSONArray mCloudMdCtrlList;
    private static final SimpleDateFormat sTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final long ONE_DAY = v.b;
    private final ArrayList mAlarmList = new ArrayList();
    private final ArrayList mCloudOptList = new ArrayList();
    private final Handler mWorkHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.CloudModemController.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 102) {
                CloudModemController.this.onCellChanged(message.arg1);
                return;
            }
            if (i != 150) {
                switch (i) {
                    case 106:
                        CloudModemController.this.mCloudOptList.forEach(new CloudModemController$$ExternalSyntheticLambda3());
                        break;
                    case 107:
                        int i2 = message.arg1;
                        if (i2 != CloudModemController.this.mDdsSlotId) {
                            CloudModemController.this.mCloudOptList.forEach(new CloudModemController$$ExternalSyntheticLambda3());
                            CloudModemController.this.mDdsSlotId = i2;
                            CloudModemControllerBase.logV("smoothen DDS slot to " + CloudModemController.this.mDdsSlotId);
                            if (SubscriptionManager.isValidSlotId(CloudModemController.this.mDdsSlotId)) {
                                PreferenceUtils.getInstance().setIntPreference("cloud_common_cmd_ver", -1);
                                CloudModemController.this.updateCloudFeatureStatus(false);
                            }
                        }
                        break;
                    case 108:
                    case 109:
                        int i3 = message.arg1;
                        Object obj = message.obj;
                        if (obj instanceof CloudOpt) {
                            ((CloudOpt) obj).onTimeoutCallback(i3);
                        }
                        break;
                }
                return;
            }
            Intent intent = (Intent) message.obj;
            CloudModemController.this.downloadCloudFile(intent.getIntExtra("type", 0), intent.getStringExtra("url"), intent.getIntExtra("version", 0));
            CloudModemController.this.notifyAlarmTriggered(intent.getIntExtra("id", 0), intent.getBooleanExtra("begin", false));
            CloudModemController.this.notifyTacChange(intent.getStringExtra("plmn"), intent.getIntExtra("tac", 0));
        }
    };
    private volatile int mDdsSlotId = -1;
    private final AlarmManager mAlarmMgr = (AlarmManager) this.mContext.getSystemService("alarm");

    public CloudModemController() {
        tryRegisterCallback();
    }

    @Override // com.android.phone.CloudController.CloudModemControllerBase
    protected void tryRegisterCallback() {
        if (this.mCloudEnable && this.mReceiver == null) {
            IntentFilter intentFilter = new IntentFilter("com.android.phone.CloudModemController.debug");
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.CloudModemController.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    CloudModemControllerBase.log("onReceive " + intent.getAction());
                    CloudModemController.this.mWorkHandler.sendMessage(CloudModemController.this.mWorkHandler.obtainMessage(150, intent));
                }
            };
            this.mReceiver = broadcastReceiver;
            this.mContext.registerReceiver(broadcastReceiver, intentFilter, 2);
            TelephonyStatAdapter.registerCallBack(this);
            return;
        }
        BroadcastReceiver broadcastReceiver2 = this.mReceiver;
        if (broadcastReceiver2 != null) {
            this.mContext.unregisterReceiver(broadcastReceiver2);
            TelephonyStatAdapter.unregisterCallBack(this);
            this.mReceiver = null;
        }
    }

    @Override // com.android.phone.CloudController.CloudModemControllerBase
    protected void updateCloudFeatureStatus(boolean z) {
        CloudCmdOpt cloudCmdOpt;
        try {
            super.updateCloudFeatureStatus(z);
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "CloudModemControl");
            if (cloudDataList != null && !cloudDataList.isEmpty()) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    if (cloudData.getBoolean("MdCtrl", false) && (cloudCmdOpt = this.mCloudCmdOpt) != null) {
                        cloudCmdOpt.commonCmdOnJson(cloudData.json(), z);
                    }
                }
                return;
            }
            CloudModemControllerBase.log("cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onCellLocationChanged(int i, CellLocation cellLocation) {
        this.mWorkHandler.obtainMessage(102, i, 0).sendToTarget();
    }

    public void onSignalStrengthChanged(int i, final SignalStrength signalStrength) {
        if (i == this.mDdsSlotId) {
            this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.CloudModemController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSignalStrengthChanged$1(signalStrength);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSignalStrengthChanged$1(final SignalStrength signalStrength) {
        this.mCloudOptList.forEach(new Consumer() { // from class: com.android.phone.CloudModemController$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((CloudModemController.CloudOpt) obj).onSignalChanged(signalStrength);
            }
        });
    }

    @Override // com.android.phone.CloudController.CloudModemControllerBase
    public void onDatabaseUpdated() {
        CloudModemControllerBase.log("onCloudDatabaseUpdated");
        this.mCloudOptList.forEach(new CloudModemController$$ExternalSyntheticLambda3());
        this.mAlarmList.clear();
        setupAlarmsForActionsV3();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCellChanged(int i) {
        CellIdentity currentCellIdentity;
        String plmn;
        int tac;
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null || (currentCellIdentity = phone.getCurrentCellIdentity()) == null) {
            return;
        }
        if (currentCellIdentity instanceof CellIdentityLte) {
            CellIdentityLte cellIdentityLte = (CellIdentityLte) currentCellIdentity;
            plmn = cellIdentityLte.getPlmn();
            tac = cellIdentityLte.getTac();
        } else if (currentCellIdentity instanceof CellIdentityNr) {
            CellIdentityNr cellIdentityNr = (CellIdentityNr) currentCellIdentity;
            plmn = cellIdentityNr.getPlmn();
            tac = cellIdentityNr.getTac();
        } else {
            CloudModemControllerBase.log("cell type not supported, return");
            return;
        }
        if (notifyTacChange(plmn, tac)) {
            CloudModemControllerBase.log("action base on tac change");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getCursorInt(Cursor cursor, String str) {
        return cursor.getInt(cursor.getColumnIndex(str));
    }

    private static long getCursorLong(Cursor cursor, String str) {
        return cursor.getLong(cursor.getColumnIndex(str));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getCursorString(Cursor cursor, String str) {
        return cursor.getString(cursor.getColumnIndex(str));
    }

    public void onActiveDataPhoneSlotChanged(int i) {
        CloudModemControllerBase.log("onActiveDataPhoneSlotChanged:" + i);
        if (SubscriptionManager.isValidSlotId(i)) {
            if (this.mDdsSlotId < 0 && i != this.mDdsSlotId) {
                this.mDdsSlotId = i;
                this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.CloudModemController$$ExternalSyntheticLambda4
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onActiveDataPhoneSlotChanged$2();
                    }
                });
            } else {
                this.mWorkHandler.removeMessages(107);
                this.mWorkHandler.sendMessageDelayed(this.mWorkHandler.obtainMessage(107, i, 0), MirrorDesktopHelper.TIMEOUT_MILLIS);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActiveDataPhoneSlotChanged$2() {
        try {
            this.mCloudOptList.add(new CloudNr2Lte());
            this.mCloudOptList.add(new CloudOffset());
            this.mCloudOptList.add(new CloudSnsOpt());
            CloudCmdOpt cloudCmdOpt = new CloudCmdOpt();
            this.mCloudCmdOpt = cloudCmdOpt;
            this.mCloudOptList.add(cloudCmdOpt);
            setupAlarmsForActionsV3();
            updateCloudFeatureStatus(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long timeStr2TimeMillis(String str) {
        if (str == null || !str.matches("^[0-9]{4}$")) {
            return -1L;
        }
        int i = Integer.parseInt(str.substring(0, 2));
        int i2 = Integer.parseInt(str.substring(2));
        if (i < 0 || i > 23 || i2 < 0 || i2 > 59) {
            return -1L;
        }
        return ZonedDateTime.of(LocalDate.now(), LocalTime.of(i, i2), ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPhoneInNr(int i) {
        ServiceState serviceState;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        return (phone == null || (serviceState = phone.getServiceState()) == null || (serviceState.getRilDataRadioTechnology() != 20 && serviceState.getRilDataRadioTechnology() != 30)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPhoneInLte(int i) {
        ServiceState serviceState;
        Phone phone = MiuiPhoneUtils.getPhone(i);
        return (phone == null || (serviceState = phone.getServiceState()) == null || (serviceState.getRilDataRadioTechnology() != 14 && serviceState.getRilDataRadioTechnology() != 19)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPoorLteSignalStrength(SignalStrength signalStrength) {
        if (signalStrength == null) {
            return false;
        }
        int lteRssnr = signalStrength.getLteRssnr();
        int lteRsrp = signalStrength.getLteRsrp();
        return lteRssnr < 0 || lteRsrp < -110 || lteRssnr == Integer.MAX_VALUE || lteRsrp == Integer.MAX_VALUE;
    }

    private void groupAlarms(long j, int i, boolean z) {
        if (this.mAlarmList.isEmpty()) {
            BeginEndAlarmListener beginEndAlarmListener = new BeginEndAlarmListener();
            beginEndAlarmListener.add(i, z);
            FiveGManagerBase.localLog("CloudMdCtrl", "groupAlarms @" + sTimeFormat.format(new Date(j)) + beginEndAlarmListener);
            this.mAlarmList.add(new Pair(Long.valueOf(j), beginEndAlarmListener));
        } else {
            ArrayList arrayList = this.mAlarmList;
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                Pair pair = (Pair) obj;
                if (Math.abs(((Long) pair.first).longValue() - j) < 600000) {
                    ((BeginEndAlarmListener) pair.second).add(i, z);
                }
            }
            BeginEndAlarmListener beginEndAlarmListener2 = new BeginEndAlarmListener();
            beginEndAlarmListener2.add(i, z);
            FiveGManagerBase.localLog("CloudMdCtrl", "groupAlarms @" + sTimeFormat.format(new Date(j)) + beginEndAlarmListener2);
            this.mAlarmList.add(new Pair(Long.valueOf(j), beginEndAlarmListener2));
        }
        this.mAlarmList.sort(new Comparator() { // from class: com.android.phone.CloudModemController$$ExternalSyntheticLambda6
            @Override // java.util.Comparator
            public final int compare(Object obj2, Object obj3) {
                return ((Long) ((Pair) obj2).first).compareTo((Long) ((Pair) obj3).first);
            }
        });
    }

    private void setupAlarmsForActionsV3() {
        CloudModemControllerBase.log("setupAlarmsForActionsV3");
        try {
            Cursor cursorQueryOtaCellV3 = this.mDbHelper.queryOtaCellV3();
            try {
                if (cursorQueryOtaCellV3 == null) {
                    CloudModemControllerBase.log("no data in ota cell v3 db");
                    if (cursorQueryOtaCellV3 == null) {
                        return;
                    }
                } else {
                    while (cursorQueryOtaCellV3.moveToNext()) {
                        String cursorString = getCursorString(cursorQueryOtaCellV3, "bTime");
                        String cursorString2 = getCursorString(cursorQueryOtaCellV3, "eTime");
                        final int cursorInt = getCursorInt(cursorQueryOtaCellV3, "_id");
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        long cursorLong = getCursorLong(cursorQueryOtaCellV3, "exp_time");
                        long jTimeStr2TimeMillis = timeStr2TimeMillis(cursorString);
                        long jTimeStr2TimeMillis2 = timeStr2TimeMillis(cursorString2);
                        if (jTimeStr2TimeMillis >= 0 && jTimeStr2TimeMillis2 >= 0 && Math.abs(jTimeStr2TimeMillis2 - jTimeStr2TimeMillis) >= 600000) {
                            if (jTimeStr2TimeMillis2 <= jTimeStr2TimeMillis) {
                                jTimeStr2TimeMillis2 += ONE_DAY;
                            }
                            if (jTimeStr2TimeMillis2 > jTimeStr2TimeMillis) {
                                int i = 0;
                                while (true) {
                                    long j = i;
                                    if (j >= 90) {
                                        break;
                                    }
                                    long j2 = ONE_DAY;
                                    int i2 = i;
                                    long j3 = jTimeStr2TimeMillis + (j * j2);
                                    long j4 = (j * j2) + jTimeStr2TimeMillis2;
                                    if (j3 >= cursorLong || j4 >= cursorLong) {
                                        break;
                                        break;
                                    }
                                    if (jCurrentTimeMillis < j3 || jCurrentTimeMillis >= j4) {
                                        if (jCurrentTimeMillis < j3) {
                                            groupAlarms(j3, cursorInt, true);
                                            groupAlarms(j4, cursorInt, false);
                                        }
                                        i = i2 + 1;
                                    } else {
                                        this.mCloudOptList.forEach(new Consumer() { // from class: com.android.phone.CloudModemController$$ExternalSyntheticLambda5
                                            @Override // java.util.function.Consumer
                                            public final void accept(Object obj) {
                                                ((CloudModemController.CloudOpt) obj).onAlarmTriggered(cursorInt, true);
                                            }
                                        });
                                        groupAlarms(j4, cursorInt, false);
                                    }
                                    i = i2 + 1;
                                }
                            } else {
                                CloudModemControllerBase.log("ERR begin/end time:" + jTimeStr2TimeMillis + " " + jTimeStr2TimeMillis2);
                            }
                        } else {
                            CloudModemControllerBase.logV("no begin/end time:" + cursorString + " " + cursorString2);
                        }
                    }
                    if (!this.mAlarmList.isEmpty()) {
                        Pair pair = (Pair) this.mAlarmList.removeFirst();
                        this.mAlarmMgr.cancel((AlarmManager.OnAlarmListener) pair.second);
                        CloudModemControllerBase.logV("cloud push alarm @" + pair.first);
                        this.mAlarmMgr.setExact(0, ((Long) pair.first).longValue(), "cloudpush-begin-end", (AlarmManager.OnAlarmListener) pair.second, this.mWorkHandler);
                    }
                }
                cursorQueryOtaCellV3.close();
            } catch (Throwable th) {
                if (cursorQueryOtaCellV3 == null) {
                    throw th;
                }
                try {
                    cursorQueryOtaCellV3.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean notifyTacChange(String str, int i) {
        ArrayList arrayList = this.mCloudOptList;
        int size = arrayList.size();
        boolean zOnTacChanged = false;
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            zOnTacChanged |= ((CloudOpt) obj).onTacChanged(str, i);
        }
        return zOnTacChanged;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAlarmTriggered(final int i, final boolean z) {
        this.mCloudOptList.forEach(new Consumer() { // from class: com.android.phone.CloudModemController$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((CloudModemController.CloudOpt) obj).onAlarmTriggered(i, z);
            }
        });
    }

    abstract class CloudOpt {
        /* JADX INFO: Access modifiers changed from: package-private */
        public void onAlarmTriggered(int i, boolean z) {
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void onSignalChanged(SignalStrength signalStrength) {
        }

        boolean onTacChanged(String str, int i) {
            return false;
        }

        void onTimeoutCallback(int i) {
        }

        abstract void reset();

        CloudOpt() {
        }
    }

    class BeginEndAlarmListener implements AlarmManager.OnAlarmListener {
        ArrayList dbItemList = new ArrayList();

        BeginEndAlarmListener() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void add(int i, boolean z) {
            this.dbItemList.add(new Pair(Integer.valueOf(i), Boolean.valueOf(z)));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAlarm$0(Pair pair) {
            CloudModemController.this.notifyAlarmTriggered(((Integer) pair.first).intValue(), ((Boolean) pair.second).booleanValue());
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            this.dbItemList.forEach(new Consumer() { // from class: com.android.phone.CloudModemController$BeginEndAlarmListener$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    this.f$0.lambda$onAlarm$0((Pair) obj);
                }
            });
            if (CloudModemController.this.mAlarmList.isEmpty()) {
                return;
            }
            Pair pair = (Pair) CloudModemController.this.mAlarmList.removeFirst();
            CloudModemController.this.mAlarmMgr.cancel((AlarmManager.OnAlarmListener) pair.second);
            CloudModemControllerBase.logV("set next alarm @" + pair.first);
            CloudModemController.this.mAlarmMgr.setExact(0, ((Long) pair.first).longValue(), "cloudpush-begin-end", (AlarmManager.OnAlarmListener) pair.second, CloudModemController.this.mWorkHandler);
        }

        public String toString() {
            return "Alarm DbIds=" + this.dbItemList;
        }
    }

    class CloudNr2Lte extends CloudOpt {
        private long lastActTime;
        private String lastPlmn;
        private int lastTac;
        private int histSlot = -1;
        private long lingerTimer = -1;

        CloudNr2Lte() {
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        public void onTimeoutCallback(int i) {
            Phone phone;
            SignalStrength signalStrength;
            CloudModemControllerBase.log("nr2lte onTimeoutCallback tag:" + i);
            if (i == 2) {
                ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).enableNetworkRecoveryNsa(true, this.histSlot);
                ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).enableNetworkRecoverySa(true, this.histSlot);
                this.histSlot = -1;
                this.lingerTimer = -1L;
                return;
            }
            if (i != 1 || (phone = PhoneFactory.getPhone(CloudModemController.this.mDdsSlotId)) == null || (signalStrength = phone.getSignalStrength()) == null || !CloudModemController.isPoorLteSignalStrength(signalStrength)) {
                return;
            }
            CloudModemControllerBase.log("poor signal detected");
            CloudModemController.this.mWorkHandler.removeMessages(108, this);
            onTimeoutCallback(2);
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        public boolean onTacChanged(String str, int i) {
            long j;
            CloudModemControllerBase.logV(str + " Nr2LteTacChanged:" + i);
            if (i != this.lastTac || !TextUtils.equals(str, this.lastPlmn)) {
                this.lastTac = i;
                this.lastPlmn = str;
                try {
                    Cursor recordByPlmnTacAction = ((CloudModemControllerBase) CloudModemController.this).mDbHelper.getRecordByPlmnTacAction(str, i, 4);
                    if (recordByPlmnTacAction != null) {
                        try {
                            if (recordByPlmnTacAction.moveToFirst()) {
                                if (recordByPlmnTacAction.getCount() > 1) {
                                    CloudModemControllerBase.log("ERR in cloud data, more than 1 Nr2Lte record found in db");
                                } else if (!CloudModemController.isPhoneInNr(CloudModemController.this.mDdsSlotId)) {
                                    CloudModemControllerBase.log("no need to nr2lte due to non 5G");
                                } else {
                                    String cursorString = CloudModemController.getCursorString(recordByPlmnTacAction, "args");
                                    this.lingerTimer = 300000L;
                                    if (TextUtils.isEmpty(cursorString)) {
                                        j = 7200000;
                                    } else {
                                        String[] strArrSplit = cursorString.split(",");
                                        j = Long.parseLong(strArrSplit[0]);
                                        this.lingerTimer = Long.parseLong(strArrSplit[1]);
                                    }
                                    if (this.histSlot >= 0) {
                                        CloudModemControllerBase.log("already Nr2Lte");
                                        this.lastActTime = SystemClock.elapsedRealtime();
                                        CloudModemController.this.mWorkHandler.removeMessages(109, this);
                                    } else if (this.lastActTime > 0 && SystemClock.elapsedRealtime() - this.lastActTime < j) {
                                        CloudModemControllerBase.log("in nr2lte guard timer");
                                    } else {
                                        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).enableNetworkRecoveryNsa(false, CloudModemController.this.mDdsSlotId);
                                        ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).enableNetworkRecoverySa(false, CloudModemController.this.mDdsSlotId);
                                        this.histSlot = CloudModemController.this.mDdsSlotId;
                                        this.lastActTime = SystemClock.elapsedRealtime();
                                        recordByPlmnTacAction.close();
                                        return true;
                                    }
                                }
                                recordByPlmnTacAction.close();
                                return false;
                            }
                        } catch (Throwable th) {
                            if (recordByPlmnTacAction != null) {
                                try {
                                    recordByPlmnTacAction.close();
                                } catch (Throwable th2) {
                                    th.addSuppressed(th2);
                                }
                            }
                            throw th;
                        }
                    }
                    if (this.histSlot >= 0 && this.lingerTimer >= 0) {
                        Message messageObtain = Message.obtain(CloudModemController.this.mWorkHandler, 109, 2, 0, this);
                        CloudModemController.this.mWorkHandler.removeMessages(109, this);
                        CloudModemController.this.mWorkHandler.sendMessageDelayed(messageObtain, this.lingerTimer);
                        CloudModemControllerBase.logV("Nr2Lte lingering");
                    }
                    if (recordByPlmnTacAction != null) {
                        recordByPlmnTacAction.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        void onSignalChanged(SignalStrength signalStrength) {
            if (CloudModemController.isPoorLteSignalStrength(signalStrength) && this.histSlot > -1) {
                CloudModemController.this.mWorkHandler.sendMessageDelayed(Message.obtain(CloudModemController.this.mWorkHandler, 108, 1, 0, this), MirrorDesktopHelper.TIMEOUT_MILLIS);
            } else {
                CloudModemController.this.mWorkHandler.removeMessages(108, this);
            }
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        void reset() {
            CloudModemControllerBase.log("nr2lte reset");
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).enableNetworkRecoveryNsa(true, CloudModemController.this.mDdsSlotId);
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).enableNetworkRecoverySa(true, CloudModemController.this.mDdsSlotId);
            this.histSlot = -1;
            this.lingerTimer = -1L;
        }
    }

    class CloudOffset extends CloudOpt {
        private final ArrayList mOptDbIdList = new ArrayList();

        CloudOffset() {
        }

        /* JADX WARN: Code duplicated, block: B:48:0x0111 A[DONT_INVERT] */
        /* JADX WARN: Code duplicated, block: B:50:0x0115 A[RETURN] */
        private boolean offsetOnRec(int i, boolean z) {
            String str;
            String str2;
            try {
                Cursor recordsById = ((CloudModemControllerBase) CloudModemController.this).mDbHelper.getRecordsById(i);
                if (recordsById != null) {
                    try {
                        if (recordsById.moveToFirst()) {
                            if (CloudModemController.getCursorInt(recordsById, "action") == 3) {
                                String cursorString = CloudModemController.getCursorString(recordsById, "args");
                                int cursorInt = CloudModemController.getCursorInt(recordsById, "pci");
                                int cursorInt2 = CloudModemController.getCursorInt(recordsById, "freq");
                                String cursorString2 = CloudModemController.getCursorString(recordsById, "plmn");
                                boolean z2 = CloudModemController.getCursorInt(recordsById, "rat") == 14;
                                if (z) {
                                    if (z2) {
                                        str2 = "AT+EGMC=1,\"LTE_high_priority_cell\",1,";
                                    } else {
                                        str2 = "AT+EGMC=1,\"NR_high_priority_cell\",1,";
                                    }
                                    StringBuilder sb = new StringBuilder(str2);
                                    sb.append(cursorString2);
                                    if (cursorString2.length() > 5) {
                                        sb.append(",");
                                    } else {
                                        sb.append("f,");
                                    }
                                    sb.append(cursorInt2 + ",");
                                    sb.append(cursorInt);
                                    sb.append("," + cursorString);
                                    FiveGManagerBase.getInstance().atCmdRunSync(CloudModemController.this.mDdsSlotId, new String[]{sb.toString(), "+EGMC"}, null);
                                    this.mOptDbIdList.add(Integer.valueOf(i));
                                } else {
                                    if (z2) {
                                        str = "AT+EGMC=1,\"LTE_high_priority_cell\",2,";
                                    } else {
                                        str = "AT+EGMC=1,\"NR_high_priority_cell\",2,";
                                    }
                                    StringBuilder sb2 = new StringBuilder(str);
                                    sb2.append(cursorString2);
                                    if (cursorString2.length() > 5) {
                                        sb2.append(",");
                                    } else {
                                        sb2.append("f,");
                                    }
                                    sb2.append(cursorInt2 + ",");
                                    sb2.append(cursorInt);
                                    FiveGManagerBase.getInstance().atCmdRunSync(CloudModemController.this.mDdsSlotId, new String[]{sb2.toString(), "+EGMC"}, null);
                                    this.mOptDbIdList.remove(Integer.valueOf(i));
                                }
                                if (recordsById == null) {
                                    return true;
                                }
                            }
                        } else if (recordsById == null) {
                            return true;
                        }
                    } catch (Throwable th) {
                        try {
                            recordsById.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } else if (recordsById == null) {
                    return true;
                }
                recordsById.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        public void onAlarmTriggered(int i, boolean z) {
            CloudModemControllerBase.log("onAlarmTriggered:" + i + " " + z);
            offsetOnRec(i, z);
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        void reset() {
            CloudModemControllerBase.log("offset reset");
            while (!this.mOptDbIdList.isEmpty()) {
                offsetOnRec(((Integer) this.mOptDbIdList.removeFirst()).intValue(), false);
            }
        }
    }

    class CloudCmdOpt extends CloudOpt {
        private final String REVERT_ACTION = "revert";
        private final AlarmManager.OnAlarmListener mCommonCmdAlarmListener = new AlarmManager.OnAlarmListener() { // from class: com.android.phone.CloudModemController$CloudCmdOpt$$ExternalSyntheticLambda1
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                this.f$0.lambda$new$0();
            }
        };

        CloudCmdOpt() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            CloudModemControllerBase.log("CloudCmdOpt onAlarm");
            onCommonCmdExpired(false);
            CloudModemController.persistJsonData(((CloudModemControllerBase) CloudModemController.this).mContext, CloudModemController.this.mCloudMdCtrlList);
        }

        private void setCommonCmdAlarm(long j) {
            if (j > System.currentTimeMillis()) {
                CloudModemControllerBase.logV("setCommonCmdAlarm @" + CloudModemController.sTimeFormat.format(new Date(j)));
                CloudModemController.this.mAlarmMgr.cancel(this.mCommonCmdAlarmListener);
                CloudModemController.this.mAlarmMgr.setExact(0, j, "CloudMdCtrl", this.mCommonCmdAlarmListener, CloudModemController.this.mWorkHandler);
            }
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        void reset() {
            CloudModemControllerBase.log("CloudCmdOpt reset");
            onCommonCmdExpired(true);
        }

        private static String[] getAtCmds(String str) {
            if (!TextUtils.isEmpty(str)) {
                String[] strArrSplit = str.split("::");
                return strArrSplit.length < 2 ? new String[]{strArrSplit[0], ""} : strArrSplit;
            }
            return new String[]{"", ""};
        }

        private void sendCommonCmd(JSONObject jSONObject) throws JSONException {
            long j;
            int i;
            JSONArray jSONArray = jSONObject.getJSONArray("cmds");
            long jCurrentTimeMillis = System.currentTimeMillis();
            int i2 = 0;
            while (i2 < jSONArray.length()) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i2);
                String string = jSONObject2.getString("action");
                String string2 = jSONObject2.getString("revert");
                int i3 = jSONObject2.getInt("slot");
                long j2 = jSONObject2.getLong("duration");
                int i4 = i2;
                long j3 = jCurrentTimeMillis + (RelayUtils.Relay.RETRY_DELAY_IN_MILLIS * j2);
                JSONArray jSONArray2 = jSONArray;
                if (i3 < 0 || i3 > MiuiPhoneUtils.PHONE_COUNT) {
                    j = jCurrentTimeMillis;
                    CloudModemControllerBase.log("invalid slot setting from cloud");
                } else {
                    if (i3 == 0) {
                        j = jCurrentTimeMillis;
                        FiveGManagerBase.getInstance().sendAtCmdSync(CloudModemController.this.mDdsSlotId, getAtCmds(string), null);
                        i = 1 << CloudModemController.this.mDdsSlotId;
                    } else {
                        j = jCurrentTimeMillis;
                        if (i3 == 1) {
                            int i5 = CloudModemController.this.mDdsSlotId == 1 ? 0 : 1;
                            FiveGManagerBase.getInstance().sendAtCmdSync(i5, getAtCmds(string), null);
                            i = 1 << i5;
                        } else if (i3 == 2) {
                            for (int i6 = 0; i6 < MiuiPhoneUtils.PHONE_COUNT; i6++) {
                                FiveGManagerBase.getInstance().sendAtCmdSync(i6, getAtCmds(string), null);
                            }
                            i = 3;
                        } else {
                            i = 0;
                        }
                    }
                    if (!TextUtils.isEmpty(string2) && j2 > 0) {
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("action", string);
                        jSONObject3.put("revert", string2);
                        jSONObject3.put("slot", i);
                        jSONObject3.put("exp_time", j3);
                        CloudModemController.this.mCloudMdCtrlList.put(jSONObject3);
                    }
                }
                i2 = i4 + 1;
                jSONArray = jSONArray2;
                jCurrentTimeMillis = j;
            }
            long jMin = Long.MAX_VALUE;
            for (int i7 = 0; CloudModemController.this.mCloudMdCtrlList != null && i7 < CloudModemController.this.mCloudMdCtrlList.length(); i7++) {
                jMin = Math.min(jMin, CloudModemController.this.mCloudMdCtrlList.getJSONObject(i7).getLong("exp_time"));
            }
            if (jMin < Long.MAX_VALUE) {
                setCommonCmdAlarm(jMin);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void commonCmdOnJson(JSONObject jSONObject, boolean z) throws JSONException {
            int i = jSONObject.getInt("version");
            int intPreference = PreferenceUtils.getInstance().getIntPreference("cloud_common_cmd_ver");
            CloudModemControllerBase.log("common cmd curVer:" + i + " prevVer:" + intPreference);
            CloudModemController cloudModemController = CloudModemController.this;
            cloudModemController.mCloudMdCtrlList = CloudModemController.readJsonFromData(((CloudModemControllerBase) cloudModemController).mContext);
            CloudModemController cloudModemController2 = CloudModemController.this;
            if (i <= intPreference) {
                if (cloudModemController2.mCloudMdCtrlList != null) {
                    onCommonCmdExpired(false);
                    CloudModemController.persistJsonData(((CloudModemControllerBase) CloudModemController.this).mContext, CloudModemController.this.mCloudMdCtrlList);
                    if (z) {
                        CloudModemControllerBase.log("common cmd on existed history");
                        for (int i2 = 0; CloudModemController.this.mCloudMdCtrlList != null && i2 < CloudModemController.this.mCloudMdCtrlList.length(); i2++) {
                            JSONObject jSONObject2 = CloudModemController.this.mCloudMdCtrlList.getJSONObject(i2);
                            int i3 = jSONObject2.getInt("slot");
                            for (int i4 = 0; i4 < MiuiPhoneUtils.PHONE_COUNT; i4++) {
                                if (((1 << i4) & i3) != 0) {
                                    FiveGManagerBase.getInstance().sendAtCmdSync(i4, getAtCmds(jSONObject2.getString("action")), null);
                                }
                            }
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            if (cloudModemController2.mCloudMdCtrlList == null) {
                CloudModemController.this.mCloudMdCtrlList = new JSONArray();
            }
            onCommonCmdExpired(true);
            sendCommonCmd(jSONObject);
            CloudModemController.persistJsonData(((CloudModemControllerBase) CloudModemController.this).mContext, CloudModemController.this.mCloudMdCtrlList);
            PreferenceUtils.getInstance().setIntPreference("cloud_common_cmd_ver", i);
        }

        private void onCommonCmdExpired(boolean z) {
            long jCurrentTimeMillis = z ? Long.MAX_VALUE : System.currentTimeMillis() + RelayUtils.Relay.RETRY_DELAY_IN_MILLIS;
            try {
                ArrayList arrayList = new ArrayList();
                for (int i = 0; CloudModemController.this.mCloudMdCtrlList != null && i < CloudModemController.this.mCloudMdCtrlList.length(); i++) {
                    JSONObject jSONObject = CloudModemController.this.mCloudMdCtrlList.getJSONObject(i);
                    if (jSONObject.getLong("exp_time") < jCurrentTimeMillis) {
                        int i2 = jSONObject.getInt("slot");
                        for (int i3 = 0; i3 < MiuiPhoneUtils.PHONE_COUNT; i3++) {
                            if (((1 << i3) & i2) != 0) {
                                FiveGManagerBase.getInstance().sendAtCmdSync(i3, getAtCmds(jSONObject.getString("revert")), null);
                            }
                        }
                        arrayList.add(Integer.valueOf(i));
                    }
                }
                arrayList.sort(Comparator.reverseOrder());
                arrayList.forEach(new Consumer() { // from class: com.android.phone.CloudModemController$CloudCmdOpt$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        this.f$0.lambda$onCommonCmdExpired$1((Integer) obj);
                    }
                });
                long jMin = Long.MAX_VALUE;
                for (int i4 = 0; CloudModemController.this.mCloudMdCtrlList != null && i4 < CloudModemController.this.mCloudMdCtrlList.length(); i4++) {
                    jMin = Math.min(jMin, CloudModemController.this.mCloudMdCtrlList.getJSONObject(i4).getLong("exp_time"));
                }
                if (jMin < Long.MAX_VALUE) {
                    setCommonCmdAlarm(jMin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCommonCmdExpired$1(Integer num) {
            CloudModemController.this.mCloudMdCtrlList.remove(num.intValue());
        }
    }

    class CloudSnsOpt extends CloudOpt {
        private static final String[] SOLUTIONS = {"MIPC_SET_DATA_STALL_OPT,0,1,0,0,5000,300,300,10000,0xFF,0xFF,0x7F,-110,-3,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "MIPC_SET_DATA_STALL_OPT,2,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,50,50,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,3,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,4,4,0,0,20000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,1,0,0,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,10,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,0,1,0,1,5000,300,300,10000,0xFF,0xFF,0x7F,-110,-3,-105,0,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,10000", "MIPC_SET_DATA_STALL_OPT,2,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,50,50,0xFFFFFFFF,0xFFFFFFFF,30,40,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,3,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0,-100,0,-105,0,30,30,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,4,4,0,1,20000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,1,0,1,5000,300,300,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,-105,0,0xFF,0xFF,10,500,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,0,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,2,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,3,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,4,0,0,0,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,0,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,1,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,2,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,3,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF", "MIPC_SET_DATA_STALL_OPT,4,0,0,1,0,0,0,0xFFFFFFFF,0xFF,0xFF,0x7F,0x7FFF,0x7F,0x7FFF,0x7F,0xFF,0xFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF,0xFFFFFFFF"};
        private int histSlot = -1;
        private long lastActTime;
        private String lastPlmn;
        private int lastTac;
        private long lingerTimer;
        private int revertIdx;

        CloudSnsOpt() {
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        boolean onTacChanged(String str, int i) {
            long j;
            int length;
            CloudModemControllerBase.logV(str + " SnsTacChanged:" + i);
            if (i != this.lastTac || !TextUtils.equals(str, this.lastPlmn)) {
                this.lastTac = i;
                this.lastPlmn = str;
                if (CloudModemController.isPhoneInNr(CloudModemController.this.mDdsSlotId) || CloudModemController.isPhoneInLte(CloudModemController.this.mDdsSlotId)) {
                    try {
                        Cursor recordByPlmnTacAction = ((CloudModemControllerBase) CloudModemController.this).mDbHelper.getRecordByPlmnTacAction(str, i, 100);
                        if (recordByPlmnTacAction != null) {
                            try {
                                if (recordByPlmnTacAction.moveToFirst()) {
                                    if (recordByPlmnTacAction.getCount() > 1) {
                                        CloudModemControllerBase.log("ERR in cloud data, more than 1 SNS record found in db");
                                    } else {
                                        String cursorString = CloudModemController.getCursorString(recordByPlmnTacAction, "args");
                                        this.lingerTimer = 300000L;
                                        if (TextUtils.isEmpty(cursorString)) {
                                            j = 3600000;
                                            length = 0;
                                        } else {
                                            String[] strArrSplit = cursorString.split(",");
                                            j = Long.parseLong(strArrSplit[1]);
                                            this.lingerTimer = Long.parseLong(strArrSplit[2]);
                                            length = Integer.parseInt(strArrSplit[0]);
                                        }
                                        if (this.histSlot >= 0) {
                                            CloudModemControllerBase.log("already SNS");
                                            this.lastActTime = SystemClock.elapsedRealtime();
                                            CloudModemController.this.mWorkHandler.removeMessages(109, this);
                                        } else if (this.lastActTime > 0 && SystemClock.elapsedRealtime() - this.lastActTime < j) {
                                            CloudModemControllerBase.log("in sns guard timer");
                                        } else {
                                            boolean z = CloudModemController.getCursorInt(recordByPlmnTacAction, "rat") == 14;
                                            if (length >= 0) {
                                                String[] strArr = SOLUTIONS;
                                                if (length <= (strArr.length / 2) - 1) {
                                                    if (!z) {
                                                        length += strArr.length / 4;
                                                    }
                                                    FiveGManagerBase.getInstance().sendAtCmdSync(CloudModemController.this.mDdsSlotId, strArr[length].split(","), null);
                                                    this.revertIdx = length + (strArr.length / 2);
                                                    this.histSlot = CloudModemController.this.mDdsSlotId;
                                                    this.lastActTime = SystemClock.elapsedRealtime();
                                                    recordByPlmnTacAction.close();
                                                    return true;
                                                }
                                            }
                                            CloudModemControllerBase.log("invalid actIdxInDb:" + length);
                                        }
                                    }
                                    recordByPlmnTacAction.close();
                                    return false;
                                }
                            } catch (Throwable th) {
                                if (recordByPlmnTacAction != null) {
                                    try {
                                        recordByPlmnTacAction.close();
                                    } catch (Throwable th2) {
                                        th.addSuppressed(th2);
                                    }
                                }
                                throw th;
                            }
                        }
                        if (this.histSlot >= 0 && this.lingerTimer >= 0) {
                            Message messageObtain = Message.obtain(CloudModemController.this.mWorkHandler, 109, 0, 0, this);
                            CloudModemController.this.mWorkHandler.removeMessages(109, this);
                            CloudModemController.this.mWorkHandler.sendMessageDelayed(messageObtain, this.lingerTimer);
                            CloudModemControllerBase.logV("Sns lingering");
                        }
                        if (recordByPlmnTacAction != null) {
                            recordByPlmnTacAction.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    CloudModemControllerBase.log("non 4/5G network, ignore");
                    return false;
                }
            }
            return false;
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        void onTimeoutCallback(int i) {
            CloudModemControllerBase.log("Sns onTimeoutCallback");
            int i2 = this.revertIdx;
            if (i2 >= 0) {
                String[] strArr = SOLUTIONS;
                if (i2 < strArr.length) {
                    FiveGManagerBase.getInstance().sendAtCmdSync(this.histSlot, strArr[this.revertIdx].split(","), null);
                    this.revertIdx = -1;
                }
            }
            this.histSlot = -1;
            this.lingerTimer = -1L;
        }

        @Override // com.android.phone.CloudModemController.CloudOpt
        void reset() {
            CloudModemControllerBase.log("Sns reset");
            int i = this.revertIdx;
            if (i >= 0) {
                String[] strArr = SOLUTIONS;
                if (i < strArr.length) {
                    FiveGManagerBase.getInstance().sendAtCmdSync(this.histSlot, strArr[this.revertIdx].split(","), null);
                    this.revertIdx = -1;
                }
            }
            this.histSlot = -1;
            this.lingerTimer = -1L;
        }
    }

    public static void persistJsonData(Context context, JSONArray jSONArray) {
        CloudModemControllerBase.logV("persist data:" + jSONArray);
        if (jSONArray == null) {
            return;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getFilesDir(), "cloud_push_v3"));
            try {
                ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
                try {
                    zipOutputStream.putNextEntry(new ZipEntry("common_cmds"));
                    byte[] bytes = jSONArray.toString().getBytes(StandardCharsets.UTF_8);
                    zipOutputStream.write(bytes, 0, bytes.length);
                    zipOutputStream.closeEntry();
                    zipOutputStream.close();
                    fileOutputStream.close();
                } catch (Throwable th) {
                    try {
                        zipOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                try {
                    fileOutputStream.close();
                } catch (Throwable th4) {
                    th3.addSuppressed(th4);
                }
                throw th3;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray readJsonFromData(Context context) {
        ZipEntry nextEntry;
        File file = new File(context.getFilesDir(), "cloud_push_v3");
        if (!file.exists()) {
            return null;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    do {
                        try {
                            nextEntry = zipInputStream.getNextEntry();
                            if (nextEntry == null) {
                                byteArrayOutputStream.close();
                                zipInputStream.close();
                                fileInputStream.close();
                                return null;
                            }
                        } catch (Throwable th) {
                            try {
                                byteArrayOutputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                        try {
                            zipInputStream.close();
                        } catch (Throwable th3) {
                            th.addSuppressed(th3);
                        }
                        throw th;
                    } while (!TextUtils.equals("common_cmds", nextEntry.getName()));
                    byte[] bArr = new byte[SmartNetworkRelayScore.TRAFFIC_UNIT];
                    while (true) {
                        int i = zipInputStream.read(bArr, 0, SmartNetworkRelayScore.TRAFFIC_UNIT);
                        if (i != -1) {
                            byteArrayOutputStream.write(bArr, 0, i);
                        } else {
                            zipInputStream.closeEntry();
                            String string = byteArrayOutputStream.toString(StandardCharsets.UTF_8);
                            byteArrayOutputStream.close();
                            JSONArray jSONArray = new JSONArray(string);
                            byteArrayOutputStream.close();
                            zipInputStream.close();
                            fileInputStream.close();
                            return jSONArray;
                        }
                        zipInputStream.close();
                        throw th;
                    }
                } catch (Throwable th4) {
                    zipInputStream.close();
                    throw th4;
                }
            } catch (Throwable th5) {
                try {
                    fileInputStream.close();
                } catch (Throwable th6) {
                    th5.addSuppressed(th6);
                }
                throw th5;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
