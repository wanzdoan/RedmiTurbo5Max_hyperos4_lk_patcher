package com.android.phone;

import ai.onnxruntime.OrtProvider$$ExternalSyntheticBUOutline0;
import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline4;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.RemoteException;
import android.provider.SimPhonebookContract;
import android.telephony.PhoneNumberUtils;
import android.telephony.Rlog;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyFrameworkInitializer;
import android.telephony.TelephonyManager;
import android.util.ArraySet;
import android.util.SparseArray;
import androidx.fragment.app.BackStackRecord$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.IIccPhoneBook;
import com.android.internal.telephony.uicc.AdnRecord;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;
import miuix.animation.utils.EaseManager;

/* JADX INFO: loaded from: classes.dex */
public class SimPhonebookProvider extends ContentProvider {
    static final String[] ELEMENTARY_FILES_ALL_COLUMNS;
    private static final Set ELEMENTARY_FILES_COLUMNS_SET;
    static final String[] SIM_RECORDS_ALL_COLUMNS;
    private static final Set SIM_RECORDS_COLUMNS_SET;
    private static final Set SIM_RECORDS_WRITABLE_COLUMNS = ImmutableSet.of((Object) "name", (Object) "phone_number");
    private static final UriMatcher URI_MATCHER;
    private ContentNotifier mContentNotifier;
    private Supplier mIccPhoneBookSupplier;
    private SubscriptionManager mSubscriptionManager;
    private final Lock mWriteLock = new ReentrantLock(true);

    interface ContentNotifier {
        void notifyChange(Uri uri);
    }

    static int efIdForEfType(int i) {
        if (i == 1) {
            return 28474;
        }
        if (i != 2) {
            return i != 3 ? 0 : 28489;
        }
        return 28475;
    }

    static {
        String[] strArr = {"slot_index", "subscription_id", "ef_type", "max_records", "record_count", "name_max_length", "phone_number_max_length"};
        ELEMENTARY_FILES_ALL_COLUMNS = strArr;
        String[] strArr2 = {"subscription_id", "elementary_file_type", "record_number", "name", "phone_number"};
        SIM_RECORDS_ALL_COLUMNS = strArr2;
        ELEMENTARY_FILES_COLUMNS_SET = ImmutableSet.copyOf(strArr);
        SIM_RECORDS_COLUMNS_SET = ImmutableSet.copyOf(strArr2);
        UriMatcher uriMatcher = new UriMatcher(-1);
        URI_MATCHER = uriMatcher;
        uriMatcher.addURI("com.android.simphonebook", "elementary_files", 100);
        uriMatcher.addURI("com.android.simphonebook", "elementary_files/subid/#/*", 101);
        uriMatcher.addURI("com.android.simphonebook", "subid/#/*", 200);
        uriMatcher.addURI("com.android.simphonebook", "subid/#/*/#", EaseManager.EaseStyleDef.PERLIN);
    }

    private static void validateProjection(Set set, String[] strArr) {
        if (strArr == null || set.containsAll(Arrays.asList(strArr))) {
            return;
        }
        LinkedHashSet linkedHashSet = new LinkedHashSet(Arrays.asList(strArr));
        linkedHashSet.removeAll(set);
        OrtUtil$$ExternalSyntheticBUOutline4.m("Unsupported columns: ", Joiner.on(",").join(linkedHashSet));
    }

    private static int getRecordSize(int[] iArr) {
        return iArr[0];
    }

