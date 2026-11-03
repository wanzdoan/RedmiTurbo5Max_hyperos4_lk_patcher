package com.android.phone.statistics;

import android.app.ActivityThread;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.RadioConfig;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.phone.FiveGCloudUtils;
import com.android.phone.FiveGManagerBase;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.phone.module.IModemModule;
import com.android.phone.performance.PreferenceUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.android.phone.utils.MiuiThreadUtil;
import com.ot.pubsub.util.v;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import miui.telephony.PhoneDebug;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AftersaleController {
    private static AftersaleController sController;
    private boolean mCloudEnable;
    private final Context mContext;
    private final Handler mHandler = new Handler(MiuiThreadUtil.getWorkThreadLooper()) { // from class: com.android.phone.statistics.AftersaleController.2
        @Override // android.os.Handler
        public void handleMessage(Message message) throws Throwable {
            if (message.what != 1) {
                return;
            }
            AftersaleController.this.updateCloudFeatureStatus();
            if (AftersaleController.this.mCloudEnable) {
                long jElapsedRealtime = SystemClock.elapsedRealtime();
                if (jElapsedRealtime - AftersaleController.this.mLastPersistTime > v.b) {
                    AftersaleController.this.mLastPersistTime = jElapsedRealtime;
                    try {
                        AftersaleController.this.writeToDataArchive(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
    private long mLastPersistTime;

    private AftersaleController() {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        this.mContext = phoneGlobals;
        this.mCloudEnable = PreferenceUtils.getInstance().getBooleanPreference("cloud_aftersale_enable", Boolean.TRUE).booleanValue();
        EventHubManager.getInstance().registerListener("telephony", "cloud_update", new BaseEvent.IEventListener() { // from class: com.android.phone.statistics.AftersaleController$$ExternalSyntheticLambda0
            @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
            public final void onEvent(BaseEvent baseEvent) {
                this.f$0.lambda$new$0(baseEvent);
            }
        });
        IntentFilter intentFilter = new IntentFilter("com.android.phone.FACTORY_RESET");
        intentFilter.addAction("com.android.phone.nvram_read");
        phoneGlobals.registerReceiver(new AnonymousClass1(), intentFilter, 2);
        initDataFromNvram();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(BaseEvent baseEvent) {
        if (baseEvent instanceof TelephonyEvent.CloudEvent) {
            this.mHandler.sendEmptyMessage(1);
        }
    }

    /* JADX INFO: renamed from: com.android.phone.statistics.AftersaleController$1, reason: invalid class name */
    class AnonymousClass1 extends BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            AftersaleController.this.log("onReceive:" + action);
            if ("com.android.phone.nvram_read".equals(action)) {
                AftersaleController.this.mHandler.post(new Runnable() { // from class: com.android.phone.statistics.AftersaleController$1$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() throws Throwable {
                        this.f$0.lambda$onReceive$0();
                    }
                });
            } else if ("com.android.phone.FACTORY_RESET".equals(action) && AftersaleController.this.mCloudEnable) {
                AftersaleController.this.mHandler.post(new Runnable() { // from class: com.android.phone.statistics.AftersaleController$1$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() throws Throwable {
                        this.f$0.lambda$onReceive$1();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() throws Throwable {
            AftersaleController.this.exportAllToExternal();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1() throws Throwable {
            try {
                AftersaleController.this.writeToDataArchive(false);
                AftersaleController.this.writeToNvram();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isAftersaleInfoSupported() {
        return (PhoneGlobals.getInstance() != null ? PhoneGlobals.getInstance() : ActivityThread.currentApplication()).getResources().getBoolean(R.bool.config_is_aftersale_info_supported);
    }

    public static AftersaleController make() {
        if (sController == null && isAftersaleInfoSupported()) {
            sController = new AftersaleController();
        }
        return sController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("AftersaleController", str);
    }

    private void logV(String str) {
        if (PhoneDebug.VDBG) {
            Log.d("AftersaleController", str);
        }
    }

    /* JADX WARN: Code duplicated, block: B:13:0x004e  */
    /* JADX WARN: Code duplicated, block: B:7:0x002e  */
    private String buildMajorText(boolean z) {
        boolean z2;
        StringBuilder sb = new StringBuilder();
        boolean z3 = true;
        if (z) {
            z2 = false;
        } else {
            sb.append(FiveGManagerBase.getInstance().getEncryptedIds());
            sb.append("\n");
            StringBuilder crashInfo = parseCrashInfo();
            if (crashInfo.length() > 0) {
                sb.append("modem crash:\n");
                sb.append((CharSequence) crashInfo);
                z2 = true;
            } else {
                z2 = false;
            }
        }
        if (ModuleDelegate.get(IModemModule.class) != null) {
            JSONObject aftersaleFromHydra = ((IModemModule) ModuleDelegate.get(IModemModule.class)).getAftersaleFromHydra();
            if (aftersaleFromHydra.length() > 0) {
                sb.append(aftersaleFromHydra);
                sb.append("\n");
            } else {
                z3 = z2;
            }
        } else {
            z3 = z2;
        }
        if (z3) {
            sb.insert(0, getCurrentDateTime() + "\n");
        }
        return sb.toString();
    }

    private String getCurrentDateTime() {
        return new SimpleDateFormat("<yyyy-MM-dd HH:mm>").format(new Date(System.currentTimeMillis()));
    }

    private String buildCriticalText() {
        StringBuilder sb = new StringBuilder();
        StringBuilder modemDumpAftersaleInfo = ((IModemModule) ModuleDelegate.get(IModemModule.class)).getModemDumpAftersaleInfo();
        if (modemDumpAftersaleInfo.length() > 0) {
            sb.append(getCurrentDateTime());
            sb.append("\n");
            sb.append((CharSequence) modemDumpAftersaleInfo);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeToDataArchive(boolean z) throws Throwable {
        String strBuildCriticalText = buildCriticalText();
        String strBuildMajorText = buildMajorText(z);
        long jTryWrite = 0;
        for (int i = 5; i > 0; i--) {
            jTryWrite = tryWrite(strBuildCriticalText, strBuildMajorText, i);
            if (jTryWrite < 524240) {
                break;
            }
        }
        recordFeatureStats(jTryWrite > 0 || !z, !z);
    }

    private void recordFeatureStats(boolean z, boolean z2) {
        long length;
        if (z) {
            HashMap map = new HashMap();
            File fileStreamPath = this.mContext.getFileStreamPath("aftersale");
            if (fileStreamPath == null || !fileStreamPath.exists()) {
                length = -1;
            } else {
                length = fileStreamPath.length();
                if (length > 524264) {
                    map.put("oversize", Boolean.valueOf(fileStreamPath.delete()));
                }
            }
            map.put("size", Long.valueOf(length));
            map.put("reset", Boolean.valueOf(z2));
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "aftersale_stats", map);
        }
    }

    private long tryWrite(String str, String str2, int i) throws Throwable {
        Throwable th;
        Throwable th2;
        Throwable th3;
        byte[] bytes;
        FileInputStream fileInputStream;
        Throwable th4;
        long j = -1;
        if (TextUtils.isEmpty(str2) && TextUtils.isEmpty(str)) {
            return -1L;
        }
        logV(str);
        logV(str2);
        File fileStreamPath = this.mContext.getFileStreamPath("aftersale.tmp");
        File fileStreamPath2 = this.mContext.getFileStreamPath("aftersale");
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileStreamPath);
                try {
                    try {
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        try {
                            try {
                                ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);
                                try {
                                    if (TextUtils.isEmpty(str)) {
                                        bytes = null;
                                    } else {
                                        try {
                                            bytes = str.getBytes(StandardCharsets.UTF_8);
                                        } catch (Throwable th5) {
                                            th3 = th5;
                                            j = -1;
                                            try {
                                                zipOutputStream.close();
                                                throw th3;
                                            } catch (Throwable th6) {
                                                th3.addSuppressed(th6);
                                                throw th3;
                                            }
                                        }
                                    }
                                    byte[] bytes2 = !TextUtils.isEmpty(str2) ? str2.getBytes(StandardCharsets.UTF_8) : null;
                                    try {
                                        if (fileStreamPath2.exists()) {
                                            FileInputStream fileInputStream2 = new FileInputStream(fileStreamPath2);
                                            try {
                                                try {
                                                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream2);
                                                    try {
                                                        ZipInputStream zipInputStream = new ZipInputStream(bufferedInputStream);
                                                        fileInputStream = fileInputStream2;
                                                        int i2 = 0;
                                                        byte[] bArr = null;
                                                        byte[] bArr2 = null;
                                                        while (i2 < 2) {
                                                            try {
                                                                try {
                                                                    ZipEntry nextEntry = zipInputStream.getNextEntry();
                                                                    if (nextEntry == null) {
                                                                        break;
                                                                    }
                                                                    int i3 = i2;
                                                                    logV("read entry:" + nextEntry.getName());
                                                                    byte[] allBytes = zipInputStream.readAllBytes();
                                                                    if ("Critical".equals(nextEntry.getName())) {
                                                                        bArr = allBytes;
                                                                    } else if ("Major".equals(nextEntry.getName())) {
                                                                        bArr2 = allBytes;
                                                                    }
                                                                    i2 = i3 + 1;
                                                                } catch (Throwable th7) {
                                                                    th = th7;
                                                                    Throwable th8 = th;
                                                                    try {
                                                                        bufferedInputStream.close();
                                                                        throw th8;
                                                                    } catch (Throwable th9) {
                                                                        th8.addSuppressed(th9);
                                                                        throw th8;
                                                                    }
                                                                }
                                                            } catch (Throwable th10) {
                                                                try {
                                                                    zipInputStream.close();
                                                                    throw th10;
                                                                } catch (Throwable th11) {
                                                                    th10.addSuppressed(th11);
                                                                    throw th10;
                                                                }
                                                            }
                                                        }
                                                        if (bytes != null || bArr != null) {
                                                            zipOutputStream.putNextEntry(new ZipEntry("Critical"));
                                                            if (bytes != null) {
                                                                int i4 = i * 20480;
                                                                int iMin = Math.min(bytes.length, i4);
                                                                zipOutputStream.write(bytes, 0, iMin);
                                                                if (iMin < i4 && bArr != null) {
                                                                    zipOutputStream.write(bArr, 0, Math.min(bArr.length, i4 - iMin));
                                                                }
                                                            } else {
                                                                zipOutputStream.write(bArr, 0, Math.min(i * 20480, bArr.length));
                                                            }
                                                        }
                                                        byte[] bArr3 = bArr2;
                                                        if (bytes2 != null || bArr3 != null) {
                                                            zipOutputStream.putNextEntry(new ZipEntry("Major"));
                                                            if (bytes2 != null) {
                                                                int i5 = i * 503784;
                                                                int iMin2 = Math.min(bytes2.length, i5);
                                                                zipOutputStream.write(bytes2, 0, iMin2);
                                                                if (iMin2 < i5 && bArr3 != null) {
                                                                    zipOutputStream.write(bArr3, 0, Math.min(bArr3.length, i5 - iMin2));
                                                                }
                                                            } else {
                                                                zipOutputStream.write(bArr3, 0, Math.min(i * 503784, bArr3.length));
                                                            }
                                                        }
                                                        zipInputStream.close();
                                                        bufferedInputStream.close();
                                                        fileInputStream.close();
                                                    } catch (Throwable th12) {
                                                        th = th12;
                                                        fileInputStream = fileInputStream2;
                                                    }
                                                } catch (Throwable th13) {
                                                    th = th13;
                                                    th4 = th;
                                                    try {
                                                        fileInputStream.close();
                                                        throw th4;
                                                    } catch (Throwable th14) {
                                                        th4.addSuppressed(th14);
                                                        throw th4;
                                                    }
                                                }
                                            } catch (Throwable th15) {
                                                th = th15;
                                                fileInputStream = fileInputStream2;
                                                th4 = th;
                                                fileInputStream.close();
                                                throw th4;
                                            }
                                        } else {
                                            if (bytes != null) {
                                                zipOutputStream.putNextEntry(new ZipEntry("Critical"));
                                                zipOutputStream.write(bytes, 0, Math.min(bytes.length, i * 20480));
                                            }
                                            if (bytes2 != null) {
                                                zipOutputStream.putNextEntry(new ZipEntry("Major"));
                                                zipOutputStream.write(bytes2, 0, Math.min(bytes2.length, i * 503784));
                                            }
                                        }
                                        zipOutputStream.close();
                                        bufferedOutputStream.close();
                                        fileOutputStream.close();
                                        long length = fileStreamPath.length();
                                        fileStreamPath.renameTo(fileStreamPath2);
                                        logV("tryWrite:" + length);
                                        return length;
                                    } catch (Throwable th16) {
                                        th = th16;
                                        th3 = th;
                                        zipOutputStream.close();
                                        throw th3;
                                    }
                                } catch (Throwable th17) {
                                    th = th17;
                                }
                            } catch (Throwable th18) {
                                th = th18;
                                j = -1;
                                th2 = th;
                                try {
                                    bufferedOutputStream.close();
                                    throw th2;
                                } catch (Throwable th19) {
                                    th2.addSuppressed(th19);
                                    throw th2;
                                }
                            }
                        } catch (Throwable th20) {
                            th = th20;
                            th2 = th;
                            bufferedOutputStream.close();
                            throw th2;
                        }
                    } catch (Throwable th21) {
                        th = th21;
                        th = th;
                        try {
                            fileOutputStream.close();
                            throw th;
                        } catch (Throwable th22) {
                            th.addSuppressed(th22);
                            throw th;
                        }
                    }
                } catch (Throwable th23) {
                    th = th23;
                    j = -1;
                    th = th;
                    fileOutputStream.close();
                    throw th;
                }
            } catch (Exception e) {
                e = e;
                j = -1;
                e.printStackTrace();
                return j;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return j;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeToNvram() {
        int iMin;
        try {
            if (!isLongSinceLastWrite()) {
                log("NvRam writes too frequently");
                return;
            }
            File fileStreamPath = this.mContext.getFileStreamPath("aftersale");
            if (fileStreamPath.exists()) {
                long jCurrentTimeMillis = System.currentTimeMillis();
                FileInputStream fileInputStream = new FileInputStream(fileStreamPath);
                try {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    try {
                        byte[] allBytes = bufferedInputStream.readAllBytes();
                        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(24);
                        byteBufferAllocate.putLong(jCurrentTimeMillis);
                        byteBufferAllocate.putInt(allBytes.length);
                        byte[] bArrArray = byteBufferAllocate.array();
                        if (allBytes.length + 24 > 524288) {
                            log("ERR encrypted data size exceeded limit");
                            bufferedInputStream.close();
                            fileInputStream.close();
                            return;
                        }
                        char[] cArr = {'A', 'B', 'C', 'D'};
                        int i = 0;
                        for (int i2 = 0; i2 < 4 && i < allBytes.length; i2++) {
                            byte[] bArr = new byte[131072];
                            if (i2 < 1) {
                                System.arraycopy(bArrArray, 0, bArr, 0, 24);
                                iMin = Math.min(131048, allBytes.length);
                                System.arraycopy(allBytes, 0, bArr, 24, iMin);
                            } else {
                                iMin = Math.min(131072, allBytes.length - i);
                                System.arraycopy(allBytes, i, bArr, 0, iMin);
                            }
                            i += iMin;
                            log("INvram AIDL write ret:" + ((int) RadioConfig.getInstance().writeNvramByNamevec("/mnt/vendor/nvdata/APCFG/APRDEB/PRODUCT_MILOG" + cArr[i2], 131072, bArr)));
                        }
                        bufferedInputStream.close();
                        fileInputStream.close();
                        logV("write to nvram @" + getCurrentDateTime());
                        PreferenceUtils.getInstance().setLongPref("aftersale_write_time", System.currentTimeMillis());
                    } catch (Throwable th) {
                        try {
                            bufferedInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th4) {
                        th3.addSuppressed(th4);
                    }
                    throw th3;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDataFromNvram() {
        boolean zBooleanValue = PreferenceUtils.getInstance().getBooleanPreference("pref_nvram_readback", Boolean.FALSE).booleanValue();
        log("initDataFromNvram:" + zBooleanValue);
        if (zBooleanValue) {
            return;
        }
        this.mHandler.post(new Runnable() { // from class: com.android.phone.statistics.AftersaleController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                this.f$0.lambda$initDataFromNvram$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initDataFromNvram$1() throws Throwable {
        try {
            File fromNvram = readFromNvram(true);
            if (fromNvram != null) {
                File fileStreamPath = this.mContext.getFileStreamPath("aftersale");
                if (fileStreamPath.exists()) {
                    log(fileStreamPath + " should not exist, check this error");
                }
                Files.move(fromNvram.toPath(), fileStreamPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PreferenceUtils.getInstance().setBooleanPreference("pref_nvram_readback", true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void exportAllToExternal() throws Throwable {
        File externalFilesDir = this.mContext.getExternalFilesDir("aftersale");
        try {
            File fromNvram = readFromNvram(false);
            if (fromNvram != null) {
                Files.move(fromNvram.toPath(), new File(externalFilesDir.getAbsolutePath() + File.separator + "as_nvram.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            File fileStreamPath = this.mContext.getFileStreamPath("aftersale");
            if (fileStreamPath.exists()) {
                Files.move(fileStreamPath.toPath(), new File(externalFilesDir.getAbsolutePath() + File.separator + "as_data.zip").toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            String strBuildCriticalText = buildCriticalText();
            String strBuildMajorText = buildMajorText(true);
            if (TextUtils.isEmpty(strBuildMajorText) && TextUtils.isEmpty(strBuildCriticalText)) {
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(new File(externalFilesDir.getAbsolutePath() + File.separator + "as_current.log"));
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                try {
                    if (!TextUtils.isEmpty(strBuildCriticalText)) {
                        bufferedOutputStream.write(strBuildCriticalText.getBytes(StandardCharsets.UTF_8));
                    }
                    if (!TextUtils.isEmpty(strBuildMajorText)) {
                        bufferedOutputStream.write(strBuildMajorText.getBytes(StandardCharsets.UTF_8));
                    }
                    bufferedOutputStream.close();
                    fileOutputStream.close();
                } catch (Throwable th) {
                    try {
                        bufferedOutputStream.close();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File readFromNvram(boolean z) throws Throwable {
        File file;
        Throwable th;
        Throwable th2;
        File fileStreamPath = this.mContext.getFileStreamPath("aftersale_nvram");
        if (fileStreamPath.exists()) {
            log("del exists:" + fileStreamPath.delete());
        }
        File file2 = null;
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(fileStreamPath);
                try {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    int i = 4;
                    try {
                        try {
                            char[] cArr = {'A', 'B', 'C', 'D'};
                            int i2 = 0;
                            int i3 = 0;
                            while (true) {
                                if (i2 < i) {
                                    String nvramByName = RadioConfig.getInstance().readNvramByName("/mnt/vendor/nvdata/APCFG/APRDEB/PRODUCT_MILOG" + cArr[i2], 131072);
                                    if (TextUtils.isEmpty(nvramByName)) {
                                        log("no data in /mnt/vendor/nvdata/APCFG/APRDEB/PRODUCT_MILOG" + cArr[i2]);
                                    } else {
                                        log("readFileByName len:" + nvramByName.length());
                                        if (nvramByName.length() % 2 != 0) {
                                            try {
                                                log("The returned data size is not a multiple of 2 from NVRAM");
                                                nvramByName = nvramByName.substring(0, nvramByName.length() - 1);
                                            } catch (Throwable th3) {
                                                th2 = th3;
                                                file = file2;
                                                try {
                                                    bufferedOutputStream.close();
                                                    throw th2;
                                                } catch (Throwable th4) {
                                                    th2.addSuppressed(th4);
                                                    throw th2;
                                                }
                                            }
                                        }
                                        byte[] bArrHexStringToBytes = IccUtils.hexStringToBytes(nvramByName);
                                        if (i2 < 1) {
                                            if (bArrHexStringToBytes.length < 24) {
                                                log("no data in /mnt/vendor/nvdata/APCFG/APRDEB/PRODUCT_MILOG" + cArr[i2]);
                                            } else {
                                                ByteBuffer byteBufferWrap = ByteBuffer.wrap(Arrays.copyOf(bArrHexStringToBytes, 24));
                                                long j = byteBufferWrap.getLong();
                                                if (z) {
                                                    file = file2;
                                                    try {
                                                        PreferenceUtils.getInstance().setLongPref("aftersale_write_time", j);
                                                    } catch (Throwable th5) {
                                                        th = th5;
                                                    }
                                                } else {
                                                    file = file2;
                                                }
                                                int i4 = byteBufferWrap.getInt();
                                                log("nvram data len:" + i4);
                                                int iMin = Math.min(i4, 131048);
                                                bufferedOutputStream.write(bArrHexStringToBytes, 24, iMin);
                                                i3 = i4 - iMin;
                                            }
                                            th2 = th;
                                            bufferedOutputStream.close();
                                            throw th2;
                                        }
                                        file = file2;
                                        int iMin2 = Math.min(i3, 131072);
                                        bufferedOutputStream.write(bArrHexStringToBytes, 0, iMin2);
                                        i3 -= iMin2;
                                        if (i3 < 1) {
                                            log("all data in NVRAM read");
                                        } else {
                                            i2++;
                                            file2 = file;
                                            i = 4;
                                        }
                                    }
                                    try {
                                        bufferedOutputStream.close();
                                        fileOutputStream.close();
                                        return file2;
                                    } catch (Throwable th6) {
                                        th = th6;
                                        file = file2;
                                        try {
                                            fileOutputStream.close();
                                            throw th;
                                        } catch (Throwable th7) {
                                            th.addSuppressed(th7);
                                            throw th;
                                        }
                                    }
                                }
                                bufferedOutputStream.close();
                                fileOutputStream.close();
                                return fileStreamPath;
                            }
                        } catch (Throwable th8) {
                            th = th8;
                            th = th;
                            fileOutputStream.close();
                            throw th;
                        }
                    } catch (Throwable th9) {
                        th = th9;
                        file = file2;
                    }
                } catch (Throwable th10) {
                    th = th10;
                    file = null;
                }
            } catch (Exception e) {
                e = e;
                file = file2;
                e.printStackTrace();
                return file;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return file;
        }
    }

    private StringBuilder parseCrashInfo() {
        File[] fileArrListFiles;
        File file = new File("/data/ramdump");
        StringBuilder sb = new StringBuilder();
        if (file.exists() && (fileArrListFiles = file.listFiles()) != null) {
            for (File file2 : fileArrListFiles) {
                logV("crash file:" + file2.getName());
                if (file2.getName().contains(".log")) {
                    try {
                        FileReader fileReader = new FileReader(file2);
                        try {
                            BufferedReader bufferedReader = new BufferedReader(fileReader);
                            try {
                                sb.append(file2.getName());
                                sb.append("\n");
                                while (true) {
                                    String line = bufferedReader.readLine();
                                    if (line == null) {
                                        break;
                                    }
                                    sb.append(line);
                                    sb.append("\n");
                                    try {
                                        fileReader.close();
                                    } catch (Throwable th) {
                                        th.addSuppressed(th);
                                    }
                                    throw th;
                                }
                                bufferedReader.close();
                                fileReader.close();
                            } catch (Throwable th2) {
                                try {
                                    bufferedReader.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                                throw th2;
                            }
                        } catch (Throwable th4) {
                            fileReader.close();
                            throw th4;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sb;
    }

    private boolean isLongSinceLastWrite() {
        long longPref = PreferenceUtils.getInstance().getLongPref("aftersale_write_time");
        return longPref <= 0 || System.currentTimeMillis() - longPref > v.b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCloudFeatureStatus() {
        boolean z;
        try {
            List<MiuiSettings.SettingsCloudData.CloudData> cloudDataList = MiuiSettings.SettingsCloudData.getCloudDataList(this.mContext.getContentResolver(), "TelephonyMtkFeature");
            if (cloudDataList != null && cloudDataList.size() != 0) {
                for (MiuiSettings.SettingsCloudData.CloudData cloudData : cloudDataList) {
                    String string = cloudData.getString("Model", "NA");
                    String string2 = cloudData.getString("SimOperator", "NA");
                    log("updateCloudFeatureStatus " + cloudData);
                    if (FiveGCloudUtils.modelMatched(string) && FiveGCloudUtils.simOperatorMatched(string2) && (z = cloudData.getBoolean("cloud_aftersale_enable", true)) != this.mCloudEnable) {
                        this.mCloudEnable = z;
                        PreferenceUtils.getInstance().setBooleanPreference("cloud_aftersale_enable", z);
                    }
                }
                return;
            }
            log("updateCloudFeatureStatus cloudList is empty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
