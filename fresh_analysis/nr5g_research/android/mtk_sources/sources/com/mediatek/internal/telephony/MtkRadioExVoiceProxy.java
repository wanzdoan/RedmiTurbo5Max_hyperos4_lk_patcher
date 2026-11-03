package com.mediatek.internal.telephony;

import android.os.RemoteException;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import com.android.internal.telephony.HalVersion;
import com.android.internal.telephony.RILUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import vendor.mediatek.hardware.mtkradioex.voice.CallForwardInfoEx;
import vendor.mediatek.hardware.mtkradioex.voice.IMtkRadioExVoice;

/* JADX INFO: loaded from: classes.dex */
public class MtkRadioExVoiceProxy extends MtkRadioExServiceProxy {
    private static final int CLIENT_RILJ = 0;
    private static final String TAG = "MtkRadioExVoiceProxy";
    private volatile IMtkRadioExVoice mVoiceProxyMtk = null;

    public HalVersion setAidl(HalVersion halVersion, IMtkRadioExVoice voice) {
        this.mHalVersion = halVersion;
        this.mVoiceProxyMtk = voice;
        this.mIsAidl = true;
        try {
            int version = voice.getInterfaceVersion();
            HalVersion newHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_4_0;
            Rlog.d(TAG, "AIDL version=" + version + ", halVersion=" + newHalVersion);
            if (this.mHalVersion.less(newHalVersion)) {
                this.mHalVersion = newHalVersion;
            }
        } catch (RemoteException e) {
            Rlog.e(TAG, "setAidl: " + e);
        }
        Rlog.d(TAG, "AIDL initialized mHalVersion=" + this.mHalVersion);
        return this.mHalVersion;
    }

