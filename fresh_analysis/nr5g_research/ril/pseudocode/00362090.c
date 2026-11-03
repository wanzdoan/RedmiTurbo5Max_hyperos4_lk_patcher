
/* RtcRadioController::onRadioStateChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcRadioController::onRadioStateChanged
          (RtcRadioController *this,undefined8 param_2,int *param_3,int *param_4)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  RfxStatusManager *this_00;
  ulong uVar4;
  undefined8 local_40;
  long local_38;
  
  lVar3 = tpidr_el0;
  local_38 = *(long *)(lVar3 + 0x28);
  if ((*param_4 == 2) && (*param_3 == 2)) {
    uVar1 = param_4[2];
    uVar2 = param_3[2];
    this_00 = (RfxStatusManager *)
              RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
    uVar4 = RfxStatusManager::getBoolValue(this_00,0x24,false);
    if (((uVar2 == 1) || (uVar1 != 10)) || ((uVar4 & 1) != 0)) {
      if (uVar1 == uVar2) {
        if (*(long *)(lVar3 + 0x28) == local_38) {
          RfxController::logI((char *)this,"RtcRadioCont",
                              "radio state not change (%d), do not update to RILJ",(ulong)uVar1);
          return;
        }
      }
      else {
        RfxController::logI((char *)this,"RtcRadioCont",
                            "radio State: %d -> %d, using google native API for urc",(ulong)uVar2,
                            (ulong)uVar1);
        if (*(long *)(lVar3 + 0x28) == local_38) {
          setRadioState(uVar1,*(undefined4 *)(this + 0x40));
          return;
        }
      }
    }
    else if (*(long *)(lVar3 + 0x28) == local_38) {
      RfxController::logI((char *)this,"RtcRadioCont",
                          "radio State: %d -> %d, STATUS_KEY_REQUEST_RADIO_POWER = false not update to RILJ"
                          ,(ulong)uVar2,10);
      return;
    }
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  uVar4 = __rfx_is_gt_mode();
  if ((uVar4 & 1) == 0) {
    mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
            "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
            ,0x113);
  }
  else {
    local_40 = 0;
    android::String8::format("%s%s",&local_40,&DAT_001fbebf,"RFX_ASSERT");
    mtkLogE(local_40,"RFX_ASSERT:%s, %d",
            "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
            ,0x113);
    android::String8::~String8((String8 *)&local_40);
  }
  mtkAssert(0);
                    /* WARNING: Subroutine does not return */
  exit(0);
}


