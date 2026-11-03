package miuix.responsive.map;

import android.content.Context;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class ResponsiveStateManager {
    private static ConcurrentHashMap mapState = new ConcurrentHashMap();
    private static volatile ResponsiveStateManager sInstance;

    public static ResponsiveStateManager getInstance() {
        if (sInstance == null) {
            synchronized (ResponsiveStateManager.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new ResponsiveStateManager();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public ResponsiveState recordState(Context context, ResponsiveState.WindowInfoWrapper windowInfoWrapper) {
        if (context == null) {
            return null;
        }
        int iHashCode = context.hashCode();
        ResponsiveState responsiveState = (ResponsiveState) mapState.get(Integer.valueOf(iHashCode));
        if (responsiveState == null) {
            responsiveState = new ResponsiveState();
            mapState.put(Integer.valueOf(iHashCode), responsiveState);
        }
        responsiveState.updateFromWindowInfoWrapper(windowInfoWrapper);
        return responsiveState;
    }

    public void remove(Context context) {
        mapState.remove(Integer.valueOf(context.hashCode()));
    }
}
