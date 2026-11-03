package com.android.internal.telephony;

import android.os.Binder;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.tensorflow.lite.DataType$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes.dex */
public abstract class BasicShellCommandHandler {
    private int mArgPos;
    private String[] mArgs;
    private String mCmd;
    private String mCurArgData;
    private FileDescriptor mErr;
    private PrintWriter mErrPrintWriter;
    private FileOutputStream mFileErr;
    private FileInputStream mFileIn;
    private FileOutputStream mFileOut;
    private FileDescriptor mIn;
    private InputStream mInputStream;
    private FileDescriptor mOut;
    private PrintWriter mOutPrintWriter;
    private Binder mTarget;

    public abstract int onCommand(String str);

    public abstract void onHelp();

    public void init(Binder binder, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr, int i) {
        this.mTarget = binder;
        this.mIn = fileDescriptor;
        this.mOut = fileDescriptor2;
        this.mErr = fileDescriptor3;
        this.mArgs = strArr;
        this.mCmd = null;
        this.mArgPos = i;
        this.mCurArgData = null;
        this.mFileIn = null;
        this.mFileOut = null;
        this.mFileErr = null;
        this.mOutPrintWriter = null;
        this.mErrPrintWriter = null;
        this.mInputStream = null;
    }

    public int exec(Binder binder, FileDescriptor fileDescriptor, FileDescriptor fileDescriptor2, FileDescriptor fileDescriptor3, String[] strArr) {
        int i;
        String str;
        if (strArr == null || strArr.length <= 0) {
            i = 0;
            str = null;
        } else {
            str = strArr[0];
            i = 1;
        }
        init(binder, fileDescriptor, fileDescriptor2, fileDescriptor3, strArr, i);
        this.mCmd = str;
        try {
            int iOnCommand = onCommand(str);
            PrintWriter printWriter = this.mOutPrintWriter;
            if (printWriter != null) {
                printWriter.flush();
            }
            PrintWriter printWriter2 = this.mErrPrintWriter;
            if (printWriter2 != null) {
                printWriter2.flush();
            }
            return iOnCommand;
        } catch (Throwable th) {
            try {
                PrintWriter errPrintWriter = getErrPrintWriter();
                errPrintWriter.println();
                errPrintWriter.println("Exception occurred while executing '" + this.mCmd + "':");
                th.printStackTrace(errPrintWriter);
            } finally {
                PrintWriter printWriter3 = this.mOutPrintWriter;
                if (printWriter3 != null) {
                    printWriter3.flush();
                }
                PrintWriter printWriter4 = this.mErrPrintWriter;
                if (printWriter4 != null) {
                    printWriter4.flush();
                }
            }
        }
    }

    public OutputStream getRawOutputStream() {
        if (this.mFileOut == null) {
            this.mFileOut = new FileOutputStream(this.mOut);
        }
        return this.mFileOut;
    }

    public PrintWriter getOutPrintWriter() {
        if (this.mOutPrintWriter == null) {
            this.mOutPrintWriter = new PrintWriter(getRawOutputStream());
        }
        return this.mOutPrintWriter;
    }

    public OutputStream getRawErrorStream() {
        if (this.mFileErr == null) {
            this.mFileErr = new FileOutputStream(this.mErr);
        }
        return this.mFileErr;
    }

    public PrintWriter getErrPrintWriter() {
        if (this.mErr == null) {
            return getOutPrintWriter();
        }
        if (this.mErrPrintWriter == null) {
            this.mErrPrintWriter = new PrintWriter(getRawErrorStream());
        }
        return this.mErrPrintWriter;
    }

    public InputStream getRawInputStream() {
        if (this.mFileIn == null) {
            this.mFileIn = new FileInputStream(this.mIn);
        }
        return this.mFileIn;
    }

    public String getNextOption() {
        if (this.mCurArgData != null) {
            DataType$$ExternalSyntheticBUOutline0.m("No argument expected after \"", this.mArgs[this.mArgPos - 1], "\"");
            return null;
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i >= strArr.length) {
            return null;
        }
        String str = strArr[i];
        if (!str.startsWith("-")) {
            return null;
        }
        this.mArgPos++;
        if (str.equals("--")) {
            return null;
        }
        if (str.length() > 1 && str.charAt(1) != '-') {
            if (str.length() > 2) {
                this.mCurArgData = str.substring(2);
                return str.substring(0, 2);
            }
            this.mCurArgData = null;
            return str;
        }
        this.mCurArgData = null;
        return str;
    }

    public String getNextArg() {
        String str = this.mCurArgData;
        if (str != null) {
            this.mCurArgData = null;
            return str;
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i >= strArr.length) {
            return null;
        }
        this.mArgPos = i + 1;
        return strArr[i];
    }

    public String peekNextArg() {
        String str = this.mCurArgData;
        if (str != null) {
            return str;
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i < strArr.length) {
            return strArr[i];
        }
        return null;
    }

    public int getRemainingArgsCount() {
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i >= strArr.length) {
            return 0;
        }
        return strArr.length - i;
    }

    public String getNextArgRequired() {
        String nextArg = getNextArg();
        if (nextArg != null) {
            return nextArg;
        }
        DataType$$ExternalSyntheticBUOutline0.m("Argument expected after \"", this.mArgs[this.mArgPos - 1], "\"");
        return null;
    }

    public int handleDefaultCommands(String str) {
        if (str == null || "help".equals(str) || "-h".equals(str)) {
            onHelp();
            return -1;
        }
        getOutPrintWriter().println("Unknown command: ".concat(str));
        return -1;
    }
}
