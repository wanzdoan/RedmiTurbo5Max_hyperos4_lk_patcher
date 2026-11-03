package miuix.core.util;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline0;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import miuix.animation.Folme$$ExternalSyntheticBUOutline0;

/* JADX INFO: loaded from: classes2.dex */
public abstract class Pools {
    private static final HashMap mInstanceHolderMap = new HashMap();
    private static final HashMap mSoftReferenceInstanceHolderMap = new HashMap();
    private static final Pool mStringBuilderPool = createSoftReferencePool(new Manager() { // from class: miuix.core.util.Pools.1
        @Override // miuix.core.util.Pools.Manager
        public StringBuilder createInstance() {
            return new StringBuilder();
        }

        @Override // miuix.core.util.Pools.Manager
        public void onRelease(StringBuilder sb) {
            sb.setLength(0);
        }
    }, 4);

    interface IInstanceHolder {
        Object get();

        boolean put(Object obj);
    }

    public abstract class Manager {
        public abstract Object createInstance();

        public void onAcquire(Object obj) {
        }

        public void onDestroy(Object obj) {
        }

        public void onRelease(Object obj) {
        }
    }

    public interface Pool {
        Object acquire();

        void release(Object obj);
    }

    class SoftReferenceInstanceHolder implements IInstanceHolder {
        private final Class mClazz;
        private volatile SoftReference[] mElements;
        private volatile int mIndex = 0;
        private volatile int mSize;

        SoftReferenceInstanceHolder(Class cls, int i) {
            this.mClazz = cls;
            this.mSize = i;
            this.mElements = new SoftReference[i];
        }

        public Class getElementClass() {
            return this.mClazz;
        }

        public synchronized void resize(int i) {
            try {
                int i2 = i + this.mSize;
                if (i2 <= 0) {
                    synchronized (Pools.mSoftReferenceInstanceHolderMap) {
                        Pools.mSoftReferenceInstanceHolderMap.remove(getElementClass());
                    }
                    return;
                }
                this.mSize = i2;
                SoftReference[] softReferenceArr = this.mElements;
                int i3 = this.mIndex;
                if (i2 > softReferenceArr.length) {
                    SoftReference[] softReferenceArr2 = new SoftReference[i2];
                    System.arraycopy(softReferenceArr, 0, softReferenceArr2, 0, i3);
                    this.mElements = softReferenceArr2;
                }
            } catch (Throwable th) {
                throw th;
            }
        }

        @Override // miuix.core.util.Pools.IInstanceHolder
        public synchronized Object get() {
            int i = this.mIndex;
            SoftReference[] softReferenceArr = this.mElements;
            while (i != 0) {
                i--;
                SoftReference softReference = softReferenceArr[i];
                if (softReference != null) {
                    Object obj = softReference.get();
                    softReferenceArr[i] = null;
                    if (obj != null) {
                        this.mIndex = i;
                        return obj;
                    }
                }
            }
            return null;
        }

