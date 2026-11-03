package gov.nist.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class DuplicateNameValueList implements Serializable, Cloneable {
    private static final long serialVersionUID = -5611332957903796952L;
    private MultiValueMapImpl nameValueMap = new MultiValueMapImpl();
    private String separator = ";";

    public void setSeparator(String str) {
        this.separator = str;
    }

    public String encode() {
        return encode(new StringBuffer()).toString();
    }

    public StringBuffer encode(StringBuffer stringBuffer) {
        if (!this.nameValueMap.isEmpty()) {
            Iterator it = this.nameValueMap.values().iterator();
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
        this.nameValueMap.put(nameValue.getName().toLowerCase(), (Object) nameValue);
    }

    public boolean equals(Object obj) {
        if (obj == null || !obj.getClass().equals(getClass())) {
            return false;
        }
        DuplicateNameValueList duplicateNameValueList = (DuplicateNameValueList) obj;
        if (this.nameValueMap.size() != duplicateNameValueList.nameValueMap.size()) {
            return false;
        }
        for (String str : this.nameValueMap.keySet()) {
            Collection nameValue = getNameValue(str);
            List list = duplicateNameValueList.nameValueMap.get((Object) str);
            if (list == null || !list.equals(nameValue)) {
                return false;
            }
        }
        return true;
    }

    public Collection getNameValue(String str) {
        return this.nameValueMap.get((Object) str.toLowerCase());
    }

    public Object clone() {
        DuplicateNameValueList duplicateNameValueList = new DuplicateNameValueList();
        duplicateNameValueList.setSeparator(this.separator);
        Iterator it = this.nameValueMap.values().iterator();
        while (it.hasNext()) {
            duplicateNameValueList.set((NameValue) ((NameValue) it.next()).clone());
        }
        return duplicateNameValueList;
    }

    public int hashCode() {
        return this.nameValueMap.keySet().hashCode();
    }
}
