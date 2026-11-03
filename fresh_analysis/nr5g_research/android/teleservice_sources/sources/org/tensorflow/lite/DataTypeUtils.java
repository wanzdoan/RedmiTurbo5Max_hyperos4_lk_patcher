package org.tensorflow.lite;

import com.android.storage.s2.S2Support$$ExternalSyntheticBUOutline0;
import com.xiaomi.settingsdk.backup.data.KeyStringSettingItem;

/* JADX INFO: loaded from: classes2.dex */
abstract class DataTypeUtils {

    /* JADX INFO: renamed from: org.tensorflow.lite.DataTypeUtils$1, reason: invalid class name */
    abstract /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$tensorflow$lite$DataType;

        static {
            int[] iArr = new int[DataType.values().length];
            $SwitchMap$org$tensorflow$lite$DataType = iArr;
            try {
                iArr[DataType.FLOAT32.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.INT32.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.INT16.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.INT8.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.UINT8.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.INT64.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.BOOL.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$org$tensorflow$lite$DataType[DataType.STRING.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
        }
    }

    static String toStringName(DataType dataType) {
        switch (AnonymousClass1.$SwitchMap$org$tensorflow$lite$DataType[dataType.ordinal()]) {
            case 1:
                return "float";
            case 2:
                return "int";
            case 3:
                return "short";
            case 4:
            case 5:
                return "byte";
            case 6:
                return "long";
            case 7:
                return "bool";
            case 8:
                return KeyStringSettingItem.TYPE;
            default:
                DataType$$ExternalSyntheticBUOutline0.m("DataType error: DataType ", dataType, " is not supported yet");
                return null;
        }
    }

    static DataType fromC(int i) {
        switch (i) {
            case 1:
                return DataType.FLOAT32;
            case 2:
                return DataType.INT32;
            case 3:
                return DataType.UINT8;
            case 4:
                return DataType.INT64;
            case 5:
                return DataType.STRING;
            case 6:
                return DataType.BOOL;
            case 7:
                return DataType.INT16;
            case 8:
            default:
                S2Support$$ExternalSyntheticBUOutline0.m("DataType error: DataType ", i, " is not recognized in Java.");
                return null;
            case 9:
                return DataType.INT8;
        }
    }
}