    public IMtkRadioExVoice getAidl() {
        return this.mVoiceProxyMtk;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void clear() {
        this.mHalVersion = MtkRIL.RADIO_HAL_VERSION_MTK_UNKNOWN;
        this.mRadioProxyMtk = null;
        this.mVoiceProxyMtk = null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxyMtk == null && this.mVoiceProxyMtk == null;
    }

    @Override // com.mediatek.internal.telephony.MtkRadioExServiceProxy
    public void responseAcknowledgementMtk() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.responseAcknowledgementMtk();
        } else {
            this.mRadioProxyMtk.responseAcknowledgementMtk();
        }
    }

    public void hangupAll(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.hangupAll(serial, 0);
        } else {
            this.mRadioProxyMtk.hangupAll(serial);
        }
    }

    public void hangupWithReason(int serial, int callId, int reason) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.hangupWithReason(serial, callId, reason, 0);
        } else {
            this.mRadioProxyMtk.hangupWithReason(serial, callId, reason);
        }
    }

    public void getCallSubAddress(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.getCallSubAddress(serial, 0);
        } else {
            this.mRadioProxyMtk.getCallSubAddress(serial);
        }
    }

    public void getEccNum(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.getEccNum(serial, 0);
        } else {
            this.mRadioProxyMtk.getEccNum(serial);
        }
    }

    public void setCallIndication(int serial, int mode, int callId, int seqNumber, int cause) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setCallIndication(serial, mode, callId, seqNumber, cause, 0);
        } else {
            this.mRadioProxyMtk.setCallIndication(serial, mode, callId, seqNumber, cause);
        }
    }

    public void setCallSubAddress(int serial, boolean enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setCallSubAddress(serial, enable, 0);
        } else {
            this.mRadioProxyMtk.setCallSubAddress(serial, enable);
        }
    }

    public void setEccMode(int serial, String number, int enable, int airplaneMode, int imsReg) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setEccMode(serial, number, enable, airplaneMode, imsReg, 0);
        } else {
            this.mRadioProxyMtk.setEccMode(serial, number, enable, airplaneMode, imsReg);
        }
    }

    public void setEccNum(int serial, String ecc_list_with_card, String ecc_list_no_card) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setEccNum(serial, ecc_list_with_card, ecc_list_no_card, 0);
        } else {
            this.mRadioProxyMtk.setEccNum(serial, ecc_list_with_card, ecc_list_no_card);
        }
    }

    public void setGwsdMode(int serial, ArrayList<String> arrList) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            String[] strMode = {arrList.get(0), arrList.get(1), arrList.get(2)};
            this.mVoiceProxyMtk.setGwsdMode(serial, strMode, 0);
        } else {
            this.mRadioProxyMtk.setGwsdMode(serial, arrList);
        }
    }

    public void setCallValidTimer(int serial, int timer) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setCallValidTimer(serial, timer, 0);
        } else {
            this.mRadioProxyMtk.setCallValidTimer(serial, timer);
        }
    }

    public void setIgnoreSameNumberInterval(int serial, int interval) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setIgnoreSameNumberInterval(serial, interval, 0);
        } else {
            this.mRadioProxyMtk.setIgnoreSameNumberInterval(serial, interval);
        }
    }

    public void setKeepAliveByPDCPCtrlPDU(int serial, String data) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setKeepAliveByPDCPCtrlPDU(serial, data, 0);
        } else {
            this.mRadioProxyMtk.setKeepAliveByPDCPCtrlPDU(serial, data);
        }
    }

    public void setKeepAliveByIpData(int serial, String data) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setKeepAliveByIpData(serial, data, 0);
        } else {
            this.mRadioProxyMtk.setKeepAliveByIpData(serial, data);
        }
    }

    public void setBarringPasswordCheckedByNW(int serial, String facility, String oldPwd, String newPwd, String newCfm) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setBarringPasswordCheckedByNW(serial, facility, oldPwd, newPwd, newCfm, 0);
        } else {
            this.mRadioProxyMtk.setBarringPasswordCheckedByNW(serial, RILUtils.convertNullToEmptyString(facility), RILUtils.convertNullToEmptyString(oldPwd), RILUtils.convertNullToEmptyString(newPwd), RILUtils.convertNullToEmptyString(newCfm));
        }
    }

    public void getColp(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.getColp(serial, 0);
        } else {
            this.mRadioProxyMtk.getColp(serial);
        }
    }

    public void getColr(int serial) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.getColr(serial, 0);
        } else {
            this.mRadioProxyMtk.getColr(serial);
        }
    }

    public void queryCallForwardInTimeSlotStatus(int serial, int cfReason, int serviceClass) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            CallForwardInfoEx cfInfoEx = new CallForwardInfoEx();
            cfInfoEx.reason = cfReason;
            cfInfoEx.serviceClass = serviceClass;
            cfInfoEx.toa = PhoneNumberUtils.toaFromString("");
            cfInfoEx.number = RILUtils.convertNullToEmptyString("");
            cfInfoEx.timeSeconds = 0;
            cfInfoEx.timeSlotBegin = RILUtils.convertNullToEmptyString("");
            cfInfoEx.timeSlotEnd = RILUtils.convertNullToEmptyString("");
            this.mVoiceProxyMtk.queryCallForwardInTimeSlotStatus(serial, cfInfoEx, 0);
            return;
        }
        vendor.mediatek.hardware.mtkradioex.V3_0.CallForwardInfoEx cfInfoEx2 = new vendor.mediatek.hardware.mtkradioex.V3_0.CallForwardInfoEx();
        cfInfoEx2.reason = cfReason;
        cfInfoEx2.serviceClass = serviceClass;
        cfInfoEx2.toa = PhoneNumberUtils.toaFromString("");
        cfInfoEx2.number = RILUtils.convertNullToEmptyString("");
        cfInfoEx2.timeSeconds = 0;
        cfInfoEx2.timeSlotBegin = RILUtils.convertNullToEmptyString("");
        cfInfoEx2.timeSlotEnd = RILUtils.convertNullToEmptyString("");
        this.mRadioProxyMtk.queryCallForwardInTimeSlotStatus(serial, cfInfoEx2);
    }

    public void sendCnap(int serial, String cnapMessage) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.sendCnap(serial, cnapMessage, 0);
        } else {
            this.mRadioProxyMtk.sendCnap(serial, cnapMessage);
        }
    }

    public void setCallForwardInTimeSlot(int serial, int action, int cfReason, int serviceClass, String number, int timeSeconds, long[] timeSlot) throws RemoteException {
        String timeSlotBegin = "";
        String timeSlotEnd = "";
        if (timeSlot != null && timeSlot.length == 2) {
            for (int i = 0; i < timeSlot.length; i++) {
                Date date = new Date(timeSlot[i]);
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                if (i == 0) {
                    timeSlotBegin = dateFormat.format(date);
                } else {
                    timeSlotEnd = dateFormat.format(date);
                }
            }
        }
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            CallForwardInfoEx cfInfoEx = new CallForwardInfoEx();
            cfInfoEx.status = action;
            cfInfoEx.reason = cfReason;
            cfInfoEx.serviceClass = serviceClass;
            cfInfoEx.toa = PhoneNumberUtils.toaFromString(number);
            cfInfoEx.number = RILUtils.convertNullToEmptyString(number);
            cfInfoEx.timeSeconds = timeSeconds;
            cfInfoEx.timeSlotBegin = RILUtils.convertNullToEmptyString(timeSlotBegin);
            cfInfoEx.timeSlotEnd = RILUtils.convertNullToEmptyString(timeSlotEnd);
            this.mVoiceProxyMtk.setCallForwardInTimeSlot(serial, cfInfoEx, 0);
            return;
        }
        vendor.mediatek.hardware.mtkradioex.V3_0.CallForwardInfoEx cfInfoEx2 = new vendor.mediatek.hardware.mtkradioex.V3_0.CallForwardInfoEx();
        cfInfoEx2.status = action;
        cfInfoEx2.reason = cfReason;
        cfInfoEx2.serviceClass = serviceClass;
        cfInfoEx2.toa = PhoneNumberUtils.toaFromString(number);
        cfInfoEx2.number = RILUtils.convertNullToEmptyString(number);
        cfInfoEx2.timeSeconds = timeSeconds;
        cfInfoEx2.timeSlotBegin = RILUtils.convertNullToEmptyString(timeSlotBegin);
        cfInfoEx2.timeSlotEnd = RILUtils.convertNullToEmptyString(timeSlotEnd);
        this.mRadioProxyMtk.setCallForwardInTimeSlot(serial, cfInfoEx2);
    }

    public void setClip(int serial, int enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setClip(serial, enable, 0);
        } else {
            this.mRadioProxyMtk.setClip(serial, enable);
        }
    }

    public void setColp(int serial, int enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setColp(serial, enable, 0);
        } else {
            this.mRadioProxyMtk.setColp(serial, enable);
        }
    }

    public void setColr(int serial, int enable) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setColr(serial, enable, 0);
        } else {
            this.mRadioProxyMtk.setColr(serial, enable);
        }
    }

    public void setSuppServProperty(int serial, String name, String value) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mVoiceProxyMtk.setSuppServProperty(serial, name, value, 0);
        } else {
            this.mRadioProxyMtk.setSuppServProperty(serial, name, value);
        }
    }
}
