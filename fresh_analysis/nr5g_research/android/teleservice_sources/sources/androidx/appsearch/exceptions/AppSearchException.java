package androidx.appsearch.exceptions;

/* JADX INFO: loaded from: classes.dex */
public class AppSearchException extends Exception {
    private final int mResultCode;

    public AppSearchException(int i, String str) {
        this(i, str, null);
    }

    public AppSearchException(int i, String str, Throwable th) {
        super(str, th);
        this.mResultCode = i;
    }
}
