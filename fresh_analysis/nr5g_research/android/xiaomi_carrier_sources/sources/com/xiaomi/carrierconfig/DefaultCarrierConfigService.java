package com.xiaomi.carrierconfig;

import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.service.carrier.CarrierIdentifier;
import android.service.carrier.CarrierService;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: compiled from: go/retraceme b4a584da9d92a5a351637a235eb001415db119a7a994af503b4f0ebfa9d3ca1c */
/* JADX INFO: loaded from: classes.dex */
public class DefaultCarrierConfigService extends CarrierService {
    public static final Map mCoatCarrierToCustRegionMap;
    public final DefaultCarrierConfigService mContext;
    public PersistableBundle mDefaultPb;
    public XmlPullParserFactory mFactory;

    static {
        HashMap map = new HashMap();
        mCoatCarrierToCustRegionMap = map;
        map.put("OR", "fr_orange");
        map.put("VF", "es_vodafone");
        map.put("SF", "fr_sfr");
        map.put("AT", "mx_at");
    }

    public DefaultCarrierConfigService() {
        Log.d("OEMCarrierConfigService", "OEM Carrier Config Service created");
        this.mFactory = null;
        this.mContext = this;
    }

    public static boolean isAttributeContainsValue(String str, String str2) {
        String[] strArrSplit;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && (strArrSplit = str.split(",")) != null && strArrSplit.length != 0) {
            for (String str3 : strArrSplit) {
                if (str3 != null && str2.equalsIgnoreCase(str3.trim())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isFileExist(String str) {
        File file = new File(str);
        Log.i("OEMCarrierConfigService", "isFileExist " + file.exists() + " " + str);
        return file.exists();
    }

    public static boolean isOpconfigFileExist(String str) {
        boolean zBooleanValue;
        try {
            Class<?> cls = Class.forName("miui.telephony.TelephonyManager");
            Method declaredMethod = cls.getDeclaredMethod("getDefault", null);
            declaredMethod.setAccessible(true);
            Object objInvoke = declaredMethod.invoke(null, null);
            Method declaredMethod2 = cls.getDeclaredMethod("isCotaOpconfigUsed", null);
            declaredMethod2.setAccessible(true);
            zBooleanValue = ((Boolean) declaredMethod2.invoke(objInvoke, null)).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
            zBooleanValue = false;
        }
        Log.i("OEMCarrierConfigService", "isCotaOpconfigUsed=" + zBooleanValue);
        if (zBooleanValue) {
            return new File(str).exists();
        }
        return false;
    }

    /* JADX WARN: Code duplicated, block: B:44:0x005a A[EXC_TOP_SPLITTER, PHI: r4
  0x005a: PHI (r4v4 java.io.FileReader) = (r4v2 java.io.FileReader), (r4v3 java.io.FileReader), (r4v5 java.io.FileReader) binds: [B:27:0x0058, B:32:0x0073, B:34:0x0087] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    public final boolean loadCarrierConfigFromXml(String str, PersistableBundle persistableBundle, CarrierIdentifier carrierIdentifier) throws Throwable {
        FileReader fileReader = null;
        try {
            try {
                FileReader fileReader2 = new FileReader(new File(str));
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    xmlPullParserNewPullParser.setInput(fileReader2);
                    PersistableBundle configFromXml = readConfigFromXml(xmlPullParserNewPullParser, carrierIdentifier);
                    PersistableBundle persistableBundle2 = this.mDefaultPb;
                    if (persistableBundle2 != null) {
                        persistableBundle.putAll(persistableBundle2);
                        this.mDefaultPb = null;
                    }
                    persistableBundle.putAll(configFromXml);
                    try {
                        fileReader2.close();
                    } catch (IOException unused) {
                    }
                    return true;
                } catch (FileNotFoundException e) {
                    e = e;
                    fileReader = fileReader2;
                    Log.e("OEMCarrierConfigService", "Path is not found exception=" + e.toString());
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException unused2) {
                        }
                    }
                    return false;
                } catch (IOException | XmlPullParserException e2) {
                    e = e2;
                    fileReader = fileReader2;
                    Log.e("OEMCarrierConfigService", "Load carrier config exception=" + e.toString());
                    if (fileReader != null) {
                        fileReader.close();
                    }
                    return false;
                } catch (Exception e3) {
                    e = e3;
                    fileReader = fileReader2;
                    Log.e("OEMCarrierConfigService", "Exception=" + e.toString());
                    if (fileReader != null) {
                        fileReader.close();
                    }
                    return false;
                } catch (Throwable th) {
                    th = th;
                    fileReader = fileReader2;
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException unused3) {
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (FileNotFoundException e4) {
            e = e4;
        } catch (IOException | XmlPullParserException e5) {
            e = e5;
        } catch (Exception e6) {
            e = e6;
        }
    }

    public final PersistableBundle loadMiuiCarrierConfig(CarrierIdentifier carrierIdentifier) throws Throwable {
        PersistableBundle persistableBundle = new PersistableBundle();
        if (!SystemProperties.getBoolean("ro.miui.carrier.apex", false)) {
            if (isOpconfigFileExist("/opconfig/common/carrierconfig/vendor_miui.xml")) {
                loadCarrierConfigFromXml("/opconfig/common/carrierconfig/vendor_miui.xml", persistableBundle, carrierIdentifier);
            } else {
                try {
                    persistableBundle.putAll(readConfigFromXml(this.mContext.getResources().getXml(R.xml.vendor_miui), carrierIdentifier));
                } catch (IOException | XmlPullParserException e) {
                    Log.e("OEMCarrierConfigService", "Load vendor_miui exception=" + e.toString());
                }
                if (isFileExist("/mi_ext/product/etc/vendor_miui.xml")) {
                    loadCarrierConfigFromXml("/mi_ext/product/etc/vendor_miui.xml", persistableBundle, carrierIdentifier);
                }
            }
            if (isOpconfigFileExist("/opconfig/common/carrierconfig/vendor_device.xml")) {
                loadCarrierConfigFromXml("/opconfig/common/carrierconfig/vendor_device.xml", persistableBundle, carrierIdentifier);
            } else {
                String str = SystemProperties.get("ro.product.device", "");
                String str2 = "device/" + str + "/res/xml/vendor_device.xml";
                try {
                    InputStream inputStreamOpen = this.mContext.getAssets().open(str2);
                    if (inputStreamOpen != null) {
                        inputStreamOpen.close();
                    }
                } catch (IOException unused) {
                    String strSubstring = SystemProperties.get("ro.product.mod_device", "");
                    if (!TextUtils.isEmpty(strSubstring)) {
                        int iIndexOf = strSubstring.indexOf(95);
                        if (iIndexOf > 0) {
                            strSubstring = strSubstring.substring(0, iIndexOf);
                        }
                        String str3 = "device/" + strSubstring + "/res/xml/vendor_device.xml";
                        if (!TextUtils.equals(str2, str3)) {
                            try {
                                InputStream inputStreamOpen2 = this.mContext.getAssets().open(str3);
                                if (inputStreamOpen2 != null) {
                                    inputStreamOpen2.close();
                                }
                                Log.d("OEMCarrierConfigService", "device config not found for " + str + ", use mod device name: " + strSubstring);
                                str2 = str3;
                            } catch (IOException unused2) {
                            }
                        }
                    }
                }
                Log.d("OEMCarrierConfigService", "will load config:".concat(str2));
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    xmlPullParserNewPullParser.setInput(this.mContext.getAssets().open(str2), "utf-8");
                    PersistableBundle configFromXml = readConfigFromXml(xmlPullParserNewPullParser, carrierIdentifier);
                    PersistableBundle persistableBundle2 = this.mDefaultPb;
                    if (persistableBundle2 != null) {
                        persistableBundle.putAll(persistableBundle2);
                        this.mDefaultPb = null;
                    }
                    persistableBundle.putAll(configFromXml);
                } catch (IOException | XmlPullParserException e2) {
                    Log.e("OEMCarrierConfigService", "Load vendor_device exception=" + e2.toString());
                }
                if (isFileExist("/mi_ext/product/etc/vendor_device.xml")) {
                    loadCarrierConfigFromXml("/mi_ext/product/etc/vendor_device.xml", persistableBundle, carrierIdentifier);
                }
            }
        } else {
            if (!loadCarrierConfigFromXml("/apex/com.miui.opconfig/etc/carrierconfig/vendor_miui.xml", persistableBundle, carrierIdentifier)) {
                Log.e("OEMCarrierConfigService", "Load vendor_miui failed");
                return persistableBundle;
            }
            if (!loadCarrierConfigFromXml("/apex/com.miui.opconfig/etc/carrierconfig/vendor_device.xml", persistableBundle, carrierIdentifier)) {
                Log.e("OEMCarrierConfigService", "Load vendor_device failed");
                return persistableBundle;
            }
        }
        return persistableBundle;
    }

    @Override // android.service.carrier.CarrierService
    public final PersistableBundle onLoadConfig(CarrierIdentifier carrierIdentifier) {
        Log.d("OEMCarrierConfigService", "Xiaomi config being fetched for id: " + carrierIdentifier);
        try {
            synchronized (this) {
                try {
                    if (this.mFactory == null) {
                        this.mFactory = XmlPullParserFactory.newInstance();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
            this.mFactory.newPullParser();
            return loadMiuiCarrierConfig(carrierIdentifier);
        } catch (XmlPullParserException e) {
            Log.e("OEMCarrierConfigService", "Failed to load Xiaomi config", e);
            return new PersistableBundle();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code duplicated, block: B:105:0x01af  */
    /* JADX WARN: Code duplicated, block: B:146:0x028e  */
    /* JADX WARN: Code duplicated, block: B:162:0x02d4  */
    /* JADX WARN: Code duplicated, block: B:203:0x038b A[PHI: r1
  0x038b: PHI (r1v11 char) = (r1v4 char), (r1v5 char), (r1v6 char), (r1v7 char), (r1v7 char), (r1v7 char), (r1v8 char), (r1v9 char), (r1v12 char) binds: [B:252:0x046a, B:247:0x045c, B:242:0x0450, B:233:0x0430, B:235:0x043a, B:237:0x0444, B:229:0x0428, B:216:0x03db, B:202:0x0389] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Code duplicated, block: B:204:0x038e A[PHI: r1 r2
  0x038e: PHI (r1v10 char) = 
  (r1v4 char)
  (r1v4 char)
  (r1v5 char)
  (r1v5 char)
  (r1v6 char)
  (r1v6 char)
  (r1v7 char)
  (r1v7 char)
  (r1v8 char)
  (r1v8 char)
  (r1v9 char)
  (r1v9 char)
  (r1v12 char)
  (r1v12 char)
 binds: [B:250:0x0462, B:252:0x046a, B:245:0x0456, B:247:0x045c, B:240:0x044a, B:242:0x0450, B:232:0x042e, B:237:0x0444, B:227:0x0422, B:229:0x0428, B:214:0x03d5, B:216:0x03db, B:200:0x0383, B:202:0x0389] A[DONT_GENERATE, DONT_INLINE]
  0x038e: PHI (r2v10 boolean) = 
  (r2v4 boolean)
  (r2v4 boolean)
  (r2v5 boolean)
  (r2v5 boolean)
  (r2v6 boolean)
  (r2v6 boolean)
  (r2v7 boolean)
  (r2v7 boolean)
  (r2v8 boolean)
  (r2v8 boolean)
  (r2v9 boolean)
  (r2v9 boolean)
  (r2v11 boolean)
  (r2v11 boolean)
 binds: [B:250:0x0462, B:252:0x046a, B:245:0x0456, B:247:0x045c, B:240:0x044a, B:242:0x0450, B:232:0x042e, B:237:0x0444, B:227:0x0422, B:229:0x0428, B:214:0x03d5, B:216:0x03db, B:200:0x0383, B:202:0x0389] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Switch 'out' block B:153:0x02b3 for B:104:0x01ac already processed. Defaulting to fallback option. */
    /* JADX WARN: Type inference failed for: r0v80, types: [java.lang.Object, java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r14v11 */
    /* JADX WARN: Type inference failed for: r14v12 */
    /* JADX WARN: Type inference failed for: r14v15 */
    /* JADX WARN: Type inference failed for: r14v16 */
    /* JADX WARN: Type inference failed for: r14v19 */
    /* JADX WARN: Type inference failed for: r14v20 */
    /* JADX WARN: Type inference failed for: r14v23 */
    /* JADX WARN: Type inference failed for: r14v24 */
    /* JADX WARN: Type inference failed for: r14v27 */
    /* JADX WARN: Type inference failed for: r14v28 */
    /* JADX WARN: Type inference failed for: r14v3 */
    /* JADX WARN: Type inference failed for: r14v31 */
    /* JADX WARN: Type inference failed for: r14v32 */
    /* JADX WARN: Type inference failed for: r14v35 */
    /* JADX WARN: Type inference failed for: r14v36 */
    /* JADX WARN: Type inference failed for: r14v39 */
    /* JADX WARN: Type inference failed for: r14v4 */
    /* JADX WARN: Type inference failed for: r14v40 */
    /* JADX WARN: Type inference failed for: r14v43 */
    /* JADX WARN: Type inference failed for: r14v44 */
    /* JADX WARN: Type inference failed for: r14v47 */
    /* JADX WARN: Type inference failed for: r14v48 */
    /* JADX WARN: Type inference failed for: r14v51 */
    /* JADX WARN: Type inference failed for: r14v52 */
    /* JADX WARN: Type inference failed for: r14v53 */
    /* JADX WARN: Type inference failed for: r14v64 */
    /* JADX WARN: Type inference failed for: r14v7 */
    /* JADX WARN: Type inference failed for: r14v8 */
    /* JADX WARN: Type inference failed for: r16v0 */
    /* JADX WARN: Type inference failed for: r16v1 */
    /* JADX WARN: Type inference failed for: r16v2 */
    /* JADX WARN: Type inference failed for: r16v3 */
    /* JADX WARN: Type inference failed for: r16v4 */
    /* JADX WARN: Type inference failed for: r16v5 */
    /* JADX WARN: Type inference failed for: r16v6 */
    /* JADX WARN: Type inference failed for: r16v7 */
    /* JADX WARN: Type inference failed for: r16v8 */
    /* JADX WARN: Type inference failed for: r16v9 */
    public final PersistableBundle readConfigFromXml(XmlPullParser xmlPullParser, CarrierIdentifier carrierIdentifier) throws XmlPullParserException, IOException {
        ?? r14;
        char c;
        boolean z;
        boolean zMatches;
        boolean z2;
        boolean z3;
        boolean zBooleanValue;
        XmlPullParser xmlPullParser2 = xmlPullParser;
        PersistableBundle persistableBundle = new PersistableBundle();
        if (xmlPullParser2 != null) {
            while (true) {
                int next = xmlPullParser2.next();
                boolean z4 = true;
                if (next != 1) {
                    char c2 = 2;
                    if (next == 2 && "carrier_config".equals(xmlPullParser2.getName())) {
                        String str = SystemProperties.get("ro.boot.product.vendor.sku", "");
                        String str2 = SystemProperties.get("ro.boot.product.hardware.sku", "");
                        boolean z5 = true;
                        int i = 0;
                        while (i < xmlPullParser2.getAttributeCount()) {
                            String attributeName = xmlPullParser2.getAttributeName(i);
                            String attributeValue = xmlPullParser2.getAttributeValue(i);
                            attributeName.hashCode();
                            ?? r16 = -1;
                            r16 = -1;
                            r16 = -1;
                            r16 = -1;
                            r16 = -1;
                            switch (attributeName.hashCode()) {
                                case -1335157162:
                                    r14 = !attributeName.equals("device") ? -1 : 0;
                                    break;
                                case -1085982027:
                                    r14 = !attributeName.equals("vendorSku") ? -1 : z4;
                                    break;
                                case -366168555:
                                    r14 = !attributeName.equals("hardwareSku") ? -1 : c2;
                                    break;
                                case 98494:
                                    r14 = !attributeName.equals("cid") ? -1 : 3;
                                    break;
                                case 107917:
                                    r14 = !attributeName.equals("mcc") ? -1 : 4;
                                    break;
                                case 108258:
                                    r14 = !attributeName.equals("mnc") ? -1 : 5;
                                    break;
                                case 113949:
                                    r14 = !attributeName.equals("sku") ? -1 : 6;
                                    break;
                                case 114097:
                                    r14 = !attributeName.equals("spn") ? -1 : 7;
                                    break;
                                case 3172527:
                                    r14 = !attributeName.equals("gid1") ? -1 : 8;
                                    break;
                                case 3172528:
                                    r14 = !attributeName.equals("gid2") ? -1 : 9;
                                    break;
                                case 3236474:
                                    r14 = !attributeName.equals("imsi") ? -1 : 10;
                                    break;
                                case 3373707:
                                    r14 = !attributeName.equals("name") ? -1 : 11;
                                    break;
                                case 100017508:
                                    r14 = !attributeName.equals("iccid") ? -1 : 12;
                                    break;
                                default:
                                    r14 = -1;
                                    break;
                            }
                            switch (r14) {
                                case 0:
                                    c = 2;
                                    z = false;
                                    if (z5 && isAttributeContainsValue(attributeValue, Build.DEVICE)) {
                                        z5 = true;
                                    } else {
                                        z5 = z;
                                    }
                                    break;
                                case 1:
                                    c = 2;
                                    z = false;
                                    if (z5 && isAttributeContainsValue(attributeValue, str)) {
                                        z5 = true;
                                    } else {
                                        z5 = z;
                                    }
                                    break;
                                case 2:
                                    c = 2;
                                    z = false;
                                    if (z5 && isAttributeContainsValue(attributeValue, str2)) {
                                        z5 = true;
                                    } else {
                                        z5 = z;
                                    }
                                    break;
                                case 3:
                                    c = 2;
                                    z = false;
                                    if (z5 && (carrierIdentifier == null || Integer.parseInt(attributeValue) == carrierIdentifier.getCarrierId() || Integer.parseInt(attributeValue) == carrierIdentifier.getSpecificCarrierId())) {
                                        z5 = true;
                                    } else {
                                        z5 = z;
                                    }
                                    break;
                                case 4:
                                    c = 2;
                                    z = false;
                                    if (carrierIdentifier != null) {
                                        String mcc = carrierIdentifier.getMcc();
                                        if (!attributeValue.contains(",")) {
                                            try {
                                                attributeValue = Integer.parseInt(attributeValue.trim()) + "";
                                            } catch (NumberFormatException e) {
                                                Log.e("OEMCarrierConfigService", "checkFilters mccValue", e);
                                            }
                                            try {
                                                mcc = Integer.parseInt(mcc.trim()) + "";
                                                break;
                                            } catch (NumberFormatException unused) {
                                            }
                                        }
                                        if (z5 && attributeValue.equals(mcc)) {
                                            z5 = true;
                                        } else {
                                            z5 = z;
                                        }
                                        break;
                                    }
                                    break;
                                case 5:
                                    c = 2;
                                    z = false;
                                    if (carrierIdentifier != null) {
                                        String mnc = carrierIdentifier.getMnc();
                                        if (!attributeValue.contains(",")) {
                                            try {
                                                attributeValue = Integer.parseInt(attributeValue.trim()) + "";
                                            } catch (NumberFormatException e2) {
                                                Log.e("OEMCarrierConfigService", "checkFilters mncValue", e2);
                                            }
                                            try {
                                                mnc = Integer.parseInt(mnc.trim()) + "";
                                                break;
                                            } catch (NumberFormatException unused2) {
                                            }
                                        }
                                        if (z5 && isAttributeContainsValue(attributeValue, mnc)) {
                                            z5 = true;
                                        } else {
                                            z5 = z;
                                        }
                                        break;
                                    }
                                    break;
                                case 6:
                                    c = 2;
                                    z = false;
                                    if (z5 && attributeValue.equalsIgnoreCase("")) {
                                        z5 = true;
                                    } else {
                                        z5 = z;
                                    }
                                    break;
                                case 7:
                                    if (z5) {
                                        if (carrierIdentifier != null) {
                                            String spn = carrierIdentifier.getSpn();
                                            if ("null".equalsIgnoreCase(attributeValue)) {
                                                zMatches = TextUtils.isEmpty(spn);
                                                c = 2;
                                            } else if (spn != null) {
                                                c = 2;
                                                zMatches = Pattern.compile(attributeValue, 2).matcher(spn).matches();
                                            } else {
                                                c = 2;
                                                zMatches = false;
                                            }
                                            if (zMatches) {
                                            }
                                            Log.i("OEMCarrierConfigService", "checkFilters spn result:" + z5);
                                        } else {
                                            c = 2;
                                        }
                                        z5 = true;
                                        Log.i("OEMCarrierConfigService", "checkFilters spn result:" + z5);
                                    } else {
                                        c = 2;
                                    }
                                    z5 = false;
                                    Log.i("OEMCarrierConfigService", "checkFilters spn result:" + z5);
                                    break;
                                case 8:
                                    if (carrierIdentifier != null) {
                                        int length = attributeValue.length();
                                        String gid1 = carrierIdentifier.getGid1();
                                        if (!z5 || gid1 == null || gid1.length() < length) {
                                            z2 = false;
                                        } else {
                                            z2 = false;
                                            if (attributeValue.equalsIgnoreCase(gid1.substring(0, length))) {
                                                z5 = true;
                                            }
                                            Log.i("OEMCarrierConfigService", "checkFilters gid1: " + gid1 + " value:" + attributeValue + " result:" + z5);
                                        }
                                        z5 = z2;
                                        Log.i("OEMCarrierConfigService", "checkFilters gid1: " + gid1 + " value:" + attributeValue + " result:" + z5);
                                    }
                                    c = 2;
                                    break;
                                case 9:
                                    if (z5 && (carrierIdentifier == null || attributeValue.equalsIgnoreCase(carrierIdentifier.getGid2()))) {
                                        z5 = true;
                                    } else {
                                        z5 = false;
                                    }
                                    c = 2;
                                    break;
                                case 10:
                                    if (z5) {
                                        if (carrierIdentifier != null) {
                                            String imsi = carrierIdentifier.getImsi();
                                            if (!(imsi != null ? Pattern.compile(attributeValue, 2).matcher(imsi).matches() : false)) {
                                                z5 = false;
                                            }
                                        }
                                        z5 = true;
                                    } else {
                                        z5 = false;
                                    }
                                    Log.i("OEMCarrierConfigService", "checkFilters imsi result:" + z5);
                                    c = 2;
                                    break;
                                case 11:
                                case 12:
                                    c = 2;
                                    break;
                                default:
                                    Log.e("OEMCarrierConfigService", "Unknown attribute " + attributeName + "=" + attributeValue);
                                    PersistableBundle configFromXml = null;
                                    if (TextUtils.equals(attributeName, "include")) {
                                        Log.i("OEMCarrierConfigService", "loadMiuiDefaultImsConfig");
                                        try {
                                            XmlPullParserFactory xmlPullParserFactoryNewInstance = XmlPullParserFactory.newInstance();
                                            xmlPullParserFactoryNewInstance.setNamespaceAware(z4);
                                            XmlPullParser xmlPullParserNewPullParser = xmlPullParserFactoryNewInstance.newPullParser();
                                            xmlPullParserNewPullParser.setInput(this.mContext.getAssets().open("default/" + attributeValue), "UTF-8");
                                            configFromXml = this.readConfigFromXml(xmlPullParserNewPullParser, carrierIdentifier);
                                        } catch (IOException | XmlPullParserException e3) {
                                            Log.e("OEMCarrierConfigService", "Load  defaultIMSConfig exception=" + e3.toString());
                                        }
                                        if (configFromXml != null) {
                                            PersistableBundle persistableBundle2 = this.mDefaultPb;
                                            if (persistableBundle2 != null) {
                                                persistableBundle2.putAll(configFromXml);
                                            } else {
                                                this.mDefaultPb = configFromXml;
                                            }
                                            z5 = z4;
                                        } else {
                                            z5 = false;
                                        }
                                        break;
                                    } else {
                                        attributeName.hashCode();
                                        switch (attributeName.hashCode()) {
                                            case -1154292251:
                                                if (attributeName.equals("build_region")) {
                                                    r16 = 0;
                                                }
                                                break;
                                            case 113722:
                                                if (attributeName.equals("sdk")) {
                                                    r16 = z4;
                                                }
                                                break;
                                            case 1350576736:
                                                if (attributeName.equals("cust_region")) {
                                                    r16 = 2;
                                                }
                                                break;
                                            case 1469634222:
                                                if (attributeName.equals("nr_support_mode")) {
                                                    r16 = 3;
                                                }
                                                break;
                                        }
                                        switch (r16) {
                                            case 0:
                                                String str3 = SystemProperties.get("ro.miui.build.region", "");
                                                if (z5 && attributeValue.equalsIgnoreCase(str3)) {
                                                    z5 = true;
                                                } else {
                                                    z5 = false;
                                                }
                                                break;
                                            case 1:
                                                if (z5) {
                                                    try {
                                                        if (Build.VERSION.SDK_INT >= Integer.valueOf(attributeValue).intValue()) {
                                                            z5 = true;
                                                        }
                                                    } catch (NumberFormatException unused3) {
                                                        Log.e("OEMCarrierConfigService", "sdk is not an integer value: " + attributeValue);
                                                        z4 = false;
                                                        z5 = z4;
                                                    }
                                                }
                                                z5 = false;
                                                break;
                                            case 2:
                                                boolean z6 = z4;
                                                String str4 = SystemProperties.get("ro.miui.customized.region", "");
                                                String str5 = SystemProperties.get("persist.sys.cota.carrier", "");
                                                Map map = mCoatCarrierToCustRegionMap;
                                                String str6 = map != null ? (String) map.get(str5) : "";
                                                z5 = (z5 && (attributeValue.equalsIgnoreCase(str4) || attributeValue.equalsIgnoreCase(str6))) ? z6 : false;
                                                Log.i("OEMCarrierConfigService", "checkMiuiFilters custRegion : " + str4 + " , cotaCarrier : " + str5 + " , cotaCarrierMapVal : " + str6 + " newResult:" + z5 + " value:" + attributeValue);
                                                break;
                                            case 3:
                                                try {
                                                    Class<?> cls = Class.forName("miui.telephony.TelephonyManager");
                                                    try {
                                                        Method declaredMethod = cls.getDeclaredMethod("getDefault", null);
                                                        try {
                                                            declaredMethod.setAccessible(true);
                                                            Object objInvoke = declaredMethod.invoke(null, null);
                                                            Method declaredMethod2 = cls.getDeclaredMethod("isFiveGCapable", null);
                                                            z3 = true;
                                                            try {
                                                                declaredMethod2.setAccessible(true);
                                                                zBooleanValue = ((Boolean) declaredMethod2.invoke(objInvoke, null)).booleanValue();
                                                            } catch (Exception e4) {
                                                                e = e4;
                                                                Log.e("OEMCarrierConfigService", "Failed to check 5G capability: " + e);
                                                                zBooleanValue = false;
                                                            }
                                                        } catch (Exception e5) {
                                                            e = e5;
                                                            z3 = true;
                                                        }
                                                    } catch (Exception e6) {
                                                        e = e6;
                                                        z3 = true;
                                                    }
                                                } catch (Exception e7) {
                                                    e = e7;
                                                    z3 = z4;
                                                }
                                                z4 = (zBooleanValue && z5 && Integer.valueOf(attributeValue).intValue() > 0) ? z3 : false;
                                                Log.i("OEMCarrierConfigService", "checkMiuiFilters isFiveGSupported: " + zBooleanValue + " newResult:" + z4 + " value:" + attributeValue);
                                                z5 = z4;
                                                break;
                                            default:
                                                z5 = false;
                                                break;
                                        }
                                    }
                                    c = 2;
                                    break;
                            }
                            i++;
                            xmlPullParser2 = xmlPullParser;
                            c2 = c;
                            z4 = true;
                            this = this;
                        }
                        if (z5) {
                            persistableBundle.putAll(PersistableBundle.restoreFromXml(xmlPullParser));
                        }
                    }
                    xmlPullParser2 = xmlPullParser;
                }
            }
        }
        return persistableBundle;
    }
}
