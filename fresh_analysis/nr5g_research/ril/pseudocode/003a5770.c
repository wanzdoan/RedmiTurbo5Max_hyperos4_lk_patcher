
/* virtual thunk to RtcNrSwitchController::~RtcNrSwitchController() */

void __thiscall RtcNrSwitchController::~RtcNrSwitchController(RtcNrSwitchController *this)

{
  long lVar1;
  
  lVar1 = *(long *)(*(long *)this + -0x18);
  RfxController::~RfxController((RfxController *)(this + lVar1));
  android::RefBase::~RefBase((RefBase *)((RfxController *)(this + lVar1) + 0x60));
  return;
}


