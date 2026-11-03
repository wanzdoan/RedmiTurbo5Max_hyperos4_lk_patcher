package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.NavigableSet;

/* JADX INFO: loaded from: classes.dex */
public abstract class ImmutableSortedSet extends ImmutableSet implements NavigableSet, SortedIterable {
    private static final long serialVersionUID = 912559;
    final transient Comparator comparator;
    transient ImmutableSortedSet descendingSet;

    abstract ImmutableSortedSet createDescendingSet();

    @Override // java.util.SortedSet
    public abstract Object first();

    abstract ImmutableSortedSet headSetImpl(Object obj, boolean z);

    @Override // java.util.SortedSet
    public abstract Object last();

    abstract ImmutableSortedSet subSetImpl(Object obj, boolean z, Object obj2, boolean z2);

    abstract ImmutableSortedSet tailSetImpl(Object obj, boolean z);

    static RegularImmutableSortedSet emptySet(Comparator comparator) {
        if (Ordering.natural().equals(comparator)) {
            return RegularImmutableSortedSet.NATURAL_EMPTY_SET;
        }
        return new RegularImmutableSortedSet(ImmutableList.of(), comparator);
    }

    static ImmutableSortedSet construct(Comparator comparator, int i, Object... objArr) {
        if (i == 0) {
            return emptySet(comparator);
        }
        ObjectArrays.checkElementsNotNull(objArr, i);
        Arrays.sort(objArr, 0, i, comparator);
        int i2 = 1;
        for (int i3 = 1; i3 < i; i3++) {
            Object obj = objArr[i3];
            if (comparator.compare(obj, objArr[i2 - 1]) != 0) {
                objArr[i2] = obj;
                i2++;
            }
        }
        Arrays.fill(objArr, i2, i, (Object) null);
        if (i2 < objArr.length / 2) {
            objArr = Arrays.copyOf(objArr, i2);
        }
        return new RegularImmutableSortedSet(ImmutableList.asImmutableList(objArr, i2), comparator);
    }

    public final class Builder extends ImmutableSet.Builder {
        private final Comparator comparator;

        public Builder(Comparator comparator) {
            comparator.getClass();
            this.comparator = comparator;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder, com.google.common.collect.ImmutableCollection.Builder
        public Builder add(Object obj) {
            super.add(obj);
            return this;
        }

        @Override // com.google.common.collect.ImmutableSet.Builder, com.google.common.collect.ImmutableCollection.ArrayBasedBuilder
        public Builder add(Object... objArr) {
            super.add(objArr);
            return this;
        }

        public ImmutableSortedSet build() {
            ImmutableSortedSet immutableSortedSetConstruct = ImmutableSortedSet.construct(this.comparator, this.size, this.contents);
            this.size = immutableSortedSetConstruct.size();
            this.forceCopy = true;
            return immutableSortedSetConstruct;
        }
    }

    int unsafeCompare(Object obj, Object obj2) {
        return unsafeCompare(this.comparator, obj, obj2);
    }

    static int unsafeCompare(Comparator comparator, Object obj, Object obj2) {
        return comparator.compare(obj, obj2);
    }

    ImmutableSortedSet(Comparator comparator) {
        this.comparator = comparator;
    }

    @Override // java.util.SortedSet, com.google.common.collect.SortedIterable
    public Comparator comparator() {
        return this.comparator;
    }

    @Override // java.util.NavigableSet, java.util.SortedSet
    public ImmutableSortedSet headSet(Object obj) {
        return headSet(obj, false);
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet headSet(Object obj, boolean z) {
        obj.getClass();
        return headSetImpl(obj, z);
    }

    @Override // java.util.NavigableSet, java.util.SortedSet
    public ImmutableSortedSet subSet(Object obj, Object obj2) {
        return subSet(obj, true, obj2, false);
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet subSet(Object obj, boolean z, Object obj2, boolean z2) {
        obj.getClass();
        obj2.getClass();
        Preconditions.checkArgument(this.comparator.compare(obj, obj2) <= 0);
        return subSetImpl(obj, z, obj2, z2);
    }

    @Override // java.util.NavigableSet, java.util.SortedSet
    public ImmutableSortedSet tailSet(Object obj) {
        return tailSet(obj, true);
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet tailSet(Object obj, boolean z) {
        obj.getClass();
        return tailSetImpl(obj, z);
    }

    @Override // java.util.NavigableSet
    public final Object pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableSet
    public final Object pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.NavigableSet
    public ImmutableSortedSet descendingSet() {
        ImmutableSortedSet immutableSortedSet = this.descendingSet;
        if (immutableSortedSet != null) {
            return immutableSortedSet;
        }
        ImmutableSortedSet immutableSortedSetCreateDescendingSet = createDescendingSet();
        this.descendingSet = immutableSortedSetCreateDescendingSet;
        immutableSortedSetCreateDescendingSet.descendingSet = this;
        return immutableSortedSetCreateDescendingSet;
    }

    class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Comparator comparator;
        final Object[] elements;

        public SerializedForm(Comparator comparator, Object[] objArr) {
            this.comparator = comparator;
            this.elements = objArr;
        }

        Object readResolve() {
            return new Builder(this.comparator).add(this.elements).build();
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override // com.google.common.collect.ImmutableSet, com.google.common.collect.ImmutableCollection
    Object writeReplace() {
        return new SerializedForm(this.comparator, toArray());
    }
}
