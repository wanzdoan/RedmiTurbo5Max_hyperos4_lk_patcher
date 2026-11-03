package com.android.phone.callcomposer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/* JADX INFO: loaded from: classes.dex */
public interface GbaCredentialsSupplier {
    CompletableFuture getCredentials(String str, Executor executor);
}
