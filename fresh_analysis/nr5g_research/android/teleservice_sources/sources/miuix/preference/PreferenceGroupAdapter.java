package miuix.preference;

import android.R;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.widget.ViewUtils;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.support.drawable.CardStateDrawable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.FolmeBlink;
import miuix.animation.internal.BlinkStateObserver;
import miuix.container.ExtraPaddingObserver;
import miuix.flexible.template.IHyperCellTemplate;
import miuix.flexible.view.HyperCellLayout;
import miuix.internal.graphics.drawable.TaggingDrawable;
import miuix.internal.util.AttributeResolver;
import miuix.preference.flexible.AbstractBaseTemplate;
import miuix.preference.utils.PreferenceLayoutUtils;
import miuix.recyclerview.card.base.ViewOutlineHelper;
import miuix.view.CompatViewMethod;

/* JADX INFO: loaded from: classes2.dex */
class PreferenceGroupAdapter extends androidx.preference.PreferenceGroupAdapter implements BlinkStateObserver, ExtraPaddingObserver {
    private static final int[] STATES_TAGS;
    private static final int[] STATE_SET_FIRST;
    private static final int[] STATE_SET_LAST;
    private static final int[] STATE_SET_MIDDLE;
    private static final int[] STATE_SET_NO_LINE;
    private static final int[] STATE_SET_NO_TITLE;
    private static final int[] STATE_SET_SINGLE;
    private final List mAnimatorPreferenceGroups;
    public int mCardMarginEnd;
    public int mCardMarginStart;
    private int mCheckableFilterColorChecked;
    private int mCheckableFilterColorNormal;
    private Paint mClipPaint;
    private RecyclerView.ItemAnimator mCurrentItemAnimator;
    private PositionDescriptor[] mDescriptors;
    private int mExtraHorizontalPadding;
    private FolmeBlink mFolmeBlink;
    private View mHighlightItemView;
    private int mHighlightPosition;
    private boolean mIsDisableAllCard;
    private boolean mIsEnableCardStyle;
    private boolean mItemSelectable;
    private View.OnTouchListener mItemTouchOnDownListener;
    private RecyclerView.OnItemTouchListener mItemTouchOnHighlightListener;
    private int mMaskPaddingBottom;
    private int mMaskPaddingEnd;
    private int mMaskPaddingStart;
    private int mMaskPaddingTop;
    private int mMaskRadius;
    private boolean mNeedCancelHighlightRequest;
    private final RecyclerView.AdapterDataObserver mObserver;
    private View.OnTouchListener mParentTouchOnHighlightListener;
    private int mPreferenceHighLightChildRadius;
    private int mRadioSetItemPaddingEndExtra;
    private int mRadioSetItemPaddingStartExtra;
    private RecyclerView mRecyclerView;
    private Preference mSelectedPreference;
    private Rect mTempBgPadding;

    static {
        int[] iArr = {R.attr.state_single, R.attr.state_first, R.attr.state_middle, R.attr.state_last, R$attr.state_no_title, R$attr.state_no_line};
        STATES_TAGS = iArr;
        Arrays.sort(iArr);
        STATE_SET_SINGLE = new int[]{R.attr.state_single};
        STATE_SET_FIRST = new int[]{R.attr.state_first};
        STATE_SET_MIDDLE = new int[]{R.attr.state_middle};
        STATE_SET_LAST = new int[]{R.attr.state_last};
        STATE_SET_NO_TITLE = new int[]{R$attr.state_no_title};
        STATE_SET_NO_LINE = new int[]{R$attr.state_no_line};
    }

