package com.android.internal.telephony;

import android.hardware.radio.V1_2.VoiceRegStateResult;
import android.hardware.radio.V1_4.DataRegStateResult;
import android.hardware.radio.V1_4.LteVopsInfo;
import android.hardware.radio.V1_4.NrIndicators;
import android.hardware.radio.network.AccessTechnologySpecificInfo;
import android.hardware.radio.network.RegStateResult;
import android.os.AsyncResult;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.AnomalyReporter;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.DataSpecificRegistrationInfo;
import android.telephony.LteVopsSupportInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.NetworkService;
import android.telephony.NetworkServiceCallback;
import android.telephony.NrVopsSupportInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.VopsSupportInfo;
import android.text.TextUtils;
import android.util.ArrayMap;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/* JADX INFO: loaded from: classes.dex */
public class CellularNetworkService extends NetworkService {
    private static final String TAG = CellularNetworkService.class.getSimpleName();
    private static final Map sNetworkTypes;

    static {
        ArrayMap arrayMap = new ArrayMap();
        sNetworkTypes = arrayMap;
        arrayMap.put(CellIdentityGsm.class, Arrays.asList(16, 1, 2));
        arrayMap.put(CellIdentityWcdma.class, Arrays.asList(3, 8, 9, 10, 15));
        arrayMap.put(CellIdentityCdma.class, Arrays.asList(4, 7, 5, 6, 12, 14));
        arrayMap.put(CellIdentityLte.class, Arrays.asList(13));
        arrayMap.put(CellIdentityNr.class, Arrays.asList(20, 30));
        arrayMap.put(CellIdentityTdscdma.class, Arrays.asList(17));
    }

    protected class CellularNetworkServiceProvider extends NetworkService.NetworkServiceProvider {
        private final Map<Message, NetworkServiceCallback> mCallbackMap;
        private final Handler mHandler;
        private final Phone mPhone;

        private int getRegStateFromHalRegState(int i) {
            int i2 = 1;
            if (i != 1) {
                i2 = 2;
                if (i != 2) {
                    if (i != 3) {
                        if (i != 4) {
                            if (i == 5) {
                                return 5;
                            }
                            if (i == 20) {
                                return 6;
                            }
                            switch (i) {
                                case 12:
                                    break;
                                case 13:
                                    break;
                                case 14:
                                    break;
                                default:
                                    return 0;
                            }
                        }
                        return 4;
                    }
                    return 3;
                }
            }
            return i2;
        }

        private boolean isEmergencyOnly(int i) {
            if (i == 10 || i == 20) {
                return true;
            }
            switch (i) {
                case 12:
                case 13:
                case 14:
                    return true;
                default:
                    return false;
            }
        }

        protected CellularNetworkServiceProvider(CellularNetworkService cellularNetworkService) {
            this(cellularNetworkService, Integer.MAX_VALUE, false);
        }

        protected CellularNetworkServiceProvider(CellularNetworkService cellularNetworkService, int i, boolean z) {
            this(i);
            if (z) {
                return;
            }
            this.mPhone.mCi.unregisterForNetworkStateChanged(this.mHandler);
        }

        CellularNetworkServiceProvider(int i) {
            super(CellularNetworkService.this, i);
            this.mCallbackMap = new HashMap();
            Phone phone = PhoneFactory.getPhone(getSlotIndex());
            this.mPhone = phone;
            Handler handler = new Handler(Looper.myLooper()) { // from class: com.android.internal.telephony.CellularNetworkService.CellularNetworkServiceProvider.1
                @Override // android.os.Handler
                public void handleMessage(Message message) {
                    NetworkServiceCallback networkServiceCallback = (NetworkServiceCallback) CellularNetworkServiceProvider.this.mCallbackMap.remove(message);
                    int i2 = message.what;
                    if (i2 != 1 && i2 != 2) {
                        if (i2 != 3) {
                            return;
                        }
                        CellularNetworkServiceProvider.this.notifyNetworkRegistrationInfoChanged();
                    } else {
                        if (networkServiceCallback == null) {
                            return;
                        }
                        AsyncResult asyncResult = (AsyncResult) message.obj;
                        NetworkRegistrationInfo registrationStateFromResult = CellularNetworkServiceProvider.this.getRegistrationStateFromResult(asyncResult.result, i2 == 1 ? 1 : 2);
                        try {
                            networkServiceCallback.onRequestNetworkRegistrationInfoComplete((asyncResult.exception != null || registrationStateFromResult == null) ? 5 : 0, registrationStateFromResult);
                        } catch (Exception e) {
                            CellularNetworkService.loge("Exception: " + e);
                        }
                    }
                }
            };
            this.mHandler = handler;
            phone.mCi.registerForNetworkStateChanged(handler, 3, null);
        }

