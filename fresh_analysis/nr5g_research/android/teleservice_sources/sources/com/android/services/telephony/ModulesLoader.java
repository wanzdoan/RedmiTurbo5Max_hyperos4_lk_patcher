package com.android.services.telephony;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.PhoneGlobals;
import com.android.phone.eventhub.EventHubManager;
import com.android.phone.eventhub.events.BaseEvent;
import com.android.phone.eventhub.events.TelephonyEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/* JADX INFO: loaded from: classes.dex */
public final class ModulesLoader {
    protected static volatile ModulesLoader mInstance;
    private final Map mModuleMap = Collections.synchronizedMap(new LinkedHashMap());
    private volatile boolean mIsLoaded = false;
    private volatile boolean mSimPresentNotified = false;
    private final BaseEvent.IEventListener mIccChangedListener = new BaseEvent.IEventListener() { // from class: com.android.services.telephony.ModulesLoader.1
        @Override // com.android.phone.eventhub.events.BaseEvent.IEventListener
        public void onEvent(BaseEvent baseEvent) {
            if (baseEvent instanceof TelephonyEvent.SimEvent) {
                boolean z = MiuiPhoneUtils.getInsertedSimCount() > 0;
                if (!ModulesLoader.this.mSimPresentNotified && z && ModulesLoader.this.mIsLoaded) {
                    ModulesLoader.this.mSimPresentNotified = true;
                    ModulesLoader.this.dispatchSimPresent();
                }
            }
        }
    };
    private Context mContext = PhoneGlobals.getInstance();
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    public static ModulesLoader getInstance() {
        if (mInstance == null) {
            synchronized (ModulesLoader.class) {
                try {
                    if (mInstance == null) {
                        mInstance = new ModulesLoader();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return mInstance;
    }

    private ModulesLoader() {
    }

    public void initialize() {
        if (this.mIsLoaded) {
            android.util.Log.w("ModulesLoader", "all modules already loaded");
        } else {
            EventHubManager.getInstance().registerListener("telephony", "icc_changed", this.mIccChangedListener);
            Executors.newSingleThreadExecutor().execute(new Runnable() { // from class: com.android.services.telephony.ModulesLoader$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$initialize$1();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$1() {
        try {
            final List list = (List) ServiceLoader.load(ICoreModule.class, ICoreModule.class.getClassLoader()).stream().map(new Function() { // from class: com.android.services.telephony.ModulesLoader$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return (ICoreModule) ((ServiceLoader.Provider) obj).get();
                }
            }).sorted(Comparator.comparingInt(new ToIntFunction() { // from class: com.android.services.telephony.ModulesLoader$$ExternalSyntheticLambda2
                @Override // java.util.function.ToIntFunction
                public final int applyAsInt(Object obj) {
                    return ((ICoreModule) obj).getPriority();
                }
            }).reversed()).collect(Collectors.toList());
            this.mMainHandler.post(new Runnable() { // from class: com.android.services.telephony.ModulesLoader$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$initialize$0(list);
                }
            });
        } catch (Exception e) {
            android.util.Log.e("ModulesLoader", "Failed to discover modules", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$0(List list) {
        loadModules(list);
        boolean z = MiuiPhoneUtils.getInsertedSimCount() > 0;
        if (!this.mSimPresentNotified && z && this.mIsLoaded) {
            dispatchSimPresent();
            this.mSimPresentNotified = true;
        }
    }

    private void loadModules(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            ICoreModule iCoreModule = (ICoreModule) it.next();
            if (iCoreModule.isEnabled(this.mContext)) {
                try {
                    iCoreModule.onCreate(this.mContext);
                    this.mModuleMap.put(iCoreModule.getModuleId(), iCoreModule);
                    android.util.Log.d("ModulesLoader", iCoreModule.getModuleId() + " loaded");
                } catch (Exception e) {
                    android.util.Log.e("ModulesLoader", "Failed to load module : " + iCoreModule.getModuleId(), e);
                }
            }
        }
        this.mIsLoaded = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchSimPresent() {
        this.mMainHandler.post(new Runnable() { // from class: com.android.services.telephony.ModulesLoader$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dispatchSimPresent$3();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchSimPresent$3() {
        for (ICoreModule iCoreModule : this.mModuleMap.values()) {
            if (iCoreModule.isEnabled(this.mContext)) {
                try {
                    iCoreModule.onSimPresent();
                } catch (Exception e) {
                    android.util.Log.e("ModulesLoader", "Failed to dispatchSimPresent: " + iCoreModule.getModuleId(), e);
                }
            }
        }
    }
}
