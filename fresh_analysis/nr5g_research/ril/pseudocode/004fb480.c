
/* RmcDcCommonReqHandler::RmcDcCommonReqHandler(int, int, RmcDcPdnManager*) */

void __thiscall
RmcDcCommonReqHandler::RmcDcCommonReqHandler
          (RmcDcCommonReqHandler *this,int param_1,int param_2,RmcDcPdnManager *param_3)

{
  undefined *puVar1;
  long lVar2;
  undefined8 uVar3;
  undefined8 uVar4;
  byte bVar5;
  undefined4 uVar6;
  int iVar7;
  int iVar8;
  long *plVar9;
  undefined8 in_x4;
  RmcDcCommonReqHandler *pRVar10;
  long lVar11;
  char local_50 [8];
  undefined2 local_48;
  char local_40 [8];
  long local_38;
  
  plVar9 = (long *)(ulong)(uint)param_1;
  lVar2 = tpidr_el0;
  local_38 = *(long *)(lVar2 + 0x28);
  puVar1 = PTR_vtable_005d2330 + 0x10;
  *(int *)(this + 8) = param_2;
  *(int *)(this + 0xc) = (int)param_3;
  *(undefined **)this = puVar1;
  lVar11 = plVar9[1];
  pRVar10 = this + 0x10;
  *(long *)pRVar10 = lVar11;
  *(long *)(pRVar10 + *(long *)(lVar11 + -0x18)) = plVar9[2];
  lVar11 = *plVar9;
  *(long *)this = lVar11;
  *(long *)pRVar10 = plVar9[3];
  *(long *)(this + *(long *)(lVar11 + -0x18)) = plVar9[4];
  *(undefined8 *)(this + 0x18) = in_x4;
  *(undefined4 *)(this + 0x20) = 0xffff;
  uVar6 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,"DMF_TC02");
  *(undefined4 *)(this + 0x28) = uVar6;
  bVar5 = RmcDcUtility::isUserBuild();
  uVar3 = s_MD_RSRAV3_0020ec64._0_8_;
  this[0x38] = (RmcDcCommonReqHandler)0x0;
  *(undefined4 *)(this + 0x3c) = 0;
  this[0x2c] = (RmcDcCommonReqHandler)(bVar5 & 1);
  uVar4 = s_MD_RSRAV3_0020ec64._0_8_;
  builtin_strncpy(local_40,"MD_RSRA",8);
  s_MD_RSRAV3_0020ec64[0] = (char)uVar3;
  s_MD_RSRAV3_0020ec64[1] = SUB81(uVar3,1);
  s_MD_RSRAV3_0020ec64[2] = SUB81(uVar3,2);
  s_MD_RSRAV3_0020ec64[3] = SUB81(uVar3,3);
  s_MD_RSRAV3_0020ec64[4] = SUB81(uVar3,4);
  s_MD_RSRAV3_0020ec64[5] = SUB81(uVar3,5);
  s_MD_RSRAV3_0020ec64[6] = SUB81(uVar3,6);
  s_MD_RSRAV3_0020ec64[7] = SUB81(uVar3,7);
  local_50[0] = s_MD_RSRAV3_0020ec64[0];
  local_50[1] = s_MD_RSRAV3_0020ec64[1];
  local_50[2] = s_MD_RSRAV3_0020ec64[2];
  local_50[3] = s_MD_RSRAV3_0020ec64[3];
  local_50[4] = s_MD_RSRAV3_0020ec64[4];
  local_50[5] = s_MD_RSRAV3_0020ec64[5];
  local_50[6] = s_MD_RSRAV3_0020ec64[6];
  local_50[7] = s_MD_RSRAV3_0020ec64[7];
  local_48 = 0x33;
  s_MD_RSRAV3_0020ec64._0_8_ = uVar4;
  iVar7 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_40);
  iVar8 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_50);
  bVar5 = RmcDcUtility::isMdrsraSupport(iVar7,iVar8);
  this[0x2d] = (RmcDcCommonReqHandler)(bVar5 & 1);
  if (*(long *)(lVar2 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


