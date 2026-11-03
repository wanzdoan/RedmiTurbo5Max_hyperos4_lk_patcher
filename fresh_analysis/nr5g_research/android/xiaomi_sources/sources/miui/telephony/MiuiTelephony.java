package miui.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.util.SparseArray;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlagsImpl;
import com.android.internal.telephony.MiuiCallControllerImpl;
import com.android.internal.telephony.MiuiDefaultPhoneNotifierImpl;
import com.android.internal.telephony.MiuiNetworkControllerImpl;
import com.android.internal.telephony.MiuiPhoneImpl;
import com.android.internal.telephony.MiuiRadioControllerImpl;
import com.android.internal.telephony.MiuiSmsControllerImpl;
import com.android.internal.telephony.MiuiSubscriptionControllerImpl;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.internal.telephony.data.MiuiPhoneSwitcherImpl;
import com.android.internal.telephony.imsphone.MiuiImsControllerImpl;
import com.android.internal.telephony.uicc.MiuiUiccControllerImpl;
import miui.util.FeatureParser;

/* JADX INFO: loaded from: classes.dex */
public class MiuiTelephony {
    public static final boolean IS_MTK;
    public static final boolean IS_PINECONE;
    public static final boolean IS_PROJECT_SUPPORT_ESIM;
    public static final boolean IS_QCOM;
    public static final boolean IS_SPRD;
    public static final boolean IS_XRING;
    private static FeatureFlags mFeatureFlags;
    private static volatile MiuiTelephony sInstance;
    private Context mContext;
    private MiuiDefaultPhoneNotifierImpl mPhoneNotifierPluginImpl;
    private MiuiPhoneSwitcherImpl mPhoneSwitchPluginImpl;
    private MiuiSubscriptionControllerImpl mSubControllerPluginImpl;
    private static String LOG_TAG = "MiuiTelephony";
    public static final int PHONE_COUNT = TelephonyManager.getDefault().getPhoneCount();
    public static final boolean IS_JLQ = "jlq".equals(FeatureParser.getString("vendor"));
    private SparseArray<MiuiPhoneImpl> mPhonePluginImpls = new SparseArray<>(PHONE_COUNT);
    private SparseArray<MiuiCallControllerImpl> mCallPluginImpls = new SparseArray<>(PHONE_COUNT);
    private SparseArray<MiuiDataControllerImpl> mDataPluginImpls = new SparseArray<>(PHONE_COUNT);
    private SparseArray<MiuiNetworkControllerImpl> mNetworkPluginImpls = new SparseArray<>(PHONE_COUNT);
    private SparseArray<MiuiSmsControllerImpl> mSmsPluginImpls = new SparseArray<>(PHONE_COUNT);
    private SparseArray<MiuiUiccControllerImpl> mUiccPluginImpls = new SparseArray<>(PHONE_COUNT);
    private SparseArray<MiuiImsControllerImpl> mImsPluginImpls = new SparseArray<>(PHONE_COUNT);
    private SparseArray<MiuiRadioControllerImpl> mRadioPluginImpls = new SparseArray<>(PHONE_COUNT);

    static {
        IS_QCOM = "qcom".equals(FeatureParser.getString("vendor")) || IS_JLQ;
        IS_MTK = "mediatek".equals(FeatureParser.getString("vendor"));
        IS_XRING = "xring".equals(FeatureParser.getString("vendor"));
        IS_PINECONE = "pinecone".equals(FeatureParser.getString("vendor"));
        IS_SPRD = "sprd".equals(FeatureParser.getString("vendor"));
        IS_PROJECT_SUPPORT_ESIM = SystemProperties.getBoolean("ro.vendor.miui.support_esim", false);
        mFeatureFlags = new FeatureFlagsImpl();
    }

    public static MiuiTelephony getInstance() {
        if (sInstance == null) {
            synchronized (MiuiTelephony.class) {
                if (sInstance == null) {
                    sInstance = new MiuiTelephony();
                }
            }
        }
        return sInstance;
    }

    public void init(Context context, FeatureFlags featureFlags) {
        this.mContext = context;
        mFeatureFlags = featureFlags;
        DefaultSimManager.init();
        TelephonyOptCloudController.getInstance().init(this.mContext);
    }

    public Context getContext() {
        return this.mContext;
    }

    public FeatureFlags getFeatureFlags() {
        return mFeatureFlags;
    }

