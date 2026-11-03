
/* RtcModeSwitchController::updateRadioCapability() */

void __thiscall RtcModeSwitchController::updateRadioCapability(RtcModeSwitchController *this)

{
  RfxClassInfo *pRVar1;
  RtcCapabilityGetController *this_00;
  
  pRVar1 = (RfxClassInfo *)RtcCapabilityGetController::getRtcCapabilityGetControllerClassInfo();
  this_00 = (RtcCapabilityGetController *)
            RfxController::findController((RfxController *)this,*(int *)(this + 0x40),pRVar1);
  if (this_00 != (RtcCapabilityGetController *)0x0) {
    RtcCapabilityGetController::updateRadioCapability(this_00,*(int *)(this + 0x48));
    return;
  }
  return;
}