    public PreferenceGroupAdapter(PreferenceGroup preferenceGroup, boolean z, int i) {
        super(preferenceGroup);
        this.mObserver = new RecyclerView.AdapterDataObserver() { // from class: miuix.preference.PreferenceGroupAdapter.1
            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                super.onChanged();
                PreferenceGroupAdapter preferenceGroupAdapter = PreferenceGroupAdapter.this;
                preferenceGroupAdapter.mDescriptors = new PositionDescriptor[preferenceGroupAdapter.getItemCount()];
            }
        };
        this.mRadioSetItemPaddingEndExtra = 0;
        this.mExtraHorizontalPadding = 0;
        this.mHighlightPosition = -1;
        this.mHighlightItemView = null;
        this.mNeedCancelHighlightRequest = false;
        this.mParentTouchOnHighlightListener = null;
        this.mItemTouchOnHighlightListener = null;
        this.mItemTouchOnDownListener = new View.OnTouchListener() { // from class: miuix.preference.PreferenceGroupAdapter.2
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() != 0) {
                    return false;
                }
                view.setPressed(true);
                return false;
            }
        };
        this.mItemSelectable = false;
        this.mTempBgPadding = new Rect();
        this.mCardMarginStart = 0;
        this.mCardMarginEnd = 0;
        this.mAnimatorPreferenceGroups = new ArrayList();
        init(preferenceGroup, z, i);
    }

    private void init(PreferenceGroup preferenceGroup, boolean z, int i) {
        this.mIsEnableCardStyle = z;
        this.mIsDisableAllCard = -1 == i;
        this.mDescriptors = new PositionDescriptor[getItemCount()];
        initAttr(preferenceGroup.getContext());
    }

    public void initAttr(Context context) {
        this.mRadioSetItemPaddingStartExtra = AttributeResolver.resolveDimensionPixelSize(context, R$attr.preferenceRadioSetChildExtraPaddingStart);
        this.mCheckableFilterColorChecked = AttributeResolver.resolveColor(context, R$attr.checkablePreferenceItemColorFilterChecked);
        this.mCheckableFilterColorNormal = AttributeResolver.resolveColor(context, R$attr.checkablePreferenceItemColorFilterNormal);
        this.mPreferenceHighLightChildRadius = context.getResources().getDimensionPixelSize(R$dimen.miuix_preference_high_light_radius);
        this.mCardMarginStart = AttributeResolver.resolveDimensionPixelSize(context, R$attr.preferenceCardGroupMarginStart);
        this.mCardMarginEnd = AttributeResolver.resolveDimensionPixelSize(context, R$attr.preferenceCardGroupMarginEnd);
    }

    public void setClipPaint(Paint paint, int i, int i2, int i3, int i4, int i5) {
        this.mClipPaint = paint;
        this.mMaskPaddingTop = i;
        this.mMaskPaddingBottom = i2;
        this.mMaskPaddingStart = i3;
        this.mMaskPaddingEnd = i4;
        this.mMaskRadius = i5;
    }

    @Override // miuix.container.ExtraPaddingObserver
    public boolean setExtraHorizontalPadding(int i) {
        if (this.mExtraHorizontalPadding == i) {
            return false;
        }
        this.mExtraHorizontalPadding = i;
        return true;
    }

    @Override // miuix.container.ExtraPaddingObserver
    public void onExtraPaddingChanged(int i) {
        this.mExtraHorizontalPadding = i;
        notifyDataSetChanged();
    }

    public void setItemSelectable(boolean z) {
        this.mItemSelectable = z;
    }

    public void setSelectedPreference(Preference preference) {
        this.mSelectedPreference = preference;
        notifyDataSetChanged();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean isNeedSetOutlineForItem(Preference preference) {
        if (preference instanceof PreferenceStyle) {
            return ((PreferenceStyle) preference).enabledCardStyle();
        }
        return this.mIsEnableCardStyle;
    }

    private boolean isNeedSetOutline(int i, Preference preference) {
        if ((i == -1 || !this.mIsEnableCardStyle || (preference instanceof PreferenceScreen) || !isNeedSetOutlineForItem(preference)) && !(preference instanceof RadioButtonPreference)) {
            return preference != null && (preference.getParent() instanceof RadioSetPreferenceCategory);
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.preference.PreferenceGroupAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder, int i) {
        int i2;
        Drawable background;
        View view = preferenceViewHolder.itemView;
        boolean z = view instanceof HyperCellLayout;
        if (z) {
            IHyperCellTemplate template = ((HyperCellLayout) view).getTemplate();
            if (template instanceof AbstractBaseTemplate) {
                ((AbstractBaseTemplate) template).storeVisibilityBeforeUpdate(preferenceViewHolder);
            }
        }
        super.onBindViewHolder(preferenceViewHolder, i);
        CompatViewMethod.setForceDarkAllowed(preferenceViewHolder.itemView, false);
        Preference item = getItem(i);
        if (!(item instanceof PreferenceAccessibility) || ((PreferenceAccessibility) item).isAccessibilityEnabled()) {
            handleAccessibility(item, preferenceViewHolder);
        }
        boolean z2 = item instanceof DropDownPreference;
        if (!z2) {
            preferenceViewHolder.itemView.setOnTouchListener(null);
        }
        boolean z3 = this.mItemSelectable;
        View view2 = preferenceViewHolder.itemView;
        if (z3) {
            view2.setActivated(item == this.mSelectedPreference);
        } else {
            view2.setActivated(false);
        }
        PositionDescriptor positionDescriptor = this.mDescriptors[i];
        int i3 = positionDescriptor != null ? positionDescriptor.type : -1;
        int preferenceDescriptor = getPreferenceDescriptor(item, i);
        if (this.mIsDisableAllCard || !isNeedSetOutline(preferenceDescriptor, item)) {
            i2 = preferenceDescriptor;
        } else {
            float f = this.mMaskRadius;
            boolean z4 = i3 != preferenceDescriptor;
            i2 = preferenceDescriptor;
            ViewOutlineHelper.setItemCardOutline(preferenceViewHolder, i2, f, z4, this.mRecyclerView.getItemAnimator() != null ? this.mRecyclerView.getItemAnimator().getAddDuration() : 0L);
        }
        if (item == 0) {
            return;
        }
        int i4 = this.mExtraHorizontalPadding;
        if (!this.mIsEnableCardStyle) {
            Drawable background2 = preferenceViewHolder.itemView.getBackground();
            if (((item instanceof PreferenceGroup) || (item.getParent() instanceof RadioSetPreferenceCategory) || (item.getParent() instanceof RadioButtonPreferenceCategory) || (item instanceof RadioButtonPreference)) && !(item instanceof PreferenceScreen)) {
                if (item instanceof androidx.preference.PreferenceCategory) {
                    if (background2 != null) {
                        if (background2 instanceof LayerDrawable) {
                            ((LayerDrawable) background2).setLayerInset(0, i4, 0, i4, 0);
                            TaggingDrawable taggingDrawable = new TaggingDrawable(background2);
                            preferenceViewHolder.itemView.setBackground(taggingDrawable);
                            int[] iArr = this.mDescriptors[i].status;
                            if (iArr != null) {
                                taggingDrawable.setTaggingState(iArr);
                            }
                        }
                        background2.getPadding(this.mTempBgPadding);
                        View view3 = preferenceViewHolder.itemView;
                        Rect rect = this.mTempBgPadding;
                        view3.setPadding(rect.left + i4, rect.top, rect.right + i4, rect.bottom);
                    }
                } else if (background2 != null) {
                    background2.getPadding(this.mTempBgPadding);
                    View view4 = preferenceViewHolder.itemView;
                    Rect rect2 = this.mTempBgPadding;
                    view4.setPadding(rect2.left, rect2.top, rect2.right, rect2.bottom);
                }
            } else if (background2 != null) {
                background2.getPadding(this.mTempBgPadding);
                boolean zIsLayoutRtl = ViewUtils.isLayoutRtl(this.mRecyclerView);
                int i5 = zIsLayoutRtl ? this.mCardMarginEnd : this.mCardMarginStart;
                int i6 = zIsLayoutRtl ? this.mCardMarginStart : this.mCardMarginEnd;
                View view5 = preferenceViewHolder.itemView;
                Rect rect3 = this.mTempBgPadding;
                view5.setPadding(rect3.left + i5 + i4, rect3.top, rect3.right + i6 + i4, rect3.bottom);
            }
        } else if (item instanceof PreferenceScreen) {
            Drawable background3 = preferenceViewHolder.itemView.getBackground();
            if (background3 != null) {
                background3.getPadding(this.mTempBgPadding);
                boolean zIsLayoutRtl2 = ViewUtils.isLayoutRtl(this.mRecyclerView);
                int i7 = zIsLayoutRtl2 ? this.mCardMarginEnd : this.mCardMarginStart;
                int i8 = zIsLayoutRtl2 ? this.mCardMarginStart : this.mCardMarginEnd;
                View view6 = preferenceViewHolder.itemView;
                Rect rect4 = this.mTempBgPadding;
                view6.setPadding(rect4.left + i7 + i4, rect4.top, rect4.right + i8 + i4, rect4.bottom);
            }
        } else if (item instanceof androidx.preference.PreferenceCategory) {
            Drawable background4 = preferenceViewHolder.itemView.getBackground();
            if (background4 != null) {
                background4.getPadding(this.mTempBgPadding);
                View view7 = preferenceViewHolder.itemView;
                Rect rect5 = this.mTempBgPadding;
                view7.setPadding(rect5.left + i4, rect5.top, rect5.right + i4, rect5.bottom);
            }
        } else if ((item instanceof PreferenceStyle) && !((PreferenceStyle) item).enabledCardStyle()) {
            Drawable background5 = preferenceViewHolder.itemView.getBackground();
            if (background5 != null) {
                background5.getPadding(this.mTempBgPadding);
                boolean zIsLayoutRtl3 = ViewUtils.isLayoutRtl(this.mRecyclerView);
                int i9 = zIsLayoutRtl3 ? this.mCardMarginEnd : this.mCardMarginStart;
                int i10 = zIsLayoutRtl3 ? this.mCardMarginStart : this.mCardMarginEnd;
                View view8 = preferenceViewHolder.itemView;
                Rect rect6 = this.mTempBgPadding;
                view8.setPadding(rect6.left + i9 + i4, rect6.top, rect6.right + i10 + i4, rect6.bottom);
            }
        } else {
            Drawable background6 = preferenceViewHolder.itemView.getBackground();
            if (background6 != null) {
                background6.getPadding(this.mTempBgPadding);
                Rect rect7 = this.mTempBgPadding;
                int i11 = rect7.left;
                if (i11 != 0 || rect7.right != 0) {
                    preferenceViewHolder.itemView.setPadding(i11, rect7.top, rect7.right, rect7.bottom);
                }
            }
        }
        if ((item.getParent() instanceof RadioSetPreferenceCategory) && !(item instanceof RadioButtonPreference) && (background = preferenceViewHolder.itemView.getBackground()) != null) {
            background.getPadding(this.mTempBgPadding);
            boolean zIsLayoutRtl4 = ViewUtils.isLayoutRtl(this.mRecyclerView);
            Rect rect8 = this.mTempBgPadding;
            if (zIsLayoutRtl4) {
                rect8.right += this.mRadioSetItemPaddingStartExtra;
            } else {
                rect8.left += this.mRadioSetItemPaddingStartExtra;
            }
            View view9 = preferenceViewHolder.itemView;
            Rect rect9 = this.mTempBgPadding;
            view9.setPadding(rect9.left, rect9.top, rect9.right, rect9.bottom);
        }
        View viewFindViewById = preferenceViewHolder.itemView.findViewById(R$id.arrow_right);
        if (viewFindViewById != null) {
            viewFindViewById.setVisibility(isArrowRightVisible(item) ? 0 : 8);
        }
        if (ableToUseFolmeAnim(item)) {
            View viewFindViewById2 = preferenceViewHolder.itemView.findViewById(R$id.miuix_preference_navigation);
            View view10 = preferenceViewHolder.itemView;
            if (viewFindViewById2 == null) {
                if (view10.getForeground() == null) {
                    Drawable drawableResolveDrawable = AttributeResolver.resolveDrawable(item.getContext(), R$attr.preferenceItemForeground);
                    if (drawableResolveDrawable instanceof CardStateDrawable) {
                        isNeedSetOutline(i2, item);
                        CardStateDrawable cardStateDrawable = (CardStateDrawable) drawableResolveDrawable;
                        cardStateDrawable.setRadius(0);
                        cardStateDrawable.setInset(0, 0, 0, 0);
                        setPreferenceItemForegroundForHighLightChild(preferenceViewHolder.itemView, cardStateDrawable, item);
                    }
                    preferenceViewHolder.itemView.setForeground(drawableResolveDrawable);
                    if (!z2) {
                        preferenceViewHolder.itemView.setOnTouchListener(this.mItemTouchOnDownListener);
                    }
                } else {
                    Drawable foreground = preferenceViewHolder.itemView.getForeground();
                    if (foreground instanceof CardStateDrawable) {
                        CardStateDrawable cardStateDrawable2 = (CardStateDrawable) foreground;
                        cardStateDrawable2.setInset(0, 0, 0, 0);
                        if (setPreferenceItemForegroundForHighLightChild(preferenceViewHolder.itemView, cardStateDrawable2, item)) {
                            preferenceViewHolder.itemView.setForeground(foreground);
                        }
                    }
                }
            } else {
                Drawable foreground2 = view10.getForeground();
                if (foreground2 == null) {
                    Drawable drawableResolveDrawable2 = AttributeResolver.resolveDrawable(item.getContext(), R$attr.navigationPreferenceItemForeground);
                    if (drawableResolveDrawable2 instanceof LayerDrawable) {
                        int i12 = this.mIsEnableCardStyle ? 0 : i4;
                        ((LayerDrawable) drawableResolveDrawable2).setLayerInset(0, i12, 0, i12, 0);
                    }
                    preferenceViewHolder.itemView.setForeground(drawableResolveDrawable2);
                    if (!z2) {
                        preferenceViewHolder.itemView.setOnTouchListener(this.mItemTouchOnDownListener);
                    }
                } else if (foreground2 instanceof LayerDrawable) {
                    LayerDrawable layerDrawable = (LayerDrawable) foreground2;
                    int i13 = this.mIsEnableCardStyle ? 0 : i4;
                    layerDrawable.setLayerInset(0, i13, 0, i13, 0);
                    layerDrawable.invalidateSelf();
                }
            }
        }
        checkHighlight(preferenceViewHolder, i, i2, item);
        if (item instanceof PreferenceExtraPadding) {
            ((PreferenceExtraPadding) item).onPreferenceExtraPadding(preferenceViewHolder, i4);
        }
        if (z) {
            IHyperCellTemplate template2 = ((HyperCellLayout) preferenceViewHolder.itemView).getTemplate();
            if (template2 instanceof AbstractBaseTemplate) {
                ((AbstractBaseTemplate) template2).refreshLayoutIfVisibleChanged(preferenceViewHolder);
            }
        }
    }

    private void handleAccessibility(final Preference preference, PreferenceViewHolder preferenceViewHolder) {
        if (preference instanceof androidx.preference.PreferenceCategory) {
            TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.title);
            if (textView == null || TextUtils.isEmpty(preference.getTitle())) {
                return;
            }
            textView.setAccessibilityHeading(true);
            return;
        }
        if (preference instanceof androidx.preference.CheckBoxPreference) {
            View viewFindViewById = preferenceViewHolder.findViewById(R.id.checkbox);
            if (viewFindViewById != null) {
                viewFindViewById.setImportantForAccessibility(2);
            }
            if (isCheckBoxPreferenceExcluded(preference)) {
                return;
            }
            ViewCompat.setAccessibilityDelegate(preferenceViewHolder.itemView, new AccessibilityDelegateCompat() { // from class: miuix.preference.PreferenceGroupAdapter.3
                @Override // androidx.core.view.AccessibilityDelegateCompat
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                    accessibilityNodeInfoCompat.setCheckable(preference.isEnabled());
                    accessibilityNodeInfoCompat.setClassName(Switch.class.getName());
                    accessibilityNodeInfoCompat.setChecked(((androidx.preference.CheckBoxPreference) preference).isChecked());
                    accessibilityNodeInfoCompat.setContentDescription(preference.toString());
                }
            });
            return;
        }
        if (preference instanceof SwitchPreference) {
            View viewFindViewById2 = preferenceViewHolder.findViewById(R.id.switch_widget);
            if (viewFindViewById2 != null) {
                viewFindViewById2.setImportantForAccessibility(2);
            }
            if (isCheckBoxPreferenceExcluded(preference)) {
                return;
            }
            ViewCompat.setAccessibilityDelegate(preferenceViewHolder.itemView, new AccessibilityDelegateCompat() { // from class: miuix.preference.PreferenceGroupAdapter.4
                @Override // androidx.core.view.AccessibilityDelegateCompat
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                    accessibilityNodeInfoCompat.setCheckable(preference.isEnabled());
                    accessibilityNodeInfoCompat.setClassName(Switch.class.getName());
                    accessibilityNodeInfoCompat.setChecked(((SwitchPreference) preference).isChecked());
                    accessibilityNodeInfoCompat.setContentDescription(preference.toString());
                }
            });
        }
    }

    private boolean isCheckBoxPreferenceExcluded(Preference preference) {
        return (preference instanceof RadioButtonPreference) || (preference instanceof SingleChoicePreference) || (preference instanceof MultiChoicePreference);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean setPreferenceItemForegroundForHighLightChild(View view, CardStateDrawable cardStateDrawable, Preference preference) {
        View childAt;
        if (!(view instanceof ViewGroup) || (childAt = ((ViewGroup) view).getChildAt(0)) == null || !childAt.getClass().getSimpleName().contains("CardView")) {
            return false;
        }
        int paddingLeft = view.getPaddingLeft();
        int paddingTop = view.getPaddingTop();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();
        if (childAt.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
            paddingLeft += marginLayoutParams.leftMargin;
            paddingTop += marginLayoutParams.topMargin;
            paddingRight += marginLayoutParams.rightMargin;
            paddingBottom += marginLayoutParams.bottomMargin;
        }
        if ((preference instanceof PreferenceStyle) && ((PreferenceStyle) preference).enabledCardStyle()) {
            cardStateDrawable.setRadius(0);
        } else {
            cardStateDrawable.setRadius(this.mPreferenceHighLightChildRadius);
        }
        cardStateDrawable.setInset(paddingLeft, paddingTop, paddingRight, paddingBottom);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(PreferenceViewHolder preferenceViewHolder) {
        super.onViewRecycled((RecyclerView.ViewHolder) preferenceViewHolder);
        stopHighlight(preferenceViewHolder.itemView);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewDetachedFromWindow(PreferenceViewHolder preferenceViewHolder) {
        super.onViewDetachedFromWindow((RecyclerView.ViewHolder) preferenceViewHolder);
        stopHighlight(preferenceViewHolder.itemView);
    }

    public void stopHighlight() {
        View view = this.mHighlightItemView;
        if (view != null) {
            stopHighlight(view);
            FolmeBlink folmeBlink = this.mFolmeBlink;
            if (folmeBlink != null) {
                folmeBlink.detach(this);
            }
            this.mFolmeBlink = null;
            this.mNeedCancelHighlightRequest = false;
        }
    }

    public void checkHighlight(PreferenceViewHolder preferenceViewHolder, int i, int i2, Preference preference) {
        View view = preferenceViewHolder.itemView;
        if (i == this.mHighlightPosition) {
            if (!this.mNeedCancelHighlightRequest) {
                if (Boolean.TRUE.equals(view.getTag(R$id.preference_highlighted))) {
                    return;
                }
                startHighlight(view, i2, preference);
                return;
            }
            this.mNeedCancelHighlightRequest = false;
            return;
        }
        if (Boolean.TRUE.equals(view.getTag(R$id.preference_highlighted))) {
            stopHighlight(view);
        }
    }

    public boolean isHighlightRequested() {
        return this.mHighlightPosition != -1;
    }

    private void startHighlight(View view, int i, Preference preference) {
        view.setTag(R$id.preference_highlighted, Boolean.TRUE);
        if (this.mFolmeBlink == null) {
            FolmeBlink folmeBlink = (FolmeBlink) Folme.useAt(view).blink();
            this.mFolmeBlink = folmeBlink;
            folmeBlink.setTintMode(3);
            setHighlightBlinkRadius(i, preference);
            this.mFolmeBlink.attach(this);
            this.mFolmeBlink.startBlink(3, new AnimConfig[0]);
            this.mHighlightItemView = view;
        }
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null) {
            recyclerView.setItemAnimator(this.mCurrentItemAnimator);
        }
    }

    private void setHighlightBlinkRadius(int i, Preference preference) {
        this.mFolmeBlink.setBlinkRadius(0.0f);
    }

    public void stopHighlight(View view) {
        if (isHighlightRequested() && view != null && Boolean.TRUE.equals(view.getTag(R$id.preference_highlighted))) {
            Folme.useAt(view).blink().stopBlink();
            view.setTag(R$id.preference_highlighted, Boolean.FALSE);
            if (this.mHighlightItemView == view) {
                this.mHighlightItemView = null;
            }
            this.mHighlightPosition = -1;
            RecyclerView recyclerView = this.mRecyclerView;
            if (recyclerView != null) {
                recyclerView.removeOnItemTouchListener(this.mItemTouchOnHighlightListener);
                this.mRecyclerView.setOnTouchListener(null);
                this.mItemTouchOnHighlightListener = null;
                this.mParentTouchOnHighlightListener = null;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private boolean ableToUseFolmeAnim(Preference preference) {
        if (preference instanceof androidx.preference.PreferenceCategory) {
            return false;
        }
        if (preference instanceof FolmeAnimationController) {
            return ((FolmeAnimationController) preference).isTouchAnimationEnable();
        }
        return true;
    }

    private boolean isArrowRightVisible(Preference preference) {
        if (preference.getIntent() == null && preference.getFragment() == null) {
            return !(preference.getOnPreferenceClickListener() == null || (preference instanceof TwoStatePreference)) || (preference instanceof DialogPreference);
        }
        return true;
    }

    /* JADX WARN: Code duplicated, block: B:9:0x0019  */
    /* JADX WARN: Multi-variable type inference failed */
    private int getPreferenceDescriptor(Preference preference, int i) {
        int[] iArr;
        PreferenceGroup parent;
        int[] iArr2;
        boolean z;
        int[] iArr3;
        int[] iArr4;
        if (i >= 0) {
            PositionDescriptor[] positionDescriptorArr = this.mDescriptors;
            if (i < positionDescriptorArr.length) {
                if (positionDescriptorArr[i] == null) {
                    positionDescriptorArr[i] = new PositionDescriptor();
                }
                iArr = this.mDescriptors[i].status;
            } else {
                iArr = null;
            }
        } else {
            iArr = null;
        }
        if (iArr == null && (parent = preference.getParent()) != null) {
            List allVisiblePreferences = getAllVisiblePreferences(parent);
            if (allVisiblePreferences.isEmpty()) {
                return -1;
            }
            boolean z2 = parent instanceof PreferenceScreen;
            int i2 = 4;
            boolean zIsEmpty = true;
            if (z2 && PreferenceLayoutUtils.isDynamicGroupItem(preference)) {
                int groupItemType = ((PreferencedynamicGroupController) preference).getGroupItemType();
                if (groupItemType == 1) {
                    PositionDescriptor positionDescriptor = this.mDescriptors[i];
                    positionDescriptor.status = STATE_SET_SINGLE;
                    positionDescriptor.type = 1;
                } else if (groupItemType == 2) {
                    PositionDescriptor positionDescriptor2 = this.mDescriptors[i];
                    positionDescriptor2.status = STATE_SET_FIRST;
                    positionDescriptor2.type = 2;
                } else if (groupItemType == 3) {
                    PositionDescriptor positionDescriptor3 = this.mDescriptors[i];
                    positionDescriptor3.status = STATE_SET_MIDDLE;
                    positionDescriptor3.type = 3;
                } else if (groupItemType == 4) {
                    PositionDescriptor positionDescriptor4 = this.mDescriptors[i];
                    positionDescriptor4.status = STATE_SET_LAST;
                    positionDescriptor4.type = 4;
                }
            } else {
                if (!(preference instanceof PreferenceGroup) && (z2 || (((parent instanceof RadioButtonPreferenceCategory) || (parent instanceof SingleChoicePreferenceCategory) || (parent instanceof MultiChoicePreferenceCategory)) && !isNeedCardGroup(preference)))) {
                    int[] iArr5 = STATE_SET_SINGLE;
                    PositionDescriptor positionDescriptor5 = this.mDescriptors[i];
                    positionDescriptor5.status = iArr5;
                    positionDescriptor5.type = 1;
                    return 1;
                }
                if (allVisiblePreferences.size() == 1) {
                    iArr2 = STATE_SET_SINGLE;
                    i2 = 1;
                } else if (preference.compareTo((Preference) allVisiblePreferences.get(0)) == 0) {
                    iArr2 = STATE_SET_FIRST;
                    i2 = 2;
                } else if (preference.compareTo((Preference) allVisiblePreferences.get(allVisiblePreferences.size() - 1)) == 0) {
                    iArr2 = STATE_SET_LAST;
                } else {
                    iArr2 = STATE_SET_MIDDLE;
                    i2 = 3;
                }
                if (preference instanceof androidx.preference.PreferenceCategory) {
                    androidx.preference.PreferenceCategory preferenceCategory = (androidx.preference.PreferenceCategory) preference;
                    if (preferenceCategory instanceof PreferenceCategory) {
                        PreferenceCategory preferenceCategory2 = (PreferenceCategory) preferenceCategory;
                        z = !preferenceCategory2.isDividerLineNeeded();
                        if (preferenceCategory2.hasTitle()) {
                            zIsEmpty = false;
                        }
                    } else {
                        zIsEmpty = TextUtils.isEmpty(preferenceCategory.getTitle());
                        z = false;
                    }
                    if (z || zIsEmpty) {
                        if (z) {
                            int[] iArr6 = STATE_SET_NO_LINE;
                            iArr3 = new int[iArr6.length];
                            System.arraycopy(iArr6, 0, iArr3, 0, iArr6.length);
                        } else {
                            iArr3 = new int[0];
                        }
                        if (zIsEmpty) {
                            int[] iArr7 = STATE_SET_NO_TITLE;
                            iArr4 = new int[iArr7.length];
                            System.arraycopy(iArr7, 0, iArr4, 0, iArr7.length);
                        } else {
                            iArr4 = new int[0];
                        }
                        int[] iArr8 = new int[iArr3.length + iArr4.length + iArr2.length];
                        System.arraycopy(iArr3, 0, iArr8, 0, iArr3.length);
                        System.arraycopy(iArr4, 0, iArr8, iArr3.length, iArr4.length);
                        System.arraycopy(iArr2, 0, iArr8, iArr3.length + iArr4.length, iArr2.length);
                        iArr2 = iArr8;
                    }
                }
                PositionDescriptor positionDescriptor6 = this.mDescriptors[i];
                positionDescriptor6.status = iArr2;
                positionDescriptor6.type = i2;
            }
        }
        return this.mDescriptors[i].type;
    }

    private boolean isNeedCardGroup(Preference preference) {
        if (!this.mIsEnableCardStyle) {
            return false;
        }
        PreferenceGroup parent = preference.getParent();
        if ((parent instanceof RadioButtonPreferenceCategory) && (preference instanceof RadioButtonPreference)) {
            return ((RadioButtonPreferenceCategory) parent).isNeedCardGroup();
        }
        if ((parent instanceof SingleChoicePreferenceCategory) && (preference instanceof SingleChoicePreference)) {
            return ((SingleChoicePreferenceCategory) parent).getCardGroupEnabled();
        }
        if ((parent instanceof MultiChoicePreferenceCategory) && (preference instanceof MultiChoicePreference)) {
            return ((MultiChoicePreferenceCategory) parent).getCardGroupEnabled();
        }
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        registerAdapterDataObserver(this.mObserver);
        this.mRecyclerView = recyclerView;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        unregisterAdapterDataObserver(this.mObserver);
        this.mRecyclerView = null;
    }

    int getPositionType(int i) {
        return this.mDescriptors[i].type;
    }

    @Override // miuix.animation.internal.BlinkStateObserver
    public void updateBlinkState(boolean z) {
        RecyclerView recyclerView;
        if (!z || (recyclerView = this.mRecyclerView) == null) {
            return;
        }
        recyclerView.removeOnItemTouchListener(this.mItemTouchOnHighlightListener);
        this.mRecyclerView.setOnTouchListener(null);
        this.mItemTouchOnHighlightListener = null;
        this.mParentTouchOnHighlightListener = null;
        FolmeBlink folmeBlink = this.mFolmeBlink;
        if (folmeBlink != null) {
            folmeBlink.detach(this);
        }
    }

    class PositionDescriptor {
        int[] status;
        int type;

        PositionDescriptor() {
        }
    }

    public List getAnimatorPreferenceGroups() {
        return this.mAnimatorPreferenceGroups;
    }

    public void clearPreferenceGroups() {
        if (this.mAnimatorPreferenceGroups.isEmpty()) {
            return;
        }
        this.mAnimatorPreferenceGroups.clear();
    }

    @Override // androidx.preference.PreferenceGroupAdapter, androidx.preference.Preference.OnPreferenceChangeInternalListener
    public void onPreferenceVisibilityChange(Preference preference) {
        PreferenceGroup parent;
        super.onPreferenceVisibilityChange(preference);
        if ((preference instanceof PreferenceGroup) || (preference.getParent() instanceof PreferenceScreen) || (parent = preference.getParent()) == null || this.mAnimatorPreferenceGroups.contains(parent)) {
            return;
        }
        this.mAnimatorPreferenceGroups.add(parent);
    }

    @Override // androidx.preference.PreferenceGroupAdapter, androidx.preference.Preference.OnPreferenceChangeInternalListener
    public void onPreferenceChange(Preference preference) {
        Preference preferenceFindPreference;
        super.onPreferenceChange(preference);
        String dependency = preference.getDependency();
        if (TextUtils.isEmpty(dependency) || (preferenceFindPreference = preference.getPreferenceManager().findPreference(dependency)) == null) {
            return;
        }
        if (preference instanceof androidx.preference.PreferenceCategory) {
            if (preferenceFindPreference instanceof TwoStatePreference) {
                preference.setVisible(((TwoStatePreference) preferenceFindPreference).isChecked());
                return;
            } else {
                preference.setVisible(preferenceFindPreference.isEnabled());
                return;
            }
        }
        preference.setVisible(preference.isEnabled());
    }

    private List getAllVisiblePreferences(PreferenceGroup preferenceGroup) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
            Preference preference = preferenceGroup.getPreference(i);
            if (preference.isVisible()) {
                arrayList.add(preference);
            }
        }
        return arrayList;
    }
}
