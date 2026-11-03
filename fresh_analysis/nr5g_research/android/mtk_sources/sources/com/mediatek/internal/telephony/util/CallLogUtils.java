package com.mediatek.internal.telephony.util;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.CallLog;
import android.telecom.PhoneAccount;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.hidden_from_bootclasspath.com.android.server.telecom.flags.Flags;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import mediatek.telephony.MtkTelephony;

/* JADX INFO: loaded from: classes.dex */
public class CallLogUtils {
    public static final String ADD_FOR_ALL_USERS = "add_for_all_users";
    private static final int DEFAULT_MAX_CALL_LOG_SIZE = 500;
    public static final String IS_PHONE_ACCOUNT_MIGRATION_PENDING = "is_call_log_phone_account_migration_pending";
    private static final String LOG_TAG = "CallLogUtils";
    private static final int MIN_DURATION_FOR_NORMALIZED_NUMBER_UPDATE_MS = 10000;
    public static final String PHONE_ACCOUNT_ADDRESS = "phone_account_address";
    private static final String SHADOW_AUTHORITY = "call_log_shadow";
    private static final boolean VERBOSE_LOG = false;
    private static final ComponentName TELEPHONY_COMPONENT_NAME = new ComponentName("com.android.phone", "com.android.services.telephony.TelephonyConnectionService");
    private static final Uri SHADOW_CONTENT_URI = Uri.parse("content://call_log_shadow/calls");

    public static Uri addCall(CallerInfo ci, Context context, String number, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage, long missedReason, int isPhoneAccountMigrationPending) {
        return addCall(ci, context, number, "", "", presentation, callType, features, accountHandle, start, duration, dataUsage, VERBOSE_LOG, null, VERBOSE_LOG, 0, null, null, missedReason, isPhoneAccountMigrationPending);
    }

    public static Uri addCall(CallerInfo ci, Context context, String number, String postDialDigits, String viaNumber, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage, boolean addForAllUsers, UserHandle userToBeInsertedTo, long missedReason, int isPhoneAccountMigrationPending) {
        return addCall(ci, context, number, postDialDigits, viaNumber, presentation, callType, features, accountHandle, start, duration, dataUsage, addForAllUsers, userToBeInsertedTo, VERBOSE_LOG, 0, null, null, missedReason, isPhoneAccountMigrationPending);
    }

    public static Uri addCall(CallerInfo ci, Context context, String number, String postDialDigits, String viaNumber, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, Long dataUsage, boolean addForAllUsers, UserHandle userToBeInsertedTo, boolean isRead, int callBlockReason, CharSequence callScreeningAppName, String callScreeningComponentName, long missedReason, int isPhoneAccountMigrationPending) {
        AddCallParams.AddCallParametersBuilder builder = new AddCallParams.AddCallParametersBuilder();
        builder.setCallerInfo(ci);
        builder.setNumber(number);
        builder.setPostDialDigits(postDialDigits);
        builder.setViaNumber(viaNumber);
        builder.setPresentation(presentation);
        builder.setCallType(callType);
        builder.setFeatures(features);
        builder.setAccountHandle(accountHandle);
        builder.setStart(start);
        builder.setDuration(duration);
        builder.setDataUsage(dataUsage == null ? Long.MIN_VALUE : dataUsage.longValue());
        builder.setAddForAllUsers(addForAllUsers);
        builder.setUserToBeInsertedTo(userToBeInsertedTo);
        builder.setIsRead(isRead);
        builder.setCallBlockReason(callBlockReason);
        builder.setCallScreeningAppName(callScreeningAppName);
        builder.setCallScreeningComponentName(callScreeningComponentName);
        builder.setMissedReason(missedReason);
        builder.setIsPhoneAccountMigrationPending(isPhoneAccountMigrationPending);
        return addCall(context, builder.build());
    }

