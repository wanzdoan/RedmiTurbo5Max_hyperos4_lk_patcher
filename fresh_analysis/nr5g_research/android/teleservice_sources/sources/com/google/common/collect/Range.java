package com.google.common.collect;

import ai.onnxruntime.OrtUtil$$ExternalSyntheticBUOutline4;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.Immutable;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
@Immutable(containerOf = {"C"})
public final class Range extends RangeGwtSerializationDependencies implements Serializable {
    private static final Range ALL = new Range(Cut.belowAll(), Cut.aboveAll());
    private static final long serialVersionUID = 0;
    final Cut lowerBound;
    final Cut upperBound;

    static Range create(Cut cut, Cut cut2) {
        return new Range(cut, cut2);
    }

    public static Range range(Comparable comparable, BoundType boundType, Comparable comparable2, BoundType boundType2) {
        boundType.getClass();
        boundType2.getClass();
        BoundType boundType3 = BoundType.OPEN;
        return create(boundType == boundType3 ? Cut.aboveValue(comparable) : Cut.belowValue(comparable), boundType2 == boundType3 ? Cut.belowValue(comparable2) : Cut.aboveValue(comparable2));
    }

    public static Range lessThan(Comparable comparable) {
        return create(Cut.belowAll(), Cut.belowValue(comparable));
    }

    public static Range atMost(Comparable comparable) {
        return create(Cut.belowAll(), Cut.aboveValue(comparable));
    }

    /* JADX INFO: renamed from: com.google.common.collect.Range$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$common$collect$BoundType;

        static {
            int[] iArr = new int[BoundType.values().length];
            $SwitchMap$com$google$common$collect$BoundType = iArr;
            try {
                iArr[BoundType.OPEN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$common$collect$BoundType[BoundType.CLOSED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public static Range upTo(Comparable comparable, BoundType boundType) {
        int i = AnonymousClass1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()];
        if (i == 1) {
            return lessThan(comparable);
        }
        if (i == 2) {
            return atMost(comparable);
        }
        throw new AssertionError();
    }

    public static Range greaterThan(Comparable comparable) {
        return create(Cut.aboveValue(comparable), Cut.aboveAll());
    }

    public static Range atLeast(Comparable comparable) {
        return create(Cut.belowValue(comparable), Cut.aboveAll());
    }

    public static Range downTo(Comparable comparable, BoundType boundType) {
        int i = AnonymousClass1.$SwitchMap$com$google$common$collect$BoundType[boundType.ordinal()];
        if (i == 1) {
            return greaterThan(comparable);
        }
        if (i == 2) {
            return atLeast(comparable);
        }
        throw new AssertionError();
    }

    public static Range all() {
        return ALL;
    }

    private Range(Cut cut, Cut cut2) {
        cut.getClass();
        this.lowerBound = cut;
        cut2.getClass();
        this.upperBound = cut2;
        if (cut.compareTo(cut2) > 0 || cut == Cut.aboveAll() || cut2 == Cut.belowAll()) {
            OrtUtil$$ExternalSyntheticBUOutline4.m("Invalid range: ", toString(cut, cut2));
            throw null;
        }
    }

    public boolean hasLowerBound() {
        return this.lowerBound != Cut.belowAll();
    }

    public Comparable lowerEndpoint() {
        return this.lowerBound.endpoint();
    }

    public boolean hasUpperBound() {
        return this.upperBound != Cut.aboveAll();
    }

    public Comparable upperEndpoint() {
        return this.upperBound.endpoint();
    }

    public boolean contains(Comparable comparable) {
        comparable.getClass();
        return this.lowerBound.isLessThan(comparable) && !this.upperBound.isLessThan(comparable);
    }

    public boolean isConnected(Range range) {
        return this.lowerBound.compareTo(range.upperBound) <= 0 && range.lowerBound.compareTo(this.upperBound) <= 0;
    }

    public Range intersection(Range range) {
        int iCompareTo = this.lowerBound.compareTo(range.lowerBound);
        int iCompareTo2 = this.upperBound.compareTo(range.upperBound);
        if (iCompareTo >= 0 && iCompareTo2 <= 0) {
            return this;
        }
        if (iCompareTo <= 0 && iCompareTo2 >= 0) {
            return range;
        }
        Cut cut = iCompareTo >= 0 ? this.lowerBound : range.lowerBound;
        Cut cut2 = iCompareTo2 <= 0 ? this.upperBound : range.upperBound;
        Preconditions.checkArgument(cut.compareTo(cut2) <= 0, "intersection is undefined for disconnected ranges %s and %s", this, range);
        return create(cut, cut2);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Range) {
            Range range = (Range) obj;
            if (this.lowerBound.equals(range.lowerBound) && this.upperBound.equals(range.upperBound)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        return (this.lowerBound.hashCode() * 31) + this.upperBound.hashCode();
    }

    public String toString() {
        return toString(this.lowerBound, this.upperBound);
    }

    private static String toString(Cut cut, Cut cut2) {
        StringBuilder sb = new StringBuilder(16);
        cut.describeAsLowerBound(sb);
        sb.append("..");
        cut2.describeAsUpperBound(sb);
        return sb.toString();
    }

    Object readResolve() {
        return equals(ALL) ? all() : this;
    }

    static int compareOrThrow(Comparable comparable, Comparable comparable2) {
        return comparable.compareTo(comparable2);
    }
}
