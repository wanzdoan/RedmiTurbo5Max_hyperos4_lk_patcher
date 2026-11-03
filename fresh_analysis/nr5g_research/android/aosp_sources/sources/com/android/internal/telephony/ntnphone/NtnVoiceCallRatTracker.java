package com.android.internal.telephony.ntnphone;

import com.android.internal.telephony.nano.PersistAtomsProto$VoiceCallRatUsage;
import com.android.telephony.Rlog;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;

/* JADX INFO: loaded from: classes.dex */
public class NtnVoiceCallRatTracker {
    private static final String TAG = "NtnVoiceCallRatTracker";
    private Key mLastKey;
    private long mLastKeyTimestampMillis;
    private final Map mRatUsageMap = new HashMap();

    NtnVoiceCallRatTracker() {
        clear();
    }

    public static NtnVoiceCallRatTracker fromProto(PersistAtomsProto$VoiceCallRatUsage[] persistAtomsProto$VoiceCallRatUsageArr) {
        final NtnVoiceCallRatTracker ntnVoiceCallRatTracker = new NtnVoiceCallRatTracker();
        if (persistAtomsProto$VoiceCallRatUsageArr == null) {
            Rlog.e(TAG, "fromProto: usages=null");
            return ntnVoiceCallRatTracker;
        }
        Arrays.stream(persistAtomsProto$VoiceCallRatUsageArr).forEach(new Consumer() { // from class: com.android.internal.telephony.ntnphone.NtnVoiceCallRatTracker$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.addProto((PersistAtomsProto$VoiceCallRatUsage) obj);
            }
        });
        return ntnVoiceCallRatTracker;
    }

    public PersistAtomsProto$VoiceCallRatUsage[] toProto() {
        return (PersistAtomsProto$VoiceCallRatUsage[]) this.mRatUsageMap.entrySet().stream().map(new Function() { // from class: com.android.internal.telephony.ntnphone.NtnVoiceCallRatTracker$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                return NtnVoiceCallRatTracker.entryToProto((Map.Entry) obj);
            }
        }).toArray(new IntFunction() { // from class: com.android.internal.telephony.ntnphone.NtnVoiceCallRatTracker$$ExternalSyntheticLambda3
            @Override // java.util.function.IntFunction
            public final Object apply(int i) {
                return NtnVoiceCallRatTracker.$r8$lambda$1wo9kefFesel8nxS8T86VHF6n10(i);
            }
        });
    }

    public static /* synthetic */ PersistAtomsProto$VoiceCallRatUsage[] $r8$lambda$1wo9kefFesel8nxS8T86VHF6n10(int i) {
        return new PersistAtomsProto$VoiceCallRatUsage[i];
    }

    public void clear() {
        this.mRatUsageMap.clear();
        this.mLastKey = null;
        this.mLastKeyTimestampMillis = 0L;
    }

    public void add(int i, int i2, long j, Set<Integer> set) {
        if (this.mLastKey != null) {
            long j2 = j - this.mLastKeyTimestampMillis;
            if (j2 < 0) {
                Rlog.e(TAG, "add: durationMillis<0");
                j2 = 0;
            }
            addToKey(this.mLastKey, j2, set);
        }
        Key key = new Key(i, i2);
        addToKey(key, 0L, set);
        this.mLastKey = key;
        this.mLastKeyTimestampMillis = j;
    }

    public void conclude(long j) {
        if (this.mLastKey != null) {
            long j2 = j - this.mLastKeyTimestampMillis;
            if (j2 < 0) {
                Rlog.e(TAG, "conclude: durationMillis<0");
                j2 = 0;
            }
            Value value = (Value) this.mRatUsageMap.get(this.mLastKey);
            if (value == null) {
                Rlog.e(TAG, "conclude: value=null && mLastKey!=null");
            } else {
                value.durationMillis += j2;
            }
            this.mRatUsageMap.values().stream().forEach(new Consumer() { // from class: com.android.internal.telephony.ntnphone.NtnVoiceCallRatTracker$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    ((NtnVoiceCallRatTracker.Value) obj).endSession();
                }
            });
            return;
        }
        Rlog.e(TAG, "conclude: mLastKey=null");
    }

    public NtnVoiceCallRatTracker mergeWith(NtnVoiceCallRatTracker ntnVoiceCallRatTracker) {
        if (ntnVoiceCallRatTracker == null) {
            Rlog.e(TAG, "mergeWith: attempting to merge with null", new Throwable());
            return this;
        }
        ntnVoiceCallRatTracker.mRatUsageMap.entrySet().stream().forEach(new Consumer() { // from class: com.android.internal.telephony.ntnphone.NtnVoiceCallRatTracker$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                this.f$0.lambda$mergeWith$2((Map.Entry) obj);
            }
        });
        return this;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$mergeWith$2(Map.Entry entry) {
        this.mRatUsageMap.merge((Key) entry.getKey(), (Value) entry.getValue(), new BiFunction() { // from class: com.android.internal.telephony.ntnphone.NtnVoiceCallRatTracker$$ExternalSyntheticLambda5
            @Override // java.util.function.BiFunction
            public final Object apply(Object obj, Object obj2) {
                return NtnVoiceCallRatTracker.Value.mergeInPlace((NtnVoiceCallRatTracker.Value) obj, (NtnVoiceCallRatTracker.Value) obj2);
            }
        });
    }

    private void addToKey(Key key, long j, Set set) {
        Value value = (Value) this.mRatUsageMap.get(key);
        if (value == null) {
            this.mRatUsageMap.put(key, new Value(j, set));
        } else {
            value.add(j, set);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addProto(PersistAtomsProto$VoiceCallRatUsage persistAtomsProto$VoiceCallRatUsage) {
        this.mRatUsageMap.put(Key.fromProto(persistAtomsProto$VoiceCallRatUsage), Value.fromProto(persistAtomsProto$VoiceCallRatUsage));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PersistAtomsProto$VoiceCallRatUsage entryToProto(Map.Entry entry) {
        Key key = (Key) entry.getKey();
        Value value = (Value) entry.getValue();
        PersistAtomsProto$VoiceCallRatUsage persistAtomsProto$VoiceCallRatUsage = new PersistAtomsProto$VoiceCallRatUsage();
        persistAtomsProto$VoiceCallRatUsage.carrierId = key.carrierId;
        persistAtomsProto$VoiceCallRatUsage.rat = key.rat;
        if (value.mConnectionIds != null) {
            Rlog.e(TAG, "call not concluded when converting to proto");
        }
        persistAtomsProto$VoiceCallRatUsage.totalDurationMillis = value.durationMillis;
        persistAtomsProto$VoiceCallRatUsage.callCount = value.callCount;
        return persistAtomsProto$VoiceCallRatUsage;
    }

    private static class Key {
        public final int carrierId;
        public final int rat;

        Key(int i, int i2) {
            this.carrierId = i;
            this.rat = i2;
        }

        static Key fromProto(PersistAtomsProto$VoiceCallRatUsage persistAtomsProto$VoiceCallRatUsage) {
            return new Key(persistAtomsProto$VoiceCallRatUsage.carrierId, persistAtomsProto$VoiceCallRatUsage.rat);
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.carrierId), Integer.valueOf(this.rat));
        }

        public boolean equals(Object obj) {
            if (obj != null && obj.getClass() == getClass()) {
                Key key = (Key) obj;
                if (key.carrierId == this.carrierId && key.rat == this.rat) {
                    return true;
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class Value {
        public long callCount;
        public long durationMillis;
        private Set mConnectionIds;

        Value(long j, Set set) {
            this.durationMillis = j;
            this.mConnectionIds = set;
            this.callCount = 0L;
        }

        private Value(long j, long j2) {
            this.durationMillis = j;
            this.mConnectionIds = null;
            this.callCount = j2;
        }

        void add(long j, Set set) {
            this.durationMillis += j;
            Set set2 = this.mConnectionIds;
            if (set2 != null) {
                set2.addAll(set);
            } else {
                Rlog.e(NtnVoiceCallRatTracker.TAG, "Value: trying to add to concluded call");
            }
        }

        void endSession() {
            if (this.mConnectionIds != null) {
                if (this.callCount != 0) {
                    Rlog.e(NtnVoiceCallRatTracker.TAG, "Value: mConnectionIds!=null && callCount!=0");
                }
                this.callCount = this.mConnectionIds.size();
                this.mConnectionIds = null;
            }
        }

        static Value fromProto(PersistAtomsProto$VoiceCallRatUsage persistAtomsProto$VoiceCallRatUsage) {
            return new Value(persistAtomsProto$VoiceCallRatUsage.totalDurationMillis, persistAtomsProto$VoiceCallRatUsage.callCount);
        }

        static Value mergeInPlace(Value value, Value value2) {
            if (value2.mConnectionIds != null || value.mConnectionIds != null) {
                Rlog.e(NtnVoiceCallRatTracker.TAG, "Value: call not concluded yet when merging");
            }
            value.durationMillis += value2.durationMillis;
            value.callCount += value2.callCount;
            return value;
        }
    }
}
