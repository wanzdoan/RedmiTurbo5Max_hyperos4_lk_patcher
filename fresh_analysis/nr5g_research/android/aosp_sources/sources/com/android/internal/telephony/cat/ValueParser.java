package com.android.internal.telephony.cat;

import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.uicc.IccUtils;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class ValueParser {
    public static CommandDetails retrieveCommandDetails(ComprehensionTlv comprehensionTlv) throws ResultException {
        CommandDetails commandDetails = new CommandDetails();
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        try {
            commandDetails.compRequired = comprehensionTlv.isComprehensionRequired();
            commandDetails.commandNumber = rawValue[valueIndex] & 255;
            commandDetails.typeOfCommand = rawValue[valueIndex + 1] & 255;
            commandDetails.commandQualifier = rawValue[valueIndex + 2] & 255;
            return commandDetails;
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    public static DeviceIdentities retrieveDeviceIdentities(ComprehensionTlv comprehensionTlv) throws ResultException {
        DeviceIdentities deviceIdentities = new DeviceIdentities();
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        try {
            deviceIdentities.sourceId = rawValue[valueIndex] & 255;
            deviceIdentities.destinationId = rawValue[valueIndex + 1] & 255;
            return deviceIdentities;
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.REQUIRED_VALUES_MISSING);
        }
    }

    public static Duration retrieveDuration(ComprehensionTlv comprehensionTlv) throws ResultException {
        Duration.TimeUnit timeUnit = Duration.TimeUnit.MINUTE;
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        try {
            return new Duration(rawValue[valueIndex + 1] & 255, Duration.TimeUnit.values()[rawValue[valueIndex] & 255]);
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    public static Item retrieveItem(ComprehensionTlv comprehensionTlv) throws ResultException {
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        int length = comprehensionTlv.getLength();
        if (length == 0) {
            return null;
        }
        try {
            return new Item(rawValue[valueIndex] & 255, IccUtils.adnStringFieldToString(rawValue, valueIndex + 1, length - 1));
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    public static int retrieveItemId(ComprehensionTlv comprehensionTlv) throws ResultException {
        try {
            return comprehensionTlv.getRawValue()[comprehensionTlv.getValueIndex()] & 255;
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    public static IconId retrieveIconId(ComprehensionTlv comprehensionTlv) throws ResultException {
        IconId iconId = new IconId();
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        int i = valueIndex + 1;
        try {
            iconId.selfExplanatory = (rawValue[valueIndex] & 255) == 0;
            iconId.recordNumber = rawValue[i] & 255;
            return iconId;
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    static ItemsIconId retrieveItemsIconId(ComprehensionTlv comprehensionTlv) throws ResultException {
        CatLog.d("ValueParser", "retrieveItemsIconId:");
        ItemsIconId itemsIconId = new ItemsIconId();
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        boolean z = true;
        int length = comprehensionTlv.getLength() - 1;
        try {
            itemsIconId.recordNumbers = new int[length];
            int i = valueIndex + 1;
            int i2 = 0;
            if ((rawValue[valueIndex] & 255) != 0) {
                z = false;
            }
            itemsIconId.selfExplanatory = z;
            while (i2 < length) {
                int i3 = i2 + 1;
                int i4 = i + 1;
                itemsIconId.recordNumbers[i2] = rawValue[i];
                i2 = i3;
                i = i4;
            }
            return itemsIconId;
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        } catch (NegativeArraySizeException unused2) {
            CatLog.d("ValueParser", "retrieveItemsIconId: numOfItems = -1");
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    public static List<TextAttribute> retrieveTextAttribute(ComprehensionTlv comprehensionTlv) throws ResultException {
        ArrayList arrayList = new ArrayList();
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        int length = comprehensionTlv.getLength();
        if (length == 0) {
            return null;
        }
        int i = length / 4;
        int i2 = 0;
        while (i2 < i) {
            try {
                int i3 = rawValue[valueIndex] & 255;
                int i4 = rawValue[valueIndex + 1] & 255;
                byte b = rawValue[valueIndex + 2];
                int i5 = rawValue[valueIndex + 3] & 255;
                TextAlignment textAlignmentFromInt = TextAlignment.fromInt(b & 3);
                FontSize fontSizeFromInt = FontSize.fromInt(((b & 255) >> 2) & 3);
                if (fontSizeFromInt == null) {
                    fontSizeFromInt = FontSize.NORMAL;
                }
                boolean z = true;
                boolean z2 = (b & 16) != 0;
                if ((b & 32) == 0) {
                    z = false;
                }
                if ((b & 64) == 0) {
                    z = false;
                }
                if ((b & 128) == 0) {
                    z = false;
                }
                arrayList.add(new TextAttribute(i3, i4, textAlignmentFromInt, fontSizeFromInt, z2, z, z, z, TextColor.fromInt(i5)));
                i2++;
                valueIndex += 4;
            } catch (IndexOutOfBoundsException unused) {
                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            }
        }
        return arrayList;
    }

    public static String retrieveAlphaId(ComprehensionTlv comprehensionTlv, boolean z) throws ResultException {
        if (comprehensionTlv == null) {
            if (z) {
                return null;
            }
            return CatService.STK_DEFAULT;
        }
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        int length = comprehensionTlv.getLength();
        if (length != 0) {
            try {
                return IccUtils.adnStringFieldToString(rawValue, valueIndex, length);
            } catch (IndexOutOfBoundsException unused) {
                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            }
        }
        CatLog.d("ValueParser", "Alpha Id length=" + length);
        return null;
    }

    public static byte retrieveTextCodingScheme(ComprehensionTlv comprehensionTlv) throws ResultException {
        try {
            byte b = (byte) (comprehensionTlv.getRawValue()[comprehensionTlv.getValueIndex()] & 12);
            if (b != 0 && b != 4 && b != 8) {
                throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
            }
            return b;
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    public static String retrieveTextString(ComprehensionTlv comprehensionTlv) throws ResultException {
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        int length = comprehensionTlv.getLength();
        if (length == 0) {
            return null;
        }
        int i = length - 1;
        try {
            byte b = (byte) (rawValue[valueIndex] & 12);
            if (b == 0) {
                return GsmAlphabet.gsm7BitPackedToString(rawValue, valueIndex + 1, (i * 8) / 7);
            }
            if (b == 4) {
                return GsmAlphabet.gsm8BitUnpackedToString(rawValue, valueIndex + 1, i);
            }
            if (b == 8) {
                return new String(rawValue, valueIndex + 1, i, "UTF-16");
            }
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        } catch (UnsupportedEncodingException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        } catch (IndexOutOfBoundsException unused2) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    public static SmsMessage retrieveTpduAsSmsMessage(ComprehensionTlv comprehensionTlv) throws ResultException {
        if (comprehensionTlv == null) {
            return null;
        }
        byte[] rawValue = comprehensionTlv.getRawValue();
        int valueIndex = comprehensionTlv.getValueIndex();
        int length = comprehensionTlv.getLength();
        if (length == 0) {
            return null;
        }
        try {
            byte[] bArrCopyOfRange = Arrays.copyOfRange(rawValue, valueIndex, length + valueIndex);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArrCopyOfRange.length + 1);
            byteArrayOutputStream.write(0);
            byteArrayOutputStream.write(bArrCopyOfRange, 0, bArrCopyOfRange.length);
            return SmsMessage.createFromPdu(byteArrayOutputStream.toByteArray(), "3gpp");
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }

    public static String retrieveAddress(ComprehensionTlv comprehensionTlv) throws ResultException {
        try {
            return PhoneNumberUtils.calledPartyBCDToString(comprehensionTlv.getRawValue(), comprehensionTlv.getValueIndex(), comprehensionTlv.getLength(), 1);
        } catch (IndexOutOfBoundsException unused) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
    }
}
