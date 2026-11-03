package com.android.internal.telephony.cat;

import com.android.internal.telephony.HyperTelephonyFactory;
import com.android.internal.telephony.IHyperCatManager;
import com.android.telephony.Rlog;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ComprehensionTlv {
    private boolean mCr;
    private int mLength;
    private byte[] mRawValue;
    private int mTag;
    private int mValueIndex;

    public ComprehensionTlv(int i, boolean z, int i2, byte[] bArr, int i3) {
        this.mTag = i;
        this.mCr = z;
        this.mLength = i2;
        this.mValueIndex = i3;
        this.mRawValue = bArr;
    }

    public int getTag() {
        return this.mTag;
    }

    public boolean isComprehensionRequired() {
        return this.mCr;
    }

    public int getLength() {
        return this.mLength;
    }

    public int getValueIndex() {
        return this.mValueIndex;
    }

    public byte[] getRawValue() {
        return this.mRawValue;
    }

    public static List<ComprehensionTlv> decodeMany(byte[] bArr, int i) throws ResultException {
        ArrayList arrayList = new ArrayList();
        int length = bArr.length;
        while (i < length) {
            ComprehensionTlv comprehensionTlvDecode = decode(bArr, i);
            if (comprehensionTlvDecode != null) {
                arrayList.add(comprehensionTlvDecode);
                i = comprehensionTlvDecode.mLength + comprehensionTlvDecode.mValueIndex;
            } else {
                CatLog.d("ComprehensionTlv", "decodeMany: ctlv is null, stop decoding");
                break;
            }
        }
        return arrayList;
    }

    /* JADX WARN: Code duplicated, block: B:32:0x008a  */
    /* JADX WARN: Code duplicated, block: B:34:0x0090  */
    /* JADX WARN: Code duplicated, block: B:38:0x009e A[Catch: IndexOutOfBoundsException -> 0x01ba, TryCatch #4 {IndexOutOfBoundsException -> 0x01ba, blocks: (B:3:0x0015, B:8:0x0022, B:9:0x0025, B:12:0x002b, B:35:0x0092, B:38:0x009e, B:39:0x00c7, B:46:0x00e1, B:47:0x010a, B:61:0x013e, B:62:0x0167, B:13:0x002e, B:17:0x0036, B:18:0x004b, B:20:0x0053, B:22:0x0061, B:65:0x018e), top: B:79:0x0015, inners: #3 }] */
    /* JADX WARN: Code duplicated, block: B:40:0x00c8  */
    /* JADX WARN: Code duplicated, block: B:42:0x00cc A[Catch: IndexOutOfBoundsException -> 0x010b, TRY_ENTER, TRY_LEAVE, TryCatch #0 {IndexOutOfBoundsException -> 0x010b, blocks: (B:28:0x007e, B:42:0x00cc, B:51:0x0112, B:63:0x0168, B:64:0x018d), top: B:72:0x007e }] */
    /* JADX WARN: Code duplicated, block: B:46:0x00e1 A[Catch: IndexOutOfBoundsException -> 0x01ba, TRY_ENTER, TryCatch #4 {IndexOutOfBoundsException -> 0x01ba, blocks: (B:3:0x0015, B:8:0x0022, B:9:0x0025, B:12:0x002b, B:35:0x0092, B:38:0x009e, B:39:0x00c7, B:46:0x00e1, B:47:0x010a, B:61:0x013e, B:62:0x0167, B:13:0x002e, B:17:0x0036, B:18:0x004b, B:20:0x0053, B:22:0x0061, B:65:0x018e), top: B:79:0x0015, inners: #3 }] */
    /* JADX WARN: Code duplicated, block: B:49:0x010e  */
    /* JADX WARN: Code duplicated, block: B:51:0x0112 A[Catch: IndexOutOfBoundsException -> 0x010b, TRY_ENTER, TRY_LEAVE, TryCatch #0 {IndexOutOfBoundsException -> 0x010b, blocks: (B:28:0x007e, B:42:0x00cc, B:51:0x0112, B:63:0x0168, B:64:0x018d), top: B:72:0x007e }] */
    /* JADX WARN: Code duplicated, block: B:61:0x013e A[Catch: IndexOutOfBoundsException -> 0x01ba, TRY_ENTER, TryCatch #4 {IndexOutOfBoundsException -> 0x01ba, blocks: (B:3:0x0015, B:8:0x0022, B:9:0x0025, B:12:0x002b, B:35:0x0092, B:38:0x009e, B:39:0x00c7, B:46:0x00e1, B:47:0x010a, B:61:0x013e, B:62:0x0167, B:13:0x002e, B:17:0x0036, B:18:0x004b, B:20:0x0053, B:22:0x0061, B:65:0x018e), top: B:79:0x0015, inners: #3 }] */
    /* JADX WARN: Code duplicated, block: B:63:0x0168 A[Catch: IndexOutOfBoundsException -> 0x010b, TRY_ENTER, TryCatch #0 {IndexOutOfBoundsException -> 0x010b, blocks: (B:28:0x007e, B:42:0x00cc, B:51:0x0112, B:63:0x0168, B:64:0x018d), top: B:72:0x007e }] */
    /* JADX WARN: Instruction removed from duplicated block: B:38:0x009e, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:46:0x00e1, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:61:0x013e, please report this as an issue */
    /* JADX WARN: Instruction removed from duplicated block: B:63:0x0168, please report this as an issue */
    public static ComprehensionTlv decode(byte[] bArr, int i) throws ResultException {
        boolean z;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int length = bArr.length;
        int i7 = i + 1;
        try {
            byte b = bArr[i];
            int i8 = b & 255;
            if (i8 != 0 && i8 != 255) {
                switch (i8) {
                    case 127:
                        int i9 = ((bArr[i7] & 255) << 8) | (bArr[i + 2] & 255);
                        z = (32768 & i9) != 0;
                        i2 = i9 & (-32769);
                        i7 = i + 3;
                        i3 = i7 + 1;
                        try {
                            i4 = bArr[i7] & 255;
                            if (i4 >= 128) {
                                if (i4 == 129) {
                                    i5 = i7 + 2;
                                    i4 = bArr[i3] & 255;
                                    if (i4 < 128) {
                                        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "length < 0x80 length=" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                    }
                                } else if (i4 == 130) {
                                    i4 = ((bArr[i3] & 255) << 8) | (255 & bArr[i7 + 2]);
                                    i5 = i7 + 3;
                                    if (i4 < 256) {
                                        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "two byte length < 0x100 length=" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                    }
                                } else {
                                    if (i4 != 131) {
                                        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "Bad length modifer=" + i4 + " startIndex=" + i + " curIndex=" + i3 + " endIndex=" + length);
                                    }
                                    i4 = ((bArr[i3] & 255) << 16) | ((bArr[i7 + 2] & 255) << 8) | (255 & bArr[i7 + 3]);
                                    i5 = i7 + 4;
                                    if (i4 < 65536) {
                                        throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "three byte length < 0x10000 length=0x" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                    }
                                }
                                i3 = i5;
                            }
                            try {
                                int i10 = i2;
                                i6 = i3;
                                try {
                                    return new ComprehensionTlv(i10, z, i4, bArr, i6);
                                } catch (IndexOutOfBoundsException unused) {
                                    i7 = i6;
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "IndexOutOfBoundsException startIndex=" + i + " curIndex=" + i7 + " endIndex=" + length);
                                }
                            } catch (IndexOutOfBoundsException unused2) {
                                i6 = i3;
                            }
                        } catch (IndexOutOfBoundsException unused3) {
                            i7 = i3;
                        }
                        break;
                    case 128:
                        break;
                    case 129:
                        boolean z2 = (b & 128) != 0;
                        int i11 = b & 127;
                        CommandDetails commandDetails = new CommandDetails();
                        IHyperCatManager iHyperCatManager = (IHyperCatManager) HyperTelephonyFactory.getInstance().initModule(IHyperCatManager.DEFAULT, new Object[0]);
                        try {
                            iHyperCatManager.setCommandDetails(iHyperCatManager.retrieveCommandDetails(bArr, i7, z2, commandDetails));
                            break;
                        } catch (ResultException unused4) {
                            CatLog.d("ComprehensionTlv", "retrieveCommandDetails fail");
                            iHyperCatManager.setCommandDetails(null);
                        }
                        z = z2;
                        i2 = i11;
                        i3 = i7 + 1;
                        i4 = bArr[i7] & 255;
                        if (i4 >= 128) {
                            if (i4 == 129) {
                                i5 = i7 + 2;
                                i4 = bArr[i3] & 255;
                                if (i4 < 128) {
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "length < 0x80 length=" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                }
                            } else if (i4 == 130) {
                                i4 = ((bArr[i3] & 255) << 8) | (255 & bArr[i7 + 2]);
                                i5 = i7 + 3;
                                if (i4 < 256) {
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "two byte length < 0x100 length=" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                }
                            } else {
                                if (i4 != 131) {
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "Bad length modifer=" + i4 + " startIndex=" + i + " curIndex=" + i3 + " endIndex=" + length);
                                }
                                i4 = ((bArr[i3] & 255) << 16) | ((bArr[i7 + 2] & 255) << 8) | (255 & bArr[i7 + 3]);
                                i5 = i7 + 4;
                                if (i4 < 65536) {
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "three byte length < 0x10000 length=0x" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                }
                            }
                            i3 = i5;
                        }
                        int i12 = i2;
                        i6 = i3;
                        return new ComprehensionTlv(i12, z, i4, bArr, i6);
                    default:
                        z = (b & 128) != 0;
                        i2 = b & 127;
                        i3 = i7 + 1;
                        i4 = bArr[i7] & 255;
                        if (i4 >= 128) {
                            if (i4 == 129) {
                                i5 = i7 + 2;
                                i4 = bArr[i3] & 255;
                                if (i4 < 128) {
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "length < 0x80 length=" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                }
                            } else if (i4 == 130) {
                                i4 = ((bArr[i3] & 255) << 8) | (255 & bArr[i7 + 2]);
                                i5 = i7 + 3;
                                if (i4 < 256) {
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "two byte length < 0x100 length=" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                }
                            } else {
                                if (i4 != 131) {
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "Bad length modifer=" + i4 + " startIndex=" + i + " curIndex=" + i3 + " endIndex=" + length);
                                }
                                i4 = ((bArr[i3] & 255) << 16) | ((bArr[i7 + 2] & 255) << 8) | (255 & bArr[i7 + 3]);
                                i5 = i7 + 4;
                                if (i4 < 65536) {
                                    throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "three byte length < 0x10000 length=0x" + Integer.toHexString(i4) + " startIndex=" + i + " curIndex=" + i5 + " endIndex=" + length);
                                }
                            }
                            i3 = i5;
                        }
                        int i13 = i2;
                        i6 = i3;
                        return new ComprehensionTlv(i13, z, i4, bArr, i6);
                }
            }
            Rlog.d("CAT     ", "decode: unexpected first tag byte=" + Integer.toHexString(i8) + ", startIndex=" + i + " curIndex=" + i7 + " endIndex=" + length);
            return null;
        } catch (IndexOutOfBoundsException unused5) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD, "IndexOutOfBoundsException startIndex=" + i + " curIndex=" + i7 + " endIndex=" + length);
        }
    }
}
