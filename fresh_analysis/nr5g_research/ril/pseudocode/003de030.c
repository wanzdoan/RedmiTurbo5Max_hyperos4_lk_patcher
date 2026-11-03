
/* RtcCapabilityGetController::updateRadioCapability(int, int) */

void __thiscall
RtcCapabilityGetController::updateRadioCapability
          (RtcCapabilityGetController *this,int param_1,int param_2)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  ulong uVar5;
  RfxStatusManager *pRVar6;
  uint uVar7;
  undefined4 local_98 [2];
  uint local_90;
  long local_68;
  
  lVar1 = tpidr_el0;
  local_68 = *(long *)(lVar1 + 0x28);
  *(int *)(this + 0x48) = param_1;
  this[0x4c] = (RtcCapabilityGetController)0x0;
  uVar5 = RtcCapabilitySwitchUtil::isDisableC2kCapability();
  iVar2 = RfxRilUtils::rfxGetSimCount();
  if ((uVar5 & 1) == 0) {
    if (iVar2 != 0) {
      uVar7 = 0;
      do {
        pRVar6 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar7);
        uVar4 = RfxStatusManager::getIntValue(pRVar6,0x2d,0);
        uVar3 = 0x31f0;
        if (param_1 != uVar7) {
          uVar3 = 0;
        }
        uVar3 = uVar4 & 0xffffce0f | uVar3;
        RfxController::logI((char *)this,"RtcCapa",
                            "updateRadioCapability, cslot=%d, session=%d, capability[%d] = %d",
                            (ulong)(uint)param_1,(ulong)(uint)param_2,(ulong)uVar7,(ulong)uVar3);
        pRVar6 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar7);
        local_98[0] = 2;
        local_90 = uVar3;
        RfxStatusManager::setValue(pRVar6,0x2d,(RfxVariant *)local_98,false,false);
        RfxVariant::deinit((RfxVariant *)local_98);
        sendRadioCapabilityURC(this,uVar7,param_2,uVar4 | 0x31f0);
        uVar7 = uVar7 + 1;
        uVar3 = RfxRilUtils::rfxGetSimCount();
      } while (uVar7 < uVar3);
    }
  }
  else if (iVar2 != 0) {
    uVar7 = 0;
    do {
      pRVar6 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar7);
      uVar3 = RfxStatusManager::getIntValue(pRVar6,0x2d,0);
      RfxController::logI((char *)this,"RtcCapa",
                          "updateRadioCapability, no c2k, session=%d, capability[%d] = %d",
                          (ulong)(uint)param_2,(ulong)uVar7,(ulong)uVar3);
      sendRadioCapabilityURC(this,uVar7,param_2,uVar3);
      uVar7 = uVar7 + 1;
      uVar3 = RfxRilUtils::rfxGetSimCount();
    } while (uVar7 < uVar3);
  }
  if (*(long *)(lVar1 + 0x28) != local_68) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


