package com.android.phone;

import com.android.internal.telephony.CommandException;
import java.util.Objects;

/* JADX INFO: compiled from: R8$$SyntheticClass */
/* JADX INFO: loaded from: classes.dex */
public abstract /* synthetic */ class PhoneInterfaceManager$MainThreadHandler$$ExternalSyntheticTypeSwitchClass1 {
    public static /* synthetic */ int switchDispatch(Object obj, int i) {
        Objects.checkIndex(i, 2);
        if (obj == null) {
            return -1;
        }
        return (i == 0 && (obj instanceof CommandException)) ? 0 : -2;
    }
}
