
/* RtcSbpController::RtcSbpController() */

void __thiscall RtcSbpController::RtcSbpController(RtcSbpController *this)

{
  undefined *puVar1;
  
  android::RefBase::RefBase((RefBase *)(this + 0x48));
  RfxController::RfxController((RfxController *)this);
  puVar1 = PTR_vtable_005d33d8 + 0xe0;
  *(undefined **)this = PTR_vtable_005d33d8 + 0x18;
  *(undefined **)(this + 0x48) = puVar1;
  return;
}


