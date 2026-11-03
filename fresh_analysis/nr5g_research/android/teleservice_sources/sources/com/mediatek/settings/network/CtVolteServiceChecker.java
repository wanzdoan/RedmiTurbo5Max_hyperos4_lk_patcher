package com.mediatek.settings.network;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.ims.ImsReasonInfo;
import android.util.Log;
import com.android.ims.ImsException;
import com.android.ims.ImsManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.TelephonyCommonUtilsStub;
import com.android.phone.R;
import com.mediatek.ims.MtkImsConnectionStateListener;
import com.mediatek.ims.internal.MtkImsManager;
import com.mediatek.internal.telephony.MtkIccCardConstants;
import com.mediatek.internal.telephony.ratconfiguration.RatConfiguration;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CtVolteServiceChecker extends Handler {
    private static CtVolteServiceChecker sInstance;
    private boolean mAirplaneMode;
    private boolean mChecking;
    private Context mContext;
    private DataContentObserver mDataContentObserver;
    private int mDataNetworkType;
    private Dialog mDialog;
    private boolean mImsAvailable;
    private MtkImsConnectionStateListener mImsConnectionStateListener;
    private ImsManager mImsManager;
    private boolean mLteEnabled;
    private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangeListener;
    private Phone mPhone;
    private int mPhoneId;
    private MyPhoneStateListener mPhoneStateListener;
    private boolean mRadioOn;
    private BroadcastReceiver mReceiver;
    private boolean mRoaming;
    private boolean mSimOnOffEnabled;
    private int mSimOnOffState;
    private int mSubId;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private int mVoiceNetworkType;
    private boolean mVolteEnabled;

    private CtVolteServiceChecker(Context context) {
        super(context.getMainLooper());
        this.mSubId = -1;
        this.mChecking = false;
        this.mPhoneStateListener = null;
        this.mImsConnectionStateListener = null;
        this.mDataContentObserver = null;
        this.mOnSubscriptionsChangeListener = new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.mediatek.settings.network.CtVolteServiceChecker.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                Log.d("CtVolteServiceChecker", "onSubscriptionsChanged");
                CtVolteServiceChecker.this.updateListenedSubInfo();
            }
        };
        this.mReceiver = new BroadcastReceiver() { // from class: com.mediatek.settings.network.CtVolteServiceChecker.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                Log.d("CtVolteServiceChecker", "onReceived, action=" + action);
                if (action.equals("android.intent.action.AIRPLANE_MODE")) {
                    CtVolteServiceChecker ctVolteServiceChecker = CtVolteServiceChecker.this;
                    ctVolteServiceChecker.mAirplaneMode = ctVolteServiceChecker.isAirplaneModeOn(ctVolteServiceChecker.mContext);
                    CtVolteServiceChecker.this.updateState();
                } else if (action.equals("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED")) {
                    CtVolteServiceChecker.this.updateListenedSubInfo();
                } else if (action.equals("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED")) {
                    CtVolteServiceChecker.this.handleSubInfoRecordUpdated(intent);
                } else if (action.equals("android.telephony.action.SIM_APPLICATION_STATE_CHANGED")) {
                    CtVolteServiceChecker.this.handleSimApplicationStateChange(intent);
                }
            }
        };
        this.mContext = context;
    }

    public static CtVolteServiceChecker getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CtVolteServiceChecker(context);
        }
        return sInstance;
    }

    public static boolean isMtkC2kSupported() {
        boolean zIsC2kSupported = RatConfiguration.isC2kSupported();
        String str = SystemProperties.get("persist.vendor.radio.disable_c2k_cap", "0");
        Log.d("CtVolteServiceChecker", "isMtkC2kSupported, ratSupport=" + zIsC2kSupported + ", disableC2k=" + str);
        return zIsC2kSupported && !str.equals("1");
    }

    public void init() {
        if (RatConfiguration.isNrSupported() || !isMtkC2kSupported()) {
            return;
        }
        this.mSubscriptionManager = SubscriptionManager.from(this.mContext);
        this.mTelephonyManager = TelephonyManager.from(this.mContext);
        this.mAirplaneMode = isAirplaneModeOn(this.mContext);
        this.mSimOnOffEnabled = MtkTelephonyManagerEx.getDefault().isSimOnOffEnabled();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.AIRPLANE_MODE");
        intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
        intentFilter.addAction("com.mediatek.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
        intentFilter.addAction("android.telephony.action.SIM_APPLICATION_STATE_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
        updateListenedSubInfo();
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mOnSubscriptionsChangeListener);
    }

    private void resetSubInfo() {
        this.mSubId = -1;
        this.mPhoneId = -1;
        this.mPhone = null;
        this.mImsManager = null;
        this.mRadioOn = false;
        this.mRoaming = false;
        this.mDataNetworkType = 0;
        this.mVoiceNetworkType = 0;
        this.mLteEnabled = false;
        this.mVolteEnabled = false;
        this.mImsAvailable = false;
        this.mSimOnOffState = 11;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateListenedSubInfo() {
        int listenedSubId = getListenedSubId();
        int phoneId = SubscriptionManager.getPhoneId(listenedSubId);
        if (listenedSubId == this.mSubId && phoneId == this.mPhoneId) {
            return;
        }
        Log.d("CtVolteServiceChecker", "updateListenedSubInfo, prevSubId=" + this.mSubId + ", prevPhoneId=" + this.mPhoneId + ", newSubId=" + listenedSubId + ", newPhoneId=" + phoneId);
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
        stopTimeOutCheck();
        MyPhoneStateListener myPhoneStateListener = this.mPhoneStateListener;
        if (myPhoneStateListener != null) {
            myPhoneStateListener.unregister();
            this.mPhoneStateListener = null;
        }
        DataContentObserver dataContentObserver = this.mDataContentObserver;
        if (dataContentObserver != null) {
            dataContentObserver.unregister();
            this.mDataContentObserver = null;
        }
        MtkImsConnectionStateListener mtkImsConnectionStateListener = this.mImsConnectionStateListener;
        if (mtkImsConnectionStateListener != null) {
            try {
                this.mImsManager.removeImsConnectionStateListener(mtkImsConnectionStateListener);
            } catch (ImsException unused) {
            }
            this.mImsConnectionStateListener = null;
        }
        resetSubInfo();
        if (listenedSubId != -1) {
            this.mSubId = listenedSubId;
            this.mPhoneId = phoneId;
            this.mPhone = PhoneFactory.getPhone(phoneId);
            this.mImsManager = ImsManager.getInstance(this.mContext, this.mPhoneId);
            Phone phone = this.mPhone;
            ServiceState serviceState = phone == null ? null : phone.getServiceState();
            Phone phone2 = this.mPhone;
            this.mRadioOn = phone2 == null ? false : phone2.isRadioOn();
            this.mRoaming = serviceState == null ? false : serviceState.getRoaming();
            this.mLteEnabled = isLteNetworkTypeEnabled(listenedSubId);
            this.mVolteEnabled = this.mImsManager.isEnhanced4gLteModeSettingEnabledByUser();
            Phone phone3 = this.mPhone;
            this.mImsAvailable = phone3 != null ? phone3.isImsRegistered() : false;
            if (this.mSimOnOffEnabled) {
                this.mSimOnOffState = MtkTelephonyManagerEx.getDefault().getSimOnOffState(this.mPhoneId);
            }
            MyPhoneStateListener myPhoneStateListener2 = new MyPhoneStateListener(Looper.getMainLooper());
            this.mPhoneStateListener = myPhoneStateListener2;
            myPhoneStateListener2.register(this.mSubId);
            DataContentObserver dataContentObserver2 = new DataContentObserver(new Handler(Looper.getMainLooper()), this.mContext, this.mSubId);
            this.mDataContentObserver = dataContentObserver2;
            dataContentObserver2.register();
            if (this.mImsManager instanceof MtkImsManager) {
                MtkImsConnectionStateListener mtkImsConnectionStateListener2 = new MtkImsConnectionStateListener() { // from class: com.mediatek.settings.network.CtVolteServiceChecker.2
                    public void onImsConnected(int i) {
                        if (CtVolteServiceChecker.this.mImsAvailable) {
                            return;
                        }
                        CtVolteServiceChecker.this.mImsAvailable = true;
                        CtVolteServiceChecker.this.updateState();
                    }

                    public void onImsDisconnected(ImsReasonInfo imsReasonInfo) {
                        if (CtVolteServiceChecker.this.mImsAvailable) {
                            CtVolteServiceChecker.this.mImsAvailable = false;
                            CtVolteServiceChecker.this.updateState();
                        }
                    }
                };
                this.mImsConnectionStateListener = mtkImsConnectionStateListener2;
                try {
                    this.mImsManager.addImsConnectionStateListener(mtkImsConnectionStateListener2);
                } catch (ImsException unused2) {
                    this.mImsConnectionStateListener = null;
                }
            }
            updateState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateState() {
        boolean zShouldShowVolteAlert = shouldShowVolteAlert();
        Log.d("CtVolteServiceChecker", "updateState, checking=" + this.mChecking + ", shouldShowAlert=" + zShouldShowVolteAlert);
        if (!this.mChecking && zShouldShowVolteAlert) {
            startTimeOutCheck();
        }
        if (!this.mChecking || zShouldShowVolteAlert) {
            return;
        }
        stopTimeOutCheck();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSubInfoRecordUpdated(Intent intent) {
        String stringExtra = intent.getStringExtra("simPropKey");
        Log.d("CtVolteServiceChecker", "handleSubInfoRecordUpdated, propKey=".concat(stringExtra == null ? "null" : stringExtra));
        if (this.mSubId == -1 || !"volte_vt_enabled".equals(stringExtra)) {
            return;
        }
        Log.d("CtVolteServiceChecker", "handleSubInfoRecordUpdated, ENHANCED_4G_MODE_ENABLED");
        boolean zIsEnhanced4gLteModeSettingEnabledByUser = this.mImsManager.isEnhanced4gLteModeSettingEnabledByUser();
        if (zIsEnhanced4gLteModeSettingEnabledByUser != this.mVolteEnabled) {
            this.mVolteEnabled = zIsEnhanced4gLteModeSettingEnabledByUser;
            updateState();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSimApplicationStateChange(Intent intent) {
        if (!this.mSimOnOffEnabled) {
            Log.d("CtVolteServiceChecker", "handleSimApplicationStateChange, SIM On/Off is not enabled.");
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.d("CtVolteServiceChecker", "handleSimApplicationStateChange, extra=null");
            return;
        }
        int i = extras.getInt("slot", -1);
        int i2 = extras.getInt("subscription", -1);
        int simOnOffState = MtkTelephonyManagerEx.getDefault().getSimOnOffState(i);
        if (SubscriptionManager.isValidSubscriptionId(i2) && this.mSubId == i2 && this.mSimOnOffState != simOnOffState) {
            Log.d("CtVolteServiceChecker", "handleSimApplicationStateChange, subId=" + i2 + ", preState=" + this.mSimOnOffState + ", curState=" + simOnOffState);
            this.mSimOnOffState = simOnOffState;
            updateState();
        }
    }

    private int getListenedSubId() {
        List<SubscriptionInfo> activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList(true);
        if (activeSubscriptionInfoList == null) {
            Log.d("CtVolteServiceChecker", "getListenedSubId, subList is null.");
            return -1;
        }
        SubscriptionInfo defaultDataSubscriptionInfo = this.mSubscriptionManager.getDefaultDataSubscriptionInfo();
        if (defaultDataSubscriptionInfo != null) {
            if (isCtSim(defaultDataSubscriptionInfo.getSimSlotIndex())) {
                if (isCt4gSim(defaultDataSubscriptionInfo.getSimSlotIndex())) {
                    return defaultDataSubscriptionInfo.getSubscriptionId();
                }
                return -1;
            }
            if (!MtkImsManager.isSupportMims()) {
                return -1;
            }
        }
        for (SubscriptionInfo subscriptionInfo : activeSubscriptionInfoList) {
            int simSlotIndex = subscriptionInfo.getSimSlotIndex();
            Phone phone = PhoneFactory.getPhone(simSlotIndex);
            ServiceState serviceState = phone == null ? null : phone.getServiceState();
            boolean roaming = serviceState == null ? false : serviceState.getRoaming();
            if (isCt4gSim(simSlotIndex) && !roaming) {
                return subscriptionInfo.getSubscriptionId();
            }
        }
        return -1;
    }

    private boolean shouldShowVolteAlert() {
        boolean z = false;
        if (TelephonyCommonUtilsStub.get().isMiuiRom()) {
            return false;
        }
        if (SubscriptionManager.isValidSubscriptionId(this.mSubId) && isCtVolteEnabled()) {
            boolean zIsCtAutoVolteEnabled = isCtAutoVolteEnabled();
            boolean z2 = this.mImsAvailable && isLteNetwork();
            boolean z3 = this.mSimOnOffState == 11;
            ServiceState serviceStateForSubscriber = this.mTelephonyManager.getServiceStateForSubscriber(this.mSubId);
            boolean z4 = (serviceStateForSubscriber != null ? serviceStateForSubscriber.getVoiceRegState() : 1) == 0;
            if (!this.mAirplaneMode && this.mRadioOn && !this.mRoaming && this.mLteEnabled && this.mVolteEnabled && z3 && !zIsCtAutoVolteEnabled && !z2 && !z4) {
                z = true;
            }
            Log.d("CtVolteServiceChecker", "shouldShowVolteAlert, ret=" + z + ", subId=" + this.mSubId + ", phoneId=" + this.mPhoneId + ", airplaneMode=" + this.mAirplaneMode + ", radioOn=" + this.mRadioOn + ", roaming=" + this.mRoaming + ", lteEnabled=" + this.mLteEnabled + ", volteEnabled=" + this.mVolteEnabled + ", simOnOffState=" + this.mSimOnOffState + ", autoVolte=" + zIsCtAutoVolteEnabled + ", imsAvailable=" + z2 + ", isVoiceEnable = " + z4);
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isLteNetworkTypeEnabled(int i) {
        boolean z;
        int i2 = Settings.Global.getInt(this.mContext.getContentResolver(), "preferred_network_mode" + i, Phone.PREFERRED_NT_MODE);
        switch (i2) {
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 15:
            case 17:
            case 19:
            case 20:
            case 22:
            case 24:
            case 25:
            case 26:
            case 27:
            case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
            case 29:
            case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
            case 31:
            case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
            case 33:
                z = true;
                break;
            case 13:
            case 14:
            case 16:
            case 18:
            case 21:
            case 23:
            default:
                z = false;
                break;
        }
        Log.d("CtVolteServiceChecker", "isLteNetworkTypeEnabled, enabled=" + z + ", networkType=" + i2);
        return z;
    }

    private void startTimeOutCheck() {
        Log.d("CtVolteServiceChecker", "startTimeOutCheck...");
        this.mChecking = true;
        sendMessageDelayed(obtainMessage(100), 120000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopTimeOutCheck() {
        Log.d("CtVolteServiceChecker", "stopTimeOutCheck...");
        this.mChecking = false;
        removeMessages(100);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what != 100) {
            return;
        }
        Log.d("CtVolteServiceChecker", "handleMessage, checking=" + this.mChecking);
        if (this.mChecking && shouldShowVolteAlert()) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        Dialog dialog = this.mDialog;
        if (dialog != null && dialog.isShowing()) {
            Log.w("CtVolteServiceChecker", "showAlertDialog, dialog is showing, do nothing.");
            return;
        }
        Log.d("CtVolteServiceChecker", "showAlertDialog, subId=" + this.mSubId);
        SubscriptionInfo activeSubscriptionInfo = this.mSubscriptionManager.getActiveSubscriptionInfo(this.mSubId);
        if (activeSubscriptionInfo == null) {
            Log.w("CtVolteServiceChecker", "showAlertDialog, sub info is null, do nothing.");
            return;
        }
        String string = activeSubscriptionInfo.getDisplayName().toString();
        Context applicationContext = this.mContext.getApplicationContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(applicationContext, 5);
        builder.setMessage(applicationContext.getString(R.string.alert_volte_no_service, string));
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // from class: com.mediatek.settings.network.CtVolteServiceChecker.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("CtVolteServiceChecker", "onClick, ok, subId=" + CtVolteServiceChecker.this.mSubId);
                CtVolteServiceChecker.this.mImsManager.setEnhanced4gLteModeSetting(false);
                CtVolteServiceChecker.this.stopTimeOutCheck();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.mediatek.settings.network.CtVolteServiceChecker.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("CtVolteServiceChecker", "onClick, cancel, subId=" + CtVolteServiceChecker.this.mSubId);
                CtVolteServiceChecker ctVolteServiceChecker = CtVolteServiceChecker.this;
                ctVolteServiceChecker.sendMessageDelayed(ctVolteServiceChecker.obtainMessage(100), 120000L);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.mediatek.settings.network.CtVolteServiceChecker.6
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                Log.d("CtVolteServiceChecker", "onCancel, subId=" + CtVolteServiceChecker.this.mSubId);
                CtVolteServiceChecker ctVolteServiceChecker = CtVolteServiceChecker.this;
                ctVolteServiceChecker.sendMessageDelayed(ctVolteServiceChecker.obtainMessage(100), 120000L);
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.mediatek.settings.network.CtVolteServiceChecker.7
            @Override // android.content.DialogInterface.OnDismissListener
            public void onDismiss(DialogInterface dialogInterface) {
                Log.d("CtVolteServiceChecker", "onDismiss, subId=" + CtVolteServiceChecker.this.mSubId);
                CtVolteServiceChecker.this.mDialog = null;
            }
        });
        AlertDialog alertDialogCreate = builder.create();
        alertDialogCreate.getWindow().setType(2003);
        alertDialogCreate.show();
        this.mDialog = alertDialogCreate;
    }

    private boolean isLteNetwork() {
        Log.d("CtVolteServiceChecker", "isLteNetwork, dataNetworkType=" + this.mDataNetworkType + ", voiceNetworkType=" + this.mVoiceNetworkType);
        int i = this.mDataNetworkType;
        if (i == 0 && (i = this.mVoiceNetworkType) == 0) {
            i = 0;
        }
        boolean z = i == 13 || i == 19;
        Log.d("CtVolteServiceChecker", "isLteNetwork, isLte=" + z);
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isAirplaneModeOn(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) != 0;
    }

    public static boolean isCtVolteEnabled() {
        String str = SystemProperties.get("persist.vendor.mtk_ct_volte_support");
        boolean z = str.equals("1") || str.equals("2") || str.equals("3");
        Log.d("CtVolteServiceChecker", "isCtVolteEnabled, volteValue=" + str + ", enabled=" + z);
        return z;
    }

    public static boolean isCtAutoVolteEnabled() {
        String str = SystemProperties.get("persist.vendor.mtk_ct_volte_support");
        boolean z = str.equals("2") || str.equals("3");
        Log.d("CtVolteServiceChecker", "isCtAutoVolteEnabled, volteValue=" + str + ", enabled=" + z);
        return z;
    }

    public static boolean isCtSim(int i) {
        MtkIccCardConstants.CardType cdmaCardType = MtkTelephonyManagerEx.getDefault().getCdmaCardType(i);
        if (cdmaCardType == null) {
            return false;
        }
        int i2 = AnonymousClass8.$SwitchMap$com$mediatek$internal$telephony$MtkIccCardConstants$CardType[cdmaCardType.ordinal()];
        return i2 == 1 || i2 == 2 || i2 == 3 || i2 == 4;
    }

    /* JADX INFO: renamed from: com.mediatek.settings.network.CtVolteServiceChecker$8, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass8 {
        static final /* synthetic */ int[] $SwitchMap$com$mediatek$internal$telephony$MtkIccCardConstants$CardType;

        static {
            int[] iArr = new int[MtkIccCardConstants.CardType.values().length];
            $SwitchMap$com$mediatek$internal$telephony$MtkIccCardConstants$CardType = iArr;
            try {
                iArr[MtkIccCardConstants.CardType.CT_3G_UIM_CARD.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkIccCardConstants$CardType[MtkIccCardConstants.CardType.CT_UIM_SIM_CARD.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkIccCardConstants$CardType[MtkIccCardConstants.CardType.CT_4G_UICC_CARD.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$mediatek$internal$telephony$MtkIccCardConstants$CardType[MtkIccCardConstants.CardType.CT_EXCEL_GG_CARD.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public static boolean isCt4gSim(int i) {
        MtkIccCardConstants.CardType cdmaCardType = MtkTelephonyManagerEx.getDefault().getCdmaCardType(i);
        return cdmaCardType != null && AnonymousClass8.$SwitchMap$com$mediatek$internal$telephony$MtkIccCardConstants$CardType[cdmaCardType.ordinal()] == 3;
    }

    class MyPhoneStateListener extends PhoneStateListener {
        public MyPhoneStateListener(Looper looper) {
            super(looper);
        }

        @Override // android.telephony.PhoneStateListener
        public void onServiceStateChanged(ServiceState serviceState) {
            Log.d("CtVolteServiceChecker", "PhoneStateListener.onServiceStateChanged, serviceState=" + serviceState + ", phoneId=" + CtVolteServiceChecker.this.mPhoneId);
            boolean roaming = serviceState.getRoaming();
            int dataNetworkType = serviceState.getDataNetworkType();
            int voiceNetworkType = serviceState.getVoiceNetworkType();
            if (roaming == CtVolteServiceChecker.this.mRoaming && dataNetworkType == CtVolteServiceChecker.this.mDataNetworkType && voiceNetworkType == CtVolteServiceChecker.this.mVoiceNetworkType) {
                return;
            }
            CtVolteServiceChecker.this.mRoaming = roaming;
            CtVolteServiceChecker.this.mDataNetworkType = dataNetworkType;
            CtVolteServiceChecker.this.mVoiceNetworkType = voiceNetworkType;
            CtVolteServiceChecker.this.updateState();
        }

        public void onRadioPowerStateChanged(int i) {
            Log.d("CtVolteServiceChecker", "PhoneStateListener.onRadioPowerStateChanged, state=" + i + ", phoneId=" + CtVolteServiceChecker.this.mPhoneId);
            boolean z = i == 1;
            if (z != CtVolteServiceChecker.this.mRadioOn) {
                CtVolteServiceChecker.this.mRadioOn = z;
                CtVolteServiceChecker.this.updateState();
            }
        }

        public void register(int i) {
            ((PhoneStateListener) this).mSubId = Integer.valueOf(i);
            CtVolteServiceChecker ctVolteServiceChecker = CtVolteServiceChecker.this;
            ctVolteServiceChecker.mTelephonyManager = ctVolteServiceChecker.mTelephonyManager.createForSubscriptionId(i);
            CtVolteServiceChecker.this.mTelephonyManager.listen(this, 8388609);
        }

        public void unregister() {
            CtVolteServiceChecker.this.mTelephonyManager.listen(this, 0);
        }
    }

    class DataContentObserver extends ContentObserver {
        private Context mContext;
        private int mSubId;

        public DataContentObserver(Handler handler, Context context, int i) {
            super(handler);
            this.mContext = context;
            this.mSubId = i;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            CtVolteServiceChecker ctVolteServiceChecker = CtVolteServiceChecker.this;
            ctVolteServiceChecker.mLteEnabled = ctVolteServiceChecker.isLteNetworkTypeEnabled(this.mSubId);
            Log.d("CtVolteServiceChecker", "ContentObserver.onChange, selfChange=" + z + ", subId=" + this.mSubId + ", lteEnabled=" + CtVolteServiceChecker.this.mLteEnabled);
            CtVolteServiceChecker.this.updateState();
        }

        public void register() {
            this.mContext.getContentResolver().registerContentObserver(Settings.Global.getUriFor("preferred_network_mode" + this.mSubId), false, this);
        }

        public void unregister() {
            this.mContext.getContentResolver().unregisterContentObserver(this);
        }
    }
}
