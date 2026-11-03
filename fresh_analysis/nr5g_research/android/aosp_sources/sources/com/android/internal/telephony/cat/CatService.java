package com.android.internal.telephony.cat;

import android.R;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.backup.BackupManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperCatManager;
import com.android.internal.telephony.ProxyController;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.TelephonyStatAdapter;
import com.android.internal.telephony.ims.ImsResolver;
import com.android.internal.telephony.subscription.SubscriptionManagerService;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.IccRefreshResponse;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccProfile;
import com.android.internal.telephony.util.WorkerThread;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class CatService extends Handler implements AppInterface {
    protected static final int DEV_ID_DISPLAY = 2;
    protected static final int DEV_ID_KEYPAD = 1;
    protected static final int DEV_ID_NETWORK = 131;
    protected static final int DEV_ID_TERMINAL = 130;
    protected static final int DEV_ID_UICC = 129;
    protected static final int MSG_ID_ALPHA_NOTIFY = 9;
    public static final int MSG_ID_CALL_SETUP = 4;
    public static final int MSG_ID_EVENT_NOTIFY = 3;
    protected static final int MSG_ID_ICC_CHANGED = 8;
    public static final int MSG_ID_ICC_RECORDS_LOADED = 20;
    public static final int MSG_ID_ICC_REFRESH = 30;
    public static final int MSG_ID_PROACTIVE_COMMAND = 2;
    public static final int MSG_ID_REFRESH = 5;
    public static final int MSG_ID_RESPONSE = 6;
    public static final int MSG_ID_RIL_MSG_DECODED = 10;
    public static final int MSG_ID_SESSION_END = 1;
    public static final int MSG_ID_SIM_READY = 7;
    public static final String STK_DEFAULT = "Default Message";
    protected static IccRecords mIccRecords;
    protected static UiccCardApplication mUiccApplication;
    protected static CatService[] sInstance;
    protected static final Object sInstanceLock = new Object();
    protected IccCardStatus.CardState mCardState;
    protected CommandsInterface mCmdIf;
    protected Context mContext;
    protected CatCmdMessage mCurrntCmd;
    private final FeatureFlags mFeatureFlags;
    private IHyperCatManager mIHyperCatManager;
    protected boolean mIsProactiveCmdResponsed;
    protected CatCmdMessage mMenuCmd;
    protected RilMessageDecoder mMsgDecoder;
    private int mPhoneType;
    private SetUpCallCommandHandler mSetUpCallHandler;
    protected int mSlotId;
    public final BroadcastReceiver mSmsBroadcastReceiver;
    protected boolean mStkAppInstalled;
    private boolean mSupportSendUssd;
    private boolean mSupportSetUpCall;
    protected UiccController mUiccController;

    protected void SendTerminalResponseWhenLengthInCorrect(RilMessage rilMessage) {
    }

    protected void checkIfKeepOldCardState(IccCardStatus.CardState cardState) {
    }

    protected void handleEventNotifyCommandWithWrongData(RilMessage rilMessage) {
    }

    protected void handleExtendCmdResponse(CatResponseMessage catResponseMessage) {
    }

    protected boolean handleExtendMessage(Message message) {
        return false;
    }

    protected boolean sendTerminalResponseDirectlyInsomeCase(CommandParams commandParams) {
        return false;
    }

    public CatService(CommandsInterface commandsInterface, UiccCardApplication uiccCardApplication, IccRecords iccRecords, Context context, IccFileHandler iccFileHandler, UiccProfile uiccProfile, int i, Looper looper, FeatureFlags featureFlags) {
        super(looper);
        this.mCurrntCmd = null;
        this.mMenuCmd = null;
        this.mMsgDecoder = null;
        this.mStkAppInstalled = false;
        this.mSupportSendUssd = false;
        this.mSupportSetUpCall = false;
        this.mCardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
        this.mSetUpCallHandler = null;
        this.mPhoneType = 0;
        this.mIsProactiveCmdResponsed = false;
        this.mSmsBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.internal.telephony.cat.CatService.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                ResultCode resultCode;
                int i2;
                CommandDetails commandDetails = (CommandDetails) intent.getExtra("cmdDetails");
                if (intent.getAction().equals("com.android.internal.telephony.cat.SMS_SENT_ACTION")) {
                    int resultCode2 = getResultCode();
                    ResultCode resultCode3 = ResultCode.NETWORK_CRNTLY_UNABLE_TO_PROCESS;
                    CatLog.d(this, "STK SMS errorCode : " + resultCode2);
                    if (resultCode2 != -1) {
                        int iIntValue = 0;
                        if (intent.hasExtra("ims") && intent.getBooleanExtra("ims", false)) {
                            resultCode3 = ResultCode.SMS_RP_ERROR;
                            if (intent.hasExtra(TelephonyStatAdapter.ERROR_CODE_KEY)) {
                                int iIntValue2 = ((Integer) intent.getExtra(TelephonyStatAdapter.ERROR_CODE_KEY)).intValue();
                                if ((iIntValue2 & 128) == 0) {
                                    i2 = iIntValue2;
                                    resultCode = resultCode3;
                                }
                            }
                            CatLog.d(this, "Error delivering STK SMS errorCode : " + i2 + " terminalResponseResultCode = " + resultCode);
                            CatService.this.sendTerminalResponse(commandDetails, resultCode, true, i2, null);
                        } else if (intent.hasExtra(TelephonyStatAdapter.ERROR_CODE_KEY)) {
                            iIntValue = ((Integer) intent.getExtra(TelephonyStatAdapter.ERROR_CODE_KEY)).intValue() | 128;
                        }
                        resultCode = resultCode3;
                        i2 = iIntValue;
                        CatLog.d(this, "Error delivering STK SMS errorCode : " + i2 + " terminalResponseResultCode = " + resultCode);
                        CatService.this.sendTerminalResponse(commandDetails, resultCode, true, i2, null);
                    } else {
                        CatLog.d(this, " STK SMS sent successfully ");
                        FeatureFlags unused = CatService.this.mFeatureFlags;
                        CatService.this.sendTerminalResponse(commandDetails, ResultCode.OK, false, 0, null);
                    }
                }
                if (intent.getAction().equals("com.android.internal.telephony.cat.SMS_DELIVERY_ACTION")) {
                    int resultCode4 = getResultCode();
                    if (resultCode4 == -1) {
                        CatLog.d(this, " STK SMS delivered successfully ");
                        FeatureFlags unused2 = CatService.this.mFeatureFlags;
                    } else {
                        CatLog.d(this, "Error delivering STK SMS : " + resultCode4);
                        CatService.this.sendTerminalResponse(commandDetails, ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS, false, 0, null);
                    }
                }
            }
        };
        if (commandsInterface == null || uiccCardApplication == null || iccRecords == null || context == null || iccFileHandler == null || uiccProfile == null) {
            throw new NullPointerException("Service: Input parameters must not be null");
        }
        this.mCmdIf = commandsInterface;
        this.mContext = context;
        this.mSlotId = i;
        this.mFeatureFlags = featureFlags;
        try {
            this.mSupportSendUssd = context.getResources().getBoolean(R.bool.config_use_strict_phone_number_comparation);
        } catch (Resources.NotFoundException unused) {
            CatLog.e(this, "config_stk_send_ussd_by_telephony resource is not found");
        }
        try {
            this.mSupportSetUpCall = this.mContext.getResources().getBoolean(R.bool.config_use_strict_phone_number_comparation_for_kazakhstan);
        } catch (Resources.NotFoundException unused2) {
            CatLog.e(this, "config_stk_set_up_call_by_telephony resource is not found");
        }
        CatLog.d(this, "Support SEND USSD:" + this.mSupportSendUssd + " Support SET UP CALL:" + this.mSupportSetUpCall);
        RilMessageDecoder rilMessageDecoder = RilMessageDecoder.getInstance(this, iccFileHandler, context, i);
        this.mMsgDecoder = rilMessageDecoder;
        if (rilMessageDecoder == null) {
            CatLog.d(this, "Null RilMessageDecoder instance");
            return;
        }
        rilMessageDecoder.start();
        this.mCmdIf.setOnCatSessionEnd(this, 1, null);
        this.mCmdIf.setOnCatProactiveCmd(this, 2, null);
        this.mCmdIf.setOnCatEvent(this, 3, null);
        this.mCmdIf.setOnCatCallSetUp(this, 4, null);
        this.mCmdIf.registerForIccRefresh(this, 30, null);
        this.mCmdIf.setOnCatCcAlphaNotify(this, 9, null);
        mIccRecords = iccRecords;
        mUiccApplication = uiccCardApplication;
        iccRecords.registerForRecordsLoaded(this, 20, null);
        CatLog.d(this, "registerForRecordsLoaded slotid=" + this.mSlotId + " instance:" + this);
        UiccController uiccController = UiccController.getInstance();
        this.mUiccController = uiccController;
        uiccController.registerForIccChanged(this, 8, null);
        this.mStkAppInstalled = isStkAppInstalled();
        CatLog.d(this, "Running CAT service on Slotid: " + this.mSlotId + ". STK app installed:" + this.mStkAppInstalled);
        this.mContext.registerReceiver(this.mSmsBroadcastReceiver, new IntentFilter("com.android.internal.telephony.cat.SMS_DELIVERY_ACTION"));
        this.mContext.registerReceiver(this.mSmsBroadcastReceiver, new IntentFilter("com.android.internal.telephony.cat.SMS_SENT_ACTION"));
        this.mIHyperCatManager = (IHyperCatManager) HyperTelephonyFactory.getInstance().initModule(IHyperCatManager.DEFAULT, new Object[0]);
    }

    private static UiccCardApplication getValidUiccApplication(UiccProfile uiccProfile) {
        if (uiccProfile == null) {
            return null;
        }
        UiccCardApplication applicationIndex = uiccProfile.getApplicationIndex(0);
        if (applicationIndex.getType() == IccCardApplicationStatus.AppType.APPTYPE_UNKNOWN) {
            if (uiccProfile.getNumApplications() > 1) {
                for (int i = 1; i < uiccProfile.getNumApplications(); i++) {
                    UiccCardApplication applicationIndex2 = uiccProfile.getApplicationIndex(i);
                    if (applicationIndex2.getType() != IccCardApplicationStatus.AppType.APPTYPE_UNKNOWN) {
                        return applicationIndex2;
                    }
                }
            }
        }
        return applicationIndex;
    }

    public static CatService getInstance(CommandsInterface commandsInterface, Context context, UiccProfile uiccProfile, int i) {
        return getInstance(commandsInterface, context, uiccProfile, i, new FeatureFlagsImpl());
    }

    public static CatService getInstance(CommandsInterface commandsInterface, Context context, UiccProfile uiccProfile, int i, FeatureFlags featureFlags) {
        IccRecords iccRecords;
        IccFileHandler iccFileHandler;
        IccRecords iccRecords2;
        UiccCardApplication validUiccApplication = getValidUiccApplication(uiccProfile);
        if (validUiccApplication != null) {
            IccFileHandler iccFileHandler2 = validUiccApplication.getIccFileHandler();
            iccRecords = validUiccApplication.getIccRecords();
            iccFileHandler = iccFileHandler2;
        } else {
            iccRecords = null;
            iccFileHandler = null;
        }
        synchronized (sInstanceLock) {
            try {
                if (sInstance == null) {
                    int supportedModemCount = TelephonyManager.getDefault().getSupportedModemCount();
                    sInstance = new CatService[supportedModemCount];
                    for (int i2 = 0; i2 < supportedModemCount; i2++) {
                        sInstance[i2] = null;
                    }
                }
                CatService[] catServiceArr = sInstance;
                CatService catService = catServiceArr[i];
                if (catService == null) {
                    if (commandsInterface != null && validUiccApplication != null && iccRecords != null && context != null && iccFileHandler != null && uiccProfile != null) {
                        catServiceArr[i] = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeCatService(commandsInterface, validUiccApplication, iccRecords, context, iccFileHandler, uiccProfile, i, WorkerThread.get().getLooper(), featureFlags);
                    }
                    return null;
                }
                if (iccRecords != null && (iccRecords2 = mIccRecords) != iccRecords) {
                    if (iccRecords2 != null) {
                        iccRecords2.unregisterForRecordsLoaded(catService);
                    }
                    mIccRecords = iccRecords;
                    mUiccApplication = validUiccApplication;
                    iccRecords.registerForRecordsLoaded(sInstance[i], 20, null);
                    CatLog.d(sInstance[i], "registerForRecordsLoaded slotid=" + i + " instance:" + sInstance[i]);
                }
                return sInstance[i];
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    @Override // com.android.internal.telephony.cat.AppInterface
    public void dispose() {
        synchronized (sInstanceLock) {
            try {
                CatLog.d(this, "Disposing CatService object");
                if (!this.mIsProactiveCmdResponsed && this.mCurrntCmd != null) {
                    CatLog.d(this, "Send TR for the last pending commands.");
                    sendTerminalResponseByCurrentCmd(this.mCurrntCmd);
                }
                this.mContext.unregisterReceiver(this.mSmsBroadcastReceiver);
                mIccRecords.unregisterForRecordsLoaded(this);
                try {
                    this.mContext.unregisterReceiver(this.mSmsBroadcastReceiver);
                } catch (IllegalArgumentException e) {
                    CatLog.e(this, "mSmsBroadcastReceiver: was not registered" + e);
                }
                broadcastCardStateAndIccRefreshResp(IccCardStatus.CardState.CARDSTATE_ABSENT, null);
                this.mCmdIf.unSetOnCatSessionEnd(this);
                this.mCmdIf.unSetOnCatProactiveCmd(this);
                this.mCmdIf.unSetOnCatEvent(this);
                this.mCmdIf.unSetOnCatCallSetUp(this);
                this.mCmdIf.unSetOnCatCcAlphaNotify(this);
                this.mCmdIf.unregisterForIccRefresh(this);
                UiccController uiccController = this.mUiccController;
                if (uiccController != null) {
                    uiccController.unregisterForIccChanged(this);
                    this.mUiccController = null;
                }
                RilMessageDecoder rilMessageDecoder = this.mMsgDecoder;
                if (rilMessageDecoder != null) {
                    rilMessageDecoder.dispose();
                    this.mMsgDecoder = null;
                }
                removeCallbacksAndMessages(null);
                CatService[] catServiceArr = sInstance;
                if (catServiceArr != null) {
                    int i = this.mSlotId;
                    if (i >= 0 && i < catServiceArr.length) {
                        catServiceArr[i] = null;
                    } else {
                        CatLog.d(this, "error: invaild slot id: " + this.mSlotId);
                    }
                }
                this.mIHyperCatManager.stopReceivingLocationUpdates();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected void finalize() {
        CatLog.d(this, "Service finalized");
    }

    protected void handleRilMsg(RilMessage rilMessage) {
        CommandParams commandParams;
        CommandParams commandParams2;
        if (rilMessage == null) {
            return;
        }
        int i = rilMessage.mId;
        if (i == 1) {
            handleSessionEnd();
            return;
        }
        if (i != 2) {
            if (i != 3) {
                if (i == 5 && (commandParams2 = (CommandParams) rilMessage.mData) != null) {
                    handleCommand(commandParams2, false);
                    return;
                }
                return;
            }
            if (rilMessage.mResCode == ResultCode.OK && (commandParams = (CommandParams) rilMessage.mData) != null) {
                handleCommand(commandParams, false);
            }
            handleEventNotifyCommandWithWrongData(rilMessage);
            return;
        }
        this.mIsProactiveCmdResponsed = false;
        try {
            CommandParams commandParams3 = (CommandParams) rilMessage.mData;
            if (commandParams3 != null) {
                ResultCode resultCode = rilMessage.mResCode;
                if (resultCode == ResultCode.OK || resultCode == ResultCode.PRFRMD_ICON_NOT_DISPLAYED) {
                    handleCommand(commandParams3, true);
                } else {
                    sendTerminalResponse(commandParams3.mCmdDet, resultCode, false, 0, null);
                }
            }
        } catch (ClassCastException unused) {
            CatLog.d(this, "Fail to parse proactive command");
            CatCmdMessage catCmdMessage = this.mCurrntCmd;
            if (catCmdMessage != null) {
                sendTerminalResponse(catCmdMessage.mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
            }
            SendTerminalResponseWhenLengthInCorrect(rilMessage);
        }
    }

    protected boolean isSupportedSetupEventCommand(CatCmdMessage catCmdMessage) {
        boolean z = true;
        for (int i : catCmdMessage.getSetEventList().eventList) {
            CatLog.d(this, "Event: " + i);
            if (i != 4 && i != 5 && i != 7 && i != 23) {
                z = false;
            }
        }
        return z;
    }

    protected void handleCommand(CommandParams commandParams, boolean z) {
        boolean z2;
        CatLog.d(this, commandParams.getCommandType().name());
        if (z && this.mUiccController != null) {
            UiccController.addLocalLog("CatService[" + this.mSlotId + "]: ProactiveCommand  cmdParams=" + commandParams);
        }
        CatCmdMessage catCmdMessage = new CatCmdMessage(commandParams);
        switch (AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[commandParams.getCommandType().ordinal()]) {
            case 1:
                if (removeMenu(catCmdMessage.getMenu())) {
                    this.mMenuCmd = null;
                } else {
                    this.mMenuCmd = catCmdMessage;
                }
                sendTerminalResponse(commandParams.mCmdDet, commandParams.mLoadIconFailed ? ResultCode.PRFRMD_ICON_NOT_DISPLAYED : ResultCode.OK, false, 0, null);
                break;
            case 2:
                if (sendTerminalResponseDirectlyInsomeCase(commandParams)) {
                    return;
                }
                break;
            case 3:
                sendTerminalResponse(commandParams.mCmdDet, commandParams.mLoadIconFailed ? ResultCode.PRFRMD_ICON_NOT_DISPLAYED : ResultCode.OK, false, 0, null);
                break;
            case 4:
                sendTerminalResponseDirectlyInsomeCase(commandParams);
                boolean zIsSupportedSetupEventCommand = isSupportedSetupEventCommand(catCmdMessage);
                CommandDetails commandDetails = commandParams.mCmdDet;
                if (zIsSupportedSetupEventCommand) {
                    sendTerminalResponse(commandDetails, ResultCode.OK, false, 0, null);
                } else {
                    sendTerminalResponse(commandDetails, ResultCode.BEYOND_TERMINAL_CAPABILITY, false, 0, null);
                }
                broadcastSetupEventList(catCmdMessage);
                break;
            case 5:
                int i = commandParams.mCmdDet.commandQualifier;
                if (i == 3) {
                    sendTerminalResponse(commandParams.mCmdDet, ResultCode.OK, false, 0, new DTTZResponseData(null));
                    return;
                } else if (i == 4) {
                    sendTerminalResponse(commandParams.mCmdDet, ResultCode.OK, false, 0, new LanguageResponseData(Locale.getDefault().getLanguage()));
                    return;
                } else {
                    if (sendTerminalResponseDirectlyInsomeCase(commandParams)) {
                        return;
                    }
                    sendTerminalResponse(commandParams.mCmdDet, ResultCode.OK, false, 0, null);
                    return;
                }
            case 6:
                LaunchBrowserParams launchBrowserParams = (LaunchBrowserParams) commandParams;
                String str = launchBrowserParams.mConfirmMsg.text;
                if (str != null && str.equals(STK_DEFAULT)) {
                    launchBrowserParams.mConfirmMsg.text = this.mContext.getText(R.string.mmcc_illegal_ms).toString();
                }
                break;
            case 7:
                if (sendTerminalResponseDirectlyInsomeCase(commandParams)) {
                    return;
                }
                break;
            case 8:
            case 9:
            case 10:
            case 23:
                break;
            case 11:
                DisplayTextParams displayTextParams = (DisplayTextParams) commandParams;
                if (STK_DEFAULT.equals(displayTextParams.mTextMsg.text)) {
                    displayTextParams.mTextMsg.text = null;
                }
                this.mIsProactiveCmdResponsed = true;
                break;
            case 12:
                if (commandParams instanceof SendSMSParams) {
                    sendStkSms((SendSMSParams) commandParams);
                    return;
                }
                DisplayTextParams displayTextParams2 = (DisplayTextParams) commandParams;
                String str2 = displayTextParams2.mTextMsg.text;
                if (str2 != null && str2.equals(STK_DEFAULT)) {
                    displayTextParams2.mTextMsg.text = this.mContext.getText(R.string.zen_mode_until_next_day).toString();
                }
                this.mIsProactiveCmdResponsed = true;
                break;
                break;
            case 13:
            case 14:
                DisplayTextParams displayTextParams3 = (DisplayTextParams) commandParams;
                String str3 = displayTextParams3.mTextMsg.text;
                if (str3 != null && str3.equals(STK_DEFAULT)) {
                    displayTextParams3.mTextMsg.text = this.mContext.getText(R.string.zen_mode_until_next_day).toString();
                }
                break;
            case 15:
                this.mIsProactiveCmdResponsed = true;
                DisplayTextParams displayTextParams4 = (DisplayTextParams) commandParams;
                String str4 = displayTextParams4.mTextMsg.text;
                if (str4 != null && str4.equals(STK_DEFAULT)) {
                    displayTextParams4.mTextMsg.text = this.mContext.getText(R.string.zen_mode_until_next_day).toString();
                }
                break;
            case 16:
                this.mIsProactiveCmdResponsed = true;
                break;
            case 17:
                CallSetupParams callSetupParams = (CallSetupParams) commandParams;
                String str5 = callSetupParams.mConfirmMsg.text;
                if (str5 != null && str5.equals(STK_DEFAULT)) {
                    callSetupParams.mConfirmMsg.text = this.mContext.getText(R.string.accessibility_dialog_button_allow).toString();
                }
                break;
            case 18:
                String str6 = ((LanguageParams) commandParams).mLanguage;
                ResultCode resultCode = ResultCode.OK;
                if (str6 != null && str6.length() > 0) {
                    try {
                        changeLanguage(str6);
                    } catch (RemoteException unused) {
                        resultCode = ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS;
                    }
                    break;
                }
                sendTerminalResponse(commandParams.mCmdDet, resultCode, false, 0, null);
                return;
            case 19:
            case 20:
            case 21:
            case 22:
                BIPClientParams bIPClientParams = (BIPClientParams) commandParams;
                try {
                    z2 = this.mContext.getResources().getBoolean(R.bool.config_use_sim_language_file);
                } catch (Resources.NotFoundException unused2) {
                    z2 = false;
                }
                if (bIPClientParams.mTextMsg.text == null && (bIPClientParams.mHasAlphaId || z2)) {
                    CatLog.d(this, "cmd " + commandParams.getCommandType() + " with null alpha id");
                    if (z) {
                        sendTerminalResponse(commandParams.mCmdDet, ResultCode.OK, false, 0, null);
                        return;
                    } else {
                        if (commandParams.getCommandType() == AppInterface.CommandType.OPEN_CHANNEL) {
                            sendTerminalResponseBySetUpCall(true, ResultCode.OK.value());
                            return;
                        }
                        return;
                    }
                }
                if (!this.mStkAppInstalled) {
                    CatLog.d(this, "No STK application found.");
                    if (z) {
                        sendTerminalResponse(commandParams.mCmdDet, ResultCode.BEYOND_TERMINAL_CAPABILITY, false, 0, null);
                        return;
                    }
                }
                if (z && (commandParams.getCommandType() == AppInterface.CommandType.CLOSE_CHANNEL || commandParams.getCommandType() == AppInterface.CommandType.RECEIVE_DATA || commandParams.getCommandType() == AppInterface.CommandType.SEND_DATA)) {
                    sendTerminalResponse(commandParams.mCmdDet, ResultCode.OK, false, 0, null);
                }
                break;
                break;
            default:
                CatLog.d(this, "Unsupported command");
                return;
        }
        this.mCurrntCmd = catCmdMessage;
        broadcastCatCmdIntent(catCmdMessage);
    }

    public void sendStkSms(String str, String str2, int i, CommandParams commandParams, ProxyController proxyController) {
        proxyController.getSmsController().sendTextForSubscriber(i, this.mContext.getOpPackageName(), this.mContext.getAttributionTag(), str2, null, str, PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.internal.telephony.cat.SMS_SENT_ACTION").putExtra("cmdDetails", commandParams.mCmdDet).setPackage(this.mContext.getPackageName()), 33554432), PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.internal.telephony.cat.SMS_DELIVERY_ACTION").putExtra("cmdDetails", commandParams.mCmdDet).setPackage(this.mContext.getPackageName()), 33554432), false, 0L, true, true);
    }

    public void sendStkSms(SendSMSParams sendSMSParams) {
        TextMessage textMessage = sendSMSParams.mDestAddress;
        String str = textMessage != null ? textMessage.text : null;
        if (sendSMSParams.mRawTpdu == null || str == null) {
            sendTerminalResponse(sendSMSParams.mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
            return;
        }
        SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = ((SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service")).getActiveSubscriptionInfoForSimSlotIndex(this.mSlotId);
        if (activeSubscriptionInfoForSimSlotIndex == null) {
            sendTerminalResponse(sendSMSParams.mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
            CatLog.d(this, "Subscription info is null");
        } else {
            ProxyController.getInstance(this.mContext, this.mFeatureFlags).getSmsController().sendRawPduForSubscriber(activeSubscriptionInfoForSimSlotIndex.getSubscriptionId(), this.mContext.getOpPackageName(), str, sendSMSParams.mSmscAddress, sendSMSParams.mRawTpdu, PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.internal.telephony.cat.SMS_SENT_ACTION").putExtra("cmdDetails", sendSMSParams.mCmdDet).setPackage(this.mContext.getPackageName()), 33554432), PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.internal.telephony.cat.SMS_DELIVERY_ACTION").putExtra("cmdDetails", sendSMSParams.mCmdDet).setPackage(this.mContext.getPackageName()), 33554432));
        }
    }

    private void broadcastCatCmdIntent(CatCmdMessage catCmdMessage) {
        Intent intent = new Intent(AppInterface.CAT_CMD_ACTION);
        intent.putExtra("STK CMD", catCmdMessage);
        intent.putExtra("SLOT_ID", this.mSlotId);
        intent.setComponent(AppInterface.getDefaultSTKApplication());
        CatLog.d(this, "Sending CmdMsg: " + catCmdMessage + " on slotid:" + this.mSlotId);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, AppInterface.STK_PERMISSION);
    }

    private void broadcastSetupEventList(CatCmdMessage catCmdMessage) {
        String setupEventListReceiver = getSetupEventListReceiver();
        if (setupEventListReceiver == null) {
            return;
        }
        Intent intent = new Intent("android.telephony.action.STK_SETUP_EVENT_LIST");
        intent.putExtra("android.telephony.extra.SETUP_EVENT_LIST", catCmdMessage.getSetEventList().eventList);
        intent.putExtra("android.telephony.extra.SUBSCRIPTION_ID", SubscriptionManager.getSubscriptionId(this.mSlotId));
        intent.setPackage(setupEventListReceiver);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, AppInterface.STK_PERMISSION);
    }

    public String getSetupEventListReceiver() {
        ImsResolver imsResolver = ImsResolver.getInstance();
        if (imsResolver == null) {
            CatLog.d(this, "getImsRcsPackage: Device does not support IMS - skipping");
            return null;
        }
        try {
            return imsResolver.getConfiguredImsServicePackageName(this.mSlotId, 1);
        } catch (Exception e) {
            CatLog.e(this, "getSetupEventListReceiver: " + e);
            return null;
        }
    }

    protected void handleSessionEnd() {
        CatLog.d(this, "SESSION END on " + this.mSlotId);
        this.mCurrntCmd = this.mMenuCmd;
        Intent intent = new Intent(AppInterface.CAT_SESSION_END_ACTION);
        intent.putExtra("SLOT_ID", this.mSlotId);
        intent.setComponent(AppInterface.getDefaultSTKApplication());
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, AppInterface.STK_PERMISSION);
    }

    protected void sendTerminalResponse(CommandDetails commandDetails, ResultCode resultCode, boolean z, int i, ResponseData responseData) {
        if (commandDetails == null) {
            return;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CatCmdMessage catCmdMessage = this.mCurrntCmd;
        Input inputGeInput = catCmdMessage != null ? catCmdMessage.geInput() : null;
        int iValue = ComprehensionTlvTag.COMMAND_DETAILS.value();
        if (commandDetails.compRequired) {
            iValue |= 128;
        }
        byteArrayOutputStream.write(iValue);
        byteArrayOutputStream.write(3);
        byteArrayOutputStream.write(commandDetails.commandNumber);
        byteArrayOutputStream.write(commandDetails.typeOfCommand);
        byteArrayOutputStream.write(commandDetails.commandQualifier);
        byteArrayOutputStream.write(ComprehensionTlvTag.DEVICE_IDENTITIES.value());
        byteArrayOutputStream.write(2);
        byteArrayOutputStream.write(130);
        byteArrayOutputStream.write(129);
        int iValue2 = ComprehensionTlvTag.RESULT.value();
        if (commandDetails.compRequired) {
            iValue2 |= 128;
        }
        byteArrayOutputStream.write(iValue2);
        byteArrayOutputStream.write(z ? 2 : 1);
        byteArrayOutputStream.write(resultCode.value());
        if (z) {
            byteArrayOutputStream.write(i);
        }
        if (responseData != null) {
            responseData.format(byteArrayOutputStream);
        } else {
            encodeOptionalTags(commandDetails, resultCode, inputGeInput, byteArrayOutputStream);
        }
        this.mCmdIf.sendTerminalResponse(IccUtils.bytesToHexString(byteArrayOutputStream.toByteArray()), null);
        this.mIsProactiveCmdResponsed = true;
    }

    protected void encodeOptionalTags(CommandDetails commandDetails, ResultCode resultCode, Input input, ByteArrayOutputStream byteArrayOutputStream) {
        AppInterface.CommandType commandTypeFromInt = AppInterface.CommandType.fromInt(commandDetails.typeOfCommand);
        if (commandTypeFromInt != null) {
            int i = AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[commandTypeFromInt.ordinal()];
            if (i == 5) {
                if (commandDetails.commandQualifier == 4 && resultCode.value() == ResultCode.OK.value()) {
                    getPliResponse(byteArrayOutputStream);
                    return;
                }
                return;
            }
            if (i == 8 || i == 9) {
                if (resultCode.value() != ResultCode.NO_RESPONSE_FROM_USER.value() || input == null || input.duration == null) {
                    return;
                }
                getInKeyResponse(byteArrayOutputStream, input);
                return;
            }
            CatLog.d(this, "encodeOptionalTags() Unsupported Cmd details=" + commandDetails);
            return;
        }
        CatLog.d(this, "encodeOptionalTags() bad Cmd details=" + commandDetails);
    }

    protected void getInKeyResponse(ByteArrayOutputStream byteArrayOutputStream, Input input) {
        byteArrayOutputStream.write(ComprehensionTlvTag.DURATION.value());
        byteArrayOutputStream.write(2);
        Duration.TimeUnit timeUnit = input.duration.timeUnit;
        byteArrayOutputStream.write(Duration.TimeUnit.SECOND.value());
        byteArrayOutputStream.write(input.duration.timeInterval);
    }

    protected void getPliResponse(ByteArrayOutputStream byteArrayOutputStream) {
        String language = Locale.getDefault().getLanguage();
        if (language != null) {
            byteArrayOutputStream.write(ComprehensionTlvTag.LANGUAGE.value());
            ResponseData.writeLength(byteArrayOutputStream, language.length());
            byteArrayOutputStream.write(language.getBytes(), 0, language.length());
        }
    }

    protected void sendMenuSelection(int i, boolean z) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(211);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(ComprehensionTlvTag.DEVICE_IDENTITIES.value() | 128);
        byteArrayOutputStream.write(2);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(129);
        byteArrayOutputStream.write(ComprehensionTlvTag.ITEM_ID.value() | 128);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(i);
        if (z) {
            byteArrayOutputStream.write(ComprehensionTlvTag.HELP_REQUEST.value());
            byteArrayOutputStream.write(0);
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArray[1] = (byte) (byteArray.length - 2);
        this.mCmdIf.sendEnvelope(IccUtils.bytesToHexString(byteArray), null);
    }

    protected void eventDownload(int i, int i2, int i3, byte[] bArr, boolean z) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(214);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(ComprehensionTlvTag.EVENT_LIST.value() | 128);
        byteArrayOutputStream.write(1);
        byteArrayOutputStream.write(i);
        byteArrayOutputStream.write(ComprehensionTlvTag.DEVICE_IDENTITIES.value() | 128);
        byteArrayOutputStream.write(2);
        byteArrayOutputStream.write(i2);
        byteArrayOutputStream.write(i3);
        if (i == 5) {
            CatLog.d(sInstance, " Sending Idle Screen Available event download to ICC");
        } else if (i == 7) {
            CatLog.d(sInstance, " Sending Language Selection event download to ICC");
            byteArrayOutputStream.write(ComprehensionTlvTag.LANGUAGE.value() | 128);
            byteArrayOutputStream.write(2);
        }
        if (bArr != null) {
            for (byte b : bArr) {
                byteArrayOutputStream.write(b);
            }
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArray[1] = (byte) (byteArray.length - 2);
        String strBytesToHexString = IccUtils.bytesToHexString(byteArray);
        CatLog.d(this, "ENVELOPE COMMAND: " + strBytesToHexString);
        this.mCmdIf.sendEnvelope(strBytesToHexString, null);
    }

    public static AppInterface getInstance() {
        return getInstance(null, null, null, SubscriptionManagerService.getInstance() != null ? SubscriptionManagerService.getInstance().getSlotIndex(SubscriptionManagerService.getInstance().getDefaultSubId()) : 0);
    }

    public static AppInterface getInstance(int i) {
        return getInstance(null, null, null, i);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Object obj;
        AsyncResult asyncResult;
        Object obj2;
        Object obj3;
        CatLog.d(this, "handleMessage[" + message.what + "]");
        int i = message.what;
        if (i != 20) {
            if (i != 30) {
                String str = null;
                switch (i) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                        CatLog.d(this, "ril message arrived,slotid:" + this.mSlotId);
                        Object obj4 = message.obj;
                        if (obj4 != null && (asyncResult = (AsyncResult) obj4) != null && (obj2 = asyncResult.result) != null) {
                            try {
                                str = (String) obj2;
                            } catch (ClassCastException unused) {
                                return;
                            }
                            break;
                        }
                        if (handleExtendMessage(message)) {
                            return;
                        }
                        RilMessageDecoder rilMessageDecoder = this.mMsgDecoder;
                        int i2 = message.what;
                        if (rilMessageDecoder != null) {
                            rilMessageDecoder.sendStartDecodingMessageParams(new RilMessage(i2, str));
                            return;
                        }
                        CatLog.e(this, "Error in handleMessage (" + i2 + ") mMsgDecoder is NULL");
                        return;
                    case 4:
                        this.mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(i, null));
                        return;
                    case 6:
                        handleCmdResponse((CatResponseMessage) message.obj);
                        return;
                    default:
                        switch (i) {
                            case 8:
                                CatLog.d(this, "MSG_ID_ICC_CHANGED");
                                updateIccAvailability();
                                return;
                            case 9:
                                CatLog.d(this, "Received CAT CC Alpha message from card");
                                Object obj5 = message.obj;
                                if (obj5 != null) {
                                    AsyncResult asyncResult2 = (AsyncResult) obj5;
                                    if (asyncResult2 != null && (obj3 = asyncResult2.result) != null) {
                                        broadcastAlphaMessage((String) obj3);
                                        return;
                                    } else {
                                        CatLog.d(this, "CAT Alpha message: ar.result is null");
                                        return;
                                    }
                                }
                                CatLog.d(this, "CAT Alpha message: msg.obj is null");
                                return;
                            case 10:
                                handleRilMsg((RilMessage) message.obj);
                                return;
                            case 11:
                                ResultCode resultCode = (ResultCode) message.obj;
                                CatLog.d(this, "Command handling result: " + resultCode);
                                CatCmdMessage catCmdMessage = this.mCurrntCmd;
                                if (catCmdMessage == null) {
                                    CatLog.e(this, "No command to handle");
                                    return;
                                }
                                sendTerminalResponse(catCmdMessage.mCmdDet, resultCode, false, 0, null);
                                if (this.mCurrntCmd.mCmdDet.typeOfCommand == AppInterface.CommandType.SET_UP_CALL.value()) {
                                    this.mSetUpCallHandler = null;
                                }
                                this.mCurrntCmd = null;
                                return;
                            default:
                                if (handleExtendMessage(message)) {
                                    return;
                                }
                                throw new AssertionError("Unrecognized CAT command: " + message.what);
                        }
                }
            }
            Object obj6 = message.obj;
            if (obj6 != null) {
                AsyncResult asyncResult3 = (AsyncResult) obj6;
                if (asyncResult3 != null && (obj = asyncResult3.result) != null) {
                    broadcastCardStateAndIccRefreshResp(IccCardStatus.CardState.CARDSTATE_PRESENT, (IccRefreshResponse) obj);
                    return;
                }
                CatLog.d(this, "Icc REFRESH with exception: " + asyncResult3.exception);
                return;
            }
            CatLog.d(this, "IccRefresh Message is null");
        }
    }

    protected void broadcastCardStateAndIccRefreshResp(IccCardStatus.CardState cardState, IccRefreshResponse iccRefreshResponse) {
        Intent intent = new Intent(AppInterface.CAT_ICC_STATUS_CHANGE);
        boolean z = cardState == IccCardStatus.CardState.CARDSTATE_PRESENT;
        if (iccRefreshResponse != null) {
            intent.putExtra(AppInterface.REFRESH_RESULT, iccRefreshResponse.refreshResult);
            CatLog.d(this, "Sending IccResult with Result: " + iccRefreshResponse.refreshResult);
        }
        intent.putExtra(AppInterface.CARD_STATUS, z);
        intent.setComponent(AppInterface.getDefaultSTKApplication());
        intent.putExtra("SLOT_ID", this.mSlotId);
        CatLog.d(this, "Sending Card Status: " + cardState + " cardPresent: " + z + "SLOT_ID: " + this.mSlotId);
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, AppInterface.STK_PERMISSION);
    }

    protected void broadcastAlphaMessage(String str) {
        CatLog.d(this, "Broadcasting CAT Alpha message from card: " + str);
        Intent intent = new Intent(AppInterface.CAT_ALPHA_NOTIFY_ACTION);
        intent.addFlags(268435456);
        intent.putExtra(AppInterface.ALPHA_STRING, str);
        intent.putExtra("SLOT_ID", this.mSlotId);
        intent.setComponent(AppInterface.getDefaultSTKApplication());
        this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL, AppInterface.STK_PERMISSION);
    }

    @Override // com.android.internal.telephony.cat.AppInterface
    public synchronized void onCmdResponse(CatResponseMessage catResponseMessage) {
        if (catResponseMessage == null) {
            return;
        }
        obtainMessage(6, catResponseMessage).sendToTarget();
    }

    protected boolean validateResponse(CatResponseMessage catResponseMessage) {
        if (catResponseMessage.mCmdDet.typeOfCommand == AppInterface.CommandType.SET_UP_EVENT_LIST.value() || catResponseMessage.mCmdDet.typeOfCommand == AppInterface.CommandType.SET_UP_MENU.value()) {
            CatLog.d(this, "CmdType: " + catResponseMessage.mCmdDet.typeOfCommand);
            return true;
        }
        CatCmdMessage catCmdMessage = this.mCurrntCmd;
        if (catCmdMessage == null) {
            return false;
        }
        boolean zCompareTo = catResponseMessage.mCmdDet.compareTo(catCmdMessage.mCmdDet);
        CatLog.d(this, "isResponse for last valid cmd: " + zCompareTo);
        return zCompareTo;
    }

    protected boolean removeMenu(Menu menu) {
        try {
            return menu.items.size() == 1 && menu.items.get(0) == null;
        } catch (NullPointerException unused) {
            CatLog.d(this, "Unable to get Menu's items size");
            return true;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:17:0x0042  */
    /* JADX WARN: Code duplicated, block: B:30:0x0069  */
    /* JADX WARN: Code duplicated, block: B:32:0x006c  */
    /* JADX WARN: Code duplicated, block: B:34:0x006f  */
    /* JADX WARN: Code duplicated, block: B:36:0x0073  */
    /* JADX WARN: Code duplicated, block: B:38:0x0077  */
    /* JADX WARN: Code duplicated, block: B:40:0x007b  */
    /* JADX WARN: Code duplicated, block: B:42:0x0085 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:43:0x0087  */
    /* JADX WARN: Code duplicated, block: B:45:0x0095  */
    /* JADX WARN: Code duplicated, block: B:46:0x009d  */
    /* JADX WARN: Code duplicated, block: B:47:0x00a5  */
    /* JADX WARN: Code duplicated, block: B:49:0x00ab  */
    /* JADX WARN: Code duplicated, block: B:50:0x00af  */
    /* JADX WARN: Code duplicated, block: B:51:0x00b4  */
    /* JADX WARN: Code duplicated, block: B:53:0x00be  */
    /* JADX WARN: Code duplicated, block: B:55:0x00cc  */
    /* JADX WARN: Code duplicated, block: B:57:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:59:0x00dc  */
    /* JADX WARN: Code duplicated, block: B:61:0x00e6  */
    /* JADX WARN: Code duplicated, block: B:63:0x00ed  */
    /* JADX WARN: Code duplicated, block: B:64:0x00f2  */
    /* JADX WARN: Code duplicated, block: B:67:0x0104  */
    /* JADX WARN: Code duplicated, block: B:70:0x010c  */
    /* JADX WARN: Failed to find 'out' block for switch in B:38:0x0077. Please report as an issue. */
    private void handleCmdResponse(CatResponseMessage catResponseMessage) {
        boolean z;
        int i;
        CatService catService;
        int i2;
        byte[] bArr;
        ResponseData selectItemResponseData;
        ResponseData responseData;
        Input inputGeInput;
        if (validateResponse(catResponseMessage)) {
            CommandDetails cmdDetails = catResponseMessage.getCmdDetails();
            AppInterface.CommandType commandTypeFromInt = AppInterface.CommandType.fromInt(cmdDetails.typeOfCommand);
            switch (AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$ResultCode[catResponseMessage.mResCode.ordinal()]) {
                case 1:
                    z = true;
                    i = AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[commandTypeFromInt.ordinal()];
                    if (i != 1) {
                        sendMenuSelection(catResponseMessage.mUsersMenuSelection, catResponseMessage.mResCode == ResultCode.HELP_INFO_REQUIRED);
                    } else {
                        if (i != 2) {
                            if (i != 4) {
                                if (i != 17) {
                                    if (i != 19) {
                                        switch (i) {
                                            case 6:
                                                if (catResponseMessage.mResCode == ResultCode.LAUNCH_BROWSER_ERROR) {
                                                    catResponseMessage.setAdditionalInfo(4);
                                                } else {
                                                    catResponseMessage.mIncludeAdditionalInfo = false;
                                                    catResponseMessage.mAdditionalInfo = 0;
                                                }
                                                catService = this;
                                                responseData = null;
                                                break;
                                            case 7:
                                                selectItemResponseData = new SelectItemResponseData(catResponseMessage.mUsersMenuSelection);
                                                responseData = selectItemResponseData;
                                                catService = this;
                                                break;
                                            case 8:
                                            case 9:
                                                inputGeInput = this.mCurrntCmd.geInput();
                                                if (!inputGeInput.yesNo) {
                                                    if (!z) {
                                                        selectItemResponseData = new GetInkeyInputResponseData(catResponseMessage.mUsersInput, inputGeInput.ucs2, inputGeInput.packed);
                                                    }
                                                    catService = this;
                                                    responseData = null;
                                                } else {
                                                    selectItemResponseData = new GetInkeyInputResponseData(catResponseMessage.mUsersYesNoSelection);
                                                }
                                                responseData = selectItemResponseData;
                                                catService = this;
                                                break;
                                            default:
                                                catService = this;
                                                responseData = null;
                                                break;
                                        }
                                    } else {
                                        this.mCmdIf.handleCallSetupRequestFromSim(catResponseMessage.mUsersConfirm, null);
                                        this.mCurrntCmd = null;
                                        break;
                                    }
                                } else {
                                    sendTerminalResponseBySetUpCall(catResponseMessage.mUsersConfirm, catResponseMessage.mResCode.value());
                                    this.mCurrntCmd = null;
                                    break;
                                }
                            } else {
                                i2 = catResponseMessage.mEventValue;
                                bArr = catResponseMessage.mAddedInfo;
                                if (5 == i2) {
                                    eventDownload(i2, 2, 129, bArr, false);
                                } else {
                                    eventDownload(i2, 130, 129, bArr, false);
                                }
                                break;
                            }
                        } else {
                            catService = this;
                            if (catResponseMessage.mResCode == ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS) {
                                catResponseMessage.setAdditionalInfo(1);
                            } else {
                                catResponseMessage.mIncludeAdditionalInfo = false;
                                catResponseMessage.mAdditionalInfo = 0;
                            }
                            responseData = null;
                        }
                        catService.sendTerminalResponse(cmdDetails, catResponseMessage.mResCode, catResponseMessage.mIncludeAdditionalInfo, catResponseMessage.mAdditionalInfo, responseData);
                        catService.mCurrntCmd = null;
                    }
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                    z = false;
                    i = AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[commandTypeFromInt.ordinal()];
                    if (i != 1) {
                        sendMenuSelection(catResponseMessage.mUsersMenuSelection, catResponseMessage.mResCode == ResultCode.HELP_INFO_REQUIRED);
                    } else {
                        if (i != 2) {
                            if (i != 4) {
                                if (i != 17) {
                                    if (i != 19) {
                                        switch (i) {
                                            case 6:
                                                if (catResponseMessage.mResCode == ResultCode.LAUNCH_BROWSER_ERROR) {
                                                    catResponseMessage.setAdditionalInfo(4);
                                                } else {
                                                    catResponseMessage.mIncludeAdditionalInfo = false;
                                                    catResponseMessage.mAdditionalInfo = 0;
                                                }
                                                catService = this;
                                                responseData = null;
                                                break;
                                            case 7:
                                                selectItemResponseData = new SelectItemResponseData(catResponseMessage.mUsersMenuSelection);
                                                responseData = selectItemResponseData;
                                                catService = this;
                                                break;
                                            case 8:
                                            case 9:
                                                inputGeInput = this.mCurrntCmd.geInput();
                                                if (!inputGeInput.yesNo) {
                                                    if (!z) {
                                                        selectItemResponseData = new GetInkeyInputResponseData(catResponseMessage.mUsersInput, inputGeInput.ucs2, inputGeInput.packed);
                                                    }
                                                    catService = this;
                                                    responseData = null;
                                                } else {
                                                    selectItemResponseData = new GetInkeyInputResponseData(catResponseMessage.mUsersYesNoSelection);
                                                }
                                                responseData = selectItemResponseData;
                                                catService = this;
                                                break;
                                            default:
                                                catService = this;
                                                responseData = null;
                                                break;
                                        }
                                    } else {
                                        this.mCmdIf.handleCallSetupRequestFromSim(catResponseMessage.mUsersConfirm, null);
                                        this.mCurrntCmd = null;
                                        break;
                                    }
                                } else {
                                    sendTerminalResponseBySetUpCall(catResponseMessage.mUsersConfirm, catResponseMessage.mResCode.value());
                                    this.mCurrntCmd = null;
                                    break;
                                }
                            } else {
                                i2 = catResponseMessage.mEventValue;
                                bArr = catResponseMessage.mAddedInfo;
                                if (5 == i2) {
                                    eventDownload(i2, 2, 129, bArr, false);
                                } else {
                                    eventDownload(i2, 130, 129, bArr, false);
                                }
                                break;
                            }
                        } else {
                            catService = this;
                            if (catResponseMessage.mResCode == ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS) {
                                catResponseMessage.setAdditionalInfo(1);
                            } else {
                                catResponseMessage.mIncludeAdditionalInfo = false;
                                catResponseMessage.mAdditionalInfo = 0;
                            }
                            responseData = null;
                        }
                        catService.sendTerminalResponse(cmdDetails, catResponseMessage.mResCode, catResponseMessage.mIncludeAdditionalInfo, catResponseMessage.mAdditionalInfo, responseData);
                        catService.mCurrntCmd = null;
                    }
                    break;
                case 14:
                    this.mIHyperCatManager.sendTrForSetUpCallNetworkError(commandTypeFromInt, catResponseMessage, cmdDetails);
                    catService = this;
                    responseData = null;
                    catService.sendTerminalResponse(cmdDetails, catResponseMessage.mResCode, catResponseMessage.mIncludeAdditionalInfo, catResponseMessage.mAdditionalInfo, responseData);
                    catService.mCurrntCmd = null;
                    break;
                case 15:
                case 16:
                    if (commandTypeFromInt == AppInterface.CommandType.SET_UP_CALL || commandTypeFromInt == AppInterface.CommandType.OPEN_CHANNEL) {
                        this.mCmdIf.handleCallSetupRequestFromSim(false, null);
                        this.mCurrntCmd = null;
                    }
                    catService = this;
                    responseData = null;
                    catService.sendTerminalResponse(cmdDetails, catResponseMessage.mResCode, catResponseMessage.mIncludeAdditionalInfo, catResponseMessage.mAdditionalInfo, responseData);
                    catService.mCurrntCmd = null;
                    break;
                case 17:
                    if (commandTypeFromInt == AppInterface.CommandType.SET_UP_CALL) {
                        this.mCmdIf.handleCallSetupRequestFromSim(false, null);
                        this.mCurrntCmd = null;
                        break;
                    }
                case 18:
                    if (commandTypeFromInt == AppInterface.CommandType.SET_UP_CALL) {
                        CatLog.d(this, "Response SET_UP_CALL as the logical of NO_RESPONSE_FROM_USER");
                        this.mCmdIf.handleCallSetupRequestFromSim(false, null);
                        this.mCurrntCmd = null;
                    }
                    catService = this;
                    responseData = null;
                    catService.sendTerminalResponse(cmdDetails, catResponseMessage.mResCode, catResponseMessage.mIncludeAdditionalInfo, catResponseMessage.mAdditionalInfo, responseData);
                    catService.mCurrntCmd = null;
                    break;
                default:
                    handleExtendCmdResponse(catResponseMessage);
                    break;
            }
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.cat.CatService$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$ResultCode;

        static {
            int[] iArr = new int[ResultCode.values().length];
            $SwitchMap$com$android$internal$telephony$cat$ResultCode = iArr;
            try {
                iArr[ResultCode.HELP_INFO_REQUIRED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.OK.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_PARTIAL_COMPREHENSION.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_MISSING_INFO.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_ADDITIONAL_EFS_READ.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_ICON_NOT_DISPLAYED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_MODIFIED_BY_NAA.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_LIMITED_SERVICE.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_MODIFICATION.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_NAA_NOT_ACTIVE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_TONE_NOT_PLAYED.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.LAUNCH_BROWSER_ERROR.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.NETWORK_CRNTLY_UNABLE_TO_PROCESS.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.BACKWARD_MOVE_BY_USER.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.USER_NOT_ACCEPT.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.NO_RESPONSE_FROM_USER.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.UICC_SESSION_TERM_BY_USER.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            int[] iArr2 = new int[AppInterface.CommandType.values().length];
            $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType = iArr2;
            try {
                iArr2[AppInterface.CommandType.SET_UP_MENU.ordinal()] = 1;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.DISPLAY_TEXT.ordinal()] = 2;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_IDLE_MODE_TEXT.ordinal()] = 3;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_EVENT_LIST.ordinal()] = 4;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PROVIDE_LOCAL_INFORMATION.ordinal()] = 5;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.LAUNCH_BROWSER.ordinal()] = 6;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SELECT_ITEM.ordinal()] = 7;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INPUT.ordinal()] = 8;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INKEY.ordinal()] = 9;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.REFRESH.ordinal()] = 10;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RUN_AT.ordinal()] = 11;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_SMS.ordinal()] = 12;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DTMF.ordinal()] = 13;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_SS.ordinal()] = 14;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_USSD.ordinal()] = 15;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PLAY_TONE.ordinal()] = 16;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_CALL.ordinal()] = 17;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.LANGUAGE_NOTIFICATION.ordinal()] = 18;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.OPEN_CHANNEL.ordinal()] = 19;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.CLOSE_CHANNEL.ordinal()] = 20;
            } catch (NoSuchFieldError unused38) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RECEIVE_DATA.ordinal()] = 21;
            } catch (NoSuchFieldError unused39) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DATA.ordinal()] = 22;
            } catch (NoSuchFieldError unused40) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GEOGRAPHICAL_LOCATION_REQUEST.ordinal()] = 23;
            } catch (NoSuchFieldError unused41) {
            }
        }
    }

    private boolean isStkAppInstalled() {
        List<ResolveInfo> listQueryBroadcastReceivers = this.mContext.getPackageManager().queryBroadcastReceivers(new Intent(AppInterface.CAT_CMD_ACTION), 128);
        return (listQueryBroadcastReceivers == null ? 0 : listQueryBroadcastReceivers.size()) > 0;
    }

    public void update(CommandsInterface commandsInterface, Context context, UiccProfile uiccProfile) {
        UiccCardApplication validUiccApplication;
        int phoneType = getPhoneType();
        CatLog.d(this, "update PhoneType : " + phoneType + ", mSlotId: " + this.mSlotId);
        int i = this.mPhoneType;
        this.mPhoneType = phoneType;
        if (i != 0 && i != phoneType) {
            CatLog.d(this, "phone type change,reset card state to absent.....");
            this.mCardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
        }
        if (this.mPhoneType == 2) {
            validUiccApplication = uiccProfile.getApplication(2);
        } else {
            validUiccApplication = getValidUiccApplication(uiccProfile);
        }
        IccRecords iccRecords = validUiccApplication != null ? validUiccApplication.getIccRecords() : null;
        synchronized (sInstanceLock) {
            if (iccRecords != null) {
                try {
                    IccRecords iccRecords2 = mIccRecords;
                    if (iccRecords2 != iccRecords) {
                        if (iccRecords2 != null) {
                            iccRecords2.unregisterForRecordsLoaded(this);
                        }
                        CatLog.d(this, "Reinitialize the Service with SIMRecords and UiccCardApplication");
                        mIccRecords = iccRecords;
                        mUiccApplication = validUiccApplication;
                        iccRecords.registerForRecordsLoaded(this, 20, null);
                        CatLog.d(this, "registerForRecordsLoaded slotid=" + this.mSlotId + " instance:" + this);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    void updateIccAvailability() {
        UiccController uiccController = this.mUiccController;
        if (uiccController == null) {
            return;
        }
        IccCardStatus.CardState cardState = IccCardStatus.CardState.CARDSTATE_ABSENT;
        UiccCard uiccCard = uiccController.getUiccCard(this.mSlotId);
        if (uiccCard != null) {
            cardState = uiccCard.getCardState();
        }
        IccCardStatus.CardState cardState2 = this.mCardState;
        this.mCardState = cardState;
        CatLog.d(this, "New Card State = " + cardState + " Old Card State = " + cardState2);
        IccCardStatus.CardState cardState3 = IccCardStatus.CardState.CARDSTATE_PRESENT;
        if (cardState2 == cardState3 && cardState != cardState3) {
            broadcastCardStateAndIccRefreshResp(cardState, null);
        } else {
            if (cardState2 == cardState3 || cardState != cardState3) {
                return;
            }
            checkIfKeepOldCardState(cardState2);
            this.mCmdIf.reportStkServiceIsRunning(null);
        }
    }

    private void changeLanguage(String str) {
        LocaleList localeList = LocaleList.getDefault();
        Locale[] localeArr = new Locale[localeList.size() + 1];
        Locale locale = new Locale(str);
        int i = 0;
        localeArr[0] = locale;
        while (i < localeList.size()) {
            int i2 = i + 1;
            localeArr[i2] = localeList.get(i);
            i = i2;
        }
        ((ActivityManager) this.mContext.getSystemService(ActivityManager.class)).setDeviceLocales(new LocaleList(localeArr));
        BackupManager.dataChanged("com.android.providers.settings");
    }

    private int getPhoneType() {
        int[] subId = SubscriptionManager.getSubId(this.mSlotId);
        if (subId == null) {
            return 0;
        }
        int currentPhoneType = TelephonyManager.getDefault().getCurrentPhoneType(subId[0]);
        CatLog.d(this, "getPhoneType phoneType:  " + currentPhoneType + ", mSlotId: " + this.mSlotId);
        return currentPhoneType;
    }

    private void sendTerminalResponseByCurrentCmd(CatCmdMessage catCmdMessage) {
        if (catCmdMessage == null) {
            CatLog.e(this, "catCmd is null.");
            return;
        }
        AppInterface.CommandType commandTypeFromInt = AppInterface.CommandType.fromInt(catCmdMessage.mCmdDet.typeOfCommand);
        CatLog.d(this, "Send TR for cmd: " + commandTypeFromInt);
        int i = AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[commandTypeFromInt.ordinal()];
        if (i == 1 || i == 3) {
            sendTerminalResponse(catCmdMessage.mCmdDet, ResultCode.OK, false, 0, null);
        } else if (i == 17) {
            this.mCmdIf.handleCallSetupRequestFromSim(false, null);
        } else {
            sendTerminalResponse(catCmdMessage.mCmdDet, ResultCode.UICC_SESSION_TERM_BY_USER, false, 0, null);
        }
    }

    protected void sendTerminalResponseBySetUpCall(boolean z, int i) {
        this.mCmdIf.handleCallSetupRequestFromSim(z, null);
    }

    private SubscriptionInfo getSubscriptionInfo(int i) {
        SubscriptionManager subscriptionManager = (SubscriptionManager) this.mContext.getSystemService("telephony_subscription_service");
        if (subscriptionManager == null) {
            return null;
        }
        return subscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(i);
    }

    public void sendUssd(final CommandDetails commandDetails, String str, final byte b) {
        TelephonyManager.UssdResponseCallback ussdResponseCallback = new TelephonyManager.UssdResponseCallback() { // from class: com.android.internal.telephony.cat.CatService.2
            @Override // android.telephony.TelephonyManager.UssdResponseCallback
            public void onReceiveUssdResponse(TelephonyManager telephonyManager, String str2, CharSequence charSequence) {
                CatService.this.sendTerminalResponse(commandDetails, ResultCode.OK, false, 0, new SendUssdResponseData(charSequence == null ? "" : charSequence.toString(), b));
            }

            @Override // android.telephony.TelephonyManager.UssdResponseCallback
            public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String str2, int i) {
                CatService.this.sendTerminalResponse(commandDetails, ResultCode.USSD_RETURN_ERROR, false, 0, null);
            }
        };
        SubscriptionInfo subscriptionInfo = getSubscriptionInfo(this.mSlotId);
        if (subscriptionInfo == null || subscriptionInfo.getSubscriptionId() == -1) {
            CatLog.e(this, "Invalid subscription info");
            sendTerminalResponse(commandDetails, ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS, false, 0, null);
        } else {
            ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(subscriptionInfo.getSubscriptionId()).sendUssdRequest(str, ussdResponseCallback, null);
        }
    }
}
