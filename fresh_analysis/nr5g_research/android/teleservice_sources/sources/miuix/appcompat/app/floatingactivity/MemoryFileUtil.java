package miuix.appcompat.app.floatingactivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.MemoryFile;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.HashMap;
import miuix.appcompat.app.floatingactivity.multiapp.IFloatingService;

/* JADX INFO: loaded from: classes2.dex */
public abstract class MemoryFileUtil {
    /* JADX WARN: Code duplicated, block: B:21:0x0040  */
    public static ParcelFileDescriptor writeToMemory(byte[] bArr, int i) throws Throwable {
        MemoryFile memoryFile;
        MemoryFile memoryFile2 = null;
        try {
            memoryFile = new MemoryFile("", i);
            try {
                try {
                    memoryFile.writeBytes(bArr, 0, 0, i);
                    Class[] clsArr = new Class[0];
                    Method declaredMethod = MemoryFile.class.getDeclaredMethod("getFileDescriptor", null);
                    declaredMethod.setAccessible(true);
                    ParcelFileDescriptor parcelFileDescriptorDup = ParcelFileDescriptor.dup((FileDescriptor) declaredMethod.invoke(memoryFile, null));
                    memoryFile.close();
                    return parcelFileDescriptorDup;
                } catch (Exception e) {
                    e = e;
                    Log.w("MemoryFileUtil", "catch write to memory error", e);
                    if (memoryFile != null) {
                        memoryFile.close();
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                memoryFile2 = memoryFile;
                if (memoryFile2 != null) {
                    memoryFile2.close();
                }
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            memoryFile = null;
        } catch (Throwable th2) {
            th = th2;
            if (memoryFile2 != null) {
                memoryFile2.close();
            }
            throw th;
        }
    }

    public static void sendToFdServer(IFloatingService iFloatingService, byte[] bArr, int i, int i2, int i3, String str, int i4) throws Throwable {
        ParcelFileDescriptor parcelFileDescriptorWriteToMemory = writeToMemory(bArr, i);
        HashMap map = new HashMap(1);
        map.put("parcelFile", parcelFileDescriptorWriteToMemory);
        Bundle bundle = new Bundle();
        bundle.putSerializable("parcelFile", map);
        bundle.putInt("parcelFileLength", i);
        bundle.putInt("key_width", i2);
        bundle.putInt("key_height", i3);
        bundle.putInt("key_task_id", i4);
        bundle.putString("key_request_identity", str);
        if (iFloatingService != null) {
            try {
                iFloatingService.callServiceMethod(7, bundle);
            } catch (RemoteException e) {
                Log.w("MemoryFileUtil", "catch stash snapshot to service error", e);
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:46:0x0053 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Not initialized variable reg: 4, insn: 0x0031: MOVE (r3 I:??[OBJECT, ARRAY]) = (r4 I:??[OBJECT, ARRAY]), block:B:17:0x0031 */
    public static byte[] readFromMemory(HashMap map, int i) throws Throwable {
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2;
        ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) map.get("parcelFile");
        FileInputStream fileInputStream3 = null;
        if (parcelFileDescriptor != null) {
            byte[] bArr = new byte[i];
            try {
                try {
                    fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                    try {
                        fileInputStream.read(bArr);
                        try {
                            fileInputStream.close();
                        } catch (IOException e) {
                            Log.w("MemoryFileUtil", "catch close FileInputStream error", e);
                        }
                        try {
                            parcelFileDescriptor.close();
                        } catch (IOException e2) {
                            Log.w("MemoryFileUtil", "catch close fd error", e2);
                        }
                        return bArr;
                    } catch (Exception e3) {
                        e = e3;
                        Log.w("MemoryFileUtil", "catch read from memory error", e);
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e4) {
                                Log.w("MemoryFileUtil", "catch close FileInputStream error", e4);
                            }
                            try {
                                parcelFileDescriptor.close();
                            } catch (IOException e5) {
                                Log.w("MemoryFileUtil", "catch close fd error", e5);
                            }
                        }
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    fileInputStream3 = fileInputStream2;
                    if (fileInputStream3 != null) {
                        try {
                            fileInputStream3.close();
                        } catch (IOException e6) {
                            Log.w("MemoryFileUtil", "catch close FileInputStream error", e6);
                        }
                        try {
                            parcelFileDescriptor.close();
                        } catch (IOException e7) {
                            Log.w("MemoryFileUtil", "catch close fd error", e7);
                        }
                    }
                    throw th;
                }
            } catch (Exception e8) {
                e = e8;
                fileInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                if (fileInputStream3 != null) {
                    fileInputStream3.close();
                    parcelFileDescriptor.close();
                }
                throw th;
            }
        }
        return null;
    }

    public static Bitmap readBitmap(Bundle bundle) throws Throwable {
        HashMap map = (HashMap) bundle.getSerializable("parcelFile");
        int i = bundle.getInt("parcelFileLength");
        int i2 = bundle.getInt("key_width");
        int i3 = bundle.getInt("key_height");
        byte[] fromMemory = readFromMemory(map, i);
        Bitmap bitmapCreateBitmap = null;
        if (fromMemory == null) {
            Log.d("MemoryFileUtil", "getSnapShot with data is null");
            return null;
        }
        try {
            bitmapCreateBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
            bitmapCreateBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(fromMemory));
            return bitmapCreateBitmap;
        } catch (IllegalArgumentException e) {
            Log.w("MemoryFileUtil", "catch illegal argument exception", e);
            return bitmapCreateBitmap;
        } catch (OutOfMemoryError e2) {
            Log.w("MemoryFileUtil", "catch oom exception", e2);
            return bitmapCreateBitmap;
        }
    }
}
