
/* RtcNrSwitchController::switchNrSlot() */

void __thiscall RtcNrSwitchController::switchNrSlot(RtcNrSwitchController *this)

{
  undefined *puVar1;
  undefined *puVar2;
  uint uVar3;
  uint uVar4;
  long lVar5;
  int iVar6;
  uint uVar7;
  RfxStatusManager *pRVar8;
  ulong uVar9;
  long lVar10;
  RfxClassInfo *pRVar11;
  RtcRatSwitchController *this_00;
  long *plVar12;
  long **pplVar13;
  long *local_d0;
  uint local_c4;
  long *local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined4 uStack_78;
  undefined4 local_74;
  undefined4 uStack_70;
  undefined8 uStack_6c;
  long local_58;
  
  pplVar13 = &local_d0;
  lVar5 = tpidr_el0;
  local_58 = *(long *)(lVar5 + 0x28);
  pRVar8 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  uVar9 = RfxStatusManager::getBoolValue(pRVar8,0x56,false);
  puVar1 = PTR_RFX_SLOT_COUNT_005d2270;
  if ((uVar9 & 1) != 0) {
    this[0x58] = (RtcNrSwitchController)0x1;
    pRVar8 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
    iVar6 = RfxStatusManager::getIntValue(pRVar8,0x34,0);
    pRVar8 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
    uVar7 = RfxStatusManager::getIntValue(pRVar8,0x47,-1);
    uVar3 = (uint)(uVar7 == 0);
    if (iVar6 != 1) {
      uVar3 = uVar7;
    }
    RfxController::logV((char *)this,"RtcNrSwCtrl",
                        "NR is in switching! mPreferredDataSlot:%d, defaultDataSim:%d",
                        (ulong)*(uint *)(this + 0x54),(ulong)uVar3);
    *(uint *)(this + 0x54) = uVar3;
    goto LAB_003a6998;
  }
  if (0 < *(int *)PTR_RFX_SLOT_COUNT_005d2270) {
    iVar6 = 0;
    do {
      pRVar8 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar6);
      uVar9 = RfxStatusManager::getBoolValue(pRVar8,0xa0);
      if ((uVar9 & 1) != 0) {
        if (*(int *)puVar1 < 1) goto LAB_003a68e8;
        iVar6 = 0;
        goto LAB_003a68c0;
      }
      iVar6 = iVar6 + 1;
    } while (iVar6 < *(int *)puVar1);
  }
  pRVar8 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  iVar6 = RfxStatusManager::getIntValue(pRVar8,0x34,0);
  pRVar8 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  uVar7 = RfxStatusManager::getIntValue(pRVar8,0x47,0);
  uVar3 = (uint)(uVar7 == 0);
  if (iVar6 != 1) {
    uVar3 = uVar7;
  }
  uVar4 = *(uint *)(this + 0x54);
  uStack_6c = 0;
  uStack_70 = 0;
  uStack_a8 = 0;
  local_b0 = 0;
  uStack_98 = 0;
  uStack_a0 = 0;
  uStack_88 = 0;
  local_90 = 0;
  uStack_78 = 0;
  local_74 = 0;
  uStack_80 = 0;
  uStack_b8 = 0;
  local_c0 = (long *)0x0;
  local_c4 = uVar3;
  rfx_property_get("persist.vendor.radio.nrslot",&local_c0,&DAT_002240c5);
  lVar10 = strtol((char *)&local_c0,(char **)0x0,10);
  *(int *)(this + 0x50) = (int)lVar10 + -1;
  RfxController::logV((char *)this,"RtcNrSwCtrl","getNrSlot:: mNrSlot:%d");
  RfxController::logV((char *)this,"RtcNrSwCtrl",
                      "switchNrSlot:defaultSim:%d, PreferredSlot:%d, OriginalSim:%d, NrSlot:%d",
                      (ulong)uVar3,(ulong)uVar4,(ulong)uVar7,(ulong)*(uint *)(this + 0x50));
  if (uVar7 == 0xffffffff) goto LAB_003a6998;
  if (*(uint *)(this + 0x54) == uVar3) {
    uStack_6c = 0;
    uStack_70 = 0;
    uStack_a8 = 0;
    local_b0 = 0;
    uStack_98 = 0;
    uStack_a0 = 0;
    uStack_88 = 0;
    local_90 = 0;
    uStack_78 = 0;
    local_74 = 0;
    uStack_80 = 0;
    uStack_b8 = 0;
    local_c0 = (long *)0x0;
    rfx_property_get("persist.vendor.radio.nrslot",&local_c0,&DAT_002240c5);
    lVar10 = strtol((char *)&local_c0,(char **)0x0,10);
    *(int *)(this + 0x50) = (int)lVar10 + -1;
    RfxController::logV((char *)this,"RtcNrSwCtrl","getNrSlot:: mNrSlot:%d");
    if (*(uint *)(this + 0x50) == uVar7) goto LAB_003a6998;
  }
  pRVar8 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  local_c0 = (long *)CONCAT44(local_c0._4_4_,1);
  uStack_b8 = CONCAT71(uStack_b8._1_7_,1);
  RfxStatusManager::setValue(pRVar8,0x56,(RfxVariant *)&local_c0,false,false);
  RfxVariant::deinit((RfxVariant *)&local_c0);
  local_d0 = (long *)0x0;
  RfxIntsData::RfxIntsData((RfxIntsData *)&local_c0,(int *)&local_c4,1);
  RfxMessage::obtainRequest((RfxMessage *)&local_d0,iVar6,0xc76c,&local_c0,0);
  RfxIntsData::~RfxIntsData((RfxIntsData *)&local_c0);
  RfxController::requestToMcl((RfxController *)this,(sp *)&local_d0,false);
  *(uint *)(this + 0x54) = local_c4;
  if (local_d0 == (long *)0x0) goto LAB_003a6998;
  lVar10 = *local_d0;
  plVar12 = local_d0;
  goto LAB_003a698c;
  while (iVar6 = iVar6 + 1, iVar6 < *(int *)puVar1) {
LAB_003a68c0:
    pRVar8 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar6);
    uVar9 = RfxStatusManager::getBoolValue(pRVar8,0xa0);
    if ((uVar9 & 1) != 0) goto LAB_003a68ec;
  }
