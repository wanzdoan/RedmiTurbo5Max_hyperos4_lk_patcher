
/* RtcImsConfigController::onRadioStateChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcImsConfigController::onRadioStateChanged
          (RtcImsConfigController *this,undefined8 param_2,int *param_3,int *param_4)

{
  uint uVar1;
  long lVar2;
  int iVar3;
  int iVar4;
  int iVar5;
  int iVar6;
  RfxStatusManager *pRVar7;
  ulong uVar8;
  int *piVar9;
  long lVar10;
  undefined8 uVar11;
  char *local_58;
  undefined8 local_50;
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  if ((*param_3 == 2) && (*param_4 == 2)) {
    uVar1 = param_4[2];
    RfxController::logI((char *)this,"RtcImsConfigController","onRadioStateChanged, state %d->%d",
                        (ulong)(uint)param_3[2],(ulong)uVar1);
    pRVar7 = (RfxStatusManager *)
             RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
    iVar3 = RfxStatusManager::getIntValue(pRVar7,0x28);
    if (uVar1 == 10) {
      iVar4 = getCurrentMccMnc(this);
      if (iVar4 == 0) {
        pRVar7 = (RfxStatusManager *)
                 RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
        uVar8 = RfxStatusManager::getBoolValue(pRVar7,0xc,false);
        if ((iVar3 == 6) && ((uVar8 & 1) != 0)) {
          processDynamicImsSwitch(this);
        }
      }
    }
    else if (uVar1 == 1) goto LAB_003b33d8;
    iVar4 = RfxController::getSlotId((RfxController *)this);
    iVar5 = getCurrentMccMnc(this);
    local_58 = (char *)0x0;
    pRVar7 = (RfxStatusManager *)
             RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
    piVar9 = (int *)RfxStatusManager::getValue(pRVar7,0x73);
    if (*piVar9 == 4) {
      android::String8::String8((String8 *)&local_58,(String8 *)(piVar9 + 2));
      if ((((*(int *)(this + 0x1a0) + 1U < 2) || (lVar10 = android::String8::length(), lVar10 == 0))
          || (iVar6 = strcmp(local_58,*(char **)(this + 0x1a8)), iVar6 != 0)) ||
         (iVar5 != *(int *)(this + 0x1a0))) {
        if ((uVar1 != 10) || (iVar5 != 0)) {
LAB_003b338c:
          resetFeatureSendCmd(this);
          goto LAB_003b3394;
        }
        if (iVar3 != 1) {
          pRVar7 = (RfxStatusManager *)
                   RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
          uVar8 = RfxStatusManager::getBoolValue(pRVar7,0xc,false);
          if ((iVar3 != 6) || ((uVar8 & 1) == 0)) goto LAB_003b338c;
        }
        RfxController::logD((char *)this,"RtcImsConfigController",
                            "onRadioStateChanged, sim absent||notready and radio on, triggerImsCfgCommand"
                           );
        triggerImsCfgCommand(this,iVar4);
        android::String8::~String8((String8 *)&local_58);
      }
      else {
        RfxController::logD((char *)this,"RtcImsConfigController",
                            "onRadioStateChanged, same sim, triggerImsCfgCommand");
        triggerImsCfgCommand(this,iVar4);
LAB_003b3394:
        android::String8::~String8((String8 *)&local_58);
        if (uVar1 != 10) goto LAB_003b33d8;
      }
      if ((iVar3 - 1U < 4) || (iVar3 == 7)) {
        this[0x180] = (RtcImsConfigController)0x1;
        RfxController::logI((char *)this,"RtcImsConfigController",
                            "onRadioStateChanged, mECCAllowSendCmd = true");
      }
LAB_003b33d8:
      if (*(long *)(lVar2 + 0x28) == local_48) {
        return;
      }
                    /* WARNING: Subroutine does not return */
      __stack_chk_fail();
    }
    uVar8 = __rfx_is_gt_mode();
    if ((uVar8 & 1) == 0) {
      uVar11 = 0x128;
      goto LAB_003b352c;
    }
    local_50 = 0;
    android::String8::format("%s%s",&local_50,&DAT_001fbebf,"RFX_ASSERT");
    uVar11 = 0x128;
  }
  else {
    uVar8 = __rfx_is_gt_mode();
    if ((uVar8 & 1) == 0) {
      uVar11 = 0x113;
LAB_003b352c:
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,uVar11);
      goto LAB_003b3530;
    }
    local_50 = 0;
    android::String8::format("%s%s",&local_50,&DAT_001fbebf,"RFX_ASSERT");
    uVar11 = 0x113;
  }
  mtkLogE(local_50,"RFX_ASSERT:%s, %d",
          "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
          ,uVar11);
  android::String8::~String8((String8 *)&local_50);
LAB_003b3530:
  mtkAssert(0);
                    /* WARNING: Subroutine does not return */
  exit(0);
}


