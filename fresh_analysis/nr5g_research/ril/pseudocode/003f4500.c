
/* RtcRedialController::onRatModeSwitchDone(RedialRatOp) */

void __thiscall RtcRedialController::onRatModeSwitchDone(RtcRedialController *this,uint param_2)

{
  long lVar1;
  long *local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  RfxController::logD((char *)this,"RtcRedialController","onRatModeSwitchDone ratOp %d",
                      (ulong)param_2);
  if (param_2 == 2) {
    *(undefined4 *)(this + 0x74) = 0;
    if (*(long *)(lVar1 + 0x28) == local_38) {
      setEmergencyMode(this,false);
      return;
    }
  }
  else {
    if (param_2 == 1) {
      local_40 = (long *)0x0;
      getRedialRequest();
      if (local_40 != (long *)0x0) {
        RfxController::requestToMcl((RfxController *)this,(sp *)&local_40,false);
        RfxController::logD((char *)this,"RtcRedialController",
                            "setRedialState reddialState:(%d->%d)",(ulong)*(uint *)(this + 0x6c),2);
        if (*(int *)(this + 0x6c) != 2) {
          *(undefined4 *)(this + 0x6c) = 2;
        }
        if (local_40 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_40 + *(long *)(*local_40 + -0x18)),&local_40);
        }
      }
    }
    if (*(long *)(lVar1 + 0x28) == local_38) {
      return;
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


