package com.android.phone.statistics;

import android.content.Context;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import com.android.ims.ImsException;
import com.android.internal.telephony.CommandException;
import com.android.internal.telephony.MmiCode;
import com.android.internal.telephony.Phone;
import com.android.phone.ModuleDelegate;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.statistics.utils.Utils;
import com.android.phone.utils.MiStatInterfaceUtil;
import com.xiaomi.internal.telephony.MEModuleSignalT1s;
import java.util.HashMap;

/* JADX INFO: loaded from: classes.dex */
public class MiuiSuppServiceStatistic {
    private static MiuiSuppServiceStatistic sInstance;
    private Context mContext;

    private MiuiSuppServiceStatistic(Context context) {
        this.mContext = context;
        Rlog.d("MiuiSuppServiceStatistic", "MiuiSuppServiceStatistic init");
    }

    public static MiuiSuppServiceStatistic getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MiuiSuppServiceStatistic(context);
        }
        return sInstance;
    }

    public void recordCountClickScene(int i, int i2, Phone phone) {
        ServiceState serviceState;
        if (phone == null || (serviceState = phone.getServiceState()) == null) {
            return;
        }
        HashMap map = new HashMap();
        map.put("supp_scene", String.valueOf(i2 + i));
        map.put("exception_code", null);
        map.put("Network", serviceState.getOperatorNumeric());
        map.put("operator", phone.getOperatorNumeric());
        if (this.mContext != null && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, phone.getPhoneId())) {
            map.put("rat", "Voicelink");
            map.put("voiceRat", "Voicelink");
        } else {
            map.put("rat", ServiceState.rilRadioTechnologyToString(serviceState.getRilDataRadioTechnology()));
            map.put("voiceRat", ServiceState.rilRadioTechnologyToString(serviceState.getRilVoiceRadioTechnology()));
        }
        map.put("supp_type", "click");
        Utils.logD("MiuiSuppServiceStatistic", "recordCountClickScene params= " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_supp_service", map);
    }

    public void recordExceptionScene(int i, int i2, Throwable th, Phone phone) {
        ServiceState serviceState;
        int exceptionCode = getExceptionCode(th);
        if (exceptionCode == -1 || phone == null || (serviceState = phone.getServiceState()) == null) {
            return;
        }
        HashMap map = new HashMap();
        map.put("supp_scene", String.valueOf(i2 + i));
        map.put("exception_code", String.valueOf(exceptionCode));
        map.put("Network", serviceState.getOperatorNumeric());
        map.put("operator", phone.getOperatorNumeric());
        if (this.mContext != null && ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isVoiceLinkRegistered(this.mContext, phone.getPhoneId())) {
            map.put("rat", "Voicelink");
            map.put("voiceRat", "Voicelink");
        } else {
            map.put("rat", ServiceState.rilRadioTechnologyToString(serviceState.getRilDataRadioTechnology()));
            map.put("voiceRat", ServiceState.rilRadioTechnologyToString(serviceState.getRilVoiceRadioTechnology()));
        }
        map.put("supp_type", "exception");
        Utils.logD("MiuiSuppServiceStatistic", "recordExceptionScene params= " + map);
        MiStatInterfaceUtil.recordObjectEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_supp_service", map);
    }

    public void recordMmiExceptionScene(MmiCode mmiCode) {
        if (mmiCode == null || mmiCode.getState() != MmiCode.State.FAILED) {
            return;
        }
        MiStatInterfaceUtil.recordCountEvent(MEModuleSignalT1s.ME_STATISTIC_CATEGORY, "event_mmi_exception", null);
    }

    private int getExceptionCode(Throwable th) {
        if (th instanceof CommandException) {
            return ((CommandException) th).getCommandError().ordinal();
        }
        if (th instanceof ImsException) {
            return ((ImsException) th).getCode();
        }
        return -1;
    }
}
