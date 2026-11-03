
/* RmcNetworkRequestHandler::onSync5GIconShowRuleWithOtherSlot(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcNetworkRequestHandler::onSync5GIconShowRuleWithOtherSlot
          (RmcNetworkRequestHandler *this,sp *param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint *puVar4;
  
  RfxBaseHandler::logV
            ((char *)this,"RmcNwReqHdlr",&DAT_00218901,"onSync5GIconShowRuleWithOtherSlot");
  puVar4 = *(uint **)(*(long *)(*(long *)param_1 + 0x10) + 8);
  uVar1 = *puVar4;
  uVar2 = puVar4[1];
  uVar3 = puVar4[2];
  RfxBaseHandler::logD
            ((char *)this,"RmcNwReqHdlr","onSync5GIconShowRuleWithOtherSlot set_nr_display_rule, %d"
             ,(ulong)uVar1);
  updateNrDisplayRule(this,true,uVar1,uVar2,uVar3);
  return;
}


