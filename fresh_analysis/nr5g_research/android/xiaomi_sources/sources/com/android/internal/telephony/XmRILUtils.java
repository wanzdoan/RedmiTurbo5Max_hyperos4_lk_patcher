package com.android.internal.telephony;

import android.os.Bundle;
import com.android.telephony.Rlog;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class XmRILUtils {
    private static final String TAG = "XmRILUtils";
    private static final Set<Class> WRAPPER_CLASSES = new HashSet(Arrays.asList(Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class));

    public static String convertNullToEmptyString(String string) {
        return string != null ? string : "";
    }

    public static String requestToString(int request) {
        switch (request) {
            case 0:
                return "SEND_COMMON_MESSAGE";
            case 1:
                return "SET_SAR_DSI";
            case 2:
                return "SET_SAR_DSI_GROUP";
            case 3:
                return "GET_SAR_DSI";
            case 4:
                return "GET_SAR_DSI_GROUP";
            case 5:
                return "SET_DEPRIORITIZE_SA";
            case 6:
                return "SET_NR_MODE";
            case 7:
                return "ENABLE_ENDC";
            case 8:
                return "SET_DATA_STALL_MODE";
            case 9:
                return "SET_MOBILITY_OPT_PARAMS";
            case 10:
                return "SET_PREFER_BAND_PARAMS";
            case 11:
                return "PROMOTE_BSR";
            case 12:
                return "GET_DDS_ASSISTANT_INFO";
            case 13:
                return "GET_MODEM_SCENE";
            case 14:
                return "GET_MODEM_REASON";
            case 15:
                return "SET_MODEM_MODEL_CONFIG";
            case 16:
                return "SET_MODEM_CALLBACK_SWITCH";
            case 17:
                return "SET_MODEM_EVENT_IND_SWITCH";
            case 18:
                return "SET_FIVE_TUPLE_BLACK_LIST";
            default:
                return "<unknown request " + request + ">";
        }
    }

    public static String responseToString(int response) {
        switch (response) {
            case 1000:
                return "COMMON_MESSAGE_INDICATION";
            case 1001:
                return "HIGH_SPEED_RAIL_INFO_INDICATION";
            case XmRILConstants.XM_RIL_UNSOL_QOS_INFO_INDICATION /* 1002 */:
                return "QOS_INFO_INDICATION";
            case XmRILConstants.XM_RIL_UNSOL_MODEM_EXCEPTION_INDICATION /* 1003 */:
                return "MODEM_EXCEPTION_INDICATION";
            case XmRILConstants.XM_RIL_UNSOL_MODEM_SCENE_INDICATION /* 1004 */:
                return "MODEM_SCENE_INDICATION";
            default:
                return "<unknown response " + response + ">";
        }
    }

    private static boolean isPrimitiveOrWrapper(Class c) {
        return c.isPrimitive() || WRAPPER_CLASSES.contains(c);
    }

    public static String convertToString(Object o) throws IllegalAccessException, InvocationTargetException {
        boolean toStringExists = false;
        int i = 0;
        try {
            toStringExists = o.getClass().getMethod("toString", new Class[0]).getDeclaringClass() != Object.class;
        } catch (NoSuchMethodException e) {
            loge(e.toString());
        }
        if (toStringExists || isPrimitiveOrWrapper(o.getClass()) || (o instanceof ArrayList)) {
            return o.toString();
        }
        if (o.getClass().isArray()) {
            StringBuilder sb = new StringBuilder("[");
            boolean added = false;
            if (isPrimitiveOrWrapper(o.getClass().getComponentType())) {
                for (int i2 = 0; i2 < Array.getLength(o); i2++) {
                    sb.append(convertToString(Array.get(o, i2))).append(", ");
                    added = true;
                }
            } else {
                Object[] objArr = (Object[]) o;
                int length = objArr.length;
                while (i < length) {
                    Object element = objArr[i];
                    sb.append(convertToString(element)).append(", ");
                    added = true;
                    i++;
                }
            }
            if (added) {
                sb.delete(sb.length() - 2, sb.length());
            }
            sb.append("]");
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder(o.getClass().getSimpleName());
        sb2.append("{");
        Field[] fields = o.getClass().getDeclaredFields();
        int tag = -1;
        try {
            tag = ((Integer) o.getClass().getDeclaredMethod("getTag", new Class[0]).invoke(o, new Object[0])).intValue();
        } catch (IllegalAccessException | InvocationTargetException e2) {
            loge(e2.toString());
        } catch (NoSuchMethodException e3) {
        }
        if (tag != -1) {
            String tagName = null;
            try {
                Method method = o.getClass().getDeclaredMethod("_tagString", Integer.TYPE);
                method.setAccessible(true);
                tagName = (String) method.invoke(o, Integer.valueOf(tag));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e4) {
                loge(e4.toString());
            }
            if (tagName != null) {
                sb2.append(tagName);
                sb2.append("=");
                String getTagMethod = "get" + tagName.substring(0, 1).toUpperCase(Locale.ROOT) + tagName.substring(1);
                Object val = null;
                try {
                    val = o.getClass().getDeclaredMethod(getTagMethod, new Class[0]).invoke(o, new Object[0]);
                } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e5) {
                    loge(e5.toString());
                }
                if (val != null) {
                    sb2.append(convertToString(val));
                }
            }
        } else {
            boolean added2 = false;
            int length2 = fields.length;
            while (i < length2) {
                Field field = fields[i];
                if (!Modifier.isStatic(field.getModifiers())) {
                    sb2.append(field.getName()).append("=");
                    Object val2 = null;
                    try {
                        val2 = field.get(o);
                    } catch (IllegalAccessException e6) {
                        loge(e6.toString());
                    }
                    if (val2 != null) {
                        sb2.append(convertToString(val2)).append(", ");
                        added2 = true;
                    }
                }
                i++;
            }
            if (added2) {
                sb2.delete(sb2.length() - 2, sb2.length());
            }
        }
        sb2.append("}");
        return sb2.toString();
    }

    public static Bundle parseIntArrayBundle(int resultCode, Bundle bundle, String key) {
        byte[] data;
        Bundle result = new Bundle();
        if (resultCode == 0 && bundle != null && key != null && (data = bundle.getByteArray("byte_array")) != null && data.length % 4 == 0) {
            int[] intArray = new int[data.length / 4];
            ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            for (int i = 0; i < intArray.length; i++) {
                intArray[i] = buffer.getInt();
            }
            result.putIntArray(key, intArray);
        }
        return result;
    }

    public static byte[] intArrayToByteArray(int[] intArray) {
        if (intArray == null) {
            return new byte[0];
        }
        ByteBuffer buffer = ByteBuffer.allocate(intArray.length * 4).order(ByteOrder.LITTLE_ENDIAN);
        for (int value : intArray) {
            buffer.putInt(value);
        }
        return buffer.array();
    }

    private static void logd(String log) {
        Rlog.d(TAG, log);
    }

    private static void loge(String log) {
        Rlog.e(TAG, log);
    }
}
