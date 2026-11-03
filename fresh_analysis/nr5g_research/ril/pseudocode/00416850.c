
/* RtcSbpController::RtcSbpController() */

void __thiscall RtcSbpController::RtcSbpController(RtcSbpController *this)

{
  long *in_x1;
  long lVar1;
  
  RfxController::RfxController((RfxController *)this);
  lVar1 = *in_x1;
  *(long *)this = lVar1;
  *(long *)(this + *(long *)(lVar1 + -0x18)) = in_x1[5];
  return;
}


