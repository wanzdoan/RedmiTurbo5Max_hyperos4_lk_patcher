package androidx.lifecycle;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.Reflection;
import kotlin.text.CharsKt;

/* JADX INFO: compiled from: ViewModelStore.kt */
/* JADX INFO: loaded from: classes.dex */
public class ViewModelStore {
    private final Map map = new LinkedHashMap();

    public final void put(Object obj, ViewModel viewModel) {
        viewModel.getClass();
        ViewModel viewModel2 = (ViewModel) this.map.put(obj, viewModel);
        if (viewModel2 != null) {
            viewModel2.clear$lifecycle_viewmodel();
        }
    }

    public final ViewModel get(Object obj) {
        return (ViewModel) this.map.get(obj);
    }

    public final Set keys() {
        return CollectionsKt.toSet(this.map.keySet());
    }

    public final void clear() {
        Map map = MapsKt.toMap(this.map);
        this.map.clear();
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            ((ViewModel) it.next()).clear$lifecycle_viewmodel();
        }
    }

    public String toString() {
        String simpleName = Reflection.getOrCreateKotlinClass(getClass()).getSimpleName();
        if (simpleName == null) {
            simpleName = "ViewModelStore";
        }
        String string = Integer.toString(hashCode(), CharsKt.checkRadix(16));
        string.getClass();
        return simpleName + '@' + string + "(keys=" + keys() + ')';
    }
}
