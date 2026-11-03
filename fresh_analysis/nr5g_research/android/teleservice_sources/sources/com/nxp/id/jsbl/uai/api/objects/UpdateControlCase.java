package com.nxp.id.jsbl.uai.api.objects;

import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.StringUtil;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class UpdateControlCase {
    private List csn;
    private List fsn;
    private HexString hash;
    private List osid;
    private List rsn;
    private Integer scriptIndex;

    public UpdateControlCase(List list, List list2, List list3, List list4, Integer num, HexString hexString) {
        Utils.checkParamNotNull(list);
        Utils.checkParamNotNull(list2);
        Utils.checkParamNotNull(list3);
        Utils.checkParamNotNull(list4);
        Utils.checkParamNotNull(num);
        Utils.checkParamNotNull(hexString);
        this.osid = new LinkedList(list);
        this.rsn = new LinkedList(list2);
        this.csn = new LinkedList(list3);
        this.fsn = new LinkedList(list4);
        this.scriptIndex = num;
        this.hash = hexString.m4777clone();
    }

    private static String toHexStringList(List list) {
        if (list == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(list.size() == 1 ? "[" : "[ ");
        for (int i = 0; i < list.size(); i++) {
            Integer num = (Integer) list.get(i);
            if (i > 0) {
                sb.append(", ");
            }
            if (num != null) {
                sb.append(Utils.integerToHexString(num.intValue(), 4));
            }
        }
        sb.append(list.size() == 1 ? "]" : " ]");
        return sb.toString();
    }

    public List getOSID() {
        return this.osid;
    }

    public List getRSN() {
        return this.rsn;
    }

    public List getCSN() {
        return this.csn;
    }

    public List getFSN() {
        return this.fsn;
    }

    public Integer getActionIndex() {
        return this.scriptIndex;
    }

    public HexString getHash() {
        return this.hash;
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int i) {
        String strCenterString = Utils.centerString("OS ID", this.osid.size() * 6, " ");
        String strCenterString2 = Utils.centerString("RSN", this.rsn.size() * 6, " ");
        String strCenterString3 = Utils.centerString("CSN", this.csn.size() * 6, " ");
        String strCenterString4 = Utils.centerString("FSN", this.fsn.size() * 6, " ");
        int length = Integer.toString(this.scriptIndex.intValue()).length();
        if (length < 12) {
            length = 12;
        }
        String strCenterString5 = Utils.centerString("SCRIPT INDEX", length, " ");
        String strCenterString6 = Utils.centerString("HASH", getHash().toString().length() * 6, " ");
        return StringUtil.repeat(' ', i) + String.format("%s | %s | %s | %s | %s | %s\n", strCenterString, strCenterString2, strCenterString3, strCenterString4, strCenterString5, strCenterString6) + toString(i, 0, 0, 0, 0, 0, 0);
    }

    public String toString(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        return StringUtil.repeat(' ', i) + Utils.toFormattedString(toHexStringList(this.osid), i2) + " | " + Utils.toFormattedString(toHexStringList(this.rsn), i3) + " | " + Utils.toFormattedString(toHexStringList(this.csn), i4) + " | " + Utils.toFormattedString(toHexStringList(this.fsn), i5) + " | " + Utils.toFormattedString(Integer.toString(this.scriptIndex.intValue()), i6) + " | " + Utils.toFormattedString(getHash().toString(), i7);
    }
}
