package miuix.springback.view;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;
import miuix.core.widget.NestedScrollView;

/* JADX INFO: loaded from: classes2.dex */
public class EmptyStateInflationDelegate {
    private NestedScrollView mContainer;
    private FrameLayout.LayoutParams mContainerParams;
    private int mEmptyStateRes;
    private View.OnLayoutChangeListener mOnLayoutChangeListener;
    private SpringBackLayout mSpringBack;
    private View mSpringBackParent;
    private Point mWindowSize = new Point();
    private Rect mGlobalVisibleRect = new Rect();
    private boolean mAttachToRoot = false;
    private boolean mInflated = false;
    private boolean mOverlay = false;
    private int mTopInset = 0;
    private int mBottomInset = 0;

    public EmptyStateInflationDelegate(SpringBackLayout springBackLayout, int i) {
        Context context = springBackLayout.getContext();
        this.mEmptyStateRes = i;
        this.mContainer = new NestedScrollView(context);
        this.mContainerParams = new FrameLayout.LayoutParams(-1, -1);
        this.mSpringBack = springBackLayout;
    }

    public void onDetachedFromWindow() {
        View.OnLayoutChangeListener onLayoutChangeListener;
        View view = this.mSpringBackParent;
        if (view == null || (onLayoutChangeListener = this.mOnLayoutChangeListener) == null) {
            return;
        }
        view.removeOnLayoutChangeListener(onLayoutChangeListener);
        this.mOnLayoutChangeListener = null;
    }
}
