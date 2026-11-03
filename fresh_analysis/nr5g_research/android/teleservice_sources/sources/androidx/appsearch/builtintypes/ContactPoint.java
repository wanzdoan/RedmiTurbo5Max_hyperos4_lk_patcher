package androidx.appsearch.builtintypes;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class ContactPoint extends Thing {
    public abstract List getAddresses();

    public abstract List getEmails();

    public abstract String getLabel();

    public abstract List getTelephones();
}
