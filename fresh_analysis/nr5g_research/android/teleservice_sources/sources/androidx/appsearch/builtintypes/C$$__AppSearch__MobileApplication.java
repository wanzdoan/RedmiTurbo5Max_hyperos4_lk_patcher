package androidx.appsearch.builtintypes;

import android.net.Uri;
import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;
import com.xiaomi.settingsdk.backup.SettingsBackupConsts;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__MobileApplication, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__MobileApplication implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(MobileApplication mobileApplication) {
        GenericDocument.Builder builder = new GenericDocument.Builder(mobileApplication.getNamespace(), mobileApplication.getId(), "builtin:MobileApplication");
        builder.setScore(mobileApplication.getDocumentScore());
        builder.setCreationTimestampMillis(mobileApplication.getCreationTimestampMillis());
        builder.setTtlMillis(mobileApplication.getDocumentTtlMillis());
        String name = mobileApplication.getName();
        if (name != null) {
            builder.setPropertyString("name", name);
        }
        List alternateNames = mobileApplication.getAlternateNames();
        if (alternateNames != null) {
            builder.setPropertyString("alternateNames", (String[]) alternateNames.toArray(new String[0]));
        }
        String description = mobileApplication.getDescription();
        if (description != null) {
            builder.setPropertyString("description", description);
        }
        String image = mobileApplication.getImage();
        if (image != null) {
            builder.setPropertyString("image", image);
        }
        String url = mobileApplication.getUrl();
        if (url != null) {
            builder.setPropertyString("url", url);
        }
        List potentialActions = mobileApplication.getPotentialActions();
        if (potentialActions != null) {
            GenericDocument[] genericDocumentArr = new GenericDocument[potentialActions.size()];
            Iterator it = potentialActions.iterator();
            int i = 0;
            while (it.hasNext()) {
                genericDocumentArr[i] = GenericDocument.fromDocumentClass((PotentialAction) it.next());
                i++;
            }
            builder.setPropertyDocument("potentialActions", genericDocumentArr);
        }
        String packageName = mobileApplication.getPackageName();
        if (packageName != null) {
            builder.setPropertyString(SettingsBackupConsts.EXTRA_PACKAGE_NAME, packageName);
        }
        String displayName = mobileApplication.getDisplayName();
        if (displayName != null) {
            builder.setPropertyString("displayName", displayName);
        }
        Uri iconUri = mobileApplication.getIconUri();
        if (iconUri != null) {
            builder.setPropertyString("iconUri", new MobileApplication.IconUriAsUri().serialize(iconUri));
        }
        byte[] sha256Certificate = mobileApplication.getSha256Certificate();
        if (sha256Certificate != null) {
            builder.setPropertyBytes("sha256Certificate", sha256Certificate);
        }
        builder.setPropertyLong("updatedTimestamp", mobileApplication.getUpdatedTimestampMillis());
        String className = mobileApplication.getClassName();
        if (className != null) {
            builder.setPropertyString("className", className);
        }
        return builder.build();
    }
}
