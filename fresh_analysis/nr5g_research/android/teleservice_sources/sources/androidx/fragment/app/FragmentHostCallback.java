package androidx.fragment.app;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import androidx.core.content.ContextCompat;
import java.io.FileDescriptor;
import java.io.PrintWriter;

/* JADX INFO: compiled from: FragmentHostCallback.kt */
/* JADX INFO: loaded from: classes.dex */
public abstract class FragmentHostCallback extends FragmentContainer {
    private final Activity activity;
    private final Context context;
    private final FragmentManager fragmentManager;
    private final Handler handler;
    private final int windowAnimations;

    public abstract void onDump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract Object onGetHost();

    public abstract LayoutInflater onGetLayoutInflater();

    public abstract void onSupportInvalidateOptionsMenu();

    public FragmentHostCallback(Activity activity, Context context, Handler handler, int i) {
        context.getClass();
        handler.getClass();
        this.activity = activity;
        this.context = context;
        this.handler = handler;
        this.windowAnimations = i;
        this.fragmentManager = new FragmentManagerImpl();
    }

    public final Activity getActivity() {
        return this.activity;
    }

    public final Context getContext() {
        return this.context;
    }

    public final Handler getHandler() {
        return this.handler;
    }

    public final FragmentManager getFragmentManager() {
        return this.fragmentManager;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public FragmentHostCallback(FragmentActivity fragmentActivity) {
        this(fragmentActivity, fragmentActivity, new Handler(), 0);
        fragmentActivity.getClass();
    }

    public void onStartActivityFromFragment(Fragment fragment, Intent intent, int i, Bundle bundle) {
        fragment.getClass();
        intent.getClass();
        if (i != -1) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("Starting activity with a requestCode requires a FragmentActivity host");
        } else {
            ContextCompat.startActivity(this.context, intent, bundle);
        }
    }
}
