package com.android.phone.statistics.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.phone.PhoneGlobals;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;

/* JADX INFO: loaded from: classes.dex */
public class DefaultPreferenceHelper {
    private static volatile DefaultPreferenceHelper sInstance;
    private final int MODE = 0;
    private final SharedPreferences mSp;

    public static DefaultPreferenceHelper getInstance() {
        if (sInstance == null) {
            synchronized (DefaultPreferenceHelper.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new DefaultPreferenceHelper(PhoneGlobals.getInstance(), MEModuleSignalT1s.ME_STATISTIC_CATEGORY);
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private DefaultPreferenceHelper(Context context, String str) {
        this.mSp = context.getSharedPreferences(str, 0);
    }

    public void setStringPreference(String str, String str2) {
        this.mSp.edit().putString(str, str2).apply();
    }

    public void setIntPreference(String str, int i) {
        this.mSp.edit().putInt(str, i).apply();
    }

    public void setLongPref(String str, long j) {
        this.mSp.edit().putLong(str, j).apply();
    }

    public String getStringPreference(String str) {
        return this.mSp.getString(str, "");
    }

    public int getIntPreference(String str) {
        return this.mSp.getInt(str, 0);
    }

    public long getLongPref(String str) {
        return this.mSp.getLong(str, 0L);
    }

    public void setLastUserMdlogTime(Long l) {
        setLongPref("last_update_user_modem_log_time", l.longValue());
    }

    public Long getLastUserMdlogTime() {
        return Long.valueOf(getLongPref("last_update_user_modem_log_time"));
    }

    public void setUserDisagreeCounter(int i) {
        setIntPreference("user_disagree_counter", i);
    }

    public int getUserDisagreeCounter() {
        return getIntPreference("user_disagree_counter");
    }

    public void updateMdlogFromSP() {
        int ruleEvent = getRuleEvent();
        setRuleEvent(ruleEvent);
        Utils.logD("DefaultPreferenceHelper", "updateMdlogFromSP ruleEvent=" + ruleEvent);
        revokeUserPermission();
    }

    public int getRuleEvent() {
        return getIntPreference("rule_event");
    }

    public void setRuleEvent(int i) {
        setIntPreference("rule_event", i);
    }

    public void setRuleEvent(int i, boolean z) {
        int i2 = 1 << i;
        int ruleEvent = getRuleEvent();
        setIntPreference("rule_event", z ? i2 | ruleEvent : (~i2) & ruleEvent);
    }

    public void resetRuleEvent() {
        setIntPreference("rule_event", 0);
    }

    public void revokeUserPermission() {
        if (Utils.isAnyValidRuleEventExist(getRuleEvent()) && Utils.isNeedToRevokeUserPermission(getLastUserMdlogTime().longValue())) {
            Utils.logD("DefaultPreferenceHelper", "revokeUserPermission reset userpermission");
            resetRuleEvent();
            setUserDisagreeCounter(0);
        }
    }

    public void setUserHotPlugCounter(int i) {
        setIntPreference("user_hot_plug_counter", i);
    }

    public int getUserHotPlugCounter() {
        return getIntPreference("user_hot_plug_counter");
    }

    public void setBlockNetworkDuration(String str) {
        setStringPreference("blocked_network_duration", str);
    }

    public void setDdsSsBinDuration(String str) {
        setStringPreference("dds_signal_strength_bin_duration", str);
    }

    public void setNonDdsSsBinDuration(String str) {
        setStringPreference("non_dds_signal_strength_bin_duration", str);
    }

    public void setDdsOosDuration(long j) {
        setLongPref("dds_out_of_service_duration", j);
    }

    public long getDdsOosDuration() {
        return getLongPref("dds_out_of_service_duration");
    }

    public void setNonDdsOosDuration(long j) {
        setLongPref("non_dds_out_of_service_duration", j);
    }

    public long getNonDdsOosDuration() {
        return getLongPref("non_dds_out_of_service_duration");
    }

    public void setDualDataActivateCount(int i) {
        setIntPreference("dual_data_activate_count", i);
    }

    public int getDualDataActivateCount() {
        return getIntPreference("dual_data_activate_count");
    }

    public void setDualDataActivateDurations(String str) {
        setStringPreference("dual_data_activate_duration", str);
    }

    public String getDualDataActivateDurations() {
        return getStringPreference("dual_data_activate_duration");
    }

    public void setDualDataActivatePkgNames(String str) {
        setStringPreference("dual_data_activate_pkgname", str);
    }

    public String getDualDataActivatePkgNames() {
        return getStringPreference("dual_data_activate_pkgname");
    }

    public void setSignalStats(String str) {
        setStringPreference("ue_signal_stats", str);
    }

    public String getSignalStats() {
        return getStringPreference("ue_signal_stats");
    }

    public void setBandStats(String str) {
        setStringPreference("ue_band_stats", str);
    }

    public String getBandStats() {
        return getStringPreference("ue_band_stats");
    }

    public void setArfcnStats(String str) {
        setStringPreference("ue_arfcn_stats", str);
    }

    public String getArfcnStats() {
        return getStringPreference("ue_arfcn_stats");
    }

    public void setGameStats(String str) {
        setStringPreference("ue_game_stats", str);
    }

    public String getGameStats() {
        return getStringPreference("ue_game_stats");
    }

    public void setCallStats(String str) {
        setStringPreference("ue_call_stats", str);
    }

    public String getCallStats() {
        return getStringPreference("ue_call_stats");
    }

    public void setNonDdsStats(String str) {
        setStringPreference("ue_non_dds_stats", str);
    }

    public String getNonDdsStats() {
        return getStringPreference("ue_non_dds_stats");
    }

    public void setRatStats(String str) {
        setStringPreference("ue_rat_stats", str);
    }

    public String getRatStats() {
        return getStringPreference("ue_rat_stats");
    }

    public void setLteNrSignalLossStats(String str) {
        setStringPreference("ue_ltenr_signal_loss_stats", str);
    }

    public String getLteNrSignalLossStats() {
        return getStringPreference("ue_ltenr_signal_loss_stats");
    }

    public void setFiveGStats(String str) {
        setStringPreference("ue_5G_stats", str);
    }

    public String getFiveGStats() {
        return getStringPreference("ue_5G_stats");
    }

    public void setVoWifiStats(String str) {
        setStringPreference("ue_vowifi_stats", str);
    }

    public String getVoWifiStats() {
        return getStringPreference("ue_vowifi_stats");
    }

    public void setVolteStats(String str) {
        setStringPreference("ue_volte_stats", str);
    }

    public String getVolteStats() {
        return getStringPreference("ue_volte_stats");
    }

    public void setWechatStats(String str) {
        setStringPreference("ue_wechat_stats", str);
    }

    public String getWechatStats() {
        return getStringPreference("ue_wechat_stats");
    }

    public void setCallConnectTimeStats(String str) {
        setStringPreference("call_connect_time", str);
    }

    public String getCallConnectTimeStats() {
        return getStringPreference("call_connect_time");
    }

    public void setAirportModeStats(String str) {
        setStringPreference("ue_airport_mode_stats", str);
    }

    public String getAirportModeStats() {
        return getStringPreference("ue_airport_mode_stats");
    }

    public void setSubwayModeStats(String str) {
        setStringPreference("ue_subway_mode_stats", str);
    }

    public void setHikingModeStats(String str) {
        setStringPreference("ue_hiking_mode_stats", str);
    }

    public String getHikingModeStats() {
        return getStringPreference("ue_hiking_mode_stats");
    }

    public void setDriveModeStats(String str) {
        setStringPreference("ue_drive_mode_stats", str);
    }

    public String getDriveModeStats() {
        return getStringPreference("ue_drive_mode_stats");
    }

    public String getQmsTriggerStats() {
        return getStringPreference("ue_qms_trigger_stats");
    }

    public void setQmsTriggerStats(String str) {
        setStringPreference("ue_qms_trigger_stats", str);
    }

    public String getSubwayModeStats() {
        return getStringPreference("ue_subway_mode_stats");
    }

    public void setWifiConnectedTime(int i) {
        setIntPreference("wifi_connected_time", i);
    }

    public int getWifiConnectedTime() {
        return getIntPreference("wifi_connected_time");
    }

    public void setMobileConnectedTime(int i) {
        setIntPreference("mobile_connected_time", i);
    }

    public int getMobileConnectedTime() {
        return getIntPreference("mobile_connected_time");
    }

    public void setRoamingRatStats(String str) {
        setStringPreference("ue_roaming_rat_stats", str);
    }

    public String getRoamingRatStats() {
        return getStringPreference("ue_roaming_rat_stats");
    }

    public void setRoamingBandStats(String str) {
        setStringPreference("ue_roaming_band_stats", str);
    }

    public String getRoamingBandStats() {
        return getStringPreference("ue_roaming_band_stats");
    }

    public void setHikingBandStats(String str) {
        setStringPreference("ue_hiking_band_stats", str);
    }

    public String getHikingBandStats() {
        return getStringPreference("ue_hiking_band_stats");
    }

    public void setDriveBandStats(String str) {
        setStringPreference("ue_drive_band_stats", str);
    }

    public String getDriveBandStats() {
        return getStringPreference("ue_drive_band_stats");
    }

    public void setHstBandStats(String str) {
        setStringPreference("ue_hst_band_stats", str);
    }

    public String getHstBandStats() {
        return getStringPreference("ue_hst_band_stats");
    }

    public void setSubwayBandStats(String str) {
        setStringPreference("ue_subway_band_stats", str);
    }

    public String getSubwayBandStats() {
        return getStringPreference("ue_subway_band_stats");
    }

    public void setRoamingCallStats(String str) {
        setStringPreference("ue_roam_call_stats", str);
    }

    public String getRoamingCallStats() {
        return getStringPreference("ue_roam_call_stats");
    }

    public void setNsaBandStats(String str) {
        setStringPreference("ue_nsa_band_stats", str);
    }

    public String getNsaBandStats() {
        return getStringPreference("ue_nsa_band_stats");
    }

    public void setHstArfcnStats(String str) {
        setStringPreference("ue_hst_arfcn_stats", str);
    }

    public String getHstArfcnStats() {
        return getStringPreference("ue_hst_arfcn_stats");
    }

    public void setNetworkInfoStats(String str) {
        setStringPreference("ue_lost_net_stats", str);
    }

    public String getNetworkInfoStats() {
        return getStringPreference("ue_lost_net_stats");
    }

    public String getFileUploadTriggerStats() {
        return getStringPreference("ue_file_upload_trigger_stats");
    }

    public void setFileUploadTriggerStats(String str) {
        setStringPreference("ue_file_upload_trigger_stats", str);
    }
}
