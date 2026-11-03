package com.android.phone;

import android.content.Context;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class MiuiEmmRejectCauseManager {
    private static String TAG = "MiuiEmmRejectCauseManager";
    private static volatile MiuiEmmRejectCauseManager sInstance;
    private boolean mSlot0CauseChanged;
    private boolean mSlot1CauseChanged;
    private boolean multipleSubscriptions;
    private String mDialogString = "";
    private AlertDialog mEmmCauseDialog = null;
    private int mSlot0CsRejectCause = 0;
    private String mSlot0SimMCCMNC = "";
    private String mSlot0DialogString = "";
    private int mSlot1CsRejectCause = 0;
    private String mSlot1SimMCCMNC = "";
    private String mSlot1DialogString = "";
    private Context mContext = PhoneGlobals.getInstance();

    public static MiuiEmmRejectCauseManager getInstance() {
        if (sInstance == null) {
            synchronized (MiuiEmmRejectCauseManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new MiuiEmmRejectCauseManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    private MiuiEmmRejectCauseManager() {
    }

    public void showRejectCauseDialogIfNeeded(int i, int i2) {
        this.mSlot0CauseChanged = false;
        this.mSlot1CauseChanged = false;
        this.multipleSubscriptions = TelephonyManager.getDefault().getPhoneCount() > 1;
        String simOperatorNumericForPhone = TelephonyManager.getDefault().getSimOperatorNumericForPhone(i);
        if (TextUtils.isEmpty(simOperatorNumericForPhone)) {
            Log.e(TAG, "not handle cause because mccmnc is empty");
            return;
        }
        Log.e(TAG, "SlotId = " + i);
        Log.e(TAG, "CsRejectcause = " + i2);
        Log.e(TAG, "multipleSubscriptions = " + this.multipleSubscriptions);
        Log.e(TAG, "SimMCCMNC = " + simOperatorNumericForPhone);
        if (TelephonyManager.getDefault().getSimState(i) == 1) {
            clearInformationAsCardAbsent(i);
        }
        updateDialogString(i, i2, simOperatorNumericForPhone);
        Log.e(TAG, "mSlot0CsRejectCause = " + this.mSlot0CsRejectCause);
        Log.e(TAG, "mSlot0SimMCCMNC = " + this.mSlot0SimMCCMNC);
        Log.e(TAG, "mSlot0CauseChanged = " + this.mSlot0CauseChanged);
        Log.e(TAG, "mSlot0DialogString = " + this.mSlot0DialogString);
        Log.e(TAG, "mSlot1CsRejectCause = " + this.mSlot1CsRejectCause);
        Log.e(TAG, "mSlot1SimMCCMNC = " + this.mSlot1SimMCCMNC);
        Log.e(TAG, "mSlot1CauseChanged = " + this.mSlot1CauseChanged);
        Log.e(TAG, "mSlot1DialogString = " + this.mSlot1DialogString);
        Log.e(TAG, "mDialogString = " + this.mDialogString);
        String str = this.mDialogString;
        if (str == null || "".equals(str.trim())) {
            dismissGenericErrorDialog();
        } else if (this.mSlot0CauseChanged || this.mSlot1CauseChanged) {
            dismissGenericErrorDialog();
            showGenericErrorDialog(this.mDialogString);
        }
    }

    private void showGenericErrorDialog(CharSequence charSequence) {
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this.mContext, miuix.appcompat.R$style.AlertDialog_Theme_DayNight).setMessage(charSequence).setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener) null).create();
        this.mEmmCauseDialog = alertDialogCreate;
        alertDialogCreate.setCanceledOnTouchOutside(false);
        this.mEmmCauseDialog.getWindow().setType(2003);
        this.mEmmCauseDialog.show();
        this.mSlot0CauseChanged = false;
        this.mSlot1CauseChanged = false;
    }

    private void dismissGenericErrorDialog() {
        if (this.mEmmCauseDialog != null) {
            Log.e(TAG, "dismissGenericErrorDialog = ");
            this.mEmmCauseDialog.dismiss();
            this.mEmmCauseDialog = null;
        }
    }

    private void clearInformationAsCardAbsent(int i) {
        if (i == 0) {
            this.mSlot0CsRejectCause = 0;
            this.mSlot0SimMCCMNC = "";
            this.mSlot0DialogString = "";
            this.mSlot0CauseChanged = false;
            return;
        }
        if (i == 1) {
            this.mSlot1CsRejectCause = 0;
            this.mSlot1SimMCCMNC = "";
            this.mSlot1DialogString = "";
            this.mSlot1CauseChanged = false;
        }
    }

    private void updateDialogString(int i, int i2, String str) {
        String strSelectResourceForCSRejectCodeKrKtLgu;
        if (MiuiKoreaOperatorCustimizedUtils.isCustForKrSkt()) {
            strSelectResourceForCSRejectCodeKrKtLgu = selectResourceForCSRejectCodeKrSkt(this.mContext, i, str, this.multipleSubscriptions, i2);
        } else if (MiuiKoreaOperatorCustimizedUtils.isCustForKrKt()) {
            strSelectResourceForCSRejectCodeKrKtLgu = selectResourceForCSRejectCodeKrKtLte(this.mContext, i, this.multipleSubscriptions, i2);
        } else {
            strSelectResourceForCSRejectCodeKrKtLgu = MiuiKoreaOperatorCustimizedUtils.isCustForKrLgu() ? selectResourceForCSRejectCodeKrKtLgu(this.mContext, i, this.multipleSubscriptions, i2) : "";
        }
        Log.e(TAG, "updateDialogString SlotId = " + i);
        Log.e(TAG, "updateDialogString CsRejectcause = " + i2);
        Log.e(TAG, "updateDialogString mSlot0CsRejectCause = " + this.mSlot0CsRejectCause);
        Log.e(TAG, "updateDialogString mSlot1CsRejectCause = " + this.mSlot1CsRejectCause);
        if (i == 0) {
            if (this.mSlot0CsRejectCause != i2) {
                this.mSlot0CsRejectCause = i2;
                this.mSlot0CauseChanged = true;
                this.mSlot0SimMCCMNC = str;
                if (TextUtils.isEmpty(strSelectResourceForCSRejectCodeKrKtLgu) || i2 == 0) {
                    this.mSlot0DialogString = "";
                } else {
                    this.mSlot0DialogString = strSelectResourceForCSRejectCodeKrKtLgu + "(" + i2 + ")";
                }
            } else {
                this.mSlot0CauseChanged = false;
            }
        }
        if (i == 1) {
            if (this.mSlot1CsRejectCause != i2) {
                this.mSlot1CsRejectCause = i2;
                this.mSlot1CauseChanged = true;
                this.mSlot1SimMCCMNC = str;
                if (TextUtils.isEmpty(strSelectResourceForCSRejectCodeKrKtLgu) || i2 == 0) {
                    this.mSlot1DialogString = "";
                } else {
                    this.mSlot1DialogString = strSelectResourceForCSRejectCodeKrKtLgu + "(" + i2 + ")";
                }
            } else {
                this.mSlot1CauseChanged = false;
            }
        }
        this.mDialogString = this.mSlot0DialogString + this.mSlot1DialogString;
    }

    public static String selectResourceForCSRejectCodeKrSkt(Context context, int i, String str, boolean z, int i2) {
        if (TextUtils.equals(str, "") || str.startsWith("450")) {
            if (i2 == 2) {
                if (z) {
                    return i == 0 ? context.getString(R.string.skt_msim_emm_cause_2, 1, 1599, 0, 0, 11) : context.getString(R.string.skt_msim_emm_cause_2, 2, 1599, 0, 0, 11);
                }
                return context.getString(R.string.skt_emm_cause_2, 1599, 0, 0, 11);
            }
            if (i2 == 3) {
                if (z) {
                    return i == 0 ? context.getString(R.string.skt_msim_emm_cause_3, 1, 1599, 0, 0, 11) : context.getString(R.string.skt_msim_emm_cause_3, 2, 1599, 0, 0, 11);
                }
                return context.getString(R.string.skt_emm_cause_3, 1599, 0, 0, 11);
            }
            if (i2 == 6) {
                if (z) {
                    return i == 0 ? context.getString(R.string.skt_msim_emm_cause_6, 1, 1599, 0, 0, 11) : context.getString(R.string.skt_msim_emm_cause_6, 2, 1599, 0, 0, 11);
                }
                return context.getString(R.string.skt_emm_cause_6, 1599, 0, 0, 11);
            }
            if (i2 != 7 && i2 != 8) {
                if (i2 != 22) {
                    switch (i2) {
                        case 11:
                            if (!z) {
                                return context.getString(R.string.emm_cause_11);
                            }
                            if (i == 0) {
                                return context.getString(R.string.msim_emm_cause_11, 1);
                            }
                            return context.getString(R.string.msim_emm_cause_11, 2);
                        case 14:
                            if (!z) {
                                return context.getString(R.string.emm_cause_14);
                            }
                            if (i == 0) {
                                return context.getString(R.string.msim_emm_cause_14, 1);
                            }
                            return context.getString(R.string.msim_emm_cause_14, 2);
                    }
                }
                if (!z) {
                    return context.getString(R.string.skt_emm_cause_12_13_16_17_22);
                }
                if (i == 0) {
                    return context.getString(R.string.skt_msim_emm_cause_12_13_16_17_22, 1);
                }
                return context.getString(R.string.skt_msim_emm_cause_12_13_16_17_22, 2);
            }
            if (z) {
                return i == 0 ? context.getString(R.string.skt_msim_emm_cause_7_8_15, 1, 1599, 0, 0, 11) : context.getString(R.string.skt_msim_emm_cause_7_8_15, 2, 1599, 0, 0, 11);
            }
            return context.getString(R.string.skt_emm_cause_7_8_15, 1599, 0, 0, 11);
        }
        if (i2 == 2) {
            if (!z) {
                return context.getString(R.string.emm_cause_2);
            }
            if (i == 0) {
                return context.getString(R.string.msim_emm_cause_2, 1);
            }
            return context.getString(R.string.msim_emm_cause_2, 2);
        }
        if (i2 == 3) {
            if (!z) {
                return context.getString(R.string.emm_cause_3);
            }
            if (i == 0) {
                return context.getString(R.string.msim_emm_cause_3, 1);
            }
            return context.getString(R.string.msim_emm_cause_3, 2);
        }
        if (i2 == 6) {
            if (!z) {
                return context.getString(R.string.emm_cause_6);
            }
            if (i == 0) {
                return context.getString(R.string.msim_emm_cause_6, 1);
            }
            return context.getString(R.string.msim_emm_cause_6, 2);
        }
        if (i2 != 7 && i2 != 8 && i2 != 22) {
            switch (i2) {
                case 11:
                    if (!z) {
                        return context.getString(R.string.emm_cause_11);
                    }
                    if (i == 0) {
                        return context.getString(R.string.msim_emm_cause_11, 1);
                    }
                    return context.getString(R.string.msim_emm_cause_11, 2);
                case 14:
                    if (!z) {
                        return context.getString(R.string.emm_cause_14);
                    }
                    if (i == 0) {
                        return context.getString(R.string.msim_emm_cause_14, 1);
                    }
                    return context.getString(R.string.msim_emm_cause_14, 2);
            }
        }
        if (z) {
            return i == 0 ? context.getString(R.string.msim_emm_cause_7_8_12_13_15_16_17_22, 1, 82, 2, 6346, 9000) : context.getString(R.string.msim_emm_cause_7_8_12_13_15_16_17_22, 2, 82, 2, 6346, 9000);
        }
        return context.getString(R.string.emm_cause_7_8_12_13_15_16_17_22, 82, 2, 6346, 9000);
        return "";
    }

    public static String selectResourceForCSRejectCodeKrKtLte(Context context, int i, boolean z, int i2) {
        if (i2 == 3) {
            if (!z) {
                return context.getString(R.string.kt_lte_emm_cause_3);
            }
            if (i != 0) {
                return context.getString(R.string.kt_lte_msim_emm_cause_3, 2);
            }
            return context.getString(R.string.kt_lte_msim_emm_cause_3, 1);
        }
        if (i2 == 6) {
            if (!z) {
                return context.getString(R.string.kt_lte_emm_cause_6, 2, 3);
            }
            if (i != 0) {
                return context.getString(R.string.kt_lte_msim_emm_cause_6, 2);
            }
            return context.getString(R.string.kt_lte_msim_emm_cause_6, 1);
        }
        if (i2 == 8) {
            if (!z) {
                return context.getString(R.string.kt_lte_emm_cause_8);
            }
            if (i != 0) {
                return context.getString(R.string.kt_lte_msim_emm_cause_8, 2);
            }
            return context.getString(R.string.kt_lte_msim_emm_cause_8, 1);
        }
        if (i2 == 15) {
            return "";
        }
        if (i2 == 19 || i2 == 22) {
            if (!z) {
                return context.getString(R.string.kt_lte_emm_cause_19_22);
            }
            if (i != 0) {
                return context.getString(R.string.kt_lte_msim_emm_cause_19_22, 2);
            }
            return context.getString(R.string.kt_lte_msim_emm_cause_19_22, 1);
        }
        if (!z) {
            return context.getString(R.string.kt_lte_emm_cause_others);
        }
        if (i != 0) {
            return context.getString(R.string.kt_lte_msim_emm_cause_others, 2);
        }
        return context.getString(R.string.kt_lte_msim_emm_cause_others, 1);
    }

    public static String selectResourceForCSRejectCodeKrKtLgu(Context context, int i, boolean z, int i2) {
        if (i2 == 2) {
            if (!z) {
                return context.getString(R.string.lgu_emm_cause_2, 82, 2, 3416, 7010);
            }
            if (i != 0) {
                return context.getString(R.string.lgu_msim_emm_cause_2, 2, 82, 2, 3416, 7010);
            }
            return context.getString(R.string.lgu_msim_emm_cause_2, 1, 82, 2, 3416, 7010);
        }
        if (i2 == 3) {
            if (!z) {
                return context.getString(R.string.lgu_emm_cause_3, 82, 2, 3416, 7010);
            }
            if (i != 0) {
                return context.getString(R.string.lgu_msim_emm_cause_3, 2, 82, 2, 3416, 7010);
            }
            return context.getString(R.string.lgu_msim_emm_cause_3, 1, 82, 2, 3416, 7010);
        }
        if (i2 == 6) {
            if (!z) {
                return context.getString(R.string.lgu_emm_cause_6, 82, 2, 3416, 7010);
            }
            if (i != 0) {
                return context.getString(R.string.lgu_msim_emm_cause_6, 2, 82, 2, 3416, 7010);
            }
            return context.getString(R.string.lgu_msim_emm_cause_6, 1, 82, 2, 3416, 7010);
        }
        if (i2 != 7) {
            if (i2 == 8) {
                if (!z) {
                    return context.getString(R.string.lgu_emm_cause_8, 82, 2, 3416, 7010);
                }
                if (i != 0) {
                    return context.getString(R.string.lgu_msim_emm_cause_8, 2, 82, 2, 3416, 7010);
                }
                return context.getString(R.string.lgu_msim_emm_cause_8, 1, 82, 2, 3416, 7010);
            }
            if (i2 != 22) {
                switch (i2) {
                    case 11:
                    case 12:
                    case 13:
                    case 15:
                        if (!z) {
                            return context.getString(R.string.lgu_emm_cause_11_12_13_15);
                        }
                        if (i != 0) {
                            return context.getString(R.string.lgu_msim_emm_cause_11_12_13_15, 2);
                        }
                        return context.getString(R.string.lgu_msim_emm_cause_11_12_13_15, 1);
                    case 14:
                        break;
                    case 16:
                    case 17:
                        break;
                    default:
                        if (!z) {
                            return context.getString(R.string.kt_lte_emm_cause_others);
                        }
                        if (i != 0) {
                            return context.getString(R.string.kt_lte_msim_emm_cause_others, 2);
                        }
                        return context.getString(R.string.kt_lte_msim_emm_cause_others, 1);
                }
            }
            if (!z) {
                return context.getString(R.string.lgu_emm_16_17_22);
            }
            if (i != 0) {
                return context.getString(R.string.lgu_msim_emm_cause_16_17_22, 2);
            }
            return context.getString(R.string.lgu_msim_emm_cause_16_17_22, 1);
        }
        if (!z) {
            return context.getString(R.string.lgu_emm_cause_7_14);
        }
        if (i != 0) {
            return context.getString(R.string.lgu_msim_emm_cause_7_14, 2);
        }
        return context.getString(R.string.lgu_msim_emm_cause_7_14, 1);
    }
}
