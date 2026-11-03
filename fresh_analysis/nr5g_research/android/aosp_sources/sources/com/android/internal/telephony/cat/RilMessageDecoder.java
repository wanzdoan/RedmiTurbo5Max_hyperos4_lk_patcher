package com.android.internal.telephony.cat;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperCatManager;
import com.android.internal.telephony.State;
import com.android.internal.telephony.StateMachine;
import com.android.internal.telephony.TelephonyComponentFactory;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.IccUtils;

/* JADX INFO: loaded from: classes.dex */
public class RilMessageDecoder extends StateMachine {
    protected static final int CMD_PARAMS_READY = 2;
    protected static final int CMD_START = 1;
    protected static RilMessageDecoder[] mInstance;
    protected static int mSimCount;
    public Handler mCaller;
    protected CommandParamsFactory mCmdParamsFactory;
    protected RilMessage mCurrentRilMessage;
    private final Object mLock;
    protected StateCmdParamsReady mStateCmdParamsReady;
    protected StateStart mStateStart;

    protected boolean needResetAllInstance() {
        return true;
    }

    protected void sendSenssionEndWhenException(RilMessage rilMessage) {
    }

    public static synchronized RilMessageDecoder getInstance(Handler handler, IccFileHandler iccFileHandler, Context context, int i) {
        try {
            if (mInstance == null) {
                int supportedModemCount = TelephonyManager.getDefault().getSupportedModemCount();
                mSimCount = supportedModemCount;
                mInstance = new RilMessageDecoder[supportedModemCount];
                for (int i2 = 0; i2 < mSimCount; i2++) {
                    mInstance[i2] = null;
                }
            }
            if (i != -1 && i < mSimCount) {
                RilMessageDecoder[] rilMessageDecoderArr = mInstance;
                if (rilMessageDecoderArr[i] == null) {
                    rilMessageDecoderArr[i] = TelephonyComponentFactory.getInstance().inject(TelephonyComponentFactory.class.getName()).makeRilMessageDecoder(handler, iccFileHandler, context, i);
                }
                return mInstance[i];
            }
            CatLog.d("RilMessageDecoder", "invaild slot id: " + i);
            return null;
        } catch (Throwable th) {
            throw th;
        }
    }

    public void sendStartDecodingMessageParams(RilMessage rilMessage) {
        Message messageObtainMessage = obtainMessage(1);
        messageObtainMessage.obj = rilMessage;
        sendMessage(messageObtainMessage);
    }

    public void sendMsgParamsDecoded(ResultCode resultCode, CommandParams commandParams) {
        Message messageObtainMessage = obtainMessage(2);
        messageObtainMessage.arg1 = resultCode.value();
        messageObtainMessage.obj = commandParams;
        sendMessage(messageObtainMessage);
    }

