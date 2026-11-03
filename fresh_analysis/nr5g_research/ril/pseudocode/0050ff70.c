
/* RmcDcCommonReqHandler::onQualifiedNetworkTypeChanged(android::sp<RfxMclMessage> const&) */

void __thiscall
RmcDcCommonReqHandler::onQualifiedNetworkTypeChanged(RmcDcCommonReqHandler *this,sp *param_1)

{
  long lVar1;
  int iVar2;
  ulong uVar3;
  undefined8 local_50;
  char local_48 [8];
  undefined4 local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_48 = (char  [8])s_IWLPL_VERNO_002098da._0_8_;
  local_40 = 0x4f4e52;
  iVar2 = RfxBaseHandler::getFeatureVersion((RfxBaseHandler *)this,local_48,1);
  if (iVar2 == 2) {
    handleQualifiedNetworkTypeChanged_V2(this,param_1);
  }
  else if (iVar2 == 1) {
    handleQualifiedNetworkTypeChanged_V1(this,param_1);
  }
  else {
    uVar3 = __rfx_is_gt_mode();
    if ((uVar3 & 1) == 0) {
      mtkLogE("RmcDcCommon","[%d][%s] unknown version for QualifiedNetworkTypeChanged!",
              *(undefined4 *)(this + 8),"onQualifiedNetworkTypeChanged");
    }
    else {
      local_50 = 0;
      android::String8::format("%s%s",&local_50,&DAT_001fbebf,"RmcDcCommon");
      mtkLogE(local_50,"[%d][%s] unknown version for QualifiedNetworkTypeChanged!",
              *(undefined4 *)(this + 8),"onQualifiedNetworkTypeChanged");
      android::String8::~String8((String8 *)&local_50);
    }
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


