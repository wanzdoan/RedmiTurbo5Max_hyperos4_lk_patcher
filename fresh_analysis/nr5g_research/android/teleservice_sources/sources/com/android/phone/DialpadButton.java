package com.android.phone;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.phone.utils.MaterialUtils;
import com.android.phone.utils.PressLightAnimator;
import miuix.animation.Folme;
import miuix.animation.FolmeEase;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;
import miuix.miuixbasewidget.widget.CallOvalImageButton;
import miuix.theme.token.GlassToken;

/* JADX INFO: loaded from: classes.dex */
public class DialpadButton extends FrameLayout {
    private ITouchStyle mBottomTouchStyle;
    private View mBottomView;
    private OnButtonClickListener mButtonClickListener;
    private final LinearLayout mContainer;
    private AnimConfig mIconDownConfig;
    private AnimConfig mIconUpConfig;
    private CallOvalImageButton mOvalButton;
    private PressLightAnimator mPressLightAnimator;
    private int mTopImageResId;
    private ITouchStyle mTopTouchStyle;
    private View mTopView;

    public interface OnButtonClickListener {
        void onButtonClick(View view);
    }

    public interface OnButtonHoverListener {
        boolean onButtonHover(View view, MotionEvent motionEvent);
    }

    public DialpadButton(Context context) {
        this(context, null);
    }

