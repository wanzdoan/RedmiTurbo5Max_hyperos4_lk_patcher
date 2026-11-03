
/* RtcImsConfigController::setVoNREnable(android::sp<RfxMessage> const&) */

void RtcImsConfigController::setVoNREnable(sp *param_1)

{
  uint uVar1;
  undefined4 uVar2;
  undefined4 uVar3;
  long lVar4;
  int iVar5;
  long *in_x1;
  undefined8 in_x2;
  uint *puVar6;
  undefined4 uVar7;
  char *pcVar8;
  RfxVoidData local_70 [6];
  char acStack_6a [2];
  char acStack_68 [16];
  long *local_58;
  undefined2 local_50;
  long local_48;
  
  lVar4 = tpidr_el0;
  local_48 = *(long *)(lVar4 + 0x28);
  puVar6 = *(uint **)(*(long *)(*in_x1 + 0x48) + 8);
  if (puVar6 == (uint *)0x0) {
    RfxController::logD((char *)param_1,"RtcImsConfigController","setVoNREnable(), params is null");
    uVar7 = 2;
  }
  else {
    uVar1 = *puVar6;
    local_50 = 0;
    local_58 = (long *)0x0;
    iVar5 = sprintf((char *)&local_58,(char *)in_x1,in_x2,(ulong)uVar1);
    if (iVar5 < 0) {
      RfxController::logE((char *)param_1,"RtcImsConfigController",
                          "setVoNREnable, sprintf to isEnable fail");
    }
    RfxController::logD((char *)param_1,"RtcImsConfigController",
                        "setVoNREnable(), params[0]: %d, value: %s",(ulong)uVar1,&local_58);
    pcVar8 = *(char **)PTR_PROPERTY_VILTE_ENALBE_005d2cf0;
    iVar5 = RfxController::getSlotId((RfxController *)param_1);
    iVar5 = ImsConfigUtils::getFeaturePropValue(pcVar8,iVar5);
    if ((uVar1 == 1) && (iVar5 == 1)) {
      pcVar8 = *(char **)PTR_PROPERTY_VINR_ENALBE_005d2d10;
      iVar5 = RfxController::getSlotId((RfxController *)param_1);
      ImsConfigUtils::setFeaturePropValue(pcVar8,(char *)&local_58,iVar5);
    }
    pcVar8 = *(char **)PTR_PROPERTY_VONR_ENALBE_005d2d00;
    iVar5 = RfxController::getSlotId((RfxController *)param_1);
    ImsConfigUtils::setFeaturePropValue(pcVar8,(char *)&local_58,iVar5);
    local_70[0] = (RfxVoidData)s_IMS_over_3gpp_001f9bb1[0];
    local_70[1] = (RfxVoidData)s_IMS_over_3gpp_001f9bb1[1];
    local_70[2] = (RfxVoidData)s_IMS_over_3gpp_001f9bb1[2];
    local_70[3] = (RfxVoidData)s_IMS_over_3gpp_001f9bb1[3];
    local_70[4] = (RfxVoidData)s_IMS_over_3gpp_001f9bb1[4];
    local_70[5] = (RfxVoidData)s_IMS_over_3gpp_001f9bb1[5];
    acStack_6a[0] = s_IMS_over_3gpp_001f9bb1[6];
    acStack_6a[1] = s_IMS_over_3gpp_001f9bb1[7];
    acStack_68[0] = s_IMS_over_3gpp_001f9bb1[8];
    acStack_68[1] = s_IMS_over_3gpp_001f9bb1[9];
    acStack_68[2] = s_IMS_over_3gpp_001f9bb1[10];
    acStack_68[3] = s_IMS_over_3gpp_001f9bb1[0xb];
    acStack_68[4] = s_IMS_over_3gpp_001f9bb1[0xc];
    acStack_68[5] = s_IMS_over_3gpp_001f9bb1[0xd];
    iVar5 = RfxController::getFeatureVersion((RfxController *)param_1,(char *)local_70);
    if (1 < iVar5) {
      iVar5 = RfxController::getSlotId((RfxController *)param_1);
      triggerImsCfgCommand((RtcImsConfigController *)param_1,iVar5);
    }
    uVar7 = 0;
  }
  local_58 = (long *)0x0;
  uVar2 = *(undefined4 *)(*in_x1 + 0x24);
  uVar3 = *(undefined4 *)(*in_x1 + 0x1c);
  RfxVoidData::RfxVoidData(local_70);
  RfxMessage::obtainResponse((RfxMessage *)&local_58,uVar2,uVar3,uVar7,local_70);
  RfxVoidData::~RfxVoidData(local_70);
  RfxController::responseToRilj((RfxController *)param_1,(sp *)&local_58);
  if (local_58 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
  }
  if (*(long *)(lVar4 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


