package com.android.phone.freqopt.engine;

import android.content.ComponentName;
import android.os.Handler;
import com.android.phone.freqopt.util.LogUtils;
import com.android.phone.utils.MiuiAppStateMonitor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes.dex */
public class WhitelistAppMonitor implements MiuiAppStateMonitor.TopAppObserver {
    private volatile String mForegroundWhitelistApp;
    private final Handler mHandler;
    private final Set mWhitelistApps = new HashSet();
    private final CopyOnWriteArrayList mListeners = new CopyOnWriteArrayList();

    public interface OnWhitelistAppStateChangedListener {
        void onWhitelistAppBackground(String str);

        void onWhitelistAppForeground(String str);
    }

    public WhitelistAppMonitor(Handler handler, String[] strArr) {
        this.mHandler = handler;
        if (strArr != null) {
            for (String str : strArr) {
                if (str != null && !str.isEmpty()) {
                    this.mWhitelistApps.add(str);
                }
            }
        }
        MiuiAppStateMonitor.init().registerAppObserver(this);
        LogUtils.d("Freq_Opt_WhitelistAppMon", "init, whitelist=" + Arrays.toString(strArr));
    }

    @Override // com.android.phone.utils.MiuiAppStateMonitor.TopAppObserver
    public void onTopAppChanged(List list, int i) {
        if (list == null || list.isEmpty()) {
            return;
        }
        String strFindWhitelistApp = findWhitelistApp(list);
        String str = this.mForegroundWhitelistApp;
        if (Objects.equals(str, strFindWhitelistApp)) {
            return;
        }
        this.mForegroundWhitelistApp = strFindWhitelistApp;
        LogUtils.d("Freq_Opt_WhitelistAppMon", "app state changed: " + str + " -> " + strFindWhitelistApp);
        dispatchStateChange(str, strFindWhitelistApp);
    }

    private String findWhitelistApp(List list) {
        for (Object obj : list) {
            if (obj instanceof ComponentName) {
                String packageName = ((ComponentName) obj).getPackageName();
                if (this.mWhitelistApps.contains(packageName)) {
                    return packageName;
                }
            }
        }
        return null;
    }

    private void dispatchStateChange(final String str, final String str2) {
        this.mHandler.post(new Runnable() { // from class: com.android.phone.freqopt.engine.WhitelistAppMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dispatchStateChange$0(str, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dispatchStateChange$0(String str, String str2) {
        for (OnWhitelistAppStateChangedListener onWhitelistAppStateChangedListener : this.mListeners) {
            if (str != null) {
                onWhitelistAppStateChangedListener.onWhitelistAppBackground(str);
            }
            if (str2 != null) {
                onWhitelistAppStateChangedListener.onWhitelistAppForeground(str2);
            }
        }
    }

    public String getForegroundWhitelistApp() {
        return this.mForegroundWhitelistApp;
    }

    public void addListener(OnWhitelistAppStateChangedListener onWhitelistAppStateChangedListener) {
        this.mListeners.addIfAbsent(onWhitelistAppStateChangedListener);
    }
}