        private List<Integer> getAvailableServices(int i, int i2, boolean z) {
            ArrayList arrayList = new ArrayList();
            if (z) {
                arrayList.add(5);
                return arrayList;
            }
            if (i == 5 || i == 1) {
                if (i2 == 2) {
                    arrayList.add(2);
                    if (CellularNetworkService.this.isMmsEnabled(this.mPhone)) {
                        arrayList.add(6);
                        return arrayList;
                    }
                } else if (i2 == 1) {
                    arrayList.add(1);
                    arrayList.add(3);
                    arrayList.add(4);
                }
            }
            return arrayList;
        }

        protected NetworkRegistrationInfo getRegistrationStateFromResult(Object obj, int i) {
            if (obj == null) {
                return null;
            }
            if (i == 1) {
                return createRegistrationStateFromVoiceRegState(obj);
            }
            if (i == 2) {
                return createRegistrationStateFromDataRegState(obj);
            }
            return null;
        }

        private String getPlmnFromCellIdentity(CellIdentity cellIdentity) {
            if (cellIdentity == null || (cellIdentity instanceof CellIdentityCdma)) {
                return "";
            }
            String mccString = cellIdentity.getMccString();
            String mncString = cellIdentity.getMncString();
            if (TextUtils.isEmpty(mccString) || TextUtils.isEmpty(mncString)) {
                return "";
            }
            return mccString + mncString;
        }

        private NetworkRegistrationInfo createRegistrationStateFromVoiceRegState(Object obj) {
            if (obj instanceof RegStateResult) {
                return getNetworkRegistrationInfoAidl(1, 1, (RegStateResult) obj);
            }
            if (obj instanceof android.hardware.radio.V1_6.RegStateResult) {
                return getNetworkRegistrationInfo1_6(1, 1, (android.hardware.radio.V1_6.RegStateResult) obj);
            }
            if (obj instanceof android.hardware.radio.V1_5.RegStateResult) {
                return getNetworkRegistrationInfo(1, 1, (android.hardware.radio.V1_5.RegStateResult) obj);
            }
            if (!(obj instanceof VoiceRegStateResult)) {
                return null;
            }
            VoiceRegStateResult voiceRegStateResult = (VoiceRegStateResult) obj;
            int regStateFromHalRegState = getRegStateFromHalRegState(voiceRegStateResult.regState);
            int iRilRadioTechnologyToNetworkType = ServiceState.rilRadioTechnologyToNetworkType(voiceRegStateResult.rat);
            int i = voiceRegStateResult.reasonForDenial;
            boolean zIsEmergencyOnly = isEmergencyOnly(voiceRegStateResult.regState);
            boolean z = voiceRegStateResult.cssSupported;
            int i2 = voiceRegStateResult.roamingIndicator;
            int i3 = voiceRegStateResult.systemIsInPrl;
            int i4 = voiceRegStateResult.defaultRoamingIndicator;
            List<Integer> availableServices = getAvailableServices(regStateFromHalRegState, 1, zIsEmergencyOnly);
            CellIdentity cellIdentityConvertHalCellIdentity = RILUtils.convertHalCellIdentity(voiceRegStateResult.cellIdentity);
            return new NetworkRegistrationInfo(1, 1, regStateFromHalRegState, iRilRadioTechnologyToNetworkType, i, zIsEmergencyOnly, availableServices, cellIdentityConvertHalCellIdentity, getPlmnFromCellIdentity(cellIdentityConvertHalCellIdentity), z, i2, i3, i4);
        }

