
/* RtcImsConfigController::handleGetProvisionResponse(android::sp<RfxMessage> const&) */

void __thiscall
RtcImsConfigController::handleGetProvisionResponse(RtcImsConfigController *this,sp *param_1)

{
  int iVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  long lVar4;
  long *plVar5;
  RtcImsConfigController *pRVar6;
  char *__ptr;
  RtcImsConfigController *pRVar7;
  RtcImsConfigController *pRVar8;
  RtcImsConfigController *pRVar9;
  long *local_70;
  long *local_68;
  RfxStringData aRStack_60 [24];
  long local_48;
  
  lVar4 = tpidr_el0;
  local_48 = *(long *)(lVar4 + 0x28);
  iVar1 = *(int *)(*(long *)param_1 + 0x2c);
  if (iVar1 == 0) {
    if (*(long *)(lVar4 + 0x28) == local_48) {
      RfxController::logD((char *)this,"RtcImsConfigController",
                          "handleGetProvisionResponse, wait for URC to response.");
      return;
    }
    goto LAB_003b7acc;
  }
  if (iVar1 == 0x20c) {
    local_68 = (long *)0x0;
    iVar1 = *(int *)(this + 400);
    pRVar7 = *(RtcImsConfigController **)(this + 0x140);
    if (*(RtcImsConfigController **)(this + 0x140) == (RtcImsConfigController *)0x0) {
      pRVar8 = this + 0x140;
      pRVar9 = this + 0x140;
    }
    else {
      do {
        while (pRVar6 = pRVar7, pRVar8 = pRVar6, *(int *)(pRVar6 + 0x20) <= iVar1) {
          if (iVar1 <= *(int *)(pRVar6 + 0x20)) goto LAB_003b78d4;
          pRVar7 = *(RtcImsConfigController **)(pRVar6 + 8);
          if (*(RtcImsConfigController **)(pRVar6 + 8) == (RtcImsConfigController *)0x0) {
            pRVar9 = pRVar6 + 8;
            goto LAB_003b7880;
          }
        }
        pRVar7 = *(RtcImsConfigController **)pRVar6;
        pRVar9 = pRVar6;
      } while (*(RtcImsConfigController **)pRVar6 != (RtcImsConfigController *)0x0);
    }
LAB_003b7880:
    pRVar6 = operator_new(0x40);
    *(int *)(pRVar6 + 0x20) = iVar1;
    *(undefined8 *)(pRVar6 + 0x30) = 0;
    *(undefined8 *)(pRVar6 + 0x38) = 0;
    *(undefined8 *)(pRVar6 + 0x28) = 0;
    *(undefined8 *)pRVar6 = 0;
    *(undefined8 *)(pRVar6 + 8) = 0;
    *(RtcImsConfigController **)(pRVar6 + 0x10) = pRVar8;
    *(RtcImsConfigController **)pRVar9 = pRVar6;
    pRVar7 = pRVar6;
    if (**(long **)(this + 0x138) != 0) {
      *(long *)(this + 0x138) = **(long **)(this + 0x138);
      pRVar7 = *(RtcImsConfigController **)pRVar9;
    }
    std::__tree_balance_after_insert_abi_nn180000_<std::__tree_node_base<void*>*>
              (*(__tree_node_base **)(this + 0x140),(__tree_node_base *)pRVar7);
    *(long *)(this + 0x148) = *(long *)(this + 0x148) + 1;
LAB_003b78d4:
    pRVar7 = pRVar6 + 0x29;
    if (((byte)pRVar6[0x28] & 1) != 0) {
      pRVar7 = *(RtcImsConfigController **)(pRVar6 + 0x38);
    }
    __ptr = strdup((char *)pRVar7);
    if (__ptr == (char *)0x0) {
      RfxController::logE((char *)this,"RtcImsConfigController",
                          "handleGetProvisionResponse(), data is NULL!");
      uVar2 = *(undefined4 *)(*(long *)(this + 0x198) + 0x24);
      uVar3 = *(undefined4 *)(*(long *)(this + 0x198) + 0x1c);
      RfxVoidData::RfxVoidData((RfxVoidData *)aRStack_60);
      RfxMessage::obtainResponse((RfxMessage *)&local_70,uVar2,uVar3,0x2c,aRStack_60,this + 0x198);
      plVar5 = local_68;
      if (local_68 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
      }
      if (plVar5 != local_68) {
        android::sp_report_race();
      }
      local_68 = local_70;
      local_70 = (long *)0x0;
      goto LAB_003b7a74;
    }
    uVar2 = *(undefined4 *)(*(long *)(this + 0x198) + 0x24);
    uVar3 = *(undefined4 *)(*(long *)(this + 0x198) + 0x1c);
    RfxStringData::RfxStringData(aRStack_60,__ptr);
    RfxMessage::obtainResponse((RfxMessage *)&local_70,uVar2,uVar3,0,aRStack_60,this + 0x198);
    plVar5 = local_68;
    if (local_68 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
    }
    if (plVar5 != local_68) {
      android::sp_report_race();
    }
    local_68 = local_70;
    local_70 = (long *)0x0;
    RfxStringData::~RfxStringData(aRStack_60);
    RfxController::responseToRilj((RfxController *)this,(sp *)&local_68);
    free(__ptr);
  }
  else {
    local_68 = (long *)0x0;
    uVar2 = *(undefined4 *)(*(long *)(this + 0x198) + 0x24);
    uVar3 = *(undefined4 *)(*(long *)(this + 0x198) + 0x1c);
    RfxVoidData::RfxVoidData((RfxVoidData *)aRStack_60);
    RfxMessage::obtainResponse((RfxMessage *)&local_68,uVar2,uVar3,iVar1,aRStack_60,this + 0x198);
LAB_003b7a74:
    RfxVoidData::~RfxVoidData((RfxVoidData *)aRStack_60);
    RfxController::responseToRilj((RfxController *)this,(sp *)&local_68);
  }
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (*(long *)(lVar4 + 0x28) == local_48) {
    return;
  }
LAB_003b7acc:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


