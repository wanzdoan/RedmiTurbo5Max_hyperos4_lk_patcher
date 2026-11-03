package org.tensorflow.lite.support.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SequentialProcessor<T> implements Processor<T> {
    protected final Map<String, List<Integer>> operatorIndex;
    protected final List<Operator<T>> operatorList;

    protected SequentialProcessor(Builder<T> builder) {
        this.operatorList = ((Builder) builder).operatorList;
        this.operatorIndex = Collections.unmodifiableMap(((Builder) builder).operatorIndex);
    }

    @Override // org.tensorflow.lite.support.common.Processor
    public T process(T x) {
        for (Operator<T> op : this.operatorList) {
            x = op.apply(x);
        }
        return x;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static class Builder<T> {
        private final List<Operator<T>> operatorList = new ArrayList();
        private final Map<String, List<Integer>> operatorIndex = new HashMap();

        protected Builder() {
        }

        public Builder<T> add(Operator<T> op) {
            SupportPreconditions.checkNotNull(op, "Adding null Op is illegal.");
            this.operatorList.add(op);
            String operatorName = op.getClass().getName();
            if (!this.operatorIndex.containsKey(operatorName)) {
                this.operatorIndex.put(operatorName, new ArrayList());
            }
            this.operatorIndex.get(operatorName).add(Integer.valueOf(this.operatorList.size() - 1));
            return this;
        }

        public SequentialProcessor<T> build() {
            return new SequentialProcessor<>(this);
        }
    }
}