    public DialpadButton(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes;
        super(context, attributeSet);
        LinearLayout linearLayout = new LinearLayout(((FrameLayout) this).mContext);
        this.mContainer = linearLayout;
        linearLayout.setOrientation(1);
        linearLayout.setGravity(17);
        addBackgroundView(context);
        addView(linearLayout, new FrameLayout.LayoutParams(-1, -1));
        if (attributeSet == null || (typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DialpadButton)) == null) {
            return;
        }
        if (!TextUtils.isEmpty(typedArrayObtainStyledAttributes.getString(R.styleable.DialpadButton_topText))) {
            addTopTextView(context.getString(typedArrayObtainStyledAttributes.getResourceId(R.styleable.DialpadButton_topText, -1), Integer.valueOf(typedArrayObtainStyledAttributes.getInteger(R.styleable.DialpadButton_topTextOrigin, -1))));
        }
        this.mTopImageResId = typedArrayObtainStyledAttributes.getResourceId(R.styleable.DialpadButton_topImage, 0);
        addImageView(typedArrayObtainStyledAttributes.getDrawable(R.styleable.DialpadButton_topImage), false);
        addBottomTextView(typedArrayObtainStyledAttributes.getString(R.styleable.DialpadButton_bottomText));
        addImageView(typedArrayObtainStyledAttributes.getDrawable(R.styleable.DialpadButton_bottomImage), true);
        typedArrayObtainStyledAttributes.recycle();
    }

    private void addBackgroundView(Context context) {
        CallOvalImageButton callOvalImageButton = (CallOvalImageButton) LayoutInflater.from(context).inflate(R.layout.call_panel_button_view, (ViewGroup) null);
        this.mOvalButton = callOvalImageButton;
        callOvalImageButton.setMaterial(MaterialUtils.Glass_Colored_Extra_Thin_Dark);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dial_panel_button_height), context.getResources().getDimensionPixelSize(R.dimen.dial_panel_button_height));
        layoutParams.gravity = 17;
        addView(this.mOvalButton, layoutParams);
    }

    private void addTopTextView(String str) {
        TextView textView;
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (getId() == R.id.star) {
            textView = new DialpadTextView(((FrameLayout) this).mContext);
        } else {
            textView = new TextView(((FrameLayout) this).mContext);
        }
        textView.setImportantForAccessibility(2);
        textView.setText(str);
        textView.setTextColor(getResources().getColor(R.color.dialpad_button_top_text_color));
        if (getId() == R.id.star) {
            textView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.dialpad_button_star_size));
            textView.setFontFeatureSettings("ss08");
        } else if (getId() == R.id.pound) {
            textView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.dialpad_button_pound_size));
            textView.setFontFeatureSettings("ss08");
        } else {
            int dimensionPixelOffset = ((FrameLayout) this).mContext.getResources().getDimensionPixelOffset(R.dimen.dialpad_button_top_text_padding_bottom);
            textView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.dialpad_button_top_text_size));
            textView.setPadding(0, 0, 0, dimensionPixelOffset);
        }
        textView.setLineSpacing(0.0f, 1.0f);
        textView.setIncludeFontPadding(false);
        this.mContainer.addView(textView, new LinearLayout.LayoutParams(-2, -2));
        this.mTopView = textView;
        initIconPressEffect(textView, true);
    }

    private void addBottomTextView(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        TextView textView = new TextView(((FrameLayout) this).mContext);
        textView.setImportantForAccessibility(2);
        textView.setText(str);
        textView.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.dialpad_button_bottom_text_size));
        textView.setTextColor(getResources().getColor(R.color.dialpad_button_bottom_text_color));
        textView.setLineSpacing(0.0f, 1.0f);
        textView.setIncludeFontPadding(false);
        this.mContainer.addView(textView, new LinearLayout.LayoutParams(-2, -2));
        this.mBottomView = textView;
        initIconPressEffect(textView, false);
    }

    private void addImageView(Drawable drawable, boolean z) {
        if (drawable != null) {
            ImageView imageView = new ImageView(((FrameLayout) this).mContext);
            imageView.setImportantForAccessibility(2);
            imageView.setImageDrawable(drawable);
            if (z) {
                imageView.setPadding(0, (-((FrameLayout) this).mContext.getResources().getDimensionPixelOffset(R.dimen.dialpad_button_top_text_padding_bottom)) / 2, 0, 0);
                this.mBottomView = imageView;
                initIconPressEffect(imageView, false);
            } else {
                this.mTopView = imageView;
                this.mTopView.setPadding(0, 0, 0, ((FrameLayout) this).mContext.getResources().getDimensionPixelOffset(R.dimen.dialpad_button_top_image_padding_bottom));
                initIconPressEffect(this.mTopView, true);
            }
            this.mContainer.addView(imageView, new LinearLayout.LayoutParams(-2, -2));
        }
    }

    public void updateButtonSize(Context context) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = context.getResources().getDimensionPixelSize(R.dimen.dial_panel_button_height);
            setLayoutParams(layoutParams);
        }
        CallOvalImageButton callOvalImageButton = this.mOvalButton;
        if (callOvalImageButton != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) callOvalImageButton.getLayoutParams();
            layoutParams2.width = context.getResources().getDimensionPixelSize(R.dimen.dial_panel_button_height);
            layoutParams2.height = context.getResources().getDimensionPixelSize(R.dimen.dial_panel_button_height);
            this.mOvalButton.setLayoutParams(layoutParams2);
        }
    }

    public void updateButtonTextSize(Context context) {
        int i;
        View view = this.mTopView;
        if (view instanceof TextView) {
            ((TextView) view).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.dialpad_button_top_text_size));
        }
        View view2 = this.mTopView;
        if (view2 != null && (view2 instanceof ImageView) && (i = this.mTopImageResId) != 0) {
            ImageView imageView = (ImageView) view2;
            imageView.setImageResource(i);
            imageView.setPadding(0, 0, 0, ((FrameLayout) this).mContext.getResources().getDimensionPixelOffset(R.dimen.dialpad_button_top_image_padding_bottom));
        }
        View view3 = this.mBottomView;
        if (view3 instanceof TextView) {
            ((TextView) view3).setTextSize(0, getResources().getDimensionPixelSize(R.dimen.dialpad_button_bottom_text_size));
        }
    }

    private void initIconPressEffect(View view, boolean z) {
        if (view == null) {
            return;
        }
        ITouchStyle iTouchStyle = Folme.use(view).touch();
        iTouchStyle.setTint(0.0f, 0.0f, 0.0f, 0.0f);
        if (this.mIconDownConfig == null) {
            AnimConfig animConfig = new AnimConfig();
            this.mIconDownConfig = animConfig;
            animConfig.setEase(FolmeEase.spring(1.0f, 0.2f));
        }
        if (this.mIconUpConfig == null) {
            AnimConfig animConfig2 = new AnimConfig();
            this.mIconUpConfig = animConfig2;
            animConfig2.setEase(FolmeEase.spring(0.95f, 0.35f));
        }
        if (z) {
            this.mTopTouchStyle = iTouchStyle;
        } else {
            this.mBottomTouchStyle = iTouchStyle;
        }
    }

    private void updateIconPressEffect(MotionEvent motionEvent) {
        updateIconPressEffect(this.mTopTouchStyle, motionEvent);
        updateIconPressEffect(this.mBottomTouchStyle, motionEvent);
    }

    private void updateIconPressEffect(ITouchStyle iTouchStyle, MotionEvent motionEvent) {
        if (iTouchStyle == null || motionEvent == null) {
            return;
        }
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            iTouchStyle.touchDown(this.mIconDownConfig);
        } else if (actionMasked == 1 || actionMasked == 3) {
            iTouchStyle.touchUp(this.mIconUpConfig);
        }
    }

    private void startPressLightEffect(boolean z) {
        getPressLightAnimator().animate(z);
    }

    private void resetPressLightEffect() {
        PressLightAnimator pressLightAnimator = this.mPressLightAnimator;
        if (pressLightAnimator != null) {
            pressLightAnimator.reset();
        }
    }

    private PressLightAnimator getPressLightAnimator() {
        if (this.mPressLightAnimator == null) {
            this.mPressLightAnimator = new PressLightAnimator(new PressLightAnimator.TokenProvider() { // from class: com.android.phone.DialpadButton.1
                @Override // com.android.phone.utils.PressLightAnimator.TokenProvider
                public CallOvalImageButton getGlassView() {
                    return DialpadButton.this.mOvalButton;
                }

                @Override // com.android.phone.utils.PressLightAnimator.TokenProvider
                public GlassToken getNormalToken() {
                    return MaterialUtils.Glass_Bionic_Medium_Bold_Motion;
                }

                @Override // com.android.phone.utils.PressLightAnimator.TokenProvider
                public GlassToken getPressToken() {
                    return MaterialUtils.Glass_Bionic_Medium_Bold_Motion_Press;
                }
            });
        }
        return this.mPressLightAnimator;
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        updateIconPressEffect(motionEvent);
        int action = motionEvent.getAction();
        if (action == 0) {
            if (MaterialUtils.isHyperGlassEnable()) {
                startPressLightEffect(true);
            }
        } else if ((action == 1 || action == 3) && MaterialUtils.isHyperGlassEnable()) {
            startPressLightEffect(false);
        }
        boolean zDispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (!zDispatchTouchEvent) {
            ITouchStyle iTouchStyle = this.mTopTouchStyle;
            if (iTouchStyle != null) {
                iTouchStyle.touchUp(this.mIconUpConfig);
            }
            ITouchStyle iTouchStyle2 = this.mBottomTouchStyle;
            if (iTouchStyle2 != null) {
                iTouchStyle2.touchUp(this.mIconUpConfig);
            }
            if (MaterialUtils.isHyperGlassEnable()) {
                resetPressLightEffect();
                CallOvalImageButton callOvalImageButton = this.mOvalButton;
                if (callOvalImageButton != null) {
                    callOvalImageButton.setMaterial(MaterialUtils.Glass_Colored_Extra_Thin_Dark);
                }
            }
        }
        return zDispatchTouchEvent;
    }

    @Override // android.view.View
    public boolean performClick() {
        OnButtonClickListener onButtonClickListener = this.mButtonClickListener;
        if (onButtonClickListener != null) {
            onButtonClickListener.onButtonClick(this);
        }
        return super.performClick();
    }

    public void setButtonClickListener(final OnButtonClickListener onButtonClickListener) {
        this.mOvalButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.phone.DialpadButton.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                OnButtonClickListener onButtonClickListener2 = onButtonClickListener;
                if (onButtonClickListener2 != null) {
                    onButtonClickListener2.onButtonClick(DialpadButton.this);
                }
            }
        });
        this.mButtonClickListener = onButtonClickListener;
    }

    public void setOnButtonHoverListener(final OnButtonHoverListener onButtonHoverListener) {
        this.mOvalButton.setOnHoverListener(new View.OnHoverListener() { // from class: com.android.phone.DialpadButton.3
            @Override // android.view.View.OnHoverListener
            public boolean onHover(View view, MotionEvent motionEvent) {
                OnButtonHoverListener onButtonHoverListener2 = onButtonHoverListener;
                if (onButtonHoverListener2 != null) {
                    return onButtonHoverListener2.onButtonHover(DialpadButton.this, motionEvent);
                }
                return false;
            }
        });
    }
}
