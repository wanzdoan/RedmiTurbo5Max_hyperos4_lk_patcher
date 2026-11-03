package com.google.common.base;

import com.google.errorprone.annotations.DoNotMock;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use Optional.of(value) or Optional.absent()")
public abstract class Optional implements Serializable {
    private static final long serialVersionUID = 0;

    public abstract boolean isPresent();

    public abstract Object or(Object obj);

    public static Optional absent() {
        return Absent.withType();
    }

    public static Optional of(Object obj) {
        obj.getClass();
        return new Present(obj);
    }

    Optional() {
    }
}
