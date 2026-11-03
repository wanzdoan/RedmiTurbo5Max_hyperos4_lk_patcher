package miuix.animation.controller;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import miuix.animation.FolmeEase;
import miuix.animation.IAnimTarget;
import miuix.animation.IBlinkStyle;
import miuix.animation.ITouchStyle;
import miuix.animation.R;
import miuix.animation.base.AnimConfig;
import miuix.animation.internal.BlinkStateObserver;
import miuix.animation.internal.BlinkStateSubject;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewPropertyExt;
import miuix.animation.styles.ForegroundColorStyle;
import miuix.animation.utils.LogUtils;
import miuix.folme.R$color;

/* JADX INFO: loaded from: classes2.dex */
public class FolmeBlink extends FolmeBase implements IBlinkStyle, BlinkStateSubject {
    private static final String ALIAS_BLINK_HIGHLIGHT = "blinkHighLight";
    private static final String ALIAS_BLINK_NORMAL = "blinkNormal";
    private boolean mBlinkEnabled;
    private List mBlinkStateObserverList;
    private Runnable mBlinkTask;
    int mFlashCount;
    private long mInterval;
    private int mLimitCount;
    private Drawable[] mOriginFgs;
    private AnimConfig mStopConfig;
    private IAnimTarget[] mTargets;
    private AnimConfig mToHighlightConfig;
    private AnimConfig mToNormalConfig;

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle resetConfig() {
        this.mToHighlightConfig.setEase(FolmeEase.cubicOut(600L)).addListeners(new TransitionListener() { // from class: miuix.animation.controller.FolmeBlink.2
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                if (LogUtils.isLogMainEnabled()) {
                    LogUtils.debug("blink.reset: onComplete " + obj, new Object[0]);
                }
                IFolmeStateStyle iFolmeStateStyle = FolmeBlink.this.mState;
                iFolmeStateStyle.to(iFolmeStateStyle.getState(IBlinkStyle.BlinkType.NORMAL), FolmeBlink.this.mToNormalConfig);
            }
        });
        this.mToNormalConfig.setEase(FolmeEase.sinInOut(400L)).addListeners(new TransitionListener() { // from class: miuix.animation.controller.FolmeBlink.3
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                FolmeBlink folmeBlink = FolmeBlink.this;
                folmeBlink.mFlashCount++;
                Object targetObject = folmeBlink.mState.getTarget().getTargetObject();
                FolmeBlink folmeBlink2 = FolmeBlink.this;
                if (folmeBlink2.mFlashCount != folmeBlink2.mLimitCount && targetObject != null) {
                    FolmeBlink.this.doStartBlink();
                    return;
                }
                FolmeBlink folmeBlink3 = FolmeBlink.this;
                folmeBlink3.mFlashCount = 0;
                folmeBlink3.restoreOriginFgs(folmeBlink3.mTargets);
                FolmeBlink.this.notifyState(true);
            }
        });
        this.mFlashCount = 0;
        this.mInterval = 0L;
        this.mLimitCount = 1;
        return this;
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setToHighlightConfig(AnimConfig animConfig) {
        this.mToHighlightConfig = animConfig;
        animConfig.addListeners(new TransitionListener() { // from class: miuix.animation.controller.FolmeBlink.4
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                IFolmeStateStyle iFolmeStateStyle = FolmeBlink.this.mState;
                iFolmeStateStyle.to(iFolmeStateStyle.getState(IBlinkStyle.BlinkType.NORMAL), FolmeBlink.this.mToNormalConfig);
            }
        });
        return this;
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setToNormalConfig(AnimConfig animConfig) {
        this.mToNormalConfig = animConfig;
        animConfig.addListeners(new TransitionListener() { // from class: miuix.animation.controller.FolmeBlink.5
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                FolmeBlink folmeBlink = FolmeBlink.this;
                folmeBlink.mFlashCount++;
                Object targetObject = folmeBlink.mState.getTarget().getTargetObject();
                FolmeBlink folmeBlink2 = FolmeBlink.this;
                if (folmeBlink2.mFlashCount != folmeBlink2.mLimitCount && targetObject != null) {
                    FolmeBlink.this.doStartBlink();
                    return;
                }
                FolmeBlink folmeBlink3 = FolmeBlink.this;
                folmeBlink3.mFlashCount = 0;
                folmeBlink3.restoreOriginFgs(folmeBlink3.mTargets);
                FolmeBlink.this.notifyState(true);
            }
        });
        return this;
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setInterval(long j) {
        this.mInterval = j;
        return this;
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setLimitCount(int i) {
        this.mLimitCount = i;
        return this;
    }

    public FolmeBlink(IAnimTarget... iAnimTargetArr) {
        super(iAnimTargetArr);
        this.mInterval = 0L;
        this.mLimitCount = 1;
        this.mFlashCount = 0;
        this.mBlinkStateObserverList = new CopyOnWriteArrayList();
        this.mBlinkEnabled = true;
        this.mToHighlightConfig = new AnimConfig().setEase(FolmeEase.cubicOut(600L));
        this.mToNormalConfig = new AnimConfig().setEase(FolmeEase.sinInOut(400L));
        this.mStopConfig = new AnimConfig().setEase(FolmeEase.cubicOut(100L));
        this.mBlinkTask = new Runnable() { // from class: miuix.animation.controller.FolmeBlink.1
            @Override // java.lang.Runnable
            public void run() {
                if (FolmeBlink.this.mBlinkEnabled) {
                    IFolmeStateStyle iFolmeStateStyle = FolmeBlink.this.mState;
                    iFolmeStateStyle.to(iFolmeStateStyle.getState(IBlinkStyle.BlinkType.HIGHLIGHT), FolmeBlink.this.mToHighlightConfig);
                }
            }
        };
        this.mTargets = iAnimTargetArr;
        saveOriginFgs(iAnimTargetArr);
        this.mState.addState(new AnimState(IBlinkStyle.BlinkType.HIGHLIGHT, ALIAS_BLINK_HIGHLIGHT));
        this.mState.addState(new AnimState(IBlinkStyle.BlinkType.NORMAL, ALIAS_BLINK_NORMAL));
        setTintColor();
        this.mToNormalConfig.addListeners(new TransitionListener() { // from class: miuix.animation.controller.FolmeBlink.6
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                FolmeBlink.this.mFlashCount++;
                if (LogUtils.isLogMainEnabled()) {
                    LogUtils.debug("blink: onComplete " + obj + " flashcount=" + FolmeBlink.this.mFlashCount, new Object[0]);
                }
                Object targetObject = FolmeBlink.this.mState.getTarget().getTargetObject();
                FolmeBlink folmeBlink = FolmeBlink.this;
                if (folmeBlink.mFlashCount != folmeBlink.mLimitCount && targetObject != null) {
                    FolmeBlink.this.doStartBlink();
                    return;
                }
                FolmeBlink folmeBlink2 = FolmeBlink.this;
                folmeBlink2.mFlashCount = 0;
                folmeBlink2.restoreOriginFgs(folmeBlink2.mTargets);
                FolmeBlink.this.notifyState(true);
            }
        });
        this.mToHighlightConfig.addListeners(new TransitionListener() { // from class: miuix.animation.controller.FolmeBlink.7
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                if (LogUtils.isLogMainEnabled()) {
                    LogUtils.debug("blink: onComplete " + obj, new Object[0]);
                }
                IFolmeStateStyle iFolmeStateStyle = FolmeBlink.this.mState;
                iFolmeStateStyle.to(iFolmeStateStyle.getState(IBlinkStyle.BlinkType.NORMAL), FolmeBlink.this.mToNormalConfig);
            }
        });
        this.mStopConfig.addListeners(new TransitionListener() { // from class: miuix.animation.controller.FolmeBlink.8
            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                FolmeBlink.this.mState.cancel(ViewPropertyExt.FOREGROUND);
                FolmeBlink folmeBlink = FolmeBlink.this;
                folmeBlink.mFlashCount = 0;
                folmeBlink.mBlinkEnabled = false;
                FolmeBlink folmeBlink2 = FolmeBlink.this;
                folmeBlink2.restoreOriginFgs(folmeBlink2.mTargets);
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onCancel(Object obj) {
                super.onCancel(obj);
                FolmeBlink.this.mState.cancel(ViewPropertyExt.FOREGROUND);
                FolmeBlink folmeBlink = FolmeBlink.this;
                folmeBlink.mFlashCount = 0;
                folmeBlink.mBlinkEnabled = false;
                FolmeBlink folmeBlink2 = FolmeBlink.this;
                folmeBlink2.restoreOriginFgs(folmeBlink2.mTargets);
            }
        });
    }

    private void setTintColor() {
        int iArgb = Color.argb(30, 0, 0, 0);
        Object targetObject = this.mState.getTarget().getTargetObject();
        if (targetObject instanceof View) {
            iArgb = ((View) targetObject).getResources().getColor(R$color.miuix_folme_color_blink_tint);
        }
        ViewPropertyExt.ForegroundProperty foregroundProperty = ViewPropertyExt.FOREGROUND;
        this.mState.getState(IBlinkStyle.BlinkType.HIGHLIGHT).add(foregroundProperty, iArgb);
        this.mState.getState(IBlinkStyle.BlinkType.NORMAL).add(foregroundProperty, 0.0d);
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setBlinkRect(RectF rectF, ITouchStyle.TouchRectGravity touchRectGravity) {
        setTargetValue(R.id.miuix_animation_tag_view_touch_rect, rectF);
        setTargetValue(R.id.miuix_animation_tag_view_touch_rect_gravity, touchRectGravity);
        setTargetValue(R.id.miuix_animation_tag_view_touch_rect_location_mode, Integer.valueOf(ForegroundColorStyle.MIUIX_TOUCH_RECT_LOCATION_MODE_RELATIVE));
        return this;
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setBlinkPadding(float f, float f2, float f3, float f4) {
        setTargetValue(R.id.miuix_animation_tag_view_touch_padding_rect, new RectF(f, f2, f3, f4));
        setTargetValue(R.id.miuix_animation_tag_view_touch_rect_location_mode, 4);
        return this;
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setBlinkRadius(float f) {
        setTargetValue(R.id.miuix_animation_tag_view_touch_corners, Float.valueOf(f));
        return this;
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setBlinkRadius(float f, float f2, float f3, float f4) {
        setTargetValue(R.id.miuix_animation_tag_view_touch_corners, new RectF(f, f2, f3, f4));
        return this;
    }

    @Override // miuix.animation.IBlinkStyle
    public IBlinkStyle setTintMode(int i) {
        this.mToHighlightConfig.setTintMode(i);
        this.mToNormalConfig.setTintMode(i);
        return this;
    }

    private void setTargetValue(int i, Object obj) {
        Object targetObject = this.mState.getTarget().getTargetObject();
        if (targetObject instanceof View) {
            ((View) targetObject).setTag(i, obj);
        }
    }

    @Override // miuix.animation.IBlinkStyle
    public void startBlink(AnimConfig... animConfigArr) {
        if (animConfigArr.length > 0) {
            setToHighlightConfig(animConfigArr[0]);
            if (animConfigArr.length > 1) {
                AnimConfig animConfig = animConfigArr[0];
                AnimConfig animConfig2 = animConfigArr[1];
                if (animConfig != animConfig2) {
                    setToNormalConfig(animConfig2);
                }
            }
        }
        this.mBlinkEnabled = true;
        doStartBlink();
    }

    @Override // miuix.animation.IBlinkStyle
    public void startBlink(int i, AnimConfig... animConfigArr) {
        this.mLimitCount = i;
        if (animConfigArr.length > 0) {
            setToHighlightConfig(animConfigArr[0]);
            if (animConfigArr.length > 1) {
                AnimConfig animConfig = animConfigArr[0];
                AnimConfig animConfig2 = animConfigArr[1];
                if (animConfig != animConfig2) {
                    setToNormalConfig(animConfig2);
                }
            }
        }
        this.mBlinkEnabled = true;
        doStartBlink();
    }

    @Override // miuix.animation.IBlinkStyle
    public void stopBlink() {
        this.mState.getTarget().removeTask(this.mBlinkTask);
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        iFolmeStateStyle.to(iFolmeStateStyle.getState(IBlinkStyle.BlinkType.NORMAL), this.mStopConfig);
    }

    @Override // miuix.animation.internal.BlinkStateSubject
    public void attach(BlinkStateObserver blinkStateObserver) {
        this.mBlinkStateObserverList.add(blinkStateObserver);
    }

    @Override // miuix.animation.internal.BlinkStateSubject
    public void detach(BlinkStateObserver blinkStateObserver) {
        this.mBlinkStateObserverList.remove(blinkStateObserver);
    }

    @Override // miuix.animation.internal.BlinkStateSubject
    public void notifyState(boolean z) {
        Iterator it = this.mBlinkStateObserverList.iterator();
        while (it.hasNext()) {
            ((BlinkStateObserver) it.next()).updateBlinkState(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStartBlink() {
        if (this.mBlinkTask != null) {
            this.mState.getTarget().postDelayed(this.mBlinkTask, this.mFlashCount == 0 ? 0L : this.mInterval);
        }
    }

    private void saveOriginFgs(IAnimTarget[] iAnimTargetArr) {
        this.mOriginFgs = new Drawable[iAnimTargetArr.length];
        for (int i = 0; i < iAnimTargetArr.length; i++) {
            IAnimTarget iAnimTarget = iAnimTargetArr[i];
            if (iAnimTarget.getTargetObject() instanceof View) {
                this.mOriginFgs[i] = ((View) iAnimTarget.getTargetObject()).getForeground();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreOriginFgs(IAnimTarget[] iAnimTargetArr) {
        for (int i = 0; i < iAnimTargetArr.length; i++) {
            IAnimTarget iAnimTarget = iAnimTargetArr[i];
            if (iAnimTarget.getTargetObject() instanceof View) {
                ((View) iAnimTarget.getTargetObject()).setForeground(this.mOriginFgs[i]);
            }
        }
    }
}
