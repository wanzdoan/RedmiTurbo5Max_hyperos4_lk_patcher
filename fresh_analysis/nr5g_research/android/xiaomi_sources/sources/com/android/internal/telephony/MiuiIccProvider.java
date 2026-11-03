package com.android.internal.telephony;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.xiaomi.platform.flags.Flags;
import com.android.internal.telephony.uicc.MiuiAdnRecord;
import com.android.internal.telephony.uicc.MiuiUiccControllerImpl;
import java.util.List;
import miui.telephony.MiuiTelephony;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;

/* JADX INFO: loaded from: classes.dex */
public class MiuiIccProvider extends ContentProvider {
    public static final int ADDRESS_BOOK_COLUMN_ANR = 4;
    public static final int ADDRESS_BOOK_COLUMN_EMAILS = 2;
    public static final int ADDRESS_BOOK_COLUMN_ID = 3;
    public static final int ADDRESS_BOOK_COLUMN_NAME = 0;
    public static final String[] ADDRESS_BOOK_COLUMN_NAMES;
    public static final int ADDRESS_BOOK_COLUMN_NUMBER = 1;
    private static final int ADN = 1;
    private static final int ADN_CAPACITY = 5;
    private static final int ADN_STORAGE = 6;
    private static final int ADN_SUB = 11;
    static final boolean DBG = false;
    private static final int FDN = 2;
    private static final int FDN_SUB = 12;
    private static final int FREE_ADN = 4;
    private static final int IS_PHONEBOOK_LOADED = 10;
    private static final int IS_PHONEBOOK_READY = 7;
    private static final int IS_USIM_PHONEBOOK = 8;
    private static final int LAST_ERROR = 9;
    private static final int SDN = 3;
    private static final int SDN_SUB = 13;
    public static final String STR_ANR = "anr";
    public static final String STR_ANRS = "anrs";
    public static final String STR_EMAILS = "emails";
    public static final String STR_ID = "_id";
    public static final String STR_NAME = "name";
    public static final String STR_NEW_ANR = "newAnr";
    public static final String STR_NEW_ANRS = "newAnrs";
    public static final String STR_NEW_EMAILS = "newEmails";
    public static final String STR_NEW_NAME = "newName";
    public static final String STR_NEW_NUMBER = "newNumber";
    public static final String STR_NEW_TAG = "newTag";
    public static final String STR_NUMBER = "number";
    public static final String STR_PIN2 = "pin2";
    public static final String STR_TAG = "tag";
    static final String TAG = "IccProvider";
    private static final UriMatcher URL_MATCHER;

