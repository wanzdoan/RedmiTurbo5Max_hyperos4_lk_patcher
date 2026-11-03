
/* RmcImsRttControlUrcHandler::onRttCapabilityIndication(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcImsRttControlUrcHandler::onRttCapabilityIndication(RmcImsRttControlUrcHandler *this,sp *param_1)

{
  RfxBaseHandler::notifyIntsDataToTcl((RfxBaseHandler *)this,param_1,0xcf75,5,true);
  return;
}


