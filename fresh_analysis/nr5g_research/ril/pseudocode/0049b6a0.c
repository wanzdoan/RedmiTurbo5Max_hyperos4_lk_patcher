
/* RmcNetworkUrcHandler::handleVoNrEmergencySupportedChanged(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcNetworkUrcHandler::handleVoNrEmergencySupportedChanged(RmcNetworkUrcHandler *this,sp *param_1)

{
  long lVar1;
  undefined *puVar2;
  undefined *puVar3;
  uint uVar4;
  uint uVar5;
  RfxMclStatusManager *pRVar6;
  char *pcVar7;
  ulong uVar8;
  long lVar9;
  RfxAtLine *this_00;
  int local_7c;
  undefined4 local_78 [2];
  uint local_70;
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  local_7c = 0;
  this_00 = *(RfxAtLine **)(*(long *)param_1 + 0x30);
  RfxAtLine::atTokStart(this_00,&local_7c);
  if (local_7c < 0) {
    pcVar7 = "VoNrEmergency URC parse failed";
  }
  else {
    uVar4 = RfxAtLine::atTokNextint(this_00,&local_7c);
    if (local_7c < 0) {
      pcVar7 = "VoNrEmergency URC emcSupport parse failed";
    }
    else {
      uVar5 = RfxAtLine::atTokNextint(this_00,&local_7c);
      if (-1 < local_7c) {
        RfxBaseHandler::logD
                  ((char *)this,"RmcNwHdlr",
                   "handleVoLteEmergencySupportedChanged: emcSupport[%d], emfSupport[%d]",
                   (ulong)uVar4,(ulong)uVar5);
        puVar3 = PTR_imsEmergencyMutex_005d3d48;
        pthread_mutex_lock((pthread_mutex_t *)
                           (PTR_imsEmergencyMutex_005d3d48 + (ulong)*(uint *)(this + 8) * 0x28));
        puVar2 = PTR_imsEmergencyInfo_005d3d40;
        uVar8 = (ulong)*(uint *)(this + 8);
        lVar9 = *(long *)(PTR_imsEmergencyInfo_005d3d40 + uVar8 * 8);
        if (uVar4 != *(uint *)(lVar9 + 4)) {
          *(uint *)(lVar9 + 4) = uVar4;
          pRVar6 = (RfxMclStatusManager *)
                   RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this);
          local_78[0] = 2;
          local_70 = uVar4;
          RfxMclStatusManager::setValue(pRVar6,0xba,(RfxVariant *)local_78,false,false);
          RfxVariant::deinit((RfxVariant *)local_78);
          uVar8 = (ulong)*(uint *)(this + 8);
          lVar9 = *(long *)(puVar2 + uVar8 * 8);
        }
        if (uVar5 != *(uint *)(lVar9 + 8)) {
          *(uint *)(lVar9 + 8) = uVar5;
          pRVar6 = (RfxMclStatusManager *)
                   RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this);
          local_78[0] = 2;
          local_70 = uVar5;
          RfxMclStatusManager::setValue(pRVar6,0xbb,(RfxVariant *)local_78,false,false);
          RfxVariant::deinit((RfxVariant *)local_78);
          uVar8 = (ulong)*(uint *)(this + 8);
        }
        uVar4 = pthread_mutex_unlock((pthread_mutex_t *)(puVar3 + uVar8 * 0x28));
        uVar8 = (ulong)uVar4;
        goto LAB_0049b83c;
      }
      pcVar7 = "VoNrEmergency URC emfSupport parse failed";
    }
  }
  uVar8 = RfxBaseHandler::logE((char *)this,"RmcNwHdlr",pcVar7);
LAB_0049b83c:
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail(uVar8);
}


