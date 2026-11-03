
/* RfxController::unregisterToHandleRequest(int, int const*, unsigned long,
   RfxController::HANDLER_PRIORITY) */

void __thiscall
RfxController::unregisterToHandleRequest
          (undefined8 param_1_00,undefined4 param_1,undefined8 param_2,undefined8 param_3,
          undefined4 param_5)

{
  RfxRootController *pRVar1;
  
  pRVar1 = (RfxRootController *)RfxRootController::getInstance();
  RfxRootController::unregisterToHandleRequest(pRVar1,param_1,param_2,param_3,param_5);
  return;
}


