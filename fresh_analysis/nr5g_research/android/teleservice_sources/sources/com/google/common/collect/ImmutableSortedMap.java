package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class ImmutableSortedMap extends ImmutableMap implements NavigableMap {
    private static final long serialVersionUID = 0;
    private transient ImmutableSortedMap descendingMap;
    private final transient RegularImmutableSortedSet keySet;
    private final transient ImmutableList valueList;
    private static final Comparator NATURAL_ORDER = Ordering.natural();
    private static final ImmutableSortedMap NATURAL_EMPTY_MAP = new ImmutableSortedMap(ImmutableSortedSet.emptySet(Ordering.natural()), ImmutableList.of());

    static ImmutableSortedMap emptyMap(Comparator comparator) {
        if (Ordering.natural().equals(comparator)) {
            return of();
        }
        return new ImmutableSortedMap(ImmutableSortedSet.emptySet(comparator), ImmutableList.of());
    }

    public static ImmutableSortedMap of() {
        return NATURAL_EMPTY_MAP;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ImmutableSortedMap of(Comparator comparator, Object obj, Object obj2) {
        ImmutableList immutableListOf = ImmutableList.of(obj);
        comparator.getClass();
        return new ImmutableSortedMap(new RegularImmutableSortedSet(immutableListOf, comparator), ImmutableList.of(obj2));
    }

    public class Builder extends ImmutableMap.Builder {
        private final Comparator comparator;
        private transient Object[] keys;
        private transient Object[] values;

        public Builder(Comparator comparator) {
            this(comparator, 4);
        }

        private Builder(Comparator comparator, int i) {
            comparator.getClass();
            this.comparator = comparator;
            this.keys = new Object[i];
            this.values = new Object[i];
        }

        private void ensureCapacity(int i) {
            Object[] objArr = this.keys;
            if (i > objArr.length) {
                int iExpandedCapacity = ImmutableCollection.Builder.expandedCapacity(objArr.length, i);
                this.keys = Arrays.copyOf(this.keys, iExpandedCapacity);
                this.values = Arrays.copyOf(this.values, iExpandedCapacity);
            }
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public Builder put(Object obj, Object obj2) {
            ensureCapacity(this.size + 1);
            CollectPreconditions.checkEntryNotNull(obj, obj2);
            Object[] objArr = this.keys;
            int i = this.size;
            objArr[i] = obj;
            this.values[i] = obj2;
            this.size = i + 1;
            return this;
        }

        @Override // com.google.common.collect.ImmutableMap.Builder
        public ImmutableSortedMap buildOrThrow() {
            int i = this.size;
            if (i == 0) {
                return ImmutableSortedMap.emptyMap(this.comparator);
            }
            if (i == 1) {
                Comparator comparator = this.comparator;
                Object obj = this.keys[0];
                Objects.requireNonNull(obj);
                Object obj2 = this.values[0];
                Objects.requireNonNull(obj2);
                return ImmutableSortedMap.of(comparator, obj, obj2);
            }
            Object[] objArrCopyOf = Arrays.copyOf(this.keys, i);
            Arrays.sort(objArrCopyOf, this.comparator);
            Object[] objArr = new Object[this.size];
            for (int i2 = 0; i2 < this.size; i2++) {
                if (i2 > 0) {
                    int i3 = i2 - 1;
                    if (this.comparator.compare(objArrCopyOf[i3], objArrCopyOf[i2]) == 0) {
                        StringBuilder sb = new StringBuilder("keys required to be distinct but compared as equal: ");
                        sb.append(objArrCopyOf[i3]);
                        Object obj3 = objArrCopyOf[i2];
                        sb.append(" and ");
                        sb.append(obj3);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
                Object obj4 = this.keys[i2];
                Objects.requireNonNull(obj4);
                int iBinarySearch = Arrays.binarySearch(objArrCopyOf, obj4, this.comparator);
                Object obj5 = this.values[i2];
                Objects.requireNonNull(obj5);
                objArr[iBinarySearch] = obj5;
            }
            return new ImmutableSortedMap(new RegularImmutableSortedSet(ImmutableList.asImmutableList(objArrCopyOf), this.comparator), ImmutableList.asImmutableList(objArr));
        }
    }

    ImmutableSortedMap(RegularImmutableSortedSet regularImmutableSortedSet, ImmutableList immutableList) {
        this(regularImmutableSortedSet, immutableList, null);
    }

    ImmutableSortedMap(RegularImmutableSortedSet regularImmutableSortedSet, ImmutableList immutableList, ImmutableSortedMap immutableSortedMap) {
        this.keySet = regularImmutableSortedSet;
        this.valueList = immutableList;
        this.descendingMap = immutableSortedMap;
    }

    @Override // java.util.Map
    public int size() {
        return this.valueList.size();
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public Object get(Object obj) {
        int iIndexOf = this.keySet.indexOf(obj);
        if (iIndexOf == -1) {
            return null;
        }
        return this.valueList.get(iIndexOf);
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSet entrySet() {
        return super.entrySet();
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet createEntrySet() {
        return isEmpty() ? ImmutableSet.of() : new ImmutableMapEntrySet() { // from class: com.google.common.collect.ImmutableSortedMap.1EntrySet
            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
            public UnmodifiableIterator iterator() {
                return asList().iterator();
            }

            @Override // com.google.common.collect.ImmutableSet
            ImmutableList createAsList() {
                return new ImmutableList() { // from class: com.google.common.collect.ImmutableSortedMap.1EntrySet.1
                    @Override // java.util.List
                    public Map.Entry get(int i) {
                        return new AbstractMap.SimpleImmutableEntry(ImmutableSortedMap.this.keySet.asList().get(i), ImmutableSortedMap.this.valueList.get(i));
                    }

                    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
                    public int size() {
                        return ImmutableSortedMap.this.size();
                    }

                    @Override // com.google.common.collect.ImmutableList, com.google.common.collect.ImmutableCollection
                    Object writeReplace() {
                        return super.writeReplace();
                    }
                };
            }

            @Override // com.google.common.collect.ImmutableMapEntrySet
            ImmutableMap map() {
                return ImmutableSortedMap.this;
            }

            @Override // com.google.common.collect.ImmutableMapEntrySet, com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
            Object writeReplace() {
                return super.writeReplace();
            }
        };
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableSortedSet keySet() {
        return this.keySet;
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableSet createKeySet() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.ImmutableMap, java.util.Map
    public ImmutableCollection values() {
        return this.valueList;
    }

    @Override // com.google.common.collect.ImmutableMap
    ImmutableCollection createValues() {
        throw new AssertionError("should never be called");
    }

    @Override // java.util.SortedMap
    public Comparator comparator() {
        return keySet().comparator();
    }

    @Override // java.util.SortedMap
    public Object firstKey() {
        return keySet().first();
    }

    @Override // java.util.SortedMap
    public Object lastKey() {
        return keySet().last();
    }

    private ImmutableSortedMap getSubMap(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        if (i == i2) {
            return emptyMap(comparator());
        }
        return new ImmutableSortedMap(this.keySet.getSubSet(i, i2), this.valueList.subList(i, i2));
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap headMap(Object obj) {
        return headMap(obj, false);
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedMap headMap(Object obj, boolean z) {
        RegularImmutableSortedSet regularImmutableSortedSet = this.keySet;
        obj.getClass();
        return getSubMap(0, regularImmutableSortedSet.headIndex(obj, z));
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap subMap(Object obj, Object obj2) {
        return subMap(obj, true, obj2, false);
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedMap subMap(Object obj, boolean z, Object obj2, boolean z2) {
        obj.getClass();
        obj2.getClass();
        Preconditions.checkArgument(comparator().compare(obj, obj2) <= 0, "expected fromKey <= toKey but %s > %s", obj, obj2);
        return headMap(obj2, z2).tailMap(obj, z);
    }

    @Override // java.util.NavigableMap, java.util.SortedMap
    public ImmutableSortedMap tailMap(Object obj) {
        return tailMap(obj, true);
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedMap tailMap(Object obj, boolean z) {
        RegularImmutableSortedSet regularImmutableSortedSet = this.keySet;
        obj.getClass();
        return getSubMap(regularImmutableSortedSet.tailIndex(obj, z), size());
    }

    @Override // java.util.NavigableMap
    public Map.Entry lowerEntry(Object obj) {
        return headMap(obj, false).lastEntry();
    }

    @Override // java.util.NavigableMap
    public Object lowerKey(Object obj) {
        return Maps.keyOrNull(lowerEntry(obj));
    }

    @Override // java.util.NavigableMap
    public Map.Entry floorEntry(Object obj) {
        return headMap(obj, true).lastEntry();
    }

    @Override // java.util.NavigableMap
    public Object floorKey(Object obj) {
        return Maps.keyOrNull(floorEntry(obj));
    }

    @Override // java.util.NavigableMap
    public Map.Entry ceilingEntry(Object obj) {
        return tailMap(obj, true).firstEntry();
    }

    @Override // java.util.NavigableMap
    public Object ceilingKey(Object obj) {
        return Maps.keyOrNull(ceilingEntry(obj));
    }

    @Override // java.util.NavigableMap
    public Map.Entry higherEntry(Object obj) {
        return tailMap(obj, false).firstEntry();
    }

    @Override // java.util.NavigableMap
    public Object higherKey(Object obj) {
        return Maps.keyOrNull(higherEntry(obj));
    }

    @Override // java.util.NavigableMap
    public Map.Entry firstEntry() {
        if (isEmpty()) {
            return null;
        }
        return (Map.Entry) entrySet().asList().get(0);
    }

    @Override // java.util.NavigableMap
    public Map.Entry lastEntry() {
        if (isEmpty()) {
            return null;
        }
        return (Map.Entry) entrySet().asList().get(size() - 1);
    }

    @Override // java.util.NavigableMap
    public final Map.Entry pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableMap
    public final Map.Entry pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedMap descendingMap() {
        ImmutableSortedMap immutableSortedMap = this.descendingMap;
        if (immutableSortedMap != null) {
            return immutableSortedMap;
        }
        if (isEmpty()) {
            return emptyMap(Ordering.from(comparator()).reverse());
        }
        return new ImmutableSortedMap((RegularImmutableSortedSet) this.keySet.descendingSet(), this.valueList.reverse(), this);
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedSet navigableKeySet() {
        return this.keySet;
    }

    @Override // java.util.NavigableMap
    public ImmutableSortedSet descendingKeySet() {
        return this.keySet.descendingSet();
    }

    class SerializedForm extends ImmutableMap.SerializedForm {
        private static final long serialVersionUID = 0;
        private final Comparator comparator;

        SerializedForm(ImmutableSortedMap immutableSortedMap) {
            super(immutableSortedMap);
            this.comparator = immutableSortedMap.comparator();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.ImmutableMap.SerializedForm
        public Builder makeBuilder(int i) {
            return new Builder(this.comparator);
        }
    }

    @Override // com.google.common.collect.ImmutableMap
    Object writeReplace() {
        return new SerializedForm(this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }
}
