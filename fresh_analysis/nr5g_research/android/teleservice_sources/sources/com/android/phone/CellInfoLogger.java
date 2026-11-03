package com.android.phone;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.android.internal.os.BackgroundThread;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/* JADX INFO: loaded from: classes.dex */
public class CellInfoLogger {
    private static final ThreadLocal sDateFormat = ThreadLocal.withInitial(new Supplier() { // from class: com.android.phone.CellInfoLogger$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final Object get() {
            return CellInfoLogger.$r8$lambda$HP0kHJhdGUs26t7EGoiQwY9_CmA();
        }
    });
    private static volatile CellInfoLogger sInstance;
    private LinkedHashMap mAllCellInfoMap = new LinkedHashMap();
    private LinkedHashMap mCellLocationMap = new LinkedHashMap();
    private LinkedHashMap mNeighboringCellInfoMap = new LinkedHashMap();
    private CellInfoHandler mCellInfoHandler = new CellInfoHandler(BackgroundThread.get().getLooper());

    public static /* synthetic */ SimpleDateFormat $r8$lambda$HP0kHJhdGUs26t7EGoiQwY9_CmA() {
        return new SimpleDateFormat("yyyy-MM-dd:HH");
    }

    class InterfaceCallerInfo {
        String callingPkgName;
        int count = 1;

        public InterfaceCallerInfo(CellInfoLogger cellInfoLogger, String str) {
            this.callingPkgName = str;
        }

        public String toString() {
            if (this.callingPkgName == null) {
                return "";
            }
            return this.callingPkgName + ":" + this.count;
        }
    }

    public class CellInfoHandler extends Handler {
        public CellInfoHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                CellInfoLogger cellInfoLogger = CellInfoLogger.this;
                cellInfoLogger.recordCallerInfo(cellInfoLogger.mAllCellInfoMap, (String) message.obj);
            } else if (i == 1) {
                CellInfoLogger cellInfoLogger2 = CellInfoLogger.this;
                cellInfoLogger2.recordCallerInfo(cellInfoLogger2.mCellLocationMap, (String) message.obj);
            } else {
                if (i != 2) {
                    return;
                }
                CellInfoLogger cellInfoLogger3 = CellInfoLogger.this;
                cellInfoLogger3.recordCallerInfo(cellInfoLogger3.mNeighboringCellInfoMap, (String) message.obj);
            }
        }
    }

    public static void init() {
        if (sInstance == null) {
            sInstance = new CellInfoLogger();
        }
    }

    public static CellInfoLogger getInstance() {
        return sInstance;
    }

    private CellInfoLogger() {
    }

    public void recordCallerInfo(int i, String str) {
        this.mCellInfoHandler.obtainMessage(i, str).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void recordCallerInfo(LinkedHashMap linkedHashMap, String str) {
        if (linkedHashMap != null) {
            if (!TextUtils.isEmpty(str)) {
                String currentTime = formatCurrentTime();
                int size = linkedHashMap.size();
                String str2 = null;
                for (Map.Entry entry : linkedHashMap.entrySet()) {
                    String str3 = (String) entry.getKey();
                    if (str2 == null) {
                        str2 = str3;
                    }
                    if (currentTime.equals(str3)) {
                        operateInterfaceCallerInfos((ArrayList) entry.getValue(), str);
                        return;
                    }
                }
                if (size >= 24) {
                    linkedHashMap.remove(str2);
                }
                InterfaceCallerInfo interfaceCallerInfo = new InterfaceCallerInfo(this, str);
                ArrayList arrayList = new ArrayList();
                arrayList.add(interfaceCallerInfo);
                linkedHashMap.put(currentTime, arrayList);
            }
        }
    }

    private String formatCurrentTime() {
        return ((SimpleDateFormat) sDateFormat.get()).format(new Date());
    }

    private void operateInterfaceCallerInfos(ArrayList arrayList, String str) {
        if (arrayList == null || TextUtils.isEmpty(str)) {
            return;
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            InterfaceCallerInfo interfaceCallerInfo = (InterfaceCallerInfo) obj;
            if (str.equals(interfaceCallerInfo.callingPkgName)) {
                interfaceCallerInfo.count++;
                return;
            }
        }
        arrayList.add(new InterfaceCallerInfo(this, str));
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("CellInfoLogger start:");
        printWriter.println("call getAllCellInfo packages---------:");
        dumpListDetail(printWriter, this.mAllCellInfoMap);
        printWriter.println("call getCellLocation packages---------:");
        dumpListDetail(printWriter, this.mCellLocationMap);
        printWriter.println("call getNeighboringCellInfo packages---------:");
        dumpListDetail(printWriter, this.mNeighboringCellInfoMap);
        printWriter.println("CellInfoLogger end.");
    }

    private synchronized void dumpListDetail(PrintWriter printWriter, LinkedHashMap linkedHashMap) {
        if (linkedHashMap == null) {
            return;
        }
        try {
            for (Map.Entry entry : linkedHashMap.entrySet()) {
                String str = (String) entry.getKey();
                ArrayList arrayList = (ArrayList) entry.getValue();
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(str);
                stringBuffer.append("=[");
                int size = arrayList.size();
                int size2 = arrayList.size();
                int i = 0;
                int i2 = 0;
                while (i2 < size2) {
                    Object obj = arrayList.get(i2);
                    i2++;
                    i++;
                    stringBuffer.append("\"");
                    stringBuffer.append((InterfaceCallerInfo) obj);
                    stringBuffer.append("\"");
                    if (i != size) {
                        stringBuffer.append(", ");
                    }
                }
                stringBuffer.append("]");
                printWriter.println(stringBuffer.toString());
            }
        } catch (Throwable th) {
            throw th;
        }
    }
}
