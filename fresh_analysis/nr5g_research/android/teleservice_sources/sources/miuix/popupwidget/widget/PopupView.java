package miuix.popupwidget.widget;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.activity.ComponentActivity;
import androidx.activity.OnBackPressedCallback;
import androidx.core.view.ViewCompat;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import miuix.animation.utils.CommonUtils;
import miuix.core.util.EnvStateManager;
import miuix.core.util.HyperBloomStrokeUtils;
import miuix.core.util.HyperMaterialUtils;
import miuix.core.util.MaterialConfig;
import miuix.core.util.MaterialDayNightConfig;
import miuix.core.util.MiShadowUtils;
import miuix.core.util.MiuiBlurUtils;
import miuix.core.util.MiuixUIUtils;
import miuix.core.util.WindowBaseInfo;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.ViewUtils;
import miuix.popupwidget.R$attr;
import miuix.popupwidget.R$color;
import miuix.popupwidget.R$dimen;
import miuix.popupwidget.R$id;
import miuix.popupwidget.R$layout;
import miuix.popupwidget.R$string;
import miuix.popupwidget.internal.strategy.IPopupWindowStrategy;
import miuix.popupwidget.internal.strategy.PopupWindowSpec;
import miuix.popupwidget.internal.strategy.PopupWindowStrategy;
import miuix.smooth.SmoothFrameLayout2;
import miuix.springback.view.SpringBackLayout;
import miuix.theme.token.BloomStrokeToken;
import miuix.theme.token.ColorBlendToken;
import miuix.theme.token.MaterialDayNightToken;
import miuix.theme.token.MaterialToken;
import miuix.theme.token.ShadowToken;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;
import miuix.view.ITransformableView;

/* JADX INFO: loaded from: classes2.dex */
public class PopupView extends FrameLayout {
    private static final boolean MENU_MATERIAL_ENABLED;
    public static final MaterialToken PopupView_Glass_Dark;
    public static final MaterialToken PopupView_Glass_Light;
    public static final MaterialDayNightToken PopupView_Glass_Material;
    private ListAdapter mAdapter;
    ViewTreeObserver.OnGlobalLayoutListener mAnchorGlobalLayoutListener;
    private WeakReference mAnchorView;
    private SpringBackLayout mAnimContainerView;
    private PopupAnimHelper mAnimHelper;
    private OnBackPressedCallback mBackPressedCallback;
    private View mContentView;
    private Context mContext;
    private boolean mDetachAnchorLayoutFlag;
    private boolean mDismissOnConfigurationChanged;
    private int mElevation;
    private int mElevationExtra;
    private WeakReference mFenceDecor;
    private boolean mHasShadow;
    private boolean mHideSoftInputEnabled;
    protected boolean mIgnoreAnchorVisibility;
    private boolean mInterceptTouchEvent;
    private boolean mIsShowing;
    private boolean mIsTransitioningToDismiss;
    private boolean mMaterialEnabled;
    private int mMaxAllowedHeight;
    private int mMaxAllowedWidth;
    private View mMenuLayer;
    private int mMinAllowedWidth;
    private int mMinSafeInsetDimen;
    private OnDismissListener mOnDismissListener;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private OnPreDismissListener mOnPreDismissListener;
    private final ViewOutlineProvider mOutlineProvider;
    private boolean mPassBlurEnabled;
    private PopupWindowSpec mPopupWindowSpec;
    protected IPopupWindowStrategy mPopupWindowStrategy;
    private float mRadius;
    private boolean mSelfBlurEnabled;
    private int mShadowColor;
    private CharSequence mShowDescription;
    private final ITransformableView mTransformableDelegate;
    private int mUserAnimationGravity;
    private int mVerticalPadding;

    public interface OnDismissListener {
        void onDismiss();
    }

    public interface OnPreDismissListener {
        void onPreDismiss();
    }

    static {
        boolean zEqualsIgnoreCase;
        MaterialToken materialTokenBuild = new MaterialToken.Builder(30, "popupview-glass", "light").setColorBlend(ColorBlendToken.Pured_Thin_Glass_Light).setMaskBlur(60).setBloomStroke(BloomStrokeToken.Glass_Stroke_Small_Light).setShadow(ShadowToken.Regular).build();
        PopupView_Glass_Light = materialTokenBuild;
        MaterialToken materialTokenBuild2 = new MaterialToken.Builder(30, "popupview-glass", "dark").setColorBlend(ColorBlendToken.Pured_Thin_Glass_Dark).setMaskBlur(60).setBloomStroke(BloomStrokeToken.Glass_Stroke_Small_Dark).setShadow(ShadowToken.Regular).build();
        PopupView_Glass_Dark = materialTokenBuild2;
        PopupView_Glass_Material = new MaterialDayNightToken(materialTokenBuild, materialTokenBuild2);
        try {
            zEqualsIgnoreCase = "true".equalsIgnoreCase(CommonUtils.readProp("miuix.material.menu"));
        } catch (Exception unused) {
            zEqualsIgnoreCase = false;
        }
        MENU_MATERIAL_ENABLED = zEqualsIgnoreCase;
    }

