
/* virtual thunk to RtcSbpController::~RtcSbpController() */

void __thiscall RtcSbpController::~RtcSbpController(RtcSbpController *this)

{
  RfxController *this_00;
  
  this_00 = (RfxController *)(this + *(long *)(*(long *)this + -0x18));
  RfxController::~RfxController(this_00);
  android::RefBase::~RefBase((RefBase *)(this_00 + 0x48));
  operator_delete(this_00);
  return;
}


