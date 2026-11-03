
/* RmcNetworkHandler::checkNrCapability(unsigned int) */

uint __thiscall RmcNetworkHandler::checkNrCapability(RmcNetworkHandler *this,uint param_1)

{
  long lVar1;
  bool bVar2;
  undefined *puVar3;
  long *plVar4;
  uint uVar5;
  uint uVar6;
  uint uVar7;
  RfxMclStatusManager *pRVar8;
  ulong uVar9;
  uint uVar10;
  RfxAtLine *this_00;
  long *local_80;
  long *local_78;
  int local_6c;
  long local_68;
  
  lVar1 = tpidr_el0;
  local_68 = *(long *)(lVar1 + 0x28);
  pRVar8 = (RfxMclStatusManager *)
           RfxBaseHandler::getMclStatusManager((RfxBaseHandler *)this,param_1);
  uVar5 = RfxMclStatusManager::getIntValue(pRVar8,0x54);
  pRVar8 = (RfxMclStatusManager *)RfxBaseHandler::getNonSlotMclStatusManager();
  uVar6 = RfxMclStatusManager::getIntValue(pRVar8,0x55);
  puVar3 = PTR_endc_capability_005d3e00;
  uVar10 = 0xffffffff;
  local_6c = -1;
  if ((uVar5 - 0x17 < 0xb) &&
     (((uVar6 == param_1 + 1 || (uVar6 == 0)) &&
      (*(int *)(PTR_endc_capability_005d3e00 + (ulong)param_1 * 4) == 1)))) {
    local_78 = (long *)0x0;
    RfxBaseHandler::atSendCommandSingleline((char *)this,"AT+E5GOPT?","+E5GOPT:");
    plVar4 = local_78;
    if (local_78 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
    }
    if (plVar4 != local_78) {
      android::sp_report_race();
    }
    local_78 = local_80;
    if ((int)local_80[5] < 0) {
      uVar9 = 1;
      uVar10 = 0xffffffff;
      bVar2 = true;
LAB_00473388:
      android::RefBase::decStrong
                ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
    }
    else {
      if (*(int *)((long)local_80 + 0xc) == 0) {
LAB_00473364:
        bVar2 = true;
        uVar10 = 0xffffffff;
        uVar9 = 1;
      }
      else {
        this_00 = (RfxAtLine *)local_80[4];
        RfxAtLine::atTokStart(this_00,&local_6c);
        if (local_6c < 0) goto LAB_00473364;
        uVar7 = RfxAtLine::atTokNextint(this_00,&local_6c);
        uVar9 = (ulong)uVar7;
        if (local_6c < 0) {
          bVar2 = true;
          uVar10 = 0xffffffff;
        }
        else {
          bVar2 = false;
          uVar10 = 0xffffffff;
          if ((uVar7 & 0xfffffffd) == 5) {
            uVar10 = 1;
          }
          *(uint *)(PTR_current_5gopt_config_005d3d60 + (ulong)param_1 * 4) = uVar7;
        }
      }
      if (local_78 != (long *)0x0) goto LAB_00473388;
    }
    if (bVar2) goto LAB_00473330;
  }
  else {
    uVar9 = 1;
  }
  RfxBaseHandler::logV
            ((char *)this,"RmcNwHdlr",
             "checkNrCapability(%d) type=%d, e5gopt=%d, nr_sw_slot=%d, endc_capability=%d",
             (ulong)uVar10,(ulong)uVar5,uVar9,(ulong)uVar6,
             (ulong)*(uint *)(puVar3 + (ulong)param_1 * 4));
LAB_00473330:
  if (*(long *)(lVar1 + 0x28) == local_68) {
    return uVar10;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


