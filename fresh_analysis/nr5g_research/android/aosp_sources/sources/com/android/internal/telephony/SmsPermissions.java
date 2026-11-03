package com.android.internal.telephony;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.telephony.Rlog;

/* JADX INFO: loaded from: classes.dex */
public class SmsPermissions {
    private final AppOpsManager mAppOps;
    private final Context mContext;
    private final Phone mPhone;

    public SmsPermissions(Phone phone, Context context, AppOpsManager appOpsManager) {
        this.mPhone = phone;
        this.mContext = context;
        this.mAppOps = appOpsManager;
    }

    public boolean checkCallingCanSendText(boolean z, String str, String str2, String str3) {
        if (!z) {
            try {
                enforceCallerIsImsAppOrCarrierApp(str3);
                return true;
            } catch (SecurityException unused) {
                this.mContext.enforceCallingPermission("android.permission.MODIFY_PHONE_STATE", str3);
            }
        }
        return checkCallingCanSendSms(str, str2, str3);
    }

    public void enforceCallerIsImsAppOrCarrierApp(String str) {
        String imsRcsPackageForIntent = CarrierSmsUtils.getImsRcsPackageForIntent(this.mContext, this.mPhone, new Intent("android.service.carrier.CarrierMessagingService"));
        if (imsRcsPackageForIntent == null || !packageNameMatchesCallingUid(imsRcsPackageForIntent)) {
            TelephonyPermissions.enforceCallingOrSelfCarrierPrivilege(this.mContext, this.mPhone.getSubId(), str);
        }
    }

    public boolean checkCallingCanSendSms(String str, String str2, String str3) {
        this.mContext.enforceCallingPermission("android.permission.SEND_SMS", str3);
        return this.mAppOps.noteOp("android:send_sms", Binder.getCallingUid(), str, str2, null) == 0;
    }

    public boolean checkCallingOrSelfCanSendSms(String str, String str2, String str3) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.SEND_SMS", str3);
        return this.mAppOps.noteOp("android:send_sms", Binder.getCallingUid(), str, str2, null) == 0;
    }

    public boolean checkCallingOrSelfCanGetSmscAddress(String str, String str2) {
        int callingUid = Binder.getCallingUid();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            boolean zIsCallerDefaultSmsPackage = isCallerDefaultSmsPackage(str, callingUid);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            if (zIsCallerDefaultSmsPackage) {
                return true;
            }
            Rlog.d("SmsPermissions", "Caller is not a default SMS application");
            TelephonyPermissions.enforceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(this.mContext, this.mPhone.getSubId(), str2);
            return true;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean checkCallingOrSelfCanSetSmscAddress(String str, String str2) {
        int callingUid = Binder.getCallingUid();
        long jClearCallingIdentity = Binder.clearCallingIdentity();
        try {
            boolean zIsCallerDefaultSmsPackage = isCallerDefaultSmsPackage(str, callingUid);
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            if (zIsCallerDefaultSmsPackage) {
                return true;
            }
            Rlog.d("SmsPermissions", "Caller is not a default SMS application");
            TelephonyPermissions.enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(this.mContext, this.mPhone.getSubId(), str2);
            return true;
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(jClearCallingIdentity);
            throw th;
        }
    }

    public boolean isCallerDefaultSmsPackage(String str, int i) {
        if (!packageNameMatchesCallingUid(str, i)) {
            return false;
        }
        return SmsApplication.isDefaultSmsApplicationAsUser(this.mContext, str, TelephonyUtils.getSubscriptionUserHandle(this.mContext, this.mPhone.getSubId()));
    }

    public boolean packageNameMatchesCallingUid(String str) {
        return packageNameMatchesCallingUid(str, Binder.getCallingUid());
    }

    public boolean packageNameMatchesCallingUid(String str, int i) {
        try {
            ((AppOpsManager) this.mContext.getSystemService("appops")).checkPackage(i, str);
            return true;
        } catch (SecurityException unused) {
            return false;
        }
    }

    protected void log(String str) {
        Rlog.d("SmsPermissions", str);
    }

    protected void loge(String str) {
        Rlog.e("SmsPermissions", str);
    }

    protected void loge(String str, Throwable th) {
        Rlog.e("SmsPermissions", str, th);
    }
}
