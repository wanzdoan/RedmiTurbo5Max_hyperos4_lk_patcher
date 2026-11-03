
/* RtcCapabilitySwitchController::onRilConnected(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcCapabilitySwitchController::onRilConnected
          (RtcCapabilitySwitchController *this,uint param_1,int *param_3,int *param_4)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  undefined8 uVar5;
  RfxStatusManager *this_00;
  ulong uVar6;
  undefined8 local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  uVar5 = RfxStatusManager::getKeyString(param_1);
  if ((*param_3 != 1) || (*param_4 != 1)) {
    uVar6 = __rfx_is_gt_mode();
    if ((uVar6 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x10c);
    }
    else {
      local_40 = 0;
      android::String8::format("%s%s",&local_40,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_40,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x10c);
      android::String8::~String8((String8 *)&local_40);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  RfxController::logI((char *)this,"RtcCapa","onRilConnected:%s(%d==>%d)",uVar5,
                      (ulong)*(byte *)(param_3 + 2),(ulong)*(byte *)(param_4 + 2));
  iVar2 = RfxRilUtils::rfxGetSimCount();
  if (iVar2 != 0) {
    uVar4 = 0;
    do {
      this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar4);
      uVar6 = RfxStatusManager::getBoolValue(this_00,0x74,false);
      if ((uVar6 & 1) == 0) goto LAB_003df5cc;
      uVar4 = uVar4 + 1;
      uVar3 = RfxRilUtils::rfxGetSimCount();
    } while (uVar4 < uVar3);
  }
  uVar6 = RtcCapabilitySwitchChecker::isReadyForSwitch
                    (*(RtcCapabilitySwitchChecker **)(this + 0x70),true);
  if (((uVar6 & 1) == 0) || (this[0x60] != (RtcCapabilitySwitchController)0x0)) {
    RfxController::logD((char *)this,"RtcCapa","onRilConnected, not ready for switch");
    if (*(long *)(lVar1 + 0x28) == local_38) {
      registerStatusKeys(this);
      return;
    }
  }
  else {
    uVar4 = (**(code **)(**(long **)(this + 0x70) + 0x98))();
    uVar6 = (ulong)uVar4;
    RfxController::logD((char *)this,"RtcCapa","checkAndSwitchCapability, high_priority_slot=%d",
                        uVar6);
    if ((int)uVar4 < 0) {
      uVar6 = (ulong)*(uint *)(this + 0x50);
    }
    else {
      *(uint *)(this + 0x50) = uVar4;
    }
    uVar6 = (**(code **)(**(long **)(this + 0x70) + 0x90))(*(long **)(this + 0x70),uVar6);
    if ((uVar6 & 1) == 0) {
      if (*(long *)(lVar1 + 0x28) == local_38) {
        handleRequestSwitchCapability(this);
        return;
      }
    }
    else {
LAB_003df5cc:
      if (*(long *)(lVar1 + 0x28) == local_38) {
        return;
      }
    }
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


