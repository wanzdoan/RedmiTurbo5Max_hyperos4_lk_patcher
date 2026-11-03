package com.nxp.osu.osucore;

import android.util.Log;
import com.nxp.id.jsbl.uai.api.objects.CustomerControlImage;
import com.nxp.id.jsbl.uai.api.objects.ExtendedDeliveryPackage;
import com.nxp.id.jsbl.uai.api.objects.ExtendedDeliveryPackageData;
import com.nxp.id.jsbl.uai.api.objects.JCOPControlImage;
import com.nxp.id.jsbl.uai.api.objects.UpdateControlCase;
import com.nxp.id.jsbl.uai.api.objects.UpdateControlData;
import com.nxp.id.jsbl.uai.api.objects.UpdateScripts;
import com.nxp.id.jsbl.uai.api.util.APDUUAI;
import com.nxp.id.jsbl.uai.api.util.UAIAPIException;
import com.nxp.id.jsbl.uai.api.util.Utils;
import com.nxp.id.tp.common.HexString;
import com.nxp.id.tp.common.coding.asn1.Asn1IA5String;
import com.nxp.id.tp.common.coding.asn1.Asn1Sequence;
import com.nxp.osu.SecOsuStatusCode;
import com.nxp.osu.util.OSULogger;
import java.io.File;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
class OsuAsnHelper implements IOsuAsnHelper {
    private static OsuAsnHelper sInstance;
    public static final int OS_TYPE_UNKNOWN = APDUUAI.osuType.OS_TYPE_UNKNOWN.ordinal();
    public static final int OS_TYPE_SELF_UPDATE = APDUUAI.osuType.OS_TYPE_SELF_UPDATE.ordinal();
    public static final int OS_TYPE_SELF_UPDATE_WITH_AMDH = APDUUAI.osuType.OS_TYPE_SELF_UPDATE_WITH_AMDH.ordinal();
    public static final int OS_TYPE_REAL_UPDATE_WITH_AMDH = APDUUAI.osuType.OS_TYPE_REAL_UPDATE_WITH_AMDH.ordinal();
    public static final int OS_TYPE_PATCH_UPDATE = APDUUAI.osuType.OS_TYPE_PATCH_UPDATE.ordinal();
    private static final boolean IS_DEBUG = OSULogger.isDebug();
    private ExtendedDeliveryPackage mExtDelPackage = null;
    private ExtendedDeliveryPackageData mExtDelPackageData = null;
    private JCOPControlImage mJcopControlImage = null;
    private CustomerControlImage mCustomerControlImage = null;
    private UpdateScripts mUpdateScripts = null;
    private List mCtrlDataList = null;

    static {
        sInstance = null;
        sInstance = new OsuAsnHelper();
    }

    private OsuAsnHelper() {
    }

