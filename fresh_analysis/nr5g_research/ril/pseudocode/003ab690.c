
/* RtcRatSwitchController::setPreferredNetworkType(int, android::sp<RfxAction> const&) */

void __thiscall
RtcRatSwitchController::setPreferredNetworkType
          (RtcRatSwitchController *this,int param_1,sp *param_2)

{
  long lVar1;
  undefined4 uVar2;
  undefined8 uVar3;
  undefined8 local_48;
  long *local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  if (param_1 == -1) {
    RfxController::logD((char *)this,"RtcRatSwCtrl",
                        "[setRestrictedNetworkMode] leaving restricted mode");
    this[0x110] = (RtcRatSwitchController)0x0;
    if (*(int *)(this + 0xa0) == -1) {
      if (*(int *)(this + 0xc0) == -1) {
        RfxController::logD((char *)this,"RtcRatSwCtrl",
                            "[setRestrictedNetworkMode] No pending record in queue");
        (**(code **)**(undefined8 **)param_2)();
        if (*(long *)(lVar1 + 0x28) == local_38) {
          processPendingRatSwitchRecord(this);
          return;
        }
      }
      else {
        uVar2 = filterPrefNwTypeFromRilj(this,*(int *)(this + 0xc0));
        RfxController::logD((char *)this,"RtcRatSwCtrl",
                            "[setRestrictedNetworkMode] Norm pending record in queue");
        *(undefined4 *)(this + 0xe0) = 0xffffffff;
        if (*(long *)(lVar1 + 0x28) == local_38) {
          switchNwRat(this,uVar2,2,param_2,this + 0xd0);
          return;
        }
      }
      goto LAB_003ab8a0;
    }
    RfxController::logD((char *)this,"RtcRatSwCtrl",
                        "[setRestrictedNetworkMode] Init pending record in queue");
    uVar2 = *(undefined4 *)(this + 0xa0);
    uVar3 = 0;
    *(undefined4 *)(this + 0xe0) = 0xffffffff;
  }
  else {
    RfxController::logD((char *)this,"RtcRatSwCtrl",
                        "[setRestrictedNetworkMode] entering restricted mode: %d",
                        (ulong)(uint)param_1);
    if ((*(int *)(this + 0xa0) == -1) && (*(int *)(this + 0xc0) == -1)) {
      local_48 = 0;
      local_40 = (long *)0x0;
      queueRatSwitchRecord(this,*(undefined4 *)(this + 0xfc),2,&local_40,&local_48);
    }
    this[0x110] = (RtcRatSwitchController)0x1;
    uVar2 = RtcCapabilitySwitchUtil::getRafFromNetworkType(param_1);
    uVar3 = 3;
  }
  local_40 = (long *)0x0;
  switchNwRat(this,uVar2,uVar3,param_2,&local_40);
  if (local_40 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_40 + *(long *)(*local_40 + -0x18)),&local_40);
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
LAB_003ab8a0:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