LAB_003a68e8:
  iVar6 = -1;
LAB_003a68ec:
  pRVar11 = (RfxClassInfo *)RtcRatSwitchController::getRtcRatSwitchControllerClassInfo();
  this_00 = (RtcRatSwitchController *)
            RfxController::findController((RfxController *)this,iVar6,pRVar11);
  plVar12 = operator_new(0x38);
  android::RefBase::RefBase((RefBase *)(plVar12 + 5));
  lVar10 = *(long *)this;
  puVar1 = PTR_vtable_005d2b80 + 0x18;
  puVar2 = PTR_vtable_005d2b80 + 0x68;
  plVar12[1] = (long)this;
  *plVar12 = (long)puVar1;
  plVar12[5] = (long)puVar2;
  lVar10 = android::RefBase::createWeak(this + *(long *)(lVar10 + -0x18));
  puVar1 = PTR_onLocalAbortAvailableNetworkDone_005d2b88;
  plVar12[2] = lVar10;
  plVar12[3] = (long)puVar1;
  plVar12[4] = 0;
  local_c0 = plVar12;
  android::RefBase::incStrong((void *)((long)plVar12 + *(long *)(*plVar12 + -0x18)));
  RtcRatSwitchController::setLocalAbortAvailableNetwork(this_00,(sp *)&local_c0);
  if (local_c0 == (long *)0x0) goto LAB_003a6998;
  lVar10 = *local_c0;
  pplVar13 = &local_c0;
  plVar12 = local_c0;
LAB_003a698c:
  android::RefBase::decStrong((RefBase *)((long)plVar12 + *(long *)(lVar10 + -0x18)),pplVar13);
LAB_003a6998:
  if (*(long *)(lVar5 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


