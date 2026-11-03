package miuix.popupwidget.widget;

import android.view.View;
import miuix.view.ITransformableView;

/* JADX INFO: loaded from: classes2.dex */
public class PopupAnimHelper {
    private final View mAnchorView;
    private final IPopupAnim mPopupAnim;

    /* JADX WARN: Multi-variable type inference failed */
    public PopupAnimHelper(View view, View view2, ITransformableView iTransformableView) {
        this.mAnchorView = view2;
        if (view2 instanceof ITransformableView) {
            this.mPopupAnim = new TransformAnimation((ITransformableView) view2, iTransformableView);
        } else if (view2 instanceof IPopupAnimView) {
            this.mPopupAnim = new ArcAnimation(view);
        } else {
            this.mPopupAnim = new DirectionAnimation(view);
        }
        initListener();
    }

    public PopupAnimHelper(View view, View view2) {
        this.mAnchorView = view2;
        if (view2 instanceof IPopupAnimView) {
            this.mPopupAnim = new ArcAnimation(view);
        } else {
            this.mPopupAnim = new DirectionAnimation(view);
        }
        initListener();
    }

    private void initListener() {
        this.mPopupAnim.addPopupAnimationListener(new PopupAnimationListener() { // from class: miuix.popupwidget.widget.PopupAnimHelper.1
            @Override // miuix.popupwidget.widget.PopupAnimationListener, miuix.popupwidget.widget.IPopupAnimationListener
            public void onForwardAnimationStart() {
                if (PopupAnimHelper.this.mAnchorView instanceof IPopupAnimView) {
                    ((IPopupAnimView) PopupAnimHelper.this.mAnchorView).applyAnchorTransition();
                }
            }

            @Override // miuix.popupwidget.widget.PopupAnimationListener, miuix.popupwidget.widget.IPopupAnimationListener
            public void onReverseAnimationEnd() {
                if (PopupAnimHelper.this.mAnchorView instanceof IPopupAnimView) {
                    ((IPopupAnimView) PopupAnimHelper.this.mAnchorView).revertAnchorTransition();
                }
            }
        });
    }

    public void setGravity(int i) {
        this.mPopupAnim.setGravity(i);
    }

    public void show() {
        this.mPopupAnim.showWithAnim();
    }

    public void dismiss() {
        this.mPopupAnim.dismissWithAnim();
    }

    public void update(int i) {
        this.mPopupAnim.update(i);
    }

    public int getAnimGravity() {
        IPopupAnim iPopupAnim = this.mPopupAnim;
        if (iPopupAnim instanceof DirectionAnimation) {
            return ((DirectionAnimation) iPopupAnim).getAnimGravity();
        }
        if (iPopupAnim instanceof ArcAnimation) {
            return ((ArcAnimation) iPopupAnim).getAnimGravity();
        }
        return 0;
    }

    public void setBlurView(View view) {
        this.mPopupAnim.setBlurView(view);
    }

    public void setBlurEnabled(boolean z) {
        this.mPopupAnim.setBlurEnabled(z);
    }

    public void resetFromContentView() {
        IPopupAnim iPopupAnim = this.mPopupAnim;
        if (iPopupAnim instanceof TransformAnimation) {
            ((TransformAnimation) iPopupAnim).resetFromContentView();
        }
    }

    public void updateAnimationOptions() {
        IPopupAnim iPopupAnim = this.mPopupAnim;
        if (iPopupAnim instanceof TransformAnimation) {
            ((TransformAnimation) iPopupAnim).updateAnimationOptions();
        }
    }

    public void setCrossWindow(boolean z) {
        IPopupAnim iPopupAnim = this.mPopupAnim;
        if (iPopupAnim instanceof TransformAnimation) {
            ((TransformAnimation) iPopupAnim).setCrossWindow(z);
        }
    }

    public void addPopupAnimationListener(IPopupAnimationListener iPopupAnimationListener) {
        this.mPopupAnim.addPopupAnimationListener(iPopupAnimationListener);
    }

    public void removePopupAnimationListener(IPopupAnimationListener iPopupAnimationListener) {
        this.mPopupAnim.removePopupAnimationListener(iPopupAnimationListener);
    }
}
