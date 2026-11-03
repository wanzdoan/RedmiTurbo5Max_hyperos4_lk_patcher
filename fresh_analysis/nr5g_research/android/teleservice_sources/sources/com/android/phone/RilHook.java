package com.android.phone;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import androidx.profileinstaller.ProfileInstallReceiver$$ExternalSyntheticLambda0;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.INetworkModule;
import com.android.phone.utils.ModemUtils;
import com.xiaomi.hyperradio.HyperRadioManager;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookCallback;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.DefaultSimManager;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManagerEx;

/* JADX INFO: loaded from: classes.dex */
public class RilHook {
    private Context mContext;
    private Class mHookCls;
    private Object mQcHook;
    private RilhookHandler mRilhookHandler;
    private HandlerThread mWorkThread;
    private static final int[] uLSpeedLimitAmbr = {960000, 3072000, 5120000, 6144000, 1024000, 7372000};
    private static final int[] dLSpeedLimitAmbr = {960000, 1024000, 10300000, 6144000, 1024000, 7372000};
    private static RilHook mInstance = null;
    private boolean mQcrilHookReady = false;
    private ConcurrentHashMap mMethodMap = new ConcurrentHashMap();
    private boolean[] mSimGutiReSend = {false, false};

    public boolean qcRilPurgeGutiForSoftbank(int i) {
        return false;
    }

