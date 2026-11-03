package gov.nist.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
public class NameValueList implements Serializable, Cloneable, Map {
    private static final long serialVersionUID = -6998271876574260243L;
    private String separator = ";";
    private Map hmap = new LinkedHashMap();

    public void setSeparator(String str) {
        this.separator = str;
    }

    public String encode() {
        return encode(new StringBuffer()).toString();
    }

    public StringBuffer encode(StringBuffer stringBuffer) {
        if (!this.hmap.isEmpty()) {
            Iterator it = this.hmap.values().iterator();
            if (it.hasNext()) {
                while (true) {
                    Object next = it.next();
                    if (next instanceof GenericObject) {
                        ((GenericObject) next).encode(stringBuffer);
                    } else {
                        stringBuffer.append(next.toString());
                    }
                    if (!it.hasNext()) {
                        break;
                    }
                    stringBuffer.append(this.separator);
                }
            }
        }
        return stringBuffer;
    }

    public String toString() {
        return encode();
    }

    public void set(NameValue nameValue) {
        this.hmap.put(nameValue.getName().toLowerCase(), nameValue);
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        NameValueList nameValueList = (NameValueList) obj;
        if (this.hmap.size() != nameValueList.hmap.size()) {
            return false;
        }
        for (String str : this.hmap.keySet()) {
            NameValue nameValue = getNameValue(str);
            NameValue nameValue2 = (NameValue) nameValueList.hmap.get(str);
            if (nameValue2 == null || !nameValue2.equals(nameValue)) {
                return false;
            }
        }
        return true;
    }

    public Object getValue(String str) {
        NameValue nameValue = getNameValue(str.toLowerCase());
        if (nameValue != null) {
            return nameValue.getValueAsObject();
        }
        return null;
    }

    public NameValue getNameValue(String str) {
        return (NameValue) this.hmap.get(str.toLowerCase());
    }

    public Object clone() {
        NameValueList nameValueList = new NameValueList();
        nameValueList.setSeparator(this.separator);
        Iterator it = this.hmap.values().iterator();
        while (it.hasNext()) {
            nameValueList.set((NameValue) ((NameValue) it.next()).clone());
        }
        return nameValueList;
    }

    @Override // java.util.Map
    public int size() {
        return this.hmap.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.hmap.isEmpty();
    }

    public String getParameter(String str) {
        Object value = getValue(str);
        if (value == null) {
            return null;
        }
        if (value instanceof GenericObject) {
            return ((GenericObject) value).encode();
        }
        return value.toString();
    }

    @Override // java.util.Map
    public void clear() {
        this.hmap.clear();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.hmap.containsKey(obj.toString().toLowerCase());
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.hmap.containsValue(obj);
    }

    @Override // java.util.Map
    public Set entrySet() {
        return this.hmap.entrySet();
    }

    @Override // java.util.Map
    public NameValue get(Object obj) {
        return (NameValue) this.hmap.get(obj.toString().toLowerCase());
    }

    @Override // java.util.Map
    public Set keySet() {
        return this.hmap.keySet();
    }

    @Override // java.util.Map
    public NameValue put(String str, NameValue nameValue) {
        return (NameValue) this.hmap.put(str, nameValue);
    }

    @Override // java.util.Map
    public void putAll(Map map) {
        this.hmap.putAll(map);
    }

    @Override // java.util.Map
    public NameValue remove(Object obj) {
        return (NameValue) this.hmap.remove(obj.toString().toLowerCase());
    }

    @Override // java.util.Map
    public Collection values() {
        return this.hmap.values();
    }

    @Override // java.util.Map
    public int hashCode() {
        return this.hmap.keySet().hashCode();
    }
}
