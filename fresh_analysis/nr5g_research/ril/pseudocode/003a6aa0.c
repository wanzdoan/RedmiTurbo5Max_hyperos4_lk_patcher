
/* RtcNrSwitchController::switchNrSlot(int) */

void __thiscall RtcNrSwitchController::switchNrSlot(RtcNrSwitchController *this,int param_1)

{
  undefined *puVar1;
  undefined *puVar2;
  long lVar3;
  RfxStatusManager *pRVar4;
  ulong uVar5;
  RfxClassInfo *pRVar6;
  RtcRatSwitchController *this_00;
  long *plVar7;
  long **pplVar8;
  long lVar9;
  int iVar10;
  long *local_88;
  int local_7c;
  long *local_78;
  undefined1 local_70;
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  local_7c = param_1;
  pRVar4 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  uVar5 = RfxStatusManager::getBoolValue(pRVar4,0x56,false);
  puVar1 = PTR_RFX_SLOT_COUNT_005d2270;
  if ((uVar5 & 1) != 0) {
    RfxController::logV((char *)this,"RtcNrSwCtrl",
                        "NR is in switching! mPreferredDataSlot:%d, nrSlot:%d",
                        (ulong)*(uint *)(this + 0x54),(ulong)(uint)param_1);
    *(int *)(this + 0x54) = param_1;
    this[0x58] = (RtcNrSwitchController)0x1;
    goto LAB_003a6d18;
  }
  if (0 < *(int *)PTR_RFX_SLOT_COUNT_005d2270) {
    iVar10 = 0;
    do {
      pRVar4 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar10);
      uVar5 = RfxStatusManager::getBoolValue(pRVar4,0xa0);
      if ((uVar5 & 1) != 0) {
        if (*(int *)puVar1 < 1) goto LAB_003a6c68;
        iVar10 = 0;
        goto LAB_003a6c40;
      }
      iVar10 = iVar10 + 1;
    } while (iVar10 < *(int *)puVar1);
  }
  pRVar4 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  uVar5 = RfxStatusManager::getIntValue(pRVar4,0x34,0);
  RfxController::logV((char *)this,"RtcNrSwCtrl","switchNrSlot:: mainSlot:%d nrSlot:%d",
                      uVar5 & 0xffffffff,(ulong)(uint)param_1);
  pRVar4 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  local_78 = (long *)CONCAT44(local_78._4_4_,1);
  local_70 = 1;
  RfxStatusManager::setValue(pRVar4,0x56,(RfxVariant *)&local_78,false,false);
  RfxVariant::deinit((RfxVariant *)&local_78);
  local_88 = (long *)0x0;
  RfxIntsData::RfxIntsData((RfxIntsData *)&local_78,&local_7c,1);
  RfxMessage::obtainRequest((RfxMessage *)&local_88,uVar5 & 0xffffffff,0xc76c,&local_78,0);
  RfxIntsData::~RfxIntsData((RfxIntsData *)&local_78);
  RfxController::requestToMcl((RfxController *)this,(sp *)&local_88,false);
  *(int *)(this + 0x54) = local_7c;
  if (local_88 == (long *)0x0) goto LAB_003a6d18;
  lVar9 = *local_88;
  pplVar8 = &local_88;
  plVar7 = local_88;
  goto LAB_003a6d0c;
  while (iVar10 = iVar10 + 1, iVar10 < *(int *)puVar1) {
LAB_003a6c40:
    pRVar4 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar10);
    uVar5 = RfxStatusManager::getBoolValue(pRVar4,0xa0);
    if ((uVar5 & 1) != 0) goto LAB_003a6c6c;
  }
LAB_003a6c68:
  iVar10 = -1;
LAB_003a6c6c:
  pRVar6 = (RfxClassInfo *)RtcRatSwitchController::getRtcRatSwitchControllerClassInfo();
  this_00 = (RtcRatSwitchController *)
            RfxController::findController((RfxController *)this,iVar10,pRVar6);
  plVar7 = operator_new(0x38);
  android::RefBase::RefBase((RefBase *)(plVar7 + 5));
  lVar9 = *(long *)this;
  puVar1 = PTR_vtable_005d2b80 + 0x18;
  puVar2 = PTR_vtable_005d2b80 + 0x68;
  plVar7[1] = (long)this;
  *plVar7 = (long)puVar1;
  plVar7[5] = (long)puVar2;
  lVar9 = android::RefBase::createWeak(this + *(long *)(lVar9 + -0x18));
  puVar1 = PTR_onLocalAbortAvailableNetworkDone_005d2b88;
  plVar7[2] = lVar9;
  plVar7[3] = (long)puVar1;
  plVar7[4] = 0;
  local_78 = plVar7;
  android::RefBase::incStrong((void *)((long)plVar7 + *(long *)(*plVar7 + -0x18)));
  RtcRatSwitchController::setLocalAbortAvailableNetwork(this_00,(sp *)&local_78);
  if (local_78 == (long *)0x0) goto LAB_003a6d18;
  lVar9 = *local_78;
  pplVar8 = &local_78;
  plVar7 = local_78;
LAB_003a6d0c:
  android::RefBase::decStrong((RefBase *)((long)plVar7 + *(long *)(lVar9 + -0x18)),pplVar8);
LAB_003a6d18:
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


