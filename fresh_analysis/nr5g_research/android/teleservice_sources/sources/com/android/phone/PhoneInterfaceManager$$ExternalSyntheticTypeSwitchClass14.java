package com.android.phone;

import java.util.Objects;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public abstract /* synthetic */ class PhoneInterfaceManager$$ExternalSyntheticTypeSwitchClass14 {
    public static /* synthetic */ int switchDispatch(Object obj, int i) {
        Objects.checkIndex(i, 3);
        if (obj == null) {
            return -1;
        }
        if (i != 0) {
            if (i != 1) {
                return -2;
            }
        } else if (obj instanceof PhoneInterfaceManager.Success) {
            return 0;
        }
        return obj instanceof RuntimeException ? 1 : -2;
    }
}
