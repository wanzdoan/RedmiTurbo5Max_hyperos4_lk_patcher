package com.android.internal.telephony;

import android.hardware.radio.V1_6.IRadio;
import android.hardware.radio.data.DataProfileInfo;
import android.hardware.radio.data.IRadioData;
import android.hardware.radio.data.ImsDataNetworkInfo;
import android.hardware.radio.data.KeepaliveRequest;
import android.net.KeepalivePacketData;
import android.net.LinkProperties;
import android.os.AsyncResult;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.Rlog;
import android.telephony.data.DataProfile;
import android.telephony.data.NetworkSliceInfo;
import android.telephony.data.TrafficDescriptor;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

/* JADX INFO: loaded from: classes.dex */
public class RadioDataProxy extends RadioServiceProxy {
    private volatile IRadioData mDataProxy = null;

    public HalVersion setAidl(HalVersion halVersion, IRadioData iRadioData) {
        try {
            halVersion = RIL.getServiceHalVersion(iRadioData.getInterfaceVersion());
        } catch (RemoteException e) {
            Rlog.e("RadioDataProxy", "setAidl: " + e);
        }
        this.mHalVersion = halVersion;
        this.mDataProxy = iRadioData;
        this.mIsAidl = true;
        Rlog.d("RadioDataProxy", "AIDL initialized mHalVersion=" + this.mHalVersion);
        return this.mHalVersion;
    }

