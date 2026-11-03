package com.mediatek.internal.telephony.util;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;
import mediatek.telephony.MtkTelephony;

/* JADX INFO: loaded from: classes.dex */
public class CallerInfo {
    public static final long USER_TYPE_CURRENT = 0;
    public static final long USER_TYPE_WORK = 1;
    public Drawable cachedPhoto;
    public Bitmap cachedPhotoIcon;
    public String cnapName;
    private Uri contactDisplayPhotoUri;
    public boolean contactExists;
    private long contactIdOrZero;
    public Uri contactRefUri;
    public Uri contactRingtoneUri;
    public boolean isCachedPhotoCurrent;
    public String lookupKey;
    private String name;
    public int namePresentation;
    public boolean needUpdate;
    public String normalizedNumber;
    public String numberLabel;
    public int numberPresentation;
    public int numberType;
    public String phoneLabel;
    private String phoneNumber;
    public int photoResource;
    public ComponentName preferredPhoneAccountComponent;
    public String preferredPhoneAccountId;
    public boolean shouldSendToVoicemail;
    private static final String TAG = "CallerInfo";
    private static final boolean VDBG = Log.isLoggable(TAG, 2);
    private boolean mIsEmergency = false;
    private boolean mIsVoiceMail = false;
    public long userType = 0;

