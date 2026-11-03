package com.android.carrierconfig;

import android.os.Build;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import android.service.carrier.CarrierIdentifier;
import android.service.carrier.CarrierService;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: loaded from: classes.dex */
public class DefaultCarrierConfigService extends CarrierService {
    private XmlPullParserFactory mFactory;

    public DefaultCarrierConfigService() {
        Log.d("DefaultCarrierConfigService", "Service created");
        this.mFactory = null;
    }

    @Override // android.service.carrier.CarrierService
    public PersistableBundle onLoadConfig(CarrierIdentifier carrierIdentifier) {
        Log.d("DefaultCarrierConfigService", "Config being fetched");
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
            return loadConfig(this.mFactory.newPullParser(), carrierIdentifier);
        } catch (XmlPullParserException e) {
            Log.e("DefaultCarrierConfigService", "Failed to load config", e);
            return new PersistableBundle();
        }
    }

    PersistableBundle getNoSimConfig(XmlPullParser xmlPullParser, String str) {
        PersistableBundle persistableBundle = new PersistableBundle();
        try {
            InputStream inputStreamOpenAsset = openAsset("carrier_config_no_sim.xml");
            try {
                xmlPullParser.setInput(inputStreamOpenAsset, "utf-8");
                PersistableBundle configFromXml = readConfigFromXml(xmlPullParser, null, str);
                configFromXml.putAll(readConfigFromXml(getApplicationContext().getResources().getXml(R.xml.vendor_no_sim), null, str));
                if (inputStreamOpenAsset == null) {
                    return configFromXml;
                }
                inputStreamOpenAsset.close();
                return configFromXml;
            } catch (Throwable th) {
                if (inputStreamOpenAsset != null) {
                    try {
                        inputStreamOpenAsset.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e("DefaultCarrierConfigService", "Failed to load config for no SIM", e);
            return persistableBundle;
        }
    }

    String[] listAssets(String str) throws IOException {
        return getApplicationContext().getAssets().list(str);
    }

    InputStream openAsset(String str) {
        return getApplicationContext().getAssets().open(str);
    }

    /* JADX WARN: Code duplicated, block: B:68:0x0134 A[Catch: IOException | XmlPullParserException -> 0x0174, IOException | XmlPullParserException -> 0x0174, TRY_LEAVE, TryCatch #1 {IOException | XmlPullParserException -> 0x0174, blocks: (B:3:0x000f, B:6:0x0018, B:6:0x0018, B:8:0x0058, B:8:0x0058, B:10:0x0079, B:10:0x0079, B:13:0x0086, B:13:0x0086, B:55:0x0112, B:55:0x0112, B:66:0x012e, B:66:0x012e, B:68:0x0134, B:68:0x0134, B:71:0x0162, B:71:0x0162, B:80:0x0172, B:80:0x0172, B:79:0x016f, B:79:0x016f, B:58:0x011a, B:58:0x011a, B:61:0x0122, B:61:0x0122, B:22:0x0097, B:22:0x0097, B:21:0x0094, B:21:0x0094, B:23:0x0098, B:23:0x0098, B:25:0x00b4, B:25:0x00b4, B:28:0x00c2, B:28:0x00c2, B:53:0x010a, B:53:0x010a, B:37:0x00d2, B:37:0x00d2, B:36:0x00cf, B:36:0x00cf, B:38:0x00d3, B:38:0x00d3, B:40:0x00eb, B:40:0x00eb, B:43:0x00f9, B:43:0x00f9, B:52:0x0109, B:52:0x0109, B:51:0x0106, B:51:0x0106), top: B:87:0x000f }] */
    /* JADX WARN: Code duplicated, block: B:71:0x0162 A[Catch: IOException | XmlPullParserException -> 0x0174, IOException | XmlPullParserException -> 0x0174, TRY_ENTER, TRY_LEAVE, TryCatch #1 {IOException | XmlPullParserException -> 0x0174, blocks: (B:3:0x000f, B:6:0x0018, B:6:0x0018, B:8:0x0058, B:8:0x0058, B:10:0x0079, B:10:0x0079, B:13:0x0086, B:13:0x0086, B:55:0x0112, B:55:0x0112, B:66:0x012e, B:66:0x012e, B:68:0x0134, B:68:0x0134, B:71:0x0162, B:71:0x0162, B:80:0x0172, B:80:0x0172, B:79:0x016f, B:79:0x016f, B:58:0x011a, B:58:0x011a, B:61:0x0122, B:61:0x0122, B:22:0x0097, B:22:0x0097, B:21:0x0094, B:21:0x0094, B:23:0x0098, B:23:0x0098, B:25:0x00b4, B:25:0x00b4, B:28:0x00c2, B:28:0x00c2, B:53:0x010a, B:53:0x010a, B:37:0x00d2, B:37:0x00d2, B:36:0x00cf, B:36:0x00cf, B:38:0x00d3, B:38:0x00d3, B:40:0x00eb, B:40:0x00eb, B:43:0x00f9, B:43:0x00f9, B:52:0x0109, B:52:0x0109, B:51:0x0106, B:51:0x0106), top: B:87:0x000f }] */
    /* JADX WARN: Code duplicated, block: B:81:0x0173 A[RETURN] */
    /* JADX WARN: Instruction removed from duplicated block: B:68:0x0134, please report this as an issue */
    private PersistableBundle getMatchedCarrierConfig(XmlPullParser xmlPullParser, CarrierIdentifier carrierIdentifier, String str) {
        PersistableBundle persistableBundle;
        PersistableBundle persistableBundle2;
        InputStream inputStreamOpenAsset;
        PersistableBundle persistableBundle3 = new PersistableBundle();
        try {
            if (carrierIdentifier.getCarrierId() != -1) {
                PersistableBundle persistableBundle4 = new PersistableBundle();
                PersistableBundle persistableBundle5 = new PersistableBundle();
                PersistableBundle persistableBundle6 = new PersistableBundle();
                int carrierIdFromMccMnc = ((TelephonyManager) getApplicationContext().getSystemService(TelephonyManager.class)).getCarrierIdFromMccMnc(carrierIdentifier.getMcc() + carrierIdentifier.getMnc());
                String[] strArrListAssets = listAssets("");
                int length = strArrListAssets.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        persistableBundle = persistableBundle3;
                        break;
                    }
                    String str2 = strArrListAssets[i];
                    StringBuilder sb = new StringBuilder();
                    sb.append("carrier_config_carrierid_");
                    persistableBundle = persistableBundle3;
                    sb.append(carrierIdentifier.getSpecificCarrierId());
                    sb.append("_");
                    if (str2.startsWith(sb.toString())) {
                        InputStream inputStreamOpenAsset2 = openAsset(str2);
                        try {
                            xmlPullParser.setInput(inputStreamOpenAsset2, "utf-8");
                            persistableBundle5 = readConfigFromXml(xmlPullParser, null, str);
                            if (inputStreamOpenAsset2 == null) {
                                break;
                            }
                            inputStreamOpenAsset2.close();
                            break;
                        } catch (Throwable th) {
                            if (inputStreamOpenAsset2 == null) {
                                throw th;
                            }
                            try {
                                inputStreamOpenAsset2.close();
                                throw th;
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                                throw th;
                            }
                        }
                    }
                    if (str2.startsWith("carrier_config_carrierid_" + carrierIdentifier.getCarrierId() + "_")) {
                        InputStream inputStreamOpenAsset3 = openAsset(str2);
                        try {
                            xmlPullParser.setInput(inputStreamOpenAsset3, "utf-8");
                            persistableBundle4 = readConfigFromXml(xmlPullParser, null, str);
                            if (inputStreamOpenAsset3 != null) {
                                inputStreamOpenAsset3.close();
                            }
                        } catch (Throwable th3) {
                            if (inputStreamOpenAsset3 == null) {
                                throw th3;
                            }
                            try {
                                inputStreamOpenAsset3.close();
                                throw th3;
                            } catch (Throwable th4) {
                                th3.addSuppressed(th4);
                                throw th3;
                            }
                        }
                    } else {
                        if (str2.startsWith("carrier_config_carrierid_" + carrierIdFromMccMnc + "_")) {
                            InputStream inputStreamOpenAsset4 = openAsset(str2);
                            try {
                                xmlPullParser.setInput(inputStreamOpenAsset4, "utf-8");
                                persistableBundle6 = readConfigFromXml(xmlPullParser, null, str);
                                if (inputStreamOpenAsset4 != null) {
                                    inputStreamOpenAsset4.close();
                                }
                            } catch (Throwable th5) {
                                if (inputStreamOpenAsset4 == null) {
                                    throw th5;
                                }
                                try {
                                    inputStreamOpenAsset4.close();
                                    throw th5;
                                } catch (Throwable th6) {
                                    th5.addSuppressed(th6);
                                    throw th5;
                                }
                            }
                        } else {
                            continue;
                        }
                    }
                    i++;
                    persistableBundle3 = persistableBundle;
                }
                if (!persistableBundle5.isEmpty()) {
                    persistableBundle2 = persistableBundle5;
                } else if (!persistableBundle4.isEmpty()) {
                    persistableBundle2 = persistableBundle4;
                } else if (!persistableBundle6.isEmpty()) {
                    persistableBundle2 = persistableBundle6;
                }
                if (persistableBundle2.isEmpty()) {
                    return persistableBundle2;
                }
                inputStreamOpenAsset = openAsset("carrier_config_mccmnc_" + carrierIdentifier.getMcc() + carrierIdentifier.getMnc() + ".xml");
                try {
                    xmlPullParser.setInput(inputStreamOpenAsset, "utf-8");
                    PersistableBundle configFromXml = readConfigFromXml(xmlPullParser, carrierIdentifier, str);
                    if (inputStreamOpenAsset != null) {
                        inputStreamOpenAsset.close();
                    }
                    return configFromXml;
                } catch (Throwable th7) {
                    if (inputStreamOpenAsset == null) {
                        throw th7;
                    }
                    try {
                        inputStreamOpenAsset.close();
                        throw th7;
                    } catch (Throwable th8) {
                        th7.addSuppressed(th8);
                        throw th7;
                    }
                }
            }
            persistableBundle = persistableBundle3;
            persistableBundle2 = persistableBundle;
            if (persistableBundle2.isEmpty()) {
                return persistableBundle2;
            }
            inputStreamOpenAsset = openAsset("carrier_config_mccmnc_" + carrierIdentifier.getMcc() + carrierIdentifier.getMnc() + ".xml");
            xmlPullParser.setInput(inputStreamOpenAsset, "utf-8");
            PersistableBundle configFromXml2 = readConfigFromXml(xmlPullParser, carrierIdentifier, str);
            if (inputStreamOpenAsset != null) {
                inputStreamOpenAsset.close();
            }
            return configFromXml2;
        } catch (IOException | XmlPullParserException e) {
            Log.d("DefaultCarrierConfigService", e.toString());
            return new PersistableBundle();
        }
    }

    PersistableBundle loadConfig(XmlPullParser xmlPullParser, CarrierIdentifier carrierIdentifier) {
        String string = getApplicationContext().getResources().getString(R.string.sku_filter);
        if (carrierIdentifier == null) {
            return getNoSimConfig(xmlPullParser, string);
        }
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putAll(getMatchedCarrierConfig(xmlPullParser, carrierIdentifier, string));
        try {
            persistableBundle.putAll(readConfigFromXml(getApplicationContext().getResources().getXml(R.xml.vendor), carrierIdentifier, string));
            return persistableBundle;
        } catch (IOException | XmlPullParserException e) {
            Log.e("DefaultCarrierConfigService", e.toString());
            return persistableBundle;
        }
    }

    static PersistableBundle readConfigFromXml(XmlPullParser xmlPullParser, CarrierIdentifier carrierIdentifier, String str) throws XmlPullParserException, IOException {
        PersistableBundle persistableBundle = new PersistableBundle();
        if (xmlPullParser != null) {
            while (true) {
                int next = xmlPullParser.next();
                if (next == 1) {
                    break;
                }
                if (next == 2 && "carrier_config".equals(xmlPullParser.getName()) && checkFilters(xmlPullParser, carrierIdentifier, str)) {
                    persistableBundle.putAll(PersistableBundle.restoreFromXml(xmlPullParser));
                }
            }
        }
        return persistableBundle;
    }

    /* JADX WARN: Code duplicated, block: B:66:0x00f5  */
    static boolean checkFilters(XmlPullParser xmlPullParser, CarrierIdentifier carrierIdentifier, String str) {
        String str2 = SystemProperties.get("ro.boot.product.vendor.sku", "");
        String str3 = SystemProperties.get("ro.boot.product.hardware.sku", "");
        int i = 0;
        while (true) {
            boolean zEqualsIgnoreCase = true;
            if (i >= xmlPullParser.getAttributeCount()) {
                return true;
            }
            String attributeName = xmlPullParser.getAttributeName(i);
            String attributeValue = xmlPullParser.getAttributeValue(i);
            attributeName.hashCode();
            switch (attributeName) {
                case "device":
                    zEqualsIgnoreCase = attributeValue.equalsIgnoreCase(Build.DEVICE);
                    break;
                case "vendorSku":
                    zEqualsIgnoreCase = attributeValue.equalsIgnoreCase(str2);
                    break;
                case "hardwareSku":
                    zEqualsIgnoreCase = attributeValue.equalsIgnoreCase(str3);
                    break;
                case "cid":
                    if (carrierIdentifier != null && Integer.parseInt(attributeValue) != carrierIdentifier.getCarrierId() && Integer.parseInt(attributeValue) != carrierIdentifier.getSpecificCarrierId()) {
                        zEqualsIgnoreCase = false;
                        break;
                    }
                    break;
                case "mcc":
                    if (carrierIdentifier != null && !attributeValue.equals(carrierIdentifier.getMcc())) {
                        zEqualsIgnoreCase = false;
                        break;
                    }
                    break;
                case "mnc":
                    if (carrierIdentifier != null && !attributeValue.equals(carrierIdentifier.getMnc())) {
                        zEqualsIgnoreCase = false;
                        break;
                    }
                    break;
                case "sku":
                    zEqualsIgnoreCase = attributeValue.equalsIgnoreCase(str);
                    break;
                case "spn":
                    if (carrierIdentifier != null && !matchOnSP(attributeValue, carrierIdentifier)) {
                        zEqualsIgnoreCase = false;
                        break;
                    }
                    break;
                case "gid1":
                    if (carrierIdentifier != null && !attributeValue.equalsIgnoreCase(carrierIdentifier.getGid1())) {
                        zEqualsIgnoreCase = false;
                        break;
                    }
                    break;
                case "gid2":
                    if (carrierIdentifier != null && !attributeValue.equalsIgnoreCase(carrierIdentifier.getGid2())) {
                        zEqualsIgnoreCase = false;
                        break;
                    }
                    break;
                case "imsi":
                    if (carrierIdentifier != null && !matchOnImsi(attributeValue, carrierIdentifier)) {
                        zEqualsIgnoreCase = false;
                        break;
                    }
                    break;
                case "name":
                    break;
                case "board":
                    zEqualsIgnoreCase = attributeValue.equalsIgnoreCase(Build.BOARD);
                    break;
                case "iccid":
                    zEqualsIgnoreCase = matchOnIccid(attributeValue, carrierIdentifier);
                    break;
                default:
                    Log.e("DefaultCarrierConfigService", "Unknown attribute " + attributeName + "=" + attributeValue);
                    zEqualsIgnoreCase = false;
                    break;
            }
            if (!zEqualsIgnoreCase) {
                return false;
            }
            i++;
        }
    }

    static boolean matchOnImsi(String str, CarrierIdentifier carrierIdentifier) {
        String imsi = carrierIdentifier.getImsi();
        if (imsi != null) {
            return Pattern.compile(str, 2).matcher(imsi).matches();
        }
        return false;
    }

    static boolean matchOnSP(String str, CarrierIdentifier carrierIdentifier) {
        String spn = carrierIdentifier.getSpn();
        if ("null".equalsIgnoreCase(str)) {
            return TextUtils.isEmpty(spn);
        }
        if (spn != null) {
            return Pattern.compile(str, 2).matcher(spn).matches();
        }
        return false;
    }

    static boolean matchOnIccid(String str, CarrierIdentifier carrierIdentifier) {
        String iccid = carrierIdentifier.getIccid();
        if ("null".equalsIgnoreCase(str)) {
            if (TextUtils.isEmpty(iccid)) {
                return true;
            }
        } else if (iccid != null) {
            for (String str2 : str.split(",")) {
                if (iccid.startsWith(str2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