    public IRadioData getAidl() {
        return this.mDataProxy;
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public void clear() {
        super.clear();
        this.mDataProxy = null;
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxy == null && this.mDataProxy == null;
    }

    public void allocatePduSessionId(int i) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.allocatePduSessionId(i);
        } else {
            this.mRadioProxy.allocatePduSessionId(i);
        }
    }

    public void cancelHandover(int i, int i2) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.cancelHandover(i, i2);
        } else {
            this.mRadioProxy.cancelHandover(i, i2);
        }
    }

    public void deactivateDataCall(int i, int i2, int i3) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.deactivateDataCall(i, i2, i3);
        } else {
            this.mRadioProxy.deactivateDataCall_1_2(i, i2, i3);
        }
    }

    public void getDataCallList(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.getDataCallList(i);
            return;
        }
        boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6);
        IRadio iRadio = this.mRadioProxy;
        if (zGreaterOrEqual) {
            iRadio.getDataCallList_1_6(i);
        } else {
            iRadio.getDataCallList(i);
        }
    }

    public void getSlicingConfig(int i) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.getSlicingConfig(i);
        } else {
            this.mRadioProxy.getSlicingConfig(i);
        }
    }

    public void releasePduSessionId(int i, int i2) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.releasePduSessionId(i, i2);
        } else {
            this.mRadioProxy.releasePduSessionId(i, i2);
        }
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public void responseAcknowledgement() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.responseAcknowledgement();
        } else {
            this.mRadioProxy.responseAcknowledgement();
        }
    }

    public void setDataAllowed(int i, boolean z) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.setDataAllowed(i, z);
        } else {
            this.mRadioProxy.setDataAllowed(i, z);
        }
    }

    public void setDataProfile(int i, DataProfile[] dataProfileArr) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        int i2 = 0;
        if (isAidl()) {
            DataProfileInfo[] dataProfileInfoArr = new DataProfileInfo[dataProfileArr.length];
            while (i2 < dataProfileArr.length) {
                dataProfileInfoArr[i2] = RILUtils.convertToHalDataProfile(dataProfileArr[i2], this.mHalVersion);
                i2++;
            }
            this.mDataProxy.setDataProfile(i, dataProfileInfoArr);
            return;
        }
        if (this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            ArrayList arrayList = new ArrayList();
            int length = dataProfileArr.length;
            while (i2 < length) {
                arrayList.add(RILUtils.convertToHalDataProfile15(dataProfileArr[i2]));
                i2++;
            }
            this.mRadioProxy.setDataProfile_1_5(i, arrayList);
            return;
        }
        ArrayList arrayList2 = new ArrayList();
        int length2 = dataProfileArr.length;
        while (i2 < length2) {
            arrayList2.add(RILUtils.convertToHalDataProfile14(dataProfileArr[i2]));
            i2++;
        }
        this.mRadioProxy.setDataProfile_1_4(i, arrayList2);
    }

    public void setDataThrottling(int i, byte b, long j) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.setDataThrottling(i, b, j);
        } else {
            this.mRadioProxy.setDataThrottling(i, b, j);
        }
    }

    public void setInitialAttachApn(int i, DataProfile dataProfile) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.setInitialAttachApn(i, RILUtils.convertToHalDataProfile(dataProfile, this.mHalVersion));
            return;
        }
        boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5);
        android.hardware.radio.V1_5.IRadio iRadio = this.mRadioProxy;
        if (zGreaterOrEqual) {
            iRadio.setInitialAttachApn_1_5(i, RILUtils.convertToHalDataProfile15(dataProfile));
        } else {
            iRadio.setInitialAttachApn_1_4(i, RILUtils.convertToHalDataProfile14(dataProfile));
        }
    }

    public void setupDataCall(int i, int i2, DataProfile dataProfile, boolean z, int i3, LinkProperties linkProperties, int i4, NetworkSliceInfo networkSliceInfo, TrafficDescriptor trafficDescriptor, boolean z2) throws RemoteException {
        String[] strArr;
        if (isEmpty()) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        if (linkProperties != null) {
            Iterator it = linkProperties.getAddresses().iterator();
            while (it.hasNext()) {
                arrayList.add(((InetAddress) it.next()).getHostAddress());
            }
            strArr = new String[linkProperties.getDnsServers().size()];
            for (int i5 = 0; i5 < linkProperties.getDnsServers().size(); i5++) {
                arrayList2.add(linkProperties.getDnsServers().get(i5).getHostAddress());
                strArr[i5] = linkProperties.getDnsServers().get(i5).getHostAddress();
            }
        } else {
            strArr = new String[0];
        }
        String[] strArr2 = strArr;
        if (isAidl()) {
            this.mDataProxy.setupDataCall(i, i2, RILUtils.convertToHalDataProfile(new DataProfile.Builder().setType(dataProfile.getType()).setPreferred(dataProfile.isPreferred()).setTrafficDescriptor(trafficDescriptor).setApnSetting(dataProfile.getApnSetting()).build(), this.mHalVersion), z, i3, RILUtils.convertToHalLinkProperties(linkProperties), strArr2, i4, RILUtils.convertToHalSliceInfoAidl(networkSliceInfo), z2);
        } else {
            if (this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6)) {
                this.mRadioProxy.setupDataCall_1_6(i, i2, RILUtils.convertToHalDataProfile15(dataProfile), z, i3, RILUtils.convertToHalLinkProperties15(linkProperties), arrayList2, i4, RILUtils.convertToHalSliceInfo(networkSliceInfo), RILUtils.convertToHalTrafficDescriptor(trafficDescriptor), z2);
                return;
            }
            boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5);
            android.hardware.radio.V1_5.IRadio iRadio = this.mRadioProxy;
            if (zGreaterOrEqual) {
                iRadio.setupDataCall_1_5(i, i2, RILUtils.convertToHalDataProfile15(dataProfile), z, i3, RILUtils.convertToHalLinkProperties15(linkProperties), arrayList2);
            } else {
                iRadio.setupDataCall_1_4(i, i2, RILUtils.convertToHalDataProfile14(dataProfile), z, i3, arrayList, arrayList2);
            }
        }
    }

    public void startHandover(int i, int i2) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.startHandover(i, i2);
        } else {
            this.mRadioProxy.startHandover(i, i2);
        }
    }

    public void startKeepalive(int i, int i2, KeepalivePacketData keepalivePacketData, int i3, Message message) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            KeepaliveRequest keepaliveRequest = new KeepaliveRequest();
            keepaliveRequest.cid = i2;
            if (keepalivePacketData.getDstAddress() instanceof Inet4Address) {
                keepaliveRequest.type = 0;
            } else if (keepalivePacketData.getDstAddress() instanceof Inet6Address) {
                keepaliveRequest.type = 1;
            } else {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(44));
                message.sendToTarget();
                return;
            }
            InetAddress srcAddress = keepalivePacketData.getSrcAddress();
            InetAddress dstAddress = keepalivePacketData.getDstAddress();
            int length = srcAddress.getAddress().length;
            byte[] bArr = new byte[length];
            for (int i4 = 0; i4 < length; i4++) {
                bArr[i4] = srcAddress.getAddress()[i4];
            }
            keepaliveRequest.sourceAddress = bArr;
            keepaliveRequest.sourcePort = keepalivePacketData.getSrcPort();
            int length2 = dstAddress.getAddress().length;
            byte[] bArr2 = new byte[length2];
            for (int i5 = 0; i5 < length2; i5++) {
                bArr2[i5] = dstAddress.getAddress()[i5];
            }
            keepaliveRequest.destinationAddress = bArr2;
            keepaliveRequest.destinationPort = keepalivePacketData.getDstPort();
            keepaliveRequest.maxKeepaliveIntervalMillis = i3;
            this.mDataProxy.startKeepalive(i, keepaliveRequest);
            return;
        }
        android.hardware.radio.V1_1.KeepaliveRequest keepaliveRequest2 = new android.hardware.radio.V1_1.KeepaliveRequest();
        keepaliveRequest2.cid = i2;
        if (keepalivePacketData.getDstAddress() instanceof Inet4Address) {
            keepaliveRequest2.type = 0;
        } else if (keepalivePacketData.getDstAddress() instanceof Inet6Address) {
            keepaliveRequest2.type = 1;
        } else {
            AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(44));
            message.sendToTarget();
            return;
        }
        InetAddress srcAddress2 = keepalivePacketData.getSrcAddress();
        InetAddress dstAddress2 = keepalivePacketData.getDstAddress();
        RILUtils.appendPrimitiveArrayToArrayList(srcAddress2.getAddress(), keepaliveRequest2.sourceAddress);
        keepaliveRequest2.sourcePort = keepalivePacketData.getSrcPort();
        RILUtils.appendPrimitiveArrayToArrayList(dstAddress2.getAddress(), keepaliveRequest2.destinationAddress);
        keepaliveRequest2.destinationPort = keepalivePacketData.getDstPort();
        keepaliveRequest2.maxKeepaliveIntervalMillis = i3;
        this.mRadioProxy.startKeepalive(i, keepaliveRequest2);
    }

    public void stopKeepalive(int i, int i2) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mDataProxy.stopKeepalive(i, i2);
        } else {
            this.mRadioProxy.stopKeepalive(i, i2);
        }
    }

    public void setUserDataEnabled(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mDataProxy.setUserDataEnabled(i, z);
        }
    }

    public void setUserDataRoamingEnabled(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mDataProxy.setUserDataRoamingEnabled(i, z);
        }
    }

    public void notifyImsDataNetwork(int i, ImsDataNetworkInfo imsDataNetworkInfo) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mDataProxy.notifyImsDataNetwork(i, imsDataNetworkInfo);
        }
    }
}
