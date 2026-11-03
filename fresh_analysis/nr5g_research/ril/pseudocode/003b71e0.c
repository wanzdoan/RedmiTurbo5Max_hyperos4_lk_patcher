
/* RtcImsConfigController::isVoNREnabled(android::sp<RfxMessage> const&) */

void __thiscall RtcImsConfigController::isVoNREnabled(RtcImsConfigController *this,sp *param_1)

{
  undefined4 uVar1;
  undefined4 uVar2;
  long lVar3;
  int iVar4;
  char *pcVar5;
  long *local_70;
  uint local_64;
  RfxIntsData aRStack_60 [24];
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  pcVar5 = *(char **)PTR_PROPERTY_VONR_ENALBE_005d2d00;
  iVar4 = RfxController::getSlotId((RfxController *)this);
  local_64 = ImsConfigUtils::getFeaturePropValue(pcVar5,iVar4);
  RfxController::logD((char *)this,"RtcImsConfigController","isVoNREnabled(), value: %d",
                      (ulong)local_64);
  local_70 = (long *)0x0;
  uVar1 = *(undefined4 *)(*(long *)param_1 + 0x24);
  uVar2 = *(undefined4 *)(*(long *)param_1 + 0x1c);
  RfxIntsData::RfxIntsData(aRStack_60,&local_64,4);
  RfxMessage::obtainResponse((RfxMessage *)&local_70,uVar1,uVar2,0,aRStack_60,param_1);
  RfxIntsData::~RfxIntsData(aRStack_60);
  RfxController::responseToRilj((RfxController *)this,(sp *)&local_70);
  if (local_70 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_70 + *(long *)(*local_70 + -0x18)),&local_70);
  }
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


