package com.android.phone;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/* JADX INFO: loaded from: classes.dex */
public class MtkModemTcpKeepAliveController extends ModemTcpKaBase {
    private HbaMessageHandler mMtkHandler;
    private ConcurrentHashMap mSessions = new ConcurrentHashMap();
    private Phone mPhone = PhoneFactory.getPhone(this.mDdsPhoneId);

    enum SessionStatus {
        INIT,
        ESTABLISHED,
        STOPPED
    }

    class SessionState {
        int clientId;
        String destIp;
        int destPort;
        long lastTcpInfoUpdateMs;
        Phone phone;
        String proxyKey;
        long rcvNxt;
        long rcvWnd;
        long sndNxt;
        long sndWnd;
        String sourceIp;
        int sourcePort;
        boolean tcpInfoReady;
        int cycleSeconds = 180;
        SessionStatus status = SessionStatus.INIT;

        SessionState(int i) {
            this.clientId = i;
            this.proxyKey = "PROXY_" + i;
        }
    }

    private static String[] buildEstablishParams(SessionState sessionState, int i) {
        return new String[]{"MIPC_HBA_ESTABLISH_REQ", sessionState.proxyKey, String.valueOf(i), "120", "0", "0", "0", String.valueOf(sessionState.cycleSeconds), "0", "0", "0", "0", "255", "1234", "00", "255", "00", "1", "8", "1", String.valueOf(sessionState.sndWnd), String.valueOf(sessionState.rcvWnd & 65535), String.valueOf(sessionState.sndNxt), String.valueOf(sessionState.rcvNxt), String.format("%08X", Integer.valueOf(MeModemUtils.ipStringToIntBigEndian(sessionState.sourceIp))), String.format("%08X", Integer.valueOf(MeModemUtils.ipStringToIntBigEndian(sessionState.destIp))), "0", "0", "0", "6", String.valueOf(sessionState.sourcePort), String.valueOf(sessionState.destPort), "3"};
    }

    private static String[] buildStopParams(SessionState sessionState) {
        return new String[]{"MIPC_HBA_CTRL_REQ", "1", sessionState.proxyKey};
    }

    MtkModemTcpKeepAliveController() {
        log("Initialized mPhone with phoneId: " + this.mDdsPhoneId);
        HbaMessageHandler hbaMessageHandler = new HbaMessageHandler(this.mWorkThread.getLooper());
        this.mMtkHandler = hbaMessageHandler;
        MeModemUtils.registerForOemIndication(true, hbaMessageHandler, 16410);
        log("MtkModemTcpKeepAliveController init");
    }

