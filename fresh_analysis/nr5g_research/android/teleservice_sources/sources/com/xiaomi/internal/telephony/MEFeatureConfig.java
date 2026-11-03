package com.xiaomi.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemProperties;
import android.util.Log;
import com.ot.pubsub.a.a;
import java.io.FileInputStream;
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
    private static String[] feature_prop_name = {null, "persist.radio.me.fr1", "persist.radio.me.fr2", "persist.radio.me.fr3", "persist.radio.me.fr4", "persist.radio.me.fr5", null};
    private static String mDeviceName;
    private static boolean[] mFeatureConfig;
    private static boolean[] mFeatureConfigHW;
    private static boolean[] mFeatureConfigSW;
    private static boolean[] mFeatureConfigSWForMP;
    private static int[] mFeatureConfigSWVersion;
    private static String mHWC;
    private static MEFeatureConfig mInstance;
    private static ModemEnhanceMain mModemEnhanceMain;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(this) { // from class: com.xiaomi.internal.telephony.MEFeatureConfig.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(MEFeatureConfig.TAG, " action: " + action);
            if (MEFeatureConfig.ME_CLOUD_CONTROL.equals(action)) {
                int intExtra = intent.getIntExtra("FEATURE_TYPE", -1);
                int intExtra2 = intent.getIntExtra("CONTROL_CODE", -1);
                Log.d(MEFeatureConfig.TAG, "ME_CONTROL _ID=" + intExtra + ";   _VALUE:" + intExtra2);
                if (intExtra >= 1000 && intExtra < MEFeatureConfig.MEFEATURE_CONTROL_ID_END) {
                    if (intExtra2 == 0 || intExtra2 == 1) {
                        int i = intExtra - 1000;
                        if (i <= 0 || i >= 6) {
                            Log.e(MEFeatureConfig.TAG, "  feature control id invalid = " + i);
                            return;
                        }
                        if (!MEFeatureConfig.mFeatureConfigHW[i]) {
                            Log.e(MEFeatureConfig.TAG, "  feature control id set failed as hw does not support this feature. id = " + i);
                            return;
                        }
                        boolean z = intExtra2 == 1;
                        MEFeatureConfig.mFeatureConfig[i] = z;
                        Log.d(MEFeatureConfig.TAG, "ME_FEATURE_CONTROL  feature id=" + i + " eable flag =" + z);
                        try {
                            SystemProperties.set(MEFeatureConfig.feature_prop_name[i], z ? "true" : "false");
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            } else {
                Log.d(MEFeatureConfig.TAG, "UNKNOWN action");
            }
            for (int i2 = 0; i2 < 6; i2++) {
                Log.i(MEFeatureConfig.TAG, "after switch changed mFeatureConfig[" + i2 + "] = " + MEFeatureConfig.mFeatureConfig[i2]);
            }
        }
    };
    private Context mContext;

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
        mDeviceName = SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_NAME);
        mHWC = SystemProperties.get(ModemSarUtils.PROPERTY_DEVICE_HWC);
        mInstance = new MEFeatureConfig(context);
        Log.d(TAG, "mFeatureConfig  deviceName = " + mDeviceName + ";       hwc=" + mHWC);
        boolean zInitConfigFromXml = mInstance.initConfigFromXml("/odm/etc/me_config.xml");
        StringBuilder sb = new StringBuilder("mFeatureConfig  initFromXmlSucc = ");
        sb.append(zInitConfigFromXml);
        Log.d(TAG, sb.toString());
        String str = SystemProperties.get("ro.boot.hwlevel", "MP");
        String str2 = "null";
        for (int i2 = 0; i2 < 6; i2++) {
            if (mFeatureConfigHW[i2]) {
                String str3 = feature_prop_name[i2];
                if (str3 != null) {
                    str2 = SystemProperties.get(str3, "null");
                }
                if (str2.equals("null")) {
                    mFeatureConfig[i2] = str.equals("MP") ? mFeatureConfigSWForMP[i2] : mFeatureConfigSW[i2];
                } else {
                    mFeatureConfig[i2] = str2.equals("true");
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

    public static boolean isFeatureSupported(int i) {
        boolean[] zArr = mFeatureConfig;
        if (zArr != null && i > 0 && i < 6) {
            return zArr[i];
        }
        return false;
    }

    public static boolean isAnyFeatureSupported() {
        if (mFeatureConfig == null) {
            return false;
        }
        boolean z = false;
        for (int i = 1; i < 6; i++) {
            z = z || mFeatureConfig[i];
        }
        return z;
    }

    public static int getFeatureSWVersion(int i) {
        int[] iArr = mFeatureConfigSWVersion;
        if (iArr != null && i > 0 && i < 6) {
            return iArr[i];
        }
        return 0;
    }

    public void startListen() {
        IntentFilter intentFilter = new IntentFilter();
        try {
            intentFilter.addAction(ME_CLOUD_CONTROL);
        } catch (Exception e) {
            Log.e(TAG, "errors");
            e.printStackTrace();
        }
        this.mContext.registerReceiver(this.mBroadcastReceiver, intentFilter, 2);
    }

    private boolean initConfigFromXml(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(str);
            try {
                SAXParser sAXParserNewSAXParser = SAXParserFactory.newInstance().newSAXParser();
                XMLContentHandler xMLContentHandler = new XMLContentHandler(this);
                sAXParserNewSAXParser.parse(fileInputStream, xMLContentHandler);
                fileInputStream.close();
                return xMLContentHandler.isXmlConfigMatchHW();
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

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] cArr, int i, int i2) throws SAXException {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
        }

        public XMLContentHandler(MEFeatureConfig mEFeatureConfig) {
        }

        public boolean isXmlConfigMatchHW() {
            return this.mMatched;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            int i;
            Log.i(MEFeatureConfig.TAG, "startElement  localName= " + str2 + ";     skip= " + this.mSkipFlag);
            if (str2.equals("deviceConfig")) {
                if (attributes.getValue("device_name") == null || MEFeatureConfig.mDeviceName == null || !MEFeatureConfig.mDeviceName.equals(attributes.getValue("device_name")) || this.mMatched) {
                    this.mSkipFlag = true;
                    return;
                }
                this.mSkipFlag = false;
            } else if (str2.equals("hwc") && !this.mSkipFlag) {
                if (attributes.getValue("hwc_name") == null || MEFeatureConfig.mHWC == null || (!MEFeatureConfig.mHWC.equals(attributes.getValue("hwc_name")) && !attributes.getValue("hwc_name").equals("default"))) {
                    this.mSkipFlag = true;
                    return;
                }
                this.mMatched = true;
            } else {
                if (str2.equals("GestureModeMapConfigBitMask") && !this.mSkipFlag) {
                    this.mSkipFlag = true;
                    return;
                }
                if (str2.equals("GestureModeMapConfig") && !this.mSkipFlag) {
                    this.mSkipFlag = true;
                    return;
                }
                if (str2.equals(a.n) && !this.mSkipFlag) {
                    for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                        if (attributes.getLocalName(i2).equals("feature_id")) {
                            this.feature_id = Integer.parseInt(attributes.getValue(i2));
                        } else if (attributes.getLocalName(i2).equals("hw_support")) {
                            int i3 = this.feature_id;
                            if (i3 > 0 && i3 < 6) {
                                MEFeatureConfig.mFeatureConfigHW[this.feature_id] = !attributes.getValue(i2).equals("0");
                            }
                        } else if (attributes.getLocalName(i2).equals("sw_version")) {
                            int i4 = this.feature_id;
                            if (i4 > 0 && i4 < 6) {
                                MEFeatureConfig.mFeatureConfigSWVersion[this.feature_id] = Integer.parseInt(attributes.getValue(i2));
                            }
                        } else if (attributes.getLocalName(i2).equals("sw_enable")) {
                            int i5 = this.feature_id;
                            if (i5 > 0 && i5 < 6) {
                                MEFeatureConfig.mFeatureConfigSW[this.feature_id] = !attributes.getValue(i2).equals("0");
                            }
                        } else if (attributes.getLocalName(i2).equals("sw_mp_enable") && (i = this.feature_id) > 0 && i < 6) {
                            MEFeatureConfig.mFeatureConfigSWForMP[this.feature_id] = !attributes.getValue(i2).equals("0");
                        }
                    }
                }
            }
            this.tagName = str2;
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String str, String str2, String str3) throws SAXException {
            str2.equals("deviceConfig");
            this.tagName = null;
            this.tagAttrValue = null;
            this.feature_id = 0;
        }
    }
}
