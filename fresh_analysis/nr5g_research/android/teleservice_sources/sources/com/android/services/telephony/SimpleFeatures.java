package com.android.services.telephony;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.telecom.Connection;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import com.android.internal.telephony.Phone;
import com.android.phone.ModemProvider;
import com.android.phone.ModuleDelegate;
import com.android.phone.R;
import com.android.phone.module.ITelephonyModule;
import com.android.phone.utils.MiuiKoreaOperatorCustimizedUtils;
import com.xiaomi.internal.telephony.DynamicSarService;
import gov.nist.javax.sip.address.GenericURI;
import java.util.Iterator;
import miui.telephony.TelephonyConstants;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleFeatures {
    public static final String[] krSktPrefixes = {"+82", "0082", "+082", "082", "82"};
    public static final String[] jpKdPrefixes = {"+81"};
    public static final String[] krSktTRoamingNumbersToReplace = {"114", "010114", "011114", "017114", "0082114", "+82114"};
    public static final String[] krSktDiplomaticCenterToReplace = {"111", "112", "113", "117", "118", "119", "122", "125"};

    public static TelephonyConstants.MiuiCallExtraAccessor accessMiuiConnectionExtra(final Connection connection) {
        return new TelephonyConstants.MiuiCallExtraAccessor(new TelephonyConstants.MiuiCallExtraAccessor.ExtraContainer() { // from class: com.android.services.telephony.SimpleFeatures.1
            public void setExtras(Bundle bundle) {
                connection.setExtras(TelephonyConstants.copyMiuiCallExtras(bundle, connection.getExtras(), "telephony."));
            }

            public Bundle getExtras() {
                return connection.getExtras();
            }
        });
    }

    public static Uri updateAddresIfCallForwdNumExist(TelephonyConnection telephonyConnection, Uri uri) {
        if (ModemProvider.IS_QCOM) {
            String schemeSpecificPart = uri != null ? uri.getSchemeSpecificPart() : null;
            int iIndexOf = (telephonyConnection.getOriginalConnection() == null || !telephonyConnection.getOriginalConnection().isIncoming() || schemeSpecificPart == null) ? -1 : schemeSpecificPart.indexOf(38);
            if (iIndexOf > 0) {
                uri = Uri.fromParts(GenericURI.TEL, schemeSpecificPart.substring(0, iIndexOf), null);
                if (!accessMiuiConnectionExtra(telephonyConnection).getBoolean("telephony.IS_FORWARDED_CALL", false)) {
                    String strSubstring = schemeSpecificPart.substring(iIndexOf + 1, schemeSpecificPart.length());
                    boolean zIsRoamingCFUCall = telephonyConnection.getPhone() == null ? false : ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isRoamingCFUCall(telephonyConnection.getPhone().getSubId(), strSubstring);
                    accessMiuiConnectionExtra(telephonyConnection).putBoolean("telephony.IS_SMART_FORWARD_CALL", ((ITelephonyModule) ModuleDelegate.get(ITelephonyModule.class)).isSmartCallForwardedCall(strSubstring)).commit();
                    accessMiuiConnectionExtra(telephonyConnection).putBoolean("telephony.IS_ROAMING_FORWARD_CALL", zIsRoamingCFUCall).commit();
                    accessMiuiConnectionExtra(telephonyConnection).putBoolean("telephony.IS_FORWARDED_CALL", true).commit();
                    Log.i("SimpleFeatures", "isCallForwardNumberExist result = ture", new Object[0]);
                }
            }
        }
        return uri;
    }

    public static Uri updateAddress(TelephonyConnection telephonyConnection, Uri uri) {
        Uri uriUpdateAddresIfCallForwdNumExist = updateAddresIfCallForwdNumExist(telephonyConnection, uri);
        return MiuiKoreaOperatorCustimizedUtils.isCustForKrSkt() ? updateAddressForKrSkt(telephonyConnection, uriUpdateAddresIfCallForwdNumExist) : uriUpdateAddresIfCallForwdNumExist;
    }

    private static Uri updateAddressForKrSkt(TelephonyConnection telephonyConnection, Uri uri) {
        Uri uriFromParts;
        Phone phone = telephonyConnection.getPhone();
        if (phone == null || telephonyConnection.getOriginalConnection() == null || !telephonyConnection.getOriginalConnection().isIncoming()) {
            return uri;
        }
        if (shouldReplaceAnnymousNumber(uri != null ? uri.getSchemeSpecificPart() : null)) {
            Log.i("SimpleFeatures", "updateAddressForKrSkt anonymous", new Object[0]);
            uriFromParts = Uri.fromParts(GenericURI.TEL, phone.getContext().getResources().getString(R.string.incall_number_anonymous), null);
        } else {
            uriFromParts = uri;
        }
        return phone.getServiceState().getRoaming() ? replaceNumberPrefix(uri, krSktPrefixes) : uriFromParts;
    }

    public static String updateDisplayNameForJPKD(TelephonyConnection telephonyConnection, Uri uri) {
        Phone phone = telephonyConnection.getPhone();
        String strReplace = "";
        if (phone != null && telephonyConnection.getOriginalConnection() != null && telephonyConnection.getOriginalConnection().isIncoming()) {
            int cnapNamePresentation = telephonyConnection.getOriginalConnection().getCnapNamePresentation();
            String cnapName = telephonyConnection.getOriginalConnection().getCnapName();
            String schemeSpecificPart = uri != null ? uri.getSchemeSpecificPart() : null;
            Log.i("SimpleFeatures", "updateDisplayNameForJPKD,number : " + schemeSpecificPart + " ; namePresentation:" + cnapNamePresentation + " ; name:" + cnapName, new Object[0]);
            if (TextUtils.isEmpty(cnapName) && cnapNamePresentation == 3) {
                if (!TextUtils.isEmpty(schemeSpecificPart)) {
                    for (String str : jpKdPrefixes) {
                        if (schemeSpecificPart.startsWith(str)) {
                            strReplace = schemeSpecificPart.replace(str, "0");
                            Log.i("SimpleFeatures", "replaceNumberPrefix replace prefix:" + str, new Object[0]);
                            break;
                        }
                    }
                }
                Log.i("SimpleFeatures", "updateDisplayNameForJPKD--round 2, displayName:" + strReplace, new Object[0]);
                return strReplace;
            }
            if (!TextUtils.isEmpty(cnapName) && cnapName.startsWith("+81")) {
                Log.i("SimpleFeatures", "updateDisplayNameForJPKD--round 1, displayName:".concat(cnapName), new Object[0]);
                return cnapName;
            }
            if (!TextUtils.isEmpty(cnapName) && cnapName.toLowerCase().contains("anonymous")) {
                String string = phone.getContext().getResources().getString(R.string.incall_number_anonymous_kd);
                Log.i("SimpleFeatures", "updateDisplayNameForJPKD--round 3, displayName:" + string, new Object[0]);
                return string;
            }
            if ((!TextUtils.isEmpty(cnapName) && cnapName.toLowerCase().contains("interaction")) || (!TextUtils.isEmpty(cnapName) && cnapName.toLowerCase().contains("unavailable") && cnapNamePresentation == 1)) {
                String string2 = phone.getContext().getResources().getString(R.string.incall_number_unavailable_kd);
                Log.i("SimpleFeatures", "updateDisplayNameForJPKD--round 5 or 6, displayName:" + string2, new Object[0]);
                return string2;
            }
        }
        return "";
    }

    private static Uri replaceNumberPrefix(Uri uri, String[] strArr) {
        String schemeSpecificPart = uri != null ? uri.getSchemeSpecificPart() : null;
        if (TextUtils.isEmpty(schemeSpecificPart)) {
            return uri;
        }
        for (String str : strArr) {
            if (schemeSpecificPart.startsWith(str)) {
                schemeSpecificPart = schemeSpecificPart.replace(str, "0");
                Log.i("SimpleFeatures", "replaceNumberPrefix replace prefix:" + str, new Object[0]);
                break;
            }
        }
        return Uri.fromParts(GenericURI.TEL, schemeSpecificPart, null);
    }

    public static boolean hasBaseUserRestriction(Context context) {
        return ((UserManager) context.getSystemService("user")).hasBaseUserRestriction("no_data_roaming", UserHandle.CURRENT);
    }

    public static boolean checkDataRoamingRestriction(Context context) {
        UserManager userManager = UserManager.get(context);
        UserHandle userHandleOf = UserHandle.of(userManager.getUserHandle());
        return userManager.hasUserRestriction("no_data_roaming", userHandleOf) && !userManager.hasBaseUserRestriction("no_data_roaming", userHandleOf);
    }

    public static void ShowAdminSupportDetailsIntent(Context context) {
        Intent intentCreateAdminSupportIntent;
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        if (devicePolicyManager == null || (intentCreateAdminSupportIntent = devicePolicyManager.createAdminSupportIntent("no_data_roaming")) == null) {
            return;
        }
        context.startActivity(intentCreateAdminSupportIntent);
    }

    public static boolean isHideVoicemailSettings(Context context) {
        String defaultDialerPackage = ((TelecomManager) context.getSystemService(TelecomManager.class)).getDefaultDialerPackage();
        if (defaultDialerPackage == null) {
            return false;
        }
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(defaultDialerPackage, DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY).metaData;
            if (bundle != null && bundle.getBoolean("android.telephony.HIDE_VOICEMAIL_SETTINGS_MENU", false)) {
                Log.d("SimpleFeatures", "isHideVoicemailSettings(): disabled by default dialer", new Object[0]);
                return true;
            }
            Log.d("SimpleFeatures", "isHideVoicemailSettings(): not disabled by default dialer", new Object[0]);
            return false;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d("SimpleFeatures", "isHideVoicemailSettings(): not controlled by default dialer", new Object[0]);
            return false;
        }
    }

    public static boolean isHidePhoneAccountSettings(Context context) {
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            TelecomManager telecomManagerFrom = TelecomManager.from(context);
            Iterator it = telecomManagerFrom.getCallCapablePhoneAccounts(true).iterator();
            while (it.hasNext()) {
                if (!telecomManagerFrom.getPhoneAccount((PhoneAccountHandle) it.next()).hasCapabilities(4)) {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.i("SimpleFeatures", "isHidePhoneAccountSettings exception= " + e, new Object[0]);
        } finally {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
        }
        return true;
    }

    private static boolean shouldReplaceAnnymousNumber(String str) {
        return str != null && MiuiKoreaOperatorCustimizedUtils.isCustForKrSkt() && str.contains("anonymous");
    }
}