    public PopupView(Context context) {
        this(context, null);
    }

    public PopupView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PopupView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mHasShadow = true;
        this.mUserAnimationGravity = 0;
        this.mHideSoftInputEnabled = true;
        this.mDetachAnchorLayoutFlag = false;
        this.mPassBlurEnabled = false;
        this.mIgnoreAnchorVisibility = false;
        this.mInterceptTouchEvent = false;
        this.mOutlineProvider = new ViewOutlineProvider() { // from class: miuix.popupwidget.widget.PopupView.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), PopupView.this.mRadius);
            }
        };
        this.mTransformableDelegate = new ITransformableView() { // from class: miuix.popupwidget.widget.PopupView.2
            @Override // miuix.view.ITransformableView
            public View getContainerView() {
                return PopupView.this.mContentView;
            }

            @Override // miuix.view.ITransformableView
            public View getContentView() {
                return ((ViewGroup) PopupView.this.mContentView).getChildAt(0);
            }

            @Override // miuix.view.ITransformableView
            public float getCornerRadius() {
                return PopupView.this.getResources().getDimension(R$dimen.miuix_appcompat_drop_down_menu_radius);
            }

            @Override // miuix.view.ITransformableView
            public View getElevationView() {
                return PopupView.this.mContentView;
            }

            @Override // miuix.view.ITransformableView
            public void setCornerRadius(float f) {
                PopupView.this.mRadius = f;
                if (PopupView.this.mContentView instanceof SmoothFrameLayout2) {
                    ((SmoothFrameLayout2) PopupView.this.mContentView).setCornerRadius(f);
                }
                PopupView.this.invalidateOutline();
            }

            @Override // miuix.view.ITransformableView
            public void applyTransformElevation(View view) {
                if (PopupView.this.shouldSetElevation()) {
                    boolean z = MiShadowUtils.SUPPORT_MI_SHADOW;
                    PopupView popupView = PopupView.this;
                    if (z) {
                        float f = popupView.mContentView.getContext().getResources().getDisplayMetrics().density;
                        MiShadowUtils.setMiShadow(PopupView.this.mContentView, PopupView.this.mShadowColor, 0.0f * f, f * 26.0f, PopupView.this.mElevation);
                    } else {
                        popupView.mContentView.setElevation(PopupView.this.mElevation);
                        PopupView popupView2 = PopupView.this;
                        popupView2.setPopupShadowAlpha(popupView2.mContentView);
                    }
                }
            }
        };
        this.mAnchorGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: miuix.popupwidget.widget.PopupView.8
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                PopupView popupView = PopupView.this;
                popupView.updatePopupWindowSpec(popupView.mPopupWindowSpec);
                View anchor = PopupView.this.getAnchor();
                if (anchor != null) {
                    PopupView.this.updateLocation(anchor);
                }
            }
        };
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R$layout.miuix_appcompat_popup_menu_view, (ViewGroup) this, true);
        this.mShowDescription = context.getResources().getString(R$string.miuix_popup_show_description);
        this.mContext = context;
        this.mPopupWindowSpec = new PopupWindowSpec();
        this.mPopupWindowStrategy = new PopupWindowStrategy();
        updateDisplayConfig(null);
        applyContentView();
        applyShadow();
        setClipChildren(false);
        setClipToOutline(false);
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    private void applyContentView() {
        this.mMenuLayer = findViewById(R$id.miuix_popupwidget_popup_offset_anim_container);
        this.mContentView = findViewById(R$id.content_view);
        setOnClickListener(new View.OnClickListener() { // from class: miuix.popupwidget.widget.PopupView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) throws Exception {
                this.f$0.dismiss();
            }
        });
        this.mContentView.setBackground(AttributeResolver.resolveDrawable(this.mContext, R$attr.immersionWindowBackground));
        this.mRadius = this.mContext.getResources().getDimension(R$dimen.miuix_appcompat_drop_down_menu_radius);
        this.mVerticalPadding = this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_popup_window_vertical_padding);
        this.mContentView.setOutlineProvider(this.mOutlineProvider);
    }

    private void applyShadow() {
        float f = this.mContext.getResources().getDisplayMetrics().density;
        this.mShadowColor = this.mContext.getResources().getColor(R$color.miuix_appcompat_drop_down_menu_spot_shadow_color);
        if (MiShadowUtils.SUPPORT_MI_SHADOW) {
            this.mElevation = (int) (f * 32.0f);
        } else {
            this.mElevation = AttributeResolver.resolveDimensionPixelSize(this.mContext, R$attr.popupWindowElevation);
            this.mElevationExtra = this.mContext.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_menu_popup_extra_elevation);
        }
        this.mMenuLayer.setElevation(this.mElevation + this.mElevationExtra);
    }

    public void showWithAnchor() {
        ViewGroup viewGroup = (ViewGroup) getDecorView();
        ViewGroup viewLayer = getViewLayer();
        if (!prepareShow(getAnchor()) || viewGroup == null || viewLayer == null) {
            return;
        }
        int xInWindow = this.mPopupWindowStrategy.getXInWindow(this.mPopupWindowSpec);
        int yInWindow = this.mPopupWindowStrategy.getYInWindow(this.mPopupWindowSpec);
        PopupWindowSpec popupWindowSpec = this.mPopupWindowSpec;
        int i = popupWindowSpec.mFinalPopupWidth;
        int i2 = popupWindowSpec.mFinalPopupHeight;
        viewLayer.addView(this, new FrameLayout.LayoutParams(-1, -1));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mContentView.getLayoutParams();
        layoutParams.width = i;
        layoutParams.height = i2;
        this.mMenuLayer.setTranslationX(xInWindow - 128);
        this.mMenuLayer.setTranslationY(yInWindow - 128);
        Rect rect = new Rect();
        Rect rect2 = this.mPopupWindowSpec.mDecorViewBounds;
        int i3 = rect2.left;
        int i4 = rect2.top;
        rect.set(0, 0, i, i2);
        rect.offsetTo(xInWindow + i3, yInWindow + i4);
        if (!isShowing()) {
            HapticCompat.performHapticFeedback(this, HapticFeedbackConstants.MIUI_BUTTON_SMALL, HapticFeedbackConstants.MIUI_POPUP_NORMAL);
        }
        int iComputeGravity = this.mUserAnimationGravity;
        if (iComputeGravity == 0) {
            PopupWindowSpec popupWindowSpec2 = this.mPopupWindowSpec;
            iComputeGravity = computeGravity(popupWindowSpec2.mAnchorViewBounds, rect, popupWindowSpec2.mGravity, viewGroup.getLayoutDirection());
        }
        showWithAnim(iComputeGravity);
        ViewCompat.setAccessibilityPaneTitle(this.mContentView, this.mShowDescription);
        this.mIsShowing = true;
        registerBackPressedCallback();
        requestFocus();
    }

    public boolean prepareShow(View view) {
        if (view == null) {
            Log.e("PopupView", "show: anchor is null");
            return false;
        }
        boolean localVisibleRect = view.getLocalVisibleRect(new Rect());
        if (!this.mIgnoreAnchorVisibility && !localVisibleRect) {
            return false;
        }
        this.mAnchorView = new WeakReference(view);
        updatePopupWindowSpec(this.mPopupWindowSpec);
        PopupWindowSpec popupWindowSpec = this.mPopupWindowSpec;
        if (popupWindowSpec.mMinWidth <= 0 || popupWindowSpec.mMaxWidth <= 0 || popupWindowSpec.mMaxHeight <= 0) {
            return false;
        }
        prepareElevation();
        prepareContentView();
        computePopupContentSize();
        prepareSoftInputMethod(view.getWindowToken());
        prepareHyperMaterial();
        prepareAnim();
        return true;
    }

    private void prepareContentView() {
        final ListView listView = (ListView) findViewById(R.id.list);
        this.mAnimContainerView = (SpringBackLayout) this.mContentView.findViewById(R$id.miuix_popupwidget_popup_anim_container);
        this.mContentView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: miuix.popupwidget.widget.PopupView.3
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                PopupView popupView = PopupView.this;
                boolean zIsNeedScroll = popupView.mPopupWindowStrategy.isNeedScroll(i4 - i2, popupView.mPopupWindowSpec);
                PopupView.this.mAnimContainerView.setEnabled(zIsNeedScroll);
                ListView listView2 = listView;
                if (listView2 != null) {
                    listView2.setVerticalScrollBarEnabled(zIsNeedScroll);
                }
            }
        });
        if (listView != null) {
            listView.setAdapter(this.mAdapter);
            listView.setOnTouchListener(new AnonymousClass4(listView));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: miuix.popupwidget.widget.PopupView$$ExternalSyntheticLambda2
                @Override // android.widget.AdapterView.OnItemClickListener
                public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                    PopupView.$r8$lambda$Xi4rjntVe7PvtlLUnIxNJ34izKw(this.f$0, listView, adapterView, view, i, j);
                }
            });
        }
    }

    /* JADX INFO: renamed from: miuix.popupwidget.widget.PopupView$4, reason: invalid class name */
    class AnonymousClass4 implements View.OnTouchListener {
        int lastIndex = -1;
        final /* synthetic */ ListView val$listView;

        AnonymousClass4(ListView listView) {
            this.val$listView = listView;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(final View view, MotionEvent motionEvent) {
            int firstVisiblePosition;
            int i;
            View childAt;
            int iPointToPosition = this.val$listView.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action == 1 || action == 3 || action == 6) {
                    this.lastIndex = -1;
                    this.val$listView.postDelayed(new Runnable() { // from class: miuix.popupwidget.widget.PopupView$4$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            PopupView.AnonymousClass4.$r8$lambda$EoGEnTs1zXnUWEQqT2qrxoVohZ8(view);
                        }
                    }, ViewConfiguration.getPressedStateDuration());
                }
            } else if (iPointToPosition != -1 && (firstVisiblePosition = iPointToPosition - this.val$listView.getFirstVisiblePosition()) != (i = this.lastIndex)) {
                if (i != -1 && (childAt = this.val$listView.getChildAt(i)) != null) {
                    childAt.setPressed(false);
                }
                this.val$listView.getChildAt(firstVisiblePosition).setPressed(true);
                this.lastIndex = firstVisiblePosition;
            }
            return false;
        }

        public static /* synthetic */ void $r8$lambda$EoGEnTs1zXnUWEQqT2qrxoVohZ8(View view) {
            if (view instanceof ViewGroup) {
                try {
                    int childCount = ((ViewGroup) view).getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        ((ViewGroup) view).getChildAt(i).setPressed(false);
                    }
                } catch (Exception e) {
                    Log.e("PopupView", "list onTouch error " + e);
                }
            }
        }
    }

    public static /* synthetic */ void $r8$lambda$Xi4rjntVe7PvtlLUnIxNJ34izKw(PopupView popupView, ListView listView, AdapterView adapterView, View view, int i, long j) {
        popupView.getClass();
        int headerViewsCount = i - listView.getHeaderViewsCount();
        if (popupView.mOnItemClickListener == null || headerViewsCount < 0 || headerViewsCount >= popupView.mAdapter.getCount()) {
            return;
        }
        popupView.mOnItemClickListener.onItemClick(adapterView, view, headerViewsCount, j);
    }

    private void prepareSoftInputMethod(IBinder iBinder) {
        if (this.mHideSoftInputEnabled) {
            ((InputMethodManager) this.mContext.getApplicationContext().getSystemService("input_method")).hideSoftInputFromWindow(iBinder, 0);
        }
    }

    private void prepareHyperMaterial() {
        if (isMaterialEnabled()) {
            applyMaterialEffects();
        } else {
            clearMaterialEffects();
        }
    }

    private void applyMaterialEffects() {
        setContentViewBackgroundTransparent();
        MaterialConfig materialConfig = MaterialDayNightConfig.create(PopupView_Glass_Material).get(MiuixUIUtils.isLightTheme(this.mContext));
        if (materialConfig != null) {
            float f = this.mContext.getResources().getDisplayMetrics().density;
            MiuiBlurUtils.setPassWindowBlurEnabled(this.mContentView, true);
            applyBlurEffect(materialConfig, f);
            applyColorBlend(materialConfig);
            applyBloomStroke(materialConfig);
            applyShadow(materialConfig);
        }
    }

    private void setContentViewBackgroundTransparent() {
        Drawable background = this.mContentView.getBackground();
        if (background != null) {
            background.setAlpha(0);
        }
    }

    private void applyBlurEffect(MaterialConfig materialConfig, float f) {
        int i;
        if (materialConfig.getBlurConfig() == null || (i = materialConfig.getBlurConfig().blurRadius) <= 0) {
            return;
        }
        MiuiBlurUtils.setBackgroundOnlyBlur(this.mMenuLayer, (int) ((i * f) + 0.5f));
    }

    private void applyColorBlend(MaterialConfig materialConfig) {
        MiuiBlurUtils.setViewBlurMode(this.mContentView, 1);
        if (materialConfig.getColorBlendConfig() != null) {
            MiuiBlurUtils.setBackgroundBlendConfig(this.mContentView, materialConfig.getColorBlendConfig().blendColors, materialConfig.getColorBlendConfig().blendModes);
        }
    }

    private void applyBloomStroke(MaterialConfig materialConfig) {
        if (materialConfig.getBloomStrokeConfig() != null) {
            HyperBloomStrokeUtils.setBloomStrokeConfig(this.mContentView, materialConfig.getBloomStrokeConfig());
        }
    }

    private void applyShadow(MaterialConfig materialConfig) {
        if (materialConfig.getShadowConfig() != null) {
            MiShadowUtils.setShadowConfig(this.mContentView, materialConfig.getShadowConfig());
        }
    }

    private void clearMaterialEffects() {
        MiuiBlurUtils.setPassWindowBlurEnabled(this.mContentView, false);
        MiuiBlurUtils.clearBackgroundBlur(this.mMenuLayer);
        MiuiBlurUtils.setViewBlurMode(this.mContentView, 0);
        MiuiBlurUtils.clearBackgroundBlendConfig(this.mContentView);
        restoreDefaultBackground();
        prepareElevation();
    }

    private void restoreDefaultBackground() {
        Drawable background = this.mContentView.getBackground();
        if (background != null) {
            background.setAlpha(255);
        }
    }

    private void prepareAnim() {
        View anchor = getAnchor();
        if (this.mAnimHelper != null || anchor == null) {
            return;
        }
        PopupAnimHelper popupAnimHelper = new PopupAnimHelper(this.mContentView, anchor, this.mTransformableDelegate);
        this.mAnimHelper = popupAnimHelper;
        popupAnimHelper.setBlurEnabled(isSelfBlurEnabled());
    }

    public void dismiss() throws Exception {
        unregisterBackPressedCallback();
        if (isShowing()) {
            clearAccessibilityFocus();
            OnPreDismissListener onPreDismissListener = this.mOnPreDismissListener;
            if (onPreDismissListener != null) {
                onPreDismissListener.onPreDismiss();
            }
            this.mIsShowing = false;
            this.mIsTransitioningToDismiss = true;
            this.mInterceptTouchEvent = true;
            PopupAnimHelper popupAnimHelper = this.mAnimHelper;
            if (popupAnimHelper == null) {
                this.mIsTransitioningToDismiss = false;
                realDismiss();
            } else {
                popupAnimHelper.addPopupAnimationListener(new PopupAnimationListener() { // from class: miuix.popupwidget.widget.PopupView.5
                    @Override // miuix.popupwidget.widget.PopupAnimationListener, miuix.popupwidget.widget.IPopupAnimationListener
                    public void onReverseAnimationEnd() {
                        PopupView.this.mAnimHelper.removePopupAnimationListener(this);
                        PopupView.this.mIsTransitioningToDismiss = false;
                        PopupView.this.realDismiss();
                    }
                });
                this.mAnimHelper.dismiss();
            }
        }
    }

    public void realDismiss() {
        this.mInterceptTouchEvent = false;
        PopupAnimHelper popupAnimHelper = this.mAnimHelper;
        if (popupAnimHelper != null) {
            popupAnimHelper.resetFromContentView();
        }
        this.mIsShowing = false;
        releaseAccessibilityFocus();
        detachAnchorView();
        if (getParent() instanceof ViewGroup) {
            post(new Runnable() { // from class: miuix.popupwidget.widget.PopupView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PopupView.m5476$r8$lambda$oRSBuWAqhrm0JoBx76PwxIgRIo(this.f$0);
                }
            });
        }
        OnDismissListener onDismissListener = this.mOnDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$oRSBuWAqhr-m0JoBx76PwxIgRIo, reason: not valid java name */
    public static /* synthetic */ void m5476$r8$lambda$oRSBuWAqhrm0JoBx76PwxIgRIo(PopupView popupView) {
        ViewParent parent = popupView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(popupView);
        }
    }

    private void showWithAnim(int i) {
        PopupAnimHelper popupAnimHelper = this.mAnimHelper;
        if (popupAnimHelper != null) {
            popupAnimHelper.setBlurEnabled(isSelfBlurEnabled());
            this.mAnimHelper.setBlurView(this.mMenuLayer);
            this.mAnimHelper.addPopupAnimationListener(new PopupAnimationListener() { // from class: miuix.popupwidget.widget.PopupView.6
                @Override // miuix.popupwidget.widget.PopupAnimationListener, miuix.popupwidget.widget.IPopupAnimationListener
                public void onForwardAnimationEnd() {
                    PopupView.this.mAnimHelper.removePopupAnimationListener(this);
                    PopupView.this.requestAccessibilityFocus();
                }

                @Override // miuix.popupwidget.widget.PopupAnimationListener, miuix.popupwidget.widget.IPopupAnimationListener
                public void onForwardAnimationCancel() {
                    PopupView.this.mAnimHelper.removePopupAnimationListener(this);
                }
            });
            this.mAnimHelper.setGravity(i);
            this.mAnimHelper.show();
        }
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public PopupView setAdapter(ListAdapter listAdapter) {
        this.mAdapter = listAdapter;
        return this;
    }

    public PopupView setStrategy(IPopupWindowStrategy iPopupWindowStrategy) {
        this.mPopupWindowStrategy = iPopupWindowStrategy;
        updateMinSafeInset();
        return this;
    }

    public boolean isSelfBlurEnabled() {
        return this.mSelfBlurEnabled;
    }

    public PopupView setMaterialEnabled(boolean z) {
        this.mMaterialEnabled = z;
        return this;
    }

    public boolean isMaterialEnabled() {
        if (!HyperMaterialUtils.isFeatureEnable(this.mContext)) {
            return false;
        }
        if (MENU_MATERIAL_ENABLED) {
            return true;
        }
        if (this.mMaterialEnabled) {
            return MiuiBlurUtils.isPassWindowBlurWhitelisted(this.mContentView, this.mContext.getPackageName());
        }
        return false;
    }

    public PopupView setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    public PopupView setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
        return this;
    }

    public PopupView setOnPreDismissListener(OnPreDismissListener onPreDismissListener) {
        this.mOnPreDismissListener = onPreDismissListener;
        return this;
    }

    public PopupView setAnchorView(View view) {
        if (view == null) {
            return this;
        }
        if (getAnchor() != view) {
            detachAnchorView();
        }
        ViewUtils.getBoundsInWindow(view, this.mPopupWindowSpec.mAnchorViewBounds);
        this.mAnchorView = new WeakReference(view);
        return this;
    }

    public View getAnchor() {
        WeakReference weakReference = this.mAnchorView;
        if (weakReference != null) {
            return (View) weakReference.get();
        }
        return null;
    }

    public PopupView setDecorView(View view) {
        if (view == null) {
            return this;
        }
        this.mFenceDecor = new WeakReference(view);
        ViewUtils.getBoundsInWindow(view, this.mPopupWindowSpec.mDecorViewBounds);
        return this;
    }

    public PopupView setDropDownGravity(int i) {
        this.mPopupWindowSpec.mGravity = i;
        return this;
    }

    protected View getDecorView() {
        WeakReference weakReference = this.mFenceDecor;
        if (weakReference == null || weakReference.get() == null) {
            WeakReference weakReference2 = this.mAnchorView;
            if (weakReference2 != null) {
                return ((View) weakReference2.get()).getRootView();
            }
            return null;
        }
        return (View) this.mFenceDecor.get();
    }

    private ViewGroup getViewLayer() {
        View anchor = getAnchor();
        if (anchor == null) {
            return null;
        }
        View rootView = anchor.getRootView();
        if (rootView instanceof ViewGroup) {
            return (ViewGroup) rootView;
        }
        return null;
    }

    protected boolean shouldSetElevation() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
        if (accessibilityManager.isEnabled()) {
            accessibilityManager.isTouchExplorationEnabled();
        }
        return this.mHasShadow;
    }

    protected void prepareElevation() {
        if (shouldSetElevation()) {
            boolean z = MiShadowUtils.SUPPORT_MI_SHADOW;
            View view = this.mContentView;
            if (z) {
                ShadowToken shadowToken = ShadowToken.Regular;
                MiShadowUtils.setMiShadow(view, shadowToken.color, shadowToken.offsetX, shadowToken.offsetY, shadowToken.radius);
            } else {
                view.setElevation(this.mElevation);
                setPopupShadowAlpha(this.mContentView);
            }
        }
    }

    protected void setPopupShadowAlpha(View view) {
        if (EnvStateManager.isFreeFormMode(this.mContext)) {
            view.setOutlineProvider(null);
        } else {
            view.setOutlineProvider(new ViewOutlineProvider() { // from class: miuix.popupwidget.widget.PopupView.7
                @Override // android.view.ViewOutlineProvider
                public void getOutline(View view2, Outline outline) {
                    if (view2.getWidth() == 0 || view2.getHeight() == 0) {
                        return;
                    }
                    outline.setAlpha(AttributeResolver.resolveFloat(view2.getContext(), R$attr.popupWindowShadowAlpha, 0.3f));
                    if (view2.getBackground() != null) {
                        view2.getBackground().getOutline(outline);
                    }
                }
            });
            view.setOutlineSpotShadowColor(this.mContext.getColor(R$color.miuix_appcompat_drop_down_menu_spot_shadow_color));
        }
    }

    protected void updatePopupWindowSpec(PopupWindowSpec popupWindowSpec) {
        View anchor = getAnchor();
        View decorView = getDecorView();
        if (anchor == null || decorView == null) {
            return;
        }
        Rect rectUpdateSafeInsets = updateSafeInsets(decorView);
        ViewUtils.getBoundsInWindow(decorView, popupWindowSpec.mDecorViewBounds);
        ViewUtils.getBoundsInWindow(anchor, popupWindowSpec.mAnchorViewBounds);
        Rect rect = popupWindowSpec.mDecorViewBounds;
        Point windowSize = EnvStateManager.getWindowSize(this.mContext);
        rect.set(Math.max(0, rect.left), Math.max(0, rect.top), Math.min(windowSize.x, rect.right), Math.min(windowSize.y, rect.bottom));
        int iCheckMaxWidth = checkMaxWidth(rect, rectUpdateSafeInsets);
        int iCheckMinWidth = checkMinWidth(rect, rectUpdateSafeInsets);
        int iCheckMaxHeight = checkMaxHeight(rect, rectUpdateSafeInsets);
        popupWindowSpec.mSafeInsets = rectUpdateSafeInsets;
        popupWindowSpec.mMaxWidth = iCheckMaxWidth;
        popupWindowSpec.mMinWidth = iCheckMinWidth;
        popupWindowSpec.mMaxHeight = iCheckMaxHeight;
        popupWindowSpec.layoutDirection = decorView.getLayoutDirection();
    }

    protected int[][] getItemViewBounds(ListAdapter listAdapter, ViewGroup viewGroup, Context context) {
        int iMakeMeasureSpec = View.MeasureSpec.makeMeasureSpec((this.mPopupWindowSpec.mMaxWidth - this.mContentView.getPaddingLeft()) - this.mContentView.getPaddingRight(), Integer.MIN_VALUE);
        int iMakeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(0, 0);
        int count = listAdapter.getCount();
        int[][] iArr = (int[][]) Array.newInstance((Class<?>) Integer.TYPE, count, 2);
        int i = 0;
        View view = null;
        for (int i2 = 0; i2 < count; i2++) {
            int itemViewType = listAdapter.getItemViewType(i2);
            if (itemViewType != i) {
                view = null;
                i = itemViewType;
            }
            if (viewGroup == null) {
                viewGroup = new FrameLayout(context);
            }
            view = listAdapter.getView(i2, view, viewGroup);
            view.measure(iMakeMeasureSpec, iMakeMeasureSpec2);
            iArr[i2][0] = view.getMeasuredWidth();
            iArr[i2][1] = view.getMeasuredHeight();
        }
        return iArr;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() throws Exception {
        unregisterBackPressedCallback();
        detachAnchorView();
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) throws Exception {
        super.onConfigurationChanged(configuration);
        if (this.mDismissOnConfigurationChanged) {
            dismiss();
        } else {
            configurationChanged(configuration);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) throws Exception {
        if (keyEvent.getKeyCode() == 4 && isShowing() && !this.mIsTransitioningToDismiss) {
            if (keyEvent.getAction() == 0) {
                return true;
            }
            if (keyEvent.getAction() == 1) {
                dismiss();
                return true;
            }
        }
        return super.dispatchKeyEvent(keyEvent);
    }

    protected void updateLocation(View view) {
        if (isShowing()) {
            boolean localVisibleRect = view.getLocalVisibleRect(new Rect());
            if (!this.mIgnoreAnchorVisibility && !localVisibleRect) {
                realDismiss();
            }
            computePopupContentSize();
            ViewUtils.getBoundsInWindow(view, this.mPopupWindowSpec.mAnchorViewBounds);
            int xInWindow = this.mPopupWindowStrategy.getXInWindow(this.mPopupWindowSpec);
            int yInWindow = this.mPopupWindowStrategy.getYInWindow(this.mPopupWindowSpec);
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mContentView.getLayoutParams();
            int i = layoutParams.width;
            PopupWindowSpec popupWindowSpec = this.mPopupWindowSpec;
            int i2 = popupWindowSpec.mFinalPopupWidth;
            if (i != i2 || layoutParams.height != popupWindowSpec.mFinalPopupHeight) {
                layoutParams.width = i2;
                layoutParams.height = popupWindowSpec.mFinalPopupHeight;
                this.mContentView.setLayoutParams(layoutParams);
            }
            this.mMenuLayer.setTranslationX(xInWindow - 128);
            this.mMenuLayer.setTranslationY(yInWindow - 128);
            PopupAnimHelper popupAnimHelper = this.mAnimHelper;
            if (popupAnimHelper != null) {
                popupAnimHelper.updateAnimationOptions();
                PopupWindowSpec popupWindowSpec2 = this.mPopupWindowSpec;
                Rect rect = new Rect(0, 0, popupWindowSpec2.mFinalPopupWidth, popupWindowSpec2.mFinalPopupHeight);
                Rect rect2 = this.mPopupWindowSpec.mDecorViewBounds;
                rect.offsetTo(xInWindow + rect2.left, yInWindow + rect2.top);
                int iComputeGravity = this.mUserAnimationGravity;
                if (iComputeGravity == 0) {
                    iComputeGravity = computeGravity(this.mPopupWindowSpec.mAnchorViewBounds, rect, 0, view.getLayoutDirection());
                }
                this.mAnimHelper.update(iComputeGravity);
            }
        }
    }

    protected static int computeGravity(Rect rect, Rect rect2, int i, int i2) {
        int absoluteGravity = Gravity.getAbsoluteGravity(i, i2) & 112;
        int i3 = 48;
        if (Math.abs(rect2.centerY() - rect.centerY()) <= 10 ? absoluteGravity == 80 : rect2.centerY() <= rect.centerY()) {
            i3 = 80;
        }
        if (Math.abs(rect2.centerX() - rect.centerX()) > 10) {
            return rect2.centerX() > rect.centerX() ? i3 | 3 : i3 | 5;
        }
        return i3;
    }

    protected Rect updateSafeInsets(View view) {
        return updateSafeInsetsByDecor(this.mContext, view, this.mMinSafeInsetDimen);
    }

    protected int checkMaxHeight(Rect rect, Rect rect2) {
        return Math.min(this.mMaxAllowedHeight, (rect.height() - rect2.top) - rect2.bottom);
    }

    protected int checkMaxWidth(Rect rect, Rect rect2) {
        return Math.min(this.mMaxAllowedWidth, (rect.width() - rect2.left) - rect2.right);
    }

    protected int checkMinWidth(Rect rect, Rect rect2) {
        return Math.min(this.mMinAllowedWidth, (rect.width() - rect2.left) - rect2.right);
    }

    private void computePopupContentSize() {
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter != null) {
            this.mPopupWindowSpec.mItemViewBounds = getItemViewBounds(listAdapter, null, getContext());
        }
        PopupWindowSpec popupWindowSpec = this.mPopupWindowSpec;
        int i = this.mVerticalPadding;
        popupWindowSpec.mPaddingTop = i;
        popupWindowSpec.mPaddingBottom = i;
        this.mPopupWindowStrategy.measureContentSize(popupWindowSpec);
    }

    private void updateDisplayConfig(View view) {
        if (view == null) {
            view = getDecorView();
        }
        Resources resources = this.mContext.getResources();
        WindowBaseInfo windowInfo = EnvStateManager.getWindowInfo(this.mContext);
        int width = view != null ? view.getWidth() : windowInfo.windowSize.x;
        int height = view != null ? view.getHeight() : windowInfo.windowSize.y;
        this.mMaxAllowedWidth = Math.min(width, resources.getDimensionPixelSize(R$dimen.miuix_popup_window_max_width));
        this.mMinAllowedWidth = Math.min(width, resources.getDimensionPixelSize(R$dimen.miuix_popup_window_min_width));
        this.mMaxAllowedHeight = Math.min(height, resources.getDimensionPixelSize(R$dimen.miuix_popup_window_max_height));
        updateMinSafeInset();
    }

    protected void updateMinSafeInset() {
        this.mMinSafeInsetDimen = this.mContext.getResources().getDimensionPixelSize(this.mPopupWindowStrategy.getSafeMarginResId());
    }

    private void configurationChanged(Configuration configuration) {
        View anchor = getAnchor();
        if (anchor == null || this.mDetachAnchorLayoutFlag) {
            return;
        }
        this.mDetachAnchorLayoutFlag = true;
        anchor.getViewTreeObserver().addOnGlobalLayoutListener(this.mAnchorGlobalLayoutListener);
    }

    private void detachAnchorView() {
        if (this.mDetachAnchorLayoutFlag) {
            this.mDetachAnchorLayoutFlag = false;
            View anchor = getAnchor();
            if (anchor != null) {
                anchor.getViewTreeObserver().removeOnGlobalLayoutListener(this.mAnchorGlobalLayoutListener);
            }
        }
    }

    private static Rect updateSafeInsetsByDecor(Context context, View view, int i) {
        Rect rect = new Rect();
        rect.set(i, 0, i, i);
        Rect rect2 = new Rect();
        ViewUtils.getBoundsInWindow(view, rect2);
        WindowInsets rootWindowInsets = view.getRootWindowInsets();
        if (rootWindowInsets != null) {
            Insets insets = rootWindowInsets.getInsets(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            rect.set(insets.left, insets.top, insets.right, insets.bottom);
        }
        WindowBaseInfo windowInfo = EnvStateManager.getWindowInfo(context);
        rect.left = Math.max(i, rect.left - rect2.left);
        rect.right = Math.max(i, rect.right - Math.max(0, windowInfo.windowSize.x - rect2.right));
        rect.top = Math.max(i, rect.top - rect2.top);
        rect.bottom = Math.max(i, rect.bottom - Math.max(0, windowInfo.windowSize.y - rect2.bottom));
        return rect;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestAccessibilityFocus() {
        trapAccessibilityFocus();
        this.mContentView.sendAccessibilityEvent(32);
        ListView listView = (ListView) findViewById(R.id.list);
        if (listView == null || listView.getChildCount() <= 0) {
            return;
        }
        listView.getChildAt(0).performAccessibilityAction(64, null);
    }

    private void clearAccessibilityFocus() {
        ListView listView = (ListView) findViewById(R.id.list);
        if (listView == null) {
            return;
        }
        listView.performAccessibilityAction(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, null);
        for (int i = 0; i < listView.getChildCount(); i++) {
            listView.getChildAt(i).performAccessibilityAction(DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY, null);
        }
    }

    private void trapAccessibilityFocus() {
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt != this) {
                    childAt.setImportantForAccessibility(4);
                }
            }
        }
    }

    private void releaseAccessibilityFocus() {
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) parent;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt != this) {
                    childAt.setImportantForAccessibility(0);
                }
            }
        }
    }

    private Activity getActivity() {
        for (Context baseContext = this.mContext; baseContext instanceof ContextWrapper; baseContext = ((ContextWrapper) baseContext).getBaseContext()) {
            if (baseContext instanceof Activity) {
                return (Activity) baseContext;
            }
        }
        return null;
    }

    private void registerBackPressedCallback() {
        Activity activity = getActivity();
        if ((activity instanceof ComponentActivity) && this.mBackPressedCallback == null) {
            this.mBackPressedCallback = new OnBackPressedCallback(true) { // from class: miuix.popupwidget.widget.PopupView.9
                @Override // androidx.activity.OnBackPressedCallback
                public void handleOnBackPressed() throws Exception {
                    if (!PopupView.this.isShowing() || PopupView.this.mIsTransitioningToDismiss) {
                        return;
                    }
                    PopupView.this.dismiss();
                }
            };
            ((ComponentActivity) activity).getOnBackPressedDispatcher().addCallback(this.mBackPressedCallback);
        }
    }

    private void unregisterBackPressedCallback() throws Exception {
        OnBackPressedCallback onBackPressedCallback = this.mBackPressedCallback;
        if (onBackPressedCallback != null) {
            onBackPressedCallback.remove();
            this.mBackPressedCallback = null;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mInterceptTouchEvent;
    }
}
