package miuix.flexible.template;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.services.telephony.relay.network.SmartNetworkRelayScore;
import com.xiaomi.internal.telephony.DynamicSarService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import miuix.core.util.MiuixUIUtils;
import miuix.flexible.R$styleable;
import miuix.flexible.mark.MarkHelper;
import miuix.flexible.mark.ViewList;
import miuix.flexible.mark.ViewNode;
import miuix.flexible.template.level.FontLevelSupplier;
import miuix.flexible.template.level.LevelSupplier;
import miuix.flexible.view.HyperCellLayout;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractMarkTemplate implements IHyperCellTemplate, MarkHelper.IParamsGetter {
    protected static final int NOT_SET = Integer.MAX_VALUE;
    protected Context mContext;
    protected float mDensity;
    protected int mLevel;
    private HyperCellLayout.LevelCallback mLevelCallback;
    private LevelSupplier mLevelSupplier;
    private ViewList mViewList;
    private int mGravity = 0;
    private boolean mFinishInflate = false;
    private int mColumnSpacing = 0;
    private int mRowSpacing = 0;

    public void applyLevel() {
    }

    public abstract HyperCellLayout.LayoutParams getLayoutParams(View view);

    public void onAddAuxiliaryViews(ViewGroup viewGroup) {
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public void onAttachedToWindow(ViewGroup viewGroup) {
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public void onDetachedFromWindow(ViewGroup viewGroup) {
    }

    public void onPreBuildViewTree(ViewGroup viewGroup) {
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public void init(ViewGroup viewGroup, Context context, AttributeSet attributeSet) {
        this.mContext = context;
        this.mDensity = context.getResources().getDisplayMetrics().density;
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.HyperCellLayout);
            int indexCount = typedArrayObtainStyledAttributes.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = typedArrayObtainStyledAttributes.getIndex(i);
                if (index == R$styleable.HyperCellLayout_android_gravity) {
                    this.mGravity = typedArrayObtainStyledAttributes.getInt(index, 0);
                } else if (index == R$styleable.HyperCellLayout_column_spacing) {
                    this.mColumnSpacing = (int) (typedArrayObtainStyledAttributes.getDimension(index, 0.0f) + 0.5f);
                } else if (index == R$styleable.HyperCellLayout_row_spacing) {
                    this.mRowSpacing = (int) (typedArrayObtainStyledAttributes.getDimension(index, 0.0f) + 0.5f);
                }
            }
            typedArrayObtainStyledAttributes.recycle();
        }
        this.mLevelSupplier = createLevelSupplier();
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public void onFinishInflate(ViewGroup viewGroup) {
        onAddAuxiliaryViews(viewGroup);
        onPreBuildViewTree(viewGroup);
        buildViewTree(viewGroup);
        this.mFinishInflate = true;
        applyLevel();
    }

    @Override // miuix.flexible.mark.MarkHelper.IParamsGetter
    public int getMark(View view) {
        return getLayoutParams(view).getMark();
    }

    @Override // miuix.flexible.mark.MarkHelper.IParamsGetter
    public float getWeight(View view) {
        HyperCellLayout.LayoutParams childViewLayoutParamsSafe = getChildViewLayoutParamsSafe(view);
        if ((childViewLayoutParamsSafe.getCustomParams() & 512) != 0) {
            return childViewLayoutParamsSafe.getWeight();
        }
        return getLayoutParams(view).getWeight();
    }

    @Override // miuix.flexible.mark.MarkHelper.IParamsGetter
    public float getGroupWeight(View view) {
        HyperCellLayout.LayoutParams childViewLayoutParamsSafe = getChildViewLayoutParamsSafe(view);
        if ((childViewLayoutParamsSafe.getCustomParams() & SmartNetworkRelayScore.TRAFFIC_UNIT) != 0) {
            return childViewLayoutParamsSafe.getGroupWeight();
        }
        return getLayoutParams(view).getGroupWeight();
    }

    @Override // miuix.flexible.mark.MarkHelper.IParamsGetter
    public int getOrder(View view) {
        return getLayoutParams(view).getOrder();
    }

    public void buildViewTree(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        View[] viewArr = new View[childCount];
        for (int i = 0; i < childCount; i++) {
            viewArr[i] = viewGroup.getChildAt(i);
        }
        this.mViewList = MarkHelper.buildViewNodeTree(viewArr, this);
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public void onViewAdded(ViewGroup viewGroup, View view) {
        if (this.mFinishInflate) {
            onPreBuildViewTree(viewGroup);
            buildViewTree(viewGroup);
        }
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public void onViewRemoved(ViewGroup viewGroup, View view) {
        if (this.mFinishInflate) {
            onPreBuildViewTree(viewGroup);
            buildViewTree(viewGroup);
        }
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public void onConfigurationChanged(ViewGroup viewGroup, Configuration configuration) {
        float f = this.mContext.getResources().getDisplayMetrics().density;
        int i = this.mLevel;
        getLevel();
        if (this.mFinishInflate) {
            if (Math.abs(f - this.mDensity) > 0.001f || i != this.mLevel) {
                onPreBuildViewTree(viewGroup);
                buildViewTree(viewGroup);
                viewGroup.requestLayout();
                if (i != this.mLevel) {
                    applyLevel();
                }
            }
        }
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public int[] onMeasure(ViewGroup viewGroup, int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        ViewList viewList = this.mViewList;
        if (viewList != null) {
            measureViewList(null, viewList, (size - viewGroup.getPaddingStart()) - viewGroup.getPaddingEnd(), (size2 - viewGroup.getPaddingTop()) - viewGroup.getPaddingBottom(), mode, mode2);
            if (mode != 1073741824) {
                size = this.mViewList.getWidth() + viewGroup.getPaddingStart() + viewGroup.getPaddingEnd();
            }
            if (mode2 != 1073741824) {
                size2 = this.mViewList.getHeight() + viewGroup.getPaddingTop() + viewGroup.getPaddingBottom();
            }
            return new int[]{Math.max(size, viewGroup.getMinimumWidth()), Math.max(size2, viewGroup.getMinimumHeight())};
        }
        return new int[]{viewGroup.getMinimumWidth(), viewGroup.getMinimumHeight()};
    }

    private void measureViewList(ViewList viewList, ViewList viewList2, int i, int i2, int i3, int i4) {
        int i5;
        float f;
        int i6;
        int iMax;
        int iMax2;
        final AbstractMarkTemplate abstractMarkTemplate = this;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(viewList2.getList());
        Collections.sort(arrayList2, new Comparator() { // from class: miuix.flexible.template.AbstractMarkTemplate$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return AbstractMarkTemplate.m5462$r8$lambda$7DiWQ5bM4IlYkKeKPw_XRWMDNw(this.f$0, (ViewNode) obj, (ViewNode) obj2);
            }
        });
        int size = arrayList2.size();
        float f2 = 0.0f;
        int i7 = 0;
        int width = 0;
        int height = 0;
        float weight = 0.0f;
        while (i7 < size) {
            int i8 = i7 + 1;
            ViewNode viewNode = (ViewNode) arrayList2.get(i7);
            if (viewNode.getWeight() > 0.0f) {
                weight += viewNode.getWeight();
                arrayList.add(viewNode);
            } else {
                int i9 = viewList2.getOrientation() == 1 ? i - width : i;
                int i10 = viewList2.getOrientation() == 1 ? i2 : i2 - height;
                if (viewNode instanceof ViewList) {
                    ViewList viewList3 = (ViewList) viewNode;
                    abstractMarkTemplate.measureViewList(viewList2, viewList3, i9, i10, i3, i4);
                    if (viewList2.getOrientation() == 1) {
                        width += viewList3.getWidth() + abstractMarkTemplate.mColumnSpacing;
                        iMax2 = Math.max(height, viewList3.getHeight());
                    } else {
                        width = Math.max(width, viewList3.getWidth());
                        iMax2 = viewList3.getHeight() + abstractMarkTemplate.mRowSpacing + height;
                    }
                } else {
                    int i11 = i9;
                    int i12 = i10;
                    if (viewNode.getView() != null && viewNode.getView().getVisibility() != 8) {
                        abstractMarkTemplate.measureChildView(viewNode, i11, i12, i3, i4);
                        if (viewList2.getOrientation() == 1) {
                            width += viewNode.getWidth() + abstractMarkTemplate.mColumnSpacing;
                            iMax2 = Math.max(height, viewNode.getHeight());
                        } else {
                            int iMax3 = Math.max(width, viewNode.getWidth());
                            height += viewNode.getHeight() + abstractMarkTemplate.mRowSpacing;
                            width = iMax3;
                        }
                    }
                }
                height = iMax2;
            }
            i7 = i8;
        }
        int i13 = 1;
        if (!arrayList.isEmpty()) {
            int size2 = (i - width) - ((arrayList.size() - 1) * abstractMarkTemplate.mColumnSpacing);
            int size3 = (i2 - height) - ((arrayList.size() - 1) * abstractMarkTemplate.mRowSpacing);
            int size4 = arrayList.size();
            int i14 = 0;
            while (i14 < size4) {
                int i15 = i14 + 1;
                ViewNode viewNode2 = (ViewNode) arrayList.get(i14);
                int weight2 = viewList2.getOrientation() == i13 ? (int) ((size2 * viewNode2.getWeight()) / weight) : i;
                int weight3 = viewList2.getOrientation() == i13 ? i2 : (int) ((size3 * viewNode2.getWeight()) / weight);
                if (viewNode2 instanceof ViewList) {
                    ViewList viewList4 = (ViewList) viewNode2;
                    i5 = size4;
                    f = f2;
                    i6 = i13;
                    abstractMarkTemplate.measureViewList(viewList2, viewList4, weight2, weight3, i3, i4);
                    if (viewList2.getOrientation() == i6) {
                        width += viewList4.getWidth() + abstractMarkTemplate.mColumnSpacing;
                        iMax = Math.max(height, viewList4.getHeight());
                    } else {
                        width = Math.max(width, viewList4.getWidth());
                        iMax = viewList4.getHeight() + abstractMarkTemplate.mRowSpacing + height;
                    }
                } else {
                    i5 = size4;
                    f = f2;
                    i6 = i13;
                    if (viewNode2.getView() != null && viewNode2.getView().getVisibility() != 8) {
                        abstractMarkTemplate.measureChildView(viewNode2, weight2, weight3, i3, i4);
                        if (viewList2.getOrientation() == i6) {
                            width += viewNode2.getWidth() + abstractMarkTemplate.mColumnSpacing;
                            iMax = Math.max(height, viewNode2.getHeight());
                        } else {
                            int iMax4 = Math.max(width, viewNode2.getWidth());
                            height += viewNode2.getHeight() + abstractMarkTemplate.mRowSpacing;
                            width = iMax4;
                        }
                    }
                    i13 = i6;
                    i14 = i15;
                    size4 = i5;
                    f2 = f;
                }
                height = iMax;
                i13 = i6;
                i14 = i15;
                size4 = i5;
                f2 = f;
            }
        }
        float f3 = f2;
        int i16 = i13;
        ViewList viewList5 = viewList2;
        int i17 = viewList5.getWeight() > f3 ? i16 : 0;
        if (viewList5.getOrientation() == i16) {
            width -= abstractMarkTemplate.mColumnSpacing;
        } else {
            height -= abstractMarkTemplate.mRowSpacing;
        }
        if (i17 != 0 && i3 == 1073741824 && (viewList == null || viewList.getOrientation() == i16)) {
            width = i;
        }
        viewList5.setWidth(width);
        if (i17 != 0 && i4 == 1073741824 && (viewList == null || viewList.getOrientation() == 0)) {
            height = i2;
        }
        viewList5.setHeight(height);
        for (ViewNode viewNode3 : viewList5.getList()) {
            if (viewNode3 instanceof ViewList) {
                ViewList viewList6 = (ViewList) viewNode3;
                if (viewList5.getOrientation() == i16 && viewList6.getHeight() < viewList5.getHeight() && abstractMarkTemplate.hasMatchParentChild(viewList6, 0)) {
                    abstractMarkTemplate.measureViewList(viewList2, viewList6, viewList6.getWidth(), viewList5.getHeight(), 1073741824, 1073741824);
                } else if (viewList2.getOrientation() == 0 && viewList6.getWidth() < viewList2.getWidth() && abstractMarkTemplate.hasMatchParentChild(viewList6, i16)) {
                    abstractMarkTemplate.measureViewList(viewList2, viewList6, viewList2.getWidth(), viewList6.getHeight(), 1073741824, 1073741824);
                }
            } else if (viewNode3.getView() != null && viewNode3.getView().getVisibility() != 8) {
                HyperCellLayout.LayoutParams childViewLayoutParamsSafe = abstractMarkTemplate.getChildViewLayoutParamsSafe(viewNode3.getView());
                if (((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).height == -1 && viewList2.getOrientation() == i16 && viewNode3.getHeight() < viewList2.getHeight()) {
                    abstractMarkTemplate.measureChildView(viewNode3, viewNode3.getWidth(), viewList2.getHeight(), 1073741824, 1073741824);
                } else if (((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).width == -1 && viewList2.getOrientation() == 0 && viewNode3.getWidth() < viewList2.getWidth()) {
                    measureChildView(viewNode3, viewList2.getWidth(), viewNode3.getHeight(), 1073741824, 1073741824);
                }
            }
            abstractMarkTemplate = this;
            viewList5 = viewList2;
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$7DiWQ5bM4IlYkKeKPw_XRWMDN-w, reason: not valid java name */
    public static /* synthetic */ int m5462$r8$lambda$7DiWQ5bM4IlYkKeKPw_XRWMDNw(AbstractMarkTemplate abstractMarkTemplate, ViewNode viewNode, ViewNode viewNode2) {
        return abstractMarkTemplate.getViewNodePriority(viewNode2) - abstractMarkTemplate.getViewNodePriority(viewNode);
    }

    /* JADX WARN: Code duplicated, block: B:18:0x003a  */
    /* JADX WARN: Code duplicated, block: B:20:0x0042 A[DONT_INVERT] */
    /* JADX WARN: Code duplicated, block: B:21:0x0044  */
    /* JADX WARN: Code duplicated, block: B:25:0x004b  */
    /* JADX WARN: Code duplicated, block: B:27:0x0052  */
    /* JADX WARN: Code duplicated, block: B:30:0x007c  */
    /* JADX WARN: Code duplicated, block: B:32:0x0084  */
    /* JADX WARN: Code duplicated, block: B:35:0x0093  */
    private void measureChildView(ViewNode viewNode, int i, int i2, int i3, int i4) {
        int marginStart;
        int marginEnd;
        int i5;
        int i6;
        int i7;
        int measuredWidth;
        int measuredHeight;
        View view = viewNode.getView();
        HyperCellLayout.LayoutParams childViewLayoutParamsSafe = getChildViewLayoutParamsSafe(view);
        if (childViewLayoutParamsSafe != null) {
            int i8 = ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).width;
            if (i8 == -1) {
                marginStart = i - childViewLayoutParamsSafe.getMarginStart();
                marginEnd = childViewLayoutParamsSafe.getMarginEnd();
            } else {
                if (i8 == -2) {
                    i3 = (i3 == 1073741824 || i3 == Integer.MIN_VALUE) ? Integer.MIN_VALUE : 0;
                    marginStart = i - childViewLayoutParamsSafe.getMarginStart();
                    marginEnd = childViewLayoutParamsSafe.getMarginEnd();
                } else {
                    i3 = 1073741824;
                }
                i5 = ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).height;
                if (i5 == -1) {
                    i6 = i2 - ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).topMargin;
                    i7 = ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).bottomMargin;
                } else {
                    if (i5 == -2) {
                        if (i4 != 1073741824 || i4 == Integer.MIN_VALUE) {
                            i4 = Integer.MIN_VALUE;
                        } else {
                            i4 = 0;
                        }
                        i6 = i2 - ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).topMargin;
                        i7 = ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).bottomMargin;
                    } else {
                        i4 = 1073741824;
                    }
                    view.measure(View.MeasureSpec.makeMeasureSpec(i8, i3), View.MeasureSpec.makeMeasureSpec(i5, i4));
                    measuredWidth = view.getMeasuredWidth() + childViewLayoutParamsSafe.getMarginStart() + childViewLayoutParamsSafe.getMarginEnd();
                    measuredHeight = view.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).topMargin + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).bottomMargin;
                    if (childViewLayoutParamsSafe.isAnimating()) {
                        if ((childViewLayoutParamsSafe.getAnimSpec() & 1) > 0) {
                            measuredWidth = (int) (measuredWidth * childViewLayoutParamsSafe.getAnimationProgress());
                        }
                        if ((childViewLayoutParamsSafe.getAnimSpec() & 2) > 0) {
                            measuredHeight = (int) (measuredHeight * childViewLayoutParamsSafe.getAnimationProgress());
                        }
                    }
                    viewNode.setWidth(measuredWidth);
                    viewNode.setHeight(measuredHeight);
                }
                i5 = i6 - i7;
                view.measure(View.MeasureSpec.makeMeasureSpec(i8, i3), View.MeasureSpec.makeMeasureSpec(i5, i4));
                measuredWidth = view.getMeasuredWidth() + childViewLayoutParamsSafe.getMarginStart() + childViewLayoutParamsSafe.getMarginEnd();
                measuredHeight = view.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).topMargin + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).bottomMargin;
                if (childViewLayoutParamsSafe.isAnimating()) {
                    if ((childViewLayoutParamsSafe.getAnimSpec() & 1) > 0) {
                        measuredWidth = (int) (measuredWidth * childViewLayoutParamsSafe.getAnimationProgress());
                    }
                    if ((childViewLayoutParamsSafe.getAnimSpec() & 2) > 0) {
                        measuredHeight = (int) (measuredHeight * childViewLayoutParamsSafe.getAnimationProgress());
                    }
                }
                viewNode.setWidth(measuredWidth);
                viewNode.setHeight(measuredHeight);
            }
            i8 = marginStart - marginEnd;
            i5 = ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).height;
            if (i5 == -1) {
                i6 = i2 - ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).topMargin;
                i7 = ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).bottomMargin;
            } else {
                if (i5 == -2) {
                    if (i4 != 1073741824) {
                        i4 = Integer.MIN_VALUE;
                    } else {
                        i4 = Integer.MIN_VALUE;
                    }
                    i6 = i2 - ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).topMargin;
                    i7 = ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).bottomMargin;
                } else {
                    i4 = 1073741824;
                }
                view.measure(View.MeasureSpec.makeMeasureSpec(i8, i3), View.MeasureSpec.makeMeasureSpec(i5, i4));
                measuredWidth = view.getMeasuredWidth() + childViewLayoutParamsSafe.getMarginStart() + childViewLayoutParamsSafe.getMarginEnd();
                measuredHeight = view.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).topMargin + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).bottomMargin;
                if (childViewLayoutParamsSafe.isAnimating()) {
                    if ((childViewLayoutParamsSafe.getAnimSpec() & 1) > 0) {
                        measuredWidth = (int) (measuredWidth * childViewLayoutParamsSafe.getAnimationProgress());
                    }
                    if ((childViewLayoutParamsSafe.getAnimSpec() & 2) > 0) {
                        measuredHeight = (int) (measuredHeight * childViewLayoutParamsSafe.getAnimationProgress());
                    }
                }
                viewNode.setWidth(measuredWidth);
                viewNode.setHeight(measuredHeight);
            }
            i5 = i6 - i7;
            view.measure(View.MeasureSpec.makeMeasureSpec(i8, i3), View.MeasureSpec.makeMeasureSpec(i5, i4));
            measuredWidth = view.getMeasuredWidth() + childViewLayoutParamsSafe.getMarginStart() + childViewLayoutParamsSafe.getMarginEnd();
            measuredHeight = view.getMeasuredHeight() + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).topMargin + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).bottomMargin;
            if (childViewLayoutParamsSafe.isAnimating()) {
                if ((childViewLayoutParamsSafe.getAnimSpec() & 1) > 0) {
                    measuredWidth = (int) (measuredWidth * childViewLayoutParamsSafe.getAnimationProgress());
                }
                if ((childViewLayoutParamsSafe.getAnimSpec() & 2) > 0) {
                    measuredHeight = (int) (measuredHeight * childViewLayoutParamsSafe.getAnimationProgress());
                }
            }
            viewNode.setWidth(measuredWidth);
            viewNode.setHeight(measuredHeight);
        }
    }

    protected int getViewNodePriority(ViewNode viewNode) {
        if (viewNode.getView() != null) {
            return getChildViewLayoutParamsSafe(viewNode.getView()).getPriority();
        }
        if (viewNode instanceof ViewList) {
            return getViewNodePriority((ViewNode) ((ViewList) viewNode).getList().get(0));
        }
        return 0;
    }

    protected boolean hasMatchParentChild(ViewList viewList, int i) {
        for (ViewNode viewNode : viewList.getList()) {
            if (viewNode instanceof ViewList) {
                if (hasMatchParentChild((ViewList) viewNode, i)) {
                    return true;
                }
            } else if (viewNode.getView() != null) {
                HyperCellLayout.LayoutParams childViewLayoutParamsSafe = getChildViewLayoutParamsSafe(viewNode.getView());
                if (i == 1) {
                    if (((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).width == -1) {
                        return true;
                    }
                } else if (((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe).height == -1) {
                    return true;
                }
            } else {
                continue;
            }
        }
        return false;
    }

    @Override // miuix.flexible.template.IHyperCellTemplate
    public void onLayout(ViewGroup viewGroup, boolean z, int i, int i2, int i3, int i4) {
        if (this.mViewList != null) {
            boolean z2 = viewGroup.getLayoutDirection() == 1;
            int i5 = i3 - i;
            int i6 = i4 - i2;
            int paddingStart = viewGroup.getPaddingStart();
            int paddingTop = viewGroup.getPaddingTop();
            int i7 = this.mGravity;
            int i8 = i7 & 112;
            int i9 = i7 & 7;
            if (i8 == 16) {
                paddingTop = viewGroup.getPaddingTop() + ((((i6 - viewGroup.getPaddingTop()) - viewGroup.getPaddingBottom()) - this.mViewList.getHeight()) / 2);
            } else if (i8 == 80) {
                paddingTop = (i6 - this.mViewList.getHeight()) - viewGroup.getPaddingBottom();
            }
            int i10 = paddingTop;
            if (i9 == 1) {
                paddingStart = viewGroup.getPaddingStart() + ((((i5 - viewGroup.getPaddingStart()) - viewGroup.getPaddingEnd()) - this.mViewList.getWidth()) / 2);
            } else if (i9 == 5) {
                paddingStart = (i5 - this.mViewList.getWidth()) - viewGroup.getPaddingEnd();
            }
            layoutViewList(this.mViewList, z2, i5, paddingStart, i10);
        }
    }

    /* JADX WARN: Code duplicated, block: B:35:0x00d3  */
    /* JADX WARN: Code duplicated, block: B:36:0x00dd  */
    private void layoutViewList(ViewList viewList, boolean z, int i, int i2, int i3) {
        int i4;
        int i5;
        int i6;
        int width;
        int height;
        AbstractMarkTemplate abstractMarkTemplate = this;
        int width2 = i2 + viewList.getWidth();
        int height2 = i3 + viewList.getHeight();
        ArrayList arrayList = new ArrayList(viewList.getList());
        int i7 = 1;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            ViewNode viewNode = (ViewNode) arrayList.get(size);
            View view = viewNode.getView();
            if (view != null) {
                int gravity = abstractMarkTemplate.getChildViewLayoutParamsSafe(view).getGravity();
                if ((viewList.getOrientation() == 1 && (gravity & 7) == 5) || (viewList.getOrientation() == 0 && (gravity & 112) == 80)) {
                    arrayList.remove(size);
                    arrayList.add(viewNode);
                }
            }
        }
        int size2 = arrayList.size();
        int i8 = 0;
        int width3 = i2;
        int height3 = i3;
        int width4 = width2;
        int height4 = height2;
        int i9 = 0;
        while (i9 < size2) {
            int i10 = i9 + 1;
            ViewNode viewNode2 = (ViewNode) arrayList.get(i9);
            if (viewNode2 instanceof ViewList) {
                ViewList viewList2 = (ViewList) viewNode2;
                HyperCellLayout.LayoutParams childViewLayoutParamsSafe = abstractMarkTemplate.getChildViewLayoutParamsSafe(((ViewNode) viewList2.getList().get(i8)).getView());
                if (viewList.getOrientation() == i7 && (childViewLayoutParamsSafe.getGravity() & 112) == 16) {
                    height = ((viewList.getHeight() - viewList2.getHeight()) / 2) + height3;
                    width = width3;
                } else {
                    if (viewList.getOrientation() == 0 && (childViewLayoutParamsSafe.getGravity() & 7) == i7) {
                        width = ((viewList.getWidth() - viewList2.getWidth()) / 2) + width3;
                        height = height3;
                    } else {
                        i = i;
                        width = width3;
                        height = height3;
                    }
                    abstractMarkTemplate.layoutViewList(viewList2, z, i, width, height);
                    if (viewList.getOrientation() == i7) {
                        width3 += viewList2.getWidth() + abstractMarkTemplate.mColumnSpacing;
                    } else {
                        height3 += viewList2.getHeight() + abstractMarkTemplate.mRowSpacing;
                    }
                }
                abstractMarkTemplate.layoutViewList(viewList2, z, i, width, height);
                if (viewList.getOrientation() == i7) {
                    width3 += viewList2.getWidth() + abstractMarkTemplate.mColumnSpacing;
                } else {
                    height3 += viewList2.getHeight() + abstractMarkTemplate.mRowSpacing;
                }
            } else if (viewNode2.getView() != null && viewNode2.getView().getVisibility() != 8) {
                View view2 = viewNode2.getView();
                HyperCellLayout.LayoutParams childViewLayoutParamsSafe2 = abstractMarkTemplate.getChildViewLayoutParamsSafe(view2);
                int marginStart = childViewLayoutParamsSafe2.getMarginStart() + width3;
                int measuredHeight = ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe2).topMargin + height3;
                if (viewList.getOrientation() == i7) {
                    if ((childViewLayoutParamsSafe2.getGravity() & 7) == 5) {
                        marginStart = (width4 - view2.getMeasuredWidth()) - childViewLayoutParamsSafe2.getMarginEnd();
                        width4 -= viewNode2.getWidth() + abstractMarkTemplate.mColumnSpacing;
                    } else {
                        width3 += viewNode2.getWidth() + abstractMarkTemplate.mColumnSpacing;
                    }
                    if ((childViewLayoutParamsSafe2.getGravity() & 112) == 80) {
                        measuredHeight = ((viewList.getHeight() + height3) - view2.getMeasuredHeight()) - ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe2).bottomMargin;
                    } else if ((childViewLayoutParamsSafe2.getGravity() & 112) == 16) {
                        measuredHeight = ((viewList.getHeight() - viewNode2.getHeight()) / 2) + height3 + ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe2).topMargin;
                    }
                } else {
                    if ((childViewLayoutParamsSafe2.getGravity() & 112) == 80) {
                        measuredHeight = (height4 - view2.getMeasuredHeight()) - ((ViewGroup.MarginLayoutParams) childViewLayoutParamsSafe2).bottomMargin;
                        height4 -= viewNode2.getHeight() + abstractMarkTemplate.mRowSpacing;
                    } else {
                        height3 += viewNode2.getHeight() + abstractMarkTemplate.mRowSpacing;
                    }
                    if ((childViewLayoutParamsSafe2.getGravity() & 7) == 5) {
                        marginStart = ((viewList.getWidth() + width3) - view2.getMeasuredWidth()) - childViewLayoutParamsSafe2.getMarginEnd();
                    } else {
                        i7 = 1;
                        if ((childViewLayoutParamsSafe2.getGravity() & 7) == 1) {
                            marginStart = ((viewList.getWidth() - viewNode2.getWidth()) / 2) + width3 + childViewLayoutParamsSafe2.getMarginStart();
                        }
                        i4 = marginStart;
                        i5 = measuredHeight;
                        i6 = height4;
                    }
                    int i11 = width3;
                    int i12 = width4;
                    int i13 = height3;
                    abstractMarkTemplate.layoutChildView(view2, z, i, i4, i5, i4 + view2.getMeasuredWidth(), i5 + view2.getMeasuredHeight());
                    width3 = i11;
                    height3 = i13;
                    width4 = i12;
                    height4 = i6;
                }
                i4 = marginStart;
                i5 = measuredHeight;
                i6 = height4;
                i7 = 1;
                int i14 = width3;
                int i15 = width4;
                int i16 = height3;
                abstractMarkTemplate.layoutChildView(view2, z, i, i4, i5, i4 + view2.getMeasuredWidth(), i5 + view2.getMeasuredHeight());
                width3 = i14;
                height3 = i16;
                width4 = i15;
                height4 = i6;
            }
            i8 = 0;
            abstractMarkTemplate = this;
            i9 = i10;
        }
    }

    public void layoutChildView(View view, boolean z, int i, int i2, int i3, int i4, int i5) {
        int i6 = z ? i - i4 : i2;
        if (z) {
            i4 = i - i2;
        }
        view.layout(i6, i3, i4, i5);
    }

    public final int getLevel() {
        int level = this.mLevelSupplier.getLevel();
        this.mLevel = level;
        return level;
    }

    public LevelSupplier createLevelSupplier() {
        return new FontLevelSupplier(this.mContext);
    }

    public void setLevelCallback(HyperCellLayout.LevelCallback levelCallback) {
        applyLevel();
    }

    protected static HyperCellLayout.LayoutParams generateAuxiliaryLayoutParams(int i) {
        return generateAuxiliaryLayoutParams(i, 0, 0);
    }

    protected static HyperCellLayout.LayoutParams generateAuxiliaryLayoutParams(int i, int i2, int i3) {
        HyperCellLayout.LayoutParams layoutParams = new HyperCellLayout.LayoutParams(i2, i3);
        layoutParams.setAreaId(i);
        return layoutParams;
    }

    protected static void addAuxiliaryView(ViewGroup viewGroup, Context context, int i) {
        addAuxiliaryView(viewGroup, context, i, 0, 0);
    }

    protected static void addAuxiliaryView(ViewGroup viewGroup, Context context, int i, int i2, int i3) {
        View view = new View(context);
        view.setWillNotDraw(true);
        HyperCellLayout.LayoutParams layoutParamsGenerateAuxiliaryLayoutParams = generateAuxiliaryLayoutParams(i, i2, i3);
        layoutParamsGenerateAuxiliaryLayoutParams.setAreaId(i);
        viewGroup.addView(view, layoutParamsGenerateAuxiliaryLayoutParams);
    }

    protected HyperCellLayout.LayoutParams getChildViewLayoutParamsSafe(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof HyperCellLayout.LayoutParams) {
            return (HyperCellLayout.LayoutParams) layoutParams;
        }
        StringBuilder sb = new StringBuilder("LayoutParams ");
        sb.append(layoutParams);
        sb.append(" of child view ");
        sb.append(view);
        Context context = view.getContext();
        sb.append(" is not instance of HyperCellLayout.LayoutParams! Context is ");
        sb.append(context);
        throw new IllegalArgumentException(sb.toString());
    }

    protected static HyperCellLayout.LayoutParams generateLayoutParams(int i, float f, float f2, int i2, int i3) {
        return generateLayoutParams(i, f, f2, i2, i3, NOT_SET, NOT_SET, NOT_SET, NOT_SET);
    }

    protected static HyperCellLayout.LayoutParams generateLayoutParams(int i, float f, float f2, int i2, int i3, int i4, int i5, int i6, int i7) {
        HyperCellLayout.LayoutParams layoutParams = new HyperCellLayout.LayoutParams(0, 0);
        layoutParams.setMark(i);
        layoutParams.setWeight(f);
        layoutParams.setGroupWeight(f2);
        layoutParams.setGravity(i2);
        layoutParams.setOrder(i3);
        layoutParams.setMarginStart(i4);
        layoutParams.setMarginEnd(i6);
        ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = i5;
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = i7;
        return layoutParams;
    }

    protected void setMargin(HyperCellLayout.LayoutParams layoutParams, HyperCellLayout.LayoutParams layoutParams2) {
        int i;
        int i2;
        if ((layoutParams.getCustomParams() & 2) == 0 && layoutParams2.getMarginStart() != NOT_SET) {
            layoutParams.setMarginStart(dp2px(layoutParams2.getMarginStart()));
        }
        if ((layoutParams.getCustomParams() & 4) == 0 && layoutParams2.getMarginEnd() != NOT_SET) {
            layoutParams.setMarginEnd(dp2px(layoutParams2.getMarginEnd()));
        }
        if ((layoutParams.getCustomParams() & 8) == 0 && (i2 = ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin) != NOT_SET) {
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = dp2px(i2);
        }
        if ((layoutParams.getCustomParams() & 16) != 0 || (i = ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin) == NOT_SET) {
            return;
        }
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = dp2px(i);
    }

    protected void setGravity(HyperCellLayout.LayoutParams layoutParams, HyperCellLayout.LayoutParams layoutParams2) {
        if ((layoutParams.getCustomParams() & 1) != 0 || layoutParams2.getGravity() == NOT_SET) {
            return;
        }
        layoutParams.setGravity(layoutParams2.getGravity());
    }

    protected void setPriority(HyperCellLayout.LayoutParams layoutParams, HyperCellLayout.LayoutParams layoutParams2) {
        if ((layoutParams.getCustomParams() & DynamicSarService.WIFI_DATA_STATE_AP_VENDOR_IE_NUM_EMPTY) == 0 && layoutParams2.getPriority() != NOT_SET) {
            layoutParams.setPriority(layoutParams2.getPriority());
        }
        if ((layoutParams.getCustomParams() & DynamicSarService.WIFI_DATA_STATE_DHCP_CLIENT_IDENTIFIER) != 0 || layoutParams2.getGroupPriority() == NOT_SET) {
            return;
        }
        layoutParams.setGroupPriority(layoutParams2.getGroupPriority());
    }

    protected void setWidthHeight(HyperCellLayout.LayoutParams layoutParams, HyperCellLayout.LayoutParams layoutParams2) {
        int i;
        int i2;
        if ((layoutParams.getCustomParams() & 32) == 0 && (i2 = ((ViewGroup.MarginLayoutParams) layoutParams2).width) != NOT_SET) {
            ((ViewGroup.MarginLayoutParams) layoutParams).width = i2;
        }
        if ((layoutParams.getCustomParams() & 64) != 0 || (i = ((ViewGroup.MarginLayoutParams) layoutParams2).height) == NOT_SET) {
            return;
        }
        ((ViewGroup.MarginLayoutParams) layoutParams).height = i;
    }

    protected int dp2px(float f) {
        return MiuixUIUtils.dp2px(this.mDensity, f);
    }

    protected View findViewByAreaId(ViewGroup viewGroup, int i) {
        if (viewGroup instanceof HyperCellLayout) {
            return ((HyperCellLayout) viewGroup).findViewByAreaId(i);
        }
        return null;
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setGravity(int i) {
        this.mGravity = i;
    }

    public int getColumnSpacing() {
        return this.mColumnSpacing;
    }

    public void setColumnSpacing(int i) {
        this.mColumnSpacing = i;
    }

    public int getRowSpacing() {
        return this.mRowSpacing;
    }

    public void setRowSpacing(int i) {
        this.mRowSpacing = i;
    }
}