    public static RilHook init(Context context) {
        if (mInstance == null) {
            synchronized (RilHook.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new RilHook(context.getApplicationContext());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    public static RilHook get(Context context) {
        if (mInstance == null) {
            synchronized (RilHook.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new RilHook(context.getApplicationContext());
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    private RilHook(Context context) {
        this.mContext = null;
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread("RilHook", 10);
        this.mWorkThread = handlerThread;
        handlerThread.start();
        this.mRilhookHandler = new RilhookHandler(this.mWorkThread.getLooper());
        try {
            int i = MiRilHook.DEFAULT_CUST_XIAOMI_CALL_ANALYSIS_APP_ID;
            this.mHookCls = MiRilHook.class;
        } catch (ClassNotFoundException unused) {
            log("mirilhook not available");
        }
        if (ModemProvider.IS_QCOM) {
            qcRilHook();
        }
    }

    class QcrilHookCbHandler implements InvocationHandler {
        QcrilHookCbHandler() {
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            if ("onQcRilHookReady".equals(method.getName())) {
                RilHook.this.log("QcRilHook is Ready");
                RilHook.this.mQcrilHookReady = true;
                RilHook.this.notifyRilHookListener(true);
                for (int i = 0; i < RilHook.this.mSimGutiReSend.length; i++) {
                    if (RilHook.this.mSimGutiReSend[i]) {
                        RilHook.this.log("QcRilHook is Ready i =" + i);
                        RilHook.this.qcRilPurgeGutiForSoftbank(i);
                        RilHook.this.mSimGutiReSend[i] = false;
                    }
                }
                return null;
            }
            if (!"onQcRilHookDisconnected".equals(method.getName())) {
                return null;
            }
            RilHook.this.log("QcRilHook is disconnected");
            RilHook.this.mQcrilHookReady = false;
            RilHook.this.notifyRilHookListener(false);
            return null;
        }
    }

    private void qcRilHook() {
        Constructor constructor;
        try {
            Object objNewProxyInstance = Proxy.newProxyInstance(this.mContext.getClassLoader(), new Class[]{MiRilHookCallback.class}, new QcrilHookCbHandler());
            Class cls = this.mHookCls;
            if (cls == null || (constructor = cls.getConstructor(Context.class, MiRilHookCallback.class)) == null) {
                return;
            }
            this.mQcHook = constructor.newInstance(this.mContext, objNewProxyInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class RilhookHandler extends Handler {
        RilhookHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int iIntValue = ((Integer) message.obj).intValue();
            RilHook.this.log("handleMessage=" + message.what + " phoneId:" + iIntValue);
            if (message.what != 0) {
                return;
            }
            RilHook.this.forceLteScanReq(iIntValue);
        }
    }

    public void forceScanLte(int i) {
        if (ModemProvider.IS_QCOM) {
            this.mRilhookHandler.obtainMessage(0, Integer.valueOf(i)).sendToTarget();
            return;
        }
        if (ModemProvider.IS_MTK) {
            mtkForceLte(i);
        } else if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
            RilhookHandler rilhookHandler = this.mRilhookHandler;
            Objects.requireNonNull(rilhookHandler);
            hyperRadioManager.forceLteScanReq(i, new RilHook$$ExternalSyntheticLambda0(rilhookHandler), new HyperRadioManager.GenericResponse() { // from class: com.android.phone.RilHook.1
                public void onResponse(int i2) {
                    RilHook.this.log("forceScanLte done with errorCode " + i2);
                }
            });
        }
    }

    public void forceLteScanReq(int i) {
        try {
            if (!this.mQcrilHookReady) {
                log("forceLteScanReq, qcril is not ready");
                return;
            }
            Method declaredMethod = this.mHookCls.getDeclaredMethod("forceLteScanReq", Integer.TYPE);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forceLte2Nr(int i) {
        if (ModemProvider.IS_QCOM) {
            ((INetworkModule) ModuleDelegate.get(INetworkModule.class)).sendQmsRequestRawAsync(1002, i, null, "");
            return;
        }
        if (ModemProvider.IS_MTK) {
            mtkSetL2NrSelection(i);
        } else if (ModemProvider.IS_XRING) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
            RilhookHandler rilhookHandler = this.mRilhookHandler;
            Objects.requireNonNull(rilhookHandler);
            hyperRadioManager.setL2NrSelection(i, new RilHook$$ExternalSyntheticLambda0(rilhookHandler), new HyperRadioManager.GenericResponse() { // from class: com.android.phone.RilHook.2
                public void onResponse(int i2) {
                    RilHook.this.log("forceLte2Nr done with errorCode " + i2);
                }
            });
        }
    }

    public void lteReAttach(int i) {
        if (MiuiTelephony.IS_QCOM) {
            try {
                if (!this.mQcrilHookReady) {
                    log("lteReAttach, qcril is not ready");
                    return;
                }
                Method declaredMethod = this.mHookCls.getDeclaredMethod("lteReAttach", Integer.TYPE);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (MiuiTelephony.IS_MTK) {
            mtkLteReAttach(i);
        } else if (MiuiTelephony.IS_XRING) {
            xringLteReAttach(i);
        }
    }

    public void setImsHighPriorityDataDuration(int i, int i2) {
        try {
            if (!this.mQcrilHookReady) {
                log("setImsHighPriorityDataDuration, qcril is not ready");
                return;
            }
            Class cls = this.mHookCls;
            Class<?> cls2 = Integer.TYPE;
            Method declaredMethod = cls.getDeclaredMethod("setImsHighPriorityDataDuration", cls2, cls2);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this.mQcHook, Integer.valueOf(i), Integer.valueOf(i2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String readOemNvItem(int i) {
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit != null) {
            String oemNvItem = modemUtilsInit.readOemNvItem(i);
            log("readOemNvItemMethod get result, item:" + i);
            return oemNvItem;
        }
        return "";
    }

    public boolean writeOemNvItem(int i, String str) {
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit != null) {
            return modemUtilsInit.writeOemNvItem(i, str);
        }
        return false;
    }

    private boolean isSupportInvalidSimHalVersion() {
        return HyperOSCustFeatureResolve.getBoolean("config_is_support_invalid_sim_state", false) || MiuiPhoneUtils.isPlatformDE30() || MiuiPhoneUtils.isPlatformDE50() || MiuiPhoneUtils.isPlatformDE70() || MiuiPhoneUtils.isPlatformDE90();
    }

    public void setInvalidSimState(int i, int i2) {
        if (MiuiTelephony.IS_QCOM) {
            if (isSupportInvalidSimHalVersion()) {
                log("HAL Version");
                if (featureSetCmn(50, i2, i)) {
                    return;
                }
                log("HAL Version setInvalidSimState failed");
                return;
            }
            if (TelephonyManagerEx.getDefault().isPlatformX450() || TelephonyManagerEx.getDefault().isPlatformX475()) {
                log("8450/8475 Qc Platform");
                try {
                    if (!this.mQcrilHookReady) {
                        log("setInvalidSimState, qcril is not ready");
                        return;
                    }
                    Class cls = this.mHookCls;
                    Class<?> cls2 = Integer.TYPE;
                    Method declaredMethod = cls.getDeclaredMethod("setInvalidSimState", cls2, cls2);
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(this.mQcHook, Integer.valueOf(i), Integer.valueOf(i2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getQcRilReportMsg(int i) {
        byte[] bArrFeatureQueryCmn = featureQueryCmn(9, 11, i);
        if (bArrFeatureQueryCmn != null) {
            return new String(bArrFeatureQueryCmn);
        }
        return "";
    }

    public void deleteQcRilReportMsg(int i) {
        featureQueryCmn(9, 111, i);
    }

    private byte[] featureQueryCmn(int i, int i2, int i3) {
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit != null) {
            return modemUtilsInit.featureQueryCmn(i, i2, i3);
        }
        return null;
    }

    private boolean featureSetCmn(int i, int i2, int i3) {
        ModemUtils modemUtilsInit = ModemUtils.init(this.mContext);
        if (modemUtilsInit != null) {
            return modemUtilsInit.featureSetCmn(i, i2, i3);
        }
        return false;
    }

    public boolean getFakeCellStatusMsg(int i) {
        log("getFakeCellStatusMsg");
        byte[] bArrFeatureQueryCmn = featureQueryCmn(13, 11, i);
        if (bArrFeatureQueryCmn == null) {
            return false;
        }
        for (byte b : bArrFeatureQueryCmn) {
            log("getFakeCellStatusMsg flag:" + ((int) b));
            if (b == 1) {
                return true;
            }
        }
        return false;
    }

    public boolean setFakeCellStatusMsg(int i, boolean z) {
        log("setFakeCellStatusMsg isSupportFakeCell:" + z);
        boolean zFeatureSetCmn = featureSetCmn(13, z ? 1 : 0, i);
        if (!zFeatureSetCmn) {
            log("setFakeCellStatusMsg failed");
        }
        return zFeatureSetCmn;
    }

    public boolean setNrCaStatusMsg(int i, boolean z, String str) {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        log("setNrCaStatusMsg[" + i + "] enable:" + z + " triggerReason:" + str);
        boolean zFeatureSetCmn = featureSetCmn(30, z ? 1 : 0, i);
        if (!zFeatureSetCmn) {
            log("setNrCaStatusMsg failed");
        }
        return zFeatureSetCmn;
    }

    public boolean getNrCaStatusMsg(int i) {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        log("getNrCaStatusMsg[" + i + "]");
        byte[] bArrFeatureQueryCmn = featureQueryCmn(30, 11, i);
        return (bArrFeatureQueryCmn == null || bArrFeatureQueryCmn.length == 0 || bArrFeatureQueryCmn[0] != 1) ? false : true;
    }

    private ByteBuffer getAmbrInfo(int i) {
        if (!ModemProvider.IS_QCOM) {
            return null;
        }
        try {
            if (!this.mQcrilHookReady) {
                log("getAmbrInfo, qcril is not ready");
                return null;
            }
            Method declaredMethod = this.mHookCls.getDeclaredMethod("getAmbrInfo", Integer.TYPE);
            declaredMethod.setAccessible(true);
            return (ByteBuffer) declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
        } catch (Exception e) {
            log("getAmbrInfo Exception: " + e);
            return null;
        }
    }

    public boolean isSpeedLimit(int i) {
        ByteBuffer ambrInfo = getAmbrInfo(i);
        boolean z = false;
        int i2 = 0;
        z = false;
        z = false;
        if (ambrInfo == null) {
            log("isSpeedLimit invalid Ambr param");
            return false;
        }
        if (ambrInfo != null) {
            try {
                if (ambrInfo.remaining() >= 16) {
                    int i3 = ambrInfo.getInt();
                    ambrInfo.getInt();
                    int i4 = ambrInfo.getInt();
                    ambrInfo.getInt();
                    log("isSpeedLimit ulAmbr = " + i3 + ",dlAmbr=" + i4);
                    boolean z2 = false;
                    while (true) {
                        try {
                            int[] iArr = uLSpeedLimitAmbr;
                            if (i2 >= iArr.length) {
                                return z2;
                            }
                            if (i3 == iArr[i2] && i4 == dLSpeedLimitAmbr[i2]) {
                                z2 = true;
                            }
                            i2++;
                        } catch (Exception e) {
                            e = e;
                            z = z2;
                            log("isSpeedLimit Exception: " + e);
                            return z;
                        }
                    }
                }
            } catch (Exception e2) {
                e = e2;
            }
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void notifyRilHookListener(boolean z) {
        EventHubManager.getInstance().postEvent(new TelephonyEvent.RilEvent("ril_ready", z));
    }

    public ByteBuffer readQetAlarmEfsFile(int i) {
        return handleEfsFileValue("/nv/item_files/modem/rf/qet_alarm_report.dat", 0, i);
    }

    public boolean deleteQetAlarmEfsFile(int i) {
        ByteBuffer byteBufferHandleEfsFileValue = handleEfsFileValue("/nv/item_files/modem/rf/qet_alarm_report.dat", 11, i);
        return byteBufferHandleEfsFileValue != null && byteBufferHandleEfsFileValue.remaining() >= 4 && byteBufferHandleEfsFileValue.getInt() == 0;
    }

    public boolean onFeatureCtlCmn(int i, int i2, int i3) {
        log("onFeatureCtlCmn, phoneId = " + i3 + ", ctlCode = " + i2 + ", featureType = " + i);
        if (i < 1000 || i >= 1200 || this.mContext == null) {
            return false;
        }
        Intent intent = new Intent("com.android.phone.intent.action.ME_CLOUD_CONTROL");
        intent.putExtra("FEATURE_TYPE", i);
        intent.putExtra("CONTROL_CODE", i2);
        this.mContext.sendBroadcast(intent, "android.permission.READ_PRIVILEGED_PHONE_STATE");
        return true;
    }

    public byte[] onFeatureQueryCmn(int i, int i2, int i3) {
        log("onFeatureQueryCmn, phoneId = " + i3 + ", featureType = " + i + ", queryCode = " + i2);
        if (i < 1000 || i >= 1200) {
            return null;
        }
        return onModemEnhanceFeatureQuery(i, i2, i3);
    }

    private byte[] onModemEnhanceFeatureQuery(int i, int i2, int i3) {
        int i4;
        int i5;
        byte[] bArr = new byte[4];
        if (i >= 1000 && i < 1100) {
            try {
                String str = SystemProperties.get("persist.radio.me.fr" + (i - 1000), "null");
                if (str.equals("true")) {
                    i5 = 1;
                } else {
                    i5 = str.equals("false") ? 0 : 11;
                }
                createBufferWithNativeByteOrder(bArr).putInt(i5);
                return bArr;
            } catch (Exception e) {
                e.printStackTrace();
                return bArr;
            }
        }
        if (i >= 1100 && i < 1200) {
            try {
                String str2 = SystemProperties.get("persist.radio.me.para" + (i - 1100), "null");
                if (str2.equals("null")) {
                    i4 = Integer.MAX_VALUE;
                } else {
                    try {
                        i4 = Integer.parseInt(str2);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        return bArr;
                    }
                }
                createBufferWithNativeByteOrder(bArr).putInt(i4);
                return bArr;
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return bArr;
    }

    private ByteBuffer handleEfsFileValue(String str, int i, int i2) {
        ByteBuffer byteBufferWrap = null;
        if (!TextUtils.isEmpty(str)) {
            if (!MiuiPhoneUtils.isValidatePhoneId(i2)) {
                i2 = 0;
            }
            try {
                Class cls = this.mHookCls;
                Class<?> cls2 = Integer.TYPE;
                Method declaredMethod = cls.getDeclaredMethod("qcRilEFSCtlCmn", String.class, cls2, cls2);
                if (declaredMethod != null) {
                    declaredMethod.setAccessible(true);
                    byte[] bArr = (byte[]) declaredMethod.invoke(this.mQcHook, str, Integer.valueOf(i), Integer.valueOf(i2));
                    if (bArr != null && bArr.length >= 4) {
                        byteBufferWrap = ByteBuffer.wrap(bArr);
                        byteBufferWrap.order(ByteOrder.nativeOrder());
                        return byteBufferWrap;
                    }
                }
            } catch (Exception e) {
                log("handleEfsFileValue Exception: " + e);
                return byteBufferWrap;
            }
        }
        return null;
    }

    private static ByteBuffer createBufferWithNativeByteOrder(byte[] bArr) {
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
        byteBufferWrap.order(ByteOrder.nativeOrder());
        return byteBufferWrap;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("RilHook", str);
    }

    private void mtkLteReAttach(final int i) {
        this.mRilhookHandler.post(new Runnable() { // from class: com.android.phone.RilHook$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$mtkLteReAttach$1(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$mtkLteReAttach$1(final int i) {
        final CommandsInterface[] commandsInterfaces;
        if (MiuiPhoneUtils.isValidatePhoneId(i) && (commandsInterfaces = PhoneFactory.getCommandsInterfaces()) != null && commandsInterfaces.length > i) {
            try {
                Class<?> cls = Class.forName("com.mediatek.internal.telephony.MtkRIL");
                Class<?> cls2 = Integer.TYPE;
                Method method = cls.getMethod("dataConnectionDetach", cls2, Message.class);
                final Method method2 = cls.getMethod("dataConnectionAttach", cls2, Message.class);
                log("mtkLteReAttach:" + i);
                method.invoke(commandsInterfaces[i], 0, null);
                this.mRilhookHandler.postDelayed(new Runnable() { // from class: com.android.phone.RilHook$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        RilHook.$r8$lambda$HueZbL6PfemmkV0M321dBwJSoR4(method2, commandsInterfaces, i);
                    }
                }, 100L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static /* synthetic */ void $r8$lambda$HueZbL6PfemmkV0M321dBwJSoR4(Method method, CommandsInterface[] commandsInterfaceArr, int i) {
        try {
            method.invoke(commandsInterfaceArr[i], 0, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void xringLteReAttach(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            if (ModemProvider.IS_MTK) {
                HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(this.mContext);
                RilhookHandler rilhookHandler = this.mRilhookHandler;
                Objects.requireNonNull(rilhookHandler);
                hyperRadioManager.dataConnectionDetach(i, 0, new RilHook$$ExternalSyntheticLambda0(rilhookHandler), new AnonymousClass3(i));
                return;
            }
            if (ModemProvider.IS_XRING) {
                HyperRadioManager.getInstance(this.mContext).lteReattach(i, new ProfileInstallReceiver$$ExternalSyntheticLambda0(), new HyperRadioManager.GenericResponse() { // from class: com.android.phone.RilHook.4
                    public void onResponse(int i2) {
                        RilHook.this.log("lteReAttach done with errorCode " + i2);
                    }
                });
            }
        }
    }

    /* JADX INFO: renamed from: com.android.phone.RilHook$3, reason: invalid class name */
    class AnonymousClass3 extends HyperRadioManager.GenericResponse {
        final /* synthetic */ int val$slotId;

        AnonymousClass3(int i) {
            this.val$slotId = i;
        }

        public void onResponse(int i) {
            RilHook.this.log("dataConnectionDetach done with errorCode " + i);
            if (i != 0) {
                return;
            }
            RilhookHandler rilhookHandler = RilHook.this.mRilhookHandler;
            final int i2 = this.val$slotId;
            rilhookHandler.postDelayed(new Runnable() { // from class: com.android.phone.RilHook$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onResponse$0(i2);
                }
            }, 100L);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResponse$0(int i) {
            HyperRadioManager hyperRadioManager = HyperRadioManager.getInstance(RilHook.this.mContext);
            RilhookHandler rilhookHandler = RilHook.this.mRilhookHandler;
            Objects.requireNonNull(rilhookHandler);
            hyperRadioManager.dataConnectionAttach(i, 0, new RilHook$$ExternalSyntheticLambda0(rilhookHandler), new HyperRadioManager.GenericResponse() { // from class: com.android.phone.RilHook.3.1
                public void onResponse(int i2) {
                    RilHook.this.log("dataConnectionAttach with errorCode " + i2);
                }
            });
        }
    }

    private void mtkSetL2NrSelection(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(i);
            if (TextUtils.isEmpty(networkNumeric)) {
                return;
            }
            String forceNRCMDforMtk = getForceNRCMDforMtk(i, networkNumeric);
            Log.d("QmsClient", "mtk setL2NrSelection cmd:" + forceNRCMDforMtk);
            FiveGManagerBase.getInstance().sendAtCmdSync(i, new String[]{forceNRCMDforMtk, ""}, null);
        }
    }

    private String getForceNRCMDforMtk(int i, String str) {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null && phone.getDataConnectionState("default") == PhoneConstants.DataState.CONNECTED && defaultDataSlotId == i && MiuiPhoneUtils.isMtkForceSearchNrSupported()) {
            return "AT+ERETNR";
        }
        return "AT+EOPS=7,2,\"" + str + "\",11,1,-110";
    }

    private void mtkForceLte(int i) {
        if (MiuiPhoneUtils.isValidatePhoneId(i)) {
            String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(i);
            if (TextUtils.isEmpty(networkNumeric)) {
                return;
            }
            String forceLteCMDforMtk = getForceLteCMDforMtk(i, networkNumeric);
            log("mtkForceLte cmd:" + forceLteCMDforMtk);
            FiveGManagerBase.getInstance().sendAtCmdSync(i, new String[]{forceLteCMDforMtk, ""}, null);
        }
    }

    private String getForceLteCMDforMtk(int i, String str) {
        int defaultDataSlotId = DefaultSimManager.getDefaultDataSlotId();
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone != null && phone.getDataConnectionState("default") == PhoneConstants.DataState.CONNECTED && defaultDataSlotId == i && MiuiPhoneUtils.isMtkForceSearchNrSupported()) {
            return "AT+ERETLTE";
        }
        return "AT+EOPS=7,2,\"" + str + "\",7,1,-110";
    }

    public void setGamingModeStatus(int i, int i2) {
        if (featureSetCmn(51, i2, i)) {
            return;
        }
        log("setGamingModeStatus failed");
    }

    public void setProcTypeInfo(int i, int i2) {
        try {
            if (ModemProvider.IS_QCOM) {
                if (!this.mQcrilHookReady) {
                    log("setProcTypeInfo, qcril is not ready");
                    return;
                }
                Class cls = this.mHookCls;
                Class<?> cls2 = Integer.TYPE;
                Method declaredMethod = cls.getDeclaredMethod("setProcTypeInfo", cls2, cls2);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(this.mQcHook, Integer.valueOf(i), Integer.valueOf(i2));
                log("setProcTypeInfo proc = " + i + ", phoneId = " + i2);
            }
        } catch (Exception e) {
            log("setProcTypeInfo Exception: " + e);
        }
    }

    public ByteBuffer getLteBestNbrMeasInfo(int i) {
        ByteBuffer byteBuffer = null;
        try {
            if (!ModemProvider.IS_QCOM) {
                return null;
            }
            if (!this.mQcrilHookReady) {
                log("getLteBestNbrMeasInfo, qcril is not ready");
                return null;
            }
            Method declaredMethod = this.mHookCls.getDeclaredMethod("getLTEBestNbrMeasInfo", Integer.TYPE);
            declaredMethod.setAccessible(true);
            ByteBuffer byteBuffer2 = (ByteBuffer) declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
            try {
                log("getNr5gBestNbrMeasInfo phoneId = " + i);
                return byteBuffer2;
            } catch (Exception e) {
                e = e;
                byteBuffer = byteBuffer2;
                log("getLteBestNbrMeasInfo Exception: " + e);
                return byteBuffer;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public ByteBuffer getNr5gBestNbrMeasInfo(int i) {
        ByteBuffer byteBuffer = null;
        try {
            if (!ModemProvider.IS_QCOM) {
                return null;
            }
            if (!this.mQcrilHookReady) {
                log("getNr5gBestNbrMeasInfo, qcril is not ready");
                return null;
            }
            Method declaredMethod = this.mHookCls.getDeclaredMethod("getNr5gBestNbrMeasInfo", Integer.TYPE);
            declaredMethod.setAccessible(true);
            ByteBuffer byteBuffer2 = (ByteBuffer) declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
            try {
                log("getNr5gBestNbrMeasInfo phoneId = " + i);
                return byteBuffer2;
            } catch (Exception e) {
                e = e;
                byteBuffer = byteBuffer2;
                log("getNr5gBestNbrMeasInfo Exception: " + e);
                return byteBuffer;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public boolean qcRilSetXiaomiBandMode(long[] jArr, int i) {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        if (!this.mQcrilHookReady) {
            log("qcRilSetLteBandPref, qcril is not ready");
            return false;
        }
        try {
            Method declaredMethod = this.mHookCls.getDeclaredMethod("qcRilSetXiaomiBandMode", long[].class, Integer.TYPE);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(this.mQcHook, jArr, Integer.valueOf(i));
            log("qcRilSetXiaomiBandMode bandMode  = " + jArr + ", phoneId = " + i);
            return ((Boolean) objInvoke).booleanValue();
        } catch (Exception e) {
            log("qcRilSetXiaomiBandMode Exception: " + e);
            return false;
        }
    }

    public boolean qcRilSetLteBandPref(int[] iArr, int i) {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        if (!this.mQcrilHookReady) {
            log("qcRilSetLteBandPref, qcril is not ready");
            return false;
        }
        try {
            Method declaredMethod = this.mHookCls.getDeclaredMethod("qcRilSetLteBandPref", int[].class, Integer.TYPE);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(this.mQcHook, iArr, Integer.valueOf(i));
            log("qcRilSetLteBandPref bandMode = " + iArr + ", phoneId = " + i);
            return ((Boolean) objInvoke).booleanValue();
        } catch (Exception e) {
            log("qcRilSetLteBandPref Exception: " + e);
            return false;
        }
    }

    public boolean qcRilDeprioritizeNR5gAsync(boolean z, int i) {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        if (!this.mQcrilHookReady) {
            log("qcRilDeprioritizeNR5gAsync, qcril is not ready");
            return false;
        }
        try {
            Method declaredMethod = this.mHookCls.getDeclaredMethod("qcRilDeprioritizeNR5gAsync", Boolean.TYPE, Integer.TYPE);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(this.mQcHook, Boolean.valueOf(z), Integer.valueOf(i));
            log("qcRilDeprioritizeNR5gAsync value = " + z + ", phoneId = " + i);
            return ((Boolean) objInvoke).booleanValue();
        } catch (Exception e) {
            log("qcRilDeprioritizeNR5gAsync Exception: " + e);
            return false;
        }
    }

    public byte[] qcRilUpdateEccList(int i, int i2, int i3, byte[] bArr) {
        if (!ModemProvider.IS_QCOM) {
            return null;
        }
        if (!this.mQcrilHookReady) {
            log("qcRilSetLteBandPref, qcril is not ready");
            return null;
        }
        try {
            Class cls = this.mHookCls;
            Class<?> cls2 = Integer.TYPE;
            Method declaredMethod = cls.getDeclaredMethod("qcRilFeatureQueryCmn", cls2, cls2, cls2, byte[].class);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(this.mQcHook, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), bArr);
            log("qcRilFeatureQueryCmn featureType = " + i + ", phoneId = " + i3);
            return (byte[]) objInvoke;
        } catch (Exception e) {
            log("qcRilFeatureQueryCmn Exception: " + e);
            return null;
        }
    }

    public void qcGetSIMLockState(int i, Message message) {
        if (!ModemProvider.IS_QCOM) {
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            }
            return;
        }
        if (!this.mQcrilHookReady) {
            log("qcGetSIMLockState, qcril is not ready");
            if (message != null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(1));
                message.sendToTarget();
                return;
            }
            return;
        }
        try {
            Method declaredMethod = this.mHookCls.getDeclaredMethod("getSIMLockState", Integer.TYPE, Message.class);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(this.mQcHook, Integer.valueOf(i), message);
            log("qcGetSIMLockState mRfState = " + i);
        } catch (Exception e) {
            log("qcGetSIMLockState Exception: " + e);
        }
    }

    public ByteBuffer getQmiIndInfo() {
        ByteBuffer byteBuffer = null;
        try {
            if (!ModemProvider.IS_QCOM) {
                return null;
            }
            if (!this.mQcrilHookReady) {
                log("getQmiIndInfo, qcril is not ready");
                return null;
            }
            Class[] clsArr = new Class[0];
            Method declaredMethod = this.mHookCls.getDeclaredMethod("getQmiIndInfo", null);
            declaredMethod.setAccessible(true);
            ByteBuffer byteBuffer2 = (ByteBuffer) declaredMethod.invoke(this.mQcHook, null);
            try {
                StringBuilder sb = new StringBuilder("getQmiIndInfo result = ");
                sb.append(byteBuffer2 == null);
                log(sb.toString());
                return byteBuffer2;
            } catch (Exception e) {
                e = e;
                byteBuffer = byteBuffer2;
                log("getQmiIndInfo Exception: " + e);
                return byteBuffer;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public boolean qcRilPerformIncrManualScan(int i) {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        if (!this.mQcrilHookReady) {
            log("qcRilPerformIncrManualScan, qcril is not ready");
            return false;
        }
        try {
            Method declaredMethod = this.mHookCls.getDeclaredMethod("qcRilPerformIncrManualScan", Integer.TYPE);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
            log("qcRilPerformIncrManualScan phoneId = " + i);
            return ((Boolean) objInvoke).booleanValue();
        } catch (Exception e) {
            log("qcRilPerformIncrManualScan Exception: " + e);
            return false;
        }
    }

    public boolean qcRilAbortNetworkScan(int i) {
        if (!ModemProvider.IS_QCOM) {
            return false;
        }
        if (!this.mQcrilHookReady) {
            log("qcRilAbortNetworkScan, qcril is not ready");
            return false;
        }
        try {
            Method declaredMethod = this.mHookCls.getDeclaredMethod("qcRilAbortNetworkScan", Integer.TYPE);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
            log("qcRilAbortNetworkScan phoneId = " + i);
            return ((Boolean) objInvoke).booleanValue();
        } catch (Exception e) {
            log("qcRilAbortNetworkScan Exception: " + e);
            return false;
        }
    }

    public int getDeviceNetworkPersonalizedNum() {
        if (!ModemProvider.IS_QCOM) {
            return -1;
        }
        try {
            if (!this.mQcrilHookReady) {
                log("getDeviceNetworkPersonalizedNum, qcril is not ready");
                return -1;
            }
            Class[] clsArr = new Class[0];
            Method declaredMethod = this.mHookCls.getDeclaredMethod("getDeviceNetworkPersonalizedNum", null);
            declaredMethod.setAccessible(true);
            return ((Integer) declaredMethod.invoke(this.mQcHook, null)).intValue();
        } catch (Exception e) {
            log("getDeviceNetworkPersonalizedNum Exception: " + e);
            return -1;
        }
    }

    public ByteBuffer getLteRrcPowerStats(int i) {
        ByteBuffer byteBuffer = null;
        try {
            if (!ModemProvider.IS_QCOM) {
                return null;
            }
            if (!this.mQcrilHookReady) {
                log("getLteRrcPowerStats, qcril is not ready");
                return null;
            }
            boolean z = true;
            Method declaredMethod = this.mHookCls.getDeclaredMethod("getLteRrcPowerStats", Integer.TYPE);
            declaredMethod.setAccessible(true);
            ByteBuffer byteBuffer2 = (ByteBuffer) declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
            try {
                StringBuilder sb = new StringBuilder("getLteRrcPowerStats resultIsNull = ");
                if (byteBuffer2 != null) {
                    z = false;
                }
                sb.append(z);
                sb.append(", phoneId = ");
                sb.append(i);
                log(sb.toString());
                return byteBuffer2;
            } catch (Exception e) {
                e = e;
                byteBuffer = byteBuffer2;
                log("getLteRrcPowerStats Exception: " + e);
                return byteBuffer;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    public ByteBuffer getNr5gRrcPowerStats(int i) {
        ByteBuffer byteBuffer = null;
        try {
            if (!ModemProvider.IS_QCOM) {
                return null;
            }
            if (!this.mQcrilHookReady) {
                log("getNr5gRrcPowerStats, qcril is not ready");
                return null;
            }
            boolean z = true;
            Method declaredMethod = this.mHookCls.getDeclaredMethod("getNr5gRrcPowerStats", Integer.TYPE);
            declaredMethod.setAccessible(true);
            ByteBuffer byteBuffer2 = (ByteBuffer) declaredMethod.invoke(this.mQcHook, Integer.valueOf(i));
            try {
                StringBuilder sb = new StringBuilder("getNr5gRrcPowerStats resultIsNull = ");
                if (byteBuffer2 != null) {
                    z = false;
                }
                sb.append(z);
                sb.append(", phoneId = ");
                sb.append(i);
                log(sb.toString());
                return byteBuffer2;
            } catch (Exception e) {
                e = e;
                byteBuffer = byteBuffer2;
                log("getNr5gRrcPowerStats Exception: " + e);
                return byteBuffer;
            }
        } catch (Exception e2) {
            e = e2;
        }
    }
}
