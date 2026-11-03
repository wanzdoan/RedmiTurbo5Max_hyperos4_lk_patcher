package com.android.phone;

import android.os.DropBoxManager;
import android.os.SystemClock;
import android.os.TransactionTooLargeException;
import android.telephony.AnomalyReporter;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes.dex */
public class DiagnosticDataCollector {
    private final Executor mAsyncTaskExecutor;
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss.mmm", Locale.US);
    private final DropBoxManager mDropBoxManager;
    private final boolean mIsLowRamDevice;
    private final Runtime mJavaRuntime;
    private static final String[] TELECOM_DUMPSYS_COMMAND = {"/system/bin/dumpsys", "telecom", "EmergencyDiagnostics"};
    private static final String[] TELEPHONY_DUMPSYS_COMMAND = {"/system/bin/dumpsys", "telephony.registry", "EmergencyDiagnostics"};
    public static final UUID DROPBOX_TRANSACTION_TOO_LARGE_EXCEPTION = UUID.fromString("ab27e97a-ef7b-11ed-a05b-0242ac120003");

    public DiagnosticDataCollector(Runtime runtime, Executor executor, DropBoxManager dropBoxManager, boolean z) {
        this.mJavaRuntime = runtime;
        this.mAsyncTaskExecutor = executor;
        this.mDropBoxManager = dropBoxManager;
        this.mIsLowRamDevice = z;
    }

    public void persistEmergencyDianosticData(DataCollectorConfig.Adapter adapter, TelephonyManager.EmergencyCallDiagnosticData emergencyCallDiagnosticData, String str) {
        if (emergencyCallDiagnosticData.isTelephonyDumpsysCollectionEnabled()) {
            persistTelephonyState(adapter, str);
        }
        if (emergencyCallDiagnosticData.isTelecomDumpsysCollectionEnabled()) {
            persistTelecomState(adapter, str);
        }
        if (emergencyCallDiagnosticData.isLogcatCollectionEnabled()) {
            persistLogcat(adapter, str, emergencyCallDiagnosticData.getLogcatCollectionStartTimeMillis());
        }
    }

    private void persistLogcat(DataCollectorConfig.Adapter adapter, String str, long j) {
        String str2 = this.mDateFormat.format(new Date(j - 75));
        Log.d("DDC", "Persisting Logcat");
        if (this.mIsLowRamDevice) {
            adapter.getMaxLogcatLinesForLowMemDevice();
        } else {
            adapter.getMaxLogcatLines();
        }
        this.mAsyncTaskExecutor.execute(new DiagnosticRunnable(new String[]{"/system/bin/logcat", "-t", str2, "-b", "system,radio"}, adapter.getLogcatReadTimeoutMillis(), adapter.getLogcatProcTimeoutMillis(), str, adapter.getMaxLogcatLinesForLowMemDevice()));
    }

    private void persistTelecomState(DataCollectorConfig.Adapter adapter, String str) {
        Log.d("DDC", "Persisting Telecom state");
        this.mAsyncTaskExecutor.execute(new DiagnosticRunnable(TELECOM_DUMPSYS_COMMAND, adapter.getDumpsysReadTimeoutMillis(), adapter.getDumpsysProcTimeoutMillis(), str, adapter.getMaxLogcatLines()));
    }

    private void persistTelephonyState(DataCollectorConfig.Adapter adapter, String str) {
        Log.d("DDC", "Persisting Telephony state");
        this.mAsyncTaskExecutor.execute(new DiagnosticRunnable(TELEPHONY_DUMPSYS_COMMAND, adapter.getDumpsysReadTimeoutMillis(), adapter.getDumpsysProcTimeoutMillis(), str, adapter.getMaxLogcatLines()));
    }

    class DiagnosticRunnable implements Runnable {
        private final String[] mCmd;
        private final String mDropBoxTag;
        private final int mMaxLogcatLines;
        private long mProcTimeout;
        private long mStreamTimeout;

        DiagnosticRunnable(String[] strArr, long j, long j2, String str, int i) {
            this.mCmd = strArr;
            this.mStreamTimeout = j;
            this.mProcTimeout = j2;
            this.mDropBoxTag = str;
            this.mMaxLogcatLines = i;
            Log.d("DDC-DiagnosticRunnable", "Runnable created with cmd: " + Arrays.toString(strArr));
        }

