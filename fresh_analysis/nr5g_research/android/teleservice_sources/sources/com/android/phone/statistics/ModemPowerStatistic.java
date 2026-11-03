package com.android.phone.statistics;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.ModemProvider;
import com.android.phone.RilHook;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.ATCommandUtils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import com.xiaomi.internal.telephony.MEModuleSimAbnormalMonitor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.os.HyperOSCustFeatureResolve;
import org.json.JSONArray;

/* JADX INFO: loaded from: classes.dex */
public class ModemPowerStatistic {
    private static final String[] MD_START = {"AT+EDMFAPP=20102,7,1", "+EDMFAPP"};
    private static final String[] MD_STOP = {"AT+EDMFAPP=20102,7,0", "+EDMFAPP"};
    private static final int[] MTK_EVENT_IDS = {288, 289, 290, 291};
    private Context mContext;
    private boolean mIsSleepMode;
    private final boolean mSupportModemPowerStatistic;
    private Handler mWorkHandler;
    private HandlerThread mWorkThread;

    public ModemPowerStatistic(Context context) {
        boolean z = HyperOSCustFeatureResolve.getBoolean("config_support_modem_power_statistic", false);
        this.mSupportModemPowerStatistic = z;
        this.mContext = context;
        if (z) {
            HandlerThread handlerThread = new HandlerThread("ModemPowerStatistic", 10);
            this.mWorkThread = handlerThread;
            handlerThread.start();
            this.mWorkHandler = new Handler(this.mWorkThread.getLooper());
        }
    }

