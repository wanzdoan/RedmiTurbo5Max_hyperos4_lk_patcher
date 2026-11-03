package miuix.internal.util;

import android.view.MotionEvent;
import android.view.View;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;
import miuix.appcompat.R$attr;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AnimHelper {
    private static volatile boolean sIsDebugEnabled = false;

    public static void addItemPressEffect(View view) {
        if (view == null) {
            return;
        }
        view.setForeground(AttributeResolver.resolveDrawable(view.getContext(), R$attr.itemPressEffectForeground));
        if (view.isClickable()) {
            return;
        }
        view.setOnHoverListener(new View.OnHoverListener() { // from class: miuix.internal.util.AnimHelper$$ExternalSyntheticLambda1
            @Override // android.view.View.OnHoverListener
            public final boolean onHover(View view2, MotionEvent motionEvent) {
                return AnimHelper.m5464$r8$lambda$090dziXw8w7or7gWr5GFy8fBbs(view2, motionEvent);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$090dziXw8w7or7gWr-5GFy8fBbs, reason: not valid java name */
    public static /* synthetic */ boolean m5464$r8$lambda$090dziXw8w7or7gWr5GFy8fBbs(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 9) {
            view.setHovered(true);
            return false;
        }
        if (motionEvent.getAction() == 10) {
            view.setHovered(false);
        }
        return false;
    }

    public static void addItemPressEffect(View view, boolean z) {
        if (view == null) {
            return;
        }
        if (!z) {
            addItemPressEffect(view);
            return;
        }
        view.setForeground(AttributeResolver.resolveDrawable(view.getContext(), R$attr.itemPressRoundEffectForeground));
        if (view.isClickable()) {
            return;
        }
        view.setOnHoverListener(new View.OnHoverListener() { // from class: miuix.internal.util.AnimHelper$$ExternalSyntheticLambda0
            @Override // android.view.View.OnHoverListener
            public final boolean onHover(View view2, MotionEvent motionEvent) {
                return AnimHelper.m5465$r8$lambda$vu16R1tcSCJ5cqP2T92L3E4rXM(view2, motionEvent);
            }
        });
    }

    /* JADX INFO: renamed from: $r8$lambda$vu16R1tcSCJ5cqP2T92L3E-4rXM, reason: not valid java name */
    public static /* synthetic */ boolean m5465$r8$lambda$vu16R1tcSCJ5cqP2T92L3E4rXM(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == 9) {
            view.setHovered(true);
            return false;
        }
        if (motionEvent.getAction() == 10) {
            view.setHovered(false);
        }
        return false;
    }

    public static void addPressAnim(View view) {
        addPressAnim(view, IHoverStyle.HoverEffect.NORMAL);
    }

    public static void addPressAnim(View view, IHoverStyle.HoverEffect hoverEffect) {
        Folme.useAt(view).touch().setScale(1.0f, new ITouchStyle.TouchType[0]).handleTouchOf(view, new AnimConfig[0]);
        Folme.useAt(view).hover().setEffect(hoverEffect).handleHoverOf(view, new AnimConfig[0]);
    }

    public static void addPressAnimWithBg(View view) {
        ITouchStyle scale = Folme.useAt(view).touch().setTint(0).setScale(1.0f, new ITouchStyle.TouchType[0]);
        IHoverStyle effect = Folme.useAt(view).hover().setTint(0).setEffect(IHoverStyle.HoverEffect.NORMAL);
        if (AttributeResolver.resolveBoolean(view.getContext(), R$attr.isLightTheme, true)) {
            scale.setBackgroundColor(335544320);
            effect.setBackgroundColor(335544320);
        } else {
            scale.setBackgroundColor(620756991);
            effect.setBackgroundColor(620756991);
        }
        scale.handleTouchOf(view, new AnimConfig[0]);
        effect.handleHoverOf(view, new AnimConfig[0]);
    }

    public static boolean isDialogDebugInAndroidUIThreadEnabled() {
        return sIsDebugEnabled;
    }
}