        private NetworkRegistrationInfo createRegistrationStateFromDataRegState(Object obj) {
            LteVopsSupportInfo lteVopsSupportInfo;
            if (obj instanceof RegStateResult) {
                return getNetworkRegistrationInfoAidl(2, 1, (RegStateResult) obj);
            }
            if (obj instanceof android.hardware.radio.V1_6.RegStateResult) {
                return getNetworkRegistrationInfo1_6(2, 1, (android.hardware.radio.V1_6.RegStateResult) obj);
            }
            if (obj instanceof android.hardware.radio.V1_5.RegStateResult) {
                return getNetworkRegistrationInfo(2, 1, (android.hardware.radio.V1_5.RegStateResult) obj);
            }
            if (obj instanceof DataRegStateResult) {
                DataRegStateResult dataRegStateResult = (DataRegStateResult) obj;
                if (dataRegStateResult.vopsInfo.getDiscriminator() == 1 && ServiceState.rilRadioTechnologyToAccessNetworkType(dataRegStateResult.base.rat) == 3) {
                    LteVopsInfo lteVopsInfo = dataRegStateResult.vopsInfo.lteVopsInfo();
                    lteVopsSupportInfo = convertHalLteVopsSupportInfo(lteVopsInfo.isVopsSupported, lteVopsInfo.isEmcBearerSupported);
                } else {
                    lteVopsSupportInfo = new LteVopsSupportInfo(1, 1);
                }
                LteVopsSupportInfo lteVopsSupportInfo2 = lteVopsSupportInfo;
                int regStateFromHalRegState = getRegStateFromHalRegState(dataRegStateResult.base.regState);
                int iRilRadioTechnologyToNetworkType = ServiceState.rilRadioTechnologyToNetworkType(dataRegStateResult.base.rat);
                android.hardware.radio.V1_2.DataRegStateResult dataRegStateResult2 = dataRegStateResult.base;
                int i = dataRegStateResult2.reasonDataDenied;
                boolean zIsEmergencyOnly = isEmergencyOnly(dataRegStateResult2.regState);
                android.hardware.radio.V1_2.DataRegStateResult dataRegStateResult3 = dataRegStateResult.base;
                int i2 = dataRegStateResult3.maxDataCalls;
                CellIdentity cellIdentityConvertHalCellIdentity = RILUtils.convertHalCellIdentity(dataRegStateResult3.cellIdentity);
                NrIndicators nrIndicators = dataRegStateResult.nrIndicators;
                boolean z = nrIndicators.isEndcAvailable;
                boolean z2 = nrIndicators.isNrAvailable;
                return new NetworkRegistrationInfo(2, 1, regStateFromHalRegState, iRilRadioTechnologyToNetworkType, i, zIsEmergencyOnly, getAvailableServices(regStateFromHalRegState, 2, zIsEmergencyOnly), cellIdentityConvertHalCellIdentity, getPlmnFromCellIdentity(cellIdentityConvertHalCellIdentity), i2, nrIndicators.isDcNrRestricted, z2, z, lteVopsSupportInfo2);
            }
            CellularNetworkService.loge("Unknown type of DataRegStateResult " + obj);
            return null;
        }