        @Override // java.lang.Runnable
        public void run() throws Throwable {
            Log.d("DDC-DiagnosticRunnable", "Running async persist for tag" + this.mDropBoxTag);
            getProcOutputAndPersist(this.mCmd, this.mStreamTimeout, this.mProcTimeout, this.mDropBoxTag, this.mMaxLogcatLines);
        }

        /* JADX WARN: Code duplicated, block: B:20:0x00af A[PHI: r5
  0x00af: PHI (r5v4 java.lang.Process) = (r5v2 java.lang.Process), (r5v5 java.lang.Process) binds: [B:22:0x00cd, B:19:0x00ad] A[DONT_GENERATE, DONT_INLINE]] */
        private void getProcOutputAndPersist(String[] strArr, long j, long j2, String str, int i) throws Throwable {
            Throwable th;
            int length;
            String string;
            StringBuilder sb = new StringBuilder();
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            Process process = null;
            try {
                try {
                    Process processExec = DiagnosticDataCollector.this.mJavaRuntime.exec(strArr);
                    try {
                        readStreamLinesWithTimeout(new BufferedReader(new InputStreamReader(processExec.getInputStream())), sb, j, i);
                        int length2 = sb.length();
                        readStreamLinesWithTimeout(new BufferedReader(new InputStreamReader(processExec.getErrorStream())), sb, j, i);
                        Log.d("DDC-DiagnosticRunnable", "[" + strArr[0] + "]streams read in " + (SystemClock.elapsedRealtime() - jElapsedRealtime) + " milliseconds");
                        processExec.waitFor(j2, TimeUnit.MILLISECONDS);
                        length = sb.length() - length2;
                        processExec.destroy();
                    } catch (IOException e) {
                        e = e;
                        process = processExec;
                        sb.append("DiagnosticDataCollector error executing cmd" + e.toString() + System.lineSeparator());
                        if (process != null) {
                            process.destroy();
                        }
                        length = 0;
                    } catch (InterruptedException e2) {
                        e = e2;
                        process = processExec;
                        sb.append("DiagnosticDataCollector error executing cmd" + e.toString() + System.lineSeparator());
                        if (process != null) {
                            process.destroy();
                        }
                        length = 0;
                    } catch (Throwable th2) {
                        th = th2;
                        process = processExec;
                        if (process != null) {
                            process.destroy();
                            throw th;
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                }
            } catch (IOException e3) {
                e = e3;
            } catch (InterruptedException e4) {
                e = e4;
            }
            Log.d("DDC-DiagnosticRunnable", "[" + strArr[0] + "]output collected in " + (SystemClock.elapsedRealtime() - jElapsedRealtime) + " milliseconds. Size:" + sb.toString().length());
            if (length > 0) {
                Log.w("DDC-DiagnosticRunnable", "Cmd ran with errors");
                sb.append("DiagnosticDataCollector error executing cmd" + System.lineSeparator());
            }
            try {
                if (sb.length() > 131072) {
                    string = sb.substring(0, 131072) + "\n\n[TRUNCATED]\n";
                } else {
                    string = sb.toString();
                }
                DiagnosticDataCollector.this.mDropBoxManager.addText(str, string);
            } catch (Exception e5) {
                if (e5 instanceof TransactionTooLargeException) {
                    AnomalyReporter.reportAnomaly(DiagnosticDataCollector.DROPBOX_TRANSACTION_TOO_LARGE_EXCEPTION, "DiagnosticDataCollector: transaction too large");
                }
                Log.w("DDC-DiagnosticRunnable", "Exception while writing to Dropbox " + e5);
            }
        }

        private void readStreamLinesWithTimeout(BufferedReader bufferedReader, StringBuilder sb, long j, int i) throws IOException {
            long jElapsedRealtime = SystemClock.elapsedRealtime();
            int i2 = 0;
            while (SystemClock.elapsedRealtime() < jElapsedRealtime + j) {
                while (bufferedReader.ready() && i2 < i) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        return;
                    }
                    sb.append(line);
                    i2++;
                    sb.append(System.lineSeparator());
                }
                SystemClock.sleep(j / 10);
            }
        }
    }
}
