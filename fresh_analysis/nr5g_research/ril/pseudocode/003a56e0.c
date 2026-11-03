
/* RtcNrSwitchController::RtcNrSwitchController() */

void __thiscall RtcNrSwitchController::RtcNrSwitchController(RtcNrSwitchController *this)

{
  long *in_x1;
  long lVar1;
  
  RfxController::RfxController((RfxController *)this);
  lVar1 = *in_x1;
  *(long *)this = lVar1;
  *(long *)(this + *(long *)(lVar1 + -0x18)) = in_x1[5];
  *(undefined8 *)(this + 0x48) = 0;
  *(undefined8 *)(this + 0x50) = 0xffffffff00000000;
  this[0x58] = (RtcNrSwitchController)0x0;
  *(undefined4 *)(this + 0x5c) = 0;
  return;
}


