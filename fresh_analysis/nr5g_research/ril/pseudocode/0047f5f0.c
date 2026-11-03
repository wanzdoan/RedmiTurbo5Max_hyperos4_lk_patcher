
/* RmcNetworkRequestHandler::requestDeactivateNrScgCommunication(android::sp<RfxMclMessage> const&)
    */

void __thiscall
RmcNetworkRequestHandler::requestDeactivateNrScgCommunication
          (RmcNetworkRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long **pplVar3;
  long *plVar4;
  long lVar5;
  long *local_78;
  long *local_70;
  long *local_68;
  long *local_60;
  long *local_58;
  long *local_50 [3];
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  local_60 = (long *)0x0;
  local_58 = (long *)0x0;
  android::String8::format
            ("AT+EGMC=1,\"endc_deactivation\",%d,%d",&local_68,
             (ulong)**(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8),1);
  RfxBaseHandler::atSendCommand((String8 *)this,&local_68);
  plVar4 = local_58;
  if (local_58 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
  }
  if (plVar4 != local_58) {
    android::sp_report_race();
  }
  local_58 = local_50[0];
  local_50[0] = (long *)0x0;
  android::String8::~String8((String8 *)&local_68);
  if (((int)local_58[5] < 0) || (*(int *)((long)local_58 + 0xc) == 0)) {
    uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)local_50);
    local_78 = *(long **)param_1;
    if (local_78 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_78 + *(long *)(*local_78 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_68,uVar1,2,local_50,&local_78,0);
    plVar4 = local_60;
    if (local_60 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_60 + *(long *)(*local_60 + -0x18)),&local_60);
    }
    if (plVar4 != local_60) {
      android::sp_report_race();
    }
    local_60 = local_68;
    local_68 = (long *)0x0;
    if (local_78 == (long *)0x0) goto LAB_0047f7d4;
    lVar5 = *local_78;
    pplVar3 = &local_78;
    plVar4 = local_78;
  }
  else {
    uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)local_50);
    local_70 = *(long **)param_1;
    if (local_70 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_70 + *(long *)(*local_70 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_68,uVar1,0,local_50,&local_70,0);
    plVar4 = local_60;
    if (local_60 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_60 + *(long *)(*local_60 + -0x18)),&local_60);
    }
    if (plVar4 != local_60) {
      android::sp_report_race();
    }
    local_60 = local_68;
    local_68 = (long *)0x0;
    if (local_70 == (long *)0x0) goto LAB_0047f7d4;
    lVar5 = *local_70;
    pplVar3 = &local_70;
    plVar4 = local_70;
  }
  local_68 = (long *)0x0;
  android::RefBase::decStrong((RefBase *)((long)plVar4 + *(long *)(lVar5 + -0x18)),pplVar3);
LAB_0047f7d4:
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_50);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_60);
  if (local_60 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_60 + *(long *)(*local_60 + -0x18)),&local_60);
  }
  if (local_58 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


