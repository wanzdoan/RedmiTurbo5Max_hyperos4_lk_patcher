
/* RmcCapabilitySwitchRequestHandler::requestGetRadioCapability(android::sp<RfxMclMessage> const&)
    */

void __thiscall
RmcCapabilitySwitchRequestHandler::requestGetRadioCapability
          (RmcCapabilitySwitchRequestHandler *this,sp *param_1)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  RfxMclStatusManager *this_00;
  ulong uVar4;
  long *local_b8;
  long *local_b0;
  RfxRadioCapabilityData aRStack_a8 [24];
  undefined8 local_90;
  undefined4 local_88;
  uint local_84;
  char local_80 [72];
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_80[0x40] = '\x01';
  local_80[0x41] = '\0';
  local_80[0x42] = '\0';
  local_80[0x43] = '\0';
  local_80[8] = '\0';
  local_80[9] = '\0';
  local_80[10] = '\0';
  local_80[0xb] = '\0';
  local_80[0xc] = '\0';
  local_80[0xd] = '\0';
  local_80[0xe] = '\0';
  local_80[0xf] = '\0';
  local_80[0] = '\0';
  local_80[1] = '\0';
  local_80[2] = '\0';
  local_80[3] = '\0';
  local_80[4] = '\0';
  local_80[5] = '\0';
  local_80[6] = '\0';
  local_80[7] = '\0';
  local_80[0x18] = '\0';
  local_80[0x19] = '\0';
  local_80[0x1a] = '\0';
  local_80[0x1b] = '\0';
  local_80[0x1c] = '\0';
  local_80[0x1d] = '\0';
  local_80[0x1e] = '\0';
  local_80[0x1f] = '\0';
  local_80[0x10] = '\0';
  local_80[0x11] = '\0';
  local_80[0x12] = '\0';
  local_80[0x13] = '\0';
  local_80[0x14] = '\0';
  local_80[0x15] = '\0';
  local_80[0x16] = '\0';
  local_80[0x17] = '\0';
  local_80[0x28] = '\0';
  local_80[0x29] = '\0';
  local_80[0x2a] = '\0';
  local_80[0x2b] = '\0';
  local_80[0x2c] = '\0';
  local_80[0x2d] = '\0';
  local_80[0x2e] = '\0';
  local_80[0x2f] = '\0';
  local_80[0x20] = '\0';
  local_80[0x21] = '\0';
  local_80[0x22] = '\0';
  local_80[0x23] = '\0';
  local_80[0x24] = '\0';
  local_80[0x25] = '\0';
  local_80[0x26] = '\0';
  local_80[0x27] = '\0';
  local_80[0x38] = '\0';
  local_80[0x39] = '\0';
  local_80[0x3a] = '\0';
  local_80[0x3b] = '\0';
  local_80[0x3c] = '\0';
  local_80[0x3d] = '\0';
  local_80[0x3e] = '\0';
  local_80[0x3f] = '\0';
  local_80[0x30] = '\0';
  local_80[0x31] = '\0';
  local_80[0x32] = '\0';
  local_80[0x33] = '\0';
  local_80[0x34] = '\0';
  local_80[0x35] = '\0';
  local_80[0x36] = '\0';
  local_80[0x37] = '\0';
  local_88 = 3;
  local_90 = DAT_00252378;
  this_00 = (RfxMclStatusManager *)
            RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this,*(int *)(this + 8));
  uVar2 = RfxMclStatusManager::getIntValue(this_00,0x2d,0);
  uVar4 = RmcCapabilitySwitchUtil::isDisableC2kCapability();
  if ((uVar4 & 1) == 0) {
    iVar3 = RatConfig_isC2kSupported();
    if (iVar3 != 0) {
      uVar2 = uVar2 | 0x31f0;
    }
  }
  RfxBaseHandler::logD
            ((char *)this,"RmcCapa",
             "requestGetRadioCapability, cap[%d] = %d, sizeof(RIL_RadioCapability)=%d",
             (ulong)*(uint *)(this + 8),(ulong)uVar2,0x54);
  local_84 = uVar2;
  RfxRilUtils::getLogicalModemId(local_80,0x40,*(int *)(this + 8));
  local_b0 = (long *)0x0;
  RfxRadioCapabilityData::RfxRadioCapabilityData(aRStack_a8,&local_90,0x54);
  local_b8 = *(long **)param_1;
  if (local_b8 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_b8 + *(long *)(*local_b8 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_b0,0,aRStack_a8,&local_b8);
  if (local_b8 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_b8 + *(long *)(*local_b8 + -0x18)),&local_b8);
  }
  RfxRadioCapabilityData::~RfxRadioCapabilityData(aRStack_a8);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_b0);
  if (local_b0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_b0 + *(long *)(*local_b0 + -0x18)),&local_b0);
  }
  if (*(long *)(lVar1 + 0x28) != local_38) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


