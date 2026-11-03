
/* RmcImsRttControlUrcHandler::onRttModifyRequestReceive(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcImsRttControlUrcHandler::onRttModifyRequestReceive(RmcImsRttControlUrcHandler *this,sp *param_1)

{
  RfxBaseHandler::notifyIntsDataToTcl((RfxBaseHandler *)this,param_1,0xcf78,2,true);
  return;
}


