package com.android.internal.telephony.protobuf;

import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.Phone$$ExternalSyntheticBUOutline1;
import com.android.internal.telephony.SomeArgs$$ExternalSyntheticBUOutline0;
import com.android.internal.telephony.protobuf.FieldSet.FieldDescriptorLite;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
final class FieldSet<T extends FieldDescriptorLite<T>> {
    private static final FieldSet DEFAULT_INSTANCE = new FieldSet(true);
    private final SmallSortedMap fields;
    private boolean hasLazyField;
    private boolean isImmutable;

    public interface FieldDescriptorLite<T extends FieldDescriptorLite<T>> extends Comparable<T> {
        Internal.EnumLiteMap<?> getEnumType();

        WireFormat.JavaType getLiteJavaType();

        WireFormat.FieldType getLiteType();

        int getNumber();

        void internalMergeFrom(Object obj, Object obj2);

        boolean internalMessageIsImmutable(Object obj);

        boolean isPacked();

        boolean isRepeated();
    }

    /* synthetic */ FieldSet(SmallSortedMap smallSortedMap, AnonymousClass1 anonymousClass1) {
        this(smallSortedMap);
    }

    private FieldSet() {
        this.fields = SmallSortedMap.newFieldMap();
    }

    private FieldSet(boolean z) {
        this(SmallSortedMap.newFieldMap());
        makeImmutable();
    }

    private FieldSet(SmallSortedMap smallSortedMap) {
        this.fields = smallSortedMap;
        makeImmutable();
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> newFieldSet() {
        return new FieldSet<>();
    }

    public static <T extends FieldDescriptorLite<T>> FieldSet<T> emptySet() {
        return DEFAULT_INSTANCE;
    }

    public static <T extends FieldDescriptorLite<T>> Builder<T> newBuilder() {
        return new Builder<>((AnonymousClass1) null);
    }

    boolean isEmpty() {
        return this.fields.isEmpty();
    }

    public void makeImmutable() {
        SmallSortedMap smallSortedMap;
        if (this.isImmutable) {
            return;
        }
        int numArrayEntries = this.fields.getNumArrayEntries();
        int i = 0;
        while (true) {
            smallSortedMap = this.fields;
            if (i >= numArrayEntries) {
                break;
            }
            Object value = smallSortedMap.getArrayEntryAt(i).getValue();
            if (value instanceof GeneratedMessageLite) {
                ((GeneratedMessageLite) value).makeImmutable();
            }
            i++;
        }
        Iterator it = smallSortedMap.getOverflowEntries().iterator();
        while (it.hasNext()) {
            Object value2 = ((Map.Entry) it.next()).getValue();
            if (value2 instanceof GeneratedMessageLite) {
                ((GeneratedMessageLite) value2).makeImmutable();
            }
        }
        this.fields.makeImmutable();
        this.isImmutable = true;
    }

    public boolean isImmutable() {
        return this.isImmutable;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof FieldSet) {
            return this.fields.equals(((FieldSet) obj).fields);
        }
        return false;
    }

    public int hashCode() {
        return this.fields.hashCode();
    }

    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public FieldSet<T> m1526clone() {
        SmallSortedMap smallSortedMap;
        FieldSet<T> fieldSetNewFieldSet = newFieldSet();
        int numArrayEntries = this.fields.getNumArrayEntries();
        int i = 0;
        while (true) {
            smallSortedMap = this.fields;
            if (i >= numArrayEntries) {
                break;
            }
            Map.Entry arrayEntryAt = smallSortedMap.getArrayEntryAt(i);
            fieldSetNewFieldSet.setField((FieldDescriptorLite) arrayEntryAt.getKey(), arrayEntryAt.getValue());
            i++;
        }
        for (Map.Entry entry : smallSortedMap.getOverflowEntries()) {
            fieldSetNewFieldSet.setField((FieldDescriptorLite) entry.getKey(), entry.getValue());
        }
        fieldSetNewFieldSet.hasLazyField = this.hasLazyField;
        return fieldSetNewFieldSet;
    }

    public void clear() {
        this.fields.clear();
        this.hasLazyField = false;
    }

