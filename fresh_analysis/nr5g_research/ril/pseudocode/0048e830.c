
/* RmcNetworkRequestHandler::setNrOption(int) */

undefined4 __thiscall
RmcNetworkRequestHandler::setNrOption(RmcNetworkRequestHandler *this,int param_1)

{
  long lVar1;
  long *plVar2;
  undefined4 uVar3;
  String8 aSStack_50 [8];
  long *local_48;
  long *local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_40 = (long *)0x0;
  android::String8::format("AT+E5GOPT=%d",aSStack_50,(ulong)(uint)param_1);
  RfxBaseHandler::atSendCommand((String8 *)this,aSStack_50);
  plVar2 = local_40;
  if (local_40 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_40 + *(long *)(*local_40 + -0x18)),&local_40);
  }
  if (plVar2 != local_40) {
    android::sp_report_race();
  }
  local_40 = local_48;
  local_48 = (long *)0x0;
  android::String8::~String8(aSStack_50);
  if (((int)local_40[5] < 0) || (*(int *)((long)local_40 + 0xc) == 0)) {
    RfxBaseHandler::logE((char *)this,"RmcNwReqHdlr","+E5GOPT[%d] fail",(ulong)(uint)param_1);
    uVar3 = 0xffffffff;
    if (local_40 == (long *)0x0) goto LAB_0048e914;
  }
  else {
    uVar3 = 0;
  }
  android::RefBase::decStrong((RefBase *)((long)local_40 + *(long *)(*local_40 + -0x18)),&local_40);
LAB_0048e914:
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return uVar3;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