    static {
        if (TelephonyManager.isGoogleCsp()) {
            ADDRESS_BOOK_COLUMN_NAMES = new String[]{STR_NAME, STR_NUMBER, STR_EMAILS, STR_ID, STR_ANRS};
        } else {
            ADDRESS_BOOK_COLUMN_NAMES = new String[]{STR_NAME, STR_NUMBER, STR_EMAILS, STR_ID, STR_ANR};
        }
        URL_MATCHER = new UriMatcher(-1);
        URL_MATCHER.addURI("icc", "adn", 1);
        URL_MATCHER.addURI("icc", "fdn", 2);
        URL_MATCHER.addURI("icc", "sdn", 3);
        URL_MATCHER.addURI("icc", "freeadn", 4);
        URL_MATCHER.addURI("icc", "adncapacity", 5);
        URL_MATCHER.addURI("icc", "adnstroage", 6);
        URL_MATCHER.addURI("icc", "isphonebookready", 7);
        URL_MATCHER.addURI("icc", "isusimphonebook", 8);
        URL_MATCHER.addURI("icc", "lasterror", 9);
        URL_MATCHER.addURI("icc", "isphonebookloaded", 10);
        URL_MATCHER.addURI("icc", "adn/subId/#", 11);
        URL_MATCHER.addURI("icc", "fdn/subId/#", 12);
        URL_MATCHER.addURI("icc", "sdn/subId/#", 13);
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        String slotIdStr = uri.getQueryParameter(SubscriptionManager.SLOT_KEY);
        int slotId = TextUtils.isEmpty(slotIdStr) ? SubscriptionManager.getDefault().getDefaultSlotId() : Integer.parseInt(slotIdStr);
        switch (URL_MATCHER.match(uri)) {
            case 1:
                return loadFromEf(28474, selection, selectionArgs, slotId);
            case 2:
                return loadFromEf(28475, selection, selectionArgs, slotId);
            case 3:
                return loadFromEf(28489, selection, selectionArgs, slotId);
            case 4:
                return getFreeAdn(slotId);
            case 5:
                return getAdnCapacity(slotId);
            case 6:
                return getStorage(slotId);
            case 7:
                return isPhoneBookReady(slotId);
            case 8:
                return isUsimPhoneBook(slotId);
            case 9:
                return getLastError(slotId);
            case 10:
                return isPhoneBookLoaded(slotId);
            case 11:
                return loadFromEf(28474, selection, selectionArgs, getRequestSlotId(uri));
            case 12:
                return loadFromEf(28475, selection, selectionArgs, getRequestSlotId(uri));
            case 13:
                return loadFromEf(28489, selection, selectionArgs, getRequestSlotId(uri));
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues values) {
        int slotId;
        String pin2;
        int efid;
        String tag;
        boolean success;
        String slotIdStr = uri.getQueryParameter(SubscriptionManager.SLOT_KEY);
        int slotId2 = TextUtils.isEmpty(slotIdStr) ? SubscriptionManager.getDefault().getDefaultSlotId() : Integer.parseInt(slotIdStr);
        int match = URL_MATCHER.match(uri);
        switch (match) {
            case 1:
                slotId = slotId2;
                pin2 = null;
                efid = 28474;
                break;
            case 2:
                String pin3 = values.getAsString(STR_PIN2);
                slotId = slotId2;
                pin2 = pin3;
                efid = 28475;
                break;
            case 11:
                slotId = getRequestSlotId(uri);
                pin2 = null;
                efid = 28474;
                break;
            case 12:
                String pin4 = values.getAsString(STR_PIN2);
                slotId = getRequestSlotId(uri);
                pin2 = pin4;
                efid = 28475;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        String tag2 = values.getAsString(STR_TAG);
        if (!TextUtils.isEmpty(tag2)) {
            tag = tag2;
        } else {
            tag = values.getAsString(STR_NAME);
        }
        String number = PhoneNumberUtils.stripSeparators(values.getAsString(STR_NUMBER));
        if (efid == 28475) {
            success = updateAdnRecordsInEfBySearch(efid, "", "", tag, number, pin2, slotId);
        } else {
            String strEmail = values.getAsString(STR_EMAILS);
            String[] emails = null;
            if (!TextUtils.isEmpty(strEmail)) {
                emails = new String[]{strEmail};
            }
            String anr = values.getAsString(STR_ANR);
            if (TelephonyManager.isGoogleCsp() && TextUtils.isEmpty(anr)) {
                anr = values.getAsString(STR_ANRS);
            }
            MiuiAdnRecord adn = new MiuiAdnRecord(tag, number, emails, anr);
            success = updateAdnRecordsInEfBySearch(efid, new MiuiAdnRecord("", "", (String[]) null, ""), adn, slotId);
        }
        if (!success) {
            return null;
        }
        StringBuilder buf = new StringBuilder("content://icc/");
        switch (match) {
            case 1:
                buf.append("adn/");
                break;
            case 2:
                buf.append("fdn/");
                break;
            case 11:
                buf.append("adn/subId/");
                break;
            case 12:
                buf.append("fdn/subId/");
                break;
        }
        buf.append(0);
        Uri resultUri = Uri.parse(buf.toString());
        return resultUri;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        int requestSlotId;
        String asString;
        int i;
        String asString2;
        String asString3;
        String queryParameter = uri.getQueryParameter(SubscriptionManager.SLOT_KEY);
        int defaultSlotId = TextUtils.isEmpty(queryParameter) ? SubscriptionManager.getDefault().getDefaultSlotId() : Integer.parseInt(queryParameter);
        switch (URL_MATCHER.match(uri)) {
            case 1:
                requestSlotId = defaultSlotId;
                asString = null;
                i = 28474;
                break;
            case 2:
                requestSlotId = defaultSlotId;
                asString = contentValues.getAsString(STR_PIN2);
                i = 28475;
                break;
            case 11:
                requestSlotId = getRequestSlotId(uri);
                asString = null;
                i = 28474;
                break;
            case 12:
                String asString4 = contentValues.getAsString(STR_PIN2);
                requestSlotId = getRequestSlotId(uri);
                asString = asString4;
                i = 28475;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        String asString5 = contentValues.getAsString(STR_TAG);
        if (!TextUtils.isEmpty(asString5)) {
            asString2 = asString5;
        } else {
            asString2 = contentValues.getAsString(STR_NAME);
        }
        String strStripSeparators = PhoneNumberUtils.stripSeparators(contentValues.getAsString(STR_NUMBER));
        String asString6 = contentValues.getAsString(STR_NEW_TAG);
        if (!TextUtils.isEmpty(asString6)) {
            asString3 = asString6;
        } else {
            asString3 = contentValues.getAsString(STR_NEW_NAME);
        }
        String strStripSeparators2 = PhoneNumberUtils.stripSeparators(contentValues.getAsString(STR_NEW_NUMBER));
        Integer asInteger = contentValues.getAsInteger(STR_ID);
        if (i == 28475) {
            if (asInteger != null) {
                return updateAdnRecordsInEfByIndex(i, asString3, strStripSeparators2, asString, asInteger.intValue(), requestSlotId) ? 1 : 0;
            }
            return updateAdnRecordsInEfBySearch(i, asString2, strStripSeparators, asString3, strStripSeparators2, asString, requestSlotId) ? 1 : 0;
        }
        String str2 = asString2;
        int i2 = requestSlotId;
        String str3 = asString3;
        String asString7 = contentValues.getAsString(STR_NEW_EMAILS);
        String[] strArr2 = null;
        if (!TextUtils.isEmpty(asString7)) {
            strArr2 = new String[]{asString7};
        }
        String asString8 = contentValues.getAsString(STR_NEW_ANR);
        if (TelephonyManager.isGoogleCsp() && TextUtils.isEmpty(asString8)) {
            asString8 = contentValues.getAsString(STR_NEW_ANRS);
        }
        MiuiAdnRecord miuiAdnRecord = new MiuiAdnRecord(str3, strStripSeparators2, strArr2, asString8);
        if (asInteger != null) {
            return updateAdnRecordsInEfByIndex(i, miuiAdnRecord, asInteger.intValue(), i2) ? 1 : 0;
        }
        return Flags.sprdEnabled() ? updateAdnRecordsInEfBySearchForSprd(i, new MiuiAdnRecord(str2, strStripSeparators, (String[]) null, ""), miuiAdnRecord, i2) ? 1 : 0 : updateAdnRecordsInEfBySearch(i, new MiuiAdnRecord(str2, strStripSeparators, (String[]) null, ""), miuiAdnRecord, i2) ? 1 : 0;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        int requestSlotId;
        int i;
        String queryParameter = uri.getQueryParameter(SubscriptionManager.SLOT_KEY);
        int defaultSlotId = TextUtils.isEmpty(queryParameter) ? SubscriptionManager.getDefault().getDefaultSlotId() : Integer.parseInt(queryParameter);
        switch (URL_MATCHER.match(uri)) {
            case 1:
                requestSlotId = defaultSlotId;
                i = 28474;
                break;
            case 2:
                requestSlotId = defaultSlotId;
                i = 28475;
                break;
            case 11:
                requestSlotId = getRequestSlotId(uri);
                i = 28474;
                break;
            case 12:
                requestSlotId = getRequestSlotId(uri);
                i = 28475;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        String[] selection = parseSelection(str, strArr);
        if (selection == null) {
            return 0;
        }
        int i2 = selection[3] != null ? Integer.parseInt(selection[3]) : -1;
        if (i == 28475) {
            return i2 >= 0 ? updateAdnRecordsInEfByIndex(i, "", "", selection[ADDRESS_BOOK_COLUMN_NAMES.length], i2, requestSlotId) ? 1 : 0 : updateAdnRecordsInEfBySearch(i, selection[0], selection[1], "", "", selection[ADDRESS_BOOK_COLUMN_NAMES.length], requestSlotId) ? 1 : 0;
        }
        int i3 = i;
        if (i2 >= 0) {
            return updateAdnRecordsInEfByIndex(i3, new MiuiAdnRecord("", "", (String[]) null, ""), i2, requestSlotId) ? 1 : 0;
        }
        return Flags.sprdEnabled() ? updateAdnRecordsInEfBySearchForSprd(i3, new MiuiAdnRecord(selection[0], selection[1], (String[]) null, ""), new MiuiAdnRecord("", "", (String[]) null, ""), requestSlotId) ? 1 : 0 : updateAdnRecordsInEfBySearch(i3, new MiuiAdnRecord(selection[0], selection[1], (String[]) null, ""), new MiuiAdnRecord("", "", (String[]) null, ""), requestSlotId) ? 1 : 0;
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
            case 8:
            case 9:
            case 11:
            case 12:
            case 13:
                return "vnd.android.cursor.dir/sim-contact";
            case 10:
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    private MiuiIccPhoneBookInterfaceManager getIccPhoneBookService(int slotId) {
        MiuiUiccControllerImpl uiccImpl;
        if (SubscriptionManager.isRealSlotId(slotId) && (uiccImpl = (MiuiUiccControllerImpl) MiuiTelephony.getInstance().getTelephonyPlugin(MiuiUiccControllerImpl.class, slotId)) != null) {
            return uiccImpl.getMiuiIccPhoneBookInterfaceManager();
        }
        return null;
    }

    protected Cursor loadFromEf(int efid, String selection, String[] selectionArgs, int slotId) {
        List<MiuiAdnRecord> adnRecords = null;
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                List<MiuiAdnRecord> records = phoneBook.getAdnRecordsInEf(efid);
                List<MiuiAdnRecord> list = records;
                adnRecords = records;
            }
        } catch (SecurityException e) {
        }
        if (adnRecords != null) {
            String[] selections = parseSelection(selection, selectionArgs);
            MatrixCursor cursor = new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES, adnRecords.size());
            int s = adnRecords.size();
            for (int i = 0; i < s; i++) {
                loadRecord(adnRecords.get(i), cursor, i, selections);
            }
            return cursor;
        }
        return new MatrixCursor(ADDRESS_BOOK_COLUMN_NAMES);
    }

    protected MatrixCursor getFreeAdn(int slotId) {
        int freeAdn = 0;
        MatrixCursor cursor = new MatrixCursor(new String[]{"count"}, 1);
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                freeAdn = phoneBook.getFreeAdn();
            }
        } catch (SecurityException e) {
        }
        Object[] count = {Integer.valueOf(freeAdn)};
        cursor.addRow(count);
        return cursor;
    }

