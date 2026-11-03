package miuix.transition;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.property.ViewProperty;
import miuix.internal.util.ViewUtils;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ChangeBounds extends MiuixTransition {

    /* JADX INFO: renamed from: miuix.transition.ChangeBounds$1, reason: invalid class name */
    class AnonymousClass1 extends TransitionListener {
        final /* synthetic */ ChangeBounds this$0;
        final /* synthetic */ AnimConfig val$listener;

        AnonymousClass1(ChangeBounds changeBounds, AnimConfig animConfig) {
            this.val$listener = animConfig;
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onBegin(Object obj) {
            if (obj == "changebounds_end") {
                throw null;
            }
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            if (obj == "changebounds_end") {
                throw null;
            }
        }
    }

    /* JADX INFO: renamed from: miuix.transition.ChangeBounds$2, reason: invalid class name */
    class AnonymousClass2 extends TransitionListener {
        final /* synthetic */ ChangeBounds this$0;
        final /* synthetic */ AnimConfig val$listener;
        final /* synthetic */ ViewGroup val$parent;

        AnonymousClass2(ChangeBounds changeBounds, AnimConfig animConfig, ViewGroup viewGroup) {
            this.val$listener = animConfig;
            this.val$parent = viewGroup;
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            if (obj == "changebounds_end") {
                this.val$listener.removeListeners(this);
                this.val$parent.suppressLayout(false);
            }
        }
    }

    /* JADX INFO: renamed from: miuix.transition.ChangeBounds$3, reason: invalid class name */
    class AnonymousClass3 extends TransitionListener {
        final /* synthetic */ ChangeBounds this$0;
        final /* synthetic */ float val$alpha;
        final /* synthetic */ View val$copy;
        final /* synthetic */ AnimConfig val$listener;
        final /* synthetic */ ViewGroup val$sceneRoot;
        final /* synthetic */ View val$view;

        AnonymousClass3(ChangeBounds changeBounds, AnimConfig animConfig, ViewGroup viewGroup, View view, View view2, float f) {
            this.val$listener = animConfig;
            this.val$sceneRoot = viewGroup;
            this.val$copy = view;
            this.val$view = view2;
            this.val$alpha = f;
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            if (obj == "changebounds_end") {
                this.val$listener.removeListeners(this);
                this.val$sceneRoot.getOverlay().remove(this.val$copy);
                this.val$view.setAlpha(this.val$alpha);
            }
        }
    }

    public class ViewBounds {
        private WeakReference mView;
        int left = -1;
        int top = -1;
        int right = -1;
        int bottom = -1;

        ViewBounds(View view) {
            this.mView = new WeakReference(view);
        }

        public void setLeft(int i) {
            if (this.left != i) {
                this.left = i;
                setLeftTopRightBottom();
            }
        }

        int getLeft() {
            return this.left;
        }

        public void setRight(int i) {
            if (this.right != i) {
                this.right = i;
                setLeftTopRightBottom();
            }
        }

        int getRight() {
            return this.right;
        }

        public void setTop(int i) {
            if (this.top != i) {
                this.top = i;
                setLeftTopRightBottom();
            }
        }

        int getTop() {
            return this.top;
        }

        public void setBottom(int i) {
            if (this.bottom != i) {
                this.bottom = i;
                setLeftTopRightBottom();
            }
        }

        int getBottom() {
            return this.bottom;
        }

        private void setLeftTopRightBottom() {
            View view = (View) this.mView.get();
            if (view != null) {
                if (view instanceof TextView) {
                    view.measure(View.MeasureSpec.makeMeasureSpec(this.right - this.left, 1073741824), View.MeasureSpec.makeMeasureSpec(this.bottom - this.top, 1073741824));
                    view.layout(this.left, this.top, this.right, this.bottom);
                } else {
                    ViewUtils.setLeftTopRightBottom(view, this.left, this.top, this.right, this.bottom);
                }
            }
        }
    }

    public class LeftProperty extends ViewProperty implements IIntValueProperty {
        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return 0.0f;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
        }

        public LeftProperty() {
            super("left");
        }

        @Override // miuix.animation.property.IIntValueProperty
        public void setIntValue(ViewBounds viewBounds, int i) {
            viewBounds.setLeft(i);
        }

        @Override // miuix.animation.property.IIntValueProperty
        public int getIntValue(ViewBounds viewBounds) {
            return viewBounds.getLeft();
        }
    }

    public class RightProperty extends ViewProperty implements IIntValueProperty {
        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return 0.0f;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
        }

        public RightProperty() {
            super("right");
        }

        @Override // miuix.animation.property.IIntValueProperty
        public void setIntValue(ViewBounds viewBounds, int i) {
            viewBounds.setRight(i);
        }

        @Override // miuix.animation.property.IIntValueProperty
        public int getIntValue(ViewBounds viewBounds) {
            return viewBounds.getRight();
        }
    }

    public class TopProperty extends ViewProperty implements IIntValueProperty {
        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return 0.0f;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
        }

        public TopProperty() {
            super("top");
        }

        @Override // miuix.animation.property.IIntValueProperty
        public void setIntValue(ViewBounds viewBounds, int i) {
            viewBounds.setTop(i);
        }

        @Override // miuix.animation.property.IIntValueProperty
        public int getIntValue(ViewBounds viewBounds) {
            return viewBounds.getTop();
        }
    }

    public class BottomProperty extends ViewProperty implements IIntValueProperty {
        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return 0.0f;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
        }

        public BottomProperty() {
            super("bottom");
        }

        @Override // miuix.animation.property.IIntValueProperty
        public void setIntValue(ViewBounds viewBounds, int i) {
            viewBounds.setBottom(i);
        }

        @Override // miuix.animation.property.IIntValueProperty
        public int getIntValue(ViewBounds viewBounds) {
            return viewBounds.getBottom();
        }
    }
}
