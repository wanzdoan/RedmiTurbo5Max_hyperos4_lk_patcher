
/* RmcCommSimRequestHandler::handleCommonRsuRequest(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcCommSimRequestHandler::handleCommonRsuRequest(RmcCommSimRequestHandler *this,sp *param_1)

{
  int iVar1;
  undefined4 uVar2;
  long lVar3;
  char *pcVar4;
  char *pcVar5;
  long lVar6;
  long *local_60;
  long *local_58;
  RfxVoidData aRStack_50 [24];
  long local_38;
  
  lVar3 = tpidr_el0;
  local_38 = *(long *)(lVar3 + 0x28);
  lVar6 = *(long *)(*(long *)(*(long *)param_1 + 0x10) + 8);
  iVar1 = *(int *)(lVar6 + 4);
  if (iVar1 < 3) {
    if (iVar1 == 1) {
      if (*(long *)(lVar3 + 0x28) == local_38) {
        handleCommonRsuGetLockInfo(this,param_1);
        return;
      }
      goto LAB_004c79d4;
    }
    if (iVar1 == 2) {
      if (*(long *)(lVar3 + 0x28) == local_38) {
        handleCommonRsuGetCatInfo(this,param_1);
        return;
      }
      goto LAB_004c79d4;
    }
LAB_004c7890:
    pcVar4 = *(char **)(this + 0x10);
    pcVar5 = "[RSU-SIMLOCK] handleCommonRsuRequest: unknow requestId: %d";
  }
  else {
    if (iVar1 != 3) {
      if (iVar1 == 4) {
        if (*(long *)(lVar3 + 0x28) == local_38) {
          handleCommonRsuGetLockStatus(this,param_1);
          return;
        }
        goto LAB_004c79d4;
      }
      goto LAB_004c7890;
    }
    if (*(int *)(lVar6 + 0x18) == 1) {
      if (*(long *)(lVar3 + 0x28) == local_38) {
        handleTfRsuVerifyStatus(this,param_1);
        return;
      }
      goto LAB_004c79d4;
    }
    pcVar4 = *(char **)(this + 0x10);
    pcVar5 = "[RSU-SIMLOCK] RSU_COMMON_REQUEST_VERIFY_PIN: unknow lock type: %d";
  }
  RfxBaseHandler::logE((char *)this,pcVar4,pcVar5);
  local_58 = (long *)0x0;
  uVar2 = *(undefined4 *)(*(long *)param_1 + 0xc);
  RfxVoidData::RfxVoidData(aRStack_50);
  local_60 = *(long **)param_1;
  if (local_60 != (long *)0x0) {
    android::RefBase::incStrong((void *)((long)local_60 + *(long *)(*local_60 + -0x18)));
  }
  RfxMclMessage::obtainResponse((RfxMclMessage *)&local_58,uVar2,6,aRStack_50,&local_60,0);
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
LAB_004c79d4:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


