package androidx.collection;

import androidx.collection.internal.ContainerHelpersKt;
import androidx.collection.internal.RuntimeHelpersKt;
import kotlin.ULong;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: OrderedScatterSet.kt */
/* JADX INFO: loaded from: classes.dex */
public final class MutableOrderedScatterSet extends OrderedScatterSet {
    private int growthLimit;

    public MutableOrderedScatterSet(int i) {
        super(null);
        if (!(i >= 0)) {
            RuntimeHelpersKt.throwIllegalArgumentException("Capacity must be a positive value.");
        }
        initializeStorage(ScatterMapKt.unloadedCapacity(i));
    }

    public /* synthetic */ MutableOrderedScatterSet(int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this((i2 & 1) != 0 ? 6 : i);
    }

    private final int findAbsoluteInsertIndex(Object obj) {
        int iHashCode = (obj != null ? obj.hashCode() : 0) * (-862048943);
        int i = iHashCode ^ (iHashCode << 16);
        int i2 = i >>> 7;
        int i3 = i & 127;
        int i4 = this._capacity;
        int i5 = i2 & i4;
        int i6 = 0;
        while (true) {
            long[] jArr = this.metadata;
            int i7 = i5 >> 3;
            int i8 = (i5 & 7) << 3;
            long j = ((jArr[i7 + 1] << (64 - i8)) & ((-i8) >> 63)) | (jArr[i7] >>> i8);
            long j2 = i3;
            int i9 = i3;
            long j3 = j ^ (j2 * 72340172838076673L);
            for (long j4 = (~j3) & (j3 - 72340172838076673L) & (-9187201950435737472L); j4 != 0; j4 &= j4 - 1) {
                int iNumberOfTrailingZeros = (i5 + (Long.numberOfTrailingZeros(j4) >> 3)) & i4;
                if (Intrinsics.areEqual(this.elements[iNumberOfTrailingZeros], obj)) {
                    return iNumberOfTrailingZeros;
                }
            }
            if ((((~j) << 6) & j & (-9187201950435737472L)) != 0) {
                int iFindFirstAvailableSlot = findFirstAvailableSlot(i2);
                if (this.growthLimit == 0 && ((this.metadata[iFindFirstAvailableSlot >> 3] >> ((iFindFirstAvailableSlot & 7) << 3)) & 255) != 254) {
                    adjustStorage$collection();
                    iFindFirstAvailableSlot = findFirstAvailableSlot(i2);
                }
                this._size++;
                int i10 = this.growthLimit;
                long[] jArr2 = this.metadata;
                int i11 = iFindFirstAvailableSlot >> 3;
                long j5 = jArr2[i11];
                int i12 = (iFindFirstAvailableSlot & 7) << 3;
                this.growthLimit = i10 - (((j5 >> i12) & 255) == 128 ? 1 : 0);
                int i13 = this._capacity;
                long j6 = ((~(255 << i12)) & j5) | (j2 << i12);
                jArr2[i11] = j6;
                jArr2[(((iFindFirstAvailableSlot - 7) & i13) + (i13 & 7)) >> 3] = j6;
                return iFindFirstAvailableSlot;
            }
            i6 += 8;
            i5 = (i5 + i6) & i4;
            i3 = i9;
        }
    }

    public final boolean remove(Object obj) {
        int iNumberOfTrailingZeros;
        int iHashCode = (obj != null ? obj.hashCode() : 0) * (-862048943);
        int i = iHashCode ^ (iHashCode << 16);
        int i2 = i & 127;
        int i3 = this._capacity;
        int i4 = (i >>> 7) & i3;
        int i5 = 0;
        loop0: while (true) {
            long[] jArr = this.metadata;
            int i6 = i4 >> 3;
            int i7 = (i4 & 7) << 3;
            long j = ((jArr[i6 + 1] << (64 - i7)) & ((-i7) >> 63)) | (jArr[i6] >>> i7);
            long j2 = (((long) i2) * 72340172838076673L) ^ j;
            for (long j3 = (~j2) & (j2 - 72340172838076673L) & (-9187201950435737472L); j3 != 0; j3 &= j3 - 1) {
                iNumberOfTrailingZeros = ((Long.numberOfTrailingZeros(j3) >> 3) + i4) & i3;
                if (Intrinsics.areEqual(this.elements[iNumberOfTrailingZeros], obj)) {
                    break loop0;
                }
            }
            if ((j & ((~j) << 6) & (-9187201950435737472L)) != 0) {
                iNumberOfTrailingZeros = -1;
                break;
            }
            i5 += 8;
            i4 = (i4 + i5) & i3;
        }
        boolean z = iNumberOfTrailingZeros >= 0;
        if (z) {
            removeElementAt(iNumberOfTrailingZeros);
        }
        return z;
    }

