package com.android.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.spradar.SpradarFuncInfo;
import com.android.phone.spradar.SpradarFuncInfoFactory;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class SprdModemDump extends ModemDumpBase {
    private BroadcastReceiver mBroadcastReceiver;
    private Context mContext;
    private final DisplayManager.DisplayListener mDisplayListener;
    private final Handler mHandler;
    private boolean mIsScreenOn;
    private final Object mRadioInteractor;
    private Method mSendAtCmdMethod;
    private static final int PHONE_COUNT = MiuiPhoneUtils.PHONE_COUNT;
    private static ConcurrentHashMap mSpradarStatusMap = new ConcurrentHashMap();
    private static boolean VDBG = false;
    private static final Pattern INFOU_PATTERN = Pattern.compile("^(\\+SPRADARINFO[U]?: )(\\d+),(\\d+),(.+)$");
    private Set mSpradarConnFuncId = new HashSet(Arrays.asList(20003, 20004, 20005, 33033));
    private final Object[] mListeners = new Object[PHONE_COUNT];
    private final Object mFileLock = new Object();
    private boolean mAllSpradarEnable = false;

    @Override // com.android.phone.ModemDumpBase
    public boolean isModemDumpFeatureSupported(int i) {
        return true;
    }

    SprdModemDump() {
        boolean z = false;
        this.mIsScreenOn = false;
        DisplayManager.DisplayListener displayListener = new DisplayManager.DisplayListener() { // from class: com.android.phone.SprdModemDump.1
            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayAdded(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayRemoved(int i) {
            }

            @Override // android.hardware.display.DisplayManager.DisplayListener
            public void onDisplayChanged(int i) {
                DisplayManager displayManager;
                Display display;
                if (i != 0 || SprdModemDump.this.mContext == null || (displayManager = (DisplayManager) SprdModemDump.this.mContext.getSystemService("display")) == null || (display = displayManager.getDisplay(0)) == null) {
                    return;
                }
                int state = display.getState();
                SprdModemDump sprdModemDump = SprdModemDump.this;
                if (state == 2) {
                    sprdModemDump.mIsScreenOn = true;
                    SprdModemDump.this.mHandler.sendMessage(SprdModemDump.this.mHandler.obtainMessage(4));
                } else {
                    sprdModemDump.mIsScreenOn = false;
                    SprdModemDump.this.mHandler.sendMessageDelayed(SprdModemDump.this.mHandler.obtainMessage(5), 30000L);
                }
            }
        };
        this.mDisplayListener = displayListener;
        this.mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.phone.SprdModemDump.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if ("com.android.phone.modemdump.sprd".equals(action)) {
                    SprdModemDump.VDBG = true;
                    SprdModemDump.this.queryAllSpradar();
                } else if ("com.android.phone.modemdump.sprdtest".equals(action)) {
                    SprdModemDump.VDBG = true;
                }
            }
        };
        this.mContext = PhoneGlobals.getInstance();
        this.mHandler = new WorkHandler(Looper.getMainLooper());
        if (this.mContext == null) {
            loge("SprdModemDump: context is null");
            this.mRadioInteractor = null;
            return;
        }
        this.mRadioInteractor = initRadioInteractor();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.phone.modemdump.sprd");
        intentFilter.addAction("com.android.phone.modemdump.sprdtest");
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 4);
        DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService("display");
        if (displayManager != null) {
            displayManager.registerDisplayListener(displayListener, null);
            Display display = displayManager.getDisplay(0);
            if (display != null && display.getState() == 2) {
                z = true;
            }
            this.mIsScreenOn = z;
        }
        registerModemRestart();
        enableRegisterSpradar(true);
    }

    private Object initRadioInteractor() {
        try {
            Class<?> cls = Class.forName("com.android.unisoc.telephony.RadioInteractor");
            Object objNewInstance = cls.getConstructor(Context.class).newInstance(this.mContext);
            this.mSendAtCmdMethod = cls.getMethod("sendAtCmd", String.class, String[].class, Integer.TYPE);
            return objNewInstance;
        } catch (Exception e) {
            loge("Failed to init RadioInteractor: " + e);
            return null;
        }
    }

    private int sendAtCmd(String str, String[] strArr, int i) {
        Method method;
        Object obj = this.mRadioInteractor;
        if (obj != null && (method = this.mSendAtCmdMethod) != null) {
            try {
                return ((Integer) method.invoke(obj, str, strArr, Integer.valueOf(i))).intValue();
            } catch (Exception e) {
                loge("sendAtCmd error: " + e);
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerModemRestart() {
        Phone phone = PhoneFactory.getPhone(0);
        if (phone != null) {
            phone.mCi.registerForModemReset(this.mHandler, 6, (Object) null);
        } else {
            logd("registerModemRestart: phone not ready, retrying...");
            this.mHandler.sendEmptyMessageDelayed(8, MirrorDesktopHelper.TIMEOUT_MILLIS);
        }
    }

    @Override // com.android.phone.ModemDumpBase
    public void destroy() {
        enableAllSpradar(false, 2);
        unRegisterSpradar();
        Phone phone = PhoneFactory.getPhone(0);
        if (phone != null) {
            phone.mCi.unregisterForModemReset(this.mHandler);
        }
        this.mHandler.removeCallbacksAndMessages(null);
        Context context = this.mContext;
        if (context != null) {
            try {
                context.unregisterReceiver(this.mBroadcastReceiver);
            } catch (IllegalArgumentException e) {
                loge("destroy unregisterReceiver error: " + e);
            }
            DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService("display");
            if (displayManager != null) {
                displayManager.unregisterDisplayListener(this.mDisplayListener);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableRegisterSpradar(boolean z) {
        Message messageObtainMessage = this.mHandler.obtainMessage(1);
        messageObtainMessage.arg1 = z ? 1 : 0;
        this.mHandler.sendMessage(messageObtainMessage);
    }

    private Set getFuncIdByType(int i) {
        if (i == 1) {
            return SpradarFuncInfoFactory.getSpradarFuncIds(0, 1);
        }
        if (i == 2) {
            return this.mSpradarConnFuncId;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enableAllSpradar(boolean z, int i) {
        Set funcIdByType = getFuncIdByType(i);
        if (funcIdByType == null || funcIdByType.isEmpty() || this.mRadioInteractor == null) {
            return;
        }
        String str = (String) funcIdByType.stream().map(new SprdModemDump$$ExternalSyntheticLambda1()).collect(Collectors.joining(","));
        logv("enableAllSpradar funcIds: " + str);
        String str2 = String.format("AT+SPRADAR=%s,\"%s\"", Integer.valueOf(z ? 1 : 0), str);
        this.mAllSpradarEnable = false;
        int i2 = 0;
        boolean z2 = false;
        while (i2 < PHONE_COUNT) {
            String[] strArr = {"", "", ""};
            sendAtCmd(str2, strArr, i2);
            logv("enableAllSpradar slot " + i2 + " result[0]: " + strArr[0]);
            String str3 = strArr[0];
            if (str3 == null || !str3.contains("OK")) {
                loge("enableAllSpradar: failed on slot " + i2);
            } else {
                z2 = true;
            }
            i2++;
            z2 = z2;
        }
        if (!z2) {
            loge("enableAllSpradar: failed on all slots!");
            return;
        }
        if (z && i == 1) {
            logd("enabled all funcIds: " + str);
            this.mAllSpradarEnable = true;
            return;
        }
        if (!z && i == 1) {
            logd("disabled all funcIds: " + str);
        } else {
            if (z || i != 2) {
                return;
            }
            logd("disabled connection funcIds: " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerSpradar() {
        logv("registerSpradar");
        if (this.mRadioInteractor == null) {
            return;
        }
        try {
            Class<?> cls = Class.forName("com.android.unisoc.telephony.RadioInteractorListener");
            int i = cls.getField("LISTEN_AT_CMD_EVENT").getInt(null);
            cls.getField("LISTEN_NONE").getInt(null);
            Method method = this.mRadioInteractor.getClass().getMethod("listen", cls, Integer.TYPE, Boolean.TYPE);
            for (int i2 = 0; i2 < PHONE_COUNT; i2++) {
                Proxy.newProxyInstance(cls.getClassLoader(), new Class[0], new InvocationHandler() { // from class: com.android.phone.SprdModemDump$$ExternalSyntheticLambda2
                    @Override // java.lang.reflect.InvocationHandler
                    public final Object invoke(Object obj, Method method2, Object[] objArr) {
                        return SprdModemDump.$r8$lambda$5xqTdnzGWIGLRkueV1VLFjEDX7c(obj, method2, objArr);
                    }
                });
                this.mListeners[i2] = cls.getConstructor(Integer.TYPE, Looper.class).newInstance(Integer.valueOf(i2), Looper.getMainLooper());
                method.invoke(this.mRadioInteractor, this.mListeners[i2], Integer.valueOf(i), Boolean.TRUE);
            }
        } catch (Exception e) {
            loge("registerSpradar error: " + e);
        }
    }

    public static /* synthetic */ Object $r8$lambda$5xqTdnzGWIGLRkueV1VLFjEDX7c(Object obj, Method method, Object[] objArr) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterSpradar() {
        try {
            if (this.mRadioInteractor == null) {
                return;
            }
            Class<?> cls = Class.forName("com.android.unisoc.telephony.RadioInteractorListener");
            int i = cls.getField("LISTEN_AT_CMD_EVENT").getInt(null);
            Method method = this.mRadioInteractor.getClass().getMethod("listen", cls, Integer.TYPE, Boolean.TYPE);
            for (int i2 = 0; i2 < PHONE_COUNT; i2++) {
                Object obj = this.mListeners[i2];
                if (obj != null) {
                    method.invoke(this.mRadioInteractor, obj, Integer.valueOf(i), Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            loge("unRegisterSpradar error: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queryAllSpradar() {
        Set spradarFuncIds;
        if (this.mRadioInteractor == null || (spradarFuncIds = SpradarFuncInfoFactory.getSpradarFuncIds(1)) == null || spradarFuncIds.isEmpty()) {
            return;
        }
        Iterator it = spradarFuncIds.iterator();
        while (it.hasNext()) {
            String strValueOf = String.valueOf((Integer) it.next());
            for (int i = 0; i < PHONE_COUNT; i++) {
                String[] strArr = {"", "", ""};
                sendAtCmd(String.format("AT+SPRADARINFO=%s,%s", strValueOf, Integer.valueOf(i)), strArr, i);
                String str = strArr[0];
                if (str != null && str.contains("+SPRADARINFO:")) {
                    String strTrim = str.replace("\nOK", "").replace("\r", "").trim();
                    logv("queryAllSpradar: " + strTrim);
                    Handler handler = this.mHandler;
                    handler.sendMessage(handler.obtainMessage(2, i, 0, strTrim));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void parseSpradarInfo(int i, String str) {
        String str2;
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            String[] strArr = parse(str);
            logv("parseSpradarInfo: values = " + Arrays.toString(strArr));
            if (strArr != null && strArr.length > 3 && (str2 = strArr[1]) != null && strArr[3] != null) {
                try {
                    List<SpradarFuncInfo> listCreateFuncInfo = SpradarFuncInfoFactory.createFuncInfo(i, Integer.parseInt(str2), strArr[3]);
                    if (listCreateFuncInfo != null && !listCreateFuncInfo.isEmpty()) {
                        for (SpradarFuncInfo spradarFuncInfo : listCreateFuncInfo) {
                            logv("parseSpradarInfo: " + spradarFuncInfo);
                            dumpModemInfo(spradarFuncInfo.toString());
                        }
                    }
                } catch (NumberFormatException unused) {
                    loge("parseSpradarInfo: invalid funcId: " + strArr[1]);
                }
            } else {
                loge("parseSpradarInfo: values is invalid");
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public static String[] parse(String str) {
        if (str != null && !str.isEmpty()) {
            Matcher matcher = INFOU_PATTERN.matcher(str);
            if (matcher.matches()) {
                return new String[]{matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4)};
            }
        }
        return null;
    }

    @Override // com.android.phone.ModemDumpBase
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        dump(fileDescriptor, printWriter, strArr, false);
    }

    @Override // com.android.phone.ModemDumpBase
    public void dump(final FileDescriptor fileDescriptor, final PrintWriter printWriter, String[] strArr, final boolean z) {
        new Thread(new Runnable() { // from class: com.android.phone.SprdModemDump$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dump$1(fileDescriptor, printWriter, z);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$1(FileDescriptor fileDescriptor, PrintWriter printWriter, boolean z) {
        try {
            queryAllSpradar();
            dumpCacheFile(fileDescriptor, printWriter, z);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dumpModemInfo(String str) {
        synchronized (this.mFileLock) {
            try {
                initCacheFile();
                writeToCache(str);
                closeCacheFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dumpCacheFile(FileDescriptor fileDescriptor, PrintWriter printWriter, boolean z) {
        synchronized (this.mFileLock) {
            File file = new File("/sdcard/MIUI/debug_log/common/Modem/modemdump_cache.dump");
            if (file.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                    try {
                        LinkedList<String> linkedList = new LinkedList();
                        while (true) {
                            String line = bufferedReader.readLine();
                            if (line == null) {
                                break;
                            }
                            if (linkedList.size() >= 1000) {
                                linkedList.poll();
                            }
                            linkedList.offer(line);
                        }
                        for (String str : linkedList) {
                            if (z && printWriter != null && fileDescriptor != null && fileDescriptor.valid()) {
                                printWriter.println(str);
                            } else {
                                logd(str);
                            }
                        }
                        bufferedReader.close();
                    } catch (Throwable th) {
                        try {
                            bufferedReader.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    removeMessages(1);
                    int i = message.arg1;
                    SprdModemDump sprdModemDump = SprdModemDump.this;
                    if (i == 1) {
                        sprdModemDump.enableAllSpradar(true, 1);
                        SprdModemDump.this.unRegisterSpradar();
                        sendEmptyMessageDelayed(3, 3000L);
                    } else {
                        sprdModemDump.enableAllSpradar(false, 2);
                    }
                    break;
                case 2:
                    SprdModemDump.this.parseSpradarInfo(message.arg1, (String) message.obj);
                    break;
                case 3:
                    SprdModemDump.this.registerSpradar();
                    break;
                case 4:
                    removeMessages(4);
                    if (SprdModemDump.this.mIsScreenOn && !SprdModemDump.this.mAllSpradarEnable) {
                        SprdModemDump.this.enableRegisterSpradar(true);
                        break;
                    }
                    break;
                case 5:
                    removeMessages(5);
                    if (!SprdModemDump.this.mIsScreenOn) {
                        SprdModemDump.this.enableRegisterSpradar(false);
                    }
                    break;
                case 6:
                    sendEmptyMessageDelayed(7, 30000L);
                    break;
                case 7:
                    if (SprdModemDump.this.mIsScreenOn) {
                        SprdModemDump.this.enableRegisterSpradar(true);
                    }
                    break;
                case 8:
                    SprdModemDump.this.registerModemRestart();
                    break;
            }
        }
    }

    private static void logd(String str) {
        Log.d("SprdModemDump", str);
    }

    private void loge(String str) {
        Log.e("SprdModemDump", str);
    }

    private static void logv(String str) {
        if (VDBG) {
            Log.d("SprdModemDump", str);
        }
    }
}
