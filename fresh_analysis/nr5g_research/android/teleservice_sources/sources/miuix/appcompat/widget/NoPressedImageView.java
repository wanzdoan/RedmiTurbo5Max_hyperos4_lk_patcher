package miuix.appcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/* JADX INFO: loaded from: classes2.dex */
class NoPressedImageView extends ImageView {
    public NoPressedImageView(Context context) {
        super(context);
    }

    public NoPressedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public NoPressedImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        super.setPressed(false);
    }
}
