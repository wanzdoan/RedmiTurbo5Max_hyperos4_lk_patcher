package ai.onnxruntime;

import com.ot.pubsub.a.a;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/* JADX INFO: loaded from: classes.dex */
final class OnnxRuntime {
    static final String ONNXRUNTIME_JNI_LIBRARY_NAME = "onnxruntime4j_jni";
    static final String ONNXRUNTIME_LIBRARY_CUDA_NAME = "onnxruntime_providers_cuda";
    static final String ONNXRUNTIME_LIBRARY_DNNL_NAME = "onnxruntime_providers_dnnl";
    static final String ONNXRUNTIME_LIBRARY_NAME = "onnxruntime";
    static final String ONNXRUNTIME_LIBRARY_OPENVINO_NAME = "onnxruntime_providers_openvino";
    static final String ONNXRUNTIME_LIBRARY_ROCM_NAME = "onnxruntime_providers_rocm";
    static final String ONNXRUNTIME_LIBRARY_SHARED_NAME = "onnxruntime_providers_shared";
    static final String ONNXRUNTIME_LIBRARY_TENSORRT_NAME = "onnxruntime_providers_tensorrt";
    static final String ONNXRUNTIME_NATIVE_PATH = "onnxruntime.native.path";
    private static final int ORT_API_VERSION_1 = 1;
    private static final int ORT_API_VERSION_11 = 11;
    private static final int ORT_API_VERSION_13 = 13;
    private static final int ORT_API_VERSION_14 = 14;
    private static final int ORT_API_VERSION_2 = 2;
    private static final int ORT_API_VERSION_3 = 3;
    private static final int ORT_API_VERSION_7 = 7;
    private static final int ORT_API_VERSION_8 = 8;
    private static final int ORT_TRAINING_API_VERSION_1 = 1;
    private static String libraryDirPathProperty;
    static long ortApiHandle;
    static long ortTrainingApiHandle;
    static EnumSet providers;
    private static Path tempDirectory;
    static boolean trainingEnabled;
    private static String version;
    private static final Logger logger = Logger.getLogger(OnnxRuntime.class.getName());
    private static final String OS_ARCH_STR = initOsArch();
    private static boolean loaded = false;
    private static final Set extractedSharedProviders = new HashSet();

    private static native String[] getAvailableProviders(long j);

    private static native long initialiseAPIBase(int i);

    private static native long initialiseTrainingAPIBase(long j, int i);

    private static native String initialiseVersion();

    private OnnxRuntime() {
    }

    /* JADX WARN: Code duplicated, block: B:26:0x0074 A[PHI: r1
  0x0074: PHI (r1v11 java.lang.String) = (r1v6 java.lang.String), (r1v7 java.lang.String), (r1v8 java.lang.String) binds: [B:25:0x0072, B:28:0x007c, B:31:0x0086] A[DONT_GENERATE, DONT_INLINE]] */
    private static String initOsArch() {
        String str;
        String property = System.getProperty("os.name", "generic");
        Locale locale = Locale.ENGLISH;
        String lowerCase = property.toLowerCase(locale);
        if (lowerCase.contains(a.B) || lowerCase.contains("darwin")) {
            str = "osx";
        } else {
            str = "win";
            if (!lowerCase.contains("win")) {
                if (lowerCase.contains("nux")) {
                    str = "linux";
                } else if (isAndroid()) {
                    str = "android";
                } else {
                    OrtUtil$$ExternalSyntheticBUOutline0.m("Unsupported os:".concat(lowerCase));
                    return null;
                }
            }
        }
        String lowerCase2 = System.getProperty("os.arch", "generic").toLowerCase(locale);
        if (lowerCase2.startsWith("amd64") || lowerCase2.startsWith("x86_64")) {
            lowerCase2 = "x64";
        } else {
            String str2 = "x86";
            if (lowerCase2.startsWith("x86")) {
                lowerCase2 = str2;
            } else {
                str2 = "aarch64";
                if (lowerCase2.startsWith("aarch64")) {
                    lowerCase2 = str2;
                } else {
                    str2 = "ppc64";
                    if (lowerCase2.startsWith("ppc64")) {
                        lowerCase2 = str2;
                    } else if (!isAndroid()) {
                        OrtUtil$$ExternalSyntheticBUOutline0.m("Unsupported arch:".concat(lowerCase2));
                        return null;
                    }
                }
            }
        }
        return str + '-' + lowerCase2;
    }

