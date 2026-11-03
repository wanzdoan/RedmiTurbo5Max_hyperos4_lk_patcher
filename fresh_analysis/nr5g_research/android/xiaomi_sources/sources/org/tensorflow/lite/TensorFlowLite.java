package org.tensorflow.lite;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
public final class TensorFlowLite {
    private static final Throwable LOAD_LIBRARY_EXCEPTION;
    private static final AtomicBoolean[] haveLogged;
    private static final Logger logger = Logger.getLogger(InterpreterApi.class.getName());
    private static final String[][] TFLITE_RUNTIME_LIBNAMES = {new String[]{"tensorflowlite_jni", "tensorflowlite_jni_stable"}, new String[]{"tensorflowlite_jni_gms_client"}};
    private static volatile boolean isInit = false;

    private static native void nativeDoNothing();

    static {
        Throwable loadLibraryException = null;
        for (String[] group : TFLITE_RUNTIME_LIBNAMES) {
            for (String libName : group) {
                try {
                    System.loadLibrary(libName);
                    logger.info("Loaded native library: " + libName);
                    break;
                } catch (UnsatisfiedLinkError e) {
                    logger.info("Didn't load native library: " + libName);
                    if (loadLibraryException == null) {
                        loadLibraryException = e;
                    } else {
                        loadLibraryException.addSuppressed(e);
                    }
                }
            }
        }
        LOAD_LIBRARY_EXCEPTION = loadLibraryException;
        haveLogged = new AtomicBoolean[InterpreterApi.Options.TfLiteRuntime.values().length];
        for (int i = 0; i < InterpreterApi.Options.TfLiteRuntime.values().length; i++) {
            haveLogged[i] = new AtomicBoolean();
        }
    }

    private TensorFlowLite() {
    }

    @Deprecated
    public static String version() {
        return schemaVersion();
    }

    public static String runtimeVersion(InterpreterApi.Options.TfLiteRuntime runtime) {
        return getFactory(runtime, "org.tensorflow.lite.TensorFlowLite", "runtimeVersion").runtimeVersion();
    }

    public static String runtimeVersion() {
        return runtimeVersion(null);
    }

    public static String schemaVersion(InterpreterApi.Options.TfLiteRuntime runtime) {
        return getFactory(runtime, "org.tensorflow.lite.TensorFlowLite", "schemaVersion").schemaVersion();
    }

    public static String schemaVersion() {
        return schemaVersion(null);
    }

    public static void init() {
        if (isInit) {
            return;
        }
        try {
            nativeDoNothing();
            isInit = true;
        } catch (UnsatisfiedLinkError e) {
            Throwable exceptionToLog = LOAD_LIBRARY_EXCEPTION != null ? LOAD_LIBRARY_EXCEPTION : e;
            UnsatisfiedLinkError exceptionToThrow = new UnsatisfiedLinkError("Failed to load native TensorFlow Lite methods. Check that the correct native libraries are present, and, if using a custom native library, have been properly loaded via System.loadLibrary():\n  " + exceptionToLog);
            exceptionToThrow.initCause(e);
            throw exceptionToThrow;
        }
    }

    private static class PossiblyAvailableRuntime {
        private final Exception exception;
        private final InterpreterFactoryApi factory;

        PossiblyAvailableRuntime(String namespace, String category) {
            InterpreterFactoryApi factory = null;
            Exception exception = null;
            try {
                Class<?> clazz = Class.forName(namespace + ".InterpreterFactoryImpl");
                Constructor<?> factoryConstructor = clazz.getDeclaredConstructor(new Class[0]);
                factoryConstructor.setAccessible(true);
                factory = (InterpreterFactoryApi) factoryConstructor.newInstance(new Object[0]);
                if (factory != null) {
                    TensorFlowLite.logger.info(String.format("Found %s TF Lite runtime client in %s", category, namespace));
                } else {
                    TensorFlowLite.logger.warning(String.format("Failed to construct TF Lite runtime client from %s", namespace));
                }
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                TensorFlowLite.logger.info(String.format("Didn't find %s TF Lite runtime client in %s", category, namespace));
                exception = e;
            }
            this.exception = exception;
            this.factory = factory;
        }

