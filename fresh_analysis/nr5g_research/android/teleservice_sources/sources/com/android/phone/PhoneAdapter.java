package com.android.phone;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.util.Log;
import com.android.ims.ImsManager;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.IccCard;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.ServiceStateTracker;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.uicc.IccCardApplicationStatus;
import com.android.internal.telephony.uicc.IccCardStatus;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.UiccCard;
import com.android.internal.telephony.uicc.UiccController;
import com.android.internal.telephony.uicc.UiccSlot;
import com.android.phone.utils.Utils;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHook;
import com.xiaomi.mirilhook.MiRilHookBase;
import java.util.ArrayList;
import miui.os.HyperOSCustFeatureResolve;
import miui.telephony.MiuiTelephony;
import miui.telephony.TelephonyManager;
import miuix.core.util.SystemProperties;

/* JADX INFO: loaded from: classes.dex */
public class PhoneAdapter {
    private static final String[] PROPERTY_RIL_CT3G = {"vendor.gsm.ril.ct3g", "vendor.gsm.ril.ct3g.2", "vendor.gsm.ril.ct3g.3", "vendor.gsm.ril.ct3g.4"};
    protected static volatile PhoneAdapter sInstance;

    public ArrayList convertPlmnList(int i, Object obj) {
        return null;
    }

    public int getNtnVoiceRate(boolean z) {
        return 0;
    }

    public boolean isManualNetSelAllowed(Phone phone) {
        return true;
    }

    public int toTelecomDisconnectCauseCode(int i) {
        return 0;
    }

