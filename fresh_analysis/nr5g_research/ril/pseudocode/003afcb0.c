
/* RtcNetworkController::isNRSwitching() */

void __thiscall RtcNetworkController::isNRSwitching(RtcNetworkController *this)

{
  RfxStatusManager *this_00;
  
  this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  RfxStatusManager::getBoolValue(this_00,0x56,false);
  return;
}


