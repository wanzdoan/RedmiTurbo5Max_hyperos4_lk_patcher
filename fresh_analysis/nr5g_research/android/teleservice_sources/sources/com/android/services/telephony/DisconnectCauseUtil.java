package com.android.services.telephony;

import android.content.Context;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.telecom.DisconnectCause;
import android.telephony.SubscriptionManager;
import android.telephony.ims.ImsReasonInfo;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.satellite.SatelliteController;
import com.android.phone.ImsUtil;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import com.mediatek.phone.ext.ExtensionManager;
import com.ot.pubsub.i.a;
import com.xiaomi.internal.telephony.DynamicSarService;
import com.xiaomi.internal.telephony.ModemServiceMoniter;
import com.xiaomi.mirilhook.MiRilHookBase;
import com.xiaomi.modem.sar.DeviceStateListenerBase;
import miuix.animation.utils.EaseManager;
import miuix.core.util.SystemProperties;
import miuix.preference.flexible.AbstractBaseTemplate;

/* JADX INFO: loaded from: classes.dex */
public abstract class DisconnectCauseUtil {
    public static DisconnectCause toTelecomDisconnectCause(int i) {
        return toTelecomDisconnectCause(i, -1, (String) null);
    }

    public static DisconnectCause toTelecomDisconnectCause(int i, String str) {
        return toTelecomDisconnectCause(i, -1, str);
    }

    public static DisconnectCause toTelecomDisconnectCause(int i, int i2, String str) {
        return toTelecomDisconnectCause(i, i2, str, SubscriptionManager.getDefaultVoicePhoneId(), null, new FlagsAdapterImpl());
    }

    public static DisconnectCause toTelecomDisconnectCause(int i, String str, int i2) {
        return toTelecomDisconnectCause(i, -1, str, i2, null, new FlagsAdapterImpl(), false);
    }

    public static DisconnectCause toTelecomDisconnectCause(int i, int i2, String str, int i3, ImsReasonInfo imsReasonInfo, FlagsAdapter flagsAdapter) {
        return toTelecomDisconnectCause(i, i2, str, i3, imsReasonInfo, getCarrierConfigBundle(i3), flagsAdapter, false);
    }

    public static DisconnectCause toTelecomDisconnectCause(int i, int i2, String str, int i3, ImsReasonInfo imsReasonInfo, FlagsAdapter flagsAdapter, boolean z) {
        return toTelecomDisconnectCause(i, i2, str, i3, imsReasonInfo, getCarrierConfigBundle(i3), flagsAdapter, z);
    }

