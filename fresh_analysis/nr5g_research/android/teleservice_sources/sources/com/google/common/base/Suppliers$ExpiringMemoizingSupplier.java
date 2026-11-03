package com.google.common.base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
class Suppliers$ExpiringMemoizingSupplier implements Supplier, Serializable {
    private static final long serialVersionUID = 0;
    final Supplier delegate;
    final long durationNanos;
    private transient Object lock;

    public String toString() {
        return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        this.lock = new Object();
    }
}
