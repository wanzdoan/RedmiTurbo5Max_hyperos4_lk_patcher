package org.tensorflow.lite.support.common;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class FileUtil {
    private FileUtil() {
    }

    public static List<String> loadLabels(Context context, String filePath) throws IOException {
        return loadLabels(context, filePath, Charset.defaultCharset());
    }

    public static List<String> loadLabels(Context context, String filePath, Charset cs) throws IOException {
        SupportPreconditions.checkNotNull(context, "Context cannot be null.");
        SupportPreconditions.checkNotNull(filePath, "File path cannot be null.");
        InputStream inputStream = context.getAssets().open(filePath);
        try {
            List<String> listLoadLabels = loadLabels(inputStream, cs);
            if (inputStream != null) {
                inputStream.close();
            }
            return listLoadLabels;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static List<String> loadLabels(InputStream inputStream) throws IOException {
        return loadLabels(inputStream, Charset.defaultCharset());
    }

    public static List<String> loadLabels(InputStream inputStream, Charset cs) throws IOException {
        List<String> labels = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, cs));
        while (true) {
            try {
                String line = reader.readLine();
                if (line != null) {
                    if (line.trim().length() > 0) {
                        labels.add(line);
                    }
                } else {
                    reader.close();
                    return labels;
                }
            } catch (Throwable th) {
                try {
                    reader.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }
    }

    public static List<String> loadSingleColumnTextFile(Context context, String filePath, Charset cs) throws IOException {
        return loadLabels(context, filePath, cs);
    }

    public static List<String> loadSingleColumnTextFile(InputStream inputStream, Charset cs) throws IOException {
        return loadLabels(inputStream, cs);
    }

    public static MappedByteBuffer loadMappedFile(Context context, String filePath) throws IOException {
        SupportPreconditions.checkNotNull(context, "Context should not be null.");
        SupportPreconditions.checkNotNull(filePath, "File path cannot be null.");
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(filePath);
        try {
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            try {
                FileChannel fileChannel = inputStream.getChannel();
                long startOffset = fileDescriptor.getStartOffset();
                long declaredLength = fileDescriptor.getDeclaredLength();
                MappedByteBuffer map = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
                inputStream.close();
                if (fileDescriptor != null) {
                    fileDescriptor.close();
                }
                return map;
            } catch (Throwable th) {
                try {
                    inputStream.close();
                    throw th;
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                    throw th;
                }
            }
        } catch (Throwable th3) {
            if (fileDescriptor == null) {
                throw th3;
            }
            try {
                fileDescriptor.close();
                throw th3;
            } catch (Throwable th4) {
                th3.addSuppressed(th4);
                throw th3;
            }
        }
    }

    public static byte[] loadByteFromFile(Context context, String filePath) throws IOException {
        ByteBuffer buffer = loadMappedFile(context, filePath);
        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        return byteArray;
    }
}
