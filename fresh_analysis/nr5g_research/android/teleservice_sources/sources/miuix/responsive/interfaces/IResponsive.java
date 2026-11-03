package miuix.responsive.interfaces;

import android.content.res.Configuration;
import miuix.responsive.map.ScreenSpec;

/* JADX INFO: loaded from: classes2.dex */
public interface IResponsive {
    Object getResponsiveSubject();

    void onResponsiveLayout(Configuration configuration, ScreenSpec screenSpec, boolean z);

    default void dispatchResponsiveLayout(Configuration configuration, ScreenSpec screenSpec, boolean z) {
        onResponsiveLayout(configuration, screenSpec, z);
    }
}
