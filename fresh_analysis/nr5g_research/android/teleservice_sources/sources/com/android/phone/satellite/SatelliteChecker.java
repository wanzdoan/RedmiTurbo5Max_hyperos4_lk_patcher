package com.android.phone.satellite;

import android.util.Log;
import com.android.phone.MiuiPhoneUtils;
import com.android.phone.SatelliteManager;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class SatelliteChecker {
    private static SatelliteChecker sInstance;
    private SatelliteCheckerBase mBatteryChecker;
    private SatelliteCheckerBase mCameraChecker;
    private SatelliteCheckerBase mCarrierChecker;
    private Map mCheckerMap = new HashMap();
    private SatelliteManager mManager;
    private SatelliteCheckerBase mPrivacyChecker;
    private SatelliteCheckerBase mSaveUserChecker;
    private SatelliteCheckerBase mSimCardChecker;
    private SatelliteCheckerBase mSuperSaveChecker;
    private SatelliteController mTask;
    private SatelliteCheckerBase mUpgradeChecker;

    public static SatelliteChecker getInstance() {
        if (sInstance == null) {
            loge("SatelliteUIController was not yet initialized.");
        }
        return sInstance;
    }

    private SatelliteChecker(SatelliteController satelliteController, SatelliteManager satelliteManager) {
        this.mTask = satelliteController;
        this.mManager = satelliteManager;
        this.mCameraChecker = new CameraChecker();
        UpgradeChecker upgradeChecker = new UpgradeChecker();
        this.mUpgradeChecker = upgradeChecker;
        this.mCameraChecker.setNext(upgradeChecker);
        BatteryChecker batteryChecker = new BatteryChecker();
        this.mBatteryChecker = batteryChecker;
        this.mUpgradeChecker.setNext(batteryChecker);
        PrivacyChecker privacyChecker = new PrivacyChecker();
        this.mPrivacyChecker = privacyChecker;
        this.mBatteryChecker.setNext(privacyChecker);
        CarrierChecker carrierChecker = new CarrierChecker();
        this.mCarrierChecker = carrierChecker;
        this.mPrivacyChecker.setNext(carrierChecker);
        SaveUserShowTipsChecker saveUserShowTipsChecker = new SaveUserShowTipsChecker();
        this.mSaveUserChecker = saveUserShowTipsChecker;
        this.mCarrierChecker.setNext(saveUserShowTipsChecker);
        SimCardChecker simCardChecker = new SimCardChecker();
        this.mSimCardChecker = simCardChecker;
        this.mSaveUserChecker.setNext(simCardChecker);
        SuperSaveModeChecker superSaveModeChecker = new SuperSaveModeChecker();
        this.mSuperSaveChecker = superSaveModeChecker;
        this.mSimCardChecker.setNext(superSaveModeChecker);
        this.mCheckerMap.put(0, this.mBatteryChecker);
        this.mCheckerMap.put(1, this.mPrivacyChecker);
        this.mCheckerMap.put(2, this.mCarrierChecker);
        this.mCheckerMap.put(3, this.mSaveUserChecker);
        this.mCheckerMap.put(4, this.mSimCardChecker);
        this.mCheckerMap.put(5, this.mSuperSaveChecker);
    }

    public static SatelliteChecker make(SatelliteController satelliteController, SatelliteManager satelliteManager) {
        if (sInstance == null) {
            sInstance = new SatelliteChecker(satelliteController, satelliteManager);
        }
        return sInstance;
    }

    public boolean handle() {
        return this.mCameraChecker.handle(this.mTask, this.mManager);
    }

    public boolean handleFrom(int i) {
        return ((SatelliteCheckerBase) this.mCheckerMap.get(Integer.valueOf(i))).handle(this.mTask, this.mManager);
    }

    abstract class SatelliteCheckerBase {
        private SatelliteCheckerBase mNext;

        public abstract boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager);

        private SatelliteCheckerBase(SatelliteChecker satelliteChecker) {
        }

        public final boolean handle(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            boolean zCheck = check(satelliteController, satelliteManager);
            if (zCheck && this.mNext != null) {
                SatelliteChecker.logd("check pass, try next checker: " + this.mNext);
                return this.mNext.handle(satelliteController, satelliteManager);
            }
            SatelliteChecker.logd("check end, handled = " + zCheck + " checker = " + this);
            return zCheck;
        }

        public void setNext(SatelliteCheckerBase satelliteCheckerBase) {
            this.mNext = satelliteCheckerBase;
        }

        public String toString() {
            return "current checker: " + getClass().getSimpleName() + ", next checker: " + this.mNext;
        }
    }

    class CameraChecker extends SatelliteCheckerBase {
        private CameraChecker(SatelliteChecker satelliteChecker) {
            super();
        }

        @Override // com.android.phone.satellite.SatelliteChecker.SatelliteCheckerBase
        public boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            return !satelliteManager.getUIController().checkWhetherCameraIsEnabled(true, satelliteController.getOpenFlag());
        }
    }

    class UpgradeChecker extends SatelliteCheckerBase {
        private UpgradeChecker(SatelliteChecker satelliteChecker) {
            super();
        }

        @Override // com.android.phone.satellite.SatelliteChecker.SatelliteCheckerBase
        public boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            return satelliteManager.getUIController().isSatelliteUpgradingSuccess(satelliteManager.getOTAManager().getFlashState(), satelliteController.getOpenFlag());
        }
    }

    class BatteryChecker extends SatelliteCheckerBase {
        private BatteryChecker(SatelliteChecker satelliteChecker) {
            super();
        }

        @Override // com.android.phone.satellite.SatelliteChecker.SatelliteCheckerBase
        public boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            return satelliteManager.getUIController().isBatteryWork(satelliteController.getOpenFlag());
        }
    }

    class PrivacyChecker extends SatelliteCheckerBase {
        private PrivacyChecker(SatelliteChecker satelliteChecker) {
            super();
        }

        @Override // com.android.phone.satellite.SatelliteChecker.SatelliteCheckerBase
        public boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            return satelliteManager.getUIController().isPrivacyAccept(satelliteController.getOpenFlag());
        }
    }

    class CarrierChecker extends SatelliteCheckerBase {
        private CarrierChecker(SatelliteChecker satelliteChecker) {
            super();
        }

        @Override // com.android.phone.satellite.SatelliteChecker.SatelliteCheckerBase
        public boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            return satelliteManager.getUIController().isCarrierSupport(satelliteController.getOpenFlag());
        }
    }

    class SaveUserShowTipsChecker extends SatelliteCheckerBase {
        private SaveUserShowTipsChecker(SatelliteChecker satelliteChecker) {
            super();
        }

        @Override // com.android.phone.satellite.SatelliteChecker.SatelliteCheckerBase
        public boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            return satelliteManager.getUIController().isUserSaveTips(satelliteController.getOpenFlag());
        }
    }

    class SimCardChecker extends SatelliteCheckerBase {
        private SimCardChecker(SatelliteChecker satelliteChecker) {
            super();
        }

        @Override // com.android.phone.satellite.SatelliteChecker.SatelliteCheckerBase
        public boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            int supportSatelliteSlotId = MiuiPhoneUtils.getSupportSatelliteSlotId();
            if (satelliteController.getEnabledSatelliteSlotId() != -1) {
                satelliteController.setEnabledSatelliteSlotId(satelliteController.getEnabledSatelliteSlotId());
                return true;
            }
            if (supportSatelliteSlotId != 0 && supportSatelliteSlotId != 1) {
                satelliteManager.getUIController().startDialogActivity(satelliteController.getOpenFlag(), 2);
                return false;
            }
            satelliteController.setEnabledSatelliteSlotId(supportSatelliteSlotId);
            return true;
        }
    }

    class SuperSaveModeChecker extends SatelliteCheckerBase {
        private SuperSaveModeChecker(SatelliteChecker satelliteChecker) {
            super();
        }

        @Override // com.android.phone.satellite.SatelliteChecker.SatelliteCheckerBase
        public boolean check(SatelliteController satelliteController, SatelliteManager satelliteManager) {
            return !satelliteManager.getUIController().isSuperSaveMode(satelliteController.getEnabledSatelliteSlotId(), satelliteController.getOpenFlag());
        }
    }

    public void setEnabledSlotId(int i) {
        this.mTask.setEnabledSatelliteSlotId(i);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void logd(String str) {
        Log.d("SatelliteChecker", str);
    }

    private static void loge(String str) {
        Log.e("SatelliteChecker", str);
    }
}