        private NetworkRegistrationInfo getNetworkRegistrationInfo(int i, int i2, android.hardware.radio.V1_5.RegStateResult regStateResult) {
            int i3;
            int i4;
            int i5;
            boolean z;
            boolean z2;
            VopsSupportInfo vopsSupportInfoConvertHalLteVopsSupportInfo;
            boolean z3;
            boolean z4;
            int regStateFromHalRegState = getRegStateFromHalRegState(regStateResult.regState);
            boolean zIsEmergencyOnly = isEmergencyOnly(regStateResult.regState);
            List<Integer> availableServices = getAvailableServices(regStateFromHalRegState, i, zIsEmergencyOnly);
            CellIdentity cellIdentityConvertHalCellIdentity = RILUtils.convertHalCellIdentity(regStateResult.cellIdentity);
            String str = regStateResult.registeredPlmn;
            int i6 = regStateResult.reasonForDenial;
            int networkTypeForCellIdentity = CellularNetworkService.getNetworkTypeForCellIdentity(ServiceState.rilRadioTechnologyToNetworkType(regStateResult.rat), cellIdentityConvertHalCellIdentity, this.mPhone.getCarrierId());
            VopsSupportInfo lteVopsSupportInfo = new LteVopsSupportInfo(1, 1);
            byte discriminator = regStateResult.accessTechnologySpecificInfo.getDiscriminator();
            if (discriminator == 1) {
                android.hardware.radio.V1_5.RegStateResult.AccessTechnologySpecificInfo.Cdma2000RegistrationInfo cdma2000RegistrationInfoCdmaInfo = regStateResult.accessTechnologySpecificInfo.cdmaInfo();
                boolean z5 = cdma2000RegistrationInfoCdmaInfo.cssSupported;
                i3 = cdma2000RegistrationInfoCdmaInfo.roamingIndicator;
                int i7 = cdma2000RegistrationInfoCdmaInfo.systemIsInPrl;
                i4 = cdma2000RegistrationInfoCdmaInfo.defaultRoamingIndicator;
                i5 = i7;
                z = false;
                z2 = false;
                vopsSupportInfoConvertHalLteVopsSupportInfo = lteVopsSupportInfo;
                z3 = z5;
                z4 = false;
            } else if (discriminator == 2) {
                android.hardware.radio.V1_5.RegStateResult.AccessTechnologySpecificInfo.EutranRegistrationInfo eutranRegistrationInfoEutranInfo = regStateResult.accessTechnologySpecificInfo.eutranInfo();
                NrIndicators nrIndicators = eutranRegistrationInfoEutranInfo.nrIndicators;
                boolean z6 = nrIndicators.isDcNrRestricted;
                z = nrIndicators.isNrAvailable;
                boolean z7 = nrIndicators.isEndcAvailable;
                LteVopsInfo lteVopsInfo = eutranRegistrationInfoEutranInfo.lteVopsInfo;
                vopsSupportInfoConvertHalLteVopsSupportInfo = convertHalLteVopsSupportInfo(lteVopsInfo.isVopsSupported, lteVopsInfo.isEmcBearerSupported);
                z4 = z7;
                z2 = z6;
                i4 = 0;
                z3 = false;
                i3 = 0;
                i5 = 0;
            } else {
                CellularNetworkService.log("No access tech specific info passes for RegStateResult");
                vopsSupportInfoConvertHalLteVopsSupportInfo = lteVopsSupportInfo;
                i4 = 0;
                z4 = false;
                z3 = false;
                i3 = 0;
                z = false;
                i5 = 0;
                z2 = false;
            }
            if (i == 1) {
                return new NetworkRegistrationInfo(i, i2, regStateFromHalRegState, networkTypeForCellIdentity, i6, zIsEmergencyOnly, availableServices, cellIdentityConvertHalCellIdentity, str, z3, i3, i5, i4);
            }
            if (i != 2) {
                CellularNetworkService.loge("Unknown domain passed to CellularNetworkService= " + i);
            }
            return new NetworkRegistrationInfo(i, i2, regStateFromHalRegState, networkTypeForCellIdentity, i6, zIsEmergencyOnly, availableServices, cellIdentityConvertHalCellIdentity, str, 16, z2, z, z4, vopsSupportInfoConvertHalLteVopsSupportInfo);
        }

