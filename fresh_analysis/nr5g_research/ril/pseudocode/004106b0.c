
/* RtcCallController::onRadioOnForEccTimeout() */

void __thiscall RtcCallController::onRadioOnForEccTimeout(RtcCallController *this)

{
  RfxController::logI((char *)this,"RtcCC","onRadioOnForEccTimeout");
  cancelRadioOnForEcc(this);
  return;
}


