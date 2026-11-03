package com.android.phone.utils;

/* JADX INFO: loaded from: classes.dex */
public enum NrSceneConstants$Scene {
    UNKNOWN(-1),
    INIT(0),
    SCREEN(1),
    WWAN(2),
    WLAN(3),
    SSR(4),
    UPDATE(5),
    GAME(6),
    SPEED_TEST(7),
    FIVEG_HOSTED_SPEED_MODE(8),
    CALL(9),
    SLM_WLAN(10),
    WHITE_LIST(11),
    SA_CDRX(12),
    SA_WECHAT_VOIP(13),
    SA_NIGHT_MODE(14),
    SA_THERMAL_MODE(15),
    NSA_THERMAL_MODE(16),
    SMART_NETWORK(17),
    POWER_SAVE_MODE(18),
    NETWORK_RECOVERY_MODE(19),
    SMART_TIERED_POWER_SAVING(20),
    IRAT_PINGPONG_MODE(21),
    WECHAT_VIDEO_MTK(22),
    NSA_SCG_POOR_MODE(23),
    SPECIAL_SIM(24),
    SA_SMS(25),
    WIFI_CALL(26),
    NETWORK_RECOVERY_STRATEGY(27),
    SMART_NETWORK_RECOVERY(28),
    RECOVERY_NR(29),
    DEPRI_SA_TEST(30),
    WECHAT_VIDEO_QCOM(31),
    STREAM_MEDIA_MTK(32),
    GAME_RESIDENT_CELL(33),
    NSA_NETWORK_RECOVERY_STRATEGY(34),
    ENDC_TEST(35),
    CALL_FAILED(36);

    private int value;

    NrSceneConstants$Scene(int i) {
        this.value = i;
    }

    public int getValue() {
        return this.value;
    }
}