        private NetworkRegistrationInfo getNetworkRegistrationInfoAidl(int i, int i2, RegStateResult regStateResult) {
            boolean geranDtmSupported;
            int i3;
            int i4;
            int i5;
            VopsSupportInfo vopsSupportInfo;
            int i6;
            boolean z;
            boolean z2;
            boolean z3;
            byte b;
            int i7;
            int regStateFromHalRegState = getRegStateFromHalRegState(regStateResult.regState);
            boolean zIsEmergencyOnly = isEmergencyOnly(regStateResult.regState);
            List<Integer> availableServices = getAvailableServices(regStateFromHalRegState, i, zIsEmergencyOnly);
            CellIdentity cellIdentityConvertHalCellIdentity = RILUtils.convertHalCellIdentity(regStateResult.cellIdentity);
            String str = regStateResult.registeredPlmn;
            int i8 = regStateResult.reasonForDenial;
            if (regStateFromHalRegState == 3 && i8 == 0) {
                AnomalyReporter.reportAnomaly(UUID.fromString("62ed270f-e139-418a-a427-8bcc1bca8f21"), "RIL Missing Reg Fail Reason", this.mPhone.getCarrierId());
            }
            int iRilRadioTechnologyToNetworkType = ServiceState.rilRadioTechnologyToNetworkType(regStateResult.rat);
            if (iRilRadioTechnologyToNetworkType == 19) {
                iRilRadioTechnologyToNetworkType = 13;
            }
            AccessTechnologySpecificInfo accessTechnologySpecificInfo = regStateResult.accessTechnologySpecificInfo;
            int tag = accessTechnologySpecificInfo.getTag();
            int i9 = 1;
            VopsSupportInfo nrVopsSupportInfo = null;
            if (tag == 1) {
                boolean z4 = accessTechnologySpecificInfo.getCdmaInfo().cssSupported;
                int i10 = accessTechnologySpecificInfo.getCdmaInfo().roamingIndicator;
                int i11 = accessTechnologySpecificInfo.getCdmaInfo().systemIsInPrl;
                geranDtmSupported = z4;
                i3 = accessTechnologySpecificInfo.getCdmaInfo().defaultRoamingIndicator;
                i4 = i10;
                i5 = i11;
                vopsSupportInfo = null;
                i6 = 0;
                z = false;
                z2 = false;
                z3 = false;
                i9 = 1;
                b = 0;
                i7 = 0;
            } else if (tag != 2) {
                if (tag == 3) {
                    nrVopsSupportInfo = new NrVopsSupportInfo(accessTechnologySpecificInfo.getNgranNrVopsInfo().vopsSupported, accessTechnologySpecificInfo.getNgranNrVopsInfo().emcSupported, accessTechnologySpecificInfo.getNgranNrVopsInfo().emfSupported);
                } else if (tag == 4) {
                    geranDtmSupported = accessTechnologySpecificInfo.getGeranDtmSupported();
                    vopsSupportInfo = null;
                    i6 = 0;
                    z = false;
                    z2 = false;
                    z3 = false;
                    b = 0;
                    i7 = 0;
                    i4 = 0;
                    i5 = 0;
                    i3 = 0;
                } else if (tag == 5) {
                    nrVopsSupportInfo = new NrVopsSupportInfo(accessTechnologySpecificInfo.getNrInfo().nrVopsInfo.vopsSupported, accessTechnologySpecificInfo.getNrInfo().nrVopsInfo.emcSupported, accessTechnologySpecificInfo.getNrInfo().nrVopsInfo.emfSupported);
                    if (accessTechnologySpecificInfo.getNrInfo().satelliteTechnology == 2) {
                        vopsSupportInfo = nrVopsSupportInfo;
                        geranDtmSupported = false;
                        i6 = 0;
                        z = false;
                        z2 = false;
                        z3 = false;
                        i4 = 0;
                        i5 = 0;
                        i3 = 0;
                        i7 = 6;
                        b = 0;
                    } else {
                        if (accessTechnologySpecificInfo.getNrInfo().satelliteTechnology == 3) {
                            i7 = 2;
                            vopsSupportInfo = nrVopsSupportInfo;
                            geranDtmSupported = false;
                            i6 = 0;
                            z = false;
                            z2 = false;
                            z3 = false;
                            b = 0;
                        } else if (accessTechnologySpecificInfo.getNrInfo().satelliteTechnology != 0) {
                            CellularNetworkService.logw("Not supported satellite technology for NR: 0");
                        }
                        i4 = 0;
                        i5 = 0;
                        i3 = 0;
                    }
                } else {
                    CellularNetworkService.log("No access tech specific info passes for RegStateResult");
                }
                vopsSupportInfo = nrVopsSupportInfo;
                geranDtmSupported = false;
                i6 = 0;
                z = false;
                z2 = false;
                z3 = false;
                b = 0;
                i7 = 0;
                i4 = 0;
                i5 = 0;
                i3 = 0;
            } else {
                z2 = accessTechnologySpecificInfo.getEutranInfo().nrIndicators.isDcNrRestricted;
                z = accessTechnologySpecificInfo.getEutranInfo().nrIndicators.isNrAvailable;
                z3 = accessTechnologySpecificInfo.getEutranInfo().nrIndicators.isEndcAvailable;
                VopsSupportInfo vopsSupportInfoConvertHalLteVopsSupportInfo = convertHalLteVopsSupportInfo(accessTechnologySpecificInfo.getEutranInfo().lteVopsInfo.isVopsSupported, accessTechnologySpecificInfo.getEutranInfo().lteVopsInfo.isEmcBearerSupported);
                byte b2 = accessTechnologySpecificInfo.getEutranInfo().lteAttachResultType;
                i6 = accessTechnologySpecificInfo.getEutranInfo().extraInfo;
                vopsSupportInfo = vopsSupportInfoConvertHalLteVopsSupportInfo;
                if (accessTechnologySpecificInfo.getEutranInfo().satelliteTechnology == 2) {
                    geranDtmSupported = false;
                    i7 = 5;
                } else if (accessTechnologySpecificInfo.getEutranInfo().satelliteTechnology == 1) {
                    geranDtmSupported = false;
                    i7 = 1;
                } else {
                    if (accessTechnologySpecificInfo.getEutranInfo().satelliteTechnology != 0) {
                        CellularNetworkService.logw("Not supported satellite technology for LTE: 0");
                    }
                    geranDtmSupported = false;
                    i7 = 0;
                }
                i4 = 0;
                i5 = 0;
                i3 = 0;
                b = b2;
                i9 = 1;
            }
            if (i == i9) {
                return new NetworkRegistrationInfo(i, i2, regStateFromHalRegState, iRilRadioTechnologyToNetworkType, i8, zIsEmergencyOnly, availableServices, cellIdentityConvertHalCellIdentity, str, geranDtmSupported, i4, i5, i3);
            }
            if (i != 2) {
                CellularNetworkService.loge("Unknown domain passed to CellularNetworkService= " + i);
            }
            return new NetworkRegistrationInfo.Builder().setDomain(i).setTransportType(i2).setRegistrationState(regStateFromHalRegState).setAccessNetworkTechnology(iRilRadioTechnologyToNetworkType).setRejectCause(i8).setEmergencyOnly(zIsEmergencyOnly).setAvailableServices(availableServices).setCellIdentity(cellIdentityConvertHalCellIdentity).setRegisteredPlmn(str).setSatelliteTechnology(i7).setIsNonTerrestrialNetwork(i7 != 0).setDataSpecificInfo(new DataSpecificRegistrationInfo.Builder(16).setDcNrRestricted(z2).setNrAvailable(z).setEnDcAvailable(z3).setVopsSupportInfo(vopsSupportInfo).setLteAttachResultType(b).setLteAttachExtraInfo(i6).build()).build();
        }

