
/* RtcRatSwitchController::isNetworkTypeContainGsmCdma() */

bool __thiscall RtcRatSwitchController::isNetworkTypeContainGsmCdma(RtcRatSwitchController *this)

{
  return (*(uint *)(this + 0xfc) & 0x1bce0e) != 0 && (*(uint *)(this + 0xfc) & 0x31f0) != 0;
}


