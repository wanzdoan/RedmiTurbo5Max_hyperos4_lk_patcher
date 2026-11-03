
/* RmcNetworkRequestHandler::requestGetDeactivateNrScgCommunication(android::sp<RfxMclMessage>
   const&) */

void __thiscall
RmcNetworkRequestHandler::requestGetDeactivateNrScgCommunication
          (RmcNetworkRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long *plVar3;
  uint uVar4;
  uint uVar5;
  undefined8 uVar6;
  RfxAtLine *this_00;
  long *local_98;
  long *local_90;
  long *local_88;
  long *local_80;
  long *local_78;
  int local_6c;
  long *local_68 [3];
  uint local_50 [2];
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  local_6c = 0;
  local_50[0] = 0;
  local_50[1] = 0;
  local_80 = (long *)0x0;
  local_78 = (long *)0x0;
  android::String8::format("AT+EGMC=0,\"endc_deactivation\"",&local_88);
  RfxBaseHandler::atSendCommandSingleline((String8 *)this,(char *)&local_88,"+EGMC:");
  plVar3 = local_78;
  if (local_78 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
  }
  if (plVar3 != local_78) {
    android::sp_report_race();
  }
  local_78 = local_68[0];
  local_68[0] = (long *)0x0;
  android::String8::~String8((String8 *)&local_88);
  local_6c = (int)local_78[5];
  if (((local_6c == 0) && (*(int *)((long)local_78 + 0xc) != 0)) &&
     (this_00 = (RfxAtLine *)local_78[4], this_00 != (RfxAtLine *)0x0)) {
    RfxAtLine::atTokStart(this_00,&local_6c);
    if ((-1 < local_6c) && (uVar6 = RfxAtLine::atTokNextstr(this_00,&local_6c), -1 < local_6c)) {
      uVar4 = RfxAtLine::atTokNextint(this_00,&local_6c);
      local_50[0] = uVar4;
      if (-1 < local_6c) {
        uVar5 = RfxAtLine::atTokNextint(this_00,&local_6c);
        local_50[1] = uVar5;
        if (-1 < local_6c) {
          RfxBaseHandler::logV
                    ((char *)this,"RmcNwReqHdlr",
                     "requestGetDeactivateNrScgCommunication tmp=%s deactivate=%d allowSCGAdd=%d",
                     uVar6,(ulong)uVar4,(ulong)uVar5);
          uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
          RfxIntsData::RfxIntsData((RfxIntsData *)local_68,(int *)local_50,2);
          local_90 = *(long **)param_1;
          if (local_90 != (long *)0x0) {
            android::RefBase::incStrong((void *)((long)local_90 + *(long *)(*local_90 + -0x18)));
          }
          RfxMclMessage::obtainResponse((RfxMclMessage *)&local_88,uVar1,0,local_68,&local_90,0);
          android::sp<RfxMclMessage>::operator=((sp<RfxMclMessage> *)&local_80,(sp *)&local_88);
          if (local_88 != (long *)0x0) {
            android::RefBase::decStrong
                      ((RefBase *)((long)local_88 + *(long *)(*local_88 + -0x18)),&local_88);
          }
          if (local_90 != (long *)0x0) {
            android::RefBase::decStrong
                      ((RefBase *)((long)local_90 + *(long *)(*local_90 + -0x18)),&local_90);
          }
          RfxIntsData::~RfxIntsData((RfxIntsData *)local_68);
          goto LAB_0047f9ac;
        }
      }
    }
    uVar6 = 0;
  }
  else {
    uVar6 = 2;
  }
  uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxVoidData::RfxVoidData((RfxVoidData *)local_68);
  local_98 = *(long **)param_1;
  if (local_98 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_98 + *(long *)(*local_98 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_88,uVar1,uVar6,local_68,&local_98,0);
  plVar3 = local_80;
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  if (plVar3 != local_80) {
    android::sp_report_race();
  }
  local_80 = local_88;
  local_88 = (long *)0x0;
  if (local_98 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_98 + *(long *)(*local_98 + -0x18)),&local_98);
  }
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_68);
LAB_0047f9ac:
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_80);
  if (local_80 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_80 + *(long *)(*local_80 + -0x18)),&local_80);
  }
  if (local_78 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
  }
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


