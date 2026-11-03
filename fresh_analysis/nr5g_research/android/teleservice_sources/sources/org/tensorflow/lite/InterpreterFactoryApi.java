package org.tensorflow.lite;

import org.tensorflow.lite.nnapi.NnApiDelegate;

/* JADX INFO: loaded from: classes2.dex */
public interface InterpreterFactoryApi {
    NnApiDelegate.PrivateInterface createNnApiDelegateImpl(NnApiDelegate.Options options);
}
