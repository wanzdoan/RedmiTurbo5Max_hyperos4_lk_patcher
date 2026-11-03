package com.android.internal.telephony.uicc;

import android.os.Environment;
import android.telephony.Rlog;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import miui.telephony.CotaOpconfigManager;
import miui.telephony.TelephonyManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class SpnOverride {
    static final String EXT_SPN_OVERRIDE_PATH = "/mi_ext/product/etc/spn-conf.xml";
    private static final String LOG_TAG = "SpnOverride";
    static final String OEM_SPN_OVERRIDE_PATH = "telephony/spn-conf.xml";
    static final String PARTNER_APEX_SPN_OVERRIDE_PATH = "/apex/com.miui.opconfig/etc/spn/spn-conf.xml";
    static final String PARTNER_SPN_OVERRIDE_PATH = "etc/spn-conf.xml";
    private static volatile SpnOverride sInstance;
    private HashMap<String, String> mCarrierSpnMap = new HashMap<>();

    public static SpnOverride getInstance() {
        if (sInstance == null) {
            synchronized (SpnOverride.class) {
                if (sInstance == null) {
                    sInstance = new SpnOverride();
                }
            }
        }
        return sInstance;
    }

    private SpnOverride() {
        loadSpnOverrides();
    }

    boolean containsCarrier(String carrier) {
        return this.mCarrierSpnMap.containsKey(carrier);
    }

    String getSpn(String carrier) {
        if (TelephonyManager.isCustForJpSb() && "44000".equals(carrier)) {
            return carrier;
        }
        return this.mCarrierSpnMap.get(carrier);
    }

    private void loadSpnOverrides() {
        File spnFile;
        File extSpnFile = null;
        if (TelephonyManager.isCarrierApexEnabled()) {
            spnFile = new File(PARTNER_APEX_SPN_OVERRIDE_PATH);
            Rlog.d(LOG_TAG, "loadSpnOverrides load apex spn file");
        } else if (CotaOpconfigManager.isOpconfigFileExist(CotaOpconfigManager.OpconfigFileType.Spn)) {
            spnFile = new File(CotaOpconfigManager.getOpconfigPath(CotaOpconfigManager.OpconfigFileType.Spn));
            Rlog.d(LOG_TAG, "loadSpnOverrides load opconfig spn file");
        } else {
            spnFile = new File(Environment.getProductDirectory(), PARTNER_SPN_OVERRIDE_PATH);
            if (isFileExist(EXT_SPN_OVERRIDE_PATH)) {
                extSpnFile = new File(EXT_SPN_OVERRIDE_PATH);
            }
        }
        File oemSpnFile = new File(Environment.getOemDirectory(), OEM_SPN_OVERRIDE_PATH);
        if (oemSpnFile.exists()) {
            long oemSpnTime = oemSpnFile.lastModified();
            long sysSpnTime = spnFile.lastModified();
            Rlog.d(LOG_TAG, "SPN Timestamp: oemTime = " + oemSpnTime + " sysTime = " + sysSpnTime);
            if (oemSpnTime > sysSpnTime) {
                Rlog.d(LOG_TAG, "SPN in OEM image is newer than System image");
                spnFile = oemSpnFile;
            }
        } else {
            Rlog.d(LOG_TAG, "No SPN in OEM image = " + oemSpnFile.getPath() + " Load SPN from system image");
        }
        loadSpnFile(spnFile);
        if (extSpnFile != null) {
            loadSpnFile(extSpnFile);
        }
    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    void loadSpnFile(File spnFile) {
        try {
            FileReader spnReader = new FileReader(spnFile);
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(spnReader);
                XmlUtils.beginDocument(parser, "spnOverrides");
                while (true) {
                    XmlUtils.nextElement(parser);
                    String name = parser.getName();
                    if ("spnOverride".equals(name)) {
                        String numeric = parser.getAttributeValue(null, "numeric");
                        String data = parser.getAttributeValue(null, "spn");
                        this.mCarrierSpnMap.put(numeric, data);
                    } else {
                        spnReader.close();
                        return;
                    }
                }
            } catch (IOException e) {
                Rlog.w(LOG_TAG, "Exception in spn-conf parser " + e);
            } catch (XmlPullParserException e2) {
                Rlog.w(LOG_TAG, "Exception in spn-conf parser " + e2);
            }
        } catch (FileNotFoundException e3) {
            Rlog.w(LOG_TAG, "Can not open " + spnFile.getAbsolutePath());
        }
    }
}
