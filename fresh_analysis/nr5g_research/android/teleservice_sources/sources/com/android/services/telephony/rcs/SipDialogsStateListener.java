package com.android.services.telephony.rcs;

import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface SipDialogsStateListener {
    void notifySipDialogState();

    void reMappingSipDelegateState(String str, List list);
}
