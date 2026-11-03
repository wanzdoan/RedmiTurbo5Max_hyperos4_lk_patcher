package com.android.internal.telephony.data;

import android.net.IpPrefix;
import android.net.LinkAddress;
import android.net.QosFilter;
import android.os.Handler;
import android.telephony.data.EpsBearerQosSessionAttributes;
import android.telephony.data.EpsQos;
import android.telephony.data.NrQos;
import android.telephony.data.NrQosSessionAttributes;
import android.telephony.data.QosBearerFilter;
import android.telephony.data.QosBearerSession;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.metrics.RcsStats;
import com.android.telephony.Rlog;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public class QosCallbackTracker extends Handler {
    private final String mLogTag;
    private TelephonyNetworkAgent mNetworkAgent;
    private final int mPhoneId;
    private final Map mQosBearerSessions = new HashMap();
    private final Map mCallbacksToFilter = new HashMap();
    private final RcsStats mRcsStats = RcsStats.getInstance();

    public interface IFilter {
        boolean matchesLocalAddress(InetAddress inetAddress, int i, int i2, int i3);

        boolean matchesProtocol(int i);

        boolean matchesRemoteAddress(InetAddress inetAddress, int i, int i2, int i3);
    }

    public QosCallbackTracker(TelephonyNetworkAgent telephonyNetworkAgent, Phone phone) {
        this.mNetworkAgent = telephonyNetworkAgent;
        this.mPhoneId = phone.getPhoneId();
        this.mLogTag = "QOSCT-" + this.mNetworkAgent.getNetwork().getNetId();
        registerTelephonyNetworkAgentCallback(this.mNetworkAgent);
    }

    public void updateNetworkAgent(final TelephonyNetworkAgent telephonyNetworkAgent) {
        post(new Runnable() { // from class: com.android.internal.telephony.data.QosCallbackTracker$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateNetworkAgent$0(telephonyNetworkAgent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateNetworkAgent$0(TelephonyNetworkAgent telephonyNetworkAgent) {
        this.mCallbacksToFilter.clear();
        this.mNetworkAgent = telephonyNetworkAgent;
        registerTelephonyNetworkAgentCallback(telephonyNetworkAgent);
        log("updateNetworkAgent done netId:" + this.mNetworkAgent.getNetwork().getNetId());
    }

    private void registerTelephonyNetworkAgentCallback(TelephonyNetworkAgent telephonyNetworkAgent) {
        telephonyNetworkAgent.registerCallback(new TelephonyNetworkAgent.TelephonyNetworkAgentCallback(new Executor() { // from class: com.android.internal.telephony.data.QosCallbackTracker$$ExternalSyntheticLambda1
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                this.f$0.post(runnable);
            }
        }) { // from class: com.android.internal.telephony.data.QosCallbackTracker.1
            @Override // com.android.internal.telephony.data.TelephonyNetworkAgent.TelephonyNetworkAgentCallback
            public void onQosCallbackRegistered(int i, final QosFilter qosFilter) {
                QosCallbackTracker.this.addFilter(i, new IFilter(this) { // from class: com.android.internal.telephony.data.QosCallbackTracker.1.1
                    @Override // com.android.internal.telephony.data.QosCallbackTracker.IFilter
                    public boolean matchesLocalAddress(InetAddress inetAddress, int i2, int i3, int i4) {
                        return qosFilter.matchesLocalPrefix(new IpPrefix(inetAddress, i2), i3, i4);
                    }

                    @Override // com.android.internal.telephony.data.QosCallbackTracker.IFilter
                    public boolean matchesRemoteAddress(InetAddress inetAddress, int i2, int i3, int i4) {
                        return qosFilter.matchesRemotePrefix(new IpPrefix(inetAddress, i2), i3, i4);
                    }

                    @Override // com.android.internal.telephony.data.QosCallbackTracker.IFilter
                    public boolean matchesProtocol(int i2) {
                        return qosFilter.matchesProtocol(i2);
                    }
                });
            }

            @Override // com.android.internal.telephony.data.TelephonyNetworkAgent.TelephonyNetworkAgentCallback
            public void onQosCallbackUnregistered(int i) {
                QosCallbackTracker.this.removeFilter(i);
            }
        });
    }

    public void addFilter(final int i, final IFilter iFilter) {
        post(new Runnable() { // from class: com.android.internal.telephony.data.QosCallbackTracker$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$addFilter$1(i, iFilter);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addFilter$1(int i, IFilter iFilter) {
        log("addFilter: callbackId=" + i);
        this.mCallbacksToFilter.put(Integer.valueOf(i), iFilter);
        for (QosBearerSession qosBearerSession : this.mQosBearerSessions.values()) {
            if (doFiltersMatch(qosBearerSession, iFilter)) {
                sendSessionAvailable(i, qosBearerSession, iFilter);
                notifyMetricDedicatedBearerListenerAdded(i, qosBearerSession);
            }
        }
    }

    public void removeFilter(final int i) {
        post(new Runnable() { // from class: com.android.internal.telephony.data.QosCallbackTracker$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$removeFilter$2(i);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeFilter$2(int i) {
        log("removeFilter: callbackId=" + i);
        this.mCallbacksToFilter.remove(Integer.valueOf(i));
        notifyMetricDedicatedBearerListenerRemoved(i);
    }

    public void updateSessions(final List<QosBearerSession> list) {
        post(new Runnable() { // from class: com.android.internal.telephony.data.QosCallbackTracker$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateSessions$3(list);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSessions$3(List list) {
        log("updateSessions: sessions size=" + list.size());
        ArrayList arrayList = new ArrayList();
        HashMap map = new HashMap();
        HashSet hashSet = new HashSet();
        Iterator it = list.iterator();
        int i = 0;
        while (it.hasNext()) {
            QosBearerSession qosBearerSession = (QosBearerSession) it.next();
            int qosBearerSessionId = qosBearerSession.getQosBearerSessionId();
            map.put(Integer.valueOf(qosBearerSessionId), qosBearerSession);
            QosBearerSession qosBearerSession2 = (QosBearerSession) this.mQosBearerSessions.get(Integer.valueOf(qosBearerSessionId));
            for (Integer num : this.mCallbacksToFilter.keySet()) {
                int iIntValue = num.intValue();
                IFilter iFilter = (IFilter) this.mCallbacksToFilter.get(num);
                boolean zDoFiltersMatch = doFiltersMatch(qosBearerSession, iFilter);
                boolean z = qosBearerSession2 != null && doFiltersMatch(qosBearerSession2, iFilter);
                if (!z && zDoFiltersMatch) {
                    sendSessionAvailable(iIntValue, qosBearerSession, iFilter);
                    i = 1;
                }
                if (z && zDoFiltersMatch && !qosBearerSession.getQos().equals(qosBearerSession2.getQos())) {
                    sendSessionAvailable(iIntValue, qosBearerSession, iFilter);
                    i = 2;
                }
                if (!hashSet.contains(Integer.valueOf(qosBearerSessionId)) && zDoFiltersMatch) {
                    notifyMetricDedicatedBearerEvent(qosBearerSession, i, true);
                    hashSet.add(Integer.valueOf(qosBearerSessionId));
                }
            }
            if (!hashSet.contains(Integer.valueOf(qosBearerSessionId))) {
                notifyMetricDedicatedBearerEvent(qosBearerSession, 1, false);
                hashSet.add(Integer.valueOf(qosBearerSessionId));
                i = 1;
            }
            arrayList.add(qosBearerSession);
        }
        ArrayList arrayList2 = new ArrayList();
        hashSet.clear();
        for (QosBearerSession qosBearerSession3 : this.mQosBearerSessions.values()) {
            int qosBearerSessionId2 = qosBearerSession3.getQosBearerSessionId();
            if (!map.containsKey(Integer.valueOf(qosBearerSessionId2))) {
                for (Integer num2 : this.mCallbacksToFilter.keySet()) {
                    int iIntValue2 = num2.intValue();
                    if (doFiltersMatch(qosBearerSession3, (IFilter) this.mCallbacksToFilter.get(num2))) {
                        sendSessionLost(iIntValue2, qosBearerSession3);
                        notifyMetricDedicatedBearerEvent(qosBearerSession3, 3, true);
                        hashSet.add(Integer.valueOf(qosBearerSessionId2));
                    }
                }
                arrayList2.add(Integer.valueOf(qosBearerSessionId2));
                if (!hashSet.contains(Integer.valueOf(qosBearerSessionId2))) {
                    notifyMetricDedicatedBearerEvent(qosBearerSession3, 3, false);
                    hashSet.add(Integer.valueOf(qosBearerSessionId2));
                }
            }
        }
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            QosBearerSession qosBearerSession4 = (QosBearerSession) obj;
            this.mQosBearerSessions.put(Integer.valueOf(qosBearerSession4.getQosBearerSessionId()), qosBearerSession4);
        }
        int size2 = arrayList2.size();
        int i3 = 0;
        while (i3 < size2) {
            Object obj2 = arrayList2.get(i3);
            i3++;
            Integer num3 = (Integer) obj2;
            num3.intValue();
            this.mQosBearerSessions.remove(num3);
        }
    }

    private boolean doFiltersMatch(QosBearerSession qosBearerSession, IFilter iFilter) {
        return getMatchingQosBearerFilter(qosBearerSession, iFilter) != null;
    }

    private boolean matchesByLocalAddress(QosBearerFilter qosBearerFilter, IFilter iFilter) {
        int start;
        int end;
        if (qosBearerFilter.getLocalPortRange() != null) {
            if (qosBearerFilter.getLocalPortRange().isValid()) {
                start = qosBearerFilter.getLocalPortRange().getStart();
                end = qosBearerFilter.getLocalPortRange().getEnd();
            }
            return false;
        }
        start = 20;
        end = 65535;
        if (qosBearerFilter.getLocalAddresses().isEmpty()) {
            try {
                return iFilter.matchesLocalAddress(InetAddress.getByAddress(new byte[]{0, 0, 0, 0}), 32, start, end);
            } catch (UnknownHostException unused) {
                return false;
            }
        }
        Iterator it = qosBearerFilter.getLocalAddresses().iterator();
        if (it.hasNext()) {
            LinkAddress linkAddress = (LinkAddress) it.next();
            return iFilter.matchesLocalAddress(linkAddress.getAddress(), linkAddress.getPrefixLength(), start, end);
        }
        return false;
    }

    private boolean matchesByRemoteAddress(QosBearerFilter qosBearerFilter, IFilter iFilter) {
        int start;
        int end;
        if (qosBearerFilter.getRemotePortRange() != null) {
            if (qosBearerFilter.getRemotePortRange().isValid()) {
                start = qosBearerFilter.getRemotePortRange().getStart();
                end = qosBearerFilter.getRemotePortRange().getEnd();
            }
            return false;
        }
        start = 20;
        end = 65535;
        if (qosBearerFilter.getRemoteAddresses().isEmpty()) {
            try {
                return iFilter.matchesRemoteAddress(InetAddress.getByAddress(new byte[]{0, 0, 0, 0}), 32, start, end);
            } catch (UnknownHostException unused) {
                return false;
            }
        }
        Iterator it = qosBearerFilter.getRemoteAddresses().iterator();
        if (it.hasNext()) {
            LinkAddress linkAddress = (LinkAddress) it.next();
            return iFilter.matchesRemoteAddress(linkAddress.getAddress(), linkAddress.getPrefixLength(), start, end);
        }
        return false;
    }

    private boolean matchesByProtocol(QosBearerFilter qosBearerFilter, IFilter iFilter, boolean z) {
        int protocol = qosBearerFilter.getProtocol();
        return (protocol == 6 || protocol == 17) ? iFilter.matchesProtocol(protocol) : z;
    }

    private QosBearerFilter getFilterByPrecedence(QosBearerFilter qosBearerFilter, QosBearerFilter qosBearerFilter2) {
        return (qosBearerFilter == null || qosBearerFilter2.getPrecedence() < qosBearerFilter.getPrecedence()) ? qosBearerFilter2 : qosBearerFilter;
    }

    private QosBearerFilter getMatchingQosBearerFilter(QosBearerSession qosBearerSession, IFilter iFilter) {
        boolean z;
        QosBearerFilter filterByPrecedence = null;
        for (QosBearerFilter qosBearerFilter : qosBearerSession.getQosBearerFilterList()) {
            boolean z2 = false;
            boolean z3 = true;
            if (qosBearerFilter.getLocalAddresses().isEmpty() && qosBearerFilter.getLocalPortRange() == null) {
                z = false;
            } else if (matchesByLocalAddress(qosBearerFilter, iFilter)) {
                z = false;
                z2 = true;
            } else {
                z = true;
            }
            if (!qosBearerFilter.getRemoteAddresses().isEmpty() || qosBearerFilter.getRemotePortRange() != null) {
                if (matchesByRemoteAddress(qosBearerFilter, iFilter)) {
                    z2 = true;
                } else {
                    z = true;
                }
            }
            if (qosBearerFilter.getProtocol() == -1) {
                z3 = z;
            } else if (matchesByProtocol(qosBearerFilter, iFilter, z2)) {
                z2 = true;
                z3 = z;
            }
            if (!z3 && z2) {
                filterByPrecedence = getFilterByPrecedence(filterByPrecedence, qosBearerFilter);
            }
        }
        return filterByPrecedence;
    }

    private void sendSessionAvailable(int i, QosBearerSession qosBearerSession, IFilter iFilter) {
        QosBearerFilter matchingQosBearerFilter = getMatchingQosBearerFilter(qosBearerSession, iFilter);
        ArrayList arrayList = new ArrayList();
        if (matchingQosBearerFilter != null && !matchingQosBearerFilter.getRemoteAddresses().isEmpty() && matchingQosBearerFilter.getRemotePortRange() != null) {
            arrayList.add(new InetSocketAddress(((LinkAddress) matchingQosBearerFilter.getRemoteAddresses().get(0)).getAddress(), matchingQosBearerFilter.getRemotePortRange().getStart()));
        }
        if (qosBearerSession.getQos() instanceof EpsQos) {
            EpsQos qos = qosBearerSession.getQos();
            this.mNetworkAgent.sendQosSessionAvailable(i, qosBearerSession.getQosBearerSessionId(), new EpsBearerQosSessionAttributes(qos.getQci(), qos.getUplinkBandwidth().getMaxBitrateKbps(), qos.getDownlinkBandwidth().getMaxBitrateKbps(), qos.getDownlinkBandwidth().getGuaranteedBitrateKbps(), qos.getUplinkBandwidth().getGuaranteedBitrateKbps(), arrayList));
        } else {
            NrQos qos2 = qosBearerSession.getQos();
            this.mNetworkAgent.sendQosSessionAvailable(i, qosBearerSession.getQosBearerSessionId(), new NrQosSessionAttributes(qos2.get5Qi(), qos2.getQfi(), qos2.getUplinkBandwidth().getMaxBitrateKbps(), qos2.getDownlinkBandwidth().getMaxBitrateKbps(), qos2.getDownlinkBandwidth().getGuaranteedBitrateKbps(), qos2.getUplinkBandwidth().getGuaranteedBitrateKbps(), qos2.getAveragingWindow(), arrayList));
        }
        notifyMetricDedicatedBearerListenerBearerUpdateSession(i, qosBearerSession);
        log("sendSessionAvailable, callbackId=" + i);
    }

    private void sendSessionLost(int i, QosBearerSession qosBearerSession) {
        this.mNetworkAgent.sendQosSessionLost(i, qosBearerSession.getQosBearerSessionId(), qosBearerSession.getQos() instanceof EpsQos ? 1 : 2);
        log("sendSessionLost, callbackId=" + i);
    }

    private void notifyMetricDedicatedBearerListenerAdded(int i, QosBearerSession qosBearerSession) {
        this.mRcsStats.onImsDedicatedBearerListenerAdded(i, this.mPhoneId, getRatInfoFromSessionInfo(qosBearerSession), getQCIFromSessionInfo(qosBearerSession));
    }

    private void notifyMetricDedicatedBearerListenerBearerUpdateSession(int i, QosBearerSession qosBearerSession) {
        this.mRcsStats.onImsDedicatedBearerListenerUpdateSession(i, this.mPhoneId, getRatInfoFromSessionInfo(qosBearerSession), getQCIFromSessionInfo(qosBearerSession), true);
    }

    private void notifyMetricDedicatedBearerListenerRemoved(int i) {
        this.mRcsStats.onImsDedicatedBearerListenerRemoved(i);
    }

    private int getQCIFromSessionInfo(QosBearerSession qosBearerSession) {
        if (qosBearerSession.getQos() instanceof EpsQos) {
            return qosBearerSession.getQos().getQci();
        }
        if (qosBearerSession.getQos() instanceof NrQos) {
            return qosBearerSession.getQos().get5Qi();
        }
        return 0;
    }

    private int getRatInfoFromSessionInfo(QosBearerSession qosBearerSession) {
        if (qosBearerSession.getQos() instanceof EpsQos) {
            return 13;
        }
        return qosBearerSession.getQos() instanceof NrQos ? 20 : 0;
    }

    private boolean doesLocalConnectionInfoExist(QosBearerSession qosBearerSession) {
        for (QosBearerFilter qosBearerFilter : qosBearerSession.getQosBearerFilterList()) {
            if (!qosBearerFilter.getLocalAddresses().isEmpty() && qosBearerFilter.getLocalPortRange() != null && qosBearerFilter.getLocalPortRange().isValid()) {
                return true;
            }
        }
        return false;
    }

    private boolean doesRemoteConnectionInfoExist(QosBearerSession qosBearerSession) {
        for (QosBearerFilter qosBearerFilter : qosBearerSession.getQosBearerFilterList()) {
            if (!qosBearerFilter.getRemoteAddresses().isEmpty() && qosBearerFilter.getRemotePortRange() != null && qosBearerFilter.getRemotePortRange().isValid()) {
                return true;
            }
        }
        return false;
    }

    private void notifyMetricDedicatedBearerEvent(QosBearerSession qosBearerSession, int i, boolean z) {
        this.mRcsStats.onImsDedicatedBearerEvent(this.mPhoneId, getRatInfoFromSessionInfo(qosBearerSession), getQCIFromSessionInfo(qosBearerSession), i, doesLocalConnectionInfoExist(qosBearerSession), doesRemoteConnectionInfoExist(qosBearerSession), z);
    }

    private void log(String str) {
        Rlog.d(this.mLogTag, str);
    }
}
