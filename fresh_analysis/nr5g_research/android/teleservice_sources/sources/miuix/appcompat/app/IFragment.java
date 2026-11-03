package miuix.appcompat.app;

import android.content.Context;
import miuix.container.ExtraPaddingObserver;

/* JADX INFO: loaded from: classes2.dex */
public interface IFragment extends IContentInsetState, ExtraPaddingObserver {
    ActionBar getActionBar();

    Context getThemedContext();

    boolean hasActionBar();
}
