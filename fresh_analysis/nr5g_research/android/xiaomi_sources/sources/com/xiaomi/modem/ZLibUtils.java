package com.xiaomi.modem;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import vendor.xiaomi.hardware.misys.V1_0.IResultValue;

/* JADX WARN: Classes with same name are omitted, all sources:
  classes.dex
  classes.dex
 */
/* JADX INFO: loaded from: classes.dex */
public abstract class ZLibUtils {
    private static final String LOG_TAG = "ZLibUtils";
    private static boolean mTmpLogPrintFlag = false;

    public static byte[] compress(byte[] data) {
        byte[] output;
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            try {
                byte[] buf = new byte[IResultValue.MISYS_UNKNOWN];
                while (!compresser.finished()) {
                    int i = compresser.deflate(buf);
                    bos.write(buf, 0, i);
                }
                output = bos.toByteArray();
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                try {
                    bos.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
                output = null;
            }
            compresser.end();
            return output;
        } catch (Throwable th) {
            try {
                bos.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            throw th;
        }
    }

    public static void compress(byte[] data, OutputStream os) {
        DeflaterOutputStream dos = new DeflaterOutputStream(os);
        try {
            dos.write(data, 0, data.length);
            dos.finish();
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] decompress(byte[] data) {
        byte[] output;
        Inflater decompresser = new Inflater();
        decompresser.reset();
        decompresser.setInput(data);
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
        try {
            try {
                byte[] buf = new byte[IResultValue.MISYS_UNKNOWN];
                while (!decompresser.finished()) {
                    int i = decompresser.inflate(buf);
                    o.write(buf, 0, i);
                }
                output = o.toByteArray();
                try {
                    o.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    o.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                throw th;
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            try {
                o.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            output = null;
        }
        decompresser.end();
        return output;
    }

    public static byte[] decompress(InputStream is) {
        InflaterInputStream iis = new InflaterInputStream(is);
        ByteArrayOutputStream o = new ByteArrayOutputStream(IResultValue.MISYS_UNKNOWN);
        int i = IResultValue.MISYS_UNKNOWN;
        try {
            byte[] buf = new byte[IResultValue.MISYS_UNKNOWN];
            while (true) {
                int i2 = iis.read(buf, 0, i);
                i = i2;
                if (i2 <= 0) {
                    break;
                }
                o.write(buf, 0, i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return o.toByteArray();
    }

    public static String readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer bf = new StringBuffer();
        try {
            try {
                try {
                    log("以行为单位读取文件内容，一次读一整行：");
                    reader = new BufferedReader(new FileReader(file));
                    while (true) {
                        String tempString = reader.readLine();
                        if (tempString == null) {
                            break;
                        }
                        bf.append(tempString);
                        bf.append(System.getProperty("line.separator"));
                    }
                    reader.close();
                } catch (Throwable th) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                if (reader != null) {
                }
                return bf.toString();
            }
            reader.close();
        } catch (IOException e3) {
        }
        return bf.toString();
    }

    private static void tmpLog(String msg) {
        if (mTmpLogPrintFlag) {
            log(msg);
        }
    }

    private static void log(String msg) {
        ModemUtils.logd(LOG_TAG, msg);
    }

    public static void main(String[] args) {
        log("字节压缩／解压缩测试");
        String inputStr = readFileByLines("C:\\Users\\Administrator\\Documents\\weamax_access_log.20161115");
        log("输入字符串:" + inputStr);
        byte[] input = inputStr.getBytes();
        log("输入字节长度:" + input.length);
        byte[] data = compress(input);
        log("压缩后字节长度:" + data.length);
        byte[] output = decompress(data);
        log("解压缩后字节长度:" + output.length);
    }
}
