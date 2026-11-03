package miuix.preference.flexible;

import android.view.ViewGroup;

/* JADX INFO: loaded from: classes2.dex */
public class MiuixPreferenceTemplate extends AbstractBaseTemplate {
    @Override // miuix.preference.flexible.AbstractBaseTemplate
    public void checkView(ViewGroup viewGroup) {
    }

    @Override // miuix.preference.flexible.AbstractBaseTemplate
    public int onLargeLayoutSelected() {
        boolean z = this.mHasTitle;
        if (z && this.mHasSummary) {
            return PreferenceMarkLevel.LEVEL_LARGE_FULL_TITLE_MULTI;
        }
        if (!z || this.mHasSummary) {
            return (z || !this.mHasSummary) ? PreferenceMarkLevel.LEVEL_LARGE_FULL_TITLE_MULTI : PreferenceMarkLevel.LEVEL_LARGE_ONLY_SUMMARY;
        }
        return PreferenceMarkLevel.LEVEL_LARGE_ONLY_TITLE;
    }
}
