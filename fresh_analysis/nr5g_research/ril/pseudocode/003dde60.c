
/* RtcCapabilityGetController::updateRadioCapability(int) */

void __thiscall
RtcCapabilityGetController::updateRadioCapability(RtcCapabilityGetController *this,int param_1)

{
  long lVar1;
  int iVar2;
  int iVar3;
  uint uVar4;
  RfxStatusManager *pRVar5;
  ulong uVar6;
  uint uVar7;
  char local_d0 [104];
  RtcCapabilityGetController *local_68;
  weakref_type *local_60;
  undefined *puStack_58;
  undefined8 local_50;
  long local_38;
  
  lVar1 = tpidr_el0;
  local_38 = *(long *)(lVar1 + 0x28);
  local_d0[0x54] = '\0';
  local_d0[0x55] = '\0';
  local_d0[0x56] = '\0';
  local_d0[0x57] = '\0';
  local_d0[0x58] = '\0';
  local_d0[0x59] = '\0';
  local_d0[0x5a] = '\0';
  local_d0[0x5b] = '\0';
  local_d0[0x50] = '\0';
  local_d0[0x51] = '\0';
  local_d0[0x52] = '\0';
  local_d0[0x53] = '\0';
  local_d0[0x18] = '\0';
  local_d0[0x19] = '\0';
  local_d0[0x1a] = '\0';
  local_d0[0x1b] = '\0';
  local_d0[0x1c] = '\0';
  local_d0[0x1d] = '\0';
  local_d0[0x1e] = '\0';
  local_d0[0x1f] = '\0';
  local_d0[0x10] = '\0';
  local_d0[0x11] = '\0';
  local_d0[0x12] = '\0';
  local_d0[0x13] = '\0';
  local_d0[0x14] = '\0';
  local_d0[0x15] = '\0';
  local_d0[0x16] = '\0';
  local_d0[0x17] = '\0';
  local_d0[0x28] = '\0';
  local_d0[0x29] = '\0';
  local_d0[0x2a] = '\0';
  local_d0[0x2b] = '\0';
  local_d0[0x2c] = '\0';
  local_d0[0x2d] = '\0';
  local_d0[0x2e] = '\0';
  local_d0[0x2f] = '\0';
  local_d0[0x20] = '\0';
  local_d0[0x21] = '\0';
  local_d0[0x22] = '\0';
  local_d0[0x23] = '\0';
  local_d0[0x24] = '\0';
  local_d0[0x25] = '\0';
  local_d0[0x26] = '\0';
  local_d0[0x27] = '\0';
  local_d0[0x38] = '\0';
  local_d0[0x39] = '\0';
  local_d0[0x3a] = '\0';
  local_d0[0x3b] = '\0';
  local_d0[0x3c] = '\0';
  local_d0[0x3d] = '\0';
  local_d0[0x3e] = '\0';
  local_d0[0x3f] = '\0';
  local_d0[0x30] = '\0';
  local_d0[0x31] = '\0';
  local_d0[0x32] = '\0';
  local_d0[0x33] = '\0';
  local_d0[0x34] = '\0';
  local_d0[0x35] = '\0';
  local_d0[0x36] = '\0';
  local_d0[0x37] = '\0';
  local_d0[0x48] = '\0';
  local_d0[0x49] = '\0';
  local_d0[0x4a] = '\0';
  local_d0[0x4b] = '\0';
  local_d0[0x4c] = '\0';
  local_d0[0x4d] = '\0';
  local_d0[0x4e] = '\0';
  local_d0[0x4f] = '\0';
  local_d0[0x40] = '\0';
  local_d0[0x41] = '\0';
  local_d0[0x42] = '\0';
  local_d0[0x43] = '\0';
  local_d0[0x44] = '\0';
  local_d0[0x45] = '\0';
  local_d0[0x46] = '\0';
  local_d0[0x47] = '\0';
  local_d0[8] = '\0';
  local_d0[9] = '\0';
  local_d0[10] = '\0';
  local_d0[0xb] = '\0';
  local_d0[0xc] = '\0';
  local_d0[0xd] = '\0';
  local_d0[0xe] = '\0';
  local_d0[0xf] = '\0';
  local_d0[0] = '\0';
  local_d0[1] = '\0';
  local_d0[2] = '\0';
  local_d0[3] = '\0';
  local_d0[4] = '\0';
  local_d0[5] = '\0';
  local_d0[6] = '\0';
  local_d0[7] = '\0';
  rfx_property_get("vendor.ril.rc.session.id1",local_d0,&DAT_001f9509);
  iVar2 = atoi(local_d0);
  if (iVar2 == -1) {
    pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
    iVar3 = RfxStatusManager::getIntValue(pRVar5,0x5d);
    if (iVar3 != 2) {
      if ((*(int *)(this + 0x48) == param_1) && (this[0x4c] == (RtcCapabilityGetController)0x0)) {
        RfxController::logI((char *)this,"RtcCapa",
                            "updateRadioCapability cslot=%d is same with before.",
                            (ulong)(uint)param_1);
      }
      else {
        updateRadioCapability(this,param_1,-1);
      }
      goto LAB_003ddfbc;
    }
  }
  updateRadioCapability(this,param_1,iVar2);
  rfx_property_set("vendor.ril.rc.session.id1",&DAT_001f9509);
  uVar6 = RtcCapabilitySwitchUtil::isDssNoResetSupport();
  if (((uVar6 & 1) == 0) && (iVar2 = RfxRilUtils::rfxGetSimCount(), iVar2 != 0)) {
    uVar7 = 0;
    do {
      pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,uVar7);
      local_68 = (RtcCapabilityGetController *)CONCAT44(local_68._4_4_,2);
      local_60 = (weakref_type *)((ulong)local_60 & 0xffffffff00000000);
      RfxStatusManager::setValue(pRVar5,0x23,(RfxVariant *)&local_68,false,false);
      RfxVariant::deinit((RfxVariant *)&local_68);
      uVar7 = uVar7 + 1;
      uVar4 = RfxRilUtils::rfxGetSimCount();
    } while (uVar7 < uVar4);
  }
  pRVar5 = (RfxStatusManager *)RfxController::getStatusManager((RfxController *)this,-1);
  local_68 = this;
  local_60 = (weakref_type *)android::RefBase::createWeak(this + *(long *)(*(long *)this + -0x18));
  local_50 = 0;
  puStack_58 = PTR_onModeSwitchFinished_005d3078;
  RfxStatusManager::registerStatusChanged(pRVar5,0x5b,(RfxCallback3 *)&local_68);
  if (local_68 != (RtcCapabilityGetController *)0x0) {
    android::RefBase::weakref_type::decWeak(local_60,&local_68);
  }
LAB_003ddfbc:
  if (*(long *)(lVar1 + 0x28) == local_38) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


