package com.android.phone.ntn;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.phone.PhoneGlobals;

/* JADX INFO: loaded from: classes.dex */
class NtnStackStateMachine extends StateMachine {
    public static final String NAME = NtnStackStateMachine.class.getName();
    public static final Context sContext = PhoneGlobals.getInstance();
    private NtnAudioServiceManager mCsAudioManager;
    private final IdleState mIdleState;
    private boolean mIsRadioPoweredOn;
    private boolean mIsStackTurnedon;
    private final OfflineState mOfflineState;
    private final OnlineState mOnlineState;

    protected NtnStackStateMachine(String str, Looper looper) {
        super(str, looper);
        IdleState idleState = new IdleState();
        this.mIdleState = idleState;
        OfflineState offlineState = new OfflineState();
        this.mOfflineState = offlineState;
        OnlineState onlineState = new OnlineState();
        this.mOnlineState = onlineState;
        this.mCsAudioManager = new NtnAudioServiceManager();
        addState(idleState);
        addState(offlineState);
        addState(onlineState);
        setInitialState(idleState);
        start();
    }

    abstract class NtnState extends State {
        public abstract String getName();

        NtnState() {
        }

        public void enter() {
            super.enter();
            NtnLogUtils.logd("NtnStackStateMachine", getName() + " entered! mIsStackTurnedon=" + NtnStackStateMachine.this.mIsStackTurnedon + ", mIsRadioPoweredOn:" + NtnStackStateMachine.this.mIsRadioPoweredOn);
        }

        public boolean processMessage(Message message) {
            NtnLogUtils.logd("NtnStackStateMachine", getName() + " handleMessage: " + NtnStackStateMachine.this.convertMessageIdToString(message.what));
            if (message.what == 1 && NtnStackStateMachine.this.mIsStackTurnedon) {
                NtnBoxServiceManager.getInstance().clearBeforeNtnPhoneDestroyed();
                NtnPhoneProxyBase.getInstance().destroyNtnPhone();
                NtnStackStateMachine.this.bootNtnStack(false);
                NtnStackStateMachine.this.mIsStackTurnedon = false;
                NtnStackStateMachine.this.mIsRadioPoweredOn = false;
            }
            return false;
        }

        public void exit() {
            super.exit();
            NtnLogUtils.logd("NtnStackStateMachine", getName() + " exit!");
        }
    }

    class IdleState extends NtnState {
        IdleState() {
            super();
        }

        @Override // com.android.phone.ntn.NtnStackStateMachine.NtnState
        public String getName() {
            return "IdleState";
        }

        @Override // com.android.phone.ntn.NtnStackStateMachine.NtnState
        public boolean processMessage(Message message) {
            if (super.processMessage(message)) {
                return true;
            }
            NtnLogUtils.logd("NtnStackStateMachine", getName() + " handleMessage: " + NtnStackStateMachine.this.convertMessageIdToString(message.what));
            if (message.what != 0) {
                return false;
            }
            if (NtnStackStateMachine.this.mIsStackTurnedon) {
                NtnLogUtils.logw("NtnStackStateMachine", "IdleState stack already on, enter OfflineState!");
                NtnStackStateMachine ntnStackStateMachine = NtnStackStateMachine.this;
                ntnStackStateMachine.transitionTo(ntnStackStateMachine.mOfflineState);
                return true;
            }
            if (!NtnStackStateMachine.this.bootNtnStack(true)) {
                NtnLogUtils.logd("NtnStackStateMachine", "IdleState handleMessage bootNtnStack failed");
            } else {
                NtnPhoneProxyBase.getInstance().startNtnPhone();
                NtnBoxServiceManager.getInstance().onNtnPhoneCreated();
                NtnStackStateMachine.this.mIsStackTurnedon = true;
                NtnStackStateMachine ntnStackStateMachine2 = NtnStackStateMachine.this;
                ntnStackStateMachine2.transitionTo(ntnStackStateMachine2.mOfflineState);
            }
            return true;
        }
    }

    class OfflineState extends NtnState {
        OfflineState() {
            super();
        }

        @Override // com.android.phone.ntn.NtnStackStateMachine.NtnState
        public String getName() {
            return "OfflineState";
        }