    public void onSleepModeChange(final boolean z) {
        if (this.mSupportModemPowerStatistic && this.mIsSleepMode != z) {
            this.mIsSleepMode = z;
            this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.statistics.ModemPowerStatistic$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSleepModeChange$0(z);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: reportModemPowerRrcInfo, reason: merged with bridge method [inline-methods] */
    public void lambda$onSleepModeChange$0(boolean z) {
        Utils.logI("ModemPowerStatistic", "reportModemPowerRrcInfo enter");
        if (ModemProvider.IS_QCOM) {
            reportQcomModemPowerRrcInfo(z);
        } else if (ModemProvider.IS_MTK || ModemProvider.IS_XRING) {
            reportMtkModemPowerRrcInfo(z);
        }
    }

    private void reportQcomModemPowerRrcInfo(boolean z) {
        RilHook rilHook = RilHook.get(this.mContext);
        if (rilHook == null) {
            Utils.logE("ModemPowerStatistic", "reportQcomModemPowerRrcInfo rilhook is null");
            return;
        }
        Utils.logI("ModemPowerStatistic", "reportQcomModemPowerRrcInfo: phoneCount=" + MiuiPhoneUtils.PHONE_COUNT);
        for (int i = 0; i < MiuiPhoneUtils.PHONE_COUNT; i++) {
            reportQcomNr5gRrcPowerStats(rilHook, i);
            reportQcomLteRrcPowerStats(rilHook, i);
        }
    }

    private boolean parseLteRrcBuffer(ByteBuffer byteBuffer, int i, Map map, List list) {
        try {
            byte b = byteBuffer.get();
            Utils.logI("ModemPowerStatistic", "parseLteRrcBuffer: slotId=" + i + ", version=" + ((int) b) + ", bufRemaining=" + byteBuffer.remaining());
            if (b == 1) {
                return parseLteRrcPowerStatsV1(byteBuffer, map, list);
            }
            Utils.logW("ModemPowerStatistic", "parseLteRrcBuffer: unsupported version=" + ((int) b) + ", skip");
            return false;
        } catch (Exception e) {
            Utils.logE("ModemPowerStatistic", "parseLteRrcBuffer exception: " + e);
            return false;
        }
    }

    private boolean parseLteRrcPowerStatsV1(ByteBuffer byteBuffer, Map map, List list) {
        short s = byteBuffer.getShort();
        Utils.logI("ModemPowerStatistic", "parseLteRrcPowerStatsV1: count=" + ((int) s) + ", bufRemaining=" + byteBuffer.remaining());
        int i = byteBuffer.getInt();
        int i2 = byteBuffer.getInt();
        int i3 = byteBuffer.getInt();
        long j = byteBuffer.getLong();
        map.put("lte_rrc_version", 1);
        map.put("lte_rrc_count", Integer.valueOf(i));
        map.put("lte_rrc_count_mo", Integer.valueOf(i2));
        map.put("lte_rrc_count_mt", Integer.valueOf(i3));
        map.put("lte_rrc_state_total_time", Long.valueOf(j));
        for (int i4 = 0; i4 < s; i4++) {
            HashMap map2 = new HashMap();
            map2.put("lte_timestamp_ms", Long.valueOf(byteBuffer.getLong()));
            map2.put("lte_freq", Integer.valueOf(byteBuffer.getInt()));
            map2.put("lte_pci", Integer.valueOf(byteBuffer.getShort()));
            map2.put("lte_establishment_cause", Integer.valueOf(byteBuffer.get()));
            map2.put("lte_rrc_state_time", Integer.valueOf(byteBuffer.getInt()));
            map2.put("lte_rrc_establishment_time", Integer.valueOf(byteBuffer.getInt()));
            map2.put("lte_rrc_release_time", Integer.valueOf(byteBuffer.getInt()));
            map2.put("lte_avg_tx_power", Float.valueOf(byteBuffer.getFloat()));
            map2.put("lte_avg_rsrp", Float.valueOf(byteBuffer.getFloat()));
            map2.put("lte_avg_snr", Float.valueOf(byteBuffer.getFloat()));
            map2.put("lte_avg_rsrq", Float.valueOf(byteBuffer.getFloat()));
            map2.put("lte_sleep_time_ratio", Float.valueOf(byteBuffer.getFloat()));
            list.add(map2);
        }
        return true;
    }

    private boolean parseNr5gRrcBuffer(ByteBuffer byteBuffer, int i, Map map, List list) {
        try {
            byte b = byteBuffer.get();
            Utils.logI("ModemPowerStatistic", "parseNr5gRrcBuffer: slotId=" + i + ", version=" + ((int) b) + ", bufRemaining=" + byteBuffer.remaining());
            if (b == 1) {
                return parseNr5gRrcPowerStatsV1(byteBuffer, map, list);
            }
            Utils.logW("ModemPowerStatistic", "parseNr5gRrcBuffer: unsupported version=" + ((int) b) + ", skip");
            return false;
        } catch (Exception e) {
            Utils.logE("ModemPowerStatistic", "parseNr5gRrcBuffer exception: " + e);
            return false;
        }
    }

    private boolean parseNr5gRrcPowerStatsV1(ByteBuffer byteBuffer, Map map, List list) {
        short s = byteBuffer.getShort();
        Utils.logI("ModemPowerStatistic", "parseNr5gRrcPowerStatsV1: count=" + ((int) s) + ", bufRemaining=" + byteBuffer.remaining());
        int i = byteBuffer.getInt();
        int i2 = byteBuffer.getInt();
        int i3 = byteBuffer.getInt();
        long j = byteBuffer.getLong();
        map.put("nr5g_rrc_version", 1);
        map.put("nr5g_rrc_count", Integer.valueOf(i));
        map.put("nr5g_rrc_count_mo", Integer.valueOf(i2));
        map.put("nr5g_rrc_count_mt", Integer.valueOf(i3));
        map.put("nr5g_rrc_state_total_time", Long.valueOf(j));
        for (int i4 = 0; i4 < s; i4++) {
            HashMap map2 = new HashMap();
            map2.put("nr5g_timestamp_ms", Long.valueOf(byteBuffer.getLong()));
            map2.put("nr5g_freq", Integer.valueOf(byteBuffer.getInt()));
            map2.put("nr5g_pci", Integer.valueOf(byteBuffer.getShort()));
            map2.put("nr5g_establishment_cause", Integer.valueOf(byteBuffer.get()));
            map2.put("nr5g_rrc_state_time", Integer.valueOf(byteBuffer.getInt()));
            map2.put("nr5g_rrc_establishment_time", Integer.valueOf(byteBuffer.getInt()));
            map2.put("nr5g_rrc_release_time", Integer.valueOf(byteBuffer.getInt()));
            map2.put("nr5g_avg_tx_power", Float.valueOf(byteBuffer.getFloat()));
            map2.put("nr5g_avg_mtpl", Float.valueOf(byteBuffer.getFloat()));
            map2.put("nr5g_avg_pa_state", Float.valueOf(byteBuffer.getFloat()));
            map2.put("nr5g_avg_rsrp", Float.valueOf(byteBuffer.getFloat()));
            map2.put("nr5g_avg_snr", Float.valueOf(byteBuffer.getFloat()));
            map2.put("nr5g_avg_rsrq", Float.valueOf(byteBuffer.getFloat()));
            map2.put("nr5g_sleep_time_ratio", Float.valueOf(byteBuffer.getFloat()));
            long[] jArr = new long[2];
            for (int i5 = 0; i5 < 2; i5++) {
                jArr[i5] = byteBuffer.getLong();
            }
            map2.put("nr5g_bandwidth_time", Arrays.toString(jArr));
            long[] jArr2 = new long[5];
            for (int i6 = 0; i6 < 5; i6++) {
                jArr2[i6] = byteBuffer.getLong();
            }
            map2.put("nr5g_num_rx_time", Arrays.toString(jArr2));
            long[] jArr3 = new long[4];
            for (int i7 = 0; i7 < 4; i7++) {
                jArr3[i7] = byteBuffer.getLong();
            }
            map2.put("nr5g_bwp_id_time", Arrays.toString(jArr3));
            list.add(map2);
        }
        return true;
    }

    private void reportQcomNr5gRrcPowerStats(RilHook rilHook, int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (int i3 = 0; i3 < 50; i3++) {
            HashMap map = new HashMap();
            ArrayList arrayList2 = new ArrayList();
            ByteBuffer nr5gRrcPowerStats = rilHook.getNr5gRrcPowerStats(i);
            if (nr5gRrcPowerStats == null) {
                Utils.logI("ModemPowerStatistic", "reportQcomNr5gRrcPowerStats: slotId=" + i + ", fetch=" + i3 + ", buf is null, break");
                break;
            }
            if (!parseNr5gRrcBuffer(nr5gRrcPowerStats, i, map, arrayList2) || arrayList2.isEmpty()) {
                Utils.logI("ModemPowerStatistic", "reportQcomNr5gRrcPowerStats: slotId=" + i + ", fetch=" + i3 + ", no new records, break");
                break;
            }
            HashMap map2 = new HashMap(map);
            map2.put("nr5g_rrc_count_data", arrayList2);
            arrayList.add(map2);
            i2++;
            if (i2 % 25 == 0) {
                Utils.logI("ModemPowerStatistic", "reportQcomNr5gRrcPowerStats: slotId=" + i + ", fetchCount=" + i2 + ", batch report");
                doReportNr5gBatch(i, arrayList);
                arrayList.clear();
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        Utils.logI("ModemPowerStatistic", "reportQcomNr5gRrcPowerStats: slotId=" + i + ", fetchCount=" + i2 + ", flush remaining=" + arrayList.size());
        doReportNr5gBatch(i, arrayList);
    }

    private void doReportNr5gBatch(int i, List list) {
        HashMap map = new HashMap();
        map.put("nr5g_rrc_data", new JSONArray((Collection) list).toString());
        map.put("slotId", Integer.valueOf(i));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "modem_power_rrc_info", map);
    }

    private void reportQcomLteRrcPowerStats(RilHook rilHook, int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        for (int i3 = 0; i3 < 50; i3++) {
            HashMap map = new HashMap();
            ArrayList arrayList2 = new ArrayList();
            ByteBuffer lteRrcPowerStats = rilHook.getLteRrcPowerStats(i);
            if (lteRrcPowerStats == null) {
                Utils.logI("ModemPowerStatistic", "reportQcomLteRrcPowerStats: slotId=" + i + ", fetch=" + i3 + ", buf is null, break");
                break;
            }
            if (!parseLteRrcBuffer(lteRrcPowerStats, i, map, arrayList2) || arrayList2.isEmpty()) {
                Utils.logI("ModemPowerStatistic", "reportQcomLteRrcPowerStats: slotId=" + i + ", fetch=" + i3 + ", no new records, break");
                break;
            }
            HashMap map2 = new HashMap(map);
            map2.put("lte_rrc_count_data", arrayList2);
            arrayList.add(map2);
            i2++;
            if (i2 % 25 == 0) {
                Utils.logI("ModemPowerStatistic", "reportQcomLteRrcPowerStats: slotId=" + i + ", fetchCount=" + i2 + ", batch report");
                doReportLteBatch(i, arrayList);
                arrayList.clear();
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        Utils.logI("ModemPowerStatistic", "reportQcomLteRrcPowerStats: slotId=" + i + ", fetchCount=" + i2 + ", flush remaining=" + arrayList.size());
        doReportLteBatch(i, arrayList);
    }

    private void doReportLteBatch(int i, List list) {
        HashMap map = new HashMap();
        map.put("lte_rrc_data", new JSONArray((Collection) list).toString());
        map.put("slotId", Integer.valueOf(i));
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "modem_power_rrc_info", map);
    }

    private void reportMtkModemPowerRrcInfo(boolean z) {
        Utils.logI("ModemPowerStatistic", "reportMtkModemPowerRrcInfo enter");
        if (z) {
            if (ATCommandUtils.getInstance() == null) {
                Log.w("ModemPowerStatistic", "reportMtkModemPowerRrcInfo: ATCommandUtils instance is null");
                return;
            } else {
                ATCommandUtils.getInstance().atCmdRunSync(0, MD_START, null);
                return;
            }
        }
        getModemPowerInfos();
    }

    private void getModemPowerInfos() {
        StringBuilder sb = new StringBuilder("getModemPowerInfos: start, eventId size = ");
        int[] iArr = MTK_EVENT_IDS;
        sb.append(iArr.length);
        Log.d("ModemPowerStatistic", sb.toString());
        if (ATCommandUtils.getInstance() == null) {
            Log.w("ModemPowerStatistic", "getModemPowerInfos: ATCommandUtils instance is null");
            return;
        }
        ATCommandUtils.getInstance().atCmdRunSync(0, MD_STOP, null);
        for (int i : iArr) {
            String str = "AT+EDMFAPP=20100," + i;
            Object[] objArr = new Object[1];
            int i2 = 0;
            boolean z = true;
            while (i2 < 8 && z && ATCommandUtils.getInstance().atCmdRunSync(0, new String[]{str, "+EDMFAPP"}, objArr)) {
                String[] strArr = (String[]) objArr[0];
                boolean z2 = strArr != null && strArr.length > 10;
                Log.d("ModemPowerStatistic", "getModemPowerInfos: key = " + i + ", fetch = " + i2 + ", toContinue = " + z2 + ", result = " + Arrays.toString(strArr));
                if (strArr != null) {
                    parseMtkPowerInfoResponse(i, strArr);
                }
                i2++;
                z = z2;
            }
        }
        Log.d("ModemPowerStatistic", "getModemPowerInfos: done");
    }

    private void parseMtkPowerInfoResponse(int i, String[] strArr) {
        Map mtkRecordV4;
        if (strArr != null) {
            if (strArr.length <= 1) {
                return;
            }
            ArrayList arrayList = new ArrayList();
            for (int i2 = 1; i2 < strArr.length; i2++) {
                String str = strArr[i2];
                if (str != null && !str.isEmpty()) {
                    Log.d("ModemPowerStatistic", "parseMtkPowerInfoResponse: key = " + i + ", idx = " + i2 + ", resp = " + str);
                    String strStripEdmfappHeader = stripEdmfappHeader(str);
                    if (strStripEdmfappHeader != null && !strStripEdmfappHeader.isEmpty()) {
                        String[] strArrSplit = strStripEdmfappHeader.split(",");
                        if (strArrSplit.length != 0) {
                            try {
                                int i3 = Integer.parseInt(strArrSplit[0].trim());
                                if (i3 == 4) {
                                    mtkRecordV4 = parseMtkRecordV4(i, strArrSplit);
                                } else {
                                    Log.w("ModemPowerStatistic", "parseMtkPowerInfoResponse: unsupported version_m=" + i3 + ", key=" + i + ", skip");
                                    mtkRecordV4 = null;
                                }
                                if (mtkRecordV4 != null) {
                                    arrayList.add(mtkRecordV4);
                                }
                            } catch (Exception e) {
                                Log.e("ModemPowerStatistic", "parseMtkPowerInfoResponse: parse error, key=" + i, e);
                            }
                        }
                    }
                }
            }
            if (arrayList.isEmpty()) {
                return;
            }
            HashMap map = new HashMap();
            map.put("version_m", 4);
            map.put("event_id", Integer.valueOf(i));
            map.put("record_data", new JSONArray((Collection) arrayList).toString());
            Log.d("ModemPowerStatistic", "MTK report: event_id=" + i + ", recordCount=" + arrayList.size() + ", dataKeys=" + map.keySet());
            MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "modem_power_rrc_info", map);
        }
    }

    private String stripEdmfappHeader(String str) {
        if (str == null) {
            return null;
        }
        String strTrim = str.trim();
        int i = strTrim.startsWith("+EDMFAPP:") ? 9 : -1;
        if (i < 0) {
            Log.w("ModemPowerStatistic", "stripEdmfappHeader: line does not start with +EDMFAPP:, line=".concat(str));
            return strTrim;
        }
        while (i < strTrim.length() && strTrim.charAt(i) == ' ') {
            i++;
        }
        String strSubstring = strTrim.substring(i);
        if (!strSubstring.startsWith("20100,")) {
            Log.d("ModemPowerStatistic", "stripEdmfappHeader: header id mismatch, afterPrefix=".concat(strSubstring));
            return strSubstring;
        }
        return strSubstring.substring(6);
    }

    private Map parseMtkRecordV4(int i, String[] strArr) {
        switch (i) {
            case 288:
                return parseMtkNrParam1V4(strArr);
            case 289:
                return parseMtkLteParam1V4(strArr);
            case 290:
                return parseMtkNrParam2V4(strArr);
            case 291:
                return parseMtkLteParam2V4(strArr);
            default:
                Log.w("ModemPowerStatistic", "parseMtkRecordV4: unknown key=" + i);
                return null;
        }
    }

    private Map parseMtkNrParam1V4(String[] strArr) {
        if (strArr.length < 6) {
            Log.w("ModemPowerStatistic", "parseMtkNrParam1V4: fields too short, len=" + strArr.length);
            return null;
        }
        HashMap map = new HashMap();
        map.put("version_m", Integer.valueOf(Integer.parseInt(strArr[0].trim())));
        map.put("cur_time", Long.valueOf(Long.parseLong(strArr[1].trim())));
        map.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, Integer.valueOf(Integer.parseInt(strArr[2].trim())));
        map.put("nr_mo_setup_cnt", Integer.valueOf(Integer.parseInt(strArr[3].trim())));
        map.put("nr_mt_setup_cnt", Integer.valueOf(Integer.parseInt(strArr[4].trim())));
        map.put("nrrc_connect_time", Long.valueOf(Long.parseLong(strArr[5].trim())));
        return map;
    }

    private Map parseMtkLteParam1V4(String[] strArr) {
        if (strArr.length < 6) {
            Log.w("ModemPowerStatistic", "parseMtkLteParam1V4: fields too short, len=" + strArr.length);
            return null;
        }
        HashMap map = new HashMap();
        map.put("version_m", Integer.valueOf(Integer.parseInt(strArr[0].trim())));
        map.put("cur_time", Long.valueOf(Long.parseLong(strArr[1].trim())));
        map.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, Integer.valueOf(Integer.parseInt(strArr[2].trim())));
        map.put("lte_mo_setup_cnt", Integer.valueOf(Integer.parseInt(strArr[3].trim())));
        map.put("lte_mt_setup_cnt", Integer.valueOf(Integer.parseInt(strArr[4].trim())));
        map.put("errc_connect_time", Long.valueOf(Long.parseLong(strArr[5].trim())));
        return map;
    }

    private Map parseMtkNrParam2V4(String[] strArr) {
        if (strArr.length < 18) {
            Log.w("ModemPowerStatistic", "parseMtkNrParam2V4: fields too short, len=" + strArr.length);
            return null;
        }
        HashMap map = new HashMap();
        map.put("version_m", Integer.valueOf(Integer.parseInt(strArr[0].trim())));
        map.put("cur_time", Long.valueOf(Long.parseLong(strArr[1].trim())));
        map.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, Integer.valueOf(Integer.parseInt(strArr[2].trim())));
        map.put("nr_scs", Integer.valueOf(Integer.parseInt(strArr[3].trim())));
        map.put("nr_avg_ul_duty_1", Integer.valueOf(Integer.parseInt(strArr[4].trim())));
        map.put("nr_avg_ul_duty_2", Integer.valueOf(Integer.parseInt(strArr[5].trim())));
        map.put("nr_dl_bandwidth_1", Integer.valueOf(Integer.parseInt(strArr[6].trim())));
        map.put("nr_dl_bandwidth_ratio_1", Integer.valueOf(Integer.parseInt(strArr[7].trim())));
        map.put("nr_dl_bandwidth_2", Integer.valueOf(Integer.parseInt(strArr[8].trim())));
        map.put("nr_dl_bandwidth_ratio_2", Integer.valueOf(Integer.parseInt(strArr[9].trim())));
        map.put("nr_avg_rsrp", Integer.valueOf(Integer.parseInt(strArr[10].trim())));
        map.put("nr_avg_sinr", Integer.valueOf(Integer.parseInt(strArr[11].trim())));
        map.put("nr_avg_pusch_tx_power", Integer.valueOf(Integer.parseInt(strArr[12].trim())));
        map.put("nr_avg_pucch_tx_power", Integer.valueOf(Integer.parseInt(strArr[13].trim())));
        map.put("nr_avg_srs_tx_power", Integer.valueOf(Integer.parseInt(strArr[14].trim())));
        map.put("nr_avg_ddpc_report", Integer.valueOf(Integer.parseInt(strArr[15].trim())));
        map.put("nr_cdrx_inactive_time", Long.valueOf(Long.parseLong(strArr[16].trim())));
        map.put("nr_cid", Long.valueOf(Long.parseLong(strArr[17].trim())));
        return map;
    }

