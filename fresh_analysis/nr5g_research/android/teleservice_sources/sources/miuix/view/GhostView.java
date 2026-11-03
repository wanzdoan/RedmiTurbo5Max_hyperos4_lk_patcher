package miuix.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RenderNode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import miuix.core.R$id;

/* JADX INFO: loaded from: classes2.dex */
public class GhostView extends View {
    private int bottom;
    private int left;
    private View mOriginalView;
    private ViewGroup mOverlay;
    private RenderNode mRenderNode;
    private int right;
    private int top;

    public GhostView(Context context, View view) {
        super(context);
        this.mOriginalView = view;
        this.mRenderNode = new RenderNode("GhostView");
        int width = view.getWidth();
        int height = view.getHeight();
        this.right = width;
        this.bottom = height;
        this.mRenderNode.setPosition(0, 0, width, height);
        recordViewContent();
    }

    private void recordViewContent() {
        this.mRenderNode.setPosition(0, 0, this.right - this.left, this.bottom - this.top);
        RenderNode renderNode = this.mRenderNode;
        if (renderNode == null || this.mOriginalView == null) {
            return;
        }
        this.mOriginalView.draw(renderNode.beginRecording());
        this.mRenderNode.endRecording();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!canvas.isHardwareAccelerated() || this.mRenderNode == null) {
            return;
        }
        updateContent();
        canvas.drawRenderNode(this.mRenderNode);
    }

    public void updateContent() {
        recordViewContent();
        invalidate();
    }

    public static GhostView addGhost(View view, ViewGroup viewGroup) {
        if (view.getTag(R$id.miuix_ghost_view_tag) instanceof GhostView) {
            return (GhostView) view.getTag(R$id.miuix_ghost_view_tag);
        }
        GhostView ghostView = new GhostView(view.getContext(), view);
        view.setTag(R$id.miuix_ghost_view_tag, ghostView);
        int[] iArr = new int[2];
        view.getLocationInWindow(iArr);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view.getWidth(), view.getHeight());
        layoutParams.leftMargin = iArr[0];
        layoutParams.topMargin = iArr[1];
        layoutParams.gravity = 51;
        ghostView.setLayoutParams(layoutParams);
        viewGroup.addView(ghostView);
        ghostView.mOverlay = viewGroup;
        return ghostView;
    }

    public void removeGhost() {
        this.mOverlay.removeView(this);
        RenderNode renderNode = this.mRenderNode;
        if (renderNode != null) {
            renderNode.discardDisplayList();
            this.mRenderNode = null;
        }
        View view = this.mOriginalView;
        if (view != null) {
            view.setTag(R$id.miuix_ghost_view_tag, null);
            this.mOriginalView = null;
        }
    }
}
