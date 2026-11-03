
/* RmcCommSimRequestHandler::handleTfnRsuRequest(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcCommSimRequestHandler::handleTfnRsuRequest(RmcCommSimRequestHandler *this,sp *param_1)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  long *local_60;
  long *local_58;
  RfxVoidData aRStack_50 [24];
  long local_38;
  
  lVar3 = tpidr_el0;
  local_38 = *(long *)(lVar3 + 0x28);
  iVar1 = *(int *)(*(long *)(*(long *)(*(long *)param_1 + 0x10) + 8) + 4);
  if (iVar1 < 3) {
    if (iVar1 == 1) {
      if (*(long *)(lVar3 + 0x28) == local_38) {
        handleTfnRsuGetImei(this,param_1);
        return;
      }
      goto LAB_004c7498;
    }
    if (iVar1 == 2) {
      if (*(long *)(lVar3 + 0x28) == local_38) {
        handleTfnRsuProcessLockData(this,param_1);
        return;
      }
      goto LAB_004c7498;
    }
  }
  else {
    if (iVar1 == 3) {
      if (*(long *)(lVar3 + 0x28) == local_38) {
        handleTfnRsuGetStatus(this,param_1);
        return;
      }
      goto LAB_004c7498;
    }
    if (iVar1 == 4) {
      if (*(long *)(lVar3 + 0x28) == local_38) {
        handleTfnRsuGenerateRequest(this,param_1);
        return;
      }
      goto LAB_004c7498;
    }
    if (iVar1 == 5) {
      if (*(long *)(lVar3 + 0x28) == local_38) {
        handleTfnRsuRelock(this,param_1);
        return;
      }
      goto LAB_004c7498;
    }
  }
  uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
  local_58 = (long *)0x0;
  RfxVoidData::RfxVoidData(aRStack_50);
  local_60 = *(long **)param_1;
  if (local_60 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_60 + *(long *)(*local_60 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_58,uVar2,2,aRStack_50,&local_60,0);
  if (local_60 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_60 + *(long *)(*local_60 + -0x18)),&local_60);
  }
  RfxVoidData::~RfxVoidData(aRStack_50);
  RfxBaseHandler::responseToTelCore((RfxBaseHandler *)this,(sp *)&local_58);
  if (local_58 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
  }
  if (*(long *)(lVar3 + 0x28) == local_38) {
    return;
  }
LAB_004c7498:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


