
/* RmcNetworkRealTimeRequestHandler::requestIWlanRegistrationState(android::sp<RfxMclMessage>
   const&) */

void __thiscall
RmcNetworkRealTimeRequestHandler::requestIWlanRegistrationState
          (RmcNetworkRealTimeRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long *local_68;
  long *local_60;
  int local_54;
  RfxIntsData aRStack_50 [24];
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  if (*PTR_s_en3epcreg_support_005d3f28 == '\0') {
    local_54 = -1;
  }
  else {
    local_54 = *(int *)(PTR_s_iwlan_reg_status_005d3f30 + (ulong)*(uint *)(this + 0x10) * 4);
  }
  RfxBaseHandler::logD
            ((char *)this,"RmcNwRTReqHdlr","[%s] support:%d, state:%d",
             "requestIWlanRegistrationState");
  local_60 = (long *)0x0;
  uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxIntsData::RfxIntsData(aRStack_50,&local_54,1);
  local_68 = *(long **)param_1;
  if (local_68 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_68 + *(long *)(*local_68 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_60,uVar1,0,aRStack_50,&local_68,0);
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  RfxIntsData::~RfxIntsData(aRStack_50);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_60);
  if (local_60 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_60 + *(long *)(*local_60 + -0x18)),&local_60);
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


