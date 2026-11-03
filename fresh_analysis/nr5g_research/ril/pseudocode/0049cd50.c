
/* RmcNetworkUrcHandler::handleIWlanRegStateChanged(android::sp<RfxMclMessage> const&) */

void RmcNetworkUrcHandler::handleIWlanRegStateChanged(sp *param_1)

{
  undefined *puVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  undefined *puVar5;
  long *plVar6;
  ulong uVar7;
  uint uVar8;
  undefined4 uVar9;
  long *in_x1;
  RfxBaseData *in_x3;
  RfxAtLine *this;
  long *local_a0;
  long *local_98;
  undefined8 local_90;
  RfxIntsData aRStack_88 [24];
  int local_70 [6];
  long local_58;
  
  puVar1 = PTR_s_iwlan_reg_status_005d3f30;
  lVar4 = tpidr_el0;
  local_58 = *(long *)(lVar4 + 0x28);
  local_98 = (long *)0x0;
  local_90 = 0;
  iVar3 = *(int *)(PTR_s_iwlan_reg_status_005d3f30 + (ulong)*(uint *)(param_1 + 8) * 4);
  local_70[0] = 0;
  local_70[1] = 0;
  local_70[2] = 0;
  local_70[3] = 0;
  local_70[4] = 0;
  this = *(RfxAtLine **)(*in_x1 + 0x30);
  RfxAtLine::atTokStart(this,(int *)((long)&local_90 + 4));
  if (-1 < (long)local_90) {
    uVar8 = RfxAtLine::atTokNextint(this,(int *)((long)&local_90 + 4));
    uVar7 = local_90;
    puVar5 = PTR_s_iwlanRegStatusMutex_005d3cb0;
    local_90 = CONCAT44(local_90._4_4_,uVar8);
    if ((uVar8 < 2) && (-1 < (long)uVar7)) {
      pthread_mutex_lock((pthread_mutex_t *)
                         (PTR_s_iwlanRegStatusMutex_005d3cb0 + (ulong)*(uint *)(param_1 + 8) * 0x28)
                        );
      *(uint *)(puVar1 + (ulong)*(uint *)(param_1 + 8) * 4) = uVar8;
      pthread_mutex_unlock((pthread_mutex_t *)(puVar5 + (ulong)*(uint *)(param_1 + 8) * 0x28));
      iVar2 = *(int *)(param_1 + 8);
      RfxIntsData::RfxIntsData(aRStack_88,(int *)&local_90,1);
      RfxMclMessage::obtainUrc((RfxMclMessage *)0xc7c7,iVar2,(int)aRStack_88,in_x3);
      plVar6 = local_98;
      if (local_98 != (long *)0x0) {
        android::RefBase::decStrong
                  ((RefBase *)((long)local_98 + *(long *)(*local_98 + -0x18)),&local_98);
      }
      if (plVar6 != local_98) {
        android::sp_report_race();
      }
      local_98 = local_a0;
      RfxIntsData::~RfxIntsData(aRStack_88);
      RfxBaseHandler::responseToTelCore((RfxBaseHandler *)param_1,(sp *)&local_98);
      RfxBaseHandler::logD
                ((char *)param_1,"RmcNwHdlr","[%s] status:%d","handleIWlanRegStateChanged",
                 local_90 & 0xffffffff);
      puVar1 = PTR_urc_data_reg_state_cache_005d3c38;
      uVar9 = RmcNetworkHandler::convertRegState
                        ((RmcNetworkHandler *)param_1,
                         **(uint **)(PTR_urc_data_reg_state_cache_005d3c38 +
                                    (ulong)*(uint *)(param_1 + 8) * 8),false);
      local_70[0] = uVar9;
      uVar9 = RmcNetworkHandler::convertPSNetworkType
                        ((RmcNetworkHandler *)param_1,
                         *(uint *)(*(long *)(puVar1 + (ulong)*(uint *)(param_1 + 8) * 8) + 0x10));
      local_70[1] = uVar9;
      uVar9 = combineWfcEgregState((RmcNetworkUrcHandler *)param_1);
      local_70[2] = uVar9;
      if ((int)local_90 == 1) {
        uVar9 = 0x12;
      }
      else {
        uVar9 = RmcNetworkHandler::convertPSNetworkType
                          ((RmcNetworkHandler *)param_1,*(uint *)(param_1 + 0x24));
      }
      local_70[3] = uVar9;
      local_70[4] = 0;
      RfxIntsData::RfxIntsData(aRStack_88,local_70,5);
      RfxBaseHandler::sendEvent
                ((RfxBaseHandler *)param_1,0xc7d1,aRStack_88,1,*(undefined4 *)(param_1 + 8),
                 0xffffffff,0xffffffff,0,1);
      RfxIntsData::~RfxIntsData(aRStack_88);
      if (*PTR_s_iwlan_in_legacy_mode_005d3e30 != '\0') {
        if (iVar3 != (int)local_90) {
          puVar1 = &DAT_002240c5;
          if ((int)local_90 != 1) {
            puVar1 = &DAT_00235024;
          }
          setMSimProperty(*(undefined4 *)(param_1 + 8),"persist.vendor.radio.wfc_state",puVar1);
          RmcNetworkHandler::isNeedNotifyStateChanged((RmcNetworkHandler *)param_1);
        }
      }
      goto LAB_0049cf18;
    }
  }
  RfxBaseHandler::logE((char *)param_1,"RmcNwHdlr","[%s] URC error","handleIWlanRegStateChanged");
LAB_0049cf18:
  if (local_98 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_98 + *(long *)(*local_98 + -0x18)),&local_98);
  }
  if (*(long *)(lVar4 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


