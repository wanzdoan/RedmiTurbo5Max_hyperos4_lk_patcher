package com.android.phone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.provider.MiuiSettings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.preference.PreferenceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;
import java.util.Map;
import miui.telephony.SubscriptionInfo;
import miui.telephony.SubscriptionManager;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.ProgressDialog;

/* JADX INFO: loaded from: classes.dex */
public class SmartCallForwardManager extends Handler {
    private static SmartCallForwardManager sInstance;
    private Context mContext = null;
    private AlertDialog mNumberConfirmingDialog = null;
    private AlertDialog mSetCFExceptionDialog = null;
    private ProgressDialog mProgressDialog = null;
    private CallBack mCallBack = null;
    private Map mForwardNumberMap = new HashMap();
    private int mSetCFExceptionCount = 0;
    private boolean mIsNeedUI = true;

    public interface CallBack {
        void onSetSmartCallForwardResult(boolean z);
    }

    public static SmartCallForwardManager getInstance() {
        if (sInstance == null) {
            sInstance = new SmartCallForwardManager();
        }
        return sInstance;
    }

    public void setSmartCallForward(Context context, boolean z, boolean z2, int i, CallBack callBack) {
        this.mCallBack = callBack;
        this.mContext = context;
        this.mIsNeedUI = z2;
        if (z) {
            showSmartCallForwardDialog();
            return;
        }
        AlertDialog alertDialog = this.mNumberConfirmingDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mNumberConfirmingDialog.dismiss();
        } else {
            setSmartCallForward(z, 2, i);
        }
    }

    public void showSmartCallForwardDialog() {
        View viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.number_confirming_layout, (ViewGroup) null);
        final EditText editText = (EditText) viewInflate.findViewById(R.id.sim_number_1);
        SubscriptionInfo availableSubscriptionInfoBySlot = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(0);
        editText.setText(availableSubscriptionInfoBySlot == null ? null : availableSubscriptionInfoBySlot.getDisplayNumber());
        final EditText editText2 = (EditText) viewInflate.findViewById(R.id.sim_number_2);
        SubscriptionInfo availableSubscriptionInfoBySlot2 = MiuiPhoneUtils.getAvailableSubscriptionInfoBySlot(1);
        editText2.setText(availableSubscriptionInfoBySlot2 != null ? availableSubscriptionInfoBySlot2.getDisplayNumber() : null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setView(viewInflate);
        builder.setTitle(R.string.confirm_phone_number);
        builder.setMessage(R.string.dual_sim_smart_call_forward_dialog_message);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.android.phone.SmartCallForwardManager.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                String string = editText.getText().toString();
                String string2 = editText2.getText().toString();
                if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) {
                    if (SmartCallForwardManager.this.mCallBack != null) {
                        SmartCallForwardManager.this.mCallBack.onSetSmartCallForwardResult(false);
                        return;
                    }
                    return;
                }
                SmartCallForwardManager.this.mForwardNumberMap.clear();
                SmartCallForwardManager.this.mSetCFExceptionCount = 0;
                String[] strArr = {string2, string};
                int i2 = 0;
                while (true) {
                    int i3 = MiuiPhoneUtils.PHONE_COUNT;
                    SmartCallForwardManager smartCallForwardManager = SmartCallForwardManager.this;
                    if (i2 < i3) {
                        smartCallForwardManager.mForwardNumberMap.put(Integer.valueOf(i2), strArr[i2]);
                        i2++;
                    } else {
                        smartCallForwardManager.setSmartCallForward(true, 0, 0);
                        return;
                    }
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.android.phone.SmartCallForwardManager.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (SmartCallForwardManager.this.mCallBack != null) {
                    SmartCallForwardManager.this.mCallBack.onSetSmartCallForwardResult(false);
                }
            }
        });
        AlertDialog alertDialogCreate = builder.create();
        this.mNumberConfirmingDialog = alertDialogCreate;
        alertDialogCreate.show();
    }

    public void setSmartCallForward(boolean z, int i, int i2) {
        Log.d("SmartCallForwardManager", "set sim: " + i2 + " to " + z + " for message: " + i);
        maybeShowOrDismissProgressDialog(this.mIsNeedUI);
        int i3 = z ? 3 : 0;
        Phone[] phones = PhoneFactory.getPhones();
        if (i2 < phones.length) {
            Phone phone = phones[i2];
            Map map = this.mForwardNumberMap;
            PhoneProxy.setCallForwardingOption(phone, i3, 3, map != null ? (String) map.get(Integer.valueOf(i2)) : null, 1, 0, obtainMessage(i, i3, i2));
        }
    }

    public void maybeShowOrDismissProgressDialog(boolean z) {
        ProgressDialog progressDialog;
        if (z && this.mProgressDialog == null) {
            if (sInstance == null) {
                Log.w("SmartCallForwardManager", "skip progress dialog since the host page is destroyed.");
                return;
            }
            ProgressDialog progressDialog2 = new ProgressDialog(this.mContext);
            this.mProgressDialog = progressDialog2;
            progressDialog2.setMessage(this.mContext.getResources().getString(R.string.dual_sim_smart_call_forward_updating));
            this.mProgressDialog.setIndeterminate(true);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.setMax(100);
            this.mProgressDialog.show();
            return;
        }
        if (z || (progressDialog = this.mProgressDialog) == null) {
            return;
        }
        progressDialog.dismiss();
        this.mProgressDialog = null;
        if (this.mSetCFExceptionCount == 0 || !this.mIsNeedUI) {
            return;
        }
        showSetCFExceptionDialog();
        this.mSetCFExceptionCount = 0;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        boolean z = message.arg1 == 3;
        int size = SubscriptionManager.getDefault().getAvailableSubscriptionInfoList().size();
        StringBuilder sb = new StringBuilder("handleMessage: what=");
        sb.append(message.what);
        sb.append(", phoneId=");
        sb.append(message.arg2);
        sb.append(", enabled=");
        sb.append(z);
        sb.append(", destroyed=");
        sb.append(sInstance == null);
        Log.d("SmartCallForwardManager", sb.toString());
        int i = message.what;
        if (i != 0 && i != 2) {
            if (i != 4) {
                return;
            }
            handleSetCFResponse(!z, false);
            return;
        }
        int i2 = message.arg2;
        if (i2 == 0) {
            if (((AsyncResult) message.obj).exception != null) {
                this.mSetCFExceptionCount++;
                handleSetCFResponse(z, false);
                return;
            } else if (size > 1) {
                setSmartCallForward(z, i, 1);
                return;
            } else {
                handleSetCFResponse(z, true);
                return;
            }
        }
        if (i2 == 1 && ((AsyncResult) message.obj).exception != null) {
            this.mSetCFExceptionCount++;
            setSmartCallForward(!z, 4, 0);
            Log.d("SmartCallForwardManager", "reset sim1 callforward since the sim2 failed.");
        } else {
            MiuiSettings.Telephony.setSmartCallForwardEnabled(this.mContext.getContentResolver(), z);
            handleSetCFResponse(z, true);
        }
    }

    private void handleSetCFResponse(boolean z, boolean z2) {
        Log.d("SmartCallForwardManager", "The result is " + z2 + " when switch smart callforward to " + z);
        maybeShowOrDismissProgressDialog(false);
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.onSetSmartCallForwardResult(z2);
        }
        if (z && z2) {
            storageSmartCallForwardNumber();
        }
        HashMap map = new HashMap();
        map.put("smart_call_forward_switch_status", Boolean.valueOf(z));
        map.put("set_smart_call_forward_error", Boolean.valueOf(!z2));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "set_smart_call_forward", map);
    }

    public void showSetCFExceptionDialog() {
        if (sInstance == null) {
            Log.w("SmartCallForwardManager", "skip exception dialog since the host page is destroyed.");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        builder.setTitle(R.string.dual_sim_smart_call_forward_failed);
        builder.setMessage(R.string.dual_sim_smart_call_forward_failed_cause);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.close_dialog, new DialogInterface.OnClickListener(this) { // from class: com.android.phone.SmartCallForwardManager.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialogCreate = builder.create();
        this.mSetCFExceptionDialog = alertDialogCreate;
        alertDialogCreate.show();
    }

    public void storageSmartCallForwardNumber() {
        if (this.mForwardNumberMap == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            if (i != 0) {
                sb.append(',');
            }
            sb.append(this.mForwardNumberMap.get(Integer.valueOf(i)) == null ? "<null>" : (String) this.mForwardNumberMap.get(Integer.valueOf(i)));
        }
        SharedPreferences.Editor editorEdit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        editorEdit.putString("key_telephony_last_stored_number", sb.toString());
        editorEdit.apply();
    }

    public void clear() {
        StringBuilder sb = new StringBuilder("clear: host page destroyed, progressShowing=");
        sb.append(this.mProgressDialog != null);
        sb.append(", exceptionCount=");
        sb.append(this.mSetCFExceptionCount);
        Log.d("SmartCallForwardManager", sb.toString());
        sInstance = null;
        this.mCallBack = null;
        this.mForwardNumberMap = null;
        this.mSetCFExceptionCount = 0;
        AlertDialog alertDialog = this.mNumberConfirmingDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mNumberConfirmingDialog.dismiss();
        }
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null && progressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
        AlertDialog alertDialog2 = this.mSetCFExceptionDialog;
        if (alertDialog2 != null && alertDialog2.isShowing()) {
            this.mSetCFExceptionDialog.dismiss();
        }
        this.mNumberConfirmingDialog = null;
        this.mProgressDialog = null;
        this.mSetCFExceptionDialog = null;
    }
}