    public static Uri addCall(Context context, AddCallParams addCallParams) {
        UserHandle userHandleOf;
        String str;
        Uri uriMaybeInsertLocation;
        ContentResolver contentResolver = context.getContentResolver();
        String logAccountAddress = getLogAccountAddress(context, addCallParams.mAccountHandle);
        int logNumberPresentation = getLogNumberPresentation(addCallParams.mNumber, addCallParams.mPresentation);
        String name = addCallParams.mCallerInfo != null ? addCallParams.mCallerInfo.getName() : "";
        if (logNumberPresentation == 2) {
            addCallParams.mNumber = "";
            if (addCallParams.mCallerInfo != null) {
                name = "";
            }
        }
        String strFlattenToString = null;
        String id = null;
        if (addCallParams.mAccountHandle != null) {
            strFlattenToString = addCallParams.mAccountHandle.getComponentName().flattenToString();
            id = addCallParams.mAccountHandle.getId();
        }
        ContentValues contentValues = new ContentValues(14);
        contentValues.put(MtkTelephony.SmsCb.CbChannel.NUMBER, addCallParams.mNumber);
        contentValues.put("post_dial_digits", addCallParams.mPostDialDigits);
        contentValues.put("via_number", addCallParams.mViaNumber);
        contentValues.put("presentation", Integer.valueOf(logNumberPresentation));
        contentValues.put("type", Integer.valueOf(addCallParams.mCallType));
        contentValues.put("features", Integer.valueOf(addCallParams.mFeatures));
        contentValues.put("date", Long.valueOf(addCallParams.mStart));
        contentValues.put("duration", Long.valueOf(addCallParams.mDuration));
        if (addCallParams.mDataUsage != Long.MIN_VALUE) {
            contentValues.put("data_usage", Long.valueOf(addCallParams.mDataUsage));
        }
        contentValues.put("subscription_component_name", strFlattenToString);
        contentValues.put("subscription_id", id);
        contentValues.put(PHONE_ACCOUNT_ADDRESS, logAccountAddress);
        contentValues.put("new", (Integer) 1);
        contentValues.put("name", name);
        contentValues.put(ADD_FOR_ALL_USERS, Integer.valueOf(addCallParams.mAddForAllUsers ? 1 : 0));
        if (addCallParams.mCallType == 3) {
            contentValues.put("is_read", Integer.valueOf(addCallParams.mIsRead ? 1 : 0));
        }
        contentValues.put("block_reason", Integer.valueOf(addCallParams.mCallBlockReason));
        contentValues.put("call_screening_app_name", charSequenceToString(addCallParams.mCallScreeningAppName));
        contentValues.put("call_screening_component_name", addCallParams.mCallScreeningComponentName);
        contentValues.put("missed_reason", Long.valueOf(addCallParams.mMissedReason));
        contentValues.put(MtkTelephony.MtkMwi.PRIORITY, Integer.valueOf(addCallParams.mPriority));
        contentValues.put(MtkTelephony.MtkMwi.SUBJECT, addCallParams.mSubject);
        if (addCallParams.mPictureUri != null) {
            contentValues.put("composer_photo_uri", addCallParams.mPictureUri.toString());
        }
        contentValues.put(IS_PHONE_ACCOUNT_MIGRATION_PENDING, Integer.valueOf(addCallParams.mIsPhoneAccountMigrationPending));
        if (Flags.businessCallComposer()) {
            contentValues.put("is_business_call", Integer.valueOf(addCallParams.mIsBusinessCall ? 1 : 0));
            contentValues.put("asserted_display_name", addCallParams.mAssertedDisplayName);
        }
        if (Flags.integratedCallLogs()) {
            contentValues.put("uuid", addCallParams.mUuid);
        }
        if (com.android.internal.hidden_from_bootclasspath.android.telecom.flags.Flags.integratedCallLogsStage2() && addCallParams.mVoipContactLookupUri != null) {
            contentValues.put("lookup_uri", addCallParams.mVoipContactLookupUri.toString());
        }
        if (Flags.supportDisplayNameCallLog()) {
            contentValues.put("preferred_display_name", addCallParams.mPreferredDisplayName);
        }
        Uri uri = null;
        UserManager userManager = (UserManager) context.getSystemService(UserManager.class);
        int iMyUserId = UserHandle.myUserId();
        if (addCallParams.mAddForAllUsers) {
            if (!userManager.isUserUnlocked(UserHandle.SYSTEM) || (uriMaybeInsertLocation = maybeInsertLocation(addCallParams, contentResolver, UserHandle.SYSTEM)) == null) {
                str = null;
            } else {
                str = null;
                contentValues.put("location", uriMaybeInsertLocation.toString());
            }
            Uri uriAddEntryAndRemoveExpiredEntries = addEntryAndRemoveExpiredEntries(context, userManager, UserHandle.SYSTEM, contentValues);
            if (uriAddEntryAndRemoveExpiredEntries != null && !SHADOW_AUTHORITY.equals(uriAddEntryAndRemoveExpiredEntries.getAuthority())) {
                if (userManager.isSystemUser()) {
                    uri = uriAddEntryAndRemoveExpiredEntries;
                }
                List userHandles = userManager.getUserHandles(true);
                int size = userHandles.size();
                int i = 0;
                while (i < size) {
                    int i2 = i;
                    UserHandle userHandle = (UserHandle) userHandles.get(i);
                    int i3 = logNumberPresentation;
                    int identifier = userHandle.getIdentifier();
                    if (userHandle.isSystem()) {
                        name = name;
                    } else if (!shouldHaveSharedCallLogEntries(context, userManager, userHandle)) {
                        name = name;
                    } else if (!userManager.isUserRunning(userHandle)) {
                        name = name;
                    } else if (!userManager.isUserUnlocked(userHandle)) {
                        name = name;
                    } else {
                        Uri uriMaybeInsertLocation2 = maybeInsertLocation(addCallParams, contentResolver, userHandle);
                        if (uriMaybeInsertLocation2 != null) {
                            contentValues.put("location", uriMaybeInsertLocation2.toString());
                        } else {
                            contentValues.put("location", str);
                        }
                        Uri uriAddEntryAndRemoveExpiredEntries2 = addEntryAndRemoveExpiredEntries(context, userManager, userHandle, contentValues);
                        if (identifier == iMyUserId) {
                            uri = uriAddEntryAndRemoveExpiredEntries2;
                        }
                    }
                    i = i2 + 1;
                    logNumberPresentation = i3;
                    name = name;
                    str = null;
                }
                return uri;
            }
            return null;
        }
        if (addCallParams.mUserToBeInsertedTo != null) {
            userHandleOf = addCallParams.mUserToBeInsertedTo;
        } else {
            userHandleOf = UserHandle.of(iMyUserId);
        }
        if (userManager.isUserRunning(userHandleOf) && userManager.isUserUnlocked(userHandleOf)) {
            Uri uriMaybeInsertLocation3 = maybeInsertLocation(addCallParams, contentResolver, userHandleOf);
            if (uriMaybeInsertLocation3 != null) {
                contentValues.put("location", uriMaybeInsertLocation3.toString());
            } else {
                contentValues.put("location", (String) null);
            }
        }
        return addEntryAndRemoveExpiredEntries(context, userManager, userHandleOf, contentValues);
    }