    private final void initializeStorage(int i) {
        long[] emptyNodes;
        int iMax = i > 0 ? Math.max(7, ScatterMapKt.normalizeCapacity(i)) : 0;
        this._capacity = iMax;
        initializeMetadata(iMax);
        this.elements = iMax == 0 ? ContainerHelpersKt.EMPTY_OBJECTS : new Object[iMax];
        if (iMax == 0) {
            emptyNodes = SieveCacheKt.getEmptyNodes();
        } else {
            long[] jArr = new long[iMax];
            ArraysKt.fill$default(jArr, 4611686018427387903L, 0, 0, 6, null);
            emptyNodes = jArr;
        }
        this.nodes = emptyNodes;
    }

    private final void initializeMetadata(int i) {
        long[] jArr;
        if (i == 0) {
            jArr = ScatterMapKt.EmptyGroup;
        } else {
            long[] jArr2 = new long[((i + 15) & (-8)) >> 3];
            ArraysKt.fill$default(jArr2, -9187201950435737472L, 0, 0, 6, null);
            jArr = jArr2;
        }
        this.metadata = jArr;
        int i2 = i >> 3;
        long j = 255 << ((i & 7) << 3);
        jArr[i2] = (jArr[i2] & (~j)) | j;
        initializeGrowth();
    }

    private final void initializeGrowth() {
        this.growthLimit = ScatterMapKt.loadedCapacity(getCapacity()) - this._size;
    }

    public final boolean add(Object obj) {
        int size = getSize();
        int iFindAbsoluteInsertIndex = findAbsoluteInsertIndex(obj);
        this.elements[iFindAbsoluteInsertIndex] = obj;
        long[] jArr = this.nodes;
        int i = this.head;
        jArr[iFindAbsoluteInsertIndex] = (((long) i) & 2147483647L) | 4611686016279904256L;
        if (i != Integer.MAX_VALUE) {
            jArr[i] = ((((long) iFindAbsoluteInsertIndex) & 2147483647L) << 31) | (jArr[i] & (-4611686016279904257L));
        }
        this.head = iFindAbsoluteInsertIndex;
        if (this.tail == Integer.MAX_VALUE) {
            this.tail = iFindAbsoluteInsertIndex;
        }
        return getSize() != size;
    }

    public final void plusAssign(Object obj) {
        int iFindAbsoluteInsertIndex = findAbsoluteInsertIndex(obj);
        this.elements[iFindAbsoluteInsertIndex] = obj;
        long[] jArr = this.nodes;
        int i = this.head;
        jArr[iFindAbsoluteInsertIndex] = (((long) i) & 2147483647L) | 4611686016279904256L;
        if (i != Integer.MAX_VALUE) {
            jArr[i] = ((((long) iFindAbsoluteInsertIndex) & 2147483647L) << 31) | (jArr[i] & (-4611686016279904257L));
        }
        this.head = iFindAbsoluteInsertIndex;
        if (this.tail == Integer.MAX_VALUE) {
            this.tail = iFindAbsoluteInsertIndex;
        }
    }

    public final void removeElementAt(int i) {
        this._size--;
        long[] jArr = this.metadata;
        int i2 = this._capacity;
        int i3 = i >> 3;
        int i4 = (i & 7) << 3;
        long j = (jArr[i3] & (~(255 << i4))) | (254 << i4);
        jArr[i3] = j;
        jArr[(((i - 7) & i2) + (i2 & 7)) >> 3] = j;
        this.elements[i] = null;
        long[] jArr2 = this.nodes;
        long j2 = jArr2[i];
        int i5 = (int) ((j2 >> 31) & 2147483647L);
        int i6 = (int) (j2 & 2147483647L);
        if (i5 == Integer.MAX_VALUE) {
            this.head = i6;
        } else {
            jArr2[i5] = (jArr2[i5] & (-2147483648L)) | (((long) i6) & 2147483647L);
        }
        if (i6 == Integer.MAX_VALUE) {
            this.tail = i5;
        } else {
            jArr2[i6] = ((((long) i5) & 2147483647L) << 31) | (jArr2[i6] & (-4611686016279904257L));
        }
        jArr2[i] = 4611686018427387903L;
    }

    private final int findFirstAvailableSlot(int i) {
        int i2 = this._capacity;
        int i3 = i & i2;
        int i4 = 0;
        while (true) {
            long[] jArr = this.metadata;
            int i5 = i3 >> 3;
            int i6 = (i3 & 7) << 3;
            long j = ((jArr[i5 + 1] << (64 - i6)) & ((-i6) >> 63)) | (jArr[i5] >>> i6);
            long j2 = j & ((~j) << 7) & (-9187201950435737472L);
            if (j2 != 0) {
                return (i3 + (Long.numberOfTrailingZeros(j2) >> 3)) & i2;
            }
            i4 += 8;
            i3 = (i3 + i4) & i2;
        }
    }

