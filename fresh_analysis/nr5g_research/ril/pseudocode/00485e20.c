
/* RmcNetworkRequestHandler::requestGetNrdcState(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcNetworkRequestHandler::requestGetNrdcState(RmcNetworkRequestHandler *this,sp *param_1)

{
  undefined4 uVar1;
  long lVar2;
  long *plVar3;
  uint uVar4;
  int iVar5;
  ulong uVar6;
  long *local_78;
  long *local_70;
  long *local_68;
  uint local_5c;
  long *local_58;
  RfxIntsData aRStack_50 [24];
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  local_58 = (long *)0x0;
  uVar6 = RmcNetworkHandler::isNrSupported((RmcNetworkHandler *)this);
  if ((((uVar6 & 1) == 0) || (uVar4 = getNrOption(this), (int)uVar4 < 0)) ||
     (iVar5 = isEndcDeactivate(this), iVar5 < 0)) {
    uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxVoidData::RfxVoidData((RfxVoidData *)aRStack_50);
    local_78 = *(long **)param_1;
    if (local_78 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_78 + *(long *)(*local_78 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_68,uVar1,0x26,aRStack_50,&local_78,0);
    plVar3 = local_58;
    if (local_58 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
    }
    if (plVar3 != local_58) {
      android::sp_report_race();
    }
    local_58 = local_68;
    local_68 = (long *)0x0;
    if (local_78 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_78 + *(long *)(*local_78 + -0x18)),&local_78);
    }
    RfxVoidData::~RfxVoidData((RfxVoidData *)aRStack_50);
  }
  else {
    local_5c = (uint)(iVar5 == 0) & uVar4 >> 2;
    uVar1 = *(undefined4 *)(*(long *)param_1 + 0xc);
    RfxIntsData::RfxIntsData(aRStack_50,(int *)&local_5c,1);
    local_70 = *(long **)param_1;
    if (local_70 != (long *)0x0) {
      android::RefBase::incStrong((void *)((long)local_70 + *(long *)(*local_70 + -0x18)));
    }
    RfxMclMessage::obtainResponse((RfxMclMessage *)&local_68,uVar1,0,aRStack_50,&local_70,0);
    plVar3 = local_58;
    if (local_58 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
    }
    if (plVar3 != local_58) {
      android::sp_report_race();
    }
    local_58 = local_68;
    local_68 = (long *)0x0;
    if (local_70 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
    }
    RfxIntsData::~RfxIntsData(aRStack_50);
  }
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_58);
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


