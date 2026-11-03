package androidx.appsearch.builtintypes;

import androidx.appsearch.app.DocumentClassFactory;
import androidx.appsearch.app.GenericDocument;

/* JADX INFO: renamed from: androidx.appsearch.builtintypes.$$__AppSearch__Account, reason: invalid class name */
/* JADX INFO: loaded from: classes.dex */
public final class C$$__AppSearch__Account implements DocumentClassFactory {
    @Override // androidx.appsearch.app.DocumentClassFactory
    public GenericDocument toGenericDocument(Account account) {
        GenericDocument.Builder builder = new GenericDocument.Builder(account.getNamespace(), account.getId(), "builtin:Account");
        String accountType = account.getAccountType();
        if (accountType != null) {
            builder.setPropertyString("accountType", accountType);
        }
        String accountName = account.getAccountName();
        if (accountName != null) {
            builder.setPropertyString("accountName", accountName);
        }
        String accountId = account.getAccountId();
        if (accountId != null) {
            builder.setPropertyString("accountId", accountId);
        }
        return builder.build();
    }
}