    public final void adjustStorage$collection() {
        if (this._capacity > 8 && Long.compareUnsigned(ULong.m5424constructorimpl(ULong.m5424constructorimpl(this._size) * 32), ULong.m5424constructorimpl(ULong.m5424constructorimpl(this._capacity) * 25)) <= 0) {
            dropDeletes$collection();
        } else {
            resizeStorage$collection(ScatterMapKt.nextCapacity(this._capacity));
        }
    }

    public final void dropDeletes$collection() {
        int i;
        long[] jArr = this.metadata;
        if (jArr == null) {
            return;
        }
        int i2 = this._capacity;
        Object[] objArr = this.elements;
        long[] jArr2 = this.nodes;
        long[] jArr3 = new long[i2];
        long j = 9223372034707292159L;
        int i3 = 0;
        ArraysKt.fill(jArr3, 9223372034707292159L, 0, i2);
        int i4 = (i2 + 7) >> 3;
        for (int i5 = 0; i5 < i4; i5++) {
            long j2 = jArr[i5] & (-9187201950435737472L);
            jArr[i5] = (-72340172838076674L) & ((~j2) + (j2 >>> 7));
        }
        int lastIndex = ArraysKt.getLastIndex(jArr);
        int i6 = lastIndex - 1;
        jArr[i6] = (jArr[i6] & 72057594037927935L) | (-72057594037927936L);
        jArr[lastIndex] = jArr[0];
        int i7 = 0;
        while (i7 != i2) {
            int i8 = i7 >> 3;
            int i9 = (i7 & 7) << 3;
            long j3 = (jArr[i8] >> i9) & 255;
            if (j3 != 128 && j3 == 254) {
                Object obj = objArr[i7];
                int iHashCode = (obj != null ? obj.hashCode() : i3) * (-862048943);
                int i10 = iHashCode ^ (iHashCode << 16);
                int i11 = i10 >>> 7;
                long j4 = j;
                int iFindFirstAvailableSlot = findFirstAvailableSlot(i11);
                int i12 = i11 & i2;
                if (((iFindFirstAvailableSlot - i12) & i2) / 8 == ((i7 - i12) & i2) / 8) {
                    jArr[i8] = (((long) (i10 & 127)) << i9) | (jArr[i8] & (~(255 << i9)));
                    if (jArr3[i7] == j4) {
                        long j5 = i7;
                        jArr3[i7] = j5 | (j5 << 32);
                    }
                    jArr[jArr.length - 1] = jArr[i3];
                    i7++;
                    j = j4;
                } else {
                    int i13 = iFindFirstAvailableSlot >> 3;
                    long j6 = jArr[i13];
                    int i14 = (iFindFirstAvailableSlot & 7) << 3;
                    int i15 = i3;
                    if (((j6 >> i14) & 255) == 128) {
                        int i16 = i7;
                        jArr[i13] = (j6 & (~(255 << i14))) | (((long) (i10 & 127)) << i14);
                        jArr[i8] = (jArr[i8] & (~(255 << i9))) | (128 << i9);
                        objArr[iFindFirstAvailableSlot] = objArr[i16];
                        objArr[i16] = null;
                        jArr2[iFindFirstAvailableSlot] = jArr2[i16];
                        jArr2[i16] = 4611686018427387903L;
                        int i17 = (int) ((jArr3[i16] >> 32) & 4294967295L);
                        if (i17 != Integer.MAX_VALUE) {
                            jArr3[i17] = (jArr3[i17] & (-4294967296L)) | ((long) iFindFirstAvailableSlot);
                            jArr3[i16] = (jArr3[i16] & 4294967295L) | (-4294967296L);
                        } else {
                            jArr3[i16] = 9223372032559808512L | ((long) iFindFirstAvailableSlot);
                        }
                        i = i16;
                        jArr3[iFindFirstAvailableSlot] = (((long) i) << 32) | 2147483647L;
                    } else {
                        jArr[i13] = (((long) (i10 & 127)) << i14) | (j6 & (~(255 << i14)));
                        Object obj2 = objArr[iFindFirstAvailableSlot];
                        objArr[iFindFirstAvailableSlot] = objArr[i7];
                        objArr[i7] = obj2;
                        long j7 = jArr2[iFindFirstAvailableSlot];
                        jArr2[iFindFirstAvailableSlot] = jArr2[i7];
                        jArr2[i7] = j7;
                        int i18 = (int) ((jArr3[i7] >> 32) & 4294967295L);
                        if (i18 != Integer.MAX_VALUE) {
                            long j8 = iFindFirstAvailableSlot;
                            jArr3[i18] = (jArr3[i18] & (-4294967296L)) | j8;
                            jArr3[i7] = (jArr3[i7] & 4294967295L) | (j8 << 32);
                        } else {
                            long j9 = iFindFirstAvailableSlot;
                            jArr3[i7] = j9 | (j9 << 32);
                            i18 = i7;
                        }
                        jArr3[iFindFirstAvailableSlot] = (((long) i18) << 32) | ((long) i7);
                        i = i7 - 1;
                    }
                    jArr[jArr.length - 1] = jArr[i15];
                    i7 = i + 1;
                    j = j4;
                    i3 = i15;
                }
            } else {
                i7++;
            }
        }
        initializeGrowth();
        fixupNodes(jArr3);
    }

