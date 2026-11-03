package miuix.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;

/* JADX INFO: loaded from: classes2.dex */
public abstract class ExtraSettings$Secure {
    public static int getInt(ContentResolver contentResolver, String str) {
        return Settings.Secure.getInt(contentResolver, str);
    }

    public static Uri getUriFor(String str) {
        return Settings.Secure.getUriFor(str);
    }
}