    private Map parseMtkLteParam2V4(String[] strArr) {
        if (strArr.length < 17) {
            Log.w("ModemPowerStatistic", "parseMtkLteParam2V4: fields too short, len=" + strArr.length);
            return null;
        }
        HashMap map = new HashMap();
        map.put("version_m", Integer.valueOf(Integer.parseInt(strArr[0].trim())));
        map.put("cur_time", Long.valueOf(Long.parseLong(strArr[1].trim())));
        map.put(MEModuleSimAbnormalMonitor.EXTRA_SLOT_ID, Integer.valueOf(Integer.parseInt(strArr[2].trim())));
        map.put("lte_avg_ul_duty_1", Integer.valueOf(Integer.parseInt(strArr[3].trim())));
        map.put("lte_avg_ul_duty_2", Integer.valueOf(Integer.parseInt(strArr[4].trim())));
        map.put("lte_dl_bandwidth_1", Integer.valueOf(Integer.parseInt(strArr[5].trim())));
        map.put("lte_dl_bandwidth_ratio_1", Integer.valueOf(Integer.parseInt(strArr[6].trim())));
        map.put("lte_dl_bandwidth_2", Integer.valueOf(Integer.parseInt(strArr[7].trim())));
        map.put("lte_dl_bandwidth_ratio_2", Integer.valueOf(Integer.parseInt(strArr[8].trim())));
        map.put("lte_avg_rsrp", Integer.valueOf(Integer.parseInt(strArr[9].trim())));
        map.put("lte_avg_sinr", Integer.valueOf(Integer.parseInt(strArr[10].trim())));
        map.put("lte_avg_pusch_tx_power", Integer.valueOf(Integer.parseInt(strArr[11].trim())));
        map.put("lte_avg_pucch_tx_power", Integer.valueOf(Integer.parseInt(strArr[12].trim())));
        map.put("lte_avg_srs_tx_power", Integer.valueOf(Integer.parseInt(strArr[13].trim())));
        map.put("lte_avg_pd_pwr", Integer.valueOf(Integer.parseInt(strArr[14].trim())));
        map.put("lte_cdrx_inactive_time", Long.valueOf(Long.parseLong(strArr[15].trim())));
        map.put("lte_cid", Long.valueOf(Long.parseLong(strArr[16].trim())));
        return map;
    }
}
