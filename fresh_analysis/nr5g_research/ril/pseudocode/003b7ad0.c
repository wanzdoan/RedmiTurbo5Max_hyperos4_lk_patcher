
/* RtcImsConfigController::handleSetProvisionResponse(android::sp<RfxMessage> const&) */

void __thiscall
RtcImsConfigController::handleSetProvisionResponse(RtcImsConfigController *this,sp *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  long lVar4;
  long *local_68;
  RfxVoidData aRStack_60 [24];
  long local_48;
  
  lVar4 = tpidr_el0;
  local_48 = *(long *)(lVar4 + 0x28);
  RfxController::logD((char *)this,"RtcImsConfigController","handleSetProvisionResponse, result:%d",
                      (ulong)*(uint *)(*(long *)param_1 + 0x2c));
  local_68 = (long *)0x0;
  uVar1 = *(undefined4 *)(*(long *)(this + 0x188) + 0x24);
  uVar2 = *(undefined4 *)(*(long *)(this + 0x188) + 0x1c);
  uVar3 = *(undefined4 *)(*(long *)param_1 + 0x2c);
  RfxVoidData::RfxVoidData(aRStack_60);
  RfxMessage::obtainResponse((RfxMessage *)&local_68,uVar1,uVar2,uVar3,aRStack_60,this + 0x188);
  RfxVoidData::~RfxVoidData(aRStack_60);
  RfxController::responseToRilj((RfxController *)this,(sp *)&local_68);
  if (local_68 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
  }
  if (*(long *)(lVar4 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


