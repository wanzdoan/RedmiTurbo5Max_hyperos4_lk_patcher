
/* RtcRatSwitchController::responseSetPreferredNetworkType(android::sp<RfxMessage> const&) */

void __thiscall
RtcRatSwitchController::responseSetPreferredNetworkType(RtcRatSwitchController *this,sp *param_1)

{
  int iVar1;
  long lVar2;
  undefined *puVar3;
  int iVar4;
  RtcRatSwitchController *pRVar5;
  long *plVar6;
  undefined8 local_60;
  long *local_58;
  undefined4 local_50;
  int iStack_4c;
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  iVar1 = *(int *)(*(long *)param_1 + 0x2c);
  if (iVar1 == 0) {
    updateState(this,(ulong)*(uint *)(this + 0x80),0);
    if ((*(int *)(this + 0x84) == 0) && (*(long *)(this + 0x48) != 0)) {
      local_50 = *(undefined4 *)(this + 0x40);
      local_58 = (long *)(PTR_vtable_005d2be8 + 0x10);
      iStack_4c = 0;
      local_60 = 0;
      RfxSlotList::assignWithList((RfxSlotList *)&local_60,(RfxSlotList *)(this + 0x48));
      RfxBaseSignal::emitSlots
                ((RfxSlotList *)&local_60,RfxSignal2<int,int>::emitEntry,&local_58,(RfxObject *)0x0)
      ;
      RfxSlotList::clear((RfxSlotList *)&local_60);
    }
    if (*(undefined8 **)(this + 0x88) != (undefined8 *)0x0) {
      (**(code **)**(undefined8 **)(this + 0x88))();
    }
    if (*(long *)(this + 0x90) != 0) {
      local_58 = (long *)0x0;
      RfxMessage::obtainResponse((RfxMessage *)&local_58,0,this + 0x90,0);
      RfxController::responseToRilj((RfxController *)this,(sp *)&local_58);
      if (local_58 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
      }
    }
    updatePhoneMode(this,0);
  }
  else {
    RfxController::logV((char *)this,"RtcRatSwCtrl","[updateState] prefNwType: %d, switchResut: %d",
                        (ulong)*(uint *)(this + 0x80),0xffffffff);
    iVar4 = *(int *)(this + 0x84);
    *PTR_sIsInSwitching_005d2bd0 = 0;
    if (((iVar1 == 0x36) && (iVar4 == 2)) && (*(int *)(this + 0xc0) == -1)) {
      pRVar5 = this + 0x90;
      if (*(long *)pRVar5 != 0) {
        local_58 = (long *)0x0;
        RfxMessage::obtainResponse((RfxMessage *)&local_58,0,pRVar5,1);
        RfxController::responseToRilj((RfxController *)this,(sp *)&local_58);
        plVar6 = *(long **)(this + 0x90);
        if (plVar6 != (long *)0x0) {
          android::RefBase::decStrong((RefBase *)((long)plVar6 + *(long *)(*plVar6 + -0x18)),pRVar5)
          ;
        }
        if (plVar6 != *(long **)pRVar5) {
          android::sp_report_race();
        }
        *(undefined8 *)pRVar5 = 0;
        if (local_58 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
        }
      }
      local_60 = 0;
      local_58 = (long *)0x0;
      queueRatSwitchRecord(this,*(undefined4 *)(this + 0x80),2,&local_58,&local_60);
      RfxController::logD((char *)this,"RtcRatSwCtrl","Queue network type: %d in call",
                          (ulong)*(uint *)(this + 0x80));
      iVar4 = *(int *)(this + 0x84);
    }
    if ((iVar4 == 0) && (*(long *)(this + 0x48) != 0)) {
      local_50 = *(undefined4 *)(this + 0x40);
      local_58 = (long *)(PTR_vtable_005d2be8 + 0x10);
      local_60 = 0;
      iStack_4c = iVar1;
      RfxSlotList::assignWithList((RfxSlotList *)&local_60,(RfxSlotList *)(this + 0x48));
      RfxBaseSignal::emitSlots
                ((RfxSlotList *)&local_60,RfxSignal2<int,int>::emitEntry,&local_58,(RfxObject *)0x0)
      ;
      RfxSlotList::clear((RfxSlotList *)&local_60);
    }
    if (*(undefined8 **)(this + 0x88) != (undefined8 *)0x0) {
      (**(code **)**(undefined8 **)(this + 0x88))();
    }
    if (*(long *)(this + 0x90) != 0) {
      local_58 = (long *)0x0;
      RfxMessage::obtainResponse((RfxMessage *)&local_58,iVar1,this + 0x90,0);
      RfxController::responseToRilj((RfxController *)this,(sp *)&local_58);
      if (local_58 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
      }
    }
    iVar1 = *(int *)(this + 0x84);
    if (iVar1 == 0) {
      puVar3 = &DAT_0023e10a;
    }
    else if (iVar1 == 3) {
      puVar3 = &DAT_001fc86c;
    }
    else if (iVar1 == 2) {
      puVar3 = &DAT_001fc868;
    }
    else {
      RfxController::logW((char *)this,"RtcRatSwCtrl","<UNKNOWN> %d");
      puVar3 = &DAT_00207797;
    }
    RfxController::logD((char *)this,"RtcRatSwCtrl","%s switch prefNwType: %d fail!",puVar3,
                        (ulong)*(uint *)(this + 0x80));
  }
  processPendingRatSwitchRecord(this);
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


