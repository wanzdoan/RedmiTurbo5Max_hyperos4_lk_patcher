package com.google.common.collect;

import com.google.common.base.Function;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class Multimaps {
    public static ListMultimap transformValues(ListMultimap listMultimap, Function function) {
        function.getClass();
        return transformEntries(listMultimap, Maps.asEntryTransformer(function));
    }

    public static ListMultimap transformEntries(ListMultimap listMultimap, Maps.EntryTransformer entryTransformer) {
        return new TransformedEntriesListMultimap(listMultimap, entryTransformer);
    }

    abstract class TransformedEntriesMultimap extends AbstractMultimap {
        final Multimap fromMultimap;
        final Maps.EntryTransformer transformer;

        @Override // com.google.common.collect.Multimap
        public abstract Collection get(Object obj);

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Collection transform(Object obj, Collection collection);

        TransformedEntriesMultimap(Multimap multimap, Maps.EntryTransformer entryTransformer) {
            multimap.getClass();
            this.fromMultimap = multimap;
            entryTransformer.getClass();
            this.transformer = entryTransformer;
        }

        @Override // com.google.common.collect.AbstractMultimap
        Map createAsMap() {
            return Maps.transformEntries(this.fromMultimap.asMap(), new Maps.EntryTransformer() { // from class: com.google.common.collect.Multimaps$TransformedEntriesMultimap$$ExternalSyntheticLambda0
                @Override // com.google.common.collect.Maps.EntryTransformer
                public final Object transformEntry(Object obj, Object obj2) {
                    return this.f$0.transform(obj, (Collection) obj2);
                }
            });
        }

        @Override // com.google.common.collect.Multimap
        public void clear() {
            this.fromMultimap.clear();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Collection createEntries() {
            return new AbstractMultimap.Entries();
        }

        @Override // com.google.common.collect.AbstractMultimap
        Iterator entryIterator() {
            return Iterators.transform(this.fromMultimap.entries().iterator(), Maps.asEntryToEntryFunction(this.transformer));
        }

        @Override // com.google.common.collect.Multimap
        public boolean remove(Object obj, Object obj2) {
            return get(obj).remove(obj2);
        }

        @Override // com.google.common.collect.Multimap
        public int size() {
            return this.fromMultimap.size();
        }
    }

    final class TransformedEntriesListMultimap extends TransformedEntriesMultimap implements ListMultimap {
        TransformedEntriesListMultimap(ListMultimap listMultimap, Maps.EntryTransformer entryTransformer) {
            super(listMultimap, entryTransformer);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap
        public List transform(Object obj, Collection collection) {
            return Lists.transform((List) collection, Maps.asValueToValueFunction(this.transformer, obj));
        }

        @Override // com.google.common.collect.Multimaps.TransformedEntriesMultimap, com.google.common.collect.Multimap
        public List get(Object obj) {
            return transform(obj, this.fromMultimap.get(obj));
        }
    }

    abstract class Entries extends AbstractCollection {
        abstract Multimap multimap();

        Entries() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public int size() {
            return multimap().size();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return multimap().containsEntry(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            return multimap().remove(entry.getKey(), entry.getValue());
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public void clear() {
            multimap().clear();
        }
    }

    static boolean equalsImpl(Multimap multimap, Object obj) {
        if (obj == multimap) {
            return true;
        }
        if (obj instanceof Multimap) {
            return multimap.asMap().equals(((Multimap) obj).asMap());
        }
        return false;
    }
}
