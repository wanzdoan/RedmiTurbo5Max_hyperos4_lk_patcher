package com.google.common.base;

/* JADX INFO: loaded from: classes.dex */
final class Absent extends Optional {
    static final Absent INSTANCE = new Absent();
    private static final long serialVersionUID = 0;

    public boolean equals(Object obj) {
        return obj == this;
    }

    public int hashCode() {
        return 2040732332;
    }

    @Override // com.google.common.base.Optional
    public boolean isPresent() {
        return false;
    }

    static Optional withType() {
        return INSTANCE;
    }

    private Absent() {
    }

    @Override // com.google.common.base.Optional
    public Object or(Object obj) {
        obj.getClass();
        return obj;
    }

    public String toString() {
        return "Optional.absent()";
    }

    private Object readResolve() {
        return INSTANCE;
    }
}
