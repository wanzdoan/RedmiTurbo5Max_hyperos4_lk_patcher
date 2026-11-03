
/* RtcCatController::handleQueryCpinResponse(android::sp<RfxMessage> const&) */

void __thiscall RtcCatController::handleQueryCpinResponse(RtcCatController *this,sp *param_1)

{
  int iVar1;
  int iVar2;
  long lVar3;
  int *piVar4;
  
  RfxController::logD((char *)this,"RtcCatController","handleQueryCpinResponse");
  lVar3 = *(long *)(*(long *)param_1 + 0x48);
  iVar1 = *(int *)(*(long *)param_1 + 0x1c);
  if (((lVar3 == 0) || (piVar4 = *(int **)(lVar3 + 8), piVar4 == (int *)0x0)) ||
     (iVar2 = *piVar4,
     RfxController::logD((char *)this,"RtcCatController","handleQueryCpinResponse: isCpinReady = %d"
                         ,(ulong)(iVar2 == 1)), iVar2 != 1)) {
    if (*(int *)(this + 0xb8) < 0xe) {
      *(int *)(this + 0xb8) = *(int *)(this + 0xb8) + 1;
      sendEventWithDelay(this,iVar1,200);
      RfxController::logD((char *)this,"RtcCatController",
                          "handleQueryCpinResponse: cpin not ready, after 200ms retry again!");
      return;
    }
  }
  else {
    *(undefined4 *)(this + 0xb8) = 0;
  }
  handleSimRefreshAfterQueryCpin(this);
  return;
}


