package com.android.internal.telephony.cat;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class CatCmdMessage implements Parcelable {
    public static final Parcelable.Creator<CatCmdMessage> CREATOR = new Parcelable.Creator<CatCmdMessage>() { // from class: com.android.internal.telephony.cat.CatCmdMessage.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CatCmdMessage createFromParcel(Parcel parcel) {
            return new CatCmdMessage(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public CatCmdMessage[] newArray(int i) {
            return new CatCmdMessage[i];
        }
    };
    public BrowserSettings mBrowserSettings;
    public CallSettings mCallSettings;
    public CommandDetails mCmdDet;
    public Input mInput;
    public boolean mLoadIconFailed;
    public Menu mMenu;
    private SMSSettings mSMSSettings;
    public SetupEventListSettings mSetupEventListSettings;
    public TextMessage mTextMsg;
    public ToneSettings mToneSettings;

    public enum SetUpCallType {
        IF_NOT_BUSY,
        PUT_OTHERS_HOLD,
        DISCONNECT_ALL_OTHERS
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public class BrowserSettings {
        public LaunchBrowserMode mode;
        public String url;

        public BrowserSettings(CatCmdMessage catCmdMessage) {
        }
    }

    public class CallSettings {
        public String address;
        public TextMessage callMsg;
        public TextMessage confirmMsg;
        public Duration duration;
        public boolean redial;
        public SetUpCallType setUpCallType;

        private boolean getRedialPolicy(int i) {
            return i == 1 || i == 3 || i == 5;
        }

        public CallSettings(CatCmdMessage catCmdMessage) {
        }

        public CallSettings(CatCmdMessage catCmdMessage, CallSetupParams callSetupParams) {
            this.confirmMsg = callSetupParams.mConfirmMsg;
            this.callMsg = callSetupParams.mCallMsg;
            this.address = callSetupParams.mAddress;
            this.duration = callSetupParams.mDuration;
            this.redial = getRedialPolicy(callSetupParams.mCmdDet.commandQualifier);
            this.setUpCallType = getSetUpCallType(callSetupParams.mCmdDet.commandQualifier);
        }

        private SetUpCallType getSetUpCallType(int i) {
            if (i == 0 || i == 1) {
                return SetUpCallType.IF_NOT_BUSY;
            }
            if (i == 2 || i == 3) {
                return SetUpCallType.PUT_OTHERS_HOLD;
            }
            if (i == 4 || i == 5) {
                return SetUpCallType.DISCONNECT_ALL_OTHERS;
            }
            return SetUpCallType.IF_NOT_BUSY;
        }
    }

    public class SMSSettings {
        public TextMessage destAddr;
        public TextMessage smsText;

        public SMSSettings(CatCmdMessage catCmdMessage) {
        }
    }

    public class SetupEventListSettings {
        public int[] eventList;

        public SetupEventListSettings(CatCmdMessage catCmdMessage) {
        }
    }

    public final class SetupEventListConstants {
        public static final int BROWSER_TERMINATION_EVENT = 8;
        public static final int BROWSING_STATUS_EVENT = 15;
        public static final int IDLE_SCREEN_AVAILABLE_EVENT = 5;
        public static final int IMS_REGISTRATION_EVENT = 23;
        public static final int LANGUAGE_SELECTION_EVENT = 7;
        public static final int USER_ACTIVITY_EVENT = 4;

        public SetupEventListConstants(CatCmdMessage catCmdMessage) {
        }
    }

    public final class BrowserTerminationCauses {
        public static final int ERROR_TERMINATION = 1;
        public static final int USER_TERMINATION = 0;

        public BrowserTerminationCauses(CatCmdMessage catCmdMessage) {
        }
    }

    public CatCmdMessage(CommandParams commandParams) {
        this.mBrowserSettings = null;
        this.mToneSettings = null;
        this.mCallSettings = null;
        this.mSMSSettings = null;
        this.mSetupEventListSettings = null;
        this.mLoadIconFailed = false;
        this.mCmdDet = commandParams.mCmdDet;
        this.mLoadIconFailed = commandParams.mLoadIconFailed;
        switch (AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()]) {
            case 1:
            case 2:
                this.mMenu = ((SelectItemParams) commandParams).mMenu;
                break;
            case 3:
                if (commandParams instanceof SendSMSParams) {
                    SMSSettings sMSSettings = new SMSSettings(this);
                    this.mSMSSettings = sMSSettings;
                    SendSMSParams sendSMSParams = (SendSMSParams) commandParams;
                    sMSSettings.smsText = sendSMSParams.mTextSmsMsg;
                    sMSSettings.destAddr = sendSMSParams.mDestAddress;
                    this.mTextMsg = sendSMSParams.mDisplayText.mTextMsg;
                } else {
                    this.mTextMsg = ((DisplayTextParams) commandParams).mTextMsg;
                }
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                this.mTextMsg = ((DisplayTextParams) commandParams).mTextMsg;
                break;
            case 10:
                this.mTextMsg = ((DisplayTextParams) commandParams).mTextMsg;
                break;
            case 11:
            case 12:
                this.mInput = ((GetInputParams) commandParams).mInput;
                break;
            case 13:
                LaunchBrowserParams launchBrowserParams = (LaunchBrowserParams) commandParams;
                this.mTextMsg = launchBrowserParams.mConfirmMsg;
                BrowserSettings browserSettings = new BrowserSettings(this);
                this.mBrowserSettings = browserSettings;
                browserSettings.url = launchBrowserParams.mUrl;
                browserSettings.mode = launchBrowserParams.mMode;
                break;
            case 14:
                PlayToneParams playToneParams = (PlayToneParams) commandParams;
                this.mToneSettings = playToneParams.mSettings;
                this.mTextMsg = playToneParams.mTextMsg;
                break;
            case 15:
                this.mTextMsg = ((CallSetupParams) commandParams).mConfirmMsg;
                break;
            case 16:
                CallSettings callSettings = new CallSettings(this);
                this.mCallSettings = callSettings;
                CallSetupParams callSetupParams = (CallSetupParams) commandParams;
                callSettings.confirmMsg = callSetupParams.mConfirmMsg;
                callSettings.callMsg = callSetupParams.mCallMsg;
                break;
            case 17:
            case 18:
            case 19:
            case 20:
                this.mTextMsg = ((BIPClientParams) commandParams).mTextMsg;
                break;
            case 21:
                SetupEventListSettings setupEventListSettings = new SetupEventListSettings(this);
                this.mSetupEventListSettings = setupEventListSettings;
                setupEventListSettings.eventList = ((SetEventListParams) commandParams).mEventInfo;
                break;
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.cat.CatCmdMessage$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
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
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_SMS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.DISPLAY_TEXT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_IDLE_MODE_TEXT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DTMF.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.REFRESH.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RUN_AT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_SS.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_USSD.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INPUT.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_INKEY.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.LAUNCH_BROWSER.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PLAY_TONE.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GET_CHANNEL_STATUS.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_CALL.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.OPEN_CHANNEL.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.CLOSE_CHANNEL.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.RECEIVE_DATA.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SEND_DATA.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.SET_UP_EVENT_LIST.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.PROVIDE_LOCAL_INFORMATION.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[AppInterface.CommandType.GEOGRAPHICAL_LOCATION_REQUEST.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
        }
    }

    public CatCmdMessage(Parcel parcel) {
        this.mBrowserSettings = null;
        this.mToneSettings = null;
        this.mCallSettings = null;
        this.mSMSSettings = null;
        this.mSetupEventListSettings = null;
        this.mLoadIconFailed = false;
        this.mCmdDet = (CommandDetails) parcel.readParcelable(CommandDetails.class.getClassLoader());
        this.mTextMsg = (TextMessage) parcel.readParcelable(TextMessage.class.getClassLoader());
        this.mMenu = (Menu) parcel.readParcelable(Menu.class.getClassLoader());
        this.mInput = (Input) parcel.readParcelable(Input.class.getClassLoader());
        this.mLoadIconFailed = parcel.readByte() == 1;
        int i = AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()];
        if (i == 3) {
            SMSSettings sMSSettings = new SMSSettings(this);
            this.mSMSSettings = sMSSettings;
            sMSSettings.smsText = (TextMessage) parcel.readParcelable(SendSMSParams.class.getClassLoader());
            this.mSMSSettings.destAddr = (TextMessage) parcel.readParcelable(SendSMSParams.class.getClassLoader());
            return;
        }
        if (i == 16) {
            CallSettings callSettings = new CallSettings(this);
            this.mCallSettings = callSettings;
            callSettings.confirmMsg = (TextMessage) parcel.readParcelable(TextMessage.class.getClassLoader());
            this.mCallSettings.callMsg = (TextMessage) parcel.readParcelable(TextMessage.class.getClassLoader());
            return;
        }
        if (i == 21) {
            this.mSetupEventListSettings = new SetupEventListSettings(this);
            int i2 = parcel.readInt();
            this.mSetupEventListSettings.eventList = new int[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                this.mSetupEventListSettings.eventList[i3] = parcel.readInt();
            }
            return;
        }
        if (i == 13) {
            BrowserSettings browserSettings = new BrowserSettings(this);
            this.mBrowserSettings = browserSettings;
            browserSettings.url = parcel.readString();
            this.mBrowserSettings.mode = LaunchBrowserMode.values()[parcel.readInt()];
        } else {
            if (i != 14) {
                return;
            }
            this.mToneSettings = (ToneSettings) parcel.readParcelable(ToneSettings.class.getClassLoader());
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mCmdDet, 0);
        parcel.writeParcelable(this.mTextMsg, 0);
        parcel.writeParcelable(this.mMenu, 0);
        parcel.writeParcelable(this.mInput, 0);
        parcel.writeByte(this.mLoadIconFailed ? (byte) 1 : (byte) 0);
        int i2 = AnonymousClass2.$SwitchMap$com$android$internal$telephony$cat$AppInterface$CommandType[getCmdType().ordinal()];
        if (i2 == 3) {
            SMSSettings sMSSettings = this.mSMSSettings;
            if (sMSSettings != null) {
                parcel.writeParcelable(sMSSettings.smsText, 0);
                parcel.writeParcelable(this.mSMSSettings.destAddr, 0);
                return;
            }
            return;
        }
        if (i2 == 16) {
            parcel.writeParcelable(this.mCallSettings.confirmMsg, 0);
            parcel.writeParcelable(this.mCallSettings.callMsg, 0);
        } else {
            if (i2 == 21) {
                parcel.writeIntArray(this.mSetupEventListSettings.eventList);
                return;
            }
            if (i2 == 13) {
                parcel.writeString(this.mBrowserSettings.url);
                parcel.writeInt(this.mBrowserSettings.mode.ordinal());
            } else {
                if (i2 != 14) {
                    return;
                }
                parcel.writeParcelable(this.mToneSettings, 0);
            }
        }
    }

    public AppInterface.CommandType getCmdType() {
        return AppInterface.CommandType.fromInt(this.mCmdDet.typeOfCommand);
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public Input geInput() {
        return this.mInput;
    }

    public TextMessage geTextMessage() {
        return this.mTextMsg;
    }

    public BrowserSettings getBrowserSettings() {
        return this.mBrowserSettings;
    }

    public ToneSettings getToneSettings() {
        return this.mToneSettings;
    }

    public CallSettings getCallSettings() {
        return this.mCallSettings;
    }

    public SetupEventListSettings getSetEventList() {
        return this.mSetupEventListSettings;
    }

    public boolean hasIconLoadFailed() {
        return this.mLoadIconFailed;
    }
}
