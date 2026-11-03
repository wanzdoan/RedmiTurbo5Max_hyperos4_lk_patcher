
/* RmcCatCommonRequestHandler::isCpinReady() */

bool __thiscall RmcCatCommonRequestHandler::isCpinReady(RmcCatCommonRequestHandler *this)

{
  long lVar1;
  long *plVar2;
  bool bVar3;
  int iVar4;
  char *pcVar5;
  ulong uVar6;
  RfxAtLine *this_00;
  char *local_58;
  long *local_50;
  long *local_48;
  uint local_3c;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_48 = (long *)0x0;
  RfxBaseHandler::logD
            ((char *)this,"RmcCatComReqHandler","isCpinReady: stk detect cpin state at slot %d",
             (ulong)*(uint *)(this + 8));
  RfxBaseHandler::atSendCommandSingleline((char *)this,"AT+CPIN?","+CPIN:");
  plVar2 = local_48;
  if (local_48 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_48 + *(long *)(*local_48 + -0x18)),&local_48);
  }
  if (plVar2 != local_48) {
    android::sp_report_race();
  }
  local_3c = *(uint *)(local_50 + 5);
  local_48 = local_50;
  if ((local_3c == 0) && (*(int *)((long)local_50 + 0xc) != 0)) {
    this_00 = (RfxAtLine *)local_50[4];
    RfxAtLine::atTokStart(this_00,(int *)&local_3c);
    uVar6 = (ulong)local_3c;
    if ((int)local_3c < 0) {
      pcVar5 = "isCpinReady: atTokStart: err: %d and need retry later";
    }
    else {
      pcVar5 = (char *)RfxAtLine::atTokNextstr(this_00,(int *)&local_3c);
      uVar6 = (ulong)local_3c;
      if (-1 < (int)local_3c) {
        local_50 = (long *)0x0;
        android::String8::String8((String8 *)&local_50,pcVar5);
        android::String8::format("READY",&local_58);
        iVar4 = strcmp((char *)local_50,local_58);
        bVar3 = iVar4 == 0;
        android::String8::~String8((String8 *)&local_58);
        if (iVar4 == 0) {
          RfxBaseHandler::logD
                    ((char *)this,"RmcCatComReqHandler","stk query cpin state at slot %d: success",
                     (ulong)*(uint *)(this + 8));
        }
        else {
          RfxBaseHandler::logE
                    ((char *)this,"RmcCatComReqHandler",
                     "stk query cpin state at slot %d: fail and need retry later");
        }
        android::String8::~String8((String8 *)&local_50);
        goto joined_r0x0042d404;
      }
      pcVar5 = "isCpinReady: atTokNextstr: err: %d and need retry later";
    }
  }
  else {
    uVar6 = (ulong)*(uint *)(this + 8);
    pcVar5 = "isCpinReady: stk detectSim fail at slot %d and need retry later";
  }
  RfxBaseHandler::logE((char *)this,"RmcCatComReqHandler",pcVar5,uVar6);
  bVar3 = false;
joined_r0x0042d404:
  if (local_48 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_48 + *(long *)(*local_48 + -0x18)),&local_48);
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return bVar3;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


