package com.google.common.collect;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public class ImmutableListMultimap extends ImmutableMultimap implements ListMultimap {
    private static final long serialVersionUID = 0;

    public static ImmutableListMultimap of() {
        return EmptyImmutableListMultimap.INSTANCE;
    }

    public static ImmutableListMultimap of(Object obj, Object obj2) {
        Builder builder = builder();
        builder.put(obj, obj2);
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public final class Builder extends ImmutableMultimap.Builder {
        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public Builder put(Object obj, Object obj2) {
            super.put(obj, obj2);
            return this;
        }

        @Override // com.google.common.collect.ImmutableMultimap.Builder
        public ImmutableListMultimap build() {
            return (ImmutableListMultimap) super.build();
        }
    }

    static ImmutableListMultimap fromMapBuilderEntries(Collection collection, Comparator comparator) {
        if (collection.isEmpty()) {
            return of();
        }
        ImmutableMap.Builder builder = new ImmutableMap.Builder(collection.size());
        Iterator it = collection.iterator();
        int size = 0;
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            ImmutableList.Builder builder2 = (ImmutableList.Builder) entry.getValue();
            ImmutableList immutableListBuild = comparator == null ? builder2.build() : builder2.buildSorted(comparator);
            builder.put(key, immutableListBuild);
            size += immutableListBuild.size();
        }
        return new ImmutableListMultimap(builder.buildOrThrow(), size);
    }

    ImmutableListMultimap(ImmutableMap immutableMap, int i) {
        super(immutableMap, i);
    }

    @Override // com.google.common.collect.Multimap
    public ImmutableList get(Object obj) {
        ImmutableList immutableList = (ImmutableList) this.map.get(obj);
        return immutableList == null ? ImmutableList.of() : immutableList;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Serialization.writeMultimap(this, objectOutputStream);
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        int i = objectInputStream.readInt();
        if (i < 0) {
            CompactHashMap$$ExternalSyntheticBUOutline0.m("Invalid key count ", i);
            return;
        }
        ImmutableMap.Builder builder = ImmutableMap.builder();
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            Object object = objectInputStream.readObject();
            Objects.requireNonNull(object);
            int i4 = objectInputStream.readInt();
            if (i4 <= 0) {
                CompactHashMap$$ExternalSyntheticBUOutline0.m("Invalid value count ", i4);
                return;
            }
            ImmutableList.Builder builder2 = ImmutableList.builder();
            for (int i5 = 0; i5 < i4; i5++) {
                Object object2 = objectInputStream.readObject();
                Objects.requireNonNull(object2);
                builder2.add(object2);
            }
            builder.put(object, builder2.build());
            i2 += i4;
        }
        try {
            ImmutableMultimap.FieldSettersHolder.MAP_FIELD_SETTER.set(this, builder.buildOrThrow());
            ImmutableMultimap.FieldSettersHolder.SIZE_FIELD_SETTER.set(this, i2);
        } catch (IllegalArgumentException e) {
            throw ((InvalidObjectException) new InvalidObjectException(e.getMessage()).initCause(e));
        }
    }
}
