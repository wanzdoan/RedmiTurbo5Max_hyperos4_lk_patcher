package androidx.appsearch.app;

import androidx.appsearch.exceptions.AppSearchException;
import androidx.core.util.Preconditions;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class DocumentClassFactoryRegistry {
    private static volatile DocumentClassFactoryRegistry sInstance;
    private final Map mFactories = new HashMap();

    private DocumentClassFactoryRegistry() {
    }

    public static DocumentClassFactoryRegistry getInstance() {
        if (sInstance == null) {
            synchronized (DocumentClassFactoryRegistry.class) {
                try {
                    if (sInstance == null) {
                        sInstance = new DocumentClassFactoryRegistry();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
        return sInstance;
    }

    public DocumentClassFactory getOrCreateFactory(Class cls) throws AppSearchException {
        DocumentClassFactory documentClassFactory;
        Preconditions.checkNotNull(cls);
        synchronized (this) {
            documentClassFactory = (DocumentClassFactory) this.mFactories.get(cls);
        }
        if (documentClassFactory != null) {
            return documentClassFactory;
        }
        DocumentClassFactory documentClassFactoryLoadFactoryByReflection = loadFactoryByReflection(cls);
        synchronized (this) {
            try {
                DocumentClassFactory documentClassFactory2 = (DocumentClassFactory) this.mFactories.get(cls);
                if (documentClassFactory2 == null) {
                    this.mFactories.put(cls, documentClassFactoryLoadFactoryByReflection);
                } else {
                    documentClassFactoryLoadFactoryByReflection = documentClassFactory2;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return documentClassFactoryLoadFactoryByReflection;
    }

    public DocumentClassFactory getOrCreateFactory(Object obj) {
        Preconditions.checkNotNull(obj);
        return getOrCreateFactory((Class) obj.getClass());
    }

    private DocumentClassFactory loadFactoryByReflection(Class cls) throws AppSearchException {
        String str;
        Package r0 = cls.getPackage();
        String canonicalName = cls.getCanonicalName();
        if (canonicalName == null) {
            throw new AppSearchException(2, "Failed to find simple name for document class \"" + cls + "\". Perhaps it is anonymous?");
        }
        if (r0 == null) {
            str = "";
        } else {
            str = r0.getName() + ".";
            canonicalName = canonicalName.substring(str.length()).replace(".", "$$__");
        }
        String str2 = str + "$$__AppSearch__" + canonicalName;
        try {
            try {
                Class[] clsArr = new Class[0];
                return (DocumentClassFactory) Class.forName(str2).getDeclaredConstructor(null).newInstance(null);
            } catch (Exception e) {
                throw new AppSearchException(2, "Failed to construct document class converter \"" + str2 + "\"", e);
            }
        } catch (ClassNotFoundException e2) {
            Class superclass = cls.getSuperclass();
            Class<?>[] interfaces = cls.getInterfaces();
            Class cls2 = superclass != Object.class ? superclass : null;
            int length = interfaces.length;
            if (cls2 != null) {
                length++;
            }
            if (length == 1) {
                if (cls2 != null) {
                    return loadFactoryByReflection(cls2);
                }
                return loadFactoryByReflection(interfaces[0]);
            }
            String strConcat = "Failed to find document class converter \"" + str2 + "\". Perhaps the annotation processor was not run or the class was proguarded out?";
            if (length > 1) {
                strConcat = strConcat.concat(" Or, this class may not have been annotated with @Document, and there is an ambiguity to determine a unique @Document annotated parent class/interface.");
            }
            throw new AppSearchException(2, strConcat, e2);
        }
    }
}
