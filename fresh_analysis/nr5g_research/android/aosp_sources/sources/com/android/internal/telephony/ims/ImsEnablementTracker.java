package com.android.internal.telephony.ims;

import android.content.ComponentName;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.ims.aidl.IImsServiceController;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.telephony.IState;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.satellite.SatelliteSessionController;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ImsEnablementTracker {
    public static final int COMMAND_DISABLE_MSG = 2;
    public static final int COMMAND_ENABLE_MSG = 1;
    public static final int COMMAND_POST_RESETTING_DONE = 10;
    public static final int COMMAND_RESETTING_DONE = 6;
    private static final Map EVENT_DESCRIPTION;
    protected static final int STATE_IMS_DEFAULT = 1;
    protected static final int STATE_IMS_DISABLED = 4;
    protected static final int STATE_IMS_DISABLING = 3;
    protected static final int STATE_IMS_DISCONNECTED = 0;
    protected static final int STATE_IMS_ENABLED = 2;
    protected static final int STATE_IMS_ENABLING = 5;
    protected static final int STATE_IMS_POSTRESETTING = 7;
    protected static final int STATE_IMS_RESETTING = 6;
    private final ComponentName mComponentName;
    private IImsServiceController mIImsServiceController;
    private long mLastImsOperationTimeMs;
    protected final Object mLock;
    private final Looper mLooper;
    private final int mState;
    private final SparseArray mStateMachines;

    static {
        HashMap map = new HashMap();
        EVENT_DESCRIPTION = map;
        map.put(0, "COMMAND_NONE_MSG");
        map.put(1, "COMMAND_ENABLE_MSG");
        map.put(2, "COMMAND_DISABLE_MSG");
        map.put(3, "COMMAND_RESET_MSG");
        map.put(4, "COMMAND_ENABLING_DONE");
        map.put(5, "COMMAND_DISABLING_DONE");
        map.put(6, "COMMAND_RESETTING_DONE");
        map.put(7, "COMMAND_CONNECTED_MSG");
        map.put(8, "COMMAND_DISCONNECTED_MSG");
        map.put(9, "COMMAND_INVALID_SUBID_MSG");
    }

    class ImsEnablementTrackerStateMachine extends StateMachine {
        public final Default mDefault;
        public final Disabled mDisabled;
        public final Disabling mDisabling;
        private final Disconnected mDisconnected;
        public final Enabled mEnabled;
        public final Enabling mEnabling;
        private int mLastMsg;
        private final int mPhoneId;
        public final PostResetting mPostResetting;
        private IState mPreviousState;
        public final Resetting mResetting;
        private int mSlotId;
        private int mSubId;

        ImsEnablementTrackerStateMachine(String str, Looper looper, int i, int i2) {
            super(str, looper);
            this.mLastMsg = 0;
            this.mPhoneId = i2;
            Default r5 = new Default();
            this.mDefault = r5;
            Enabled enabled = new Enabled();
            this.mEnabled = enabled;
            Disabling disabling = new Disabling();
            this.mDisabling = disabling;
            Disabled disabled = new Disabled();
            this.mDisabled = disabled;
            Enabling enabling = new Enabling();
            this.mEnabling = enabling;
            Resetting resetting = new Resetting();
            this.mResetting = resetting;
            Disconnected disconnected = new Disconnected();
            this.mDisconnected = disconnected;
            PostResetting postResetting = new PostResetting();
            this.mPostResetting = postResetting;
            addState(r5);
            addState(enabled);
            addState(disabling);
            addState(disabled);
            addState(enabling);
            addState(resetting);
            addState(disconnected);
            addState(postResetting);
            setInitialState(getState(i));
            this.mPreviousState = getState(i);
        }

        public void clearAllMessage() {
            Log.d("ImsEnablementTracker", "clearAllMessage");
            removeMessages(1);
            removeMessages(2);
            removeMessages(3);
            removeMessages(4);
            removeMessages(5);
            removeMessages(6);
            removeMessages(10);
        }

        public void serviceBinderConnected() {
            clearAllMessage();
            sendMessage(7);
        }

        public void serviceBinderDisconnected() {
            clearAllMessage();
            sendMessage(8);
        }

        public boolean isState(int i) {
            IState iState;
            switch (i) {
                case 0:
                    iState = this.mDisconnected;
                    break;
                case 1:
                    iState = this.mDefault;
                    break;
                case 2:
                    iState = this.mEnabled;
                    break;
                case 3:
                    iState = this.mDisabling;
                    break;
                case 4:
                    iState = this.mDisabled;
                    break;
                case 5:
                    iState = this.mEnabling;
                    break;
                case 6:
                    iState = this.mResetting;
                    break;
                case 7:
                    iState = this.mPostResetting;
                    break;
                default:
                    iState = null;
                    break;
            }
            return getCurrentState() == iState;
        }

        private State getState(int i) {
            if (i != 0) {
                switch (i) {
                    case 2:
                        return this.mEnabled;
                    case 3:
                        return this.mDisabling;
                    case 4:
                        return this.mDisabled;
                    case 5:
                        return this.mEnabling;
                    case 6:
                        return this.mResetting;
                    case 7:
                        return this.mPostResetting;
                    default:
                        return this.mDefault;
                }
            }
            return this.mDisconnected;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void handleInvalidSubIdMessage() {
            clearAllMessage();
            transitionState(this.mDefault);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void transitionState(State state) {
            this.mPreviousState = getCurrentState();
            transitionTo(state);
        }

        class Default extends State {
            Default() {
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void enter() {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Default state:enter");
                ImsEnablementTrackerStateMachine.this.mSubId = -1;
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public boolean processMessage(Message message) {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Default state:processMessage. msg.what=" + ((String) ImsEnablementTracker.EVENT_DESCRIPTION.get(Integer.valueOf(message.what))) + ",component:" + ImsEnablementTracker.this.mComponentName);
                int i = message.what;
                if (i == 1) {
                    ImsEnablementTracker.this.sendEnableIms(message.arg1, message.arg2);
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mEnabled);
                    return true;
                }
                if (i == 2) {
                    ImsEnablementTracker.this.sendDisableIms(message.arg1, message.arg2);
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine2.transitionState(imsEnablementTrackerStateMachine2.mDisabled);
                    return true;
                }
                if (i != 3) {
                    if (i != 8) {
                        return false;
                    }
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine3 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine3.transitionState(imsEnablementTrackerStateMachine3.mDisconnected);
                    return true;
                }
                ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine4 = ImsEnablementTrackerStateMachine.this;
                imsEnablementTrackerStateMachine4.transitionState(imsEnablementTrackerStateMachine4.mResetting);
                return true;
            }
        }

        class Enabled extends State {
            Enabled() {
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void enter() {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Enabled state:enter");
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public boolean processMessage(Message message) {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Enabled state:processMessage. msg.what=" + ((String) ImsEnablementTracker.EVENT_DESCRIPTION.get(Integer.valueOf(message.what))) + ",component:" + ImsEnablementTracker.this.mComponentName);
                int i = message.what;
                if (i == 2) {
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mDisabling);
                    return true;
                }
                if (i == 3) {
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine2.transitionState(imsEnablementTrackerStateMachine2.mResetting);
                    return true;
                }
                if (i == 8) {
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine3 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine3.transitionState(imsEnablementTrackerStateMachine3.mDisconnected);
                    return true;
                }
                if (i != 9) {
                    return false;
                }
                ImsEnablementTrackerStateMachine.this.handleInvalidSubIdMessage();
                return true;
            }
        }

        class Disabling extends State {
            Disabling() {
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void enter() {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Disabling state:enter");
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                imsEnablementTrackerStateMachine.sendMessageDelayed(5, imsEnablementTrackerStateMachine.mSlotId, ImsEnablementTrackerStateMachine.this.mSubId, ImsEnablementTracker.this.getRemainThrottleTime());
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public boolean processMessage(Message message) {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Disabling state:processMessage. msg.what=" + ((String) ImsEnablementTracker.EVENT_DESCRIPTION.get(Integer.valueOf(message.what))) + ",component:" + ImsEnablementTracker.this.mComponentName);
                int i = message.what;
                if (i == 1) {
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    ImsEnablementTrackerStateMachine.this.clearAllMessage();
                    IState iState = ImsEnablementTrackerStateMachine.this.mPreviousState;
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                    if (iState == imsEnablementTrackerStateMachine.mResetting) {
                        imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mEnabling);
                    } else {
                        imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mEnabled);
                    }
                    return true;
                }
                if (i == 3) {
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    ImsEnablementTrackerStateMachine.this.clearAllMessage();
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine2.transitionState(imsEnablementTrackerStateMachine2.mResetting);
                    return true;
                }
                if (i == 5) {
                    ImsEnablementTrackerStateMachine.this.removeMessages(2);
                    ImsEnablementTracker.this.sendDisableIms(message.arg1, message.arg2);
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine3 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine3.transitionState(imsEnablementTrackerStateMachine3.mDisabled);
                    return true;
                }
                if (i == 8) {
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine4 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine4.transitionState(imsEnablementTrackerStateMachine4.mDisconnected);
                    return true;
                }
                if (i != 9) {
                    return false;
                }
                ImsEnablementTrackerStateMachine.this.handleInvalidSubIdMessage();
                return true;
            }
        }

        class Disabled extends State {
            Disabled() {
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void enter() {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Disabled state:enter");
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public boolean processMessage(Message message) {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Disabled state:processMessage. msg.what=" + ((String) ImsEnablementTracker.EVENT_DESCRIPTION.get(Integer.valueOf(message.what))) + ",component:" + ImsEnablementTracker.this.mComponentName);
                int i = message.what;
                if (i == 1) {
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mEnabling);
                    return true;
                }
                if (i == 3) {
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine2.transitionState(imsEnablementTrackerStateMachine2.mResetting);
                    return true;
                }
                if (i == 8) {
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine3 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine3.transitionState(imsEnablementTrackerStateMachine3.mDisconnected);
                    return true;
                }
                if (i != 9) {
                    return false;
                }
                ImsEnablementTrackerStateMachine.this.handleInvalidSubIdMessage();
                return true;
            }
        }

        class Enabling extends State {
            Enabling() {
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void enter() {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Enabling state:enter");
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                imsEnablementTrackerStateMachine.sendMessageDelayed(4, imsEnablementTrackerStateMachine.mSlotId, ImsEnablementTrackerStateMachine.this.mSubId, ImsEnablementTracker.this.getRemainThrottleTime());
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public boolean processMessage(Message message) {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Enabling state:processMessage. msg.what=" + ((String) ImsEnablementTracker.EVENT_DESCRIPTION.get(Integer.valueOf(message.what))) + ",component:" + ImsEnablementTracker.this.mComponentName);
                int i = message.what;
                if (i == 2) {
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    ImsEnablementTrackerStateMachine.this.clearAllMessage();
                    IState iState = ImsEnablementTrackerStateMachine.this.mPreviousState;
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                    if (iState == imsEnablementTrackerStateMachine.mResetting) {
                        imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mDisabling);
                    } else {
                        imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mDisabled);
                    }
                    return true;
                }
                if (i == 3) {
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine2.transitionState(imsEnablementTrackerStateMachine2.mResetting);
                    return true;
                }
                if (i == 4) {
                    ImsEnablementTrackerStateMachine.this.removeMessages(1);
                    ImsEnablementTracker.this.sendEnableIms(message.arg1, message.arg2);
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine3 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine3.transitionState(imsEnablementTrackerStateMachine3.mEnabled);
                    return true;
                }
                if (i == 8) {
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine4 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine4.transitionState(imsEnablementTrackerStateMachine4.mDisconnected);
                    return true;
                }
                if (i != 9) {
                    return false;
                }
                ImsEnablementTrackerStateMachine.this.handleInvalidSubIdMessage();
                return true;
            }
        }

        class Resetting extends State {
            Resetting() {
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void enter() {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Resetting state:enter");
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                imsEnablementTrackerStateMachine.sendMessageDelayed(6, imsEnablementTrackerStateMachine.mSlotId, ImsEnablementTrackerStateMachine.this.mSubId, ImsEnablementTracker.this.getRemainThrottleTime());
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public boolean processMessage(Message message) {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Resetting state:processMessage. msg.what=" + ((String) ImsEnablementTracker.EVENT_DESCRIPTION.get(Integer.valueOf(message.what))) + ",component:" + ImsEnablementTracker.this.mComponentName);
                int i = message.what;
                if (i == 1) {
                    ImsEnablementTrackerStateMachine.this.mLastMsg = 1;
                    return true;
                }
                if (i == 2) {
                    ImsEnablementTrackerStateMachine.this.mLastMsg = 2;
                    return true;
                }
                if (i != 6) {
                    if (i == 8) {
                        ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                        imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mDisconnected);
                        return true;
                    }
                    if (i != 9) {
                        return false;
                    }
                    ImsEnablementTrackerStateMachine.this.handleInvalidSubIdMessage();
                    return true;
                }
                ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                ImsEnablementTrackerStateMachine.this.removeMessages(3);
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = ImsEnablementTrackerStateMachine.this;
                ImsEnablementTracker.this.sendResetIms(imsEnablementTrackerStateMachine2.mSlotId, ImsEnablementTrackerStateMachine.this.mSubId);
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine3 = ImsEnablementTrackerStateMachine.this;
                imsEnablementTrackerStateMachine3.transitionState(imsEnablementTrackerStateMachine3.mPostResetting);
                return true;
            }
        }

        class Disconnected extends State {
            private int mLastMsg = 0;

            Disconnected() {
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void enter() {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Disconnected state:enter");
                ImsEnablementTrackerStateMachine.this.clearAllMessage();
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public boolean processMessage(Message message) {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]Disconnected state:processMessage. msg.what=" + ((String) ImsEnablementTracker.EVENT_DESCRIPTION.get(Integer.valueOf(message.what))) + ",component:" + ImsEnablementTracker.this.mComponentName);
                int i = message.what;
                if (i == 1 || i == 2 || i == 3) {
                    this.mLastMsg = i;
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    return true;
                }
                if (i != 7) {
                    return false;
                }
                ImsEnablementTrackerStateMachine.this.clearAllMessage();
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mDefault);
                int i2 = this.mLastMsg;
                if (i2 != 0) {
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = ImsEnablementTrackerStateMachine.this;
                    imsEnablementTrackerStateMachine2.sendMessageDelayed(i2, imsEnablementTrackerStateMachine2.mSlotId, ImsEnablementTrackerStateMachine.this.mSubId, 0L);
                    this.mLastMsg = 0;
                }
                return true;
            }
        }

        class PostResetting extends State {
            PostResetting() {
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void enter() {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]PostResetting state:enter");
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                imsEnablementTrackerStateMachine.sendMessageDelayed(10, imsEnablementTrackerStateMachine.mSlotId, ImsEnablementTrackerStateMachine.this.mSubId, ImsEnablementTracker.this.getRemainThrottleTime());
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public void exit() {
                ImsEnablementTrackerStateMachine.this.mLastMsg = 0;
            }

            @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
            public boolean processMessage(Message message) {
                Log.d("ImsEnablementTracker", "[" + ImsEnablementTrackerStateMachine.this.mPhoneId + "]PostResetting state:processMessage. msg.what=" + ((String) ImsEnablementTracker.EVENT_DESCRIPTION.get(Integer.valueOf(message.what))) + ",component:" + ImsEnablementTracker.this.mComponentName);
                int i = message.what;
                if (i == 1 || i == 2) {
                    ImsEnablementTrackerStateMachine.this.mLastMsg = i;
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    return true;
                }
                if (i == 3) {
                    ImsEnablementTrackerStateMachine.this.mLastMsg = 1;
                    ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                    ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                    return true;
                }
                switch (i) {
                    case 8:
                        ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = ImsEnablementTrackerStateMachine.this;
                        imsEnablementTrackerStateMachine.transitionState(imsEnablementTrackerStateMachine.mDisconnected);
                        return true;
                    case 9:
                        ImsEnablementTrackerStateMachine.this.handleInvalidSubIdMessage();
                        return true;
                    case 10:
                        ImsEnablementTrackerStateMachine.this.mSlotId = message.arg1;
                        ImsEnablementTrackerStateMachine.this.mSubId = message.arg2;
                        int i2 = ImsEnablementTrackerStateMachine.this.mLastMsg;
                        ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = ImsEnablementTrackerStateMachine.this;
                        if (i2 == 2) {
                            ImsEnablementTracker.this.sendDisableIms(imsEnablementTrackerStateMachine2.mSlotId, ImsEnablementTrackerStateMachine.this.mSubId);
                            ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine3 = ImsEnablementTrackerStateMachine.this;
                            imsEnablementTrackerStateMachine3.transitionState(imsEnablementTrackerStateMachine3.mDisabled);
                        } else {
                            ImsEnablementTracker.this.sendEnableIms(imsEnablementTrackerStateMachine2.mSlotId, ImsEnablementTrackerStateMachine.this.mSubId);
                            ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine4 = ImsEnablementTrackerStateMachine.this;
                            imsEnablementTrackerStateMachine4.transitionState(imsEnablementTrackerStateMachine4.mEnabled);
                        }
                        return true;
                    default:
                        return false;
                }
            }
        }
    }

    public ImsEnablementTracker(Looper looper, ComponentName componentName) {
        this.mLock = new Object();
        this.mLastImsOperationTimeMs = 0L;
        this.mIImsServiceController = null;
        this.mStateMachines = new SparseArray();
        this.mLooper = looper;
        this.mState = 0;
        this.mComponentName = componentName;
    }

    public ImsEnablementTracker(Looper looper, IImsServiceController iImsServiceController, int i, int i2) {
        this.mLock = new Object();
        this.mLastImsOperationTimeMs = 0L;
        this.mIImsServiceController = iImsServiceController;
        this.mStateMachines = new SparseArray();
        this.mLooper = looper;
        this.mState = i;
        this.mComponentName = null;
        setNumOfSlots(i2);
    }

    public void setNumOfSlots(int i) {
        int size = this.mStateMachines.size();
        Log.d("ImsEnablementTracker", "set the slots: old[" + size + "], new[" + i + "],component:" + this.mComponentName);
        if (i == size) {
            return;
        }
        if (size < i) {
            int i2 = size;
            while (i2 < i) {
                ImsEnablementTracker imsEnablementTracker = this;
                ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = imsEnablementTracker.new ImsEnablementTrackerStateMachine("ImsEnablementTracker", this.mLooper, this.mState, i2);
                imsEnablementTrackerStateMachine.start();
                imsEnablementTracker.mStateMachines.put(i2, imsEnablementTrackerStateMachine);
                i2++;
                this = imsEnablementTracker;
            }
            return;
        }
        if (size <= i) {
            return;
        }
        while (true) {
            size--;
            if (size <= i - 1) {
                return;
            }
            ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine2 = (ImsEnablementTrackerStateMachine) this.mStateMachines.get(size);
            this.mStateMachines.remove(size);
            imsEnablementTrackerStateMachine2.quitNow();
        }
    }

    public Handler getHandler(int i) {
        return ((ImsEnablementTrackerStateMachine) this.mStateMachines.get(i)).getHandler();
    }

    public boolean isState(int i, int i2) {
        return ((ImsEnablementTrackerStateMachine) this.mStateMachines.get(i)).isState(i2);
    }

    public void subIdChangedToInvalid(int i) {
        Log.d("ImsEnablementTracker", "[" + i + "] subId changed to invalid, component:" + this.mComponentName);
        ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = (ImsEnablementTrackerStateMachine) this.mStateMachines.get(i);
        if (imsEnablementTrackerStateMachine != null) {
            imsEnablementTrackerStateMachine.sendMessage(9, i);
        } else {
            Log.w("ImsEnablementTracker", "There is no state machine associated with this slotId.");
        }
    }

    public void enableIms(int i, int i2) {
        Log.d("ImsEnablementTracker", "[" + i + "][" + i2 + "]enableIms, component:" + this.mComponentName);
        ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = (ImsEnablementTrackerStateMachine) this.mStateMachines.get(i);
        if (imsEnablementTrackerStateMachine != null) {
            imsEnablementTrackerStateMachine.sendMessage(1, i, i2);
        } else {
            Log.w("ImsEnablementTracker", "There is no state machine associated with this slotId.");
        }
    }

    public void disableIms(int i, int i2) {
        Log.d("ImsEnablementTracker", "[" + i + "][" + i2 + "]disableIms, component:" + this.mComponentName);
        ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = (ImsEnablementTrackerStateMachine) this.mStateMachines.get(i);
        if (imsEnablementTrackerStateMachine != null) {
            imsEnablementTrackerStateMachine.sendMessage(2, i, i2);
        } else {
            Log.w("ImsEnablementTracker", "There is no state machine associated with this slotId.");
        }
    }

    public void resetIms(int i, int i2) {
        Log.d("ImsEnablementTracker", "[" + i + "][" + i2 + "]resetIms, component:" + this.mComponentName);
        ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = (ImsEnablementTrackerStateMachine) this.mStateMachines.get(i);
        if (imsEnablementTrackerStateMachine != null) {
            imsEnablementTrackerStateMachine.sendMessage(3, i, i2);
        } else {
            Log.w("ImsEnablementTracker", "There is no state machine associated with this slotId.");
        }
    }

    protected void setServiceController(IBinder iBinder) {
        synchronized (this.mLock) {
            try {
                this.mIImsServiceController = IImsServiceController.Stub.asInterface(iBinder);
                Log.d("ImsEnablementTracker", "setServiceController with Binder:" + this.mIImsServiceController + ", component:" + this.mComponentName);
                for (int i = 0; i < this.mStateMachines.size(); i++) {
                    ImsEnablementTrackerStateMachine imsEnablementTrackerStateMachine = (ImsEnablementTrackerStateMachine) this.mStateMachines.get(i);
                    if (imsEnablementTrackerStateMachine == null) {
                        Log.w("ImsEnablementTracker", "There is no state machine associated withthe slotId[" + i + "]");
                    } else if (isServiceControllerAvailable()) {
                        imsEnablementTrackerStateMachine.serviceBinderConnected();
                    } else {
                        imsEnablementTrackerStateMachine.serviceBinderDisconnected();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected long getLastOperationTimeMillis() {
        return this.mLastImsOperationTimeMs;
    }

    public long getRemainThrottleTime() {
        long jCurrentTimeMillis = SatelliteSessionController.DEMO_MODE_SATELLITE_STAY_AT_LISTENING_MILLIS - (System.currentTimeMillis() - getLastOperationTimeMillis());
        if (jCurrentTimeMillis < 0) {
            jCurrentTimeMillis = 0;
        }
        Log.d("ImsEnablementTracker", "getRemainThrottleTime:" + jCurrentTimeMillis);
        return jCurrentTimeMillis;
    }

    private boolean isServiceControllerAvailable() {
        if (this.mIImsServiceController != null) {
            return true;
        }
        Log.d("ImsEnablementTracker", "isServiceControllerAvailable : binder is not alive");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendEnableIms(int i, int i2) {
        try {
            synchronized (this.mLock) {
                try {
                    if (isServiceControllerAvailable()) {
                        Log.d("ImsEnablementTracker", "[" + i + "][" + i2 + "]sendEnableIms,componentName[" + this.mComponentName + "]");
                        this.mIImsServiceController.enableIms(i, i2);
                        this.mLastImsOperationTimeMs = System.currentTimeMillis();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (RemoteException e) {
            Log.w("ImsEnablementTracker", "Couldn't enable IMS: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendDisableIms(int i, int i2) {
        try {
            synchronized (this.mLock) {
                try {
                    if (isServiceControllerAvailable()) {
                        Log.d("ImsEnablementTracker", "[" + i + "][" + i2 + "]sendDisableIms componentName[" + this.mComponentName + "]");
                        this.mIImsServiceController.disableIms(i, i2);
                        this.mLastImsOperationTimeMs = System.currentTimeMillis();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (RemoteException e) {
            Log.w("ImsEnablementTracker", "Couldn't disable IMS: " + e.getMessage());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendResetIms(int i, int i2) {
        try {
            synchronized (this.mLock) {
                try {
                    if (isServiceControllerAvailable()) {
                        Log.d("ImsEnablementTracker", "[" + i + "][" + i2 + "]sendResetIms");
                        this.mIImsServiceController.resetIms(i, i2);
                        this.mLastImsOperationTimeMs = System.currentTimeMillis();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        } catch (RemoteException e) {
            Log.w("ImsEnablementTracker", "Couldn't reset IMS: " + e.getMessage());
        }
    }
}
