
/* RtcRatSwitchController::onRequestRadioOffDone(int) */

void __thiscall
RtcRatSwitchController::onRequestRadioOffDone(RtcRatSwitchController *this,int param_1)

{
  int iVar1;
  long lVar2;
  int iVar3;
  RfxStatusManager *this_00;
  undefined4 local_68 [2];
  undefined4 local_60;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,param_1);
  local_68[0] = 2;
  local_60 = 6;
  RfxStatusManager::setValue(this_00,0x4e,(RfxVariant *)local_68,false,false);
  RfxVariant::deinit((RfxVariant *)local_68);
  *(int *)(this + 0x134) = *(int *)(this + 0x134) + 1;
  RfxController::logD((char *)this,"RtcRatSwCtrl","radioPowerOffDone slotid=%d RadioCount=%d",
                      (ulong)(uint)param_1);
  iVar1 = *(int *)(this + 0x134);
  iVar3 = RfxRilUtils::rfxGetSimCount();
  if (iVar1 == iVar3) {
    *(undefined4 *)(this + 0x134) = 0;
    RfxController::requestToMcl((RfxController *)this,(sp *)(this + 0x120),false);
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


