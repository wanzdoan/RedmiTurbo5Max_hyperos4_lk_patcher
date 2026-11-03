package miuix.animation.internal;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.util.List;
import miuix.animation.IAnimTarget;
import miuix.animation.utils.LogUtils;

/* JADX INFO: loaded from: classes2.dex */
public final class TargetHandler extends Handler {
    public static final int ANIM_MSG_END = 2;
    public static final int ANIM_MSG_REMOVE_WAIT = 3;
    public static final int ANIM_MSG_REPLACED = 5;
    public static final int ANIM_MSG_REPLACE_LISTENER = 4;
    public static final int ANIM_MSG_START = 0;
    public static final int ANIM_MSG_UPDATE = 1;
    private final IAnimTarget mTarget;

    public TargetHandler(Looper looper, IAnimTarget iAnimTarget) {
        super(looper);
        this.mTarget = iAnimTarget;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            TransitionInfo transitionInfo = (TransitionInfo) this.mTarget.animManager.mTempTransMap.remove(Integer.valueOf(message.arg1));
            if (LogUtils.isLogMoreEnable()) {
                LogUtils.debug("<<< receive msg=ANIM_MSG_START, info=" + transitionInfo + ", info.id=" + message.arg1 + ", obj =" + message.obj + ", target=" + this.mTarget, new Object[0]);
            }
            if (transitionInfo == null || transitionInfo.hasOnStart) {
                return;
            }
            AnimManager animManager = this.mTarget.animManager;
            Object obj = message.obj;
            animManager.onStart(transitionInfo, obj != null ? (List) obj : null, message.arg2 == 1);
            return;
        }
        if (i == 1) {
            TransitionInfo transitionInfo2 = (TransitionInfo) this.mTarget.animManager.mTempTransForUpdateMap.remove(Integer.valueOf(message.arg1));
            if (LogUtils.isLogMoreEnable()) {
                LogUtils.debug("<<< receive msg=ANIM_MSG_UPDATE, info=" + transitionInfo2 + ", info.id=" + message.arg1 + ", obj =" + message.obj + ", target=" + this.mTarget, new Object[0]);
            }
            if (transitionInfo2 != null) {
                AnimManager animManager2 = this.mTarget.animManager;
                Object obj2 = message.obj;
                animManager2.onUpdate(transitionInfo2, obj2 != null ? (List) obj2 : null);
                return;
            }
            return;
        }
        if (i == 2) {
            TransitionInfo transitionInfo3 = (TransitionInfo) this.mTarget.animManager.mTempTransMap.remove(Integer.valueOf(message.arg1));
            if (LogUtils.isLogMoreEnable()) {
                LogUtils.debug("<<< receive msg=ANIM_MSG_END, info=" + transitionInfo3 + ", info.id=" + message.arg1 + ", obj =" + message.obj + ", target=" + this.mTarget, new Object[0]);
            }
            if (transitionInfo3 != null) {
                this.mTarget.animManager.onEnd(transitionInfo3, message.arg2);
                return;
            }
            return;
        }
        if (i == 3) {
            if (LogUtils.isLogMoreEnable()) {
                LogUtils.debug("<<< receive msg=ANIM_MSG_REMOVE_WAIT, target=" + this.mTarget, new Object[0]);
            }
            this.mTarget.animManager.onRemoveWait();
            return;
        }
        if (i == 4) {
            TransitionInfo transitionInfo4 = (TransitionInfo) this.mTarget.animManager.mTempTransMap.remove(Integer.valueOf(message.arg1));
            if (LogUtils.isLogMoreEnable()) {
                LogUtils.debug("<<< receive msg=ANIM_MSG_REPLACE_LISTENER, info=" + transitionInfo4 + ", info.id=" + message.arg1 + ", obj =" + message.obj + ", target=" + this.mTarget, new Object[0]);
            }
            if (transitionInfo4 != null) {
                this.mTarget.animManager.onReplaceListeners(transitionInfo4);
                return;
            }
            return;
        }
        if (i != 5) {
            return;
        }
        TransitionInfo transitionInfo5 = (TransitionInfo) this.mTarget.animManager.mTempTransMap.remove(Integer.valueOf(message.arg1));
        if (LogUtils.isLogMoreEnable()) {
            LogUtils.debug("<<< receive msg=ANIM_MSG_REPLACED, info=" + transitionInfo5 + ", info.id=" + message.arg1 + ", obj =" + message.obj + ", target=" + this.mTarget, new Object[0]);
        }
        if (transitionInfo5 != null) {
            this.mTarget.animManager.onReplaced(transitionInfo5);
        }
    }

    public boolean isInTargetThread() {
        return getLooper().isCurrentThread();
    }
}
