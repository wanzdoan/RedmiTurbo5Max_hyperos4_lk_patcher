package com.android.internal.telephony.protobuf;

/* JADX INFO: loaded from: classes.dex */
interface MutabilityOracle {
    public static final MutabilityOracle IMMUTABLE = new MutabilityOracle() { // from class: com.android.internal.telephony.protobuf.MutabilityOracle.1
        @Override // com.android.internal.telephony.protobuf.MutabilityOracle
        public void ensureMutable() {
            throw new UnsupportedOperationException();
        }
    };

    void ensureMutable();
}