        @Override // com.android.phone.ntn.NtnStackStateMachine.NtnState
        public boolean processMessage(Message message) {
            if (super.processMessage(message)) {
                return true;
            }
            NtnLogUtils.logd("NtnStackStateMachine", getName() + " handleMessage: " + NtnStackStateMachine.this.convertMessageIdToString(message.what));
            int i = message.what;
            if (i == 1) {
                NtnStackStateMachine.this.mIsRadioPoweredOn = false;
                NtnStackStateMachine ntnStackStateMachine = NtnStackStateMachine.this;
                ntnStackStateMachine.transitionTo(ntnStackStateMachine.mIdleState);
                return true;
            }
            if (i == 2) {
                if (NtnStackStateMachine.this.mIsRadioPoweredOn) {
                    NtnStackStateMachine.this.setNtnPhoneRadio(false, false);
                    NtnStackStateMachine.this.mIsRadioPoweredOn = false;
                }
                return true;
            }
            if (i != 3) {
                return false;
            }
            if (!NtnStackStateMachine.this.mIsRadioPoweredOn) {
                NtnStackStateMachine.this.setNtnPhoneRadio(true, true);
                NtnStackStateMachine.this.mIsRadioPoweredOn = true;
            }
            NtnStackStateMachine ntnStackStateMachine2 = NtnStackStateMachine.this;
            ntnStackStateMachine2.transitionTo(ntnStackStateMachine2.mOnlineState);
            return true;
        }
    }

    class OnlineState extends NtnState {
        OnlineState() {
            super();
        }

        @Override // com.android.phone.ntn.NtnStackStateMachine.NtnState
        public String getName() {
            return "OnlineState";
        }

        @Override // com.android.phone.ntn.NtnStackStateMachine.NtnState
        public boolean processMessage(Message message) {
            if (super.processMessage(message)) {
                return true;
            }
            NtnLogUtils.logd("NtnStackStateMachine", getName() + " handleMessage: " + NtnStackStateMachine.this.convertMessageIdToString(message.what));
            int i = message.what;
            if (i == 1) {
                NtnStackStateMachine.this.mIsRadioPoweredOn = false;
                NtnStackStateMachine ntnStackStateMachine = NtnStackStateMachine.this;
                ntnStackStateMachine.transitionTo(ntnStackStateMachine.mIdleState);
                return true;
            }
            if (i != 2) {
                if (i != 3) {
                    return false;
                }
                if (!NtnStackStateMachine.this.mIsRadioPoweredOn) {
                    NtnStackStateMachine.this.setNtnPhoneRadio(true, false);
                    NtnStackStateMachine.this.mIsRadioPoweredOn = true;
                }
                return true;
            }
            if (NtnStackStateMachine.this.mIsRadioPoweredOn) {
                NtnStackStateMachine.this.setNtnPhoneRadio(false, true);
                NtnStackStateMachine.this.mIsRadioPoweredOn = false;
            }
            NtnStackStateMachine ntnStackStateMachine2 = NtnStackStateMachine.this;
            ntnStackStateMachine2.transitionTo(ntnStackStateMachine2.mOfflineState);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean bootNtnStack(boolean z) {
        try {
            NtnLogUtils.logd("NtnStackStateMachine", "bootNtnStack state:" + (z ? 1 : 0));
            SystemProperties.set("persist.radio.ntn_stack_on", Integer.toString(z ? 1 : 0));
            if (SystemProperties.getInt("persist.radio.ntn_stack_on", -1) != z) {
                NtnLogUtils.logd("NtnStackStateMachine", "set prop failed!");
                return false;
            }
            Thread.sleep(200L);
            this.mIsStackTurnedon = z;
            return true;
        } catch (InterruptedException e) {
            NtnLogUtils.loge("NtnStackStateMachine", "bootNtnStack InterruptedException: " + e);
            return false;
        } catch (Exception e2) {
            NtnLogUtils.loge("NtnStackStateMachine", "bootNtnStack: " + e2);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setNtnPhoneRadio(final boolean z, final boolean z2) {
        new Handler().postDelayed(new Runnable() { // from class: com.android.phone.ntn.NtnStackStateMachine.1
            @Override // java.lang.Runnable
            public void run() {
                NtnPhoneProxyBase.getInstance().setRadioPower(z, false, false, z2);
                NtnStackStateMachine.this.mCsAudioManager.launchAudioServiceManager(z);
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String convertMessageIdToString(int i) {
        if (i == 0) {
            return "SWITCH_STACK_START";
        }
        if (i == 1) {
            return "SWITCH_STACK_SHUTDOWN";
        }
        if (i == 2) {
            return "SWITCH_RADIOPOWER_OFF";
        }
        if (i == 3) {
            return "SWITCH_RADIOPOWER_ON";
        }
        return String.valueOf(i);
    }
}
