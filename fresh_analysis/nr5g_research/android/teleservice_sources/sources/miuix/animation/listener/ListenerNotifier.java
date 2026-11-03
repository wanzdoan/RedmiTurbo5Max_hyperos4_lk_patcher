package miuix.animation.listener;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import miuix.animation.FolmeFactory;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.internal.FolmeEngine;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.ObjectPool;

/* JADX INFO: loaded from: classes2.dex */
public class ListenerNotifier {
    final Map mListenerMap = new ConcurrentHashMap();
    final IAnimTarget mTarget;
    static final BeginNotifier sBegin = new BeginNotifier();
    static final UpdateNotifier sUpdate = new UpdateNotifier();
    static final CancelNotifier sCancelAll = new CancelNotifier();
    static final EndNotifier sEndAll = new EndNotifier();

    interface INotifier {
        void doNotify(Object obj, TransitionListener transitionListener, Collection collection, UpdateInfo updateInfo);
    }

    class BeginNotifier implements INotifier {
        BeginNotifier() {
        }

        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection collection, UpdateInfo updateInfo) {
            transitionListener.onBegin(obj);
            if (collection != null) {
                transitionListener.onBegin(obj, collection);
            }
        }
    }

    class PropertyBeginNotifier implements INotifier {
        PropertyBeginNotifier() {
        }

        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection collection, UpdateInfo updateInfo) {
            transitionListener.onBegin(obj, collection);
        }
    }

    class MassUpdateNotifier implements INotifier {
        static final List sEmptyList = new ArrayList();

        MassUpdateNotifier() {
        }

        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection collection, UpdateInfo updateInfo) {
            transitionListener.onUpdate(obj, sEmptyList);
        }
    }

    class UpdateNotifier implements INotifier {
        UpdateNotifier() {
        }

        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection collection, UpdateInfo updateInfo) {
            transitionListener.onUpdate(obj, collection);
        }
    }

    class CancelNotifier implements INotifier {
        CancelNotifier() {
        }

        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection collection, UpdateInfo updateInfo) {
            transitionListener.onCancel(obj);
        }
    }

    class EndNotifier implements INotifier {
        EndNotifier() {
        }

        @Override // miuix.animation.listener.ListenerNotifier.INotifier
        public void doNotify(Object obj, TransitionListener transitionListener, Collection collection, UpdateInfo updateInfo) {
            transitionListener.onComplete(obj);
        }
    }

    public ListenerNotifier(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
    }

    public boolean addListeners(Object obj, AnimConfig animConfig) {
        if (animConfig.listeners.isEmpty()) {
            return false;
        }
        CommonUtils.addTo(animConfig.listeners, getListenerSet(obj));
        return true;
    }

    public void removeListeners(Object obj) {
        List list = (List) this.mListenerMap.remove(obj);
        FolmeEngine engine = FolmeFactory.getEngine();
        if (engine != null) {
            ObjectPool.release(engine.getObjPool(), list);
        }
    }

    public void removeListeners() {
        FolmeEngine engine = FolmeFactory.getEngine();
        if (engine != null) {
            ObjectPool.release(engine.getObjPool(), this.mListenerMap.values());
        }
        this.mListenerMap.clear();
    }

    private List getListenerSet(Object obj) {
        List arrayList = (List) this.mListenerMap.get(obj);
        if (arrayList == null) {
            FolmeEngine engine = FolmeFactory.getEngine();
            if (engine != null) {
                arrayList = (List) ObjectPool.acquire(engine.getObjPool(), ArrayList.class, new Object[0]);
            } else {
                arrayList = new ArrayList();
            }
            this.mListenerMap.put(obj, arrayList);
        }
        return arrayList;
    }

    public void notifyBegin(Object obj, Object obj2, Collection collection, Set set) {
        notify(obj, obj2, sBegin, collection, null, set);
    }

    public void notifyUpdate(Object obj, Object obj2, Collection collection, Set set) {
        notify(obj, obj2, sUpdate, collection, null, set);
    }

    public void notifyCancelAll(Object obj, Object obj2, Set set) {
        notify(obj, obj2, sCancelAll, null, null, set);
    }

    public void notifyEndAll(Object obj, Object obj2, Set set) {
        notify(obj, obj2, sEndAll, null, null, set);
    }

    private void notify(Object obj, Object obj2, INotifier iNotifier, Collection collection, UpdateInfo updateInfo, Set set) {
        List list = (List) this.mListenerMap.get(obj);
        if (list == null || list.isEmpty()) {
            return;
        }
        notifyListenerSet(obj2, list, iNotifier, collection, updateInfo, set);
    }

    private static void notifyListenerSet(Object obj, List list, INotifier iNotifier, Collection collection, UpdateInfo updateInfo, Set set) {
        set.addAll(list);
        Iterator it = set.iterator();
        while (it.hasNext()) {
            TransitionListener transitionListener = (TransitionListener) it.next();
            if (transitionListener == null) {
                Log.e(CommonUtils.TAG, "listener null tag=" + obj);
            } else {
                iNotifier.doNotify(obj, transitionListener, collection, updateInfo);
            }
        }
        set.clear();
    }
}
