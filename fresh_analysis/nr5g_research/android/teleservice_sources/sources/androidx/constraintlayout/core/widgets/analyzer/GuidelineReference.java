package androidx.constraintlayout.core.widgets.analyzer;

import androidx.constraintlayout.core.widgets.ConstraintWidget;
import androidx.constraintlayout.core.widgets.Guideline;

/* JADX INFO: loaded from: classes.dex */
class GuidelineReference extends WidgetRun {
    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    boolean supportsWrapComputation() {
        return false;
    }

    public GuidelineReference(ConstraintWidget constraintWidget) {
        super(constraintWidget);
        constraintWidget.horizontalRun.clear();
        constraintWidget.verticalRun.clear();
        this.orientation = ((Guideline) constraintWidget).getOrientation();
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    void clear() {
        this.start.clear();
    }

    private void addDependency(DependencyNode dependencyNode) {
        this.start.dependencies.add(dependencyNode);
        dependencyNode.targets.add(this.start);
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun, androidx.constraintlayout.core.widgets.analyzer.Dependency
    public void update(Dependency dependency) {
        DependencyNode dependencyNode = this.start;
        if (dependencyNode.readyToSolve && !dependencyNode.resolved) {
            this.start.resolve((int) ((((DependencyNode) dependencyNode.targets.get(0)).value * ((Guideline) this.widget).getRelativePercent()) + 0.5f));
        }
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    void apply() {
        Guideline guideline = (Guideline) this.widget;
        int relativeBegin = guideline.getRelativeBegin();
        int relativeEnd = guideline.getRelativeEnd();
        guideline.getRelativePercent();
        if (guideline.getOrientation() == 1) {
            if (relativeBegin != -1) {
                this.start.targets.add(this.widget.mParent.horizontalRun.start);
                this.widget.mParent.horizontalRun.start.dependencies.add(this.start);
                this.start.margin = relativeBegin;
            } else {
                DependencyNode dependencyNode = this.start;
                if (relativeEnd != -1) {
                    dependencyNode.targets.add(this.widget.mParent.horizontalRun.end);
                    this.widget.mParent.horizontalRun.end.dependencies.add(this.start);
                    this.start.margin = -relativeEnd;
                } else {
                    dependencyNode.delegateToWidgetRun = true;
                    dependencyNode.targets.add(this.widget.mParent.horizontalRun.end);
                    this.widget.mParent.horizontalRun.end.dependencies.add(this.start);
                }
            }
            addDependency(this.widget.horizontalRun.start);
            addDependency(this.widget.horizontalRun.end);
            return;
        }
        if (relativeBegin != -1) {
            this.start.targets.add(this.widget.mParent.verticalRun.start);
            this.widget.mParent.verticalRun.start.dependencies.add(this.start);
            this.start.margin = relativeBegin;
        } else {
            DependencyNode dependencyNode2 = this.start;
            if (relativeEnd != -1) {
                dependencyNode2.targets.add(this.widget.mParent.verticalRun.end);
                this.widget.mParent.verticalRun.end.dependencies.add(this.start);
                this.start.margin = -relativeEnd;
            } else {
                dependencyNode2.delegateToWidgetRun = true;
                dependencyNode2.targets.add(this.widget.mParent.verticalRun.end);
                this.widget.mParent.verticalRun.end.dependencies.add(this.start);
            }
        }
        addDependency(this.widget.verticalRun.start);
        addDependency(this.widget.verticalRun.end);
    }

    @Override // androidx.constraintlayout.core.widgets.analyzer.WidgetRun
    public void applyToWidget() {
        int orientation = ((Guideline) this.widget).getOrientation();
        ConstraintWidget constraintWidget = this.widget;
        if (orientation == 1) {
            constraintWidget.setX(this.start.value);
        } else {
            constraintWidget.setY(this.start.value);
        }
    }
}
