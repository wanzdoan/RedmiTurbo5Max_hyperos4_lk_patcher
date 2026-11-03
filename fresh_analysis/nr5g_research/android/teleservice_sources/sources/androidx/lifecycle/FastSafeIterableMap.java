package androidx.lifecycle;

import androidx.collection.MutableScatterMap;
import java.util.Map;
import java.util.NoSuchElementException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;

/* JADX INFO: compiled from: FastSafeIterableMap.kt */
/* JADX INFO: loaded from: classes.dex */
public final class FastSafeIterableMap {
    private Entry head;
    private final MutableScatterMap map = new MutableScatterMap(0, 1, null);
    private Entry tail;

    public final boolean contains(Object obj) {
        obj.getClass();
        return this.map.containsKey(obj);
    }

    public final Object putIfAbsent(Object obj, Object obj2) {
        obj.getClass();
        obj2.getClass();
        Entry entry = (Entry) this.map.get(obj);
        if (entry != null) {
            return entry.getValue();
        }
        Entry entry2 = new Entry(obj, obj2);
        this.map.set(obj, entry2);
        Entry entry3 = this.tail;
        if (entry3 == null) {
            this.head = entry2;
            this.tail = entry2;
            return null;
        }
        if (entry3 != null) {
            entry3.setNext(entry2);
        }
        entry2.setPrev(this.tail);
        this.tail = entry2;
        return null;
    }

    public final Object remove(Object obj) {
        obj.getClass();
        Entry entry = (Entry) this.map.remove(obj);
        if (entry == null) {
            return null;
        }
        if (entry.getPrev() == null) {
            this.head = entry.getNext();
        } else {
            Entry prev = entry.getPrev();
            if (prev != null) {
                prev.setNext(entry.getNext());
            }
        }
        if (entry.getNext() == null) {
            this.tail = entry.getPrev();
        } else {
            Entry next = entry.getNext();
            if (next != null) {
                next.setPrev(entry.getPrev());
            }
        }
        entry.markRemoved();
        return entry.getValue();
    }

    public final Map.Entry ceil(Object obj) {
        obj.getClass();
        Entry entry = (Entry) this.map.get(obj);
        if (entry != null) {
            return entry.getPrev();
        }
        return null;
    }

    public final Map.Entry first() {
        Entry entry = this.head;
        if (entry != null) {
            return entry;
        }
        throw new NoSuchElementException("Collection is empty.");
    }

    public final Map.Entry last() {
        Entry entry = this.tail;
        if (entry != null) {
            return entry;
        }
        throw new NoSuchElementException("Collection is empty.");
    }

    public final Map.Entry lastOrNull() {
        return this.tail;
    }

    public final int size() {
        return this.map.getSize();
    }

    public final void forEachWithAdditions(Function1 function1) {
        function1.getClass();
        for (Entry next = this.head; next != null; next = next.getNext()) {
            if (!next.isRemoved()) {
                function1.invoke(next);
            }
        }
    }

    public final void forEachReversed(Function1 function1) {
        function1.getClass();
        for (Entry prev = this.tail; prev != null; prev = prev.getPrev()) {
            if (!prev.isRemoved()) {
                function1.invoke(prev);
            }
        }
    }

    /* JADX INFO: compiled from: FastSafeIterableMap.kt */
    final class Entry implements Map.Entry, KMappedMarker {
        private boolean isRemoved;
        private final Object key;
        private Entry next;
        private Entry prev;
        private final Object value;

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            return Intrinsics.areEqual(this.key, entry.key) && Intrinsics.areEqual(this.value, entry.value);
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            Object obj = this.key;
            int iHashCode = (obj == null ? 0 : obj.hashCode()) * 31;
            Object obj2 = this.value;
            return iHashCode + (obj2 != null ? obj2.hashCode() : 0);
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        public String toString() {
            return "Entry(key=" + this.key + ", value=" + this.value + ')';
        }

        public Entry(Object obj, Object obj2) {
            this.key = obj;
            this.value = obj2;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this.value;
        }

        public final Entry getNext() {
            return this.next;
        }

        public final void setNext(Entry entry) {
            this.next = entry;
        }

        public final Entry getPrev() {
            return this.prev;
        }

        public final void setPrev(Entry entry) {
            this.prev = entry;
        }

        public final boolean isRemoved() {
            return this.isRemoved;
        }

        public final void markRemoved() {
            this.isRemoved = true;
        }
    }
}
