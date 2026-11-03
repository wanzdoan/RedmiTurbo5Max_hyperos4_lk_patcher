package androidx.viewpager2.widget;

import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes.dex */
final class OriginalFakeDrag {
    private final RecyclerView mRecyclerView;
    private final OriginalScrollEventAdapter mScrollEventAdapter;
    private final OriginalViewPager2 mViewPager;

    OriginalFakeDrag(OriginalViewPager2 originalViewPager2, OriginalScrollEventAdapter originalScrollEventAdapter, RecyclerView recyclerView) {
        this.mViewPager = originalViewPager2;
        this.mScrollEventAdapter = originalScrollEventAdapter;
        this.mRecyclerView = recyclerView;
    }

    boolean isFakeDragging() {
        return this.mScrollEventAdapter.isFakeDragging();
    }
}
