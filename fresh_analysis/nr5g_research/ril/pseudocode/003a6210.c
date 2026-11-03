
/* RtcNrSwitchController::onNrSwitchRetry(unsigned int, RfxVariant, RfxVariant) */

void __thiscall
RtcNrSwitchController::onNrSwitchRetry
          (RtcNrSwitchController *this,ulong param_2,int *param_3,int *param_4)

{
  uint uVar1;
  uint uVar2;
  byte bVar3;
  long lVar4;
  int iVar5;
  uint uVar6;
  RfxStatusManager *pRVar7;
  long lVar8;
  ulong uVar9;
  long *local_d0;
  uint local_c4;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 uStack_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 uStack_80;
  undefined4 uStack_78;
  undefined4 local_74;
  undefined4 uStack_70;
  undefined8 uStack_6c;
  long local_58;
  
  lVar4 = tpidr_el0;
  local_58 = *(long *)(lVar4 + 0x28);
  if ((*param_3 != 1) || (*param_4 != 1)) {
    uVar9 = __rfx_is_gt_mode();
    if ((uVar9 & 1) == 0) {
      mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x10c);
    }
    else {
      local_c0 = 0;
      android::String8::format("%s%s",&local_c0,&DAT_001fbebf,"RFX_ASSERT");
      mtkLogE(local_c0,"RFX_ASSERT:%s, %d",
              "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/framework/include/base/RfxVariant.h"
              ,0x10c);
      android::String8::~String8((String8 *)&local_c0);
    }
    mtkAssert(0);
                    /* WARNING: Subroutine does not return */
    exit(0);
  }
  bVar3 = *(byte *)(param_4 + 2);
  RfxController::logV((char *)this,"RtcNrSwCtrl","%s, key:%d oldMode:%d, mode:%d, mTriggerRetry:%d",
                      "onNrSwitchRetry",param_2 & 0xffffffff,(ulong)*(byte *)(param_3 + 2),
                      (ulong)bVar3,(ulong)(byte)this[0x58]);
  if ((bVar3 == 0) && (this[0x58] != (RtcNrSwitchController)0x0)) {
    pRVar7 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
    iVar5 = RfxStatusManager::getIntValue(pRVar7,0x34,0);
    pRVar7 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
    uVar6 = RfxStatusManager::getIntValue(pRVar7,0x47,0);
    uVar1 = (uint)(uVar6 == 0);
    if (iVar5 != 1) {
      uVar1 = uVar6;
    }
    uVar2 = *(uint *)(this + 0x54);
    uStack_6c = 0;
    uStack_70 = 0;
    uStack_a8 = 0;
    local_b0 = 0;
    uStack_98 = 0;
    uStack_a0 = 0;
    uStack_88 = 0;
    local_90 = 0;
    uStack_78 = 0;
    local_74 = 0;
    uStack_80 = 0;
    uStack_b8 = 0;
    local_c0 = 0;
    local_c4 = uVar1;
    rfx_property_get("persist.vendor.radio.nrslot",&local_c0,&DAT_002240c5);
    lVar8 = strtol((char *)&local_c0,(char **)0x0,10);
    *(int *)(this + 0x50) = (int)lVar8 + -1;
    RfxController::logV((char *)this,"RtcNrSwCtrl","getNrSlot:: mNrSlot:%d");
    RfxController::logV((char *)this,"RtcNrSwCtrl",
                        "switchNrSlot:defaultSim:%d, PreferredSlot:%d, OriginalSim:%d, NrSlot:%d",
                        (ulong)uVar1,(ulong)uVar2,(ulong)uVar6,(ulong)*(uint *)(this + 0x50));
    pRVar7 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
    local_c0 = CONCAT44(local_c0._4_4_,1);
    uStack_b8 = CONCAT71(uStack_b8._1_7_,1);
    RfxStatusManager::setValue(pRVar7,0x56,(RfxVariant *)&local_c0,false,false);
    RfxVariant::deinit((RfxVariant *)&local_c0);
    this[0x58] = (RtcNrSwitchController)0x0;
    local_d0 = (long *)0x0;
    RfxIntsData::RfxIntsData((RfxIntsData *)&local_c0,(int *)&local_c4,1);
    RfxMessage::obtainRequest((RfxMessage *)&local_d0,iVar5,0xc76c,&local_c0,0);
    RfxIntsData::~RfxIntsData((RfxIntsData *)&local_c0);
    RfxController::requestToMcl((RfxController *)this,(sp *)&local_d0,false);
    *(uint *)(this + 0x54) = local_c4;
    if (local_d0 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_d0 + *(long *)(*local_d0 + -0x18)),&local_d0);
    }
  }
  if (*(long *)(lVar4 + 0x28) == local_58) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


