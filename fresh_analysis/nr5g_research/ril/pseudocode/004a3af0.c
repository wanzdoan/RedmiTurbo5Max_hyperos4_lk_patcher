
/* RmcNetworkRealTimeRequestHandler::skipDCNRRestrictedState(char const*) */

bool __thiscall
RmcNetworkRealTimeRequestHandler::skipDCNRRestrictedState
          (RmcNetworkRealTimeRequestHandler *this,char *param_1)

{
  int iVar1;
  
  iVar1 = RfxMisc::strStartsWith(param_1,"42507");
  return iVar1 != 0;
}