    public final void resizeStorage$collection(int i) {
        long[] jArr = this.metadata;
        Object[] objArr = this.elements;
        long[] jArr2 = this.nodes;
        int i2 = this._capacity;
        int[] iArr = new int[i2];
        initializeStorage(i);
        long[] jArr3 = this.metadata;
        Object[] objArr2 = this.elements;
        long[] jArr4 = this.nodes;
        int i3 = this._capacity;
        int i4 = 0;
        while (i4 < i2) {
            if (((jArr[i4 >> 3] >> ((i4 & 7) << 3)) & 255) < 128) {
                Object obj = objArr[i4];
                int iHashCode = (obj != null ? obj.hashCode() : 0) * (-862048943);
                int i5 = iHashCode ^ (iHashCode << 16);
                int iFindFirstAvailableSlot = findFirstAvailableSlot(i5 >>> 7);
                long j = i5 & 127;
                int i6 = iFindFirstAvailableSlot >> 3;
                int i7 = (iFindFirstAvailableSlot & 7) << 3;
                long j2 = (jArr3[i6] & (~(255 << i7))) | (j << i7);
                jArr3[i6] = j2;
                jArr3[(((iFindFirstAvailableSlot - 7) & i3) + (i3 & 7)) >> 3] = j2;
                objArr2[iFindFirstAvailableSlot] = obj;
                jArr4[iFindFirstAvailableSlot] = jArr2[i4];
                iArr[i4] = iFindFirstAvailableSlot;
            }
            i4++;
            jArr = jArr;
            objArr = objArr;
        }
        fixupNodes(iArr);
    }

    private final void fixupNodes(long[] jArr) {
        long[] jArr2 = this.nodes;
        int length = jArr2.length;
        int i = 0;
        while (true) {
            int i2 = Integer.MAX_VALUE;
            if (i >= length) {
                break;
            }
            long j = jArr2[i];
            int i3 = (int) ((j >> 31) & 2147483647L);
            int i4 = (int) (j & 2147483647L);
            long j2 = ((j & (-4611686018427387904L)) | ((long) (i3 == Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) (jArr[i3] & 4294967295L)))) << 31;
            if (i4 != Integer.MAX_VALUE) {
                i2 = (int) (4294967295L & jArr[i4]);
            }
            jArr2[i] = ((long) i2) | j2;
            i++;
        }
        int i5 = this.head;
        if (i5 != Integer.MAX_VALUE) {
            this.head = (int) (jArr[i5] & 4294967295L);
        }
        int i6 = this.tail;
        if (i6 != Integer.MAX_VALUE) {
            this.tail = (int) (jArr[i6] & 4294967295L);
        }
    }

    private final void fixupNodes(int[] iArr) {
        long[] jArr = this.nodes;
        int length = jArr.length;
        int i = 0;
        while (true) {
            int i2 = Integer.MAX_VALUE;
            if (i >= length) {
                break;
            }
            long j = jArr[i];
            int i3 = (int) ((j >> 31) & 2147483647L);
            int i4 = (int) (j & 2147483647L);
            long j2 = ((j & (-4611686018427387904L)) | ((long) (i3 == Integer.MAX_VALUE ? Integer.MAX_VALUE : iArr[i3]))) << 31;
            if (i4 != Integer.MAX_VALUE) {
                i2 = iArr[i4];
            }
            jArr[i] = j2 | ((long) i2);
            i++;
        }
        int i5 = this.head;
        if (i5 != Integer.MAX_VALUE) {
            this.head = iArr[i5];
        }
        int i6 = this.tail;
        if (i6 != Integer.MAX_VALUE) {
            this.tail = iArr[i6];
        }
    }
}
