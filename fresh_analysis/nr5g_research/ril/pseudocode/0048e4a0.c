
/* RmcNetworkRequestHandler::getNrOption() */

undefined4 __thiscall RmcNetworkRequestHandler::getNrOption(RmcNetworkRequestHandler *this)

{
  long lVar1;
  long *plVar2;
  undefined4 uVar3;
  RfxAtLine *this_00;
  long *local_40;
  int local_34;
  long *local_30;
  long local_28;
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  local_30 = (long *)0x0;
  RfxBaseHandler::atSendCommandSingleline((char *)this,"AT+E5GOPT?","+E5GOPT:");
  plVar2 = local_30;
  if (local_30 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_30 + *(long *)(*local_30 + -0x18)),&local_30);
  }
  if (plVar2 != local_30) {
    android::sp_report_race();
  }
  local_34 = (int)local_40[5];
  local_30 = local_40;
  if (local_34 == 0) {
    if (((*(int *)((long)local_40 + 0xc) == 0) ||
        (this_00 = (RfxAtLine *)local_40[4], this_00 == (RfxAtLine *)0x0)) ||
       (RfxAtLine::atTokStart(this_00,&local_34), local_34 < 0)) {
      uVar3 = 0xffffffff;
    }
    else {
      uVar3 = RfxAtLine::atTokNextint(this_00,&local_34);
      if (local_34 < 0) {
        uVar3 = 0xffffffff;
      }
    }
    if (local_30 == (long *)0x0) goto LAB_0048e58c;
  }
  else {
    uVar3 = 0xffffffff;
  }
  android::RefBase::decStrong((RefBase *)((long)local_30 + *(long *)(*local_30 + -0x18)),&local_30);
LAB_0048e58c:
  if (*(long *)(lVar1 + 0x28) != local_28) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return uVar3;
}


