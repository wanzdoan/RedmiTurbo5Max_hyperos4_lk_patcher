package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintAnchor;
import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.ConstraintWidgetContainer;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class ChainRun extends WidgetRun {
    private int chainStyle;
    ArrayList widgets;

    public ChainRun(ConstraintWidget constraintWidget, int i) {
        super(constraintWidget);
        this.widgets = new ArrayList();
        this.orientation = i;
        build();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ChainRun ");
        sb.append(this.orientation == 0 ? "horizontal : " : "vertical : ");
        ArrayList arrayList = this.widgets;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            sb.append("<");
            sb.append((WidgetRun) obj);
            sb.append("> ");
        }
        return sb.toString();
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    boolean supportsWrapComputation() {
        int size = this.widgets.size();
        for (int i = 0; i < size; i++) {
            if (!((WidgetRun) this.widgets.get(i)).supportsWrapComputation()) {
                return false;
            }
        }
        return true;
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    public long getWrapDimension() {
        int size = this.widgets.size();
        long wrapDimension = 0;
        for (int i = 0; i < size; i++) {
            WidgetRun widgetRun = (WidgetRun) this.widgets.get(i);
            wrapDimension = wrapDimension + ((long) widgetRun.start.margin) + widgetRun.getWrapDimension() + ((long) widgetRun.end.margin);
        }
        return wrapDimension;
    }

    private void build() {
        ConstraintWidget constraintWidget;
        ArrayList arrayList;
        ConstraintWidget constraintWidget2 = this.widget;
        ConstraintWidget previousChainMember = constraintWidget2.getPreviousChainMember(this.orientation);
        while (true) {
            ConstraintWidget constraintWidget3 = previousChainMember;
            constraintWidget = constraintWidget2;
            constraintWidget2 = constraintWidget3;
            if (constraintWidget2 == null) {
                break;
            } else {
                previousChainMember = constraintWidget2.getPreviousChainMember(this.orientation);
            }
        }
        this.widget = constraintWidget;
        this.widgets.add(constraintWidget.getRun(this.orientation));
        ConstraintWidget nextChainMember = constraintWidget.getNextChainMember(this.orientation);
        while (true) {
            arrayList = this.widgets;
            if (nextChainMember == null) {
                break;
            }
            arrayList.add(nextChainMember.getRun(this.orientation));
            nextChainMember = nextChainMember.getNextChainMember(this.orientation);
        }
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            WidgetRun widgetRun = (WidgetRun) obj;
            int i2 = this.orientation;
            if (i2 == 0) {
                widgetRun.widget.horizontalChainRun = this;
            } else if (i2 == 1) {
                widgetRun.widget.verticalChainRun = this;
            }
        }
        if (this.orientation == 0 && ((ConstraintWidgetContainer) this.widget.getParent()).isRtl() && this.widgets.size() > 1) {
            ArrayList arrayList2 = this.widgets;
            this.widget = ((WidgetRun) arrayList2.get(arrayList2.size() - 1)).widget;
        }
        int i3 = this.orientation;
        ConstraintWidget constraintWidget4 = this.widget;
        this.chainStyle = i3 == 0 ? constraintWidget4.getHorizontalChainStyle() : constraintWidget4.getVerticalChainStyle();
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    void clear() {
        this.runGroup = null;
        ArrayList arrayList = this.widgets;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((WidgetRun) obj).clear();
        }
    }

    /* JADX WARN: Code duplicated, block: B:90:0x0160  */
    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun, androidx.constraintlayout.core.widgets.analyzer.Dependency
    public void update(Dependency dependency) {
        int i;
        int i2;
        boolean z;
        float f;
        float f2;
        int i3;
        int i4;
        int i5;
        int i6;
        float f3;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        boolean z2;
        if (this.start.resolved && this.end.resolved) {
            ConstraintWidget parent = this.widget.getParent();
            boolean zIsRtl = parent instanceof ConstraintWidgetContainer ? ((ConstraintWidgetContainer) parent).isRtl() : false;
            int i12 = this.end.value - this.start.value;
            int size = this.widgets.size();
            int i13 = 0;
            while (true) {
                i = -1;
                i2 = 8;
                if (i13 >= size) {
                    i13 = -1;
                    break;
                } else if (((WidgetRun) this.widgets.get(i13)).widget.getVisibility() != 8) {
                    break;
                } else {
                    i13++;
                }
            }
            int i14 = size - 1;
            for (int i15 = i14; i15 >= 0; i15--) {
                if (((WidgetRun) this.widgets.get(i15)).widget.getVisibility() != 8) {
                    i = i15;
                    break;
                }
            }
            int i16 = 0;
            while (true) {
                if (i16 >= 2) {
                    z = zIsRtl;
                    f = 0.0f;
                    f2 = 0.0f;
                    i3 = 0;
                    i4 = 0;
                    i5 = 0;
                    break;
                }
                int i17 = 0;
                i4 = 0;
                i5 = 0;
                int i18 = 0;
                f2 = 0.0f;
                while (i17 < size) {
                    WidgetRun widgetRun = (WidgetRun) this.widgets.get(i17);
                    if (widgetRun.widget.getVisibility() == i2) {
                        z2 = zIsRtl;
                    } else {
                        i18++;
                        if (i17 > 0 && i17 >= i13) {
                            i4 += widgetRun.start.margin;
                        }
                        DimensionDependency dimensionDependency = widgetRun.dimension;
                        int i19 = dimensionDependency.value;
                        boolean z3 = widgetRun.dimensionBehavior != ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT;
                        if (z3) {
                            int i20 = this.orientation;
                            if (i20 == 0 && !widgetRun.widget.horizontalRun.dimension.resolved) {
                                return;
                            }
                            if (i20 == 1 && !widgetRun.widget.verticalRun.dimension.resolved) {
                                return;
                            } else {
                                z2 = zIsRtl;
                            }
                        } else {
                            z2 = zIsRtl;
                            if (widgetRun.matchConstraintsType == 1 && i16 == 0) {
                                i19 = dimensionDependency.wrapValue;
                                i5++;
                            } else if (dimensionDependency.resolved) {
                            }
                            z3 = true;
                        }
                        if (z3) {
                            i4 += i19;
                        } else {
                            i5++;
                            float f4 = widgetRun.widget.mWeight[this.orientation];
                            if (f4 >= 0.0f) {
                                f2 += f4;
                            }
                        }
                        if (i17 < i14 && i17 < i) {
                            i4 += -widgetRun.end.margin;
                        }
                    }
                    i17++;
                    zIsRtl = z2;
                    i2 = 8;
                }
                z = zIsRtl;
                f = 0.0f;
                if (i4 < i12 || i5 == 0) {
                    i3 = i18;
                    break;
                } else {
                    i16++;
                    zIsRtl = z;
                    i2 = 8;
                }
            }
            int i21 = this.start.value;
            if (z) {
                i21 = this.end.value;
            }
            float f5 = 0.5f;
            if (i4 > i12) {
                i21 = z ? i21 + ((int) (((i4 - i12) / 2.0f) + 0.5f)) : i21 - ((int) (((i4 - i12) / 2.0f) + 0.5f));
            }
            if (i5 > 0) {
                float f6 = i12 - i4;
                int i22 = (int) ((f6 / i5) + 0.5f);
                int i23 = 0;
                int i24 = 0;
                while (i23 < size) {
                    WidgetRun widgetRun2 = (WidgetRun) this.widgets.get(i23);
                    float f7 = f5;
                    int i25 = i21;
                    if (widgetRun2.widget.getVisibility() != 8 && widgetRun2.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT) {
                        DimensionDependency dimensionDependency2 = widgetRun2.dimension;
                        if (dimensionDependency2.resolved) {
                            i22 = i22;
                            i24 = i24;
                        } else {
                            int i26 = f2 > f ? (int) (((widgetRun2.widget.mWeight[this.orientation] * f6) / f2) + f7) : i22;
                            int i27 = this.orientation;
                            ConstraintWidget constraintWidget = widgetRun2.widget;
                            if (i27 == 0) {
                                i10 = constraintWidget.mMatchConstraintMaxWidth;
                                i11 = constraintWidget.mMatchConstraintMinWidth;
                            } else {
                                i10 = constraintWidget.mMatchConstraintMaxHeight;
                                i11 = constraintWidget.mMatchConstraintMinHeight;
                            }
                            int i28 = i24;
                            int iMax = Math.max(i11, widgetRun2.matchConstraintsType == 1 ? Math.min(i26, dimensionDependency2.wrapValue) : i26);
                            if (i10 > 0) {
                                iMax = Math.min(i10, iMax);
                            }
                            if (iMax != i26) {
                                i24 = i28 + 1;
                                i26 = iMax;
                            } else {
                                i24 = i28;
                            }
                            widgetRun2.dimension.resolve(i26);
                        }
                    } else {
                        i22 = i22;
                        i24 = i24;
                    }
                    i23++;
                    f5 = f7;
                    i21 = i25;
                    f6 = f6;
                    i22 = i22;
                }
                i6 = i21;
                f3 = f5;
                int i29 = i24;
                if (i29 > 0) {
                    i5 -= i29;
                    i4 = 0;
                    for (int i30 = 0; i30 < size; i30++) {
                        WidgetRun widgetRun3 = (WidgetRun) this.widgets.get(i30);
                        if (widgetRun3.widget.getVisibility() != 8) {
                            if (i30 > 0 && i30 >= i13) {
                                i4 += widgetRun3.start.margin;
                            }
                            i4 += widgetRun3.dimension.value;
                            if (i30 < i14 && i30 < i) {
                                i4 += -widgetRun3.end.margin;
                            }
                        }
                    }
                }
                i8 = 2;
                if (this.chainStyle == 2 && i29 == 0) {
                    i7 = 0;
                    this.chainStyle = 0;
                } else {
                    i7 = 0;
                }
            } else {
                i6 = i21;
                f3 = 0.5f;
                i7 = 0;
                i8 = 2;
            }
            if (i4 > i12) {
                this.chainStyle = i8;
            }
            if (i3 > 0 && i5 == 0 && i13 == i) {
                this.chainStyle = i8;
            }
            int i31 = this.chainStyle;
            if (i31 == 1) {
                if (i3 > 1) {
                    i9 = (i12 - i4) / (i3 - 1);
                } else {
                    i9 = i3 == 1 ? (i12 - i4) / 2 : i7;
                }
                if (i5 > 0) {
                    i9 = i7;
                }
                int i32 = i6;
                while (i7 < size) {
                    WidgetRun widgetRun4 = (WidgetRun) this.widgets.get(z ? size - (i7 + 1) : i7);
                    if (widgetRun4.widget.getVisibility() == 8) {
                        widgetRun4.start.resolve(i32);
                        widgetRun4.end.resolve(i32);
                    } else {
                        if (i7 > 0) {
                            i32 = z ? i32 - i9 : i32 + i9;
                        }
                        if (i7 > 0 && i7 >= i13) {
                            DependencyNode dependencyNode = widgetRun4.start;
                            if (z) {
                                i32 -= dependencyNode.margin;
                            } else {
                                i32 += dependencyNode.margin;
                            }
                        }
                        if (z) {
                            widgetRun4.end.resolve(i32);
                        } else {
                            widgetRun4.start.resolve(i32);
                        }
                        DimensionDependency dimensionDependency3 = widgetRun4.dimension;
                        int i33 = dimensionDependency3.value;
                        if (widgetRun4.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun4.matchConstraintsType == 1) {
                            i33 = dimensionDependency3.wrapValue;
                        }
                        i32 = z ? i32 - i33 : i32 + i33;
                        if (z) {
                            widgetRun4.start.resolve(i32);
                        } else {
                            widgetRun4.end.resolve(i32);
                        }
                        widgetRun4.resolved = true;
                        if (i7 < i14 && i7 < i) {
                            DependencyNode dependencyNode2 = widgetRun4.end;
                            if (z) {
                                i32 -= -dependencyNode2.margin;
                            } else {
                                i32 += -dependencyNode2.margin;
                            }
                        }
                    }
                    i7++;
                }
                return;
            }
            if (i31 == 0) {
                int i34 = (i12 - i4) / (i3 + 1);
                if (i5 > 0) {
                    i34 = i7;
                }
                int i35 = i6;
                while (i7 < size) {
                    WidgetRun widgetRun5 = (WidgetRun) this.widgets.get(z ? size - (i7 + 1) : i7);
                    if (widgetRun5.widget.getVisibility() == 8) {
                        widgetRun5.start.resolve(i35);
                        widgetRun5.end.resolve(i35);
                    } else {
                        int i36 = z ? i35 - i34 : i35 + i34;
                        if (i7 > 0 && i7 >= i13) {
                            DependencyNode dependencyNode3 = widgetRun5.start;
                            if (z) {
                                i36 -= dependencyNode3.margin;
                            } else {
                                i36 += dependencyNode3.margin;
                            }
                        }
                        if (z) {
                            widgetRun5.end.resolve(i36);
                        } else {
                            widgetRun5.start.resolve(i36);
                        }
                        DimensionDependency dimensionDependency4 = widgetRun5.dimension;
                        int iMin = dimensionDependency4.value;
                        if (widgetRun5.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun5.matchConstraintsType == 1) {
                            iMin = Math.min(iMin, dimensionDependency4.wrapValue);
                        }
                        i35 = z ? i36 - iMin : i36 + iMin;
                        if (z) {
                            widgetRun5.start.resolve(i35);
                        } else {
                            widgetRun5.end.resolve(i35);
                        }
                        if (i7 < i14 && i7 < i) {
                            DependencyNode dependencyNode4 = widgetRun5.end;
                            if (z) {
                                i35 -= -dependencyNode4.margin;
                            } else {
                                i35 += -dependencyNode4.margin;
                            }
                        }
                    }
                    i7++;
                }
                return;
            }
            if (i31 == 2) {
                int i37 = this.orientation;
                ConstraintWidget constraintWidget2 = this.widget;
                float horizontalBiasPercent = i37 == 0 ? constraintWidget2.getHorizontalBiasPercent() : constraintWidget2.getVerticalBiasPercent();
                if (z) {
                    horizontalBiasPercent = 1.0f - horizontalBiasPercent;
                }
                int i38 = (int) (((i12 - i4) * horizontalBiasPercent) + f3);
                if (i38 < 0 || i5 > 0) {
                    i38 = i7;
                }
                int i39 = z ? i6 - i38 : i6 + i38;
                while (i7 < size) {
                    WidgetRun widgetRun6 = (WidgetRun) this.widgets.get(z ? size - (i7 + 1) : i7);
                    if (widgetRun6.widget.getVisibility() == 8) {
                        widgetRun6.start.resolve(i39);
                        widgetRun6.end.resolve(i39);
                    } else {
                        if (i7 > 0 && i7 >= i13) {
                            DependencyNode dependencyNode5 = widgetRun6.start;
                            if (z) {
                                i39 -= dependencyNode5.margin;
                            } else {
                                i39 += dependencyNode5.margin;
                            }
                        }
                        if (z) {
                            widgetRun6.end.resolve(i39);
                        } else {
                            widgetRun6.start.resolve(i39);
                        }
                        DimensionDependency dimensionDependency5 = widgetRun6.dimension;
                        int i40 = dimensionDependency5.value;
                        if (widgetRun6.dimensionBehavior == ConstraintWidget.DimensionBehaviour.MATCH_CONSTRAINT && widgetRun6.matchConstraintsType == 1) {
                            i40 = dimensionDependency5.wrapValue;
                        }
                        i39 = z ? i39 - i40 : i39 + i40;
                        if (z) {
                            widgetRun6.start.resolve(i39);
                        } else {
                            widgetRun6.end.resolve(i39);
                        }
                        if (i7 < i14 && i7 < i) {
                            DependencyNode dependencyNode6 = widgetRun6.end;
                            if (z) {
                                i39 -= -dependencyNode6.margin;
                            } else {
                                i39 += -dependencyNode6.margin;
                            }
                        }
                    }
                    i7++;
                }
            }
        }
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    public void applyToWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            ((WidgetRun) this.widgets.get(i)).applyToWidget();
        }
    }

    private ConstraintWidget getFirstVisibleWidget() {
        for (int i = 0; i < this.widgets.size(); i++) {
            WidgetRun widgetRun = (WidgetRun) this.widgets.get(i);
            if (widgetRun.widget.getVisibility() != 8) {
                return widgetRun.widget;
            }
        }
        return null;
    }

    private ConstraintWidget getLastVisibleWidget() {
        for (int size = this.widgets.size() - 1; size >= 0; size--) {
            WidgetRun widgetRun = (WidgetRun) this.widgets.get(size);
            if (widgetRun.widget.getVisibility() != 8) {
                return widgetRun.widget;
            }
        }
        return null;
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    void apply() {
        ArrayList arrayList = this.widgets;
        int size = arrayList.size();
        int i = 0;
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            ((WidgetRun) obj).apply();
        }
        int size2 = this.widgets.size();
        if (size2 < 1) {
            return;
        }
        ConstraintWidget constraintWidget = ((WidgetRun) this.widgets.get(0)).widget;
        ConstraintWidget constraintWidget2 = ((WidgetRun) this.widgets.get(size2 - 1)).widget;
        if (this.orientation == 0) {
            ConstraintAnchor constraintAnchor = constraintWidget.mLeft;
            ConstraintAnchor constraintAnchor2 = constraintWidget2.mRight;
            DependencyNode target = getTarget(constraintAnchor, 0);
            int margin = constraintAnchor.getMargin();
            ConstraintWidget firstVisibleWidget = getFirstVisibleWidget();
            if (firstVisibleWidget != null) {
                margin = firstVisibleWidget.mLeft.getMargin();
            }
            if (target != null) {
                addTarget(this.start, target, margin);
            }
            DependencyNode target2 = getTarget(constraintAnchor2, 0);
            int margin2 = constraintAnchor2.getMargin();
            ConstraintWidget lastVisibleWidget = getLastVisibleWidget();
            if (lastVisibleWidget != null) {
                margin2 = lastVisibleWidget.mRight.getMargin();
            }
            if (target2 != null) {
                addTarget(this.end, target2, -margin2);
            }
        } else {
            ConstraintAnchor constraintAnchor3 = constraintWidget.mTop;
            ConstraintAnchor constraintAnchor4 = constraintWidget2.mBottom;
            DependencyNode target3 = getTarget(constraintAnchor3, 1);
            int margin3 = constraintAnchor3.getMargin();
            ConstraintWidget firstVisibleWidget2 = getFirstVisibleWidget();
            if (firstVisibleWidget2 != null) {
                margin3 = firstVisibleWidget2.mTop.getMargin();
            }
            if (target3 != null) {
                addTarget(this.start, target3, margin3);
            }
            DependencyNode target4 = getTarget(constraintAnchor4, 1);
            int margin4 = constraintAnchor4.getMargin();
            ConstraintWidget lastVisibleWidget2 = getLastVisibleWidget();
            if (lastVisibleWidget2 != null) {
                margin4 = lastVisibleWidget2.mBottom.getMargin();
            }
            if (target4 != null) {
                addTarget(this.end, target4, -margin4);
            }
        }
        this.start.updateDelegate = this;
        this.end.updateDelegate = this;
    }
}
