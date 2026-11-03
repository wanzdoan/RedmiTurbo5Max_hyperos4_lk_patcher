package miuix.animation.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import miuix.animation.FolmeFactory;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.ListenerNotifier;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.utils.LogUtils;
import miuix.animation.utils.ObjectPool;

/* JADX INFO: loaded from: classes2.dex */
public class NotifyManager {
    private AnimConfig mConfig = new AnimConfig();
    ListenerNotifier mNotifier;
    ListenerNotifier mSetToNotifier;
    IAnimTarget mTarget;

    public NotifyManager(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
        this.mSetToNotifier = new ListenerNotifier(iAnimTarget);
        this.mNotifier = new ListenerNotifier(iAnimTarget);
    }

    public void setToNotify(AnimState animState, AnimConfigLink animConfigLink) {
        Set hashSet;
        boolean zIsLogMainEnabled = LogUtils.isLogMainEnabled();
        if (zIsLogMainEnabled) {
            LogUtils.debug("setTo->setToNotify start", new Object[0]);
        }
        if (animConfigLink == null) {
            if (zIsLogMainEnabled) {
                LogUtils.debug("setTo->setToNotify warning!! configLink is null, return", new Object[0]);
                return;
            }
            return;
        }
        Object tag = animState.getTag();
        this.mConfig.copy(animState.getConfig());
        animConfigLink.addTo(this.mConfig);
        if (!this.mSetToNotifier.addListeners(tag, this.mConfig)) {
            this.mConfig.clear();
            return;
        }
        FolmeEngine engine = FolmeFactory.getEngine();
        ObjectPool objPool = engine != null ? engine.getObjPool() : null;
        if (objPool != null) {
            hashSet = (Set) ObjectPool.acquire(objPool, HashSet.class, new Object[0]);
        } else {
            hashSet = new HashSet();
        }
        dispatchSetToNotifications(tag, animState, hashSet, zIsLogMainEnabled);
        this.mSetToNotifier.removeListeners(tag);
        if (objPool != null) {
            ObjectPool.release(objPool, hashSet);
        }
        this.mConfig.clear();
    }

    private void dispatchSetToNotifications(Object obj, AnimState animState, Set set, boolean z) {
        this.mSetToNotifier.notifyBegin(obj, obj, null, set);
        if (z) {
            LogUtils.debug("setTo->setToNotify >>> onStart", new Object[0]);
        }
        List listCollectMatchingUpdates = collectMatchingUpdates(animState);
        this.mSetToNotifier.notifyUpdate(obj, obj, listCollectMatchingUpdates, set);
        if (z) {
            LogUtils.debug("setTo->setToNotify >>> onUpdate updates.size=" + listCollectMatchingUpdates.size(), new Object[0]);
        }
        this.mSetToNotifier.notifyEndAll(obj, obj, set);
        if (z) {
            LogUtils.debug("setTo->setToNotify >>> onComplete", new Object[0]);
        }
    }

    private List collectMatchingUpdates(AnimState animState) {
        ArrayList arrayList = new ArrayList();
        for (UpdateInfo updateInfo : this.mTarget.animManager.mUpdateMap.values()) {
            if (animState.contains(updateInfo.property)) {
                arrayList.add(updateInfo);
            }
        }
        return arrayList;
    }

    public ListenerNotifier getNotifier() {
        return this.mNotifier;
    }
}