    static synchronized void init() throws IOException {
        if (loaded) {
            return;
        }
        tempDirectory = isAndroid() ? null : Files.createTempDirectory("onnxruntime-java", new FileAttribute[0]);
        try {
            libraryDirPathProperty = System.getProperty(ONNXRUNTIME_NATIVE_PATH);
            extractProviderLibrary(ONNXRUNTIME_LIBRARY_SHARED_NAME);
            load(ONNXRUNTIME_LIBRARY_NAME);
            load(ONNXRUNTIME_JNI_LIBRARY_NAME);
            long jInitialiseAPIBase = initialiseAPIBase(14);
            ortApiHandle = jInitialiseAPIBase;
            if (jInitialiseAPIBase == 0) {
                throw new IllegalStateException("There is a mismatch between the ORT class files and the ORT native library, and the native library could not be loaded");
            }
            long jInitialiseTrainingAPIBase = initialiseTrainingAPIBase(jInitialiseAPIBase, 14);
            ortTrainingApiHandle = jInitialiseTrainingAPIBase;
            trainingEnabled = jInitialiseTrainingAPIBase != 0;
            providers = initialiseProviders(ortApiHandle);
            version = initialiseVersion();
            loaded = true;
            Path path = tempDirectory;
            if (path != null) {
                cleanUp(path.toFile());
            }
        } catch (Throwable th) {
            Path path2 = tempDirectory;
            if (path2 != null) {
                cleanUp(path2.toFile());
            }
            throw th;
        }
    }

    private static void cleanUp(File file) {
        if (file.exists()) {
            logger.log(Level.FINE, "Deleting " + file + " on exit");
            file.deleteOnExit();
        }
    }

    static String version() {
        return version;
    }

    static boolean extractCUDA() {
        return extractProviderLibrary(ONNXRUNTIME_LIBRARY_CUDA_NAME);
    }

    static boolean extractROCM() {
        return extractProviderLibrary(ONNXRUNTIME_LIBRARY_ROCM_NAME);
    }

    static boolean extractDNNL() {
        return extractProviderLibrary(ONNXRUNTIME_LIBRARY_DNNL_NAME);
    }

    static boolean extractOpenVINO() {
        return extractProviderLibrary(ONNXRUNTIME_LIBRARY_OPENVINO_NAME);
    }

    static boolean extractTensorRT() {
        return extractProviderLibrary(ONNXRUNTIME_LIBRARY_TENSORRT_NAME);
    }

    static synchronized boolean extractProviderLibrary(String str) {
        if (isAndroid()) {
            return false;
        }
        Set set = extractedSharedProviders;
        if (set.contains(str)) {
            return true;
        }
        if (extractFromResources(str).isPresent()) {
            set.add(str);
            return true;
        }
        if (libraryDirPathProperty == null) {
            return false;
        }
        if (!Paths.get(libraryDirPathProperty, mapLibraryName(str)).toFile().exists()) {
            return false;
        }
        set.add(str);
        return true;
    }

    static boolean isAndroid() {
        return System.getProperty("java.vendor", "generic").equals("The Android Project");
    }