    private static String charSequenceToString(CharSequence sequence) {
        if (sequence == null) {
            return null;
        }
        return sequence.toString();
    }

    private static boolean shouldHaveSharedCallLogEntries(Context context, UserManager userManager, UserHandle userHandle) {
        if (userManager.hasUserRestrictionForUser("no_outgoing_calls", userHandle)) {
            return VERBOSE_LOG;
        }
        return !isProfile(context, userHandle);
    }

    private static boolean isProfile(Context context, UserHandle userHandle) {
        try {
            return ((UserManager) context.createContextAsUser(userHandle, 0).getSystemService(UserManager.class)).isProfile();
        } catch (IllegalStateException e) {
            Log.e(LOG_TAG, e + "Error while creating context as user = " + userHandle);
            return VERBOSE_LOG;
        }
    }

    private static Uri addEntryAndRemoveExpiredEntries(Context context, UserManager userManager, UserHandle user, ContentValues values) {
        Uri baseUri;
        int numDeleted;
        ContentResolver resolver = context.getContentResolver();
        String uuid = values.containsKey("uuid") ? values.getAsString("uuid") : null;
        boolean handlingVoipEntry = uuid != null ? true : VERBOSE_LOG;
        if (handlingVoipEntry && Flags.integratedCallLogs()) {
            baseUri = CallLog.Calls.CONTENT_VOIP_URI;
        } else {
            baseUri = CallLog.Calls.CONTENT_URI;
        }
        Uri uri = maybeAddUserId(userManager.isUserUnlocked(user) ? baseUri : SHADOW_CONTENT_URI, user.getIdentifier());
        Log.i(LOG_TAG, String.format(Locale.getDefault(), "addEntryAndRemoveExpiredEntries: provider uri=%s", uri));
        try {
            Uri result = resolver.insert(uri, values);
            if (result != null) {
                String lastPathSegment = result.getLastPathSegment();
                if (lastPathSegment != null && lastPathSegment.equals("0")) {
                    Log.w(LOG_TAG, "Failed to insert into call log due to appops denial; resultUri=" + result);
                }
            } else {
                Log.w(LOG_TAG, "Failed to insert into call log; null result uri.");
            }
            String phoneAccountId = values.containsKey("subscription_id") ? values.getAsString("subscription_id") : null;
            String phoneAccountComponentName = values.containsKey("subscription_component_name") ? values.getAsString("subscription_component_name") : null;
            int maxCallLogSize = 500;
            if (TextUtils.isEmpty(phoneAccountId) || TextUtils.isEmpty(phoneAccountComponentName)) {
                numDeleted = resolver.delete(uri, "_id IN (SELECT _id FROM calls ORDER BY date DESC LIMIT -1 OFFSET 500)", null);
            } else {
                if (TELEPHONY_COMPONENT_NAME.flattenToString().equals(phoneAccountComponentName)) {
                    int resId = context.getResources().getIdentifier("config_maximumCallLogEntriesPerSim", "integer", "android");
                    maxCallLogSize = context.getResources().getInteger(resId);
                }
                numDeleted = resolver.delete(uri, "_id IN (SELECT _id FROM calls WHERE subscription_component_name = ? AND subscription_id = ? ORDER BY date DESC LIMIT -1 OFFSET " + maxCallLogSize + ")", new String[]{phoneAccountComponentName, phoneAccountId});
            }
            Log.i(LOG_TAG, "addEntry: cleaned up " + numDeleted + " old entries");
            return result;
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, "Failed to insert calllog", e);
            return null;
        }
    }

    private static Uri maybeInsertLocation(AddCallParams params, ContentResolver resolver, UserHandle user) {
        if (Double.isNaN(params.mLatitude) || Double.isNaN(params.mLongitude)) {
            return null;
        }
        ContentValues locationValues = new ContentValues();
        locationValues.put("latitude", Double.valueOf(params.mLatitude));
        locationValues.put("longitude", Double.valueOf(params.mLongitude));
        Uri locationUri = maybeAddUserId(CallLog.Locations.CONTENT_URI, user.getIdentifier());
        try {
            return resolver.insert(locationUri, locationValues);
        } catch (SecurityException e) {
            Log.w(LOG_TAG, "Skipping inserting location for " + e);
            return null;
        }
    }

    private static int getLogNumberPresentation(String number, int presentation) {
        if (presentation == 2) {
            return 2;
        }
        if (presentation == 4) {
            return 4;
        }
        if (presentation == 5) {
            return 5;
        }
        return (TextUtils.isEmpty(number) || presentation == 3) ? 3 : 1;
    }

    private static String getLogAccountAddress(Context context, PhoneAccountHandle accountHandle) {
        PhoneAccount account;
        Uri address;
        TelecomManager tm = null;
        try {
            tm = (TelecomManager) context.getSystemService(TelecomManager.class);
        } catch (UnsupportedOperationException e) {
        }
        if (tm == null || accountHandle == null || (account = tm.getPhoneAccount(accountHandle)) == null || (address = account.getSubscriptionAddress()) == null) {
            return null;
        }
        String accountAddress = address.getSchemeSpecificPart();
        return accountAddress;
    }

    public static class AddCallParams {
        private final PhoneAccountHandle mAccountHandle;
        private final boolean mAddForAllUsers;
        private String mAssertedDisplayName;
        private final int mCallBlockReason;
        private final CharSequence mCallScreeningAppName;
        private final String mCallScreeningComponentName;
        private final int mCallType;
        private final CallerInfo mCallerInfo;
        private final long mDataUsage;
        private final int mDuration;
        private final int mFeatures;
        private boolean mIsBusinessCall;
        private final int mIsPhoneAccountMigrationPending;
        private final boolean mIsRead;
        private double mLatitude;
        private double mLongitude;
        private final long mMissedReason;
        private String mNumber;
        private final Uri mPictureUri;
        private final String mPostDialDigits;
        private String mPreferredDisplayName;
        private final int mPresentation;
        private final int mPriority;
        private final long mStart;
        private final String mSubject;
        private final UserHandle mUserToBeInsertedTo;
        private String mUuid;
        private final String mViaNumber;
        private Uri mVoipContactLookupUri;

        private AddCallParams(CallerInfo callerInfo, String number, String postDialDigits, String viaNumber, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, long dataUsage, boolean addForAllUsers, UserHandle userToBeInsertedTo, boolean isRead, int callBlockReason, CharSequence callScreeningAppName, String callScreeningComponentName, long missedReason, int priority, String subject, double latitude, double longitude, Uri pictureUri, int isPhoneAccountMigrationPending) {
            this.mLatitude = Double.NaN;
            this.mLongitude = Double.NaN;
            this.mCallerInfo = callerInfo;
            this.mNumber = number;
            this.mPostDialDigits = postDialDigits;
            this.mViaNumber = viaNumber;
            this.mPresentation = presentation;
            this.mCallType = callType;
            this.mFeatures = features;
            this.mAccountHandle = accountHandle;
            this.mStart = start;
            this.mDuration = duration;
            this.mDataUsage = dataUsage;
            this.mAddForAllUsers = addForAllUsers;
            this.mUserToBeInsertedTo = userToBeInsertedTo;
            this.mIsRead = isRead;
            this.mCallBlockReason = callBlockReason;
            this.mCallScreeningAppName = callScreeningAppName;
            this.mCallScreeningComponentName = callScreeningComponentName;
            this.mMissedReason = missedReason;
            this.mPriority = priority;
            this.mSubject = subject;
            this.mLatitude = latitude;
            this.mLongitude = longitude;
            this.mPictureUri = pictureUri;
            this.mIsPhoneAccountMigrationPending = isPhoneAccountMigrationPending;
        }

        private AddCallParams(CallerInfo callerInfo, String number, String postDialDigits, String viaNumber, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, long dataUsage, boolean addForAllUsers, UserHandle userToBeInsertedTo, boolean isRead, int callBlockReason, CharSequence callScreeningAppName, String callScreeningComponentName, long missedReason, int priority, String subject, double latitude, double longitude, Uri pictureUri, int isPhoneAccountMigrationPending, boolean isBusinessCall, String assertedDisplayName) {
            this.mLatitude = Double.NaN;
            this.mLongitude = Double.NaN;
            this.mCallerInfo = callerInfo;
            this.mNumber = number;
            this.mPostDialDigits = postDialDigits;
            this.mViaNumber = viaNumber;
            this.mPresentation = presentation;
            this.mCallType = callType;
            this.mFeatures = features;
            this.mAccountHandle = accountHandle;
            this.mStart = start;
            this.mDuration = duration;
            this.mDataUsage = dataUsage;
            this.mAddForAllUsers = addForAllUsers;
            this.mUserToBeInsertedTo = userToBeInsertedTo;
            this.mIsRead = isRead;
            this.mCallBlockReason = callBlockReason;
            this.mCallScreeningAppName = callScreeningAppName;
            this.mCallScreeningComponentName = callScreeningComponentName;
            this.mMissedReason = missedReason;
            this.mPriority = priority;
            this.mSubject = subject;
            this.mLatitude = latitude;
            this.mLongitude = longitude;
            this.mPictureUri = pictureUri;
            this.mIsPhoneAccountMigrationPending = isPhoneAccountMigrationPending;
            this.mIsBusinessCall = isBusinessCall;
            this.mAssertedDisplayName = assertedDisplayName;
        }

        private AddCallParams(CallerInfo callerInfo, String number, String postDialDigits, String viaNumber, int presentation, int callType, int features, PhoneAccountHandle accountHandle, long start, int duration, long dataUsage, boolean addForAllUsers, UserHandle userToBeInsertedTo, boolean isRead, int callBlockReason, CharSequence callScreeningAppName, String callScreeningComponentName, long missedReason, int priority, String subject, double latitude, double longitude, Uri pictureUri, int isPhoneAccountMigrationPending, boolean isBusinessCall, String assertedDisplayName, String uuid, String preferredDisplayName, Uri voipLookupUri) {
            this(callerInfo, number, postDialDigits, viaNumber, presentation, callType, features, accountHandle, start, duration, dataUsage, addForAllUsers, userToBeInsertedTo, isRead, callBlockReason, callScreeningAppName, callScreeningComponentName, missedReason, priority, subject, latitude, longitude, pictureUri, isPhoneAccountMigrationPending, isBusinessCall, assertedDisplayName);
            this.mUuid = uuid;
            if (Flags.supportDisplayNameCallLog()) {
                this.mPreferredDisplayName = preferredDisplayName;
            }
            if (com.android.internal.hidden_from_bootclasspath.android.telecom.flags.Flags.integratedCallLogsStage2()) {
                this.mVoipContactLookupUri = voipLookupUri;
            }
        }

        public static final class AddCallParametersBuilder {
            public static final int MAX_NUMBER_OF_CHARACTERS = 256;
            private PhoneAccountHandle mAccountHandle;
            private boolean mAddForAllUsers;
            private String mAssertedDisplayName;
            private CharSequence mCallScreeningAppName;
            private String mCallScreeningComponentName;
            private CallerInfo mCallerInfo;
            private int mDuration;
            private int mFeatures;
            private boolean mIsBusinessCall;
            private int mIsPhoneAccountMigrationPending;
            private boolean mIsRead;
            private String mNumber;
            private Uri mPictureUri;
            private String mPostDialDigits;
            private String mPreferredDisplayName;
            private long mStart;
            private String mSubject;
            private UserHandle mUserToBeInsertedTo;
            private String mUuid;
            private String mViaNumber;
            private Uri mVoipContactLookupUri;
            private int mPresentation = 3;
            private int mCallType = 1;
            private Long mDataUsage = Long.MIN_VALUE;
            private int mCallBlockReason = 0;
            private long mMissedReason = 0;
            private int mPriority = 0;
            private double mLatitude = Double.NaN;
            private double mLongitude = Double.NaN;

            public AddCallParametersBuilder setCallerInfo(CallerInfo callerInfo) {
                this.mCallerInfo = callerInfo;
                return this;
            }

            public AddCallParametersBuilder setNumber(String number) {
                this.mNumber = number;
                return this;
            }

            public AddCallParametersBuilder setPostDialDigits(String postDialDigits) {
                this.mPostDialDigits = postDialDigits;
                return this;
            }

            public AddCallParametersBuilder setViaNumber(String viaNumber) {
                this.mViaNumber = viaNumber;
                return this;
            }

            public AddCallParametersBuilder setPresentation(int presentation) {
                this.mPresentation = presentation;
                return this;
            }

            public AddCallParametersBuilder setCallType(int callType) {
                this.mCallType = callType;
                return this;
            }

            public AddCallParametersBuilder setFeatures(int features) {
                this.mFeatures = features;
                return this;
            }

            public AddCallParametersBuilder setAccountHandle(PhoneAccountHandle accountHandle) {
                this.mAccountHandle = accountHandle;
                return this;
            }

            public AddCallParametersBuilder setStart(long start) {
                this.mStart = start;
                return this;
            }

            public AddCallParametersBuilder setDuration(int duration) {
                this.mDuration = duration;
                return this;
            }

            public AddCallParametersBuilder setDataUsage(long dataUsage) {
                this.mDataUsage = Long.valueOf(dataUsage);
                return this;
            }

            public AddCallParametersBuilder setAddForAllUsers(boolean addForAllUsers) {
                this.mAddForAllUsers = addForAllUsers;
                return this;
            }

            public AddCallParametersBuilder setUserToBeInsertedTo(UserHandle userToBeInsertedTo) {
                this.mUserToBeInsertedTo = userToBeInsertedTo;
                return this;
            }

            public AddCallParametersBuilder setIsRead(boolean isRead) {
                this.mIsRead = isRead;
                return this;
            }

            public AddCallParametersBuilder setCallBlockReason(int callBlockReason) {
                this.mCallBlockReason = callBlockReason;
                return this;
            }

            public AddCallParametersBuilder setCallScreeningAppName(CharSequence callScreeningAppName) {
                this.mCallScreeningAppName = callScreeningAppName;
                return this;
            }

            public AddCallParametersBuilder setCallScreeningComponentName(String callScreeningComponentName) {
                this.mCallScreeningComponentName = callScreeningComponentName;
                return this;
            }

            public AddCallParametersBuilder setMissedReason(long missedReason) {
                this.mMissedReason = missedReason;
                return this;
            }

            public AddCallParametersBuilder setPriority(int priority) {
                this.mPriority = priority;
                return this;
            }

            public AddCallParametersBuilder setSubject(String subject) {
                this.mSubject = subject;
                return this;
            }

            public AddCallParametersBuilder setLatitude(double latitude) {
                this.mLatitude = latitude;
                return this;
            }

            public AddCallParametersBuilder setLongitude(double longitude) {
                this.mLongitude = longitude;
                return this;
            }

            public AddCallParametersBuilder setPictureUri(Uri pictureUri) {
                this.mPictureUri = pictureUri;
                return this;
            }

            public AddCallParametersBuilder setIsPhoneAccountMigrationPending(int isPhoneAccountMigrationPending) {
                this.mIsPhoneAccountMigrationPending = isPhoneAccountMigrationPending;
                return this;
            }

            public AddCallParametersBuilder setIsBusinessCall(boolean isBusinessCall) {
                this.mIsBusinessCall = isBusinessCall;
                return this;
            }

            public AddCallParametersBuilder setAssertedDisplayName(String assertedDisplayName) {
                if (assertedDisplayName != null && assertedDisplayName.length() > 256) {
                    throw new IllegalArgumentException("assertedDisplayName exceeds the character limit of 256.");
                }
                this.mAssertedDisplayName = assertedDisplayName;
                return this;
            }

            public AddCallParametersBuilder setUuid(String uuid) {
                if (uuid != null && uuid.length() > 256) {
                    throw new IllegalArgumentException("assertedDisplayName exceeds the character limit of 256.");
                }
                try {
                    UUID.fromString(uuid);
                    this.mUuid = uuid;
                } catch (Exception e) {
                    Log.e(CallLogUtils.LOG_TAG, e + "Invalid uuid passed in: " + uuid);
                }
                return this;
            }

            public AddCallParametersBuilder setPreferredDisplayName(String preferredDisplayName) {
                if (preferredDisplayName != null && preferredDisplayName.length() > 256) {
                    throw new IllegalArgumentException("preferredDisplayName exceeds the character limit of 256.");
                }
                this.mPreferredDisplayName = preferredDisplayName;
                return this;
            }

            public AddCallParametersBuilder setVoipContactLookupUri(Uri voipContactLookupUri) {
                this.mVoipContactLookupUri = voipContactLookupUri;
                return this;
            }

            public AddCallParams build() {
                if (Flags.businessCallComposer()) {
                    if (Flags.integratedCallLogs()) {
                        return new AddCallParams(this.mCallerInfo, this.mNumber, this.mPostDialDigits, this.mViaNumber, this.mPresentation, this.mCallType, this.mFeatures, this.mAccountHandle, this.mStart, this.mDuration, this.mDataUsage.longValue(), this.mAddForAllUsers, this.mUserToBeInsertedTo, this.mIsRead, this.mCallBlockReason, this.mCallScreeningAppName, this.mCallScreeningComponentName, this.mMissedReason, this.mPriority, this.mSubject, this.mLatitude, this.mLongitude, this.mPictureUri, this.mIsPhoneAccountMigrationPending, this.mIsBusinessCall, this.mAssertedDisplayName, this.mUuid, this.mPreferredDisplayName, this.mVoipContactLookupUri);
                    }
                    return new AddCallParams(this.mCallerInfo, this.mNumber, this.mPostDialDigits, this.mViaNumber, this.mPresentation, this.mCallType, this.mFeatures, this.mAccountHandle, this.mStart, this.mDuration, this.mDataUsage.longValue(), this.mAddForAllUsers, this.mUserToBeInsertedTo, this.mIsRead, this.mCallBlockReason, this.mCallScreeningAppName, this.mCallScreeningComponentName, this.mMissedReason, this.mPriority, this.mSubject, this.mLatitude, this.mLongitude, this.mPictureUri, this.mIsPhoneAccountMigrationPending, this.mIsBusinessCall, this.mAssertedDisplayName);
                }
                return new AddCallParams(this.mCallerInfo, this.mNumber, this.mPostDialDigits, this.mViaNumber, this.mPresentation, this.mCallType, this.mFeatures, this.mAccountHandle, this.mStart, this.mDuration, this.mDataUsage.longValue(), this.mAddForAllUsers, this.mUserToBeInsertedTo, this.mIsRead, this.mCallBlockReason, this.mCallScreeningAppName, this.mCallScreeningComponentName, this.mMissedReason, this.mPriority, this.mSubject, this.mLatitude, this.mLongitude, this.mPictureUri, this.mIsPhoneAccountMigrationPending);
            }
        }
    }

    private static Uri maybeAddUserId(Uri uri, int userId) {
        if (uri == null) {
            return null;
        }
        if (userId != UserHandle.CURRENT.getIdentifier() && "content".equals(uri.getScheme()) && !uriHasUserId(uri)) {
            Uri.Builder builder = uri.buildUpon();
            builder.encodedAuthority("" + userId + "@" + uri.getEncodedAuthority());
            return builder.build();
        }
        return uri;
    }

    private static boolean uriHasUserId(Uri uri) {
        return uri == null ? VERBOSE_LOG : !TextUtils.isEmpty(uri.getUserInfo());
    }
}
