package com.google.common.hash;

import com.google.common.base.Preconditions;

/* JADX INFO: loaded from: classes.dex */
abstract class AbstractHashFunction implements HashFunction {
    AbstractHashFunction() {
    }

    @Override // com.google.common.hash.HashFunction
    public HashCode hashLong(long j) {
        return newHasher(8).putLong(j).hash();
    }

    public Hasher newHasher(int i) {
        Preconditions.checkArgument(i >= 0, "expectedInputSize must be >= 0 but was %s", i);
        return newHasher();
    }
}
