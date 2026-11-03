package com.android.internal.telephony.cat;

import android.R;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.telephony.AnomalyReporter;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class CommandParamsFactory extends Handler {
    public static final int DTTZ_SETTING = 3;
    public static final int LANGUAGE_SETTING = 4;
    protected static final int LOAD_MULTI_ICONS = 2;
    protected static final int LOAD_NO_ICON = 0;
    protected static final int LOAD_SINGLE_ICON = 1;
    protected static final int MAX_GSM7_DEFAULT_CHARS = 239;
    protected static final int MAX_UCS2_CHARS = 118;
    protected static final int MSG_ID_LOAD_ICON_DONE = 1;
    public static final String NPE_WHEN_CALLED_SEND_CMD_PARAMS_ERROR_MSG = "mCaller[RilMessageDecoder] is Null when called SendCmdParams";
    public static final UUID NPE_WHEN_CALLED_SEND_CMD_PARAMS_UUID = UUID.fromString("c2b85688-516e-11ee-be56-0242ac120002");
    protected static CommandParamsFactory sInstance;
    protected RilMessageDecoder mCaller;
    private Context mContext;
    protected IconLoader mIconLoader;
    protected boolean mNoAlphaUsrCnf;
    private String mRequestedLanguage;
    private String mSavedLanguage;
    private boolean mStkSmsSendViaTelephony;
    private boolean mSupportSendUssd;
    protected CommandParams mCmdParams = null;
    protected int mIconLoadState = 0;
    protected boolean mloadIcon = false;

    protected boolean handleBatteryStateInfo(CommandDetails commandDetails) {
        return false;
    }

    protected void onSetNextActionIndicator(List<ComprehensionTlv> list, Menu menu) {
    }

    public static synchronized CommandParamsFactory getInstance(RilMessageDecoder rilMessageDecoder, IccFileHandler iccFileHandler, Context context) {
        CommandParamsFactory commandParamsFactory = sInstance;
        if (commandParamsFactory != null) {
            return commandParamsFactory;
        }
        if (iccFileHandler == null) {
            return null;
        }
        return TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeCommandParamsFactory(rilMessageDecoder, iccFileHandler, context);
    }

    public CommandParamsFactory(RilMessageDecoder rilMessageDecoder, IccFileHandler iccFileHandler, Context context) {
        this.mNoAlphaUsrCnf = false;
        this.mStkSmsSendViaTelephony = false;
        this.mSupportSendUssd = false;
        this.mCaller = rilMessageDecoder;
        this.mContext = context;
        this.mIconLoader = IconLoader.getInstance(this, iccFileHandler);
        try {
            this.mNoAlphaUsrCnf = context.getResources().getBoolean(R.bool.config_use_sim_language_file);
        } catch (Resources.NotFoundException unused) {
            this.mNoAlphaUsrCnf = false;
        }
        try {
            this.mStkSmsSendViaTelephony = context.getResources().getBoolean(R.bool.config_use_strict_phone_number_comparation_for_russia);
        } catch (Resources.NotFoundException unused2) {
            this.mStkSmsSendViaTelephony = false;
        }
        try {
            this.mSupportSendUssd = this.mContext.getResources().getBoolean(R.bool.config_use_strict_phone_number_comparation);
        } catch (Resources.NotFoundException unused3) {
            this.mSupportSendUssd = false;
        }
    }

    private CommandDetails processCommandDetails(List list) {
        ComprehensionTlv comprehensionTlvSearchForTag;
        if (list == null || (comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.COMMAND_DETAILS, list)) == null) {
            return null;
        }
        try {
            return ValueParser.retrieveCommandDetails(comprehensionTlvSearchForTag);
        } catch (ResultException e) {
            CatLog.d(this, "processCommandDetails: Failed to procees command details e=" + e);
            return null;
        }
    }

    public void make(BerTlv berTlv) {
        if (berTlv == null) {
            return;
        }
        this.mCmdParams = null;
        boolean zProcessSelectItem = false;
        this.mIconLoadState = 0;
        if (berTlv.getTag() != 208) {
            sendCmdParams(ResultCode.CMD_TYPE_NOT_UNDERSTOOD);
            return;
        }
        List<ComprehensionTlv> comprehensionTlvs = berTlv.getComprehensionTlvs();
        CommandDetails commandDetailsProcessCommandDetails = processCommandDetails(comprehensionTlvs);
        if (commandDetailsProcessCommandDetails == null) {
            sendCmdParams(ResultCode.CMD_TYPE_NOT_UNDERSTOOD);
            return;
        }
        AppInterface.CommandType commandTypeFromInt = AppInterface.CommandType.fromInt(commandDetailsProcessCommandDetails.typeOfCommand);
        if (commandTypeFromInt == null) {
            this.mCmdParams = new CommandParams(commandDetailsProcessCommandDetails);
            sendCmdParams(ResultCode.BEYOND_TERMINAL_CAPABILITY);
            return;
        }
        if (!berTlv.isLengthValid()) {
            this.mCmdParams = new CommandParams(commandDetailsProcessCommandDetails);
            sendCmdParams(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            return;
        }
        try {
            switch (AnonymousClass1.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[commandTypeFromInt.ordinal()]) {
                case 1:
                    zProcessSelectItem = processSelectItem(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 2:
                    zProcessSelectItem = processSelectItem(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 3:
                    zProcessSelectItem = processDisplayText(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 4:
                    zProcessSelectItem = processSetUpIdleModeText(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 5:
                    zProcessSelectItem = processGetInkey(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 6:
                    zProcessSelectItem = processGetInput(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 7:
                    if (this.mStkSmsSendViaTelephony) {
                        zProcessSelectItem = processSMSEventNotify(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    } else {
                        zProcessSelectItem = processEventNotify(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    }
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    zProcessSelectItem = processEventNotify(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 12:
                    zProcessSelectItem = processEventNotify(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 13:
                case 14:
                    zProcessSelectItem = processSetupCall(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 15:
                    zProcessSelectItem = processLaunchBrowser(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 16:
                    zProcessSelectItem = processPlayTone(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 17:
                    zProcessSelectItem = processSetUpEventList(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 18:
                    zProcessSelectItem = processProvideLocalInfo(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 19:
                    zProcessSelectItem = processLanguageNotification(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 20:
                case 21:
                case 22:
                case 23:
                    zProcessSelectItem = processBIPClient(commandDetailsProcessCommandDetails, comprehensionTlvs);
                    break;
                case 24:
                    break;
                default:
                    this.mCmdParams = new CommandParams(commandDetailsProcessCommandDetails);
                    sendCmdParams(ResultCode.BEYOND_TERMINAL_CAPABILITY);
                    return;
            }
            if (zProcessSelectItem) {
                return;
            }
            sendCmdParams(ResultCode.OK);
        } catch (ResultException e) {
            CatLog.d(this, "make: caught ResultException e=" + e);
            this.mCmdParams = new CommandParams(commandDetailsProcessCommandDetails);
            sendCmdParams(e.result());
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.cat.CommandParamsFactory$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType;

        static {
            int[] iArr = new int[AppInterface.CommandType.values().length];
            $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType = iArr;
            try {
                iArr[AppInterface.CommandType.SET_UP_MENU.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SELECT_ITEM.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.DISPLAY_TEXT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_IDLE_MODE_TEXT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INKEY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INPUT.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_SMS.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DTMF.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.REFRESH.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RUN_AT.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_SS.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_USSD.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_CHANNEL_STATUS.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_CALL.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.LAUNCH_BROWSER.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PLAY_TONE.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_EVENT_LIST.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PROVIDE_LOCAL_INFORMATION.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.LANGUAGE_NOTIFICATION.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.OPEN_CHANNEL.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.CLOSE_CHANNEL.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RECEIVE_DATA.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DATA.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GEOGRAPHICAL_LOCATION_REQUEST.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
        }
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        if (message.what == 1 && this.mIconLoader != null) {
            sendCmdParams(setIcons(message.obj));
        }
    }

    protected ResultCode setIcons(Object obj) {
        if (obj == null) {
            CatLog.d(this, "Optional Icon data is NULL");
            this.mCmdParams.mLoadIconFailed = true;
            this.mloadIcon = false;
            return ResultCode.OK;
        }
        int i = this.mIconLoadState;
        if (i == 1) {
            this.mCmdParams.setIcon((Bitmap) obj);
        } else if (i == 2) {
            for (Bitmap bitmap : (Bitmap[]) obj) {
                this.mCmdParams.setIcon(bitmap);
                if (bitmap == null && this.mloadIcon) {
                    CatLog.d(this, "Optional Icon data is NULL while loading multi icons");
                    this.mCmdParams.mLoadIconFailed = true;
                }
            }
        }
        return ResultCode.OK;
    }

    private void sendCmdParams(ResultCode resultCode) {
        RilMessageDecoder rilMessageDecoder = this.mCaller;
        if (rilMessageDecoder != null) {
            rilMessageDecoder.sendMsgParamsDecoded(resultCode, this.mCmdParams);
        } else {
            CatLog.e(this, "mCaller[RilMessageDecoder] is NULL");
            AnomalyReporter.reportAnomaly(NPE_WHEN_CALLED_SEND_CMD_PARAMS_UUID, NPE_WHEN_CALLED_SEND_CMD_PARAMS_ERROR_MSG);
        }
    }

    protected ComprehensionTlv searchForTag(ComprehensionTlvTag comprehensionTlvTag, List<ComprehensionTlv> list) {
        return searchForNextTag(comprehensionTlvTag, list.iterator());
    }

    protected ComprehensionTlv searchForNextTag(ComprehensionTlvTag comprehensionTlvTag, Iterator<ComprehensionTlv> it) {
        int iValue = comprehensionTlvTag.value();
        while (it.hasNext()) {
            ComprehensionTlv next = it.next();
            if (next.getTag() == iValue) {
                return next;
            }
        }
        return null;
    }

    private boolean processDisplayText(CommandDetails commandDetails, List list) throws ResultException {
        CatLog.d(this, "process DisplayText");
        TextMessage textMessage = new TextMessage();
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.TEXT_STRING, list);
        if (comprehensionTlvSearchForTag != null) {
            textMessage.text = ValueParser.retrieveTextString(comprehensionTlvSearchForTag);
        }
        if (textMessage.text == null) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
        if (searchForTag(ComprehensionTlvTag.IMMEDIATE_RESPONSE, list) != null) {
            textMessage.responseNeeded = false;
        }
        ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.ICON_ID, list);
        IconId iconIdRetrieveIconId = null;
        if (comprehensionTlvSearchForTag2 != null) {
            try {
                iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag2);
            } catch (ResultException e) {
                CatLog.e(this, "retrieveIconId ResultException: " + e.result());
            }
            try {
                textMessage.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
            } catch (NullPointerException unused) {
                CatLog.e(this, "iconId is null.");
            }
        }
        ComprehensionTlv comprehensionTlvSearchForTag3 = searchForTag(ComprehensionTlvTag.DURATION, list);
        if (comprehensionTlvSearchForTag3 != null) {
            try {
                textMessage.duration = ValueParser.retrieveDuration(comprehensionTlvSearchForTag3);
            } catch (ResultException e2) {
                CatLog.e(this, "retrieveDuration ResultException: " + e2.result());
            }
        }
        ComprehensionTlv comprehensionTlvSearchForTag4 = searchForTag(ComprehensionTlvTag.DEVICE_IDENTITIES, list);
        if (comprehensionTlvSearchForTag4 != null) {
            try {
                textMessage.deviceIdentities = ValueParser.retrieveDeviceIdentities(comprehensionTlvSearchForTag4);
            } catch (ResultException e3) {
                CatLog.e(this, "retrieveDeviceIdentities ResultException: " + e3.result());
            }
        }
        int i = commandDetails.commandQualifier;
        textMessage.isHighPriority = (i & 1) != 0;
        textMessage.userClear = (i & 128) != 0;
        this.mCmdParams = new DisplayTextParams(commandDetails, textMessage);
        if (iconIdRetrieveIconId == null) {
            return false;
        }
        this.mloadIcon = true;
        this.mIconLoadState = 1;
        this.mIconLoader.loadIcon(iconIdRetrieveIconId.recordNumber, obtainMessage(1));
        return true;
    }

    private boolean processSetUpIdleModeText(CommandDetails commandDetails, List list) throws ResultException {
        IconId iconIdRetrieveIconId;
        CatLog.d(this, "process SetUpIdleModeText");
        TextMessage textMessage = new TextMessage();
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.TEXT_STRING, list);
        if (comprehensionTlvSearchForTag != null) {
            textMessage.text = ValueParser.retrieveTextString(comprehensionTlvSearchForTag);
        }
        ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.ICON_ID, list);
        if (comprehensionTlvSearchForTag2 != null) {
            iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag2);
            textMessage.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
        } else {
            iconIdRetrieveIconId = null;
        }
        if (textMessage.text == null && iconIdRetrieveIconId != null && !textMessage.iconSelfExplanatory) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
        this.mCmdParams = new DisplayTextParams(commandDetails, textMessage);
        if (iconIdRetrieveIconId == null) {
            return false;
        }
        this.mloadIcon = true;
        this.mIconLoadState = 1;
        this.mIconLoader.loadIcon(iconIdRetrieveIconId.recordNumber, obtainMessage(1));
        return true;
    }

    private boolean processGetInkey(CommandDetails commandDetails, List list) throws ResultException {
        CatLog.d(this, "process GetInkey");
        Input input = new Input();
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.TEXT_STRING, list);
        if (comprehensionTlvSearchForTag != null) {
            input.text = ValueParser.retrieveTextString(comprehensionTlvSearchForTag);
            ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.ICON_ID, list);
            IconId iconIdRetrieveIconId = null;
            if (comprehensionTlvSearchForTag2 != null) {
                try {
                    iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag2);
                } catch (ResultException e) {
                    CatLog.e(this, "retrieveIconId ResultException: " + e.result());
                }
                try {
                    input.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
                } catch (NullPointerException unused) {
                    CatLog.e(this, "iconId is null.");
                }
            }
            ComprehensionTlv comprehensionTlvSearchForTag3 = searchForTag(ComprehensionTlvTag.DURATION, list);
            if (comprehensionTlvSearchForTag3 != null) {
                try {
                    input.duration = ValueParser.retrieveDuration(comprehensionTlvSearchForTag3);
                } catch (ResultException e2) {
                    CatLog.e(this, "retrieveDuration ResultException: " + e2.result());
                }
            }
            input.minLen = 1;
            input.maxLen = 1;
            int i = commandDetails.commandQualifier;
            input.digitOnly = (i & 1) == 0;
            input.ucs2 = (i & 2) != 0;
            input.yesNo = (i & 4) != 0;
            input.helpAvailable = (i & 128) != 0;
            input.echo = true;
            this.mCmdParams = new GetInputParams(commandDetails, input);
            if (iconIdRetrieveIconId == null) {
                return false;
            }
            this.mloadIcon = true;
            this.mIconLoadState = 1;
            this.mIconLoader.loadIcon(iconIdRetrieveIconId.recordNumber, obtainMessage(1));
            return true;
        }
        throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
    }

    private boolean processGetInput(CommandDetails commandDetails, List list) throws ResultException {
        CatLog.d(this, "process GetInput");
        Input input = new Input();
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.TEXT_STRING, list);
        if (comprehensionTlvSearchForTag != null) {
            input.text = ValueParser.retrieveTextString(comprehensionTlvSearchForTag);
            ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.RESPONSE_LENGTH, list);
            if (comprehensionTlvSearchForTag2 != null) {
                try {
                    byte[] rawValue = comprehensionTlvSearchForTag2.getRawValue();
                    int valueIndex = comprehensionTlvSearchForTag2.getValueIndex();
                    int i = rawValue[valueIndex] & 255;
                    input.minLen = i;
                    int i2 = rawValue[valueIndex + 1] & 255;
                    input.maxLen = i2;
                    if (i > 239) {
                        input.minLen = 239;
                    }
                    if (i2 > 239) {
                        input.maxLen = 239;
                    }
                    ComprehensionTlv comprehensionTlvSearchForTag3 = searchForTag(ComprehensionTlvTag.DEFAULT_TEXT, list);
                    if (comprehensionTlvSearchForTag3 != null) {
                        try {
                            input.defaultText = ValueParser.retrieveTextString(comprehensionTlvSearchForTag3);
                        } catch (ResultException e) {
                            CatLog.e(this, "retrieveTextString ResultException: " + e.result());
                        }
                    }
                    ComprehensionTlv comprehensionTlvSearchForTag4 = searchForTag(ComprehensionTlvTag.ICON_ID, list);
                    IconId iconIdRetrieveIconId = null;
                    if (comprehensionTlvSearchForTag4 != null) {
                        try {
                            iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag4);
                        } catch (ResultException e2) {
                            CatLog.e(this, "retrieveIconId ResultException: " + e2.result());
                        }
                        try {
                            input.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
                        } catch (NullPointerException unused) {
                            CatLog.e(this, "iconId is null.");
                        }
                    }
                    ComprehensionTlv comprehensionTlvSearchForTag5 = searchForTag(ComprehensionTlvTag.DURATION, list);
                    if (comprehensionTlvSearchForTag5 != null) {
                        try {
                            input.duration = ValueParser.retrieveDuration(comprehensionTlvSearchForTag5);
                        } catch (ResultException e3) {
                            CatLog.e(this, "retrieveDuration ResultException: " + e3.result());
                        }
                    }
                    int i3 = commandDetails.commandQualifier;
                    input.digitOnly = (i3 & 1) == 0;
                    boolean z = (i3 & 2) != 0;
                    input.ucs2 = z;
                    input.echo = (i3 & 4) == 0;
                    boolean z2 = (i3 & 8) != 0;
                    input.packed = z2;
                    input.helpAvailable = (i3 & 128) != 0;
                    if (z && input.maxLen > 118) {
                        CatLog.d(this, "UCS2: received maxLen = " + input.maxLen + ", truncating to 118");
                        input.maxLen = 118;
                    } else if (!z2 && input.maxLen > 239) {
                        CatLog.d(this, "GSM 7Bit Default: received maxLen = " + input.maxLen + ", truncating to 239");
                        input.maxLen = 239;
                    }
                    this.mCmdParams = new GetInputParams(commandDetails, input);
                    if (iconIdRetrieveIconId == null) {
                        return false;
                    }
                    this.mloadIcon = true;
                    this.mIconLoadState = 1;
                    this.mIconLoader.loadIcon(iconIdRetrieveIconId.recordNumber, obtainMessage(1));
                    return true;
                } catch (IndexOutOfBoundsException unused2) {
                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
                }
            }
            throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
        }
        throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
    }

    private boolean processSelectItem(CommandDetails commandDetails, List list) throws ResultException {
        IconId iconIdRetrieveIconId;
        int i;
        CatLog.d(this, "process SelectItem");
        Menu menuOnCreatMenu = onCreatMenu();
        Iterator<ComprehensionTlv> it = list.iterator();
        AppInterface.CommandType commandTypeFromInt = AppInterface.CommandType.fromInt(commandDetails.typeOfCommand);
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.ALPHA_ID, list);
        if (comprehensionTlvSearchForTag != null) {
            menuOnCreatMenu.title = onGetAlphaId(comprehensionTlvSearchForTag);
        } else if (commandTypeFromInt == AppInterface.CommandType.SET_UP_MENU) {
            throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
        }
        while (true) {
            ComprehensionTlv comprehensionTlvSearchForNextTag = searchForNextTag(ComprehensionTlvTag.ITEM, it);
            if (comprehensionTlvSearchForNextTag == null) {
                break;
            }
            menuOnCreatMenu.items.add(ValueParser.retrieveItem(comprehensionTlvSearchForNextTag));
        }
        if (menuOnCreatMenu.items.size() == 0) {
            throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
        }
        onSetNextActionIndicator(list, menuOnCreatMenu);
        ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.ITEM_ID, list);
        if (comprehensionTlvSearchForTag2 != null) {
            try {
                menuOnCreatMenu.defaultItem = ValueParser.retrieveItemId(comprehensionTlvSearchForTag2) - 1;
            } catch (ResultException e) {
                CatLog.e(this, "retrieveItemId ResultException: " + e.result());
            }
        }
        ComprehensionTlv comprehensionTlvSearchForTag3 = searchForTag(ComprehensionTlvTag.ICON_ID, list);
        ItemsIconId itemsIconIdRetrieveItemsIconId = null;
        if (comprehensionTlvSearchForTag3 != null) {
            this.mIconLoadState = 1;
            try {
                iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag3);
            } catch (ResultException e2) {
                CatLog.e(this, "retrieveIconId ResultException: " + e2.result());
                iconIdRetrieveIconId = null;
            }
            try {
                menuOnCreatMenu.titleIconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
            } catch (NullPointerException unused) {
                CatLog.e(this, "titleIconId is null.");
            }
        } else {
            iconIdRetrieveIconId = null;
        }
        ComprehensionTlv comprehensionTlvSearchForTag4 = searchForTag(ComprehensionTlvTag.ITEM_ICON_ID_LIST, list);
        if (comprehensionTlvSearchForTag4 != null) {
            this.mIconLoadState = 2;
            itemsIconIdRetrieveItemsIconId = ValueParser.retrieveItemsIconId(comprehensionTlvSearchForTag4);
            menuOnCreatMenu.itemsIconSelfExplanatory = itemsIconIdRetrieveItemsIconId.selfExplanatory;
        }
        int i2 = commandDetails.commandQualifier;
        if ((i2 & 1) != 0) {
            if ((i2 & 2) == 0) {
                menuOnCreatMenu.presentationType = PresentationType.DATA_VALUES;
            } else {
                menuOnCreatMenu.presentationType = PresentationType.NAVIGATION_OPTIONS;
            }
        }
        menuOnCreatMenu.softKeyPreferred = (i2 & 4) != 0;
        menuOnCreatMenu.helpAvailable = (i2 & 128) != 0;
        this.mCmdParams = new SelectItemParams(commandDetails, menuOnCreatMenu, iconIdRetrieveIconId != null);
        int i3 = this.mIconLoadState;
        if (i3 != 0) {
            if (i3 != 1) {
                if (i3 == 2) {
                    if (itemsIconIdRetrieveItemsIconId == null) {
                        return false;
                    }
                    int[] iArr = itemsIconIdRetrieveItemsIconId.recordNumbers;
                    if (iconIdRetrieveIconId != null) {
                        int[] iArr2 = new int[iArr.length + 1];
                        iArr2[0] = iconIdRetrieveIconId.recordNumber;
                        System.arraycopy(iArr, 0, iArr2, 1, iArr.length);
                        iArr = iArr2;
                    }
                    this.mloadIcon = true;
                    this.mIconLoader.loadIcons(iArr, obtainMessage(1));
                }
            } else if (iconIdRetrieveIconId != null && (i = iconIdRetrieveIconId.recordNumber) > 0) {
                this.mloadIcon = true;
                this.mIconLoader.loadIcon(i, obtainMessage(1));
            }
            return true;
        }
        return false;
    }

    private boolean processEventNotify(CommandDetails commandDetails, List list) throws ResultException {
        IconId iconIdRetrieveIconId;
        CatLog.d(this, "process EventNotify");
        TextMessage textMessage = new TextMessage();
        textMessage.text = onGetAlphaId(searchForTag(ComprehensionTlvTag.ALPHA_ID, list));
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.ICON_ID, list);
        if (comprehensionTlvSearchForTag != null) {
            iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag);
            textMessage.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
        } else {
            iconIdRetrieveIconId = null;
        }
        textMessage.responseNeeded = false;
        this.mCmdParams = new DisplayTextParams(commandDetails, textMessage);
        if (iconIdRetrieveIconId == null) {
            return false;
        }
        this.mloadIcon = true;
        this.mIconLoadState = 1;
        this.mIconLoader.loadIcon(iconIdRetrieveIconId.recordNumber, obtainMessage(1));
        return true;
    }

    public boolean processSendUssd(CommandDetails commandDetails, List<ComprehensionTlv> list) throws ResultException {
        CatLog.d(this, "process SendUssd");
        TextMessage textMessage = new TextMessage();
        SequentialParser sequentialParser = new SequentialParser(list.iterator());
        sequentialParser.parseMandatory(ComprehensionTlvTag.COMMAND_DETAILS, null);
        sequentialParser.parseMandatory(ComprehensionTlvTag.DEVICE_IDENTITIES, null);
        textMessage.text = (String) sequentialParser.parseOptional(ComprehensionTlvTag.ALPHA_ID, new SequentialParser.ParseFunction() { // from class: com.android.internal.telephony.cat.CommandParamsFactory$$ExternalSyntheticLambda0
            @Override // com.android.internal.telephony.cat.SequentialParser.ParseFunction
            public final Object apply(ComprehensionTlv comprehensionTlv) {
                return this.f$0.lambda$processSendUssd$0(comprehensionTlv);
            }
        }, null);
        ComprehensionTlvTag comprehensionTlvTag = ComprehensionTlvTag.USSD_STRING;
        byte bByteValue = ((Byte) sequentialParser.parseMandatory(comprehensionTlvTag, new SequentialParser.ParseFunction() { // from class: com.android.internal.telephony.cat.CommandParamsFactory$$ExternalSyntheticLambda1
            @Override // com.android.internal.telephony.cat.SequentialParser.ParseFunction
            public final Object apply(ComprehensionTlv comprehensionTlv) {
                return Byte.valueOf(ValueParser.retrieveTextCodingScheme(comprehensionTlv));
            }
        }, false)).byteValue();
        String str = (String) sequentialParser.parseMandatory(comprehensionTlvTag, new SequentialParser.ParseFunction() { // from class: com.android.internal.telephony.cat.CommandParamsFactory$$ExternalSyntheticLambda2
            @Override // com.android.internal.telephony.cat.SequentialParser.ParseFunction
            public final Object apply(ComprehensionTlv comprehensionTlv) {
                return ValueParser.retrieveTextString(comprehensionTlv);
            }
        });
        IconId iconId = (IconId) sequentialParser.parseOptional(ComprehensionTlvTag.ICON_ID, new SequentialParser.ParseFunction() { // from class: com.android.internal.telephony.cat.CommandParamsFactory$$ExternalSyntheticLambda3
            @Override // com.android.internal.telephony.cat.SequentialParser.ParseFunction
            public final Object apply(ComprehensionTlv comprehensionTlv) {
                return ValueParser.retrieveIconId(comprehensionTlv);
            }
        }, null);
        if (iconId != null) {
            textMessage.iconSelfExplanatory = iconId.selfExplanatory;
        }
        this.mCmdParams = new SendUssdParams(commandDetails, textMessage, str, bByteValue);
        if (iconId == null) {
            return false;
        }
        this.mloadIcon = true;
        this.mIconLoadState = 1;
        this.mIconLoader.loadIcon(iconId.recordNumber, obtainMessage(1));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ String lambda$processSendUssd$0(ComprehensionTlv comprehensionTlv) {
        return ValueParser.retrieveAlphaId(comprehensionTlv, this.mNoAlphaUsrCnf);
    }

    public boolean processSMSEventNotify(CommandDetails commandDetails, List<ComprehensionTlv> list) throws ResultException {
        CatLog.d(this, "processSMSEventNotify");
        TextMessage textMessage = new TextMessage();
        textMessage.text = ValueParser.retrieveAlphaId(searchForTag(ComprehensionTlvTag.ALPHA_ID, list), this.mNoAlphaUsrCnf);
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.ICON_ID, list);
        if (comprehensionTlvSearchForTag != null) {
            textMessage.iconSelfExplanatory = ValueParser.retrieveIconId(comprehensionTlvSearchForTag).selfExplanatory;
        }
        textMessage.responseNeeded = false;
        DisplayTextParams displayTextParams = new DisplayTextParams(commandDetails, textMessage);
        ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.SMS_TPDU, list);
        ComprehensionTlv comprehensionTlvSearchForTag3 = searchForTag(ComprehensionTlvTag.ADDRESS, list);
        String strRetrieveAddress = comprehensionTlvSearchForTag3 != null ? ValueParser.retrieveAddress(comprehensionTlvSearchForTag3) : null;
        SmsMessage smsMessageRetrieveTpduAsSmsMessage = ValueParser.retrieveTpduAsSmsMessage(comprehensionTlvSearchForTag2);
        if (smsMessageRetrieveTpduAsSmsMessage == null) {
            return true;
        }
        TextMessage textMessage2 = new TextMessage();
        textMessage2.text = smsMessageRetrieveTpduAsSmsMessage.getMessageBody();
        TextMessage textMessage3 = new TextMessage();
        textMessage3.text = smsMessageRetrieveTpduAsSmsMessage.getRecipientAddress();
        byte[] rawValue = comprehensionTlvSearchForTag2.getRawValue();
        int valueIndex = comprehensionTlvSearchForTag2.getValueIndex();
        int length = comprehensionTlvSearchForTag2.getLength();
        byte[] bArr = new byte[length];
        try {
            System.arraycopy(rawValue, valueIndex, bArr, 0, length);
        } catch (IndexOutOfBoundsException e) {
            CatLog.d(this, "processSMSEventNotify: Error copying rawTpdu: " + e);
            bArr = null;
        }
        this.mCmdParams = new SendSMSParams(commandDetails, textMessage2, textMessage3, displayTextParams, strRetrieveAddress, bArr);
        return false;
    }

    private boolean processSetUpEventList(CommandDetails commandDetails, List list) {
        CatLog.d(this, "process SetUpEventList");
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.EVENT_LIST, list);
        if (comprehensionTlvSearchForTag == null) {
            return false;
        }
        try {
            byte[] rawValue = comprehensionTlvSearchForTag.getRawValue();
            int valueIndex = comprehensionTlvSearchForTag.getValueIndex();
            int length = comprehensionTlvSearchForTag.getLength();
            int[] iArr = new int[length];
            onGetEventList(rawValue, valueIndex, length, iArr);
            this.mCmdParams = new SetEventListParams(commandDetails, iArr);
            return false;
        } catch (IndexOutOfBoundsException unused) {
            CatLog.e(this, " IndexOutofBoundException in processSetUpEventList");
            return false;
        }
    }

    /* JADX WARN: Code duplicated, block: B:17:0x0050  */
    private boolean processLaunchBrowser(CommandDetails commandDetails, List list) throws ResultException {
        String strGsm8BitUnpackedToString;
        LaunchBrowserMode launchBrowserMode;
        KeyguardManager keyguardManager = (KeyguardManager) this.mContext.getSystemService(KeyguardManager.class);
        if (keyguardManager != null && keyguardManager.isDeviceLocked()) {
            CatLog.d(this, "The device is locked, cannot launch the Browser");
            throw new ResultException(ResultCode.LAUNCH_BROWSER_ERROR, "The device is locked, unable to process the command.");
        }
        CatLog.d(this, "process LaunchBrowser");
        TextMessage textMessage = new TextMessage();
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.URL, list);
        IconId iconIdRetrieveIconId = null;
        if (comprehensionTlvSearchForTag != null) {
            try {
                byte[] rawValue = comprehensionTlvSearchForTag.getRawValue();
                int valueIndex = comprehensionTlvSearchForTag.getValueIndex();
                int length = comprehensionTlvSearchForTag.getLength();
                if (length > 0) {
                    strGsm8BitUnpackedToString = GsmAlphabet.gsm8BitUnpackedToString(rawValue, valueIndex, length);
                } else {
                    strGsm8BitUnpackedToString = null;
                }
            } catch (IndexOutOfBoundsException unused) {
                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            }
        } else {
            strGsm8BitUnpackedToString = null;
        }
        textMessage.text = onGetAlphaId(searchForTag(ComprehensionTlvTag.ALPHA_ID, list));
        ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.ICON_ID, list);
        if (comprehensionTlvSearchForTag2 != null) {
            iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag2);
            textMessage.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
        }
        int i = commandDetails.commandQualifier;
        if (i == 2) {
            launchBrowserMode = LaunchBrowserMode.USE_EXISTING_BROWSER;
        } else if (i != 3) {
            launchBrowserMode = LaunchBrowserMode.LAUNCH_IF_NOT_ALREADY_LAUNCHED;
        } else {
            launchBrowserMode = LaunchBrowserMode.LAUNCH_NEW_BROWSER;
        }
        this.mCmdParams = new LaunchBrowserParams(commandDetails, textMessage, strGsm8BitUnpackedToString, launchBrowserMode);
        if (iconIdRetrieveIconId == null) {
            return false;
        }
        this.mIconLoadState = 1;
        this.mIconLoader.loadIcon(iconIdRetrieveIconId.recordNumber, obtainMessage(1));
        return true;
    }

    private boolean processPlayTone(CommandDetails commandDetails, List list) throws ResultException {
        Tone toneFromInt;
        Duration durationRetrieveDuration;
        CatLog.d(this, "process PlayTone");
        TextMessage textMessage = new TextMessage();
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.TONE, list);
        IconId iconIdRetrieveIconId = null;
        if (comprehensionTlvSearchForTag == null || comprehensionTlvSearchForTag.getLength() <= 0) {
            toneFromInt = null;
        } else {
            try {
                toneFromInt = Tone.fromInt(comprehensionTlvSearchForTag.getRawValue()[comprehensionTlvSearchForTag.getValueIndex()]);
            } catch (IndexOutOfBoundsException unused) {
                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            }
        }
        ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.ALPHA_ID, list);
        if (comprehensionTlvSearchForTag2 != null) {
            String strRetrieveAlphaId = ValueParser.retrieveAlphaId(comprehensionTlvSearchForTag2, this.mNoAlphaUsrCnf);
            textMessage.text = strRetrieveAlphaId;
            if (strRetrieveAlphaId == null) {
                textMessage.text = "";
            }
        }
        ComprehensionTlv comprehensionTlvSearchForTag3 = searchForTag(ComprehensionTlvTag.DURATION, list);
        if (comprehensionTlvSearchForTag3 != null) {
            try {
                durationRetrieveDuration = ValueParser.retrieveDuration(comprehensionTlvSearchForTag3);
            } catch (ResultException e) {
                CatLog.e(this, "retrieveDuration ResultException: " + e.result());
                durationRetrieveDuration = null;
            }
        } else {
            durationRetrieveDuration = null;
        }
        ComprehensionTlv comprehensionTlvSearchForTag4 = searchForTag(ComprehensionTlvTag.ICON_ID, list);
        if (comprehensionTlvSearchForTag4 != null) {
            iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag4);
            textMessage.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
        }
        IconId iconId = iconIdRetrieveIconId;
        boolean z = (commandDetails.commandQualifier & 1) != 0;
        textMessage.responseNeeded = false;
        this.mCmdParams = new PlayToneParams(commandDetails, textMessage, toneFromInt, durationRetrieveDuration, z);
        if (iconId == null) {
            return false;
        }
        this.mIconLoadState = 1;
        this.mIconLoader.loadIcon(iconId.recordNumber, obtainMessage(1));
        return true;
    }

    private boolean processSetupCall(CommandDetails commandDetails, List list) throws ResultException {
        IconId iconIdRetrieveIconId;
        CatLog.d(this, "process SetupCall");
        Iterator<ComprehensionTlv> it = list.iterator();
        TextMessage textMessage = new TextMessage();
        TextMessage textMessage2 = new TextMessage();
        ComprehensionTlvTag comprehensionTlvTag = ComprehensionTlvTag.ALPHA_ID;
        textMessage.text = onGetAlphaId(searchForNextTag(comprehensionTlvTag, it));
        ComprehensionTlvTag comprehensionTlvTag2 = ComprehensionTlvTag.ICON_ID;
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(comprehensionTlvTag2, list);
        IconId iconIdRetrieveIconId2 = null;
        if (comprehensionTlvSearchForTag != null) {
            iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag);
            textMessage.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
        } else {
            iconIdRetrieveIconId = null;
        }
        ComprehensionTlv comprehensionTlvSearchForNextTag = searchForNextTag(comprehensionTlvTag, it);
        if (comprehensionTlvSearchForNextTag != null) {
            textMessage2.text = onGetAlphaId(comprehensionTlvSearchForNextTag);
        }
        ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(comprehensionTlvTag2, list);
        if (comprehensionTlvSearchForTag2 != null) {
            iconIdRetrieveIconId2 = ValueParser.retrieveIconId(comprehensionTlvSearchForTag2);
            textMessage2.iconSelfExplanatory = iconIdRetrieveIconId2.selfExplanatory;
        }
        this.mCmdParams = new CallSetupParams(commandDetails, textMessage, textMessage2);
        if (iconIdRetrieveIconId == null && iconIdRetrieveIconId2 == null) {
            return false;
        }
        this.mIconLoadState = 2;
        this.mIconLoader.loadIcons(new int[]{iconIdRetrieveIconId != null ? iconIdRetrieveIconId.recordNumber : -1, iconIdRetrieveIconId2 != null ? iconIdRetrieveIconId2.recordNumber : -1}, obtainMessage(1));
        return true;
    }

    protected boolean processProvideLocalInfo(CommandDetails commandDetails, List<ComprehensionTlv> list) throws ResultException {
        CatLog.d(this, "process ProvideLocalInfo");
        int i = commandDetails.commandQualifier;
        if (i == 3) {
            CatLog.d(this, "PLI [DTTZ_SETTING]");
            this.mCmdParams = new CommandParams(commandDetails);
            return false;
        }
        if (i == 4) {
            CatLog.d(this, "PLI [LANGUAGE_SETTING]");
            this.mCmdParams = new CommandParams(commandDetails);
            return false;
        }
        if (handleBatteryStateInfo(commandDetails)) {
            return false;
        }
        CatLog.d(this, "PLI[" + commandDetails.commandQualifier + "] Command Not Supported");
        this.mCmdParams = new CommandParams(commandDetails);
        throw new ResultException(ResultCode.BEYOND_TERMINAL_CAPABILITY);
    }

    private boolean processLanguageNotification(CommandDetails commandDetails, List list) throws ResultException {
        String str;
        CatLog.d(this, "process Language Notification");
        String language = Locale.getDefault().getLanguage();
        int i = commandDetails.commandQualifier;
        String strGsm8BitUnpackedToString = null;
        if (i == 0) {
            if (TextUtils.isEmpty(this.mSavedLanguage) || TextUtils.isEmpty(this.mRequestedLanguage) || !this.mRequestedLanguage.equals(language)) {
                str = null;
            } else {
                CatLog.d(this, "Non-specific language notification changes the language setting back to " + this.mSavedLanguage);
                str = this.mSavedLanguage;
            }
            this.mSavedLanguage = null;
            this.mRequestedLanguage = null;
            strGsm8BitUnpackedToString = str;
        } else if (i == 1) {
            ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.LANGUAGE, list);
            if (comprehensionTlvSearchForTag != null) {
                if (comprehensionTlvSearchForTag.getLength() != 2) {
                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
                }
                strGsm8BitUnpackedToString = GsmAlphabet.gsm8BitUnpackedToString(comprehensionTlvSearchForTag.getRawValue(), comprehensionTlvSearchForTag.getValueIndex(), 2);
                if (TextUtils.isEmpty(this.mSavedLanguage) || (!TextUtils.isEmpty(this.mRequestedLanguage) && !this.mRequestedLanguage.equals(language))) {
                    this.mSavedLanguage = language;
                }
                this.mRequestedLanguage = strGsm8BitUnpackedToString;
                CatLog.d(this, "Specific language notification changes the language setting to " + this.mRequestedLanguage);
            }
        } else {
            CatLog.d(this, "LN[" + commandDetails.commandQualifier + "] Command Not Supported");
        }
        this.mCmdParams = new LanguageParams(commandDetails, strGsm8BitUnpackedToString);
        return false;
    }

    private boolean processBIPClient(CommandDetails commandDetails, List list) throws ResultException {
        boolean z;
        IconId iconIdRetrieveIconId;
        AppInterface.CommandType commandTypeFromInt = AppInterface.CommandType.fromInt(commandDetails.typeOfCommand);
        if (commandTypeFromInt != null) {
            CatLog.d(this, "process " + commandTypeFromInt.name());
        }
        TextMessage textMessage = new TextMessage();
        ComprehensionTlv comprehensionTlvSearchForTag = searchForTag(ComprehensionTlvTag.ALPHA_ID, list);
        if (comprehensionTlvSearchForTag != null) {
            textMessage.text = ValueParser.retrieveAlphaId(comprehensionTlvSearchForTag, this.mNoAlphaUsrCnf);
            CatLog.d(this, "alpha TLV text=" + textMessage.text);
            z = true;
        } else {
            z = false;
        }
        ComprehensionTlv comprehensionTlvSearchForTag2 = searchForTag(ComprehensionTlvTag.ICON_ID, list);
        if (comprehensionTlvSearchForTag2 != null) {
            iconIdRetrieveIconId = ValueParser.retrieveIconId(comprehensionTlvSearchForTag2);
            textMessage.iconSelfExplanatory = iconIdRetrieveIconId.selfExplanatory;
        } else {
            iconIdRetrieveIconId = null;
        }
        textMessage.responseNeeded = false;
        this.mCmdParams = new BIPClientParams(commandDetails, textMessage, z);
        if (iconIdRetrieveIconId == null) {
            return false;
        }
        this.mIconLoadState = 1;
        this.mIconLoader.loadIcon(iconIdRetrieveIconId.recordNumber, obtainMessage(1));
        return true;
    }

    public void dispose() {
        this.mIconLoader.dispose();
        this.mIconLoader = null;
        this.mCmdParams = null;
        this.mCaller = null;
        sInstance = null;
    }

    protected Menu onCreatMenu() {
        return new Menu();
    }

    protected String onGetAlphaId(ComprehensionTlv comprehensionTlv) throws ResultException {
        return ValueParser.retrieveAlphaId(comprehensionTlv, this.mNoAlphaUsrCnf);
    }

    protected void onGetEventList(byte[] bArr, int i, int i2, int[] iArr) {
        int i3 = 0;
        while (i2 > 0) {
            int i4 = bArr[i] & 255;
            i++;
            i2--;
            if (i4 == 4 || i4 == 5 || i4 == 7 || i4 == 8 || i4 == 15 || i4 == 23) {
                iArr[i3] = i4;
                i3++;
            }
        }
    }
}
