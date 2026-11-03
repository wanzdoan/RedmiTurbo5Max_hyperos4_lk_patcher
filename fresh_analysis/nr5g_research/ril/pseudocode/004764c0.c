
/* RmcNetworkHandler::updateNrCaBandInfo() */

void __thiscall RmcNetworkHandler::updateNrCaBandInfo(RmcNetworkHandler *this)

{
  long lVar1;
  undefined *puVar2;
  undefined *puVar3;
  long *plVar4;
  uint uVar5;
  undefined4 uVar6;
  int iVar7;
  ulong uVar8;
  RfxAtLine *this_00;
  undefined8 *puVar9;
  undefined4 *puVar10;
  int local_58;
  undefined4 uStack_54;
  long *local_50;
  long local_48;
  
  puVar3 = PTR_s_nrCaMutex_005d3ca0;
  puVar2 = PTR_nr_ca_cache_005d3c58;
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  local_50 = (long *)0x0;
  puVar9 = *(undefined8 **)(PTR_nr_ca_cache_005d3c58 + (ulong)*(uint *)(this + 8) * 8);
  pthread_mutex_lock((pthread_mutex_t *)
                     (PTR_s_nrCaMutex_005d3ca0 + (ulong)*(uint *)(this + 8) * 0x28));
  *puVar9 = 0;
  *(undefined4 *)(puVar9 + 1) = 0;
  pthread_mutex_unlock((pthread_mutex_t *)(puVar3 + (ulong)*(uint *)(this + 8) * 0x28));
  puVar10 = *(undefined4 **)(puVar2 + (ulong)*(uint *)(this + 8) * 8);
  RfxBaseHandler::atSendCommandSingleline((char *)this,"AT+ENRCABAND?","+ENRCABAND:");
  plVar4 = local_50;
  if (local_50 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_50 + *(long *)(*local_50 + -0x18)),&local_50);
  }
  if (plVar4 != local_50) {
    android::sp_report_race();
  }
  local_50 = (long *)CONCAT44(uStack_54,local_58);
  pthread_mutex_lock((pthread_mutex_t *)(puVar3 + (ulong)*(uint *)(this + 8) * 0x28));
  local_58 = (int)local_50[5];
  if ((((local_58 == 0) && (*(int *)((long)local_50 + 0xc) != 0)) &&
      (this_00 = (RfxAtLine *)local_50[4], this_00 != (RfxAtLine *)0x0)) &&
     (RfxAtLine::atTokStart(this_00,&local_58), -1 < local_58)) {
    uVar6 = RfxAtLine::atTokNextint(this_00,&local_58);
    *puVar10 = uVar6;
    if (-1 < local_58) {
      uVar6 = RfxAtLine::atTokNextint(this_00,&local_58);
      puVar10[1] = uVar6;
      if (-1 < local_58) {
        iVar7 = RfxAtLine::atTokHasmore(this_00);
        if (iVar7 == 0) goto LAB_004765d8;
        uVar6 = RfxAtLine::atTokNextint(this_00,&local_58);
        puVar10[2] = uVar6;
        if (-1 < local_58) goto LAB_004765d8;
      }
    }
  }
  pthread_mutex_unlock((pthread_mutex_t *)(puVar3 + (ulong)*(uint *)(this + 8) * 0x28));
  puVar9 = *(undefined8 **)(puVar2 + (ulong)*(uint *)(this + 8) * 8);
  pthread_mutex_lock((pthread_mutex_t *)(puVar3 + (ulong)*(uint *)(this + 8) * 0x28));
  *puVar9 = 0;
  *(undefined4 *)(puVar9 + 1) = 0;
LAB_004765d8:
  uVar5 = pthread_mutex_unlock((pthread_mutex_t *)(puVar3 + (ulong)*(uint *)(this + 8) * 0x28));
  uVar8 = (ulong)uVar5;
  if (local_50 != (long *)0x0) {
    uVar8 = android::RefBase::decStrong
                      ((RefBase *)((long)local_50 + *(long *)(*local_50 + -0x18)),&local_50);
  }
  if (*(long *)(lVar1 + 0x28) != local_48) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail(uVar8);
  }
  return;
}


