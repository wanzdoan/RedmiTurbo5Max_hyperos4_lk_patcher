
/* RtcCallController::canResumeHandleAnswerRequest(android::sp<RfxMessage> const&) */

undefined8 __thiscall
RtcCallController::canResumeHandleAnswerRequest(RtcCallController *this,sp *param_1)

{
  int iVar1;
  ulong uVar2;
  
  if ((*(int *)(*(long *)param_1 + 8) == 0) &&
     (iVar1 = *(int *)(*(long *)param_1 + 0x1c), iVar1 == 0xcf2a || iVar1 == 0xc943)) {
    RfxController::logD((char *)this,"RtcCC","canResumeHandleAnswerRequest");
    uVar2 = isExistAliveCallOnOtherSub(this);
    if (((uVar2 & 1) != 0) ||
       ((*(long *)(this + 0xf0) != 0 && (*(int *)(*(long *)(this + 0xf0) + 0x1c) == 0xcf25)))) {
      return 0;
    }
  }
  return 1;
}


