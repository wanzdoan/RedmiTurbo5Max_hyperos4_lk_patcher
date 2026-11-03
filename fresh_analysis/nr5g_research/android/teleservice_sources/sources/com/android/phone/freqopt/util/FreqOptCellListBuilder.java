package com.android.phone.freqopt.util;

import java.util.ArrayList;
import java.util.List;
import miui.telephony.qms.CellListInfoS;
import miui.telephony.qms.CellListS;
import miui.telephony.qms.CellPropertyCategoryCommonS;
import miui.telephony.qms.CellPropertyCategoryHdrS;
import miui.telephony.qms.CellPropertyCategoryRsS;
import miui.telephony.qms.CellPropertyCategoryS;
import miui.telephony.qms.LocationS;
import miui.telephony.qms.PlmnIdS;
import miui.telephony.qms.UniqueCellInfoS;

/* JADX INFO: loaded from: classes.dex */
public abstract class FreqOptCellListBuilder {
    public static List buildCellListInfo(String str, int i, int i2, int i3, int i4, int i5, int i6) {
        return buildCellListInfo(str, i, i2, i3, 0L, i4, i5, i6);
    }

    public static List buildCellListInfo(String str, int i, int i2, int i3, long j, int i4, int i5, int i6) {
        ArrayList arrayList = new ArrayList();
        CellListS cellListS = new CellListS();
        cellListS.mitigation_type = i4;
        cellListS.reset_flag = true;
        ArrayList arrayList2 = new ArrayList();
        CellListInfoS cellListInfoS = new CellListInfoS();
        cellListInfoS.neighbor_cluster_id = -1;
        cellListInfoS.anchor_cell = 0;
        cellListInfoS.cause_bitmask = i5;
        cellListInfoS.expiry_timer = i6;
        UniqueCellInfoS uniqueCellInfoS = new UniqueCellInfoS();
        PlmnIdS plmnIdS = new PlmnIdS();
        if (str != null && str.length() >= 5) {
            try {
                plmnIdS.mcc = Short.parseShort(str.substring(0, 3));
                plmnIdS.mnc = Short.parseShort(str.substring(3));
                plmnIdS.mncIncludesPcsDigit = str.length() - 3 == 3;
            } catch (NumberFormatException unused) {
                LogUtils.w("Freq_Opt_CellListBuilder", "buildCellListInfo invalid plmn: ".concat(str));
            }
        }
        uniqueCellInfoS.plmn = plmnIdS;
        uniqueCellInfoS.cell_identity = j;
        uniqueCellInfoS.band = i;
        uniqueCellInfoS.freq = i2;
        uniqueCellInfoS.pci = (short) i3;
        cellListInfoS.cell_info = uniqueCellInfoS;
        CellPropertyCategoryS cellPropertyCategoryS = new CellPropertyCategoryS();
        cellPropertyCategoryS.common_valid = false;
        cellPropertyCategoryS.hdr_valid = true;
        cellPropertyCategoryS.rs_valid = false;
        CellPropertyCategoryCommonS cellPropertyCategoryCommonS = new CellPropertyCategoryCommonS();
        cellPropertyCategoryCommonS.priority = -1;
        cellPropertyCategoryCommonS.location_valid = false;
        LocationS locationS = new LocationS();
        locationS.longitude = 0;
        locationS.latitude = 0;
        cellPropertyCategoryCommonS.location = locationS;
        CellPropertyCategoryHdrS cellPropertyCategoryHdrS = new CellPropertyCategoryHdrS();
        cellPropertyCategoryHdrS.service_type = -1;
        cellPropertyCategoryHdrS.load = -1;
        cellPropertyCategoryHdrS.expiry_timer = i6;
        CellPropertyCategoryRsS cellPropertyCategoryRsS = new CellPropertyCategoryRsS();
        cellPropertyCategoryRsS.shared_plmn_len = 0;
        cellPropertyCategoryRsS.shared_plmn = new ArrayList();
        cellPropertyCategoryS.common = cellPropertyCategoryCommonS;
        cellPropertyCategoryS.hdr = cellPropertyCategoryHdrS;
        cellPropertyCategoryS.rs = cellPropertyCategoryRsS;
        cellListInfoS.property = cellPropertyCategoryS;
        arrayList2.add(cellListInfoS);
        cellListS.lte_cell_list_len = (byte) 0;
        cellListS.nr_cell_list_len = (byte) 1;
        cellListS.lte_cell_list = new ArrayList();
        cellListS.nr_cell_list = arrayList2;
        arrayList.add(cellListS);
        return arrayList;
    }
}
