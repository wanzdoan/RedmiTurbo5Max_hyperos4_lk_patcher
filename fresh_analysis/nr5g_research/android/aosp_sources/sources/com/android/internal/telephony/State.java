package com.android.internal.telephony;

import android.os.Message;

/* JADX INFO: loaded from: classes.dex */
public class State implements IState {
    @Override // com.android.internal.telephony.IState
    public void enter() {
    }

    @Override // com.android.internal.telephony.IState
    public void exit() {
    }

    @Override // com.android.internal.telephony.IState
    public boolean processMessage(Message message) {
        return false;
    }

    protected State() {
    }

    @Override // com.android.internal.telephony.IState
    public String getName() {
        String name = getClass().getName();
        return name.substring(name.lastIndexOf(36) + 1);
    }
}
