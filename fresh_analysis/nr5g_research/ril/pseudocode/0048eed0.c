
/* RmcNetworkRequestHandler::requestGetAllBandModeWithoutNr(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcNetworkRequestHandler::requestGetAllBandModeWithoutNr(RmcNetworkRequestHandler *this,sp *param_1)

{
  long lVar1;
  undefined4 uVar2;
  int iVar3;
  long **pplVar4;
  long *plVar5;
  long lVar6;
  RfxAtLine *this_00;
  long *local_c8;
  long *local_c0;
  long *local_b8;
  long *local_b0;
  long *local_a8;
  int local_9c;
  long *local_98 [3];
  int local_80 [14];
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  local_b0 = (long *)0x0;
  local_a8 = (long *)0x0;
  local_80[9] = 0;
  local_80[10] = 0;
  local_80[8] = 0;
  local_80[2] = 0;
  local_80[3] = 0;
  local_80[0] = 0;
  local_80[1] = 0;
  local_80[6] = 0;
  local_80[7] = 0;
  local_80[4] = 0;
  local_80[5] = 0;
  RfxBaseHandler::atSendCommandSingleline((char *)this,"AT+EPBSE?","+EPBSE:");
  plVar5 = local_a8;
  if (local_a8 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_a8 + *(long *)(*local_a8 + -0x18)),&local_a8);
  }
  if (plVar5 != local_a8) {
    android::sp_report_race();
  }
  local_9c = (int)local_98[0][5];
  local_a8 = local_98[0];
  if (((local_9c == 0) && (*(int *)((long)local_98[0] + 0xc) != 0)) &&
     (this_00 = (RfxAtLine *)local_98[0][4], this_00 != (RfxAtLine *)0x0)) {
    RfxAtLine::atTokStart(this_00,&local_9c);
    if (local_9c < 0) goto LAB_0048ef68;
    uVar2 = RfxAtLine::atTokNextint(this_00,&local_9c);
    local_80[0] = uVar2;
    if (local_9c < 0) goto LAB_0048ef68;
    uVar2 = RfxAtLine::atTokNextint(this_00,&local_9c);
    local_80[1] = uVar2;
    if (local_9c < 0) goto LAB_0048ef68;
    lVar6 = 0;
    local_80[2] = 0;
    do {
      iVar3 = RfxAtLine::atTokHasmore(this_00);
      if (iVar3 == 0) break;
      iVar3 = RfxAtLine::atTokNextint(this_00,&local_9c);
      local_80[lVar6 + 3] = iVar3;
      if (local_9c < 0) break;
      lVar6 = lVar6 + 1;
      local_80[2] = local_80[2] + 1;
    } while (local_80[2] != 8);
    uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxIntsData::RfxIntsData((RfxIntsData *)local_98,local_80,(int)lVar6 + 3);
    local_c0 = *(long **)param_1;
    if (local_c0 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_c0 + *(long *)(*local_c0 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_b8,uVar2,0,local_98,&local_c0,0);
    android::sp<RfxMclMessage>::operator=((sp<RfxMclMessage> *)&local_b0,(sp *)&local_b8);
    if (local_b8 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_b8 + *(long *)(*local_b8 + -0x18)),&local_b8);
    }
    if (local_c0 == (long *)0x0) goto LAB_0048f01c;
    lVar6 = *local_c0;
    pplVar4 = &local_c0;
    plVar5 = local_c0;
  }
  else {
LAB_0048ef68:
    RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","There is something wrong with AT+EPBSE");
    uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxIntsData::RfxIntsData((RfxIntsData *)local_98);
    local_c8 = *(long **)param_1;
    if (local_c8 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_c8 + *(long *)(*local_c8 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_b8,uVar2,2,local_98,&local_c8,0);
    plVar5 = local_b0;
    if (local_b0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_b0 + *(long *)(*local_b0 + -0x18)),&local_b0);
    }
    if (plVar5 != local_b0) {
      android::sp_report_race();
    }
    local_b0 = local_b8;
    local_b8 = (long *)0x0;
    if (local_c8 == (long *)0x0) goto LAB_0048f01c;
    lVar6 = *local_c8;
    pplVar4 = &local_c8;
    plVar5 = local_c8;
  }
  android::RefBase::decStrong((RefBase *)((long)plVar5 + *(long *)(lVar6 + -0x18)),pplVar4);
LAB_0048f01c:
  RfxIntsData::~RfxIntsData((RfxIntsData *)local_98);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_b0);
  if (local_b0 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_b0 + *(long *)(*local_b0 + -0x18)),&local_b0);
  }
  if (local_a8 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_a8 + *(long *)(*local_a8 + -0x18)),&local_a8);
  }
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


