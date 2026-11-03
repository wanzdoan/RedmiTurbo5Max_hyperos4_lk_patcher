package com.android.phone.settings.fdn;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.Phone;
import com.android.phone.R;
import com.android.phone.SubscriptionInfoHelper;
import com.android.services.telephony.relay.phonecall.message.response.ResponseObject;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseFdnContactScreen extends Activity implements FdnErrorDialogFragment.Listener {
    protected String mName;
    protected String mNumber;
    protected Phone mPhone;
    protected String mPin2;
    protected QueryHandler mQueryHandler;
    protected SubscriptionInfoHelper mSubscriptionInfoHelper;
    protected Handler mHandler = new Handler();
    private Handler mFDNHandler = new Handler() { // from class: com.android.phone.settings.fdn.BaseFdnContactScreen.1
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what != 10) {
                return;
            }
            CommandException commandException = ((AsyncResult) message.obj).exception;
            if (commandException != null) {
                if (commandException.getCommandError() == CommandException.Error.SIM_PUK2) {
                    BaseFdnContactScreen.this.showFdnErrorDialog(11);
                    return;
                }
                int i = message.arg1;
                if (i > 0) {
                    Toast.makeText(BaseFdnContactScreen.this, BaseFdnContactScreen.this.getString(R.string.pin2_invalid) + BaseFdnContactScreen.this.getString(R.string.pin2_attempts, new Object[]{Integer.valueOf(i)}), 1).show();
                    BaseFdnContactScreen.this.finish();
                    return;
                }
                return;
            }
            BaseFdnContactScreen.this.pin2AuthenticationSucceed();
        }
    };

    protected void handleResult(boolean z) {
    }

    protected void handleResult(boolean z, boolean z2) {
    }

    protected abstract void pin2AuthenticationSucceed();

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        resolveIntent();
        getWindow().requestFeature(5);
        getWindow().addSystemFlags(524288);
    }

    protected void authenticatePin2() {
        if (this.mPhone.getIccCard().getIccPin2Blocked()) {
            showFdnErrorDialog(17);
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, GetPin2Screen.class);
        intent.setData(FdnList.getContentUri(this.mSubscriptionInfoHelper));
        intent.putExtra("com.android.phone.settings.SubscriptionInfoHelper.SubscriptionId", this.mSubscriptionInfoHelper.getSubId());
        startActivityForResult(intent, 100);
    }

    protected void displayProgress(boolean z) {
        getWindow().setFeatureInt(5, z ? -1 : -2);
    }

    protected void log(String str) {
        Log.d("PhoneGlobals", getClass().getSimpleName() + " : " + str);
    }

    protected void processPin2(String str) {
        this.mPhone.getIccCard().supplyPin2(str, this.mFDNHandler.obtainMessage(10));
    }

    protected void resolveIntent() {
        Intent intent = getIntent();
        SubscriptionInfoHelper subscriptionInfoHelper = new SubscriptionInfoHelper(this, intent);
        this.mSubscriptionInfoHelper = subscriptionInfoHelper;
        this.mPhone = subscriptionInfoHelper.getPhone();
        this.mName = intent.getStringExtra("name");
        this.mNumber = intent.getStringExtra("number");
    }

    protected void showStatus(CharSequence charSequence) {
        if (charSequence != null) {
            Toast.makeText(this, charSequence, 1).show();
        }
    }

    public class QueryHandler extends AsyncQueryHandler {
        protected QueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onInsertComplete(int i, Object obj, Uri uri) {
            BaseFdnContactScreen.this.log("onInsertComplete");
            BaseFdnContactScreen.this.displayProgress(false);
            BaseFdnContactScreen baseFdnContactScreen = BaseFdnContactScreen.this;
            baseFdnContactScreen.handleResult(baseFdnContactScreen.getInsertResult(uri) > 0, false);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onUpdateComplete(int i, Object obj, int i2) {
            BaseFdnContactScreen.this.log("onUpdateComplete");
            BaseFdnContactScreen.this.displayProgress(false);
            BaseFdnContactScreen.this.handleResult(i2 > 0, false);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onDeleteComplete(int i, Object obj, int i2) {
            BaseFdnContactScreen.this.log("onDeleteComplete");
            BaseFdnContactScreen.this.displayProgress(false);
            BaseFdnContactScreen.this.handleResult(i2 > 0);
        }
    }

    protected void showFdnErrorDialog(int i) {
        FragmentManager fragmentManager = getFragmentManager();
        FdnErrorDialogFragment fdnErrorDialogFragment = (FdnErrorDialogFragment) fragmentManager.findFragmentByTag("tag_pin2_locked_dialog");
        if (fdnErrorDialogFragment == null) {
            FdnErrorDialogFragment fdnErrorDialogFragment2 = new FdnErrorDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("key_dialog_id", i);
            fdnErrorDialogFragment2.setArguments(bundle);
            fdnErrorDialogFragment2.show(fragmentManager, "tag_pin2_locked_dialog");
            return;
        }
        FragmentTransaction fragmentTransactionBeginTransaction = fragmentManager.beginTransaction();
        fragmentTransactionBeginTransaction.show(fdnErrorDialogFragment);
        fragmentTransactionBeginTransaction.commitNow();
    }

    @Override // com.android.phone.settings.fdn.FdnErrorDialogFragment.Listener
    public void onRequestPuk2(int i) {
        Intent intent = this.mSubscriptionInfoHelper.getIntent(FdnSetting.class);
        intent.setAction("android.intent.action.MAIN");
        if (i == 12 || i == 17) {
            intent.putExtra("auto_change_pin2", true);
        }
        startActivity(intent);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getInsertResult(Uri uri) {
        if (uri == null) {
            log("[getInsertResult]  uri == null.");
            return 0;
        }
        log("[getInsertResult]  uri.toString() = " + uri.toString());
        String string = uri.toString();
        int iIntValue = string.indexOf(ResponseObject.DEFAULT_MESSAGE_FAILED) == -1 ? 1 : Integer.valueOf(string.replace("content://icc/error/", "")).intValue();
        log("[getInsertResult] result=" + iIntValue);
        return iIntValue;
    }
}