    @Override // com.android.phone.ModemTcpKaBase
    protected void updateDefaultDataSlotId() {
        super.updateDefaultDataSlotId();
        int i = this.mDdsPhoneId;
        Phone phone = this.mPhone;
        if (phone == null || phone.getPhoneId() != i) {
            this.mPhone = PhoneFactory.getPhone(i);
            log("Updated mPhone to phoneId: " + i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Rlog.d("MtkModemTcpKeepAliveController", str);
    }

    private boolean refreshTcpInfo(SessionState sessionState) {
        log("refreshTcpInfo: sourceIp=" + sessionState.sourceIp + ", sourcePort=" + sessionState.sourcePort + ", destIp=" + sessionState.destIp + ", destPort=" + sessionState.destPort);
        try {
            byte[] bArr = new byte[25];
            ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr);
            byteBufferWrap.order(ByteOrder.nativeOrder());
            byteBufferWrap.put((byte) 15);
            byteBufferWrap.putInt(MeModemUtils.ipStringToIntBigEndian(sessionState.sourceIp));
            byteBufferWrap.putInt(sessionState.sourcePort);
            byteBufferWrap.putInt(MeModemUtils.ipStringToIntBigEndian(sessionState.destIp));
            byteBufferWrap.putInt(sessionState.destPort);
            MeModemUtils.setModemPara(bArr);
            ByteBuffer modemPara = MeModemUtils.getModemPara(15);
            if (modemPara != null) {
                modemPara.rewind();
                modemPara.getInt();
                modemPara.getInt();
                long j = ((long) modemPara.getInt()) & 4294967295L;
                int i = modemPara.getInt() / 2;
                long j2 = ((long) modemPara.getInt()) & 4294967295L;
                long j3 = 4294967295L & ((long) modemPara.getInt());
                sessionState.sndWnd = j;
                sessionState.rcvWnd = 65535 & i;
                sessionState.sndNxt = j3;
                sessionState.rcvNxt = j2;
                sessionState.tcpInfoReady = true;
                sessionState.lastTcpInfoUpdateMs = System.currentTimeMillis();
                log("TCP info updated successfully: snd_wnd=" + j + ", rcv_wnd=" + i + ", snd_nxt=" + j3 + ", rcv_nxt=" + j2);
                return true;
            }
            log("Failed to get ModemPara response");
            sessionState.tcpInfoReady = false;
            return false;
        } catch (Exception e) {
            log("Error in refreshTcpInfo: " + e);
            sessionState.tcpInfoReady = false;
            return false;
        }
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int startTcpMonitorRegister(String str, int i, String str2, int i2, int i3) {
        log("startTcpMonitorRegister for clientId: " + i3 + ", params: source=" + str + ":" + i + ", dest=" + str2 + ":" + i2);
        return 0;
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int stopTcpMonitorRegister(int i) {
        log("stopTcpMonitorRegister for clientId: " + i);
        return 0;
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int startTcpKeepAlive(String str, int i, String str2, int i2, int i3, int i4) {
        localLog("startTcpKeepAlive for clientId: " + i4 + ", interval: " + i3 + " params: source=" + str + ":" + i + ", dest=" + str2 + ":" + i2);
        SessionState sessionState = (SessionState) this.mSessions.computeIfAbsent(Integer.valueOf(i4), new Function() { // from class: com.android.phone.MtkModemTcpKeepAliveController$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return new MtkModemTcpKeepAliveController.SessionState(((Integer) obj).intValue());
            }
        });
        if (sessionState.status == SessionStatus.ESTABLISHED) {
            localLog("startTcpKeepAlive: session already established for clientId: " + i4);
            return 0;
        }
        this.mDlyKaTrigCnt++;
        this.mFlgWatingKaInd = true;
        this.mHandler.sendEmptyMessageDelayed(3, 3000L);
        sessionState.sourceIp = str;
        sessionState.sourcePort = i;
        sessionState.destIp = str2;
        sessionState.destPort = i2;
        sessionState.phone = this.mPhone;
        sessionState.cycleSeconds = Math.max(1, i3 / 1000);
        if (!refreshTcpInfo(sessionState)) {
            log("TCP info not ready, cannot establish");
            this.mHandler.removeMessages(3);
            dataTrackingForKaInd("KEEP_ALIVE_RESULT_ERROR");
            return 1;
        }
        log("Mapped intervalMillis " + i3 + " to cycle " + sessionState.cycleSeconds + " seconds");
        int defaultDataIfaceNumber = MeModemUtils.getDefaultDataIfaceNumber(this.mContext);
        if (defaultDataIfaceNumber < 0) {
            log("Cannot get iface number, abort establish");
            this.mHandler.removeMessages(3);
            dataTrackingForKaInd("KEEP_ALIVE_RESULT_ERROR");
            return 1;
        }
        Phone phone = sessionState.phone;
        if (phone == null) {
            phone = this.mPhone;
        }
        if (phone == null) {
            log("Cannot get valid phone object, abort establish");
            this.mHandler.removeMessages(3);
            dataTrackingForKaInd("KEEP_ALIVE_RESULT_ERROR");
            return 1;
        }
        Message messageObtainMessage = this.mMtkHandler.obtainMessage(1001);
        messageObtainMessage.arg1 = i4;
        MeModemUtils.sendATCommand(phone, buildEstablishParams(sessionState, defaultDataIfaceNumber), messageObtainMessage);
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.phone.ModemTcpKaBase
    /* JADX INFO: renamed from: tcpMonitorRelease */
    public void lambda$onCloudChanged$0() {
        log("tcpMonitorRelease begin, sessions: " + this.mSessions.size());
        this.mHandler.removeMessages(3);
        this.mFlgWatingKaInd = false;
        Iterator it = this.mSessions.entrySet().iterator();
        while (it.hasNext()) {
            SessionState sessionState = (SessionState) ((Map.Entry) it.next()).getValue();
            if (sessionState.status == SessionStatus.ESTABLISHED) {
                String[] strArrBuildStopParams = buildStopParams(sessionState);
                Phone phone = sessionState.phone;
                if (phone == null) {
                    phone = this.mPhone;
                }
                MeModemUtils.sendATCommand(phone, strArrBuildStopParams, this.mMtkHandler.obtainMessage(1002));
                log("tcpMonitorRelease stopped session clientId: " + sessionState.clientId);
            }
        }
        this.mSessions.clear();
        log("tcpMonitorRelease done");
    }

    @Override // com.android.phone.ModemTcpKaBase
    public int stopTcpKeepAlive(int i) {
        log("stopTcpKeepAlive for clientId: " + i);
        this.mKaStopTime = System.currentTimeMillis();
        dataTrackingForDuration();
        SessionState sessionState = (SessionState) this.mSessions.get(Integer.valueOf(i));
        if (sessionState == null) {
            return 0;
        }
        String[] strArrBuildStopParams = buildStopParams(sessionState);
        Phone phone = sessionState.phone;
        if (phone == null) {
            phone = this.mPhone;
        }
        MeModemUtils.sendATCommand(phone, strArrBuildStopParams, this.mMtkHandler.obtainMessage(1002));
        this.mSessions.remove(Integer.valueOf(i));
        return 0;
    }

    @Override // com.android.phone.ModemTcpKaBase
    protected void recordStatisticsEvent() {
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_cellular_tcp_keepavlie", new LinkedHashMap(this.mDataTackingMap));
    }

    @Override // com.android.phone.ModemTcpKaBase
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("MtkModemTcpKeepAliveController Dump:");
        printWriter.println("  Active Clients: " + this.mSessions.size());
        Iterator it = this.mSessions.entrySet().iterator();
        while (it.hasNext()) {
            SessionState sessionState = (SessionState) ((Map.Entry) it.next()).getValue();
            StringBuilder sb = new StringBuilder("    ClientId: ");
            sb.append(sessionState.clientId);
            sb.append(", ProxyKey: ");
            sb.append(sessionState.proxyKey);
            sb.append(", State: ");
            sb.append(sessionState.status);
            sb.append(", PhoneId: ");
            Phone phone = sessionState.phone;
            sb.append(phone != null ? phone.getPhoneId() : -1);
            sb.append(", Cycle: ");
            sb.append(sessionState.cycleSeconds);
            sb.append(", TcpReady: ");
            sb.append(sessionState.tcpInfoReady);
            printWriter.println(sb.toString());
        }
        printWriter.println("  Registered Listeners: " + this.mListeners.size());
        printWriter.println("  Local Logs:");
        ModemTcpKaBase.mLocalLog.dump(fileDescriptor, printWriter, strArr);
        printWriter.flush();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getHbaResultName(String str) {
        if (str == null || str.isEmpty()) {
            return "UNKNOWN_HBA_RESULT";
        }
        str.hashCode();
        switch (str) {
            case "2883585":
                return "HBA_PROCESS_OK";
            case "2883586":
                return "HBA_NUM_ALREADY_MAX";
            case "2883587":
                return "HBA_PROXY_ALREADY_EXIST";
            case "2883588":
                return "HBA_INTERNAL_ABNORMAL";
            case "2883589":
                return "HBA_PROXY_NOT_EXIST";
            case "2883590":
                return "HBA_HW_FILTER_EXHAUST";
            case "2883591":
                return "HBA_PASUE_ERR";
            case "2883592":
                return "HBA_RESUME_ERR";
            case "2883593":
                return "HBA_STOP_ERR";
            case "2883594":
                return "HBA_SEDN_TIME_OUT";
            case "2883595":
                return "HBA_ACK_TIME_OUT";
            case "2883839":
                return "HBA_RESULT_INVALID";
            default:
                return "UNKNOWN_HBA_RESULT";
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHbaTimeoutInd(String str) {
        if (str.startsWith("+HBA_TIMEOUT:")) {
            try {
                String[] strArrSplit = str.substring(13).split(",");
                if (strArrSplit.length < 6) {
                    log("HBA Timeout: unexpected parts count: " + strArrSplit.length);
                    return;
                }
                String str2 = strArrSplit[0];
                localLog("HBA Timeout Details: Proxy Key=" + str2 + ", Current Cycle=" + Integer.parseInt(strArrSplit[1]) + ", Send Count=" + Integer.parseInt(strArrSplit[2]) + ", Recv Count=" + Integer.parseInt(strArrSplit[3]) + ", Short RRC Count=" + Integer.parseInt(strArrSplit[4]) + ", Hitchhike Count=" + Integer.parseInt(strArrSplit[5]));
                Iterator it = this.mSessions.entrySet().iterator();
                while (it.hasNext()) {
                    SessionState sessionState = (SessionState) ((Map.Entry) it.next()).getValue();
                    if (str2.equals(sessionState.proxyKey)) {
                        sessionState.status = SessionStatus.INIT;
                        notifyListeners("HBA_TIMEOUT_OCCURRED");
                        dataTrackingForKaInd("KEEP_ALIVE_NETWORK_DOWN_ERROR");
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                log("Error parsing HBA timeout data: " + e);
            }
        }
    }

    class HbaMessageHandler extends Handler {
        HbaMessageHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            String strReplaceAll;
            Object obj = message.obj;
            if (!(obj instanceof AsyncResult)) {
                MtkModemTcpKeepAliveController.log("Expected AsyncResult but got: ".concat(obj != null ? obj.getClass().getName() : "null"));
                return;
            }
            AsyncResult asyncResult = (AsyncResult) obj;
            String str = "KEEP_ALIVE_RESULT_ERROR";
            if (asyncResult.exception != null) {
                MtkModemTcpKeepAliveController.log("Error in AsyncResult: " + asyncResult.exception);
                if (message.what == 1001) {
                    MtkModemTcpKeepAliveController.this.mHandler.removeMessages(3);
                    MtkModemTcpKeepAliveController.this.dataTrackingForKaInd("KEEP_ALIVE_RESULT_ERROR");
                    MtkModemTcpKeepAliveController.this.notifyListeners("KEEP_ALIVE_RESULT_ERROR");
                    return;
                }
                return;
            }
            Object obj2 = asyncResult.result;
            if (obj2 instanceof String) {
                strReplaceAll = (String) obj2;
            } else {
                strReplaceAll = obj2 instanceof String[] ? Arrays.toString((String[]) obj2).replaceAll("[\\[\\]\\s]", "") : "";
            }
            String hbaResultName = MtkModemTcpKeepAliveController.getHbaResultName(strReplaceAll);
            int i = message.what;
            if (i != 1001) {
                if (i == 1002) {
                    MtkModemTcpKeepAliveController.this.localLog("HBA Stop Result: " + hbaResultName);
                    return;
                }
                if (i == 16410) {
                    MtkModemTcpKeepAliveController.this.handleHbaTimeoutInd(strReplaceAll);
                    return;
                }
                MtkModemTcpKeepAliveController.log("Unhandled message: " + message.what);
                return;
            }
            MtkModemTcpKeepAliveController.this.localLog("HBA Establish Result: " + hbaResultName);
            MtkModemTcpKeepAliveController.this.mHandler.removeMessages(3);
            SessionState sessionState = (SessionState) MtkModemTcpKeepAliveController.this.mSessions.get(Integer.valueOf(message.arg1));
            if ("HBA_PROCESS_OK".equals(hbaResultName)) {
                if (sessionState != null) {
                    sessionState.status = SessionStatus.ESTABLISHED;
                }
                str = "KEEP_ALIVE_RESULT_OK";
            } else if (sessionState != null) {
                sessionState.status = SessionStatus.INIT;
            }
            MtkModemTcpKeepAliveController.this.dataTrackingForKaInd(str);
            MtkModemTcpKeepAliveController.this.notifyListeners(str);
        }
    }
}
