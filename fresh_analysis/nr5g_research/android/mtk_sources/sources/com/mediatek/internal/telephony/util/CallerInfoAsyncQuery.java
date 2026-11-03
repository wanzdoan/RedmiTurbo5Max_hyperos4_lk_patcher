package com.mediatek.internal.telephony.util;

import android.app.ActivityManager;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class CallerInfoAsyncQuery {
    private static final boolean DBG = false;
    private static final int EVENT_ADD_LISTENER = 2;
    private static final int EVENT_EMERGENCY_NUMBER = 4;
    private static final int EVENT_END_OF_QUEUE = 3;
    private static final int EVENT_NEW_QUERY = 1;
    private static final int EVENT_VOICEMAIL_NUMBER = 5;
    private static final String LOG_TAG = "CallerInfoAsyncQuery";
    private CallerInfoAsyncQueryHandler mHandler;

    public interface OnQueryCompleteListener {
        void onQueryComplete(int i, Object obj, CallerInfo callerInfo);
    }

    private CallerInfoAsyncQuery() {
    }

    static ContentResolver getCurrentProfileContentResolver(Context context) {
        int currentUser = ActivityManager.getCurrentUser();
        int myUser = UserHandle.myUserId();
        if (myUser != currentUser) {
            try {
                Context otherContext = context.createPackageContextAsUser(context.getPackageName(), 0, UserHandle.of(currentUser));
                return otherContext.getContentResolver();
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(LOG_TAG, "Can't find self package");
            }
        }
        return context.getContentResolver();
    }

    public static CallerInfoAsyncQuery startQuery(int token, Context context, Uri contactRef, OnQueryCompleteListener listener, Object cookie) {
        CallerInfoAsyncQuery c = new CallerInfoAsyncQuery();
        c.allocate(context, contactRef);
        CookieWrapper cw = new CookieWrapper();
        cw.listener = listener;
        cw.cookie = cookie;
        cw.event = 1;
        c.mHandler.startQuery(token, cw, contactRef, null, null, null, null);
        return c;
    }

    public static CallerInfoAsyncQuery startQuery(int token, Context context, String number, OnQueryCompleteListener listener, Object cookie) {
        int subId = SubscriptionManager.getDefaultSubscriptionId();
        return startQuery(token, context, number, listener, cookie, subId);
    }

    public static CallerInfoAsyncQuery startQuery(int token, Context context, String number, OnQueryCompleteListener listener, Object cookie, int subId) {
        boolean isEmergencyNumber;
        boolean isVoicemailNumber;
        Uri contactRef = ContactsContract.PhoneLookup.ENTERPRISE_CONTENT_FILTER_URI.buildUpon().appendPath(number).appendQueryParameter("sip", String.valueOf(PhoneNumberUtils.isUriNumber(number))).build();
        CallerInfoAsyncQuery c = new CallerInfoAsyncQuery();
        c.allocate(context, contactRef);
        CookieWrapper cw = new CookieWrapper();
        cw.listener = listener;
        cw.cookie = cookie;
        cw.number = number;
        cw.subId = subId;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        try {
            boolean isEmergencyNumber2 = tm.isEmergencyNumber(number);
            isEmergencyNumber = isEmergencyNumber2;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while invoking TelephonyManager#isEmergencyNumber. Defaulting to PhoneNumberUtils API instead.");
            boolean isEmergencyNumber3 = PhoneNumberUtils.isLocalEmergencyNumber(context, number);
            isEmergencyNumber = isEmergencyNumber3;
        }
        try {
            isVoicemailNumber = PhoneNumberUtils.isVoiceMailNumber(context, subId, number);
        } catch (UnsupportedOperationException e2) {
            isVoicemailNumber = false;
        }
        if (isEmergencyNumber) {
            cw.event = 4;
        } else if (isVoicemailNumber) {
            cw.event = 5;
        } else {
            cw.event = 1;
        }
        c.mHandler.startQuery(token, cw, contactRef, null, null, null, null);
        return c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String sanitizeUriToString(Uri uri) {
        if (uri != null) {
            String uriString = uri.toString();
            int indexOfLastSlash = uriString.lastIndexOf(47);
            if (indexOfLastSlash > 0) {
                return uriString.substring(0, indexOfLastSlash) + "/xxxxxxx";
            }
            return uriString;
        }
        return "";
    }

    public void addQueryListener(int token, OnQueryCompleteListener listener, Object cookie) {
        CookieWrapper cw = new CookieWrapper();
        cw.listener = listener;
        cw.cookie = cookie;
        cw.event = 2;
        this.mHandler.startQuery(token, cw, null, null, null, null, null);
    }

    private void allocate(Context context, Uri contactRef) {
        if (context == null || contactRef == null) {
            throw new QueryPoolException("Bad context or query uri.");
        }
        this.mHandler = new CallerInfoAsyncQueryHandler(context);
        this.mHandler.mQueryUri = contactRef;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void release() {
        this.mHandler.mContext = null;
        this.mHandler.mQueryUri = null;
        this.mHandler.mCallerInfo = null;
        this.mHandler = null;
    }

    private static final class CookieWrapper {
        public Object cookie;
        public int event;
        public OnQueryCompleteListener listener;
        public String number;
        public int subId;

        private CookieWrapper() {
        }
    }

    public static class QueryPoolException extends SQLException {
        public QueryPoolException(String error) {
            super(error);
        }
    }

    private class CallerInfoAsyncQueryHandler extends AsyncQueryHandler {
        private CallerInfo mCallerInfo;
        private Context mContext;
        private final List<Runnable> mPendingListenerCallbacks;
        private Uri mQueryUri;

        private CallerInfoAsyncQueryHandler(Context context) {
            super(CallerInfoAsyncQuery.getCurrentProfileContentResolver(context));
            this.mPendingListenerCallbacks = new ArrayList();
            this.mContext = context;
        }

        @Override // android.content.AsyncQueryHandler
        protected Handler createHandler(Looper looper) {
            return new CallerInfoWorkerHandler(this, looper);
        }

        @Override // android.content.AsyncQueryHandler
        protected void onQueryComplete(final int token, Object cookie, Cursor cursor) {
            Log.d(CallerInfoAsyncQuery.LOG_TAG, "##### onQueryComplete() #####   query complete for token: " + token);
            final CookieWrapper cw = (CookieWrapper) cookie;
            if (cw == null) {
                Log.i(CallerInfoAsyncQuery.LOG_TAG, "Cookie is null, ignoring onQueryComplete() request.");
                if (cursor != null) {
                    cursor.close();
                    return;
                }
                return;
            }
            if (cw.event == 3) {
                for (Runnable r : this.mPendingListenerCallbacks) {
                    r.run();
                }
                this.mPendingListenerCallbacks.clear();
                CallerInfoAsyncQuery.this.release();
                if (cursor != null) {
                    cursor.close();
                    return;
                }
                return;
            }
            if (this.mCallerInfo == null) {
                if (this.mContext == null || this.mQueryUri == null) {
                    throw new QueryPoolException("Bad context or query uri, or CallerInfoAsyncQuery already released.");
                }
                if (cw.event == 4) {
                    this.mCallerInfo = new CallerInfo().markAsEmergency(this.mContext);
                } else if (cw.event == 5) {
                    this.mCallerInfo = new CallerInfo().markAsVoiceMail(this.mContext, cw.subId);
                } else {
                    this.mCallerInfo = CallerInfo.getCallerInfo(this.mContext, this.mQueryUri, cursor);
                    CallerInfo newCallerInfo = CallerInfo.doSecondaryLookupIfNecessary(this.mContext, cw.number, this.mCallerInfo);
                    if (newCallerInfo != this.mCallerInfo) {
                        this.mCallerInfo = newCallerInfo;
                    }
                    if (!TextUtils.isEmpty(cw.number)) {
                        this.mCallerInfo.setPhoneNumber(PhoneNumberUtils.formatNumber(cw.number, this.mCallerInfo.normalizedNumber, CallerInfo.getCurrentCountryIso(this.mContext)));
                    }
                }
                CookieWrapper endMarker = new CookieWrapper();
                endMarker.event = 3;
                startQuery(token, endMarker, null, null, null, null, null);
            }
            if (cw.listener != null) {
                this.mPendingListenerCallbacks.add(new Runnable() { // from class: com.mediatek.internal.telephony.util.CallerInfoAsyncQuery.CallerInfoAsyncQueryHandler.1
                    @Override // java.lang.Runnable
                    public void run() {
                        cw.listener.onQueryComplete(token, cw.cookie, CallerInfoAsyncQueryHandler.this.mCallerInfo);
                    }
                });
            } else {
                Log.w(CallerInfoAsyncQuery.LOG_TAG, "There is no listener to notify for this query.");
            }
            if (cursor != null) {
                cursor.close();
            }
        }

        protected class CallerInfoWorkerHandler extends AsyncQueryHandler.WorkerHandler {
            public CallerInfoWorkerHandler(CallerInfoAsyncQueryHandler this$1, Looper looper) {
                super(this$1, looper);
            }

            @Override // android.content.AsyncQueryHandler.WorkerHandler, android.os.Handler
            public void handleMessage(Message msg) {
                AsyncQueryHandler.WorkerArgs args = (AsyncQueryHandler.WorkerArgs) msg.obj;
                CookieWrapper cw = (CookieWrapper) args.cookie;
                if (cw == null) {
                    Log.i(CallerInfoAsyncQuery.LOG_TAG, "Unexpected command (CookieWrapper is null): " + msg.what + " ignored by CallerInfoWorkerHandler, passing onto parent.");
                    super.handleMessage(msg);
                    return;
                }
                Log.d(CallerInfoAsyncQuery.LOG_TAG, "Processing event: " + cw.event + " token (arg1): " + msg.arg1 + " command: " + msg.what + " query URI: " + CallerInfoAsyncQuery.sanitizeUriToString(args.uri));
                switch (cw.event) {
                    case 1:
                        super.handleMessage(msg);
                        break;
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        Message reply = args.handler.obtainMessage(msg.what);
                        reply.obj = args;
                        reply.arg1 = msg.arg1;
                        reply.sendToTarget();
                        break;
                }
            }
        }
    }
}
