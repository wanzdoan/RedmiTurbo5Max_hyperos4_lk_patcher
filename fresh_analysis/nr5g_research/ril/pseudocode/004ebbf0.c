
/* RmcDcReqHandler::handleQualifiedNetworkTypeChanged(android::sp<RfxMclMessage> const&) */

void RmcDcReqHandler::handleQualifiedNetworkTypeChanged(sp *param_1)

{
  long lVar1;
  ulong uVar2;
  undefined8 local_20;
  long local_18;
  
  lVar1 = tpidr_el0;
  local_18 = *(long *)(lVar1 + 0x28);
  if (*(long **)(param_1 + 0x18) == (long *)0x0) {
    uVar2 = __rfx_is_gt_mode();
    if ((uVar2 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/data/RmcDcReqHandler.cpp"
              ,0x313);
    }
    else {
      local_20 = 0;
      android::String8::format("%s%s",&local_20,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_20,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/mdcomm/data/RmcDcReqHandler.cpp"
              ,0x313);
      android::String8::~String8((String8 *)&local_20);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  if (*(long *)(lVar1 + 0x28) == local_18) {
                    /* WARNING: Could not recover jumptable at 0x004ebc30. Too many branches */
                    /* WARNING: Treating indirect jump as call */
    (**(code **)(**(long **)(param_1 + 0x18) + 0xa8))();
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


