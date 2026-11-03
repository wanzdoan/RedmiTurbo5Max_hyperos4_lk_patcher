
/* virtual thunk to RtcNrSwitchController::~RtcNrSwitchController() */

void __thiscall RtcNrSwitchController::~RtcNrSwitchController(RtcNrSwitchController *this)

{
  RfxController *this_00;
  
  this_00 = (RfxController *)(this + *(long *)(*(long *)this + -0x18));
  RfxController::~RfxController(this_00);
  android::RefBase::~RefBase((RefBase *)(this_00 + 0x60));
  operator_delete(this_00);
  return;
}


