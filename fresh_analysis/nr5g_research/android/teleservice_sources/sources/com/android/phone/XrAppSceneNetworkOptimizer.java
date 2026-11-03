package com.android.phone;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.ResultReceiver;
import android.telephony.Rlog;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.internal.util.ArrayUtils;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.BroadcastEvent;
import com.android.phone.eventhub.events.DeviceStateEvent;
import com.google.android.appfunctions.schema.common.v1.devicestate.DeviceStateKt;
import com.xiaomi.internal.telephony.ExtPhoneConstants;
import com.xiaomi.telephony.XmTelephonyManagerInner;
import java.util.List;
import miui.os.HyperOSCustFeatureResolve;
import miui.process.ForegroundInfo;

/* JADX INFO: loaded from: classes.dex */
public class XrAppSceneNetworkOptimizer {
    private static final List GAME_APP_PACKAGES = HyperOSCustFeatureResolve.getStringArray("hyperos_game_app_packages");
    private static final List LIVE_STREAM_APP_PACKAGES = HyperOSCustFeatureResolve.getStringArray("hyperos_live_stream_app_packages");
    private static final List SHORT_VIDEO_APP_PACKAGES = HyperOSCustFeatureResolve.getStringArray("hyperos_short_video_app_packages");
    private final Context mContext;
    private final BaseEvent.IEventListener mEventListener;
    private final ResultReceiver mPromoteBSRReceiver;
    private final ResultReceiver mSetDataStallModeReceiver;
    private final ResultReceiver mSetMobilityOptParamsReceiver;
    private final Handler mWorkHandler;
    private final XmTelephonyManagerInner mXmTelephony = XmTelephonyManagerInner.getDefault();
    private boolean mIsNetworkOptActive = false;
    private ExtPhoneConstants.ForegroundAppType mLastOptimizedAppType = ExtPhoneConstants.ForegroundAppType.APP_TYPE_OTHER;
    private int mLastOptRat = 4;
    private boolean mIsWechatVideoActive = false;

