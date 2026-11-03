
/* RmcAtciCommonRequestHandler::RmcAtciCommonRequestHandler(int, int) */

void __thiscall
RmcAtciCommonRequestHandler::RmcAtciCommonRequestHandler
          (RmcAtciCommonRequestHandler *this,int param_1,int param_2)

{
  long lVar1;
  int local_48 [2];
  char *local_40;
  undefined *puStack_38;
  long local_28;
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  RmcAtciRequestHandler::RmcAtciRequestHandler((RmcAtciRequestHandler *)this,param_1,param_2);
  puStack_38 = PTR_s__ERSSI_005ce2e8;
  local_40 = PTR_s__ERXRSSI_005ce2e0;
  local_48[0] = 0xd107;
  local_48[1] = 0xd105;
  *(undefined **)this = PTR_vtable_005d4128 + 0x10;
  RfxBaseHandler::registerToHandleRequest((RfxBaseHandler *)this,local_48 + 1,1);
  RfxBaseHandler::registerToHandleEvent((RfxBaseHandler *)this,local_48,1);
  RfxBaseHandler::registerToHandleURC((RfxBaseHandler *)this,&local_40,2);
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


