package com.android.phone.settings;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.R;
import com.android.phone.custompreferences.CustomListPreference;
import com.android.phone.statistics.MiuiSuppServiceStatistic;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;

/* JADX INFO: loaded from: classes.dex */
public class CLIRListPreference extends CustomListPreference {
    private boolean DBG;
    int[] clirArray;
    private final MyHandler mHandler;
    private Phone mPhone;
    private TimeConsumingPreferenceListener mTcpListener;

    public CLIRListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
    }

    public CLIRListPreference(Context context) {
        super(context);
        this.DBG = MiuiPhoneUtils.DBG_LEVEL >= 2;
        this.mHandler = new MyHandler();
    }

    @Override // com.android.phone.custompreferences.CustomListPreference
    protected void onDialogClosed(boolean z) {
        super.onDialogClosed(z);
        this.mPhone.setOutgoingCallerIdDisplay(findIndexOfValue(getValue()), this.mHandler.obtainMessage(3));
        TimeConsumingPreferenceListener timeConsumingPreferenceListener = this.mTcpListener;
        if (timeConsumingPreferenceListener != null) {
            timeConsumingPreferenceListener.onStarted(this, false);
        }
    }

    public void init(TimeConsumingPreferenceListener timeConsumingPreferenceListener, boolean z, int i) {
        Log.d("CLIRListPreference", "CLIRListPreference init, subscription :" + i);
        Phone phone = MiuiPhoneUtils.getPhone(i);
        this.mPhone = phone;
        this.mTcpListener = timeConsumingPreferenceListener;
        if (z) {
            return;
        }
        phone.getOutgoingCallerIdDisplay(this.mHandler.obtainMessage(0, 0, 0));
        TimeConsumingPreferenceListener timeConsumingPreferenceListener2 = this.mTcpListener;
        if (timeConsumingPreferenceListener2 != null) {
            timeConsumingPreferenceListener2.onStarted(this, true);
        }
    }

    void handleGetCLIRResult(int[] iArr) {
        this.clirArray = iArr;
        int i = iArr[1];
        int i2 = 0;
        setEnabled(i == 1 || i == 3 || i == 4);
        int i3 = iArr[1];
        if (i3 == 1 || i3 == 3 || i3 == 4) {
            int i4 = iArr[0];
            if (i4 == 1) {
                i2 = 1;
            } else if (i4 == 2) {
                i2 = 2;
            }
        }
        setValueIndex(i2);
        int i5 = R.string.sum_default_caller_id;
        if (i2 != 0) {
            if (i2 == 1) {
                i5 = R.string.sum_hide_caller_id;
            } else if (i2 == 2) {
                i5 = R.string.sum_show_caller_id;
            }
        }
        setSummary(i5);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleCommandException(Throwable th) {
        if (th instanceof CommandException) {
            CommandException commandException = (CommandException) th;
            if ((ModemProvider.IS_MTK || ModemProvider.IS_XRING) && commandException.getCommandError() == CommandException.Error.REQUEST_NOT_SUPPORTED) {
                Log.d("CLIRListPreference", "receive REQUEST_NOT_SUPPORTED CLIR !!");
                setEnabled(false);
                setSummary("");
                return;
            }
            this.mTcpListener.onException(this, commandException);
        }
    }

    class MyHandler extends Handler {
        private MyHandler() {
        }

        /* JADX WARN: Code duplicated, block: B:29:0x00ab  */
        /* JADX WARN: Code duplicated, block: B:34:? A[RETURN, SYNTHETIC] */
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AsyncResult asyncResult;
            int i;
            int i2 = message.what;
            if (i2 == 0) {
                if (message.arg2 != 3) {
                    MiuiSuppServiceStatistic.getInstance(CLIRListPreference.this.getContext()).recordCountClickScene(message.what, ResponseObject.RELAY_FAILED_CODE, CLIRListPreference.this.mPhone);
                }
            } else {
                if (i2 != 1 && i2 != 2) {
                    if (i2 == 3) {
                        MiuiSuppServiceStatistic.getInstance(CLIRListPreference.this.getContext()).recordCountClickScene(message.what, ResponseObject.RELAY_FAILED_CODE, CLIRListPreference.this.mPhone);
                    } else if (i2 == 4 || i2 == 5) {
                    }
                    if (message.what != 5 && ((AsyncResult) message.obj).exception != null) {
                        Phone phone = CLIRListPreference.this.mPhone;
                        CLIRListPreference cLIRListPreference = CLIRListPreference.this;
                        phone.setOutgoingCallerIdDisplay(cLIRListPreference.findIndexOfValue(cLIRListPreference.getValue()), CLIRListPreference.this.mHandler.obtainMessage(message.what + 1, message.arg1, message.arg2));
                    }
                    handleSetCLIRResponse(message);
                }
                asyncResult = (AsyncResult) message.obj;
                if (asyncResult.exception != null) {
                    i = message.what;
                    if (i != 2 || i == 5) {
                        MiuiSuppServiceStatistic.getInstance(CLIRListPreference.this.getContext()).recordExceptionScene(message.what, ResponseObject.RELAY_FAILED_CODE, asyncResult.exception, CLIRListPreference.this.mPhone);
                    }
                    return;
                }
            }
            if (message.what != 2 && ((AsyncResult) message.obj).exception != null) {
                CLIRListPreference.this.mPhone.getOutgoingCallerIdDisplay(CLIRListPreference.this.mHandler.obtainMessage(message.what + 1, message.arg1, message.arg2));
            } else {
                handleGetCLIRResponse(message);
            }
            asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null) {
                i = message.what;
                if (i != 2) {
                }
                MiuiSuppServiceStatistic.getInstance(CLIRListPreference.this.getContext()).recordExceptionScene(message.what, ResponseObject.RELAY_FAILED_CODE, asyncResult.exception, CLIRListPreference.this.mPhone);
            }
        }

        private void handleGetCLIRResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            int i = message.arg2;
            CLIRListPreference cLIRListPreference = CLIRListPreference.this;
            if (i == 3) {
                cLIRListPreference.mTcpListener.onFinished(CLIRListPreference.this, false);
            } else {
                cLIRListPreference.mTcpListener.onFinished(CLIRListPreference.this, true);
            }
            CLIRListPreference cLIRListPreference2 = CLIRListPreference.this;
            cLIRListPreference2.clirArray = null;
            if (asyncResult.exception != null) {
                if (cLIRListPreference2.DBG) {
                    Log.d("CLIRListPreference", "handleGetCLIRResponse: ar.exception=" + asyncResult.exception);
                }
                CLIRListPreference.this.handleCommandException(asyncResult.exception);
                return;
            }
            if (asyncResult.userObj instanceof Throwable) {
                cLIRListPreference2.mTcpListener.onError(CLIRListPreference.this, 400);
                return;
            }
            int[] iArr = (int[]) asyncResult.result;
            if (iArr.length != 2) {
                cLIRListPreference2.mTcpListener.onError(CLIRListPreference.this, 400);
                return;
            }
            if (cLIRListPreference2.DBG) {
                Log.d("CLIRListPreference", "handleGetCLIRResponse: CLIR successfully queried, clirArray[0]=" + iArr[0] + ", clirArray[1]=" + iArr[1]);
            }
            CLIRListPreference.this.handleGetCLIRResult(iArr);
        }

        private void handleSetCLIRResponse(Message message) {
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult.exception != null && CLIRListPreference.this.DBG) {
                Log.d("CLIRListPreference", "handleSetCallWaitingResponse: ar.exception=" + asyncResult.exception);
            }
            if (CLIRListPreference.this.DBG) {
                Log.d("CLIRListPreference", "handleSetCallWaitingResponse: re get");
            }
            CLIRListPreference.this.mPhone.getOutgoingCallerIdDisplay(obtainMessage(0, 3, 3, asyncResult.exception));
        }
    }
}
