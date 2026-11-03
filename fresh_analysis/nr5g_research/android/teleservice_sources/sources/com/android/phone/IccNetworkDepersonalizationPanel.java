package com.android.phone;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.MiuiTelephonyManagerStub;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyBaseProxy;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import com.android.services.telephony.MiTelephonyServiceStub;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.IDepersoResCallback;
import com.xiaomi.mirror.synergy.MirrorDesktopHelper;
import com.xiaomi.settingsdk.backup.data.KeyStringSettingItem;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miuix.animation.utils.EaseManager;

/* JADX INFO: loaded from: classes.dex */
public class IccNetworkDepersonalizationPanel extends IccPanel {
    private Context mContext;
    private Button mDismissButton;
    View.OnClickListener mDismissListener;
    private LinearLayout mEntryPanel;
    private Handler mHandler;
    private boolean mIsSimLockEnabled;
    private int mLockRetryCount;
    private IccCardApplicationStatus.PersoSubState mPersoSubState;
    private int mPersoSubtype;
    private TextView mPersoSubtypeText;
    private Phone mPhone;
    private TextView mPhoneIdText;
    private EditText mPinEntry;
    private TextWatcher mPinEntryWatcher;
    private RilHookListener mRilHookListener;
    private boolean mSimLocked;
    private SubscriptionInfo mSir;
    private LinearLayout mStatusPanel;
    private TextView mStatusText;
    private TelephonyManager mTelephonyManager;
    private UiccProfile mUiccProfile;
    private Button mUnlockButton;
    private TextView mUnlockChancesLeft;
    View.OnClickListener mUnlockListener;
    private static boolean[] sShowingDialog = new boolean[TelephonyManager.getDefault().getSupportedModemCount()];
    private static IccNetworkDepersonalizationPanel[] sNdpPanel = new IccNetworkDepersonalizationPanel[TelephonyManager.getDefault().getSupportedModemCount()];
    public static final boolean IS_JP_SB = "jp_sb".equals(SystemProperties.get("ro.miui.customized.region"));

    enum statusType {
        ENTRY,
        IN_PROGRESS,
        ERROR,
        SUCCESS
    }

