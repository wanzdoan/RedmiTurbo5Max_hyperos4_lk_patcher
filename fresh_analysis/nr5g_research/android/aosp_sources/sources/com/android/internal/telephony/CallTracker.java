package com.android.internal.telephony;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.text.TextUtils;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public abstract class CallTracker extends Handler {
    protected static final int EVENT_CALL_STATE_CHANGE = 2;
    protected static final int EVENT_CONFERENCE_RESULT = 11;
    protected static final int EVENT_ECT_RESULT = 13;
    protected static final int EVENT_EXIT_ECM_RESPONSE_CDMA = 14;
    protected static final int EVENT_EXIT_SCBM_RESPONSE_CDMA = 19;
    protected static final int EVENT_GET_LAST_CALL_FAIL_CAUSE = 5;
    protected static final int EVENT_OPERATION_COMPLETE = 4;
    protected static final int EVENT_POLL_CALLS_RESULT = 1;
    protected static final int EVENT_RADIO_AVAILABLE = 9;
    protected static final int EVENT_RADIO_NOT_AVAILABLE = 10;
    protected static final int EVENT_REPOLL_AFTER_DELAY = 3;
    protected static final int EVENT_SEPARATE_RESULT = 12;
    protected static final int EVENT_SWITCH_RESULT = 8;
    public CommandsInterface mCi;
    protected final FeatureFlags mFeatureFlags;
    protected Message mLastRelevantPoll;
    protected boolean mNeedsPoll;
    protected int mPendingOperations;
    protected ArrayList<Connection> mHandoverConnections = new ArrayList<>();
    protected boolean mNumberConverted = false;
    private final int VALID_COMPARE_LENGTH = 3;

    public void cleanupCalls() {
    }

    protected abstract Phone getPhone();

    public abstract PhoneConstants.State getState();

    @Override // android.os.Handler
    public abstract void handleMessage(Message message);

    protected abstract void handlePollCalls(AsyncResult asyncResult);

    protected abstract void log(String str);

    public abstract void registerForVoiceCallEnded(Handler handler, int i, Object obj);

    public abstract void registerForVoiceCallStarted(Handler handler, int i, Object obj);

    public abstract void unregisterForVoiceCallEnded(Handler handler);

    public abstract void unregisterForVoiceCallStarted(Handler handler);

    public CallTracker(FeatureFlags featureFlags) {
        this.mFeatureFlags = featureFlags;
    }

    protected void pollCallsWhenSafe() {
        this.mNeedsPoll = true;
        if (checkNoOperationsPending()) {
            Message messageObtainMessage = obtainMessage(1);
            this.mLastRelevantPoll = messageObtainMessage;
            this.mCi.getCurrentCalls(messageObtainMessage);
        }
    }

    protected void pollCallsAfterDelay() {
        if (!this.mCi.getHalVersion(6).greaterOrEqual(RIL.RADIO_HAL_VERSION_1_4)) {
            log("Skip polling because HAL_SERVICE_VOICE < RADIO_HAL_VERSION_1.4");
            return;
        }
        Message messageObtainMessage = obtainMessage();
        messageObtainMessage.what = 3;
        sendMessageDelayed(messageObtainMessage, 250L);
    }

    protected boolean isCommandExceptionRadioNotAvailable(Throwable th) {
        return th != null && (th instanceof CommandException) && ((CommandException) th).getCommandError() == CommandException.Error.RADIO_NOT_AVAILABLE;
    }

    protected boolean isPseudoDsdaCall() {
        return QtiCallTracker.isPseudoDsdaCall(getPhone());
    }

    protected Connection getHoConnection(DriverCall driverCall) {
        ArrayList<Connection> arrayList = this.mHandoverConnections;
        int size = arrayList.size();
        int i = 0;
        int i2 = 0;
        while (i2 < size) {
            Connection connection = arrayList.get(i2);
            i2++;
            Connection connection2 = connection;
            log("getHoConnection - compare number: hoConn= " + connection2.toString());
            String str = driverCall.number;
            if (str == null || str.isEmpty()) {
                break;
            }
            if (connection2.getAddress() != null && connection2.getAddress().equals(driverCall.number)) {
                log("getHoConnection: Handover connection match found = " + connection2.toString());
                return connection2;
            }
        }
        ArrayList<Connection> arrayList2 = this.mHandoverConnections;
        int size2 = arrayList2.size();
        while (i < size2) {
            Connection connection3 = arrayList2.get(i);
            i++;
            Connection connection4 = connection3;
            log("getHoConnection: compare state hoConn= " + connection4.toString());
            if (connection4.getStateBeforeHandover() == Call.stateFromDCState(driverCall.state)) {
                log("getHoConnection: Handover connection match found = " + connection4.toString());
                return connection4;
            }
        }
        return null;
    }

    protected void notifySrvccState(Call.SrvccState srvccState, ArrayList<Connection> arrayList) {
        if (srvccState == Call.SrvccState.STARTED && arrayList != null) {
            this.mHandoverConnections.addAll(arrayList);
        } else if (srvccState != Call.SrvccState.COMPLETED) {
            this.mHandoverConnections.clear();
        }
        log("notifySrvccState: state=" + srvccState.name() + ", mHandoverConnections= " + this.mHandoverConnections.toString());
    }

    protected void handleRadioAvailable() {
        pollCallsWhenSafe();
    }

    protected Message obtainNoPollCompleteMessage(int i) {
        this.mPendingOperations++;
        this.mLastRelevantPoll = null;
        return obtainMessage(i);
    }

    private boolean checkNoOperationsPending() {
        return this.mPendingOperations == 0;
    }

    protected String convertNumberIfNecessary(Phone phone, String str) {
        String[] stringArray;
        String strConcat;
        if (str == null) {
            return str;
        }
        PersistableBundle configForSubId = ((CarrierConfigManager) phone.getContext().getSystemService("carrier_config")).getConfigForSubId(phone.getSubId());
        if (configForSubId == null) {
            stringArray = null;
        } else if (shouldPerformInternationalNumberRemapping(phone, configForSubId)) {
            stringArray = configForSubId.getStringArray("international_roaming_dial_string_replace_string_array");
        } else {
            stringArray = configForSubId.getStringArray("dial_string_replace_string_array");
        }
        if (stringArray == null) {
            log("convertNumberIfNecessary convertMaps is null");
            return str;
        }
        log("convertNumberIfNecessary Roaming convertMaps.length " + stringArray.length + " dialNumber.length() " + str.length());
        if (stringArray.length >= 1 && str.length() >= 3) {
            int length = stringArray.length;
            int i = 0;
            while (true) {
                strConcat = "";
                if (i >= length) {
                    break;
                }
                String str2 = stringArray[i];
                log("convertNumberIfNecessary: " + str2);
                String[] strArrSplit = str2.split(":");
                if (strArrSplit != null && strArrSplit.length > 1) {
                    String str3 = strArrSplit[0];
                    String str4 = strArrSplit[1];
                    if (!TextUtils.isEmpty(str3) && str.equals(str3)) {
                        if (!TextUtils.isEmpty(str4) && str4.endsWith("MDN")) {
                            String line1Number = phone.getLine1Number();
                            if (!TextUtils.isEmpty(line1Number)) {
                                if (!line1Number.startsWith("+")) {
                                    strConcat = str4.substring(0, str4.length() - 3).concat(line1Number);
                                    break;
                                }
                                strConcat = line1Number;
                                break;
                            }
                            break;
                        }
                        strConcat = str4;
                        break;
                    }
                }
                i++;
            }
            if (!TextUtils.isEmpty(strConcat)) {
                log("convertNumberIfNecessary: convert service number");
                this.mNumberConverted = true;
                return strConcat;
            }
        }
        return str;
    }

    private boolean shouldPerformInternationalNumberRemapping(Phone phone, PersistableBundle persistableBundle) {
        if (phone == null || phone.getDefaultPhone() == null) {
            log("shouldPerformInternationalNumberRemapping: phone was null");
            return false;
        }
        if (persistableBundle.getStringArray("international_roaming_dial_string_replace_string_array") != null) {
            return phone.getDefaultPhone().getServiceState().getVoiceRoamingType() == 3;
        }
        log("shouldPerformInternationalNumberRemapping: did not set the KEY_INTERNATIONAL_ROAMING_DIAL_STRING_REPLACE_STRING_ARRAY");
        return false;
    }

    public Connection getRingingHandoverConnection() {
        ArrayList<Connection> arrayList = this.mHandoverConnections;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Connection connection = arrayList.get(i);
            i++;
            Connection connection2 = connection;
            if (connection2.getCall().isRinging()) {
                return connection2;
            }
        }
        return null;
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("CallTracker:");
        printWriter.println(" mPendingOperations=" + this.mPendingOperations);
        printWriter.println(" mNeedsPoll=" + this.mNeedsPoll);
        printWriter.println(" mLastRelevantPoll=" + this.mLastRelevantPoll);
    }
}
