package com.mediatek.internal.telephony.cat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.CatCmdMessage;
import com.android.internal.telephony.cat.CatResponseMessage;
import com.android.internal.telephony.cat.CatService;
import com.android.internal.telephony.cat.CommandDetails;
import com.android.internal.telephony.cat.CommandParams;
import com.android.internal.telephony.cat.ComprehensionTlvTag;
import com.android.internal.telephony.cat.DisplayTextParams;
import com.android.internal.telephony.cat.Input;
import com.android.internal.telephony.cat.ResponseData;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.RilMessage;
import com.android.internal.telephony.cat.TextMessage;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccCardApplication;
import com.android.internal.telephony.uicc.UiccProfile;
import com.mediatek.internal.telephony.MtkRIL;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.telephony.MtkTelephonyManagerEx;
import java.io.ByteArrayOutputStream;
import mediatek.telephony.MtkTelephony;

/* JADX INFO: loaded from: classes.dex */
public class MtkCatService extends CatService {
    static final String BIP_STATE_CHANGED = "mediatek.intent.action.BIP_STATE_CHANGED";
    private static final boolean DBG = true;
    private static final int DISABLE_DISPLAY_TEXT_DELAYED_TIME = 30000;
    private static final int IVSR_DELAYED_TIME = 60000;
    public static final int MSG_ID_CACHED_DISPLAY_TEXT_TIMEOUT = 46;
    private static final int MSG_ID_CALL_CTRL = 25;
    public static final int MSG_ID_CONN_RETRY_TIMEOUT = 47;
    private static final int MSG_ID_DISABLE_DISPLAY_TEXT_DELAYED = 15;
    static final int MSG_ID_EVENT_DOWNLOAD = 11;
    private static final int MSG_ID_IVSR_DELAYED = 14;
    private static final int MSG_ID_SETUP_MENU_RESET = 24;
    private BroadcastReceiver MtkCatServiceReceiver;
    private boolean isDisplayTextDisabled;
    private boolean isIvsrBootUp;
    private MtkRIL mMtkCmdIf;
    private int mPhoneType;
    private boolean mReadFromPreferenceDone;
    Handler mTimeoutHandler;
    private int simState;
    private static String[] sInstKey = {"sInstanceSim1", "sInstanceSim2", "sInstanceSim3", "sInstanceSim4"};
    protected static Object mLock = new Object();

    void cancelTimeOut(int msg) {
        MtkCatLog.d(this, "cancelTimeOut, sim_id: " + this.mSlotId + ", msg id: " + msg);
        this.mTimeoutHandler.removeMessages(msg);
    }

