package androidx.appsearch.builtintypes;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class Person extends Thing {
    final List mAdditionalNameTypes;
    final List mAdditionalNames;
    final String mExternalUri;
    final String mImageUri;
    final boolean mIsBot;
    final boolean mIsImportant;

    public abstract List getAffiliations();

    public abstract List getContactPoints();

    public abstract String getFamilyName();

    public abstract String getGivenName();

    public abstract String getMiddleName();

    public abstract List getNotes();

    public abstract List getRelations();
}
