package miui.telephony;

import android.app.ActivityThread;
import android.os.Environment;
import android.os.SystemProperties;
import android.provider.Settings;
import android.sysprop.TelephonyProperties;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.Xml;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.data.MiuiDataControllerImpl;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.util.XmlUtils;
import com.xiaomi.internal.telephony.SarDeviceStateListener;
import com.xiaomi.modem.ModemUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import miui.os.HyperOSCustFeatureResolve;
import org.xmlpull.v1.XmlPullParser;

/* JADX INFO: loaded from: classes.dex */
public class ServiceProviderUtils {
    private static final SparseArray<ServiceProvider> ALL_SERVICE_PROVIDER;
    private static final String CTS_CARRIER_NAME_OVERRIDE = "carrier_a";
    private static final boolean DEBUG;
    private static final String EXT_MIUI_SPN_OVERRIDE_PATH = "/mi_ext/product/etc/miui-spn-conf.xml";
    public static final String GSM_OPERATOR_ORIG_ALPHA = "gsm.operator.orig.alpha";
    public static final String GSM_SIM_OPERATOR_ORIG_ALPHA = "gsm.sim.operator.orig.alpha";
    private static final String LOG_TAG = "ServiceProvider";
    private static final String MIUI_APEX_SPN_OVERRIDE_PATH = "/apex/com.miui.opconfig/etc/spn/miui-spn-conf.xml";
    private static final String MIUI_SPN_OVERRIDE_PATH = "etc/miui-spn-conf.xml";
    private static final int MIUI_SPN_OVERRIDE_VERSION = 1;
    private static final String SETTING_PREFIX = "MOBILE_OPERATOR_NAME_";

    private interface IServiceProvider {
        int getBaseNumeric();

        Name getName();

        int[] getNumerics();

        boolean match(int i, String str, int i2);
    }

    static {
        DEBUG = SystemProperties.getInt(ModemUtils.PROP_ROOT_TYPE, 0) == 1;
        ALL_SERVICE_PROVIDER = new SparseArray<>();
        loadMiuiSpnOverride();
    }

    private static class Name {
        public String longAlpha;
        public String shortAlpha;

        private Name() {
        }

        public String toString() {
            return "longAlpha=[" + this.longAlpha + "], shortAlpha=[" + this.shortAlpha + "]";
        }
    }

    private static abstract class ServiceProviderBase implements IServiceProvider {
        private final Condition[] conditions;
        private Map<String, Name> names;

        public ServiceProviderBase(Map<String, Name> names, Condition[] conditions) {
            this.names = new HashMap();
            if (names == null || names.size() == 0 || names.get("") == null) {
                throw new RuntimeException("spn should have base name");
            }
            this.names = names;
            this.conditions = conditions;
        }

        @Override // miui.telephony.ServiceProviderUtils.IServiceProvider
        public boolean match(int slotId, String spn, int numeric) {
            if (this.conditions == null) {
                return true;
            }
            for (Condition c : this.conditions) {
                if (c.execute(slotId, spn, numeric)) {
                    return true;
                }
            }
            return false;
        }

