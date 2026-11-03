package com.android.internal.telephony.uicc;

import android.os.Environment;
import android.util.Xml;
import com.android.internal.telephony.util.XmlUtils;
import com.android.telephony.Rlog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class CarrierTestOverride {
    private HashMap mCarrierTestParamMap = new HashMap();

    CarrierTestOverride(int i) {
        loadCarrierTestOverrides(i);
    }

    boolean isInTestMode() {
        return this.mCarrierTestParamMap.containsKey("isInTestMode") && ((String) this.mCarrierTestParamMap.get("isInTestMode")).equals("true");
    }

    String getFakeSpn() {
        try {
            String str = (String) this.mCarrierTestParamMap.get("spn");
            Rlog.d("CarrierTestOverride", "reading spn from CarrierTestConfig file: " + str);
            return str;
        } catch (NullPointerException unused) {
            Rlog.w("CarrierTestOverride", "No spn in CarrierTestConfig file ");
            return null;
        }
    }

    String getFakeIMSI() {
        try {
            String str = (String) this.mCarrierTestParamMap.get("imsi");
            Rlog.d("CarrierTestOverride", "reading imsi from CarrierTestConfig file: " + str);
            return str;
        } catch (NullPointerException unused) {
            Rlog.w("CarrierTestOverride", "No imsi in CarrierTestConfig file ");
            return null;
        }
    }

    String getFakeGid1() {
        try {
            String str = (String) this.mCarrierTestParamMap.get("gid1");
            Rlog.d("CarrierTestOverride", "reading gid1 from CarrierTestConfig file: " + str);
            return str;
        } catch (NullPointerException unused) {
            Rlog.w("CarrierTestOverride", "No gid1 in CarrierTestConfig file ");
            return null;
        }
    }

    String getFakeGid2() {
        try {
            String str = (String) this.mCarrierTestParamMap.get("gid2");
            Rlog.d("CarrierTestOverride", "reading gid2 from CarrierTestConfig file: " + str);
            return str;
        } catch (NullPointerException unused) {
            Rlog.w("CarrierTestOverride", "No gid2 in CarrierTestConfig file ");
            return null;
        }
    }

    String getFakePnnHomeName() {
        try {
            String str = (String) this.mCarrierTestParamMap.get("pnn");
            Rlog.d("CarrierTestOverride", "reading pnn from CarrierTestConfig file: " + str);
            return str;
        } catch (NullPointerException unused) {
            Rlog.w("CarrierTestOverride", "No pnn in CarrierTestConfig file ");
            return null;
        }
    }

    String getFakeIccid() {
        try {
            String str = (String) this.mCarrierTestParamMap.get("iccid");
            Rlog.d("CarrierTestOverride", "reading iccid from CarrierTestConfig file: " + str);
            return str;
        } catch (NullPointerException unused) {
            Rlog.w("CarrierTestOverride", "No iccid in CarrierTestConfig file ");
            return null;
        }
    }

    void override(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.mCarrierTestParamMap.put("isInTestMode", "true");
        this.mCarrierTestParamMap.put("mccmnc", str);
        this.mCarrierTestParamMap.put("imsi", str2);
        this.mCarrierTestParamMap.put("iccid", str3);
        this.mCarrierTestParamMap.put("gid1", str4);
        this.mCarrierTestParamMap.put("gid2", str5);
        this.mCarrierTestParamMap.put("pnn", str6);
        this.mCarrierTestParamMap.put("spn", str7);
    }

    private void loadCarrierTestOverrides(int i) {
        String str = "/user_de/0/com.android.phone/files/carrier_test_conf_sim" + Integer.toString(i) + ".xml";
        Rlog.d("CarrierTestOverride", "File path : ".concat(str));
        File file = new File(Environment.getDataDirectory(), str);
        try {
            FileReader fileReader = new FileReader(file);
            Rlog.d("CarrierTestOverride", "CarrierTestConfig file Modified Timestamp: " + file.lastModified());
            try {
                XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                xmlPullParserNewPullParser.setInput(fileReader);
                XmlUtils.beginDocument(xmlPullParserNewPullParser, "carrierTestOverrides");
                while (true) {
                    XmlUtils.nextElement(xmlPullParserNewPullParser);
                    if ("carrierTestOverride".equals(xmlPullParserNewPullParser.getName())) {
                        String attributeValue = xmlPullParserNewPullParser.getAttributeValue(null, "key");
                        String attributeValue2 = xmlPullParserNewPullParser.getAttributeValue(null, "value");
                        Rlog.d("CarrierTestOverride", "extracting key-values from CarrierTestConfig file: " + attributeValue + "|" + attributeValue2);
                        this.mCarrierTestParamMap.put(attributeValue, attributeValue2);
                    } else {
                        fileReader.close();
                        return;
                    }
                }
            } catch (IOException e) {
                Rlog.w("CarrierTestOverride", "Exception in carrier_test_conf parser " + e);
            } catch (XmlPullParserException e2) {
                Rlog.w("CarrierTestOverride", "Exception in carrier_test_conf parser " + e2);
            }
        } catch (FileNotFoundException unused) {
            Rlog.w("CarrierTestOverride", "Can not open " + file.getAbsolutePath());
        }
    }
}