    /* JADX INFO: renamed from: com.android.phone.XrAppSceneNetworkOptimizer$4, reason: invalid class name */
    class AnonymousClass4 implements BaseEvent.IEventListener {
        AnonymousClass4() {
        }

        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof DeviceStateEvent.AppStateEvent) {
                ForegroundInfo foregroundInfo = ((DeviceStateEvent.AppStateEvent) baseEvent).getForegroundInfo();
                if (foregroundInfo != null) {
                    final String str = foregroundInfo.mForegroundPackageName;
                    XrAppSceneNetworkOptimizer.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.XrAppSceneNetworkOptimizer$4$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onEvent$0(str);
                        }
                    });
                    return;
                }
                return;
            }
            if (baseEvent instanceof BroadcastEvent) {
                final boolean z = baseEvent.getBundle().getBoolean("wechatVoipEnabled", false);
                XrAppSceneNetworkOptimizer.log("WeChat VoIP scene changed: " + z);
                XrAppSceneNetworkOptimizer.this.mWorkHandler.post(new Runnable() { // from class: com.android.phone.XrAppSceneNetworkOptimizer$4$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$onEvent$1(z);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEvent$0(String str) {
            ExtPhoneConstants.ForegroundAppType networkOptimizationAppType = XrAppSceneNetworkOptimizer.this.getNetworkOptimizationAppType(str);
            XrAppSceneNetworkOptimizer.log("foregroundApp: " + str + " networkOptAppType: " + networkOptimizationAppType);
            XrAppSceneNetworkOptimizer.this.applyNetworkOptimization(networkOptimizationAppType);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onEvent$1(boolean z) {
            XrAppSceneNetworkOptimizer.this.applyWechatVideoOptimization(z);
        }
    }

    public static XrAppSceneNetworkOptimizer make(Context context) {
        return new XrAppSceneNetworkOptimizer(context);
    }

    private XrAppSceneNetworkOptimizer(Context context) {
        Handler handler = null;
        this.mSetDataStallModeReceiver = new ResultReceiver(this, handler) { // from class: com.android.phone.XrAppSceneNetworkOptimizer.1
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                XrAppSceneNetworkOptimizer.log("setDataStallMode result: " + i);
            }
        };
        this.mSetMobilityOptParamsReceiver = new ResultReceiver(this, handler) { // from class: com.android.phone.XrAppSceneNetworkOptimizer.2
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                XrAppSceneNetworkOptimizer.log("setMobilityOptParams result: " + i);
            }
        };
        this.mPromoteBSRReceiver = new ResultReceiver(this, handler) { // from class: com.android.phone.XrAppSceneNetworkOptimizer.3
            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int i, Bundle bundle) {
                XrAppSceneNetworkOptimizer.log("promoteBSR result: " + i);
            }
        };
        AnonymousClass4 anonymousClass4 = new AnonymousClass4();
        this.mEventListener = anonymousClass4;
        this.mContext = context;
        HandlerThread handlerThread = new HandlerThread("XrAppSceneNetworkOptimizer", 10);
        handlerThread.start();
        Handler handler2 = new Handler(handlerThread.getLooper());
        this.mWorkHandler = handler2;
        EventHubManager.getInstance().registerListener(DeviceStateKt.DEVICE_STATE_CATEGORY, "foreground_changed", anonymousClass4);
        EventHubManager.getInstance().registerListener("broadcast", "wechat_voip_scene", anonymousClass4, handler2);
    }

    private int getCurrentPrefBandRat() {
        Phone activeDataPhone = MiuiPhoneUtils.getActiveDataPhone();
        return (activeDataPhone == null || activeDataPhone.getServiceState() == null || activeDataPhone.getServiceState().getDataNetworkType() != 20) ? 4 : 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ExtPhoneConstants.ForegroundAppType getNetworkOptimizationAppType(String str) {
        if (TextUtils.isEmpty(str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_OTHER;
        }
        if (ArrayUtils.contains(GAME_APP_PACKAGES, str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_GAME;
        }
        if (ArrayUtils.contains(LIVE_STREAM_APP_PACKAGES, str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_LIVE;
        }
        if (ArrayUtils.contains(SHORT_VIDEO_APP_PACKAGES, str)) {
            return ExtPhoneConstants.ForegroundAppType.APP_TYPE_SHORT_VIDEO;
        }
        return ExtPhoneConstants.ForegroundAppType.APP_TYPE_OTHER;
    }

    /* JADX INFO: renamed from: com.android.phone.XrAppSceneNetworkOptimizer$5, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$xiaomi$internal$telephony$ExtPhoneConstants$ForegroundAppType;

        static {
            int[] iArr = new int[ExtPhoneConstants.ForegroundAppType.values().length];
            $SwitchMap$com$xiaomi$internal$telephony$ExtPhoneConstants$ForegroundAppType = iArr;
            try {
                iArr[ExtPhoneConstants.ForegroundAppType.APP_TYPE_GAME.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$xiaomi$internal$telephony$ExtPhoneConstants$ForegroundAppType[ExtPhoneConstants.ForegroundAppType.APP_TYPE_LIVE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$xiaomi$internal$telephony$ExtPhoneConstants$ForegroundAppType[ExtPhoneConstants.ForegroundAppType.APP_TYPE_SHORT_VIDEO.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    private int getDataStallScene(ExtPhoneConstants.ForegroundAppType foregroundAppType) {
        int i = AnonymousClass5.$SwitchMap$com$xiaomi$internal$telephony$ExtPhoneConstants$ForegroundAppType[foregroundAppType.ordinal()];
        int i2 = 1;
        if (i != 1) {
            i2 = 2;
            if (i != 2) {
                return i != 3 ? 0 : 4;
            }
        }
        return i2;
    }

    private boolean isOptimizableAppType(ExtPhoneConstants.ForegroundAppType foregroundAppType) {
        return foregroundAppType == ExtPhoneConstants.ForegroundAppType.APP_TYPE_GAME || foregroundAppType == ExtPhoneConstants.ForegroundAppType.APP_TYPE_LIVE || foregroundAppType == ExtPhoneConstants.ForegroundAppType.APP_TYPE_SHORT_VIDEO;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyNetworkOptimization(ExtPhoneConstants.ForegroundAppType foregroundAppType) {
        boolean zIsOptimizableAppType = isOptimizableAppType(foregroundAppType);
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        if (!MiuiPhoneUtils.isValidatePhoneId(activeDataPhoneId)) {
            log("invalid phoneId=" + activeDataPhoneId + ", skip applyNetworkOptimization");
            return;
        }
        if (zIsOptimizableAppType && !this.mIsNetworkOptActive) {
            log("Enabling network optimization for " + foregroundAppType + " on phoneId=" + activeDataPhoneId);
            enableScene(activeDataPhoneId, foregroundAppType);
            this.mIsNetworkOptActive = true;
            this.mLastOptimizedAppType = foregroundAppType;
            return;
        }
        if (!zIsOptimizableAppType && this.mIsNetworkOptActive) {
            log("Disabling network optimization on phoneId=" + activeDataPhoneId);
            disableScene(activeDataPhoneId, this.mLastOptimizedAppType);
            this.mIsNetworkOptActive = false;
            this.mLastOptimizedAppType = ExtPhoneConstants.ForegroundAppType.APP_TYPE_OTHER;
            return;
        }
        if (zIsOptimizableAppType && this.mIsNetworkOptActive && foregroundAppType != this.mLastOptimizedAppType) {
            log("Switching network optimization from " + this.mLastOptimizedAppType + " to " + foregroundAppType + " on phoneId=" + activeDataPhoneId);
            disableScene(activeDataPhoneId, this.mLastOptimizedAppType);
            enableScene(activeDataPhoneId, foregroundAppType);
            this.mLastOptimizedAppType = foregroundAppType;
        }
    }

    private void enableScene(int i, ExtPhoneConstants.ForegroundAppType foregroundAppType) {
        this.mXmTelephony.setDataStallMode(i, 1, 1, 1, getDataStallScene(foregroundAppType), this.mSetDataStallModeReceiver);
        if (foregroundAppType == ExtPhoneConstants.ForegroundAppType.APP_TYPE_GAME) {
            this.mLastOptRat = getCurrentPrefBandRat();
            log("Enabling mobilityOpt and BSR, rat=" + this.mLastOptRat);
            this.mXmTelephony.setMobilityOptParams(i, true, this.mLastOptRat, 9, this.mSetMobilityOptParamsReceiver);
            this.mXmTelephony.setMobilityOptParams(i, true, this.mLastOptRat, 10, this.mSetMobilityOptParamsReceiver);
            this.mXmTelephony.promoteBSR(i, 2, this.mPromoteBSRReceiver);
        }
    }

    private void disableScene(int i, ExtPhoneConstants.ForegroundAppType foregroundAppType) {
        this.mXmTelephony.setDataStallMode(i, 1, 1, 1, 0, this.mSetDataStallModeReceiver);
        if (foregroundAppType == ExtPhoneConstants.ForegroundAppType.APP_TYPE_GAME) {
            log("Disabling mobilityOpt and BSR, rat=" + this.mLastOptRat);
            this.mXmTelephony.setMobilityOptParams(i, false, this.mLastOptRat, 9, this.mSetMobilityOptParamsReceiver);
            this.mXmTelephony.setMobilityOptParams(i, false, this.mLastOptRat, 10, this.mSetMobilityOptParamsReceiver);
            this.mXmTelephony.promoteBSR(i, 0, this.mPromoteBSRReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyWechatVideoOptimization(boolean z) {
        if (this.mIsWechatVideoActive == z) {
            return;
        }
        int activeDataPhoneId = MiuiPhoneUtils.getActiveDataPhoneId();
        if (!MiuiPhoneUtils.isValidatePhoneId(activeDataPhoneId)) {
            log("invalid phoneId=" + activeDataPhoneId + ", skip applyWechatVideoOptimization");
            return;
        }
        this.mIsWechatVideoActive = z;
        if (z) {
            log("Enabling WeChat video optimization on phoneId=" + activeDataPhoneId);
            this.mXmTelephony.setDataStallMode(activeDataPhoneId, 1, 1, 1, 3, this.mSetDataStallModeReceiver);
            return;
        }
        int dataStallScene = this.mIsNetworkOptActive ? getDataStallScene(this.mLastOptimizedAppType) : 0;
        log("Disabling WeChat video optimization on phoneId=" + activeDataPhoneId + ", restoring scene=" + dataStallScene);
        this.mXmTelephony.setDataStallMode(activeDataPhoneId, 1, 1, 1, dataStallScene, this.mSetDataStallModeReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void log(String str) {
        Rlog.d("XrAppSceneNetworkOptimizer", str);
    }
}
