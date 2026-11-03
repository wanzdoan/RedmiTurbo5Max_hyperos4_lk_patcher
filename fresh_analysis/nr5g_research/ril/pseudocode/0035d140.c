
/* RtcModeSwitchController::hasCRadioCapability(int) */

bool __thiscall
RtcModeSwitchController::hasCRadioCapability(RtcModeSwitchController *this,int param_1)

{
  return (param_1 & 0x21f0U) != 0;
}


