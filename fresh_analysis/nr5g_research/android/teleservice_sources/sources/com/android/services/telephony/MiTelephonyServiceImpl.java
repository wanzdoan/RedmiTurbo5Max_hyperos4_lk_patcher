package com.android.services.telephony;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.telecom.Conference;
import android.telecom.Connection;
import android.telecom.ConnectionRequest;
import android.telecom.ConnectionService;
import android.telecom.DisconnectCause;
import android.telecom.StatusHints;
import android.telephony.CarrierConfigManager;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.MiuiCallControllerImpl;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneInternalInterface;
import com.android.internal.telephony.emergency.RadioOnHelper;
import com.android.internal.telephony.emergency.RadioOnStateListener;
import com.android.internal.telephony.gsm.MiuiGsmMmiCodeUtils;
import com.android.internal.telephony.imsphone.ImsPhoneConnection;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.MiuiNotificationMgrUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModuleDelegate;
import com.android.phone.PhoneGlobals;
import com.android.phone.PhoneUtils;
import com.android.phone.R;
import com.android.phone.module.IMiConnectModule;
import com.android.phone.module.ISettingsModule;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.module.IUtilityModule;
import com.android.phone.utils.MiuiJpOperatorCustimizedUtils;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import gov.nist.javax.sip.address.GenericURI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import miui.os.Build;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.CarrierConfigManagerEx;
import miui.telephony.PhoneNumberUtils;
import miui.telephony.TelephonyConstants;
import miui.telephony.TelephonyManager;
import miui.telephony.TelephonyManagerEx;
import miuix.core.util.SystemProperties;

