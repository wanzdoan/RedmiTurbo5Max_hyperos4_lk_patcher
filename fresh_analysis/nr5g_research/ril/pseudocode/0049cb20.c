
/* RmcNetworkUrcHandler::handleNrCaBandChanged(android::sp<RfxMclMessage> const&) */

void __thiscall RmcNetworkUrcHandler::handleNrCaBandChanged(RmcNetworkUrcHandler *this,sp *param_1)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  undefined *puVar4;
  undefined *puVar5;
  long *plVar6;
  int iVar7;
  uint uVar8;
  ulong uVar9;
  ulong uVar10;
  char *pcVar11;
  uint *puVar12;
  int *piVar13;
  RfxAtLine *this_00;
  long *local_98;
  long *local_90;
  int local_84;
  RfxIntsData aRStack_80 [28];
  int local_64;
  undefined4 uStack_60;
  int local_5c;
  long local_58;
  
  lVar2 = tpidr_el0;
  local_58 = *(long *)(lVar2 + 0x28);
  local_84 = 0;
  local_90 = (long *)0x0;
  this_00 = *(RfxAtLine **)(*(long *)param_1 + 0x30);
  uVar9 = RfxAtLine::atTokStart(this_00,&local_84);
  if ((-1 < local_84) &&
     (uVar10 = RfxAtLine::atTokNextint(this_00,&local_84), uVar9 = uVar10, -1 < local_84)) {
    uVar9 = RfxAtLine::atTokNextint(this_00,&local_84);
    if (-1 < local_84) {
      uVar3 = (undefined4)uVar9;
      iVar7 = RfxAtLine::atTokHasmore(this_00);
      if (iVar7 == 0) {
        iVar7 = 0;
      }
      else {
        uVar9 = RfxAtLine::atTokNextint(this_00,&local_84);
        if (local_84 < 0) goto LAB_0049cd04;
        iVar7 = (int)uVar9;
      }
      puVar5 = PTR_s_nrCaMutex_005d3ca0;
      pthread_mutex_lock((pthread_mutex_t *)
                         (PTR_s_nrCaMutex_005d3ca0 + (ulong)*(uint *)(this + 8) * 0x28));
      puVar4 = PTR_nr_ca_cache_005d3c58;
      puVar12 = *(uint **)(PTR_nr_ca_cache_005d3c58 + (ulong)*(uint *)(this + 8) * 8);
      pcVar11 = "handleNrCaBandChanged";
      RfxBaseHandler::logD
                ((char *)this,"RmcNwHdlr","%s, old[%d, %d, %d], new[%d, %d, %d]",
                 "handleNrCaBandChanged",(ulong)*puVar12,(ulong)puVar12[1],(ulong)puVar12[2],
                 uVar10 & 0xffffffff,uVar3,iVar7);
      piVar13 = *(int **)(puVar4 + (ulong)*(uint *)(this + 8) * 8);
      if (piVar13[2] != iVar7) {
        RmcNetworkHandler::isNeedNotifyStateChanged((RmcNetworkHandler *)this);
        piVar13 = *(int **)(puVar4 + (ulong)*(uint *)(this + 8) * 8);
      }
      *piVar13 = (int)uVar10;
      *(undefined4 *)(*(long *)(puVar4 + (ulong)*(uint *)(this + 8) * 8) + 4) = uVar3;
      *(int *)(*(long *)(puVar4 + (ulong)*(uint *)(this + 8) * 8) + 8) = iVar7;
      uVar8 = pthread_mutex_unlock((pthread_mutex_t *)(puVar5 + (ulong)*(uint *)(this + 8) * 0x28));
      uVar9 = (ulong)uVar8;
      if (*PTR_urc_enrcaband_enable_005d3e80 != '\0') {
        iVar1 = *(int *)(this + 8);
        local_64 = (int)uVar10;
        uStack_60 = uVar3;
        local_5c = iVar7;
        RfxIntsData::RfxIntsData(aRStack_80,&local_64,3);
        RfxMclMessage::obtainUrc
                  ((RfxMclMessage *)0xc7c8,iVar1,(int)aRStack_80,(RfxBaseData *)pcVar11);
        plVar6 = local_90;
        if (local_90 != (long *)0x0) {
          android::RefBase::decStrong
                    ((RefBase *)((long)local_90 + *(long *)(*local_90 + -0x18)),&local_90);
        }
        if (plVar6 != local_90) {
          android::sp_report_race();
        }
        local_90 = local_98;
        RfxIntsData::~RfxIntsData(aRStack_80);
        uVar9 = RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_90);
      }
    }
  }
LAB_0049cd04:
  if (local_90 != (long *)0x0) {
    uVar9 = android::RefBase::decStrong
                      ((RefBase *)((long)local_90 + *(long *)(*local_90 + -0x18)),&local_90);
  }
  if (*(long *)(lVar2 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar9);
}