        private NetworkRegistrationInfo getNetworkRegistrationInfo1_6(int i, int i2, android.hardware.radio.V1_6.RegStateResult regStateResult) {
            int i3;
            int i4;
            NrVopsSupportInfo nrVopsSupportInfoConvertHalLteVopsSupportInfo;
            boolean z;
            boolean z2;
            int i5;
            boolean zGeranDtmSupported;
            boolean z3;
            int regStateFromHalRegState = getRegStateFromHalRegState(regStateResult.regState);
            boolean zIsEmergencyOnly = isEmergencyOnly(regStateResult.regState);
            List<Integer> availableServices = getAvailableServices(regStateFromHalRegState, i, zIsEmergencyOnly);
            CellIdentity cellIdentityConvertHalCellIdentity = RILUtils.convertHalCellIdentity(regStateResult.cellIdentity);
            String str = regStateResult.registeredPlmn;
            int i6 = regStateResult.reasonForDenial;
            int networkTypeForCellIdentity = CellularNetworkService.getNetworkTypeForCellIdentity(ServiceState.rilRadioTechnologyToNetworkType(regStateResult.rat), cellIdentityConvertHalCellIdentity, this.mPhone.getCarrierId());
            if (regStateFromHalRegState == 3 && i6 == 0) {
                AnomalyReporter.reportAnomaly(UUID.fromString("62ed270f-e139-418a-a427-8bcc1bca8f21"), "RIL Missing Reg Fail Reason", this.mPhone.getCarrierId());
            }
            android.hardware.radio.V1_6.RegStateResult.AccessTechnologySpecificInfo accessTechnologySpecificInfo = regStateResult.accessTechnologySpecificInfo;
            byte discriminator = accessTechnologySpecificInfo.getDiscriminator();
            NrVopsSupportInfo nrVopsSupportInfo = null;
            if (discriminator == 1) {
                boolean z4 = accessTechnologySpecificInfo.cdmaInfo().cssSupported;
                int i7 = accessTechnologySpecificInfo.cdmaInfo().roamingIndicator;
                int i8 = accessTechnologySpecificInfo.cdmaInfo().systemIsInPrl;
                i3 = accessTechnologySpecificInfo.cdmaInfo().defaultRoamingIndicator;
                i4 = i8;
                nrVopsSupportInfoConvertHalLteVopsSupportInfo = null;
                z = false;
                z2 = false;
                i5 = i7;
                zGeranDtmSupported = z4;
                z3 = false;
            } else if (discriminator != 2) {
                if (discriminator != 3) {
                    if (discriminator == 4) {
                        zGeranDtmSupported = accessTechnologySpecificInfo.geranDtmSupported();
                        nrVopsSupportInfoConvertHalLteVopsSupportInfo = null;
                        z3 = false;
                        i3 = 0;
                    } else {
                        CellularNetworkService.log("No access tech specific info passes for RegStateResult");
                    }
                    i5 = 0;
                    i4 = 0;
                    z = false;
                    z2 = false;
                } else {
                    nrVopsSupportInfo = new NrVopsSupportInfo(accessTechnologySpecificInfo.ngranNrVopsInfo().vopsSupported, accessTechnologySpecificInfo.ngranNrVopsInfo().emcSupported, accessTechnologySpecificInfo.ngranNrVopsInfo().emfSupported);
                }
                nrVopsSupportInfoConvertHalLteVopsSupportInfo = nrVopsSupportInfo;
                z3 = false;
                i3 = 0;
                zGeranDtmSupported = false;
                i5 = 0;
                i4 = 0;
                z = false;
                z2 = false;
            } else {
                boolean z5 = accessTechnologySpecificInfo.eutranInfo().nrIndicators.isDcNrRestricted;
                boolean z6 = accessTechnologySpecificInfo.eutranInfo().nrIndicators.isNrAvailable;
                z = accessTechnologySpecificInfo.eutranInfo().nrIndicators.isEndcAvailable;
                nrVopsSupportInfoConvertHalLteVopsSupportInfo = convertHalLteVopsSupportInfo(accessTechnologySpecificInfo.eutranInfo().lteVopsInfo.isVopsSupported, accessTechnologySpecificInfo.eutranInfo().lteVopsInfo.isEmcBearerSupported);
                z2 = z5;
                z3 = z6;
                i3 = 0;
                zGeranDtmSupported = false;
                i5 = 0;
                i4 = 0;
            }
            if (i == 1) {
                return new NetworkRegistrationInfo(i, i2, regStateFromHalRegState, networkTypeForCellIdentity, i6, zIsEmergencyOnly, availableServices, cellIdentityConvertHalCellIdentity, str, zGeranDtmSupported, i5, i4, i3);
            }
            if (i != 2) {
                CellularNetworkService.loge("Unknown domain passed to CellularNetworkService= " + i);
            }
            return new NetworkRegistrationInfo(i, i2, regStateFromHalRegState, networkTypeForCellIdentity, i6, zIsEmergencyOnly, availableServices, cellIdentityConvertHalCellIdentity, str, 16, z2, z3, z, nrVopsSupportInfoConvertHalLteVopsSupportInfo);
        }

