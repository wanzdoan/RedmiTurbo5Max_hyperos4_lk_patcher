package com.google.common.collect;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* JADX INFO: loaded from: classes.dex */
public abstract class LinkedHashMultimap extends LinkedHashMultimapGwtSerializationDependencies {
    static final double VALUE_SET_LOAD_FACTOR = 1.0d;
    private static final long serialVersionUID = 1;

    interface ValueSetLink {
        ValueSetLink getPredecessorInValueSet();

        ValueSetLink getSuccessorInValueSet();

        void setPredecessorInValueSet(ValueSetLink valueSetLink);

        void setSuccessorInValueSet(ValueSetLink valueSetLink);
    }

    static /* synthetic */ ValueEntry access$300(LinkedHashMultimap linkedHashMultimap) {
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void succeedsInValueSet(ValueSetLink valueSetLink, ValueSetLink valueSetLink2) {
        valueSetLink.setSuccessorInValueSet(valueSetLink2);
        valueSetLink2.setPredecessorInValueSet(valueSetLink);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void succeedsInMultimap(ValueEntry valueEntry, ValueEntry valueEntry2) {
        valueEntry.setSuccessorInMultimap(valueEntry2);
        valueEntry2.setPredecessorInMultimap(valueEntry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void deleteFromValueSet(ValueSetLink valueSetLink) {
        succeedsInValueSet(valueSetLink.getPredecessorInValueSet(), valueSetLink.getSuccessorInValueSet());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void deleteFromMultimap(ValueEntry valueEntry) {
        succeedsInMultimap(valueEntry.getPredecessorInMultimap(), valueEntry.getSuccessorInMultimap());
    }

    final class ValueEntry extends ImmutableEntry implements ValueSetLink {
        ValueEntry nextInValueBucket;
        private ValueEntry predecessorInMultimap;
        private ValueSetLink predecessorInValueSet;
        final int smearedValueHash;
        private ValueEntry successorInMultimap;
        private ValueSetLink successorInValueSet;

        ValueEntry(Object obj, Object obj2, int i, ValueEntry valueEntry) {
            super(obj, obj2);
            this.smearedValueHash = i;
            this.nextInValueBucket = valueEntry;
        }

        boolean matchesValue(Object obj, int i) {
            return this.smearedValueHash == i && Objects.equal(getValue(), obj);
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public ValueSetLink getPredecessorInValueSet() {
            ValueSetLink valueSetLink = this.predecessorInValueSet;
            java.util.Objects.requireNonNull(valueSetLink);
            return valueSetLink;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public ValueSetLink getSuccessorInValueSet() {
            ValueSetLink valueSetLink = this.successorInValueSet;
            java.util.Objects.requireNonNull(valueSetLink);
            return valueSetLink;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public void setPredecessorInValueSet(ValueSetLink valueSetLink) {
            this.predecessorInValueSet = valueSetLink;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public void setSuccessorInValueSet(ValueSetLink valueSetLink) {
            this.successorInValueSet = valueSetLink;
        }

        public ValueEntry getPredecessorInMultimap() {
            ValueEntry valueEntry = this.predecessorInMultimap;
            java.util.Objects.requireNonNull(valueEntry);
            return valueEntry;
        }

        public ValueEntry getSuccessorInMultimap() {
            ValueEntry valueEntry = this.successorInMultimap;
            java.util.Objects.requireNonNull(valueEntry);
            return valueEntry;
        }

        public void setSuccessorInMultimap(ValueEntry valueEntry) {
            this.successorInMultimap = valueEntry;
        }

        public void setPredecessorInMultimap(ValueEntry valueEntry) {
            this.predecessorInMultimap = valueEntry;
        }
    }

    final class ValueSet extends Sets.ImprovedAbstractSet implements ValueSetLink {
        private ValueSetLink firstEntry;
        ValueEntry[] hashTable;
        private final Object key;
        private ValueSetLink lastEntry;
        private int modCount;
        private int size;

        private int mask() {
            return this.hashTable.length - 1;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public ValueSetLink getPredecessorInValueSet() {
            return this.lastEntry;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public ValueSetLink getSuccessorInValueSet() {
            return this.firstEntry;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public void setPredecessorInValueSet(ValueSetLink valueSetLink) {
            this.lastEntry = valueSetLink;
        }

        @Override // com.google.common.collect.LinkedHashMultimap.ValueSetLink
        public void setSuccessorInValueSet(ValueSetLink valueSetLink) {
            this.firstEntry = valueSetLink;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.Set
        public Iterator iterator() {
            return new Iterator() { // from class: com.google.common.collect.LinkedHashMultimap.ValueSet.1
                int expectedModCount;
                ValueSetLink nextEntry;
                ValueEntry toRemove;

                {
                    this.nextEntry = ValueSet.this.firstEntry;
                    this.expectedModCount = ValueSet.this.modCount;
                }

                private void checkForComodification() {
                    if (ValueSet.this.modCount != this.expectedModCount) {
                        throw new ConcurrentModificationException();
                    }
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    checkForComodification();
                    return this.nextEntry != ValueSet.this;
                }

                @Override // java.util.Iterator
                public Object next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    ValueEntry valueEntry = (ValueEntry) this.nextEntry;
                    Object value = valueEntry.getValue();
                    this.toRemove = valueEntry;
                    this.nextEntry = valueEntry.getSuccessorInValueSet();
                    return value;
                }

                @Override // java.util.Iterator
                public void remove() {
                    checkForComodification();
                    Preconditions.checkState(this.toRemove != null, "no calls to next() since the last call to remove()");
                    ValueSet.this.remove(this.toRemove.getValue());
                    this.expectedModCount = ValueSet.this.modCount;
                    this.toRemove = null;
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.size;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            int iSmearedHash = Hashing.smearedHash(obj);
            for (ValueEntry valueEntry = this.hashTable[mask() & iSmearedHash]; valueEntry != null; valueEntry = valueEntry.nextInValueBucket) {
                if (valueEntry.matchesValue(obj, iSmearedHash)) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean add(Object obj) {
            int iSmearedHash = Hashing.smearedHash(obj);
            int iMask = mask() & iSmearedHash;
            ValueEntry valueEntry = this.hashTable[iMask];
            for (ValueEntry valueEntry2 = valueEntry; valueEntry2 != null; valueEntry2 = valueEntry2.nextInValueBucket) {
                if (valueEntry2.matchesValue(obj, iSmearedHash)) {
                    return false;
                }
            }
            ValueEntry valueEntry3 = new ValueEntry(this.key, obj, iSmearedHash, valueEntry);
            LinkedHashMultimap.succeedsInValueSet(this.lastEntry, valueEntry3);
            LinkedHashMultimap.succeedsInValueSet(valueEntry3, this);
            LinkedHashMultimap.succeedsInMultimap(LinkedHashMultimap.access$300(null).getPredecessorInMultimap(), valueEntry3);
            LinkedHashMultimap.succeedsInMultimap(valueEntry3, LinkedHashMultimap.access$300(null));
            this.hashTable[iMask] = valueEntry3;
            this.size++;
            this.modCount++;
            rehashIfNecessary();
            return true;
        }

        private void rehashIfNecessary() {
            if (Hashing.needsResizing(this.size, this.hashTable.length, 1.0d)) {
                int length = this.hashTable.length * 2;
                ValueEntry[] valueEntryArr = new ValueEntry[length];
                this.hashTable = valueEntryArr;
                int i = length - 1;
                for (ValueSetLink successorInValueSet = this.firstEntry; successorInValueSet != this; successorInValueSet = successorInValueSet.getSuccessorInValueSet()) {
                    ValueEntry valueEntry = (ValueEntry) successorInValueSet;
                    int i2 = valueEntry.smearedValueHash & i;
                    valueEntry.nextInValueBucket = valueEntryArr[i2];
                    valueEntryArr[i2] = valueEntry;
                }
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean remove(Object obj) {
            int iSmearedHash = Hashing.smearedHash(obj);
            int iMask = mask() & iSmearedHash;
            ValueEntry valueEntry = null;
            for (ValueEntry valueEntry2 = this.hashTable[iMask]; valueEntry2 != null; valueEntry2 = valueEntry2.nextInValueBucket) {
                if (valueEntry2.matchesValue(obj, iSmearedHash)) {
                    if (valueEntry == null) {
                        this.hashTable[iMask] = valueEntry2.nextInValueBucket;
                    } else {
                        valueEntry.nextInValueBucket = valueEntry2.nextInValueBucket;
                    }
                    LinkedHashMultimap.deleteFromValueSet(valueEntry2);
                    LinkedHashMultimap.deleteFromMultimap(valueEntry2);
                    this.size--;
                    this.modCount++;
                    return true;
                }
                valueEntry = valueEntry2;
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public void clear() {
            Arrays.fill(this.hashTable, (Object) null);
            this.size = 0;
            for (ValueSetLink successorInValueSet = this.firstEntry; successorInValueSet != this; successorInValueSet = successorInValueSet.getSuccessorInValueSet()) {
                LinkedHashMultimap.deleteFromMultimap((ValueEntry) successorInValueSet);
            }
            LinkedHashMultimap.succeedsInValueSet(this, this);
            this.modCount++;
        }
    }
}
