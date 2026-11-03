
/* RtcEccNumberController::onRadioStateChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcEccNumberController::onRadioStateChanged
          (RtcEccNumberController *this,undefined8 param_2,undefined8 param_3,int *param_4)

{
  long lVar1;
  ulong uVar2;
  undefined8 local_30;
  long local_28;
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  if (*param_4 != 2) {
    uVar2 = __rfx_is_gt_mode();
    if ((uVar2 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
    }
    else {
      local_30 = 0;
      android::String8::format("%s%s",&local_30,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_30,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
      android::String8::~String8((String8 *)&local_30);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  if (param_4[2] == 0) {
    RfxController::logI((char *)this,"RtcEccNumberController",
                        "[%s] radio off, clear network ECC list","onRadioStateChanged");
    rfx_property_set("vendor.ril.ecc.service.category.list" + (long)*(int *)(this + 0x40) * 0x5c,
                     &DAT_00207797);
    android::VectorImpl::clear();
    android::VectorImpl::clear();
    if (*(long *)(lVar1 + 0x28) == local_28) {
      updateNetworkEccList(this,true);
      return;
    }
  }
  else if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


