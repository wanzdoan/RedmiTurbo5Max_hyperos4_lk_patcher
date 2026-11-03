package com.google.common.base;

/* JADX INFO: loaded from: classes.dex */
class Suppliers$NonSerializableMemoizingSupplier implements Supplier {
    private static final Supplier SUCCESSFULLY_COMPUTED = new Supplier() { // from class: com.google.common.base.Suppliers$NonSerializableMemoizingSupplier$$ExternalSyntheticLambda0
    };
    private volatile Supplier delegate;
    private Object value;

    public String toString() {
        Object obj = this.delegate;
        StringBuilder sb = new StringBuilder("Suppliers.memoize(");
        if (obj == SUCCESSFULLY_COMPUTED) {
            obj = "<supplier that returned " + this.value + ">";
        }
        sb.append(obj);
        sb.append(")");
        return sb.toString();
    }
}
