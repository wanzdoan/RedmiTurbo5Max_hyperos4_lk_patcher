
/* RtcCallController::handleSetCallIndicationRequest(android::sp<RfxMessage> const&) */

undefined8 __thiscall
RtcCallController::handleSetCallIndicationRequest(RtcCallController *this,sp *param_1)

{
  uint uVar1;
  long lVar2;
  undefined8 uVar3;
  long lVar4;
  long *plVar5;
  RfxStatusManager *this_00;
  int *piVar6;
  undefined4 local_68 [2];
  undefined1 local_60;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  piVar6 = *(int **)(*(long *)(*(long *)param_1 + 0x48) + 8);
  if (*(int *)(this + 0x268) == 0) {
    uVar1 = piVar6[1];
    lVar4 = RfxGwsdUtils::getCallControlHandler();
    if (lVar4 != 0) {
      RfxController::logD((char *)this,"RtcCC",
                          "handleSetCallIndicationRequest notify gwsd ims call : %d",(ulong)uVar1);
      plVar5 = (long *)RfxGwsdUtils::getCallControlHandler();
      (**(code **)(*plVar5 + 0x98))(plVar5,this,uVar1);
    }
    this_00 = (RfxStatusManager *)
              RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
    local_60 = 0;
    local_68[0] = 1;
    RfxStatusManager::setValue(this_00,0x4b,(RfxVariant *)local_68,false,false);
    RfxVariant::deinit((RfxVariant *)local_68);
  }
  else if (*piVar6 == 0) {
    if (*(long *)(lVar2 + 0x28) == local_38) {
      uVar3 = reportFakeImsRingingCall(this,param_1,piVar6[2]);
      return uVar3;
    }
    goto LAB_00402c18;
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return 1;
  }
LAB_00402c18:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


