
/* RtcRatSwitchController::onRequestRadioOnDone(int) */

void __thiscall
RtcRatSwitchController::onRequestRadioOnDone(RtcRatSwitchController *this,int param_1)

{
  int iVar1;
  long lVar2;
  int iVar3;
  long *plVar4;
  long *local_40;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  *(int *)(this + 0x134) = *(int *)(this + 0x134) + 1;
  RfxController::logD((char *)this,"RtcRatSwCtrl","radioPowerOnDone slotid=%d RadioCount=%d",
                      (ulong)(uint)param_1);
  iVar1 = *(int *)(this + 0x134);
  iVar3 = RfxRilUtils::rfxGetSimCount();
  if ((iVar1 == iVar3) && (*(undefined4 *)(this + 0x134) = 0, *(long *)(this + 0x120) != 0)) {
    local_40 = (long *)0x0;
    RfxMessage::obtainResponse((RfxMessage *)&local_40,*(undefined4 *)(this + 300),this + 0x120,0);
    RfxController::responseToRilj((RfxController *)this,(sp *)&local_40);
    plVar4 = *(long **)(this + 0x120);
    if (plVar4 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)plVar4 + *(long *)(*plVar4 + -0x18)),this + 0x120);
    }
    if (plVar4 != *(long **)(this + 0x120)) {
      android::sp_report_race();
    }
    *(undefined8 *)(this + 0x120) = 0;
    *(undefined4 *)(this + 300) = 0;
    if (local_40 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_40 + *(long *)(*local_40 + -0x18)),&local_40);
    }
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


