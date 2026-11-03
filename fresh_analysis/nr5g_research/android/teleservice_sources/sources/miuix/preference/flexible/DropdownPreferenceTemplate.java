package miuix.preference.flexible;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import miuix.preference.R$id;

/* JADX INFO: loaded from: classes2.dex */
public class DropdownPreferenceTemplate extends AbstractBaseTemplate {
    @Override // miuix.preference.flexible.AbstractBaseTemplate
    public int onLargeLayoutSelected() {
        boolean z = this.mHasTitle;
        if (z && this.mHasSummary) {
            return PreferenceMarkLevel.LEVEL_LARGE_FULL_TITLE_MULTI_TEXT;
        }
        return ((!z || this.mHasSummary) && !z && this.mHasSummary) ? PreferenceMarkLevel.LEVEL_LARGE_SUMMARY_WIDGET_TEXT : PreferenceMarkLevel.LEVEL_LARGE_TITLE_MULTI_WIDGET_TEXT;
    }

    @Override // miuix.preference.flexible.AbstractBaseTemplate
    public void checkView(ViewGroup viewGroup) {
        View viewFindViewByAreaId = findViewByAreaId(viewGroup, R$id.area_end2);
        if (viewFindViewByAreaId instanceof LinearLayout) {
            boolean z = false;
            View childAt = ((LinearLayout) viewFindViewByAreaId).getChildAt(0);
            if (childAt != null && childAt.getVisibility() == 0) {
                z = true;
            }
            this.mHasWidget = z;
        }
    }
}
