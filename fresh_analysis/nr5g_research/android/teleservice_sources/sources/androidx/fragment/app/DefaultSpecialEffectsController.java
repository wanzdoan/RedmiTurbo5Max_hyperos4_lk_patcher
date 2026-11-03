package androidx.fragment.app;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.activity.BackEventCompat;
import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import androidx.collection.ArrayMap;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Ref$ObjectRef;

/* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
/* JADX INFO: loaded from: classes.dex */
public final class DefaultSpecialEffectsController extends SpecialEffectsController {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DefaultSpecialEffectsController(ViewGroup viewGroup) {
        super(viewGroup);
        viewGroup.getClass();
    }

    /* JADX WARN: Code duplicated, block: B:31:0x00bd  */
    @Override // androidx.fragment.app.SpecialEffectsController
    public void collectEffects(List list, boolean z) {
        Object obj;
        Object next;
        list.getClass();
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "Collecting Effects");
        }
        Iterator it = list.iterator();
        while (true) {
            obj = null;
            if (!it.hasNext()) {
                next = null;
                break;
            }
            next = it.next();
            SpecialEffectsController.Operation operation = (SpecialEffectsController.Operation) next;
            SpecialEffectsController.Operation.State.Companion companion = SpecialEffectsController.Operation.State.Companion;
            View view = operation.getFragment().mView;
            view.getClass();
            SpecialEffectsController.Operation.State stateAsOperationState = companion.asOperationState(view);
            SpecialEffectsController.Operation.State state = SpecialEffectsController.Operation.State.VISIBLE;
            if (stateAsOperationState == state && operation.getFinalState() != state) {
                break;
            }
        }
        SpecialEffectsController.Operation operation2 = (SpecialEffectsController.Operation) next;
        ListIterator listIterator = list.listIterator(list.size());
        while (listIterator.hasPrevious()) {
            Object objPrevious = listIterator.previous();
            SpecialEffectsController.Operation operation3 = (SpecialEffectsController.Operation) objPrevious;
            SpecialEffectsController.Operation.State.Companion companion2 = SpecialEffectsController.Operation.State.Companion;
            View view2 = operation3.getFragment().mView;
            view2.getClass();
            SpecialEffectsController.Operation.State stateAsOperationState2 = companion2.asOperationState(view2);
            SpecialEffectsController.Operation.State state2 = SpecialEffectsController.Operation.State.VISIBLE;
            if (stateAsOperationState2 != state2 && operation3.getFinalState() == state2) {
                obj = objPrevious;
                break;
            }
        }
        SpecialEffectsController.Operation operation4 = (SpecialEffectsController.Operation) obj;
        if (FragmentManager.isLoggingEnabled(2)) {
            Log.v("FragmentManager", "Executing operations from " + operation2 + " to " + operation4);
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        syncAnimations(list);
        Iterator it2 = list.iterator();
        while (it2.hasNext()) {
            final SpecialEffectsController.Operation operation5 = (SpecialEffectsController.Operation) it2.next();
            arrayList.add(new AnimationInfo(operation5, z));
            boolean z2 = false;
            if (z) {
                if (operation5 == operation2) {
                    z2 = true;
                }
            } else if (operation5 == operation4) {
                z2 = true;
            }
            arrayList2.add(new TransitionInfo(operation5, z, z2));
            operation5.addCompletionListener(new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.applyContainerChangesToOperation$fragment(operation5);
                }
            });
        }
        createTransitionEffect(arrayList2, z, operation2, operation4);
        collectAnimEffects(arrayList);
    }

    private final void syncAnimations(List list) {
        Fragment fragment = ((SpecialEffectsController.Operation) CollectionsKt.last(list)).getFragment();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            SpecialEffectsController.Operation operation = (SpecialEffectsController.Operation) it.next();
            operation.getFragment().mAnimationInfo.mEnterAnim = fragment.mAnimationInfo.mEnterAnim;
            operation.getFragment().mAnimationInfo.mExitAnim = fragment.mAnimationInfo.mExitAnim;
            operation.getFragment().mAnimationInfo.mPopEnterAnim = fragment.mAnimationInfo.mPopEnterAnim;
            operation.getFragment().mAnimationInfo.mPopExitAnim = fragment.mAnimationInfo.mPopExitAnim;
        }
    }

    private final void collectAnimEffects(List list) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            CollectionsKt.addAll(arrayList2, ((AnimationInfo) it.next()).getOperation().getEffects$fragment());
        }
        boolean zIsEmpty = arrayList2.isEmpty();
        Iterator it2 = list.iterator();
        int i = 0;
        boolean z = false;
        while (it2.hasNext()) {
            AnimationInfo animationInfo = (AnimationInfo) it2.next();
            Context context = getContainer().getContext();
            SpecialEffectsController.Operation operation = animationInfo.getOperation();
            context.getClass();
            FragmentAnim.AnimationOrAnimator animation = animationInfo.getAnimation(context);
            if (animation != null) {
                if (animation.animator == null) {
                    arrayList.add(animationInfo);
                } else {
                    Fragment fragment = operation.getFragment();
                    if (!operation.getEffects$fragment().isEmpty()) {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "Ignoring Animator set on " + fragment + " as this Fragment was involved in a Transition.");
                        }
                    } else {
                        if (operation.getFinalState() == SpecialEffectsController.Operation.State.GONE) {
                            operation.setAwaitingContainerChanges(false);
                        }
                        operation.addEffect(new AnimatorEffect(animationInfo));
                        z = true;
                    }
                }
            }
        }
        int size = arrayList.size();
        while (i < size) {
            Object obj = arrayList.get(i);
            i++;
            AnimationInfo animationInfo2 = (AnimationInfo) obj;
            SpecialEffectsController.Operation operation2 = animationInfo2.getOperation();
            Fragment fragment2 = operation2.getFragment();
            if (zIsEmpty) {
                if (z) {
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "Ignoring Animation set on " + fragment2 + " as Animations cannot run alongside Animators.");
                    }
                } else {
                    operation2.addEffect(new AnimationEffect(animationInfo2));
                }
            } else if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Ignoring Animation set on " + fragment2 + " as Animations cannot run alongside Transitions.");
            }
        }
    }

    /* JADX INFO: renamed from: $r8$lambda$z6s7cx-bmGkCno_M40DFN57B1RI, reason: not valid java name */
    public static boolean m20$r8$lambda$z6s7cxbmGkCno_M40DFN57B1RI(Collection collection, Map.Entry entry) {
        entry.getClass();
        return CollectionsKt.contains(collection, ViewCompat.getTransitionName((View) entry.getValue()));
    }

    private final void retainMatchingViews(ArrayMap arrayMap, final Collection collection) {
        CollectionsKt.retainAll(arrayMap.entrySet(), new Function1() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$$ExternalSyntheticLambda1
            @Override // kotlin.jvm.functions.Function1
            public final Object invoke(Object obj) {
                return Boolean.valueOf(DefaultSpecialEffectsController.m20$r8$lambda$z6s7cxbmGkCno_M40DFN57B1RI(collection, (Map.Entry) obj));
            }
        });
    }

    private final void findNamedViews(Map map, View view) {
        String transitionName = ViewCompat.getTransitionName(view);
        if (transitionName != null) {
            map.put(transitionName, view);
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt.getVisibility() == 0) {
                    findNamedViews(map, childAt);
                }
            }
        }
    }

    /* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
    public abstract class SpecialEffectsInfo {
        private final SpecialEffectsController.Operation operation;

        public SpecialEffectsInfo(SpecialEffectsController.Operation operation) {
            operation.getClass();
            this.operation = operation;
        }

        public final SpecialEffectsController.Operation getOperation() {
            return this.operation;
        }

        public final boolean isVisibilityUnchanged() {
            View view = this.operation.getFragment().mView;
            SpecialEffectsController.Operation.State stateAsOperationState = view != null ? SpecialEffectsController.Operation.State.Companion.asOperationState(view) : null;
            SpecialEffectsController.Operation.State finalState = this.operation.getFinalState();
            if (stateAsOperationState == finalState) {
                return true;
            }
            SpecialEffectsController.Operation.State state = SpecialEffectsController.Operation.State.VISIBLE;
            return (stateAsOperationState == state || finalState == state) ? false : true;
        }
    }

    /* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
    final class AnimationInfo extends SpecialEffectsInfo {
        private FragmentAnim.AnimationOrAnimator animation;
        private boolean isAnimLoaded;
        private final boolean isPop;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnimationInfo(SpecialEffectsController.Operation operation, boolean z) {
            super(operation);
            operation.getClass();
            this.isPop = z;
        }

        public final FragmentAnim.AnimationOrAnimator getAnimation(Context context) {
            context.getClass();
            if (this.isAnimLoaded) {
                return this.animation;
            }
            FragmentAnim.AnimationOrAnimator animationOrAnimatorLoadAnimation = FragmentAnim.loadAnimation(context, getOperation().getFragment(), getOperation().getFinalState() == SpecialEffectsController.Operation.State.VISIBLE, this.isPop);
            this.animation = animationOrAnimatorLoadAnimation;
            this.isAnimLoaded = true;
            return animationOrAnimatorLoadAnimation;
        }
    }

    /* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
    final class TransitionInfo extends SpecialEffectsInfo {
        private final boolean isOverlapAllowed;
        private final Object sharedElementTransition;
        private final Object transition;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TransitionInfo(SpecialEffectsController.Operation operation, boolean z, boolean z2) {
            Object exitTransition;
            boolean allowEnterTransitionOverlap;
            Object sharedElementEnterTransition;
            super(operation);
            operation.getClass();
            SpecialEffectsController.Operation.State finalState = operation.getFinalState();
            SpecialEffectsController.Operation.State state = SpecialEffectsController.Operation.State.VISIBLE;
            if (finalState == state) {
                if (z) {
                    exitTransition = operation.getFragment().getReenterTransition();
                } else {
                    exitTransition = operation.getFragment().getEnterTransition();
                }
            } else if (z) {
                exitTransition = operation.getFragment().getReturnTransition();
            } else {
                exitTransition = operation.getFragment().getExitTransition();
            }
            this.transition = exitTransition;
            if (operation.getFinalState() != state) {
                allowEnterTransitionOverlap = true;
            } else if (z) {
                allowEnterTransitionOverlap = operation.getFragment().getAllowReturnTransitionOverlap();
            } else {
                allowEnterTransitionOverlap = operation.getFragment().getAllowEnterTransitionOverlap();
            }
            this.isOverlapAllowed = allowEnterTransitionOverlap;
            if (!z2) {
                sharedElementEnterTransition = null;
            } else if (z) {
                sharedElementEnterTransition = operation.getFragment().getSharedElementReturnTransition();
            } else {
                sharedElementEnterTransition = operation.getFragment().getSharedElementEnterTransition();
            }
            this.sharedElementTransition = sharedElementEnterTransition;
        }

        public final Object getTransition() {
            return this.transition;
        }

        public final boolean isOverlapAllowed() {
            return this.isOverlapAllowed;
        }

        public final Object getSharedElementTransition() {
            return this.sharedElementTransition;
        }

        public final boolean hasSharedElementTransition() {
            return this.sharedElementTransition != null;
        }

        public final FragmentTransitionImpl getHandlingImpl() {
            FragmentTransitionImpl handlingImpl = getHandlingImpl(this.transition);
            FragmentTransitionImpl handlingImpl2 = getHandlingImpl(this.sharedElementTransition);
            if (handlingImpl == null || handlingImpl2 == null || handlingImpl == handlingImpl2) {
                return handlingImpl == null ? handlingImpl2 : handlingImpl;
            }
            StringBuilder sb = new StringBuilder("Mixing framework transitions and AndroidX transitions is not allowed. Fragment ");
            sb.append(getOperation().getFragment());
            sb.append(" returned Transition ");
            sb.append(this.transition);
            Object obj = this.sharedElementTransition;
            sb.append(" which uses a different Transition  type than its shared element transition ");
            sb.append(obj);
            throw new IllegalArgumentException(sb.toString().toString());
        }

        private final FragmentTransitionImpl getHandlingImpl(Object obj) {
            if (obj == null) {
                return null;
            }
            FragmentTransitionImpl fragmentTransitionImpl = FragmentTransition.PLATFORM_IMPL;
            if (fragmentTransitionImpl != null && fragmentTransitionImpl.canHandle(obj)) {
                return fragmentTransitionImpl;
            }
            FragmentTransitionImpl fragmentTransitionImpl2 = FragmentTransition.SUPPORT_IMPL;
            if (fragmentTransitionImpl2 != null && fragmentTransitionImpl2.canHandle(obj)) {
                return fragmentTransitionImpl2;
            }
            StringBuilder sb = new StringBuilder("Transition ");
            sb.append(obj);
            Fragment fragment = getOperation().getFragment();
            sb.append(" for fragment ");
            sb.append(fragment);
            sb.append(" is not a valid framework Transition or AndroidX Transition");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
    final class AnimationEffect extends SpecialEffectsController.Effect {
        private final AnimationInfo animationInfo;

        public AnimationEffect(AnimationInfo animationInfo) {
            animationInfo.getClass();
            this.animationInfo = animationInfo;
        }

        public final AnimationInfo getAnimationInfo() {
            return this.animationInfo;
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onCommit(ViewGroup viewGroup) {
            viewGroup.getClass();
            if (this.animationInfo.isVisibilityUnchanged()) {
                this.animationInfo.getOperation().completeEffect(this);
                return;
            }
            Context context = viewGroup.getContext();
            SpecialEffectsController.Operation operation = this.animationInfo.getOperation();
            View view = operation.getFragment().mView;
            AnimationInfo animationInfo = this.animationInfo;
            context.getClass();
            FragmentAnim.AnimationOrAnimator animation = animationInfo.getAnimation(context);
            if (animation == null) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Required value was null.");
                return;
            }
            Animation animation2 = animation.animation;
            if (animation2 == null) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Required value was null.");
                return;
            }
            if (operation.getFinalState() != SpecialEffectsController.Operation.State.REMOVED) {
                view.startAnimation(animation2);
                this.animationInfo.getOperation().completeEffect(this);
                return;
            }
            viewGroup.startViewTransition(view);
            FragmentAnim.EndViewTransitionAnimation endViewTransitionAnimation = new FragmentAnim.EndViewTransitionAnimation(animation2, viewGroup, view);
            endViewTransitionAnimation.setAnimationListener(new DefaultSpecialEffectsController$AnimationEffect$onCommit$1(operation, viewGroup, view, this));
            view.startAnimation(endViewTransitionAnimation);
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Animation from operation " + operation + " has started.");
            }
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onCancel(ViewGroup viewGroup) {
            viewGroup.getClass();
            SpecialEffectsController.Operation operation = this.animationInfo.getOperation();
            View view = operation.getFragment().mView;
            view.clearAnimation();
            viewGroup.endViewTransition(view);
            this.animationInfo.getOperation().completeEffect(this);
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Animation from operation " + operation + " has been cancelled.");
            }
        }
    }

    /* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
    final class AnimatorEffect extends SpecialEffectsController.Effect {
        private AnimatorSet animator;
        private final AnimationInfo animatorInfo;

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public boolean isSeekingSupported() {
            return true;
        }

        public AnimatorEffect(AnimationInfo animationInfo) {
            animationInfo.getClass();
            this.animatorInfo = animationInfo;
        }

        public final AnimationInfo getAnimatorInfo() {
            return this.animatorInfo;
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onStart(final ViewGroup viewGroup) {
            final AnimatorEffect animatorEffect;
            viewGroup.getClass();
            if (this.animatorInfo.isVisibilityUnchanged()) {
                return;
            }
            Context context = viewGroup.getContext();
            AnimationInfo animationInfo = this.animatorInfo;
            context.getClass();
            FragmentAnim.AnimationOrAnimator animation = animationInfo.getAnimation(context);
            this.animator = animation != null ? animation.animator : null;
            final SpecialEffectsController.Operation operation = this.animatorInfo.getOperation();
            Fragment fragment = operation.getFragment();
            final boolean z = operation.getFinalState() == SpecialEffectsController.Operation.State.GONE;
            final View view = fragment.mView;
            viewGroup.startViewTransition(view);
            AnimatorSet animatorSet = this.animator;
            if (animatorSet != null) {
                animatorEffect = this;
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$AnimatorEffect$onStart$1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        animator.getClass();
                        viewGroup.endViewTransition(view);
                        if (z || operation.getFinalState() == SpecialEffectsController.Operation.State.GONE) {
                            SpecialEffectsController.Operation.State finalState = operation.getFinalState();
                            View view2 = view;
                            view2.getClass();
                            finalState.applyState(view2, viewGroup);
                        }
                        animatorEffect.getAnimatorInfo().getOperation().completeEffect(animatorEffect);
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "Animator from operation " + operation + " has ended.");
                        }
                    }
                });
            } else {
                animatorEffect = this;
            }
            AnimatorSet animatorSet2 = animatorEffect.animator;
            if (animatorSet2 != null) {
                animatorSet2.setTarget(view);
            }
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onProgress(BackEventCompat backEventCompat, ViewGroup viewGroup) {
            backEventCompat.getClass();
            viewGroup.getClass();
            SpecialEffectsController.Operation operation = this.animatorInfo.getOperation();
            AnimatorSet animatorSet = this.animator;
            if (animatorSet == null) {
                this.animatorInfo.getOperation().completeEffect(this);
                return;
            }
            if (operation.getFragment().mTransitioning) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "Adding BackProgressCallbacks for Animators to operation " + operation);
                }
                long j = Api24Impl.INSTANCE.totalDuration(animatorSet);
                long progress = (long) (backEventCompat.getProgress() * j);
                if (progress == 0) {
                    progress = 1;
                }
                if (progress == j) {
                    progress = j - 1;
                }
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "Setting currentPlayTime to " + progress + " for Animator " + animatorSet + " on operation " + operation);
                }
                Api26Impl.INSTANCE.setCurrentPlayTime(animatorSet, progress);
            }
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onCommit(ViewGroup viewGroup) {
            viewGroup.getClass();
            SpecialEffectsController.Operation operation = this.animatorInfo.getOperation();
            AnimatorSet animatorSet = this.animator;
            if (animatorSet == null) {
                this.animatorInfo.getOperation().completeEffect(this);
                return;
            }
            animatorSet.start();
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Animator from operation " + operation + " has started.");
            }
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onCancel(ViewGroup viewGroup) {
            viewGroup.getClass();
            AnimatorSet animatorSet = this.animator;
            AnimationInfo animationInfo = this.animatorInfo;
            if (animatorSet == null) {
                animationInfo.getOperation().completeEffect(this);
                return;
            }
            SpecialEffectsController.Operation operation = animationInfo.getOperation();
            if (operation.isSeeking()) {
                Api26Impl.INSTANCE.reverse(animatorSet);
            } else {
                animatorSet.end();
            }
            if (FragmentManager.isLoggingEnabled(2)) {
                StringBuilder sb = new StringBuilder("Animator from operation ");
                sb.append(operation);
                sb.append(" has been canceled");
                sb.append(operation.isSeeking() ? " with seeking." : ".");
                sb.append(' ');
                Log.v("FragmentManager", sb.toString());
            }
        }
    }

    /* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
    final class TransitionEffect extends SpecialEffectsController.Effect {
        private Object controller;
        private final ArrayList enteringNames;
        private final ArrayList exitingNames;
        private final SpecialEffectsController.Operation firstOut;
        private final ArrayMap firstOutViews;
        private final boolean isPop;
        private final SpecialEffectsController.Operation lastIn;
        private final ArrayMap lastInViews;
        private boolean noControllerReturned;
        private final ArrayList sharedElementFirstOutViews;
        private final ArrayList sharedElementLastInViews;
        private final ArrayMap sharedElementNameMapping;
        private final Object sharedElementTransition;
        private final FragmentTransitionImpl transitionImpl;
        private final List transitionInfos;
        private final CancellationSignal transitionSignal;

        public TransitionEffect(List list, SpecialEffectsController.Operation operation, SpecialEffectsController.Operation operation2, FragmentTransitionImpl fragmentTransitionImpl, Object obj, ArrayList arrayList, ArrayList arrayList2, ArrayMap arrayMap, ArrayList arrayList3, ArrayList arrayList4, ArrayMap arrayMap2, ArrayMap arrayMap3, boolean z) {
            list.getClass();
            fragmentTransitionImpl.getClass();
            arrayList.getClass();
            arrayList2.getClass();
            arrayMap.getClass();
            arrayList3.getClass();
            arrayList4.getClass();
            arrayMap2.getClass();
            arrayMap3.getClass();
            this.transitionInfos = list;
            this.firstOut = operation;
            this.lastIn = operation2;
            this.transitionImpl = fragmentTransitionImpl;
            this.sharedElementTransition = obj;
            this.sharedElementFirstOutViews = arrayList;
            this.sharedElementLastInViews = arrayList2;
            this.sharedElementNameMapping = arrayMap;
            this.enteringNames = arrayList3;
            this.exitingNames = arrayList4;
            this.firstOutViews = arrayMap2;
            this.lastInViews = arrayMap3;
            this.isPop = z;
            this.transitionSignal = new CancellationSignal();
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public boolean isSeekingSupported() {
            if (!this.transitionImpl.isSeekingSupported()) {
                return false;
            }
            List<TransitionInfo> list = this.transitionInfos;
            if (list == null || !list.isEmpty()) {
                for (TransitionInfo transitionInfo : list) {
                    if (transitionInfo.getTransition() == null || !this.transitionImpl.isSeekingSupported(transitionInfo.getTransition())) {
                        return false;
                    }
                }
            }
            Object obj = this.sharedElementTransition;
            return obj == null || this.transitionImpl.isSeekingSupported(obj);
        }

        public final boolean getTransitioning() {
            List list = this.transitionInfos;
            if (list != null && list.isEmpty()) {
                return true;
            }
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (!((TransitionInfo) it.next()).getOperation().getFragment().mTransitioning) {
                    return false;
                }
            }
            return true;
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onStart(final ViewGroup viewGroup) {
            viewGroup.getClass();
            if (!viewGroup.isLaidOut()) {
                Iterator it = this.transitionInfos.iterator();
                while (it.hasNext()) {
                    SpecialEffectsController.Operation operation = ((TransitionInfo) it.next()).getOperation();
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Container " + viewGroup + " has not been laid out. Skipping onStart for operation " + operation);
                    }
                }
                return;
            }
            if (getTransitioning() && this.sharedElementTransition != null && !isSeekingSupported()) {
                Log.i("FragmentManager", "Ignoring shared elements transition " + this.sharedElementTransition + " between " + this.firstOut + " and " + this.lastIn + " as neither fragment has set a Transition. In order to run a SharedElementTransition, you must also set either an enter or exit transition on a fragment involved in the transaction. The sharedElementTransition will run after the back gesture has been committed.");
            }
            if (isSeekingSupported() && getTransitioning()) {
                final Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
                Pair pairCreateMergedTransition = createMergedTransition(viewGroup, this.lastIn, this.firstOut);
                ArrayList arrayList = (ArrayList) pairCreateMergedTransition.component1();
                final Object objComponent2 = pairCreateMergedTransition.component2();
                List list = this.transitionInfos;
                ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
                Iterator it2 = list.iterator();
                while (it2.hasNext()) {
                    arrayList2.add(((TransitionInfo) it2.next()).getOperation());
                }
                int size = arrayList2.size();
                for (int i = 0; i < size; i++) {
                    final SpecialEffectsController.Operation operation2 = (SpecialEffectsController.Operation) arrayList2.get(i);
                    this.transitionImpl.setListenerForTransitionEnd(operation2.getFragment(), objComponent2, this.transitionSignal, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            DefaultSpecialEffectsController.TransitionEffect.onStart$lambda$2$0(ref$ObjectRef);
                        }
                    }, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            DefaultSpecialEffectsController.TransitionEffect.onStart$lambda$2$1(operation2, this);
                        }
                    });
                }
                runTransition(arrayList, viewGroup, new Function0() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda2
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DefaultSpecialEffectsController.TransitionEffect.$r8$lambda$qnUxet1oHyNM6F6HQCSG_D4LKuE(this.f$0, viewGroup, objComponent2, ref$ObjectRef);
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void onStart$lambda$2$0(Ref$ObjectRef ref$ObjectRef) {
            Function0 function0 = (Function0) ref$ObjectRef.element;
            if (function0 != null) {
                function0.invoke();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void onStart$lambda$2$1(SpecialEffectsController.Operation operation, TransitionEffect transitionEffect) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Transition for operation " + operation + " has completed");
            }
            operation.completeEffect(transitionEffect);
        }

        public static Unit $r8$lambda$qnUxet1oHyNM6F6HQCSG_D4LKuE(final TransitionEffect transitionEffect, final ViewGroup viewGroup, final Object obj, Ref$ObjectRef ref$ObjectRef) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Attempting to create TransitionSeekController");
            }
            Object objControlDelayedTransition = transitionEffect.transitionImpl.controlDelayedTransition(viewGroup, obj);
            transitionEffect.controller = objControlDelayedTransition;
            if (objControlDelayedTransition == null) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "TransitionSeekController was not created.");
                }
                transitionEffect.noControllerReturned = true;
                return Unit.INSTANCE;
            }
            ref$ObjectRef.element = new Function0() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda8
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return DefaultSpecialEffectsController.TransitionEffect.onStart$lambda$3$0(this.f$0, obj, viewGroup);
                }
            };
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Started executing operations from " + transitionEffect.firstOut + " to " + transitionEffect.lastIn);
            }
            return Unit.INSTANCE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX WARN: Code duplicated, block: B:18:0x005d  */
        public static final Unit onStart$lambda$3$0(final TransitionEffect transitionEffect, Object obj, final ViewGroup viewGroup) {
            List list = transitionEffect.transitionInfos;
            if (list != null && list.isEmpty()) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "Animating to start");
                }
                FragmentTransitionImpl fragmentTransitionImpl = transitionEffect.transitionImpl;
                Object obj2 = transitionEffect.controller;
                obj2.getClass();
                fragmentTransitionImpl.animateToStart(obj2, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        DefaultSpecialEffectsController.TransitionEffect.onStart$lambda$3$0$1(this.f$0, viewGroup);
                    }
                });
            } else {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    if (!((TransitionInfo) it.next()).getOperation().isSeeking()) {
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "Completing animating immediately");
                        }
                        CancellationSignal cancellationSignal = new CancellationSignal();
                        transitionEffect.transitionImpl.setListenerForTransitionEnd(((TransitionInfo) transitionEffect.transitionInfos.get(0)).getOperation().getFragment(), obj, cancellationSignal, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda10
                            @Override // java.lang.Runnable
                            public final void run() {
                                DefaultSpecialEffectsController.TransitionEffect.onStart$lambda$3$0$2(this.f$0);
                            }
                        });
                        cancellationSignal.cancel();
                    }
                }
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "Animating to start");
                }
                FragmentTransitionImpl fragmentTransitionImpl2 = transitionEffect.transitionImpl;
                Object obj3 = transitionEffect.controller;
                obj3.getClass();
                fragmentTransitionImpl2.animateToStart(obj3, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        DefaultSpecialEffectsController.TransitionEffect.onStart$lambda$3$0$1(this.f$0, viewGroup);
                    }
                });
            }
            return Unit.INSTANCE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void onStart$lambda$3$0$1(TransitionEffect transitionEffect, ViewGroup viewGroup) {
            Iterator it = transitionEffect.transitionInfos.iterator();
            while (it.hasNext()) {
                SpecialEffectsController.Operation operation = ((TransitionInfo) it.next()).getOperation();
                View view = operation.getFragment().getView();
                if (view != null) {
                    operation.getFinalState().applyState(view, viewGroup);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void onStart$lambda$3$0$2(TransitionEffect transitionEffect) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Transition for all operations has completed");
            }
            Iterator it = transitionEffect.transitionInfos.iterator();
            while (it.hasNext()) {
                ((TransitionInfo) it.next()).getOperation().completeEffect(transitionEffect);
            }
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onProgress(BackEventCompat backEventCompat, ViewGroup viewGroup) {
            backEventCompat.getClass();
            viewGroup.getClass();
            Object obj = this.controller;
            if (obj != null) {
                this.transitionImpl.setCurrentPlayTime(obj, backEventCompat.getProgress());
            }
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onCommit(final ViewGroup viewGroup) {
            viewGroup.getClass();
            int i = 0;
            if (viewGroup.isLaidOut() && !this.noControllerReturned) {
                Object obj = this.controller;
                if (obj != null) {
                    FragmentTransitionImpl fragmentTransitionImpl = this.transitionImpl;
                    obj.getClass();
                    fragmentTransitionImpl.animateToEnd(obj);
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "Ending execution of operations from " + this.firstOut + " to " + this.lastIn);
                        return;
                    }
                    return;
                }
                Pair pairCreateMergedTransition = createMergedTransition(viewGroup, this.lastIn, this.firstOut);
                ArrayList arrayList = (ArrayList) pairCreateMergedTransition.component1();
                final Object objComponent2 = pairCreateMergedTransition.component2();
                List list = this.transitionInfos;
                ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(list, 10));
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    arrayList2.add(((TransitionInfo) it.next()).getOperation());
                }
                int size = arrayList2.size();
                while (i < size) {
                    Object obj2 = arrayList2.get(i);
                    i++;
                    final SpecialEffectsController.Operation operation = (SpecialEffectsController.Operation) obj2;
                    this.transitionImpl.setListenerForTransitionEnd(operation.getFragment(), objComponent2, this.transitionSignal, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda3
                        @Override // java.lang.Runnable
                        public final void run() {
                            DefaultSpecialEffectsController.TransitionEffect.onCommit$lambda$2$0(operation, this);
                        }
                    });
                }
                runTransition(arrayList, viewGroup, new Function0() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda4
                    @Override // kotlin.jvm.functions.Function0
                    public final Object invoke() {
                        return DefaultSpecialEffectsController.TransitionEffect.m25$r8$lambda$yS8si9lqGzND4BzkLvMUfJw8U(this.f$0, viewGroup, objComponent2);
                    }
                });
                if (FragmentManager.isLoggingEnabled(2)) {
                    Log.v("FragmentManager", "Completed executing operations from " + this.firstOut + " to " + this.lastIn);
                    return;
                }
                return;
            }
            for (TransitionInfo transitionInfo : this.transitionInfos) {
                SpecialEffectsController.Operation operation2 = transitionInfo.getOperation();
                if (FragmentManager.isLoggingEnabled(2)) {
                    if (this.noControllerReturned) {
                        Log.v("FragmentManager", "SpecialEffectsController: TransitionSeekController was not created. Completing operation " + operation2);
                    } else {
                        Log.v("FragmentManager", "SpecialEffectsController: Container " + viewGroup + " has not been laid out. Completing operation " + operation2);
                    }
                }
                transitionInfo.getOperation().completeEffect(this);
            }
            this.noControllerReturned = false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void onCommit$lambda$2$0(SpecialEffectsController.Operation operation, TransitionEffect transitionEffect) {
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Transition for operation " + operation + " has completed");
            }
            operation.completeEffect(transitionEffect);
        }

        /* JADX INFO: renamed from: $r8$lambda$yS8-si9lqG-zND4BzkLvMUfJw8U, reason: not valid java name */
        public static Unit m25$r8$lambda$yS8si9lqGzND4BzkLvMUfJw8U(TransitionEffect transitionEffect, ViewGroup viewGroup, Object obj) {
            transitionEffect.transitionImpl.beginDelayedTransition(viewGroup, obj);
            return Unit.INSTANCE;
        }

        private final Pair createMergedTransition(ViewGroup viewGroup, final SpecialEffectsController.Operation operation, final SpecialEffectsController.Operation operation2) {
            Object obj;
            final ArrayList arrayList;
            operation = operation;
            operation2 = operation2;
            View view = new View(viewGroup.getContext());
            final Rect rect = new Rect();
            Iterator it = this.transitionInfos.iterator();
            boolean z = false;
            View view2 = null;
            while (it.hasNext()) {
                if (((TransitionInfo) it.next()).hasSharedElementTransition() && operation2 != null && operation != null && !this.sharedElementNameMapping.isEmpty() && this.sharedElementTransition != null) {
                    FragmentTransition.callSharedElementStartEnd(operation.getFragment(), operation2.getFragment(), this.isPop, this.firstOutViews, true);
                    OneShotPreDrawListener.add(viewGroup, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            SpecialEffectsController.Operation operation3 = operation;
                            SpecialEffectsController.Operation operation4 = operation2;
                            DefaultSpecialEffectsController.TransitionEffect transitionEffect = this;
                            FragmentTransition.callSharedElementStartEnd(operation3.getFragment(), operation4.getFragment(), transitionEffect.isPop, transitionEffect.lastInViews, false);
                        }
                    });
                    this.sharedElementFirstOutViews.addAll(this.firstOutViews.values());
                    if (!this.exitingNames.isEmpty()) {
                        Object obj2 = this.exitingNames.get(0);
                        obj2.getClass();
                        view2 = (View) this.firstOutViews.get((String) obj2);
                        this.transitionImpl.setEpicenter(this.sharedElementTransition, view2);
                    }
                    this.sharedElementLastInViews.addAll(this.lastInViews.values());
                    if (!this.enteringNames.isEmpty()) {
                        Object obj3 = this.enteringNames.get(0);
                        obj3.getClass();
                        final View view3 = (View) this.lastInViews.get((String) obj3);
                        if (view3 != null) {
                            final FragmentTransitionImpl fragmentTransitionImpl = this.transitionImpl;
                            OneShotPreDrawListener.add(viewGroup, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda6
                                @Override // java.lang.Runnable
                                public final void run() {
                                    fragmentTransitionImpl.getBoundsOnScreen(view3, rect);
                                }
                            });
                            z = true;
                        }
                    }
                    this.transitionImpl.setSharedElementTargets(this.sharedElementTransition, view, this.sharedElementFirstOutViews);
                    FragmentTransitionImpl fragmentTransitionImpl2 = this.transitionImpl;
                    Object obj4 = this.sharedElementTransition;
                    fragmentTransitionImpl2.scheduleRemoveTargets(obj4, null, null, null, null, obj4, this.sharedElementLastInViews);
                }
            }
            ArrayList arrayList2 = new ArrayList();
            Iterator it2 = this.transitionInfos.iterator();
            Object objMergeTransitionsTogether = null;
            Object objMergeTransitionsTogether2 = null;
            while (it2.hasNext()) {
                TransitionInfo transitionInfo = (TransitionInfo) it2.next();
                SpecialEffectsController.Operation operation3 = transitionInfo.getOperation();
                z = z;
                Object objCloneTransition = this.transitionImpl.cloneTransition(transitionInfo.getTransition());
                if (objCloneTransition != null) {
                    ArrayList arrayList3 = new ArrayList();
                    Iterator it3 = it2;
                    View view4 = operation3.getFragment().mView;
                    view4.getClass();
                    captureTransitioningViews(arrayList3, view4);
                    if (this.sharedElementTransition != null && (operation3 == operation2 || operation3 == operation)) {
                        if (operation3 == operation2) {
                            arrayList3.removeAll(CollectionsKt.toSet(this.sharedElementFirstOutViews));
                        } else {
                            arrayList3.removeAll(CollectionsKt.toSet(this.sharedElementLastInViews));
                        }
                    }
                    boolean zIsEmpty = arrayList3.isEmpty();
                    FragmentTransitionImpl fragmentTransitionImpl3 = this.transitionImpl;
                    if (zIsEmpty) {
                        fragmentTransitionImpl3.addTarget(objCloneTransition, view);
                        obj = objCloneTransition;
                        arrayList = arrayList3;
                    } else {
                        fragmentTransitionImpl3.addTargets(objCloneTransition, arrayList3);
                        this.transitionImpl.scheduleRemoveTargets(objCloneTransition, objCloneTransition, arrayList3, null, null, null, null);
                        obj = objCloneTransition;
                        arrayList = arrayList3;
                        if (operation3.getFinalState() == SpecialEffectsController.Operation.State.GONE) {
                            operation3.setAwaitingContainerChanges(false);
                            ArrayList arrayList4 = new ArrayList(arrayList);
                            arrayList4.remove(operation3.getFragment().mView);
                            this.transitionImpl.scheduleHideFragmentView(obj, operation3.getFragment().mView, arrayList4);
                            OneShotPreDrawListener.add(viewGroup, new Runnable() { // from class: androidx.fragment.app.DefaultSpecialEffectsController$TransitionEffect$$ExternalSyntheticLambda7
                                @Override // java.lang.Runnable
                                public final void run() {
                                    FragmentTransition.setViewVisibility(arrayList, 4);
                                }
                            });
                        }
                    }
                    if (operation3.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
                        arrayList2.addAll(arrayList);
                        if (z) {
                            this.transitionImpl.setEpicenter(obj, rect);
                        }
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "Entering Transition: " + obj);
                            Log.v("FragmentManager", ">>>>> EnteringViews <<<<<");
                            Iterator it4 = arrayList.iterator();
                            it4.getClass();
                            while (it4.hasNext()) {
                                Object next = it4.next();
                                next.getClass();
                                Log.v("FragmentManager", "View: " + ((View) next));
                            }
                        }
                    } else {
                        this.transitionImpl.setEpicenter(obj, view2);
                        if (FragmentManager.isLoggingEnabled(2)) {
                            Log.v("FragmentManager", "Exiting Transition: " + obj);
                            Log.v("FragmentManager", ">>>>> ExitingViews <<<<<");
                            Iterator it5 = arrayList.iterator();
                            it5.getClass();
                            while (it5.hasNext()) {
                                Object next2 = it5.next();
                                next2.getClass();
                                Log.v("FragmentManager", "View: " + ((View) next2));
                            }
                        }
                    }
                    boolean zIsOverlapAllowed = transitionInfo.isOverlapAllowed();
                    FragmentTransitionImpl fragmentTransitionImpl4 = this.transitionImpl;
                    if (zIsOverlapAllowed) {
                        objMergeTransitionsTogether = fragmentTransitionImpl4.mergeTransitionsTogether(objMergeTransitionsTogether, obj, null);
                    } else {
                        objMergeTransitionsTogether2 = fragmentTransitionImpl4.mergeTransitionsTogether(objMergeTransitionsTogether2, obj, null);
                    }
                    it2 = it3;
                }
            }
            Object objMergeTransitionsInSequence = this.transitionImpl.mergeTransitionsInSequence(objMergeTransitionsTogether, objMergeTransitionsTogether2, this.sharedElementTransition);
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", "Final merged transition: " + objMergeTransitionsInSequence + " for container " + viewGroup);
            }
            return new Pair(arrayList2, objMergeTransitionsInSequence);
        }

        private final void runTransition(ArrayList arrayList, ViewGroup viewGroup, Function0 function0) {
            FragmentTransition.setViewVisibility(arrayList, 4);
            ArrayList arrayListPrepareSetNameOverridesReordered = this.transitionImpl.prepareSetNameOverridesReordered(this.sharedElementLastInViews);
            if (FragmentManager.isLoggingEnabled(2)) {
                Log.v("FragmentManager", ">>>>> Beginning transition <<<<<");
                Log.v("FragmentManager", ">>>>> SharedElementFirstOutViews <<<<<");
                Iterator it = this.sharedElementFirstOutViews.iterator();
                it.getClass();
                while (it.hasNext()) {
                    Object next = it.next();
                    next.getClass();
                    View view = (View) next;
                    Log.v("FragmentManager", "View: " + view + " Name: " + ViewCompat.getTransitionName(view));
                }
                Log.v("FragmentManager", ">>>>> SharedElementLastInViews <<<<<");
                Iterator it2 = this.sharedElementLastInViews.iterator();
                it2.getClass();
                while (it2.hasNext()) {
                    Object next2 = it2.next();
                    next2.getClass();
                    View view2 = (View) next2;
                    Log.v("FragmentManager", "View: " + view2 + " Name: " + ViewCompat.getTransitionName(view2));
                }
            }
            function0.invoke();
            this.transitionImpl.setNameOverridesReordered(viewGroup, this.sharedElementFirstOutViews, this.sharedElementLastInViews, arrayListPrepareSetNameOverridesReordered, this.sharedElementNameMapping);
            FragmentTransition.setViewVisibility(arrayList, 0);
            this.transitionImpl.swapSharedElementTargets(this.sharedElementTransition, this.sharedElementFirstOutViews, this.sharedElementLastInViews);
        }

        @Override // androidx.fragment.app.SpecialEffectsController.Effect
        public void onCancel(ViewGroup viewGroup) {
            viewGroup.getClass();
            this.transitionSignal.cancel();
        }

        private final void captureTransitioningViews(ArrayList arrayList, View view) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                if (ViewGroupCompat.isTransitionGroup(viewGroup)) {
                    if (arrayList.contains(view)) {
                        return;
                    }
                    arrayList.add(view);
                    return;
                }
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = viewGroup.getChildAt(i);
                    if (childAt.getVisibility() == 0) {
                        captureTransitioningViews(arrayList, childAt);
                    }
                }
                return;
            }
            if (arrayList.contains(view)) {
                return;
            }
            arrayList.add(view);
        }
    }

    private final void createTransitionEffect(List list, boolean z, SpecialEffectsController.Operation operation, SpecialEffectsController.Operation operation2) {
        Object objWrapTransitionInSet;
        ArrayList arrayList;
        FragmentTransitionImpl fragmentTransitionImpl;
        ArrayList arrayList2;
        Pair pair;
        ArrayList arrayList3 = new ArrayList();
        for (Object obj : list) {
            if (!((TransitionInfo) obj).isVisibilityUnchanged()) {
                arrayList3.add(obj);
            }
        }
        ArrayList arrayList4 = new ArrayList();
        int size = arrayList3.size();
        int i = 0;
        while (i < size) {
            Object obj2 = arrayList3.get(i);
            i++;
            if (((TransitionInfo) obj2).getHandlingImpl() != null) {
                arrayList4.add(obj2);
            }
        }
        int size2 = arrayList4.size();
        FragmentTransitionImpl fragmentTransitionImpl2 = null;
        int i2 = 0;
        while (i2 < size2) {
            Object obj3 = arrayList4.get(i2);
            i2++;
            TransitionInfo transitionInfo = (TransitionInfo) obj3;
            FragmentTransitionImpl handlingImpl = transitionInfo.getHandlingImpl();
            if (fragmentTransitionImpl2 != null && handlingImpl != fragmentTransitionImpl2) {
                StringBuilder sb = new StringBuilder("Mixing framework transitions and AndroidX transitions is not allowed. Fragment ");
                sb.append(transitionInfo.getOperation().getFragment());
                Object transition = transitionInfo.getTransition();
                sb.append(" returned Transition ");
                sb.append(transition);
                sb.append(" which uses a different Transition type than other Fragments.");
                throw new IllegalArgumentException(sb.toString().toString());
            }
            fragmentTransitionImpl2 = handlingImpl;
        }
        if (fragmentTransitionImpl2 == null) {
            return;
        }
        ArrayList arrayList5 = new ArrayList();
        ArrayList arrayList6 = new ArrayList();
        ArrayMap arrayMap = new ArrayMap();
        ArrayList arrayList7 = new ArrayList();
        ArrayList arrayList8 = new ArrayList();
        ArrayMap arrayMap2 = new ArrayMap();
        ArrayMap arrayMap3 = new ArrayMap();
        int size3 = arrayList4.size();
        int i3 = 0;
        loop3: while (true) {
            objWrapTransitionInSet = null;
            while (true) {
                if (i3 >= size3) {
                    break loop3;
                }
                Object obj4 = arrayList4.get(i3);
                i3++;
                TransitionInfo transitionInfo2 = (TransitionInfo) obj4;
                if (transitionInfo2.hasSharedElementTransition() && operation != null && operation2 != null) {
                    objWrapTransitionInSet = fragmentTransitionImpl2.wrapTransitionInSet(fragmentTransitionImpl2.cloneTransition(transitionInfo2.getSharedElementTransition()));
                    arrayList8 = operation2.getFragment().getSharedElementSourceNames();
                    arrayList8.getClass();
                    ArrayList sharedElementSourceNames = operation.getFragment().getSharedElementSourceNames();
                    sharedElementSourceNames.getClass();
                    ArrayList sharedElementTargetNames = operation.getFragment().getSharedElementTargetNames();
                    sharedElementTargetNames.getClass();
                    arrayList = arrayList5;
                    int size4 = sharedElementTargetNames.size();
                    fragmentTransitionImpl = fragmentTransitionImpl2;
                    int i4 = 0;
                    while (i4 < size4) {
                        int i5 = size4;
                        int iIndexOf = arrayList8.indexOf(sharedElementTargetNames.get(i4));
                        ArrayList arrayList9 = sharedElementTargetNames;
                        if (iIndexOf != -1) {
                            arrayList8.set(iIndexOf, sharedElementSourceNames.get(i4));
                        }
                        i4++;
                        size4 = i5;
                        sharedElementTargetNames = arrayList9;
                    }
                    arrayList7 = operation2.getFragment().getSharedElementTargetNames();
                    arrayList7.getClass();
                    if (z) {
                        operation.getFragment().getEnterTransitionCallback();
                        operation2.getFragment().getExitTransitionCallback();
                        pair = TuplesKt.to(null, null);
                    } else {
                        operation.getFragment().getExitTransitionCallback();
                        operation2.getFragment().getEnterTransitionCallback();
                        pair = TuplesKt.to(null, null);
                    }
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(pair.component1());
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(pair.component2());
                    int i6 = 0;
                    for (int size5 = arrayList8.size(); i6 < size5; size5 = size5) {
                        Object obj5 = arrayList8.get(i6);
                        obj5.getClass();
                        String str = (String) obj5;
                        Object obj6 = arrayList7.get(i6);
                        obj6.getClass();
                        arrayMap.put(str, (String) obj6);
                        i6++;
                    }
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", ">>> entering view names <<<");
                        Iterator it = arrayList7.iterator();
                        it.getClass();
                        while (true) {
                            Iterator it2 = it;
                            if (!it.hasNext()) {
                                break;
                            }
                            Log.v("FragmentManager", "Name: " + ((String) it2.next()));
                            it = it2;
                            arrayList6 = arrayList6;
                        }
                        arrayList2 = arrayList6;
                        Log.v("FragmentManager", ">>> exiting view names <<<");
                        Iterator it3 = arrayList8.iterator();
                        it3.getClass();
                        while (it3.hasNext()) {
                            Log.v("FragmentManager", "Name: " + ((String) it3.next()));
                            it3 = it3;
                        }
                    } else {
                        arrayList2 = arrayList6;
                    }
                    View view = operation.getFragment().mView;
                    view.getClass();
                    findNamedViews(arrayMap2, view);
                    arrayMap2.retainAll(arrayList8);
                    arrayMap.retainAll(arrayMap2.keySet());
                    View view2 = operation2.getFragment().mView;
                    view2.getClass();
                    findNamedViews(arrayMap3, view2);
                    arrayMap3.retainAll(arrayList7);
                    arrayMap3.retainAll(arrayMap.values());
                    FragmentTransition.retainValues(arrayMap, arrayMap3);
                    retainMatchingViews(arrayMap2, arrayMap.keySet());
                    retainMatchingViews(arrayMap3, arrayMap.values());
                    if (arrayMap.isEmpty()) {
                        break;
                    }
                } else {
                    arrayList = arrayList5;
                    fragmentTransitionImpl = fragmentTransitionImpl2;
                    arrayList2 = arrayList6;
                }
                arrayList5 = arrayList;
                fragmentTransitionImpl2 = fragmentTransitionImpl;
                arrayList6 = arrayList2;
            }
            Log.i("FragmentManager", "Ignoring shared elements transition " + objWrapTransitionInSet + " between " + operation + " and " + operation2 + " as there are no matching elements in both the entering and exiting fragment. In order to run a SharedElementTransition, both fragments involved must have the element.");
            arrayList.clear();
            arrayList2.clear();
            arrayList5 = arrayList;
            fragmentTransitionImpl2 = fragmentTransitionImpl;
            arrayList6 = arrayList2;
        }
        ArrayList arrayList10 = arrayList5;
        FragmentTransitionImpl fragmentTransitionImpl3 = fragmentTransitionImpl2;
        ArrayList arrayList11 = arrayList6;
        if (objWrapTransitionInSet == null) {
            if (arrayList4.isEmpty()) {
                return;
            }
            int size6 = arrayList4.size();
            int i7 = 0;
            while (i7 < size6) {
                Object obj7 = arrayList4.get(i7);
                i7++;
                if (((TransitionInfo) obj7).getTransition() == null) {
                }
            }
            return;
        }
        TransitionEffect transitionEffect = new TransitionEffect(arrayList4, operation, operation2, fragmentTransitionImpl3, objWrapTransitionInSet, arrayList10, arrayList11, arrayMap, arrayList7, arrayList8, arrayMap2, arrayMap3, z);
        int size7 = arrayList4.size();
        int i8 = 0;
        while (i8 < size7) {
            Object obj8 = arrayList4.get(i8);
            i8++;
            ((TransitionInfo) obj8).getOperation().addEffect(transitionEffect);
        }
    }

    /* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
    public final class Api24Impl {
        public static final Api24Impl INSTANCE = new Api24Impl();

        private Api24Impl() {
        }

        public final long totalDuration(AnimatorSet animatorSet) {
            animatorSet.getClass();
            return animatorSet.getTotalDuration();
        }
    }

    /* JADX INFO: compiled from: DefaultSpecialEffectsController.kt */
    public final class Api26Impl {
        public static final Api26Impl INSTANCE = new Api26Impl();

        private Api26Impl() {
        }

        public final void reverse(AnimatorSet animatorSet) {
            animatorSet.getClass();
            animatorSet.reverse();
        }

        public final void setCurrentPlayTime(AnimatorSet animatorSet, long j) {
            animatorSet.getClass();
            animatorSet.setCurrentPlayTime(j);
        }
    }
}