    public static PhoneAdapter getInstance() {
        if (sInstance == null) {
            synchronized (PhoneAdapter.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new PhoneAdapter();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    protected PhoneAdapter() {
    }

    public static IccCardStatus.CardState getCardState(int i) {
        UiccSlot uiccSlotForPhone = UiccController.getInstance().getUiccSlotForPhone(i);
        if (uiccSlotForPhone == null) {
            return null;
        }
        UiccCard uiccCard = uiccSlotForPhone.getUiccCard();
        if (uiccCard != null) {
            return uiccCard.getCardState();
        }
        if (uiccSlotForPhone.isStateUnknown()) {
            return null;
        }
        return IccCardStatus.CardState.CARDSTATE_ABSENT;
    }

    public boolean getIccFdnAvailable(Phone phone) {
        IccCard iccCard = phone.getIccCard();
        if (iccCard != null) {
            return iccCard.getIccFdnAvailable();
        }
        return false;
    }

    public void getCallWaitingOption(Phone phone, Message message) {
        if (phone != null) {
            ImsPhone imsPhone = phone.getImsPhone();
            if (imsPhone != null && ((imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled()) && message.what % 2 == 0)) {
                imsPhone.getCallWaiting(message);
                Log.i("PhoneAdapter", "getCallWaiting Try IMS");
            } else {
                PhoneProxy.getCommandsInterface(phone).queryCallWaiting(0, message);
                Log.i("PhoneAdapter", "getCallWaiting Try RIL");
            }
        }
    }

    public void setCallWaitingOption(Phone phone, boolean z, Message message) {
        if (phone != null) {
            ImsPhone imsPhone = phone.getImsPhone();
            if (imsPhone != null && ((imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled()) && message.what % 2 == 0)) {
                imsPhone.setCallWaiting(z, message);
                Log.i("PhoneAdapter", "setCallWaiting Try IMS enable: " + z);
                return;
            }
            PhoneProxy.getCommandsInterface(phone).setCallWaiting(z, 1, message);
            Log.i("PhoneAdapter", "setCallWaiting Try RIL enable: " + z);
        }
    }

    public void getCallForwardingOption(Phone phone, int i, int i2, Message message) {
        if (phone != null) {
            ImsPhone imsPhone = phone.getImsPhone();
            if (imsPhone != null && ((imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled()) && message.what % 2 == 0)) {
                imsPhone.getCallForwardingOption(i, i2, message);
                Log.i("PhoneAdapter", "getCallForwarding Try IMS CFReason: " + i);
                return;
            }
            PhoneProxy.getCommandsInterface(phone).queryCallForwardStatus(i, i2, (String) null, Utils.handleGetCFMessage(i, message));
            Log.i("PhoneAdapter", "getCallForwarding Try RIL CFReason: " + i);
        }
    }

    public void setCallForwardingOption(Phone phone, int i, int i2, String str, int i3, int i4, Message message) {
        if (phone != null) {
            ImsPhone imsPhone = phone.getImsPhone();
            if (imsPhone != null && ((imsPhone.getServiceState().getState() == 0 || imsPhone.isUtEnabled()) && message.what % 2 == 0)) {
                imsPhone.setCallForwardingOption(i, i2, str, i3, configCallForwardTimerSeconds(phone, i4, true), message);
                Log.i("PhoneAdapter", "setCallForwarding Try IMS Action: " + i + " CFReason: " + i2);
                return;
            }
            PhoneProxy.getCommandsInterface(phone).setCallForward(i, i2, i3, str, configCallForwardTimerSeconds(phone, i4, false), Utils.handleSetCFMessage(i, i2, str, i3, message));
            Log.i("PhoneAdapter", "setCallForwarding Try RIL Action: " + i + " CFReason: " + i2);
        }
    }

    private int configCallForwardTimerSeconds(Phone phone, int i, boolean z) {
        if (z) {
            return HyperOSCustFeatureResolve.getIntForPhoneId(phone.getPhoneId(), "config_call_forward_ps_timer_seconds", i);
        }
        return HyperOSCustFeatureResolve.getIntForPhoneId(phone.getPhoneId(), "config_call_forward_cs_timer_seconds", i);
    }

    public void getCallBarringOption(Phone phone, String str, String str2, Message message) {
        if (phone != null) {
            ImsPhone imsPhone = phone.getImsPhone();
            if (imsPhone != null && imsPhone.isUtEnabled() && message.what % 2 == 0) {
                PhoneProxy.getCallBarringOverIms(imsPhone, str, str2, message, MiuiPhoneUtils.callBarringShouldUseVoiceClass(phone) ? 1 : 0);
                Log.i("PhoneAdapter", "getCallBarring Try IMS facility: " + str);
                return;
            }
            PhoneProxy.getCommandsInterface(phone).queryFacilityLock(str, str2, 0, message);
            Log.i("PhoneAdapter", "getCallBarring Try RIL facility: " + str);
        }
    }

    public void setCallBarringOption(Phone phone, String str, boolean z, String str2, Message message) {
        if (phone != null) {
            ImsPhone imsPhone = phone.getImsPhone();
            if (imsPhone != null && imsPhone.isUtEnabled() && message.what % 2 == 0) {
                PhoneProxy.setCallBarringOverIms(imsPhone, str, z, str2, message, MiuiPhoneUtils.callBarringShouldUseVoiceClass(phone) ? 1 : 0);
                Log.i("PhoneAdapter", "setCallBarring Try IMS facility: " + str + " lockState: " + z);
                return;
            }
            PhoneProxy.getCommandsInterface(phone).setFacilityLock(str, z, str2, 0, message);
            Log.i("PhoneAdapter", "setCallBarring Try RIL facility: " + str + " lockState: " + z);
        }
    }

    public void getPlmnList(Phone phone, IccFileHandler iccFileHandler, Message message) {
        replyMessageError(message);
    }

    public void setPlmnList(Phone phone, IccFileHandler iccFileHandler, ArrayList arrayList, Message message) {
        replyMessageError(message);
    }

    public void registerForVoiceRoaming(Handler handler, int i, int i2) {
        for (Phone phone : MiuiPhoneUtils.getPhones()) {
            ServiceStateTracker serviceStateTracker = PhoneProxy.getServiceStateTracker(phone);
            serviceStateTracker.registerForVoiceRoamingOn(handler, i, (Object) null);
            serviceStateTracker.registerForVoiceRoamingOff(handler, i2, (Object) null);
        }
    }

    public boolean getVoiceRoaming(ServiceState serviceState) {
        return serviceState.getVoiceRoaming();
    }

    public boolean isUtEnabled(Phone phone) {
        return phone.isUtEnabled();
    }

    public int getVideoCFServiceClass() {
        return MiuiTelephony.IS_MTK ? 512 : 80;
    }

    public boolean isUTError(CommandException commandException) {
        if (commandException == null) {
            return false;
        }
        CommandException.Error commandError = commandException.getCommandError();
        return commandError == CommandException.Error.OPERATION_NOT_ALLOWED || commandError == CommandException.Error.OEM_ERROR_2 || commandError == CommandException.Error.OEM_ERROR_3;
    }

    public int getPhoneType(int i) {
        Phone phone = MiuiPhoneUtils.getPhone(i);
        if (phone == null) {
            return 0;
        }
        IccCard iccCard = phone.getIccCard();
        if (iccCard.isApplicationOnIcc(IccCardApplicationStatus.AppType.APPTYPE_RUIM) || iccCard.isApplicationOnIcc(IccCardApplicationStatus.AppType.APPTYPE_CSIM) || isCt3gDualMode(i)) {
            return 2;
        }
        return (iccCard.isApplicationOnIcc(IccCardApplicationStatus.AppType.APPTYPE_SIM) || iccCard.isApplicationOnIcc(IccCardApplicationStatus.AppType.APPTYPE_USIM)) ? 1 : 0;
    }

    public void cancelAvailableNetworks(Handler handler, int i, int i2) {
        Phone phone = PhoneFactory.getPhone(SubscriptionManager.getPhoneId(i2));
        if (phone == null) {
            return;
        }
        if (MiuiTelephony.IS_XRING || MiuiTelephony.IS_MTK) {
            RIL ril = phone.mCi;
            if (ril != null) {
                ril.cancelAvailableNetworks(handler.obtainMessage(i));
                return;
            }
            return;
        }
        Log.i("PhoneAdapter", "cancelAvailableNetworks: not supported");
    }

    public Integer toTelecomDisconnectCauseDescription(Context context, int i, int i2, Integer num) {
        if (i2 == -1 || context == null) {
            Log.d("PhoneAdapter", "null phoneId or context");
            return null;
        }
        if (TelephonyManager.isSupportCallClearCode(i2)) {
            if (i == 2) {
                return Integer.valueOf(com.android.phone.common.R$string.callFailed_normal);
            }
            if (i == 4) {
                return Integer.valueOf(com.android.phone.common.R$string.callFailed_userBusy);
            }
            if (i == 7) {
                return Integer.valueOf(com.android.phone.common.R$string.clh_callFailed_invalid_number_format_txt);
            }
            if (i == 15) {
                return Integer.valueOf(com.android.phone.common.R$string.callFailed_limitExceeded);
            }
            if (i == 25) {
                return Integer.valueOf(com.android.phone.common.R$string.callFailed_unobtainable_number);
            }
            if (i == 65) {
                return Integer.valueOf(com.android.phone.common.R$string.callFailed_normal_unspecified);
            }
            if (i == 126) {
                return Integer.valueOf(com.android.phone.common.R$string.callFailed_non_selected_user_clearing);
            }
            if (i != 129) {
                switch (i) {
                    case 83:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_bearer_capability_unavailable);
                    case SmartNetworkRelayScore.SCORE_PASS /* 84 */:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_service_option_not_available);
                    case 85:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_bearer_service_not_implemented);
                    case 86:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_requested_facility_not_implemented);
                    case 87:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_only_digital_information_bearer_available);
                    case 88:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_service_or_option_not_implemented);
                    case 89:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_invalid_transaction_identifier);
                    case 90:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_user_not_member_of_CUG);
                    case SystemProperties.PROP_VALUE_MAX /* 91 */:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_incompatible_destination);
                    case 92:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_invalid_transit_NW_selection);
                    case 93:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_semantically_incorrect_message);
                    case 94:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_invalid_mandatory_information);
                    case 95:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_message_type_non_implemented);
                    case 96:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_message_type_not_compatible_with_protocol_state);
                    case 97:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_information_element_non_existent);
                    case 98:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_conditional_IE_error);
                    case 99:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_message_not_compatible_with_protocol_state);
                    case 100:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_recovery_on_timer_expired);
                    case 101:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_protocol_error_unspecified);
                    case 102:
                        return Integer.valueOf(com.android.phone.common.R$string.callFailed_interworking_unspecified);
                    default:
                        switch (i) {
                            case MiRilHook.HOOK_ID_EL1_PUSCH_POWER_CONTROL_BY_AT /* 104 */:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_no_circuit_avail);
                            case MiRilHook.HOOK_ID_NL1_PUSCH_POWER_CONTROL_BY_AT /* 105 */:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_no_route_to_destination);
                            case 106:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_operator_determined_barring);
                            case 107:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_no_user_responding);
                            case 108:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_no_answer_from_user);
                            case 109:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_destination_out_of_order);
                            case 110:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_bearer_capability_not_authorized);
                            case 111:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_channel_unacceptable);
                            case 112:
                                return Integer.valueOf(com.android.phone.common.R$string.clh_callFailed_call_rejected_txt);
                            case ModemServiceMoniter.EVENT_OEMHOOK_NOTIFY_MODEM_SYNC_DONE /* 113 */:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_number_changed);
                            case ModemServiceMoniter.EVENT_OEMHOOK_SCREEN_STATUS_NOTIFY_EX /* 114 */:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_preemption);
                            case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_GET /* 115 */:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_facility_rejected);
                            case MiRilHookBase.EVENT_OEMHOOK_DISPLAY_MIPI_FEATURE_SET /* 116 */:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_resp_to_status_enquiry);
                            case 117:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_network_out_of_order);
                            case 118:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_tempopary_failure);
                            case 119:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_switching_equipment_congestion);
                            case 120:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_access_information_discarded);
                            case 121:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_requested_circuit_or_channel_not_available);
                            case 122:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_resources_unavailable_or_unspecified);
                            case 123:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_QOS_unavailable);
                            case 124:
                                return Integer.valueOf(com.android.phone.common.R$string.callFailed_requested_facility_not_subscribed);
                        }
                }
            }
            return Integer.valueOf(com.android.phone.common.R$string.callFailed_incoming_calls_barred_within_CUG);
        }
        return num;
    }

    public boolean isVoiceLinkRegistered(Context context, int i) {
        ImsManager imsManager = ImsManager.getInstance(context, i);
        int imsPdnStatus = -1;
        if (imsManager != null) {
            try {
                imsPdnStatus = imsManager.getImsPdnStatus(i);
                Log.d("PhoneAdapter", "isVoiceLikRegistered pdn status is " + imsPdnStatus);
            } catch (Throwable th) {
                Log.e("PhoneAdapter", "getImsPdnStatus Exception: " + th);
            }
        }
        return imsPdnStatus == 6 || imsPdnStatus == 7;
    }

    public boolean isCrossSimCallingEnabledByUser(Context context, int i) {
        if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
            ImsManager imsManager = ImsManager.getInstance(context, i);
            if (imsManager != null) {
                return imsManager.isCrossSimCallingEnabledByUser();
            }
            return false;
        }
        int subscriptionId = SubscriptionManager.getSubscriptionId(i);
        if (!SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
            return false;
        }
        ContentResolver contentResolver = context.getContentResolver();
        StringBuilder sb = new StringBuilder("cross_sim_call_");
        sb.append(subscriptionId);
        return Settings.Global.getInt(contentResolver, sb.toString(), 0) == 1;
    }

    public void setCrossSimCallingEnabled(Context context, int i, boolean z) {
        ImsManager imsManager;
        if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
            Settings.Global.putInt(context.getContentResolver(), "cross_sim_call_" + i, z ? 1 : 0);
            ImsManager imsManager2 = ImsManager.getInstance(context, i);
            if (imsManager2 != null) {
                imsManager2.setCrossSimCallingEnabled(z);
                return;
            }
            return;
        }
        int subscriptionId = SubscriptionManager.getSubscriptionId(i);
        if (SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
            Settings.Global.putInt(context.getContentResolver(), "cross_sim_call_" + subscriptionId, z ? 1 : 0);
            if (!z && (imsManager = ImsManager.getInstance(context, i)) != null && imsManager.isCrossSimCallingEnabled()) {
                imsManager.setCrossSimCallingEnabled(false);
            }
            if (VoiceLinkControllerBase.getInstance() != null) {
                VoiceLinkControllerBase.getInstance().SetCrossSimCallingSwitch();
            }
        }
    }

    public void restartRILD(Context context, int i, int i2) {
        RIL ril;
        if (MiuiTelephony.IS_MTK || MiuiTelephony.IS_XRING) {
            Phone phone = PhoneFactory.getPhone(i);
            if (phone == null || (ril = phone.mCi) == null) {
                return;
            } else {
                ril.restartRILD((Message) null);
            }
        } else if (MiuiTelephony.IS_QCOM || MiuiTelephony.IS_SPRD) {
            android.os.SystemProperties.set("persist.radio.restart_rild", Integer.toString(MiuiTelephony.PHONE_COUNT));
        } else {
            Log.i("PhoneAdapter", "restartRILD not supported ");
            return;
        }
        Intent intent = new Intent("miui.intent.action.RESET_RADIO");
        intent.putExtra("reset_rild", true);
        intent.setFlags(285212672);
        context.sendBroadcast(intent, "miui.permission.RESET_RADIO");
    }

    public boolean isCt3gDualMode(int i) {
        if (i >= 0) {
            String[] strArr = PROPERTY_RIL_CT3G;
            if (i < strArr.length) {
                String str = android.os.SystemProperties.get(strArr[i], "");
                Log.d("PhoneAdapter", "isCt3gDualMode:  " + str);
                return "1".equals(str);
            }
        }
        Log.e("PhoneAdapter", "isCt3gDualMode: invalid slotId " + i);
        return false;
    }

    public static PhoneAdapter make() {
        return getInstance();
    }

    public boolean isDataEnabled(Phone phone) {
        return phone != null && phone.isUserDataEnabled();
    }

    private static void replyMessageError(Message message) {
        if (message == null || message.getTarget() == null) {
            return;
        }
        AsyncResult.forMessage(message, (Object) null, CommandException.fromRilErrno(6));
        message.sendToTarget();
    }
}
