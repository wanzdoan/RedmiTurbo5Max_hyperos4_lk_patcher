package androidx.viewpager2.widget;

import androidx.recyclerview.widget.LinearLayoutManager;

/* JADX INFO: loaded from: classes.dex */
final class OriginalPageTransformerAdapter extends OriginalViewPager2.OnPageChangeCallback {
    private final LinearLayoutManager mLayoutManager;

    @Override // androidx.viewpager2.widget.OriginalViewPager2.OnPageChangeCallback
    public void onPageScrollStateChanged(int i) {
    }

    @Override // androidx.viewpager2.widget.OriginalViewPager2.OnPageChangeCallback
    public void onPageScrolled(int i, float f, int i2) {
    }

    @Override // androidx.viewpager2.widget.OriginalViewPager2.OnPageChangeCallback
    public void onPageSelected(int i) {
    }

    OriginalPageTransformerAdapter(LinearLayoutManager linearLayoutManager) {
        this.mLayoutManager = linearLayoutManager;
    }
}
