package gov.nist.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
public class MultiValueMapImpl implements Map, Serializable, Cloneable {
    private static final long serialVersionUID = 4275505380960964605L;
    private HashMap map = new HashMap();

    public List put(String str, Object obj) {
        ArrayList arrayList = (ArrayList) this.map.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList(10);
            this.map.put(str, arrayList);
        }
        arrayList.add(obj);
        return arrayList;
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        Set setEntrySet = this.map.entrySet();
        if (setEntrySet == null) {
            return false;
        }
        Iterator it = setEntrySet.iterator();
        while (it.hasNext()) {
            if (((ArrayList) ((Map.Entry) it.next()).getValue()).contains(obj)) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public void clear() {
        Iterator it = this.map.entrySet().iterator();
        while (it.hasNext()) {
            ((ArrayList) ((Map.Entry) it.next()).getValue()).clear();
        }
        this.map.clear();
    }

    @Override // java.util.Map
    public Collection values() {
        ArrayList arrayList = new ArrayList(this.map.size());
        Iterator it = this.map.entrySet().iterator();
        while (it.hasNext()) {
            for (Object obj : ((ArrayList) ((Map.Entry) it.next()).getValue()).toArray()) {
                arrayList.add(obj);
            }
        }
        return arrayList;
    }

    public Object clone() {
        MultiValueMapImpl multiValueMapImpl = new MultiValueMapImpl();
        multiValueMapImpl.map = (HashMap) this.map.clone();
        return multiValueMapImpl;
    }

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        return this.map.containsKey(obj);
    }

    @Override // java.util.Map
    public Set entrySet() {
        return this.map.entrySet();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public Set keySet() {
        return this.map.keySet();
    }

    @Override // java.util.Map
    public List get(Object obj) {
        return (List) this.map.get(obj);
    }

    @Override // java.util.Map
    public List put(String str, List list) {
        return (List) this.map.put(str, (ArrayList) list);
    }

    @Override // java.util.Map
    public List remove(Object obj) {
        return (List) this.map.remove(obj);
    }

    @Override // java.util.Map
    public void putAll(Map map) {
        for (String str : map.keySet()) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll((Collection) map.get(str));
            this.map.put(str, arrayList);
        }
    }
}
