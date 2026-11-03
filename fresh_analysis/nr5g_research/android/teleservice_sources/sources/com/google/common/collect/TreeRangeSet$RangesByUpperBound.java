package com.google.common.collect;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;

/* JADX INFO: loaded from: classes.dex */
final class TreeRangeSet$RangesByUpperBound extends AbstractNavigableMap {
    private final NavigableMap rangesByLowerBound;
    private final Range upperBoundWindow;

    private TreeRangeSet$RangesByUpperBound(NavigableMap navigableMap, Range range) {
        this.rangesByLowerBound = navigableMap;
        this.upperBoundWindow = range;
    }

    private NavigableMap subMap(Range range) {
        if (range.isConnected(this.upperBoundWindow)) {
            return new TreeRangeSet$RangesByUpperBound(this.rangesByLowerBound, range.intersection(this.upperBoundWindow));
        }
        return ImmutableSortedMap.of();
    }

    @Override // java.util.NavigableMap
    public NavigableMap subMap(Cut cut, boolean z, Cut cut2, boolean z2) {
        return subMap(Range.range(cut, BoundType.forBoolean(z), cut2, BoundType.forBoolean(z2)));
    }

    @Override // java.util.NavigableMap
    public NavigableMap headMap(Cut cut, boolean z) {
        return subMap(Range.upTo(cut, BoundType.forBoolean(z)));
    }

    @Override // java.util.NavigableMap
    public NavigableMap tailMap(Cut cut, boolean z) {
        return subMap(Range.downTo(cut, BoundType.forBoolean(z)));
    }

    @Override // java.util.SortedMap
    public Comparator comparator() {
        return Ordering.natural();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        return get(obj) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Range get(Object obj) {
        Map.Entry entryLowerEntry;
        if (obj instanceof Cut) {
            try {
                Cut cut = (Cut) obj;
                if (this.upperBoundWindow.contains(cut) && (entryLowerEntry = this.rangesByLowerBound.lowerEntry(cut)) != null && ((Range) entryLowerEntry.getValue()).upperBound.equals(cut)) {
                    return (Range) entryLowerEntry.getValue();
                }
            } catch (ClassCastException unused) {
            }
        }
        return null;
    }

    @Override // com.google.common.collect.Maps.IteratorBasedAbstractMap
    Iterator entryIterator() {
        final Iterator it;
        boolean zHasLowerBound = this.upperBoundWindow.hasLowerBound();
        NavigableMap navigableMap = this.rangesByLowerBound;
        if (!zHasLowerBound) {
            it = navigableMap.values().iterator();
        } else {
            Map.Entry entryLowerEntry = navigableMap.lowerEntry((Cut) this.upperBoundWindow.lowerEndpoint());
            if (entryLowerEntry == null) {
                it = this.rangesByLowerBound.values().iterator();
            } else {
                boolean zIsLessThan = this.upperBoundWindow.lowerBound.isLessThan(((Range) entryLowerEntry.getValue()).upperBound);
                NavigableMap navigableMap2 = this.rangesByLowerBound;
                if (zIsLessThan) {
                    it = navigableMap2.tailMap((Cut) entryLowerEntry.getKey(), true).values().iterator();
                } else {
                    it = navigableMap2.tailMap((Cut) this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
                }
            }
        }
        return new AbstractIterator() { // from class: com.google.common.collect.TreeRangeSet$RangesByUpperBound.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.AbstractIterator
            public Map.Entry computeNext() {
                if (!it.hasNext()) {
                    return (Map.Entry) endOfData();
                }
                Range range = (Range) it.next();
                if (TreeRangeSet$RangesByUpperBound.this.upperBoundWindow.upperBound.isLessThan(range.upperBound)) {
                    return (Map.Entry) endOfData();
                }
                return Maps.immutableEntry(range.upperBound, range);
            }
        };
    }

    @Override // com.google.common.collect.AbstractNavigableMap
    Iterator descendingEntryIterator() {
        Collection collectionValues;
        boolean zHasUpperBound = this.upperBoundWindow.hasUpperBound();
        NavigableMap navigableMap = this.rangesByLowerBound;
        if (zHasUpperBound) {
            collectionValues = navigableMap.headMap((Cut) this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
        } else {
            collectionValues = navigableMap.descendingMap().values();
        }
        final PeekingIterator peekingIterator = Iterators.peekingIterator(collectionValues.iterator());
        if (peekingIterator.hasNext() && this.upperBoundWindow.upperBound.isLessThan(((Range) peekingIterator.peek()).upperBound)) {
            peekingIterator.next();
        }
        return new AbstractIterator() { // from class: com.google.common.collect.TreeRangeSet$RangesByUpperBound.2
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.common.collect.AbstractIterator
            public Map.Entry computeNext() {
                if (!peekingIterator.hasNext()) {
                    return (Map.Entry) endOfData();
                }
                Range range = (Range) peekingIterator.next();
                if (TreeRangeSet$RangesByUpperBound.this.upperBoundWindow.lowerBound.isLessThan(range.upperBound)) {
                    return Maps.immutableEntry(range.upperBound, range);
                }
                return (Map.Entry) endOfData();
            }
        };
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        if (this.upperBoundWindow.equals(Range.all())) {
            return this.rangesByLowerBound.size();
        }
        return Iterators.size(entryIterator());
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean isEmpty() {
        if (this.upperBoundWindow.equals(Range.all())) {
            return this.rangesByLowerBound.isEmpty();
        }
        return !entryIterator().hasNext();
    }
}
