package com.android.phone.utils;

import android.net.Uri;
import android.provider.Settings;
import android.telecom.Connection;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import com.android.internal.telephony.Phone;
import com.android.phone.MiuiImsPhoneUtils;
import com.android.phone.PhoneGlobals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import miui.telephony.TelephonyUtils;

/* JADX INFO: loaded from: classes.dex */
public class ImsConferenceHandler {
    private static volatile ImsConferenceHandler mConfHdler;
    private static final String[] unusualParticipants = {"88888888", "anonymous"};
    private static final ArrayList mRetryPairedNumber = new ArrayList();
    private final HashMap mNeedReplaceAddrs = new HashMap();
    private LinkedHashMap mParticipantsLocalCache = new LinkedHashMap();
    private final LinkedHashMap mAddrOccurrence = new LinkedHashMap();
    private int mOrigAddrCount = 0;
    private ArrayList mUnPairedParticipantsAddr = new ArrayList();
    private ArrayList mPairedParticipantsAddr = new ArrayList();
    private int mMinMatch = 0;

    public static ImsConferenceHandler getInstance() {
        if (mConfHdler == null) {
            synchronized (ImsConferenceHandler.class) {
                try {
                    if (mConfHdler == null) {
                        mConfHdler = new ImsConferenceHandler();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mConfHdler;
    }

    public void addParticipantsLocalCache(Connection connection, Phone phone) {
        if (connection == null || connection.getAddress() == null || phone == null) {
            return;
        }
        String userNameFromSipTelUriString = getUserNameFromSipTelUriString(connection.getAddress());
        int addressPresentation = connection.getAddressPresentation();
        if ((connection.getConnectionProperties() & 4096) == 0) {
            this.mOrigAddrCount++;
        }
        if (!MiuiImsPhoneUtils.shouldRestoreParticipantsPresentation(phone) || "".equals(userNameFromSipTelUriString) || "anonymous".equals(userNameFromSipTelUriString)) {
            return;
        }
        updateMinMatch(userNameFromSipTelUriString);
        this.mParticipantsLocalCache.put(userNameFromSipTelUriString, Integer.valueOf(addressPresentation));
        Integer num = (Integer) this.mAddrOccurrence.get(userNameFromSipTelUriString);
        this.mAddrOccurrence.put(userNameFromSipTelUriString, Integer.valueOf(num != null ? 1 + num.intValue() : 1));
        Log.d("ImsConferenceHandler", "addParticipantsLocalCache address : " + TelephonyUtils.pii(userNameFromSipTelUriString) + ", presentation: " + addressPresentation);
    }

    public void removeParticipantsNum(Uri uri) {
        String userNameFromSipTelUriString = getUserNameFromSipTelUriString(uri);
        Iterator it = this.mParticipantsLocalCache.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (compareNumberLoosely(str, userNameFromSipTelUriString)) {
                Integer num = (Integer) this.mAddrOccurrence.get(str);
                if (num != null && num.intValue() > 1) {
                    this.mAddrOccurrence.put(str, Integer.valueOf(num.intValue() - 1));
                } else {
                    this.mAddrOccurrence.remove(str);
                    it.remove();
                }
            }
        }
        Log.d("ImsConferenceHandler", "The address of the removed member： " + TelephonyUtils.pii(userNameFromSipTelUriString));
    }

    public void clearLocalCache() {
        Log.d("ImsConferenceHandler", "clear.");
        this.mParticipantsLocalCache.clear();
        this.mAddrOccurrence.clear();
        this.mOrigAddrCount = 0;
        this.mUnPairedParticipantsAddr.clear();
        this.mPairedParticipantsAddr.clear();
        this.mNeedReplaceAddrs.clear();
        this.mMinMatch = 0;
        clearRetryNumber();
    }

    private void clearRetryNumber() {
        mRetryPairedNumber.clear();
        Settings.Global.putString(PhoneGlobals.getInstance().getContentResolver(), "retry_remove_conf_number", "");
    }

    private boolean compareNumberLoosely(String str, String str2) {
        if (str == null || str2 == null) {
            return str == str2;
        }
        if (str.length() == 0 || str2.length() == 0) {
            return false;
        }
        for (String str3 : unusualParticipants) {
            if (!str.contains(str3) && str2.contains(str3)) {
                return false;
            }
        }
        String strReplaceAll = str.replaceAll("[+]", "");
        String strReplaceAll2 = str2.replaceAll("[+]", "");
        int length = strReplaceAll.length();
        int length2 = strReplaceAll2.length();
        int iMin = Math.min(length, length2);
        int i = this.mMinMatch;
        if (i != 0) {
            iMin = Math.min(iMin, i);
        }
        return PhoneNumberUtils.compareLoosely(strReplaceAll.substring(length - iMin), strReplaceAll2.substring(length2 - iMin));
    }

    private String getUserNameFromSipTelUriString(Uri uri) {
        String schemeSpecificPart;
        String usernameFromUriNumber;
        if (uri == null || (schemeSpecificPart = uri.getSchemeSpecificPart()) == null || (usernameFromUriNumber = PhoneNumberUtils.getUsernameFromUriNumber(schemeSpecificPart)) == null) {
            return null;
        }
        int iIndexOf = usernameFromUriNumber.indexOf(59);
        int iIndexOf2 = usernameFromUriNumber.indexOf(44);
        if (iIndexOf >= 0 && iIndexOf2 >= 0) {
            return usernameFromUriNumber.substring(0, Math.min(iIndexOf, iIndexOf2));
        }
        if (iIndexOf >= 0) {
            return usernameFromUriNumber.substring(0, iIndexOf);
        }
        return iIndexOf2 >= 0 ? usernameFromUriNumber.substring(0, iIndexOf2) : usernameFromUriNumber;
    }

    private void updateMinMatch(String str) {
        Iterator it = this.mParticipantsLocalCache.entrySet().iterator();
        while (it.hasNext()) {
            String str2 = (String) ((Map.Entry) it.next()).getKey();
            int iMin = Math.min(str.length(), str2.length());
            int i = 1;
            while (i <= iMin && str.charAt(str.length() - i) == str2.charAt(str2.length() - i)) {
                i++;
            }
            this.mMinMatch = Math.max(i, this.mMinMatch);
        }
        Log.d("ImsConferenceHandler", "min match to compare participant address: " + this.mMinMatch);
    }
}