    protected MatrixCursor getAdnCapacity(int slotId) {
        int capacity = 0;
        MatrixCursor cursor = new MatrixCursor(new String[]{"count"}, 1);
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                capacity = phoneBook.getAdnCapacity();
            }
        } catch (SecurityException e) {
        }
        Object[] count = {Integer.valueOf(capacity)};
        cursor.addRow(count);
        return cursor;
    }

    protected Cursor getStorage(int slotId) {
        int freeAdn = 0;
        int capacity = 0;
        MatrixCursor cursor = new MatrixCursor(new String[]{"total", "free"}, 1);
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                capacity = phoneBook.getAdnCapacity();
                freeAdn = phoneBook.getFreeAdn();
            }
        } catch (SecurityException e) {
        }
        Object[] storage = {Integer.valueOf(capacity), Integer.valueOf(freeAdn)};
        cursor.addRow(storage);
        return cursor;
    }

    protected MatrixCursor isPhoneBookReady(int slotId) {
        boolean ready = DBG;
        MatrixCursor cursor = new MatrixCursor(new String[]{"ready"}, 1);
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                ready = phoneBook.isPhoneBookReady();
            }
        } catch (SecurityException e) {
        }
        Object[] count = new Object[1];
        count[0] = Integer.valueOf(ready ? 1 : 0);
        cursor.addRow(count);
        return cursor;
    }

    protected MatrixCursor isPhoneBookLoaded(int slotId) {
        boolean ready = DBG;
        MatrixCursor cursor = new MatrixCursor(new String[]{"loaded"}, 1);
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                ready = phoneBook.isPhoneBookLoaded();
            }
        } catch (SecurityException e) {
        }
        Object[] count = new Object[1];
        count[0] = Integer.valueOf(ready ? 1 : 0);
        cursor.addRow(count);
        return cursor;
    }

    protected MatrixCursor isUsimPhoneBook(int slotId) {
        boolean usim = DBG;
        MatrixCursor cursor = new MatrixCursor(new String[]{"usimphonebook"}, 1);
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                usim = phoneBook.isUsimPhoneBookRecords();
            }
        } catch (SecurityException e) {
        }
        Object[] count = new Object[1];
        count[0] = Integer.valueOf(usim ? 1 : 0);
        cursor.addRow(count);
        return cursor;
    }

    private MatrixCursor getLastError(int slotId) {
        int errno = 0;
        MatrixCursor cursor = new MatrixCursor(new String[]{"error"}, 1);
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                errno = phoneBook.getLastError();
            }
        } catch (SecurityException e) {
        }
        Object[] count = {Integer.valueOf(errno)};
        cursor.addRow(count);
        return cursor;
    }

    protected boolean updateAdnRecordsInEfBySearch(int efid, String oldName, String oldNumber, String newName, String newNumber, String pin2, int slotId) {
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                try {
                    return phoneBook.updateAdnRecordsInEfBySearch(efid, oldName, oldNumber, newName, newNumber, pin2);
                } catch (SecurityException e) {
                    return DBG;
                }
            }
            return DBG;
        } catch (SecurityException e2) {
            return DBG;
        }
    }

    protected boolean updateAdnRecordsInEfByIndex(int efid, String newName, String newNumber, String pin2, int index, int slotId) {
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                try {
                    return phoneBook.updateAdnRecordsInEfByIndex(efid, newName, newNumber, index, pin2);
                } catch (SecurityException e) {
                    return DBG;
                }
            }
            return DBG;
        } catch (SecurityException e2) {
            return DBG;
        }
    }

    protected boolean updateAdnRecordsInEfBySearch(int efid, MiuiAdnRecord oldAdn, MiuiAdnRecord newAdn, int slotId) {
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                return phoneBook.updateUsimPhoneBookRecordsBySearch(efid, oldAdn, newAdn);
            }
            return DBG;
        } catch (SecurityException e) {
            return DBG;
        }
    }

    protected boolean updateAdnRecordsInEfBySearchForSprd(int efid, MiuiAdnRecord oldAdn, MiuiAdnRecord newAdn, int slotId) {
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                return phoneBook.updateUsimPhoneBookRecordsBySearchForSprd(efid, oldAdn, newAdn);
            }
            return DBG;
        } catch (SecurityException e) {
            return DBG;
        }
    }

    protected boolean updateAdnRecordsInEfByIndex(int efid, MiuiAdnRecord newAdn, int index, int slotId) {
        try {
            MiuiIccPhoneBookInterfaceManager phoneBook = getIccPhoneBookService(slotId);
            if (phoneBook != null) {
                return phoneBook.updateUsimPhoneBookRecordsByIndex(efid, newAdn, index);
            }
            return DBG;
        } catch (SecurityException e) {
            return DBG;
        }
    }

    protected void loadRecord(MiuiAdnRecord adn, MatrixCursor cursor, int id, String[] selections) {
        int length = ADDRESS_BOOK_COLUMN_NAMES.length;
        if (!adn.isEmpty() && match(selections, adn, id + 1)) {
            Object[] contact = new Object[length];
            contact[3] = Integer.valueOf(id + 1);
            contact[0] = adn.getAlphaTag();
            contact[1] = adn.getNumber();
            if (adn.getEmails() != null) {
                StringBuilder builder = new StringBuilder();
                builder.append(adn.getEmails()[0]);
                int s = adn.getEmails().length;
                for (int i = 1; i < s; i++) {
                    builder.append(',');
                    builder.append(adn.getEmails()[i]);
                }
                contact[2] = builder.toString();
            }
            contact[4] = adn.getAnr();
            cursor.addRow(contact);
        }
    }

    private String normalizeValue(String str) {
        int len = str.length();
        if (len > 1 && str.charAt(0) == '\'' && str.charAt(len - 1) == '\'') {
            return str.substring(1, len - 1);
        }
        return str;
    }

    protected boolean match(String[] selections, MiuiAdnRecord adn, int id) {
        boolean match = true;
        if (selections != null) {
            for (int i = 0; i < ADDRESS_BOOK_COLUMN_NAMES.length; i++) {
                if (selections[i] != null) {
                    boolean match2 = DBG;
                    switch (i) {
                        case 0:
                            match = TextUtils.equals(adn.getAlphaTag(), selections[i]);
                            break;
                        case 1:
                            match = TextUtils.equals(adn.getNumber(), selections[i]);
                            break;
                        case 2:
                            match = adn.getEmails() == null ? "".equals(selections[i]) : TextUtils.equals(adn.getEmails()[0], selections[i]);
                            break;
                        case 3:
                            if (id == Integer.parseInt(selections[i])) {
                                match2 = true;
                            }
                            match = match2;
                            break;
                        case 4:
                            match = TextUtils.equals(adn.getAnr(), selections[i]);
                            break;
                    }
                    if (!match) {
                    }
                }
            }
        }
        return match;
    }

    protected String[] parseSelection(String selection, String[] selectionArgs) {
        if (TextUtils.isEmpty(selection)) {
            return null;
        }
        String[] results = new String[ADDRESS_BOOK_COLUMN_NAMES.length + 1];
        String[] tokens = selection.split("AND");
        int argsIndex = 0;
        for (String param : tokens) {
            String[] keyVal = param.split("=");
            if (keyVal.length == 2) {
                String key = keyVal[0].trim();
                String val = keyVal[1].trim();
                if ("?".equals(val)) {
                    val = selectionArgs[argsIndex];
                    argsIndex++;
                }
                if (STR_TAG.equals(key)) {
                    results[0] = normalizeValue(val);
                } else if (STR_PIN2.equals(key)) {
                    results[ADDRESS_BOOK_COLUMN_NAMES.length] = normalizeValue(val);
                } else if (STR_NUMBER.equals(key)) {
                    results[1] = PhoneNumberUtils.stripSeparators(normalizeValue(val));
                } else if (STR_ID.equals(key)) {
                    results[3] = normalizeValue(val);
                } else if (STR_NAME.equals(key) && TextUtils.isEmpty(results[0])) {
                    results[0] = normalizeValue(val);
                } else if (STR_EMAILS.equals(key)) {
                    results[2] = normalizeValue(val);
                } else if (STR_ANR.equals(key)) {
                    results[4] = normalizeValue(val);
                } else if (STR_ANRS.equals(key)) {
                    results[4] = normalizeValue(val);
                }
            }
        }
        return results;
    }

    private void log(String msg) {
        Log.d(TAG, "[IccProvider] " + msg);
    }

    private int getRequestSlotId(Uri url) {
        try {
            int subId = Integer.parseInt(url.getLastPathSegment());
            int slotId = SubscriptionManager.getDefault().getSlotIdForSubscription(subId);
            log("getRequestSlotId slotId=" + slotId);
            return slotId;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unknown URL " + url);
        }
    }
}
