package com.mediatek.services.telephony;

import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telecom.Conference;
import android.telecom.Connection;
import android.telephony.PhoneNumberUtils;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.gsm.SuppServiceNotification;
import com.android.services.telephony.ImsConference;
import com.android.services.telephony.Log;
import com.android.services.telephony.TelephonyConnection;
import com.android.services.telephony.TelephonyConnectionService;
import com.mediatek.internal.telephony.MtkGsmCdmaPhone;
import com.mediatek.internal.telephony.gsm.MtkSuppCrssNotification;
import gov.nist.javax.sip.address.GenericURI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class MtkSuppMessageManager {
    private TelephonyConnectionService mConnectionService;
    private List mSuppMessageHandlerList = new ArrayList();
    private HashMap mCachedSsnsMap = new HashMap();
    private HashMap mCachedCrssnsMap = new HashMap();

    public MtkSuppMessageManager(TelephonyConnectionService telephonyConnectionService) {
        this.mConnectionService = telephonyConnectionService;
    }

    public void registerSuppMessageForPhones() {
        this.mSuppMessageHandlerList.clear();
        for (MtkGsmCdmaPhone mtkGsmCdmaPhone : PhoneFactory.getPhones()) {
            if (mtkGsmCdmaPhone != null) {
                Log.d(this, "registerSuppMessageForPhones()...for service" + this.mConnectionService + " for phone " + mtkGsmCdmaPhone.getPhoneId(), new Object[0]);
                SuppMessageHandler suppMessageHandler = new SuppMessageHandler(mtkGsmCdmaPhone);
                this.mSuppMessageHandlerList.add(suppMessageHandler);
                if (mtkGsmCdmaPhone instanceof MtkGsmCdmaPhone) {
                    mtkGsmCdmaPhone.registerForCrssSuppServiceNotification(suppMessageHandler, 251, (Object) null);
                }
                mtkGsmCdmaPhone.registerForSuppServiceNotification(suppMessageHandler, 252, (Object) null);
            }
        }
    }

    public void unregisterSuppMessageForPhones() {
        for (SuppMessageHandler suppMessageHandler : this.mSuppMessageHandlerList) {
            MtkGsmCdmaPhone phone = suppMessageHandler.getPhone();
            if (phone != null) {
                Log.d(this, "unregisterSuppMessageForPhones()... for phone " + phone.getPhoneId(), new Object[0]);
                phone.unregisterForSuppServiceNotification(suppMessageHandler);
                if (phone instanceof MtkGsmCdmaPhone) {
                    phone.unregisterForCrssSuppServiceNotification(suppMessageHandler);
                }
            }
        }
        this.mSuppMessageHandlerList.clear();
        this.mCachedSsnsMap.clear();
        this.mCachedCrssnsMap.clear();
    }

    public void registerSuppMessageForPhone(Phone phone) {
        Iterator it = this.mSuppMessageHandlerList.iterator();
        while (it.hasNext()) {
            if (phone == ((SuppMessageHandler) it.next()).getPhone()) {
                return;
            }
        }
        SuppMessageHandler suppMessageHandler = new SuppMessageHandler(phone);
        this.mSuppMessageHandlerList.add(suppMessageHandler);
        if (phone instanceof MtkGsmCdmaPhone) {
            ((MtkGsmCdmaPhone) phone).registerForCrssSuppServiceNotification(suppMessageHandler, 251, (Object) null);
        }
        phone.registerForSuppServiceNotification(suppMessageHandler, 252, (Object) null);
    }

    public void unregisterSuppMessageForPhone(Phone phone) {
        for (SuppMessageHandler suppMessageHandler : this.mSuppMessageHandlerList) {
            MtkGsmCdmaPhone phone2 = suppMessageHandler.getPhone();
            if (phone == phone2) {
                phone2.unregisterForSuppServiceNotification(suppMessageHandler);
                if (phone2 instanceof MtkGsmCdmaPhone) {
                    phone2.unregisterForCrssSuppServiceNotification(suppMessageHandler);
                }
                this.mSuppMessageHandlerList.remove(suppMessageHandler);
                return;
            }
        }
    }

    public void forceSuppMessageUpdate(TelephonyConnection telephonyConnection, Phone phone) {
        int i = 0;
        Log.d(this, "forceSuppMessageUpdate for " + telephonyConnection + ", " + phone + " phone " + phone.getPhoneId(), new Object[0]);
        ArrayList arrayList = (ArrayList) this.mCachedSsnsMap.get(Integer.valueOf(phone.getPhoneId()));
        if (arrayList != null) {
            Log.d(this, "forceSuppMessageUpdate()... for SuppServiceNotification for " + telephonyConnection + " for phone " + phone.getPhoneId(), new Object[0]);
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                onSuppServiceNotification((SuppServiceNotification) obj, phone, telephonyConnection);
            }
            this.mCachedSsnsMap.remove(Integer.valueOf(phone.getPhoneId()));
        }
        ArrayList arrayList2 = (ArrayList) this.mCachedCrssnsMap.get(Integer.valueOf(phone.getPhoneId()));
        if (arrayList2 != null) {
            Log.d(this, "forceSuppMessageUpdate()... for SuppCrssNotification for " + telephonyConnection + " for phone " + phone.getPhoneId(), new Object[0]);
            int size2 = arrayList2.size();
            while (i < size2) {
                Object obj2 = arrayList2.get(i);
                i++;
                onCrssSuppServiceNotification((MtkSuppCrssNotification) obj2, phone, telephonyConnection);
            }
            this.mCachedCrssnsMap.remove(Integer.valueOf(phone.getPhoneId()));
        }
    }

    class SuppMessageHandler extends Handler {
        private Phone mPhone;

        public SuppMessageHandler(Phone phone) {
            super(phone.getLooper());
            this.mPhone = phone;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Phone getPhone() {
            return this.mPhone;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d(this, "handleMessage()... for service " + MtkSuppMessageManager.this.mConnectionService + " for phone " + this.mPhone.getPhoneId(), new Object[0]);
            AsyncResult asyncResult = (AsyncResult) message.obj;
            if (asyncResult == null || asyncResult.result == null || MtkSuppMessageManager.this.mConnectionService == null) {
                Log.w(this, "handleMessage()...Wrong condition: ar / mConnectionService = " + asyncResult + " / " + MtkSuppMessageManager.this.mConnectionService, new Object[0]);
                return;
            }
            int i = message.what;
            if (i == 251) {
                Object obj = asyncResult.result;
                if (!(obj instanceof MtkSuppCrssNotification)) {
                    Log.w(this, "handleMessage()...Wrong data for SuppCrssNotification", new Object[0]);
                    return;
                } else {
                    MtkSuppMessageManager.this.onCrssSuppServiceNotification((MtkSuppCrssNotification) obj, this.mPhone, null);
                    return;
                }
            }
            if (i != 252) {
                return;
            }
            Object obj2 = asyncResult.result;
            if (!(obj2 instanceof SuppServiceNotification)) {
                Log.w(this, "handleMessage()..Wrong data for SuppServiceNotification", new Object[0]);
            } else {
                MtkSuppMessageManager.this.onSuppServiceNotification((SuppServiceNotification) obj2, this.mPhone, null);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSuppServiceNotification(SuppServiceNotification suppServiceNotification, Phone phone, Connection connection) {
        Log.v(this, "onSuppServiceNotification()... " + suppServiceNotification, new Object[0]);
        int i = suppServiceNotification.notificationType;
        if (i == 0) {
            if (suppServiceNotification.code == 10) {
                Connection connectionWithState = getConnectionWithState(Call.State.DIALING);
                if (connectionWithState == null) {
                    connectionWithState = getConnectionWithState(Call.State.ALERTING);
                }
                if (connectionWithState instanceof TelephonyConnection) {
                    if (connection == null || connection == connectionWithState) {
                        ((TelephonyConnection) connectionWithState).notifyEcc();
                        return;
                    }
                    return;
                }
                if (connectionWithState == null && connection == null) {
                    Log.d(this, "onSuppServiceNotification()...MO connection is null", new Object[0]);
                    addSsnList(suppServiceNotification, phone);
                    return;
                }
                return;
            }
        } else if (i != 1) {
            return;
        }
        TelephonyConnection telephonyConnection = (TelephonyConnection) findConnection(getProperOriginalConnection(phone));
        if (telephonyConnection == null) {
            if (connection == null) {
                Log.d(this, "onSuppServiceNotification()...MT connection is null", new Object[0]);
                addSsnList(suppServiceNotification, phone);
                return;
            }
            return;
        }
        if (connection == null || connection == telephonyConnection) {
            int i2 = suppServiceNotification.code;
            if (i2 == 11) {
                if (suppServiceNotification.notificationType == 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", suppServiceNotification.code);
                    bundle.putString("data", "user-idle");
                    telephonyConnection.sendTelephonyConnectionEvent("mediatek.telecom.event.IMS_EVENT_NOTIFICATION_RINGTONE", bundle);
                    return;
                }
                return;
            }
            if (i2 == 2) {
                if (suppServiceNotification.notificationType == 0) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putInt("type", suppServiceNotification.code);
                    bundle2.putString("data", "call-forwarding");
                    telephonyConnection.sendTelephonyConnectionEvent("mediatek.telecom.event.IMS_EVENT_NOTIFICATION_RINGTONE", bundle2);
                    return;
                }
                return;
            }
            if (i2 == 3) {
                if (suppServiceNotification.notificationType == 0) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putInt("type", suppServiceNotification.code);
                    bundle3.putString("data", "call-waiting");
                    telephonyConnection.sendTelephonyConnectionEvent("mediatek.telecom.event.IMS_EVENT_NOTIFICATION_RINGTONE", bundle3);
                    return;
                }
                return;
            }
            if (i2 == 12) {
                if (suppServiceNotification.notificationType == 0) {
                    Bundle bundle4 = new Bundle();
                    bundle4.putInt("type", suppServiceNotification.code);
                    bundle4.putString("data", suppServiceNotification.number);
                    telephonyConnection.sendTelephonyConnectionEvent("mediatek.telecom.event.IMS_EVENT_VIDEO_CMOS", bundle4);
                    return;
                }
                return;
            }
            if (i2 == 13 && suppServiceNotification.notificationType == 0 && suppServiceNotification.number.contains("visualized-voice-call")) {
                Bundle bundle5 = new Bundle();
                bundle5.putInt("type", suppServiceNotification.code);
                bundle5.putString("data", suppServiceNotification.number);
                telephonyConnection.sendTelephonyConnectionEvent("mediatek.telecom.event.IMS_EVENT_VIDEO_CVVC", bundle5);
            }
        }
    }

    private void addSsnList(SuppServiceNotification suppServiceNotification, Phone phone) {
        Log.d(this, "addSsnList for " + phone + " phone " + phone.getPhoneId(), new Object[0]);
        ArrayList arrayList = (ArrayList) this.mCachedSsnsMap.get(Integer.valueOf(phone.getPhoneId()));
        if (arrayList == null) {
            arrayList = new ArrayList();
        } else {
            this.mCachedSsnsMap.remove(Integer.valueOf(phone.getPhoneId()));
        }
        arrayList.add(suppServiceNotification);
        this.mCachedSsnsMap.put(Integer.valueOf(phone.getPhoneId()), arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCrssSuppServiceNotification(MtkSuppCrssNotification mtkSuppCrssNotification, Phone phone, Connection connection) {
        Log.v(this, "onCrssSuppServiceNotification... " + mtkSuppCrssNotification, new Object[0]);
        int i = mtkSuppCrssNotification.code;
        int i2 = 1;
        if (i == 1) {
            TelephonyConnection telephonyConnection = (TelephonyConnection) findConnection(getOriginalConnectionWithState(phone, Call.State.ACTIVE));
            if (telephonyConnection != null) {
                if (connection == null || connection == telephonyConnection) {
                    telephonyConnection.setAddress(Uri.fromParts(GenericURI.TEL, PhoneNumberUtils.stringFromStringAndTOA(mtkSuppCrssNotification.number, mtkSuppCrssNotification.type), null), telephonyConnection.getAddressPresentation());
                    return;
                }
                return;
            }
            if (connection == null) {
                Log.d(this, "onCrssSuppServiceNotification()...connection is null", new Object[0]);
                addCrssnList(mtkSuppCrssNotification, phone);
                return;
            }
            return;
        }
        if (i != 2) {
            if (i != 3) {
                return;
            }
            TelephonyConnection telephonyConnection2 = (TelephonyConnection) findConnection(getOriginalConnectionWithState(phone, Call.State.DIALING));
            if (telephonyConnection2 == null && (telephonyConnection2 = (TelephonyConnection) findConnection(getOriginalConnectionWithState(phone, Call.State.ALERTING))) == null) {
                telephonyConnection2 = (TelephonyConnection) findConnection(getOriginalConnectionWithState(phone, Call.State.ACTIVE));
            }
            if (telephonyConnection2 != null) {
                if (connection == null || connection == telephonyConnection2) {
                    telephonyConnection2.setAddress(Uri.fromParts(GenericURI.TEL, PhoneNumberUtils.stringFromStringAndTOA(mtkSuppCrssNotification.number, mtkSuppCrssNotification.type), null), telephonyConnection2.getAddressPresentation());
                    return;
                }
                return;
            }
            if (connection == null) {
                Log.d(this, "onCrssSuppServiceNotification()...connection is null", new Object[0]);
                addCrssnList(mtkSuppCrssNotification, phone);
                return;
            }
            return;
        }
        TelephonyConnection telephonyConnection3 = (TelephonyConnection) findConnection(getOriginalConnectionWithState(phone, Call.State.INCOMING));
        if (telephonyConnection3 == null) {
            if (connection == null) {
                Log.d(this, "onCrssSuppServiceNotification()...connection is null", new Object[0]);
                addCrssnList(mtkSuppCrssNotification, phone);
                return;
            }
            return;
        }
        if (connection == null || connection == telephonyConnection3) {
            int i3 = mtkSuppCrssNotification.cli_validity;
            if (i3 != 0) {
                if (i3 != 1) {
                    i2 = (i3 == 2 || i3 != 3) ? 3 : 4;
                } else {
                    i2 = 2;
                }
            }
            telephonyConnection3.setAddress(telephonyConnection3.getAddress(), i2);
        }
    }

    private void addCrssnList(MtkSuppCrssNotification mtkSuppCrssNotification, Phone phone) {
        Log.d(this, "addCrssnList for " + phone + " phone " + phone.getPhoneId(), new Object[0]);
        ArrayList arrayList = (ArrayList) this.mCachedCrssnsMap.get(Integer.valueOf(phone.getPhoneId()));
        if (arrayList == null) {
            arrayList = new ArrayList();
        } else {
            this.mCachedCrssnsMap.remove(Integer.valueOf(phone.getPhoneId()));
        }
        arrayList.add(mtkSuppCrssNotification);
        this.mCachedCrssnsMap.put(Integer.valueOf(phone.getPhoneId()), arrayList);
    }

    private com.android.internal.telephony.Connection getOriginalConnectionWithState(Phone phone, Call.State state) {
        Call foregroundCall;
        if (state == Call.State.INCOMING) {
            foregroundCall = phone.getRingingCall();
        } else if (state == Call.State.HOLDING) {
            foregroundCall = phone.getBackgroundCall();
        } else {
            foregroundCall = (state == Call.State.DIALING || state == Call.State.ALERTING || state == Call.State.ACTIVE) ? phone.getForegroundCall() : null;
        }
        if (foregroundCall == null || foregroundCall.getState() != state) {
            return null;
        }
        return foregroundCall.getLatestConnection();
    }

    private com.android.internal.telephony.Connection getProperOriginalConnection(Phone phone) {
        if (phone == null) {
            Log.d(this, "getProperOriginalConnection: phone is null", new Object[0]);
            return null;
        }
        com.android.internal.telephony.Connection latestConnection = phone.getRingingCall().getLatestConnection();
        Log.d(this, "getRingingCall originalConnection = " + latestConnection, new Object[0]);
        if (latestConnection == null) {
            ArrayList connections = phone.getForegroundCall().getConnections();
            for (int i = 0; i < connections.size(); i++) {
                com.android.internal.telephony.Connection connection = (com.android.internal.telephony.Connection) connections.get(i);
                Log.d(this, "getForegroundCall iterate conn = " + connection, new Object[0]);
                if (connection != null && connection.isAlive()) {
                    latestConnection = connection;
                    break;
                }
            }
        }
        if (latestConnection == null) {
            ArrayList connections2 = phone.getBackgroundCall().getConnections();
            for (int i2 = 0; i2 < connections2.size(); i2++) {
                com.android.internal.telephony.Connection connection2 = (com.android.internal.telephony.Connection) connections2.get(i2);
                Log.d(this, "getBackgroundCall iterate conn = " + connection2, new Object[0]);
                if (connection2 != null && connection2.isAlive()) {
                    latestConnection = connection2;
                    break;
                }
            }
        }
        Phone imsPhone = phone.getImsPhone();
        if (imsPhone == null) {
            return latestConnection;
        }
        if (latestConnection == null) {
            ArrayList connections3 = imsPhone.getForegroundCall().getConnections();
            for (int i3 = 0; i3 < connections3.size(); i3++) {
                try {
                    com.android.internal.telephony.Connection connection3 = (com.android.internal.telephony.Connection) connections3.get(i3);
                    Log.d(this, "getForegroundCall (IMS) iterate conn = " + connection3, new Object[0]);
                    if (connection3 != null && connection3.isAlive()) {
                        latestConnection = connection3;
                        break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.e(this, e, "getForegroundCall (IMS) Exception in getting element: ", new Object[0]);
                }
            }
        }
        if (latestConnection != null) {
            return latestConnection;
        }
        ArrayList connections4 = imsPhone.getBackgroundCall().getConnections();
        for (int i4 = 0; i4 < connections4.size(); i4++) {
            try {
                com.android.internal.telephony.Connection connection4 = (com.android.internal.telephony.Connection) connections4.get(i4);
                Log.d(this, "getBackgroundCall (IMS)  iterate conn = " + connection4, new Object[0]);
                if (connection4 != null && connection4.isAlive()) {
                    return connection4;
                }
            } catch (IndexOutOfBoundsException e2) {
                Log.e(this, e2, "getBackgroundCall (IMS) Exception in getting element: ", new Object[0]);
                return latestConnection;
            }
        }
        return latestConnection;
    }

    private ArrayList getAllTelephonyConnectionsFromService() {
        ArrayList arrayList = new ArrayList();
        TelephonyConnectionService telephonyConnectionService = this.mConnectionService;
        if (telephonyConnectionService != null) {
            for (Connection connection : telephonyConnectionService.getAllConnections()) {
                if (connection instanceof TelephonyConnection) {
                    arrayList.add((TelephonyConnection) connection);
                }
            }
        }
        return arrayList;
    }

    private ArrayList getAllImsConferenceFromService() {
        ArrayList arrayList = new ArrayList();
        TelephonyConnectionService telephonyConnectionService = this.mConnectionService;
        if (telephonyConnectionService != null) {
            for (Conference conference : telephonyConnectionService.getAllConferences()) {
                if (conference instanceof ImsConference) {
                    arrayList.add((ImsConference) conference);
                }
            }
        }
        return arrayList;
    }

    private Connection findConnection(com.android.internal.telephony.Connection connection) {
        ArrayList allTelephonyConnectionsFromService = getAllTelephonyConnectionsFromService();
        ArrayList allImsConferenceFromService = getAllImsConferenceFromService();
        Log.d(this, "findConnection originalConnection = " + connection, new Object[0]);
        Log.d(this, "findConnection telephonyConnections.size = " + allTelephonyConnectionsFromService.size() + ", imsConferences.size = " + allImsConferenceFromService.size(), new Object[0]);
        TelephonyConnection telephonyConnection = null;
        if (connection != null) {
            int size = allTelephonyConnectionsFromService.size();
            int i = 0;
            while (i < size) {
                Object obj = allTelephonyConnectionsFromService.get(i);
                i++;
                TelephonyConnection telephonyConnection2 = (TelephonyConnection) obj;
                Log.d(this, "findConnection telephonyConnection = " + telephonyConnection2, new Object[0]);
                Log.d(this, "findConnection telephonyConnection.getOriginalConnection = " + telephonyConnection2.getOriginalConnection(), new Object[0]);
                if (connection == telephonyConnection2.getOriginalConnection()) {
                    telephonyConnection = telephonyConnection2;
                }
            }
        }
        if (connection != null) {
            int size2 = allImsConferenceFromService.size();
            int i2 = 0;
            while (i2 < size2) {
                Object obj2 = allImsConferenceFromService.get(i2);
                i2++;
                Connection conferenceHost = ((ImsConference) obj2).getConferenceHost();
                if (conferenceHost == null) {
                    Log.d(this, "findConnection ImsConference connection null.", new Object[0]);
                } else {
                    TelephonyConnection telephonyConnection3 = (TelephonyConnection) conferenceHost;
                    Log.d(this, "findConnection telephonyConnection = " + telephonyConnection3, new Object[0]);
                    Log.d(this, "findConnection telephonyConnection.getOriginalConnection = " + telephonyConnection3.getOriginalConnection(), new Object[0]);
                    if (connection == telephonyConnection3.getOriginalConnection()) {
                        telephonyConnection = telephonyConnection3;
                    }
                }
            }
        }
        return telephonyConnection;
    }

    private Connection getConnectionWithState(Call.State state) {
        Call call;
        ArrayList allTelephonyConnectionsFromService = getAllTelephonyConnectionsFromService();
        if (allTelephonyConnectionsFromService == null) {
            return null;
        }
        int size = allTelephonyConnectionsFromService.size();
        int i = 0;
        while (i < size) {
            Object obj = allTelephonyConnectionsFromService.get(i);
            i++;
            TelephonyConnection telephonyConnection = (TelephonyConnection) obj;
            com.android.internal.telephony.Connection originalConnection = telephonyConnection.getOriginalConnection();
            if (originalConnection != null && (call = originalConnection.getCall()) != null && call.getState() == state) {
                return telephonyConnection;
            }
        }
        return null;
    }
}
