package com.android.internal.telephony;

import android.R;
import android.app.role.RoleManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.XmlResourceParser;
import android.database.ContentObserver;
import android.os.Binder;
import android.os.Handler;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.AtomicFile;
import android.util.Xml;
import com.android.internal.hidden_from_bootclasspath.com.android.internal.telephony.flags.FeatureFlags;
import com.android.internal.telephony.util.XmlUtils;
import com.android.telephony.Rlog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class SmsUsageMonitor {
    public static final int PREMIUM_SMS_PERMISSION_ALWAYS_ALLOW = 3;
    public static final int PREMIUM_SMS_PERMISSION_ASK_USER = 1;
    public static final int PREMIUM_SMS_PERMISSION_NEVER_ALLOW = 2;
    public static final int PREMIUM_SMS_PERMISSION_UNKNOWN = 0;
    private final AtomicBoolean mCheckEnabled;
    private final int mCheckPeriod;
    private final Context mContext;
    private String mCurrentCountry;
    private ShortCodePatternMatcher mCurrentPatternMatcher;
    private final FeatureFlags mFeatureFlags;
    private final int mMaxAllowed;
    private final File mPatternFile;
    private long mPatternFileLastModified;
    private int mPatternFileVersion;
    private AtomicFile mPolicyFile;
    private final HashMap mPremiumSmsPolicy;
    private RoleManager mRoleManager;
    private final SettingsObserverHandler mSettingsObserverHandler;
    private final HashMap mSmsStamp = new HashMap();

    public interface SmsAuthorizationCallback {
        void onAuthorizationResult(boolean z);
    }

    public static int mergeShortCodeCategories(int i, int i2) {
        return i > i2 ? i : i2;
    }

    private static final class ShortCodePatternMatcher {
        private final Pattern mFreeShortCodePattern;
        private final Pattern mPremiumShortCodePattern;
        private final Pattern mShortCodePattern;
        private final Pattern mStandardShortCodePattern;

        ShortCodePatternMatcher(String str, String str2, String str3, String str4) {
            this.mShortCodePattern = str != null ? Pattern.compile(str) : null;
            this.mPremiumShortCodePattern = str2 != null ? Pattern.compile(str2) : null;
            this.mFreeShortCodePattern = str3 != null ? Pattern.compile(str3) : null;
            this.mStandardShortCodePattern = str4 != null ? Pattern.compile(str4) : null;
        }

        int getNumberCategory(String str) {
            Pattern pattern = this.mFreeShortCodePattern;
            if (pattern != null && pattern.matcher(str).matches()) {
                return 1;
            }
            Pattern pattern2 = this.mStandardShortCodePattern;
            if (pattern2 != null && pattern2.matcher(str).matches()) {
                return 2;
            }
            Pattern pattern3 = this.mPremiumShortCodePattern;
            if (pattern3 != null && pattern3.matcher(str).matches()) {
                return 4;
            }
            Pattern pattern4 = this.mShortCodePattern;
            return (pattern4 == null || !pattern4.matcher(str).matches()) ? 0 : 3;
        }
    }

    private static class SettingsObserver extends ContentObserver {
        private final Context mContext;
        private final AtomicBoolean mEnabled;

        SettingsObserver(Handler handler, Context context, AtomicBoolean atomicBoolean) {
            super(handler);
            this.mContext = context;
            this.mEnabled = atomicBoolean;
            onChange(false);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            this.mEnabled.set(Settings.Global.getInt(this.mContext.getContentResolver(), "sms_short_code_confirmation", 1) != 0);
        }
    }

    private static class SettingsObserverHandler extends Handler {
        SettingsObserverHandler(Context context, AtomicBoolean atomicBoolean) {
            context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("sms_short_code_confirmation"), false, new SettingsObserver(this, context, atomicBoolean));
        }
    }

    public SmsUsageMonitor(Context context, FeatureFlags featureFlags) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        this.mCheckEnabled = atomicBoolean;
        this.mPatternFile = new File("/data/misc/sms/codes");
        this.mPatternFileLastModified = 0L;
        this.mPatternFileVersion = -1;
        this.mPremiumSmsPolicy = new HashMap();
        this.mContext = context;
        this.mFeatureFlags = featureFlags;
        ContentResolver contentResolver = context.getContentResolver();
        this.mRoleManager = (RoleManager) context.getSystemService("role");
        this.mMaxAllowed = Settings.Global.getInt(contentResolver, "sms_outgoing_check_max_count", 30);
        this.mCheckPeriod = Settings.Global.getInt(contentResolver, "sms_outgoing_check_interval_ms", ServiceStateTracker.DEFAULT_GPRS_CHECK_PERIOD_MILLIS);
        this.mSettingsObserverHandler = new SettingsObserverHandler(context, atomicBoolean);
        loadPremiumSmsPolicyDb();
    }

    /* JADX WARN: Code duplicated, block: B:36:0x003c A[EXC_TOP_SPLITTER, PHI: r2
  0x003c: PHI (r2v5 java.io.FileReader) = (r2v4 java.io.FileReader), (r2v6 java.io.FileReader) binds: [B:19:0x003a, B:24:0x004d] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:40:0x005b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0022: MOVE (r1 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:9:0x0022 */
    private ShortCodePatternMatcher getPatternMatcherFromFile(String str) throws Throwable {
        FileReader fileReader;
        FileReader fileReader2;
        FileReader fileReader3 = null;
        try {
            try {
                fileReader = new FileReader(this.mPatternFile);
                try {
                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                    xmlPullParserNewPullParser.setInput(fileReader);
                    ShortCodePatternMatcher patternMatcherFromXmlParser = getPatternMatcherFromXmlParser(xmlPullParserNewPullParser, str);
                    this.mPatternFileLastModified = this.mPatternFile.lastModified();
                    try {
                        fileReader.close();
                    } catch (IOException unused) {
                    }
                    return patternMatcherFromXmlParser;
                } catch (FileNotFoundException unused2) {
                    Rlog.e("SmsUsageMonitor", "Short Code Pattern File not found");
                    this.mPatternFileLastModified = this.mPatternFile.lastModified();
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException unused3) {
                        }
                    }
                    return null;
                } catch (XmlPullParserException e) {
                    e = e;
                    Rlog.e("SmsUsageMonitor", "XML parser exception reading short code pattern file", e);
                    this.mPatternFileLastModified = this.mPatternFile.lastModified();
                    if (fileReader != null) {
                        fileReader.close();
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                fileReader3 = fileReader2;
                this.mPatternFileLastModified = this.mPatternFile.lastModified();
                if (fileReader3 != null) {
                    try {
                        fileReader3.close();
                    } catch (IOException unused4) {
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException unused5) {
            fileReader = null;
        } catch (XmlPullParserException e2) {
            e = e2;
            fileReader = null;
        } catch (Throwable th2) {
            th = th2;
            this.mPatternFileLastModified = this.mPatternFile.lastModified();
            if (fileReader3 != null) {
                fileReader3.close();
            }
            throw th;
        }
    }

    private ShortCodePatternMatcher getPatternMatcherFromResource(String str) {
        XmlResourceParser xml = null;
        try {
            xml = this.mContext.getResources().getXml(18284571);
            return getPatternMatcherFromXmlParser(xml, str);
        } finally {
            if (xml != null) {
                xml.close();
            }
        }
    }

    private ShortCodePatternMatcher getPatternMatcherFromXmlParser(XmlPullParser xmlPullParser, String str) {
        try {
            XmlUtils.beginDocument(xmlPullParser, "shortcodes");
            while (true) {
                XmlUtils.nextElement(xmlPullParser);
                String name = xmlPullParser.getName();
                if (name == null) {
                    Rlog.e("SmsUsageMonitor", "Parsing pattern data found null");
                    break;
                }
                if (name.equals("shortcode")) {
                    if (str.equals(xmlPullParser.getAttributeValue(null, "country"))) {
                        return new ShortCodePatternMatcher(xmlPullParser.getAttributeValue(null, "pattern"), xmlPullParser.getAttributeValue(null, "premium"), xmlPullParser.getAttributeValue(null, "free"), xmlPullParser.getAttributeValue(null, "standard"));
                    }
                } else {
                    Rlog.e("SmsUsageMonitor", "Error: skipping unknown XML tag " + name);
                }
            }
        } catch (IOException e) {
            Rlog.e("SmsUsageMonitor", "I/O exception reading short code patterns", e);
        } catch (XmlPullParserException e2) {
            Rlog.e("SmsUsageMonitor", "XML parser exception reading short code patterns", e2);
        }
        return null;
    }

    public boolean check(String str, int i) {
        synchronized (this.mSmsStamp) {
            try {
                removeExpiredTimestamps();
                ArrayList arrayList = (ArrayList) this.mSmsStamp.get(str);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    this.mSmsStamp.put(str, arrayList);
                }
                if (this.mRoleManager.getRoleHolders("android.app.role.SMS").contains(str)) {
                    return true;
                }
                return isUnderLimit(arrayList, i);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int checkDestination(String str, String str2) {
        String str3;
        synchronized (this.mSettingsObserverHandler) {
            try {
                TelephonyManager telephonyManager = (TelephonyManager) this.mContext.getSystemService(TelephonyManager.class);
                if (TelephonyCapabilities.supportsTelephonyCalling(this.mFeatureFlags, this.mContext) && telephonyManager != null && telephonyManager.isEmergencyNumber(str)) {
                    return 0;
                }
                if (!this.mCheckEnabled.get()) {
                    return 0;
                }
                if (str2 != null && ((str3 = this.mCurrentCountry) == null || !str2.equals(str3) || this.mPatternFile.lastModified() != this.mPatternFileLastModified)) {
                    if (this.mPatternFile.exists()) {
                        this.mCurrentPatternMatcher = getPatternMatcherFromFile(str2);
                        this.mPatternFileVersion = getPatternFileVersionFromFile();
                    } else {
                        this.mCurrentPatternMatcher = getPatternMatcherFromResource(str2);
                        this.mPatternFileVersion = -1;
                    }
                    this.mCurrentCountry = str2;
                }
                ShortCodePatternMatcher shortCodePatternMatcher = this.mCurrentPatternMatcher;
                if (shortCodePatternMatcher == null) {
                    Rlog.e("SmsUsageMonitor", "No patterns for \"" + str2 + "\": using generic short code rule");
                    return str.length() <= 5 ? 3 : 0;
                }
                return shortCodePatternMatcher.getNumberCategory(str);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:49:0x00d4 A[Catch: all -> 0x004a, IOException -> 0x00f5, PHI: r1
  0x00d4: PHI (r1v9 java.io.FileInputStream) = 
  (r1v6 java.io.FileInputStream)
  (r1v7 java.io.FileInputStream)
  (r1v8 java.io.FileInputStream)
  (r1v10 java.io.FileInputStream)
 binds: [B:48:0x00d2, B:55:0x00e9, B:61:0x00f4, B:53:0x00e1] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TRY_LEAVE, TryCatch #2 {IOException -> 0x00f5, blocks: (B:13:0x0045, B:49:0x00d4), top: B:73:0x0022 }] */
    private void loadPremiumSmsPolicyDb() {
        synchronized (this.mPremiumSmsPolicy) {
            if (this.mPolicyFile == null) {
                this.mPolicyFile = new AtomicFile(new File(new File("/data/misc/sms"), "premium_sms_policy.xml"));
                this.mPremiumSmsPolicy.clear();
                FileInputStream fileInputStream = null;
                try {
                    try {
                        try {
                            FileInputStream fileInputStreamOpenRead = this.mPolicyFile.openRead();
                            try {
                                try {
                                    XmlPullParser xmlPullParserNewPullParser = Xml.newPullParser();
                                    xmlPullParserNewPullParser.setInput(fileInputStreamOpenRead, StandardCharsets.UTF_8.name());
                                    XmlUtils.beginDocument(xmlPullParserNewPullParser, "premium-sms-policy");
                                    while (true) {
                                        XmlUtils.nextElement(xmlPullParserNewPullParser);
                                        String name = xmlPullParserNewPullParser.getName();
                                        if (name == null) {
                                            break;
                                        }
                                        if (name.equals("package")) {
                                            String attributeValue = xmlPullParserNewPullParser.getAttributeValue(null, "name");
                                            String attributeValue2 = xmlPullParserNewPullParser.getAttributeValue(null, "sms-policy");
                                            if (attributeValue == null) {
                                                Rlog.e("SmsUsageMonitor", "Error: missing package name attribute");
                                            } else if (attributeValue2 == null) {
                                                Rlog.e("SmsUsageMonitor", "Error: missing package policy attribute");
                                            } else {
                                                try {
                                                    this.mPremiumSmsPolicy.put(attributeValue, Integer.valueOf(Integer.parseInt(attributeValue2)));
                                                } catch (NumberFormatException unused) {
                                                    Rlog.e("SmsUsageMonitor", "Error: non-numeric policy type " + attributeValue2);
                                                }
                                            }
                                        } else {
                                            Rlog.e("SmsUsageMonitor", "Error: skipping unknown XML tag " + name);
                                        }
                                    }
                                    if (fileInputStreamOpenRead != null) {
                                        fileInputStreamOpenRead.close();
                                    }
                                } catch (NumberFormatException e) {
                                    e = e;
                                    fileInputStream = fileInputStreamOpenRead;
                                    Rlog.e("SmsUsageMonitor", "Unable to parse premium SMS policy database", e);
                                    if (fileInputStream != null) {
                                        fileInputStream.close();
                                    }
                                }
                            } catch (FileNotFoundException unused2) {
                                fileInputStream = fileInputStreamOpenRead;
                                if (fileInputStream != null) {
                                    fileInputStream.close();
                                }
                            } catch (IOException e2) {
                                e = e2;
                                fileInputStream = fileInputStreamOpenRead;
                                Rlog.e("SmsUsageMonitor", "Unable to read premium SMS policy database", e);
                                if (fileInputStream != null) {
                                    fileInputStream.close();
                                }
                            } catch (XmlPullParserException e3) {
                                e = e3;
                                fileInputStream = fileInputStreamOpenRead;
                                Rlog.e("SmsUsageMonitor", "Unable to parse premium SMS policy database", e);
                                if (fileInputStream != null) {
                                    fileInputStream.close();
                                }
                            } catch (Throwable th) {
                                th = th;
                                fileInputStream = fileInputStreamOpenRead;
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (IOException unused3) {
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } catch (FileNotFoundException unused4) {
                    } catch (IOException e4) {
                        e = e4;
                    } catch (NumberFormatException e5) {
                        e = e5;
                    } catch (XmlPullParserException e6) {
                        e = e6;
                    }
                } catch (IOException unused5) {
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writePremiumSmsPolicyDb() {
        FileOutputStream fileOutputStreamStartWrite;
        IOException e;
        synchronized (this.mPremiumSmsPolicy) {
            try {
                try {
                    fileOutputStreamStartWrite = this.mPolicyFile.startWrite();
                    try {
                        FastXmlSerializer fastXmlSerializer = new FastXmlSerializer();
                        fastXmlSerializer.setOutput(fileOutputStreamStartWrite, StandardCharsets.UTF_8.name());
                        fastXmlSerializer.startDocument(null, Boolean.TRUE);
                        fastXmlSerializer.startTag(null, "premium-sms-policy");
                        for (Map.Entry entry : this.mPremiumSmsPolicy.entrySet()) {
                            fastXmlSerializer.startTag(null, "package");
                            fastXmlSerializer.attribute(null, "name", (String) entry.getKey());
                            fastXmlSerializer.attribute(null, "sms-policy", ((Integer) entry.getValue()).toString());
                            fastXmlSerializer.endTag(null, "package");
                        }
                        fastXmlSerializer.endTag(null, "premium-sms-policy");
                        fastXmlSerializer.endDocument();
                        this.mPolicyFile.finishWrite(fileOutputStreamStartWrite);
                    } catch (IOException e2) {
                        e = e2;
                        Rlog.e("SmsUsageMonitor", "Unable to write premium SMS policy database", e);
                        if (fileOutputStreamStartWrite != null) {
                            this.mPolicyFile.failWrite(fileOutputStreamStartWrite);
                        }
                    }
                } catch (IOException e3) {
                    fileOutputStreamStartWrite = null;
                    e = e3;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public int getPremiumSmsPermission(String str) {
        checkCallerIsSystemOrPhoneOrSameApp(str);
        synchronized (this.mPremiumSmsPolicy) {
            try {
                Integer num = (Integer) this.mPremiumSmsPolicy.get(str);
                if (num == null) {
                    return 0;
                }
                return num.intValue();
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setPremiumSmsPermission(String str, int i) {
        checkCallerIsSystemOrPhoneApp();
        if (i < 1 || i > 3) {
            RILUtils$$ExternalSyntheticBUOutline0.m("invalid SMS permission type ", i);
            return;
        }
        synchronized (this.mPremiumSmsPolicy) {
            this.mPremiumSmsPolicy.put(str, Integer.valueOf(i));
        }
        new Thread(new Runnable() { // from class: com.android.internal.telephony.SmsUsageMonitor.1
            @Override // java.lang.Runnable
            public void run() {
                SmsUsageMonitor.this.writePremiumSmsPolicyDb();
            }
        }).start();
    }

    private boolean isSameAppIncludingPccUid(int i, int i2) {
        if (Process.isPrivateComputeCoreUid(i)) {
            i = this.mContext.getPackageManager().getAppUidForPrivateComputeCoreUid(i);
        }
        if (Process.isPrivateComputeCoreUid(i2)) {
            i2 = this.mContext.getPackageManager().getAppUidForPrivateComputeCoreUid(i2);
        }
        return UserHandle.isSameApp(i, i2);
    }

    public void authorizeOutgoingSms(PackageInfo packageInfo, String str, String str2, SmsAuthorizationCallback smsAuthorizationCallback, Handler handler) {
        smsAuthorizationCallback.onAuthorizationResult(true);
    }

    public boolean isSmsAuthorizationEnabled() {
        return this.mContext.getResources().getBoolean(R.bool.config_useDefaultFocusHighlight);
    }

    private void checkCallerIsSystemOrPhoneOrSameApp(String str) {
        int callingUid = Binder.getCallingUid();
        int appId = UserHandle.getAppId(callingUid);
        if (appId == 1000 || appId == 1001 || callingUid == 0) {
            return;
        }
        String str2 = "Calling uid " + callingUid + " gave package " + str + " which is either unknown or owned by another uid";
        try {
            if (isSameAppIncludingPccUid(this.mContext.getPackageManager().getApplicationInfoAsUser(str, 0, UserHandle.getUserHandleForUid(callingUid)).uid, callingUid)) {
            } else {
                throw new SecurityException(str2);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            SmsController$$ExternalSyntheticBUOutline0.m(str2);
        }
    }

    private static void checkCallerIsSystemOrPhoneApp() {
        int callingUid = Binder.getCallingUid();
        int appId = UserHandle.getAppId(callingUid);
        if (appId == 1000 || appId == 1001 || callingUid == 0) {
            return;
        }
        throw new SecurityException("Disallowed call for uid " + callingUid);
    }

    private void removeExpiredTimestamps() {
        long jCurrentTimeMillis = System.currentTimeMillis() - ((long) this.mCheckPeriod);
        synchronized (this.mSmsStamp) {
            try {
                Iterator it = this.mSmsStamp.entrySet().iterator();
                while (it.hasNext()) {
                    ArrayList arrayList = (ArrayList) ((Map.Entry) it.next()).getValue();
                    if (arrayList.isEmpty() || ((Long) arrayList.get(arrayList.size() - 1)).longValue() < jCurrentTimeMillis) {
                        it.remove();
                    }
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private boolean isUnderLimit(ArrayList arrayList, int i) {
        int i2;
        long jCurrentTimeMillis = System.currentTimeMillis();
        Long lValueOf = Long.valueOf(jCurrentTimeMillis);
        long j = jCurrentTimeMillis - ((long) this.mCheckPeriod);
        while (true) {
            if (arrayList.isEmpty() || ((Long) arrayList.get(0)).longValue() >= j) {
                break;
            }
            arrayList.remove(0);
        }
        if (arrayList.size() + i > this.mMaxAllowed) {
            return false;
        }
        for (i2 = 0; i2 < i; i2++) {
            arrayList.add(lValueOf);
        }
        return true;
    }

    private int getPatternFileVersionFromFile() throws Throwable {
        File file = new File("/data/misc/sms/metadata/version");
        if (!file.exists()) {
            return -1;
        }
        BufferedReader bufferedReader = null;
        try {
            try {
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file));
                    try {
                        String line = bufferedReader2.readLine();
                        if (line != null) {
                            int i = Integer.parseInt(line);
                            try {
                                bufferedReader2.close();
                                return i;
                            } catch (IOException e) {
                                Rlog.e("SmsUsageMonitor", "File reader exception closing short code pattern file version reader", e);
                                return i;
                            }
                        }
                        bufferedReader2.close();
                    } catch (IOException e2) {
                        e = e2;
                        bufferedReader = bufferedReader2;
                        Rlog.e("SmsUsageMonitor", "File reader exception reading short code pattern file version", e);
                        if (bufferedReader == null) {
                            return -1;
                        }
                        bufferedReader.close();
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader = bufferedReader2;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e3) {
                                Rlog.e("SmsUsageMonitor", "File reader exception closing short code pattern file version reader", e3);
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    Rlog.e("SmsUsageMonitor", "File reader exception closing short code pattern file version reader", e4);
                    return -1;
                }
            } catch (IOException e5) {
                e = e5;
            }
            return -1;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public int getShortCodeXmlFileVersion() {
        return this.mPatternFileVersion;
    }
}