    public Map<T, Object> getAllFields() {
        boolean z = this.hasLazyField;
        SmallSortedMap smallSortedMap = this.fields;
        if (z) {
            SmallSortedMap smallSortedMapCloneAllFieldsMap = cloneAllFieldsMap(smallSortedMap, false, true);
            if (this.fields.isImmutable()) {
                smallSortedMapCloneAllFieldsMap.makeImmutable();
            }
            return smallSortedMapCloneAllFieldsMap;
        }
        boolean zIsImmutable = smallSortedMap.isImmutable();
        SmallSortedMap smallSortedMap2 = this.fields;
        return zIsImmutable ? smallSortedMap2 : Collections.unmodifiableMap(smallSortedMap2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static SmallSortedMap cloneAllFieldsMap(SmallSortedMap smallSortedMap, boolean z, boolean z2) {
        SmallSortedMap smallSortedMapNewFieldMap = SmallSortedMap.newFieldMap();
        int numArrayEntries = smallSortedMap.getNumArrayEntries();
        for (int i = 0; i < numArrayEntries; i++) {
            cloneFieldEntry(smallSortedMapNewFieldMap, smallSortedMap.getArrayEntryAt(i), z, z2);
        }
        Iterator it = smallSortedMap.getOverflowEntries().iterator();
        while (it.hasNext()) {
            cloneFieldEntry(smallSortedMapNewFieldMap, (Map.Entry) it.next(), z, z2);
        }
        return smallSortedMapNewFieldMap;
    }

    private static void cloneFieldEntry(Map map, Map.Entry entry, boolean z, boolean z2) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
        Object value = entry.getValue();
        if (z2 && (value instanceof LazyField)) {
            map.put(fieldDescriptorLite, ((LazyField) value).getValue());
        } else if (z && (value instanceof List)) {
            map.put(fieldDescriptorLite, new ArrayList((List) value));
        } else {
            map.put(fieldDescriptorLite, value);
        }
    }

    public Iterator<Map.Entry<T, Object>> iterator() {
        if (isEmpty()) {
            return Collections.emptyIterator();
        }
        if (this.hasLazyField) {
            return new LazyField.LazyIterator(this.fields.entrySet().iterator());
        }
        return this.fields.entrySet().iterator();
    }

    Iterator descendingIterator() {
        if (isEmpty()) {
            return Collections.emptyIterator();
        }
        if (this.hasLazyField) {
            return new LazyField.LazyIterator(this.fields.descendingEntrySet().iterator());
        }
        return this.fields.descendingEntrySet().iterator();
    }

    public boolean hasField(T t) {
        if (!t.isRepeated()) {
            return this.fields.get(t) != null;
        }
        Phone$$ExternalSyntheticBUOutline0.m("hasField() can only be called on non-repeated fields.");
        return false;
    }

    public Object getField(T t) {
        Object obj = this.fields.get(t);
        return obj instanceof LazyField ? ((LazyField) obj).getValue() : obj;
    }

    public void setField(T t, Object obj) {
        if (t.isRepeated()) {
            if (!(obj instanceof List)) {
                Phone$$ExternalSyntheticBUOutline0.m("Wrong object type used with protocol message reflection.");
                return;
            }
            List list = (List) obj;
            int size = list.size();
            ArrayList arrayList = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                Object obj2 = list.get(i);
                verifyType(t, obj2);
                arrayList.add(obj2);
            }
            obj = arrayList;
        } else {
            verifyType(t, obj);
        }
        if (obj instanceof LazyField) {
            this.hasLazyField = true;
        }
        this.fields.put(t, obj);
    }

    public void clearField(T t) {
        this.fields.remove(t);
        if (this.fields.isEmpty()) {
            this.hasLazyField = false;
        }
    }

    public int getRepeatedFieldCount(T t) {
        if (!t.isRepeated()) {
            Phone$$ExternalSyntheticBUOutline0.m("getRepeatedField() can only be called on repeated fields.");
            return 0;
        }
        Object field = getField(t);
        if (field == null) {
            return 0;
        }
        return ((List) field).size();
    }

    public Object getRepeatedField(T t, int i) {
        if (!t.isRepeated()) {
            Phone$$ExternalSyntheticBUOutline0.m("getRepeatedField() can only be called on repeated fields.");
            return null;
        }
        Object field = getField(t);
        if (field == null) {
            throw new IndexOutOfBoundsException();
        }
        return ((List) field).get(i);
    }

    public void setRepeatedField(T t, int i, Object obj) {
        if (!t.isRepeated()) {
            Phone$$ExternalSyntheticBUOutline0.m("getRepeatedField() can only be called on repeated fields.");
            return;
        }
        Object field = getField(t);
        if (field == null) {
            throw new IndexOutOfBoundsException();
        }
        verifyType(t, obj);
        ((List) field).set(i, obj);
    }

    public void addRepeatedField(T t, Object obj) {
        List arrayList;
        if (!t.isRepeated()) {
            Phone$$ExternalSyntheticBUOutline0.m("addRepeatedField() can only be called on repeated fields.");
            return;
        }
        verifyType(t, obj);
        Object field = getField(t);
        if (field == null) {
            arrayList = new ArrayList();
            this.fields.put(t, arrayList);
        } else {
            arrayList = (List) field;
        }
        arrayList.add(obj);
    }

