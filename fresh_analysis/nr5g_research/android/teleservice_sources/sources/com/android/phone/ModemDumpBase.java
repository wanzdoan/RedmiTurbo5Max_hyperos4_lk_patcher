package com.android.phone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

/* JADX INFO: loaded from: classes.dex */
public abstract class ModemDumpBase {
    protected static volatile ModemDumpBase mInstance;
    private File mCacheFile = null;
    private BufferedWriter mDumpCacheFileWriter = null;

    public void destroy() {
    }

    public abstract void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr, boolean z);

    public abstract boolean isModemDumpFeatureSupported(int i);

    public static ModemDumpBase getInstance() {
        return mInstance;
    }

    public void initCacheFile() {
        try {
            File file = new File("/sdcard/MIUI/debug_log/common/Modem/modemdump_cache.dump");
            this.mCacheFile = file;
            File parentFile = file.getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (!this.mCacheFile.exists()) {
                this.mCacheFile.createNewFile();
            }
            this.mDumpCacheFileWriter = new BufferedWriter(new FileWriter("/sdcard/MIUI/debug_log/common/Modem/modemdump_cache.dump", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeCacheFile() {
        try {
            BufferedWriter bufferedWriter = this.mDumpCacheFileWriter;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            long jCount = Files.lines(Paths.get("/sdcard/MIUI/debug_log/common/Modem/modemdump_cache.dump", new String[0])).count();
            if (jCount > 10000) {
                deleteFirstNLines("/sdcard/MIUI/debug_log/common/Modem/modemdump_cache.dump", jCount - 10000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToCache(String str) {
        try {
            BufferedWriter bufferedWriter = this.mDumpCacheFileWriter;
            if (bufferedWriter != null) {
                bufferedWriter.write(str);
                this.mDumpCacheFileWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFirstNLines(String str, long j) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(str, "rw");
        try {
            shiftFileContent(randomAccessFile, findCutPosition(randomAccessFile, j));
            randomAccessFile.close();
            randomAccessFile.close();
        } catch (Throwable th) {
            try {
                randomAccessFile.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    public long findCutPosition(RandomAccessFile randomAccessFile, long j) throws IOException {
        long j2 = 0;
        long filePointer = 0;
        while (j2 < j && randomAccessFile.readLine() != null) {
            j2++;
            filePointer = randomAccessFile.getFilePointer();
        }
        return filePointer;
    }

    public void shiftFileContent(RandomAccessFile randomAccessFile, long j) throws IOException {
        FileChannel channel = randomAccessFile.getChannel();
        long size = channel.size() - j;
        channel.transferTo(j, size, channel.position(0L));
        channel.truncate(size);
        channel.close();
    }

    public String ConfusionMethod(String str) {
        if ("0".equals(str)) {
            return str;
        }
        int length = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        if (length >= 8) {
            sb.append(str.substring(0, 2));
            sb.append(new String(new char[length - 5]).replace("\u0000", "*"));
            sb.append(str.substring(length - 3));
        } else if (length <= 3) {
            sb.append(new String(new char[length]).replace("\u0000", "*"));
        } else {
            int i = (length - 3) / 2;
            sb.append(str.substring(0, i) + "***" + str.substring(i + 3));
        }
        return sb.toString();
    }

    public StringBuilder getAftersaleInfo() {
        return new StringBuilder();
    }
}
