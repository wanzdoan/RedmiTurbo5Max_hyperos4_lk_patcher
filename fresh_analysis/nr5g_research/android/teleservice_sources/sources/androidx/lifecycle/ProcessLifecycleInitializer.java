package androidx.lifecycle;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.content.Context;
import androidx.startup.AppInitializer;
import androidx.startup.Initializer;
import java.util.List;
import kotlin.collections.CollectionsKt;

/* JADX INFO: compiled from: ProcessLifecycleInitializer.kt */
/* JADX INFO: loaded from: classes.dex */
public final class ProcessLifecycleInitializer implements Initializer {
    @Override // androidx.startup.Initializer
    public LifecycleOwner create(Context context) {
        context.getClass();
        AppInitializer appInitializer = AppInitializer.getInstance(context);
        appInitializer.getClass();
        if (!appInitializer.isEagerlyInitialized(ProcessLifecycleInitializer.class)) {
            OrtUtil$$ExternalSyntheticBUOutline0.m("ProcessLifecycleInitializer cannot be initialized lazily.\n               Please ensure that you have:\n               <meta-data\n                   android:name='androidx.lifecycle.ProcessLifecycleInitializer'\n                   android:value='androidx.startup' />\n               under InitializationProvider in your AndroidManifest.xml");
            return null;
        }
        LifecycleDispatcher.init(context);
        ProcessLifecycleOwner.Companion companion = ProcessLifecycleOwner.Companion;
        companion.init$lifecycle_process(context);
        return companion.get();
    }

    @Override // androidx.startup.Initializer
    public List dependencies() {
        return CollectionsKt.emptyList();
    }
}
