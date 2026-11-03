package miuix.appcompat.app;

import android.view.Menu;
import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
interface ActionBarDelegate extends IContentInsetState {
    ActionBar createActionBar();

    void invalidateOptionsMenu();

    void onPanelViewAdded(int i, View view, Menu menu, Menu menu2);
}
