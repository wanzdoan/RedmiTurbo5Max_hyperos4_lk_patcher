package com.android.phone.dsda.vtcallfeature;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.Rlog;
import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallManager;
import com.android.internal.telephony.Connection;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.imsphone.ImsPhone;
import com.android.internal.telephony.imsphone.ImsPhoneCall;
import com.android.phone.ModemProvider;
import com.android.phone.R;
import com.android.phone.statistics.MiuiDsdaStatistic;
import com.android.phone.statistics.utils.Utils;
import com.android.services.telephony.relay.phonecall.RelayUtils;
import java.util.ArrayList;
import miui.telephony.TelephonyManagerEx;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;

/* JADX INFO: loaded from: classes.dex */
public class TxsDsdaNonDdsVtOptimize {
    private static final String VICE_SLOT_VOLTE_DATA_ENABLED;
    private static SharedPreferences mPreferences;
    private static TxsDsdaNonDdsVtOptimize sInstance;
    private Context mContext;
    private TxsDsdaNonDdsVtOptimizeHandler mHandler;
    private MiuiDsdaStatistic mInstance;
    private AlertDialog mAlertDialog = null;
    int mPreTxConcurrency = -1;
    Call.State mPreFgViceVtCallState = Call.State.IDLE;
    long mStartTimeMilliSeconds = 0;
    long mPreTxBytes = 0;

    static {
        VICE_SLOT_VOLTE_DATA_ENABLED = (ModemProvider.IS_MTK || ModemProvider.IS_XRING) ? "data_service_enabled" : "vice_slot_volte_data_enabled";
    }

