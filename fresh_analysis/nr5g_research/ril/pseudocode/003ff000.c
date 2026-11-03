
/* RtcCallController::onRadioPowerEccStatusChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcCallController::onRadioPowerEccStatusChanged
          (RtcCallController *this,undefined8 param_2,int *param_3,int *param_4)

{
  byte bVar1;
  byte bVar2;
  long lVar3;
  int iVar4;
  int iVar5;
  ulong uVar6;
  RtcEccController *this_00;
  RtcCallController *local_68;
  weakref_type *local_60;
  undefined *puStack_58;
  undefined8 local_50;
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  if ((*param_3 != 1) || (*param_4 != 1)) {
    uVar6 = __rfx_is_gt_mode();
    if ((uVar6 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x10c);
    }
    else {
      local_68 = (RtcCallController *)0x0;
      android::String8::format("%s%s",&local_68,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_68,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x10c);
      android::String8::~String8((String8 *)&local_68);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  bVar1 = *(byte *)(param_3 + 2);
  bVar2 = *(byte *)(param_4 + 2);
  iVar4 = RfxRilUtils::getMajorSim();
  iVar5 = RfxController::getSlotId((RfxController *)this);
  if (iVar5 == iVar4 + -1) {
    if ((bVar1 != 0) == (bVar2 != 0)) {
      if (*(long *)(lVar3 + 0x28) == local_48) {
        RfxController::logD((char *)this,"RtcCC",
                            "onRadioPowerEccStatusChanged status unchanged (%d)",(ulong)(uint)bVar1)
        ;
        return;
      }
      goto LAB_003ff20c;
    }
    RfxController::logD((char *)this,"RtcCC","onRadioPowerEccStatusChanged (%d -> %d)",(ulong)bVar1,
                        (ulong)(uint)bVar2);
    this_00 = *(RtcEccController **)(this + 0x278);
    if (bVar2 == 0) {
      if (*(long *)(lVar3 + 0x28) == local_48) {
        RtcEccController::setRadioOnForEcc(this_00,false);
        return;
      }
      goto LAB_003ff20c;
    }
    local_68 = this;
    local_60 = (weakref_type *)android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18))
    ;
    local_50 = 0;
    puStack_58 = PTR_onRadioOnForEccTimeout_005d3328;
    RtcEccController::setRadioOnForEcc(this_00,true,(RfxCallback0 *)&local_68,30000);
    if (local_68 != (RtcCallController *)0x0) {
      android::RefBase::weakref_type::decWeak(local_60,&local_68);
    }
  }
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
LAB_003ff20c:
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


