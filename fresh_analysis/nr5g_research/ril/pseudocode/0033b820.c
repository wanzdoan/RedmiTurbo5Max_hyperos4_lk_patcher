
/* RfxBaseHandler::unregisterToHandleMipcURC(int const*, unsigned long) */

void __thiscall
RfxBaseHandler::unregisterToHandleMipcURC(RfxBaseHandler *this,int *param_1,ulong param_2)

{
  ulong uVar1;
  uint uVar2;
  int iVar3;
  undefined8 uVar4;
  ulong uVar5;
  ulong uVar6;
  
  RfxHandlerManager::unregisterToHandleMipcUrc
            (this,*(int *)(this + 0xc) + *(int *)(this + 8) * 0xc,*(int *)(this + 8),param_1,
             (int)param_2);
  if (param_2 != 0) {
    uVar1 = 1;
    uVar6 = 0;
    do {
      uVar5 = uVar1;
      uVar4 = RfxMipcUtils::slotIdChangeToMipcSlotId(*(int *)(this + 8));
      uVar2 = mipc_msg_unregister_ind_api(uVar4,param_1[uVar6]);
      if ((((int)uVar2 < 0) &&
          (logE((char *)this,"RfxBaseHandler",
                "unregisterToHandleMipcURC error:%d, slotId:%d, mipcId:%04x",(ulong)uVar2,
                (ulong)*(uint *)(this + 8),(ulong)(uint)param_1[uVar6]), uVar2 == 0xfffffffe)) &&
         ((iVar3 = isInternalLoad(), iVar3 == 1 || (iVar3 = isUserLoad(), iVar3 != 1)))) {
        mtkAssert("unregisterToHandleMipcURC unexpected type mipcId");
      }
      uVar1 = (ulong)((int)uVar5 + 1);
      uVar6 = uVar5;
    } while (uVar5 < param_2);
  }
  return;
}


