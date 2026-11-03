package com.google.common.collect;

import com.google.common.base.Function;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public abstract class Ordering implements Comparator {
    @Override // java.util.Comparator
    public abstract int compare(Object obj, Object obj2);

    public static Ordering natural() {
        return NaturalOrdering.INSTANCE;
    }

    public static Ordering from(Comparator comparator) {
        if (comparator instanceof Ordering) {
            return (Ordering) comparator;
        }
        return new ComparatorOrdering(comparator);
    }

    class ArbitraryOrdering extends Ordering {
        private final AtomicInteger counter = new AtomicInteger(0);
        private final ConcurrentMap uids = Platform.tryWeakKeys(new MapMaker()).makeMap();

        ArbitraryOrdering() {
        }

        private Integer getUid(Object obj) {
            Integer numValueOf = (Integer) this.uids.get(obj);
            if (numValueOf == null) {
                numValueOf = Integer.valueOf(this.counter.getAndIncrement());
                Integer num = (Integer) this.uids.putIfAbsent(obj, numValueOf);
                if (num != null) {
                    return num;
                }
            }
            return numValueOf;
        }

        @Override // com.google.common.collect.Ordering, java.util.Comparator
        public int compare(Object obj, Object obj2) {
            if (obj == obj2) {
                return 0;
            }
            if (obj == null) {
                return -1;
            }
            if (obj2 == null) {
                return 1;
            }
            int iIdentityHashCode = identityHashCode(obj);
            int iIdentityHashCode2 = identityHashCode(obj2);
            if (iIdentityHashCode != iIdentityHashCode2) {
                return iIdentityHashCode < iIdentityHashCode2 ? -1 : 1;
            }
            int iCompareTo = getUid(obj).compareTo(getUid(obj2));
            if (iCompareTo != 0) {
                return iCompareTo;
            }
            throw new AssertionError();
        }

        public String toString() {
            return "Ordering.arbitrary()";
        }

        int identityHashCode(Object obj) {
            return System.identityHashCode(obj);
        }
    }

    protected Ordering() {
    }

    public Ordering reverse() {
        return new ReverseOrdering(this);
    }

    public Ordering onResultOf(Function function) {
        return new ByFunctionOrdering(function, this);
    }

    Ordering onKeys() {
        return onResultOf(Maps.keyFunction());
    }

    public Ordering compound(Comparator comparator) {
        comparator.getClass();
        return new CompoundOrdering(this, comparator);
    }

    public List sortedCopy(Iterable iterable) {
        Object[] array = Iterables.toArray(iterable);
        Arrays.sort(array, this);
        return Lists.newArrayList(Arrays.asList(array));
    }

    public ImmutableList immutableSortedCopy(Iterable iterable) {
        return ImmutableList.sortedCopyOf(this, iterable);
    }
}
