
/* RtcNetworkController::onHandleImsRegistrationRatChangedEvent(unsigned int, RfxVariant,
   RfxVariant) */

void __thiscall
RtcNetworkController::onHandleImsRegistrationRatChangedEvent
          (RtcNetworkController *this,undefined8 param_2,int *param_3,int *param_4)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  long *plVar4;
  int iVar5;
  RfxStatusManager *this_00;
  ulong uVar6;
  long *local_70;
  long *local_68;
  undefined8 local_60 [3];
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  if ((*param_3 != 2) || (*param_4 != 2)) {
    uVar6 = __rfx_is_gt_mode();
    if ((uVar6 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
    }
    else {
      local_60[0] = 0;
      android::String8::format("%s%s",local_60,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_60[0],"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
      android::String8::~String8((String8 *)local_60);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  uVar1 = param_3[2];
  uVar2 = param_4[2];
  this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  iVar5 = RfxStatusManager::getIntValue(this_00,0x47,-1);
  RfxController::logD((char *)this,"RtcNwCtrl",
                      "onHandleImsRegistrationRatChangedEvent: old_mode:%d, new_mode:%d",
                      (ulong)uVar1,(ulong)uVar2);
  if (((*(int *)(this + 0x40) == iVar5) &&
      (((uVar1 != 1 || (uVar2 != 1)) && (uVar2 == 1 || uVar1 == 1)))) &&
     (this[0x68] != (RtcNetworkController)0x0)) {
    local_68 = (long *)0x0;
    RfxVoidData::RfxVoidData((RfxVoidData *)local_60);
    RfxMessage::obtainUrc((RfxMessage *)&local_70,iVar5,0xc7a9,local_60,0);
    plVar4 = local_68;
    if (local_68 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
    }
    if (plVar4 != local_68) {
      android::sp_report_race();
    }
    local_68 = local_70;
    local_70 = (long *)0x0;
    RfxVoidData::~RfxVoidData((RfxVoidData *)local_60);
    RfxController::responseToRilj((RfxController *)this,(sp *)&local_68);
    if (local_68 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_68 + *(long *)(*local_68 + -0x18)),&local_68);
    }
  }
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


