
/* RmcCapabilitySwitchRequestHandler::sendRadioCapabilityDoneIfNeeded() */

void __thiscall
RmcCapabilitySwitchRequestHandler::sendRadioCapabilityDoneIfNeeded
          (RmcCapabilitySwitchRequestHandler *this)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  RfxMclStatusManager *this_00;
  ulong uVar4;
  RfxIntsData aRStack_58 [28];
  uint local_3c;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  this_00 = (RfxMclStatusManager *)
            RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this,*(int *)(this + 8));
  uVar2 = RfxMclStatusManager::getIntValue(this_00,0x2d,0);
  uVar4 = RmcCapabilitySwitchUtil::isDisableC2kCapability();
  local_3c = uVar2;
  if ((uVar4 & 1) == 0) {
    iVar3 = RatConfig_isC2kSupported();
    if (iVar3 != 0) {
      local_3c = uVar2 | 0x31f0;
    }
  }
  RfxIntsData::RfxIntsData(aRStack_58,(int *)&local_3c,1);
  RfxBaseHandler::sendEvent
            ((RfxBaseHandler *)this,0xcb26,aRStack_58,*(undefined4 *)(this + 0xc),
             *(undefined4 *)(this + 8),0xffffffff,0xffffffff,0,1);
  RfxIntsData::~RfxIntsData(aRStack_58);
  if (*(long *)(lVar1 + 0x28) != local_38) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