    protected void sendCmdForExecution(RilMessage rilMessage) {
        synchronized (this.mLock) {
            try {
                Handler handler = this.mCaller;
                if (handler != null) {
                    handler.obtainMessage(10, new RilMessage(rilMessage)).sendToTarget();
                } else {
                    CatLog.d(this, "mCaller is null, return");
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public RilMessageDecoder(Handler handler, IccFileHandler iccFileHandler, Context context) {
        super("RilMessageDecoder");
        Object obj = new Object();
        this.mLock = obj;
        this.mCmdParamsFactory = null;
        this.mCurrentRilMessage = null;
        this.mCaller = null;
        this.mStateStart = new StateStart();
        this.mStateCmdParamsReady = new StateCmdParamsReady();
        addState(this.mStateStart);
        addState(this.mStateCmdParamsReady);
        setInitialState(this.mStateStart);
        synchronized (obj) {
            this.mCaller = handler;
            this.mCmdParamsFactory = CommandParamsFactory.getInstance(this, iccFileHandler, context);
        }
    }

    public RilMessageDecoder() {
        super("RilMessageDecoder");
        this.mLock = new Object();
        this.mCmdParamsFactory = null;
        this.mCurrentRilMessage = null;
        this.mCaller = null;
        this.mStateStart = new StateStart();
        this.mStateCmdParamsReady = new StateCmdParamsReady();
    }

    private class StateStart extends State {
        private StateStart() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (message.what == 1) {
                if (RilMessageDecoder.this.decodeMessageParams((RilMessage) message.obj)) {
                    RilMessageDecoder rilMessageDecoder = RilMessageDecoder.this;
                    rilMessageDecoder.transitionTo(rilMessageDecoder.mStateCmdParamsReady);
                }
            } else {
                CatLog.d(this, "StateStart unexpected expecting START=1 got " + message.what);
            }
            return true;
        }
    }

    private class StateCmdParamsReady extends State {
        private StateCmdParamsReady() {
        }

        @Override // com.android.internal.telephony.State, com.android.internal.telephony.IState
        public boolean processMessage(Message message) {
            if (message.what == 2) {
                RilMessageDecoder.this.mCurrentRilMessage.mResCode = ResultCode.fromInt(message.arg1);
                RilMessageDecoder rilMessageDecoder = RilMessageDecoder.this;
                RilMessage rilMessage = rilMessageDecoder.mCurrentRilMessage;
                rilMessage.mData = message.obj;
                rilMessageDecoder.sendCmdForExecution(rilMessage);
                RilMessageDecoder rilMessageDecoder2 = RilMessageDecoder.this;
                rilMessageDecoder2.transitionTo(rilMessageDecoder2.mStateStart);
                return true;
            }
            CatLog.d(this, "StateCmdParamsReady expecting CMD_PARAMS_READY=2 got " + message.what);
            RilMessageDecoder.this.deferMessage(message);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean decodeMessageParams(RilMessage rilMessage) {
        this.mCurrentRilMessage = rilMessage;
        int i = rilMessage.mId;
        boolean z = false;
        if (i != 1) {
            if (i != 2 && i != 3) {
                if (i != 4) {
                    if (i != 5) {
                        return false;
                    }
                }
            }
            try {
                byte[] bArrHexStringToBytes = IccUtils.hexStringToBytes((String) rilMessage.mData);
                synchronized (this.mLock) {
                    CommandParamsFactory commandParamsFactory = this.mCmdParamsFactory;
                    if (commandParamsFactory != null) {
                        try {
                            commandParamsFactory.make(BerTlv.decode(bArrHexStringToBytes));
                            z = true;
                        } catch (ResultException e) {
                            CatLog.d(this, "decodeMessageParams: caught ResultException e=" + e);
                            this.mCurrentRilMessage.mResCode = e.result();
                            IHyperCatManager iHyperCatManager = (IHyperCatManager) HyperTelephonyFactory.getInstance().initModule(IHyperCatManager.DEFAULT, new Object[0]);
                            if (iHyperCatManager.getCommandDetails() != null) {
                                this.mCurrentRilMessage.mData = iHyperCatManager.getCommandDetails();
                            }
                            sendCmdForExecution(this.mCurrentRilMessage);
                            sendSenssionEndWhenException(this.mCurrentRilMessage);
                            iHyperCatManager.setCommandDetails(null);
                        }
                    }
                }
            } catch (Exception unused) {
                CatLog.d(this, "decodeMessageParams dropping zombie messages");
            }
            return z;
        }
        rilMessage.mResCode = ResultCode.OK;
        sendCmdForExecution(rilMessage);
        return false;
    }

    public void dispose() {
        quitNow();
        this.mStateStart = null;
        this.mStateCmdParamsReady = null;
        synchronized (this.mLock) {
            try {
                CommandParamsFactory commandParamsFactory = this.mCmdParamsFactory;
                if (commandParamsFactory != null) {
                    commandParamsFactory.dispose();
                    this.mCmdParamsFactory = null;
                }
                this.mCaller = null;
            } catch (Throwable th) {
                throw th;
            }
        }
        this.mCurrentRilMessage = null;
        if (needResetAllInstance()) {
            mInstance = null;
        }
    }
}
