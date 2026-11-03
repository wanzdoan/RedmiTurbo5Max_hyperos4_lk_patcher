
/* RtcWpController::onRequestRadioOnDone(int) */

void __thiscall RtcWpController::onRequestRadioOnDone(RtcWpController *this,int param_1)

{
  int iVar1;
  long lVar2;
  RfxStatusManager *this_00;
  undefined4 local_58 [2];
  undefined4 local_50;
  long local_28;
  
  lVar2 = tpidr_el0;
  local_28 = *(long *)(lVar2 + 0x28);
  iVar1 = *(int *)(this + 0x50);
  *(uint *)(this + 0x50) = iVar1 + 1U;
  RfxController::logD((char *)this,"RtcWp",
                      "openRadioCount slotid=%d openRadioCount=%d, mModifyRadioNum=%d",
                      (ulong)(uint)param_1,(ulong)(iVar1 + 1U),(ulong)*(uint *)(this + 0x6c));
  if (*(int *)(this + 0x50) == *(int *)(this + 0x6c)) {
    *(undefined4 *)(this + 0x6c) = 0;
    *(undefined4 *)(this + 0x50) = 0;
    this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
    local_58[0] = 2;
    local_50 = 1;
    RfxStatusManager::setValue(this_00,0x2f,(RfxVariant *)local_58,false,false);
    RfxVariant::deinit((RfxVariant *)local_58);
  }
  if (*(long *)(lVar2 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


