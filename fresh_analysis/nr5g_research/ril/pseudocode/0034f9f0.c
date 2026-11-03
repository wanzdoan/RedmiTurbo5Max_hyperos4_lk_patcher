
/* RfxController::unregisterToHandleUrc(int, int const*, unsigned long) */

void __thiscall
RfxController::unregisterToHandleUrc(RfxController *this,int param_1,int *param_2,ulong param_3)

{
  RfxRootController *this_00;
  
  this_00 = (RfxRootController *)RfxRootController::getInstance();
  RfxRootController::unregisterToHandleUrc(this_00,param_1,param_2,param_3);
  return;
}


