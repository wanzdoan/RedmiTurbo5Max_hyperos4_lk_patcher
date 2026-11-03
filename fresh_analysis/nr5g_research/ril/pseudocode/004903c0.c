
/* RmcNetworkNrtRequestHandler::RmcNetworkNrtRequestHandler(int, int) */

void __thiscall
RmcNetworkNrtRequestHandler::RmcNetworkNrtRequestHandler
          (RmcNetworkNrtRequestHandler *this,int param_1,int param_2)

{
  undefined *puVar1;
  long lVar2;
  long *plVar3;
  long *local_48;
  long *local_40;
  long local_38;
  
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  RmcNetworkHandler::RmcNetworkHandler((RmcNetworkHandler *)this,param_1,param_2);
  puVar1 = PTR_vtable_005d3f58 + 0x10;
  *(undefined4 *)(this + 0x18) = 0;
  *(undefined **)this = puVar1;
  local_40 = (long *)0x0;
  RfxBaseHandler::logV
            ((char *)this,"RmcNwReqHdlr","%s[%d] start","RmcNetworkNrtRequestHandler",
             (ulong)(uint)param_1);
  *(int *)(this + 0x10) = param_1;
  *(int *)(this + 0x14) = param_2;
  RfxBaseHandler::registerToHandleRequest((RfxBaseHandler *)this,(int *)&DAT_00293078,4);
  RfxBaseHandler::registerToHandleEvent((RfxBaseHandler *)this,(int *)&DAT_00293088,1);
  RfxBaseHandler::atSendCommand((char *)this,"AT+COPS=3,3");
  plVar3 = local_40;
  if (local_40 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_40 + *(long *)(*local_40 + -0x18)),&local_40);
  }
  if (plVar3 != local_40) {
    android::sp_report_race();
  }
  if ((-1 < (int)local_48[5]) || (*(int *)((long)local_48 + 0xc) != 0)) {
    *(undefined4 *)(this + 0x18) = 1;
  }
  android::RefBase::decStrong((RefBase *)((long)local_48 + *(long *)(*local_48 + -0x18)),&local_40);
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


