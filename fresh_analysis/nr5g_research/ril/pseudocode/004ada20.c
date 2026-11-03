
/* RmcImsControlRequestHandler::enableImsRegistrationReport(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcImsControlRequestHandler::enableImsRegistrationReport
          (RmcImsControlRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long *plVar3;
  long lVar4;
  undefined4 uVar5;
  long *local_78;
  long *local_70;
  long *local_68;
  long *local_60 [3];
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  local_68 = (long *)0x0;
  RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
  RfxBaseHandler::sendEvent
            ((RfxBaseHandler *)this,0xcf71,local_60,0,*(undefined4 *)(this + 8),0xffffffff,
             0xffffffff,0,1);
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_60);
  RfxBaseHandler::logD((char *)this,"RmcImsCtlReqHdl","Enable +CIREGU report");
  android::String8::format("AT+CIREG=2",&local_70);
  RfxBaseHandler::atSendCommand((String8 *)this,&local_70);
  plVar3 = local_68;
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (plVar3 != local_68) {
    android::sp_report_race();
  }
  local_68 = local_60[0];
  local_60[0] = (long *)0x0;
  android::String8::~String8((String8 *)&local_70);
  if (((int)local_68[5] == 0) && (*(int *)((long)local_68 + 0xc) == 1)) {
    uVar5 = 0;
    lVar4 = *(long *)param_1;
  }
  else {
    RfxBaseHandler::logE((char *)this,"RmcImsCtlReqHdl","Enable +CIREGU report error: %d");
    uVar5 = 2;
    lVar4 = *(long *)param_1;
  }
  if (lVar4 != 0) {
    uVar1 = *(undefined4 *)(lVar4 + 0xc);
    local_70 = (long *)0x0;
    RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
    local_78 = *(long **)param_1;
    if (local_78 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_78 + *(long *)(*local_78 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_70,uVar1,uVar5,local_60,&local_78,0);
    if (local_78 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
    }
    RfxVoidData::~RfxVoidData((RfxVoidData *)local_60);
    RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_70);
    if (local_70 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
    }
  }
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


