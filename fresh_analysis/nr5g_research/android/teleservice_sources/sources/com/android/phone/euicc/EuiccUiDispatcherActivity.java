package com.android.phone.euicc;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.UserHandle;
import android.permission.LegacyPermissionManager;
import android.telephony.TelephonyManager;
import android.telephony.euicc.EuiccManager;
import android.util.Log;
import com.android.internal.telephony.euicc.EuiccConnector;
import com.android.internal.telephony.util.TelephonyUtils;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.utils.WizardManagerHelper;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public class EuiccUiDispatcherActivity extends Activity {
    private ThreadPoolExecutor mExecutor;
    private boolean mGrantPermissionDone = false;
    private LegacyPermissionManager mPermissionManager;

    protected void onDispatchFailure() {
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPermissionManager = (LegacyPermissionManager) getSystemService("legacy_permission");
        this.mExecutor = new ThreadPoolExecutor(1, 1, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new ThreadFactory(this) { // from class: com.android.phone.euicc.EuiccUiDispatcherActivity.1
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable, "EuiccService #" + this.mCount.getAndIncrement());
            }
        });
        try {
            if ("android.telephony.euicc.action.PROVISION_EMBEDDED_SUBSCRIPTION".equals(getIntent().getAction()) && MiuiPhoneUtils.shouldSkipGlpa()) {
                Log.w("EuiccUiDispatcher", "should skip glpa during setup wizard, just return.");
                WizardManagerHelper.goToNextPage(this, getIntent(), -1);
                setResult(0);
                return;
            }
            Intent intentResolveEuiccUiIntent = resolveEuiccUiIntent();
            if (intentResolveEuiccUiIntent == null) {
                setResult(0);
                onDispatchFailure();
            } else {
                intentResolveEuiccUiIntent.setFlags(33554432);
                startActivityAsUser(intentResolveEuiccUiIntent, UserHandle.CURRENT);
            }
        } finally {
            finish();
        }
    }

    Intent resolveEuiccUiIntent() {
        EuiccManager euiccManager = (EuiccManager) getSystemService("euicc");
        if (euiccManager == null || !euiccManager.isEnabled()) {
            Log.w("EuiccUiDispatcher", "eUICC not enabled");
            return null;
        }
        Intent euiccUiIntent = getEuiccUiIntent();
        if (euiccUiIntent == null) {
            Log.w("EuiccUiDispatcher", "Unable to handle intent");
            return null;
        }
        ComponentName testEuiccUiComponent = ((TelephonyManager) getSystemService("phone")).getTestEuiccUiComponent();
        if (testEuiccUiComponent != null) {
            Log.i("EuiccUiDispatcher", "Test mode: " + testEuiccUiComponent);
            euiccUiIntent.setComponent(testEuiccUiComponent);
            return euiccUiIntent;
        }
        revokePermissionFromLuiApps(euiccUiIntent);
        ActivityInfo activityInfoFindBestActivity = findBestActivity(euiccUiIntent);
        if (activityInfoFindBestActivity == null) {
            Log.w("EuiccUiDispatcher", "Could not resolve activity for intent: " + euiccUiIntent);
            return null;
        }
        grantDefaultPermissionsToLuiApp(activityInfoFindBestActivity);
        euiccUiIntent.setComponent(new ComponentName(activityInfoFindBestActivity.packageName, activityInfoFindBestActivity.name));
        return euiccUiIntent;
    }

    protected Intent getEuiccUiIntent() {
        String action = getIntent().getAction();
        if (action == null) {
            Log.w("EuiccUiDispatcher", "No action is specified in the intent");
            return null;
        }
        Intent intent = new Intent();
        intent.putExtras(getIntent());
        action.hashCode();
        switch (action) {
            case "android.telephony.euicc.action.CONVERT_TO_EMBEDDED_SUBSCRIPTION":
                intent.setAction("android.service.euicc.action.CONVERT_TO_EMBEDDED_SUBSCRIPTION");
                return intent;
            case "android.telephony.euicc.action.MANAGE_EMBEDDED_SUBSCRIPTIONS":
                intent.setAction("android.service.euicc.action.MANAGE_EMBEDDED_SUBSCRIPTIONS");
                return intent;
            case "android.telephony.euicc.action.PROVISION_EMBEDDED_SUBSCRIPTION":
                intent.setAction("android.service.euicc.action.PROVISION_EMBEDDED_SUBSCRIPTION");
                return intent;
            case "android.telephony.euicc.action.TRANSFER_EMBEDDED_SUBSCRIPTIONS":
                intent.setAction("android.service.euicc.action.TRANSFER_EMBEDDED_SUBSCRIPTIONS");
                return intent;
            default:
                Log.w("EuiccUiDispatcher", "Unsupported action: ".concat(action));
                return null;
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.mExecutor.shutdownNow();
        super.onDestroy();
    }

    ActivityInfo findBestActivity(Intent intent) {
        return EuiccConnector.findBestActivity(getPackageManager(), intent);
    }

    protected void grantDefaultPermissionsToLuiApp(ActivityInfo activityInfo) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            this.mPermissionManager.grantDefaultPermissionsToLuiApp(activityInfo.packageName, UserHandle.of(UserHandle.myUserId()), this.mExecutor, new Consumer() { // from class: com.android.phone.euicc.EuiccUiDispatcherActivity$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    EuiccUiDispatcherActivity.$r8$lambda$XEO53T56OIRh8460j3C0RzZOhdQ(countDownLatch, (Boolean) obj);
                }
            });
            TelephonyUtils.waitUntilReady(countDownLatch, 15000L);
        } catch (RuntimeException e) {
            Log.e("EuiccUiDispatcher", "Failed to grant permissions to active LUI app.", e);
        }
    }

    public static /* synthetic */ void $r8$lambda$XEO53T56OIRh8460j3C0RzZOhdQ(CountDownLatch countDownLatch, Boolean bool) {
        if (bool.booleanValue()) {
            countDownLatch.countDown();
        } else {
            Log.e("EuiccUiDispatcher", "Failed to revoke LUI app permissions.");
        }
    }

    protected void revokePermissionFromLuiApps(Intent intent) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            Set allLuiAppPackageNames = getAllLuiAppPackageNames(intent);
            this.mPermissionManager.revokeDefaultPermissionsFromLuiApps((String[]) allLuiAppPackageNames.toArray(new String[allLuiAppPackageNames.size()]), UserHandle.of(UserHandle.myUserId()), this.mExecutor, new Consumer() { // from class: com.android.phone.euicc.EuiccUiDispatcherActivity$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    EuiccUiDispatcherActivity.m2504$r8$lambda$b0ADx6tKdBOUTmY9XwYKdjHl1g(countDownLatch, (Boolean) obj);
                }
            });
            TelephonyUtils.waitUntilReady(countDownLatch, 15000L);
        } catch (RuntimeException e) {
            Log.e("EuiccUiDispatcher", "Failed to revoke LUI app permissions.");
            throw e;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$b0ADx6tKdBOUTm-Y9XwYKdjHl1g, reason: not valid java name */
    public static /* synthetic */ void m2504$r8$lambda$b0ADx6tKdBOUTmY9XwYKdjHl1g(CountDownLatch countDownLatch, Boolean bool) {
        if (bool.booleanValue()) {
            countDownLatch.countDown();
        } else {
            Log.e("EuiccUiDispatcher", "Failed to revoke LUI app permissions.");
        }
    }

    private Set getAllLuiAppPackageNames(Intent intent) {
        List<ResolveInfo> listQueryIntentServices = getPackageManager().queryIntentServices(intent, 269484096);
        HashSet hashSet = new HashSet();
        Iterator<ResolveInfo> it = listQueryIntentServices.iterator();
        while (it.hasNext()) {
            ServiceInfo serviceInfo = it.next().serviceInfo;
            if (serviceInfo != null) {
                hashSet.add(serviceInfo.packageName);
            }
        }
        return hashSet;
    }
}
