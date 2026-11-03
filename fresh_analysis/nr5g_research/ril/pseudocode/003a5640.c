
/* RtcNrSwitchController::RtcNrSwitchController() */

void __thiscall RtcNrSwitchController::RtcNrSwitchController(RtcNrSwitchController *this)

{
  undefined *puVar1;
  
  android::RefBase::RefBase((RefBase *)(this + 0x60));
  RfxController::RfxController((RfxController *)this);
  puVar1 = PTR_vtable_005d2b50;
  *(undefined8 *)(this + 0x48) = 0;
  this[0x58] = (RtcNrSwitchController)0x0;
  *(undefined8 *)(this + 0x50) = 0xffffffff00000000;
  *(undefined **)this = puVar1 + 0x18;
  *(undefined **)(this + 0x60) = puVar1 + 0xe0;
  *(undefined4 *)(this + 0x5c) = 0;
  return;
}


