package com.miui.support.drawable;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import com.miui.support.cardview.R$styleable;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes.dex */
public class CardDrawable extends CardStateDrawable {
    private int mCardColor;
    private int mCardPaddingBottom;
    private int mCardPaddingLeft;
    private int mCardPaddingRight;
    private int mCardPaddingTop;
    private int mCardRadius;
    private CardState mCardState;
    private boolean mIsSupportOutline;
    private final Rect mPadding;
    private final Paint mPaint;

    @Override // com.miui.support.drawable.CardStateDrawable, android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    public CardDrawable() {
        this.mPaint = new Paint();
        this.mPadding = new Rect();
        this.mIsSupportOutline = true;
        this.mCardState = new CardState();
    }

    public CardDrawable(CardState cardState, Resources resources) {
        super(cardState, resources);
        this.mPaint = new Paint();
        this.mPadding = new Rect();
        this.mIsSupportOutline = true;
        this.mCardState = new CardState(cardState);
        initCardDrawable(cardState);
        cacheCardDrawable();
    }

    private void updateParentState() {
        CardState cardState = this.mCardState;
        cardState.mTintColor = this.mTintColor;
        cardState.mRadius = this.mRadius;
        cardState.mNormalAlpha = this.mNormalAlpha;
        cardState.mPressedAlpha = this.mPressedAlpha;
        cardState.mHoveredAlpha = this.mHoveredAlpha;
        cardState.mFocusedAlpha = this.mFocusedAlpha;
        cardState.mActivatedAlpha = this.mActivatedAlpha;
        cardState.mCheckedAlpha = this.mCheckedAlpha;
        cardState.mHoveredCheckedAlpha = this.mHoveredCheckedAlpha;
    }

    private void cacheCardDrawable() {
        CardState cardState = this.mCardState;
        cardState.color = this.mCardColor;
        cardState.radius = this.mCardRadius;
        cardState.paddingLeft = this.mCardPaddingLeft;
        cardState.paddingTop = this.mCardPaddingTop;
        cardState.paddingRight = this.mCardPaddingRight;
        cardState.paddingBottom = this.mCardPaddingBottom;
        cardState.radiusMode = this.mCardRadiusMode;
        cardState.isSupportOutline = this.mIsSupportOutline;
        updateParentState();
    }

    @Override // com.miui.support.drawable.CardStateDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (isVisible()) {
            this.mPath.reset();
            this.mPath.addRoundRect(this.mRect, this.mAllRadii, Path.Direction.CW);
            canvas.drawPath(this.mPath, this.mPaint);
        }
        super.draw(canvas);
    }

    @Override // android.graphics.drawable.Drawable
    public boolean getPadding(Rect rect) {
        rect.set(this.mPadding);
        return true;
    }

    @Override // com.miui.support.drawable.CardStateDrawable, android.graphics.drawable.Drawable
    public void inflate(Resources resources, XmlPullParser xmlPullParser, AttributeSet attributeSet, Resources.Theme theme) throws XmlPullParserException, IOException {
        TypedArray typedArrayObtainAttributes;
        super.inflate(resources, xmlPullParser, attributeSet, theme);
        if (theme != null) {
            typedArrayObtainAttributes = theme.obtainStyledAttributes(attributeSet, R$styleable.CardDrawable, 0, 0);
        } else {
            typedArrayObtainAttributes = resources.obtainAttributes(attributeSet, R$styleable.CardDrawable);
        }
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mCardColor = typedArrayObtainAttributes.getColor(R$styleable.CardDrawable_backgroundColor, -16777216);
        this.mCardPaddingLeft = typedArrayObtainAttributes.getDimensionPixelSize(R$styleable.CardDrawable_paddingLeft, 0);
        this.mCardPaddingRight = typedArrayObtainAttributes.getDimensionPixelSize(R$styleable.CardDrawable_paddingRight, 0);
        this.mCardPaddingTop = typedArrayObtainAttributes.getDimensionPixelSize(R$styleable.CardDrawable_paddingTop, 0);
        this.mCardPaddingBottom = typedArrayObtainAttributes.getDimensionPixelSize(R$styleable.CardDrawable_paddingBottom, 0);
        this.mCardRadius = typedArrayObtainAttributes.getDimensionPixelSize(R$styleable.CardDrawable_cardRadius, 0);
        this.mCardRadiusMode = typedArrayObtainAttributes.getInteger(R$styleable.CardDrawable_radiusMode, 0);
        this.mIsSupportOutline = typedArrayObtainAttributes.getBoolean(R$styleable.CardDrawable_supportOutline, true);
        this.mPadding.set(this.mCardPaddingLeft, this.mCardPaddingTop, this.mCardPaddingRight, this.mCardPaddingBottom);
        this.mPaint.setColor(this.mCardColor);
        setRadiusMode(this.mCardRadius, this.mCardRadiusMode);
        cacheCardDrawable();
        typedArrayObtainAttributes.recycle();
    }

    private void initCardDrawable(CardState cardState) {
        this.mPaint.setStyle(Paint.Style.FILL);
        this.mCardColor = cardState.color;
        int i = cardState.paddingLeft;
        this.mCardPaddingLeft = i;
        int i2 = cardState.paddingRight;
        this.mCardPaddingRight = i2;
        int i3 = cardState.paddingTop;
        this.mCardPaddingTop = i3;
        int i4 = cardState.paddingBottom;
        this.mCardPaddingBottom = i4;
        this.mCardRadius = cardState.radius;
        this.mCardRadiusMode = cardState.radiusMode;
        this.mIsSupportOutline = cardState.isSupportOutline;
        this.mPadding.set(i, i3, i2, i4);
        this.mPaint.setColor(this.mCardColor);
        setRadiusMode(this.mCardRadius, this.mCardRadiusMode);
    }

    @Override // com.miui.support.drawable.CardStateDrawable, android.graphics.drawable.Drawable
    public Drawable.ConstantState getConstantState() {
        return this.mCardState;
    }

    @Override // android.graphics.drawable.Drawable
    public void getOutline(Outline outline) {
        if (!this.mIsSupportOutline) {
            super.getOutline(outline);
        } else {
            outline.setPath(this.mPath);
        }
    }

    public final class CardState extends CardStateDrawable.AlphaBlendingState {
        int color;
        boolean isSupportOutline;
        int paddingBottom;
        int paddingLeft;
        int paddingRight;
        int paddingTop;
        int radius;
        int radiusMode;

        @Override // com.miui.support.drawable.CardStateDrawable.AlphaBlendingState, android.graphics.drawable.Drawable.ConstantState
        public int getChangingConfigurations() {
            return 0;
        }

        public CardState() {
            this.isSupportOutline = true;
        }

        CardState(CardState cardState) {
            super(cardState);
            this.isSupportOutline = true;
            this.color = cardState.color;
            this.paddingLeft = cardState.paddingLeft;
            this.paddingRight = cardState.paddingRight;
            this.paddingTop = cardState.paddingTop;
            this.paddingBottom = cardState.paddingBottom;
            this.radius = cardState.radius;
            this.radiusMode = cardState.radiusMode;
            this.isSupportOutline = cardState.isSupportOutline;
        }

        @Override // com.miui.support.drawable.CardStateDrawable.AlphaBlendingState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new CardDrawable();
        }

        @Override // com.miui.support.drawable.CardStateDrawable.AlphaBlendingState, android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            return new CardDrawable(new CardState(this), resources);
        }
    }
}
