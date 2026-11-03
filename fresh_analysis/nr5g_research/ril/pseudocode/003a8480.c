
/* RtcRatSwitchController::onRestrictedModeChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcRatSwitchController::onRestrictedModeChanged
          (RtcRatSwitchController *this,uint param_1,int *param_3,int *param_4)

{
  bool bVar1;
  long lVar2;
  undefined *puVar3;
  int iVar4;
  RfxStatusManager *this_00;
  ulong uVar5;
  undefined8 uVar6;
  uint uVar7;
  int iVar8;
  uint uVar9;
  undefined8 local_50;
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  if (param_1 == 0x37) {
    if ((*param_3 == 1) && (*param_4 == 1)) {
      uVar7 = (uint)*(byte *)(param_3 + 2);
      uVar9 = (uint)*(byte *)(param_4 + 2);
      if (*(byte *)(param_3 + 2) == 0) {
LAB_003a84fc:
        if ((int)uVar9 < 1) goto LAB_003a8510;
        iVar8 = *(int *)(this + 0x10c) + 1;
LAB_003a8524:
        *(int *)(this + 0x10c) = iVar8;
      }
      else {
LAB_003a8510:
        if ((0 < (int)uVar7) && (uVar9 == 0)) {
          iVar8 = *(int *)(this + 0x10c) + -1;
          goto LAB_003a8524;
        }
      }
      RfxController::logD((char *)this,"RtcRatSwCtrl",
                          "[onRestrictedModeChanged] key:%d oldMode:%d, mode:%d, Restricted Count:%d"
                          ,(ulong)param_1,(ulong)uVar7,(ulong)uVar9);
      puVar3 = PTR_RFX_SLOT_COUNT_005d2270;
      if ((((int)uVar7 < 1) || (uVar9 != 0)) || (*(int *)(this + 0x10c) != 0)) {
LAB_003a85b8:
        if (*(long *)(lVar2 + 0x28) == local_48) {
          return;
        }
      }
      else {
        if (0 < *(int *)PTR_RFX_SLOT_COUNT_005d2270) {
          iVar8 = 0;
          bVar1 = false;
          do {
            this_00 = (RfxStatusManager *)
                      RfxController::getStatusManager((RfxController *)this,iVar8);
            iVar4 = RfxStatusManager::getIntValue(this_00,0x2c,0);
            iVar8 = iVar8 + 1;
            bVar1 = (bool)(0 < iVar4 | bVar1);
          } while (iVar8 < *(int *)puVar3);
          if (bVar1) goto LAB_003a85b8;
        }
        RfxController::logD((char *)this,"RtcRatSwCtrl",
                            "[onRestrictedModeChanged] do pending record in queue");
        if (*(long *)(lVar2 + 0x28) == local_48) {
          processPendingRatSwitchRecord(this);
          return;
        }
      }
                    /* WARNING: Subroutine does not return */
      __stack_chk_fail();
    }
    uVar5 = __rfx_is_gt_mode(0x37);
    if ((uVar5 & 1) == 0) {
      uVar6 = 0x10c;
LAB_003a8700:
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,uVar6);
      goto LAB_003a8704;
    }
    local_50 = 0;
    android::String8::format("%s%s",&local_50,&DAT_001fbebf,"RFX_ASSERT");
    uVar6 = 0x10c;
  }
  else {
    if ((*param_3 == 2) && (*param_4 == 2)) {
      uVar7 = param_3[2];
      uVar9 = param_4[2];
      if (uVar7 == 0) goto LAB_003a84fc;
      goto LAB_003a8510;
    }
    uVar5 = __rfx_is_gt_mode((ulong)param_1);
    if ((uVar5 & 1) == 0) {
      uVar6 = 0x113;
      goto LAB_003a8700;
    }
    local_50 = 0;
    android::String8::format("%s%s",&local_50,&DAT_001fbebf,"RFX_ASSERT");
    uVar6 = 0x113;
  }
  mtkLogE(local_50,"RFX_ASSERT:%s, %d",
          "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
          ,uVar6);
  android::String8::~String8((String8 *)&local_50);
LAB_003a8704:
  mtkAssert(0);
                    /* WARNING: Subroutine does not return */
  exit(0);
}


