
/* RtcCapabilityGetController::sendRadioCapabilityURC(int, int, int) */

void __thiscall
RtcCapabilityGetController::sendRadioCapabilityURC
          (RtcCapabilityGetController *this,int param_1,int param_2,int param_3)

{
  long lVar1;
  long *local_b0;
  RfxRadioCapabilityData aRStack_a8 [28];
  undefined4 local_8c;
  int iStack_88;
  undefined4 local_84;
  int iStack_80;
  char local_7c [68];
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_7c[0x38] = '\0';
  local_7c[0x39] = '\0';
  local_7c[0x3a] = '\0';
  local_7c[0x3b] = '\0';
  local_7c[0x3c] = '\0';
  local_7c[0x3d] = '\0';
  local_7c[0x3e] = '\0';
  local_7c[0x3f] = '\0';
  local_7c[0x30] = '\0';
  local_7c[0x31] = '\0';
  local_7c[0x32] = '\0';
  local_7c[0x33] = '\0';
  local_7c[0x34] = '\0';
  local_7c[0x35] = '\0';
  local_7c[0x36] = '\0';
  local_7c[0x37] = '\0';
  local_8c = 1;
  local_7c[0x28] = '\0';
  local_7c[0x29] = '\0';
  local_7c[0x2a] = '\0';
  local_7c[0x2b] = '\0';
  local_7c[0x2c] = '\0';
  local_7c[0x2d] = '\0';
  local_7c[0x2e] = '\0';
  local_7c[0x2f] = '\0';
  local_7c[0x20] = '\0';
  local_7c[0x21] = '\0';
  local_7c[0x22] = '\0';
  local_7c[0x23] = '\0';
  local_7c[0x24] = '\0';
  local_7c[0x25] = '\0';
  local_7c[0x26] = '\0';
  local_7c[0x27] = '\0';
  local_7c[0x18] = '\0';
  local_7c[0x19] = '\0';
  local_7c[0x1a] = '\0';
  local_7c[0x1b] = '\0';
  local_7c[0x1c] = '\0';
  local_7c[0x1d] = '\0';
  local_7c[0x1e] = '\0';
  local_7c[0x1f] = '\0';
  local_7c[0x10] = '\0';
  local_7c[0x11] = '\0';
  local_7c[0x12] = '\0';
  local_7c[0x13] = '\0';
  local_7c[0x14] = '\0';
  local_7c[0x15] = '\0';
  local_7c[0x16] = '\0';
  local_7c[0x17] = '\0';
  local_7c[8] = '\0';
  local_7c[9] = '\0';
  local_7c[10] = '\0';
  local_7c[0xb] = '\0';
  local_7c[0xc] = '\0';
  local_7c[0xd] = '\0';
  local_7c[0xe] = '\0';
  local_7c[0xf] = '\0';
  local_7c[0] = '\0';
  local_7c[1] = '\0';
  local_7c[2] = '\0';
  local_7c[3] = '\0';
  local_7c[4] = '\0';
  local_7c[5] = '\0';
  local_7c[6] = '\0';
  local_7c[7] = '\0';
  local_7c[0x40] = '\x01';
  local_7c[0x41] = '\0';
  local_7c[0x42] = '\0';
  local_7c[0x43] = '\0';
  local_84 = 3;
  iStack_88 = param_2;
  iStack_80 = param_3;
  RfxRilUtils::getLogicalModemId(local_7c,0x40,param_1);
  local_b0 = (long *)0x0;
  RfxRadioCapabilityData::RfxRadioCapabilityData(aRStack_a8,&local_8c,0x54);
  RfxMessage::obtainUrc((RfxMessage *)&local_b0,param_1,0xcb2a,aRStack_a8,0);
  RfxRadioCapabilityData::~RfxRadioCapabilityData(aRStack_a8);
  RfxController::responseToRilj((RfxController *)this,(sp *)&local_b0);
  if (local_b0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_b0 + *(long *)(*local_b0 + -0x18)),&local_b0);
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


