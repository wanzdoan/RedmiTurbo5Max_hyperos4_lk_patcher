
/* RtcNetworkController::onHandleNrSwitchSlotChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcNetworkController::onHandleNrSwitchSlotChanged
          (RtcNetworkController *this,undefined8 param_2,int *param_3,int *param_4)

{
  uint uVar1;
  uint uVar2;
  undefined4 uVar3;
  long lVar4;
  long *plVar5;
  ulong uVar6;
  bool bVar7;
  long *local_60;
  long *local_58;
  undefined8 local_50 [3];
  long local_38;
  
  lVar4 = tpidr_el0;
  local_38 = *(long *)(lVar4 + 0x28);
  if ((*param_3 != 2) || (*param_4 != 2)) {
    uVar6 = __rfx_is_gt_mode();
    if ((uVar6 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
    }
    else {
      local_50[0] = 0;
      android::String8::format("%s%s",local_50,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_50[0],"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
      android::String8::~String8((String8 *)local_50);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  uVar1 = param_3[2];
  uVar2 = param_4[2];
  RfxController::logE((char *)this,"RtcNwCtrl",
                      "onHandleNrSwitchSlotChanged :old_slot:%d, new_slot:%d",(ulong)uVar1,
                      (ulong)uVar2);
  if ((uVar1 == 0) || (uVar1 == *(int *)(this + 0x40) + 1U)) {
    bVar7 = true;
    if (uVar2 != 0) goto LAB_003ae8f8;
LAB_003ae908:
    if (bVar7) goto LAB_003ae9c8;
  }
  else {
    bVar7 = false;
    if (uVar2 == 0) goto LAB_003ae908;
LAB_003ae8f8:
    if (uVar2 == *(int *)(this + 0x40) + 1U) goto LAB_003ae908;
    if (!bVar7) goto LAB_003ae9c8;
  }
  RfxController::logV((char *)this,"RtcNwCtrl",
                      "onHandleNrSwitchSlotChanged send VOICE_NETWORK_STATE_CHANGED");
  uVar3 = *(undefined4 *)(this + 0x40);
  local_58 = (long *)0x0;
  RfxVoidData::RfxVoidData((RfxVoidData *)local_50);
  RfxMessage::obtainUrc((RfxMessage *)&local_60,uVar3,0xc7a9,local_50,0);
  plVar5 = local_58;
  if (local_58 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
  }
  if (plVar5 != local_58) {
    android::sp_report_race();
  }
  local_58 = local_60;
  local_60 = (long *)0x0;
  RfxVoidData::~RfxVoidData((RfxVoidData *)local_50);
  RfxController::responseToRilj((RfxController *)this,(sp *)&local_58);
  if (local_58 != (long *)0x0) {
    android::RefBase::decStrong
              ((RefBase *)((long)local_58 + *(long *)(*local_58 + -0x18)),&local_58);
  }
LAB_003ae9c8:
  if (*(long *)(lVar4 + 0x28) != local_38) {
                    /* WARNING: Subroutine does not return */
    __stack_chk_fail();
  }
  return;
}


