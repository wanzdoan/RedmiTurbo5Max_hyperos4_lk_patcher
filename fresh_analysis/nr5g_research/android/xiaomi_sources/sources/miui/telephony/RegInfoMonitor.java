package miui.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class RegInfoMonitor {
    private static final String REPORT_CONTROL = "sys.reginfo.control";
    private static final int REPORT_LAC = 1;
    private static final int REPORT_REJECT = 2;
    private static final int RIL_REG_STATE_DENIED = 3;
    private static final int RIL_REG_STATE_DENIED_EMERGENCY_CALL_ENABLED = 13;
    private static final String TAG = "RegInfo";
    private Context mContext;
    private int mReportControl;
    private int mSlotId;
    private static final boolean DEBUG = SystemProperties.getBoolean("persist.fakecell.settings.test", false);
    private static final RegInfoMonitor[] sInstances = new RegInfoMonitor[MiuiTelephony.PHONE_COUNT];
    private RegInfoRecord mRegInfo = new RegInfoRecord();
    private RegInfoRecord mTempRegInfo = new RegInfoRecord();
    private boolean mLacChangedAfterDeny = false;

    private RegInfoMonitor(Context context) {
        this.mContext = context;
    }

    public static void notifyRegInfoUpdate(Phone phone, NetworkRegistrationInfo networkRegState) {
        getInstance(phone.getContext(), phone.getPhoneId()).onRegInfoUpdate(networkRegState);
    }

    public static synchronized RegInfoMonitor getInstance(Context context, int slotId) {
        if (sInstances[slotId] == null) {
            sInstances[slotId] = new RegInfoMonitor(context);
            sInstances[slotId].mSlotId = slotId;
            Rlog.d(TAG, "RegInfoMonitor instance created for slot:" + slotId);
        }
        return sInstances[slotId];
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int[] getLacAndCid(CellIdentity cellIdentity) {
        int[] info = {-1, -1};
        if (cellIdentity == null) {
            return info;
        }
        switch (cellIdentity.getType()) {
            case 1:
                info[0] = ((CellIdentityGsm) cellIdentity).getCid();
                info[1] = ((CellIdentityGsm) cellIdentity).getLac();
                return info;
            case 2:
            default:
                return info;
            case 3:
                info[0] = ((CellIdentityLte) cellIdentity).getCi();
                info[1] = ((CellIdentityLte) cellIdentity).getTac();
                return info;
            case 4:
                info[0] = ((CellIdentityWcdma) cellIdentity).getCid();
                info[1] = ((CellIdentityWcdma) cellIdentity).getLac();
                return info;
            case 5:
                info[0] = ((CellIdentityTdscdma) cellIdentity).getCid();
                info[1] = ((CellIdentityTdscdma) cellIdentity).getLac();
                return info;
        }
    }

    public void onRegInfoUpdate(NetworkRegistrationInfo networkRegState) {
        int i = SystemProperties.getInt(REPORT_CONTROL, 3);
        this.mReportControl = i;
        if (i == 0 || networkRegState == null) {
            return;
        }
        int cid = -1;
        int lac = -1;
        boolean isGsm = false;
        try {
            int[] info = getLacAndCid(networkRegState.getCellIdentity());
            int type = networkRegState.getAccessNetworkTechnology();
            if (info != null && info.length == 2) {
                cid = info[0];
                lac = info[1];
            }
            isGsm = type == 16 || type == 2;
        } catch (NumberFormatException ex) {
            Rlog.d(TAG, "error parsing type: " + ex);
        }
        if (lac == this.mTempRegInfo.mLac && cid != -1 && cid != this.mTempRegInfo.mCid) {
            this.mTempRegInfo.mCid = cid;
            this.mTempRegInfo.mIsGsm = isGsm;
        } else if (lac != this.mTempRegInfo.mLac) {
            this.mTempRegInfo.logLacEnd();
            this.mRegInfo.from(this.mTempRegInfo);
            this.mTempRegInfo.reset();
            this.mTempRegInfo.logLacStart(lac, cid, isGsm);
            this.mLacChangedAfterDeny = true;
            if (lac != -1 && (this.mReportControl & 1) != 0) {
                onValidLacChanged(lac, cid, isGsm);
            }
        }
        int regState = networkRegState.getRegistrationState();
        if ((2 & this.mReportControl) != 0) {
            if (regState == 3 || regState == 13) {
                try {
                    int rejCode = networkRegState.getRejectCause();
                    onRegDenied(rejCode, lac);
                } catch (NumberFormatException ex2) {
                    Rlog.d(TAG, "error parsing rejCode: " + ex2);
                }
            }
        }
    }

    public void onRegDenied(int rejCode, int lac) {
        if (DEBUG) {
            Rlog.d(TAG, "onRegDenied: rejCode = " + rejCode + ", lac = " + lac + ", last lac = " + this.mRegInfo.mLac);
        }
        this.mRegInfo.mRejCode = rejCode;
        this.mRegInfo.mRejTimestamp = System.currentTimeMillis();
        if (rejCode != 0 && this.mLacChangedAfterDeny) {
            Intent intent = new Intent("miui.action.metok.FALSE_STATION");
            intent.putExtra("slot", this.mSlotId);
            intent.putExtra("lac", this.mRegInfo.mLac);
            intent.putExtra("cid", this.mRegInfo.mCid);
            intent.putExtra("startTime", this.mRegInfo.mStartTime);
            intent.putExtra("endTime", this.mRegInfo.mEndTime);
            intent.putExtra("rejCode", this.mRegInfo.mRejCode);
            intent.setPackage("com.xiaomi.joyose");
            this.mContext.sendBroadcast(intent);
            if (DEBUG) {
                Rlog.d(TAG, "network reject on:(" + this.mRegInfo.toString() + ")");
            }
            this.mLacChangedAfterDeny = false;
        }
    }

    public void onValidLacChanged(int lac, int cid, boolean isGsm) {
        Intent intent = new Intent("miui.action.metok.LAC_CHANGED");
        intent.putExtra("slot", this.mSlotId);
        intent.putExtra("lac", lac);
        intent.putExtra("cid", cid);
        intent.putExtra("isGsm", isGsm);
        intent.setPackage("com.xiaomi.joyose");
        this.mContext.sendBroadcast(intent);
        if (DEBUG) {
            Rlog.d(TAG, "broadcast LAC changed, lac = " + lac + ", cid = " + cid + ", isGsm = " + isGsm);
        }
    }

    private static class RegInfoRecord {
        private static final long INVALID_TIME = -10;
        private static final int INVALID_VAL = -10;
        private int mCid;
        private long mEndTime;
        private boolean mIsGsm;
        private int mLac;
        private int mRejCode;
        private long mRejTimestamp;
        private long mStartTime;

        public RegInfoRecord() {
            reset();
        }

        public void logLacStart(int lac, int cid, boolean isGsm) {
            this.mLac = lac;
            this.mCid = cid;
            this.mIsGsm = isGsm;
            this.mStartTime = System.currentTimeMillis();
        }

        public void logLacEnd() {
            this.mEndTime = System.currentTimeMillis();
        }

        public long getLacDuration() {
            return (this.mStartTime == INVALID_TIME || this.mEndTime == INVALID_TIME) ? INVALID_TIME : this.mEndTime - this.mStartTime;
        }

        public void from(RegInfoRecord r) {
            this.mLac = r.mLac;
            this.mCid = r.mCid;
            this.mStartTime = r.mStartTime;
            this.mEndTime = r.mEndTime;
            this.mRejCode = r.mRejCode;
            this.mRejTimestamp = r.mRejTimestamp;
            this.mIsGsm = r.mIsGsm;
        }

        public void reset() {
            this.mLac = INVALID_VAL;
            this.mCid = INVALID_VAL;
            this.mRejCode = INVALID_VAL;
            this.mStartTime = INVALID_TIME;
            this.mEndTime = INVALID_TIME;
            this.mRejTimestamp = INVALID_TIME;
            this.mIsGsm = false;
        }

        public String toString() {
            return String.format("lac = %s, cid = %s, startTime = %s, endTime = %s, regCode = %s,rejTimestamp = %s", Integer.valueOf(this.mLac), Integer.valueOf(this.mCid), Long.valueOf(this.mStartTime), Long.valueOf(this.mEndTime), Integer.valueOf(this.mRejCode), Long.valueOf(this.mRejTimestamp));
        }
    }
}
