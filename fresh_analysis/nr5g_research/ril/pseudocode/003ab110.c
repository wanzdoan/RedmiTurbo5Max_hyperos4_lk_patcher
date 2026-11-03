
/* RtcRatSwitchController::calculateDefaultNetworkType(RatSwitchInfo) */

uint __thiscall
RtcRatSwitchController::calculateDefaultNetworkType(RtcRatSwitchController *this,uint *param_2)

{
  char *pcVar1;
  uint uVar2;
  uint uVar3;
  RfxStatusManager *this_00;
  ulong uVar4;
  uint uVar5;
  uint uVar6;
  
  this_00 = (RfxStatusManager *)
            RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
  uVar3 = RfxStatusManager::getIntValue(this_00,0x2d,0);
  uVar6 = *param_2;
  uVar4 = (ulong)uVar6;
  uVar5 = uVar3;
  if (param_2[3] == 0xffffffff) {
    if ((((uVar6 < 0x13) && ((1 << (ulong)(uVar6 & 0x1f) & 0x60006U) != 0)) &&
        ((char)param_2[2] == '\0')) ||
       (uVar4 = needDisableCdmaRat(this), uVar6 = uVar3, (uVar4 & 1) != 0)) {
      uVar6 = uVar3 & 0xffffce0f;
    }
    uVar2 = *param_2;
    uVar4 = (ulong)uVar2;
    uVar5 = uVar6;
    if ((((uVar6 & 0x31f0) != 0) && ((uVar2 & 0xc) != 0)) &&
       (((uVar2 >> 1 & 1) == 0 &&
        (((char)param_2[2] == '\0' && (uVar5 = 0x70, (uVar6 & 0x3180) != 0)))))) {
      uVar5 = 0x31f0;
    }
  }
  pcVar1 = "false";
  if ((char)param_2[2] != '\0') {
    pcVar1 = "true";
  }
  RfxController::logD((char *)this,"RtcRatSwCtrl",
                      "[calculateDefaultNetworkType] card_type: %d, card_state: %d, rat_mode: %d, isCt3GDualMode: %s, ct3gStatus: %d.  slotCap: %d, defaultRaf: %d."
                      ,uVar4,(ulong)param_2[1],(ulong)param_2[3],pcVar1,(ulong)param_2[4],uVar3,
                      uVar5);
  return uVar5;
}


