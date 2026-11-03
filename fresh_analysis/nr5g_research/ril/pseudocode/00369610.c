
/* RtcWpController::onRetryResumeTimeOut() */

void __thiscall RtcWpController::onRetryResumeTimeOut(RtcWpController *this)

{
  long lVar1;
  undefined8 uVar2;
  char *pcVar3;
  sp *psVar4;
  long *plVar5;
  RtcWpController *local_60;
  weakref_type *local_58;
  undefined *puStack_50;
  undefined8 local_48;
  undefined8 local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  psVar4 = (sp *)(this + 0x58);
  if (*(long *)psVar4 != 0) {
    RfxTimer::stop(psVar4);
  }
  plVar5 = *(long **)psVar4;
  if (plVar5 != (long *)0x0) {
    android::RefBase::decStrong((RefBase *)((long)plVar5 + *(long *)(*plVar5 + -0x18)),psVar4);
  }
  if (plVar5 != *(long **)psVar4) {
    android::sp_report_race();
  }
  pcVar3 = "onRetryResumeTimeOut count = %d";
  *(undefined8 *)(this + 0x58) = 0;
  RfxController::logD((char *)this,"RtcWp","onRetryResumeTimeOut count = %d",
                      (ulong)*(uint *)(this + 0x60));
  if (*(int *)(this + 0x60) < 0x1e) {
    *(int *)(this + 0x60) = *(int *)(this + 0x60) + 1;
    local_60 = this;
    local_58 = (weakref_type *)android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18))
    ;
    local_48 = 0;
    puStack_50 = PTR_resumeWorldModeChange_005d2720;
    RfxTimer::start((RfxTimer *)&local_60,(RfxCallback0 *)0x3b9aca00,(long)pcVar3);
    plVar5 = *(long **)(this + 0x58);
    if (plVar5 != (long *)0x0) {
      android::RefBase::decStrong((RefBase *)((long)plVar5 + *(long *)(*plVar5 + -0x18)),psVar4);
    }
    if (plVar5 != *(long **)psVar4) {
      android::sp_report_race();
    }
    uVar2 = local_40;
    local_40 = 0;
    *(undefined8 *)psVar4 = uVar2;
    if (local_60 != (RtcWpController *)0x0) {
      android::RefBase::weakref_type::decWeak(local_58,&local_60);
    }
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


