package com.android.internal.telephony.protobuf;

/* JADX INFO: loaded from: classes.dex */
final class ExtensionRegistryFactory {
    static final Class EXTENSION_REGISTRY_CLASS = reflectExtensionRegistry();

    static Class reflectExtensionRegistry() {
        try {
            return Class.forName("com.android.internal.telephony.protobuf.ExtensionRegistry");
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    public static ExtensionRegistryLite create() {
        ExtensionRegistryLite extensionRegistryLiteInvokeSubclassFactory = invokeSubclassFactory("newInstance");
        return extensionRegistryLiteInvokeSubclassFactory != null ? extensionRegistryLiteInvokeSubclassFactory : new ExtensionRegistryLite();
    }

    public static ExtensionRegistryLite createEmpty() {
        ExtensionRegistryLite extensionRegistryLiteInvokeSubclassFactory = invokeSubclassFactory("getEmptyRegistry");
        return extensionRegistryLiteInvokeSubclassFactory != null ? extensionRegistryLiteInvokeSubclassFactory : ExtensionRegistryLite.EMPTY_REGISTRY_LITE;
    }

    static boolean isFullRegistry(ExtensionRegistryLite extensionRegistryLite) {
        Class cls;
        return (Android.assumeLiteRuntime || (cls = EXTENSION_REGISTRY_CLASS) == null || !cls.isAssignableFrom(extensionRegistryLite.getClass())) ? false : true;
    }

    private static final ExtensionRegistryLite invokeSubclassFactory(String str) {
        Class cls = EXTENSION_REGISTRY_CLASS;
        if (cls == null) {
            return null;
        }
        try {
            Class[] clsArr = new Class[0];
            return (ExtensionRegistryLite) cls.getDeclaredMethod(str, null).invoke(null, null);
        } catch (Exception unused) {
            return null;
        }
    }
}
