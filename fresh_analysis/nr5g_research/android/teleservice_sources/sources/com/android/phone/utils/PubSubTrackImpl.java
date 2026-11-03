package com.android.phone.utils;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import com.ot.pubsub.Configuration;
import com.ot.pubsub.PubSubTrack;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public abstract class PubSubTrackImpl {
    private static PubSubTrack mPubSubTrack;
    private static Set statEventSet = new HashSet();

    public static void init(Context context) {
        PubSubTrack pubSubTrackCreateInstance = PubSubTrack.createInstance(context, new Configuration.Builder().setProjectId("phone-e2c9a").setPrivateKeyId("00287703c68c70bb37cb09b9b5831bad30a5bf15").setAppId("31000000911").build());
        mPubSubTrack = pubSubTrackCreateInstance;
        pubSubTrackCreateInstance.setEventHook(new PubSubTrack.IEventHook() { // from class: com.android.phone.utils.PubSubTrackImpl.1
            @Override // com.ot.pubsub.PubSubTrack.IEventHook
            public boolean isCustomDauEvent(String str) {
                return false;
            }

            @Override // com.ot.pubsub.PubSubTrack.IEventHook
            public boolean isRecommendEvent(String str) {
                return true;
            }
        });
        PubSubTrack.setAccessNetworkEnable(context, isProvisionClauseAgreed(context));
        initStatEventSet();
    }

    private static void initStatEventSet() {
        statEventSet.add("data_setup_call_error");
        statEventSet.add("call_unexpected_disconnected");
        statEventSet.add("call_dial_out_error");
        statEventSet.add("data_fake_connection");
        statEventSet.add("data_fake_connection_time");
        statEventSet.add("nw_out_of_service");
        statEventSet.add("Call");
        statEventSet.add("Rat");
        statEventSet.add("FiveG");
        statEventSet.add("event_registered_volteFailed");
        statEventSet.add("Band");
        statEventSet.add("Signal");
        statEventSet.add("signal_strength_bin_duration");
        statEventSet.add("telephony_statistic_data");
        statEventSet.add("apn_recovery");
        statEventSet.add("event_vowifi");
        statEventSet.add("event_volte");
        statEventSet.add("FiveGNsa");
        statEventSet.add("FiveGSa");
        statEventSet.add("event_nw_search_duration");
        statEventSet.add("event_nw_out_of_service_duration");
        statEventSet.add("event_roaming_band");
        statEventSet.add("event_roaming_rat");
        statEventSet.add("event_fcn");
        statEventSet.add("volte_globalization");
        statEventSet.add("volte_globalization_switch");
        statEventSet.add("event_modem_crash");
        statEventSet.add("event_sim_abnormal");
        statEventSet.add("event_lte_fiveg_signal_loss");
        statEventSet.add("event_send_sms_result");
        statEventSet.add("event_consecutive_data_call_error");
        statEventSet.add("enable_vocie_link");
        statEventSet.add("event_voicelink_scene");
        statEventSet.add("event_fiveg_hosted_button");
        statEventSet.add("event_supp_service");
        statEventSet.add("event_nsa_band");
        statEventSet.add("event_network_connected_time");
        statEventSet.add("unsol_data_call_error");
        statEventSet.add("event_fiveg_offline");
        statEventSet.add("event_lost_network_info");
        statEventSet.add("sim_card_info");
        statEventSet.add("sim_card_lost_error_info");
        statEventSet.add("sim_not_recognized_error_info");
        statEventSet.add("event_deprioritize_or_restore_sa");
        statEventSet.add("event_elevator_mode_statistic");
        statEventSet.add("PingpongCell");
        statEventSet.add("StallData");
        statEventSet.add("BeforeStallData");
        statEventSet.add("TxAntenna");
        statEventSet.add("LteRach");
        statEventSet.add("LteRlf");
        statEventSet.add("NrRach");
        statEventSet.add("NrRlf");
        statEventSet.add("LteNas");
        statEventSet.add("NrNas");
        statEventSet.add("LteBler");
        statEventSet.add("NrBler");
        statEventSet.add("NsaBandComb");
        statEventSet.add("PreferCellInfo");
        statEventSet.add("AirportModeInfo");
        statEventSet.add("BorderModeInfo");
        statEventSet.add("St_Average_Sar_Info");
        statEventSet.add("NrSnsRrcMitigationInfo");
        statEventSet.add("Mbn_Space_Size_Info");
        statEventSet.add("LtePagingError");
        statEventSet.add("NrPagingError");
        statEventSet.add("abnormalTauInfo");
        statEventSet.add("MtAccessFail");
        statEventSet.add("LteSnsDataStall");
        statEventSet.add("NrSnsDataStall");
        statEventSet.add("LteSetDubiousCell");
        statEventSet.add("NrSetDubiousCell");
        statEventSet.add("LteSnsInfo");
        statEventSet.add("NrSnsInfo");
        statEventSet.add("t1s_Info");
        statEventSet.add("alternative_Info");
        statEventSet.add("VoiceQuality_Info");
        statEventSet.add("QcomCallReport");
        statEventSet.add("QcomCallReportEcc");
        statEventSet.add("GameLag");
        statEventSet.add("LteSch");
        statEventSet.add("NrSch");
        statEventSet.add("NrImsSip");
        statEventSet.add("NrScg");
        statEventSet.add("LteTas");
        statEventSet.add("NrTas");
        statEventSet.add("CallDrop");
        statEventSet.add("N1Disable");
        statEventSet.add("LteMeas");
        statEventSet.add("NrMeas");
        statEventSet.add("LteTxPwr");
        statEventSet.add("NrTxPwr");
        statEventSet.add("EndcBand");
        statEventSet.add("El1Tas");
        statEventSet.add("Nl1Tas");
        statEventSet.add("El1Pdcch");
        statEventSet.add("EScellBw");
        statEventSet.add("El1Pucch");
        statEventSet.add("LteMeas2");
        statEventSet.add("NrMeas2");
        statEventSet.add("Nl1Dci");
        statEventSet.add("Nl1PhyConfig");
        statEventSet.add("Nl1DciStats");
        statEventSet.add("Nl1Bw");
        statEventSet.add("userPortrait");
        statEventSet.add("simErrorInfo");
        statEventSet.add("Nl1Fdd");
        statEventSet.add("El1TFdd");
        statEventSet.add("MipiErr");
        statEventSet.add("ImsCallFail");
        statEventSet.add("CallUserProtrait");
        statEventSet.add("CallFailRpt");
        statEventSet.add("NrOos");
        statEventSet.add("LteOos");
        statEventSet.add("PuschPucchSrsTxPwr");
        statEventSet.add("RsrpPuschTxPwr");
        statEventSet.add("LteCaBand");
        statEventSet.add("NrCaBand");
        statEventSet.add("LteCapEnquiry");
        statEventSet.add("NrCapEnquiry");
        statEventSet.add("EpdgDns");
        statEventSet.add("IkeSetup");
        statEventSet.add("IkeSetup1");
        statEventSet.add("CallQuality");
        statEventSet.add("SMSInfo");
        statEventSet.add("ImsRegException");
        statEventSet.add("LteUlModulation");
        statEventSet.add("DropNr");
        statEventSet.add("Oos");
        statEventSet.add("MDCallReport");
        statEventSet.add("PaDetect");
        statEventSet.add("PaDetectV3");
        statEventSet.add("RxSAWlessCount");
        statEventSet.add("RxSAWlessBreakOutCauseCnt");
        statEventSet.add("RxSAWlessDeactivationCauseCnt");
        statEventSet.add("TxPwrPcmax");
        statEventSet.add("AllSceneTxpower");
        statEventSet.add("AllSceneRsrp");
        statEventSet.add("AllSceneSnr");
        statEventSet.add("RsrpStagesTxpower");
        statEventSet.add("NrChannel");
        statEventSet.add("LteChannel");
        statEventSet.add("RsrpStagesSnr");
        statEventSet.add("modemenhance_statistic_tx_fbrx");
        statEventSet.add("LteNrBlerCount");
        statEventSet.add("detect_dialer_ui");
        statEventSet.add("relaunch_incall_service");
        statEventSet.add("incall_service_info_changed");
        statEventSet.add("ril_wake_lock_request");
        statEventSet.add("ims_registration_failed_event");
        statEventSet.add("ims_registration_recovery_event");
        statEventSet.add("event_multi_sim_config_change");
        statEventSet.add("event_multi_sim_config_restore");
        statEventSet.add("event_sim_abnormal_restore");
        statEventSet.add("auto_add_country_code");
        statEventSet.add("auto_correct_country_code");
        statEventSet.add("event_sarsensor_exception");
        statEventSet.add("QcomDsda");
        statEventSet.add("mtkDsda");
        statEventSet.add("call_scene");
        statEventSet.add("call_ntn_info");
        statEventSet.add("event_dsda_to_dsds_remind");
        statEventSet.add("event_update_dsda_sub_mode");
        statEventSet.add("nondds_incoming_in_gamemode");
        statEventSet.add("dsda_ring_and_dial_content");
        statEventSet.add("telephony_call_dsda_optimization_disconnect_scene");
        statEventSet.add("call_time_out");
        statEventSet.add("telephony_epsfb_auto_rejected_call");
        statEventSet.add("esim_event");
        statEventSet.add("event_update_roaming_call_capability");
        statEventSet.add("event_roaming_call");
        statEventSet.add("roaming_oos_optimization");
        statEventSet.add("event_ecc_info");
        statEventSet.add("event_roaming_no_ltenr_event");
        statEventSet.add("event_call_optimization_statistic");
        statEventSet.add("landscape_game_dsi_statistic");
        statEventSet.add("all_scene_dsi_statistic");
        statEventSet.add("single_hand_dsi_statistic");
        statEventSet.add("event_global_saf_statistic");
        statEventSet.add("global_short_video_benefit");
        statEventSet.add("event_global_saf_detail_statistic");
        statEventSet.add("event_global_stream_media_state_statistic");
        statEventSet.add("event_customized_error_alert_dialog");
        statEventSet.add("timezone_detection_with_null_nitz");
    }

    public static void trackObjectEvent(String str, Map map) {
        if (mPubSubTrack == null || !statEventSet.contains(str)) {
            return;
        }
        HashMap map2 = new HashMap();
        if (map != null) {
            for (Map.Entry entry : map.entrySet()) {
                map2.put(SimplifyEventName.simplify((String) entry.getKey()), entry.getValue());
            }
        }
        map2.put("hwLevel", SystemProperties.get("ro.boot.hwlevel", "-1"));
        com.android.phone.statistics.utils.Utils.logD("PubSubTrackImpl", "trackObjectEvent: " + str + ", params: " + map2.toString());
        mPubSubTrack.track("mqs_phone", str, map2);
    }

    private static boolean isProvisionClauseAgreed(Context context) {
        return Settings.Global.getInt(context.getContentResolver(), "clause_agreed", 0) != 0;
    }

    public static void enableDebugMode() {
        PubSubTrack.setDebugMode(true);
    }

    public static void enableOnetrackStatAccessNetwork(Context context) {
        if (mPubSubTrack != null) {
            com.android.phone.statistics.utils.Utils.logD("PubSubTrackImpl", "enableOnetrackStatAccessNetwork: " + isProvisionClauseAgreed(context));
            PubSubTrack.setAccessNetworkEnable(context, isProvisionClauseAgreed(context));
        }
    }

    abstract class SimplifyEventName {
        private static HashMap mappingMap;

        static {
            HashMap map = new HashMap();
            mappingMap = map;
            map.put("Apn", "a");
            mappingMap.put("ApnType", "at");
            mappingMap.put("Band", "b");
            mappingMap.put("CallDirect", "cd");
            mappingMap.put("CellLocation", "cl");
            mappingMap.put("CallType", "ct");
            mappingMap.put("DiconnectCause", "dc");
            mappingMap.put("DeviceName", "dn");
            mappingMap.put("DnsServers", "ds");
            mappingMap.put("ErrorCause", "ec");
            mappingMap.put("FakeDuration", "fd");
            mappingMap.put("Interface", "i");
            mappingMap.put("isDds", "isd");
            mappingMap.put("Mtu", "m");
            mappingMap.put("NrConfigType", "nct");
            mappingMap.put("Network", "nw");
            mappingMap.put("numberType", "nt");
            mappingMap.put("PreferredApn", "pa");
            mappingMap.put("RAT", "r");
            mappingMap.put("RecoveryMethod", "rm");
            mappingMap.put("RecoveryStage", "rs");
            mappingMap.put("SignalQuality", "sq");
            mappingMap.put("SignalStrength", "ss");
            mappingMap.put("ScreenStatus", "scrs");
            mappingMap.put("Trigger", "t");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static String simplify(String str) {
            return (String) mappingMap.getOrDefault(str, str);
        }
    }

    public static String getInstanceId() {
        PubSubTrack pubSubTrack = mPubSubTrack;
        if (pubSubTrack != null) {
            return pubSubTrack.getInstanceId();
        }
        return null;
    }
}