    private void verifyType(FieldDescriptorLite fieldDescriptorLite, Object obj) {
        if (!isValidType(fieldDescriptorLite.getLiteType(), obj)) {
            throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(fieldDescriptorLite.getNumber()), fieldDescriptorLite.getLiteType().getJavaType(), obj.getClass().getName()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidType(WireFormat.FieldType fieldType, Object obj) {
        Internal.checkNotNull(obj);
        switch (AnonymousClass1.$SwitchMap$com$google$protobuf$WireFormat$JavaType[fieldType.getJavaType().ordinal()]) {
            case 1:
                return obj instanceof Integer;
            case 2:
                return obj instanceof Long;
            case 3:
                return obj instanceof Float;
            case 4:
                return obj instanceof Double;
            case 5:
                return obj instanceof Boolean;
            case 6:
                return obj instanceof String;
            case 7:
                return (obj instanceof ByteString) || (obj instanceof byte[]);
            case 8:
                return (obj instanceof Integer) || (obj instanceof Internal.EnumLite);
            case 9:
                return (obj instanceof MessageLite) || (obj instanceof LazyField);
            default:
                return false;
        }
    }

    public boolean isInitialized() {
        int numArrayEntries = this.fields.getNumArrayEntries();
        int i = 0;
        while (true) {
            SmallSortedMap smallSortedMap = this.fields;
            if (i < numArrayEntries) {
                if (!isInitialized(smallSortedMap.getArrayEntryAt(i))) {
                    return false;
                }
                i++;
            } else {
                Iterator it = smallSortedMap.getOverflowEntries().iterator();
                while (it.hasNext()) {
                    if (!isInitialized((Map.Entry) it.next())) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isInitialized(Map.Entry entry) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
        if (fieldDescriptorLite.getLiteJavaType() != WireFormat.JavaType.MESSAGE) {
            return true;
        }
        if (fieldDescriptorLite.isRepeated()) {
            List list = (List) entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (!isMessageFieldValueInitialized(list.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return isMessageFieldValueInitialized(entry.getValue());
    }

    private static boolean isMessageFieldValueInitialized(Object obj) {
        if (obj instanceof MessageLiteOrBuilder) {
            return ((MessageLiteOrBuilder) obj).isInitialized();
        }
        if (obj instanceof LazyField) {
            return true;
        }
        Phone$$ExternalSyntheticBUOutline0.m("Wrong object type used with protocol message reflection.");
        return false;
    }

    static int getWireFormatForFieldType(WireFormat.FieldType fieldType, boolean z) {
        if (z) {
            return 2;
        }
        return fieldType.getWireType();
    }

    public void mergeFrom(FieldSet<T> fieldSet) {
        SmallSortedMap smallSortedMap;
        int numArrayEntries = fieldSet.fields.getNumArrayEntries();
        int i = 0;
        while (true) {
            smallSortedMap = fieldSet.fields;
            if (i >= numArrayEntries) {
                break;
            }
            mergeFromField(smallSortedMap.getArrayEntryAt(i));
            i++;
        }
        Iterator it = smallSortedMap.getOverflowEntries().iterator();
        while (it.hasNext()) {
            mergeFromField((Map.Entry) it.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object cloneIfMutable(Object obj) {
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        byte[] bArr2 = new byte[bArr.length];
        System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
        return bArr2;
    }

    private void mergeFromField(Map.Entry entry) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
        Object value = entry.getValue();
        boolean z = value instanceof LazyField;
        if (fieldDescriptorLite.isRepeated()) {
            if (z) {
                SomeArgs$$ExternalSyntheticBUOutline0.m("Lazy fields can not be repeated");
                return;
            }
            Object field = getField(fieldDescriptorLite);
            List list = (List) value;
            int size = list.size();
            if (field == null) {
                field = new ArrayList(size);
            }
            List list2 = (List) field;
            for (int i = 0; i < size; i++) {
                list2.add(cloneIfMutable(list.get(i)));
            }
            this.fields.put(fieldDescriptorLite, field);
            return;
        }
        if (fieldDescriptorLite.getLiteJavaType() != WireFormat.JavaType.MESSAGE) {
            if (z) {
                SomeArgs$$ExternalSyntheticBUOutline0.m("Lazy fields must be message-valued");
                return;
            } else {
                this.fields.put(fieldDescriptorLite, cloneIfMutable(value));
                return;
            }
        }
        Object field2 = getField(fieldDescriptorLite);
        if (field2 == null) {
            this.fields.put(fieldDescriptorLite, cloneIfMutable(value));
            if (z) {
                this.hasLazyField = true;
                return;
            }
            return;
        }
        if (z) {
            value = ((LazyField) value).getValue();
        }
        if (fieldDescriptorLite.internalMessageIsImmutable(field2)) {
            MessageLite.Builder builder = ((MessageLite) field2).toBuilder();
            fieldDescriptorLite.internalMergeFrom(builder, value);
            this.fields.put(fieldDescriptorLite, builder.build());
            return;
        }
        fieldDescriptorLite.internalMergeFrom(field2, value);
    }

    public static Object readPrimitiveField(CodedInputStream codedInputStream, WireFormat.FieldType fieldType, boolean z) throws IOException {
        if (z) {
            return codedInputStream.readPrimitiveField(fieldType, WireFormat.Utf8Validation.STRICT);
        }
        return codedInputStream.readPrimitiveField(fieldType, WireFormat.Utf8Validation.LOOSE);
    }

    public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
        SmallSortedMap smallSortedMap;
        int numArrayEntries = this.fields.getNumArrayEntries();
        int i = 0;
        while (true) {
            smallSortedMap = this.fields;
            if (i >= numArrayEntries) {
                break;
            }
            Map.Entry arrayEntryAt = smallSortedMap.getArrayEntryAt(i);
            writeField((FieldDescriptorLite) arrayEntryAt.getKey(), arrayEntryAt.getValue(), codedOutputStream);
            i++;
        }
        for (Map.Entry entry : smallSortedMap.getOverflowEntries()) {
            writeField((FieldDescriptorLite) entry.getKey(), entry.getValue(), codedOutputStream);
        }
    }

    public void writeMessageSetTo(CodedOutputStream codedOutputStream) throws IOException {
        SmallSortedMap smallSortedMap;
        int numArrayEntries = this.fields.getNumArrayEntries();
        int i = 0;
        while (true) {
            smallSortedMap = this.fields;
            if (i >= numArrayEntries) {
                break;
            }
            writeMessageSetTo(smallSortedMap.getArrayEntryAt(i), codedOutputStream);
            i++;
        }
        Iterator it = smallSortedMap.getOverflowEntries().iterator();
        while (it.hasNext()) {
            writeMessageSetTo((Map.Entry) it.next(), codedOutputStream);
        }
    }

    private void writeMessageSetTo(Map.Entry entry, CodedOutputStream codedOutputStream) throws IOException {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
        if (fieldDescriptorLite.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !fieldDescriptorLite.isRepeated() && !fieldDescriptorLite.isPacked()) {
            Object value = entry.getValue();
            if (value instanceof LazyField) {
                codedOutputStream.writeRawMessageSetExtension(((FieldDescriptorLite) entry.getKey()).getNumber(), ((LazyField) value).toByteString());
                return;
            } else {
                codedOutputStream.writeMessageSetExtension(((FieldDescriptorLite) entry.getKey()).getNumber(), (MessageLite) value);
                return;
            }
        }
        writeField(fieldDescriptorLite, entry.getValue(), codedOutputStream);
    }

    static void writeElement(CodedOutputStream codedOutputStream, WireFormat.FieldType fieldType, int i, Object obj) {
        if (fieldType == WireFormat.FieldType.GROUP) {
            codedOutputStream.writeGroup(i, (MessageLite) obj);
        } else {
            codedOutputStream.writeTag(i, getWireFormatForFieldType(fieldType, false));
            writeElementNoTag(codedOutputStream, fieldType, obj);
        }
    }

    /* JADX INFO: renamed from: com.android.internal.telephony.protobuf.FieldSet$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$WireFormat$FieldType;
        static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$WireFormat$JavaType;

        static {
            int[] iArr = new int[WireFormat.FieldType.values().length];
            $SwitchMap$com$google$protobuf$WireFormat$FieldType = iArr;
            try {
                iArr[WireFormat.FieldType.DOUBLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FLOAT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.INT64.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.UINT64.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.INT32.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FIXED64.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.FIXED32.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.BOOL.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.GROUP.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.MESSAGE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.STRING.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.BYTES.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.UINT32.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SFIXED32.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SFIXED64.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SINT32.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.SINT64.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$FieldType[WireFormat.FieldType.ENUM.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            int[] iArr2 = new int[WireFormat.JavaType.values().length];
            $SwitchMap$com$google$protobuf$WireFormat$JavaType = iArr2;
            try {
                iArr2[WireFormat.JavaType.INT.ordinal()] = 1;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.LONG.ordinal()] = 2;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.FLOAT.ordinal()] = 3;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.DOUBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.BOOLEAN.ordinal()] = 5;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.STRING.ordinal()] = 6;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.BYTE_STRING.ordinal()] = 7;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.ENUM.ordinal()] = 8;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$google$protobuf$WireFormat$JavaType[WireFormat.JavaType.MESSAGE.ordinal()] = 9;
            } catch (NoSuchFieldError unused27) {
            }
        }
    }

    static void writeElementNoTag(CodedOutputStream codedOutputStream, WireFormat.FieldType fieldType, Object obj) throws IOException {
        switch (AnonymousClass1.$SwitchMap$com$google$protobuf$WireFormat$FieldType[fieldType.ordinal()]) {
            case 1:
                codedOutputStream.writeDoubleNoTag(((Double) obj).doubleValue());
                break;
            case 2:
                codedOutputStream.writeFloatNoTag(((Float) obj).floatValue());
                break;
            case 3:
                codedOutputStream.writeInt64NoTag(((Long) obj).longValue());
                break;
            case 4:
                codedOutputStream.writeUInt64NoTag(((Long) obj).longValue());
                break;
            case 5:
                codedOutputStream.writeInt32NoTag(((Integer) obj).intValue());
                break;
            case 6:
                codedOutputStream.writeFixed64NoTag(((Long) obj).longValue());
                break;
            case 7:
                codedOutputStream.writeFixed32NoTag(((Integer) obj).intValue());
                break;
            case 8:
                codedOutputStream.writeBoolNoTag(((Boolean) obj).booleanValue());
                break;
            case 9:
                codedOutputStream.writeGroupNoTag((MessageLite) obj);
                break;
            case 10:
                codedOutputStream.writeMessageNoTag((MessageLite) obj);
                break;
            case 11:
                if (obj instanceof ByteString) {
                    codedOutputStream.writeBytesNoTag((ByteString) obj);
                } else {
                    codedOutputStream.writeStringNoTag((String) obj);
                }
                break;
            case 12:
                if (obj instanceof ByteString) {
                    codedOutputStream.writeBytesNoTag((ByteString) obj);
                } else {
                    codedOutputStream.writeByteArrayNoTag((byte[]) obj);
                }
                break;
            case 13:
                codedOutputStream.writeUInt32NoTag(((Integer) obj).intValue());
                break;
            case 14:
                codedOutputStream.writeSFixed32NoTag(((Integer) obj).intValue());
                break;
            case 15:
                codedOutputStream.writeSFixed64NoTag(((Long) obj).longValue());
                break;
            case 16:
                codedOutputStream.writeSInt32NoTag(((Integer) obj).intValue());
                break;
            case 17:
                codedOutputStream.writeSInt64NoTag(((Long) obj).longValue());
                break;
            case 18:
                if (obj instanceof Internal.EnumLite) {
                    codedOutputStream.writeEnumNoTag(((Internal.EnumLite) obj).getNumber());
                } else {
                    codedOutputStream.writeEnumNoTag(((Integer) obj).intValue());
                }
                break;
        }
    }

    public static void writeField(FieldDescriptorLite<?> fieldDescriptorLite, Object obj, CodedOutputStream codedOutputStream) throws IOException {
        WireFormat.FieldType liteType = fieldDescriptorLite.getLiteType();
        int number = fieldDescriptorLite.getNumber();
        if (fieldDescriptorLite.isRepeated()) {
            List list = (List) obj;
            int size = list.size();
            int i = 0;
            if (!fieldDescriptorLite.isPacked()) {
                while (i < size) {
                    writeElement(codedOutputStream, liteType, number, list.get(i));
                    i++;
                }
                return;
            } else {
                if (list.isEmpty()) {
                    return;
                }
                codedOutputStream.writeTag(number, 2);
                int iComputeElementSizeNoTag = 0;
                for (int i2 = 0; i2 < size; i2++) {
                    iComputeElementSizeNoTag += computeElementSizeNoTag(liteType, list.get(i2));
                }
                codedOutputStream.writeUInt32NoTag(iComputeElementSizeNoTag);
                while (i < size) {
                    writeElementNoTag(codedOutputStream, liteType, list.get(i));
                    i++;
                }
                return;
            }
        }
        if (obj instanceof LazyField) {
            writeElement(codedOutputStream, liteType, number, ((LazyField) obj).getValue());
        } else {
            writeElement(codedOutputStream, liteType, number, obj);
        }
    }

    public int getSerializedSize() {
        SmallSortedMap smallSortedMap;
        int numArrayEntries = this.fields.getNumArrayEntries();
        int i = 0;
        int iComputeFieldSize = 0;
        while (true) {
            smallSortedMap = this.fields;
            if (i >= numArrayEntries) {
                break;
            }
            Map.Entry arrayEntryAt = smallSortedMap.getArrayEntryAt(i);
            iComputeFieldSize += computeFieldSize((FieldDescriptorLite) arrayEntryAt.getKey(), arrayEntryAt.getValue());
            i++;
        }
        for (Map.Entry entry : smallSortedMap.getOverflowEntries()) {
            iComputeFieldSize += computeFieldSize((FieldDescriptorLite) entry.getKey(), entry.getValue());
        }
        return iComputeFieldSize;
    }

    public int getMessageSetSerializedSize() {
        SmallSortedMap smallSortedMap;
        int numArrayEntries = this.fields.getNumArrayEntries();
        int i = 0;
        int messageSetSerializedSize = 0;
        while (true) {
            smallSortedMap = this.fields;
            if (i >= numArrayEntries) {
                break;
            }
            messageSetSerializedSize += getMessageSetSerializedSize(smallSortedMap.getArrayEntryAt(i));
            i++;
        }
        Iterator it = smallSortedMap.getOverflowEntries().iterator();
        while (it.hasNext()) {
            messageSetSerializedSize += getMessageSetSerializedSize((Map.Entry) it.next());
        }
        return messageSetSerializedSize;
    }

    private int getMessageSetSerializedSize(Map.Entry entry) {
        FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
        Object value = entry.getValue();
        if (fieldDescriptorLite.getLiteJavaType() == WireFormat.JavaType.MESSAGE && !fieldDescriptorLite.isRepeated() && !fieldDescriptorLite.isPacked()) {
            if (value instanceof LazyField) {
                return ((LazyField) value).computeMessageSetExtensionSize(((FieldDescriptorLite) entry.getKey()).getNumber());
            }
            return CodedOutputStream.computeMessageSetExtensionSize(((FieldDescriptorLite) entry.getKey()).getNumber(), (MessageLite) value);
        }
        return computeFieldSize(fieldDescriptorLite, value);
    }

    static int computeElementSize(WireFormat.FieldType fieldType, int i, Object obj) {
        int iComputeTagSize = CodedOutputStream.computeTagSize(i);
        if (fieldType == WireFormat.FieldType.GROUP) {
            iComputeTagSize *= 2;
        }
        return iComputeTagSize + computeElementSizeNoTag(fieldType, obj);
    }

    static int computeElementSizeNoTag(WireFormat.FieldType fieldType, Object obj) {
        switch (AnonymousClass1.$SwitchMap$com$google$protobuf$WireFormat$FieldType[fieldType.ordinal()]) {
            case 1:
                return CodedOutputStream.computeDoubleSizeNoTag(((Double) obj).doubleValue());
            case 2:
                return CodedOutputStream.computeFloatSizeNoTag(((Float) obj).floatValue());
            case 3:
                return CodedOutputStream.computeInt64SizeNoTag(((Long) obj).longValue());
            case 4:
                return CodedOutputStream.computeUInt64SizeNoTag(((Long) obj).longValue());
            case 5:
                return CodedOutputStream.computeInt32SizeNoTag(((Integer) obj).intValue());
            case 6:
                return CodedOutputStream.computeFixed64SizeNoTag(((Long) obj).longValue());
            case 7:
                return CodedOutputStream.computeFixed32SizeNoTag(((Integer) obj).intValue());
            case 8:
                return CodedOutputStream.computeBoolSizeNoTag(((Boolean) obj).booleanValue());
            case 9:
                return ((MessageLite) obj).getSerializedSize();
            case 10:
                if (obj instanceof LazyField) {
                    return ((LazyField) obj).computeSizeNoTag();
                }
                return CodedOutputStream.computeMessageSizeNoTag((MessageLite) obj);
            case 11:
                if (obj instanceof ByteString) {
                    return CodedOutputStream.computeBytesSizeNoTag((ByteString) obj);
                }
                return CodedOutputStream.computeStringSizeNoTag((String) obj);
            case 12:
                if (obj instanceof ByteString) {
                    return CodedOutputStream.computeBytesSizeNoTag((ByteString) obj);
                }
                return CodedOutputStream.computeByteArraySizeNoTag((byte[]) obj);
            case 13:
                return CodedOutputStream.computeUInt32SizeNoTag(((Integer) obj).intValue());
            case 14:
                return CodedOutputStream.computeSFixed32SizeNoTag(((Integer) obj).intValue());
            case 15:
                return CodedOutputStream.computeSFixed64SizeNoTag(((Long) obj).longValue());
            case 16:
                return CodedOutputStream.computeSInt32SizeNoTag(((Integer) obj).intValue());
            case 17:
                return CodedOutputStream.computeSInt64SizeNoTag(((Long) obj).longValue());
            case 18:
                if (obj instanceof Internal.EnumLite) {
                    return CodedOutputStream.computeEnumSizeNoTag(((Internal.EnumLite) obj).getNumber());
                }
                return CodedOutputStream.computeEnumSizeNoTag(((Integer) obj).intValue());
            default:
                Phone$$ExternalSyntheticBUOutline1.m("There is no way to get here, but the compiler thinks otherwise.");
                return 0;
        }
    }

    public static int computeFieldSize(FieldDescriptorLite<?> fieldDescriptorLite, Object obj) {
        WireFormat.FieldType liteType = fieldDescriptorLite.getLiteType();
        int number = fieldDescriptorLite.getNumber();
        if (fieldDescriptorLite.isRepeated()) {
            List list = (List) obj;
            int size = list.size();
            int i = 0;
            if (!fieldDescriptorLite.isPacked()) {
                int iComputeElementSize = 0;
                while (i < size) {
                    iComputeElementSize += computeElementSize(liteType, number, list.get(i));
                    i++;
                }
                return iComputeElementSize;
            }
            if (list.isEmpty()) {
                return 0;
            }
            int iComputeElementSizeNoTag = 0;
            while (i < size) {
                iComputeElementSizeNoTag += computeElementSizeNoTag(liteType, list.get(i));
                i++;
            }
            return CodedOutputStream.computeTagSize(number) + iComputeElementSizeNoTag + CodedOutputStream.computeUInt32SizeNoTag(iComputeElementSizeNoTag);
        }
        return computeElementSize(liteType, number, obj);
    }

    static final class Builder<T extends FieldDescriptorLite<T>> {
        private SmallSortedMap fields;
        private boolean hasLazyField;
        private boolean hasNestedBuilders;
        private boolean isMutable;

        /* synthetic */ Builder(AnonymousClass1 anonymousClass1) {
            this();
        }

        private Builder() {
            this(SmallSortedMap.newFieldMap());
        }

        private Builder(SmallSortedMap smallSortedMap) {
            this.fields = smallSortedMap;
            this.isMutable = true;
        }

        public FieldSet<T> build() {
            return buildImpl(false);
        }

        public FieldSet<T> buildPartial() {
            return buildImpl(true);
        }

        private FieldSet buildImpl(boolean z) {
            if (this.fields.isEmpty()) {
                return FieldSet.emptySet();
            }
            this.isMutable = false;
            SmallSortedMap smallSortedMapCloneAllFieldsMap = this.fields;
            if (this.hasNestedBuilders) {
                smallSortedMapCloneAllFieldsMap = FieldSet.cloneAllFieldsMap(smallSortedMapCloneAllFieldsMap, false, false);
                replaceBuilders(smallSortedMapCloneAllFieldsMap, z);
            }
            FieldSet fieldSet = new FieldSet(smallSortedMapCloneAllFieldsMap, null);
            fieldSet.hasLazyField = this.hasLazyField;
            return fieldSet;
        }

        private static void replaceBuilders(SmallSortedMap smallSortedMap, boolean z) {
            int numArrayEntries = smallSortedMap.getNumArrayEntries();
            for (int i = 0; i < numArrayEntries; i++) {
                replaceBuilders(smallSortedMap.getArrayEntryAt(i), z);
            }
            Iterator it = smallSortedMap.getOverflowEntries().iterator();
            while (it.hasNext()) {
                replaceBuilders((Map.Entry) it.next(), z);
            }
        }

        private static void replaceBuilders(Map.Entry entry, boolean z) {
            entry.setValue(replaceBuilders((FieldDescriptorLite) entry.getKey(), entry.getValue(), z));
        }

        private static Object replaceBuilders(FieldDescriptorLite fieldDescriptorLite, Object obj, boolean z) {
            if (obj == null || fieldDescriptorLite.getLiteJavaType() != WireFormat.JavaType.MESSAGE) {
                return obj;
            }
            if (fieldDescriptorLite.isRepeated()) {
                if (!(obj instanceof List)) {
                    ArrayDecoders$$ExternalSyntheticBUOutline0.m("Repeated field should contains a List but actually contains type: ", obj.getClass());
                    return null;
                }
                List arrayList = (List) obj;
                for (int i = 0; i < arrayList.size(); i++) {
                    Object obj2 = arrayList.get(i);
                    Object objReplaceBuilder = replaceBuilder(obj2, z);
                    if (objReplaceBuilder != obj2) {
                        if (arrayList == obj) {
                            arrayList = new ArrayList(arrayList);
                        }
                        arrayList.set(i, objReplaceBuilder);
                    }
                }
                return arrayList;
            }
            return replaceBuilder(obj, z);
        }

        private static Object replaceBuilder(Object obj, boolean z) {
            if (!(obj instanceof MessageLite.Builder)) {
                return obj;
            }
            MessageLite.Builder builder = (MessageLite.Builder) obj;
            if (z) {
                return builder.buildPartial();
            }
            return builder.build();
        }

        public static <T extends FieldDescriptorLite<T>> Builder<T> fromFieldSet(FieldSet<T> fieldSet) {
            Builder<T> builder = new Builder<>(FieldSet.cloneAllFieldsMap(((FieldSet) fieldSet).fields, true, false));
            ((Builder) builder).hasLazyField = ((FieldSet) fieldSet).hasLazyField;
            return builder;
        }

        public Map<T, Object> getAllFields() {
            boolean z = this.hasLazyField;
            SmallSortedMap smallSortedMap = this.fields;
            if (z) {
                SmallSortedMap smallSortedMapCloneAllFieldsMap = FieldSet.cloneAllFieldsMap(smallSortedMap, false, true);
                if (this.fields.isImmutable()) {
                    smallSortedMapCloneAllFieldsMap.makeImmutable();
                    return smallSortedMapCloneAllFieldsMap;
                }
                replaceBuilders(smallSortedMapCloneAllFieldsMap, true);
                return smallSortedMapCloneAllFieldsMap;
            }
            boolean zIsImmutable = smallSortedMap.isImmutable();
            SmallSortedMap smallSortedMap2 = this.fields;
            return zIsImmutable ? smallSortedMap2 : Collections.unmodifiableMap(smallSortedMap2);
        }

        public boolean hasField(T t) {
            if (!t.isRepeated()) {
                return this.fields.get(t) != null;
            }
            Phone$$ExternalSyntheticBUOutline0.m("hasField() can only be called on non-repeated fields.");
            return false;
        }

        public Object getField(T t) {
            return replaceBuilders(t, getFieldAllowBuilders(t), true);
        }

        Object getFieldAllowBuilders(FieldDescriptorLite fieldDescriptorLite) {
            Object obj = this.fields.get(fieldDescriptorLite);
            return obj instanceof LazyField ? ((LazyField) obj).getValue() : obj;
        }

        private void ensureIsMutable() {
            if (this.isMutable) {
                return;
            }
            this.fields = FieldSet.cloneAllFieldsMap(this.fields, true, false);
            this.isMutable = true;
        }

        public void setField(T t, Object obj) {
            ensureIsMutable();
            if (t.isRepeated()) {
                if (!(obj instanceof List)) {
                    Phone$$ExternalSyntheticBUOutline0.m("Wrong object type used with protocol message reflection.");
                    return;
                }
                ArrayList arrayList = new ArrayList((List) obj);
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    Object obj2 = arrayList.get(i);
                    verifyType(t, obj2);
                    this.hasNestedBuilders = this.hasNestedBuilders || (obj2 instanceof MessageLite.Builder);
                }
                obj = arrayList;
            } else {
                verifyType(t, obj);
            }
            if (obj instanceof LazyField) {
                this.hasLazyField = true;
            }
            this.hasNestedBuilders = this.hasNestedBuilders || (obj instanceof MessageLite.Builder);
            this.fields.put(t, obj);
        }

        public void clearField(T t) {
            ensureIsMutable();
            this.fields.remove(t);
            if (this.fields.isEmpty()) {
                this.hasLazyField = false;
            }
        }

        public int getRepeatedFieldCount(T t) {
            if (!t.isRepeated()) {
                Phone$$ExternalSyntheticBUOutline0.m("getRepeatedFieldCount() can only be called on repeated fields.");
                return 0;
            }
            Object fieldAllowBuilders = getFieldAllowBuilders(t);
            if (fieldAllowBuilders == null) {
                return 0;
            }
            return ((List) fieldAllowBuilders).size();
        }

        public Object getRepeatedField(T t, int i) {
            if (this.hasNestedBuilders) {
                ensureIsMutable();
            }
            return replaceBuilder(getRepeatedFieldAllowBuilders(t, i), true);
        }

        Object getRepeatedFieldAllowBuilders(FieldDescriptorLite fieldDescriptorLite, int i) {
            if (!fieldDescriptorLite.isRepeated()) {
                Phone$$ExternalSyntheticBUOutline0.m("getRepeatedField() can only be called on repeated fields.");
                return null;
            }
            Object fieldAllowBuilders = getFieldAllowBuilders(fieldDescriptorLite);
            if (fieldAllowBuilders == null) {
                throw new IndexOutOfBoundsException();
            }
            return ((List) fieldAllowBuilders).get(i);
        }

        public void setRepeatedField(T t, int i, Object obj) {
            ensureIsMutable();
            if (!t.isRepeated()) {
                Phone$$ExternalSyntheticBUOutline0.m("getRepeatedField() can only be called on repeated fields.");
                return;
            }
            this.hasNestedBuilders = this.hasNestedBuilders || (obj instanceof MessageLite.Builder);
            Object fieldAllowBuilders = getFieldAllowBuilders(t);
            if (fieldAllowBuilders == null) {
                throw new IndexOutOfBoundsException();
            }
            verifyType(t, obj);
            ((List) fieldAllowBuilders).set(i, obj);
        }

        public void addRepeatedField(T t, Object obj) {
            List arrayList;
            ensureIsMutable();
            if (!t.isRepeated()) {
                Phone$$ExternalSyntheticBUOutline0.m("addRepeatedField() can only be called on repeated fields.");
                return;
            }
            this.hasNestedBuilders = this.hasNestedBuilders || (obj instanceof MessageLite.Builder);
            verifyType(t, obj);
            Object fieldAllowBuilders = getFieldAllowBuilders(t);
            if (fieldAllowBuilders == null) {
                arrayList = new ArrayList();
                this.fields.put(t, arrayList);
            } else {
                arrayList = (List) fieldAllowBuilders;
            }
            arrayList.add(obj);
        }

        private void verifyType(FieldDescriptorLite fieldDescriptorLite, Object obj) {
            if (FieldSet.isValidType(fieldDescriptorLite.getLiteType(), obj)) {
                return;
            }
            if (fieldDescriptorLite.getLiteType().getJavaType() != WireFormat.JavaType.MESSAGE || !(obj instanceof MessageLite.Builder)) {
                throw new IllegalArgumentException(String.format("Wrong object type used with protocol message reflection.\nField number: %d, field java type: %s, value type: %s\n", Integer.valueOf(fieldDescriptorLite.getNumber()), fieldDescriptorLite.getLiteType().getJavaType(), obj.getClass().getName()));
            }
        }

        public boolean isInitialized() {
            int numArrayEntries = this.fields.getNumArrayEntries();
            int i = 0;
            while (true) {
                SmallSortedMap smallSortedMap = this.fields;
                if (i < numArrayEntries) {
                    if (!FieldSet.isInitialized(smallSortedMap.getArrayEntryAt(i))) {
                        return false;
                    }
                    i++;
                } else {
                    Iterator it = smallSortedMap.getOverflowEntries().iterator();
                    while (it.hasNext()) {
                        if (!FieldSet.isInitialized((Map.Entry) it.next())) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        }

        public void mergeFrom(FieldSet<T> fieldSet) {
            ensureIsMutable();
            int numArrayEntries = ((FieldSet) fieldSet).fields.getNumArrayEntries();
            for (int i = 0; i < numArrayEntries; i++) {
                mergeFromField(((FieldSet) fieldSet).fields.getArrayEntryAt(i));
            }
            Iterator it = ((FieldSet) fieldSet).fields.getOverflowEntries().iterator();
            while (it.hasNext()) {
                mergeFromField((Map.Entry) it.next());
            }
        }

        private void mergeFromField(Map.Entry entry) {
            FieldDescriptorLite fieldDescriptorLite = (FieldDescriptorLite) entry.getKey();
            Object value = entry.getValue();
            boolean z = value instanceof LazyField;
            if (fieldDescriptorLite.isRepeated()) {
                if (z) {
                    SomeArgs$$ExternalSyntheticBUOutline0.m("Lazy fields can not be repeated");
                    return;
                }
                List arrayList = (List) getFieldAllowBuilders(fieldDescriptorLite);
                List list = (List) value;
                int size = list.size();
                if (arrayList == null) {
                    arrayList = new ArrayList(size);
                    this.fields.put(fieldDescriptorLite, arrayList);
                }
                for (int i = 0; i < size; i++) {
                    arrayList.add(FieldSet.cloneIfMutable(list.get(i)));
                }
                return;
            }
            if (fieldDescriptorLite.getLiteJavaType() != WireFormat.JavaType.MESSAGE) {
                if (!z) {
                    this.fields.put(fieldDescriptorLite, FieldSet.cloneIfMutable(value));
                    return;
                } else {
                    SomeArgs$$ExternalSyntheticBUOutline0.m("Lazy fields must be message-valued");
                    return;
                }
            }
            Object fieldAllowBuilders = getFieldAllowBuilders(fieldDescriptorLite);
            if (fieldAllowBuilders == null) {
                this.fields.put(fieldDescriptorLite, FieldSet.cloneIfMutable(value));
                if (z) {
                    this.hasLazyField = true;
                    return;
                }
                return;
            }
            if (z) {
                value = ((LazyField) value).getValue();
            }
            if (fieldDescriptorLite.internalMessageIsImmutable(fieldAllowBuilders)) {
                MessageLite.Builder builder = ((MessageLite) fieldAllowBuilders).toBuilder();
                fieldDescriptorLite.internalMergeFrom(builder, value);
                this.fields.put(fieldDescriptorLite, builder.build());
                return;
            }
            fieldDescriptorLite.internalMergeFrom(fieldAllowBuilders, value);
        }
    }
}
