
/* RmcImsRttControlUrcHandler::onRttModifyResponse(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcImsRttControlUrcHandler::onRttModifyResponse(RmcImsRttControlUrcHandler *this,sp *param_1)

{
  RfxBaseHandler::notifyIntsDataToTcl((RfxBaseHandler *)this,param_1,0xcf76,2,true);
  return;
}


