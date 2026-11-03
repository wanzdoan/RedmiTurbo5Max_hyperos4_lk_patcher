package com.google.common.geometry;

import com.android.services.telephony.relay.network.SmartNetworkRelayScore;

/* JADX INFO: loaded from: classes.dex */
public final class S2CellId implements Comparable {
    private static final long[] maxValueDivs;
    private static final int[] maxValueMods;
    private final long id;
    private static final int[] LOOKUP_POS = new int[SmartNetworkRelayScore.TRAFFIC_UNIT];
    private static final int[] LOOKUP_IJ = new int[SmartNetworkRelayScore.TRAFFIC_UNIT];

    public static long lowestOnBitForLevel(int i) {
        return 1 << ((30 - i) * 2);
    }

    public static boolean unsignedLongGreaterThan(long j, long j2) {
        return j + Long.MIN_VALUE > j2 + Long.MIN_VALUE;
    }

    public static boolean unsignedLongLessThan(long j, long j2) {
        return j + Long.MIN_VALUE < j2 + Long.MIN_VALUE;
    }

    static {
        initLookupCell(0, 0, 0, 0, 0, 0);
        initLookupCell(0, 0, 0, 1, 0, 1);
        initLookupCell(0, 0, 0, 2, 0, 2);
        initLookupCell(0, 0, 0, 3, 0, 3);
        maxValueDivs = new long[]{0, 0, Long.MAX_VALUE, 6148914691236517205L, 4611686018427387903L, 3689348814741910323L, 3074457345618258602L, 2635249153387078802L, 2305843009213693951L, 2049638230412172401L, 1844674407370955161L, 1676976733973595601L, 1537228672809129301L, 1418980313362273201L, 1317624576693539401L, 1229782938247303441L, 1152921504606846975L, 1085102592571150095L, 1024819115206086200L, 970881267037344821L, 922337203685477580L, 878416384462359600L, 838488366986797800L, 802032351030850070L, 768614336404564650L, 737869762948382064L, 709490156681136600L, 683212743470724133L, 658812288346769700L, 636094623231363848L, 614891469123651720L, 595056260442243600L, 576460752303423487L, 558992244657865200L, 542551296285575047L, 527049830677415760L, 512409557603043100L};
        maxValueMods = new int[]{0, 0, 1, 0, 3, 0, 3, 1, 7, 6, 5, 4, 3, 2, 1, 0, 15, 0, 15, 16, 15, 15, 15, 5, 15, 15, 15, 24, 15, 23, 15, 15, 31, 15, 17, 15, 15};
    }

    public S2CellId(long j) {
        this.id = j;
    }

    public static S2CellId fromPoint(S2Point s2Point) {
        int iXyzToFace = S2Projections.xyzToFace(s2Point);
        R2Vector r2VectorValidFaceXyzToUv = S2Projections.validFaceXyzToUv(iXyzToFace, s2Point);
        return fromFaceIJ(iXyzToFace, stToIJ(S2Projections.uvToST(r2VectorValidFaceXyzToUv.x())), stToIJ(S2Projections.uvToST(r2VectorValidFaceXyzToUv.y())));
    }

    public static S2CellId fromLatLng(S2LatLng s2LatLng) {
        return fromPoint(s2LatLng.toPoint());
    }

    public long id() {
        return this.id;
    }

    public int face() {
        return (int) (this.id >>> 61);
    }

    public long pos() {
        return this.id & 2305843009213693951L;
    }

    public int level() {
        int i;
        if (isLeaf()) {
            return 30;
        }
        long j = this.id;
        int i2 = (int) j;
        if (i2 != 0) {
            i = 15;
        } else {
            i2 = (int) (j >>> 32);
            i = -1;
        }
        int i3 = i2 & (-i2);
        if ((i3 & 21845) != 0) {
            i += 8;
        }
        if ((5570645 & i3) != 0) {
            i += 4;
        }
        if ((84215045 & i3) != 0) {
            i += 2;
        }
        return (i3 & 286331153) != 0 ? i + 1 : i;
    }

    public boolean isLeaf() {
        return (((int) this.id) & 1) != 0;
    }

    public S2CellId parent(int i) {
        long jLowestOnBitForLevel = lowestOnBitForLevel(i);
        return new S2CellId(jLowestOnBitForLevel | (this.id & (-jLowestOnBitForLevel)));
    }

    public static S2CellId fromFaceIJ(int i, int i2, int i3) {
        long[] jArr = {0, i << 28};
        int bits = i & 1;
        for (int i4 = 7; i4 >= 0; i4--) {
            bits = getBits(jArr, i2, i3, i4, bits);
        }
        return new S2CellId((((jArr[1] << 32) + jArr[0]) << 1) + 1);
    }

    private static int getBits(long[] jArr, int i, int i2, int i3, int i4) {
        int i5 = i3 * 4;
        int i6 = LOOKUP_POS[i4 + (((i >> i5) & 15) << 6) + (((i2 >> i5) & 15) << 2)];
        int i7 = i3 >> 2;
        jArr[i7] = ((((long) i6) >> 2) << ((i3 & 3) * 8)) | jArr[i7];
        return i6 & 3;
    }

    private static int stToIJ(double d) {
        return (int) Math.max(0L, Math.min(1073741823L, Math.round((d * 5.36870912E8d) + 5.368709115E8d)));
    }

    public boolean equals(Object obj) {
        return (obj instanceof S2CellId) && id() == ((S2CellId) obj).id();
    }

    public int hashCode() {
        long j = this.id;
        return (int) ((j >>> 32) + j);
    }

    public String toString() {
        return "(face=" + face() + ", pos=" + Long.toHexString(pos()) + ", level=" + level() + ")";
    }

    private static void initLookupCell(int i, int i2, int i3, int i4, int i5, int i6) {
        if (i == 4) {
            int i7 = ((i2 << 4) + i3) << 2;
            int i8 = i5 << 2;
            LOOKUP_POS[i7 + i4] = i8 + i6;
            LOOKUP_IJ[i8 + i4] = i7 + i6;
            return;
        }
        int i9 = i + 1;
        int i10 = i2 << 1;
        int i11 = i3 << 1;
        int i12 = i5 << 2;
        for (int i13 = 0; i13 < 4; i13++) {
            int iPosToIJ = S2.posToIJ(i6, i13);
            initLookupCell(i9, (iPosToIJ >>> 1) + i10, (iPosToIJ & 1) + i11, i4, i12 + i13, i6 ^ S2.posToOrientation(i13));
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(S2CellId s2CellId) {
        if (unsignedLongLessThan(this.id, s2CellId.id)) {
            return -1;
        }
        return unsignedLongGreaterThan(this.id, s2CellId.id) ? 1 : 0;
    }
}
