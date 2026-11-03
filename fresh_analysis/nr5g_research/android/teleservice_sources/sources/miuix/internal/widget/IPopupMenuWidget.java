package miuix.internal.widget;

import android.view.Menu;
import android.view.View;

/* JADX INFO: loaded from: classes2.dex */
public interface IPopupMenuWidget {
    void dismiss();

    void showAsDropDown(View view);

    void update(Menu menu);
}
