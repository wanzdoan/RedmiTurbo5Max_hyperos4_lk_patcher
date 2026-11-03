package com.android.internal.telephony.cat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.MiuiTelephonyManagerStub;
import android.text.TextUtils;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.telephony.Rlog;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.telephony.ExtTelephonyManager;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import miui.telephony.MiuiTelephony;

/* JADX INFO: loaded from: classes.dex */
public class BindEsimService implements AppInterface {
    static final String CONFIRMATION = "confirm";
    private static final int DEV_ID_ESIMBIND = 241;
    private static final int DEV_ID_ESIMPAIR = 242;
    private static final int DEV_ID_ESIMRESULT = 243;
    private static final int DEV_ID_NETWORK = 131;
    private static final int DEV_ID_TERMINAL = 130;
    private static final int DEV_ID_UICC = 129;
    static final String FAIL_STR_RESPONSE = "01";
    static final String HELP = "help";
    static final String INPUT = "input";
    private static final String LOG_TAG = "BindEsimService";
    protected static final int MSG_ID_EVENT_NOTIFY = 3;
    protected static final int MSG_ID_ICC_CHANGED = 8;
    protected static final int MSG_ID_PROACTIVE_COMMAND = 2;
    static final int MSG_ID_REFRESH = 5;
    static final int MSG_ID_RESPONSE = 6;
    static final int MSG_ID_RIL_MSG_DECODED = 10;
    protected static final int MSG_ID_SESSION_END = 1;
    static final int NO = 0;
    static final String NO_STR_RESPONSE = "NO";
    static final String RES_ID = "response id";
    static final int RES_ID_CONFIRM = 13;
    static final int RES_ID_END_SESSION = 22;
    static final int RES_ID_INPUT = 12;
    static final int RES_ID_UNABLE_TO_PROCESS = 25;
    static final String SPECIAL_INPUT_DEFAULT_TEXT = "12345678";
    static final String SPECIAL_TEXTSTRING = "FF01FF01FF018BBE59079A8C8BC16210529F";
    static final String SPECIAL_TEXTSTRING2 = "FF01FF01FF01672A638867438BBE5907FF0C006500530049004D62D27EDD670D52A1";
    static final String SUCCESS_STR_RESPONSE = "00";
    static final String UNABLE_TO_PROCESS_STR_RESPONSE = "20";
    static final int YES = 1;
    static final String YES_STR_RESPONSE = "YES";
    private BindEsimHandler mBindEsimServiceHandler;
    private HandlerThread mBindEsimServiceThread;
    private CommandsInterface mCmdIf;
    private Context mContext;
    private EsimBindRilMessageDecoder mMsgDecoder;
    private int mSlotId;
    private UiccController mUiccController;
    private CatCmdMessage mCurrentCmd = null;
    private DeviceIdentities mCurrentInputCmdDeviceIDs = null;
    private List<ComprehensionTlv> mCurrentInputCtlvs = null;
    private List<ComprehensionTlv> mInputCtlvsWithSpecialText = null;
    private List<ComprehensionTlv> mCurrentDisplayTextCtlvs = null;
    private String mPreEsimProCmd = null;
    private IccCardStatus.CardState mCardState = IccCardStatus.CardState.CARDSTATE_ABSENT;

    public BindEsimService(CommandsInterface ci, Context context, int slotId) {
        this.mMsgDecoder = null;
        this.mCmdIf = ci;
        this.mContext = context;
        this.mSlotId = slotId;
        if (this.mBindEsimServiceThread == null) {
            this.mBindEsimServiceThread = new HandlerThread("BindEsimServiceThread");
            this.mBindEsimServiceThread.start();
        }
        this.mBindEsimServiceHandler = new BindEsimHandler(this.mBindEsimServiceThread.getLooper());
        IccFileHandler tmepfh = new IccFileHandler(this, ci) { // from class: com.android.internal.telephony.cat.BindEsimService.1
            protected String getEFPath(int efid) {
                return "";
            }

            protected void logd(String s) {
            }

            protected void loge(String s) {
            }
        };
        BindEsimLog(this, "init BindEsimService mSlotId = " + this.mSlotId);
        this.mMsgDecoder = EsimBindRilMessageDecoder.getInstance(this.mBindEsimServiceHandler, tmepfh, context, slotId);
        if (this.mMsgDecoder == null) {
            BindEsimLog(this, "Null RilMessageDecoder instance");
            return;
        }
        this.mMsgDecoder.start();
        this.mUiccController = UiccController.getInstance();
        this.mUiccController.registerForIccChanged(this.mBindEsimServiceHandler, 8, (Object) null);
    }

