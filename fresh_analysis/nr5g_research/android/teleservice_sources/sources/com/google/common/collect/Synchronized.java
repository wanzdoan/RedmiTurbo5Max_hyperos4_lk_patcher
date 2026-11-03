package com.google.common.collect;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

/* JADX INFO: loaded from: classes.dex */
abstract class Synchronized {

    abstract class SynchronizedObject implements Serializable {
        private static final long serialVersionUID = 0;
        final Object delegate;
        final Object mutex;

        SynchronizedObject(Object obj, Object obj2) {
            obj.getClass();
            this.delegate = obj;
            this.mutex = obj2 == null ? this : obj2;
        }

        Object delegate() {
            return this.delegate;
        }

        public String toString() {
            String string;
            synchronized (this.mutex) {
                string = this.delegate.toString();
            }
            return string;
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            synchronized (this.mutex) {
                objectOutputStream.defaultWriteObject();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Collection collection(Collection collection, Object obj) {
        return new SynchronizedCollection(collection, obj);
    }

    class SynchronizedCollection extends SynchronizedObject implements Collection {
        private static final long serialVersionUID = 0;

        private SynchronizedCollection(Collection collection, Object obj) {
            super(collection, obj);
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedObject
        Collection delegate() {
            return (Collection) super.delegate();
        }

        @Override // java.util.Collection
        public boolean add(Object obj) {
            boolean zAdd;
            synchronized (this.mutex) {
                zAdd = delegate().add(obj);
            }
            return zAdd;
        }

        @Override // java.util.Collection
        public boolean addAll(Collection collection) {
            boolean zAddAll;
            synchronized (this.mutex) {
                zAddAll = delegate().addAll(collection);
            }
            return zAddAll;
        }

        @Override // java.util.Collection
        public void clear() {
            synchronized (this.mutex) {
                delegate().clear();
            }
        }

        @Override // java.util.Collection
        public boolean contains(Object obj) {
            boolean zContains;
            synchronized (this.mutex) {
                zContains = delegate().contains(obj);
            }
            return zContains;
        }

        @Override // java.util.Collection
        public boolean containsAll(Collection collection) {
            boolean zContainsAll;
            synchronized (this.mutex) {
                zContainsAll = delegate().containsAll(collection);
            }
            return zContainsAll;
        }

        @Override // java.util.Collection
        public boolean isEmpty() {
            boolean zIsEmpty;
            synchronized (this.mutex) {
                zIsEmpty = delegate().isEmpty();
            }
            return zIsEmpty;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Iterator iterator() {
            return delegate().iterator();
        }

        @Override // java.util.Collection
        public boolean remove(Object obj) {
            boolean zRemove;
            synchronized (this.mutex) {
                zRemove = delegate().remove(obj);
            }
            return zRemove;
        }

        @Override // java.util.Collection
        public boolean removeAll(Collection collection) {
            boolean zRemoveAll;
            synchronized (this.mutex) {
                zRemoveAll = delegate().removeAll(collection);
            }
            return zRemoveAll;
        }

        @Override // java.util.Collection
        public boolean retainAll(Collection collection) {
            boolean zRetainAll;
            synchronized (this.mutex) {
                zRetainAll = delegate().retainAll(collection);
            }
            return zRetainAll;
        }

        @Override // java.util.Collection
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = delegate().size();
            }
            return size;
        }

        @Override // java.util.Collection
        public Object[] toArray() {
            Object[] array;
            synchronized (this.mutex) {
                array = delegate().toArray();
            }
            return array;
        }

        @Override // java.util.Collection
        public Object[] toArray(Object[] objArr) {
            Object[] array;
            synchronized (this.mutex) {
                array = delegate().toArray(objArr);
            }
            return array;
        }
    }

    static Set set(Set set, Object obj) {
        return new SynchronizedSet(set, obj);
    }

    class SynchronizedSet extends SynchronizedCollection implements Set {
        private static final long serialVersionUID = 0;

        SynchronizedSet(Set set, Object obj) {
            super(set, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedCollection, com.google.common.collect.Synchronized.SynchronizedObject
        public Set delegate() {
            return (Set) super.delegate();
        }

        @Override // java.util.Collection, java.util.Set
        public boolean equals(Object obj) {
            boolean zEquals;
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                zEquals = delegate().equals(obj);
            }
            return zEquals;
        }

        @Override // java.util.Collection, java.util.Set
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = delegate().hashCode();
            }
            return iHashCode;
        }
    }

    abstract class SynchronizedSortedSet extends SynchronizedSet implements SortedSet {
        private static final long serialVersionUID = 0;

        SynchronizedSortedSet(SortedSet sortedSet, Object obj) {
            super(sortedSet, obj);
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedSet, com.google.common.collect.Synchronized.SynchronizedCollection, com.google.common.collect.Synchronized.SynchronizedObject
        SortedSet delegate() {
            return (SortedSet) super.delegate();
        }

        @Override // java.util.SortedSet
        public Comparator comparator() {
            Comparator comparator;
            synchronized (this.mutex) {
                comparator = delegate().comparator();
            }
            return comparator;
        }

        @Override // java.util.SortedSet
        public Object first() {
            Object objFirst;
            synchronized (this.mutex) {
                objFirst = delegate().first();
            }
            return objFirst;
        }

        @Override // java.util.SortedSet
        public Object last() {
            Object objLast;
            synchronized (this.mutex) {
                objLast = delegate().last();
            }
            return objLast;
        }
    }

    static Map map(Map map, Object obj) {
        return new SynchronizedMap(map, obj);
    }

    class SynchronizedMap extends SynchronizedObject implements Map {
        private static final long serialVersionUID = 0;
        transient Set entrySet;
        transient Set keySet;
        transient Collection values;

        SynchronizedMap(Map map, Object obj) {
            super(map, obj);
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedObject
        Map delegate() {
            return (Map) super.delegate();
        }

        @Override // java.util.Map
        public void clear() {
            synchronized (this.mutex) {
                delegate().clear();
            }
        }

        @Override // java.util.Map
        public boolean containsKey(Object obj) {
            boolean zContainsKey;
            synchronized (this.mutex) {
                zContainsKey = delegate().containsKey(obj);
            }
            return zContainsKey;
        }

        @Override // java.util.Map
        public boolean containsValue(Object obj) {
            boolean zContainsValue;
            synchronized (this.mutex) {
                zContainsValue = delegate().containsValue(obj);
            }
            return zContainsValue;
        }

        @Override // java.util.Map
        public Set entrySet() {
            Set set;
            synchronized (this.mutex) {
                try {
                    if (this.entrySet == null) {
                        this.entrySet = Synchronized.set(delegate().entrySet(), this.mutex);
                    }
                    set = this.entrySet;
                } catch (Throwable th) {
                    throw th;
                }
            }
            return set;
        }

        @Override // java.util.Map
        public Object get(Object obj) {
            Object obj2;
            synchronized (this.mutex) {
                obj2 = delegate().get(obj);
            }
            return obj2;
        }

        @Override // java.util.Map
        public boolean isEmpty() {
            boolean zIsEmpty;
            synchronized (this.mutex) {
                zIsEmpty = delegate().isEmpty();
            }
            return zIsEmpty;
        }

        @Override // java.util.Map
        public Set keySet() {
            Set set;
            synchronized (this.mutex) {
                try {
                    if (this.keySet == null) {
                        this.keySet = Synchronized.set(delegate().keySet(), this.mutex);
                    }
                    set = this.keySet;
                } catch (Throwable th) {
                    throw th;
                }
            }
            return set;
        }

        @Override // java.util.Map
        public Object put(Object obj, Object obj2) {
            Object objPut;
            synchronized (this.mutex) {
                objPut = delegate().put(obj, obj2);
            }
            return objPut;
        }

        @Override // java.util.Map
        public void putAll(Map map) {
            synchronized (this.mutex) {
                delegate().putAll(map);
            }
        }

        @Override // java.util.Map
        public Object remove(Object obj) {
            Object objRemove;
            synchronized (this.mutex) {
                objRemove = delegate().remove(obj);
            }
            return objRemove;
        }

        @Override // java.util.Map
        public int size() {
            int size;
            synchronized (this.mutex) {
                size = delegate().size();
            }
            return size;
        }

        @Override // java.util.Map
        public Collection values() {
            Collection collection;
            synchronized (this.mutex) {
                try {
                    if (this.values == null) {
                        this.values = Synchronized.collection(delegate().values(), this.mutex);
                    }
                    collection = this.values;
                } catch (Throwable th) {
                    throw th;
                }
            }
            return collection;
        }

        @Override // java.util.Map
        public boolean equals(Object obj) {
            boolean zEquals;
            if (obj == this) {
                return true;
            }
            synchronized (this.mutex) {
                zEquals = delegate().equals(obj);
            }
            return zEquals;
        }

        @Override // java.util.Map
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = delegate().hashCode();
            }
            return iHashCode;
        }
    }

    abstract class SynchronizedSortedMap extends SynchronizedMap implements SortedMap {
        private static final long serialVersionUID = 0;

        SynchronizedSortedMap(SortedMap sortedMap, Object obj) {
            super(sortedMap, obj);
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedMap, com.google.common.collect.Synchronized.SynchronizedObject
        SortedMap delegate() {
            return (SortedMap) super.delegate();
        }

        @Override // java.util.SortedMap
        public Comparator comparator() {
            Comparator comparator;
            synchronized (this.mutex) {
                comparator = delegate().comparator();
            }
            return comparator;
        }

        @Override // java.util.SortedMap
        public Object firstKey() {
            Object objFirstKey;
            synchronized (this.mutex) {
                objFirstKey = delegate().firstKey();
            }
            return objFirstKey;
        }

        @Override // java.util.SortedMap
        public Object lastKey() {
            Object objLastKey;
            synchronized (this.mutex) {
                objLastKey = delegate().lastKey();
            }
            return objLastKey;
        }
    }

    final class SynchronizedNavigableSet extends SynchronizedSortedSet implements NavigableSet {
        private static final long serialVersionUID = 0;
        transient NavigableSet descendingSet;

        SynchronizedNavigableSet(NavigableSet navigableSet, Object obj) {
            super(navigableSet, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedSortedSet, com.google.common.collect.Synchronized.SynchronizedSet, com.google.common.collect.Synchronized.SynchronizedCollection, com.google.common.collect.Synchronized.SynchronizedObject
        public NavigableSet delegate() {
            return (NavigableSet) super.delegate();
        }

        @Override // java.util.NavigableSet
        public Object ceiling(Object obj) {
            Object objCeiling;
            synchronized (this.mutex) {
                objCeiling = delegate().ceiling(obj);
            }
            return objCeiling;
        }

        @Override // java.util.NavigableSet
        public Iterator descendingIterator() {
            return delegate().descendingIterator();
        }

        @Override // java.util.NavigableSet
        public NavigableSet descendingSet() {
            synchronized (this.mutex) {
                try {
                    NavigableSet navigableSet = this.descendingSet;
                    if (navigableSet != null) {
                        return navigableSet;
                    }
                    NavigableSet navigableSet2 = Synchronized.navigableSet(delegate().descendingSet(), this.mutex);
                    this.descendingSet = navigableSet2;
                    return navigableSet2;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // java.util.NavigableSet
        public Object floor(Object obj) {
            Object objFloor;
            synchronized (this.mutex) {
                objFloor = delegate().floor(obj);
            }
            return objFloor;
        }

        @Override // java.util.NavigableSet
        public NavigableSet headSet(Object obj, boolean z) {
            NavigableSet navigableSet;
            synchronized (this.mutex) {
                navigableSet = Synchronized.navigableSet(delegate().headSet(obj, z), this.mutex);
            }
            return navigableSet;
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public SortedSet headSet(Object obj) {
            return headSet(obj, false);
        }

        @Override // java.util.NavigableSet
        public Object higher(Object obj) {
            Object objHigher;
            synchronized (this.mutex) {
                objHigher = delegate().higher(obj);
            }
            return objHigher;
        }

        @Override // java.util.NavigableSet
        public Object lower(Object obj) {
            Object objLower;
            synchronized (this.mutex) {
                objLower = delegate().lower(obj);
            }
            return objLower;
        }

        @Override // java.util.NavigableSet
        public Object pollFirst() {
            Object objPollFirst;
            synchronized (this.mutex) {
                objPollFirst = delegate().pollFirst();
            }
            return objPollFirst;
        }

        @Override // java.util.NavigableSet
        public Object pollLast() {
            Object objPollLast;
            synchronized (this.mutex) {
                objPollLast = delegate().pollLast();
            }
            return objPollLast;
        }

        @Override // java.util.NavigableSet
        public NavigableSet subSet(Object obj, boolean z, Object obj2, boolean z2) {
            NavigableSet navigableSet;
            synchronized (this.mutex) {
                navigableSet = Synchronized.navigableSet(delegate().subSet(obj, z, obj2, z2), this.mutex);
            }
            return navigableSet;
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public SortedSet subSet(Object obj, Object obj2) {
            return subSet(obj, true, obj2, false);
        }

        @Override // java.util.NavigableSet
        public NavigableSet tailSet(Object obj, boolean z) {
            NavigableSet navigableSet;
            synchronized (this.mutex) {
                navigableSet = Synchronized.navigableSet(delegate().tailSet(obj, z), this.mutex);
            }
            return navigableSet;
        }

        @Override // java.util.SortedSet, java.util.NavigableSet
        public SortedSet tailSet(Object obj) {
            return tailSet(obj, true);
        }
    }

    static NavigableSet navigableSet(NavigableSet navigableSet, Object obj) {
        return new SynchronizedNavigableSet(navigableSet, obj);
    }

    static NavigableMap navigableMap(NavigableMap navigableMap, Object obj) {
        return new SynchronizedNavigableMap(navigableMap, obj);
    }

    final class SynchronizedNavigableMap extends SynchronizedSortedMap implements NavigableMap {
        private static final long serialVersionUID = 0;
        transient NavigableSet descendingKeySet;
        transient NavigableMap descendingMap;
        transient NavigableSet navigableKeySet;

        SynchronizedNavigableMap(NavigableMap navigableMap, Object obj) {
            super(navigableMap, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // com.google.common.collect.Synchronized.SynchronizedSortedMap, com.google.common.collect.Synchronized.SynchronizedMap, com.google.common.collect.Synchronized.SynchronizedObject
        public NavigableMap delegate() {
            return (NavigableMap) super.delegate();
        }

        @Override // java.util.NavigableMap
        public Map.Entry ceilingEntry(Object obj) {
            Map.Entry entryNullableSynchronizedEntry;
            synchronized (this.mutex) {
                entryNullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(delegate().ceilingEntry(obj), this.mutex);
            }
            return entryNullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Object ceilingKey(Object obj) {
            Object objCeilingKey;
            synchronized (this.mutex) {
                objCeilingKey = delegate().ceilingKey(obj);
            }
            return objCeilingKey;
        }

        @Override // java.util.NavigableMap
        public NavigableSet descendingKeySet() {
            synchronized (this.mutex) {
                try {
                    NavigableSet navigableSet = this.descendingKeySet;
                    if (navigableSet != null) {
                        return navigableSet;
                    }
                    NavigableSet navigableSet2 = Synchronized.navigableSet(delegate().descendingKeySet(), this.mutex);
                    this.descendingKeySet = navigableSet2;
                    return navigableSet2;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // java.util.NavigableMap
        public NavigableMap descendingMap() {
            synchronized (this.mutex) {
                try {
                    NavigableMap navigableMap = this.descendingMap;
                    if (navigableMap != null) {
                        return navigableMap;
                    }
                    NavigableMap navigableMap2 = Synchronized.navigableMap(delegate().descendingMap(), this.mutex);
                    this.descendingMap = navigableMap2;
                    return navigableMap2;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // java.util.NavigableMap
        public Map.Entry firstEntry() {
            Map.Entry entryNullableSynchronizedEntry;
            synchronized (this.mutex) {
                entryNullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(delegate().firstEntry(), this.mutex);
            }
            return entryNullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry floorEntry(Object obj) {
            Map.Entry entryNullableSynchronizedEntry;
            synchronized (this.mutex) {
                entryNullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(delegate().floorEntry(obj), this.mutex);
            }
            return entryNullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Object floorKey(Object obj) {
            Object objFloorKey;
            synchronized (this.mutex) {
                objFloorKey = delegate().floorKey(obj);
            }
            return objFloorKey;
        }

        @Override // java.util.NavigableMap
        public NavigableMap headMap(Object obj, boolean z) {
            NavigableMap navigableMap;
            synchronized (this.mutex) {
                navigableMap = Synchronized.navigableMap(delegate().headMap(obj, z), this.mutex);
            }
            return navigableMap;
        }

        @Override // java.util.SortedMap, java.util.NavigableMap
        public SortedMap headMap(Object obj) {
            return headMap(obj, false);
        }

        @Override // java.util.NavigableMap
        public Map.Entry higherEntry(Object obj) {
            Map.Entry entryNullableSynchronizedEntry;
            synchronized (this.mutex) {
                entryNullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(delegate().higherEntry(obj), this.mutex);
            }
            return entryNullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Object higherKey(Object obj) {
            Object objHigherKey;
            synchronized (this.mutex) {
                objHigherKey = delegate().higherKey(obj);
            }
            return objHigherKey;
        }

        @Override // java.util.NavigableMap
        public Map.Entry lastEntry() {
            Map.Entry entryNullableSynchronizedEntry;
            synchronized (this.mutex) {
                entryNullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(delegate().lastEntry(), this.mutex);
            }
            return entryNullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry lowerEntry(Object obj) {
            Map.Entry entryNullableSynchronizedEntry;
            synchronized (this.mutex) {
                entryNullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(delegate().lowerEntry(obj), this.mutex);
            }
            return entryNullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Object lowerKey(Object obj) {
            Object objLowerKey;
            synchronized (this.mutex) {
                objLowerKey = delegate().lowerKey(obj);
            }
            return objLowerKey;
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedMap, java.util.Map
        public Set keySet() {
            return navigableKeySet();
        }

        @Override // java.util.NavigableMap
        public NavigableSet navigableKeySet() {
            synchronized (this.mutex) {
                try {
                    NavigableSet navigableSet = this.navigableKeySet;
                    if (navigableSet != null) {
                        return navigableSet;
                    }
                    NavigableSet navigableSet2 = Synchronized.navigableSet(delegate().navigableKeySet(), this.mutex);
                    this.navigableKeySet = navigableSet2;
                    return navigableSet2;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override // java.util.NavigableMap
        public Map.Entry pollFirstEntry() {
            Map.Entry entryNullableSynchronizedEntry;
            synchronized (this.mutex) {
                entryNullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(delegate().pollFirstEntry(), this.mutex);
            }
            return entryNullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public Map.Entry pollLastEntry() {
            Map.Entry entryNullableSynchronizedEntry;
            synchronized (this.mutex) {
                entryNullableSynchronizedEntry = Synchronized.nullableSynchronizedEntry(delegate().pollLastEntry(), this.mutex);
            }
            return entryNullableSynchronizedEntry;
        }

        @Override // java.util.NavigableMap
        public NavigableMap subMap(Object obj, boolean z, Object obj2, boolean z2) {
            NavigableMap navigableMap;
            synchronized (this.mutex) {
                navigableMap = Synchronized.navigableMap(delegate().subMap(obj, z, obj2, z2), this.mutex);
            }
            return navigableMap;
        }

        @Override // java.util.SortedMap, java.util.NavigableMap
        public SortedMap subMap(Object obj, Object obj2) {
            return subMap(obj, true, obj2, false);
        }

        @Override // java.util.NavigableMap
        public NavigableMap tailMap(Object obj, boolean z) {
            NavigableMap navigableMap;
            synchronized (this.mutex) {
                navigableMap = Synchronized.navigableMap(delegate().tailMap(obj, z), this.mutex);
            }
            return navigableMap;
        }

        @Override // java.util.SortedMap, java.util.NavigableMap
        public SortedMap tailMap(Object obj) {
            return tailMap(obj, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Map.Entry nullableSynchronizedEntry(Map.Entry entry, Object obj) {
        if (entry == null) {
            return null;
        }
        return new SynchronizedEntry(entry, obj);
    }

    final class SynchronizedEntry extends SynchronizedObject implements Map.Entry {
        private static final long serialVersionUID = 0;

        SynchronizedEntry(Map.Entry entry, Object obj) {
            super(entry, obj);
        }

        @Override // com.google.common.collect.Synchronized.SynchronizedObject
        Map.Entry delegate() {
            return (Map.Entry) super.delegate();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            boolean zEquals;
            synchronized (this.mutex) {
                zEquals = delegate().equals(obj);
            }
            return zEquals;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            int iHashCode;
            synchronized (this.mutex) {
                iHashCode = delegate().hashCode();
            }
            return iHashCode;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            Object key;
            synchronized (this.mutex) {
                key = delegate().getKey();
            }
            return key;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            Object value;
            synchronized (this.mutex) {
                value = delegate().getValue();
            }
            return value;
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object obj) {
            Object value;
            synchronized (this.mutex) {
                value = delegate().setValue(obj);
            }
            return value;
        }
    }
}
