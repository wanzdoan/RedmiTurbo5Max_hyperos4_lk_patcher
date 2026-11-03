package miuix.appcompat.app.floatingactivity.multiapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import miuix.appcompat.R$array;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.FloatingActivitySwitcher;
import miuix.appcompat.app.floatingactivity.FloatingAnimHelper;
import miuix.appcompat.app.floatingactivity.MemoryFileUtil;
import miuix.appcompat.app.floatingactivity.OnFloatingCallback;
import miuix.appcompat.app.floatingactivity.SnapShotViewHelper;
import miuix.appcompat.app.floatingactivity.helper.FloatingHelperFactory;

/* JADX INFO: loaded from: classes2.dex */
public final class MultiAppFloatingActivitySwitcher {
    private static String[] mAllowedPackageList;
    private static MultiAppFloatingActivitySwitcher sInstance;
    private long mCloseAllActivityTime;
    private IFloatingService mIFloatingService;
    private WeakReference mLastActivityPanel;
    private long mOnDragEndTime;
    private long mOnDragStartTime;
    private boolean mServiceConnected;
    private final Handler mExitAnimationHandler = new Handler(Looper.getMainLooper());
    private final SparseArray mActivityCache = new SparseArray();
    private boolean mEnableDragToDismiss = true;
    private final ServiceConnection mServiceConnection = new ServiceConnection() { // from class: miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher.1
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("MFloatingSwitcher", "onServiceConnected");
            if (MultiAppFloatingActivitySwitcher.sInstance != null) {
                MultiAppFloatingActivitySwitcher.sInstance.setIFloatingService(IFloatingService.Stub.asInterface(iBinder));
                MultiAppFloatingActivitySwitcher.this.checkRegister();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("MFloatingSwitcher", "onServiceDisconnected");
            if (MultiAppFloatingActivitySwitcher.sInstance != null) {
                MultiAppFloatingActivitySwitcher.sInstance.unRegisterAll();
                MultiAppFloatingActivitySwitcher.this.clear();
                MultiAppFloatingActivitySwitcher.this.destroy();
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public void checkRegister() {
        for (int i = 0; i < this.mActivityCache.size(); i++) {
            ArrayList arrayList = (ArrayList) this.mActivityCache.valueAt(i);
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                ActivitySpec activitySpec = (ActivitySpec) obj;
                if (!activitySpec.register) {
                    invokeRegister(activitySpec);
                    checkBg(activitySpec.taskId, activitySpec.identity);
                }
            }
        }
    }

    void checkBg(int i, String str) {
        ActivitySpec activitySpec;
        AppCompatActivity appCompatActivity;
        ArrayList arrayList = (ArrayList) this.mActivityCache.get(i);
        if (((arrayList == null || arrayList.size() <= 1) && getServicePageCount(i) <= 1) || (activitySpec = getActivitySpec(i, str)) == null || activitySpec.serviceNotifyIndex <= 0 || (appCompatActivity = activitySpec.activity) == null) {
            return;
        }
        appCompatActivity.hideFloatingDimBackground();
    }

    void updateResumeState(int i, String str, boolean z) {
        ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec != null) {
            activitySpec.resumed = z;
        }
    }

    private boolean isCalled(long j) {
        return System.currentTimeMillis() - j <= 100;
    }

    private MultiAppFloatingActivitySwitcher() {
    }

    static MultiAppFloatingActivitySwitcher getInstance() {
        return sInstance;
    }

    public static boolean isFromMultiApp(Intent intent) {
        return (TextUtils.isEmpty(intent.getStringExtra("floating_service_pkg")) || TextUtils.isEmpty(intent.getStringExtra("floating_service_path"))) ? false : true;
    }

    public static void install(AppCompatActivity appCompatActivity, Intent intent, Bundle bundle) {
        if (!isFromMultiApp(intent)) {
            FloatingActivitySwitcher.install(appCompatActivity, bundle);
            return;
        }
        if (sInstance == null) {
            sInstance = new MultiAppFloatingActivitySwitcher();
            if (mAllowedPackageList == null) {
                mAllowedPackageList = appCompatActivity.getResources().getStringArray(R$array.multi_floating_package_allow_list);
            }
            sInstance.bindService(appCompatActivity, intent);
        }
        sInstance.init(appCompatActivity, intent, bundle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setIFloatingService(IFloatingService iFloatingService) {
        this.mIFloatingService = iFloatingService;
        this.mServiceConnected = true;
    }

    private void init(AppCompatActivity appCompatActivity, Intent intent, Bundle bundle) {
        if (FloatingHelperFactory.getFloatingHelperType(appCompatActivity) == 0) {
            return;
        }
        stashActivity(appCompatActivity, intent, bundle);
        registerActivityToService(appCompatActivity);
        appCompatActivity.getLifecycle().addObserver(new MultiAppFloatingLifecycleObserver(appCompatActivity));
        appCompatActivity.setEnableSwipToDismiss(this.mEnableDragToDismiss);
        appCompatActivity.setOnFloatingCallback(new DefineOnFloatingActivityCallback(appCompatActivity));
    }

    public static void onSaveInstanceState(int i, String str, Bundle bundle) {
        ActivitySpec activitySpec;
        MultiAppFloatingActivitySwitcher multiAppFloatingActivitySwitcher = getInstance();
        if (multiAppFloatingActivitySwitcher == null || (activitySpec = multiAppFloatingActivitySwitcher.getActivitySpec(i, str)) == null) {
            return;
        }
        bundle.putParcelable("floating_switcher_saved_key", activitySpec);
    }

    private void bindService(Context context, Intent intent) {
        Intent intent2 = new Intent();
        String stringExtra = intent.getStringExtra("floating_service_pkg");
        if (isPackageAllowed(stringExtra)) {
            intent2.setPackage(stringExtra);
            String stringExtra2 = intent.getStringExtra("floating_service_path");
            if (TextUtils.isEmpty(stringExtra2)) {
                return;
            }
            intent2.setComponent(new ComponentName(stringExtra, stringExtra2));
            context.getApplicationContext().bindService(intent2, this.mServiceConnection, 1);
        }
    }

    private boolean isPackageAllowed(String str) {
        for (String str2 : mAllowedPackageList) {
            if (str2.equals(str)) {
                return true;
            }
        }
        Log.w("MFloatingSwitcher", "Package is not allowed:" + str + ". Please contact the MIUIX developer!");
        return false;
    }

    private void registerActivityToService(AppCompatActivity appCompatActivity) {
        ActivitySpec activitySpec = getActivitySpec(appCompatActivity.getTaskId(), appCompatActivity.getActivityIdentity());
        if (activitySpec != null && activitySpec.serviceNotify == null) {
            activitySpec.serviceNotify = new ServiceNotify(appCompatActivity);
        } else if (activitySpec != null) {
            activitySpec.serviceNotify.resetAppCompatActivity(appCompatActivity);
        }
        invokeRegister(activitySpec);
    }

    private void invokeRegister(ActivitySpec activitySpec) {
        IFloatingService iFloatingService;
        if (activitySpec == null || (iFloatingService = this.mIFloatingService) == null) {
            return;
        }
        try {
            ServiceNotify serviceNotify = activitySpec.serviceNotify;
            iFloatingService.registerServiceNotify(serviceNotify, getIdentity(serviceNotify, activitySpec.taskId));
            updateServerActivityIndex(getIdentity(activitySpec.serviceNotify, activitySpec.taskId), activitySpec.index);
            if (!activitySpec.register) {
                activitySpec.register = true;
                activitySpec.serviceNotifyIndex = activitySpec.index;
            }
            Iterator it = activitySpec.pendingTasks.iterator();
            while (it.hasNext()) {
                ((Runnable) it.next()).run();
            }
            activitySpec.pendingTasks.clear();
        } catch (RemoteException e) {
            Log.w("MFloatingSwitcher", "catch register service notify exception", e);
        }
    }

    private void updateServerActivityIndex(String str, int i) {
        IFloatingService iFloatingService = this.mIFloatingService;
        if (iFloatingService != null) {
            try {
                iFloatingService.upDateRemoteActivityInfo(str, i);
            } catch (RemoteException e) {
                Log.w("MFloatingSwitcher", "catch updateServerActivityIndex service notify exception", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unRegisterAll() {
        for (int i = 0; i < this.mActivityCache.size(); i++) {
            ArrayList arrayList = (ArrayList) this.mActivityCache.valueAt(i);
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                ActivitySpec activitySpec = (ActivitySpec) obj;
                unRegisterActivityFromService(activitySpec.taskId, activitySpec.identity);
            }
        }
    }

    private void unRegisterActivityFromService(int i, String str) {
        if (this.mIFloatingService != null) {
            try {
                ActivitySpec activitySpec = getActivitySpec(i, str);
                if (activitySpec != null) {
                    IFloatingService iFloatingService = this.mIFloatingService;
                    ServiceNotify serviceNotify = activitySpec.serviceNotify;
                    iFloatingService.unregisterServiceNotify(serviceNotify, String.valueOf(serviceNotify.hashCode()));
                }
            } catch (RemoteException e) {
                Log.w("MFloatingSwitcher", "catch unregister service notify exception", e);
            }
        }
    }

    public static void configureFloatingService(Intent intent, String str) {
        configureFloatingService(intent, str, (String) null);
    }

    public static void configureFloatingService(Intent intent, String str, String str2) {
        intent.putExtra("floating_service_pkg", str);
        if (TextUtils.isEmpty(str2)) {
            str2 = FloatingService.class.getName();
        }
        intent.putExtra("floating_service_path", str2);
        if (intent.getIntExtra("service_page_index", -1) < 0) {
            intent.putExtra("first_floating_activity", true);
            intent.putExtra("service_page_index", 0);
        }
    }

    public static void configureFloatingService(Intent intent, AppCompatActivity appCompatActivity) {
        configureFloatingService(intent, appCompatActivity.getIntent(), appCompatActivity.getTaskId());
    }

    private static void configureFloatingService(Intent intent, Intent intent2, int i) {
        intent.putExtra("floating_service_pkg", intent2.getStringExtra("floating_service_pkg"));
        intent.putExtra("floating_service_path", intent2.getStringExtra("floating_service_path"));
        if (!intent.getBooleanExtra("first_floating_activity", false)) {
            int intExtra = intent2.getIntExtra("service_page_index", -1);
            if (intExtra < 0) {
                Log.w("MFloatingSwitcher", "the value of SERVICE_PAGE_INDEX is invalid  , index = " + intExtra + " , please check it");
            }
            intent.putExtra("service_page_index", intExtra + 1);
        } else {
            intent.putExtra("service_page_index", 0);
        }
        MultiAppFloatingActivitySwitcher multiAppFloatingActivitySwitcher = getInstance();
        if (multiAppFloatingActivitySwitcher != null) {
            intent.putExtra("floating_service_original_page_index", multiAppFloatingActivitySwitcher.getServicePageCount(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unbindService(Context context) {
        if (this.mServiceConnected) {
            this.mServiceConnected = false;
            context.getApplicationContext().unbindService(this.mServiceConnection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeAllActivity() {
        if (isCalled(this.mCloseAllActivityTime)) {
            return;
        }
        this.mCloseAllActivityTime = System.currentTimeMillis();
        for (int i = 0; i < this.mActivityCache.size(); i++) {
            ArrayList arrayList = (ArrayList) this.mActivityCache.valueAt(i);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                AppCompatActivity appCompatActivity = ((ActivitySpec) arrayList.get(size)).activity;
                int i2 = ((ActivitySpec) arrayList.get(size)).index;
                int servicePageCount = getServicePageCount(((ActivitySpec) arrayList.get(size)).taskId);
                if (appCompatActivity != null && i2 != servicePageCount - 1) {
                    appCompatActivity.realFinish();
                }
            }
        }
    }

    void saveBitmap(Bitmap bitmap, int i, String str) throws Throwable {
        ActivitySpec activitySpec;
        if (bitmap == null || (activitySpec = getActivitySpec(i, str)) == null) {
            return;
        }
        int byteCount = bitmap.getByteCount();
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(byteCount);
        bitmap.copyPixelsToBuffer(byteBufferAllocate);
        MemoryFileUtil.sendToFdServer(this.mIFloatingService, byteBufferAllocate.array(), byteCount, bitmap.getWidth(), bitmap.getHeight(), String.valueOf(activitySpec.serviceNotify.hashCode()), i);
    }

    String getIdentity(Object obj, int i) {
        return obj.hashCode() + ":" + i;
    }

    View getLastActivityPanel() {
        WeakReference weakReference = this.mLastActivityPanel;
        if (weakReference == null) {
            return null;
        }
        return (View) weakReference.get();
    }

    void setLastActivityPanel(View view) {
        this.mLastActivityPanel = new WeakReference(view);
    }

    boolean isAboveActivityFinishing(int i, String str) {
        ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec == null) {
            return false;
        }
        Bundle bundle = new Bundle();
        bundle.putString("key_request_identity", String.valueOf(activitySpec.serviceNotify.hashCode()));
        bundle.putInt("key_task_id", i);
        Bundle bundleNotifyService = notifyService(9, bundle);
        return bundleNotifyService != null && bundleNotifyService.getBoolean("check_finishing");
    }

    private ActivitySpec getActivitySpec(int i, String str) {
        ArrayList arrayList = (ArrayList) this.mActivityCache.get(i);
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        int i2 = 0;
        while (i2 < size) {
            Object obj = arrayList.get(i2);
            i2++;
            ActivitySpec activitySpec = (ActivitySpec) obj;
            if (TextUtils.equals(activitySpec.identity, str)) {
                return activitySpec;
            }
        }
        return null;
    }

    AppCompatActivity getActivity(int i, String str) {
        ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec != null) {
            return activitySpec.activity;
        }
        return null;
    }

    private boolean isActivityStashed(AppCompatActivity appCompatActivity) {
        return (appCompatActivity == null || getActivitySpec(appCompatActivity.getTaskId(), appCompatActivity.getActivityIdentity()) == null) ? false : true;
    }

    private void hideTopBgs(int i) {
        ArrayList arrayList = (ArrayList) this.mActivityCache.get(i);
        if (arrayList != null) {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                int i3 = ((ActivitySpec) arrayList.get(i2)).index;
                AppCompatActivity appCompatActivity = ((ActivitySpec) arrayList.get(i2)).activity;
                if (appCompatActivity != null && i3 != 0) {
                    appCompatActivity.hideFloatingDimBackground();
                }
            }
        }
    }

    private void stashActivity(AppCompatActivity appCompatActivity, Intent intent, Bundle bundle) {
        if (!isActivityStashed(appCompatActivity)) {
            ActivitySpec activitySpec = bundle != null ? (ActivitySpec) bundle.getParcelable("floating_switcher_saved_key") : null;
            int i = 0;
            if (activitySpec == null) {
                activitySpec = new ActivitySpec(true);
                if (intent == null) {
                    intent = appCompatActivity.getIntent();
                }
                activitySpec.index = intent.getIntExtra("service_page_index", 0);
            }
            activitySpec.activity = appCompatActivity;
            activitySpec.taskId = appCompatActivity.getTaskId();
            activitySpec.identity = appCompatActivity.getActivityIdentity();
            ArrayList arrayList = (ArrayList) this.mActivityCache.get(activitySpec.taskId);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.mActivityCache.put(activitySpec.taskId, arrayList);
            }
            int i2 = activitySpec.index;
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                if (i2 > ((ActivitySpec) arrayList.get(size)).index) {
                    i = size + 1;
                    break;
                }
            }
            arrayList.add(i, activitySpec);
            FloatingAnimHelper.markedPageIndex(appCompatActivity, activitySpec.index);
        }
        hideTopBgs(appCompatActivity.getTaskId());
    }

    public boolean isActivityOpenEnterAnimExecuted(int i, String str) {
        ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec != null) {
            return activitySpec.isOpenEnterAnimExecuted;
        }
        return false;
    }

    void remove(int i, String str) {
        ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec == null || activitySpec.activity == null) {
            return;
        }
        unRegisterActivityFromService(i, str);
        ArrayList arrayList = (ArrayList) this.mActivityCache.get(i);
        if (arrayList != null) {
            arrayList.remove(activitySpec);
            if (arrayList.isEmpty()) {
                this.mActivityCache.remove(i);
            }
        }
        if (this.mActivityCache.size() == 0) {
            unbindService(activitySpec.activity);
            clear();
        }
    }

    void destroy() {
        if (this.mActivityCache.size() == 0) {
            sInstance = null;
        }
    }

    public void clear() {
        this.mActivityCache.clear();
        this.mLastActivityPanel = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle notifyService(int i) {
        return notifyService(i, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bundle notifyService(int i, Bundle bundle) {
        IFloatingService iFloatingService = this.mIFloatingService;
        if (iFloatingService != null) {
            try {
                return iFloatingService.callServiceMethod(i, bundle);
            } catch (RemoteException e) {
                Log.w("MFloatingSwitcher", "catch call service method exception", e);
                return null;
            }
        }
        Log.d("MFloatingSwitcher", "ifloatingservice is null");
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeAllPage() {
        if (isCalled(this.mCloseAllActivityTime)) {
            return;
        }
        this.mCloseAllActivityTime = System.currentTimeMillis();
        for (int i = 0; i < this.mActivityCache.size(); i++) {
            ArrayList arrayList = (ArrayList) this.mActivityCache.valueAt(i);
            for (int size = arrayList.size() - 1; size >= 0; size--) {
                AppCompatActivity appCompatActivity = ((ActivitySpec) arrayList.get(size)).activity;
                int i2 = ((ActivitySpec) arrayList.get(size)).index;
                int servicePageCount = getServicePageCount(((ActivitySpec) arrayList.get(size)).taskId);
                if (appCompatActivity != null && i2 != servicePageCount - 1) {
                    appCompatActivity.realFinish();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideBehindPages() {
        final AppCompatActivity appCompatActivity;
        if (isCalled(this.mOnDragStartTime)) {
            return;
        }
        this.mOnDragStartTime = System.currentTimeMillis();
        for (int i = 0; i < this.mActivityCache.size(); i++) {
            ArrayList arrayList = (ArrayList) this.mActivityCache.valueAt(i);
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                ActivitySpec activitySpec = (ActivitySpec) obj;
                if (!activitySpec.resumed && (appCompatActivity = activitySpec.activity) != null) {
                    appCompatActivity.runOnUiThread(new Runnable() { // from class: miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            appCompatActivity.hideFloatingBrightPanel();
                        }
                    });
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDragEnd() {
        final AppCompatActivity appCompatActivity;
        if (isCalled(this.mOnDragEndTime)) {
            return;
        }
        this.mOnDragEndTime = System.currentTimeMillis();
        for (int i = 0; i < this.mActivityCache.size(); i++) {
            ArrayList arrayList = (ArrayList) this.mActivityCache.valueAt(i);
            int size = arrayList.size();
            int i2 = 0;
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                ActivitySpec activitySpec = (ActivitySpec) obj;
                if (!activitySpec.resumed && (appCompatActivity = activitySpec.activity) != null) {
                    appCompatActivity.runOnUiThread(new Runnable() { // from class: miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            appCompatActivity.showFloatingBrightPanel();
                        }
                    });
                }
            }
        }
    }

    boolean isServiceAvailable() {
        return this.mIFloatingService != null;
    }

    void postEnterAnimationTask(int i, String str, Runnable runnable) {
        if (isActivityOpenEnterAnimExecuted(i, str)) {
            return;
        }
        if (getCurrentPageCount(i) > 1 || getServicePageCount(i) > 1) {
            markActivityOpenEnterAnimExecutedInternal(i, str);
        }
        if (isServiceAvailable()) {
            runnable.run();
            return;
        }
        ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec != null) {
            activitySpec.pendingTasks.add(runnable);
        }
    }

    void clearActivitySpecTask(int i, String str) {
        ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec != null) {
            activitySpec.pendingTasks.clear();
        }
    }

    void markActivityOpenEnterAnimExecutedInternal(int i, String str) {
        ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec != null) {
            activitySpec.isOpenEnterAnimExecuted = true;
        }
    }

    void notifyPreviousActivitySlide(int i, String str) {
        final ActivitySpec activitySpec = getActivitySpec(i, str);
        if (activitySpec == null) {
            return;
        }
        Runnable runnable = new Runnable() { // from class: miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher.2
            @Override // java.lang.Runnable
            public void run() {
                String strValueOf = String.valueOf(activitySpec.serviceNotify.hashCode());
                Bundle bundle = new Bundle();
                bundle.putInt("key_task_id", activitySpec.taskId);
                bundle.putString("execute_slide", strValueOf);
                MultiAppFloatingActivitySwitcher.this.notifyService(10, bundle);
            }
        };
        if (isServiceAvailable()) {
            runnable.run();
        } else {
            activitySpec.pendingTasks.add(runnable);
        }
    }

    int getCurrentPageCount(int i) {
        ArrayList arrayList = (ArrayList) this.mActivityCache.get(i);
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    int getServicePageCount(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("key_task_id", i);
        Bundle bundleNotifyService = notifyService(6, bundle);
        int i2 = 0;
        int i3 = bundleNotifyService != null ? bundleNotifyService.getInt(String.valueOf(6)) : 0;
        ArrayList arrayList = (ArrayList) this.mActivityCache.get(i);
        if (arrayList != null) {
            int size = arrayList.size();
            while (i2 < size) {
                Object obj = arrayList.get(i2);
                i2++;
                int i4 = ((ActivitySpec) obj).index;
                if (i4 + 1 > i3) {
                    i3 = i4 + 1;
                }
            }
        }
        return i3;
    }

    class DefineOnFloatingActivityCallback implements OnFloatingCallback {
        protected int mAppCompatActivityTaskId;
        protected String mAppCompatIdentity;

        public DefineOnFloatingActivityCallback(AppCompatActivity appCompatActivity) {
            this.mAppCompatIdentity = appCompatActivity.getActivityIdentity();
            this.mAppCompatActivityTaskId = appCompatActivity.getTaskId();
        }

        protected String getActivityIdentity() {
            return this.mAppCompatIdentity;
        }

        protected int getActivityTaskId() {
            return this.mAppCompatActivityTaskId;
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingActivityCallback
        public boolean onFinish(int i) {
            if (!checkFinishEnable(i) && MultiAppFloatingActivitySwitcher.this.shouldAllFloatingClose(i, getActivityTaskId())) {
                MultiAppFloatingActivitySwitcher.this.notifyService(5);
            }
            return false;
        }

        private boolean checkFinishEnable(int i) {
            if (MultiAppFloatingActivitySwitcher.this.mEnableDragToDismiss) {
                return false;
            }
            return i == 1 || i == 2;
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void onDragStart() {
            MultiAppFloatingActivitySwitcher.this.notifyService(1);
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void onDragEnd() {
            MultiAppFloatingActivitySwitcher.this.notifyService(2);
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void onHideBehindPage() {
            MultiAppFloatingActivitySwitcher.this.notifyService(5);
        }

        public int getPageCount() {
            return Math.max(MultiAppFloatingActivitySwitcher.this.getServicePageCount(getActivityTaskId()), MultiAppFloatingActivitySwitcher.this.getCurrentPageCount(getActivityTaskId()));
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void closeAllPage() {
            MultiAppFloatingActivitySwitcher.this.notifyService(11);
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public boolean isFirstPageEnterAnimExecuteEnable() {
            ArrayList arrayList = (ArrayList) MultiAppFloatingActivitySwitcher.this.mActivityCache.get(getActivityTaskId());
            if (arrayList != null) {
                for (int i = 0; i < arrayList.size(); i++) {
                    ActivitySpec activitySpec = (ActivitySpec) arrayList.get(i);
                    if (activitySpec.index == 0) {
                        return !activitySpec.isOpenEnterAnimExecuted;
                    }
                }
            }
            return false;
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public boolean isFirstPageExitAnimExecuteEnable() {
            return getPageCount() == 1;
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void markActivityOpenEnterAnimExecuted(AppCompatActivity appCompatActivity) {
            MultiAppFloatingActivitySwitcher.this.markActivityOpenEnterAnimExecutedInternal(appCompatActivity.getTaskId(), appCompatActivity.getActivityIdentity());
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void getSnapShotAndSetPanel(AppCompatActivity appCompatActivity) throws Throwable {
            if (appCompatActivity != null) {
                try {
                    MultiAppFloatingActivitySwitcher multiAppFloatingActivitySwitcher = MultiAppFloatingActivitySwitcher.getInstance();
                    if (multiAppFloatingActivitySwitcher != null) {
                        multiAppFloatingActivitySwitcher.saveBitmap(SnapShotViewHelper.getSnapShot(appCompatActivity.getFloatingBrightPanel()), appCompatActivity.getTaskId(), appCompatActivity.getActivityIdentity());
                    }
                } catch (Exception e) {
                    Log.d("MFloatingSwitcher", "saveBitmap exception", e);
                }
            }
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public boolean isFirstPage() {
            ArrayList arrayList = (ArrayList) MultiAppFloatingActivitySwitcher.this.mActivityCache.get(getActivityTaskId());
            if (arrayList != null) {
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ActivitySpec activitySpec = (ActivitySpec) arrayList.get(i);
                    AppCompatActivity appCompatActivity = activitySpec.activity;
                    if (appCompatActivity != null && activitySpec.index == 0) {
                        return appCompatActivity.getActivityIdentity().equals(getActivityIdentity());
                    }
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldAllFloatingClose(int i, int i2) {
        return !(i == 4 || i == 3) || getServicePageCount(i2) <= 1;
    }

    class ServiceNotify extends IServiceNotify.Stub {
        protected String mActivityIdentity;
        protected int mActivityTaskId;

        protected String getActivityIdentity() {
            return this.mActivityIdentity;
        }

        protected int getActivityTaskId() {
            return this.mActivityTaskId;
        }

        public ServiceNotify(AppCompatActivity appCompatActivity) {
            this.mActivityIdentity = appCompatActivity.getActivityIdentity();
            this.mActivityTaskId = appCompatActivity.getTaskId();
        }

        public void resetAppCompatActivity(AppCompatActivity appCompatActivity) {
            this.mActivityIdentity = appCompatActivity.getActivityIdentity();
            this.mActivityTaskId = appCompatActivity.getTaskId();
        }

        private AppCompatActivity getActivity() {
            MultiAppFloatingActivitySwitcher multiAppFloatingActivitySwitcher = MultiAppFloatingActivitySwitcher.getInstance();
            if (multiAppFloatingActivitySwitcher != null) {
                return multiAppFloatingActivitySwitcher.getActivity(getActivityTaskId(), getActivityIdentity());
            }
            return null;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // miuix.appcompat.app.floatingactivity.multiapp.IServiceNotify
        public Bundle notifyFromService(int i, Bundle bundle) {
            Bundle bundle2 = new Bundle();
            if (i == 1) {
                MultiAppFloatingActivitySwitcher.sInstance.hideBehindPages();
                return bundle2;
            }
            if (i == 2) {
                MultiAppFloatingActivitySwitcher.sInstance.onDragEnd();
                return bundle2;
            }
            if (i == 3) {
                MultiAppFloatingActivitySwitcher.sInstance.closeAllActivity();
                AppCompatActivity activity = getActivity();
                if (activity != null) {
                    MultiAppFloatingActivitySwitcher.sInstance.unbindService(activity);
                }
            } else {
                if (i == 5) {
                    MultiAppFloatingActivitySwitcher.sInstance.hideBehindPages();
                    return bundle2;
                }
                switch (i) {
                    case 8:
                        AppCompatActivity activity2 = getActivity();
                        if (bundle != null && activity2 != null) {
                            View floatingBrightPanel = activity2.getFloatingBrightPanel();
                            MultiAppFloatingActivitySwitcher.this.setLastActivityPanel(SnapShotViewHelper.generateSnapShotView(floatingBrightPanel, MemoryFileUtil.readBitmap(bundle)));
                            if (MultiAppFloatingActivitySwitcher.this.mLastActivityPanel != null && MultiAppFloatingActivitySwitcher.this.mLastActivityPanel.get() != null) {
                                ((ViewGroup) floatingBrightPanel.getParent()).getOverlay().add((View) MultiAppFloatingActivitySwitcher.this.mLastActivityPanel.get());
                                return bundle2;
                            }
                        }
                        break;
                    case 9:
                        AppCompatActivity activity3 = getActivity();
                        bundle2.putBoolean("check_finishing", activity3 != null && activity3.isFinishing());
                        return bundle2;
                    case 10:
                        AppCompatActivity activity4 = getActivity();
                        if (activity4 != null) {
                            MultiAppFloatingActivitySwitcher.this.mExitAnimationHandler.postDelayed(new OpenExitAnimationExecutor(activity4), 160L);
                            return bundle2;
                        }
                        break;
                    case 11:
                        MultiAppFloatingActivitySwitcher.sInstance.closeAllPage();
                        return bundle2;
                }
            }
            return bundle2;
        }
    }

    class OpenExitAnimationExecutor implements Runnable {
        private WeakReference mAppCompatActivity;

        public OpenExitAnimationExecutor(AppCompatActivity appCompatActivity) {
            this.mAppCompatActivity = null;
            this.mAppCompatActivity = new WeakReference(appCompatActivity);
        }

        @Override // java.lang.Runnable
        public void run() {
            AppCompatActivity appCompatActivity = (AppCompatActivity) this.mAppCompatActivity.get();
            if (appCompatActivity != null) {
                appCompatActivity.executeOpenExitAnimation();
            }
        }
    }

    class ActivitySpec implements Parcelable {
        public static final Parcelable.Creator CREATOR = new Parcelable.Creator() { // from class: miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher.ActivitySpec.1
            @Override // android.os.Parcelable.Creator
            public ActivitySpec createFromParcel(Parcel parcel) {
                return new ActivitySpec(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public ActivitySpec[] newArray(int i) {
                return new ActivitySpec[i];
            }
        };
        AppCompatActivity activity;
        String identity;
        int index;
        boolean isOpenEnterAnimExecuted;
        List pendingTasks;
        boolean register;
        boolean resumed;
        ServiceNotify serviceNotify;
        int serviceNotifyIndex;
        int taskId;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        protected ActivitySpec(boolean z) {
            this.index = -1;
            this.register = false;
            this.isOpenEnterAnimExecuted = false;
            this.resumed = z;
            this.pendingTasks = new LinkedList();
        }

        protected ActivitySpec(Parcel parcel) {
            this.index = -1;
            this.register = false;
            this.isOpenEnterAnimExecuted = false;
            this.index = parcel.readInt();
            this.taskId = parcel.readInt();
            this.identity = parcel.readString();
            this.resumed = parcel.readByte() != 0;
            this.serviceNotifyIndex = parcel.readInt();
            this.register = parcel.readByte() != 0;
            this.isOpenEnterAnimExecuted = parcel.readByte() != 0;
            this.pendingTasks = new LinkedList();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.index);
            parcel.writeInt(this.taskId);
            parcel.writeString(this.identity);
            parcel.writeByte(this.resumed ? (byte) 1 : (byte) 0);
            parcel.writeInt(this.serviceNotifyIndex);
            parcel.writeByte(this.register ? (byte) 1 : (byte) 0);
            parcel.writeByte(this.isOpenEnterAnimExecuted ? (byte) 1 : (byte) 0);
        }

        public String toString() {
            return "{ index : " + this.index + "; taskId : " + this.taskId + "; taskId : " + this.taskId + "; identity : " + this.identity + "; serviceNotifyIndex : " + this.serviceNotifyIndex + "; register : " + this.register + "; isOpenEnterAnimExecuted : " + this.isOpenEnterAnimExecuted + "; }";
        }
    }
}