    public void registerTelephonyPlugins(Object obj, int phoneId) {
        if (obj instanceof MiuiDefaultPhoneNotifierImpl) {
            this.mPhoneNotifierPluginImpl = (MiuiDefaultPhoneNotifierImpl) obj;
            return;
        }
        if (obj instanceof MiuiSubscriptionControllerImpl) {
            this.mSubControllerPluginImpl = (MiuiSubscriptionControllerImpl) obj;
            return;
        }
        if (obj instanceof MiuiPhoneSwitcherImpl) {
            this.mPhoneSwitchPluginImpl = (MiuiPhoneSwitcherImpl) obj;
            return;
        }
        if (obj instanceof MiuiPhoneImpl) {
            this.mPhonePluginImpls.put(phoneId, (MiuiPhoneImpl) obj);
            return;
        }
        if (obj instanceof MiuiCallControllerImpl) {
            this.mCallPluginImpls.put(phoneId, (MiuiCallControllerImpl) obj);
            return;
        }
        if (obj instanceof MiuiDataControllerImpl) {
            this.mDataPluginImpls.put(phoneId, (MiuiDataControllerImpl) obj);
            return;
        }
        if (obj instanceof MiuiNetworkControllerImpl) {
            this.mNetworkPluginImpls.put(phoneId, (MiuiNetworkControllerImpl) obj);
            return;
        }
        if (obj instanceof MiuiSmsControllerImpl) {
            this.mSmsPluginImpls.put(phoneId, (MiuiSmsControllerImpl) obj);
            return;
        }
        if (obj instanceof MiuiUiccControllerImpl) {
            this.mUiccPluginImpls.put(phoneId, (MiuiUiccControllerImpl) obj);
        } else if (obj instanceof MiuiImsControllerImpl) {
            this.mImsPluginImpls.put(phoneId, (MiuiImsControllerImpl) obj);
        } else if (obj instanceof MiuiRadioControllerImpl) {
            this.mRadioPluginImpls.put(phoneId, (MiuiRadioControllerImpl) obj);
        }
    }

    public void unregisterTelephonyPlugins(Object obj, int phoneId) {
        if (obj instanceof MiuiDefaultPhoneNotifierImpl) {
            this.mPhoneNotifierPluginImpl = null;
            return;
        }
        if (obj instanceof MiuiSubscriptionControllerImpl) {
            this.mSubControllerPluginImpl = null;
            return;
        }
        if (obj instanceof MiuiPhoneSwitcherImpl) {
            this.mPhoneSwitchPluginImpl = null;
            return;
        }
        if (obj instanceof MiuiPhoneImpl) {
            this.mPhonePluginImpls.remove(phoneId);
            return;
        }
        if (obj instanceof MiuiCallControllerImpl) {
            this.mCallPluginImpls.remove(phoneId);
            return;
        }
        if (obj instanceof MiuiDataControllerImpl) {
            this.mDataPluginImpls.remove(phoneId);
            return;
        }
        if (obj instanceof MiuiNetworkControllerImpl) {
            this.mNetworkPluginImpls.remove(phoneId);
            return;
        }
        if (obj instanceof MiuiSmsControllerImpl) {
            this.mSmsPluginImpls.remove(phoneId);
            return;
        }
        if (obj instanceof MiuiUiccControllerImpl) {
            this.mUiccPluginImpls.remove(phoneId);
        } else if (obj instanceof MiuiImsControllerImpl) {
            this.mImsPluginImpls.remove(phoneId);
        } else if (obj instanceof MiuiRadioControllerImpl) {
            this.mRadioPluginImpls.remove(phoneId);
        }
    }

    public Object getTelephonyPlugin(Class cls, int phoneId) {
        if (cls.equals(MiuiDefaultPhoneNotifierImpl.class)) {
            return this.mPhoneNotifierPluginImpl;
        }
        if (cls.equals(MiuiSubscriptionControllerImpl.class)) {
            return this.mSubControllerPluginImpl;
        }
        if (cls.equals(MiuiPhoneSwitcherImpl.class)) {
            return this.mPhoneSwitchPluginImpl;
        }
        if (cls.equals(MiuiPhoneImpl.class)) {
            return this.mPhonePluginImpls.get(phoneId);
        }
        if (cls.equals(MiuiCallControllerImpl.class)) {
            return this.mCallPluginImpls.get(phoneId);
        }
        if (cls.equals(MiuiDataControllerImpl.class)) {
            return this.mDataPluginImpls.get(phoneId);
        }
        if (cls.equals(MiuiNetworkControllerImpl.class)) {
            return this.mNetworkPluginImpls.get(phoneId);
        }
        if (cls.equals(MiuiSmsControllerImpl.class)) {
            return this.mSmsPluginImpls.get(phoneId);
        }
        if (cls.equals(MiuiUiccControllerImpl.class)) {
            return this.mUiccPluginImpls.get(phoneId);
        }
        if (cls.equals(MiuiImsControllerImpl.class)) {
            return this.mImsPluginImpls.get(phoneId);
        }
        if (cls.equals(MiuiRadioControllerImpl.class)) {
            return this.mRadioPluginImpls.get(phoneId);
        }
        return null;
    }
}