    public static CallerInfo getCallerInfo(Context context, Uri contactRef, Cursor cursor) {
        boolean z;
        int typeColumnIndex;
        CallerInfo info = new CallerInfo();
        info.photoResource = 0;
        info.phoneLabel = null;
        info.numberType = 0;
        info.numberLabel = null;
        info.cachedPhoto = null;
        info.isCachedPhotoCurrent = false;
        info.contactExists = false;
        info.userType = 0L;
        if (VDBG) {
            Log.v(TAG, "getCallerInfo() based on cursor...");
        }
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex("display_name");
                if (columnIndex != -1) {
                    info.name = cursor.getString(columnIndex);
                }
                int columnIndex2 = cursor.getColumnIndex(MtkTelephony.SmsCb.CbChannel.NUMBER);
                if (columnIndex2 != -1) {
                    info.phoneNumber = cursor.getString(columnIndex2);
                }
                int columnIndex3 = cursor.getColumnIndex("normalized_number");
                if (columnIndex3 != -1) {
                    info.normalizedNumber = cursor.getString(columnIndex3);
                }
                int columnIndex4 = cursor.getColumnIndex("label");
                if (columnIndex4 != -1 && (typeColumnIndex = cursor.getColumnIndex("type")) != -1) {
                    info.numberType = cursor.getInt(typeColumnIndex);
                    info.numberLabel = cursor.getString(columnIndex4);
                    info.phoneLabel = ContactsContract.CommonDataKinds.Phone.getTypeLabel(context.getResources(), info.numberType, info.numberLabel).toString();
                }
                int columnIndex5 = getColumnIndexForPersonId(contactRef, cursor);
                if (columnIndex5 != -1) {
                    long contactId = cursor.getLong(columnIndex5);
                    if (contactId != 0 && !ContactsContract.Contacts.isEnterpriseContactId(contactId)) {
                        info.contactIdOrZero = contactId;
                        if (VDBG) {
                            Log.v(TAG, "==> got info.contactIdOrZero: " + info.contactIdOrZero);
                        }
                    }
                    if (ContactsContract.Contacts.isEnterpriseContactId(contactId)) {
                        info.userType = 1L;
                    }
                } else {
                    Log.w(TAG, "Couldn't find contact_id column for " + contactRef);
                }
                int columnIndex6 = cursor.getColumnIndex("lookup");
                if (columnIndex6 != -1) {
                    info.lookupKey = cursor.getString(columnIndex6);
                }
                int columnIndex7 = cursor.getColumnIndex("photo_uri");
                if (columnIndex7 != -1 && cursor.getString(columnIndex7) != null) {
                    info.contactDisplayPhotoUri = Uri.parse(cursor.getString(columnIndex7));
                } else {
                    info.contactDisplayPhotoUri = null;
                }
                int columnIndex8 = cursor.getColumnIndex("preferred_phone_account_component_name");
                if (columnIndex8 != -1 && cursor.getString(columnIndex8) != null) {
                    info.preferredPhoneAccountComponent = ComponentName.unflattenFromString(cursor.getString(columnIndex8));
                }
                int columnIndex9 = cursor.getColumnIndex("preferred_phone_account_id");
                if (columnIndex9 != -1 && cursor.getString(columnIndex9) != null) {
                    info.preferredPhoneAccountId = cursor.getString(columnIndex9);
                }
                int columnIndex10 = cursor.getColumnIndex("custom_ringtone");
                if (columnIndex10 != -1 && cursor.getString(columnIndex10) != null) {
                    if (TextUtils.isEmpty(cursor.getString(columnIndex10))) {
                        info.contactRingtoneUri = Uri.EMPTY;
                    } else {
                        info.contactRingtoneUri = Uri.parse(cursor.getString(columnIndex10));
                    }
                } else {
                    info.contactRingtoneUri = null;
                }
                int columnIndex11 = cursor.getColumnIndex("send_to_voicemail");
                if (columnIndex11 == -1 || cursor.getInt(columnIndex11) != 1) {
                    z = false;
                } else {
                    z = true;
                }
                info.shouldSendToVoicemail = z;
                info.contactExists = true;
            }
            cursor.close();
        }
        info.needUpdate = false;
        info.name = normalize(info.name);
        info.contactRefUri = contactRef;
        return info;
    }

    public static CallerInfo getCallerInfo(Context context, Uri contactRef) {
        ContentResolver cr = CallerInfoAsyncQuery.getCurrentProfileContentResolver(context);
        if (cr == null) {
            return null;
        }
        try {
            CallerInfo info = getCallerInfo(context, contactRef, cr.query(contactRef, null, null, null, null));
            return info;
        } catch (RuntimeException re) {
            Log.e(TAG, "Error getting caller info." + re.toString());
            return null;
        }
    }

    public static CallerInfo getCallerInfo(Context context, String number) {
        if (VDBG) {
            Log.v(TAG, "getCallerInfo() based on number...");
        }
        int subId = SubscriptionManager.getDefaultSubscriptionId();
        return getCallerInfo(context, number, subId);
    }

    public static CallerInfo getCallerInfo(Context context, String number, int subId) {
        if (TextUtils.isEmpty(number)) {
            return null;
        }
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            if (tm.isEmergencyNumber(number)) {
                return new CallerInfo().markAsEmergency(context);
            }
            if (PhoneNumberUtils.isVoiceMailNumber(null, subId, number)) {
                return new CallerInfo().markAsVoiceMail(context, subId);
            }
            Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI, Uri.encode(number));
            CallerInfo info = doSecondaryLookupIfNecessary(context, number, getCallerInfo(context, contactUri));
            if (TextUtils.isEmpty(info.phoneNumber)) {
                info.phoneNumber = number;
            }
            return info;
        } catch (IllegalStateException | UnsupportedOperationException e) {
        } catch (RuntimeException ex) {
            Log.e(TAG, "Error getting caller info.", ex);
        }
    }

    static CallerInfo doSecondaryLookupIfNecessary(Context context, String number, CallerInfo previousResult) {
        if (!previousResult.contactExists && PhoneNumberUtils.isUriNumber(number)) {
            String username = PhoneNumberUtils.getUsernameFromUriNumber(number);
            if (PhoneNumberUtils.isGlobalPhoneNumber(username)) {
                return getCallerInfo(context, Uri.withAppendedPath(ContactsContract.PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI, Uri.encode(username)));
            }
            return previousResult;
        }
        return previousResult;
    }

    private static String normalize(String s) {
        if (s == null || s.length() > 0) {
            return s;
        }
        return null;
    }

    private static int getColumnIndexForPersonId(Uri contactRef, Cursor cursor) {
        if (VDBG) {
            Log.v(TAG, "- getColumnIndexForPersonId: contactRef URI = '" + contactRef + "'...");
        }
        String url = contactRef.toString();
        String columnName = null;
        if (url.startsWith("content://com.android.contacts/data/phones")) {
            if (VDBG) {
                Log.v(TAG, "'data/phones' URI; using RawContacts.CONTACT_ID");
            }
            columnName = "contact_id";
        } else if (url.startsWith("content://com.android.contacts/data")) {
            if (VDBG) {
                Log.v(TAG, "'data' URI; using Data.CONTACT_ID");
            }
            columnName = "contact_id";
        } else if (url.startsWith("content://com.android.contacts/phone_lookup")) {
            if (VDBG) {
                Log.v(TAG, "'phone_lookup' URI; using PhoneLookup._ID");
            }
            columnName = "_id";
        } else {
            Log.w(TAG, "Unexpected prefix for contactRef '" + url + "'");
        }
        int columnIndex = columnName != null ? cursor.getColumnIndex(columnName) : -1;
        if (VDBG) {
            Log.v(TAG, "==> Using column '" + columnName + "' (columnIndex = " + columnIndex + ") for person_id lookup...");
        }
        return columnIndex;
    }

    private static String getCurrentCountryIso(Context context, Locale locale) {
        String countryIso = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        if (tm != null) {
            countryIso = tm.getNetworkCountryIso().toUpperCase();
        }
        if (countryIso == null || countryIso.isEmpty()) {
            String countryIso2 = locale.getCountry();
            Log.w(TAG, "No CountryDetector; falling back to countryIso based on locale: " + countryIso2);
            return countryIso2;
        }
        return countryIso;
    }

    protected static String getCurrentCountryIso(Context context) {
        return getCurrentCountryIso(context, Locale.getDefault());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String number) {
        this.phoneNumber = number;
    }

    public long getContactId() {
        return this.contactIdOrZero;
    }

    public Uri getContactDisplayPhotoUri() {
        return this.contactDisplayPhotoUri;
    }

    public void SetContactDisplayPhotoUri(Uri photoUri) {
        this.contactDisplayPhotoUri = photoUri;
    }

    public boolean isEmergencyNumber() {
        return this.mIsEmergency;
    }

    public boolean isVoiceMailNumber() {
        return this.mIsVoiceMail;
    }

    CallerInfo markAsEmergency(Context context) {
        int resourceId = Resources.getSystem().getIdentifier("emergency_call_dialog_number_for_display", "string", "android");
        if (resourceId != 0) {
            this.phoneNumber = context.getResources().getString(resourceId);
        }
        this.photoResource = Resources.getSystem().getIdentifier("picture_emergency", "drawable", "android");
        this.mIsEmergency = true;
        return this;
    }

    CallerInfo markAsVoiceMail(Context context, int subId) {
        this.mIsVoiceMail = true;
        try {
            this.phoneNumber = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(subId).getVoiceMailAlphaTag();
        } catch (SecurityException e) {
            Log.e(TAG, "Cannot access VoiceMail.");
        }
        return this;
    }

    public String toString() {
        return super.toString() + " { name " + (this.name == null ? "null" : "non-null") + ", phoneNumber " + (this.phoneNumber != null ? "non-null" : "null") + " }";
    }
}