    void startTimeOut(int msg, long delay) {
        MtkCatLog.d(this, "startTimeOut, sim_id: " + this.mSlotId + ", msg id: " + msg);
        cancelTimeOut(msg);
        this.mTimeoutHandler.sendMessageDelayed(this.mTimeoutHandler.obtainMessage(msg), delay);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public MtkCatService(CommandsInterface ci, UiccCardApplication ca, IccRecords ir, Context context, IccFileHandler fh, UiccProfile uiccProfile, int slotId, Looper looper, FeatureFlags featureFlags) {
        super(ci, ca, ir, context, fh, uiccProfile, slotId, looper, featureFlags);
        this.mReadFromPreferenceDone = false;
        this.simState = 0;
        this.isIvsrBootUp = false;
        this.isDisplayTextDisabled = false;
        this.mPhoneType = 0;
        this.mTimeoutHandler = new Handler() { // from class: com.mediatek.internal.telephony.cat.MtkCatService.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 15:
                        MtkCatLog.d(this, "[Reset Disable Display Text flag because timeout");
                        MtkCatService.this.isDisplayTextDisabled = false;
                        break;
                    case MtkCatService.MSG_ID_CACHED_DISPLAY_TEXT_TIMEOUT /* 46 */:
                        MtkCatLog.d(this, "Cache DISPLAY_TEXT time out, sim_id: " + MtkCatService.this.mSlotId);
                        break;
                }
            }
        };
        this.MtkCatServiceReceiver = new BroadcastReceiver() { // from class: com.mediatek.internal.telephony.cat.MtkCatService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                MtkCatLog.d(this, "CatServiceReceiver action: " + action);
                if (!action.equals("com.mediatek.intent.action.IVSR_NOTIFY") || MtkCatService.this.mSlotId != intent.getIntExtra("slot", 0)) {
                    return;
                }
                String ivsrAction = intent.getStringExtra(MtkTelephony.WapPush.ACTION);
                if (ivsrAction.equals("start")) {
                    MtkCatLog.d(this, "[IVSR set IVSR flag");
                    MtkCatService.this.isIvsrBootUp = MtkCatService.DBG;
                    MtkCatService.this.sendEmptyMessageDelayed(14, 60000L);
                }
            }
        };
        MtkCatLog.d(this, "slotId " + slotId);
        this.mMtkCmdIf = (MtkRIL) ci;
        IntentFilter intentFilter = new IntentFilter("com.mediatek.intent.action.IVSR_NOTIFY");
        this.mContext.registerReceiver(this.MtkCatServiceReceiver, intentFilter, 2);
        MtkCatLog.d(this, "CatService: is running");
        this.mMtkCmdIf.setOnStkSetupMenuReset(this, 24, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void dispose() {
        synchronized (sInstanceLock) {
            MtkCatLog.d(this, "Disposing MtkCatService object : " + this.mSlotId);
            if (sInstance != null && sInstance[this.mSlotId] != null) {
                if (this.MtkCatServiceReceiver != null) {
                    this.mContext.unregisterReceiver(this.MtkCatServiceReceiver);
                    this.MtkCatServiceReceiver = null;
                }
                this.mMtkCmdIf.unSetOnStkSetupMenuReset(this);
                this.mCmdIf.unregisterForIccRefresh(this);
                super.dispose();
                return;
            }
            MtkCatLog.d(this, "sInstance is null, maybe dispose already: " + this.mSlotId);
        }
    }

    protected void sendMenuSelection(int menuId, boolean helpRequired) {
        MtkCatLog.d("CatService", "sendMenuSelection SET_UP_MENU");
        super.sendMenuSelection(menuId, helpRequired);
        cancelTimeOut(15);
        this.isDisplayTextDisabled = false;
    }

    public static CatService getInstance(CommandsInterface ci, Context context, UiccProfile uiccProfile) {
        MtkCatLog.d("CatService", "call getInstance 2");
        int sim_id = 0;
        if (uiccProfile != null) {
            sim_id = uiccProfile.getPhoneId();
            MtkCatLog.d("CatService", "get SIM id from UiccCard. sim id: " + sim_id);
        }
        return CatService.getInstance(ci, context, uiccProfile, sim_id);
    }

    public synchronized void onCmdResponse(CatResponseMessage resMsg) {
        MtkCatResponseMessage resMtkMsg;
        MtkCatLog.d(this, "MtkCatService onCmdResponse");
        if (resMsg == null) {
            return;
        }
        if (MtkCatResponseMessage.class.isInstance(resMsg)) {
            Message msg = obtainMessage(6, resMsg);
            msg.sendToTarget();
        } else {
            if (this.mCurrntCmd != null) {
                resMtkMsg = new MtkCatResponseMessage(this.mCurrntCmd, resMsg);
            } else {
                CommandDetails cmdDet = new CommandDetails();
                cmdDet.typeOfCommand = 112;
                resMtkMsg = new MtkCatResponseMessage(new CatCmdMessage(new CommandParams(cmdDet)), resMsg);
            }
            Message msg2 = obtainMessage(6, resMtkMsg);
            msg2.sendToTarget();
        }
    }

    public synchronized void onEventDownload(MtkCatResponseMessage resMsg) {
        if (resMsg == null) {
            return;
        }
        Message msg = obtainMessage(11, resMsg);
        msg.sendToTarget();
    }

    private void handleEventDownload(MtkCatResponseMessage resMsg) {
        eventDownload(resMsg.mEvent, resMsg.mSourceId, resMsg.mDestinationId, resMsg.mAdditionalInfo, resMsg.mOneShot);
    }

    public Context getContext() {
        return this.mContext;
    }

    private boolean isAlarmBoot() {
        String bootReason = SystemProperties.get("vendor.sys.boot.reason");
        if (bootReason == null || !bootReason.equals(RadioCapabilitySwitchUtil.IMSI_READY)) {
            return false;
        }
        return DBG;
    }

    public IccRecords getIccRecords() {
        IccRecords iccRecords;
        synchronized (sInstanceLock) {
            iccRecords = mIccRecords;
        }
        return iccRecords;
    }

    private static void saveCmdToPreference(Context context, String key, String cmd) {
        synchronized (mLock) {
            MtkCatLog.d("MtkCatService", "saveCmdToPreference, key: " + key + ", cmd: " + cmd);
            SharedPreferences preferences = context.getSharedPreferences("set_up_menu", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, cmd);
            editor.apply();
        }
    }

    private static String readCmdFromPreference(MtkCatService inst, Context context, String key) {
        String cmd = String.valueOf("");
        if (inst == null) {
            MtkCatLog.d("MtkCatService", "readCmdFromPreference with null instance");
            return null;
        }
        synchronized (mLock) {
            if (!inst.mReadFromPreferenceDone) {
                SharedPreferences preferences = context.getSharedPreferences("set_up_menu", 0);
                cmd = preferences.getString(key, "");
                inst.mReadFromPreferenceDone = DBG;
                MtkCatLog.d("MtkCatService", "readCmdFromPreference, key: " + key + ", cmd: " + cmd);
            } else {
                MtkCatLog.d("MtkCatService", "readCmdFromPreference, do not read again");
            }
        }
        if (cmd.length() == 0) {
            return null;
        }
        return cmd;
    }

    public static int getBatteryState(Context context) {
        int batteryState = 255;
        IntentFilter filter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        Intent batteryStatus = context.registerReceiver(null, filter);
        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra("level", -1);
            int scale = batteryStatus.getIntExtra("scale", -1);
            int status = batteryStatus.getIntExtra(MtkTelephony.MtkThreads.STATUS, -1);
            boolean isCharging = (status == 2 || status == 5) ? DBG : false;
            float batteryPct = level / scale;
            MtkCatLog.d("MtkCatService", " batteryPct == " + batteryPct + "isCharging:" + isCharging);
            if (isCharging) {
                batteryState = 255;
            } else if (batteryPct <= 0.05d) {
                batteryState = 0;
            } else if (batteryPct > 0.05d && batteryPct <= 0.15d) {
                batteryState = 1;
            } else if (batteryPct > 0.15d && batteryPct <= 0.6d) {
                batteryState = 2;
            } else if (batteryPct > 0.6d && batteryPct < 1.0f) {
                batteryState = 3;
            } else if (batteryPct == 1.0f) {
                batteryState = 4;
            }
        }
        MtkCatLog.d("MtkCatService", "getBatteryState() batteryState = " + batteryState);
        return batteryState;
    }

    protected void getInKeyResponse(ByteArrayOutputStream buf, Input cmdInput) {
        int tag = ComprehensionTlvTag.DURATION.value();
        buf.write(tag);
        buf.write(2);
        buf.write(cmdInput.duration.timeUnit.value());
        buf.write(cmdInput.duration.timeInterval);
    }

    protected void handleEventNotifyCommandWithWrongData(RilMessage rilMsg) {
        int commandNumber;
        int typeOfCommand;
        int commandQualifier;
        if (rilMsg.mResCode != ResultCode.OK) {
            MtkCatLog.d(this, "event notify error code: " + rilMsg.mResCode);
            if (rilMsg.mData instanceof CommandParams) {
                CommandParams cmdParams = (CommandParams) rilMsg.mData;
                if (rilMsg.mResCode == ResultCode.PRFRMD_ICON_NOT_DISPLAYED && (cmdParams.mCmdDet.typeOfCommand == 17 || cmdParams.mCmdDet.typeOfCommand == 18 || cmdParams.mCmdDet.typeOfCommand == 19 || cmdParams.mCmdDet.typeOfCommand == 20)) {
                    MtkCatLog.d(this, "notify user text message even though get icon fail");
                    handleCommand(cmdParams, false);
                }
                if (cmdParams.mCmdDet.typeOfCommand == 64) {
                    MtkCatLog.d(this, "Open Channel with ResultCode");
                    handleCommand(cmdParams, false);
                }
                if (rilMsg.mResCode == ResultCode.CMD_DATA_NOT_UNDERSTOOD) {
                    AppInterface.CommandType type = AppInterface.CommandType.fromInt(cmdParams.mCmdDet.typeOfCommand);
                    if (type == AppInterface.CommandType.SET_UP_CALL) {
                        this.mMtkCmdIf.handleStkCallSetupRequestFromSimWithResCode(false, ResultCode.CMD_DATA_NOT_UNDERSTOOD.value(), null);
                    } else {
                        sendTerminalResponse(cmdParams.mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
                    }
                }
                return;
            }
            String cmdData = (String) rilMsg.mData;
            if (cmdData != null) {
                if (Integer.parseInt(cmdData.split("")[2]) < 7) {
                    int commandNumber2 = Integer.parseInt(cmdData.substring(8, 10), 16);
                    int typeOfCommand2 = Integer.parseInt(cmdData.substring(10, 12), 16);
                    commandNumber = commandNumber2;
                    typeOfCommand = typeOfCommand2;
                    commandQualifier = Integer.parseInt(cmdData.substring(12, 14), 16);
                } else {
                    int commandNumber3 = Integer.parseInt(cmdData.substring(10, 12), 16);
                    int typeOfCommand3 = Integer.parseInt(cmdData.substring(12, 14), 16);
                    commandNumber = commandNumber3;
                    typeOfCommand = typeOfCommand3;
                    commandQualifier = Integer.parseInt(cmdData.substring(14, 16), 16);
                }
                CommandDetails cmdDet = new CommandDetails();
                cmdDet.compRequired = DBG;
                cmdDet.commandNumber = commandNumber;
                cmdDet.typeOfCommand = typeOfCommand;
                cmdDet.commandQualifier = commandQualifier;
                sendTerminalResponse(new CommandParams(cmdDet).mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
            }
        }
    }

    protected void SendTerminalResponseWhenLengthInCorrect(RilMessage rilMsg) {
        int typeOfCommand;
        int commandNumber;
        int commandQualifier;
        String cmdData = (String) rilMsg.mData;
        if (cmdData != null) {
            if (Integer.parseInt(cmdData.split("")[2]) < 7) {
                typeOfCommand = Integer.parseInt(cmdData.substring(10, 12), 16);
                commandNumber = Integer.parseInt(cmdData.substring(8, 10), 16);
                commandQualifier = Integer.parseInt(cmdData.substring(12, 14), 16);
            } else {
                typeOfCommand = Integer.parseInt(cmdData.substring(12, 14), 16);
                commandNumber = Integer.parseInt(cmdData.substring(10, 12), 16);
                commandQualifier = Integer.parseInt(cmdData.substring(14, 16), 16);
            }
            CommandDetails cmdDet = new CommandDetails();
            cmdDet.compRequired = DBG;
            cmdDet.commandNumber = commandNumber;
            cmdDet.typeOfCommand = typeOfCommand;
            cmdDet.commandQualifier = commandQualifier;
            sendTerminalResponse(cmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
        }
    }

    protected void sendTerminalResponseBySetUpCall(boolean accept, int resCode) {
        this.mMtkCmdIf.handleStkCallSetupRequestFromSimWithResCode(accept, resCode, null);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected boolean sendTerminalResponseDirectlyInsomeCase(CommandParams cmdParams) {
        boolean isAlarmState;
        int flightMode;
        int flightMode2;
        new CatCmdMessage(cmdParams);
        switch (AnonymousClass3.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[cmdParams.getCommandType().ordinal()]) {
            case 1:
                isAlarmState = isAlarmBoot();
                try {
                    int flightMode3 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on");
                    flightMode = flightMode3;
                } catch (Settings.SettingNotFoundException e) {
                    MtkCatLog.d(this, "fail to get property from Settings");
                    flightMode = 0;
                }
                boolean isFlightMode = flightMode != 0;
                MtkCatLog.d(this, "isAlarmState = " + isAlarmState + ", isFlightMode = " + isFlightMode + ", flightMode = " + flightMode);
                if (isAlarmState && isFlightMode) {
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.OK, false, 0, null);
                    return DBG;
                }
                if (this.isIvsrBootUp) {
                    MtkCatLog.d(this, "[IVSR send TR directly");
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.BACKWARD_MOVE_BY_USER, false, 0, null);
                    return DBG;
                }
                if (this.isDisplayTextDisabled) {
                    MtkCatLog.d(this, "[Sim Recovery send TR directly");
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.BACKWARD_MOVE_BY_USER, false, 0, null);
                    return DBG;
                }
                TextMessage textMessage = ((DisplayTextParams) cmdParams).mTextMsg;
                if (textMessage.deviceIdentities != null && textMessage.deviceIdentities.destinationId == 255) {
                    MtkCatLog.d(this, "Command not understood send TR directly");
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.CMD_TYPE_NOT_UNDERSTOOD, false, 0, null);
                    return DBG;
                }
                return false;
            case 2:
                this.mIsProactiveCmdResponsed = DBG;
                return false;
            case 3:
                if (cmdParams.mCmdDet.commandQualifier == 10) {
                    int batterystate = getBatteryState(this.mContext);
                    ResponseData resp = new MtkProvideLocalInformationResponseData(batterystate);
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.OK, false, 0, resp);
                    return DBG;
                }
                return false;
            case 4:
                isAlarmState = isAlarmBoot();
                try {
                    int flightMode4 = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on");
                    flightMode2 = flightMode4;
                } catch (Settings.SettingNotFoundException e2) {
                    MtkCatLog.d(this, "fail to get property from Settings");
                    flightMode2 = 0;
                }
                boolean isFlightMode2 = flightMode2 != 0;
                MtkCatLog.d(this, "isAlarmState = " + isAlarmState + ", isFlightMode = " + isFlightMode2 + ", flightMode = " + flightMode2);
                if (isAlarmState && isFlightMode2) {
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.UICC_SESSION_TERM_BY_USER, false, 0, null);
                    return DBG;
                }
                return false;
            case 5:
            case 6:
                this.simState = MtkTelephonyManagerEx.getDefault().getSimCardState(this.mSlotId);
                MtkCatLog.d(this, "simState: " + this.simState);
                if (this.simState != 11) {
                    sendTerminalResponse(cmdParams.mCmdDet, ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS, false, 0, null);
                    return DBG;
                }
                return false;
            default:
                return false;
        }
    }

    /* JADX INFO: renamed from: com.mediatek.internal.telephony.cat.MtkCatService$3, reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType = new int[AppInterface.CommandType.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.DISPLAY_TEXT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_EVENT_LIST.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PROVIDE_LOCAL_INFORMATION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SELECT_ITEM.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INPUT.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INKEY.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    protected boolean handleExtendMessage(Message msg) {
        AsyncResult ar;
        MtkCatLog.d(this, "MtkCatservice handleExtendMessage[" + msg.what + "]");
        switch (msg.what) {
            case 1:
            case 2:
            case 3:
            case 5:
                if (msg.obj != null && (ar = (AsyncResult) msg.obj) != null && ar.result != null) {
                    try {
                        String data = (String) ar.result;
                        if (data.contains("BIP")) {
                            Intent intent = new Intent(BIP_STATE_CHANGED);
                            intent.putExtra("BIP_CMD", data);
                            intent.putExtra("SLOT_ID", this.mSlotId);
                            intent.setPackage("com.mediatek.engineermode");
                            MtkCatLog.d(this, "Broadcast BIP Intent: Sending data: " + data + " on slotid:" + this.mSlotId);
                            this.mContext.sendBroadcast(intent);
                            return DBG;
                        }
                    } catch (ClassCastException e) {
                    }
                }
                return false;
            case 11:
                handleEventDownload((MtkCatResponseMessage) msg.obj);
                return DBG;
            case 14:
                MtkCatLog.d(this, "[IVSR cancel IVSR flag");
                this.isIvsrBootUp = false;
                return DBG;
            default:
                return false;
        }
    }

    protected void handleExtendCmdResponse(CatResponseMessage resMsg) {
        CommandDetails cmdDet = resMsg.getCmdDetails();
        AppInterface.CommandType type = AppInterface.CommandType.fromInt(cmdDet.typeOfCommand);
        if (resMsg.mResCode == ResultCode.NETWORK_CRNTLY_UNABLE_TO_PROCESS) {
            if (type == AppInterface.CommandType.SET_UP_CALL) {
                this.mMtkCmdIf.handleStkCallSetupRequestFromSimWithResCode(resMsg.mUsersConfirm, resMsg.mResCode.value(), null);
                this.mCurrntCmd = null;
            } else if (type == AppInterface.CommandType.DISPLAY_TEXT) {
                sendTerminalResponse(cmdDet, resMsg.mResCode, resMsg.mIncludeAdditionalInfo, resMsg.mAdditionalInfo, null);
                this.mCurrntCmd = null;
            }
        }
    }

    protected void checkIfKeepOldCardState(IccCardStatus.CardState oldState) {
        if (this.mCmdIf.getRadioState() == 2) {
            MtkCatLog.w(this, "updateIccAvailability(): Radio unavailable");
            this.mCardState = oldState;
        }
    }
}
