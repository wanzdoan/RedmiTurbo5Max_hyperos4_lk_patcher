
/* RmcNetworkHandler::updateSbpInfos() */

void __thiscall RmcNetworkHandler::updateSbpInfos(RmcNetworkHandler *this)

{
  undefined *puVar1;
  long lVar2;
  long *plVar3;
  int iVar4;
  RfxAtLine *this_00;
  int local_48;
  undefined4 uStack_44;
  long *local_40;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  local_40 = (long *)0x0;
  RfxBaseHandler::atSendCommandSingleline
            ((char *)this,"AT+ESBP=7,\"SBP_DISABLE_AUTO_RETURN_PRE_RPLMN\"","+ESBP:");
  plVar3 = local_40;
  if (local_40 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_40 + *(long *)(*local_40 + -0x18)),&local_40);
  }
  if (plVar3 != local_40) {
    android::sp_report_race();
  }
  local_40 = (long *)CONCAT44(uStack_44,local_48);
  local_48 = (int)local_40[5];
  if ((((local_48 == 0) && (*(int *)((long)local_40 + 0xc) != 0)) &&
      (this_00 = (RfxAtLine *)local_40[4], this_00 != (RfxAtLine *)0x0)) &&
     ((RfxAtLine::atTokStart(this_00,&local_48), -1 < local_48 &&
      (iVar4 = RfxAtLine::atTokNextint(this_00,&local_48), -1 < local_48)))) {
    puVar1 = &DAT_002240c5;
    if (iVar4 != 1) {
      puVar1 = &DAT_00235024;
    }
    rfx_property_set("persist.vendor.radio.disable_auto_return_rplmn",puVar1);
  }
  else {
    RfxBaseHandler::logE((char *)this,"RmcNwHdlr","There is something wrong with AT+ESBP");
  }
  if (local_40 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_40 + *(long *)(*local_40 + -0x18)),&local_40);
  }
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


