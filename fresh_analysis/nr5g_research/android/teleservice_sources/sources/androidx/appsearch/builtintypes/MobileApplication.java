package androidx.appsearch.builtintypes;

import android.net.Uri;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class MobileApplication extends Thing {
    @Override // androidx.appsearch.builtintypes.Thing
    public abstract List getAlternateNames();

    public abstract String getClassName();

    public abstract String getDisplayName();

    public abstract Uri getIconUri();

    public abstract String getPackageName();

    public abstract byte[] getSha256Certificate();

    public abstract long getUpdatedTimestampMillis();

    class IconUriAsUri {
        IconUriAsUri() {
        }

        public String serialize(Uri uri) {
            return uri.toString();
        }
    }
}
