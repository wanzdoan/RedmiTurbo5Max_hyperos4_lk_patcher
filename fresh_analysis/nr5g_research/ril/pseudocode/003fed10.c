
/* RtcCallController::onRadioStateChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcCallController::onRadioStateChanged
          (RtcCallController *this,undefined8 param_2,int *param_3,int *param_4)

{
  long lVar1;
  undefined8 uVar2;
  long lVar3;
  long *plVar4;
  ulong uVar5;
  undefined8 local_30;
  long local_28;
  
  uVar2 = DAT_002523e8;
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  if ((*param_3 != 2) || (*param_4 != 2)) {
    uVar5 = __rfx_is_gt_mode();
    if ((uVar5 & 1) == 0) {
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
  if ((uint)param_4[2] < 2) {
    *(undefined4 *)(this + 0x268) = 0;
    *(undefined8 *)(this + 0x58) = uVar2;
    lVar3 = RfxGwsdUtils::getCallControlHandler();
    if (lVar3 != 0) {
      plVar4 = (long *)RfxGwsdUtils::getCallControlHandler();
      (**(code **)(*plVar4 + 0x80))(plVar4,this);
    }
    if (*(long *)(lVar1 + 0x28) == local_28) {
      resetImsFakeMode(this);
      return;
    }
  }
  else if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


