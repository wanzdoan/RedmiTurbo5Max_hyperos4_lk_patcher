package com.android.phone.statistics;

import android.telephony.Rlog;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.phone.MiuiDsdaStatisticEx;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.ArrayList;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiDsdaStatistic {
    protected static MiuiDsdaStatistic mInstance;

    public abstract String[] getCurrentBands();

    public abstract int getDsdaType();

    public abstract boolean isDsdaFullConcurrencyState();

    public abstract boolean isDsdaFullOrTxsConcurrencyState();

    public abstract void reportStatistics();

    public static MiuiDsdaStatistic getInstance() {
        return mInstance;
    }

    public static void init() {
        if (mInstance == null) {
            synchronized (MiuiDsdaStatistic.class) {
                try {
                    if (mInstance == null) {
                        mInstance = MiuiDsdaStatisticEx.make();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:35:0x00b8  */
    public void recordVideoCallStatistics(Connection connection) {
        boolean zIsVideo;
        boolean zIsVideo2;
        Phone phone = Utils.getPhone(connection);
        if (phone == null) {
            return;
        }
        int phoneId = phone.getPhoneId();
        if (Utils.isValidatePhoneId(phoneId) && Utils.getDsdaUsageScene(phoneId) != -1) {
            int nonDefaultDataSlotId = Utils.getNonDefaultDataSlotId();
            if (phoneId == nonDefaultDataSlotId) {
                zIsVideo = Utils.isVideo(connection.getVideoState());
            } else {
                ImsPhone imsPhone = PhoneFactory.getPhone(nonDefaultDataSlotId).getImsPhone();
                if (imsPhone != null) {
                    ImsPhone imsPhone2 = imsPhone;
                    ImsPhoneCall ringingCall = imsPhone2.getRingingCall();
                    ImsPhoneCall foregroundCall = imsPhone2.getForegroundCall();
                    ImsPhoneCall backgroundCall = imsPhone2.getBackgroundCall();
                    if (ringingCall != null && ringingCall.hasConnections()) {
                        ArrayList connections = ringingCall.getConnections();
                        int size = connections.size();
                        zIsVideo2 = false;
                        int i = 0;
                        while (i < size) {
                            Object obj = connections.get(i);
                            i++;
                            zIsVideo2 |= Utils.isVideo(((Connection) obj).getVideoState());
                        }
                    } else if (foregroundCall != null && foregroundCall.hasConnections()) {
                        ArrayList connections2 = foregroundCall.getConnections();
                        int size2 = connections2.size();
                        zIsVideo2 = false;
                        int i2 = 0;
                        while (i2 < size2) {
                            Object obj2 = connections2.get(i2);
                            i2++;
                            zIsVideo2 |= Utils.isVideo(((Connection) obj2).getVideoState());
                        }
                    } else if (backgroundCall == null || !backgroundCall.hasConnections()) {
                        zIsVideo = false;
                    } else {
                        ArrayList connections3 = backgroundCall.getConnections();
                        int size3 = connections3.size();
                        zIsVideo2 = false;
                        int i3 = 0;
                        while (i3 < size3) {
                            Object obj3 = connections3.get(i3);
                            i3++;
                            zIsVideo2 |= Utils.isVideo(((Connection) obj3).getVideoState());
                        }
                    }
                    zIsVideo = zIsVideo2;
                } else {
                    zIsVideo = false;
                }
            }
            Rlog.d("MiuiDsdaStatistic", "nonDataSlot: " + nonDefaultDataSlotId + " , isVideoCall" + zIsVideo);
            if (zIsVideo) {
                String[] currentBands = mInstance.getCurrentBands();
                HashMap map = new HashMap();
                if (currentBands.length >= 2) {
                    map.put("dsdaBandCombination", currentBands[0] + "_" + currentBands[1]);
                }
                MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_dsda_non_dds_video_call", map);
            }
        }
    }
}