        @Override // miuix.core.util.Pools.IInstanceHolder
        public synchronized boolean put(Object obj) {
            int i;
            try {
                int i2 = this.mIndex;
                SoftReference[] softReferenceArr = this.mElements;
                if (i2 < this.mSize) {
                    softReferenceArr[i2] = new SoftReference(obj);
                    this.mIndex = i2 + 1;
                    return true;
                }
                for (0; i < i2; i + 1) {
                    SoftReference softReference = softReferenceArr[i];
                    i = (softReference == null || softReference.get() == null) ? 0 : i + 1;
                    softReferenceArr[i] = new SoftReference(obj);
                    return true;
                }
                return false;
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public static Pool getStringBuilderPool() {
        return mStringBuilderPool;
    }

    static SoftReferenceInstanceHolder onSoftReferencePoolCreate(Class cls, int i) {
        SoftReferenceInstanceHolder softReferenceInstanceHolder;
        HashMap map = mSoftReferenceInstanceHolderMap;
        synchronized (map) {
            try {
                softReferenceInstanceHolder = (SoftReferenceInstanceHolder) map.get(cls);
                if (softReferenceInstanceHolder == null) {
                    softReferenceInstanceHolder = new SoftReferenceInstanceHolder(cls, i);
                    map.put(cls, softReferenceInstanceHolder);
                } else {
                    softReferenceInstanceHolder.resize(i);
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return softReferenceInstanceHolder;
    }

    static void onSoftReferencePoolClose(SoftReferenceInstanceHolder softReferenceInstanceHolder, int i) {
        synchronized (mSoftReferenceInstanceHolderMap) {
            softReferenceInstanceHolder.resize(-i);
        }
    }

    abstract class BasePool implements Pool {
        private final Object mFinalizeGuardian;
        private IInstanceHolder mInstanceHolder;
        private final Manager mManager;
        private final int mSize;

        abstract IInstanceHolder createInstanceHolder(Class cls, int i);

        abstract void destroyInstanceHolder(IInstanceHolder iInstanceHolder, int i);

        public BasePool(Manager manager, int i) {
            Object obj = new Object() { // from class: miuix.core.util.Pools.BasePool.1
                protected void finalize() throws Throwable {
                    try {
                        BasePool.this.close();
                    } finally {
                        super.finalize();
                    }
                }
            };
            this.mFinalizeGuardian = obj;
            if (manager == null || i < 1) {
                this.mSize = obj.hashCode();
                Folme$$ExternalSyntheticBUOutline0.m("manager cannot be null and size cannot less then 1");
                throw null;
            }
            this.mManager = manager;
            this.mSize = i;
            Object objCreateInstance = manager.createInstance();
            if (objCreateInstance == null) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("manager create instance cannot return null");
                throw null;
            }
            this.mInstanceHolder = createInstanceHolder(objCreateInstance.getClass(), i);
            doRelease(objCreateInstance);
        }

        protected final Object doAcquire() {
            IInstanceHolder iInstanceHolder = this.mInstanceHolder;
            if (iInstanceHolder == null) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Cannot acquire object after close()");
                return null;
            }
            Object objCreateInstance = iInstanceHolder.get();
            if (objCreateInstance == null && (objCreateInstance = this.mManager.createInstance()) == null) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("manager create instance cannot return null");
                return null;
            }
            this.mManager.onAcquire(objCreateInstance);
            return objCreateInstance;
        }

        protected final void doRelease(Object obj) {
            if (this.mInstanceHolder == null) {
                OrtUtil$$ExternalSyntheticBUOutline0.m("Cannot release object after close()");
            } else {
                if (obj == null) {
                    return;
                }
                this.mManager.onRelease(obj);
                if (this.mInstanceHolder.put(obj)) {
                    return;
                }
                this.mManager.onDestroy(obj);
            }
        }

        @Override // miuix.core.util.Pools.Pool
        public Object acquire() {
            return doAcquire();
        }

        @Override // miuix.core.util.Pools.Pool
        public void release(Object obj) {
            doRelease(obj);
        }

        public void close() {
            IInstanceHolder iInstanceHolder = this.mInstanceHolder;
            if (iInstanceHolder != null) {
                destroyInstanceHolder(iInstanceHolder, this.mSize);
                this.mInstanceHolder = null;
            }
        }
    }

    public static SoftReferencePool createSoftReferencePool(Manager manager, int i) {
        return new SoftReferencePool(manager, i);
    }

    public class SoftReferencePool extends BasePool {
        @Override // miuix.core.util.Pools.BasePool, miuix.core.util.Pools.Pool
        public /* bridge */ /* synthetic */ Object acquire() {
            return super.acquire();
        }

        @Override // miuix.core.util.Pools.BasePool
        public /* bridge */ /* synthetic */ void close() {
            super.close();
        }

        @Override // miuix.core.util.Pools.BasePool, miuix.core.util.Pools.Pool
        public /* bridge */ /* synthetic */ void release(Object obj) {
            super.release(obj);
        }

        SoftReferencePool(Manager manager, int i) {
            super(manager, i);
        }

        @Override // miuix.core.util.Pools.BasePool
        final IInstanceHolder createInstanceHolder(Class cls, int i) {
            return Pools.onSoftReferencePoolCreate(cls, i);
        }

        @Override // miuix.core.util.Pools.BasePool
        final void destroyInstanceHolder(IInstanceHolder iInstanceHolder, int i) {
            Pools.onSoftReferencePoolClose((SoftReferenceInstanceHolder) iInstanceHolder, i);
        }
    }
}