    private class BindEsimHandler extends Handler {
        BindEsimHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            AsyncResult ar;
            BindEsimService.this.BindEsimLog(this, "handleMessage[" + msg.what + "]");
            switch (msg.what) {
                case 1:
                case 2:
                case 3:
                case 5:
                    BindEsimService.this.BindEsimLog(this, "ril message arrived,slotid:" + BindEsimService.this.mSlotId);
                    String data = null;
                    if (msg.obj != null && (ar = (AsyncResult) msg.obj) != null && ar.result != null) {
                        try {
                            data = (String) ar.result;
                        } catch (ClassCastException e) {
                            return;
                        }
                    }
                    BindEsimService.this.BindEsimLog(this, "ril message arrived,data:" + data);
                    if (BindEsimService.this.mPreEsimProCmd != null && data != null && TextUtils.equals(BindEsimService.this.mPreEsimProCmd, data)) {
                        BindEsimService.this.BindEsimLog(this, "dup cmd, ingore:" + BindEsimService.this.mPreEsimProCmd);
                        return;
                    }
                    BindEsimService.this.resetCmd();
                    List<ComprehensionTlv> ctlvs = BindEsimService.this.getComprehensionTlvs(data);
                    BindEsimService.this.mCurrentInputCtlvs = ctlvs;
                    DeviceIdentities tempDeviceIDs = BindEsimService.processCommandDeviceIDs(ctlvs);
                    String textString = BindEsimService.getOriginalDataForTag(ctlvs, ComprehensionTlvTag.TEXT_STRING, false);
                    String defaultText = BindEsimService.getOriginalDataForTag(ctlvs, ComprehensionTlvTag.DEFAULT_TEXT, false);
                    if (tempDeviceIDs != null) {
                        BindEsimService.this.BindEsimLog(this, "tempDeviceIDs:" + tempDeviceIDs.sourceId + " " + tempDeviceIDs.destinationId);
                    }
                    if (tempDeviceIDs != null && (tempDeviceIDs.destinationId == BindEsimService.DEV_ID_ESIMBIND || tempDeviceIDs.destinationId == BindEsimService.DEV_ID_ESIMPAIR || tempDeviceIDs.destinationId == BindEsimService.DEV_ID_ESIMRESULT)) {
                        BindEsimService.this.mCurrentInputCmdDeviceIDs = tempDeviceIDs;
                        BindEsimService.this.mCurrentInputCtlvs = ctlvs;
                        BindEsimService.this.mPreEsimProCmd = data;
                    } else if (BindEsimService.SPECIAL_TEXTSTRING.equals(textString) || BindEsimService.SPECIAL_TEXTSTRING2.equals(textString)) {
                        BindEsimService.this.mCurrentDisplayTextCtlvs = ctlvs;
                    } else {
                        boolean zEquals = BindEsimService.SPECIAL_INPUT_DEFAULT_TEXT.equals(defaultText);
                        BindEsimService bindEsimService = BindEsimService.this;
                        if (zEquals) {
                            bindEsimService.mInputCtlvsWithSpecialText = ctlvs;
                        } else {
                            bindEsimService.resetCmd();
                            return;
                        }
                    }
                    EsimBindRilMessageDecoder esimBindRilMessageDecoder = BindEsimService.this.mMsgDecoder;
                    BindEsimService bindEsimService2 = BindEsimService.this;
                    if (esimBindRilMessageDecoder != null) {
                        bindEsimService2.mMsgDecoder.sendStartDecodingMessageParams(new RilMessage(msg.what, data));
                        return;
                    } else {
                        bindEsimService2.BindEsimLog(this, "Error in handleMessage (" + msg.what + ") mMsgDecoder is NULL");
                        return;
                    }
                case 4:
                case 7:
                case 9:
                default:
                    throw new AssertionError("Unrecognized CAT command: " + msg.what);
                case 6:
                    BindEsimService.this.handleCmdResponse((CatResponseMessage) msg.obj);
                    return;
                case 8:
                    BindEsimService.this.BindEsimLog(this, "MSG_ID_ICC_CHANGED");
                    BindEsimService.this.updateIccAvailability();
                    return;
                case 10:
                    BindEsimService.this.handleRilMsg((RilMessage) msg.obj);
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:21:0x0063  */
    public void handleCmdResponse(CatResponseMessage resMsg) {
        ResponseData resp;
        if (!validateResponse(resMsg)) {
            return;
        }
        boolean helpRequired = false;
        CommandDetails cmdDet = resMsg.getCmdDetails();
        AppInterface.CommandType type = AppInterface.CommandType.fromInt(cmdDet.typeOfCommand);
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$ResultCode[resMsg.mResCode.ordinal()]) {
            case 1:
                helpRequired = true;
                break;
            case 2:
            case 3:
            case 4:
                break;
            default:
                return;
        }
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[type.ordinal()]) {
            case 1:
            case 2:
                Input input = this.mCurrentCmd.geInput();
                if (!input.yesNo) {
                    if (!helpRequired) {
                        ResponseData resp2 = new GetInkeyInputResponseData(resMsg.mUsersInput, input.ucs2, input.packed);
                        resp = resp2;
                    } else {
                        resp = null;
                    }
                } else {
                    ResponseData resp3 = new GetInkeyInputResponseData(resMsg.mUsersYesNoSelection);
                    resp = resp3;
                }
                break;
            case 3:
                if (resMsg.mResCode == ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS) {
                    resMsg.setAdditionalInfo(1);
                } else {
                    resMsg.mIncludeAdditionalInfo = false;
                    resMsg.mAdditionalInfo = 0;
                }
                resp = null;
                break;
            default:
                resp = null;
                break;
        }
        sendTerminalResponse(cmdDet, resMsg.mResCode, resMsg.mIncludeAdditionalInfo, resMsg.mAdditionalInfo, resp);
        this.mCurrentCmd = null;
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.cat.BindEsimService$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType;
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$ResultCode = new int[ResultCode.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.HELP_INFO_REQUIRED.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.OK.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.PRFRMD_WITH_PARTIAL_COMPREHENSION.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$ResultCode[ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType = new int[AppInterface.CommandType.values().length];
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INPUT.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INKEY.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.DISPLAY_TEXT.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    private boolean validateResponse(CatResponseMessage resMsg) {
        if (this.mCurrentCmd == null) {
            return false;
        }
        boolean validResponse = resMsg.mCmdDet.compareTo(this.mCurrentCmd.mCmdDet);
        BindEsimLog(this, "isResponse for last valid cmd: " + validResponse);
        return validResponse;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRilMsg(RilMessage rilMsg) {
        if (rilMsg == null) {
        }
        switch (rilMsg.mId) {
            case 1:
                handleSessionEnd();
                break;
            case 2:
                try {
                    CommandParams cmdParams = (CommandParams) rilMsg.mData;
                    if (cmdParams != null) {
                        if (rilMsg.mResCode == ResultCode.OK) {
                            handleCommand(cmdParams, true);
                        } else {
                            sendTerminalResponse(cmdParams.mCmdDet, rilMsg.mResCode, false, 0, null);
                        }
                    }
                } catch (ClassCastException e) {
                    BindEsimLog(this, "Fail to parse proactive command");
                    if (this.mCurrentCmd != null) {
                        sendTerminalResponse(this.mCurrentCmd.mCmdDet, ResultCode.CMD_DATA_NOT_UNDERSTOOD, false, 0, null);
                        return;
                    }
                    return;
                }
                break;
        }
    }

    private void handleCommand(CommandParams cmdParams, boolean isProactiveCmd) {
        BindEsimLog(this, cmdParams.getCommandType().name() + " mSlotId = " + this.mSlotId + " cmdParams=" + cmdParams);
        CatCmdMessage cmdMsg = new CatCmdMessage(cmdParams);
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[cmdParams.getCommandType().ordinal()]) {
            case 1:
            case 2:
                this.mCurrentCmd = cmdMsg;
                if (this.mCurrentInputCmdDeviceIDs != null) {
                    int destinationId = this.mCurrentInputCmdDeviceIDs.destinationId;
                    if (destinationId == DEV_ID_ESIMBIND || destinationId == DEV_ID_ESIMPAIR || destinationId == DEV_ID_ESIMRESULT) {
                        String textString = getOriginalText(this.mCurrentInputCtlvs);
                        String cmdType = cmdMsg.getCmdType().name();
                        String action = Integer.toHexString(this.mCurrentInputCmdDeviceIDs.destinationId).toUpperCase();
                        List<String> argumentsForESIMBind = new ArrayList<>();
                        argumentsForESIMBind.add(textString);
                        argumentsForESIMBind.add(cmdType);
                        argumentsForESIMBind.add(action);
                        String resultString = processBindingData(argumentsForESIMBind);
                        BindEsimLog(this, " resultString = " + resultString);
                        sendResultResponse(resultString, cmdMsg.getCmdType(), this.mSlotId);
                    }
                } else if (this.mInputCtlvsWithSpecialText != null) {
                    String textString2 = getOriginalDataForTag(this.mInputCtlvsWithSpecialText, ComprehensionTlvTag.TEXT_STRING, false);
                    BindEsimLog(this, " textString = " + textString2);
                    String cmdType2 = cmdMsg.getCmdType().name();
                    List<String> argumentsForESIMBind2 = new ArrayList<>();
                    argumentsForESIMBind2.add(textString2);
                    argumentsForESIMBind2.add(cmdType2);
                    String resultString2 = processBindingData(argumentsForESIMBind2);
                    BindEsimLog(this, " resultString = " + resultString2);
                    sendResultResponse(resultString2, cmdMsg.getCmdType(), this.mSlotId);
                }
                break;
            case 3:
                this.mCurrentCmd = cmdMsg;
                if (this.mCurrentDisplayTextCtlvs != null) {
                    String originalTextString = getOriginalDataForTag(this.mCurrentDisplayTextCtlvs, ComprehensionTlvTag.TEXT_STRING, false);
                    List<String> params = new ArrayList<>();
                    params.add(originalTextString);
                    params.add(this.mCurrentCmd.getCmdType().name());
                    sendResultResponse(processBindingData(params), cmdMsg.getCmdType(), this.mSlotId);
                }
                break;
            default:
                BindEsimLog(this, "Unsupported command");
                return;
        }
        this.mCurrentCmd = cmdMsg;
    }

    private void sendResultResponse(String result, AppInterface.CommandType cmdType, int slotId) {
        Bundle args = new Bundle();
        if (cmdType == AppInterface.CommandType.GET_INPUT) {
            if (!TextUtils.isEmpty(result)) {
                args.putInt(RES_ID, 12);
                args.putString(INPUT, result);
            } else {
                args.putInt(RES_ID, 25);
            }
            args.putBoolean(HELP, false);
        } else if (cmdType == AppInterface.CommandType.DISPLAY_TEXT) {
            args.putInt(RES_ID, 13);
            args.putBoolean(CONFIRMATION, true);
        }
        handleCmdResponse(args, slotId);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:39:0x00d6  */
    private void handleCmdResponse(Bundle args, int slotId) {
        BindEsimLog(this, "handleCmdResponse, sim id: " + slotId);
        if (this.mCurrentCmd == null) {
            return;
        }
        CatResponseMessage resMsg = new CatResponseMessage(this.mCurrentCmd);
        byte b = 0;
        args.getBoolean(HELP, false);
        switch (args.getInt(RES_ID)) {
            case 12:
                BindEsimLog(this, "RES_ID_INPUT");
                String input = args.getString(INPUT);
                if (input != null && this.mCurrentCmd.geInput() != null && this.mCurrentCmd.geInput().yesNo) {
                    boolean yesNoSelection = input.equals(YES_STR_RESPONSE);
                    resMsg.setYesNo(yesNoSelection);
                    break;
                } else {
                    if (input == null) {
                        input = "";
                    }
                    switch (input.hashCode()) {
                        case 1536:
                            if (!input.equals(SUCCESS_STR_RESPONSE)) {
                                b = -1;
                            }
                            break;
                        case 1537:
                            if (!input.equals(FAIL_STR_RESPONSE)) {
                                b = -1;
                            } else {
                                b = 1;
                            }
                            break;
                        case 1598:
                            if (!input.equals(UNABLE_TO_PROCESS_STR_RESPONSE)) {
                                b = -1;
                            } else {
                                b = 2;
                            }
                            break;
                        default:
                            b = -1;
                            break;
                    }
                    switch (b) {
                        case 0:
                            BindEsimLog(this, "SUCCESS_STR_RESPONSE");
                            resMsg.setResultCode(ResultCode.OK);
                            resMsg.setInput("");
                            break;
                        case 1:
                            BindEsimLog(this, "FAIL_STR_RESPONSE");
                            resMsg.setResultCode(ResultCode.PRFRMD_WITH_PARTIAL_COMPREHENSION);
                            resMsg.setInput("");
                            break;
                        case 2:
                            BindEsimLog(this, "UNABLE_TO_PROCESS_STR_RESPONSE");
                            resMsg.setResultCode(ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS);
                            resMsg.setInput("");
                            break;
                        default:
                            resMsg.setResultCode(ResultCode.OK);
                            resMsg.setInput(input);
                            break;
                    }
                }
                break;
            case 13:
                BindEsimLog(this, "RES_ID_CONFIRM");
                boolean confirmed = args.getBoolean(CONFIRMATION);
                if (this.mCurrentCmd.getCmdType() == AppInterface.CommandType.DISPLAY_TEXT) {
                    if (confirmed) {
                        resMsg.setResultCode(ResultCode.OK);
                    } else {
                        resMsg.setResultCode(ResultCode.UICC_SESSION_TERM_BY_USER);
                    }
                }
                break;
            case 22:
                BindEsimLog(this, "RES_ID_END_SESSION");
                resMsg.setResultCode(ResultCode.UICC_SESSION_TERM_BY_USER);
                break;
            case 25:
                if (this.mCurrentCmd.getCmdType().value() == AppInterface.CommandType.GET_INPUT.value()) {
                    resMsg.setResultCode(ResultCode.TERMINAL_CRNTLY_UNABLE_TO_PROCESS);
                }
                break;
            default:
                BindEsimLog(this, "Unknown result id");
                return;
        }
        if (this.mCurrentCmd != null && this.mCurrentCmd.getCmdType() != null) {
            BindEsimLog(this, "handleCmdResponse- cmdName[" + this.mCurrentCmd.getCmdType().name() + "]");
        }
        handleCmdResponse(resMsg);
    }

    public synchronized void onCmdResponse(CatResponseMessage resMsg) {
        if (resMsg == null) {
            return;
        }
        Message msg = this.mBindEsimServiceHandler.obtainMessage(6, resMsg);
        msg.sendToTarget();
    }

    public void dispose() {
    }

    private void handleSessionEnd() {
        BindEsimLog(this, "SESSION END on " + this.mSlotId);
        Intent intent = new Intent("com.android.internal.stk.session_end");
        intent.putExtra("SLOT_ID", this.mSlotId);
        intent.setComponent(AppInterface.getDefaultSTKApplication());
        intent.addFlags(268435456);
        this.mContext.sendBroadcast(intent, "android.permission.RECEIVE_STK_COMMANDS");
    }

    private void sendTerminalResponse(CommandDetails cmdDet, ResultCode resultCode, boolean includeAdditionalInfo, int additionalInfo, ResponseData resp) {
        if (cmdDet == null) {
            return;
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        Input cmdInput = null;
        if (this.mCurrentCmd != null) {
            cmdInput = this.mCurrentCmd.geInput();
        }
        int tag = ComprehensionTlvTag.COMMAND_DETAILS.value();
        if (cmdDet.compRequired) {
            tag |= DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY;
        }
        buf.write(tag);
        buf.write(3);
        buf.write(cmdDet.commandNumber);
        buf.write(cmdDet.typeOfCommand);
        buf.write(cmdDet.commandQualifier);
        int tag2 = ComprehensionTlvTag.DEVICE_IDENTITIES.value();
        if (cmdDet.typeOfCommand == AppInterface.CommandType.GET_INPUT.value() && this.mCurrentInputCmdDeviceIDs != null) {
            BindEsimLog(this, "Device ID response for esim binding flow");
            buf.write(tag2 | DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
            buf.write(2);
            buf.write(this.mCurrentInputCmdDeviceIDs.destinationId);
        } else {
            buf.write(tag2);
            buf.write(2);
            buf.write(130);
        }
        buf.write(DEV_ID_UICC);
        int tag3 = ComprehensionTlvTag.RESULT.value();
        if (cmdDet.compRequired) {
            tag3 |= DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY;
        }
        buf.write(tag3);
        int i = 0;
        if (cmdDet.typeOfCommand == AppInterface.CommandType.GET_INPUT.value() && this.mCurrentInputCmdDeviceIDs != null && (resp instanceof GetInkeyInputResponseData)) {
            if (!TextUtils.isEmpty(((GetInkeyInputResponseData) resp).mInData)) {
                byte[] data = IccUtils.hexStringToBytes(((GetInkeyInputResponseData) resp).mInData);
                int length = data.length;
                buf.write(length + 1);
                BindEsimLog(this, "write resultCode.value() = " + resultCode.value());
                buf.write(resultCode.value());
                int length2 = data.length;
                while (i < length2) {
                    byte b = data[i];
                    buf.write(b);
                    i++;
                }
            } else {
                buf.write(1);
                BindEsimLog(this, "without user input, write only resultCode.value() = " + resultCode.value());
                buf.write(resultCode.value());
            }
        } else {
            int length3 = includeAdditionalInfo ? 2 : 1;
            buf.write(length3);
            buf.write(resultCode.value());
            if (includeAdditionalInfo) {
                buf.write(additionalInfo);
            }
            if (resp != null) {
                if (this.mInputCtlvsWithSpecialText != null && (resp instanceof GetInkeyInputResponseData)) {
                    if (!TextUtils.isEmpty(((GetInkeyInputResponseData) resp).mInData)) {
                        byte[] data2 = IccUtils.hexStringToBytes(((GetInkeyInputResponseData) resp).mInData);
                        buf.write(ComprehensionTlvTag.TEXT_STRING.value() | DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY);
                        if (data2.length + 1 <= 255) {
                            ResponseData.writeLength(buf, data2.length + 1);
                        } else {
                            data2 = new byte[0];
                        }
                        buf.write(8);
                        BindEsimLog(this, "write SpecialText ResponseData ");
                        int length4 = data2.length;
                        while (i < length4) {
                            byte b2 = data2[i];
                            buf.write(b2);
                            i++;
                        }
                    }
                } else {
                    resp.format(buf);
                }
            } else {
                encodeOptionalTags(cmdDet, resultCode, cmdInput, buf);
            }
        }
        byte[] rawData = buf.toByteArray();
        String hexString = IccUtils.bytesToHexString(rawData);
        BindEsimLog(this, "TERMINAL RESPONSE: " + hexString);
        this.mCmdIf.sendTerminalResponse(hexString, (Message) null);
    }

    private void encodeOptionalTags(CommandDetails cmdDet, ResultCode resultCode, Input cmdInput, ByteArrayOutputStream buf) {
        AppInterface.CommandType cmdType = AppInterface.CommandType.fromInt(cmdDet.typeOfCommand);
        if (cmdType != null) {
            switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[cmdType.ordinal()]) {
                case 1:
                case 2:
                    if (resultCode.value() == ResultCode.NO_RESPONSE_FROM_USER.value() && cmdInput != null && cmdInput.duration != null) {
                        getInKeyResponse(buf, cmdInput);
                        break;
                    }
                    break;
                default:
                    BindEsimLog(this, "encodeOptionalTags() Unsupported Cmd details=" + cmdDet);
                    break;
            }
        }
        BindEsimLog(this, "encodeOptionalTags() bad Cmd details=" + cmdDet);
    }

    private void getInKeyResponse(ByteArrayOutputStream buf, Input cmdInput) {
        int tag = ComprehensionTlvTag.DURATION.value();
        buf.write(tag);
        buf.write(2);
        Duration.TimeUnit timeUnit = cmdInput.duration.timeUnit;
        buf.write(Duration.TimeUnit.SECOND.value());
        buf.write(cmdInput.duration.timeInterval);
    }

    public String getOriginalText(List<ComprehensionTlv> ctlvs) {
        return getOriginalDataForTag(ctlvs, ComprehensionTlvTag.TEXT_STRING, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getOriginalDataForTag(List<ComprehensionTlv> ctlvs, ComprehensionTlvTag tag, boolean withScheme) {
        ComprehensionTlv ctlvTextString;
        String text = null;
        if (ctlvs != null && (ctlvTextString = searchForTag(tag, ctlvs)) != null) {
            byte[] rawValue = ctlvTextString.getRawValue();
            int valueIndex = ctlvTextString.getValueIndex();
            int textLen = ctlvTextString.getLength();
            byte[] pdu = null;
            if (withScheme && valueIndex >= 0 && valueIndex + textLen <= rawValue.length) {
                pdu = Arrays.copyOfRange(rawValue, valueIndex, valueIndex + textLen);
            } else if (valueIndex + 1 >= 0 && valueIndex + textLen <= rawValue.length) {
                pdu = Arrays.copyOfRange(rawValue, valueIndex + 1, valueIndex + textLen);
            }
            text = IccUtils.bytesToHexString(pdu);
        }
        BindEsimLog("ctlvTextString data = " + text + " for tag = " + tag.name());
        return text;
    }

    List<ComprehensionTlv> getComprehensionTlvs(String data) {
        byte[] rawData = null;
        try {
            rawData = IccUtils.hexStringToBytes(data);
        } catch (Exception e) {
            BindEsimLog(this, "dropping zombie messages");
        }
        try {
            BerTlv berTlv = BerTlv.decode(rawData);
            List<ComprehensionTlv> ctlvs = berTlv.getComprehensionTlvs();
            return ctlvs;
        } catch (ResultException e2) {
            BindEsimLog(this, "decodeMessageParams: caught ResultException e=" + e2);
            return null;
        }
    }

    public void notifyStkProCmdForEsimBinding(String data) {
        AsyncResult asyncResultData = new AsyncResult((Object) null, data, (Throwable) null);
        Message msg = this.mBindEsimServiceHandler.obtainMessage(2, asyncResultData);
        msg.sendToTarget();
    }

    public static boolean isNeedHandedByBindEsimService(String data) {
        boolean isNeedHandedByBindEsimService = false;
        try {
            byte[] rawData = IccUtils.hexStringToBytes(data);
            try {
                BerTlv berTlv = BerTlv.decode(rawData);
                List<ComprehensionTlv> ctlvs = berTlv.getComprehensionTlvs();
                DeviceIdentities cmdDeviceIDs = processCommandDeviceIDs(ctlvs);
                String textString = getOriginalDataForTag(ctlvs, ComprehensionTlvTag.TEXT_STRING, false);
                String defaultText = getOriginalDataForTag(ctlvs, ComprehensionTlvTag.DEFAULT_TEXT, false);
                if (cmdDeviceIDs.destinationId == DEV_ID_ESIMBIND || cmdDeviceIDs.destinationId == DEV_ID_ESIMPAIR || cmdDeviceIDs.destinationId == DEV_ID_ESIMRESULT || SPECIAL_TEXTSTRING.equals(textString) || SPECIAL_TEXTSTRING2.equals(textString) || SPECIAL_INPUT_DEFAULT_TEXT.equals(defaultText)) {
                    isNeedHandedByBindEsimService = true;
                } else {
                    BindEsimLog("no need handled by esim service, cmdDeviceIDs: " + cmdDeviceIDs + " ,textString: " + textString);
                }
            } catch (Exception e) {
                BindEsimLog("decodeMessageParams: caught ResultException e=" + e);
            }
            BindEsimLog("isNeedHandedByBindEsimService = " + isNeedHandedByBindEsimService);
            return isNeedHandedByBindEsimService;
        } catch (Exception e2) {
            BindEsimLog("dropping zombie messages");
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static DeviceIdentities processCommandDeviceIDs(List<ComprehensionTlv> ctlvs) {
        ComprehensionTlv ctlvDeviceId;
        if (ctlvs == null || (ctlvDeviceId = searchForTag(ComprehensionTlvTag.DEVICE_IDENTITIES, ctlvs)) == null) {
            return null;
        }
        try {
            DeviceIdentities cmdDeviceIDs = ValueParser.retrieveDeviceIdentities(ctlvDeviceId);
            return cmdDeviceIDs;
        } catch (ResultException e) {
            BindEsimLog("processCommandDetails: Failed to procees command details e=" + e);
            return null;
        }
    }

    private static ComprehensionTlv searchForTag(ComprehensionTlvTag tag, List<ComprehensionTlv> ctlvs) {
        Iterator<ComprehensionTlv> iter = ctlvs.iterator();
        return searchForNextTag(tag, iter);
    }

    private static ComprehensionTlv searchForNextTag(ComprehensionTlvTag tag, Iterator<ComprehensionTlv> iter) {
        while (iter != null && iter.hasNext()) {
            ComprehensionTlv ctlv = iter.next();
            if (tag != null && ctlv.getTag() == tag.value()) {
                return ctlv;
            }
        }
        return null;
    }

    void updateIccAvailability() {
        if (this.mUiccController == null) {
            return;
        }
        IccCardStatus.CardState newState = IccCardStatus.CardState.CARDSTATE_ABSENT;
        UiccCard newCard = this.mUiccController.getUiccCard(this.mSlotId);
        UiccSlot slot = this.mUiccController.getUiccSlot(this.mSlotId);
        boolean isEuicc = false;
        if (newCard != null) {
            newState = newCard.getCardState();
        }
        if (slot != null) {
            isEuicc = slot.isEuicc();
        }
        int numApplications = 0;
        if (this.mUiccController.getUiccProfileForPhone(this.mSlotId) != null) {
            numApplications = this.mUiccController.getUiccProfileForPhone(this.mSlotId).getNumApplications();
        }
        IccCardStatus.CardState oldState = this.mCardState;
        this.mCardState = newState;
        BindEsimLog(this, "New Card State = " + newState + " Old Card State = " + oldState + " numApplications = " + numApplications + " isEuicc = " + isEuicc);
        if (oldState != IccCardStatus.CardState.CARDSTATE_PRESENT && newState == IccCardStatus.CardState.CARDSTATE_PRESENT && numApplications == 0 && isEuicc) {
            this.mCmdIf.reportStkServiceIsRunning((Message) null);
        }
    }

    private String processBindingData(List<String> arguments) {
        if (MiuiTelephony.IS_XRING) {
            return ExtTelephonyManager.getInstance(this.mContext).processBindingData(arguments);
        }
        return MiuiTelephonyManagerStub.get().processBindingData(arguments);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetCmd() {
        this.mCurrentInputCtlvs = null;
        this.mInputCtlvsWithSpecialText = null;
        this.mCurrentInputCmdDeviceIDs = null;
        this.mCurrentDisplayTextCtlvs = null;
    }

    public static void BindEsimLog(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    public void BindEsimLog(Object caller, String msg) {
        String className = caller.getClass().getName();
        Rlog.d(LOG_TAG, "[" + this.mSlotId + "] " + className.substring(className.lastIndexOf(46) + 1) + ": " + msg);
    }
}
