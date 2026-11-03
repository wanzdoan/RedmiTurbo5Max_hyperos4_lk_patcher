package com.android.internal.telephony.cat;

import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BerTlv {
    public static final int BER_EVENT_DOWNLOAD_TAG = 214;
    public static final int BER_MENU_SELECTION_TAG = 211;
    public static final int BER_PROACTIVE_COMMAND_TAG = 208;
    public static final int BER_UNKNOWN_TAG = 0;
    private List mCompTlvs;
    private boolean mLengthValid;
    private int mTag;

    private BerTlv(int i, List list, boolean z) {
        this.mTag = i;
        this.mCompTlvs = list;
        this.mLengthValid = z;
    }

    public List<ComprehensionTlv> getComprehensionTlvs() {
        return this.mCompTlvs;
    }

    public int getTag() {
        return this.mTag;
    }

    public boolean isLengthValid() {
        return this.mLengthValid;
    }

    /* JADX WARN: Code duplicated, block: B:46:0x00bc A[PHI: r5
  0x00bc: PHI (r5v2 int) = (r5v0 int), (r5v3 int) binds: [B:31:0x0092, B:44:0x00b9] A[DONT_GENERATE, DONT_INLINE]] */
    /* JADX WARN: Multi-variable type inference failed */
    public static BerTlv decode(byte[] bArr) throws ResultException {
        int i;
        int i2;
        boolean z;
        int i3;
        int length = bArr.length;
        int i4 = 1;
        try {
            try {
                byte b = bArr[0];
                int i5 = b & 255;
                if (i5 == 208) {
                    i2 = 2;
                    try {
                        i = bArr[1] & 255;
                        if (i >= 128) {
                            if (i == 129) {
                                try {
                                    i = bArr[2] & 255;
                                    if (i < 128) {
                                        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "length < 0x80 length=" + Integer.toHexString(0) + " curIndex=3 endIndex=" + length);
                                    }
                                    i2 = 3;
                                } catch (IndexOutOfBoundsException unused) {
                                    i4 = 3;
                                }
                            } else {
                                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "Expected first byte to be length or a length tag and < 0x81 byte= " + Integer.toHexString(i) + " curIndex=2 endIndex=" + length);
                            }
                            throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING, "IndexOutOfBoundsException  curIndex=" + i4 + " endIndex=" + length);
                        }
                    } catch (IndexOutOfBoundsException unused2) {
                        i4 = 2;
                    }
                } else if (ComprehensionTlvTag.COMMAND_DETAILS.value() == (b & 127)) {
                    i2 = 0;
                    i5 = 0;
                    i = 0;
                } else {
                    i = 0;
                    i2 = 1;
                }
                if (length - i2 < i) {
                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "Command had extra data endIndex=" + length + " curIndex=" + i2 + " length=" + i);
                }
                List<ComprehensionTlv> listDecodeMany = ComprehensionTlv.decodeMany(bArr, i2);
                if (i5 == 208) {
                    Iterator<ComprehensionTlv> it = listDecodeMany.iterator();
                    int i6 = 0;
                    while (it.hasNext()) {
                        int length2 = it.next().getLength();
                        if (length2 >= 128 && length2 <= 255) {
                            i3 = length2 + 3;
                        } else {
                            if (length2 < 0 || length2 >= 128) {
                                i4 = 0;
                                break;
                            }
                            i3 = length2 + 2;
                        }
                        i6 += i3;
                    }
                    z = i == i6 ? i4 : 0;
                }
                return new BerTlv(i5, listDecodeMany, z);
            } catch (IndexOutOfBoundsException unused3) {
            }
        } catch (ResultException e) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, e.explanation());
        }
    }
}
