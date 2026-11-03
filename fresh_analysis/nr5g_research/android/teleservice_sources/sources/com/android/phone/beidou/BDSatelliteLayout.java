package com.android.phone.beidou;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.phone.PhoneGlobals;
import com.android.phone.R;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public class BDSatelliteLayout extends LinearLayout {
    public static final int PI = (int) Math.toDegrees(3.141592653589793d);
    private static final String TAG = BDSatelliteLayout.class.getSimpleName();
    private SatelliteView mASView;
    private double mAzimuthDelta;
    private double mElevationDelta;
    private TextView mGudieToStart;
    Handler mHandler;
    private ImageView mImageView;
    private BDSatelliteDynInfo mInfo;
    private RotateView mMagneticCalibrateView;
    private long mSmsTime;

    /* JADX INFO: Access modifiers changed from: private */
    public void onTimeChange(int i) {
        TextView textView = this.mGudieToStart;
        if (textView != null) {
            Resources resources = getResources();
            long j = this.mSmsTime;
            textView.setText(resources.getQuantityString(i, (int) j, Integer.valueOf((int) j)));
            this.mSmsTime--;
        }
    }

    public BDSatelliteLayout(Context context) {
        super(context);
        this.mSmsTime = 2147483647L;
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.phone.beidou.BDSatelliteLayout.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 1) {
                    if (BDSatelliteLayout.this.mSmsTime < 0) {
                        BdSmsController.getInstance().closeBdService();
                        return;
                    }
                    BDSatelliteLayout.this.onTimeChange(message.arg1);
                    Message messageObtainMessage = obtainMessage(1);
                    messageObtainMessage.arg1 = message.arg1;
                    sendMessageDelayed(messageObtainMessage, 1000L);
                }
            }
        };
    }

    public BDSatelliteLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSmsTime = 2147483647L;
        this.mHandler = new Handler(Looper.getMainLooper()) { // from class: com.android.phone.beidou.BDSatelliteLayout.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                super.handleMessage(message);
                if (message.what == 1) {
                    if (BDSatelliteLayout.this.mSmsTime < 0) {
                        BdSmsController.getInstance().closeBdService();
                        return;
                    }
                    BDSatelliteLayout.this.onTimeChange(message.arg1);
                    Message messageObtainMessage = obtainMessage(1);
                    messageObtainMessage.arg1 = message.arg1;
                    sendMessageDelayed(messageObtainMessage, 1000L);
                }
            }
        };
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mASView = (SatelliteView) findViewById(R.id.align_satellite_view);
        this.mGudieToStart = (TextView) findViewById(R.id.guide_to_start);
        this.mImageView = (ImageView) findViewById(R.id.bd_signal);
        this.mMagneticCalibrateView = (RotateView) findViewById(R.id.magnetic_calibrate);
    }

    public void updateGuidanceViews(BDSatelliteDynInfo bDSatelliteDynInfo) {
        if (bDSatelliteDynInfo == null) {
            return;
        }
        BDSatelliteDynInfo bDSatelliteDynInfo2 = this.mInfo;
        if (bDSatelliteDynInfo2 != null && bDSatelliteDynInfo2.mSateId != bDSatelliteDynInfo.mSateId) {
            Toast.makeText(PhoneGlobals.getInstance(), R.string.satellite_switch_alert, 0).show();
        }
        this.mInfo = bDSatelliteDynInfo;
        int i = bDSatelliteDynInfo.mAzimuthDelta;
        int i2 = PI;
        this.mAzimuthDelta = (i + (i2 * 2)) % (i2 * 2);
        this.mElevationDelta = bDSatelliteDynInfo.mElevationDelta;
        updateBdAliSatText(bDSatelliteDynInfo);
        updateSignalStrength();
        updateAliSatLayout();
        SatelliteView satelliteView = this.mASView;
        if (satelliteView != null) {
            satelliteView.refreshAliSatDisplay(bDSatelliteDynInfo);
        }
    }

    public void updateSignalStrength() {
        ImageView imageView;
        BDSatelliteDynInfo bDSatelliteDynInfo = this.mInfo;
        if (bDSatelliteDynInfo == null || (imageView = this.mImageView) == null) {
            return;
        }
        int i = R.drawable.bd_signal_0;
        int i2 = bDSatelliteDynInfo.mSignalStrength;
        if (i2 != 0) {
            if (i2 == 1) {
                i = R.drawable.bd_signal_1;
            } else if (i2 == 2) {
                i = R.drawable.bd_signal_2;
            } else if (i2 == 3) {
                i = R.drawable.bd_signal_3;
            } else if (i2 == 4) {
                i = R.drawable.bd_signal_4;
            }
        }
        imageView.setImageResource(i);
    }

    public void updateBdAliSatText(BDSatelliteDynInfo bDSatelliteDynInfo) {
        ImageView imageView;
        if (bDSatelliteDynInfo == null || (imageView = this.mImageView) == null || this.mGudieToStart == null) {
            return;
        }
        int handsDirectRes = R.string.satellite_guide_searching;
        imageView.setVisibility(0);
        switch (AnonymousClass2.$SwitchMap$com$android$phone$beidou$BDSysStatus[bDSatelliteDynInfo.mSysStatus.ordinal()]) {
            case 1:
            case 2:
            case 3:
                this.mImageView.setVisibility(8);
                handsDirectRes = R.string.satellite_guide_searching;
                break;
            case 4:
                handsDirectRes = getHandsDirectRes(bDSatelliteDynInfo);
                break;
            case 5:
            case 6:
                handsDirectRes = R.plurals.beidou_sending;
                break;
            case 7:
                handsDirectRes = R.plurals.beidou_receiving;
                break;
            case 8:
            case 9:
                this.mImageView.setVisibility(8);
                break;
            default:
                this.mImageView.setVisibility(8);
                Log.i(TAG, "unknown sys status.");
                break;
        }
        int smsStatusRes = getSmsStatusRes(bDSatelliteDynInfo, handsDirectRes);
        if (smsStatusRes != R.plurals.beidou_sending && smsStatusRes != R.plurals.beidou_receiving) {
            if (this.mHandler.hasMessages(1)) {
                this.mHandler.removeMessages(1);
            }
            this.mSmsTime = 2147483647L;
            this.mGudieToStart.setText(smsStatusRes);
            return;
        }
        if (this.mSmsTime == 2147483647L) {
            this.mSmsTime = ((Long) Optional.ofNullable(BdSmsController.getInstance()).filter(new Predicate() { // from class: com.android.phone.beidou.BDSatelliteLayout$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ((BdSmsController) obj).isBdMMS();
                }
            }).map(new Function() { // from class: com.android.phone.beidou.BDSatelliteLayout$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return BDSatelliteLayout.$r8$lambda$aLqCrmHxZUBYUV18s0s2c6g_aiY((BdSmsController) obj);
                }
            }).orElse(30L)).longValue();
            Message messageObtainMessage = this.mHandler.obtainMessage(1);
            messageObtainMessage.arg1 = smsStatusRes;
            this.mHandler.sendMessage(messageObtainMessage);
        }
    }

    public static /* synthetic */ Long $r8$lambda$aLqCrmHxZUBYUV18s0s2c6g_aiY(BdSmsController bdSmsController) {
        return 90L;
    }

    private int getHandsDirectRes(BDSatelliteDynInfo bDSatelliteDynInfo) {
        int i = R.string.satellite_guide_searching;
        boolean zIsAlignedAzimuth = bDSatelliteDynInfo.isAlignedAzimuth(15);
        boolean zIsAlignedElevation = bDSatelliteDynInfo.isAlignedElevation();
        Log.i(TAG, "getHandsDirectRes : alignedAzimuth = " + zIsAlignedAzimuth + ", alignedElevation = " + zIsAlignedElevation);
        if (zIsAlignedAzimuth && zIsAlignedElevation) {
            if (!this.mASView.isInMoveElevation()) {
                return R.string.satellite_guide_azimuth_in;
            }
            return R.string.beidou_state_connecting;
        }
        if (zIsAlignedAzimuth) {
            if (!zIsAlignedElevation) {
                if (!this.mASView.isInMoveElevation()) {
                    return R.string.satellite_guide_azimuth_in;
                }
                return R.string.satellite_guide_elevation;
            }
        } else {
            if (!this.mASView.isInMoveElevation()) {
                return R.string.satellite_guide_azimuth;
            }
            if (bDSatelliteDynInfo.isAlignedAzimuth(30)) {
                return R.string.satellite_guide_azimuth_leave;
            }
        }
        return i;
    }

    /* JADX INFO: renamed from: com.android.phone.beidou.BDSatelliteLayout$2, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$phone$beidou$BDSmsSendStatus;
        static final /* synthetic */ int[] $SwitchMap$com$android$phone$beidou$BDSysStatus;

        static {
            int[] iArr = new int[BDSmsSendStatus.values().length];
            $SwitchMap$com$android$phone$beidou$BDSmsSendStatus = iArr;
            try {
                iArr[BDSmsSendStatus.SEND_STATUS_SUCC.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSmsSendStatus[BDSmsSendStatus.SEND_STATUS_FAIL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSmsSendStatus[BDSmsSendStatus.SEND_STATUS_SENT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[BDSysStatus.values().length];
            $SwitchMap$com$android$phone$beidou$BDSysStatus = iArr2;
            try {
                iArr2[BDSysStatus.SYS_STATUS_ENABLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSysStatus[BDSysStatus.SYS_STATUS_RESOURCE_PREPARE.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSysStatus[BDSysStatus.SYS_STATUS_WAIT_SEARCH_SATELLITE.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSysStatus[BDSysStatus.SYS_STATUS_SEARCHING_SATELLITE.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSysStatus[BDSysStatus.SYS_STATUS_SENDING.ordinal()] = 5;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSysStatus[BDSysStatus.SYS_STATUS_WAIT_ACK.ordinal()] = 6;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSysStatus[BDSysStatus.SYS_STATUS_WAIT_MSG_RECEIVE.ordinal()] = 7;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSysStatus[BDSysStatus.SYS_STATUS_DISABLE.ordinal()] = 8;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$android$phone$beidou$BDSysStatus[BDSysStatus.SYS_STATUS_RESERVED.ordinal()] = 9;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }

    private int getSendStateRes(BDSatelliteDynInfo.BDSmsSendInfo bDSmsSendInfo, BDSysStatus bDSysStatus) {
        int i = AnonymousClass2.$SwitchMap$com$android$phone$beidou$BDSmsSendStatus[bDSmsSendInfo.mSmsSendStatus.ordinal()];
        if (i == 1) {
            return R.string.beidou_send_success;
        }
        if (i == 2) {
            if (bDSmsSendInfo.mFailedCause == BDSmsSendFailedCause.NEAR_THRESHOLD) {
                return R.string.beidou_send_success;
            }
            return R.string.beidou_send_failed;
        }
        if (i == 3) {
            return R.string.beidou_send_wait_ack;
        }
        Log.i(TAG, "Unknown send status.");
        return R.string.beidou_send_failed;
    }

    private int getSmsStatusRes(BDSatelliteDynInfo bDSatelliteDynInfo, int i) {
        BDSatelliteDynInfo.BDSmsRecInfo bDSmsRecInfo = bDSatelliteDynInfo.mRecInfo;
        if (bDSmsRecInfo != null) {
            if (bDSmsRecInfo.mRecSuccess) {
                if (bDSmsRecInfo.mHasContent) {
                    return R.string.beidou_received_success;
                }
                return R.string.beidou_received_zero_sms;
            }
            return R.string.beidou_received_failed;
        }
        BDSatelliteDynInfo.BDSmsSendInfo bDSmsSendInfo = bDSatelliteDynInfo.mSendInfo;
        return bDSmsSendInfo != null ? getSendStateRes(bDSmsSendInfo, bDSatelliteDynInfo.mSysStatus) : i;
    }

    public void updateSatelliteViewLayout() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mASView.getLayoutParams();
        int i = R.dimen.satellite_view_height;
        layoutParams.width = (int) getResources().getDimension(R.dimen.satellite_view_width);
        layoutParams.height = (int) getResources().getDimension(i);
        this.mASView.setLayoutParams(layoutParams);
    }

    public void updateAliSatLayout() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mGudieToStart.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, (int) getResources().getDimension(R.dimen.satellite_guide_text_margin_bottom));
        this.mGudieToStart.setLayoutParams(layoutParams);
    }

    public void updateMagneticCalibrate(boolean z, int i, int i2, int i3) {
        if (this.mInfo != null && z) {
            this.mASView.setVisibility(8);
            this.mGudieToStart.setText(R.string.calibrate_rotate_tip);
            this.mMagneticCalibrateView.setVisibility(0);
            this.mMagneticCalibrateView.update(i, i2);
            if (i3 % 5 == 0.0f) {
                HapticFeedbackTool.getInstance().performDirectionGet(this.mMagneticCalibrateView);
                return;
            }
            return;
        }
        updateAliSatLayout();
        updateSatelliteViewLayout();
        this.mASView.setVisibility(0);
        this.mMagneticCalibrateView.setVisibility(8);
        this.mMagneticCalibrateView.reset();
    }

    public void stopTimer() {
        this.mHandler.removeCallbacksAndMessages(null);
        this.mSmsTime = 2147483647L;
    }
}
