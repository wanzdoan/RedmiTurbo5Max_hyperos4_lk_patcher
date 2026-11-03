package com.android.internal.telephony.phonenumbers.prefixmapper;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.SortedMap;

/* JADX INFO: loaded from: classes.dex */
class DefaultMapStorage extends PhonePrefixMapStorageStrategy {
    private String[] descriptions;
    private int[] phoneNumberPrefixes;

    @Override // com.android.internal.telephony.phonenumbers.prefixmapper.PhonePrefixMapStorageStrategy
    public int getPrefix(int i) {
        return this.phoneNumberPrefixes[i];
    }

    @Override // com.android.internal.telephony.phonenumbers.prefixmapper.PhonePrefixMapStorageStrategy
    public String getDescription(int i) {
        return this.descriptions[i];
    }

    @Override // com.android.internal.telephony.phonenumbers.prefixmapper.PhonePrefixMapStorageStrategy
    public void readFromSortedMap(SortedMap<Integer, String> sortedMap) {
        int size = sortedMap.size();
        this.numOfEntries = size;
        this.phoneNumberPrefixes = new int[size];
        this.descriptions = new String[size];
        Iterator<Integer> it = sortedMap.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            int iIntValue = it.next().intValue();
            this.phoneNumberPrefixes[i] = iIntValue;
            this.possibleLengths.add(Integer.valueOf(((int) Math.log10(iIntValue)) + 1));
            i++;
        }
        sortedMap.values().toArray(this.descriptions);
    }

    @Override // com.android.internal.telephony.phonenumbers.prefixmapper.PhonePrefixMapStorageStrategy
    public void readExternal(ObjectInput objectInput) throws IOException {
        int i = objectInput.readInt();
        this.numOfEntries = i;
        int[] iArr = this.phoneNumberPrefixes;
        if (iArr == null || iArr.length < i) {
            this.phoneNumberPrefixes = new int[i];
        }
        String[] strArr = this.descriptions;
        if (strArr == null || strArr.length < i) {
            this.descriptions = new String[i];
        }
        for (int i2 = 0; i2 < this.numOfEntries; i2++) {
            this.phoneNumberPrefixes[i2] = objectInput.readInt();
            this.descriptions[i2] = objectInput.readUTF();
        }
        int i3 = objectInput.readInt();
        this.possibleLengths.clear();
        for (int i4 = 0; i4 < i3; i4++) {
            this.possibleLengths.add(Integer.valueOf(objectInput.readInt()));
        }
    }

    @Override // com.android.internal.telephony.phonenumbers.prefixmapper.PhonePrefixMapStorageStrategy
    public void writeExternal(ObjectOutput objectOutput) throws IOException {
        objectOutput.writeInt(this.numOfEntries);
        for (int i = 0; i < this.numOfEntries; i++) {
            objectOutput.writeInt(this.phoneNumberPrefixes[i]);
            objectOutput.writeUTF(this.descriptions[i]);
        }
        objectOutput.writeInt(this.possibleLengths.size());
        Iterator<Integer> it = this.possibleLengths.iterator();
        while (it.hasNext()) {
            objectOutput.writeInt(it.next().intValue());
        }
    }
}