        public InterpreterFactoryApi getFactory() {
            return this.factory;
        }

        public Exception getException() {
            return this.exception;
        }
    }

    private static class RuntimeFromSystem {
        static final PossiblyAvailableRuntime TFLITE = new PossiblyAvailableRuntime("com.google.android.gms.tflite", "system");

        private RuntimeFromSystem() {
        }
    }

    private static class RuntimeFromApplication {
        static final PossiblyAvailableRuntime TFLITE = new PossiblyAvailableRuntime("org.tensorflow.lite", "application");

        private RuntimeFromApplication() {
        }
    }

    static InterpreterFactoryApi getFactory(InterpreterApi.Options.TfLiteRuntime runtime) {
        return getFactory(runtime, "org.tensorflow.lite.InterpreterApi.Options", "setRuntime");
    }

    private static InterpreterFactoryApi getFactory(InterpreterApi.Options.TfLiteRuntime runtime, String className, String methodName) {
        String message;
        Exception exception = null;
        if (runtime == null) {
            runtime = InterpreterApi.Options.TfLiteRuntime.FROM_APPLICATION_ONLY;
        }
        if (runtime == InterpreterApi.Options.TfLiteRuntime.PREFER_SYSTEM_OVER_APPLICATION || runtime == InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY) {
            if (RuntimeFromSystem.TFLITE.getFactory() != null) {
                if (!haveLogged[runtime.ordinal()].getAndSet(true)) {
                    logger.info(String.format("TfLiteRuntime.%s: Using system TF Lite runtime client from com.google.android.gms", runtime.name()));
                }
                return RuntimeFromSystem.TFLITE.getFactory();
            }
            exception = RuntimeFromSystem.TFLITE.getException();
        }
        if (runtime == InterpreterApi.Options.TfLiteRuntime.PREFER_SYSTEM_OVER_APPLICATION || runtime == InterpreterApi.Options.TfLiteRuntime.FROM_APPLICATION_ONLY) {
            if (RuntimeFromApplication.TFLITE.getFactory() != null) {
                if (!haveLogged[runtime.ordinal()].getAndSet(true)) {
                    logger.info(String.format("TfLiteRuntime.%s: Using application TF Lite runtime client from org.tensorflow.lite", runtime.name()));
                }
                return RuntimeFromApplication.TFLITE.getFactory();
            }
            if (exception == null) {
                exception = RuntimeFromApplication.TFLITE.getException();
            } else if (exception.getSuppressed().length == 0) {
                exception.addSuppressed(RuntimeFromApplication.TFLITE.getException());
            }
        }
        switch (runtime) {
            case FROM_APPLICATION_ONLY:
                message = String.format("You should declare a build dependency on org.tensorflow.lite:tensorflow-lite, or call .%s with a value other than TfLiteRuntime.FROM_APPLICATION_ONLY (see docs for %s#%s(TfLiteRuntime)).", methodName, className, methodName);
                break;
            case FROM_SYSTEM_ONLY:
                message = String.format("You should declare a build dependency on com.google.android.gms:play-services-tflite-java, or call .%s with a value other than TfLiteRuntime.FROM_SYSTEM_ONLY  (see docs for %s#%s).", methodName, className, methodName);
                break;
            default:
                message = "You should declare a build dependency on org.tensorflow.lite:tensorflow-lite or com.google.android.gms:play-services-tflite-java";
                break;
        }
        throw new IllegalStateException("Couldn't find TensorFlow Lite runtime's InterpreterFactoryImpl class -- make sure your app links in the right TensorFlow Lite runtime. " + message, exception);
    }
}
