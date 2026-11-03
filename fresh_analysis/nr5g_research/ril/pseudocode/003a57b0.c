
/* RtcNrSwitchController::~RtcNrSwitchController() */

void __thiscall RtcNrSwitchController::~RtcNrSwitchController(RtcNrSwitchController *this)

{
  RfxController::~RfxController((RfxController *)this);
  android::RefBase::~RefBase((RefBase *)(this + 0x60));
  operator_delete(this);
  return;
}


