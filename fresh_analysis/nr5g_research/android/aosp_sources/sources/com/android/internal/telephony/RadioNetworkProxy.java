package com.android.internal.telephony;

import android.hardware.radio.V1_6.IRadio;
import android.hardware.radio.network.EmergencyNetworkScanTrigger;
import android.hardware.radio.network.IRadioNetwork;
import android.hardware.radio.network.PrioritizedNetworkScanRequest;
import android.hardware.radio.network.SatelliteNetworkInfo;
import android.os.AsyncResult;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.NetworkScanRequest;
import android.telephony.RadioAccessSpecifier;
import android.telephony.Rlog;
import android.telephony.SignalThresholdInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public class RadioNetworkProxy extends RadioServiceProxy {
    private volatile IRadioNetwork mNetworkProxy = null;

    public void getAvailableBandModes(int i) throws RemoteException {
    }

    public void setBandMode(int i, int i2) throws RemoteException {
    }

    public void setCdmaRoamingPreference(int i, int i2) throws RemoteException {
    }

    public void setLocationUpdates(int i, boolean z) throws RemoteException {
    }

    public void setSuppServiceNotifications(int i, boolean z) throws RemoteException {
    }

    public HalVersion setAidl(HalVersion halVersion, IRadioNetwork iRadioNetwork) {
        try {
            halVersion = RIL.getServiceHalVersion(iRadioNetwork.getInterfaceVersion());
        } catch (RemoteException e) {
            Rlog.e("RadioNetworkProxy", "setAidl: " + e);
        }
        this.mHalVersion = halVersion;
        this.mNetworkProxy = iRadioNetwork;
        this.mIsAidl = true;
        Rlog.d("RadioNetworkProxy", "AIDL initialized mHalVersion=" + this.mHalVersion);
        return this.mHalVersion;
    }

    public IRadioNetwork getAidl() {
        return this.mNetworkProxy;
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public void clear() {
        super.clear();
        this.mNetworkProxy = null;
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public boolean isEmpty() {
        return this.mRadioProxy == null && this.mNetworkProxy == null;
    }

    public void getAllowedNetworkTypesBitmap(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getAllowedNetworkTypesBitmap(i);
            return;
        }
        boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6);
        IRadio iRadio = this.mRadioProxy;
        if (zGreaterOrEqual) {
            iRadio.getAllowedNetworkTypesBitmap(i);
        } else {
            iRadio.getPreferredNetworkTypeBitmap(i);
        }
    }

    public void getAvailableNetworks(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getAvailableNetworks(i);
        } else {
            this.mRadioProxy.getAvailableNetworks(i);
        }
    }

    public void getBarringInfo(int i) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_5)) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getBarringInfo(i);
        } else {
            this.mRadioProxy.getBarringInfo(i);
        }
    }

    public void getCellInfoList(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getCellInfoList(i);
            return;
        }
        boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6);
        IRadio iRadio = this.mRadioProxy;
        if (zGreaterOrEqual) {
            iRadio.getCellInfoList_1_6(i);
        } else {
            iRadio.getCellInfoList(i);
        }
    }

    public void getDataRegistrationState(int i, HalVersion halVersion) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getDataRegistrationState(i);
            return;
        }
        if ((halVersion == null || halVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6)) && this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6)) {
            this.mRadioProxy.getDataRegistrationState_1_6(i);
        } else if ((halVersion == null || halVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) && this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            this.mRadioProxy.getDataRegistrationState_1_5(i);
        } else {
            this.mRadioProxy.getDataRegistrationState(i);
        }
    }

    public void getImsRegistrationState(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getImsRegistrationState(i);
        } else {
            this.mRadioProxy.getImsRegistrationState(i);
        }
    }

    public void getNetworkSelectionMode(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getNetworkSelectionMode(i);
        } else {
            this.mRadioProxy.getNetworkSelectionMode(i);
        }
    }

    public void getOperator(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getOperator(i);
        } else {
            this.mRadioProxy.getOperator(i);
        }
    }

    public void getSignalStrength(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getSignalStrength(i);
            return;
        }
        boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6);
        IRadio iRadio = this.mRadioProxy;
        if (zGreaterOrEqual) {
            iRadio.getSignalStrength_1_6(i);
        } else {
            iRadio.getSignalStrength_1_4(i);
        }
    }

    public void getSystemSelectionChannels(int i) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getSystemSelectionChannels(i);
        } else {
            this.mRadioProxy.getSystemSelectionChannels(i);
        }
    }

    public void getVoiceRadioTechnology(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getVoiceRadioTechnology(i);
        } else {
            this.mRadioProxy.getVoiceRadioTechnology(i);
        }
    }

    public void getVoiceRegistrationState(int i, HalVersion halVersion) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.getVoiceRegistrationState(i);
            return;
        }
        if ((halVersion == null || halVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6)) && this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6)) {
            this.mRadioProxy.getVoiceRegistrationState_1_6(i);
        } else if ((halVersion == null || halVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) && this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            this.mRadioProxy.getVoiceRegistrationState_1_5(i);
        } else {
            this.mRadioProxy.getVoiceRegistrationState(i);
        }
    }

    public void isNrDualConnectivityEnabled(int i) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.isNrDualConnectivityEnabled(i);
        } else {
            this.mRadioProxy.isNrDualConnectivityEnabled(i);
        }
    }

    @Override // com.android.internal.telephony.RadioServiceProxy
    public void responseAcknowledgement() throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.responseAcknowledgement();
        } else {
            this.mRadioProxy.responseAcknowledgement();
        }
    }

    public void setAllowedNetworkTypesBitmap(int i, int i2) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setAllowedNetworkTypesBitmap(i, RILUtils.convertToHalRadioAccessFamilyAidl(i2));
        } else {
            this.mRadioProxy.setAllowedNetworkTypesBitmap(i, RILUtils.convertToHalRadioAccessFamily(i2));
        }
    }

    public void setPreferredNetworkTypeBitmap(int i, int i2) throws RemoteException {
        if (isEmpty() || this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        this.mRadioProxy.setPreferredNetworkTypeBitmap(i, RILUtils.convertToHalRadioAccessFamily(i2));
    }

    public void setBarringPassword(int i, String str, String str2, String str3) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setBarringPassword(i, str, str2, str3);
        } else {
            this.mRadioProxy.setBarringPassword(i, str, str2, str3);
        }
    }

    public void setCellInfoListRate(int i, int i2) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setCellInfoListRate(i, i2);
        } else {
            this.mRadioProxy.setCellInfoListRate(i, i2);
        }
    }

    public void setIndicationFilter(int i, int i2) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setIndicationFilter(i, i2 & 255);
            return;
        }
        boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5);
        android.hardware.radio.V1_5.IRadio iRadio = this.mRadioProxy;
        if (zGreaterOrEqual) {
            iRadio.setIndicationFilter_1_5(i, i2 & 127);
        } else {
            iRadio.setIndicationFilter_1_2(i, i2 & 31);
        }
    }

    public void setLinkCapacityReportingCriteria(int i, int i2, int i3, int i4, int[] iArr, int[] iArr2, int i5) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setLinkCapacityReportingCriteria(i, i2, i3, i4, iArr, iArr2, RILUtils.convertToHalAccessNetworkAidl(i5));
            return;
        }
        if (this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            this.mRadioProxy.setLinkCapacityReportingCriteria_1_5(i, i2, i3, i4, RILUtils.primitiveArrayToArrayList(iArr), RILUtils.primitiveArrayToArrayList(iArr2), RILUtils.convertToHalAccessNetwork(i5));
        } else {
            if (i5 == 6) {
                throw new RuntimeException("NGRAN unsupported on IRadio version: " + this.mHalVersion);
            }
            this.mRadioProxy.setLinkCapacityReportingCriteria(i, i2, i3, i4, RILUtils.primitiveArrayToArrayList(iArr), RILUtils.primitiveArrayToArrayList(iArr2), RILUtils.convertToHalAccessNetwork(i5));
        }
    }

    public void setNetworkSelectionModeAutomatic(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setNetworkSelectionModeAutomatic(i);
        } else {
            this.mRadioProxy.setNetworkSelectionModeAutomatic(i);
        }
    }

    public void setNetworkSelectionModeManual(int i, String str, int i2) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setNetworkSelectionModeManual(i, str, RILUtils.convertToHalAccessNetworkAidl(i2));
            return;
        }
        boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5);
        android.hardware.radio.V1_5.IRadio iRadio = this.mRadioProxy;
        if (zGreaterOrEqual) {
            iRadio.setNetworkSelectionModeManual_1_5(i, str, RILUtils.convertToHalRadioAccessNetworks(i2));
        } else {
            iRadio.setNetworkSelectionModeManual(i, str);
        }
    }

    public void setNrDualConnectivityState(int i, byte b) throws RemoteException {
        if (isEmpty() || this.mHalVersion.less(RIL.RADIO_HAL_VERSION_1_6)) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setNrDualConnectivityState(i, b);
        } else {
            this.mRadioProxy.setNrDualConnectivityState(i, b);
        }
    }

    public void setSignalStrengthReportingCriteria(int i, List<SignalThresholdInfo> list) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            android.hardware.radio.network.SignalThresholdInfo[] signalThresholdInfoArr = new android.hardware.radio.network.SignalThresholdInfo[list.size()];
            for (int i2 = 0; i2 < list.size(); i2++) {
                signalThresholdInfoArr[i2] = RILUtils.convertToHalSignalThresholdInfoAidl(list.get(i2));
            }
            this.mNetworkProxy.setSignalStrengthReportingCriteria(i, signalThresholdInfoArr);
            return;
        }
        if (this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            for (SignalThresholdInfo signalThresholdInfo : list) {
                this.mRadioProxy.setSignalStrengthReportingCriteria_1_5(i, RILUtils.convertToHalSignalThresholdInfo(signalThresholdInfo), RILUtils.convertToHalAccessNetwork(signalThresholdInfo.getRadioAccessNetworkType()));
            }
            return;
        }
        for (SignalThresholdInfo signalThresholdInfo2 : list) {
            this.mRadioProxy.setSignalStrengthReportingCriteria(i, signalThresholdInfo2.getHysteresisMs(), signalThresholdInfo2.getHysteresisDb(), RILUtils.primitiveArrayToArrayList(signalThresholdInfo2.getThresholds()), RILUtils.convertToHalAccessNetwork(signalThresholdInfo2.getRadioAccessNetworkType()));
        }
    }

    public void setSystemSelectionChannels(int i, List<RadioAccessSpecifier> list) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.setSystemSelectionChannels(i, !list.isEmpty(), (android.hardware.radio.network.RadioAccessSpecifier[]) list.stream().map(new Function() { // from class: com.android.internal.telephony.RadioNetworkProxy$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return RILUtils.convertToHalRadioAccessSpecifierAidl((RadioAccessSpecifier) obj);
                }
            }).toArray(new IntFunction() { // from class: com.android.internal.telephony.RadioNetworkProxy$$ExternalSyntheticLambda3
                @Override // java.util.function.IntFunction
                public final Object apply(int i2) {
                    return RadioNetworkProxy.m391$r8$lambda$Zf5MSwtBJ4xsKVGbNbj3T7hzE(i2);
                }
            }));
            return;
        }
        boolean zGreaterOrEqual = this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5);
        android.hardware.radio.V1_5.IRadio iRadio = this.mRadioProxy;
        if (zGreaterOrEqual) {
            iRadio.setSystemSelectionChannels_1_5(i, !list.isEmpty(), (ArrayList) list.stream().map(new Function() { // from class: com.android.internal.telephony.RadioNetworkProxy$$ExternalSyntheticLambda4
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return RILUtils.convertToHalRadioAccessSpecifier15((RadioAccessSpecifier) obj);
                }
            }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.internal.telephony.RadioNetworkProxy$$ExternalSyntheticLambda5
                @Override // java.util.function.Supplier
                public final Object get() {
                    return new ArrayList();
                }
            })));
        } else {
            iRadio.setSystemSelectionChannels(i, !list.isEmpty(), (ArrayList) list.stream().map(new Function() { // from class: com.android.internal.telephony.RadioNetworkProxy$$ExternalSyntheticLambda6
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return RILUtils.convertToHalRadioAccessSpecifier11((RadioAccessSpecifier) obj);
                }
            }).collect(Collectors.toCollection(new Supplier() { // from class: com.android.internal.telephony.RadioNetworkProxy$$ExternalSyntheticLambda5
                @Override // java.util.function.Supplier
                public final Object get() {
                    return new ArrayList();
                }
            })));
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$Zf5M-SwtBJ4xsKVGbN-bj3T7hzE, reason: not valid java name */
    public static /* synthetic */ android.hardware.radio.network.RadioAccessSpecifier[] m391$r8$lambda$Zf5MSwtBJ4xsKVGbNbj3T7hzE(int i) {
        return new android.hardware.radio.network.RadioAccessSpecifier[i];
    }

    public void startNetworkScan(int i, NetworkScanRequest networkScanRequest, HalVersion halVersion, Message message) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        int i2 = 0;
        if (isAidl()) {
            android.hardware.radio.network.NetworkScanRequest networkScanRequest2 = new android.hardware.radio.network.NetworkScanRequest();
            networkScanRequest2.type = networkScanRequest.getScanType();
            networkScanRequest2.interval = networkScanRequest.getSearchPeriodicity();
            networkScanRequest2.maxSearchTime = networkScanRequest.getMaxSearchTime();
            networkScanRequest2.incrementalResultsPeriodicity = networkScanRequest.getIncrementalResultsPeriodicity();
            networkScanRequest2.incrementalResults = networkScanRequest.getIncrementalResults();
            networkScanRequest2.mccMncs = (String[]) networkScanRequest.getPlmns().stream().toArray(new IntFunction() { // from class: com.android.internal.telephony.RadioNetworkProxy$$ExternalSyntheticLambda0
                @Override // java.util.function.IntFunction
                public final Object apply(int i3) {
                    return RadioNetworkProxy.m392$r8$lambda$tyP24y5DXV8n15PbT4oGSMec3M(i3);
                }
            });
            ArrayList arrayList = new ArrayList();
            RadioAccessSpecifier[] specifiers = networkScanRequest.getSpecifiers();
            int length = specifiers.length;
            while (i2 < length) {
                android.hardware.radio.network.RadioAccessSpecifier radioAccessSpecifierConvertToHalRadioAccessSpecifierAidl = RILUtils.convertToHalRadioAccessSpecifierAidl(specifiers[i2]);
                if (radioAccessSpecifierConvertToHalRadioAccessSpecifierAidl == null) {
                    AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                    message.sendToTarget();
                    return;
                } else {
                    arrayList.add(radioAccessSpecifierConvertToHalRadioAccessSpecifierAidl);
                    i2++;
                }
            }
            networkScanRequest2.specifiers = (android.hardware.radio.network.RadioAccessSpecifier[]) arrayList.stream().toArray(new IntFunction() { // from class: com.android.internal.telephony.RadioNetworkProxy$$ExternalSyntheticLambda1
                @Override // java.util.function.IntFunction
                public final Object apply(int i3) {
                    return RadioNetworkProxy.$r8$lambda$xep5kfGgIjtD_me1UqUFJh6LP5g(i3);
                }
            });
            this.mNetworkProxy.startNetworkScan(i, networkScanRequest2);
            return;
        }
        if ((halVersion == null || halVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) && this.mHalVersion.greaterOrEqual(RIL.RADIO_HAL_VERSION_1_5)) {
            android.hardware.radio.V1_5.NetworkScanRequest networkScanRequest3 = new android.hardware.radio.V1_5.NetworkScanRequest();
            networkScanRequest3.type = networkScanRequest.getScanType();
            networkScanRequest3.interval = networkScanRequest.getSearchPeriodicity();
            networkScanRequest3.maxSearchTime = networkScanRequest.getMaxSearchTime();
            networkScanRequest3.incrementalResultsPeriodicity = networkScanRequest.getIncrementalResultsPeriodicity();
            networkScanRequest3.incrementalResults = networkScanRequest.getIncrementalResults();
            networkScanRequest3.mccMncs.addAll(networkScanRequest.getPlmns());
            RadioAccessSpecifier[] specifiers2 = networkScanRequest.getSpecifiers();
            int length2 = specifiers2.length;
            while (i2 < length2) {
                android.hardware.radio.V1_5.RadioAccessSpecifier radioAccessSpecifierConvertToHalRadioAccessSpecifier15 = RILUtils.convertToHalRadioAccessSpecifier15(specifiers2[i2]);
                if (radioAccessSpecifierConvertToHalRadioAccessSpecifier15 == null) {
                    AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                    message.sendToTarget();
                    return;
                } else {
                    networkScanRequest3.specifiers.add(radioAccessSpecifierConvertToHalRadioAccessSpecifier15);
                    i2++;
                }
            }
            this.mRadioProxy.startNetworkScan_1_5(i, networkScanRequest3);
            return;
        }
        android.hardware.radio.V1_2.NetworkScanRequest networkScanRequest4 = new android.hardware.radio.V1_2.NetworkScanRequest();
        networkScanRequest4.type = networkScanRequest.getScanType();
        networkScanRequest4.interval = networkScanRequest.getSearchPeriodicity();
        networkScanRequest4.maxSearchTime = networkScanRequest.getMaxSearchTime();
        networkScanRequest4.incrementalResultsPeriodicity = networkScanRequest.getIncrementalResultsPeriodicity();
        networkScanRequest4.incrementalResults = networkScanRequest.getIncrementalResults();
        networkScanRequest4.mccMncs.addAll(networkScanRequest.getPlmns());
        RadioAccessSpecifier[] specifiers3 = networkScanRequest.getSpecifiers();
        int length3 = specifiers3.length;
        while (i2 < length3) {
            android.hardware.radio.V1_1.RadioAccessSpecifier radioAccessSpecifierConvertToHalRadioAccessSpecifier11 = RILUtils.convertToHalRadioAccessSpecifier11(specifiers3[i2]);
            if (radioAccessSpecifierConvertToHalRadioAccessSpecifier11 == null) {
                AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
                message.sendToTarget();
                return;
            } else {
                networkScanRequest4.specifiers.add(radioAccessSpecifierConvertToHalRadioAccessSpecifier11);
                i2++;
            }
        }
        this.mRadioProxy.startNetworkScan_1_4(i, networkScanRequest4);
    }

    /* JADX INFO: renamed from: $r8$lambda$tyP24y5DXV8n15P-bT4oGSMec3M, reason: not valid java name */
    public static /* synthetic */ String[] m392$r8$lambda$tyP24y5DXV8n15PbT4oGSMec3M(int i) {
        return new String[i];
    }

    public static /* synthetic */ android.hardware.radio.network.RadioAccessSpecifier[] $r8$lambda$xep5kfGgIjtD_me1UqUFJh6LP5g(int i) {
        return new android.hardware.radio.network.RadioAccessSpecifier[i];
    }

    public void stopNetworkScan(int i) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.stopNetworkScan(i);
        } else {
            this.mRadioProxy.stopNetworkScan(i);
        }
    }

    public void supplyNetworkDepersonalization(int i, String str) throws RemoteException {
        if (isEmpty()) {
            return;
        }
        if (isAidl()) {
            this.mNetworkProxy.supplyNetworkDepersonalization(i, str);
        } else {
            this.mRadioProxy.supplyNetworkDepersonalization(i, str);
        }
    }

    public void getUsageSetting(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.getUsageSetting(i);
        }
    }

    public void setUsageSetting(int i, int i2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setUsageSetting(i, i2);
        }
    }

    public void setEmergencyMode(int i, int i2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setEmergencyMode(i, i2);
        }
    }

    public void triggerEmergencyNetworkScan(int i, EmergencyNetworkScanTrigger emergencyNetworkScanTrigger) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.triggerEmergencyNetworkScan(i, emergencyNetworkScanTrigger);
        }
    }

    public void cancelEmergencyNetworkScan(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.cancelEmergencyNetworkScan(i, z);
        }
    }

    public void exitEmergencyMode(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.exitEmergencyMode(i);
        }
    }

    public void setNullCipherAndIntegrityEnabled(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setNullCipherAndIntegrityEnabled(i, z);
        }
    }

    public void isNullCipherAndIntegrityEnabled(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.isNullCipherAndIntegrityEnabled(i);
        }
    }

    public void isN1ModeEnabled(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.isN1ModeEnabled(i);
        }
    }

    public void setN1ModeEnabled(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setN1ModeEnabled(i, z);
        }
    }

    public void setCellularIdentifierTransparencyEnabled(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setCellularIdentifierTransparencyEnabled(i, z);
        }
    }

    public void isCellularIdentifierTransparencyEnabled(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.isCellularIdentifierTransparencyEnabled(i);
        }
    }

    public void isSecurityAlgorithmsUpdatedEnabled(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.isSecurityAlgorithmsUpdatedEnabled(i);
        }
    }

    public void setSecurityAlgorithmsUpdatedEnabled(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setSecurityAlgorithmsUpdatedEnabled(i, z);
        }
    }

    public void setSatellitePlmn(int i, List<String> list, List<String> list2) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setSatellitePlmn(i, (String[]) list.toArray(new String[0]), (String[]) list2.toArray(new String[0]));
        }
    }

    public void setSatelliteEnabledForCarrier(int i, boolean z) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setSatelliteEnabledForCarrier(i, z);
        }
    }

    public void isSatelliteEnabledForCarrier(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.isSatelliteEnabledForCarrier(i);
        }
    }

    public void setSatelliteNetworkInfo(int i, SatelliteNetworkInfo satelliteNetworkInfo) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.setSatelliteNetworkInfo(i, satelliteNetworkInfo);
        }
    }

    public void enablePrioritizedNetworkScan(int i, PrioritizedNetworkScanRequest prioritizedNetworkScanRequest) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.enablePrioritizedNetworkScan(i, prioritizedNetworkScanRequest);
        }
    }

    public void disablePrioritizedNetworkScan(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.disablePrioritizedNetworkScan(i);
        }
    }

    public void getSupportedNetworkAlertCategories(int i) throws RemoteException {
        if (!isEmpty() && isAidl()) {
            this.mNetworkProxy.getSupportedNetworkAlertCategories(i);
        }
    }
}
