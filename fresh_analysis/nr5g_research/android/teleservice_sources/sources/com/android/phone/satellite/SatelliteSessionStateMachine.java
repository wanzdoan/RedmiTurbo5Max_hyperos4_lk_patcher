package com.android.phone.satellite;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import com.android.internal.telephony.NtnRIL;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.ntnphone.NtnPhone;
import com.android.internal.telephony.ntnphone.NtnPhoneFactory;
import com.android.internal.telephony.ntnphone.SatelliteConfig;
import com.android.internal.telephony.utils.TelephonyCommonUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.R;
import com.android.phone.ntn.NtnAudioServiceManager;
import com.android.phone.ntn.NtnPhoneOptimization;
import com.android.phone.utils.MiuiSatelliteUtils;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteSessionStateMachine extends StateMachine {
    private static Context mContext;
    private static SatelliteSessionStateMachine sInstance;
    private NtnAudioServiceManager mCsAudioManager;
    private int mCurrentState;
    private final DisconnectingState mDisconnectingState;
    private final NtnPhone.HlsErrorListener mHlsErrorListener;
    private final IdleState mIdleState;
    private int mPreviousState;
    private final RegisteredState mRegisteredState;
    private SatelliteController mSatelliteController;
    private final SearchingState mSearchingState;
    private final ShutingDownState mShutingDownState;
    private final StartingState mStartingState;

    public static SatelliteSessionStateMachine make(SatelliteController satelliteController, Context context) {
        if (sInstance == null) {
            sInstance = new SatelliteSessionStateMachine(Looper.getMainLooper(), satelliteController, context);
        }
        return sInstance;
    }

    public SatelliteSessionStateMachine(Looper looper, SatelliteController satelliteController, Context context) {
        super("SatelliteSessionStateMachine", looper);
        IdleState idleState = new IdleState();
        this.mIdleState = idleState;
        StartingState startingState = new StartingState();
        this.mStartingState = startingState;
        SearchingState searchingState = new SearchingState();
        this.mSearchingState = searchingState;
        RegisteredState registeredState = new RegisteredState();
        this.mRegisteredState = registeredState;
        DisconnectingState disconnectingState = new DisconnectingState();
        this.mDisconnectingState = disconnectingState;
        ShutingDownState shutingDownState = new ShutingDownState();
        this.mShutingDownState = shutingDownState;
        this.mHlsErrorListener = new NtnPhone.HlsErrorListener() { // from class: com.android.phone.satellite.SatelliteSessionStateMachine.1
            public void onHlsError(int i, int i2) {
                int i3;
                if (SatelliteSessionStateMachine.this.mSatelliteController.isSatelliteEnabled() && i == 7) {
                    if (i2 == 1) {
                        i3 = 21;
                    } else {
                        if (i2 != 8) {
                            SatelliteSessionStateMachine.this.logd("error unknown, return.");
                            return;
                        }
                        i3 = 22;
                    }
                    SatelliteController satelliteController2 = SatelliteSessionStateMachine.this.mSatelliteController;
                    SatelliteController unused = SatelliteSessionStateMachine.this.mSatelliteController;
                    satelliteController2.closeSatellite(1012);
                    MiuiPhoneUtils.showSatelliteAlertDialog(i3);
                }
            }
        };
        this.mSatelliteController = satelliteController;
        mContext = context;
        this.mCsAudioManager = new NtnAudioServiceManager();
        this.mPreviousState = -1;
        this.mCurrentState = -1;
        addState(idleState);
        addState(startingState);
        addState(searchingState);
        addState(registeredState);
        addState(disconnectingState);
        addState(shutingDownState);
        setInitialState(idleState);
        start();
        logd("set idle state successful!");
    }

    public void onSatelliteEnabledStateChanged(boolean z) {
        sendMessage(1, Boolean.valueOf(z));
    }

    public void onSatelliteRadioStateChanged(boolean z) {
        sendMessage(2, Boolean.valueOf(z));
    }

    public void onSatelliteVoiceStateChanged(boolean z) {
        sendMessage(3, Boolean.valueOf(z));
    }

    public void onSatelliteSimStatusChanged(boolean z) {
        sendMessage(6, Boolean.valueOf(z));
    }

    public void onSatelliteDataEnabledChanged(boolean z) {
        sendMessage(7, Boolean.valueOf(z));
    }

    public void handleStartNtnStack() {
        sendMessage(4);
    }

    public void handleStartNtnRild() {
        sendMessage(5);
    }

    class IdleState extends State {
        private IdleState() {
        }

        public void enter() {
            SatelliteSessionStateMachine.this.logd("Entering IdleState");
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            satelliteSessionStateMachine.mPreviousState = satelliteSessionStateMachine.mCurrentState;
            SatelliteSessionStateMachine.this.mCurrentState = 0;
        }

        public void exit() {
            SatelliteSessionStateMachine.this.logd("Exiting IdleState");
        }

        public boolean processMessage(Message message) {
            SatelliteSessionStateMachine.this.logd("IdleState: processing " + SatelliteSessionStateMachine.this.getWhatToString(message.what));
            if (message.what == 1) {
                handleSatelliteUserEnabledStateChanged(((Boolean) message.obj).booleanValue());
            }
            return true;
        }

        private void handleSatelliteUserEnabledStateChanged(boolean z) {
            SatelliteSessionStateMachine.this.logd("IdleState: handleSatelliteUserEnabledStateChanged enabled: " + z);
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            if (z) {
                satelliteSessionStateMachine.transitionTo(satelliteSessionStateMachine.mStartingState);
                return;
            }
            satelliteSessionStateMachine.logd("handleSatelliteUserEnabledStateChanged is idle state, enabled: " + z);
        }
    }

    class StartingState extends State {
        private StartingState() {
        }

        public void enter() {
            SatelliteSessionStateMachine.this.logd("Entering StartingState");
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            satelliteSessionStateMachine.mPreviousState = satelliteSessionStateMachine.mCurrentState;
            SatelliteSessionStateMachine.this.mCurrentState = 1;
        }

        public void exit() {
            SatelliteSessionStateMachine.this.logd("Exiting StartingState");
        }

        public boolean processMessage(Message message) {
            SatelliteSessionStateMachine.this.logd("StartingState: processing " + SatelliteSessionStateMachine.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1) {
                SatelliteSessionStateMachine.this.handleSatelliteUserEnabled(((Boolean) message.obj).booleanValue());
                return true;
            }
            if (i == 2) {
                SatelliteSessionStateMachine.this.handleRadioPowerStateChanged();
                return true;
            }
            if (i == 4) {
                startNtnStack();
                return true;
            }
            if (i != 5) {
                return false;
            }
            bindNtnRild();
            return true;
        }

        private void startNtnStack() {
            SatelliteSessionStateMachine.this.logd("startNtnStack");
            NtnPhoneFactory.makeDefaultPhone(SatelliteSessionStateMachine.mContext, SatelliteSessionStateMachine.this.mSatelliteController.getEnabledSatelliteSlotId());
            try {
                NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
                if (defaultPhone == null) {
                    return;
                }
                int iCaculateNtnStackPropertyValue = TelephonyCommonUtils.caculateNtnStackPropertyValue(SatelliteSessionStateMachine.this.mSatelliteController.getEnabledSatelliteSlotId(), true, defaultPhone.getChipType(), 0);
                if (iCaculateNtnStackPropertyValue == -1) {
                    SatelliteSessionStateMachine.this.logd("startNtnStack prop invalid, return.");
                    return;
                }
                SystemProperties.set("persist.radio.ntn_stack_on", Integer.toString(iCaculateNtnStackPropertyValue));
                int i = SystemProperties.getInt("persist.radio.ntn_stack_on", -1);
                SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
                if (i != iCaculateNtnStackPropertyValue) {
                    satelliteSessionStateMachine.logd("set prop failed!");
                    return;
                }
                SatelliteController satelliteController = satelliteSessionStateMachine.mSatelliteController;
                SatelliteController satelliteController2 = SatelliteSessionStateMachine.this.mSatelliteController;
                SatelliteController unused = SatelliteSessionStateMachine.this.mSatelliteController;
                satelliteController.sendMessage(satelliteController2.obtainMessage(4, 2));
                SatelliteController satelliteController3 = SatelliteSessionStateMachine.this.mSatelliteController;
                SatelliteController satelliteController4 = SatelliteSessionStateMachine.this.mSatelliteController;
                SatelliteController unused2 = SatelliteSessionStateMachine.this.mSatelliteController;
                satelliteController3.sendMessageDelayed(satelliteController4.obtainMessage(2), SatelliteSessionStateMachine.mContext.getResources().getInteger(R.integer.config_start_ntn_rild_delay));
            } catch (IllegalStateException e) {
                SatelliteSessionStateMachine.this.logd("startNtnStack: " + e.getMessage());
            }
        }

        public void bindNtnRild() {
            int enabledSatelliteSlotId = SatelliteSessionStateMachine.this.mSatelliteController.getEnabledSatelliteSlotId();
            NtnPhoneFactory.makeDefaultPhone(SatelliteSessionStateMachine.mContext, enabledSatelliteSlotId);
            NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
            if (defaultPhone == null) {
                return;
            }
            NtnPhone ntnPhoneUpdateSatellitePhone = NtnPhoneFactory.updateSatellitePhone(defaultPhone, enabledSatelliteSlotId);
            ntnPhoneUpdateSatellitePhone.registerHlsErrorListener(SatelliteSessionStateMachine.this.mHlsErrorListener);
            NtnRIL ntnRIL = ntnPhoneUpdateSatellitePhone.mNtnRil;
            SatelliteController satelliteController = SatelliteSessionStateMachine.this.mSatelliteController;
            SatelliteController unused = SatelliteSessionStateMachine.this.mSatelliteController;
            ntnRIL.registerForRadioStateChanged(satelliteController, 0, (Object) null);
            ntnPhoneUpdateSatellitePhone.initNtnRadio();
            NtnRIL ntnRIL2 = ntnPhoneUpdateSatellitePhone.mNtnRil;
            SatelliteController satelliteController2 = SatelliteSessionStateMachine.this.mSatelliteController;
            SatelliteController unused2 = SatelliteSessionStateMachine.this.mSatelliteController;
            ntnRIL2.registerChannelState(satelliteController2, 10, (Object) null);
            NtnRIL ntnRIL3 = ntnPhoneUpdateSatellitePhone.mNtnRil;
            SatelliteController satelliteController3 = SatelliteSessionStateMachine.this.mSatelliteController;
            SatelliteController unused3 = SatelliteSessionStateMachine.this.mSatelliteController;
            ntnRIL3.registerHighPenetrationState(satelliteController3, 14, (Object) null);
            ntnPhoneUpdateSatellitePhone.setPowerSwitch(true);
            SatelliteSessionStateMachine.this.mCsAudioManager.launchAudioServiceManager(true);
            SatelliteSessionStateMachine.this.logd("start sateliite phoneid = " + enabledSatelliteSlotId);
            MiuiSatelliteUtils.setupOnNtnBoot();
            if (!SatelliteSessionStateMachine.this.mSatelliteController.isHardSimSwapForTT()) {
                SatelliteUIController.getInstance().sendEnableSatelliteBroadcast(enabledSatelliteSlotId, true, false);
            }
            SatelliteSessionStateMachine.this.logd("startSatellite successfully!");
        }
    }

    class SearchingState extends State {
        private SearchingState() {
        }

        public void enter() {
            SatelliteSessionStateMachine.this.logd("Entering SearchingState");
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            satelliteSessionStateMachine.mPreviousState = satelliteSessionStateMachine.mCurrentState;
            SatelliteSessionStateMachine.this.mCurrentState = 2;
        }

        public void exit() {
            SatelliteSessionStateMachine.this.logd("Exiting SearchingState");
        }

        public boolean processMessage(Message message) {
            SatelliteSessionStateMachine.this.logd("SearchingState: processing " + SatelliteSessionStateMachine.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1) {
                SatelliteSessionStateMachine.this.handleSatelliteUserEnabled(((Boolean) message.obj).booleanValue());
                return true;
            }
            if (i == 2) {
                SatelliteSessionStateMachine.this.handleRadioPowerStateChanged();
                return true;
            }
            if (i != 3) {
                return false;
            }
            if (((Boolean) message.obj).booleanValue()) {
                SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
                satelliteSessionStateMachine.transitionTo(satelliteSessionStateMachine.mRegisteredState);
            }
            return true;
        }
    }

    class RegisteredState extends State {
        private RegisteredState() {
        }

        public void enter() {
            SatelliteSessionStateMachine.this.logd("Entering RegisteredState");
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            satelliteSessionStateMachine.mPreviousState = satelliteSessionStateMachine.mCurrentState;
            SatelliteSessionStateMachine.this.mCurrentState = 3;
        }

        public void exit() {
            SatelliteSessionStateMachine.this.logd("Exiting RegisteredState");
        }

        public boolean processMessage(Message message) {
            SatelliteSessionStateMachine.this.logd("RegisteredState: processing " + SatelliteSessionStateMachine.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1) {
                SatelliteSessionStateMachine.this.handleSatelliteUserEnabled(((Boolean) message.obj).booleanValue());
                return true;
            }
            if (i == 2) {
                SatelliteSessionStateMachine.this.handleRadioPowerStateChanged();
                return true;
            }
            if (i != 7) {
                return false;
            }
            restartSatelliteForDataEnable();
            return true;
        }

        private void restartSatelliteForDataEnable() {
            SatelliteController unused = SatelliteSessionStateMachine.this.mSatelliteController;
            NtnPhone defaultNtnPhone = SatelliteController.getDefaultNtnPhone();
            if (defaultNtnPhone == null) {
                return;
            }
            int chipType = defaultNtnPhone.getChipType();
            int iOrdinal = SatelliteConfig.Chip.CETC54.ordinal();
            NtnRIL ntnRIL = defaultNtnPhone.mNtnRil;
            if (chipType == iOrdinal) {
                SatelliteController satelliteController = SatelliteSessionStateMachine.this.mSatelliteController;
                SatelliteController unused2 = SatelliteSessionStateMachine.this.mSatelliteController;
                ntnRIL.requestShutdown(satelliteController.obtainMessage(12));
            } else {
                SatelliteController unused3 = SatelliteSessionStateMachine.this.mSatelliteController;
                SatelliteController unused4 = SatelliteSessionStateMachine.this.mSatelliteController;
                SatelliteController unused5 = SatelliteSessionStateMachine.this.mSatelliteController;
                SatelliteController satelliteController2 = SatelliteSessionStateMachine.this.mSatelliteController;
                SatelliteController unused6 = SatelliteSessionStateMachine.this.mSatelliteController;
                ntnRIL.setPriorityFrequencySearch(new int[]{565, 565, 565}, satelliteController2.obtainMessage(9));
            }
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            satelliteSessionStateMachine.transitionTo(satelliteSessionStateMachine.mStartingState);
        }
    }

    class DisconnectingState extends State {
        private DisconnectingState() {
        }

        public void enter() {
            SatelliteSessionStateMachine.this.logd("Entering DisconnectingState");
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            satelliteSessionStateMachine.mPreviousState = satelliteSessionStateMachine.mCurrentState;
            SatelliteSessionStateMachine.this.mCurrentState = 4;
        }

        public void exit() {
            SatelliteSessionStateMachine.this.logd("Exiting DisconnectingState");
        }

        public boolean processMessage(Message message) {
            SatelliteSessionStateMachine.this.logd("DisconnectingState: processing " + SatelliteSessionStateMachine.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 1) {
                SatelliteSessionStateMachine.this.logd("is DisconnectingState, not need to handle");
                return true;
            }
            if (i != 6) {
                return false;
            }
            SatelliteSessionStateMachine.this.changeSatellitestateToIdle();
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            satelliteSessionStateMachine.transitionTo(satelliteSessionStateMachine.mIdleState);
            return true;
        }
    }

    class ShutingDownState extends State {
        private ShutingDownState() {
        }

        public void enter() {
            SatelliteSessionStateMachine.this.logd("Entering ShutingDownState");
            SatelliteSessionStateMachine satelliteSessionStateMachine = SatelliteSessionStateMachine.this;
            satelliteSessionStateMachine.mPreviousState = satelliteSessionStateMachine.mCurrentState;
            SatelliteSessionStateMachine.this.mCurrentState = 5;
        }

        public void exit() {
            SatelliteSessionStateMachine.this.logd("Exiting ShutingDownState");
        }

        public boolean processMessage(Message message) {
            SatelliteSessionStateMachine.this.logd("ShutingDownState: processing " + SatelliteSessionStateMachine.this.getWhatToString(message.what));
            return true;
        }
    }

    protected String getWhatToString(int i) {
        switch (i) {
            case 1:
                return "EVENT_SATELLITE_USER_ENABLED_STATE_CHANGED";
            case 2:
                return "EVENT_RADIO_POWER_STATE_CHANGED";
            case 3:
                return "EVENT_VOICE_REGISTRATION_STATE_CHANGED";
            case 4:
                return "EVENT_START_NTN_STACK";
            case 5:
                return "EVENT_START_NTN_RILD";
            case 6:
                return "EVENT_SATELLITE_SIM_STATUS_CHANGED";
            case 7:
                return "EVENT_SATELLITE_DATA_ENABLED_CHANGED";
            default:
                return "UNKNOWN EVENT " + i;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSatelliteUserEnabled(boolean z) {
        if (z) {
            return;
        }
        if (this.mSatelliteController.isHardSimSwapForTT()) {
            stopSatelliteForSIMSwap();
            transitionTo(this.mDisconnectingState);
        } else {
            stopSatellite();
            transitionTo(this.mIdleState);
        }
    }

    public void handleRadioPowerStateChanged() {
        try {
            NtnPhone defaultPhone = NtnPhoneFactory.getDefaultPhone();
            if (defaultPhone != null && defaultPhone.mNtnRil.getRadioState() == 0) {
                if (SatelliteDataController.getInstance().isDataStart()) {
                    defaultPhone.mNtnRil.setPriorityFrequencySearch(new int[]{565, 565, 565}, this.mSatelliteController.obtainMessage(9));
                } else {
                    NtnPhoneOptimization ntnPhoneOptimization = new NtnPhoneOptimization(mContext);
                    if (SatelliteConfig.chipFromConfig(defaultPhone.getChipType()) == SatelliteConfig.Chip.SYLINCOM_702) {
                        logd("setfreq!");
                        defaultPhone.mNtnRil.setPriorityFrequencySearch(ntnPhoneOptimization.getFreqList(this.mSatelliteController.getLastKnownLocation()), this.mSatelliteController.obtainMessage(9));
                    } else if (SatelliteConfig.chipFromConfig(defaultPhone.getChipType()) == SatelliteConfig.Chip.CETC54) {
                        logd("setbeam!");
                        int[] beamList = ntnPhoneOptimization.getBeamList(this.mSatelliteController.getLastKnownLocation());
                        int i = -1;
                        if (beamList[0] != -1) {
                            i = 1;
                        }
                        defaultPhone.setSatBeam(i, beamList);
                    } else {
                        defaultPhone.setRadioPower(true, false, false, true);
                    }
                    this.mSatelliteController.removeMessages(0);
                }
                if (this.mCurrentState == 1) {
                    transitionTo(this.mSearchingState);
                }
            }
        } catch (Exception e) {
            loge("setSatBeam: " + e);
        }
    }

    private boolean stopSatellite() {
        NtnPhone defaultNtnPhone = SatelliteController.getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            this.mSatelliteController.setSatelliteAirPlane(false);
            SatelliteUIController.getInstance().sendEnableSatelliteBroadcast(this.mSatelliteController.getEnabledSatelliteSlotId(), false, false);
            this.mSatelliteController.setEnabledSatelliteSlotId(-1);
            TelephonyCommonUtils.resetNtnStackPropertyValue();
            this.mCsAudioManager.launchAudioServiceManager(false);
            return false;
        }
        defaultNtnPhone.shutdownRadio();
        this.mCsAudioManager.launchAudioServiceManager(false);
        SatelliteUIController.getInstance().sendEnableSatelliteBroadcast(this.mSatelliteController.getEnabledSatelliteSlotId(), false, false);
        this.mSatelliteController.setEnabledSatelliteSlotId(-1);
        logd("stop satellite success!");
        return true;
    }

    private boolean stopSatelliteForSIMSwap() {
        this.mSatelliteController.setExitSatelliteInProgress(true);
        SatelliteSIMController.getInstance().removeSatelliteModeSubInfoReadyMsg();
        NtnPhone defaultNtnPhone = SatelliteController.getDefaultNtnPhone();
        if (defaultNtnPhone == null) {
            SatelliteSIMController.getInstance().setSimPowerState(1);
            SatelliteUIController.getInstance().sendEnableSatelliteBroadcast(this.mSatelliteController.getEnabledSatelliteSlotId(), false, false);
            SatelliteSIMController.getInstance().exitSatelliteDone();
            TelephonyCommonUtils.resetNtnStackPropertyValue();
            this.mCsAudioManager.launchAudioServiceManager(false);
            return false;
        }
        defaultNtnPhone.setPowerSwitch(false, mContext.getResources().getInteger(R.integer.config_power_off_bbic_delay));
        this.mCsAudioManager.launchAudioServiceManager(false);
        SatelliteUIController.getInstance().sendEnableSatelliteBroadcast(this.mSatelliteController.getEnabledSatelliteSlotId(), false, false);
        SatelliteSIMController.getInstance().exitSatelliteDone();
        logd("stop satellite success!");
        return true;
    }

    public void changeSatellitestateToIdle() {
        if (this.mSatelliteController.isExitSatelliteInProgress()) {
            this.mSatelliteController.setExitSatelliteInProgress(false);
            this.mSatelliteController.registerListener(false);
            this.mSatelliteController.setEnabledSatelliteSlotId(-1);
            this.mSatelliteController.setSatelliteAirPlane(false);
            logd("exit satellite mode complete.");
        }
    }
}