    class RilHookListener implements BaseEvent.IEventListener {
        private RilHookListener() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof TelephonyEvent.RilEvent) {
                boolean zIsRilReady = ((TelephonyEvent.RilEvent) baseEvent).isRilReady();
                IccNetworkDepersonalizationPanel.this.log("onQcRilReady qcRilReady=" + zIsRilReady);
                if (zIsRilReady) {
                    IccNetworkDepersonalizationPanel.this.queryLockedNum();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void queryLockedNum() {
        if (!isClaroOrMxTelcel()) {
            Log.i("PhoneGlobals", "queryLockedNum not claro or telcel");
            return;
        }
        int deviceNetworkPersonalizedNum = RilHook.get(this.mContext).getDeviceNetworkPersonalizedNum();
        Log.i("PhoneGlobals", "queryLockedNum lockNum=" + deviceNetworkPersonalizedNum);
        this.mHandler.obtainMessage(200, deviceNetworkPersonalizedNum, 0).sendToTarget();
    }

    public IccNetworkDepersonalizationPanel(Context context, Phone phone, int i) {
        super(context);
        this.mLockRetryCount = 5;
        this.mIsSimLockEnabled = true;
        this.mRilHookListener = null;
        this.mSimLocked = false;
        this.mPinEntryWatcher = new TextWatcher() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.1
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i2, int i3, int i4) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (SpecialCharSequenceMgr.handleChars(IccNetworkDepersonalizationPanel.this.getContext(), editable.toString())) {
                    IccNetworkDepersonalizationPanel.this.mPinEntry.getText().clear();
                }
            }
        };
        this.mHandler = new Handler() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.2
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                String str;
                int i2 = message.what;
                if (i2 == 100) {
                    AsyncResult asyncResult = (AsyncResult) message.obj;
                    if (asyncResult != null && asyncResult.exception != null) {
                        IccNetworkDepersonalizationPanel.this.displayStatus(statusType.ERROR.name());
                        IccNetworkDepersonalizationPanel.this.querySimlockRetries();
                        postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                IccNetworkDepersonalizationPanel.this.hideAlert();
                                IccNetworkDepersonalizationPanel.this.mPinEntry.getText().clear();
                                IccNetworkDepersonalizationPanel.this.mPinEntry.requestFocus();
                            }
                        }, 3000L);
                        if (ModemProvider.IS_QCOM) {
                            IccNetworkDepersonalizationPanel.this.queryLockedNum();
                            return;
                        }
                        return;
                    }
                    IccNetworkDepersonalizationPanel.this.displayStatus(statusType.SUCCESS.name());
                    IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel = IccNetworkDepersonalizationPanel.this;
                    iccNetworkDepersonalizationPanel.unlockNoSimcardNetwork(iccNetworkDepersonalizationPanel.mPinEntry.getText().toString());
                    IccNetworkDepersonalizationPanel.this.indicateSuccess();
                    postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.2.2
                        @Override // java.lang.Runnable
                        public void run() {
                            IccNetworkDepersonalizationPanel.this.dismiss();
                        }
                    }, 3000L);
                    return;
                }
                if (i2 == 201) {
                    IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel2 = IccNetworkDepersonalizationPanel.this;
                    iccNetworkDepersonalizationPanel2.displayHint(iccNetworkDepersonalizationPanel2.getContext().getString(IccNetworkDepersonalizationPanel.this.getResId(R.string.call_barring_change_pwd), 4, 16));
                    postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.2.3
                        @Override // java.lang.Runnable
                        public void run() {
                            IccNetworkDepersonalizationPanel.this.hideAlert();
                            IccNetworkDepersonalizationPanel.this.mPinEntry.getText().clear();
                            IccNetworkDepersonalizationPanel.this.mPinEntry.requestFocus();
                        }
                    }, 3000L);
                    return;
                }
                if (i2 == 202) {
                    Throwable th = ((AsyncResult) message.obj).exception;
                    IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel3 = IccNetworkDepersonalizationPanel.this;
                    if (th != null) {
                        iccNetworkDepersonalizationPanel3.querySimlockRetries();
                        IccNetworkDepersonalizationPanel.this.indicateError();
                        postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.2.4
                            @Override // java.lang.Runnable
                            public void run() {
                                IccNetworkDepersonalizationPanel.this.hideAlert();
                                IccNetworkDepersonalizationPanel.this.mPinEntry.getText().clear();
                                IccNetworkDepersonalizationPanel.this.mPinEntry.requestFocus();
                            }
                        }, 3000L);
                        if (ModemProvider.IS_QCOM) {
                            IccNetworkDepersonalizationPanel.this.queryLockedNum();
                            return;
                        }
                        return;
                    }
                    iccNetworkDepersonalizationPanel3.indicateSuccess();
                    postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.2.5
                        @Override // java.lang.Runnable
                        public void run() {
                            IccNetworkDepersonalizationPanel.this.dismiss();
                        }
                    }, 3000L);
                    return;
                }
                if (i2 == 203) {
                    AsyncResult asyncResult2 = (AsyncResult) message.obj;
                    if (asyncResult2.exception == null && (str = ((String[]) asyncResult2.result)[0]) != null) {
                        IccNetworkDepersonalizationPanel.this.handleATCommandResult(str);
                        IccNetworkDepersonalizationPanel.this.log("retryCount: " + IccNetworkDepersonalizationPanel.this.mLockRetryCount);
                        IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel4 = IccNetworkDepersonalizationPanel.this;
                        iccNetworkDepersonalizationPanel4.updateUi(iccNetworkDepersonalizationPanel4.mLockRetryCount);
                        return;
                    }
                    return;
                }
                if (i2 == 101) {
                    AsyncResult asyncResult3 = (AsyncResult) message.obj;
                    if (asyncResult3.exception != null) {
                        IccNetworkDepersonalizationPanel.this.mSimLocked = false;
                        return;
                    }
                    String[] strArr = (String[]) asyncResult3.result;
                    int i3 = Integer.parseInt(String.valueOf(strArr[0].charAt(strArr[0].indexOf(44) + 1)));
                    IccNetworkDepersonalizationPanel.this.mSimLocked = i3 == 1;
                    if (!IccNetworkDepersonalizationPanel.this.mSimLocked) {
                        IccNetworkDepersonalizationPanel.this.indicateNoNeedUnlock();
                    }
                    Log.i("PhoneGlobals", "query success lockStatus=" + i3 + ",mSimLocked =" + IccNetworkDepersonalizationPanel.this.mSimLocked);
                    return;
                }
                if (i2 == 200) {
                    Log.i("PhoneGlobals", "sim lock state: " + message.arg1);
                    int i4 = message.arg1;
                    if (i4 < 0) {
                        IccNetworkDepersonalizationPanel.this.mUnlockButton.setEnabled(false);
                        IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel5 = IccNetworkDepersonalizationPanel.this;
                        iccNetworkDepersonalizationPanel5.displayHint(iccNetworkDepersonalizationPanel5.mContext.getString(MiTelephonyServiceStub.get().getMiuiResId(R.string.sim_ndp_unlock_text)));
                    } else {
                        IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel6 = IccNetworkDepersonalizationPanel.this;
                        if (i4 == 0) {
                            iccNetworkDepersonalizationPanel6.mUnlockButton.setEnabled(false);
                            IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel7 = IccNetworkDepersonalizationPanel.this;
                            iccNetworkDepersonalizationPanel7.displayHint(iccNetworkDepersonalizationPanel7.mContext.getString(MiTelephonyServiceStub.get().getMiuiResId(R.string.requesting_unlock)));
                        } else {
                            iccNetworkDepersonalizationPanel6.mUnlockButton.setEnabled(true);
                        }
                    }
                    IccNetworkDepersonalizationPanel.this.mUnlockChancesLeft.setText(IccNetworkDepersonalizationPanel.this.mContext.getString(MiTelephonyServiceStub.get().getMiuiResId(R.string.unlock_success)) + String.valueOf(message.arg1));
                    postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.2.6
                        @Override // java.lang.Runnable
                        public void run() {
                            IccNetworkDepersonalizationPanel.this.hideAlert();
                            IccNetworkDepersonalizationPanel.this.mPinEntry.getText().clear();
                            IccNetworkDepersonalizationPanel.this.mPinEntry.requestFocus();
                        }
                    }, 3000L);
                }
            }
        };
        this.mUnlockListener = new View.OnClickListener() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String string = IccNetworkDepersonalizationPanel.this.mPinEntry.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    return;
                }
                if (string.length() < 4 || string.length() > 16) {
                    IccNetworkDepersonalizationPanel.this.mHandler.obtainMessage(EaseManager.EaseStyleDef.PERLIN).sendToTarget();
                    return;
                }
                if (IccNetworkDepersonalizationPanel.IS_JP_SB && IccNetworkDepersonalizationPanel.this.mPinEntry.isEnabled() && (string.length() < 8 || string.length() > 16)) {
                    IccNetworkDepersonalizationPanel.this.mStatusText.setText(IccNetworkDepersonalizationPanel.this.mPhone.getContext().getString(R.string.jp_sb_sim_lock_unlock_psw_max_length, 8, 16));
                    IccNetworkDepersonalizationPanel.this.mEntryPanel.setVisibility(8);
                    IccNetworkDepersonalizationPanel.this.mStatusPanel.setVisibility(0);
                    IccNetworkDepersonalizationPanel.this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.3.1
                        @Override // java.lang.Runnable
                        public void run() {
                            IccNetworkDepersonalizationPanel.this.hideAlert();
                            IccNetworkDepersonalizationPanel.this.mPinEntry.getText().clear();
                            IccNetworkDepersonalizationPanel.this.mPinEntry.requestFocus();
                        }
                    }, 3000L);
                    return;
                }
                int state = IccNetworkDepersonalizationPanel.this.mPersoSubState.getState();
                IccNetworkDepersonalizationPanel.this.log("Requesting De-Personalization for subtype " + IccNetworkDepersonalizationPanel.this.mPersoSubtype);
                try {
                    boolean zEquals = "za_mt".equals(SystemProperties.get("ro.miui.customized.region", ""));
                    IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel = IccNetworkDepersonalizationPanel.this;
                    if (zEquals) {
                        boolean z = iccNetworkDepersonalizationPanel.mSimLocked;
                        IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel2 = IccNetworkDepersonalizationPanel.this;
                        if (z) {
                            iccNetworkDepersonalizationPanel2.unlockSimState(string);
                        } else {
                            iccNetworkDepersonalizationPanel2.indicateNoNeedUnlock();
                            return;
                        }
                    } else if (iccNetworkDepersonalizationPanel.mTelephonyManager.hasIccCard()) {
                        IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel3 = IccNetworkDepersonalizationPanel.this;
                        if (iccNetworkDepersonalizationPanel3.isSpecialCaseClaro(iccNetworkDepersonalizationPanel3.mPhone)) {
                            IccNetworkDepersonalizationPanel.this.unlockNoSimcardNetwork(string);
                        } else {
                            boolean z2 = ModemProvider.IS_QCOM;
                            IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel4 = IccNetworkDepersonalizationPanel.this;
                            if (!z2) {
                                iccNetworkDepersonalizationPanel4.mPhone.getIccCard().supplySimDepersonalization(IccNetworkDepersonalizationPanel.this.mPersoSubState, string, Message.obtain(IccNetworkDepersonalizationPanel.this.mHandler, 100));
                            } else {
                                iccNetworkDepersonalizationPanel4.supplyExtIccDepersonalization(string, state, iccNetworkDepersonalizationPanel4.mPhone.getPhoneId());
                            }
                        }
                    } else {
                        IccNetworkDepersonalizationPanel.this.unlockNoSimcardNetwork(string);
                    }
                } catch (NullPointerException e) {
                    IccNetworkDepersonalizationPanel.this.log("NullPointerException @supplySimDepersonalization" + e);
                }
                IccNetworkDepersonalizationPanel.this.displayStatus(statusType.IN_PROGRESS.name());
            }
        };
        this.mDismissListener = new View.OnClickListener() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                IccNetworkDepersonalizationPanel.this.dismiss();
            }
        };
        this.mContext = context;
        this.mPhone = phone == null ? PhoneGlobals.getPhone() : phone;
        this.mPersoSubtype = i;
        this.mSir = SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(this.mPhone.getPhoneId());
        this.mTelephonyManager = (TelephonyManager) context.getSystemService("phone");
        this.mUiccProfile = UiccController.getInstance().getUiccProfileForPhone(this.mPhone.getPhoneId());
    }

    public static void showDialog(Phone phone, int i) {
        int phoneId = phone == null ? 0 : phone.getPhoneId();
        boolean[] zArr = sShowingDialog;
        if (phoneId >= zArr.length) {
            Log.e("PhoneGlobals", "[IccNetworkDepersonalizationPanel] showDialog; invalid phoneId" + phoneId);
        } else {
            if (zArr[phoneId]) {
                Log.i("PhoneGlobals", "[IccNetworkDepersonalizationPanel] - showDialog; skipped already shown.");
                return;
            }
            Log.i("PhoneGlobals", "[IccNetworkDepersonalizationPanel] - showDialog; showing dialog.");
            sShowingDialog[phoneId] = true;
            sNdpPanel[phoneId] = new IccNetworkDepersonalizationPanel(PhoneGlobals.getInstance(), phone, i);
            sNdpPanel[phoneId].show();
        }
    }

    public static void dialogDismiss(int i) {
        boolean[] zArr = sShowingDialog;
        if (i >= zArr.length) {
            Log.e("PhoneGlobals", "[IccNetworkDepersonalizationPanel] - dismiss; invalid phoneId " + i);
        } else {
            IccNetworkDepersonalizationPanel iccNetworkDepersonalizationPanel = sNdpPanel[i];
            if (iccNetworkDepersonalizationPanel == null || !zArr[i]) {
                return;
            }
            iccNetworkDepersonalizationPanel.dismiss();
        }
    }

    @Override // com.android.phone.IccPanel, android.app.Dialog
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.sim_ndp);
        this.mRilHookListener = new RilHookListener();
        if (ModemProvider.IS_QCOM) {
            EventHubManager.getInstance().registerListener("telephony", "ril_ready", this.mRilHookListener, this.mHandler);
        }
        EditText editText = (EditText) findViewById(R.id.pin_entry);
        this.mPinEntry = editText;
        editText.setKeyListener(DialerKeyListener.getInstance());
        this.mPinEntry.setOnClickListener(this.mUnlockListener);
        Editable text = this.mPinEntry.getText();
        text.setSpan(this.mPinEntryWatcher, 0, text.length(), 18);
        this.mEntryPanel = (LinearLayout) findViewById(R.id.entry_panel);
        this.mPersoSubtypeText = (TextView) findViewById(R.id.perso_subtype_text);
        this.mPhoneIdText = (TextView) findViewById(R.id.perso_phoneid_text);
        if (isClaroOrMxTelcel()) {
            setPersoSubText(this.mPersoSubtypeText);
        }
        displayStatus(statusType.ENTRY.name());
        Button button = (Button) findViewById(R.id.ndp_unlock);
        this.mUnlockButton = button;
        button.setOnClickListener(this.mUnlockListener);
        this.mDismissButton = (Button) findViewById(R.id.ndp_dismiss);
        boolean z = PhoneGlobals.getInstance().getCarrierConfig().getBoolean("sim_network_unlock_allow_dismiss_bool");
        Button button2 = this.mDismissButton;
        if (z) {
            button2.setVisibility(0);
            this.mDismissButton.setOnClickListener(this.mDismissListener);
        } else {
            button2.setVisibility(8);
        }
        this.mStatusPanel = (LinearLayout) findViewById(R.id.status_panel);
        this.mStatusText = (TextView) findViewById(R.id.status_text);
        if (isClaroOrMxTelcel()) {
            this.mUnlockChancesLeft = (TextView) findViewById(getResId(R.id.status_text));
            querySimlockRetries();
            this.mUnlockChancesLeft.setVisibility(0);
        }
        if (ModemProvider.IS_MTK && "za_mt".equals(SystemProperties.get("ro.miui.customized.region", ""))) {
            querySimlockState();
        }
    }

    @Override // com.android.phone.IccPanel, android.app.Dialog
    protected void onStart() {
        super.onStart();
    }

    @Override // com.android.phone.IccPanel, android.app.Dialog
    public void onStop() {
        super.onStop();
        Log.i("PhoneGlobals", "[IccNetworkDepersonalizationPanel] - showDialog; hiding dialog.");
        Phone phone = this.mPhone;
        int phoneId = phone == null ? 0 : phone.getPhoneId();
        boolean[] zArr = sShowingDialog;
        if (phoneId >= zArr.length) {
            Log.e("PhoneGlobals", "[IccNetworkDepersonalizationPanel] - onStop; invalid phoneId " + phoneId);
        } else {
            zArr[phoneId] = false;
            if (ModemProvider.IS_QCOM) {
                EventHubManager.getInstance().unregisterListener(this.mRilHookListener);
            }
        }
    }

    @Override // com.android.phone.IccPanel, android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isSpecialCaseClaro(Phone phone) {
        if (isClaroOrMxTelcel() && phone != null && phone.getPhoneId() == 0 && this.mTelephonyManager.getActiveModemCount() == 2 && !this.mTelephonyManager.hasIccCard(0) && this.mTelephonyManager.hasIccCard(1)) {
            String str = SystemProperties.get("ro.miui.region", "");
            if ("".equals(str)) {
                str = SystemProperties.get("ro.product.locale.region", "");
            }
            if (MiuiTelephonyManagerStub.get().isSupportEsimForCountry(str) && !TelephonyBaseProxy.get().isEsimActive()) {
                Log.d("PhoneGlobals", "lm-cr or mx_telcel special case");
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayHint(String str) {
        this.mStatusText.setText(str);
        this.mEntryPanel.setVisibility(8);
        this.mStatusPanel.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void indicateError() {
        this.mStatusText.setText(R.string.unlock_failed);
        this.mEntryPanel.setVisibility(8);
        this.mStatusPanel.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void supplyExtIccDepersonalization(String str, int i, int i2) {
        try {
            ExtTelephonyManager extTelephonyManager = ExtTelephonyManager.getInstance(this.mPhone.getContext());
            if (extTelephonyManager == null) {
                return;
            }
            extTelephonyManager.supplyIccDepersonalization(str, Integer.toString(i), new IDepersoResCallback.Stub() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.4
                public void onDepersoResult(int i3, int i4) {
                    IccNetworkDepersonalizationPanel.this.log("on deperso result received: " + i3);
                    IccNetworkDepersonalizationPanel.this.mHandler.obtainMessage(100, i3, i4).sendToTarget();
                }
            }, i2);
        } catch (Exception e) {
            log("supplyIccDepersonalization failed" + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void indicateSuccess() {
        this.mStatusText.setText(R.string.unlock_success);
        this.mEntryPanel.setVisibility(8);
        this.mStatusPanel.setVisibility(0);
    }

    private void querySimlockState() {
        if (ModemProvider.IS_MTK) {
            sendATCommand(new String[]{"AT+ESMLCK", "+ESMLCK"}, 101);
            return;
        }
        UiccProfile uiccProfile = this.mUiccProfile;
        if (uiccProfile != null) {
            uiccProfile.queryIccNetworkLock(0, this.mHandler.obtainMessage(101));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unlockSimState(String str) {
        if (ModemProvider.IS_MTK) {
            sendATCommand(new String[]{"AT+ESMLCK=0,0,\"" + str + "\"", "+ESMLCK"}, 100);
            return;
        }
        UiccProfile uiccProfile = this.mUiccProfile;
        if (uiccProfile != null) {
            uiccProfile.setIccNetworkLockEnabled(0, 0, str, "", "", "", this.mHandler.obtainMessage(100));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayStatus(String str) {
        this.mPersoSubState = IccCardApplicationStatus.PersoSubState.values()[this.mPersoSubtype];
        log("displayStatus mPersoSubState: " + this.mPersoSubState.name() + "type: " + str);
        int identifier = getContext().getResources().getIdentifier(this.mPersoSubState.name() + "_" + str, KeyStringSettingItem.TYPE, "android");
        if (identifier == 0) {
            log("Unable to get the PersoSubType string");
            return;
        }
        if (!IccCardApplicationStatus.PersoSubState.isPersoLocked(this.mPersoSubState)) {
            log("Unsupported Perso Subtype :" + this.mPersoSubState.name());
            return;
        }
        SubscriptionInfo subscriptionInfo = this.mSir;
        if (subscriptionInfo != null) {
            CharSequence displayName = subscriptionInfo.getDisplayName();
            log("Operator displayName is: " + ((Object) displayName) + "phoneId: " + this.mPhone.getPhoneId());
            if (displayName != null && displayName != "") {
                this.mPhoneIdText.setText(getContext().getString(R.string.label_phoneid) + ": " + ((Object) displayName));
            }
        }
        if (str == statusType.ENTRY.name()) {
            getContext().getString(identifier);
            return;
        }
        this.mStatusText.setText(identifier);
        this.mEntryPanel.setVisibility(8);
        this.mStatusPanel.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void indicateNoNeedUnlock() {
        this.mStatusText.setText(R.string.no_need_unlock_tip);
        this.mEntryPanel.setVisibility(8);
        this.mStatusPanel.setVisibility(0);
        this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.5
            @Override // java.lang.Runnable
            public void run() {
                IccNetworkDepersonalizationPanel.this.dismiss();
            }
        }, MirrorDesktopHelper.TIMEOUT_MILLIS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideAlert() {
        this.mEntryPanel.setVisibility(0);
        this.mStatusPanel.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void log(String str) {
        Log.d("PhoneGlobals", "[IccNetworkDepersonalizationPanel] " + str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unlockNoSimcardNetwork(String str) {
        if (ModemProvider.IS_MTK) {
            log("unlockNoSimcardNetwork");
            sendATCommand(new String[]{"AT+ESMLCK = 0,0,\"" + str + "\"", "+ESMLCK"}, 202);
            return;
        }
        UiccProfile uiccProfile = this.mUiccProfile;
        if (uiccProfile != null) {
            uiccProfile.setIccNetworkLockEnabled(0, 0, str, "", "", "", this.mHandler.obtainMessage(202));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void querySimlockRetries() {
        if (!isClaroOrMxTelcel()) {
            Log.i("PhoneGlobals", "querySimlockRetries not claro or telcel");
            return;
        }
        log("querySimlockRetries");
        if (ModemProvider.IS_MTK) {
            sendATCommand(new String[]{"AT+ESMLCK", "+ESMLCK"}, 203);
            return;
        }
        UiccProfile uiccProfile = this.mUiccProfile;
        if (uiccProfile != null) {
            uiccProfile.queryIccNetworkLock(0, this.mHandler.obtainMessage(203));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUi(int i) {
        Log.i("PhoneGlobals", "sim lock state: " + i);
        if (!this.mIsSimLockEnabled) {
            this.mUnlockButton.setEnabled(false);
            displayHint(getContext().getString(getResId(R.string.sim_ndp_unlock_text)));
            this.mHandler.postDelayed(new Runnable() { // from class: com.android.phone.IccNetworkDepersonalizationPanel.7
                @Override // java.lang.Runnable
                public void run() {
                    IccNetworkDepersonalizationPanel.this.dismiss();
                }
            }, 3000L);
        } else {
            Button button = this.mUnlockButton;
            if (i == 0) {
                button.setEnabled(false);
                displayHint(getContext().getString(getResId(R.string.requesting_unlock)));
            } else {
                button.setEnabled(true);
            }
        }
        this.mUnlockChancesLeft.setText(getContext().getString(getResId(R.string.unlock_success)) + String.valueOf(i));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleATCommandResult(String str) {
        if (ModemProvider.IS_MTK) {
            log("handleATCommandResult: " + str);
            ArrayList arrayList = new ArrayList();
            Matcher matcher = Pattern.compile("(?<=\\()(.+?)(?=\\))").matcher(str);
            while (matcher.find()) {
                arrayList.add(matcher.group());
            }
            if (arrayList.isEmpty()) {
                return;
            }
            String[] strArrSplit = ((String) arrayList.get(0)).split(",");
            if (Integer.parseInt(strArrSplit[1]) == 2) {
                this.mIsSimLockEnabled = false;
            }
            this.mLockRetryCount = Integer.parseInt(strArrSplit[2]);
        }
    }

    private boolean isClaroOrMxTelcel() {
        return miui.telephony.TelephonyManager.isCustForMxTelcel() || miui.telephony.TelephonyManager.isCustForLmClaro();
    }

    private void setPersoSubText(TextView textView) {
        textView.setText(R.string.label_ndp_nck);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getResId(int i) {
        if (i == R.string.labelCF) {
            return R.string.title_cfu_enabled;
        }
        if (i == R.string.notification_network_selection_title) {
            return R.string.notification_switch_auto_title;
        }
        if (i == R.string.notification_network_selection_text) {
            return R.string.notification_switch_auto_text;
        }
        if (i == R.string.sim_ndp_unlock_text) {
            return R.string.sim_me_lock_disabled;
        }
        if (i == R.string.requesting_unlock) {
            return R.string.no_unlock_chance_hint;
        }
        if (i == R.string.unlock_success) {
            return R.string.unlock_chances_left;
        }
        if (i == R.string.call_barring_change_pwd) {
            return R.string.strPasswordLengthIncorrect;
        }
        return i == R.id.status_text ? R.id.unlock_chances_left : i;
    }

    private void sendATCommand(String[] strArr, int i) {
        FiveGManagerBase.getInstance().sendAtCommand(this.mPhone, strArr, Message.obtain(this.mHandler, i));
    }
}
