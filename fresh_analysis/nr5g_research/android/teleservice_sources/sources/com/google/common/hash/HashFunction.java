package com.google.common.hash;

/* JADX INFO: loaded from: classes.dex */
public interface HashFunction {
    HashCode hashLong(long j);

    Hasher newHasher();
}
