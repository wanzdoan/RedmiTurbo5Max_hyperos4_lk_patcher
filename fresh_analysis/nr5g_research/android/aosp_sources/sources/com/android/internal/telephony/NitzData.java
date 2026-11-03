package com.android.internal.telephony;

import com.android.telephony.Rlog;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public final class NitzData {
    private static final Pattern NITZ_SPLIT_PATTERN = Pattern.compile("[/:,+-]");
    private final long mCurrentTimeMillis;
    private final Integer mDstOffset;
    private final TimeZone mEmulatorHostTimeZone;
    private final String mOriginalString;
    private final int mZoneOffset;

    private NitzData(String str, int i, Integer num, long j, TimeZone timeZone) {
        if (str == null) {
            throw new NullPointerException("originalString==null");
        }
        this.mOriginalString = str;
        this.mZoneOffset = i;
        this.mDstOffset = num;
        this.mCurrentTimeMillis = j;
        this.mEmulatorHostTimeZone = timeZone;
    }

    public static NitzData parse(String str) {
        try {
            String[] strArrSplit = NITZ_SPLIT_PATTERN.split(str);
            int i = Integer.parseInt(strArrSplit[0]);
            int i2 = 1;
            try {
                if (i < 1 || i > 99) {
                    throw new DateTimeException("Invalid NITZ year == 0");
                }
                long epochMilli = LocalDateTime.of(i + 2000, Integer.parseInt(strArrSplit[1]), Integer.parseInt(strArrSplit[2]), Integer.parseInt(strArrSplit[3]), Integer.parseInt(strArrSplit[4]), Integer.parseInt(strArrSplit[5])).toInstant(ZoneOffset.UTC).toEpochMilli();
                boolean z = str.indexOf(45) == -1;
                int i3 = Integer.parseInt(strArrSplit[6]);
                if (!z) {
                    i2 = -1;
                }
                int i4 = i2 * i3 * 900000;
                Integer numValueOf = strArrSplit.length >= 8 ? Integer.valueOf(Integer.parseInt(strArrSplit[7])) : null;
                return new NitzData(str, i4, numValueOf != null ? Integer.valueOf(numValueOf.intValue() * 3600000) : null, epochMilli, strArrSplit.length >= 9 ? TimeZone.getTimeZone(strArrSplit[8].replace('!', '/')) : null);
            } catch (RuntimeException e) {
                e = e;
            }
        } catch (RuntimeException e2) {
            e = e2;
        }
        Rlog.e("SST", "NITZ: Parsing NITZ time " + str + " ex=" + e);
        return null;
    }

    public static NitzData createForTests(int i, Integer num, long j, TimeZone timeZone) {
        return new NitzData("Test data", i, num, j, timeZone);
    }

    public long getCurrentTimeInMillis() {
        return this.mCurrentTimeMillis;
    }

    public int getLocalOffsetMillis() {
        return this.mZoneOffset;
    }

    public Integer getDstAdjustmentMillis() {
        return this.mDstOffset;
    }

    public boolean isDst() {
        Integer num = this.mDstOffset;
        return (num == null || num.intValue() == 0) ? false : true;
    }

    public TimeZone getEmulatorHostTimeZone() {
        return this.mEmulatorHostTimeZone;
    }

    public String getOriginalString() {
        return this.mOriginalString;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || NitzData.class != obj.getClass()) {
            return false;
        }
        NitzData nitzData = (NitzData) obj;
        if (this.mZoneOffset == nitzData.mZoneOffset && this.mCurrentTimeMillis == nitzData.mCurrentTimeMillis && this.mOriginalString.equals(nitzData.mOriginalString) && Objects.equals(this.mDstOffset, nitzData.mDstOffset)) {
            return Objects.equals(this.mEmulatorHostTimeZone, nitzData.mEmulatorHostTimeZone);
        }
        return false;
    }

    public int hashCode() {
        int iHashCode = ((this.mOriginalString.hashCode() * 31) + this.mZoneOffset) * 31;
        Integer num = this.mDstOffset;
        int iHashCode2 = (((iHashCode + (num != null ? num.hashCode() : 0)) * 31) + Long.hashCode(this.mCurrentTimeMillis)) * 31;
        TimeZone timeZone = this.mEmulatorHostTimeZone;
        return iHashCode2 + (timeZone != null ? timeZone.hashCode() : 0);
    }

    public String toString() {
        return "NitzData{mOriginalString=" + this.mOriginalString + ", mZoneOffset=" + this.mZoneOffset + ", mDstOffset=" + this.mDstOffset + ", mCurrentTimeMillis=" + this.mCurrentTimeMillis + ", mEmulatorHostTimeZone=" + this.mEmulatorHostTimeZone + '}';
    }
}
