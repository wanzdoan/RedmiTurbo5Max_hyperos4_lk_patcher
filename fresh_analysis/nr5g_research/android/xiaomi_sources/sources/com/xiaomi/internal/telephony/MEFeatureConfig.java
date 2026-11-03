package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import com.xiaomi.modem.ModemUtils;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/* JADX INFO: loaded from: classes.dex */
public class MEFeatureConfig {
    private static final int MEFEATURE_CONTROL_ID_END = 1100;
    private static final int MEFEATURE_CONTROL_ID_START = 1000;
    public static final int MEFEATURE_TYPE_ID_GSM_FORCE_TX_IN_USB_SCENAIRO = 5;
    public static final int MEFEATURE_TYPE_ID_MAX = 6;
    public static final int MEFEATURE_TYPE_ID_MIN = 0;
    public static final int MEFEATURE_TYPE_ID_SMART_TX = 4;
    public static final int MEFEATURE_TYPE_ID_T1 = 3;
    public static final int MEFEATURE_TYPE_ID_UAC = 1;
    public static final int MEFEATURE_TYPE_ID_UL_IMBALANCE = 2;
    private static final String ME_CLOUD_CONTROL = "com.android.phone.intent.action.ME_CLOUD_CONTROL";
    private static final String TAG = "Modem_MEFeatureConfig";
    private static String mDeviceName;
    private static boolean[] mFeatureConfig;
    private static boolean[] mFeatureConfigHW;
    private static boolean[] mFeatureConfigSW;
    private static boolean[] mFeatureConfigSWForMP;
    private static int[] mFeatureConfigSWVersion;
    private static String mHWC;
    private static MEFeatureConfig mInstance;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(this) { // from class: com.xiaomi.internal.telephony.MEFeatureConfig.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(MEFeatureConfig.TAG, " action: " + action);
            if (MEFeatureConfig.ME_CLOUD_CONTROL.equals(action)) {
                int id = intent.getIntExtra("FEATURE_TYPE", -1);
                int code = intent.getIntExtra("CONTROL_CODE", -1);
                Log.d(MEFeatureConfig.TAG, "ME_CONTROL _ID=" + id + ";   _VALUE:" + code);
                if (id >= 1000 && id < 1100) {
                    if (code == 0 || code == 1) {
                        int id2 = id - 1000;
                        if (id2 <= 0 || id2 >= 6) {
                            Log.e(MEFeatureConfig.TAG, "  feature control id invalid = " + id2);
                            return;
                        }
                        if (!MEFeatureConfig.mFeatureConfigHW[id2]) {
                            Log.e(MEFeatureConfig.TAG, "  feature control id set failed as hw does not support this feature. id = " + id2);
                            return;
                        }
                        boolean enable = code == 1;
                        MEFeatureConfig.mFeatureConfig[id2] = enable;
                        Log.d(MEFeatureConfig.TAG, "ME_FEATURE_CONTROL  feature id=" + id2 + " eable flag =" + enable);
                        try {
                            SystemProperties.set(MEFeatureConfig.feature_prop_name[id2], enable ? "true" : ModemUtils.PROP_BOOL_VALUE_OFF);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            } else {
                Log.d(MEFeatureConfig.TAG, "UNKNOWN action");
            }
            for (int i = 0; i < 6; i++) {
                Log.i(MEFeatureConfig.TAG, "after switch changed mFeatureConfig[" + i + "] = " + MEFeatureConfig.mFeatureConfig[i]);
            }
        }
    };
    private Context mContext;
    private static ModemEnhanceMain mModemEnhanceMain = null;
    private static String[] feature_prop_name = new String[7];

    static {
        feature_prop_name[0] = null;
        feature_prop_name[1] = "persist.radio.me.fr1";
        feature_prop_name[2] = "persist.radio.me.fr2";
        feature_prop_name[3] = "persist.radio.me.fr3";
        feature_prop_name[4] = "persist.radio.me.fr4";
        feature_prop_name[5] = "persist.radio.me.fr5";
        feature_prop_name[6] = null;
        mFeatureConfig = null;
        mFeatureConfigHW = null;
        mFeatureConfigSW = null;
        mFeatureConfigSWForMP = null;
        mFeatureConfigSWVersion = null;
        mDeviceName = null;
        mHWC = null;
    }

    private MEFeatureConfig(Context context) {
        this.mContext = context;
    }

    public static void init(Context context) {
        mFeatureConfig = new boolean[6];
        mFeatureConfigHW = new boolean[6];
        mFeatureConfigSW = new boolean[6];
        mFeatureConfigSWForMP = new boolean[6];
        mFeatureConfigSWVersion = new int[6];
        for (int i = 0; i < 6; i++) {
            mFeatureConfig[i] = false;
            mFeatureConfigHW[i] = false;
            mFeatureConfigSW[i] = false;
            mFeatureConfigSWForMP[i] = false;
            mFeatureConfigSWVersion[i] = 0;
        }
        mDeviceName = SystemProperties.get("ro.product.device");
        mHWC = SystemProperties.get("ro.boot.hwc");
        mInstance = new MEFeatureConfig(context);
        Log.d(TAG, "mFeatureConfig  deviceName = " + mDeviceName + ";       hwc=" + mHWC);
        boolean initFromXmlSucc = mInstance.initConfigFromXml("/odm/etc/me_config.xml");
        Log.d(TAG, "mFeatureConfig  initFromXmlSucc = " + initFromXmlSucc);
        String prop_value = "null";
        String hwVersion = SystemProperties.get(ModemUtils.PROPERTY_DEVICE_INFO_HW_LEVEL, ModemUtils.PROPERTY_DEVICE_INFO_HW_LEVEL_MP);
        for (int i2 = 0; i2 < 6; i2++) {
            if (mFeatureConfigHW[i2]) {
                if (feature_prop_name[i2] != null) {
                    prop_value = SystemProperties.get(feature_prop_name[i2], "null");
                }
                if (prop_value.equals("null")) {
                    mFeatureConfig[i2] = hwVersion.equals(ModemUtils.PROPERTY_DEVICE_INFO_HW_LEVEL_MP) ? mFeatureConfigSWForMP[i2] : mFeatureConfigSW[i2];
                } else {
                    mFeatureConfig[i2] = prop_value.equals("true");
                }
            }
        }
        for (int i3 = 0; i3 < 6; i3++) {
            Log.d(TAG, "mFeatureConfig[" + i3 + "] = " + mFeatureConfig[i3]);
            Log.d(TAG, "mFeatureSWVersion[" + i3 + "] = " + mFeatureConfigSWVersion[i3]);
        }
    }

    public static MEFeatureConfig getInstance() {
        return mInstance;
    }

    public static boolean isFeatureSupported(int type) {
        if (mFeatureConfig != null && type > 0 && type < 6) {
            return mFeatureConfig[type];
        }
        return false;
    }

    public static boolean isAnyFeatureSupported() {
        if (mFeatureConfig == null) {
            return false;
        }
        boolean supported = false;
        for (int i = 1; i < 6; i++) {
            supported = supported || mFeatureConfig[i];
        }
        return supported;
    }

    public static int getFeatureSWVersion(int type) {
        if (mFeatureConfigSWVersion != null && type > 0 && type < 6) {
            return mFeatureConfigSWVersion[type];
        }
        return 0;
    }

    public void startListen() {
        IntentFilter filter = new IntentFilter();
        try {
            filter.addAction(ME_CLOUD_CONTROL);
        } catch (Exception e) {
            Log.e(TAG, "errors");
            e.printStackTrace();
        }
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter, 2);
    }

    private boolean initConfigFromXml(String url) {
        try {
            InputStream inputStream = new FileInputStream(url);
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser saxParser = spf.newSAXParser();
                XMLContentHandler handler = new XMLContentHandler(this);
                saxParser.parse(inputStream, handler);
                inputStream.close();
                return handler.isXmlConfigMatchHW();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public class XMLContentHandler extends DefaultHandler {
        private String tagName = null;
        private String tagAttrValue = null;
        private boolean mSkipFlag = false;
        private int feature_id = 0;
        private boolean mMatched = false;

        public XMLContentHandler(MEFeatureConfig this$0) {
        }

        public boolean isXmlConfigMatchHW() {
            return this.mMatched;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            Log.i(MEFeatureConfig.TAG, "startElement  localName= " + localName + ";     skip= " + this.mSkipFlag);
            if (localName.equals("deviceConfig")) {
                if (attributes.getValue("device_name") == null || MEFeatureConfig.mDeviceName == null || !MEFeatureConfig.mDeviceName.equals(attributes.getValue("device_name")) || this.mMatched) {
                    this.mSkipFlag = true;
                    return;
                }
                this.mSkipFlag = false;
            } else if (localName.equals("hwc") && !this.mSkipFlag) {
                if (attributes.getValue("hwc_name") == null || MEFeatureConfig.mHWC == null || (!MEFeatureConfig.mHWC.equals(attributes.getValue("hwc_name")) && !attributes.getValue("hwc_name").equals(ModemUtils.PROP_NTN_LOG_LEVEL_DEFAULT))) {
                    this.mSkipFlag = true;
                    return;
                }
                this.mMatched = true;
            } else {
                if (localName.equals("GestureModeMapConfigBitMask") && !this.mSkipFlag) {
                    this.mSkipFlag = true;
                    return;
                }
                if (localName.equals("GestureModeMapConfig") && !this.mSkipFlag) {
                    this.mSkipFlag = true;
                    return;
                }
                if (localName.equals("feature") && !this.mSkipFlag) {
                    for (int i = 0; i < attributes.getLength(); i++) {
                        if (attributes.getLocalName(i).equals("feature_id")) {
                            this.feature_id = Integer.parseInt(attributes.getValue(i));
                        } else if (attributes.getLocalName(i).equals("hw_support")) {
                            if (this.feature_id > 0 && this.feature_id < 6) {
                                MEFeatureConfig.mFeatureConfigHW[this.feature_id] = !attributes.getValue(i).equals("0");
                            }
                        } else if (attributes.getLocalName(i).equals("sw_version")) {
                            if (this.feature_id > 0 && this.feature_id < 6) {
                                MEFeatureConfig.mFeatureConfigSWVersion[this.feature_id] = Integer.parseInt(attributes.getValue(i));
                            }
                        } else if (attributes.getLocalName(i).equals("sw_enable")) {
                            if (this.feature_id > 0 && this.feature_id < 6) {
                                MEFeatureConfig.mFeatureConfigSW[this.feature_id] = !attributes.getValue(i).equals("0");
                            }
                        } else if (attributes.getLocalName(i).equals("sw_mp_enable") && this.feature_id > 0 && this.feature_id < 6) {
                            MEFeatureConfig.mFeatureConfigSWForMP[this.feature_id] = !attributes.getValue(i).equals("0");
                        }
                    }
                }
            }
            this.tagName = localName;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] ch, int start, int length) throws SAXException {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String uri, String localName, String qName) throws SAXException {
            localName.equals("deviceConfig");
            this.tagName = null;
            this.tagAttrValue = null;
            this.feature_id = 0;
        }
    }
}
