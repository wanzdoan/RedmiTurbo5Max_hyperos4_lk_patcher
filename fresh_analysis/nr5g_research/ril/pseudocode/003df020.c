
/* RtcCapabilitySwitchController::onRildInitDone(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcCapabilitySwitchController::onRildInitDone
          (RtcCapabilitySwitchController *this,uint param_1,int *param_3,int *param_4)

{
  long lVar1;
  uint uVar2;
  undefined8 uVar3;
  RfxStatusManager *this_00;
  ulong uVar4;
  RtcCapabilitySwitchController *local_58;
  weakref_type *local_50;
  undefined *puStack_48;
  undefined8 local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  uVar3 = RfxStatusManager::getKeyString(param_1);
  if ((*param_3 == 1) && (*param_4 == 1)) {
    RfxController::logI((char *)this,"RtcCapa","onRildInitDone:%s(%d==>%d)",uVar3,
                        (ulong)*(byte *)(param_3 + 2),(ulong)*(byte *)(param_4 + 2));
    if (*param_4 == 1) {
      if ((char)param_4[2] != '\0') {
        this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
        local_58 = this;
        local_50 = (weakref_type *)
                   android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
        local_40 = 0;
        puStack_48 = PTR_onRildInitDone_005d3098;
        RfxStatusManager::unRegisterStatusChanged(this_00,0xc6,(RfxCallback3 *)&local_58);
        if (local_58 != (RtcCapabilitySwitchController *)0x0) {
          android::RefBase::weakref_type::decWeak(local_50,&local_58);
        }
        uVar4 = RtcCapabilitySwitchChecker::isReadyForSwitch
                          (*(RtcCapabilitySwitchChecker **)(this + 0x70),true);
        if (((uVar4 & 1) == 0) || (this[0x60] != (RtcCapabilitySwitchController)0x0)) {
          RfxController::logD((char *)this,"RtcCapa","onRildInitDone, not ready for switch");
          registerStatusKeys(this);
        }
        else {
          uVar2 = (**(code **)(**(long **)(this + 0x70) + 0x98))();
          uVar4 = (ulong)uVar2;
          RfxController::logD((char *)this,"RtcCapa",
                              "checkAndSwitchCapability, high_priority_slot=%d",uVar4);
          if ((int)uVar2 < 0) {
            uVar4 = (ulong)*(uint *)(this + 0x50);
          }
          else {
            *(uint *)(this + 0x50) = uVar2;
          }
          uVar4 = (**(code **)(**(long **)(this + 0x70) + 0x90))(*(long **)(this + 0x70),uVar4);
          if ((uVar4 & 1) == 0) {
            handleRequestSwitchCapability(this);
          }
        }
      }
      if (*(long *)(lVar1 + 0x28) == local_38) {
        return;
      }
                    /* WARNING: Subroutine does not return */
      __stack_chk_fail();
    }
  }
  uVar4 = __rfx_is_gt_mode();
  if ((uVar4 & 1) == 0) {
    mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
            "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
            ,0x10c);
  }
  else {
    local_58 = (RtcCapabilitySwitchController *)0x0;
    android::String8::format("%s%s",&local_58,&DAT_001fbebf,"RFX_ASSERT");
    mtkLogE(local_58,"RFX_ASSERT:%s, %d",
            "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
            ,0x10c);
    android::String8::~String8((String8 *)&local_58);
  }
  mtkAssert(0);
                    /* WARNING: Subroutine does not return */
  exit(0);
}


