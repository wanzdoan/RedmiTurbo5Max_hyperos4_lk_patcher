package com.miui.miwallpaper;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

/* JADX INFO: loaded from: classes.dex */
public abstract class MiuiWallpaperFileUtils {
    public static Bitmap readFromFile(ParcelFileDescriptor parcelFileDescriptor) {
        if (parcelFileDescriptor == null) {
            return null;
        }
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new ParcelFileDescriptor.AutoCloseInputStream(parcelFileDescriptor));
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int i = bufferedInputStream.read();
                    if (i != -1) {
                        byteArrayOutputStream.write(i);
                    } else {
                        Bitmap bitmapDecodeBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(byteArrayOutputStream.toByteArray()), new ImageDecoder.OnHeaderDecodedListener() { // from class: com.miui.miwallpaper.MiuiWallpaperFileUtils$$ExternalSyntheticLambda0
                            @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                            public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                                imageDecoder.setMutableRequired(true);
                            }
                        });
                        bufferedInputStream.close();
                        return bitmapDecodeBitmap;
                    }
                    Log.w("MiuiWallpaperFileUtils", "Can't decode file", e);
                    return null;
                }
            } catch (Throwable th) {
                try {
                    bufferedInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        } catch (Exception e) {
            Log.w("MiuiWallpaperFileUtils", "Can't decode file", e);
            return null;
        }
    }
}
