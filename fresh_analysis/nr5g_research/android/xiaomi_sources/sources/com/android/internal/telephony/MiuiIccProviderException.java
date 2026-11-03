package com.android.internal.telephony;

/* JADX INFO: loaded from: classes.dex */
public class MiuiIccProviderException extends RuntimeException {
    public static final int ERROR_ADN_LIST_NOT_EXIST = -1010;
    public static final int ERROR_ANR_FULL = -1008;
    public static final int ERROR_ANR_TOO_LONG = -1009;
    public static final int ERROR_EMAIL_FULL = -1011;
    public static final int ERROR_EMAIL_TOO_LONG = -1012;
    public static final int ERROR_GENERIC_FAILURE = -1002;
    public static final int ERROR_ICC_CARD_RESET = -1013;
    public static final int ERROR_NOT_READY = -1006;
    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_NUMBER_TOO_LONG = -1003;
    public static final int ERROR_PASSWORD_ERROR = -1007;
    public static final int ERROR_STORAGE_FULL = -1005;
    public static final int ERROR_TEXT_TOO_LONG = -1004;
    public static final int ERROR_UNKNOWN = -1001;
    public static final int ERROR_UNKNOWN_EF = -1014;
    private static final long serialVersionUID = 1;
    private int mError;

    public MiuiIccProviderException(int error) {
        this.mError = error;
    }

    int getError() {
        return this.mError;
    }

    public static int getErrorCauseFromException(Throwable e) {
        if (e == null) {
            return 0;
        }
        if (e instanceof CommandException) {
            CommandException ce = (CommandException) e;
            switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$CommandException$Error[ce.getCommandError().ordinal()]) {
                case 1:
                    return ERROR_GENERIC_FAILURE;
                case 2:
                case 3:
                    return ERROR_PASSWORD_ERROR;
                default:
                    return ERROR_UNKNOWN;
            }
        }
        if (!(e instanceof MiuiIccProviderException)) {
            return 0;
        }
        MiuiIccProviderException ipe = (MiuiIccProviderException) e;
        int ret = ipe.getError();
        return ret;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.MiuiIccProviderException$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$CommandException$Error = new int[CommandException.Error.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.GENERIC_FAILURE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.SIM_PUK2.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$CommandException$Error[CommandException.Error.PASSWORD_INCORRECT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }
}
