package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;

/* JADX INFO: loaded from: classes.dex */
public class AiModelLoader {
    private static final String LOG_TAG = "AiModelLoader";
    private static Context mContext;
    private static final Map<String, String> modelPathMap = new HashMap();
    private Interpreter mTFLite;

    static {
        modelPathMap.put("signal_model", "/system_ext/etc/modem/signal.tflite");
        modelPathMap.put("game_model", "/system_ext/etc/modem/game_2.0_0825.tflite");
    }

    public AiModelLoader(String name, Context cnt) {
        log("on load ai model, model=" + name);
        try {
            mContext = cnt;
            Interpreter interpreter = this.mTFLite;
            Objects.isNull(interpreter);
            if (interpreter == null) {
                this.mTFLite = new Interpreter(loadModelFile(name));
            }
        } catch (Exception e) {
            StringBuilder stackTraceBuilder = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTraceBuilder.append(element.toString()).append("\n");
            }
            String stackTraceString = stackTraceBuilder.toString();
            log("Error:" + e.getMessage() + "\n" + stackTraceString);
        }
    }

    private MappedByteBuffer loadModelFile(String model) throws Throwable {
        log("on loading Model File");
        FileInputStream inputStream = null;
        FileChannel fileChannel = null;
        long declaredLength = 0;
        try {
            try {
                File tfLiteFile = new File(modelPathMap.get(model));
                inputStream = new FileInputStream(tfLiteFile);
                FileChannel fileChannel2 = inputStream.getChannel();
                try {
                    long declaredLength2 = fileChannel2.size();
                    try {
                        MappedByteBuffer mappedBuffer = fileChannel2.map(FileChannel.MapMode.READ_ONLY, 0L, declaredLength2);
                        inputStream.close();
                        return mappedBuffer;
                    } catch (Exception e) {
                        e = e;
                        fileChannel = fileChannel2;
                        declaredLength = declaredLength2;
                        log("loadModelFile error");
                        StringBuilder stackTraceBuilder = new StringBuilder();
                        for (StackTraceElement element : e.getStackTrace()) {
                            stackTraceBuilder.append(element.toString()).append("\n");
                        }
                        String stackTraceString = stackTraceBuilder.toString();
                        log("Error:" + e.getMessage() + "\n" + stackTraceString);
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        return null;
                    } catch (Throwable th) {
                        th = th;
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        throw th;
                    }
                } catch (Exception e2) {
                    e = e2;
                    fileChannel = fileChannel2;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
            }
        } catch (Exception e3) {
            e = e3;
        }
    }

    public void run(Object input, Object output) {
        log("Run Model");
        try {
            this.mTFLite.run(input, output);
        } catch (Exception e) {
            log("run model error");
            StringBuilder stackTraceBuilder = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTraceBuilder.append(element.toString()).append("\n");
            }
            String stackTraceString = stackTraceBuilder.toString();
            log("Error:" + e.getMessage() + "\n" + stackTraceString);
        }
    }

    public Tensor getInputTensor(int index) {
        log("Run getInputTensor");
        try {
            Tensor input = this.mTFLite.getInputTensor(index);
            return input;
        } catch (Exception e) {
            log("run getInputTensor error");
            StringBuilder stackTraceBuilder = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTraceBuilder.append(element.toString()).append("\n");
            }
            String stackTraceString = stackTraceBuilder.toString();
            log("Error:" + e.getMessage() + "\n" + stackTraceString);
            return null;
        }
    }

    public Tensor getOutputTensor(int index) {
        log("Run getOutputTensor");
        try {
            Tensor output = this.mTFLite.getOutputTensor(index);
            return output;
        } catch (Exception e) {
            log("run getOutputTensor error");
            StringBuilder stackTraceBuilder = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTraceBuilder.append(element.toString()).append("\n");
            }
            String stackTraceString = stackTraceBuilder.toString();
            log("Error:" + e.getMessage() + "\n" + stackTraceString);
            return null;
        }
    }

    private static final void log(String msg) {
        ModemAiLog.getInstance(mContext).log(LOG_TAG, msg);
    }
}
