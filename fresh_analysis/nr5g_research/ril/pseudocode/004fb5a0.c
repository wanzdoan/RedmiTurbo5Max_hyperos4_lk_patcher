
/* RmcDcCommonReqHandler::RmcDcCommonReqHandler(int, int, RmcDcPdnManager*) */

void __thiscall
RmcDcCommonReqHandler::RmcDcCommonReqHandler
          (RmcDcCommonReqHandler *this,int param_1,int param_2,RmcDcPdnManager *param_3)

{
  long lVar1;
  undefined8 uVar2;
  undefined8 uVar3;
  undefined *puVar4;
  byte bVar5;
  undefined4 uVar6;
  int iVar7;
  int iVar8;
  char local_60 [8];
  undefined2 local_58;
  char local_50 [8];
  long local_48;
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  android::RefBase::RefBase((RefBase *)(this + 0x40));
  puVar4 = PTR_vtable_005d4398;
  *(int *)(this + 8) = param_1;
  *(int *)(this + 0xc) = param_2;
  *(undefined **)this = puVar4 + 0x18;
  *(undefined **)(this + 0x40) = puVar4 + 0x1e8;
  *(undefined **)(this + 0x10) = puVar4 + 0x1a0;
  *(RmcDcPdnManager **)(this + 0x18) = param_3;
  *(undefined4 *)(this + 0x20) = 0xffff;
  uVar6 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,"DMF_TC02");
  *(undefined4 *)(this + 0x28) = uVar6;
  bVar5 = RmcDcUtility::isUserBuild();
  uVar2 = s_MD_RSRAV3_0020ec64._0_8_;
  this[0x38] = (RmcDcCommonReqHandler)0x0;
  *(undefined4 *)(this + 0x3c) = 0;
  this[0x2c] = (RmcDcCommonReqHandler)(bVar5 & 1);
  uVar3 = s_MD_RSRAV3_0020ec64._0_8_;
  builtin_strncpy(local_50,"MD_RSRA",8);
  s_MD_RSRAV3_0020ec64[0] = (char)uVar2;
  s_MD_RSRAV3_0020ec64[1] = SUB81(uVar2,1);
  s_MD_RSRAV3_0020ec64[2] = SUB81(uVar2,2);
  s_MD_RSRAV3_0020ec64[3] = SUB81(uVar2,3);
  s_MD_RSRAV3_0020ec64[4] = SUB81(uVar2,4);
  s_MD_RSRAV3_0020ec64[5] = SUB81(uVar2,5);
  s_MD_RSRAV3_0020ec64[6] = SUB81(uVar2,6);
  s_MD_RSRAV3_0020ec64[7] = SUB81(uVar2,7);
  local_60[0] = s_MD_RSRAV3_0020ec64[0];
  local_60[1] = s_MD_RSRAV3_0020ec64[1];
  local_60[2] = s_MD_RSRAV3_0020ec64[2];
  local_60[3] = s_MD_RSRAV3_0020ec64[3];
  local_60[4] = s_MD_RSRAV3_0020ec64[4];
  local_60[5] = s_MD_RSRAV3_0020ec64[5];
  local_60[6] = s_MD_RSRAV3_0020ec64[6];
  local_60[7] = s_MD_RSRAV3_0020ec64[7];
  local_58 = 0x33;
  s_MD_RSRAV3_0020ec64._0_8_ = uVar3;
  iVar7 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_50);
  iVar8 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_60);
  bVar5 = RmcDcUtility::isMdrsraSupport(iVar7,iVar8);
  this[0x2d] = (RmcDcCommonReqHandler)(bVar5 & 1);
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


