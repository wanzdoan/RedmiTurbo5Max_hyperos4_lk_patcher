package com.google.common.collect;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public abstract class ImmutableMultimap extends BaseImmutableMultimap implements Serializable {
    private static final long serialVersionUID = 0;
    final transient ImmutableMap map;
    final transient int size;

    abstract class FieldSettersHolder {
        static final Serialization.FieldSetter MAP_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "map");
        static final Serialization.FieldSetter SIZE_FIELD_SETTER = Serialization.getFieldSetter(ImmutableMultimap.class, "size");
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public /* bridge */ /* synthetic */ boolean containsEntry(Object obj, Object obj2) {
        return super.containsEntry(obj, obj2);
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ boolean isEmpty() {
        return super.isEmpty();
    }

    @Override // com.google.common.collect.AbstractMultimap
    public /* bridge */ /* synthetic */ String toString() {
        return super.toString();
    }

    public abstract class Builder {
        Map builderMap;
        int expectedValuesPerKey = 4;
        Comparator keyComparator;
        Comparator valueComparator;

        Map ensureBuilderMapNonNull() {
            Map map = this.builderMap;
            if (map != null) {
                return map;
            }
            Map mapPreservesInsertionOrderOnPutsMap = Platform.preservesInsertionOrderOnPutsMap();
            this.builderMap = mapPreservesInsertionOrderOnPutsMap;
            return mapPreservesInsertionOrderOnPutsMap;
        }

        ImmutableCollection.Builder newValueCollectionBuilderWithExpectedSize(int i) {
            return ImmutableList.builderWithExpectedSize(i);
        }

        public Builder put(Object obj, Object obj2) {
            CollectPreconditions.checkEntryNotNull(obj, obj2);
            ImmutableCollection.Builder builderNewValueCollectionBuilderWithExpectedSize = (ImmutableCollection.Builder) ensureBuilderMapNonNull().get(obj);
            if (builderNewValueCollectionBuilderWithExpectedSize == null) {
                builderNewValueCollectionBuilderWithExpectedSize = newValueCollectionBuilderWithExpectedSize(this.expectedValuesPerKey);
                ensureBuilderMapNonNull().put(obj, builderNewValueCollectionBuilderWithExpectedSize);
            }
            builderNewValueCollectionBuilderWithExpectedSize.add(obj2);
            return this;
        }

        public ImmutableMultimap build() {
            Map map = this.builderMap;
            if (map == null) {
                return ImmutableListMultimap.of();
            }
            Collection collectionEntrySet = map.entrySet();
            Comparator comparator = this.keyComparator;
            if (comparator != null) {
                collectionEntrySet = Ordering.from(comparator).onKeys().immutableSortedCopy(collectionEntrySet);
            }
            return ImmutableListMultimap.fromMapBuilderEntries(collectionEntrySet, this.valueComparator);
        }
    }

    ImmutableMultimap(ImmutableMap immutableMap, int i) {
        this.map = immutableMap;
        this.size = i;
    }

    @Override // com.google.common.collect.Multimap
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public final boolean remove(Object obj, Object obj2) {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.common.collect.Multimap
    public int size() {
        return this.size;
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public ImmutableMap asMap() {
        return this.map;
    }

    @Override // com.google.common.collect.AbstractMultimap
    Map createAsMap() {
        throw new AssertionError("should never be called");
    }

    @Override // com.google.common.collect.AbstractMultimap, com.google.common.collect.Multimap
    public ImmutableCollection entries() {
        return (ImmutableCollection) super.entries();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.AbstractMultimap
    public ImmutableCollection createEntries() {
        return new EntryCollection(this);
    }

    class EntryCollection extends ImmutableCollection {
        private static final long serialVersionUID = 0;
        final ImmutableMultimap multimap;

        EntryCollection(ImmutableMultimap immutableMultimap) {
            this.multimap = immutableMultimap;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable
        public UnmodifiableIterator iterator() {
            return this.multimap.entryIterator();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return this.multimap.size();
        }

        @Override // com.google.common.collect.ImmutableCollection, java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return this.multimap.containsEntry(entry.getKey(), entry.getValue());
        }

        @Override // com.google.common.collect.ImmutableCollection
        Object writeReplace() {
            return super.writeReplace();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.common.collect.AbstractMultimap
    public UnmodifiableIterator entryIterator() {
        return new UnmodifiableIterator() { // from class: com.google.common.collect.ImmutableMultimap.1
            final Iterator asMapItr;
            Object currentKey = null;
            Iterator valueItr = Iterators.emptyIterator();

            {
                this.asMapItr = ImmutableMultimap.this.map.entrySet().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.valueItr.hasNext() || this.asMapItr.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry next() {
                if (!this.valueItr.hasNext()) {
                    Map.Entry entry = (Map.Entry) this.asMapItr.next();
                    this.currentKey = entry.getKey();
                    this.valueItr = ((ImmutableCollection) entry.getValue()).iterator();
                }
                Object obj = this.currentKey;
                Objects.requireNonNull(obj);
                return Maps.immutableEntry(obj, this.valueItr.next());
            }
        };
    }
}
