
/* RtcCapabilityGetController::updateRadioCapabilityForWMChange(int) */

void __thiscall
RtcCapabilityGetController::updateRadioCapabilityForWMChange
          (RtcCapabilityGetController *this,int param_1)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  uint uVar4;
  uint uVar5;
  uint uVar6;
  RfxStatusManager *pRVar7;
  ulong uVar8;
  uint uVar9;
  undefined4 local_88 [2];
  uint local_80;
  long local_58;
  
  lVar1 = tpidr_el0;
  local_58 = *(long *)(lVar1 + 0x28);
  pRVar7 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  uVar2 = RfxStatusManager::getIntValue(pRVar7,0x34,0);
  uVar8 = RfxRilUtils::isTplusWSupport();
  if ((((uVar8 & 1) == 0) && (iVar3 = RfxRilUtils::getKeep3GMode(), iVar3 == 0)) &&
     (iVar3 = RfxRilUtils::rfxGetSimCount(), iVar3 != 0)) {
    uVar9 = 0;
    do {
      if (uVar2 != uVar9) {
        pRVar7 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar9);
        uVar5 = RfxStatusManager::getIntValue(pRVar7,0x4c,0);
        pRVar7 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar9);
        uVar6 = RfxStatusManager::getIntValue(pRVar7,0x2d,0);
        uVar4 = uVar6 & 0xfffffff7;
        if (param_1 != 2) {
          uVar4 = uVar6 | uVar5 & 8;
        }
        pRVar7 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar9);
        local_88[0] = 2;
        local_80 = uVar4;
        RfxStatusManager::setValue(pRVar7,0x2d,(RfxVariant *)local_88,false,false);
        RfxVariant::deinit((RfxVariant *)local_88);
        uVar8 = RtcCapabilitySwitchUtil::isDisableC2kCapability();
        if ((uVar8 & 1) == 0) {
          uVar4 = uVar4 | 0x31f0;
        }
        sendRadioCapabilityURC(this,uVar9,-1,uVar4);
      }
      uVar9 = uVar9 + 1;
      uVar4 = RfxRilUtils::rfxGetSimCount();
    } while (uVar9 < uVar4);
  }
  if (*(long *)(lVar1 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