    private static void load(String str) throws IOException {
        if (isAndroid()) {
            System.loadLibrary(ONNXRUNTIME_JNI_LIBRARY_NAME);
            return;
        }
        if (Boolean.TRUE.toString().equalsIgnoreCase(System.getProperty("onnxruntime.native." + str + ".skip"))) {
            logger.log(Level.FINE, "Skipping load of native library '" + str + "'");
            return;
        }
        String strMapLibraryName = mapLibraryName(str);
        if (libraryDirPathProperty != null) {
            Logger logger2 = logger;
            Level level = Level.FINE;
            logger2.log(level, "Attempting to load native library '" + str + "' from specified path: " + libraryDirPathProperty);
            File file = Paths.get(libraryDirPathProperty, strMapLibraryName).toFile();
            String absolutePath = file.getAbsolutePath();
            if (!file.exists()) {
                throw new IOException("Native library '" + str + "' not found at " + absolutePath);
            }
            System.load(absolutePath);
            logger2.log(level, "Loaded native library '" + str + "' from specified path");
            return;
        }
        String property = System.getProperty("onnxruntime.native." + str + ".path");
        if (property != null) {
            Logger logger3 = logger;
            Level level2 = Level.FINE;
            logger3.log(level2, "Attempting to load native library '" + str + "' from specified path: " + property);
            File file2 = new File(property);
            String absolutePath2 = file2.getAbsolutePath();
            if (!file2.exists()) {
                throw new IOException("Native library '" + str + "' not found at " + absolutePath2);
            }
            System.load(absolutePath2);
            logger3.log(level2, "Loaded native library '" + str + "' from specified path");
            return;
        }
        Optional optionalExtractFromResources = extractFromResources(str);
        if (optionalExtractFromResources.isPresent()) {
            System.load(((File) optionalExtractFromResources.get()).getAbsolutePath());
            logger.log(Level.FINE, "Loaded native library '" + str + "' from resource path");
            return;
        }
        Logger logger4 = logger;
        Level level3 = Level.FINE;
        logger4.log(level3, "Attempting to load native library '" + str + "' from library path");
        System.loadLibrary(str);
        logger4.log(level3, "Loaded native library '" + str + "' from library path");
    }

    /* JADX WARN: Code duplicated, block: B:45:0x00b6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    private static Optional extractFromResources(String str) {
        String strMapLibraryName = mapLibraryName(str);
        String str2 = "/ai/onnxruntime/native/" + OS_ARCH_STR + '/' + strMapLibraryName;
        File file = tempDirectory.resolve(strMapLibraryName).toFile();
        try {
            try {
                InputStream resourceAsStream = OnnxRuntime.class.getResourceAsStream(str2);
                try {
                    if (resourceAsStream == null) {
                        Optional optionalEmpty = Optional.empty();
                        if (resourceAsStream != null) {
                            resourceAsStream.close();
                        }
                        cleanUp(file);
                        return optionalEmpty;
                    }
                    logger.log(Level.FINE, "Attempting to load native library '" + str + "' from resource path " + str2 + " copying to " + file);
                    byte[] bArr = new byte[4096];
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while (true) {
                        try {
                            int i = resourceAsStream.read(bArr);
                            if (i != -1) {
                                fileOutputStream.write(bArr, 0, i);
                            } else {
                                fileOutputStream.close();
                                logger.log(Level.FINE, "Extracted native library '" + str + "' from resource path");
                                Optional optionalOf = Optional.of(file);
                                resourceAsStream.close();
                                cleanUp(file);
                                return optionalOf;
                            }
                        } catch (Throwable th) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                            throw th;
                        }
                        if (resourceAsStream != null) {
                            try {
                                resourceAsStream.close();
                            } catch (Throwable th3) {
                                th.addSuppressed(th3);
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th4) {
                    if (resourceAsStream != null) {
                        resourceAsStream.close();
                    }
                    throw th4;
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Failed to extract library '" + str + "' from the resources", (Throwable) e);
                Optional optionalEmpty2 = Optional.empty();
                cleanUp(file);
                return optionalEmpty2;
            }
        } catch (Throwable th5) {
            cleanUp(file);
            throw th5;
        }
    }

    private static String mapLibraryName(String str) {
        return System.mapLibraryName(str).replace("jnilib", "dylib");
    }

    private static EnumSet initialiseProviders(long j) {
        String[] availableProviders = getAvailableProviders(j);
        EnumSet enumSetNoneOf = EnumSet.noneOf(OrtProvider.class);
        for (String str : availableProviders) {
            enumSetNoneOf.add(OrtProvider.mapFromName(str));
        }
        return enumSetNoneOf;
    }
}
