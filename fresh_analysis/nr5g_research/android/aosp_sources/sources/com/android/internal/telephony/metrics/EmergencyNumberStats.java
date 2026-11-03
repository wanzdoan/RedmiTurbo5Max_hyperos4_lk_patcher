package com.android.internal.telephony.metrics;

import android.telephony.emergency.EmergencyNumber;
import android.util.SparseIntArray;
import com.android.internal.telephony.NetworkTypeController$$ExternalSyntheticLambda1;
import com.android.internal.telephony.nano.PersistAtomsProto$EmergencyNumbersInfo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/* JADX INFO: loaded from: classes.dex */
public class EmergencyNumberStats {
    private static EmergencyNumberStats sInstance;
    private static final SparseIntArray sRoutesMap = new SparseIntArray() { // from class: com.android.internal.telephony.metrics.EmergencyNumberStats.1
        {
            put(1, 1);
            put(2, 2);
            put(0, 0);
        }
    };
    private static final SparseIntArray sServiceCategoriesMap = new SparseIntArray() { // from class: com.android.internal.telephony.metrics.EmergencyNumberStats.2
        {
            put(0, 0);
            put(1, 1);
            put(2, 2);
            put(4, 3);
            put(8, 4);
            put(16, 5);
            put(32, 6);
            put(64, 7);
        }
    };
    private static final SparseIntArray sSourcesMap = new SparseIntArray() { // from class: com.android.internal.telephony.metrics.EmergencyNumberStats.3
        {
            put(1, 0);
            put(2, 1);
            put(16, 2);
            put(4, 3);
            put(8, 4);
        }
    };

    private EmergencyNumberStats() {
    }

    public static EmergencyNumberStats getInstance() {
        if (sInstance == null) {
            sInstance = new EmergencyNumberStats();
        }
        return sInstance;
    }

    public PersistAtomsProto$EmergencyNumbersInfo[] convertEmergencyNumbersListToProto(List<EmergencyNumber> list, int i, int i2, boolean z) {
        ArrayList arrayList = new ArrayList();
        Iterator<EmergencyNumber> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(convertEmergencyNumberToProto(it.next(), i, i2, z));
        }
        return (PersistAtomsProto$EmergencyNumbersInfo[]) arrayList.toArray(new PersistAtomsProto$EmergencyNumbersInfo[0]);
    }

    private PersistAtomsProto$EmergencyNumbersInfo convertEmergencyNumberToProto(EmergencyNumber emergencyNumber, int i, int i2, boolean z) {
        String number = emergencyNumber.getNumber();
        PersistAtomsProto$EmergencyNumbersInfo persistAtomsProto$EmergencyNumbersInfo = new PersistAtomsProto$EmergencyNumbersInfo();
        persistAtomsProto$EmergencyNumbersInfo.isDbVersionIgnored = z;
        persistAtomsProto$EmergencyNumbersInfo.assetVersion = i;
        persistAtomsProto$EmergencyNumbersInfo.otaVersion = i2;
        persistAtomsProto$EmergencyNumbersInfo.number = number;
        persistAtomsProto$EmergencyNumbersInfo.countryIso = emergencyNumber.getCountryIso();
        persistAtomsProto$EmergencyNumbersInfo.mnc = emergencyNumber.getMnc();
        persistAtomsProto$EmergencyNumbersInfo.route = sRoutesMap.get(emergencyNumber.getEmergencyCallRouting());
        persistAtomsProto$EmergencyNumbersInfo.urns = (String[]) emergencyNumber.getEmergencyUrns().toArray(new String[0]);
        persistAtomsProto$EmergencyNumbersInfo.serviceCategories = getMappedServiceCategories(emergencyNumber.getEmergencyServiceCategories());
        persistAtomsProto$EmergencyNumbersInfo.sources = getMappedSources(emergencyNumber.getEmergencyNumberSources());
        return persistAtomsProto$EmergencyNumbersInfo;
    }

    private int[] getMappedServiceCategories(List list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Stream stream = list.stream();
        SparseIntArray sparseIntArray = sServiceCategoriesMap;
        Objects.requireNonNull(sparseIntArray);
        return stream.map(new EmergencyNumberStats$$ExternalSyntheticLambda0(sparseIntArray)).mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
    }

    private int[] getMappedSources(List list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Stream stream = list.stream();
        SparseIntArray sparseIntArray = sSourcesMap;
        Objects.requireNonNull(sparseIntArray);
        return stream.map(new EmergencyNumberStats$$ExternalSyntheticLambda0(sparseIntArray)).mapToInt(new NetworkTypeController$$ExternalSyntheticLambda1()).toArray();
    }
}
