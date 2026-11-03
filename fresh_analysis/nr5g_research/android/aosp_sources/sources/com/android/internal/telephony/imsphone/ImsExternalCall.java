package com.android.internal.telephony.imsphone;

import com.android.internal.telephony.Call;
import com.android.internal.telephony.CallStateException;
import com.android.internal.telephony.Phone;

/* JADX INFO: loaded from: classes.dex */
public class ImsExternalCall extends Call {
    private Phone mPhone;

    @Override // com.android.internal.telephony.Call
    public void hangup() throws CallStateException {
    }

    @Override // com.android.internal.telephony.Call
    public void hangup(int i) throws CallStateException {
    }

    @Override // com.android.internal.telephony.Call
    public boolean isMultiparty() {
        return false;
    }

    public ImsExternalCall(Phone phone, ImsExternalConnection imsExternalConnection) {
        this.mPhone = phone;
        addConnection(imsExternalConnection);
    }

    @Override // com.android.internal.telephony.Call
    public Phone getPhone() {
        return this.mPhone;
    }

    public void setActive() {
        setState(Call.State.ACTIVE);
    }

    public void setTerminated() {
        setState(Call.State.DISCONNECTED);
    }
}
