package miuix.appcompat.internal.app.widget.actionbar;

import android.R;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.LinearLayout;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.appcompat.R$style;
import miuix.appcompat.internal.view.ColorTransitionTextView;
import miuix.core.util.RomUtils;
import miuix.internal.util.AttributeResolver;
import miuix.theme.Typography;

/* JADX INFO: loaded from: classes2.dex */
public class ExpandTitle {
    private Context mContext;
    private ColorTransitionTextView mExpandSubtitleView;
    private LinearLayout mExpandTitleLayout;
    private ColorTransitionTextView mExpandTitleView;
    private boolean mVisible = true;
    private boolean mTextColorTransitEnable = false;
    View.OnTouchListener subtitleTouchListener = new View.OnTouchListener() { // from class: miuix.appcompat.internal.app.widget.actionbar.ExpandTitle.1
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return !view.isClickable();
        }
    };
    private int mTitleStyle = R$style.Miuix_AppCompat_TextAppearance_WindowTitle_Expand;
    private int mSubtitleStyle = R$style.Miuix_AppCompat_TextAppearance_WindowTitle_Subtitle_Expand;

    public ExpandTitle(Context context) {
        this.mContext = context;
    }

    public void init() {
        LinearLayout linearLayout = new LinearLayout(this.mContext);
        this.mExpandTitleLayout = linearLayout;
        linearLayout.setImportantForAccessibility(2);
        this.mExpandTitleLayout.setOrientation(1);
        this.mExpandTitleLayout.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.actionbar.ExpandTitle$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ExpandTitle expandTitle = this.f$0;
                expandTitle.mExpandTitleLayout.setBackground(AttributeResolver.resolveDrawable(expandTitle.mContext, R.attr.actionBarItemBackground));
            }
        });
        ColorTransitionTextView colorTransitionTextView = new ColorTransitionTextView(this.mContext, null, R$attr.expandTitleTheme);
        this.mExpandTitleView = colorTransitionTextView;
        colorTransitionTextView.setId(R$id.action_bar_title_expand);
        this.mExpandTitleView.setVerticalScrollBarEnabled(false);
        this.mExpandTitleView.setHorizontalScrollBarEnabled(false);
        this.mExpandTitleView.setFocusableInTouchMode(false);
        if (RomUtils.getHyperOsVersion() <= 1) {
            Typography.applyMiSansLight(this.mExpandTitleView);
        }
        this.mExpandTitleLayout.addView(this.mExpandTitleView, getChildLayoutParams());
        ColorTransitionTextView colorTransitionTextView2 = new ColorTransitionTextView(this.mContext, null, R$attr.expandSubtitleTheme);
        this.mExpandSubtitleView = colorTransitionTextView2;
        colorTransitionTextView2.setId(R$id.action_bar_subtitle_expand);
        this.mExpandSubtitleView.setVisibility(8);
        this.mExpandSubtitleView.setVerticalScrollBarEnabled(false);
        this.mExpandSubtitleView.setHorizontalScrollBarEnabled(false);
        this.mExpandTitleLayout.addView(this.mExpandSubtitleView, getChildLayoutParams());
        Resources resources = this.mContext.getResources();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mExpandSubtitleView.getLayoutParams();
        layoutParams.topMargin = resources.getDimensionPixelOffset(R$dimen.action_bar_subtitle_top_margin);
        layoutParams.bottomMargin = resources.getDimensionPixelOffset(R$dimen.action_bar_subtitle_bottom_margin);
    }

    private LinearLayout.LayoutParams getChildLayoutParams() {
        return new LinearLayout.LayoutParams(-2, -2);
    }

    public void setOnClickListener(View.OnClickListener onClickListener, boolean z) {
        this.mExpandTitleView.setOnClickListener(onClickListener);
        this.mExpandTitleView.post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.actionbar.ExpandTitle$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ExpandTitle.$r8$lambda$mHaF4LSxGhD4b_85xnkL5Z91yog(this.f$0);
            }
        });
        this.mExpandTitleView.setClickable(z);
    }

    public static /* synthetic */ void $r8$lambda$mHaF4LSxGhD4b_85xnkL5Z91yog(ExpandTitle expandTitle) {
        expandTitle.getClass();
        expandTitle.mExpandTitleLayout.setTouchDelegate(new TouchDelegate(new Rect(0, 0, expandTitle.mExpandTitleLayout.getWidth(), expandTitle.mExpandTitleLayout.getHeight()), expandTitle.mExpandTitleView));
    }

    public void setSubTitleOnClickListener(View.OnClickListener onClickListener, boolean z) {
        ColorTransitionTextView colorTransitionTextView = this.mExpandSubtitleView;
        if (colorTransitionTextView != null) {
            colorTransitionTextView.setOnClickListener(onClickListener);
            this.mExpandSubtitleView.setClickable(z);
            this.mExpandSubtitleView.setOnTouchListener(this.subtitleTouchListener);
        }
    }

    public void setAllTitlesClickable(boolean z) {
        ColorTransitionTextView colorTransitionTextView = this.mExpandTitleView;
        if (colorTransitionTextView != null) {
            colorTransitionTextView.setClickable(z);
        }
        ColorTransitionTextView colorTransitionTextView2 = this.mExpandSubtitleView;
        if (colorTransitionTextView2 != null) {
            colorTransitionTextView2.setClickable(z);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.mExpandTitleView.setText(charSequence);
        setEnabled(!TextUtils.isEmpty(charSequence));
    }

    public void setSubTitle(CharSequence charSequence) {
        this.mExpandSubtitleView.setText(charSequence);
        setSubTitleVisibility(TextUtils.isEmpty(charSequence) ? 8 : 0);
    }

    public void setEnabled(boolean z) {
        this.mExpandTitleLayout.setEnabled(z);
    }

    public void setSubTitleVisibility(int i) {
        this.mExpandSubtitleView.setVisibility(i);
    }

    public void setVisible(boolean z) {
        if (this.mVisible != z) {
            this.mVisible = z;
            this.mExpandTitleLayout.setVisibility(z ? 0 : 4);
        }
    }

    public void setTitleVisibility(int i) {
        this.mExpandTitleView.setVisibility(i);
    }

    public void setVisibility(int i) {
        if (!this.mVisible && i == 0) {
            this.mExpandTitleLayout.setVisibility(4);
        } else {
            this.mExpandTitleLayout.setVisibility(i);
        }
    }

    public int getVisibility() {
        return this.mExpandTitleLayout.getVisibility();
    }

    public View getLayout() {
        return this.mExpandTitleLayout;
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.mExpandTitleView.setTextAppearance(this.mTitleStyle);
        this.mExpandSubtitleView.setTextAppearance(this.mSubtitleStyle);
        if (RomUtils.getHyperOsVersion() <= 1) {
            Typography.applyMiSansLight(this.mExpandTitleView);
        }
    }

    public void setTextColorTransitEnable(boolean z, int i) {
        if (this.mTextColorTransitEnable != z) {
            if (!z) {
                this.mExpandTitleView.startColorTransition(false, false);
            }
            this.mTextColorTransitEnable = z;
            if (z && i == 2) {
                this.mExpandTitleView.startColorTransition(true, false);
            }
        }
    }
}
