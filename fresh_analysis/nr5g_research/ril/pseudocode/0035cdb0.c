
/* RtcModeSwitchController::onRatSwitchDone(int, int) */

void __thiscall
RtcModeSwitchController::onRatSwitchDone(RtcModeSwitchController *this,int param_1,int param_2)

{
  long lVar1;
  uint uVar2;
  int iVar3;
  ulong uVar4;
  RfxStatusManager *this_00;
  long lVar5;
  undefined8 local_40;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  uVar4 = __rfx_is_gt_mode();
  if ((uVar4 & 1) == 0) {
    mtkLogD("RtcModeCont",
            "[SMC][onRatSwitchDone] Slot : %d switch done, error : %d, switchQueue size : %zu",
            param_1,param_2,*(undefined8 *)(*(long *)(this + 0x58) + 0x48));
  }
  else {
    local_40 = 0;
    android::String8::format("%s%s",&local_40,&DAT_001fbebf,"RtcModeCont");
    mtkLogD(local_40,
            "[SMC][onRatSwitchDone] Slot : %d switch done, error : %d, switchQueue size : %zu",
            param_1,param_2,*(undefined8 *)(*(long *)(this + 0x58) + 0x48));
    android::String8::~String8((String8 *)&local_40);
  }
  if ((param_2 == 0x36) || (param_2 == 6)) {
    handleSendEratFailByCall(this,param_1);
  }
  else {
    lVar5 = *(long *)(this + 0x58);
    if (*(long *)(lVar5 + 0x48) == 0) {
      switchState(this,3);
    }
    else {
      if (((*(int *)(this + 0x4c) != *(int *)(this + 0x48)) && (param_2 != 0)) &&
         (*(int *)(this + 0x4c) == param_1)) {
        this_00 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,param_1)
        ;
        uVar2 = RfxStatusManager::getIntValue(this_00,0x54);
        if ((((uVar2 < 0xb) && ((1 << (ulong)(uVar2 & 0x1f) & 0x5f0U) != 0)) || (uVar2 - 0x67 < 3))
           && ((iVar3 = isInternalLoad(), iVar3 == 1 || (iVar3 = isUserLoad(), iVar3 != 1)))) {
          uVar4 = __rfx_is_gt_mode();
          if ((uVar4 & 1) == 0) {
            mtkLogE("RtcModeCont",
                    "[SMC][onRatSwitchDone] C change case, Switch old C capability slot %d fail, curNwType %d, assert!"
                    ,*(undefined4 *)(this + 0x48),uVar2);
          }
          else {
            local_40 = 0;
            android::String8::format("%s%s",&local_40,&DAT_001fbebf,"RtcModeCont");
            mtkLogE(local_40,
                    "[SMC][onRatSwitchDone] C change case, Switch old C capability slot %d fail, curNwType %d, assert!"
                    ,*(undefined4 *)(this + 0x48),uVar2);
            android::String8::~String8((String8 *)&local_40);
          }
          uVar4 = __rfx_is_gt_mode();
          if ((uVar4 & 1) == 0) {
            mtkLogE("RFX_ASSERT","RFX_ASSERT:%s, %d",
                    "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/telcore/modecontroller/RtcModeSwitchController.cpp"
                    ,0x1a1);
          }
          else {
            local_40 = 0;
            android::String8::format("%s%s",&local_40,&DAT_001fbebf,"RFX_ASSERT");
            mtkLogE(local_40,"RFX_ASSERT:%s, %d",
                    "vendor/mediatek/proprietary/hardware/ril/fusion/mtk-ril/telcore/modecontroller/RtcModeSwitchController.cpp"
                    ,0x1a1);
            android::String8::~String8((String8 *)&local_40);
          }
          mtkAssert(0);
                    /* WARNING: Subroutine does not return */
          exit(0);
        }
        uVar4 = __rfx_is_gt_mode();
        if ((uVar4 & 1) == 0) {
          mtkLogE("RtcModeCont","set ERAT fail in user load. reset radio");
        }
        else {
          local_40 = 0;
          android::String8::format("%s%s",&local_40,&DAT_001fbebf,"RtcModeCont");
          mtkLogE(local_40,"set ERAT fail in user load. reset radio");
          android::String8::~String8((String8 *)&local_40);
        }
        rfx_property_set("vendor.ril.mux.report.case",&DAT_0020219e);
        rfx_property_set("vendor.ril.muxreport",&DAT_002240c5);
        lVar5 = *(long *)(this + 0x58);
      }
      iVar3 = **(int **)(lVar5 + 0x40);
      uVar4 = __rfx_is_gt_mode();
      if ((uVar4 & 1) == 0) {
        mtkLogD("RtcModeCont","[SMC][onRatSwitchDone] Start switch Slot: %d",iVar3);
      }
      else {
        local_40 = 0;
        android::String8::format("%s%s",&local_40,&DAT_001fbebf,"RtcModeCont");
        mtkLogD(local_40,"[SMC][onRatSwitchDone] Start switch Slot: %d",iVar3);
        android::String8::~String8((String8 *)&local_40);
      }
      android::VectorImpl::removeItemsAt(*(long *)(this + 0x58) + 0x38,0);
      switchRadioTechnology(this,iVar3);
    }
  }
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