        private LteVopsSupportInfo convertHalLteVopsSupportInfo(boolean z, boolean z2) {
            return new LteVopsSupportInfo(z ? 2 : 3, z2 ? 2 : 3);
        }

        public void requestNetworkRegistrationInfo(int i, NetworkServiceCallback networkServiceCallback) {
            if (i == 1) {
                Message messageObtain = Message.obtain(this.mHandler, 1);
                this.mCallbackMap.put(messageObtain, networkServiceCallback);
                this.mPhone.mCi.getVoiceRegistrationState(messageObtain);
            } else if (i == 2) {
                Message messageObtain2 = Message.obtain(this.mHandler, 2);
                this.mCallbackMap.put(messageObtain2, networkServiceCallback);
                this.mPhone.mCi.getDataRegistrationState(messageObtain2);
            } else {
                CellularNetworkService.loge("requestNetworkRegistrationInfo invalid domain " + i);
                networkServiceCallback.onRequestNetworkRegistrationInfoComplete(2, (NetworkRegistrationInfo) null);
            }
        }

        public void close() {
            this.mCallbackMap.clear();
            this.mPhone.mCi.unregisterForNetworkStateChanged(this.mHandler);
        }
    }

    public static int getNetworkTypeForCellIdentity(int i, CellIdentity cellIdentity, int i2) {
        if (cellIdentity == null) {
            if (i != 0) {
                AnomalyReporter.reportAnomaly(UUID.fromString("e67ea4ef-7251-4a69-a063-22c47fc58743"), "RIL Unexpected NetworkType", i2);
                if (Build.isDebuggable()) {
                    logw("Updating incorrect network type from " + TelephonyManager.getNetworkTypeName(i) + " to UNKNOWN");
                    return 0;
                }
                Iterator it = sNetworkTypes.values().iterator();
                while (it.hasNext()) {
                    if (((List) it.next()).contains(Integer.valueOf(i))) {
                        return i;
                    }
                }
            }
            return 0;
        }
        if (i == 18) {
            AnomalyReporter.reportAnomaly(UUID.fromString("07dfa183-b2e7-42b7-98a1-dd5ae2abdd4f"), "RIL Reported IWLAN", i2);
            if (!Build.isDebuggable()) {
                return i;
            }
            Map map = sNetworkTypes;
            if (map.containsKey(cellIdentity.getClass())) {
                int iIntValue = ((Integer) ((List) map.get(cellIdentity.getClass())).get(0)).intValue();
                logw("Updating incorrect network type from IWLAN to " + iIntValue);
                return iIntValue;
            }
            logw("Updating incorrect network type from IWLAN to UNKNOWN");
            return 0;
        }
        Map map2 = sNetworkTypes;
        if (!map2.containsKey(cellIdentity.getClass())) {
            AnomalyReporter.reportAnomaly(UUID.fromString("469858cf-46e5-416e-bc11-5e7970917857"), "RIL Unknown CellIdentity", i2);
            return i;
        }
        List list = (List) map2.get(cellIdentity.getClass());
        if (list.contains(Integer.valueOf(i))) {
            return i;
        }
        AnomalyReporter.reportAnomaly(UUID.fromString("2fb634fa-cab3-44d2-bbe8-c7689b0f3e31"), "RIL Mismatched NetworkType", i2);
        logw("Updating incorrect network type from " + TelephonyManager.getNetworkTypeName(i) + " to " + TelephonyManager.getNetworkTypeName(((Integer) list.get(0)).intValue()));
        return ((Integer) list.get(0)).intValue();
    }

    public NetworkService.NetworkServiceProvider onCreateNetworkServiceProvider(int i) {
        if (!SubscriptionManager.isValidSlotIndex(i)) {
            loge("Tried to Cellular network service with invalid slotId " + i);
            return null;
        }
        return new CellularNetworkServiceProvider(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isMmsEnabled(Phone phone) {
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) phone.getContext().getSystemService(CarrierConfigManager.class);
        if (carrierConfigManager != null) {
            PersistableBundle configForSubId = carrierConfigManager.getConfigForSubId(phone.getSubId(), "enabledMMS");
            if (configForSubId == null || configForSubId.isEmpty()) {
                configForSubId = CarrierConfigManager.getDefaultConfig();
            }
            return configForSubId.getBoolean("enabledMMS");
        }
        loge("isMmsEnabled: CarrierConfigManager is null");
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Rlog.d(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logw(String str) {
        Rlog.w(TAG, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loge(String str) {
        Rlog.e(TAG, str);
    }
}
