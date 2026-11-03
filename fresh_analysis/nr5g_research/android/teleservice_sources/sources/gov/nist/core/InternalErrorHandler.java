package gov.nist.core;

/* JADX INFO: loaded from: classes2.dex */
public abstract class InternalErrorHandler {
    public static void handleException(Exception exc) {
        System.err.println("Unexpected internal error FIXME!! " + exc.getMessage());
        exc.printStackTrace();
        throw new RuntimeException("Unexpected internal error FIXME!! " + exc.getMessage(), exc);
    }

    public static void handleException(String str) {
        new Exception().printStackTrace();
        System.err.println("Unexepcted INTERNAL ERROR FIXME!!");
        System.err.println(str);
        throw new RuntimeException(str);
    }
}
