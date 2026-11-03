
/* RtcCommSimController::onRadioStateChanged(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcCommSimController::onRadioStateChanged
          (RtcCommSimController *this,undefined8 param_2,int *param_3,int *param_4)

{
  uint uVar1;
  uint uVar2;
  long lVar3;
  ulong uVar4;
  RfxStatusManager *pRVar5;
  char *pcVar6;
  undefined8 local_78;
  uint local_70;
  long local_48;
  
  lVar3 = tpidr_el0;
  local_48 = *(long *)(lVar3 + 0x28);
  if ((*param_3 != 2) || (*param_4 != 2)) {
    uVar4 = __rfx_is_gt_mode();
    if ((uVar4 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
    }
    else {
      local_78 = 0;
      android::String8::format("%s%s",&local_78,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_78,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x113);
      android::String8::~String8((String8 *)&local_78);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  uVar1 = param_3[2];
  uVar2 = param_4[2];
  pcVar6 = *(char **)(this + 0x48);
  uVar4 = RfxController::getSlotId((RfxController *)this);
  RfxController::logD((char *)this,pcVar6,"onRadioStateChanged (%d, %d) (slot %d)",(ulong)uVar1,
                      (ulong)uVar2,uVar4 & 0xffffffff);
  if (uVar2 == 1) {
    pRVar5 = (RfxStatusManager *)
             RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
    local_70 = local_70 & 0xffffff00;
    local_78._0_4_ = 1;
    RfxStatusManager::setValue(pRVar5,0x15,(RfxVariant *)&local_78,true,false);
    RfxVariant::deinit((RfxVariant *)&local_78);
    pRVar5 = (RfxStatusManager *)
             RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
    local_78 = CONCAT44(local_78._4_4_,2);
    local_70 = 0xffffffff;
    RfxStatusManager::setValue(pRVar5,0x5c,(RfxVariant *)&local_78,false,false);
    RfxVariant::deinit((RfxVariant *)&local_78);
  }
  if (*(long *)(lVar3 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


