
/* RmcNetworkUrcHandler::handleNrSysInfoChanged(android::sp<RfxMclMessage> const&) */

void __thiscall RmcNetworkUrcHandler::handleNrSysInfoChanged(RmcNetworkUrcHandler *this,sp *param_1)

{
  long lVar1;
  undefined *puVar2;
  undefined *puVar3;
  long *plVar4;
  int iVar5;
  uint uVar6;
  char *pcVar7;
  ulong uVar8;
  long lVar9;
  RfxAtLine *this_00;
  long *local_80;
  long *local_78;
  int local_6c;
  RfxIntsData aRStack_68 [24];
  undefined8 local_50;
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  local_6c = 0;
  local_50 = 0;
  this_00 = *(RfxAtLine **)(*(long *)param_1 + 0x30);
  local_78 = (long *)0x0;
  RfxAtLine::atTokStart(this_00,&local_6c);
  if ((local_6c < 0) ||
     (iVar5 = RfxAtLine::atTokNextint(this_00,&local_6c), puVar3 = PTR_nrSysInfoMutex_005d3d38,
     local_6c < 0)) {
    uVar8 = RfxBaseHandler::logE((char *)this,"RmcNwHdlr","[%s] URC error","handleNrSysInfoChanged")
    ;
  }
  else {
    pthread_mutex_lock((pthread_mutex_t *)
                       (PTR_nrSysInfoMutex_005d3d38 + (ulong)*(uint *)(this + 8) * 0x28));
    puVar2 = PTR_nrSysInfo_005d3d30;
    pcVar7 = "handleNrSysInfoChanged";
    RfxBaseHandler::logD
              ((char *)this,"RmcNwHdlr","[%s] current NR sys info:%d, %d","handleNrSysInfoChanged",
               (ulong)**(uint **)(PTR_nrSysInfo_005d3d30 + (ulong)*(uint *)(this + 8) * 8),
               (ulong)(*(uint **)(PTR_nrSysInfo_005d3d30 + (ulong)*(uint *)(this + 8) * 8))[1]);
    uVar8 = (ulong)*(uint *)(this + 8);
    lVar9 = *(long *)(puVar2 + uVar8 * 8);
    if (*(int *)(lVar9 + 4) != iVar5) {
      *(int *)(lVar9 + 4) = iVar5;
      uVar6 = *(uint *)(this + 8);
      local_50 = **(undefined8 **)(puVar2 + (ulong)uVar6 * 8);
      RfxIntsData::RfxIntsData(aRStack_68,(int *)&local_50,2);
      RfxMclMessage::obtainUrc((RfxMclMessage *)0xc7ca,uVar6,(int)aRStack_68,(RfxBaseData *)pcVar7);
      plVar4 = local_78;
      if (local_78 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
      }
      if (plVar4 != local_78) {
        android::sp_report_race();
      }
      local_78 = local_80;
      RfxIntsData::~RfxIntsData(aRStack_68);
      RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_78);
      uVar8 = (ulong)*(uint *)(this + 8);
    }
    uVar6 = pthread_mutex_unlock((pthread_mutex_t *)(puVar3 + uVar8 * 0x28));
    uVar8 = (ulong)uVar6;
  }
  if (local_78 != (long *)0x0) {
    uVar8 = android::RefBase::decStrong
                      ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
  }
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar8);
}


