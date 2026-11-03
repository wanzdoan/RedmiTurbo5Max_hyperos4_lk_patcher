package org.tensorflow.lite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public interface InterpreterApi extends AutoCloseable {

    public abstract class Options {
        Boolean allowCancellation;
        Boolean useNNAPI;
        TfLiteRuntime runtime = TfLiteRuntime.FROM_APPLICATION_ONLY;
        int numThreads = -1;
        final List delegates = new ArrayList();
        private final List delegateFactories = new ArrayList();

        public enum TfLiteRuntime {
            FROM_APPLICATION_ONLY,
            FROM_SYSTEM_ONLY,
            PREFER_SYSTEM_OVER_APPLICATION
        }

        public int getNumThreads() {
            return this.numThreads;
        }

        public boolean getUseNNAPI() {
            Boolean bool = this.useNNAPI;
            return bool != null && bool.booleanValue();
        }

        public boolean isCancellable() {
            Boolean bool = this.allowCancellation;
            return bool != null && bool.booleanValue();
        }

        public List getDelegates() {
            return Collections.unmodifiableList(this.delegates);
        }

        public List getDelegateFactories() {
            return Collections.unmodifiableList(this.delegateFactories);
        }

        public TfLiteRuntime getRuntime() {
            return this.runtime;
        }
    }
}
