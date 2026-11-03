
/* RtcCapabilitySwitchChecker::isLteNetworkType(int) */

uint __thiscall
RtcCapabilitySwitchChecker::isLteNetworkType(RtcCapabilitySwitchChecker *this,int param_1)

{
  return (uint)(0x17 < (uint)param_1) | 0x5a9f00U >> (ulong)(param_1 & 0x1f) & 1;
}


