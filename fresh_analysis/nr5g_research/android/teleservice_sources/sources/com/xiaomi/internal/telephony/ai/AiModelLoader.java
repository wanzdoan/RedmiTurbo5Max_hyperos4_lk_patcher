package com.xiaomi.internal.telephony.ai;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;

/* JADX INFO: loaded from: classes.dex */
public class AiModelLoader {
    private static final String LOG_TAG = "AiModelLoader";
    private static Context mContext;
    private static final Map modelPathMap;
    private Interpreter mTFLite;

    static {
        HashMap map = new HashMap();
        modelPathMap = map;
        map.put("signal_model", "/system_ext/etc/modem/signal.tflite");
        map.put("game_model", "/system_ext/etc/modem/game_2.0_0825.tflite");
    }

    public AiModelLoader(String str, Context context) {
        log("on load ai model, model=" + str);
        try {
            mContext = context;
            if (this.mTFLite == null) {
                this.mTFLite = new Interpreter(loadModelFile(str));
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
            log("Error:" + e.getMessage() + "\n" + sb.toString());
        }
    }

    /* JADX WARN: Code duplicated, block: B:25:0x0087  */
    /* JADX WARN: Code duplicated, block: B:33:? A[SYNTHETIC] */
    private MappedByteBuffer loadModelFile(String str) throws Throwable {
        FileInputStream fileInputStream;
        Throwable th;
        log("on loading Model File");
        try {
            fileInputStream = new FileInputStream(new File((String) modelPathMap.get(str)));
            try {
                try {
                    FileChannel channel = fileInputStream.getChannel();
                    MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
                    fileInputStream.close();
                    return map;
                } catch (Exception e) {
                    e = e;
                    log("loadModelFile error");
                    StringBuilder sb = new StringBuilder();
                    for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                        sb.append(stackTraceElement.toString());
                        sb.append("\n");
                    }
                    log("Error:" + e.getMessage() + "\n" + sb.toString());
                    if (fileInputStream != null) {
                        fileInputStream.close();
                    }
                    return null;
                }
            } catch (Throwable th2) {
                th = th2;
                if (fileInputStream != null) {
                    fileInputStream.close();
                    throw th;
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            fileInputStream = null;
        } catch (Throwable th3) {
            fileInputStream = null;
            th = th3;
            if (fileInputStream != null) {
                fileInputStream.close();
                throw th;
            }
            throw th;
        }
    }

    public void run(Object obj, Object obj2) {
        log("Run Model");
        try {
            this.mTFLite.run(obj, obj2);
        } catch (Exception e) {
            log("run model error");
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
            log("Error:" + e.getMessage() + "\n" + sb.toString());
        }
    }

    public Tensor getInputTensor(int i) {
        log("Run getInputTensor");
        try {
            return this.mTFLite.getInputTensor(i);
        } catch (Exception e) {
            log("run getInputTensor error");
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
            log("Error:" + e.getMessage() + "\n" + sb.toString());
            return null;
        }
    }

    public Tensor getOutputTensor(int i) {
        log("Run getOutputTensor");
        try {
            return this.mTFLite.getOutputTensor(i);
        } catch (Exception e) {
            log("run getOutputTensor error");
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                sb.append(stackTraceElement.toString());
                sb.append("\n");
            }
            log("Error:" + e.getMessage() + "\n" + sb.toString());
            return null;
        }
    }

    private static final void log(String str) {
        ModemAiLog.getInstance(mContext).log(LOG_TAG, str);
    }
}