    protected static OsuAsnHelper getInstance() {
        return sInstance;
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public int loadFile(String str) {
        if (str == null) {
            return SecOsuStatusCode.OSU_STATUS_FILE_NOT_FOUND.getValue();
        }
        try {
            File file = new File(str);
            if (!file.exists()) {
                return SecOsuStatusCode.OSU_STATUS_FILE_NOT_FOUND.getValue();
            }
            String string = ((Asn1IA5String) ((Asn1Sequence) Asn1Sequence.parse(HexString.fromFile(file, true), false).get(0)).get(0)).getString();
            if (IS_DEBUG) {
                Log.d("OSU-AsnHelper", "TAG From DER is " + string);
            }
            if (!string.equals("EDP")) {
                Log.e("OSU-AsnHelper", "Tag did not match expected value. Expected JCI, but was " + string + ".");
                return SecOsuStatusCode.OSU_STATUS_INVALID_FILE.getValue();
            }
            if (readExtendedDeliveryPackage(file) && readExtendedDeliveryPackageData() && readCustomerControlImage() && readJCOPControlImage() && readUpdateScripts()) {
                UpdateControlData updateControlData = this.mJcopControlImage.getUpdateControlData();
                if (updateControlData == null) {
                    return SecOsuStatusCode.OSU_STATUS_INVALID_FILE.getValue();
                }
                List updateControlCaseList = updateControlData.getUpdateControlCaseList();
                this.mCtrlDataList = updateControlCaseList;
                if (updateControlCaseList == null) {
                    return SecOsuStatusCode.OSU_STATUS_INVALID_FILE.getValue();
                }
                return SecOsuStatusCode.OSU_STATUS_SUCCESS.getValue();
            }
            return SecOsuStatusCode.OSU_STATUS_INVALID_FILE.getValue();
        } catch (UAIAPIException e) {
            Log.e("OSU-AsnHelper", "Exception in JSBL : " + e.getMessage());
            return SecOsuStatusCode.OSU_STATUS_INVALID_FILE.getValue();
        } catch (Exception e2) {
            Log.e("OSU-AsnHelper", e2.getMessage());
            return SecOsuStatusCode.OSU_STATUS_INVALID_FILE.getValue();
        }
    }

    private boolean readExtendedDeliveryPackage(File file) {
        if (file == null) {
            return false;
        }
        this.mExtDelPackage = new ExtendedDeliveryPackage(file, false);
        if (!IS_DEBUG) {
            return true;
        }
        Log.d("OSU-AsnHelper", "readExtendedDeliveryPackage Success");
        return true;
    }

    private boolean readExtendedDeliveryPackageData() {
        ExtendedDeliveryPackage extendedDeliveryPackage = this.mExtDelPackage;
        if (extendedDeliveryPackage == null) {
            return false;
        }
        ExtendedDeliveryPackageData extendedDeliveryPackageData = extendedDeliveryPackage.getExtendedDeliveryPackageData();
        this.mExtDelPackageData = extendedDeliveryPackageData;
        if (extendedDeliveryPackageData == null) {
            Log.e("OSU-AsnHelper", "read ExtendedDeliveryPackageData Failed");
            return false;
        }
        if (!IS_DEBUG) {
            return true;
        }
        Log.d("OSU-AsnHelper", "read ExtendedDeliveryPackageData Success");
        return true;
    }

    private boolean readCustomerControlImage() {
        ExtendedDeliveryPackage extendedDeliveryPackage = this.mExtDelPackage;
        if (extendedDeliveryPackage == null) {
            return false;
        }
        CustomerControlImage customerControlImage = extendedDeliveryPackage.getCustomerControlImage();
        this.mCustomerControlImage = customerControlImage;
        if (customerControlImage == null) {
            Log.e("OSU-AsnHelper", "readCustomerControlImage Failed");
            return false;
        }
        if (!IS_DEBUG) {
            return true;
        }
        Log.d("OSU-AsnHelper", "read CCI Success");
        return true;
    }

    private boolean readJCOPControlImage() {
        ExtendedDeliveryPackage extendedDeliveryPackage = this.mExtDelPackage;
        if (extendedDeliveryPackage == null) {
            return false;
        }
        JCOPControlImage jCOPControlImage = extendedDeliveryPackage.getDeliveryPackage().getJCOPControlImage();
        this.mJcopControlImage = jCOPControlImage;
        if (jCOPControlImage == null) {
            Log.e("OSU-AsnHelper", "readJCOPControlImage Failed");
            return false;
        }
        if (!IS_DEBUG) {
            return true;
        }
        Log.d("OSU-AsnHelper", "read JCI Success");
        return true;
    }

    private boolean readUpdateScripts() {
        ExtendedDeliveryPackage extendedDeliveryPackage = this.mExtDelPackage;
        if (extendedDeliveryPackage == null) {
            return false;
        }
        UpdateScripts updateScripts = extendedDeliveryPackage.getDeliveryPackage().getUpdateScripts();
        this.mUpdateScripts = updateScripts;
        if (updateScripts == null) {
            Log.e("OSU-AsnHelper", "readUpdateScripts Failed");
            return false;
        }
        if (!IS_DEBUG) {
            return true;
        }
        Log.d("OSU-AsnHelper", "read Update Scripts Success");
        return true;
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public byte[] getSHA256Hash() {
        return this.mExtDelPackageData.getSHA256Hash();
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public byte[] getSHA384Hash() {
        return this.mExtDelPackageData.getSHA384Hash();
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public byte[] getSignaturePlain() {
        return this.mExtDelPackage.getSignaturePlain();
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public byte[] getSignaturePlainECC384() {
        return this.mExtDelPackage.getSignaturePlainECC384();
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public boolean isResetRequiredAfterAction(int i) {
        return this.mExtDelPackageData.isResetRequiredAfterAction(i);
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public int getNumOfSteps() {
        List list = this.mCtrlDataList;
        if (list == null) {
            return -1;
        }
        return list.size();
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public int getOsuType() {
        ExtendedDeliveryPackageData extendedDeliveryPackageData = this.mExtDelPackageData;
        if (extendedDeliveryPackageData == null) {
            return OS_TYPE_UNKNOWN;
        }
        return extendedDeliveryPackageData.getOSUType().ordinal();
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public int getDERFSN() {
        List<UpdateControlCase> list = this.mCtrlDataList;
        int iIntValue = -1;
        if (list == null) {
            return -1;
        }
        for (UpdateControlCase updateControlCase : list) {
            if (((Integer) updateControlCase.getFSN().get(0)).intValue() > iIntValue) {
                iIntValue = ((Integer) updateControlCase.getFSN().get(0)).intValue();
            }
        }
        return iIntValue;
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public int getDERRSN() {
        List<UpdateControlCase> list = this.mCtrlDataList;
        int iIntValue = -1;
        if (list == null) {
            return -1;
        }
        for (UpdateControlCase updateControlCase : list) {
            if (((Integer) updateControlCase.getRSN().get(0)).intValue() < iIntValue || OsuEngine.isInvalid(iIntValue)) {
                iIntValue = ((Integer) updateControlCase.getRSN().get(0)).intValue();
            }
        }
        return iIntValue;
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public int getMatchingAction(int i, int i2, int i3) {
        JCOPControlImage jCOPControlImage = this.mJcopControlImage;
        if (jCOPControlImage == null) {
            return -1;
        }
        int matchingAction = jCOPControlImage.getMatchingAction(i, i2, i3, -1);
        if (matchingAction < 0) {
            Log.e("OSU-AsnHelper", "Not able to get actionIndex");
        }
        return matchingAction;
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public List getJCIApdus(boolean z) {
        return this.mJcopControlImage.toApdu(255, z);
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public List getCCIApdus(boolean z) {
        return this.mCustomerControlImage.toApdu(z);
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public List getApdus(int i) {
        return this.mUpdateScripts.getUpdateScript(i).getApdus();
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public int getAmdhResumeIdx(int i) {
        return this.mUpdateScripts.getUpdateScript(i).getSpecificCommandIndex(1);
    }

    @Override // com.nxp.osu.osucore.IOsuAsnHelper
    public byte[] toByteArray(String str) {
        return Utils.hexStringToByteArray(str);
    }
}
