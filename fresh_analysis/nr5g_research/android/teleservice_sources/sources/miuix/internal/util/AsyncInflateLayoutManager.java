package miuix.internal.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class AsyncInflateLayoutManager {
    private static final AsyncInflateLayoutManager MANAGER = new AsyncInflateLayoutManager();
    private final HashMap mListTypeMap = new HashMap();
    private boolean isEnabled = true;
    private boolean isEnabledLog = false;
    private boolean isCachedEnable = false;
    private final AsyncLayoutInflater$OnInflateFinishedListener mListener = new AsyncLayoutInflater$OnInflateFinishedListener() { // from class: miuix.internal.util.AsyncInflateLayoutManager.1
    };

    public static AsyncInflateLayoutManager getInstance() {
        return MANAGER;
    }

    public View inflateViewById(Integer num, ViewGroup viewGroup, boolean z) {
        if (viewGroup == null) {
            return null;
        }
        List list = (List) this.mListTypeMap.get(num);
        if (list == null || list.isEmpty()) {
            if (this.isEnabledLog) {
                Log.i("AsyncInflateManager", "inflateCacheById is null.");
            }
            return LayoutInflater.from(viewGroup.getContext()).inflate(num.intValue(), viewGroup, z);
        }
        View view = (View) list.remove(0);
        if (list.isEmpty()) {
            this.mListTypeMap.remove(num);
        }
        if (this.isEnabledLog) {
            Log.i("AsyncInflateManager", "inflateCacheById is ok.");
        }
        return view;
    }

    public View inflateViewById(Integer num, Context context, ViewGroup viewGroup, boolean z) {
        View view;
        if (viewGroup == null || context == null) {
            return null;
        }
        List list = (List) this.mListTypeMap.get(num);
        if (list == null || list.isEmpty()) {
            if (this.isEnabledLog) {
                Log.i("AsyncInflateManager", "inflateCacheById is null.");
            }
            return LayoutInflater.from(context).inflate(num.intValue(), viewGroup, z);
        }
        if (this.isCachedEnable) {
            view = (View) list.get(0);
        } else {
            view = (View) list.remove(0);
        }
        if (list.isEmpty()) {
            this.mListTypeMap.remove(num);
        }
        if (this.isEnabledLog) {
            Log.i("AsyncInflateManager", "inflateCacheById is ok.");
        }
        return view;
    }
}