    private static TxsDsdaNonDdsVtOptimize getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TxsDsdaNonDdsVtOptimize(context);
        }
        return sInstance;
    }

    public static void init(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mPreferences = defaultSharedPreferences;
        boolean z = defaultSharedPreferences.getBoolean("show_txs_dsda_non_dds_vt_optimize_dialog", true);
        Rlog.d("TxsDsdaNonDdsVtOptimize", " init, showDialog is: " + z);
        if (!z) {
            Rlog.d("TxsDsdaNonDdsVtOptimize", " showDialog = false, quit optimization !");
        }
        getInstance(context);
    }

    private TxsDsdaNonDdsVtOptimize(Context context) {
        this.mInstance = null;
        MiuiDsdaStatistic miuiDsdaStatistic = MiuiDsdaStatistic.getInstance();
        this.mInstance = miuiDsdaStatistic;
        if (miuiDsdaStatistic == null) {
            return;
        }
        this.mContext = context;
        this.mHandler = new TxsDsdaNonDdsVtOptimizeHandler(Looper.myLooper());
        CallManager.getInstance().registerForPreciseCallStateChanged(this.mHandler, EaseManager.EaseStyleDef.PERLIN, (Object) null);
        Rlog.d("TxsDsdaNonDdsVtOptimize", " TxsDsdaNonDdsVtOptimize init !");
    }

    public class TxsDsdaNonDdsVtOptimizeHandler extends Handler {
        public TxsDsdaNonDdsVtOptimizeHandler(Looper looper) {
            super(looper);
            Rlog.d("TxsDsdaNonDdsVtOptimize", " TxsDsdaNonDdsVtOptimizeHandler init !");
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Object obj = message.obj;
            if (obj == null || !(obj instanceof AsyncResult)) {
                Rlog.e("TxsDsdaNonDdsVtOptimize", "handleMessage, msg.obj is null or msg.obj is not an instance of AsyncResult!");
                return;
            }
            Phone phone = (Phone) ((AsyncResult) obj).result;
            if (phone == null) {
                Rlog.e("TxsDsdaNonDdsVtOptimize", " handleMessage, phone == null !");
                return;
            }
            int phoneId = phone.getPhoneId();
            if (message.what != 201) {
                return;
            }
            if (Settings.Global.getInt(TxsDsdaNonDdsVtOptimize.this.mContext.getContentResolver(), "dual_mobile_data", 0) != 0 || Settings.Global.getInt(TxsDsdaNonDdsVtOptimize.this.mContext.getContentResolver(), "smart_dual_sim", 0) != 0 || Settings.Global.getInt(TxsDsdaNonDdsVtOptimize.this.mContext.getContentResolver(), TxsDsdaNonDdsVtOptimize.VICE_SLOT_VOLTE_DATA_ENABLED, 0) != 0) {
                Rlog.d("TxsDsdaNonDdsVtOptimize", " handleMessage, isSmartDualSimEnabled = true, quit optimization !");
                return;
            }
            if (TxsDsdaNonDdsVtOptimize.this.isViceCall(phoneId)) {
                Call.State viceFgCallState = TxsDsdaNonDdsVtOptimize.this.getViceFgCallState(phoneId);
                Call.State state = Call.State.ACTIVE;
                if (viceFgCallState == state) {
                    boolean zIsVtCall = TxsDsdaNonDdsVtOptimize.this.isVtCall(phoneId);
                    TxsDsdaNonDdsVtOptimize txsDsdaNonDdsVtOptimize = TxsDsdaNonDdsVtOptimize.this;
                    if (zIsVtCall) {
                        txsDsdaNonDdsVtOptimize.checkIfNeedVtOptimize();
                    } else if (txsDsdaNonDdsVtOptimize.mPreFgViceVtCallState == state) {
                        txsDsdaNonDdsVtOptimize.cleanFunc();
                        return;
                    }
                }
                if (viceFgCallState == Call.State.IDLE) {
                    TxsDsdaNonDdsVtOptimize.this.startVtOptimize();
                }
            }
        }
    }

    private long calculateAverageUlSpeed(long j, long j2, long j3) {
        long j4 = j2 - j;
        if (j == 0 || j4 <= 0 || j3 <= 0) {
            Rlog.d("TxsDsdaNonDdsVtOptimize", " calculateAverageUlSpeed, There is no Tx data !");
            return 0L;
        }
        long j5 = (long) (((j3 * 1000.0d) / j4) / 1024.0d);
        Rlog.d("TxsDsdaNonDdsVtOptimize", " calculateAverageUlSpeed, averageUlSpeedKbs: " + j5 + "Kbs");
        return j5;
    }

    private void showSmartDualSimEnabledDialog() {
        Context context = this.mContext;
        if (context == null) {
            Rlog.d("TxsDsdaNonDdsVtOptimize", "showSmartDualSimEnabledDialog, mContext  is null !");
            return;
        }
        AlertDialog alertDialogCreate = new AlertDialog.Builder(context, R$style.AlertDialog_Theme_DayNight).setTitle(R.string.smart_dual_sim_available_title).setMessage(R.string.smart_dual_sim_available_message).setCheckBox(false, this.mContext.getText(R.string.checkbox_do_not_show_again)).setPositiveButton(R.string.open_smart_dual_sim, new DialogInterface.OnClickListener() { // from class: com.android.phone.dsda.vtcallfeature.TxsDsdaNonDdsVtOptimize.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Intent intent = new Intent();
                    intent.addFlags(268435456);
                    intent.setClassName(RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME, "com.android.phone.settings.MobileNetworkSettings");
                    intent.putExtra("extra_from", RelayUtils.Relay.RELAY_SERVICE_PACKAGE_NAME);
                    TxsDsdaNonDdsVtOptimize.this.mContext.startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                    Rlog.d("TxsDsdaNonDdsVtOptimize", "showSmartDualSimEnabledDialog, transferMobileNetworkSettings Exception");
                }
                SharedPreferences.Editor editorEdit = TxsDsdaNonDdsVtOptimize.mPreferences.edit();
                editorEdit.putBoolean("show_txs_dsda_non_dds_vt_optimize_dialog", !TxsDsdaNonDdsVtOptimize.this.mAlertDialog.isChecked());
                editorEdit.apply();
            }
        }).setNegativeButton(R.string.close_smart_dual_sim, new DialogInterface.OnClickListener() { // from class: com.android.phone.dsda.vtcallfeature.TxsDsdaNonDdsVtOptimize.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                SharedPreferences.Editor editorEdit = TxsDsdaNonDdsVtOptimize.mPreferences.edit();
                editorEdit.putBoolean("show_txs_dsda_non_dds_vt_optimize_dialog", !TxsDsdaNonDdsVtOptimize.this.mAlertDialog.isChecked());
                editorEdit.apply();
            }
        }).setCancelable(false).create();
        this.mAlertDialog = alertDialogCreate;
        alertDialogCreate.getWindow().setType(2003);
        this.mAlertDialog.getWindow().addFlags(131072);
        this.mAlertDialog.getWindow().addPrivateFlags(524288);
        this.mAlertDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isViceCall(int i) {
        Rlog.d("TxsDsdaNonDdsVtOptimize", " isViceCall, phoneId: " + i);
        if (!Utils.isValidatePhoneId(i)) {
            Rlog.d("TxsDsdaNonDdsVtOptimize", " isViceCall, phoneId is invalid !");
            return false;
        }
        int nonDefaultDataSlotId = Utils.getNonDefaultDataSlotId();
        Rlog.d("TxsDsdaNonDdsVtOptimize", " isViceCall, nonDataSlot: " + nonDefaultDataSlotId);
        return i == nonDefaultDataSlotId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Call.State getViceFgCallState(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        if (phone == null) {
            Rlog.d("TxsDsdaNonDdsVtOptimize", " getViceFgCallState, phone == null !");
            return null;
        }
        ImsPhone imsPhone = phone.getImsPhone();
        if (!(imsPhone instanceof ImsPhone)) {
            return null;
        }
        ImsPhoneCall foregroundCall = imsPhone.getForegroundCall();
        if (foregroundCall == null) {
            Rlog.d("TxsDsdaNonDdsVtOptimize", " getViceFgCallState, foregroundCall == null !");
            return null;
        }
        Call.State state = Call.State.IDLE;
        Call.State state2 = foregroundCall.getState();
        Rlog.d("TxsDsdaNonDdsVtOptimize", " getViceFgCallState, viceFgCallState is:" + state2);
        return state2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isVtCall(int i) {
        Phone phone = PhoneFactory.getPhone(i);
        boolean zIsVideo = false;
        if (phone == null) {
            Rlog.d("TxsDsdaNonDdsVtOptimize", " isVtCall, phone == null !");
            return false;
        }
        ImsPhone imsPhone = phone.getImsPhone();
        if (imsPhone instanceof ImsPhone) {
            ImsPhoneCall foregroundCall = imsPhone.getForegroundCall();
            if (!foregroundCall.hasConnections()) {
                Rlog.d("TxsDsdaNonDdsVtOptimize", " isVtCall, foregroundCall.hasConnections == null !");
                return false;
            }
            ArrayList connections = foregroundCall.getConnections();
            int size = connections.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = connections.get(i2);
                i2++;
                zIsVideo |= Utils.isVideo(((Connection) obj).getVideoState());
            }
        }
        return zIsVideo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cleanFunc() {
        this.mPreFgViceVtCallState = Call.State.IDLE;
        this.mPreTxConcurrency = -1;
        this.mStartTimeMilliSeconds = 0L;
        this.mPreTxBytes = 0L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkIfNeedVtOptimize() {
        Call.State state = this.mPreFgViceVtCallState;
        Call.State state2 = Call.State.ACTIVE;
        if (state != state2) {
            int dsdaType = this.mInstance.getDsdaType();
            Rlog.d("TxsDsdaNonDdsVtOptimize", " checkIfNeedVtOptimize, mPreFgViceVtCallState is:" + this.mPreFgViceVtCallState + ", txConcurrency is:" + dsdaType);
            boolean zIsConcurrentCallsPossible = TelephonyManagerEx.getDefault().isConcurrentCallsPossible();
            if (dsdaType != 1 || !zIsConcurrentCallsPossible) {
                Rlog.d("TxsDsdaNonDdsVtOptimize", " Dsda tpye = " + dsdaType + " , isDSDAMode =  " + zIsConcurrentCallsPossible);
                return;
            }
            this.mPreTxConcurrency = 1;
            this.mPreFgViceVtCallState = state2;
            this.mStartTimeMilliSeconds = SystemClock.elapsedRealtime();
            this.mPreTxBytes = TrafficStats.getMobileTxBytes();
            Rlog.d("TxsDsdaNonDdsVtOptimize", " checkIfNeedVtOptimize, mStartTimeMilliSeconds: " + this.mStartTimeMilliSeconds + "ms, mPreTxBytes:" + this.mPreTxBytes + "Bytes");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startVtOptimize() {
        if (this.mPreFgViceVtCallState != Call.State.ACTIVE) {
            return;
        }
        int dsdaType = this.mInstance.getDsdaType();
        if (dsdaType != 1 || this.mPreTxConcurrency != 1) {
            cleanFunc();
            return;
        }
        long jCalculateAverageUlSpeed = calculateAverageUlSpeed(this.mStartTimeMilliSeconds, SystemClock.elapsedRealtime(), TrafficStats.getMobileTxBytes() - this.mPreTxBytes);
        Rlog.d("TxsDsdaNonDdsVtOptimize", " startVtOptimize, call disconnected !, txConcurrency:" + dsdaType);
        boolean z = mPreferences.getBoolean("show_txs_dsda_non_dds_vt_optimize_dialog", true);
        Rlog.d("TxsDsdaNonDdsVtOptimize", " startVtOptimize, showDialog is: " + z);
        if (z && jCalculateAverageUlSpeed > 1) {
            showSmartDualSimEnabledDialog();
        }
        cleanFunc();
    }
}
