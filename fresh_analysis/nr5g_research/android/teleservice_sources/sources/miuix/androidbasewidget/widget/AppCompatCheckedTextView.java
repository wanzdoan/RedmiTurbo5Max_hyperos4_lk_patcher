package miuix.androidbasewidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import miuix.androidbasewidget.R$styleable;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AppCompatCheckedTextView extends androidx.appcompat.widget.AppCompatCheckedTextView {
    public AppCompatCheckedTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.CheckedTextView, i, 0);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(R$styleable.CheckedTextView_checkMarkCompat, 0);
        if (resourceId != 0) {
            setCheckMarkDrawable(resourceId);
        }
        typedArrayObtainStyledAttributes.recycle();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        View view = (View) getParent();
        boolean z = view instanceof ListView;
        boolean z2 = view != null && view.getClass().getSimpleName().contains("RecyclerView");
        if (!z && !z2) {
            Folme.useAt(this).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(this, new AnimConfig[0]);
        } else {
            if (isClickable()) {
                return;
            }
            if (getForeground() != null) {
                setOnHoverListener(new View.OnHoverListener() { // from class: miuix.androidbasewidget.widget.AppCompatCheckedTextView$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnHoverListener
                    public final boolean onHover(View view2, MotionEvent motionEvent) {
                        return AppCompatCheckedTextView.$r8$lambda$bXvqIlYVAsqXBQFhoVyHIS0sXt0(view2, motionEvent);
                    }
                });
            } else {
                Folme.useAt(this).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(this, new AnimConfig[0]);
            }
        }
    }

    public static /* synthetic */ boolean $r8$lambda$bXvqIlYVAsqXBQFhoVyHIS0sXt0(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 9) {
            view.setHovered(true);
            return false;
        }
        if (motionEvent.getAction() == 10) {
            view.setHovered(false);
        }
        return false;
    }
}
