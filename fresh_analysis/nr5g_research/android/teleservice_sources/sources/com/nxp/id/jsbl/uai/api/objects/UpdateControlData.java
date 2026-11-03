package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.UAIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import com.nxp.id.tp.common.coding.asn1.Asn1Encodable;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class UpdateControlData implements Asn1Encodable {
    private final List updateControlCases;

    public UpdateControlData(List list) {
        this.updateControlCases = new LinkedList(list);
    }

    public static UpdateControlData decodeFromDER(Asn1Encodable asn1Encodable) throws UAIException {
        Utils.checkParamNotNull(asn1Encodable);
        if (!(asn1Encodable instanceof Asn1Sequence)) {
            throw new UAIException(String.format("Element was expected to be %s but was %s", "Asn1Sequence", asn1Encodable.getClass()));
        }
        List children = ((Asn1Sequence) asn1Encodable).getChildren();
        LinkedList linkedList = new LinkedList();
        Iterator it = children.iterator();
        while (it.hasNext()) {
            linkedList.add(UpdateControlCaseFormatLV.decodeFromDER((Asn1Encodable) it.next()));
        }
        return new UpdateControlData(linkedList);
    }

    public List getUpdateControlCaseList() {
        return new LinkedList(this.updateControlCases);
    }

    public String toString() {
        return toString(0);
    }

    private int calculateTotalUpdateTableNetoSize() {
        Iterator it = this.updateControlCases.iterator();
        int length = 0;
        while (it.hasNext()) {
            length += ((UpdateControlCaseFormatLV) it.next()).encode().length();
        }
        return length;
    }

    private Map calculateUpdateControlDataSizes() {
        HashMap map = new HashMap();
        HashSet<Integer> hashSet = new HashSet();
        Iterator it = this.updateControlCases.iterator();
        while (it.hasNext()) {
            hashSet.addAll(((UpdateControlCase) it.next()).getRSN());
        }
        for (Integer num : hashSet) {
            int length = 0;
            for (UpdateControlCaseFormatLV updateControlCaseFormatLV : this.updateControlCases) {
                if (updateControlCaseFormatLV.getRSN().contains(num)) {
                    length += updateControlCaseFormatLV.encode().length();
                }
            }
            map.put("0x" + Utils.integerToHexString(num.intValue(), 4), Integer.valueOf(length));
        }
        return map;
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public String toString(int i) {
        int i2 = 0;
        int size = 0;
        int size2 = 0;
        int size3 = 0;
        int size4 = 0;
        int iIntValue = 0;
        int length = 0;
        for (UpdateControlCase updateControlCase : this.updateControlCases) {
            if (updateControlCase.getOSID().size() > size) {
                size = updateControlCase.getOSID().size();
            }
            if (updateControlCase.getRSN().size() > size2) {
                size2 = updateControlCase.getRSN().size();
            }
            if (updateControlCase.getCSN().size() > size3) {
                size3 = updateControlCase.getCSN().size();
            }
            if (updateControlCase.getFSN().size() > size4) {
                size4 = updateControlCase.getFSN().size();
            }
            if (updateControlCase.getActionIndex().intValue() > iIntValue) {
                iIntValue = updateControlCase.getActionIndex().intValue();
            }
            if (updateControlCase.getHash().toString().length() > length) {
                length = updateControlCase.getHash().toString().length();
            }
        }
        int i3 = (size * 6) + (size > 1 ? 2 : 0);
        int i4 = (size2 * 6) + (size2 > 1 ? 2 : 0);
        int i5 = (size3 * 6) + (size3 > 1 ? 2 : 0);
        int i6 = (size4 * 6) + (size4 > 1 ? 2 : 0);
        int iMax = Math.max(Integer.toString(iIntValue).length(), 12);
        int iMax2 = Math.max(length, 4);
        StringBuilder sb = new StringBuilder();
        String strRepeat = StringUtil.repeat(' ', i);
        sb.append(strRepeat);
        sb.append("UpdateControlData (values in hex)\n");
        int i7 = i + 2;
        String strRepeat2 = StringUtil.repeat(' ', i7);
        sb.append(strRepeat2);
        sb.append(Utils.centerString("OS ID", i3, " "));
        sb.append(" | ");
        sb.append(Utils.centerString("RSN", i4, " "));
        sb.append(" | ");
        sb.append(Utils.centerString("CSN", i5, " "));
        sb.append(" | ");
        sb.append(Utils.centerString("FSN", i6, " "));
        sb.append(" | ");
        sb.append(Utils.centerString("SCRIPT INDEX", iMax, " "));
        sb.append(" | ");
        sb.append(Utils.centerString("HASH", iMax2, " "));
        sb.append("\n");
        sb.append(strRepeat2);
        sb.append(StringUtil.repeat('-', i3 + i4 + i5 + i6 + iMax + iMax2 + 15));
        for (UpdateControlCaseFormatLV updateControlCaseFormatLV : this.updateControlCases) {
            sb.append("\n");
            int i8 = i5;
            sb.append(updateControlCaseFormatLV.toString(i7, i3, i4, i8, i6, iMax, iMax2));
            i5 = i8;
        }
        Map mapCalculateUpdateControlDataSizes = calculateUpdateControlDataSizes();
        LinkedList linkedList = new LinkedList();
        for (String str : mapCalculateUpdateControlDataSizes.keySet()) {
            int iIntValue2 = ((Integer) mapCalculateUpdateControlDataSizes.get(str)).intValue();
            if (iIntValue2 >= i2) {
                if (iIntValue2 > i2) {
                    linkedList = new LinkedList();
                    i2 = iIntValue2;
                }
                linkedList.add(str);
            }
        }
        sb.append("\n");
        sb.append(strRepeat2);
        sb.append("Total update control data size is ");
        sb.append(calculateTotalUpdateTableNetoSize());
        sb.append(" bytes. Worst case size is ");
        sb.append(i2);
        sb.append(" bytes for RSN(s) ");
        sb.append(linkedList);
        sb.append(".\n");
        sb.append(strRepeat);
        sb.append("End of UpdateControlData");
        return sb.toString();
    }

    private Asn1Sequence buildObject() {
        return new Asn1Sequence(new LinkedList() { // from class: com.nxp.id.jsbl.uai.api.objects.UpdateControlData.1
            private static final long serialVersionUID = 1;

            {
                addAll(UpdateControlData.this.updateControlCases);
            }
        });
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    /* JADX INFO: renamed from: clone, reason: merged with bridge method [inline-methods] */
    public UpdateControlData m4773clone() {
        return new UpdateControlData(this.updateControlCases);
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString encode() {
        return buildObject().encode();
    }

    @Override // com.nxp.id.tp.common.coding.asn1.Asn1Encodable
    public HexString getValue() {
        return buildObject().getValue();
    }
}
