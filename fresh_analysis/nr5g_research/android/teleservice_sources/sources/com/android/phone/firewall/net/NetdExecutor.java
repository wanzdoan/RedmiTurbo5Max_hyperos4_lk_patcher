package com.android.phone.firewall.net;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import com.android.phone.firewall.utils.WatchDog;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* JADX INFO: loaded from: classes.dex */
public class NetdExecutor implements Runnable, WatchDog.Action {
    private static final ExecutorService SINGLE_EXECUTOR = Executors.newSingleThreadExecutor();
    private final Object[] mArgs;
    private final String mCmd;
    private final String mSocketName;
    private final int mTimeoutMs;
    private LocalSocket mSocket = null;
    private OutputStream mOutputStream = null;
    private InputStream mInputStream = null;

    public static synchronized void setSecFirewallRule(String str) {
        execute(60000, "dnsproxyd", "setsatelliteoperatoraddress", str);
    }

    public static synchronized void execute(int i, String str, String str2, Object... objArr) {
        new NetdExecutor(i, str, str2, objArr).execute();
    }

    private NetdExecutor(int i, String str, String str2, Object... objArr) {
        this.mTimeoutMs = i;
        this.mSocketName = str;
        this.mCmd = str2;
        this.mArgs = objArr;
    }

    private void execute() {
        SINGLE_EXECUTOR.execute(this);
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            WatchDog.get().start(1, this);
            executeCommand(this.mTimeoutMs, this.mSocketName, this.mCmd, this.mArgs);
        } catch (IOException unused) {
        } finally {
            WatchDog.get().cancel(1, this);
        }
    }

    /* JADX WARN: Code duplicated, block: B:136:0x026b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:142:0x0226 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:146:0x023b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:150:0x01f6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:152:0x0253 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:158:0x020e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:180:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:181:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Not initialized variable reg: 7, insn: 0x0058: MOVE (r17 I:??[OBJECT, ARRAY]) = (r7 I:??[OBJECT, ARRAY]), block:B:12:0x0058 */
    /* JADX WARN: Not initialized variable reg: 8, insn: 0x005c: MOVE (r9 I:??[OBJECT, ARRAY]) = (r8 I:??[OBJECT, ARRAY]), block:B:12:0x0058 */
    /* JADX WARN: Not initialized variable reg: 9, insn: 0x005b: MOVE (r0 I:??[OBJECT, ARRAY]) = (r9 I:??[OBJECT, ARRAY]), block:B:12:0x0058 */
    private void executeCommand(int i, String str, String str2, Object... objArr) throws Throwable {
        Throwable th;
        OutputStream outputStream;
        LocalSocket localSocket;
        InputStream inputStream;
        LocalSocket localSocket2;
        InputStream inputStream2;
        LocalSocket localSocket3;
        OutputStream outputStream2;
        InputStream inputStream3;
        OutputStream outputStream3;
        StringBuilder sb;
        try {
            try {
                localSocket2 = new LocalSocket();
                try {
                    this.mSocket = localSocket2;
                    localSocket2.connect(new LocalSocketAddress(str, LocalSocketAddress.Namespace.RESERVED));
                    localSocket2.setSoTimeout(i);
                    inputStream2 = localSocket2.getInputStream();
                    try {
                        this.mInputStream = inputStream2;
                        outputStream3 = localSocket2.getOutputStream();
                        try {
                            this.mOutputStream = outputStream3;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(str2);
                            for (Object obj : objArr) {
                                String strValueOf = String.valueOf(obj);
                                if (strValueOf.indexOf(0) >= 0) {
                                    throw new IllegalArgumentException("Unexpected argument: " + obj);
                                }
                                sb2.append(' ');
                                appendEscaped(sb2, strValueOf);
                            }
                            sb2.append((char) 0);
                            String string = sb2.toString();
                            outputStream3.write(string.getBytes(StandardCharsets.UTF_8));
                            Log.d("PhoneNetdExecutor", "SND -> {" + string.trim() + "} this=" + this);
                            while (true) {
                                byte[] bArr = new byte[4096];
                                int i2 = inputStream2.read(bArr);
                                if (i2 < 0) {
                                    Log.e("PhoneNetdExecutor", "got " + i2 + " reading");
                                    try {
                                        outputStream3.close();
                                        this.mOutputStream = null;
                                    } catch (IOException e) {
                                        Log.e("PhoneNetdExecutor", "Failed closing output stream: " + e);
                                    }
                                    try {
                                        inputStream2.close();
                                        this.mInputStream = null;
                                    } catch (IOException e2) {
                                        Log.e("PhoneNetdExecutor", "Failed closing input stream: " + e2);
                                    }
                                    try {
                                        localSocket2.close();
                                        this.mSocket = null;
                                        return;
                                    } catch (IOException e3) {
                                        e = e3;
                                        sb = new StringBuilder("Failed closing socket: ");
                                    }
                                } else {
                                    for (int i3 = 0; i3 < i2; i3++) {
                                        if (bArr[i3] == 0) {
                                            String str3 = new String(bArr, 0, i3, StandardCharsets.UTF_8);
                                            String[] strArrSplit = str3.split(" ");
                                            if (strArrSplit.length == 0) {
                                                throw new IllegalArgumentException("Insufficient arguments");
                                            }
                                            try {
                                                if (Integer.parseInt(strArrSplit[0]) == 200) {
                                                    Log.d("PhoneNetdExecutor", "Success REV <- {" + str3.trim() + "}");
                                                } else {
                                                    Log.d("PhoneNetdExecutor", "Failed REV <- {" + str3.trim() + "}");
                                                }
                                                try {
                                                    outputStream3.close();
                                                    this.mOutputStream = null;
                                                } catch (IOException e4) {
                                                    Log.e("PhoneNetdExecutor", "Failed closing output stream: " + e4);
                                                }
                                                try {
                                                    inputStream2.close();
                                                    this.mInputStream = null;
                                                } catch (IOException e5) {
                                                    Log.e("PhoneNetdExecutor", "Failed closing input stream: " + e5);
                                                }
                                                try {
                                                    localSocket2.close();
                                                    this.mSocket = null;
                                                    return;
                                                } catch (IOException e6) {
                                                    e = e6;
                                                    sb = new StringBuilder("Failed closing socket: ");
                                                }
                                            } catch (NumberFormatException e7) {
                                                throw new IllegalArgumentException("problem parsing code", e7);
                                            }
                                        }
                                    }
                                }
                                sb.append(e);
                                Log.e("PhoneNetdExecutor", sb.toString());
                                return;
                            }
                        } catch (IOException e8) {
                            e = e8;
                            Log.e("PhoneNetdExecutor", "Communications error: ", e);
                            if (outputStream3 != null) {
                                try {
                                    outputStream3.close();
                                    this.mOutputStream = null;
                                } catch (IOException e9) {
                                    Log.e("PhoneNetdExecutor", "Failed closing output stream: " + e9);
                                }
                            }
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                    this.mInputStream = null;
                                } catch (IOException e10) {
                                    Log.e("PhoneNetdExecutor", "Failed closing input stream: " + e10);
                                }
                            }
                            if (localSocket2 == null) {
                            }
                            try {
                                localSocket2.close();
                                this.mSocket = null;
                            } catch (IOException e11) {
                                e = e11;
                                sb = new StringBuilder("Failed closing socket: ");
                            }
                        } catch (IllegalArgumentException e12) {
                            e = e12;
                            Log.e("PhoneNetdExecutor", "Problem parsing message ", e);
                            if (outputStream3 != null) {
                                try {
                                    outputStream3.close();
                                    this.mOutputStream = null;
                                } catch (IOException e13) {
                                    Log.e("PhoneNetdExecutor", "Failed closing output stream: " + e13);
                                }
                            }
                            if (inputStream2 != null) {
                                try {
                                    inputStream2.close();
                                    this.mInputStream = null;
                                } catch (IOException e14) {
                                    Log.e("PhoneNetdExecutor", "Failed closing input stream: " + e14);
                                }
                            }
                            if (localSocket2 == null) {
                            }
                            try {
                                localSocket2.close();
                                this.mSocket = null;
                            } catch (IOException e15) {
                                e = e15;
                                sb = new StringBuilder("Failed closing socket: ");
                            }
                        }
                    } catch (IOException e16) {
                        e = e16;
                        outputStream3 = null;
                    } catch (IllegalArgumentException e17) {
                        e = e17;
                        outputStream3 = null;
                    } catch (Throwable th2) {
                        inputStream = inputStream2;
                        localSocket = localSocket2;
                        th = th2;
                        outputStream = null;
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                                this.mOutputStream = null;
                            } catch (IOException e18) {
                                Log.e("PhoneNetdExecutor", "Failed closing output stream: " + e18);
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                                this.mInputStream = null;
                            } catch (IOException e19) {
                                Log.e("PhoneNetdExecutor", "Failed closing input stream: " + e19);
                            }
                        }
                        if (localSocket == null) {
                            throw th;
                        }
                        try {
                            localSocket.close();
                            this.mSocket = null;
                            throw th;
                        } catch (IOException e20) {
                            Log.e("PhoneNetdExecutor", "Failed closing socket: " + e20);
                            throw th;
                        }
                    }
                } catch (IOException e21) {
                    e = e21;
                    inputStream2 = null;
                    outputStream3 = inputStream2;
                    Log.e("PhoneNetdExecutor", "Communications error: ", e);
                    if (outputStream3 != null) {
                        outputStream3.close();
                        this.mOutputStream = null;
                    }
                    if (inputStream2 != null) {
                        inputStream2.close();
                        this.mInputStream = null;
                    }
                    if (localSocket2 == null) {
                        localSocket2.close();
                        this.mSocket = null;
                    }
                } catch (IllegalArgumentException e22) {
                    e = e22;
                    inputStream2 = null;
                    outputStream3 = inputStream2;
                    Log.e("PhoneNetdExecutor", "Problem parsing message ", e);
                    if (outputStream3 != null) {
                        outputStream3.close();
                        this.mOutputStream = null;
                    }
                    if (inputStream2 != null) {
                        inputStream2.close();
                        this.mInputStream = null;
                    }
                    if (localSocket2 == null) {
                        localSocket2.close();
                        this.mSocket = null;
                    }
                } catch (Throwable th3) {
                    inputStream = null;
                    localSocket = localSocket2;
                    th = th3;
                    outputStream = null;
                }
            } catch (Throwable th4) {
                th = th4;
                outputStream = outputStream2;
                inputStream = inputStream3;
                localSocket = localSocket3;
            }
        } catch (IOException e23) {
            e = e23;
            localSocket2 = null;
            inputStream2 = null;
        } catch (IllegalArgumentException e24) {
            e = e24;
            localSocket2 = null;
            inputStream2 = null;
        } catch (Throwable th5) {
            th = th5;
            outputStream = null;
            localSocket = null;
            inputStream = null;
        }
    }

    @Override // com.android.phone.firewall.utils.WatchDog.Action
    public void doAction() {
        Log.w("PhoneNetdExecutor", "forceClose");
        OutputStream outputStream = this.mOutputStream;
        if (outputStream != null) {
            try {
                outputStream.close();
                this.mOutputStream = null;
            } catch (IOException e) {
                Log.e("PhoneNetdExecutor", "Failed closing output stream: " + e);
            }
        }
        InputStream inputStream = this.mInputStream;
        if (inputStream != null) {
            try {
                inputStream.close();
                this.mInputStream = null;
            } catch (IOException e2) {
                Log.e("PhoneNetdExecutor", "Failed closing input stream: " + e2);
            }
        }
        LocalSocket localSocket = this.mSocket;
        if (localSocket != null) {
            try {
                localSocket.close();
                this.mSocket = null;
            } catch (IOException e3) {
                Log.e("PhoneNetdExecutor", "Failed closing socket: " + e3);
            }
        }
    }

    private static void appendEscaped(StringBuilder sb, String str) {
        boolean z = str.indexOf(32) >= 0;
        if (z) {
            sb.append('\"');
        }
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char cCharAt = str.charAt(i);
            if (cCharAt == '\"') {
                sb.append("\\\"");
            } else if (cCharAt == '\\') {
                sb.append("\\\\");
            } else {
                sb.append(cCharAt);
            }
        }
        if (z) {
            sb.append('\"');
        }
    }
}
