
/* RmcImsRttControlUrcHandler::onRttTextReceive(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcImsRttControlUrcHandler::onRttTextReceive(RmcImsRttControlUrcHandler *this,sp *param_1)

{
  RfxBaseHandler::notifyStringsDataToTcl((RfxBaseHandler *)this,param_1,0xcf77,3,true);
  return;
}