/* JADX INFO: loaded from: classes.dex */
public class MiTelephonyServiceImpl implements MiTelephonyServiceStub.IMiTelephonyService {
    private TelephonyConnectionService mConnectionService;
    private Context mContext;
    private static final String[] CHINA_EMERGENCY_NUMBER = {"110", "119", "120", "122", "999"};
    private static final int[] BIG_SIM_SLOT_ICON = {R.drawable.ic_sim_1_big, R.drawable.ic_sim_2_big};
    private ConcurrentHashMap mMiConnectionExtras = new ConcurrentHashMap();
    private StatusHints mOutgoingWifiCallStatusHints = null;
    private StatusHints mIncomingWifiCallStatusHints = null;
    private StatusHints mStatusHints = null;
    private final String ACTION_MSIM_VOICE_CAPABILITY_CHANGED = "org.codeaurora.intent.action.MSIM_VOICE_CAPABILITY_CHANGED";
    private final String PERMISSION_MSIM_VOICE_CAPABILITY = "com.qti.permission.RECEIVE_MSIM_VOICE_CAPABILITY";
    private final String EXTRAS_MSIM_VOICE_CAPABILITY = "MsimVoiceCapability";
    private BroadcastReceiver mConcurrentCallsReceiver = new BroadcastReceiver() { // from class: com.android.services.telephony.MiTelephonyServiceImpl.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            int intExtra = intent.getIntExtra("MsimVoiceCapability", 0);
            int length = PhoneFactory.getPhones().length;
            boolean zIsDualSimDuringCall = MiuiPhoneUtils.isDualSimDuringCall();
            android.util.Log.i("MiTelephonyServiceImpl", " mConcurrentCallsReceiver: voiceCapability : " + intExtra + " ; phoneCount = " + length + " ; isDualSimDuringCall : " + zIsDualSimDuringCall);
            if (intExtra != 3 && length == 2 && zIsDualSimDuringCall) {
                MiTelephonyServiceImpl miTelephonyServiceImpl = MiTelephonyServiceImpl.this;
                miTelephonyServiceImpl.hangupBgCallForDsdsState(miTelephonyServiceImpl.pickPhoneToDisconnectCalls());
            }
        }
    };

    private boolean isMTForwardedCall(int i) {
        if (i == 0) {
            return true;
        }
        switch (i) {
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                return true;
            default:
                return false;
        }
    }

    class ConnectionExtra {
        boolean mAfterSrvccConnection;
        int mDisconnectCause;
        boolean mEnableDataAlwaysOn;
        boolean mIsSrvccConnectionTimeReset;
        boolean mLocalDisconnecting;

        private ConnectionExtra(MiTelephonyServiceImpl miTelephonyServiceImpl) {
            this.mIsSrvccConnectionTimeReset = false;
            this.mAfterSrvccConnection = false;
            this.mLocalDisconnecting = false;
            this.mDisconnectCause = -1;
            this.mEnableDataAlwaysOn = false;
        }
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void setSrvccConnectionTimeReset(TelephonyConnection telephonyConnection, boolean z) {
        ConnectionExtra connectionExtra = (ConnectionExtra) this.mMiConnectionExtras.get(telephonyConnection);
        if (connectionExtra != null) {
            connectionExtra.mIsSrvccConnectionTimeReset = z;
        }
    }

    public boolean isSrvccConnectionTimeReset(TelephonyConnection telephonyConnection) {
        ConnectionExtra connectionExtra = (ConnectionExtra) this.mMiConnectionExtras.get(telephonyConnection);
        if (connectionExtra != null) {
            return connectionExtra.mIsSrvccConnectionTimeReset;
        }
        return false;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void setAfterSrvccConnection(TelephonyConnection telephonyConnection, boolean z) {
        ConnectionExtra connectionExtra = (ConnectionExtra) this.mMiConnectionExtras.get(telephonyConnection);
        if (connectionExtra != null) {
            connectionExtra.mAfterSrvccConnection = z;
        }
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean isAfterSrvccConnection(TelephonyConnection telephonyConnection) {
        ConnectionExtra connectionExtra = (ConnectionExtra) this.mMiConnectionExtras.get(telephonyConnection);
        if (connectionExtra != null) {
            return connectionExtra.mAfterSrvccConnection;
        }
        return false;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void setLocalDisconnecting(TelephonyConnection telephonyConnection, boolean z) {
        int i;
        if (!z || isLocalDisconnecting(telephonyConnection)) {
            i = (z || !isLocalDisconnecting(telephonyConnection)) ? 0 : -1;
        } else {
            i = 2;
        }
        if (i != 0) {
            Phone phone = telephonyConnection.getPhone();
            int i2 = telephonyConnection.isImsConnection() ? 1 : 2;
            android.util.Log.i("MiTelephonyServiceImpl", "setLocalDisconnecting callType: " + i2);
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).setCallCommandMessage(telephonyConnection.getTelecomCallId(), i, phone != null ? phone.getPhoneId() : -1, i2);
        }
        ConnectionExtra connectionExtra = (ConnectionExtra) this.mMiConnectionExtras.get(telephonyConnection);
        if (connectionExtra != null) {
            connectionExtra.mLocalDisconnecting = z;
        }
    }

    public boolean isLocalDisconnecting(TelephonyConnection telephonyConnection) {
        ConnectionExtra connectionExtra = (ConnectionExtra) this.mMiConnectionExtras.get(telephonyConnection);
        if (connectionExtra != null) {
            return connectionExtra.mLocalDisconnecting;
        }
        return false;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void init(Context context) {
        this.mContext = context;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void newTelephonyConnection(TelephonyConnection telephonyConnection) {
        this.mMiConnectionExtras.put(telephonyConnection, new ConnectionExtra());
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void handleMessage(TelephonyConnection telephonyConnection, Handler handler, Message message) {
        int i = message.what;
        if (i == 3) {
            if (telephonyConnection.getPhone() == null || telephonyConnection.getPhone().getPhoneType() != 2) {
                return;
            }
            telephonyConnection.getPhone().registerForLineControlInfo(handler, 50, (Object) null);
            handleSrvccCdmaConnectionTimeReset(telephonyConnection);
            return;
        }
        if (i == 6) {
            ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).clearLocalCache();
            return;
        }
        switch (i) {
            case 50:
                android.util.Log.d("MiTelephonyServiceImpl", "MSG_GSM_CDMA_LINE_CONTROL_INFO");
                handleSrvccCdmaConnectionTimeReset(telephonyConnection);
                break;
            case 51:
                android.util.Log.d("MiTelephonyServiceImpl", "MSG_SUPP_SERVICE_FAILED");
                AsyncResult asyncResult = (AsyncResult) message.obj;
                if (asyncResult == null) {
                    android.util.Log.e("MiTelephonyServiceImpl", "MSG_SUPP_SERVICE_FAILED is null");
                } else {
                    int iOrdinal = ((PhoneInternalInterface.SuppService) asyncResult.result).ordinal();
                    Intent intent = new Intent();
                    intent.setAction("org.codeaurora.ACTION_SUPP_SERVICE_FAILURE");
                    intent.putExtra("supp_serv_failure", iOrdinal);
                    PhoneGlobals.getInstance().sendBroadcastAsUser(intent, UserHandle.CURRENT);
                }
                break;
            case 52:
                android.util.Log.d("MiTelephonyServiceImpl", "MSG_SERVICE_STATE_CHANGED");
                AsyncResult asyncResult2 = (AsyncResult) message.obj;
                if (asyncResult2 == null) {
                    android.util.Log.e("MiTelephonyServiceImpl", "MSG_SERVICE_STATE_CHANGED is null");
                    break;
                } else {
                    ServiceState serviceState = (ServiceState) asyncResult2.result;
                    if (TelephonyManagerEx.getDefault().isConcurrentCallsPossible() && MiuiPhoneUtils.isDualSimDuringCall() && serviceState.getState() == 1) {
                        android.util.Log.d("MiTelephonyServiceImpl", "hangup oos call in DSDA");
                        telephonyConnection.hangup(3);
                        break;
                    }
                }
                break;
        }
    }

    private void handleSrvccCdmaConnectionTimeReset(TelephonyConnection telephonyConnection) {
        boolean z = telephonyConnection.getOriginalConnection() instanceof ImsPhoneConnection;
        if (telephonyConnection.getOriginalConnection() != null) {
            android.util.Log.i("MiTelephonyServiceImpl", "handleSrvccCdmaConnectionTimeReset imsCall: " + z + " duration: " + telephonyConnection.getOriginalConnection().getDurationMillis() + "originalConnection: " + telephonyConnection.getOriginalConnection());
        }
        if (z || isSrvccConnectionTimeReset(telephonyConnection) || !telephonyConnection.isOutgoingCall() || telephonyConnection.getOriginalConnection() == null || telephonyConnection.getOriginalConnection().getState() != Call.State.ACTIVE || telephonyConnection.getOriginalConnection().getDurationMillis() <= 0) {
            return;
        }
        android.util.Log.i("MiTelephonyServiceImpl", "handleSrvccCdmaConnectionTimeReset");
        setSrvccConnectionTimeReset(telephonyConnection, true);
        telephonyConnection.getOriginalConnection().resetConnectionTime();
        telephonyConnection.resetConnectionTime();
        telephonyConnection.updateConnectionCapabilities();
        MiuiPhoneUtils.sendCdmaCallRealConnectedBroadcast(telephonyConnection.getPhone().getPhoneId());
    }

    private boolean shouldShowSuppToast() {
        if (!HyperOSCustFeatureResolve.getBoolean("show_supp_toast", true)) {
            android.util.Log.i("MiTelephonyServiceImpl", "show_supp_toast is false");
            return false;
        }
        return TelephonyManager.isGoogleCsp();
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void ShowSuppToastIfNeed(TelephonyConnection telephonyConnection, int i, int i2) {
        if (shouldShowSuppToast()) {
            CharSequence suppServiceMessage = getSuppServiceMessage(telephonyConnection, i, i2);
            if (suppServiceMessage == null || suppServiceMessage.length() == 0) {
                android.util.Log.i("MiTelephonyServiceImpl", "ShowSuppToastIfNeed unknown supp service message");
            } else {
                Toast.makeText(this.mContext, suppServiceMessage, 1).show();
            }
        }
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void onAnswer(TelephonyConnection telephonyConnection, int i) {
        ((IMiConnectModule) ModuleDelegate.get(IMiConnectModule.class)).onAnswer(telephonyConnection, i);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void performConference(TelephonyConnection telephonyConnection, Connection connection) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).clearLocalCache();
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).addParticipantsLocalCache(telephonyConnection, connection, telephonyConnection.getPhone());
    }

    /* JADX WARN: Code duplicated, block: B:45:0x0150  */
    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void updateAddress(TelephonyConnection telephonyConnection, Uri uri) {
        MiTelephonyServiceImpl miTelephonyServiceImpl;
        TelephonyConnection telephonyConnection2;
        com.android.internal.telephony.Connection connection = telephonyConnection.mOriginalConnection;
        if (connection != null) {
            int numberPresentation = connection.getNumberPresentation();
            android.util.Log.i("MiTelephonyServiceImpl", "updateAddress, number presentation=" + numberPresentation);
            if (MiuiPhoneUtils.shouldUseCnapPresentation(telephonyConnection.getPhone()) && !TextUtils.isEmpty(telephonyConnection.mOriginalConnection.getCnapName())) {
                numberPresentation = telephonyConnection.mOriginalConnection.getCnapNamePresentation();
                android.util.Log.i("MiTelephonyServiceImpl", "updateAddress, cnap exist, presentation=" + numberPresentation);
            }
            int i = numberPresentation;
            Phone phone = telephonyConnection.getPhone();
            boolean booleanConfigForSubId = false;
            if (phone != null) {
                booleanConfigForSubId = CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "update_kddi_call_address", false);
                miTelephonyServiceImpl = this;
                telephonyConnection2 = telephonyConnection;
                uri = miTelephonyServiceImpl.updateCountryCode(telephonyConnection2, uri, null, telephonyConnection.mOriginalConnection.isIncoming(), telephonyConnection.mOriginalConnection.isEmergencyCall(), phone, i);
            } else {
                miTelephonyServiceImpl = this;
                telephonyConnection2 = telephonyConnection;
            }
            int i2 = 1;
            if (!Objects.equals(uri, telephonyConnection2.getAddress()) || i != telephonyConnection2.getAddressPresentation()) {
                android.util.Log.v("MiTelephonyServiceImpl", "updateAddress, address changed");
                if ((telephonyConnection2.getConnectionProperties() & 64) != 0) {
                    uri = null;
                }
                Uri uriUpdateAddress = SimpleFeatures.updateAddress(telephonyConnection2, uri);
                if (TelephonyManager.isCustForKrSkt() && i == 2) {
                    uriUpdateAddress = miTelephonyServiceImpl.getAddressFromNumber(miTelephonyServiceImpl.mContext.getString(R.string.private_num_kr));
                    i = 1;
                }
                uri = uriUpdateAddress;
                if (TelephonyManager.isCustForJpKd() || booleanConfigForSubId) {
                    android.util.Log.i("MiTelephonyServiceImpl", "updateAddress--for kddi, updateAddress for KDDI");
                    String strUpdateDisplayNameForJPKD = SimpleFeatures.updateDisplayNameForJPKD(telephonyConnection2, uri);
                    if ((i == 2 || i == 3) && !TextUtils.isEmpty(telephonyConnection2.mOriginalConnection.getCnapName())) {
                        android.util.Log.i("MiTelephonyServiceImpl", "updateAddress--for kddi, number is null and CNAP is exist, presentation=" + i);
                    }
                    int cnapNamePresentation = telephonyConnection2.mOriginalConnection.getCnapNamePresentation();
                    if (!TextUtils.isEmpty(strUpdateDisplayNameForJPKD)) {
                        android.util.Log.i("MiTelephonyServiceImpl", "updateDisplayName--for kddi, newName = " + strUpdateDisplayNameForJPKD);
                        telephonyConnection2.setCallerDisplayName(strUpdateDisplayNameForJPKD, cnapNamePresentation);
                    }
                }
                android.util.Log.i("MiTelephonyServiceImpl", "updateAddress, telephonyconnection setAddress, presentation: " + i);
                telephonyConnection2.setAddress(uri, i);
            }
            String strFilterCnapName = telephonyConnection2.filterCnapName(telephonyConnection2.mOriginalConnection.getCnapName());
            int cnapNamePresentation2 = telephonyConnection2.mOriginalConnection.getCnapNamePresentation();
            if (TextUtils.isEmpty(strFilterCnapName)) {
                String strQueryFdn = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).queryFdn(uri != null ? uri.getSchemeSpecificPart() : null);
                if (TextUtils.isEmpty(strQueryFdn)) {
                    i2 = cnapNamePresentation2;
                } else {
                    strFilterCnapName = strQueryFdn;
                }
            } else {
                i2 = cnapNamePresentation2;
            }
            if ((!TelephonyManager.isCustForJpKd() && !booleanConfigForSubId) || TextUtils.isEmpty(strFilterCnapName) || strFilterCnapName.startsWith("+81")) {
                if (Objects.equals(strFilterCnapName, telephonyConnection2.getCallerDisplayName()) && i2 == telephonyConnection2.getCallerDisplayNamePresentation()) {
                    return;
                }
                android.util.Log.v("MiTelephonyServiceImpl", "updateAddress, caller display name changed");
                telephonyConnection2.setCallerDisplayName(strFilterCnapName, i2);
            }
        }
    }

    public Uri updateCountryCode(TelephonyConnection telephonyConnection, Uri uri, Uri uri2, boolean z, boolean z2, Phone phone, int i) {
        if (z2 || telephonyConnection == null || uri == null || phone == null) {
            return uri;
        }
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        boolean z3 = ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).getRoamingModeForPhoneId(phone.getPhoneId()) == 1;
        if (z) {
            boolean zIsSimilarChinaPhoneNumber = PhoneNumberUtils.isSimilarChinaPhoneNumber(schemeSpecificPart);
            android.util.Log.d("MiTelephonyServiceImpl", "UpdateCountryCode for incoming call, isSimilarCNNumber - " + zIsSimilarChinaPhoneNumber + " ,isRoamingCN2GL - " + z3);
            if (!z3 || !zIsSimilarChinaPhoneNumber) {
                return uri;
            }
            String strRemoveExtraPrefix = PhoneNumberUtils.removeExtraPrefix(schemeSpecificPart);
            if (TextUtils.equals(strRemoveExtraPrefix, schemeSpecificPart)) {
                return uri;
            }
            Uri uriFromParts = Uri.fromParts(GenericURI.TEL, strRemoveExtraPrefix, null);
            telephonyConnection.setAddress(uriFromParts, i);
            trackEvent(phone, "auto_correct_country_code");
            android.util.Log.i("MiTelephonyServiceImpl", "UpdateCountryCode, remove extra prefix in roaming mode.");
            return uriFromParts;
        }
        if (!uri.equals(uri2)) {
            return uri;
        }
        boolean zIsPreciseChinaPhoneNumber = PhoneNumberUtils.isPreciseChinaPhoneNumber(schemeSpecificPart);
        android.util.Log.d("MiTelephonyServiceImpl", "UpdateCountryCode for outgoing call , isCNNumber - " + zIsPreciseChinaPhoneNumber + " ,isRoamingCN2GL - " + z3);
        if (!z3 || !zIsPreciseChinaPhoneNumber) {
            return uri;
        }
        Uri uriFromParts2 = Uri.fromParts(GenericURI.TEL, PhoneNumberUtils.addChinaCountryCode(schemeSpecificPart), null);
        telephonyConnection.setAddress(uriFromParts2, telephonyConnection.getAddressPresentation());
        trackEvent(phone, "auto_add_country_code");
        android.util.Log.i("MiTelephonyServiceImpl", "UpdateCountryCode, add country code in roaming mode.");
        return uriFromParts2;
    }

    private void trackEvent(Phone phone, String str) {
        String networkNumeric = MiuiPhoneUtils.getNetworkNumeric(phone.getPhoneId());
        String simOperatorNumeric = MiuiPhoneUtils.getSimOperatorNumeric(phone.getPhoneId());
        HashMap map = new HashMap();
        map.put("NetworkPlmn", networkNumeric);
        map.put("SimOperator", simOperatorNumeric);
        ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, str, map);
    }

    private Uri getAddressFromNumber(String str) {
        if (str == null) {
            str = "";
        }
        return Uri.fromParts(GenericURI.TEL, str, null);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void registerForCallEvents(TelephonyConnection telephonyConnection, Phone phone, Handler handler) {
        phone.registerForSuppServiceFailed(handler, 51, (Object) null);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void unregisterForCallEvents(TelephonyConnection telephonyConnection, Phone phone, Handler handler) {
        phone.unregisterForSuppServiceFailed(handler);
        phone.unregisterForLineControlInfo(handler);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void updateStateForRelayCall(TelephonyConnection telephonyConnection) {
        ((IMiConnectModule) ModuleDelegate.get(IMiConnectModule.class)).onRelayCallStateChanged(telephonyConnection);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void updateMultiparty(TelephonyConnection telephonyConnection) {
        com.android.internal.telephony.Connection connection = telephonyConnection.mOriginalConnection;
        if (connection != null) {
            Bundle connectionExtras = connection.getConnectionExtras();
            if (telephonyConnection.mIsMultiParty || connectionExtras == null || !connectionExtras.containsKey("incomingConference")) {
                return;
            }
            telephonyConnection.mIsMultiParty = connectionExtras.getBoolean("incomingConference", false) | telephonyConnection.mIsMultiParty;
            android.util.Log.d("MiTelephonyServiceImpl", "current: isMultiparty:" + telephonyConnection.mIsMultiParty);
        }
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0064  */
    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean updateStatusHints(TelephonyConnection telephonyConnection) {
        int i;
        String str;
        String str2;
        if (telephonyConnection.isWifi() && !isCrossSimCall(telephonyConnection) && telephonyConnection.getPhone() != null) {
            if (isValidRingingCall(telephonyConnection)) {
                i = R.string.status_hint_label_incoming_wifi_call;
            } else {
                i = R.string.status_hint_label_wifi_call;
            }
            if (!MiuiImsPhoneUtils.isCustForEsVodafone() && MiuiImsPhoneUtils.shouldShowSubDisplaynameWhenWifiCall(telephonyConnection.getPhone()) && android.telephony.TelephonyManager.getDefault().getPhoneCount() > 1) {
                SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(telephonyConnection.getPhone().getPhoneId());
                if (activeSubscriptionInfoForSimSlotIndex != null) {
                    str = " " + activeSubscriptionInfoForSimSlotIndex.getDisplayName().toString();
                } else {
                    str = "";
                }
            } else {
                str = "";
            }
            if (TelephonyManager.isCustForMxTelcel()) {
                str2 = null;
            } else {
                str2 = telephonyConnection.getResourceString(i) + str;
            }
            telephonyConnection.setTelephonyStatusHints(new StatusHints(str2, Icon.createWithResource(this.mContext, R.drawable.ic_signal_wifi_4_bar_24dp), null));
        } else {
            telephonyConnection.setTelephonyStatusHints(null);
        }
        return true;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean updateHighDefAudioValue(TelephonyConnection telephonyConnection, boolean z) {
        if (telephonyConnection.getPhone() == null || MiuiPhoneUtils.hideVolteUiForH3GCustimized(telephonyConnection.getPhone().getSubId())) {
            return false;
        }
        return MiuiCallControllerImpl.Processor.sendSpeechCodecIsHDBroadcast(telephonyConnection.getPhone(), z);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean retryOutgoingOriginalConnection(final TelephonyConnection telephonyConnection, final TelephonyConnectionService telephonyConnectionService, RadioOnHelper radioOnHelper) {
        int disconnectCause;
        if (telephonyConnection.getOriginalConnection() != null) {
            disconnectCause = telephonyConnection.getOriginalConnection().getDisconnectCause();
            android.util.Log.i("MiTelephonyServiceImpl", "retryOutgoingOriginalConnection cause: " + disconnectCause);
        } else {
            disconnectCause = -1;
        }
        if (disconnectCause != 71) {
            return false;
        }
        if (Settings.Global.getInt(telephonyConnectionService.getContentResolver(), "airplane_mode_on", 0) > 0) {
            if (radioOnHelper == null) {
                radioOnHelper = new RadioOnHelper(telephonyConnectionService);
            }
            radioOnHelper.triggerRadioOnAndListen(new RadioOnStateListener.Callback(this) { // from class: com.android.services.telephony.MiTelephonyServiceImpl.1
                public void onComplete(RadioOnStateListener radioOnStateListener, boolean z) {
                    android.util.Log.i("MiTelephonyServiceImpl", "Redialing the call after exiting airplane mode");
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.android.services.telephony.MiTelephonyServiceImpl.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            TelephonyConnection telephonyConnection2 = telephonyConnection;
                            if (telephonyConnection2 != null && telephonyConnection2.getPhone() != null) {
                                AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                                TelephonyConnectionService telephonyConnectionService2 = telephonyConnectionService;
                                TelephonyConnection telephonyConnection3 = telephonyConnection;
                                telephonyConnectionService2.placeOutgoingConnection(telephonyConnection3, telephonyConnection3.getPhone(), telephonyConnection.getVideoState(), telephonyConnection.getExtras());
                                return;
                            }
                            android.util.Log.i("MiTelephonyServiceImpl", "Redialing the call after exiting airplane mode error");
                        }
                    }, 2000L);
                }

                public boolean isOkToCall(Phone phone, int i, boolean z) {
                    return phone.getState() == PhoneConstants.State.OFFHOOK || phone.getServiceStateTracker().isRadioOn();
                }

                public boolean onTimeout(Phone phone, int i, boolean z) {
                    android.util.Log.i("MiTelephonyServiceImpl", "onTimeout, serviceState = " + i + " imsVoiceCapable = " + z);
                    return true;
                }
            }, telephonyConnection.shouldTreatAsEmergencyCall(), telephonyConnection.getPhone(), false, 0);
            return true;
        }
        if (telephonyConnection.getPhone() == null) {
            return true;
        }
        telephonyConnectionService.placeOutgoingConnection(telephonyConnection, telephonyConnection.getPhone(), telephonyConnection.getVideoState(), telephonyConnection.getExtras());
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hangupBgCallForDsdsState(final Phone phone) {
        if (this.mConnectionService == null || phone == null) {
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.android.services.telephony.MiTelephonyServiceImpl.3
            @Override // java.lang.Runnable
            public void run() {
                if (MiuiPhoneUtils.isDualSimDuringCall()) {
                    for (Connection connection : MiTelephonyServiceImpl.this.mConnectionService.getAllConnections()) {
                        if ((connection instanceof TelephonyConnection) && (connection.getConnectionProperties() & 16) != 16 && Integer.valueOf(phone.getSubId()).equals(Integer.valueOf(PhoneUtils.getSubIdForPhoneAccountHandle(connection.getPhoneAccountHandle())))) {
                            TelephonyConnection telephonyConnection = (TelephonyConnection) connection;
                            android.util.Log.i("MiTelephonyServiceImpl", "hangupBgCallForDsdsState: disconnect phoneId:" + phone.getPhoneId() + " callId:" + telephonyConnection.getTelecomCallId() + " due to DSDA switched to DSDS");
                            telephonyConnection.hangup(3);
                            HashMap map = new HashMap();
                            map.put("UpdateDsdaDisconnectScene", 3);
                            ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "telephony_call_dsda_optimization_disconnect_scene", map);
                            MiTelephonyServiceImpl.this.trackerDSDACallExceptionMiEvent(3);
                        }
                    }
                }
            }
        }, 200L);
    }

    public Phone pickPhoneToDisconnectCalls() {
        Phone phone;
        Phone[] phones = PhoneFactory.getPhones();
        int length = phones.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                phone = null;
                break;
            }
            phone = phones[i];
            if ((phone.getImsPhone() != null ? phone.getImsPhone() : phone).getForegroundCall().getState() == Call.State.ACTIVE) {
                break;
            }
            i++;
        }
        for (Phone phone2 : phones) {
            if (phone2 != phone) {
                return phone2;
            }
        }
        return null;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public Connection getTelephonyConnection(ConnectionRequest connectionRequest, Context context, String str, boolean z, Uri uri, Phone phone, TelephonyConnectionService.DisconnectCauseFactory disconnectCauseFactory, DeviceState deviceState, TelephonyConnectionService.PhoneFactoryProxy phoneFactoryProxy) {
        if (phone == null) {
            if (Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) == 1) {
                return Connection.createFailedConnection(disconnectCauseFactory.toTelecomDisconnectCause(17, "Phone is null"));
            }
            IccCard iccCard = phoneFactoryProxy.getDefaultPhone().getIccCard();
            IccCardConstants.State state = IccCardConstants.State.UNKNOWN;
            if (iccCard != null) {
                state = iccCard.getState();
            }
            if (!deviceState.shouldCheckSimStateBeforeOutgoingCall(context) || state != IccCardConstants.State.PIN_REQUIRED) {
                return Connection.createFailedConnection(disconnectCauseFactory.toTelecomDisconnectCause(18, shouldReplaceErrorMessage(z) ? "Not Emergency" : "Phone is null"));
            }
        }
        boolean z2 = Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) > 0;
        if (Build.IS_CM_CUSTOMIZATION_TEST && z && !z2) {
            int i = 0;
            for (Phone phone2 : phoneFactoryProxy.getPhones()) {
                if (isAvailableForEmergencyCalls(phone2)) {
                    i++;
                }
            }
            if (i == 0) {
                return Connection.createFailedConnection(DisconnectCauseUtil.toTelecomDisconnectCause(18, "ServiceState.STATE_OUT_OF_SERVICE", phone.getPhoneId()));
            }
        }
        CarrierConfigManager carrierConfigManager = (CarrierConfigManager) this.mContext.getSystemService("carrier_config");
        boolean z3 = carrierConfigManager != null ? carrierConfigManager.getConfigForSubId(phone.getSubId()).getBoolean("allow_non_emergency_calls_in_ecm_bool") : true;
        if (z) {
            return null;
        }
        if (phone.isInEcm() && !z3) {
            return null;
        }
        if (MiuiGsmMmiCodeUtils.isBlockedMmi(phone, str) && MiuiPhoneUtils.maybeShowDialogForSuppService(phone, 3)) {
            return Connection.createFailedConnection(new DisconnectCause(1));
        }
        if (MiuiPhoneUtils.maybeShowNoCallServiceToast(context, phone)) {
            return Connection.createFailedConnection(new DisconnectCause(4));
        }
        return null;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void updateConnectionAddress(ConnectionRequest connectionRequest, Connection connection, Uri uri, Phone phone, String str, boolean z) {
        Uri uriHandleIpCall = !z ? ((ISettingsModule) ModuleDelegate.get(ISettingsModule.class)).handleIpCall(uri, phone, str, connectionRequest.getExtras()) : uri;
        connection.setAddress(uriHandleIpCall, 1);
        updateCountryCode((TelephonyConnection) connection, uri, uriHandleIpCall, connection.getCallDirection() == 0, z, phone, connection.getAddressPresentation());
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void onCreateConnectionComplete(ConnectionService connectionService, Connection connection) {
        ((IMiConnectModule) ModuleDelegate.get(IMiConnectModule.class)).onCreateConnectionComplete(this.mContext, connectionService, connection);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void onConnectionRemoved(Connection connection) {
        ((IMiConnectModule) ModuleDelegate.get(IMiConnectModule.class)).removeLocalConnection(connection);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void onParticipantConnectionDisconnect(Connection connection, Uri uri) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).removeParticipantsNum(uri);
    }

    private boolean isAvailableForEmergencyCalls(Phone phone) {
        return phone.getServiceState().getState() == 0 || phone.getServiceState().isEmergencyOnly();
    }

    private boolean shouldReplaceErrorMessage(boolean z) {
        return TelephonyManager.isCustForKrOps() && MiuiPhoneUtils.getInsertedSimCount() == 0 && !z;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean onExtrasChanged(Connection connection, Bundle bundle) {
        Bundle bundle2 = new Bundle();
        TelephonyConstants.copyMiuiCallExtras(bundle, connection.getExtras(), "telephony.");
        bundle2.putAll(bundle);
        connection.putExtras(bundle2);
        return true;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean isHoldAllowed(Conference conference, boolean z) {
        return (!z || MiuiJpOperatorCustimizedUtils.isCustForJpKd() || MiuiJpOperatorCustimizedUtils.isForJp() || MiuiJpOperatorCustimizedUtils.isCustForJpSb()) ? false : true;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void onMerge(Connection connection, Phone phone) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).addParticipantsLocalCache(connection, phone);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean setHoldable(Conference conference, boolean z) {
        return z && HyperOSCustFeatureResolve.getBoolean("is_hold_capability", true) && !MiuiJpOperatorCustimizedUtils.isDuringCallForJpOperator();
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean isAdhocConferenceCall(Conference conference, Phone phone, boolean z) {
        return z || !MiuiPhoneUtils.isEmulatingSinglePartyCallEnabled(phone);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void disconnectConferenceParticipants(Conference conference) {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).clearLocalCache();
    }

    /* JADX WARN: Code duplicated, block: B:21:0x0062  */
    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean updateStatusHints(Conference conference) {
        Phone phone;
        String str;
        String str2;
        if (!(conference instanceof ImsConference)) {
            return false;
        }
        ImsConference imsConference = (ImsConference) conference;
        TelephonyConnection telephonyConnection = (TelephonyConnection) imsConference.getConferenceHost();
        if (telephonyConnection == null) {
            conference.setStatusHints(null);
            return true;
        }
        if (!telephonyConnection.isWifi() || (phone = telephonyConnection.getPhone()) == null) {
            return false;
        }
        if (!MiuiImsPhoneUtils.isCustForEsVodafone() && MiuiImsPhoneUtils.shouldShowSubDisplaynameWhenWifiCall(phone) && android.telephony.TelephonyManager.getDefault().getPhoneCount() > 1) {
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(telephonyConnection.getPhone().getPhoneId());
            if (activeSubscriptionInfoForSimSlotIndex != null) {
                str = " " + activeSubscriptionInfoForSimSlotIndex.getDisplayName().toString();
            } else {
                str = "";
            }
        } else {
            str = "";
        }
        if (TelephonyManager.isCustForMxTelcel()) {
            str2 = null;
        } else {
            str2 = this.mContext.getString(R.string.status_hint_label_wifi_call) + str;
        }
        StatusHints statusHints = new StatusHints(str2, Icon.createWithResource(this.mContext, R.drawable.ic_signal_wifi_4_bar_24dp), null);
        conference.setStatusHints(statusHints);
        Iterator<Connection> it = imsConference.getConnections().iterator();
        while (it.hasNext()) {
            it.next().setStatusHints(statusHints);
        }
        return true;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean shouldPlayDisconnectedToneForRingCall(int i) {
        if (TelephonyManager.isCustForKrOps()) {
            return i == 1 || i == 16;
        }
        return false;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public Integer toTelecomDisconnectCauseDescription(Context context, int i, int i2, Integer num) {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).toTelecomDisconnectCauseDescription(context, i, i2, num);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean isCallTransferSupported(TelephonyConnection telephonyConnection, boolean z, boolean z2) {
        PersistableBundle carrierConfig = telephonyConnection.getCarrierConfig();
        if (telephonyConnection.isImsConnection()) {
            if (carrierConfig != null) {
                return carrierConfig.getBoolean("carrier_allow_transfer_ims_call_bool");
            }
            return false;
        }
        if (!telephonyConnection.isGsmCdmaConnection()) {
            return false;
        }
        Phone phone = telephonyConnection.getPhone();
        return carrierConfig != null && carrierConfig.getBoolean("carrier_allow_transfer_gsm_call_bool") && phone != null && phone.getPhoneType() == 1;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void notifySSNotificationToast(TelephonyConnection telephonyConnection, int i, int i2, int i3) {
        if (telephonyConnection.getPhone() != null && i == 1 && isMTForwardedCall(i3)) {
            if (TelephonyManager.isGoogleCsp()) {
                CharSequence suppServiceMessage = getSuppServiceMessage(telephonyConnection, i, i2);
                if (suppServiceMessage == null || suppServiceMessage.length() == 0) {
                    android.util.Log.i("MiTelephonyServiceImpl", "notifySSNotificationToast unknown supp service message");
                } else {
                    Toast.makeText(telephonyConnection.getPhone().getContext(), suppServiceMessage, 1).show();
                }
            }
            SimpleFeatures.accessMiuiConnectionExtra(telephonyConnection).putBoolean("telephony.IS_FORWARDED_CALL", true).commit();
        }
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public int getMiuiResId(int i) {
        if (i == R.string.labelCF) {
            return R.string.title_cfu_enabled;
        }
        if (i == R.string.notification_network_selection_title) {
            return R.string.notification_switch_auto_title;
        }
        if (i == R.string.notification_network_selection_text) {
            return R.string.notification_switch_auto_text;
        }
        if (i == R.string.sim_ndp_unlock_text) {
            return R.string.sim_me_lock_disabled;
        }
        if (i == R.string.requesting_unlock) {
            return R.string.no_unlock_chance_hint;
        }
        if (i == R.string.unlock_success) {
            return R.string.unlock_chances_left;
        }
        if (i == R.string.call_barring_change_pwd) {
            return R.string.strPasswordLengthIncorrect;
        }
        return i == R.id.status_text ? R.id.unlock_chances_left : i;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean putSubscriptionIdExtra(Intent intent, int i) {
        miui.telephony.SubscriptionManager.putSubscriptionIdExtra(intent, i);
        return true;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public String getSimCardDesp(Context context, int i) {
        return MiuiNotificationMgrUtils.getSimCardDesp(context, i);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public Bitmap getVoiceMailStatusIcon(Context context) {
        return MiuiNotificationMgrUtils.getVoiceMailStatusIcon(context);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public String getCallForwardNotiContentText(Context context, int i) {
        return MiuiNotificationMgrUtils.getCallForwardNotiContentText(context, i);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean isNoNotificationOnDomesticRoaming() {
        return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isNoNotificationOnDomesticRoaming();
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void configNetworkSelectionNotification(Context context, Notification.Builder builder, int i) {
        MiuiNotificationMgrUtils.configNetworkSelectionNotification(context, builder, i);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean isSupportDomesticRoaming() {
        return TelephonyManager.isSupportDomesticRoaming();
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean isDomesticRoamingEnable(Context context) {
        return TelephonyManager.isDomesticRoamingEnable(context);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean disableManageConference(Phone phone) {
        return CarrierConfigManagerEx.getBooleanConfigForSubId(phone.getContext(), phone.getSubId(), "mtk_disable_conference_management_after_srvcc", true);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean dismissCountryISOAfterConference(Phone phone) {
        return MiuiImsPhoneUtils.dismissCountryISOAfterConference(phone);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void trackerDSDACallExceptionMiEvent(int i) {
        ((IUtilityModule) ModuleDelegate.get(IUtilityModule.class)).trackerDSDACallExceptionMiEvent(i);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public CharSequence toTelecomDisconnectCauseLabelOrDescription(CharSequence charSequence, CharSequence charSequence2) {
        return ((TelephonyManager.isCustForMxTelcel() || TelephonyManager.isCustForLmClaro()) && !TextUtils.isEmpty(charSequence2)) ? charSequence2 : charSequence;
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public int toTelecomDisconnectCauseCode(int i) {
        switch (i) {
            case 83:
            case SmartNetworkRelayScore.SCORE_PASS /* 84 */:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case SystemProperties.PROP_VALUE_MAX /* 91 */:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 101:
            case 102:
            case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
            case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case 112:
            case ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE /* 113 */:
            case ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX /* 114 */:
            case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET /* 115 */:
            case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET /* 116 */:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 129:
                return 9;
            case 103:
            case 125:
            case 126:
            case 127:
            case DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY /* 128 */:
            default:
                return ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).toTelecomDisconnectCauseCode(i);
        }
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public boolean disableNetworkIdentifiedEmergencyCall(TelephonyConnection telephonyConnection) {
        PersistableBundle carrierConfig = telephonyConnection.getCarrierConfig();
        if (carrierConfig != null) {
            return carrierConfig.getBoolean("carrier_disable_network_identified_emergency_call", false);
        }
        return false;
    }

    public void resetIntelligentDualSimDb() {
        MiuiPhoneUtils.resetIntelligentDualSimDb(this.mContext);
    }

    @Override // com.android.services.telephony.MiTelephonyServiceStub.IMiTelephonyService
    public void factoryReset() {
        ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).reset2GManager();
        resetIntelligentDualSimDb();
    }

    private CharSequence getSuppServiceMessage(TelephonyConnection telephonyConnection, int i, int i2) {
        int i3;
        if (i == 0) {
            switch (i2) {
                case 0:
                case 1:
                    i3 = R.string.supp_service_call_forwarding_active;
                    break;
                case 2:
                    i3 = R.string.supp_service_notification_call_forwarded;
                    break;
                case 3:
                    i3 = R.string.supp_service_notification_call_waiting;
                    break;
                case 4:
                    i3 = R.string.supp_service_closed_user_group_call;
                    break;
                case 5:
                    i3 = R.string.supp_service_outgoing_calls_barred;
                    break;
                case 6:
                    i3 = R.string.supp_service_incoming_calls_barred;
                    break;
                case 7:
                    i3 = R.string.supp_service_clir_suppression_rejected;
                    break;
                case 8:
                    i3 = R.string.supp_service_notification_call_deflected;
                    break;
                default:
                    i3 = -1;
                    break;
            }
        } else if (i == 1) {
            switch (i2) {
                case 0:
                    i3 = R.string.supp_service_forwarded_call;
                    break;
                case 1:
                    i3 = R.string.supp_service_closed_user_group_call;
                    break;
                case 2:
                    i3 = R.string.supp_service_call_on_hold;
                    break;
                case 3:
                    i3 = R.string.supp_service_call_resumed;
                    break;
                case 4:
                    i3 = R.string.supp_service_conference_call;
                    break;
                case 5:
                    i3 = R.string.supp_service_held_call_released;
                    break;
                case 6:
                default:
                    i3 = -1;
                    break;
                case 7:
                    i3 = R.string.supp_service_additional_ect_connecting;
                    break;
                case 8:
                    i3 = R.string.supp_service_additional_ect_connected;
                    break;
                case 9:
                    i3 = R.string.supp_service_deflected_call;
                    break;
                case 10:
                    i3 = R.string.supp_service_additional_call_forwarded;
                    break;
            }
        } else {
            i3 = -1;
        }
        if (i3 == -1 || telephonyConnection.getPhone() == null || telephonyConnection.getPhone().getContext() == null) {
            return null;
        }
        return getResourceText(telephonyConnection, i3);
    }

    private CharSequence getResourceText(TelephonyConnection telephonyConnection, int i) {
        return SubscriptionManager.getResourcesForSubId(telephonyConnection.getPhone().getContext(), telephonyConnection.getPhone().getSubId()).getText(i);
    }

    private boolean isCrossSimCall(TelephonyConnection telephonyConnection) {
        com.android.internal.telephony.Connection connection = telephonyConnection.mOriginalConnection;
        if (connection == null || connection.getPhoneType() != 5) {
            return false;
        }
        ImsPhoneConnection imsPhoneConnection = telephonyConnection.mOriginalConnection;
        return (imsPhoneConnection instanceof ImsPhoneConnection) && imsPhoneConnection.isCrossSimCall();
    }

    private boolean isValidRingingCall(TelephonyConnection telephonyConnection) {
        if (telephonyConnection.getPhone() == null) {
            android.util.Log.v("MiTelephonyServiceImpl", "isValidRingingCall, phone is null");
            return false;
        }
        Call ringingCall = telephonyConnection.getPhone().getRingingCall();
        if (!ringingCall.getState().isRinging()) {
            android.util.Log.v("MiTelephonyServiceImpl", "isValidRingingCall, ringing call is not in ringing state");
            return false;
        }
        if (ringingCall.getEarliestConnection() != telephonyConnection.mOriginalConnection) {
            android.util.Log.v("MiTelephonyServiceImpl", "isValidRingingCall, ringing call connection does not match");
            return false;
        }
        android.util.Log.v("MiTelephonyServiceImpl", "isValidRingingCall, returning true");
        return true;
    }
}
