package com.android.phone;

import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.phone.utils.ATCommandUtils;

/* JADX INFO: loaded from: classes.dex */
class NoSimPowerExecutor {
    void applyNoSimSleepSearchPolicy(Phone phone) {
    }

    void restoreNoSimSleepSearchPolicy(Phone phone) {
    }

    void setNoSimRecoveryEnabled(Phone phone, boolean z) {
    }

    void setNoSimSkipImsEccSearch(Phone phone, boolean z) {
    }

    NoSimPowerExecutor() {
    }

    static NoSimPowerExecutor make() {
        if (ModemProvider.IS_MTK) {
            return new MtkNoSimPowerExecutor();
        }
        return new NoSimPowerExecutor();
    }

    protected Phone resolvePhone(Phone phone) {
        return phone != null ? phone : PhoneFactory.getDefaultPhone();
    }

    class MtkNoSimPowerExecutor extends NoSimPowerExecutor {
        private MtkNoSimPowerExecutor() {
        }

        @Override // com.android.phone.NoSimPowerExecutor
        void applyNoSimSleepSearchPolicy(Phone phone) {
            sendAtCommands(resolvePhone(phone), "AT+ERSCFG=120,300,300,300,300,600,600,600,600,1800,1800,1800,1800,3600,3600,600,600,1800,1800,1800,3600,3600,3600", "AT+ESRVREC= 2,7200,100,1, 180, 600, 180, 600", "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_STOP_PERIODIC_LIMITED_RECOVERY_SEARCH\", 1");
        }

        @Override // com.android.phone.NoSimPowerExecutor
        void restoreNoSimSleepSearchPolicy(Phone phone) {
            sendAtCommands(resolvePhone(phone), "AT+ERSCFG=20,20,60,60,60,60,90,90,90,90,90,90,180,180,180,180,180,180,360,360,360,360,360", "AT+ESRVREC= 5,200,30,0, 600, 180,600,180", "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_STOP_PERIODIC_LIMITED_RECOVERY_SEARCH\", 0");
        }

        @Override // com.android.phone.NoSimPowerExecutor
        void setNoSimRecoveryEnabled(Phone phone, boolean z) {
            sendAtCommands(resolvePhone(phone), z ? "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_STOP_PERIODIC_LIMITED_RECOVERY_SEARCH\", 1" : "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_STOP_PERIODIC_LIMITED_RECOVERY_SEARCH\", 0");
        }

        @Override // com.android.phone.NoSimPowerExecutor
        void setNoSimSkipImsEccSearch(Phone phone, boolean z) {
            sendAtCommands(resolvePhone(phone), z ? "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_SKIP_IMS_ECC_SEARCH\",1" : "AT+ESBP = 5,\"SBP_GMSS_NO_SIM_SKIP_IMS_ECC_SEARCH\",0");
        }

        private void sendAtCommands(Phone phone, String... strArr) {
            for (String str : strArr) {
                sendAtCommand(phone, new String[]{str, ""});
            }
        }

        private void sendAtCommand(Phone phone, String[] strArr) {
            if (phone == null) {
                log("phone is null, skip " + strArr[0]);
            } else {
                log("send AT " + strArr[0]);
                ATCommandUtils.getInstance().sendAtCommand(phone, strArr, (Message) null);
            }
        }

        private static void log(String str) {
            Rlog.d("MtkNoSimPowerExecutor", str);
        }
    }
}
