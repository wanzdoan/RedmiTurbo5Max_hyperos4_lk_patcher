
/* RtcCatController::onRadioStateChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcCatController::onRadioStateChanged
          (RtcCatController *this,undefined8 param_2,int *param_3,int *param_4)

{
  long lVar1;
  RfxStatusManager *this_00;
  ulong uVar2;
  undefined8 local_58;
  undefined1 local_50;
  long local_28;
  
  lVar1 = tpidr_el0;
  local_28 = *(long *)(lVar1 + 0x28);
  if ((*param_3 != 2) || (*param_4 != 2)) {
    uVar2 = __rfx_is_gt_mode();
    if ((uVar2 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
    }
    else {
      local_58 = 0;
      android::String8::format("%s%s",&local_58,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_58,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
      android::String8::~String8((String8 *)&local_58);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  if (param_4[2] == 1) {
    if ((this[0xcc] != (RtcCatController)0x0) && (*(int *)(this + 0xc0) != 0)) {
      sendResponseForCardMissing(this,*(int *)(this + 0xc0),*(int *)(this + 200));
      *(undefined4 *)(this + 0xc0) = 0;
      *(undefined4 *)(this + 200) = 0;
      this[0xcc] = (RtcCatController)0x0;
    }
    this[0x4a] = (RtcCatController)0x0;
    freeAllStkQueuedEventNotify(this);
    if (*(void **)(this + 0xa0) != (void *)0x0) {
      free(*(void **)(this + 0xa0));
      *(undefined8 *)(this + 0xa0) = 0;
    }
    this_00 = (RfxStatusManager *)
              RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
    local_50 = 0;
    local_58 = CONCAT44(local_58._4_4_,1);
    RfxStatusManager::setValue(this_00,0x60,(RfxVariant *)&local_58,false,false);
    RfxVariant::deinit((RfxVariant *)&local_58);
  }
  if (*(long *)(lVar1 + 0x28) == local_28) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


