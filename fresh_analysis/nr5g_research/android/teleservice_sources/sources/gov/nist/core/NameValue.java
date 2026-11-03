package gov.nist.core;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class NameValue extends GenericObject implements Map.Entry {
    private static final long serialVersionUID = -1857729012596437950L;
    protected final boolean isFlagParameter;
    protected boolean isQuotedString;
    private String name;
    private String quotes;
    private String separator;
    private Object value;

    public NameValue(String str, Object obj, boolean z) {
        this.name = str;
        this.value = obj;
        this.separator = "=";
        this.quotes = "";
        this.isFlagParameter = z;
    }

    public NameValue(String str, Object obj) {
        this(str, obj, false);
    }

    public void setQuotedValue() {
        this.isQuotedString = true;
        this.quotes = "\"";
    }

    public String getName() {
        return this.name;
    }

    public Object getValueAsObject() {
        return this.isFlagParameter ? "" : this.value;
    }

    public void setValueAsObject(Object obj) {
        this.value = obj;
    }

    @Override // gov.nist.core.GenericObject
    public String encode() {
        return encode(new StringBuffer()).toString();
    }

    @Override // gov.nist.core.GenericObject
    public StringBuffer encode(StringBuffer stringBuffer) {
        Object obj;
        Object obj2;
        String str = this.name;
        if (str == null || (obj2 = this.value) == null || this.isFlagParameter) {
            if (str == null && (obj = this.value) != null) {
                boolean zIsMySubclass = GenericObject.isMySubclass(obj.getClass());
                Object obj3 = this.value;
                if (zIsMySubclass) {
                    ((GenericObject) obj3).encode(stringBuffer);
                    return stringBuffer;
                }
                if (GenericObjectList.isMySubclass(obj3.getClass())) {
                    ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(this.value);
                    throw null;
                }
                stringBuffer.append(this.quotes);
                stringBuffer.append(this.value.toString());
                stringBuffer.append(this.quotes);
                return stringBuffer;
            }
            if (str != null && (this.value == null || this.isFlagParameter)) {
                stringBuffer.append(str);
            }
            return stringBuffer;
        }
        boolean zIsMySubclass2 = GenericObject.isMySubclass(obj2.getClass());
        Object obj4 = this.value;
        if (zIsMySubclass2) {
            stringBuffer.append(this.name);
            stringBuffer.append(this.separator);
            stringBuffer.append(this.quotes);
            ((GenericObject) obj4).encode(stringBuffer);
            stringBuffer.append(this.quotes);
            return stringBuffer;
        }
        boolean zIsMySubclass3 = GenericObjectList.isMySubclass(obj4.getClass());
        Object obj5 = this.value;
        if (zIsMySubclass3) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj5);
            stringBuffer.append(this.name);
            stringBuffer.append(this.separator);
            throw null;
        }
        if (obj5.toString().length() == 0) {
            boolean z = this.isQuotedString;
            String str2 = this.name;
            if (z) {
                stringBuffer.append(str2);
                stringBuffer.append(this.separator);
                stringBuffer.append(this.quotes);
                stringBuffer.append(this.quotes);
                return stringBuffer;
            }
            stringBuffer.append(str2);
            stringBuffer.append(this.separator);
            return stringBuffer;
        }
        stringBuffer.append(this.name);
        stringBuffer.append(this.separator);
        stringBuffer.append(this.quotes);
        stringBuffer.append(this.value.toString());
        stringBuffer.append(this.quotes);
        return stringBuffer;
    }

    @Override // gov.nist.core.GenericObject
    public Object clone() {
        NameValue nameValue = (NameValue) super.clone();
        Object obj = this.value;
        if (obj != null) {
            nameValue.value = GenericObject.makeClone(obj);
        }
        return nameValue;
    }

    @Override // java.util.Map.Entry
    public boolean equals(Object obj) {
        String str;
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        NameValue nameValue = (NameValue) obj;
        if (this == nameValue) {
            return true;
        }
        String str2 = this.name;
        if ((str2 == null && nameValue.name != null) || (str2 != null && nameValue.name == null)) {
            return false;
        }
        if (str2 != null && (str = nameValue.name) != null && str2.compareToIgnoreCase(str) != 0) {
            return false;
        }
        Object obj2 = this.value;
        if ((obj2 != null && nameValue.value == null) || (obj2 == null && nameValue.value != null)) {
            return false;
        }
        Object obj3 = nameValue.value;
        if (obj2 == obj3) {
            return true;
        }
        if (obj2 instanceof String) {
            if (this.isQuotedString) {
                return obj2.equals(obj3);
            }
            return ((String) obj2).compareToIgnoreCase((String) obj3) == 0;
        }
        return obj2.equals(obj3);
    }

    @Override // java.util.Map.Entry
    public String getKey() {
        return this.name;
    }

    @Override // java.util.Map.Entry
    public String getValue() {
        Object obj = this.value;
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    @Override // java.util.Map.Entry
    public String setValue(String str) {
        String str2 = this.value == null ? null : str;
        this.value = str;
        return str2;
    }

    @Override // java.util.Map.Entry
    public int hashCode() {
        return encode().toLowerCase().hashCode();
    }
}
