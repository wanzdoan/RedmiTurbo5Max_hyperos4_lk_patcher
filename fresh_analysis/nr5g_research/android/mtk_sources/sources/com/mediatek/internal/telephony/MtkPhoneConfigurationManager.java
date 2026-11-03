package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.PhoneCapability;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneConfigurationManager;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.RIL;
import com.android.internal.telephony.RILUtils;
import com.android.telephony.Rlog;
import java.util.Collections;
import java.util.List;
import mediatek.telephony.MtkDisconnectCause;

/* JADX INFO: loaded from: classes.dex */
public class MtkPhoneConfigurationManager extends PhoneConfigurationManager {
    private static final int EVENT_DSDA_STATE_CHANGED = 10000;
    private static final int EVENT_GET_DSDA_STATUS = 10001;
    private static final String KEY_MIPC_SUPPORT = "ro.vendor.mtk_mipc_support";
    private static final String TAG = "MtkPhoneCfgMgr";
    private Callback mCallback;
    private boolean mIsDsda;
    private Handler mMyHandler;
    private SystemPropertiesProxy mSystemPropertiesProxy;

    public interface Callback {
        void onDsdaStateChanged(boolean z);
    }

    public interface SystemPropertiesProxy {
        int getInt(String str, int i);
    }

    public void setCallback(Callback cb) {
        this.mCallback = cb;
    }

    public class SystemPropertiesProxyImpl implements SystemPropertiesProxy {
        public SystemPropertiesProxyImpl(MtkPhoneConfigurationManager this$0) {
        }

        @Override // com.mediatek.internal.telephony.MtkPhoneConfigurationManager.SystemPropertiesProxy
        public int getInt(String key, int defaultValue) {
            return SystemProperties.getInt(key, defaultValue);
        }
    }

    public void setSystemPropertiesProxy(SystemPropertiesProxy proxy) {
        this.mSystemPropertiesProxy = proxy;
    }

    public void setMyHandler(Handler myHandler) {
        this.mMyHandler = myHandler;
    }

    public void setIsDsda(boolean dsda) {
        this.mIsDsda = dsda;
    }

    private final class MtkConfigManagerHandler extends Handler {
        private MtkConfigManagerHandler() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MtkPhoneConfigurationManager.EVENT_DSDA_STATE_CHANGED /* 10000 */:
                case MtkPhoneConfigurationManager.EVENT_GET_DSDA_STATUS /* 10001 */:
                    MtkPhoneConfigurationManager.this.handleDsdaStateChanged(msg);
                    break;
            }
        }
    }

    public boolean isDsda(int dsdaMode) {
        if (dsdaMode > 100) {
            dsdaMode /= 100;
        }
        if (dsdaMode == 2 || dsdaMode == 3) {
            return true;
        }
        return false;
    }

    public int getDsdaMode(AsyncResult ar) {
        if (ar == null || ar.result == null) {
            return MtkDisconnectCause.WFC_WIFI_SIGNAL_LOST;
        }
        if (ar.result instanceof int[]) {
            int[] mode = (int[]) ar.result;
            if (mode.length <= 0) {
                return MtkDisconnectCause.WFC_WIFI_SIGNAL_LOST;
            }
            int dsdaMode = mode[0];
            return dsdaMode;
        }
        int dsdaMode2 = ((Integer) ar.result).intValue();
        return dsdaMode2;
    }

    public List<Integer> getSimulCallingList(boolean isDsda) {
        int[] enabledLogicalSlots = {0, 1};
        List<Integer> ret = isDsda ? RILUtils.primitiveArrayToArrayList(enabledLogicalSlots) : Collections.emptyList();
        Rlog.d(TAG, "Send EVENT_SIMULTANEOUS_CALLING_SUPPORT_CHANGED " + ret);
        return ret;
    }

    public Message sendMsgToParent(boolean isDsda, Handler h) {
        Message m = Message.obtain(h, 106, new AsyncResult((Object) null, getSimulCallingList(isDsda), (Throwable) null));
        m.sendToTarget();
        return m;
    }

    public void handleDsdaStateChanged(Message msg) {
        AsyncResult ar = (AsyncResult) msg.obj;
        boolean isDsda = isDsda(getDsdaMode(ar));
        if (this.mIsDsda != isDsda) {
            this.mCallback.onDsdaStateChanged(isDsda);
            this.mIsDsda = isDsda;
        }
    }

    public MtkPhoneConfigurationManager(Context context, FeatureFlags featureFlags) {
        super(context, featureFlags);
        this.mMyHandler = null;
        this.mIsDsda = false;
        this.mCallback = new Callback() { // from class: com.mediatek.internal.telephony.MtkPhoneConfigurationManager.1
            @Override // com.mediatek.internal.telephony.MtkPhoneConfigurationManager.Callback
            public void onDsdaStateChanged(boolean isDsda) {
                MtkPhoneConfigurationManager.this.sendMsgToParent(isDsda, MtkPhoneConfigurationManager.this.mHandler);
            }
        };
        this.mMyHandler = new MtkConfigManagerHandler();
    }

    private boolean supportSimulCallingReportInVendor() {
        boolean ret = this.mRadioConfig != null && this.mRadioConfig.getRadioConfigProxy((Message) null).getVersion().greaterOrEqual(RIL.RADIO_HAL_VERSION_2_2);
        Rlog.d(TAG, "supportSimulCallReportInVendor " + ret);
        return ret;
    }

    private boolean mdSupportMipc() {
        this.mSystemPropertiesProxy = new SystemPropertiesProxyImpl(this);
        boolean ret = this.mSystemPropertiesProxy.getInt(KEY_MIPC_SUPPORT, 0) == 1;
        Rlog.d(TAG, "ro.vendor.mtk_mipc_support = " + ret);
        return ret;
    }

    public boolean shallOverrideMaxActiveVoiceSubscriptions() {
        return mdSupportMipc() && !supportSimulCallingReportInVendor();
    }

    public PhoneCapability maybeOverrideMaxActiveVoiceSubscriptions(PhoneCapability staticCapability) {
        PhoneCapability parentCap = super.maybeOverrideMaxActiveVoiceSubscriptions(staticCapability);
        if (shallOverrideMaxActiveVoiceSubscriptions()) {
            Rlog.d(TAG, "Override Max Active Voice Subscriptions for U vendor and MIPC");
            return new PhoneCapability.Builder(parentCap).setMaxActiveVoiceSubscriptions(2).build();
        }
        return parentCap;
    }

    public boolean onMaybeEnableCellularDSDASupport(boolean halSupportSimulCalling, boolean bkwdsCompatDsda) {
        Rlog.d(TAG, "onMaybeEnableCellularDSDASupport halSupportSimulcalling " + halSupportSimulCalling + ", bkwdsCompatDsda = " + bkwdsCompatDsda);
        if (!halSupportSimulCalling && !bkwdsCompatDsda) {
            Phone phone = PhoneFactory.getDefaultPhone();
            if (phone != null && phone.mCi != null) {
                Rlog.d(TAG, "register DSDA status for phone " + phone.getPhoneId());
                phone.mCi.getDsdaStatus(this.mMyHandler.obtainMessage(EVENT_GET_DSDA_STATUS));
                phone.mCi.registerForDsdaStateChanged(this.mMyHandler, EVENT_DSDA_STATE_CHANGED, null);
                return true;
            }
            return true;
        }
        return bkwdsCompatDsda;
    }
}
