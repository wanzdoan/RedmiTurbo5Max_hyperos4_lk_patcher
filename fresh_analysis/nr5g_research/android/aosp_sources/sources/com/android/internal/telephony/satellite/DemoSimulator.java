package com.android.internal.telephony.satellite;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.IIntegerConsumer;
import android.telephony.satellite.stub.ISatelliteListener;
import android.telephony.satellite.stub.NtnSignalStrength;
import android.util.Log;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class DemoSimulator extends StateMachine {
    protected static final int EVENT_DEVICE_ALIGNED = 4;
    protected static final int EVENT_DEVICE_NOT_ALIGNED = 5;
    private static DemoSimulator sInstance;
    private final ConnectedState mConnectedState;
    private final Context mContext;
    private ISatelliteListener mISatelliteListener;
    private AtomicBoolean mIsAligned;
    private final NotConnectedState mNotConnectedState;
    private final PowerOffState mPowerOffState;
    private final SatelliteController mSatelliteController;

    public static DemoSimulator getInstance() {
        if (sInstance == null) {
            Log.e("DemoSimulator", "DemoSimulator was not yet initialized.");
        }
        return sInstance;
    }

    public static DemoSimulator make(Context context, SatelliteController satelliteController) {
        if (sInstance == null) {
            sInstance = new DemoSimulator(context, Looper.getMainLooper(), satelliteController);
        }
        return sInstance;
    }

    protected DemoSimulator(Context context, Looper looper, SatelliteController satelliteController) {
        super("DemoSimulator", looper);
        PowerOffState powerOffState = new PowerOffState();
        this.mPowerOffState = powerOffState;
        NotConnectedState notConnectedState = new NotConnectedState();
        this.mNotConnectedState = notConnectedState;
        ConnectedState connectedState = new ConnectedState();
        this.mConnectedState = connectedState;
        this.mIsAligned = new AtomicBoolean(false);
        this.mContext = context;
        this.mSatelliteController = satelliteController;
        addState(powerOffState);
        addState(notConnectedState);
        addState(connectedState);
        setInitialState(powerOffState);
        start();
    }

    private class PowerOffState extends State {
        private PowerOffState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            DemoSimulator.this.logd("Entering PowerOffState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            DemoSimulator.this.logd("Exiting PowerOffState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            DemoSimulator.this.log("PowerOffState: processing " + DemoSimulator.this.getWhatToString(message.what));
            if (message.what == 1) {
                DemoSimulator demoSimulator = DemoSimulator.this;
                demoSimulator.transitionTo(demoSimulator.mNotConnectedState);
            }
            return true;
        }
    }

    private class NotConnectedState extends State {
        private NotConnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            DemoSimulator.this.logd("Entering NotConnectedState");
            try {
                NtnSignalStrength ntnSignalStrength = new NtnSignalStrength();
                ntnSignalStrength.signalStrengthLevel = 0;
                DemoSimulator.this.mISatelliteListener.onSatelliteModemStateChanged(6);
                DemoSimulator.this.mISatelliteListener.onNtnSignalStrengthChanged(ntnSignalStrength);
                if (DemoSimulator.this.mIsAligned.get()) {
                    handleEventDeviceAlignedWithSatellite(true);
                }
            } catch (RemoteException e) {
                DemoSimulator.this.loge("NotConnectedState: RemoteException " + e);
            }
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            DemoSimulator.this.logd("Exiting NotConnectedState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            DemoSimulator.this.log("NotConnectedState: processing " + DemoSimulator.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 2) {
                DemoSimulator demoSimulator = DemoSimulator.this;
                demoSimulator.transitionTo(demoSimulator.mPowerOffState);
                return true;
            }
            if (i == 3) {
                handleEventDeviceAlignedWithSatellite(((Boolean) message.obj).booleanValue());
                return true;
            }
            if (i != 4) {
                return true;
            }
            DemoSimulator demoSimulator2 = DemoSimulator.this;
            demoSimulator2.transitionTo(demoSimulator2.mConnectedState);
            return true;
        }

        private void handleEventDeviceAlignedWithSatellite(boolean z) {
            if (!z || DemoSimulator.this.hasMessages(4)) {
                if (z || !DemoSimulator.this.hasMessages(4)) {
                    return;
                }
                DemoSimulator.this.logd("NotConnectedState: handleEventAlignedWithSatellite isAligned=false. Remove EVENT_DEVICE_ALIGNED message.");
                DemoSimulator.this.removeMessages(4);
                return;
            }
            long demoPointingAlignedDurationMillis = DemoSimulator.this.mSatelliteController.getDemoPointingAlignedDurationMillis();
            DemoSimulator.this.logd("NotConnectedState: handleEventAlignedWithSatellite isAligned=true. Send delayed EVENT_DEVICE_ALIGNED message in durationMillis=" + demoPointingAlignedDurationMillis);
            DemoSimulator.this.sendMessageDelayed(4, demoPointingAlignedDurationMillis);
        }
    }

    private class ConnectedState extends State {
        private ConnectedState() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void enter() {
            DemoSimulator.this.logd("Entering ConnectedState");
            try {
                NtnSignalStrength ntnSignalStrength = new NtnSignalStrength();
                ntnSignalStrength.signalStrengthLevel = 2;
                DemoSimulator.this.mISatelliteListener.onSatelliteModemStateChanged(7);
                DemoSimulator.this.mISatelliteListener.onNtnSignalStrengthChanged(ntnSignalStrength);
                if (DemoSimulator.this.mIsAligned.get()) {
                    return;
                }
                handleEventDeviceAlignedWithSatellite(false);
            } catch (RemoteException e) {
                DemoSimulator.this.loge("ConnectedState: RemoteException " + e);
            }
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public void exit() {
            DemoSimulator.this.logd("Exiting ConnectedState");
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            DemoSimulator.this.log("ConnectedState: processing " + DemoSimulator.this.getWhatToString(message.what));
            int i = message.what;
            if (i == 2) {
                DemoSimulator demoSimulator = DemoSimulator.this;
                demoSimulator.transitionTo(demoSimulator.mPowerOffState);
                return true;
            }
            if (i == 3) {
                handleEventDeviceAlignedWithSatellite(((Boolean) message.obj).booleanValue());
                return true;
            }
            if (i != 5) {
                return true;
            }
            DemoSimulator demoSimulator2 = DemoSimulator.this;
            demoSimulator2.transitionTo(demoSimulator2.mNotConnectedState);
            return true;
        }

        private void handleEventDeviceAlignedWithSatellite(boolean z) {
            if (z || DemoSimulator.this.hasMessages(5)) {
                if (z && DemoSimulator.this.hasMessages(5)) {
                    DemoSimulator.this.logd("ConnectedState: handleEventAlignedWithSatellite isAligned=true. Remove EVENT_DEVICE_NOT_ALIGNED message.");
                    DemoSimulator.this.removeMessages(5);
                    return;
                }
                return;
            }
            long demoPointingNotAlignedDurationMillis = DemoSimulator.this.mSatelliteController.getDemoPointingNotAlignedDurationMillis();
            DemoSimulator.this.logd("ConnectedState: handleEventAlignedWithSatellite isAligned=false. Send delayed EVENT_DEVICE_NOT_ALIGNED message in durationMillis=" + demoPointingNotAlignedDurationMillis);
            DemoSimulator.this.sendMessageDelayed(5, demoPointingNotAlignedDurationMillis);
        }
    }

    @Override // com.android.internal.telephony.StateMachine
    protected String getWhatToString(int i) {
        if (i == 1) {
            return "EVENT_SATELLITE_MODE_ON";
        }
        if (i == 2) {
            return "EVENT_SATELLITE_MODE_OFF";
        }
        if (i == 3) {
            return "EVENT_DEVICE_ALIGNED_WITH_SATELLITE";
        }
        if (i == 4) {
            return "EVENT_DEVICE_ALIGNED";
        }
        if (i == 5) {
            return "EVENT_DEVICE_NOT_ALIGNED";
        }
        return "UNKNOWN EVENT " + i;
    }

    public void setSatelliteListener(ISatelliteListener iSatelliteListener) {
        this.mISatelliteListener = iSatelliteListener;
    }

    public void enableTerrestrialNetworkScanWhileSatelliteModeIsOn(boolean z, IIntegerConsumer iIntegerConsumer) {
        try {
            iIntegerConsumer.accept(0);
        } catch (RemoteException e) {
            loge("enableTerrestrialNetworkScanWhileSatelliteModeIsOn: RemoteException " + e);
        }
    }

    public void requestSatelliteSuspended(boolean z, IIntegerConsumer iIntegerConsumer) {
        try {
            iIntegerConsumer.accept(0);
        } catch (RemoteException e) {
            loge("requestSatelliteSuspended: RemoteException " + e);
        }
    }

    public void onSatelliteModeOn() {
        if (this.mSatelliteController.isDemoModeEnabled()) {
            sendMessage(1);
        }
    }

    public void onSatelliteModeOff() {
        sendMessage(2);
    }

    protected void setDeviceAlignedWithSatellite(boolean z) {
        if (this.mSatelliteController.isDemoModeEnabled()) {
            this.mIsAligned.set(z);
            sendMessage(3, Boolean.valueOf(z));
        }
    }
}
