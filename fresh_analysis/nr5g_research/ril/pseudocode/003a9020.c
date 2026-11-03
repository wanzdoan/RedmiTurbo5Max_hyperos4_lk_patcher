
/* RtcRatSwitchController::getPreferredNetworkType(android::sp<RfxMessage> const&) */

void __thiscall
RtcRatSwitchController::getPreferredNetworkType(RtcRatSwitchController *this,sp *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  bool bVar3;
  long lVar4;
  undefined *puVar5;
  long *plVar6;
  int iVar7;
  RfxStatusManager *this_00;
  char *pcVar8;
  ulong uVar9;
  int iVar10;
  RtcRatSwitchController *pRVar11;
  uint local_74;
  long *local_70;
  long *local_68;
  RfxIntsData aRStack_60 [24];
  long local_48;
  
  puVar5 = PTR_RFX_SLOT_COUNT_005d2270;
  lVar4 = tpidr_el0;
  local_48 = *(long *)(lVar4 + 0x28);
  local_68 = (long *)0x0;
  if (*(int *)(this + 0x10c) < 1) {
    if (0 < *(int *)PTR_RFX_SLOT_COUNT_005d2270) {
      iVar10 = 0;
      bVar3 = false;
      do {
        this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,iVar10);
        iVar7 = RfxStatusManager::getIntValue(this_00,0x2c,0);
        iVar10 = iVar10 + 1;
        bVar3 = (bool)(0 < iVar7 | bVar3);
      } while (iVar10 < *(int *)puVar5);
      if (bVar3) goto LAB_003a905c;
    }
LAB_003a90d8:
    if (*PTR_sIsInSwitching_005d2bd0 == '\0') {
      pRVar11 = this + 0x78;
      uVar9 = (ulong)*(uint *)pRVar11;
      if (*(uint *)pRVar11 == 0xffffffff) {
        local_74 = getPreferredNetWorkTypeFromSysProp(this,*(int *)(this + 0x40));
        RfxController::logD((char *)this,"RtcRatSwCtrl",
                            "[handleGetPreferredNwType] preferredNetworkTypeFromProp :%d",
                            (ulong)local_74);
        uVar1 = *(undefined4 *)(this + 0x40);
        uVar2 = *(undefined4 *)(*(long *)param_1 + 0x1c);
        RfxIntsData::RfxIntsData(aRStack_60,(int *)&local_74,1);
        goto LAB_003a9158;
      }
      pcVar8 = "[handleGetPreferredNwType] prefNwTypeFromRilj:%d";
    }
    else {
      pcVar8 = "[handleGetPreferredNwType] in RAT switching Desired:%d";
      pRVar11 = this + 0x80;
      uVar9 = (ulong)*(uint *)pRVar11;
    }
  }
  else {
LAB_003a905c:
    pRVar11 = this + 0xc0;
    uVar9 = (ulong)*(uint *)pRVar11;
    if (*(uint *)pRVar11 == 0xffffffff) goto LAB_003a90d8;
    pcVar8 = "[handleGetPreferredNwType] in restricted or call mode:%d";
  }
  RfxController::logD((char *)this,"RtcRatSwCtrl",pcVar8,uVar9);
  uVar1 = *(undefined4 *)(this + 0x40);
  uVar2 = *(undefined4 *)(*(long *)param_1 + 0x1c);
  RfxIntsData::RfxIntsData(aRStack_60,(int *)pRVar11,1);
LAB_003a9158:
  RfxMessage::obtainResponse((RfxMessage *)&local_70,uVar1,uVar2,0,aRStack_60,param_1);
  plVar6 = local_68;
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (plVar6 != local_68) {
    android::sp_report_race();
  }
  local_68 = local_70;
  local_70 = (long *)0x0;
  RfxIntsData::~RfxIntsData(aRStack_60);
  RfxController::responseToRilj((RfxController *)this,(sp *)&local_68);
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (*(long *)(lVar4 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