        @Override // miui.telephony.ServiceProviderUtils.IServiceProvider
        public Name getName() {
            String locale;
            Name name = null;
            String locale2 = SystemProperties.get("persist.sys.locale");
            if (!TextUtils.isEmpty(locale2)) {
                locale = Locale.forLanguageTag(locale2).toString();
            } else {
                locale = Locale.getDefault().toString();
            }
            while (locale != null && name == null) {
                if (ServiceProviderUtils.DEBUG) {
                    Rlog.d(ServiceProviderUtils.LOG_TAG, "getName: locale=" + locale);
                }
                Name name2 = this.names.get(locale);
                name = name2;
                int end = locale.lastIndexOf(95);
                locale = end >= 0 ? locale.substring(0, end) : null;
            }
            if (name == null) {
                Name name3 = this.names.get("");
                return name3;
            }
            return name;
        }

        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append(this.names.get("").toString());
            if (this.names.size() > 1) {
                b.append(", {");
                boolean first = true;
                for (Map.Entry<String, Name> entry : this.names.entrySet()) {
                    if (entry.getKey().length() != 0) {
                        if (!first) {
                            b.append(", ");
                        }
                        first = false;
                        b.append('\"').append(entry.getKey()).append("\"=>\"").append(entry.getValue()).append('\"');
                    }
                }
                b.append('}');
            }
            if (this.conditions != null) {
                b.append(", condition=[");
                for (int i = 0; i < this.conditions.length; i++) {
                    if (i != 0) {
                        b.append(", ");
                    }
                    b.append(this.conditions[i]);
                }
                b.append("]");
            }
            return b.toString();
        }
    }

    private static class ServiceProvider extends ServiceProviderBase {
        private ArrayList<VirtualServiceProvider> mvnos;
        private final int[] numerics;

        public ServiceProvider(int[] numerics, Map<String, Name> names, Condition[] conditions) {
            super(names, conditions);
            if (numerics == null || numerics.length == 0) {
                throw new IllegalArgumentException("spn should have at least one numeric");
            }
            SparseIntArray sia = new SparseIntArray();
            for (int numeric : numerics) {
                sia.put(numeric, numeric);
            }
            this.numerics = new int[sia.size()];
            for (int i = 0; i < numerics.length; i++) {
                this.numerics[i] = sia.keyAt(i);
            }
            this.mvnos = new ArrayList<>();
        }

        public Collection<VirtualServiceProvider> getAllVirtualServiceProvider() {
            return this.mvnos;
        }

        public void addMvno(VirtualServiceProvider vsp) {
            this.mvnos.add(vsp);
        }

        @Override // miui.telephony.ServiceProviderUtils.IServiceProvider
        public int getBaseNumeric() {
            return this.numerics[0];
        }

        @Override // miui.telephony.ServiceProviderUtils.IServiceProvider
        public int[] getNumerics() {
            return this.numerics;
        }

        @Override // miui.telephony.ServiceProviderUtils.ServiceProviderBase
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("numeric=[");
            for (int i = 0; i < this.numerics.length; i++) {
                if (i != 0) {
                    b.append(", ");
                }
                b.append(this.numerics[i]);
            }
            b.append("], ").append(super.toString());
            return b.toString();
        }
    }

    private static abstract class Condition {
        private Condition() {
        }

        public boolean execute(int slotId, String spn, int numeric) {
            return false;
        }
    }

    private static class VirtualServiceProvider extends ServiceProviderBase {
        private final ServiceProvider provider;

        public VirtualServiceProvider(ServiceProvider provider, Map<String, Name> names, Condition[] conditions) {
            super(names, conditions);
            this.provider = provider;
        }

        @Override // miui.telephony.ServiceProviderUtils.IServiceProvider
        public int getBaseNumeric() {
            return this.provider.getBaseNumeric();
        }

        @Override // miui.telephony.ServiceProviderUtils.IServiceProvider
        public int[] getNumerics() {
            return this.provider.getNumerics();
        }

        @Override // miui.telephony.ServiceProviderUtils.ServiceProviderBase
        public String toString() {
            StringBuilder b = new StringBuilder();
            b.append("numeric=[");
            int[] numerics = this.provider.getNumerics();
            for (int i = 0; i < numerics.length; i++) {
                if (i != 0) {
                    b.append(", ");
                }
                b.append(numerics[i]);
            }
            b.append("], ").append(super.toString());
            return b.toString();
        }
    }

    private ServiceProviderUtils() {
    }

    public static String get(String numeric, int slotId) {
        return get(numeric, slotId, null, true);
    }

    public static String get(String numeric, String spn) {
        return get(numeric, -1, spn, true);
    }

    public static String get(String numeric, int slotId, boolean longName) {
        return get(numeric, slotId, null, longName);
    }

    public static String get(String numeric, String spn, boolean longName) {
        return get(numeric, -1, spn, longName);
    }

    public static String get(String numeric, int slotId, String spn) {
        return get(numeric, slotId, spn, true);
    }

    public static String get(String numeric, int phoneId, String spn, boolean longName) {
        String result = null;
        int slotId = phoneId;
        if (numeric != null && numeric.length() > 0) {
            result = getFromSettings(numeric);
            if (result == null || result.length() == 0) {
                if (noNeedOverride(phoneId) && !TextUtils.isEmpty(spn)) {
                    return spn;
                }
                if (slotId == SubscriptionManager.DEFAULT_SLOT_ID) {
                    slotId = SubscriptionManager.getDefault().getDefaultSlotId();
                }
                try {
                    IServiceProvider sp = getServiceProvider(Integer.parseInt(numeric), slotId, spn);
                    if (sp != null) {
                        Name name = sp.getName();
                        result = (longName || name.shortAlpha == null || name.shortAlpha.length() == 0) ? name.longAlpha : name.shortAlpha;
                    }
                } catch (Exception e) {
                    Rlog.w(LOG_TAG, "invalid numeric=" + numeric);
                }
            }
            if (result == null || result.length() == 0 || CTS_CARRIER_NAME_OVERRIDE.equals(spn)) {
                result = spn;
            }
        }
        if (DEBUG) {
            Rlog.d(LOG_TAG, "numeric=" + numeric + ", slotId=" + slotId + ", spn= " + spn + " => " + result);
        }
        return result;
    }

    public static boolean isVirtualServiceProvider(String numeric, int slotId, String spn) {
        if (numeric == null || numeric.length() <= 0) {
            return false;
        }
        try {
            int inumeric = Integer.parseInt(numeric);
            ServiceProvider sp = ALL_SERVICE_PROVIDER.get(inumeric);
            if (sp == null || (!SubscriptionManager.isRealSlotId(slotId) && TextUtils.isEmpty(spn))) {
                return false;
            }
            for (VirtualServiceProvider vsp : sp.getAllVirtualServiceProvider()) {
                if (vsp.match(slotId, spn, inumeric)) {
                    return true;
                }
            }
            return false;
        } catch (NumberFormatException e) {
            Rlog.e(LOG_TAG, "invalid numeric=" + numeric);
            return false;
        }
    }

    private static IServiceProvider getServiceProvider(int numeric, int slotId, String spn) {
        ServiceProvider sp = ALL_SERVICE_PROVIDER.get(numeric);
        IServiceProvider result = sp;
        if (sp != null && (SubscriptionManager.isRealSlotId(slotId) || !TextUtils.isEmpty(spn))) {
            for (VirtualServiceProvider vsp : sp.getAllVirtualServiceProvider()) {
                if (vsp.match(slotId, spn, numeric)) {
                    result = vsp;
                    break;
                }
            }
            if (!sp.match(slotId, spn, numeric)) {
                result = null;
            }
        }
        if (DEBUG) {
            Rlog.d(LOG_TAG, "getServiceProvider numeric=" + numeric + ", slotId=" + slotId + ", spn= " + spn + " => " + result);
        }
        return result;
    }

    public static boolean isSameServiceProvider(String numeric1, String numeric2) {
        if (numeric1 == null || numeric2 == null || numeric1.length() != numeric2.length()) {
            return false;
        }
        if (!numeric1.equals(numeric2)) {
            if (!numeric1.startsWith(numeric2.substring(0, 3))) {
                return false;
            }
            return isSameServiceProvider(Integer.parseInt(numeric1), Integer.parseInt(numeric2));
        }
        return true;
    }

    public static boolean isSameServiceProvider(int numeric1, int numeric2) {
        if (numeric1 == numeric2) {
            return true;
        }
        IServiceProvider sp1 = getServiceProvider(numeric1, -1, null);
        IServiceProvider sp2 = getServiceProvider(numeric2, -1, null);
        if (sp1 != null && sp2 != null) {
            if (sp1.getBaseNumeric() == sp2.getBaseNumeric()) {
                return true;
            }
            return false;
        }
        if (sp1 != null) {
            if (sp1.getBaseNumeric() == numeric2) {
                return true;
            }
            return false;
        }
        if (sp2 != null && sp2.getBaseNumeric() == numeric1) {
            return true;
        }
        return false;
    }

    private static String getFromSettings(String numeric) {
        return Settings.System.getString(ActivityThread.currentApplication().getContentResolver(), SETTING_PREFIX + numeric);
    }

    public static boolean isChineseServiceProvider(String numeric) {
        return numeric != null && numeric.length() == 5 && numeric.startsWith("460");
    }

    public static boolean isChinaMobile(String numeric) {
        if (numeric != null && numeric.length() >= 5) {
            try {
                return isSameServiceProvider(46000, Integer.parseInt(numeric.substring(0, 5)));
            } catch (NumberFormatException e) {
                Rlog.e(LOG_TAG, "invalid numeric=" + numeric);
            }
        }
        return false;
    }

    public static boolean isChinaUnicom(String numeric) {
        if (numeric != null && numeric.length() >= 5) {
            try {
                return isSameServiceProvider(46001, Integer.parseInt(numeric.substring(0, 5)));
            } catch (NumberFormatException e) {
                Rlog.e(LOG_TAG, "invalid numeric=" + numeric);
            }
        }
        return false;
    }

    public static boolean isChinaTelecom(String numeric) {
        if (numeric != null && numeric.length() >= 5) {
            try {
                return isSameServiceProvider(46003, Integer.parseInt(numeric.substring(0, 5)));
            } catch (NumberFormatException e) {
                Rlog.e(LOG_TAG, "invalid numeric=" + numeric);
            }
        }
        return false;
    }

    public static boolean isChinaBroadcast(String numeric) {
        if (numeric != null && numeric.length() >= 5) {
            try {
                return isSameServiceProvider(46015, Integer.parseInt(numeric.substring(0, 5)));
            } catch (NumberFormatException e) {
                Rlog.e(LOG_TAG, "invalid numeric=" + numeric);
            }
        }
        return false;
    }

    public static boolean isMacauTelecom(String numeric) {
        if (numeric != null && numeric.length() >= 5) {
            try {
                return isSameServiceProvider(45502, Integer.parseInt(numeric.substring(0, 5)));
            } catch (NumberFormatException e) {
                Rlog.e(LOG_TAG, "invalid numeric=" + numeric);
            }
        }
        return false;
    }

    public static boolean isChinaMobile(int mcc, int mnc) {
        if (mcc == 460) {
            return isSameServiceProvider(46000, mnc + 46000);
        }
        return false;
    }

    public static boolean isChinaUnicomSim(int mcc, int mnc) {
        if (mcc == 460) {
            return isSameServiceProvider(46001, 46000 + mnc);
        }
        return false;
    }

    public static boolean isChinaTelecomSim(int mcc, int mnc) {
        if (mcc == 460) {
            return isSameServiceProvider(46003, 46000 + mnc);
        }
        return false;
    }

    public static boolean isAPTSim(int slotId) {
        Phone phone = PhoneFactory.getPhone(slotId);
        if (phone == null || !phone.getIccRecordsLoaded()) {
            return false;
        }
        if ("46605".equals(phone.getOperatorNumeric())) {
            return true;
        }
        String gid1 = phone.getIccCard().getIccRecords().getGid1();
        if (gid1 != null) {
            return "52505".equals(phone.getOperatorNumeric()) && gid1.trim().startsWith("0A");
        }
        return false;
    }

    public static boolean isConfigOnlyShowPlmn(int slotId) {
        String[] JIO_NUMERIC_ARRAY = {"405840", "405854", "405855", "405856", "405857", "405858", "405859", "405860", "405861", "405862", "405863", "405864", "405865", "405866", "405867", "405868", "405869", "405870", "405871", "405872", "405873", "405874"};
        Phone phone = PhoneFactory.getPhone(slotId);
        if (phone == null || !phone.getIccRecordsLoaded()) {
            return false;
        }
        for (String numeric : JIO_NUMERIC_ARRAY) {
            if (numeric.equals(phone.getOperatorNumeric())) {
                return true;
            }
        }
        return false;
    }

    private static boolean noNeedOverride(int phoneId) {
        boolean noNeedOverride = false;
        if (TelephonyManager.isCustFeatureResolveEnabled()) {
            if (isHKTelecommSim(phoneId) && !isHKCountryIso()) {
                return true;
            }
            boolean noNeedOverride2 = HyperOSCustFeatureResolve.getBooleanForPhoneId(phoneId, "spn_no_need_override", false);
            return noNeedOverride2;
        }
        if (isEITSim(phoneId) || isRakutenInternationalRoaming(phoneId)) {
            noNeedOverride = true;
        }
        if (isHKTelecommSim(phoneId) && !isHKCountryIso()) {
            return true;
        }
        return noNeedOverride;
    }

    public static boolean isEITSim(int slotId) {
        Phone phone = PhoneFactory.getPhone(slotId);
        if (phone == null) {
            return false;
        }
        String gid1 = phone.getGroupIdLevel1();
        if (TextUtils.isEmpty(gid1)) {
            return false;
        }
        String gid2 = gid1.trim();
        String operatorNumeric = phone.getOperatorNumeric();
        return ("20826".equals(operatorNumeric) || "20601".equals(operatorNumeric) || "20801".equals(operatorNumeric)) && "4E524A31".equals(gid2);
    }

    private static boolean isHKTelecommSim(int slotId) {
        Phone phone = PhoneFactory.getPhone(slotId);
        if (phone == null) {
            return false;
        }
        String operatorNumeric = phone.getOperatorNumeric();
        return "45431".equals(operatorNumeric);
    }

    private static boolean isHKCountryIso() {
        return "hk".equals(TelephonyManager.getDefault().getNetworkCountryIso());
    }

    public static boolean isRakutenInternationalRoaming(int slotId) {
        Phone phone = PhoneFactory.getPhone(slotId);
        if (phone == null || phone.getIccRecords() == null) {
            return false;
        }
        IccRecords r = phone.getIccRecords();
        String operatorNumeric = r.getOperatorNumeric();
        String spn = r.getServiceProviderName();
        if ("20801".equals(operatorNumeric) && "Rakuten".equals(spn)) {
            return true;
        }
        return "44011".equals(operatorNumeric) && "Rakuten".equals(spn) && "jp_rk".equals(TelephonyManager.CUSTOMIZED_REGION);
    }

    private static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    private static void loadMiuiSpnOverride() {
        File miuiSpnFile;
        File extMiuiSpnFile = null;
        if (TelephonyManager.isCarrierApexEnabled()) {
            miuiSpnFile = new File(MIUI_APEX_SPN_OVERRIDE_PATH);
        } else if (CotaOpconfigManager.isOpconfigFileExist(CotaOpconfigManager.OpconfigFileType.MiuiSpn)) {
            miuiSpnFile = new File(CotaOpconfigManager.getOpconfigPath(CotaOpconfigManager.OpconfigFileType.MiuiSpn));
        } else {
            miuiSpnFile = new File(Environment.getProductDirectory(), MIUI_SPN_OVERRIDE_PATH);
            if (isFileExist(EXT_MIUI_SPN_OVERRIDE_PATH)) {
                Rlog.i(LOG_TAG, "Load ext spn conf ");
                extMiuiSpnFile = new File(EXT_MIUI_SPN_OVERRIDE_PATH);
            }
        }
        loadFromXml(miuiSpnFile);
        if (extMiuiSpnFile != null) {
            loadFromXml(extMiuiSpnFile);
        }
    }

    private static void loadFromXml(File file) {
        StringBuilder sb;
        XmlPullParser parser = null;
        try {
            try {
                XmlPullParser parser2 = Xml.newPullParser();
                parser2.setInput(new FileReader(file));
                XmlUtils.beginDocument(parser2, "spns");
                int version = Integer.parseInt(parser2.getAttributeValue(null, MiuiDataControllerImpl.Processor.PREFERENCE_KEY_VERSION));
                Rlog.i(LOG_TAG, "SPN file loaded version is " + version);
                if (version != 1) {
                    Rlog.w(LOG_TAG, "SPN file version does'nt match " + version + " : 1");
                    if (parser2 instanceof Closeable) {
                        try {
                            ((Closeable) parser2).close();
                            return;
                        } catch (IOException ioe) {
                            Rlog.w(LOG_TAG, "Exception in close parser " + ioe, ioe);
                            return;
                        }
                    }
                    return;
                }
                XmlUtils.nextElement(parser2);
                while (parser2.getEventType() != 1) {
                    String name = parser2.getName();
                    if ("spn".equals(name)) {
                        loadSpn(parser2);
                    }
                    XmlUtils.nextElement(parser2);
                }
                if (parser2 instanceof Closeable) {
                    try {
                        ((Closeable) parser2).close();
                    } catch (IOException e) {
                        ioe = e;
                        sb = new StringBuilder();
                        Rlog.w(LOG_TAG, sb.append("Exception in close parser ").append(ioe).toString(), ioe);
                    }
                }
            } catch (Exception e2) {
                Rlog.w(LOG_TAG, "Exception in miui-spn-conf parser " + e2, e2);
                if (parser instanceof Closeable) {
                    try {
                        ((Closeable) null).close();
                    } catch (IOException e3) {
                        ioe = e3;
                        sb = new StringBuilder();
                        Rlog.w(LOG_TAG, sb.append("Exception in close parser ").append(ioe).toString(), ioe);
                    }
                }
            }
        } catch (Throwable th) {
            if (parser instanceof Closeable) {
                try {
                    ((Closeable) null).close();
                } catch (IOException ioe2) {
                    Rlog.w(LOG_TAG, "Exception in close parser " + ioe2, ioe2);
                }
            }
            throw th;
        }
    }

    private static int[] readNumerics(String numericAttribute) {
        String[] numerics = numericAttribute.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE);
        SparseIntArray sia = new SparseIntArray();
        for (String numeric : numerics) {
            int num = Integer.parseInt(numeric);
            sia.put(num, num);
        }
        int[] result = new int[sia.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = sia.keyAt(i);
        }
        return result;
    }

    private static void loadSpn(XmlPullParser parser) {
        String numericAttribute = parser.getAttributeValue(null, "numeric");
        if (numericAttribute != null && numericAttribute.length() != 0) {
            int[] numerics = readNumerics(numericAttribute);
            Map<String, Name> names = new HashMap<>();
            Name name = new Name();
            int l = parser.getAttributeCount();
            for (int i = 0; i < l; i++) {
                String attribute = parser.getAttributeName(i);
                if ("alpha".equals(attribute)) {
                    name.longAlpha = parser.getAttributeValue(i);
                } else if ("short".equals(attribute)) {
                    name.shortAlpha = parser.getAttributeValue(i);
                } else if (attribute.startsWith("alpha-")) {
                    String attribute2 = attribute.substring("alpha-".length());
                    Name n = names.get(attribute2);
                    if (n == null) {
                        n = new Name();
                        names.put(attribute2, n);
                    }
                    n.longAlpha = parser.getAttributeValue(i);
                } else if (attribute.startsWith("short-")) {
                    String attribute3 = attribute.substring("short-".length());
                    Name n2 = names.get(attribute3);
                    if (n2 == null) {
                        n2 = new Name();
                        names.put(attribute3, n2);
                    }
                    n2.shortAlpha = parser.getAttributeValue(i);
                }
            }
            if (name.longAlpha == null) {
                return;
            }
            names.put("", name);
            Condition[] conditions = readConditions(parser.getAttributeValue(null, "condition"), names);
            String mvno = parser.getAttributeValue(null, "mvno");
            int i2 = 0;
            if (!"true".equals(mvno)) {
                ServiceProvider sp = new ServiceProvider(numerics, names, conditions);
                int length = numerics.length;
                while (i2 < length) {
                    ALL_SERVICE_PROVIDER.put(numerics[i2], sp);
                    i2++;
                }
                if (DEBUG) {
                    Rlog.d(LOG_TAG, "Loaded spn: " + sp);
                    return;
                }
                return;
            }
            HashSet<ServiceProvider> usedServiceProviders = new HashSet<>();
            int length2 = numerics.length;
            while (i2 < length2) {
                int numeric = numerics[i2];
                ServiceProvider sp2 = ALL_SERVICE_PROVIDER.get(numeric);
                if (sp2 == null) {
                    Map<String, Name> empty = new HashMap<>();
                    empty.put("", new Name());
                    sp2 = new ServiceProvider(new int[]{numeric}, empty, null);
                    ALL_SERVICE_PROVIDER.put(numeric, sp2);
                }
                if (!usedServiceProviders.contains(sp2)) {
                    usedServiceProviders.add(sp2);
                    VirtualServiceProvider vsp = new VirtualServiceProvider(sp2, names, conditions);
                    sp2.addMvno(vsp);
                    if (DEBUG) {
                        Rlog.d(LOG_TAG, "Loaded virtual spn: " + vsp);
                    }
                }
                i2++;
                numericAttribute = numericAttribute;
            }
        }
    }

    private static Condition[] readConditions(String condition, Map<String, Name> names) {
        if (TextUtils.isEmpty(condition)) {
            return null;
        }
        Map<String, String[]> conditionMap = new HashMap<>();
        for (String c : condition.split(";")) {
            String[] cs = c.split(":");
            if (cs.length == 2) {
                String type = cs[0].trim();
                String value = cs[1].trim();
                if (type.length() > 0 && value.length() > 0 && value.charAt(0) != ',') {
                    conditionMap.put(type, value.split(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE));
                }
            } else if ("alpha".equals(c.trim())) {
                conditionMap.put("alpha", new String[0]);
            } else if ("numericSpn".equals(c.trim())) {
                conditionMap.put("numericSpn", new String[0]);
            }
        }
        ArrayList<Condition> conditionList = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : conditionMap.entrySet()) {
            if ("alpha".equals(entry.getKey())) {
                HashSet<String> set = new HashSet<>();
                for (String s : entry.getValue()) {
                    set.add(s);
                }
                for (Name n : names.values()) {
                    set.add(n.longAlpha);
                    set.add(n.shortAlpha);
                }
                conditionList.add(new SpnCondition((String[]) set.toArray(new String[set.size()])));
            } else if ("imsi".equals(entry.getKey())) {
                conditionList.add(new ImsiCondition(entry.getValue()));
            } else if ("gid1".equals(entry.getKey())) {
                conditionList.add(new Gid1Condition(entry.getValue()));
            } else if ("numericSpn".equals(entry.getKey())) {
                conditionList.add(new NumericSpnCondition());
            } else if ("pnn".equals(entry.getKey())) {
                conditionList.add(new PnnCondition(entry.getValue()));
            }
        }
        return (Condition[]) conditionList.toArray(new Condition[conditionList.size()]);
    }

    private static class PnnCondition extends Condition {
        private final String[] mPnns;

        public PnnCondition(String[] pnns) {
            super();
            HashSet<String> set = new HashSet<>(pnns.length);
            for (int i = 0; i < pnns.length; i++) {
                if (pnns[i] != null) {
                    set.add(pnns[i].trim());
                }
            }
            int i2 = set.size();
            this.mPnns = (String[]) set.toArray(new String[i2]);
        }

        @Override // miui.telephony.ServiceProviderUtils.Condition
        public boolean execute(int slotId, String spn, int numeric) {
            Phone phone = PhoneFactory.getPhone(slotId);
            if (phone == null || phone.getIccRecords() == null || !phone.getIccRecords().getRecordsLoaded()) {
                return false;
            }
            String pnn = phone.getIccRecords().getPnnHomeName();
            if (!TextUtils.isEmpty(pnn)) {
                String trimPnn = pnn.trim();
                for (String s : this.mPnns) {
                    if (s.equals(trimPnn)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String toString() {
            return "CONDITION: when pnn=" + TextUtils.join(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE, this.mPnns);
        }
    }

    private static class SpnCondition extends Condition {
        private final String[] mSpns;

        public SpnCondition(String[] spns) {
            super();
            HashSet<String> set = new HashSet<>(spns.length);
            for (int i = 0; i < spns.length; i++) {
                if (spns[i] != null) {
                    set.add(spns[i].trim());
                }
            }
            int i2 = set.size();
            this.mSpns = (String[]) set.toArray(new String[i2]);
        }

        @Override // miui.telephony.ServiceProviderUtils.Condition
        public boolean execute(int slotId, String spn, int numeric) {
            String executeSpn = spn;
            if (TextUtils.isEmpty(executeSpn) && SubscriptionManager.isRealSlotId(slotId)) {
                executeSpn = TelephonyManager.getDefault().getSimOperatorNameForSlot(slotId);
            }
            if (!TextUtils.isEmpty(executeSpn)) {
                String trimSpn = executeSpn.trim();
                for (String s : this.mSpns) {
                    if (s.equals(trimSpn)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String toString() {
            return "CONDITION: when spn=" + TextUtils.join(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE, this.mSpns);
        }
    }

    public static boolean isInRange(String range, String match) {
        if (TextUtils.isEmpty(range) || TextUtils.isEmpty(match)) {
            return false;
        }
        String[] ranges = range.split("~");
        return ranges.length == 2 && match.compareTo(ranges[0]) >= 0 && match.compareTo(ranges[1]) <= 0;
    }

    private static class ImsiCondition extends Condition {
        private final String[] mImsis;

        public ImsiCondition(String[] imsis) {
            super();
            HashSet<String> set = new HashSet<>(imsis.length);
            for (int i = 0; i < imsis.length; i++) {
                if (imsis[i] != null) {
                    set.add(imsis[i].trim());
                }
            }
            int i2 = set.size();
            this.mImsis = (String[]) set.toArray(new String[i2]);
        }

        @Override // miui.telephony.ServiceProviderUtils.Condition
        public boolean execute(int slotId, String spn, int numeric) {
            String imsi;
            Phone phone = PhoneFactory.getPhone(slotId);
            if (phone == null || phone.getIccRecords() == null || !phone.getIccRecords().getRecordsLoaded() || (imsi = phone.getIccRecords().getIMSI()) == null) {
                return false;
            }
            int l = imsi.length();
            for (String m : this.mImsis) {
                if (ServiceProviderUtils.isInRange(m, imsi)) {
                    Rlog.d(ServiceProviderUtils.LOG_TAG, "matched range imsi.");
                    return true;
                }
                if (l == m.length()) {
                    for (int i = 0; i < l; i++) {
                        char c = m.charAt(i);
                        if (c != 'x' && c != 'X' && c != imsi.charAt(i)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        public String toString() {
            return "CONDITION: when imsi~=" + TextUtils.join(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE, this.mImsis);
        }
    }

    private static class Gid1Condition extends Condition {
        private final String[] mGid1s;

        public Gid1Condition(String[] gid1s) {
            super();
            HashSet<String> set = new HashSet<>(gid1s.length);
            for (int i = 0; i < gid1s.length; i++) {
                if (gid1s[i] != null) {
                    set.add(gid1s[i].trim());
                }
            }
            int i2 = set.size();
            this.mGid1s = (String[]) set.toArray(new String[i2]);
        }

        @Override // miui.telephony.ServiceProviderUtils.Condition
        public boolean execute(int slotId, String spn, int numeric) {
            String gid1;
            Phone phone = PhoneFactory.getPhone(slotId);
            if (phone != null && phone.getIccRecords() != null && phone.getIccRecords().getRecordsLoaded() && (gid1 = phone.getIccRecords().getGid1()) != null) {
                String gid2 = gid1.trim();
                for (String s : this.mGid1s) {
                    if (gid2.length() >= s.length() && gid2.substring(0, s.length()).equalsIgnoreCase(s)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public String toString() {
            return "CONDITION: when gid1=" + TextUtils.join(SarDeviceStateListener.SIM_MCC_DEFAULT_STATE, this.mGid1s);
        }
    }

    private static class NumericSpnCondition extends Condition {
        private NumericSpnCondition() {
            super();
        }

        @Override // miui.telephony.ServiceProviderUtils.Condition
        public boolean execute(int slotId, String spn, int numeric) {
            if (TextUtils.isEmpty(spn)) {
                return true;
            }
            try {
                return numeric == Integer.parseInt(spn.trim().replaceAll(" ", ""));
            } catch (Exception e) {
                Rlog.w(ServiceProviderUtils.LOG_TAG, "Exception in execute NumericSpnCondition", e);
                return false;
            }
        }

        public String toString() {
            return "CONDITION: when plmn is numeric";
        }
    }

    public static String onOperatorNumericOrNameSet(int slotId, String prop, String value) {
        if (TelephonyManager.isCustForJpKd()) {
            return value;
        }
        String alpha = null;
        String numeric = null;
        boolean isNumeric = false;
        String property = prop;
        Rlog.i(LOG_TAG, "onOperatorNumericOrNameSet...slotId = " + slotId + " , prop = " + prop + " , value = " + value);
        if ("gsm.sim.operator.alpha".equals(property)) {
            alpha = value;
            numeric = TelephonyManager.getTelephonyProperty(slotId, "gsm.sim.operator.numeric", null);
            setTelephonyProperty(slotId, GSM_SIM_OPERATOR_ORIG_ALPHA, value);
        } else if ("gsm.sim.operator.numeric".equals(property)) {
            numeric = value;
            alpha = TelephonyManager.getTelephonyProperty(slotId, GSM_SIM_OPERATOR_ORIG_ALPHA, null);
            isNumeric = true;
            property = "gsm.sim.operator.alpha";
        } else if ("gsm.operator.alpha".equals(property)) {
            alpha = value;
            numeric = TelephonyManager.getTelephonyProperty(slotId, "gsm.operator.numeric", null);
            setTelephonyProperty(slotId, GSM_OPERATOR_ORIG_ALPHA, value);
        } else if ("gsm.operator.numeric".equals(property)) {
            numeric = value;
            alpha = TelephonyManager.getTelephonyProperty(slotId, GSM_OPERATOR_ORIG_ALPHA, null);
            isNumeric = true;
            property = "gsm.operator.alpha";
        }
        if (numeric == null) {
            if (isNumeric) {
                setTelephonyProperty(slotId, property, alpha);
            }
            return value;
        }
        String newAlpha = get(numeric, slotId, alpha, true);
        if (newAlpha == null || newAlpha.length() == 0) {
            newAlpha = alpha;
        }
        if (isNumeric) {
            setTelephonyProperty(slotId, property, newAlpha);
            return value;
        }
        return newAlpha;
    }

    public static void setTelephonyProperty(int slotId, String property, String value) {
        if ("gsm.sim.operator.alpha".equals(property)) {
            List<String> newList = updateTelephonyProperty(TelephonyProperties.icc_operator_alpha(), slotId, value);
            TelephonyProperties.icc_operator_alpha(newList);
            return;
        }
        if ("gsm.sim.operator.numeric".equals(property)) {
            List<String> newList2 = updateTelephonyProperty(TelephonyProperties.icc_operator_numeric(), slotId, value);
            TelephonyProperties.icc_operator_numeric(newList2);
            return;
        }
        if ("gsm.operator.alpha".equals(property)) {
            List<String> newList3 = updateTelephonyProperty(TelephonyProperties.operator_alpha(), slotId, value);
            TelephonyProperties.operator_alpha(newList3);
            return;
        }
        if ("gsm.operator.numeric".equals(property)) {
            List<String> newList4 = updateTelephonyProperty(TelephonyProperties.operator_numeric(), slotId, value);
            TelephonyProperties.operator_numeric(newList4);
        } else if (GSM_SIM_OPERATOR_ORIG_ALPHA.equals(property)) {
            List<String> newList5 = updateTelephonyProperty(TelephonyProperties.icc_operator_orig_alpha(), slotId, value);
            TelephonyProperties.icc_operator_orig_alpha(newList5);
        } else if (GSM_OPERATOR_ORIG_ALPHA.equals(property)) {
            List<String> newList6 = updateTelephonyProperty(TelephonyProperties.operator_orig_alpha(), slotId, value);
            TelephonyProperties.operator_orig_alpha(newList6);
        }
    }

    public static <T> List<T> updateTelephonyProperty(List<T> prop, int phoneId, T value) {
        List<T> ret = new ArrayList<>(prop);
        while (ret.size() <= phoneId) {
            ret.add(null);
        }
        ret.set(phoneId, value);
        return ret;
    }
}
