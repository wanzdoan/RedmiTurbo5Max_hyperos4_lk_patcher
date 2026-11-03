package androidx.customview.poolingcontainer;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.ArrayList;
import kotlin.collections.CollectionsKt;

/* JADX INFO: compiled from: PoolingContainer.kt */
/* JADX INFO: loaded from: classes.dex */
final class PoolingContainerListenerHolder {
    private final ArrayList listeners = new ArrayList();

    public final void onRelease() {
        int lastIndex = CollectionsKt.getLastIndex(this.listeners);
        if (-1 >= lastIndex) {
            return;
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.listeners.get(lastIndex));
        throw null;
    }
}