    static DisconnectCause toTelecomDisconnectCause(int i, int i2, String str, int i3, ImsReasonInfo imsReasonInfo, PersistableBundle persistableBundle, FlagsAdapter flagsAdapter, boolean z) {
        PhoneGlobals phoneGlobals = PhoneGlobals.getInstance();
        int iChangeDisconnectCause = ExtensionManager.getIncomingCallExt().changeDisconnectCause(i);
        return new DisconnectCause.Builder(toTelecomDisconnectCauseCode(iChangeDisconnectCause, persistableBundle)).setLabel(MiTelephonyServiceStub.get().toTelecomDisconnectCauseLabelOrDescription(toTelecomDisconnectCauseLabel(phoneGlobals, iChangeDisconnectCause, i2, persistableBundle, flagsAdapter), toTelecomDisconnectCauseDescription(phoneGlobals, iChangeDisconnectCause, i3, z, persistableBundle))).setDescription(toTelecomDisconnectCauseDescription(phoneGlobals, iChangeDisconnectCause, i3, z, persistableBundle)).setReason(toTelecomDisconnectReason(phoneGlobals, iChangeDisconnectCause, str, i3)).setTone(toTelecomDisconnectCauseTone(iChangeDisconnectCause, persistableBundle, flagsAdapter)).setTelephonyDisconnectCause(iChangeDisconnectCause).setTelephonyPreciseDisconnectCause(i2).setImsReasonInfo(imsReasonInfo).build();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static int toTelecomDisconnectCauseCode(int i, PersistableBundle persistableBundle) {
        if (doesCarrierClassifyDisconnectCauseAsBusyCause(i, persistableBundle)) {
            return 7;
        }
        if (i != 82) {
            if (i == 1001) {
                return 4;
            }
            switch (i) {
                case -1:
                case 0:
                    return 0;
                case 1:
                    return 5;
                case 2:
                    return 3;
                case 3:
                    return 2;
                case 4:
                    return 7;
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 17:
                case 18:
                case 19:
                case 25:
                case 26:
                case 27:
                case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
                case 29:
                case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
                case 31:
                case DynamicSarService.WIFI_DATA_STATE_TCP_STATE /* 32 */:
                case 33:
                case 36:
                case 38:
                case 40:
                case 41:
                case 43:
                case 46:
                case ModemServiceMoniter.EVENT_OEMHOOK_CALL_STATUS_NOTIFY /* 47 */:
                case ModemServiceMoniter.EVENT_OEMHOOK_DDS_CHANGE_NOTIFY /* 48 */:
                case 49:
                case 53:
                case 54:
                case 55:
                    break;
                case 6:
                case MiRilHookBase.EVENT_OEMHOOK_CARD_SLOT_STATUS_QUERY /* 39 */:
                case 42:
                case AbstractBaseTemplate.BIG_ICON_WIDTH_THRESHOLD_DP /* 45 */:
                    return 9;
                case 15:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 34:
                case MiRilHookBase.EVENT_OEMHOOK_NV_EFS_SYNC /* 35 */:
                case 37:
                case 50:
                    return 8;
                case 16:
                    return 6;
                case MiRilHookBase.EVENT_OEMHOOK_HVOLTE_MBN_CFG /* 44 */:
                    return 4;
                case 51:
                    return 12;
                case 52:
                    return 11;
                default:
                    switch (i) {
                        default:
                            switch (i) {
                                case 65:
                                    return 3;
                                case 66:
                                case 67:
                                case 68:
                                case 69:
                                case 70:
                                case 71:
                                case 72:
                                case 73:
                                case 74:
                                case 75:
                                case 76:
                                case 77:
                                case 78:
                                case 79:
                                    break;
                                case 80:
                                    return 2;
                                default:
                                    switch (i) {
                                        case 400:
                                        case a.b /* 401 */:
                                        case 402:
                                        case 403:
                                            return 200;
                                        default:
                                            switch (i) {
                                                case 1004:
                                                    return 1;
                                                case 1005:
                                                case 1006:
                                                    return 200;
                                                default:
                                                    switch (i) {
                                                        case 1500:
                                                        case 1501:
                                                        case 1502:
                                                        case 1503:
                                                        case 1504:
                                                        case 1505:
                                                        case 1506:
                                                        case 1507:
                                                        case 1508:
                                                        case 1509:
                                                        case 1510:
                                                        case 1511:
                                                        case 1512:
                                                        case 1513:
                                                        case 1514:
                                                        case 1515:
                                                        case 1516:
                                                        case 1517:
                                                        case 1518:
                                                        case 1519:
                                                        case 1520:
                                                        case 1521:
                                                        case 1522:
                                                        case 1523:
                                                        case 1524:
                                                        case 1525:
                                                        case 1526:
                                                        case 1527:
                                                        case 1528:
                                                        case 1529:
                                                        case 1530:
                                                        case 1531:
                                                        case 1532:
                                                        case 1533:
                                                        case 1534:
                                                        case 1535:
                                                        case 1536:
                                                        case 1537:
                                                        case 1538:
                                                        case 1539:
                                                            return EaseManager.EaseStyleDef.PERLIN;
                                                        default:
                                                            Log.w("DisconnectCauseUtil.toTelecomDisconnectCauseCode", "Telephony DisconnectCause " + i, new Object[0]);
                                                            return MiTelephonyServiceStub.get().toTelecomDisconnectCauseCode(i);
                                                    }
                                            }
                                    }
                            }
                        case 57:
                        case 58:
                        case 59:
                        case 60:
                        case 61:
                        case MiRilHookBase.EVENT_OEMHOOK_MODEM_STATS_DATA_GET /* 62 */:
                            return 1;
                    }
                    break;
            }
        }
        return 1;
    }

    private static CharSequence toTelecomDisconnectCauseLabel(Context context, int i, int i2, PersistableBundle persistableBundle, FlagsAdapter flagsAdapter) {
        if (i2 != -1) {
            return getLabelFromPreciseDisconnectCause(context, i2, i);
        }
        if (doesCarrierClassifyDisconnectCauseAsBusyCause(i, persistableBundle)) {
            return context.getResources().getString(R.string.callFailed_userBusy);
        }
        if (doesCarrierClassifyDisconnectCauseAsNetworkBusyCause(i, persistableBundle)) {
            return context.getResources().getString(R.string.callFailed_NetworkBusy);
        }
        return getLabelFromDisconnectCause(context, i);
    }

    /* JADX WARN: Code duplicated, block: B:53:0x010f  */
    /* JADX WARN: Code duplicated, block: B:60:0x0140  */
    private static CharSequence getLabelFromDisconnectCause(Context context, int i) {
        Integer numValueOf;
        if (context == null) {
            return "";
        }
        if (i == 4) {
            numValueOf = Integer.valueOf(R.string.callFailed_userBusy);
        } else if (i == 5) {
            numValueOf = Integer.valueOf(R.string.callFailed_congestion);
        } else if (i == 78) {
            numValueOf = Integer.valueOf(R.string.callFailed_emergency_call_over_wfc_not_available);
        } else if (i == 79) {
            numValueOf = Integer.valueOf(R.string.callFailed_wfc_service_not_available_in_this_location);
        } else if (i == 1005) {
            numValueOf = Integer.valueOf(R.string.wfc_wifi_call_drop_bad_rssi);
        } else {
            if (i != 1006) {
                switch (i) {
                    case 7:
                        numValueOf = Integer.valueOf(R.string.callFailed_unobtainable_number);
                        break;
                    case 8:
                        numValueOf = Integer.valueOf(R.string.callFailed_number_unreachable);
                        break;
                    case 9:
                        numValueOf = Integer.valueOf(R.string.callFailed_server_unreachable);
                        break;
                    case 10:
                        numValueOf = Integer.valueOf(R.string.callFailed_invalid_credentials);
                        break;
                    case 11:
                        numValueOf = Integer.valueOf(R.string.callFailed_out_of_network);
                        break;
                    case 12:
                        numValueOf = Integer.valueOf(R.string.callFailed_server_error);
                        break;
                    case 13:
                        numValueOf = Integer.valueOf(R.string.callFailed_timedOut);
                        break;
                    case 14:
                        numValueOf = Integer.valueOf(R.string.callFailed_noSignal);
                        break;
                    case 15:
                        numValueOf = Integer.valueOf(R.string.callFailed_limitExceeded);
                        break;
                    default:
                        switch (i) {
                            case 17:
                                numValueOf = Integer.valueOf(R.string.callFailed_powerOff);
                                break;
                            case 18:
                                numValueOf = Integer.valueOf(R.string.callFailed_outOfService);
                                break;
                            case 19:
                                numValueOf = Integer.valueOf(R.string.callFailed_simError);
                                break;
                            default:
                                switch (i) {
                                    case 25:
                                        numValueOf = Integer.valueOf(R.string.callFailed_unobtainable_number);
                                        break;
                                    case 27:
                                        numValueOf = Integer.valueOf(R.string.callFailed_noSignal);
                                        break;
                                    case 29:
                                        numValueOf = Integer.valueOf(R.string.callFailed_NetworkBusy);
                                        break;
                                    case 40:
                                        numValueOf = Integer.valueOf(R.string.incall_error_missing_voicemail_number);
                                        break;
                                    case 51:
                                        numValueOf = Integer.valueOf(R.string.callEnded_pulled);
                                        break;
                                    case MiRilHookBase.EVENT_OEMHOOK_XIAOMI_MODEM_HAL_CHIP_ID_GET /* 82 */:
                                        numValueOf = getSatelliteErrorString();
                                        break;
                                    case 1500:
                                    case 1501:
                                    case 1502:
                                    case 1503:
                                    case 1504:
                                    case 1505:
                                    case 1506:
                                    case 1507:
                                    case 1508:
                                    case 1509:
                                    case 1510:
                                    case 1511:
                                    case 1512:
                                    case 1513:
                                    case 1514:
                                    case 1515:
                                    case 1516:
                                    case 1517:
                                    case 1518:
                                    case 1519:
                                    case 1520:
                                    case 1521:
                                    case 1522:
                                    case 1523:
                                    case 1524:
                                    case 1525:
                                    case 1526:
                                    case 1527:
                                    case 1528:
                                    case 1529:
                                    case 1530:
                                    case 1531:
                                    case 1532:
                                    case 1533:
                                    case 1534:
                                    case 1535:
                                    case 1536:
                                    case 1537:
                                    case 1538:
                                    case 1539:
                                        break;
                                    default:
                                        switch (i) {
                                            case 53:
                                                numValueOf = Integer.valueOf(R.string.callFailed_maximum_reached);
                                                break;
                                            case 54:
                                                numValueOf = Integer.valueOf(R.string.callFailed_data_disabled);
                                                break;
                                            case 55:
                                                numValueOf = Integer.valueOf(R.string.callFailed_data_limit_reached);
                                                break;
                                            default:
                                                switch (i) {
                                                    case 59:
                                                        numValueOf = Integer.valueOf(R.string.callFailed_wifi_lost);
                                                        break;
                                                    case 60:
                                                        numValueOf = Integer.valueOf(R.string.callFailed_NetworkCongested);
                                                        break;
                                                    case 61:
                                                        numValueOf = Integer.valueOf(R.string.callFailed_low_battery);
                                                        break;
                                                    case MiRilHookBase.EVENT_OEMHOOK_MODEM_STATS_DATA_GET /* 62 */:
                                                        numValueOf = Integer.valueOf(R.string.dialFailed_low_battery);
                                                        break;
                                                    default:
                                                        switch (i) {
                                                            case 71:
                                                                numValueOf = Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) == 0 ? Integer.valueOf(R.string.incall_error_call_failed) : Integer.valueOf(R.string.incall_error_power_off);
                                                                break;
                                                            case 72:
                                                                numValueOf = Integer.valueOf(R.string.callFailed_already_dialing);
                                                                break;
                                                            case 73:
                                                                numValueOf = Integer.valueOf(R.string.callFailed_already_ringing);
                                                                break;
                                                            case 74:
                                                                numValueOf = Integer.valueOf(R.string.callFailed_calling_disabled);
                                                                break;
                                                            case 75:
                                                                numValueOf = Integer.valueOf(R.string.callFailed_too_many_calls);
                                                                break;
                                                            case 76:
                                                                numValueOf = Integer.valueOf(R.string.callFailed_otasp_provisioning_in_process);
                                                                break;
                                                            default:
                                                                switch (i) {
                                                                    case 400:
                                                                        numValueOf = Integer.valueOf(R.string.wfc_wifi_call_drop);
                                                                        break;
                                                                    case a.b /* 401 */:
                                                                        numValueOf = Integer.valueOf(R.string.wfc_internet_connection_lost);
                                                                        break;
                                                                    case 402:
                                                                        numValueOf = Integer.valueOf(R.string.wfc_wifi_call_drop);
                                                                        break;
                                                                    case 403:
                                                                        numValueOf = Integer.valueOf(R.string.wfc_no_network);
                                                                        break;
                                                                    default:
                                                                        numValueOf = null;
                                                                        break;
                                                                }
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                        break;
                }
            } else {
                int i2 = R.string.accessibility_settings_activity_title;
            }
            numValueOf = Integer.valueOf(R.string.call_error);
        }
        if (numValueOf == null) {
            return "";
        }
        return context.getResources().getString(numValueOf.intValue());
    }

    /* JADX WARN: Code duplicated, block: B:87:0x01d1  */
    private static CharSequence getLabelFromPreciseDisconnectCause(Context context, int i, int i2) {
        Integer numValueOf;
        if (context == null) {
            return "";
        }
        if (i == 1) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_unassigned_number_txt);
        } else if (i == 3) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_no_route_to_destination_txt);
        } else if (i == 6) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_channel_unacceptable_txt);
        } else if (i == 21) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_call_rejected_txt);
        } else if (i == 22) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_number_changed_txt);
        } else if (i == 49) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_quality_of_service_unavailable_txt);
        } else if (i == 50) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_requested_facility_not_subscribed_txt);
        } else if (i == 57) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_bearer_capability_not_authorized_txt);
        } else if (i == 58) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_bearer_capability_not_presently_available_txt);
        } else if (i == 87) {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_user_not_member_of_CUG_txt);
        } else if (i != 88) {
            switch (i) {
                case 6:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_channel_unacceptable_txt);
                    break;
                case 8:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_operator_determined_barring_txt);
                    break;
                case 34:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_no_circuit_available_txt);
                    break;
                case 38:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_network_out_of_order_txt);
                    break;
                case ModemServiceMoniter.EVENT_OEMHOOK_CALL_STATUS_NOTIFY /* 47 */:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_resources_unavailable_unspecified_txt);
                    break;
                case 55:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_incoming_calls_barred_within_the_CUG_txt);
                    break;
                case 63:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_service_or_option_not_available_unspecified_txt);
                    break;
                case 65:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_bearer_service_not_implemented_txt);
                    break;
                case 79:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_service_or_option_not_implemented_unspecified_txt);
                    break;
                case MiRilHookBase.EVENT_OEMHOOK_MTB_CLASS_MASK_GET /* 81 */:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_invalid_transaction_identifier_value_txt);
                    break;
                case SystemProperties.PROP_VALUE_MAX /* 91 */:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_invalid_transit_network_selection_txt);
                    break;
                case 111:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_protocol_Error_unspecified_txt);
                    break;
                case 127:
                    numValueOf = Integer.valueOf(R.string.clh_callFailed_interworking_unspecified_txt);
                    break;
                default:
                    switch (i) {
                        case 16:
                            numValueOf = Integer.valueOf(R.string.clh_callFailed_normal_call_clearing_txt);
                            break;
                        case 17:
                            numValueOf = Integer.valueOf(R.string.clh_callFailed_user_busy_txt);
                            break;
                        case 18:
                            numValueOf = Integer.valueOf(R.string.clh_callFailed_no_user_responding_txt);
                            break;
                        case 19:
                            numValueOf = Integer.valueOf(R.string.clh_callFailed_user_alerting_txt);
                            break;
                        default:
                            switch (i) {
                                case 25:
                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_pre_emption_txt);
                                    break;
                                case 26:
                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_non_selected_user_clearing_txt);
                                    break;
                                case 27:
                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_destination_out_of_order_txt);
                                    break;
                                case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_invalid_number_format_txt);
                                    break;
                                case 29:
                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_facility_rejected_txt);
                                    break;
                                case DeviceStateListenerBase.TYPE_HST_ENHANCEMENT_STATE /* 30 */:
                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_response_to_STATUS_ENQUIRY_txt);
                                    break;
                                case 31:
                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_normal_unspecified_txt);
                                    break;
                                default:
                                    switch (i) {
                                        case 41:
                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_temporary_failure_txt);
                                            break;
                                        case 42:
                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_switching_equipment_congestion_txt);
                                            break;
                                        case 43:
                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_access_information_discarded_txt);
                                            break;
                                        case MiRilHookBase.EVENT_OEMHOOK_HVOLTE_MBN_CFG /* 44 */:
                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_requested_circuit_txt);
                                            break;
                                        default:
                                            switch (i) {
                                                case 68:
                                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_ACM_equal_to_or_greater_than_ACMmax_txt);
                                                    break;
                                                case 69:
                                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_requested_facility_not_implemented_txt);
                                                    break;
                                                case 70:
                                                    numValueOf = Integer.valueOf(R.string.clh_callFailed_only_restricted_digital_information_bearer_capability_is_available_txt);
                                                    break;
                                                default:
                                                    switch (i) {
                                                        case 95:
                                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_semantically_incorrect_message_txt);
                                                            break;
                                                        case 96:
                                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_invalid_mandatory_information_txt);
                                                            break;
                                                        case 97:
                                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_message_type_non_existent_or_not_implemented_txt);
                                                            break;
                                                        case 98:
                                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_message_type_not_compatible_with_protocol_state_txt);
                                                            break;
                                                        case 99:
                                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_information_element_non_existent_or_not_implemented_txt);
                                                            break;
                                                        case 100:
                                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_conditional_IE_error_txt);
                                                            break;
                                                        case 101:
                                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_message_not_compatible_with_protocol_state_txt);
                                                            break;
                                                        case 102:
                                                            numValueOf = Integer.valueOf(R.string.clh_callFailed_recovery_on_timer_expiry_txt);
                                                            break;
                                                        default:
                                                            if (i2 != 82) {
                                                                switch (i2) {
                                                                    case 17:
                                                                        numValueOf = Integer.valueOf(R.string.clh_callFailed_powerOff_txt);
                                                                        break;
                                                                    case 18:
                                                                        numValueOf = Integer.valueOf(R.string.clh_incall_error_out_of_service_txt);
                                                                        break;
                                                                    case 19:
                                                                        numValueOf = Integer.valueOf(R.string.clh_callFailed_simError_txt);
                                                                        break;
                                                                    default:
                                                                        numValueOf = Integer.valueOf(R.string.clh_card_title_call_ended_txt);
                                                                        break;
                                                                }
                                                            } else {
                                                                numValueOf = getSatelliteErrorString();
                                                                break;
                                                            }
                                                            break;
                                                    }
                                                    break;
                                            }
                                            break;
                                    }
                                    break;
                            }
                            break;
                    }
                    break;
            }
        } else {
            numValueOf = Integer.valueOf(R.string.clh_callFailed_incompatible_destination_txt);
        }
        return context.getResources().getString(numValueOf.intValue());
    }

    private static CharSequence toTelecomDisconnectCauseDescription(Context context, int i, int i2, boolean z, PersistableBundle persistableBundle) {
        Integer numValueOf;
        if (context == null) {
            return "";
        }
        if (doesCarrierClassifyDisconnectCauseAsNetworkBusyCause(i, persistableBundle)) {
            return context.getResources().getString(R.string.callFailed_NetworkBusy);
        }
        if (i != 43) {
            numValueOf = null;
            if (i != 44) {
                if (i == 78) {
                    numValueOf = Integer.valueOf(R.string.callFailed_emergency_call_over_wfc_not_available);
                } else if (i == 79) {
                    numValueOf = Integer.valueOf(R.string.callFailed_wfc_service_not_available_in_this_location);
                } else if (i == 1005) {
                    numValueOf = Integer.valueOf(R.string.wfc_wifi_call_drop_bad_rssi);
                } else {
                    if (i != 1006) {
                        switch (i) {
                            case 17:
                                if (isRadioOffForThermalMitigation(i2)) {
                                    numValueOf = Integer.valueOf(R.string.incall_error_power_off_thermal);
                                } else if (ImsUtil.shouldPromoteWfc(context, i2)) {
                                    numValueOf = Integer.valueOf(R.string.incall_error_promote_wfc);
                                } else if (ImsUtil.isWfcModeWifiOnly(context, i2)) {
                                    numValueOf = Integer.valueOf(R.string.incall_error_wfc_only_no_wireless_network);
                                } else if (!ImsUtil.isWfcEnabled(context, i2)) {
                                    if (Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0) > 0) {
                                        numValueOf = Integer.valueOf(R.string.incall_error_power_off);
                                    }
                                } else {
                                    numValueOf = Integer.valueOf(R.string.incall_error_power_off_wfc);
                                }
                                break;
                            case 18:
                                if (ImsUtil.shouldPromoteWfc(context, i2)) {
                                    numValueOf = Integer.valueOf(R.string.incall_error_promote_wfc);
                                } else if (ImsUtil.isWfcModeWifiOnly(context, i2)) {
                                    numValueOf = Integer.valueOf(R.string.incall_error_wfc_only_no_wireless_network);
                                } else if (ImsUtil.isWfcEnabled(context, i2)) {
                                    if (is2gDisabled(i2) && !z) {
                                        numValueOf = Integer.valueOf(R.string.incall_error_out_of_service_wfc_2g_user);
                                    } else {
                                        numValueOf = Integer.valueOf(R.string.incall_error_out_of_service_wfc);
                                    }
                                } else if (is2gDisabled(i2) && !z) {
                                    numValueOf = Integer.valueOf(R.string.incall_error_out_of_service_2g);
                                } else {
                                    numValueOf = Integer.valueOf(R.string.incall_error_out_of_service);
                                }
                                break;
                            case 19:
                                numValueOf = Integer.valueOf(R.string.callFailed_simError);
                                break;
                            case 20:
                                numValueOf = Integer.valueOf(R.string.callFailed_cb_enabled);
                                break;
                            case 21:
                                numValueOf = Integer.valueOf(R.string.callFailed_fdn_only);
                                break;
                            case 22:
                                numValueOf = Integer.valueOf(R.string.callFailed_dsac_restricted);
                                break;
                            case 23:
                                numValueOf = Integer.valueOf(R.string.callFailed_dsac_restricted_normal);
                                break;
                            case 24:
                                numValueOf = Integer.valueOf(R.string.callFailed_dsac_restricted_emergency);
                                break;
                            default:
                                switch (i) {
                                    case 29:
                                        numValueOf = Integer.valueOf(R.string.callFailed_NetworkBusy);
                                        break;
                                    case 34:
                                        numValueOf = Integer.valueOf(R.string.incall_error_ecm_emergency_only);
                                        break;
                                    case 40:
                                        numValueOf = Integer.valueOf(R.string.incall_error_missing_voicemail_number);
                                        break;
                                    case 66:
                                        numValueOf = Integer.valueOf(R.string.callFailed_dialToDialVideo);
                                        break;
                                    case 67:
                                        numValueOf = Integer.valueOf(R.string.callFailed_dialVideoToSs);
                                        break;
                                    case 68:
                                        numValueOf = Integer.valueOf(R.string.callFailed_dialVideoToUssd);
                                        break;
                                    case 69:
                                        numValueOf = Integer.valueOf(R.string.callFailed_dialVideoToDial);
                                        break;
                                    case 70:
                                        numValueOf = Integer.valueOf(R.string.callFailed_dialVideoToDialVideo);
                                        break;
                                    case 71:
                                        int i3 = Settings.Global.getInt(context.getContentResolver(), "airplane_mode_on", 0);
                                        numValueOf = Integer.valueOf(R.string.incall_error_call_failed);
                                        if (i3 != 0) {
                                            numValueOf = Integer.valueOf(R.string.incall_error_power_off);
                                        }
                                        break;
                                    case 72:
                                        numValueOf = Integer.valueOf(R.string.callFailed_already_dialing);
                                        break;
                                    case 73:
                                        numValueOf = Integer.valueOf(R.string.callFailed_already_ringing);
                                        break;
                                    case 74:
                                        numValueOf = Integer.valueOf(R.string.callFailed_calling_disabled);
                                        break;
                                    case 75:
                                        numValueOf = Integer.valueOf(R.string.callFailed_too_many_calls);
                                        break;
                                    case 76:
                                        numValueOf = Integer.valueOf(R.string.callFailed_otasp_provisioning_in_process);
                                        break;
                                    case MiRilHookBase.EVENT_OEMHOOK_XIAOMI_MODEM_HAL_CHIP_ID_GET /* 82 */:
                                        numValueOf = getSatelliteErrorString();
                                        break;
                                    case 1500:
                                        break;
                                    case 1501:
                                        numValueOf = Integer.valueOf(R.string.cause_bad_request);
                                        break;
                                    case 1502:
                                        numValueOf = Integer.valueOf(R.string.cause_unauthorized);
                                        break;
                                    case 1503:
                                        numValueOf = Integer.valueOf(R.string.cause_payment_required);
                                        break;
                                    case 1504:
                                        numValueOf = Integer.valueOf(R.string.cause_forbidden);
                                        break;
                                    case 1505:
                                        numValueOf = Integer.valueOf(R.string.cause_not_found);
                                        break;
                                    case 1506:
                                        numValueOf = Integer.valueOf(R.string.cause_method_not_allowed);
                                        break;
                                    case 1507:
                                        numValueOf = Integer.valueOf(R.string.cause_not_acceptable);
                                        break;
                                    case 1508:
                                        numValueOf = Integer.valueOf(R.string.cause_proxy_authentication_required);
                                        break;
                                    case 1509:
                                        numValueOf = Integer.valueOf(R.string.cause_request_timeout);
                                        break;
                                    case 1510:
                                        numValueOf = Integer.valueOf(R.string.cause_conflict);
                                        break;
                                    case 1511:
                                        numValueOf = Integer.valueOf(R.string.cause_gone);
                                        break;
                                    case 1512:
                                        numValueOf = Integer.valueOf(R.string.cause_length_required);
                                        break;
                                    case 1513:
                                        numValueOf = Integer.valueOf(R.string.cause_request_entry_too_long);
                                        break;
                                    case 1514:
                                        numValueOf = Integer.valueOf(R.string.cause_request_uri_too_long);
                                        break;
                                    case 1515:
                                        numValueOf = Integer.valueOf(R.string.cause_unsupported_media_type);
                                        break;
                                    case 1516:
                                        numValueOf = Integer.valueOf(R.string.cause_unsupported_uri_scheme);
                                        break;
                                    case 1517:
                                        numValueOf = Integer.valueOf(R.string.cause_bad_extension);
                                        break;
                                    case 1518:
                                        numValueOf = Integer.valueOf(R.string.cause_extension_required);
                                        break;
                                    case 1519:
                                        numValueOf = Integer.valueOf(R.string.cause_interval_too_brief);
                                        break;
                                    case 1520:
                                        numValueOf = Integer.valueOf(R.string.cause_temporarily_unavailable);
                                        break;
                                    case 1521:
                                        numValueOf = Integer.valueOf(R.string.cause_call_transaction_not_exist);
                                        break;
                                    case 1522:
                                        numValueOf = Integer.valueOf(R.string.cause_loop_detected);
                                        break;
                                    case 1523:
                                        numValueOf = Integer.valueOf(R.string.cause_too_many_hops);
                                        break;
                                    case 1524:
                                        numValueOf = Integer.valueOf(R.string.cause_address_incomplete);
                                        break;
                                    case 1525:
                                        numValueOf = Integer.valueOf(R.string.cause_ambiguous);
                                        break;
                                    case 1526:
                                        numValueOf = Integer.valueOf(R.string.cause_busy_here);
                                        break;
                                    case 1527:
                                        numValueOf = Integer.valueOf(R.string.cause_request_terminated);
                                        break;
                                    case 1528:
                                        numValueOf = Integer.valueOf(R.string.cause_not_acceptable_here);
                                        break;
                                    case 1529:
                                        numValueOf = Integer.valueOf(R.string.cause_server_internal_error);
                                        break;
                                    case 1530:
                                        numValueOf = Integer.valueOf(R.string.cause_not_implemented);
                                        break;
                                    case 1531:
                                        numValueOf = Integer.valueOf(R.string.cause_bad_gateway);
                                        break;
                                    case 1532:
                                        numValueOf = Integer.valueOf(R.string.cause_service_unavailable);
                                        break;
                                    case 1533:
                                        numValueOf = Integer.valueOf(R.string.cause_gateway_timeout);
                                        break;
                                    case 1534:
                                        numValueOf = Integer.valueOf(R.string.cause_version_not_supported);
                                        break;
                                    case 1535:
                                        numValueOf = Integer.valueOf(R.string.cause_message_too_long);
                                        break;
                                    case 1536:
                                        numValueOf = Integer.valueOf(R.string.cause_busy_everywhere);
                                        break;
                                    case 1537:
                                        numValueOf = Integer.valueOf(R.string.cause_decline);
                                        break;
                                    case 1538:
                                        numValueOf = Integer.valueOf(R.string.cause_does_not_exist_anywhere);
                                        break;
                                    case 1539:
                                        numValueOf = Integer.valueOf(R.string.cause_session_not_acceptable);
                                        break;
                                    default:
                                        switch (i) {
                                            case 36:
                                                if (ImsUtil.shouldPromoteWfc(context, i2)) {
                                                    numValueOf = Integer.valueOf(R.string.incall_error_promote_wfc);
                                                }
                                                break;
                                            case 37:
                                                numValueOf = Integer.valueOf(R.string.incall_error_emergency_only);
                                                break;
                                            case 38:
                                                numValueOf = Integer.valueOf(R.string.incall_error_no_phone_number_supplied);
                                                break;
                                            default:
                                                switch (i) {
                                                    case 46:
                                                        numValueOf = Integer.valueOf(R.string.callFailed_dialToUssd);
                                                        break;
                                                    case ModemServiceMoniter.EVENT_OEMHOOK_CALL_STATUS_NOTIFY /* 47 */:
                                                        numValueOf = Integer.valueOf(R.string.callFailed_dialToSs);
                                                        break;
                                                    case ModemServiceMoniter.EVENT_OEMHOOK_DDS_CHANGE_NOTIFY /* 48 */:
                                                        numValueOf = Integer.valueOf(R.string.callFailed_dialToDial);
                                                        break;
                                                    case 49:
                                                        numValueOf = Integer.valueOf(R.string.callFailed_cdma_activation);
                                                        break;
                                                    case 50:
                                                        numValueOf = Integer.valueOf(R.string.callFailed_video_call_tty_enabled);
                                                        break;
                                                    case 51:
                                                        numValueOf = Integer.valueOf(R.string.callEnded_pulled);
                                                        break;
                                                    default:
                                                        switch (i) {
                                                            case 53:
                                                                numValueOf = Integer.valueOf(R.string.callFailed_maximum_reached);
                                                                break;
                                                            case 54:
                                                                numValueOf = Integer.valueOf(R.string.callFailed_data_disabled);
                                                                break;
                                                            case 55:
                                                                numValueOf = Integer.valueOf(R.string.callFailed_data_limit_reached_description);
                                                                break;
                                                            default:
                                                                switch (i) {
                                                                    case 57:
                                                                        numValueOf = Integer.valueOf(android.R.string.permdesc_readSyncSettings);
                                                                        break;
                                                                    case 58:
                                                                        numValueOf = Integer.valueOf(R.string.callFailed_imei_not_accepted);
                                                                        break;
                                                                    case 59:
                                                                        numValueOf = Integer.valueOf(R.string.callFailed_wifi_lost);
                                                                        break;
                                                                    case 60:
                                                                        numValueOf = Integer.valueOf(R.string.callFailed_NetworkCongested);
                                                                        break;
                                                                    case 61:
                                                                        numValueOf = Integer.valueOf(R.string.callFailed_low_battery);
                                                                        break;
                                                                    case MiRilHookBase.EVENT_OEMHOOK_MODEM_STATS_DATA_GET /* 62 */:
                                                                        numValueOf = Integer.valueOf(R.string.dialFailed_low_battery);
                                                                        break;
                                                                    default:
                                                                        switch (i) {
                                                                            case 400:
                                                                                numValueOf = Integer.valueOf(R.string.wfc_wifi_call_drop);
                                                                                break;
                                                                            case a.b /* 401 */:
                                                                                numValueOf = Integer.valueOf(R.string.wfc_internet_connection_lost);
                                                                                break;
                                                                            case 402:
                                                                                numValueOf = Integer.valueOf(R.string.wfc_wifi_call_drop);
                                                                                break;
                                                                            case 403:
                                                                                numValueOf = Integer.valueOf(R.string.wfc_no_network);
                                                                                break;
                                                                        }
                                                                        break;
                                                                }
                                                                break;
                                                        }
                                                        break;
                                                }
                                                break;
                                        }
                                        break;
                                }
                                break;
                        }
                    } else {
                        int i4 = R.string.accessibility_settings_activity_title;
                    }
                    numValueOf = Integer.valueOf(R.string.cause_moved_permanently);
                }
            }
        } else {
            numValueOf = Integer.valueOf(R.string.incall_error_call_failed);
        }
        Integer telecomDisconnectCauseDescription = MiTelephonyServiceStub.get().toTelecomDisconnectCauseDescription(context, i, i2, numValueOf);
        if (telecomDisconnectCauseDescription == null) {
            return "";
        }
        return context.getResources().getString(telecomDisconnectCauseDescription.intValue());
    }

    private static boolean isRadioOffForThermalMitigation(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return false;
        }
        return phone.isRadioOffForThermalMitigation();
    }

    private static String toTelecomDisconnectReason(Context context, int i, String str, int i2) {
        if (context == null) {
            return "";
        }
        if (i == 17 || i == 18 || i == 36) {
            if (ImsUtil.shouldPromoteWfc(context, i2)) {
                return "REASON_WIFI_ON_BUT_WFC_OFF";
            }
        } else {
            if (i == 60) {
                return "REASON_IMS_ACCESS_BLOCKED";
            }
            if (i == 80) {
                return "REASON_EMERGENCY_CALL_PLACED";
            }
            if (i == 82) {
                return str;
            }
        }
        String string = android.telephony.DisconnectCause.toString(i);
        if (str == null) {
            return string;
        }
        return str + ", " + string;
    }

    private static int toTelecomDisconnectCauseTone(int i, PersistableBundle persistableBundle, FlagsAdapter flagsAdapter) {
        if (MiTelephonyServiceStub.get().shouldPlayDisconnectedToneForRingCall(i)) {
            return 27;
        }
        int i2 = 17;
        if (doesCarrierClassifyDisconnectCauseAsBusyCause(i, persistableBundle)) {
            return 17;
        }
        if (i != 4) {
            i2 = 18;
            if (i != 5) {
                if (i == 18) {
                    return 95;
                }
                if (i == 25) {
                    return 21;
                }
                if (i == 45) {
                    return -1;
                }
                switch (i) {
                    case 27:
                        return 95;
                    case DeviceStateListenerBase.TYPE_HORIZONTAL_GAME_LIST_STATE /* 28 */:
                        return 37;
                    case 29:
                        return 38;
                    default:
                        return 27;
                }
            }
        }
        return i2;
    }

    public static boolean doesCarrierClassifyDisconnectCauseAsBusyCause(int i, PersistableBundle persistableBundle) {
        int[] intArray;
        if (persistableBundle == null || (intArray = persistableBundle.getIntArray("disconnect_cause_play_busytone_int_array")) == null) {
            return false;
        }
        for (int i2 : intArray) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public static boolean doesCarrierClassifyDisconnectCauseAsNetworkBusyCause(int i, PersistableBundle persistableBundle) {
        int[] intArray;
        if (persistableBundle != null && (intArray = persistableBundle.getIntArray("disconnect_cause_network_busy_int_array")) != null) {
            for (int i2 : intArray) {
                if (i2 == i) {
                    return true;
                }
            }
        }
        return false;
    }

    private static PersistableBundle getCarrierConfigBundle(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone != null) {
            return PhoneGlobals.getInstance().getCarrierConfigForSubId(phone.getSubId());
        }
        return PhoneGlobals.getInstance().getCarrierConfig();
    }

    protected static boolean is2gDisabled(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            return false;
        }
        return !((phone.getAllowedNetworkTypes(3) & 32843) != 0);
    }

    private static Integer getSatelliteErrorString() {
        if (SatelliteController.getInstance().isSatelliteEnabledOrBeingEnabled()) {
            return Integer.valueOf(R.string.incall_error_satellite_enabled);
        }
        return Integer.valueOf(R.string.incall_error_carrier_roaming_satellite_mode);
    }
}
