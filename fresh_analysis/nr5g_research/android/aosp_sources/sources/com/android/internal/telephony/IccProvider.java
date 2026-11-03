package com.android.internal.telephony;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyFrameworkInitializer;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.IccConstants;
import com.android.telephony.Rlog;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class IccProvider extends ContentProvider {
    protected static final int ADN = 1;
    protected static final int ADN_ALL = 7;
    protected static final int ADN_SUB = 2;
    private static final boolean DBG = true;
    protected static final int FDN = 3;
    protected static final int FDN_SUB = 4;
    protected static final int SDN = 5;
    protected static final int SDN_SUB = 6;
    public static final String STR_NEW_ANRS = "newAnrs";
    public static final String STR_NEW_EMAILS = "newEmails";
    public static final String STR_NEW_NUMBER = "newNumber";
    public static final String STR_NEW_TAG = "newTag";
    public static final String STR_PIN2 = "pin2";
    public static final String STR_TAG = "tag";
    private static final UriMatcher URL_MATCHER;
    private SubscriptionManager mSubscriptionManager;
    public static final String STR_NUMBER = "number";
    public static final String STR_EMAILS = "emails";
    public static final String STR_ANRS = "anrs";
    private static final String[] ADDRESS_BOOK_COLUMN_NAMES = {"name", STR_NUMBER, STR_EMAILS, STR_ANRS, "_id"};
    static IccInternalInterface sMtkIccProvider = null;

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        URL_MATCHER = uriMatcher;
        uriMatcher.addURI("icc", "adn", 1);
        uriMatcher.addURI("icc", "adn/subId/#", 2);
        uriMatcher.addURI("icc", "fdn", 3);
        uriMatcher.addURI("icc", "fdn/subId/#", 4);
        uriMatcher.addURI("icc", "sdn", 5);
        uriMatcher.addURI("icc", "sdn/subId/#", 6);
    }

    private static IccInternalInterface makeIccProvider(UriMatcher uriMatcher, Context context) {
        if (SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", "0").equals("0")) {
            try {
                Class<?> cls = Class.forName("com.mediatek.internal.telephony.phb.MtkIccProvider", false, ClassLoader.getSystemClassLoader());
                Rlog.d("IccProvider", "class = " + cls);
                Constructor<?> constructor = cls.getConstructor(UriMatcher.class, Context.class);
                Rlog.d("IccProvider", "constructor function = " + constructor);
                return (IccInternalInterface) constructor.newInstance(uriMatcher, context);
            } catch (Exception unused) {
                Rlog.e("IccProvider", "No MtkIccProvider! Used AOSP for instead!");
                return null;
            }
        }
        Rlog.e("IccProvider", "No MtkIccProvider! Used AOSP for instead!");
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        this.mSubscriptionManager = SubscriptionManager.from(getContext());
        sMtkIccProvider = makeIccProvider(URL_MATCHER, getContext());
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        log("query");
        IccInternalInterface iccInternalInterface = sMtkIccProvider;
        if (iccInternalInterface != null) {
            return iccInternalInterface.query(uri, strArr, str, strArr2, str2);
        }
        switch (URL_MATCHER.match(uri)) {
            case 1:
                return loadFromEf(28474, SubscriptionManager.getDefaultSubscriptionId());
            case 2:
                return loadFromEf(28474, getRequestSubId(uri));
            case 3:
                return loadFromEf(IccConstants.EF_FDN, SubscriptionManager.getDefaultSubscriptionId());
            case 4:
                return loadFromEf(IccConstants.EF_FDN, getRequestSubId(uri));
            case 5:
                return loadFromEf(IccConstants.EF_SDN, SubscriptionManager.getDefaultSubscriptionId());
            case 6:
                return loadFromEf(IccConstants.EF_SDN, getRequestSubId(uri));
            case 7:
                return loadAllSimContacts(28474);
            default:
                RILUtils$$ExternalSyntheticBUOutline1.m("Unknown URL ", uri);
                return null;
        }
    }

    private Cursor loadAllSimContacts(int i) {
        Cursor[] cursorArr;
        List activeSubscriptionInfoList = this.mSubscriptionManager.getActiveSubscriptionInfoList(false);
        if (activeSubscriptionInfoList == null || activeSubscriptionInfoList.size() == 0) {
            cursorArr = new Cursor[0];
        } else {
            int size = activeSubscriptionInfoList.size();
            cursorArr = new Cursor[size];
            for (int i2 = 0; i2 < size; i2++) {
                int subscriptionId = ((SubscriptionInfo) activeSubscriptionInfoList.get(i2)).getSubscriptionId();
                cursorArr[i2] = loadFromEf(i, subscriptionId);
                Rlog.i("IccProvider", "ADN Records loaded for Subscription ::" + subscriptionId);
            }
        }
        return new MergeCursor(cursorArr);
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        switch (URL_MATCHER.match(uri)) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                return "vnd.android.cursor.dir/sim-contact";
            default:
                RILUtils$$ExternalSyntheticBUOutline1.m("Unknown URL ", uri);
                return null;
        }
    }

    /* JADX WARN: Code duplicated, block: B:22:0x00a7 A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:23:0x00a8  */
    /* JADX WARN: Code duplicated, block: B:25:0x00b1 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:26:0x00b3 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:27:0x00b5 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:29:0x00b8  */
    /* JADX WARN: Code duplicated, block: B:30:0x00be  */
    /* JADX WARN: Code duplicated, block: B:31:0x00c4  */
    /* JADX WARN: Code duplicated, block: B:32:0x00ca  */
    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        int defaultSubscriptionId;
        String str;
        ContentValues contentValues2;
        StringBuilder sb;
        String asString;
        IccInternalInterface iccInternalInterface = sMtkIccProvider;
        if (iccInternalInterface != null) {
            Uri uriInsert = iccInternalInterface.insert(uri, contentValues);
            getContext().getContentResolver().notifyChange(uri, null);
            return uriInsert;
        }
        log("insert");
        int iMatch = URL_MATCHER.match(uri);
        int i = 28474;
        if (iMatch == 1) {
            defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        } else {
            if (iMatch != 2) {
                if (iMatch == 3) {
                    defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
                    asString = contentValues.getAsString(STR_PIN2);
                } else if (iMatch == 4) {
                    defaultSubscriptionId = getRequestSubId(uri);
                    asString = contentValues.getAsString(STR_PIN2);
                } else {
                    throw new UnsupportedOperationException("Cannot insert into URL: " + uri);
                }
                str = asString;
                i = 28475;
            } else {
                defaultSubscriptionId = getRequestSubId(uri);
            }
            String asString2 = contentValues.getAsString(STR_TAG);
            String asString3 = contentValues.getAsString(STR_NUMBER);
            String asString4 = contentValues.getAsString(STR_EMAILS);
            String asString5 = contentValues.getAsString(STR_ANRS);
            contentValues2 = new ContentValues();
            contentValues2.put(STR_NEW_TAG, asString2);
            contentValues2.put(STR_NEW_NUMBER, asString3);
            contentValues2.put(STR_NEW_EMAILS, asString4);
            contentValues2.put(STR_NEW_ANRS, asString5);
            if (!updateIccRecordInEf(i, contentValues2, str, defaultSubscriptionId)) {
                return null;
            }
            sb = new StringBuilder("content://icc/");
            if (iMatch != 1) {
                sb.append("adn/");
            } else if (iMatch != 2) {
                sb.append("adn/subId/");
            } else if (iMatch != 3) {
                sb.append("fdn/");
            } else if (iMatch == 4) {
                sb.append("fdn/subId/");
            }
            sb.append(0);
            Uri uri2 = Uri.parse(sb.toString());
            getContext().getContentResolver().notifyChange(uri, null);
            return uri2;
        }
        str = null;
        String asString6 = contentValues.getAsString(STR_TAG);
        String asString7 = contentValues.getAsString(STR_NUMBER);
        String asString8 = contentValues.getAsString(STR_EMAILS);
        String asString9 = contentValues.getAsString(STR_ANRS);
        contentValues2 = new ContentValues();
        contentValues2.put(STR_NEW_TAG, asString6);
        contentValues2.put(STR_NEW_NUMBER, asString7);
        contentValues2.put(STR_NEW_EMAILS, asString8);
        contentValues2.put(STR_NEW_ANRS, asString9);
        if (!updateIccRecordInEf(i, contentValues2, str, defaultSubscriptionId)) {
            return null;
        }
        sb = new StringBuilder("content://icc/");
        if (iMatch != 1) {
            sb.append("adn/");
        } else if (iMatch != 2) {
            sb.append("adn/subId/");
        } else if (iMatch != 3) {
            sb.append("fdn/");
        } else if (iMatch == 4) {
            sb.append("fdn/subId/");
        }
        sb.append(0);
        Uri uri3 = Uri.parse(sb.toString());
        getContext().getContentResolver().notifyChange(uri, null);
        return uri3;
    }

    private String normalizeValue(String str) {
        int length = str.length();
        if (length == 0) {
            log("len of input String is 0");
            return str;
        }
        if (str.charAt(0) == '\'') {
            int i = length - 1;
            if (str.charAt(i) == '\'') {
                return str.substring(1, i);
            }
        }
        return str;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        int defaultSubscriptionId;
        int i;
        IccInternalInterface iccInternalInterface = sMtkIccProvider;
        if (iccInternalInterface != null) {
            int iDelete = iccInternalInterface.delete(uri, str, strArr);
            if (iDelete <= 0) {
                return iDelete;
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return iDelete;
        }
        int iMatch = URL_MATCHER.match(uri);
        int i2 = 28474;
        int i3 = 1;
        if (iMatch == 1) {
            defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        } else if (iMatch != 2) {
            if (iMatch == 3) {
                defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
            } else if (iMatch == 4) {
                defaultSubscriptionId = getRequestSubId(uri);
            } else {
                throw new UnsupportedOperationException("Cannot insert into URL: " + uri);
            }
            i2 = 28475;
        } else {
            defaultSubscriptionId = getRequestSubId(uri);
        }
        log("delete");
        String[] strArrSplit = str.split(" AND ");
        int length = strArrSplit.length;
        String strNormalizeValue = null;
        String strNormalizeValue2 = null;
        String strNormalizeValue3 = null;
        String strNormalizeValue4 = null;
        String strNormalizeValue5 = null;
        while (true) {
            int i4 = length - 1;
            i = i3;
            if (i4 < 0) {
                break;
            }
            String str2 = strArrSplit[i4];
            String[] strArr2 = strArrSplit;
            log("parsing '" + str2 + "'");
            String[] strArrSplit2 = str2.split("=", 2);
            if (strArrSplit2.length != 2) {
                Rlog.e("IccProvider", "resolve: bad whereClause parameter: ".concat(str2));
            } else {
                String strTrim = strArrSplit2[0].trim();
                String strTrim2 = strArrSplit2[i].trim();
                if (STR_TAG.equals(strTrim)) {
                    strNormalizeValue = normalizeValue(strTrim2);
                } else if (STR_NUMBER.equals(strTrim)) {
                    strNormalizeValue2 = normalizeValue(strTrim2);
                } else if (STR_EMAILS.equals(strTrim)) {
                    strNormalizeValue3 = normalizeValue(strTrim2);
                } else if (STR_ANRS.equals(strTrim)) {
                    strNormalizeValue4 = normalizeValue(strTrim2);
                } else if (STR_PIN2.equals(strTrim)) {
                    strNormalizeValue5 = normalizeValue(strTrim2);
                }
            }
            strArrSplit = strArr2;
            i3 = i;
            length = i4;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(STR_TAG, strNormalizeValue);
        contentValues.put(STR_NUMBER, strNormalizeValue2);
        contentValues.put(STR_EMAILS, strNormalizeValue3);
        contentValues.put(STR_ANRS, strNormalizeValue4);
        log("delete mvalues= " + contentValues);
        if (!updateIccRecordInEf(i2, contentValues, strNormalizeValue5, defaultSubscriptionId)) {
            return 0;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return i;
    }

    /* JADX WARN: Code duplicated, block: B:27:0x006e A[RETURN] */
    /* JADX WARN: Code duplicated, block: B:29:0x0070  */
    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        int defaultSubscriptionId;
        String str2;
        String asString;
        IccInternalInterface iccInternalInterface = sMtkIccProvider;
        if (iccInternalInterface != null) {
            int iUpdate = iccInternalInterface.update(uri, contentValues, str, strArr);
            if (iUpdate <= 0) {
                return iUpdate;
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return iUpdate;
        }
        log("update");
        int iMatch = URL_MATCHER.match(uri);
        int i = 28474;
        if (iMatch == 1) {
            defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
        } else {
            if (iMatch != 2) {
                if (iMatch == 3) {
                    defaultSubscriptionId = SubscriptionManager.getDefaultSubscriptionId();
                    asString = contentValues.getAsString(STR_PIN2);
                } else if (iMatch == 4) {
                    defaultSubscriptionId = getRequestSubId(uri);
                    asString = contentValues.getAsString(STR_PIN2);
                } else {
                    throw new UnsupportedOperationException("Cannot insert into URL: " + uri);
                }
                str2 = asString;
                i = 28475;
            } else {
                defaultSubscriptionId = getRequestSubId(uri);
            }
            if (!updateIccRecordInEf(i, contentValues, str2, defaultSubscriptionId)) {
                return 0;
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        str2 = null;
        if (!updateIccRecordInEf(i, contentValues, str2, defaultSubscriptionId)) {
            return 0;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return 1;
    }

    private MatrixCursor loadFromEf(int i, int i2) {
        log("loadFromEf: efType=0x" + Integer.toHexString(i).toUpperCase(Locale.ROOT) + ", subscription=" + i2);
        List<AdnRecord> adnRecordsInEfForSubscriber = null;
        try {
            IIccPhoneBook iIccPhoneBookAsInterface = IIccPhoneBook.Stub.asInterface(TelephonyFrameworkInitializer.getTelephonyServiceManager().getIccPhoneBookServiceRegisterer().get());
            if (iIccPhoneBookAsInterface != null) {
                adnRecordsInEfForSubscriber = iIccPhoneBookAsInterface.getAdnRecordsInEfForSubscriber(i2, i);
            }
        } catch (RemoteException unused) {
        } catch (SecurityException e) {
            log(e.toString());
        }
        if (adnRecordsInEfForSubscriber != null) {
            int size = adnRecordsInEfForSubscriber.size();
            MatrixCursor matrixCursor = new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES, size);
            log("adnRecords.size=" + size);
            for (int i3 = 0; i3 < size; i3++) {
                loadRecord(adnRecordsInEfForSubscriber.get(i3), matrixCursor, i3);
            }
            return matrixCursor;
        }
        Rlog.w("IccProvider", "Cannot load ADN records");
        return new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES);
    }

    private boolean updateIccRecordInEf(int i, ContentValues contentValues, String str, int i2) {
        log("updateIccRecordInEf: efType=" + i + ", values: [ " + contentValues + "  ], subId:" + i2);
        boolean zUpdateAdnRecordsInEfBySearchForSubscriber = false;
        try {
            IIccPhoneBook iIccPhoneBookAsInterface = IIccPhoneBook.Stub.asInterface(TelephonyFrameworkInitializer.getTelephonyServiceManager().getIccPhoneBookServiceRegisterer().get());
            if (iIccPhoneBookAsInterface != null) {
                zUpdateAdnRecordsInEfBySearchForSubscriber = iIccPhoneBookAsInterface.updateAdnRecordsInEfBySearchForSubscriber(i2, i, contentValues, str);
            }
        } catch (RemoteException unused) {
        } catch (SecurityException e) {
            log(e.toString());
        }
        log("updateIccRecordInEf: " + zUpdateAdnRecordsInEfBySearchForSubscriber);
        return zUpdateAdnRecordsInEfBySearchForSubscriber;
    }

    private void loadRecord(AdnRecord adnRecord, MatrixCursor matrixCursor, int i) {
        if (adnRecord.isEmpty()) {
            return;
        }
        Object[] objArr = new Object[5];
        String alphaTag = adnRecord.getAlphaTag();
        String number = adnRecord.getNumber();
        log("loadRecord: " + alphaTag + ", " + Rlog.pii("IccProvider", number));
        objArr[0] = alphaTag;
        objArr[1] = number;
        String[] emails = adnRecord.getEmails();
        if (emails != null) {
            StringBuilder sb = new StringBuilder();
            for (String str : emails) {
                log("Adding email:" + Rlog.pii("IccProvider", str));
                sb.append(str);
                sb.append(",");
            }
            objArr[2] = sb.toString();
        }
        String[] additionalNumbers = adnRecord.getAdditionalNumbers();
        if (additionalNumbers != null) {
            StringBuilder sb2 = new StringBuilder();
            for (String str2 : additionalNumbers) {
                log("Adding anr:" + str2);
                sb2.append(str2);
                sb2.append(":");
            }
            objArr[3] = sb2.toString();
        }
        objArr[4] = Integer.valueOf(i);
        matrixCursor.addRow(objArr);
    }

    private void log(String str) {
        Rlog.d("IccProvider", "[IccProvider] " + str);
    }

    private int getRequestSubId(Uri uri) {
        log("getRequestSubId url: " + uri);
        try {
            return Integer.parseInt(uri.getLastPathSegment());
        } catch (NumberFormatException unused) {
            RILUtils$$ExternalSyntheticBUOutline1.m("Unknown URL ", uri);
            return 0;
        }
    }
}