    private static int getRecordCount(int[] iArr) {
        return iArr[2];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static IIccPhoneBook getIccPhoneBook() {
        return IIccPhoneBook.Stub.asInterface(TelephonyFrameworkInitializer.getTelephonyServiceManager().getIccPhoneBookServiceRegisterer().get());
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        final ContentResolver contentResolver = getContext().getContentResolver();
        SubscriptionManager subscriptionManager = (SubscriptionManager) getContext().getSystemService(SubscriptionManager.class);
        if (subscriptionManager == null) {
            return false;
        }
        return onCreate(subscriptionManager, new Supplier() { // from class: com.android.phone.SimPhonebookProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return SimPhonebookProvider.getIccPhoneBook();
            }
        }, new ContentNotifier() { // from class: com.android.phone.SimPhonebookProvider$$ExternalSyntheticLambda1
            @Override // com.android.phone.SimPhonebookProvider.ContentNotifier
            public final void notifyChange(Uri uri) {
                contentResolver.notifyChange(uri, null);
            }
        });
    }

    boolean onCreate(SubscriptionManager subscriptionManager, Supplier supplier, final ContentNotifier contentNotifier) {
        this.mSubscriptionManager = subscriptionManager;
        this.mIccPhoneBookSupplier = supplier;
        this.mContentNotifier = contentNotifier;
        subscriptionManager.addOnSubscriptionsChangedListener(MoreExecutors.directExecutor(), new SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.phone.SimPhonebookProvider.1
            boolean mFirstCallback = true;
            private int[] mNotifiedSubIds = new int[0];

            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                if (this.mFirstCallback) {
                    this.mFirstCallback = false;
                    return;
                }
                int[] activeSubscriptionIdList = SimPhonebookProvider.this.mSubscriptionManager.getActiveSubscriptionIdList();
                if (Arrays.equals(this.mNotifiedSubIds, activeSubscriptionIdList)) {
                    return;
                }
                contentNotifier.notifyChange(SimPhonebookContract.AUTHORITY_URI);
                this.mNotifiedSubIds = Arrays.copyOf(activeSubscriptionIdList, activeSubscriptionIdList.length);
            }
        });
        return true;
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        if ("get_encoded_name_length".equals(str)) {
            return callForEncodedNameLength(str2);
        }
        return super.call(str, str2, bundle);
    }

    private Bundle callForEncodedNameLength(String str) {
        Bundle bundle = new Bundle();
        bundle.putInt("android.provider.extra.ENCODED_NAME_LENGTH", getEncodedNameLength(str));
        return bundle;
    }

    private int getEncodedNameLength(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return 0;
        }
        return AdnRecord.encodeAlphaTag(str).length;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, Bundle bundle, CancellationSignal cancellationSignal) {
        if (bundle != null && (bundle.containsKey("android:query-arg-sql-selection") || bundle.containsKey("android:query-arg-sql-selection-args") || bundle.containsKey("android:query-arg-sql-limit"))) {
            Folme$$ExternalSyntheticBUOutline0.m("A SQL selection was provided but it is not supported by this provider.");
            return null;
        }
        int iMatch = URI_MATCHER.match(uri);
        if (iMatch == 100) {
            return queryElementaryFiles(strArr);
        }
        if (iMatch == 101) {
            return queryElementaryFilesItem(PhonebookArgs.forElementaryFilesItem(uri), strArr);
        }
        if (iMatch == 200) {
            return querySimRecords(PhonebookArgs.forSimRecords(uri, bundle), strArr);
        }
        if (iMatch == 201) {
            return querySimRecordsItem(PhonebookArgs.forSimRecordsItem(uri, bundle), strArr);
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("Unsupported Uri ", uri);
        return null;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, CancellationSignal cancellationSignal) {
        throw new UnsupportedOperationException("Only query with Bundle is supported");
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        throw new UnsupportedOperationException("Only query with Bundle is supported");
    }

    private Cursor queryElementaryFiles(String[] strArr) {
        validateProjection(ELEMENTARY_FILES_COLUMNS_SET, strArr);
        if (strArr == null) {
            strArr = ELEMENTARY_FILES_ALL_COLUMNS;
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        for (SubscriptionInfo subscriptionInfo : getActiveSubscriptionInfoList()) {
            try {
                addEfToCursor(matrixCursor, subscriptionInfo, 1);
                addEfToCursor(matrixCursor, subscriptionInfo, 2);
                addEfToCursor(matrixCursor, subscriptionInfo, 3);
            } catch (RemoteException unused) {
                return new MatrixCursor(strArr, 0);
            }
        }
        return matrixCursor;
    }

    private Cursor queryElementaryFilesItem(PhonebookArgs phonebookArgs, String[] strArr) {
        validateProjection(ELEMENTARY_FILES_COLUMNS_SET, strArr);
        if (strArr == null) {
            strArr = ELEMENTARY_FILES_ALL_COLUMNS;
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        try {
            SubscriptionInfo activeSubscriptionInfo = getActiveSubscriptionInfo(phonebookArgs.subscriptionId);
            if (activeSubscriptionInfo != null) {
                addEfToCursor(matrixCursor, activeSubscriptionInfo, phonebookArgs.efType);
            }
            return matrixCursor;
        } catch (RemoteException unused) {
            return new MatrixCursor(strArr, 0);
        }
    }

    private void addEfToCursor(MatrixCursor matrixCursor, SubscriptionInfo subscriptionInfo, int i) {
        addEfToCursor(matrixCursor, subscriptionInfo, i, ((IIccPhoneBook) this.mIccPhoneBookSupplier.get()).getAdnRecordsSizeForSubscriber(subscriptionInfo.getSubscriptionId(), efIdForEfType(i)));
    }

    private void addEfToCursor(MatrixCursor matrixCursor, SubscriptionInfo subscriptionInfo, int i, int[] iArr) {
        if (iArr == null || getRecordCount(iArr) == 0) {
            return;
        }
        List adnRecordsInEfForSubscriber = ((IIccPhoneBook) this.mIccPhoneBookSupplier.get()).getAdnRecordsInEfForSubscriber(subscriptionInfo.getSubscriptionId(), efIdForEfType(i));
        if (adnRecordsInEfForSubscriber == null) {
            adnRecordsInEfForSubscriber = ImmutableList.of();
        }
        MatrixCursor.RowBuilder rowBuilderAdd = matrixCursor.newRow().add("slot_index", Integer.valueOf(subscriptionInfo.getSimSlotIndex())).add("subscription_id", Integer.valueOf(subscriptionInfo.getSubscriptionId())).add("ef_type", Integer.valueOf(i)).add("max_records", Integer.valueOf(adnRecordsInEfForSubscriber.size())).add("name_max_length", Integer.valueOf(AdnRecord.getMaxAlphaTagBytes(getRecordSize(iArr)))).add("phone_number_max_length", Integer.valueOf(AdnRecord.getMaxPhoneNumberDigits()));
        if (matrixCursor.getColumnIndex("record_count") != -1) {
            Iterator it = adnRecordsInEfForSubscriber.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                if (!((AdnRecord) it.next()).isEmpty()) {
                    i2++;
                }
            }
            rowBuilderAdd.add("record_count", Integer.valueOf(i2));
        }
    }

    private Cursor querySimRecords(PhonebookArgs phonebookArgs, String[] strArr) {
        validateProjection(SIM_RECORDS_COLUMNS_SET, strArr);
        validateSubscriptionAndEf(phonebookArgs);
        if (strArr == null) {
            strArr = SIM_RECORDS_ALL_COLUMNS;
        }
        List listLoadRecordsForEf = loadRecordsForEf(phonebookArgs);
        if (listLoadRecordsForEf == null) {
            return new MatrixCursor(strArr, 0);
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr, listLoadRecordsForEf.size());
        SparseArray sparseArray = new SparseArray(listLoadRecordsForEf.size());
        for (int i = 0; i < listLoadRecordsForEf.size(); i++) {
            if (!((AdnRecord) listLoadRecordsForEf.get(i)).isEmpty()) {
                sparseArray.put(i, matrixCursor.newRow());
            }
        }
        for (String str : strArr) {
            str.hashCode();
            switch (str) {
                case "elementary_file_type":
                    for (int i2 = 0; i2 < sparseArray.size(); i2++) {
                        ((MatrixCursor.RowBuilder) sparseArray.valueAt(i2)).add(Integer.valueOf(phonebookArgs.efType));
                    }
                    break;
                case "subscription_id":
                    for (int i3 = 0; i3 < sparseArray.size(); i3++) {
                        ((MatrixCursor.RowBuilder) sparseArray.valueAt(i3)).add(Integer.valueOf(phonebookArgs.subscriptionId));
                    }
                    break;
                case "record_number":
                    for (int i4 = 0; i4 < sparseArray.size(); i4++) {
                        ((MatrixCursor.RowBuilder) sparseArray.valueAt(i4)).add(Integer.valueOf(sparseArray.keyAt(i4) + 1));
                    }
                    break;
                case "phone_number":
                    for (int i5 = 0; i5 < sparseArray.size(); i5++) {
                        ((MatrixCursor.RowBuilder) sparseArray.valueAt(i5)).add(((AdnRecord) listLoadRecordsForEf.get(sparseArray.keyAt(i5))).getNumber());
                    }
                    break;
                case "name":
                    for (int i6 = 0; i6 < sparseArray.size(); i6++) {
                        ((MatrixCursor.RowBuilder) sparseArray.valueAt(i6)).add(((AdnRecord) listLoadRecordsForEf.get(sparseArray.keyAt(i6))).getAlphaTag());
                    }
                    break;
                default:
                    Rlog.w("SimPhonebookProvider", "Column " + str + " is unsupported for " + phonebookArgs.uri);
                    break;
            }
        }
        return matrixCursor;
    }

    private Cursor querySimRecordsItem(PhonebookArgs phonebookArgs, String[] strArr) {
        validateProjection(SIM_RECORDS_COLUMNS_SET, strArr);
        if (strArr == null) {
            strArr = SIM_RECORDS_ALL_COLUMNS;
        }
        validateSubscriptionAndEf(phonebookArgs);
        AdnRecord adnRecordLoadRecord = loadRecord(phonebookArgs);
        MatrixCursor matrixCursor = new MatrixCursor(strArr, 1);
        if (adnRecordLoadRecord != null && !adnRecordLoadRecord.isEmpty()) {
            matrixCursor.newRow().add("subscription_id", Integer.valueOf(phonebookArgs.subscriptionId)).add("elementary_file_type", Integer.valueOf(phonebookArgs.efType)).add("record_number", Integer.valueOf(adnRecordLoadRecord.getRecId())).add("name", adnRecordLoadRecord.getAlphaTag()).add("phone_number", adnRecordLoadRecord.getNumber());
        }
        return matrixCursor;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        int iMatch = URI_MATCHER.match(uri);
        if (iMatch == 100) {
            return "vnd.android.cursor.dir/sim-elementary-file";
        }
        if (iMatch == 101) {
            return "vnd.android.cursor.item/sim-elementary-file";
        }
        if (iMatch == 200) {
            return "vnd.android.cursor.dir/sim-contact_v2";
        }
        if (iMatch == 201) {
            return "vnd.android.cursor.item/sim-contact_v2";
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("Unsupported Uri ", uri);
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return insert(uri, contentValues, null);
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues, Bundle bundle) {
        int iMatch = URI_MATCHER.match(uri);
        if (iMatch != 100 && iMatch != 101) {
            if (iMatch == 200) {
                return insertSimRecord(PhonebookArgs.forSimRecords(uri, bundle), contentValues);
            }
            if (iMatch != 201) {
                OrtProvider$$ExternalSyntheticBUOutline0.m("Unsupported Uri ", uri);
                return null;
            }
        }
        throw new UnsupportedOperationException(uri + " does not support insert");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0 */
    /* JADX WARN: Type inference failed for: r3v2 */
    /* JADX WARN: Type inference failed for: r3v8, types: [boolean] */
    private Uri insertSimRecord(PhonebookArgs phonebookArgs, ContentValues contentValues) throws Throwable {
        ?? IsEmpty;
        Throwable th;
        SimPhonebookProvider simPhonebookProvider;
        AdnRecord adnRecord;
        validateWritableEf(phonebookArgs, "insert");
        validateSubscriptionAndEf(phonebookArgs);
        if (contentValues == null || contentValues.isEmpty()) {
            return null;
        }
        validateValues(phonebookArgs, contentValues);
        String strNullToEmpty = Strings.nullToEmpty(contentValues.getAsString("name"));
        String strNullToEmpty2 = Strings.nullToEmpty(contentValues.getAsString("phone_number"));
        acquireWriteLockOrThrow();
        try {
            List listLoadRecordsForEf = loadRecordsForEf(phonebookArgs);
            try {
                if (listLoadRecordsForEf == null) {
                    Rlog.e("SimPhonebookProvider", "Failed to load existing records for " + phonebookArgs.uri);
                    releaseWriteLock();
                    return null;
                }
                Iterator it = listLoadRecordsForEf.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        adnRecord = null;
                        break;
                    }
                    AdnRecord adnRecord2 = (AdnRecord) it.next();
                    IsEmpty = adnRecord2.isEmpty();
                    if (IsEmpty != 0) {
                        adnRecord = adnRecord2;
                        break;
                    }
                }
                try {
                    if (adnRecord == null) {
                        throw new IllegalStateException(phonebookArgs.uri + " is full. Please delete records to add new ones.");
                    }
                    if (updateRecord(phonebookArgs, adnRecord, phonebookArgs.pin2, strNullToEmpty, strNullToEmpty2)) {
                        notifyChange();
                        Uri itemUri = SimPhonebookContract.SimRecords.getItemUri(phonebookArgs.subscriptionId, phonebookArgs.efType, adnRecord.getRecId());
                        releaseWriteLock();
                        return itemUri;
                    }
                    Rlog.e("SimPhonebookProvider", "Insert failed for " + phonebookArgs.uri);
                    releaseWriteLock();
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                simPhonebookProvider = this;
            }
        } catch (Throwable th4) {
            th = th4;
            IsEmpty = this;
        }
        th = th;
        simPhonebookProvider = IsEmpty;
        simPhonebookProvider.releaseWriteLock();
        throw th;
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        throw new UnsupportedOperationException("Only delete with Bundle is supported");
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, Bundle bundle) {
        int iMatch = URI_MATCHER.match(uri);
        if (iMatch == 100 || iMatch == 101 || iMatch == 200) {
            throw new UnsupportedOperationException(uri + " does not support delete");
        }
        if (iMatch == 201) {
            return deleteSimRecordsItem(PhonebookArgs.forSimRecordsItem(uri, bundle));
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("Unsupported Uri ", uri);
        return 0;
    }

    private int deleteSimRecordsItem(PhonebookArgs phonebookArgs) throws Throwable {
        SimPhonebookProvider simPhonebookProvider;
        validateWritableEf(phonebookArgs, "delete");
        validateSubscriptionAndEf(phonebookArgs);
        acquireWriteLockOrThrow();
        try {
            AdnRecord adnRecordLoadRecord = loadRecord(phonebookArgs);
            if (adnRecordLoadRecord == null || adnRecordLoadRecord.isEmpty()) {
                releaseWriteLock();
                return 0;
            }
            simPhonebookProvider = this;
            try {
                if (!simPhonebookProvider.updateRecord(phonebookArgs, adnRecordLoadRecord, phonebookArgs.pin2, "", "")) {
                    Rlog.e("SimPhonebookProvider", "Failed to delete " + phonebookArgs.uri);
                }
                simPhonebookProvider.notifyChange();
                simPhonebookProvider.releaseWriteLock();
                return 1;
            } catch (Throwable th) {
                th = th;
            }
        } catch (Throwable th2) {
            th = th2;
            simPhonebookProvider = this;
        }
        Throwable th3 = th;
        simPhonebookProvider.releaseWriteLock();
        throw th3;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, Bundle bundle) {
        int iMatch = URI_MATCHER.match(uri);
        if (iMatch == 100 || iMatch == 101 || iMatch == 200) {
            throw new UnsupportedOperationException(uri + " does not support update");
        }
        if (iMatch == 201) {
            return updateSimRecordsItem(PhonebookArgs.forSimRecordsItem(uri, bundle), contentValues);
        }
        OrtProvider$$ExternalSyntheticBUOutline0.m("Unsupported Uri ", uri);
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new UnsupportedOperationException("Only Update with bundle is supported");
    }

    private int updateSimRecordsItem(PhonebookArgs phonebookArgs, ContentValues contentValues) throws Throwable {
        SimPhonebookProvider simPhonebookProvider;
        validateWritableEf(phonebookArgs, "update");
        validateSubscriptionAndEf(phonebookArgs);
        if (contentValues == null || contentValues.isEmpty()) {
            return 0;
        }
        validateValues(phonebookArgs, contentValues);
        String strNullToEmpty = Strings.nullToEmpty(contentValues.getAsString("name"));
        String strNullToEmpty2 = Strings.nullToEmpty(contentValues.getAsString("phone_number"));
        acquireWriteLockOrThrow();
        try {
            AdnRecord adnRecordLoadRecord = loadRecord(phonebookArgs);
            if (adnRecordLoadRecord != null) {
                simPhonebookProvider = this;
                try {
                    if (!simPhonebookProvider.updateRecord(phonebookArgs, adnRecordLoadRecord, phonebookArgs.pin2, strNullToEmpty, strNullToEmpty2)) {
                        Rlog.e("SimPhonebookProvider", "Failed to update " + phonebookArgs.uri);
                        simPhonebookProvider.releaseWriteLock();
                        return 0;
                    }
                    simPhonebookProvider.notifyChange();
                    simPhonebookProvider.releaseWriteLock();
                    return 1;
                } catch (Throwable th) {
                    th = th;
                }
            } else {
                releaseWriteLock();
                return 0;
            }
        } catch (Throwable th2) {
            th = th2;
            simPhonebookProvider = this;
        }
        Throwable th3 = th;
        simPhonebookProvider.releaseWriteLock();
        throw th3;
    }

    void validateSubscriptionAndEf(PhonebookArgs phonebookArgs) {
        int i = phonebookArgs.subscriptionId;
        if ((i != -1 ? getActiveSubscriptionInfo(i) : null) == null) {
            BackStackRecord$$ExternalSyntheticBUOutline0.m("No active SIM with subscription ID ", phonebookArgs.subscriptionId);
            return;
        }
        int[] recordsSizeForEf = getRecordsSizeForEf(phonebookArgs);
        if (recordsSizeForEf == null || recordsSizeForEf[1] == 0) {
            throw new IllegalArgumentException(phonebookArgs.efName + " is not supported for SIM with subscription ID " + phonebookArgs.subscriptionId);
        }
    }

    private void acquireWriteLockOrThrow() {
        try {
            if (this.mWriteLock.tryLock(30L, TimeUnit.SECONDS)) {
            } else {
                throw new IllegalStateException("Timeout waiting to write");
            }
        } catch (InterruptedException unused) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Write failed");
        }
    }

    private void releaseWriteLock() {
        this.mWriteLock.unlock();
    }

    private void validateWritableEf(PhonebookArgs phonebookArgs, String str) {
        if ((phonebookArgs.efType == 2 && hasPermissionsForFdnWrite(phonebookArgs)) || phonebookArgs.efType == 1) {
            return;
        }
        throw new UnsupportedOperationException(phonebookArgs.uri + " does not support " + str);
    }

    private boolean hasPermissionsForFdnWrite(PhonebookArgs phonebookArgs) {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(TelephonyManager.class);
        Objects.requireNonNull(telephonyManager);
        String callingPackage = getCallingPackage();
        return (callingPackage != null ? getContext().createContextAsUser(Binder.getCallingUserHandle(), 0).getPackageManager().checkPermission("android.permission.MODIFY_PHONE_STATE", callingPackage) : -1) == 0 || telephonyManager.hasCarrierPrivileges(phonebookArgs.subscriptionId);
    }

    private boolean updateRecord(PhonebookArgs phonebookArgs, AdnRecord adnRecord, String str, String str2, String str3) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("newTag", str2);
            contentValues.put("newNumber", str3);
            return ((IIccPhoneBook) this.mIccPhoneBookSupplier.get()).updateAdnRecordsInEfByIndexForSubscriber(phonebookArgs.subscriptionId, adnRecord.getEfid(), contentValues, adnRecord.getRecId(), str);
        } catch (RemoteException unused) {
            return false;
        }
    }

    private void validatePhoneNumber(String str) {
        if (str == null || str.isEmpty()) {
            Folme$$ExternalSyntheticBUOutline0.m("phone_number is required.");
            return;
        }
        int length = str.length();
        if (str.startsWith("+")) {
            length--;
        }
        if (length > AdnRecord.getMaxPhoneNumberDigits()) {
            Folme$$ExternalSyntheticBUOutline0.m("phone_number is too long.");
            return;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!PhoneNumberUtils.isNonSeparator(str.charAt(i))) {
                Folme$$ExternalSyntheticBUOutline0.m("phone_number contains unsupported characters.");
                return;
            }
        }
    }

    private void validateValues(PhonebookArgs phonebookArgs, ContentValues contentValues) {
        Set set = SIM_RECORDS_WRITABLE_COLUMNS;
        if (!set.containsAll(contentValues.keySet())) {
            ArraySet arraySet = new ArraySet(contentValues.keySet());
            arraySet.removeAll(set);
            OrtUtil$$ExternalSyntheticBUOutline4.m("Unsupported columns: ", Joiner.on(',').join(arraySet));
            return;
        }
        validatePhoneNumber(contentValues.getAsString("phone_number"));
        int encodedNameLength = getEncodedNameLength(contentValues.getAsString("name"));
        int[] recordsSizeForEf = getRecordsSizeForEf(phonebookArgs);
        if (recordsSizeForEf == null) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Failed to get name_max_length from SIM");
        } else {
            if (encodedNameLength <= AdnRecord.getMaxAlphaTagBytes(getRecordSize(recordsSizeForEf))) {
                return;
            }
            Folme$$ExternalSyntheticBUOutline0.m("name is too long.");
        }
    }

    private List getActiveSubscriptionInfoList() {
        ContentProvider.CallingIdentity callingIdentityClearCallingIdentity = clearCallingIdentity();
        try {
            return this.mSubscriptionManager.getActiveSubscriptionInfoList();
        } finally {
            restoreCallingIdentity(callingIdentityClearCallingIdentity);
        }
    }

    private SubscriptionInfo getActiveSubscriptionInfo(int i) {
        ContentProvider.CallingIdentity callingIdentityClearCallingIdentity = clearCallingIdentity();
        try {
            return this.mSubscriptionManager.getActiveSubscriptionInfo(i);
        } finally {
            restoreCallingIdentity(callingIdentityClearCallingIdentity);
        }
    }

    private List loadRecordsForEf(PhonebookArgs phonebookArgs) {
        try {
            return ((IIccPhoneBook) this.mIccPhoneBookSupplier.get()).getAdnRecordsInEfForSubscriber(phonebookArgs.subscriptionId, phonebookArgs.efid);
        } catch (RemoteException unused) {
            return null;
        }
    }

    private AdnRecord loadRecord(PhonebookArgs phonebookArgs) {
        List listLoadRecordsForEf = loadRecordsForEf(phonebookArgs);
        if (listLoadRecordsForEf == null || phonebookArgs.recordNumber > listLoadRecordsForEf.size()) {
            return null;
        }
        return (AdnRecord) listLoadRecordsForEf.get(phonebookArgs.recordNumber - 1);
    }

    private int[] getRecordsSizeForEf(PhonebookArgs phonebookArgs) {
        try {
            return ((IIccPhoneBook) this.mIccPhoneBookSupplier.get()).getAdnRecordsSizeForSubscriber(phonebookArgs.subscriptionId, phonebookArgs.efid);
        } catch (RemoteException unused) {
            return null;
        }
    }

    void notifyChange() {
        this.mContentNotifier.notifyChange(SimPhonebookContract.AUTHORITY_URI);
    }

    class PhonebookArgs {
        public final String efName;
        public final int efType;
        public final int efid;
        public final String pin2;
        public final int recordNumber;
        public final int subscriptionId;
        public final Uri uri;

        PhonebookArgs(Uri uri, int i, String str, int i2, int i3, int i4, Bundle bundle) {
            this.uri = uri;
            this.subscriptionId = i;
            this.efName = str;
            this.efType = i2;
            this.efid = i3;
            this.recordNumber = i4;
            this.pin2 = (i2 != 2 || bundle == null) ? null : bundle.getString("android:query-arg-pin2");
        }

        static PhonebookArgs createFromEfName(Uri uri, int i, String str, int i2, Bundle bundle) {
            int i3;
            int i4;
            int i5;
            int i6;
            if (str != null) {
                str.hashCode();
                switch (str) {
                    case "adn":
                        i5 = 28474;
                        i6 = 1;
                        break;
                    case "fdn":
                        i5 = 28475;
                        i6 = 2;
                        break;
                    case "sdn":
                        i6 = 3;
                        i5 = 28489;
                        break;
                    default:
                        Folme$$ExternalSyntheticBUOutline0.m("Unrecognized elementary file ".concat(str));
                        return null;
                }
                i3 = i6;
                i4 = i5;
            } else {
                i3 = 0;
                i4 = 0;
            }
            return new PhonebookArgs(uri, i, str, i3, i4, i2, bundle);
        }

        static PhonebookArgs forElementaryFilesItem(Uri uri) {
            return createFromEfName(uri, parseSubscriptionIdFromUri(uri, 2), uri.getPathSegments().get(3), -1, null);
        }

        static PhonebookArgs forSimRecords(Uri uri, Bundle bundle) {
            return createFromEfName(uri, parseSubscriptionIdFromUri(uri, 1), uri.getPathSegments().get(2), -1, bundle);
        }

        static PhonebookArgs forSimRecordsItem(Uri uri, Bundle bundle) {
            return createFromEfName(uri, parseSubscriptionIdFromUri(uri, 1), uri.getPathSegments().get(2), parseRecordNumberFromUri(uri, 3), bundle);
        }

        private static int parseSubscriptionIdFromUri(Uri uri, int i) {
            if (i == -1) {
                return -1;
            }
            String str = uri.getPathSegments().get(i);
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException unused) {
                OrtProvider$$ExternalSyntheticBUOutline0.m("Invalid subscription ID: ", str);
                return 0;
            }
        }

        private static int parseRecordNumberFromUri(Uri uri, int i) {
            try {
                return Integer.parseInt(uri.getPathSegments().get(i));
            } catch (NumberFormatException unused) {
                OrtUtil$$ExternalSyntheticBUOutline4.m("Invalid record index: ", uri.getLastPathSegment());
                return 0;
            }
        }
    }
}
