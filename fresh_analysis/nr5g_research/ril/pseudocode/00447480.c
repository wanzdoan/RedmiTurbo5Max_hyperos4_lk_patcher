
/* RmcSuppServRequestNRTHandler::RmcSuppServRequestNRTHandler(int, int) */

void __thiscall
RmcSuppServRequestNRTHandler::RmcSuppServRequestNRTHandler
          (RmcSuppServRequestNRTHandler *this,int param_1,int param_2)

{
  undefined *puVar1;
  
  puVar1 = PTR_vtable_005d36b0 + 0x10;
  *(int *)(this + 8) = param_1;
  *(int *)(this + 0xc) = param_2;
  *(undefined **)this = puVar1;
  RfxBaseHandler::registerToHandleRequest((RfxBaseHandler *)this,(int *)&DAT_0025a678,1);
  return;
}


