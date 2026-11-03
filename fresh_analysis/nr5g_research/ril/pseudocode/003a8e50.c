
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* RtcRatSwitchController::setPreferredNetworkType(android::sp<RfxMessage> const&) */

void __thiscall
RtcRatSwitchController::setPreferredNetworkType(RtcRatSwitchController *this,sp *param_1)

{
  uint uVar1;
  long lVar2;
  int iVar3;
  uint uVar4;
  int iVar5;
  RfxStatusManager *this_00;
  char *extraout_x1;
  long *local_150;
  char acStack_148 [8];
  char acStack_140 [8];
  char acStack_138 [8];
  undefined8 local_130;
  char acStack_128 [8];
  char acStack_120 [8];
  char acStack_118 [8];
  char local_110 [8];
  undefined8 uStack_108;
  char acStack_100 [8];
  char acStack_f8 [8];
  char local_f0 [8];
  char acStack_e8 [8];
  undefined8 uStack_e0;
  char acStack_d8 [8];
  char local_d0 [8];
  char acStack_c8 [8];
  char acStack_c0 [8];
  undefined8 uStack_b8;
  char local_b0 [104];
  long local_48;
  
  lVar2 = tpidr_el0;
  local_48 = *(long *)(lVar2 + 0x28);
  this_00 = (RfxStatusManager *)
            RfxController::getStatusManager((RfxController *)this,*(int *)(this + 0x40));
  iVar3 = RfxStatusManager::getIntValue(this_00,1,0);
  if (iVar3 == 0) {
    RfxController::logD((char *)this,"RtcRatSwCtrl",
                        "setPreferredNetworkType: return directly because no sim.");
    local_150 = (long *)0x0;
    RfxMessage::obtainResponse((RfxMessage *)&local_150,0x26,param_1,0);
    RfxController::responseToRilj((RfxController *)this,(sp *)&local_150);
    if (local_150 != (long *)0x0) {
      android::RefBase::decStrong
                ((RefBase *)((long)local_150 + *(long *)(*local_150 + -0x18)),&local_150);
    }
  }
  else {
    iVar3 = *(int *)(this + 0x40);
    uVar1 = **(uint **)(*(long *)(*(long *)param_1 + 0x48) + 8);
    acStack_e8[0] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x18];
    acStack_e8[1] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x19];
    acStack_e8[2] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x1a];
    acStack_e8[3] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x1b];
    acStack_e8[4] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x1c];
    acStack_e8[5] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x1d];
    acStack_e8[6] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x1e];
    acStack_e8[7] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x1f];
    local_f0[0] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x10];
    local_f0[1] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x11];
    local_f0[2] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x12];
    local_f0[3] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x13];
    local_f0[4] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x14];
    local_f0[5] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x15];
    local_f0[6] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x16];
    local_f0[7] = s_persist_vendor_radio_rilj_nw_raf_00258808[0x17];
    acStack_d8[0] = s_persist_vendor_radio_rilj_nw_raf_00258830[0];
    acStack_d8[1] = s_persist_vendor_radio_rilj_nw_raf_00258830[1];
    acStack_d8[2] = s_persist_vendor_radio_rilj_nw_raf_00258830[2];
    acStack_d8[3] = s_persist_vendor_radio_rilj_nw_raf_00258830[3];
    acStack_d8[4] = s_persist_vendor_radio_rilj_nw_raf_00258830[4];
    acStack_d8[5] = s_persist_vendor_radio_rilj_nw_raf_00258830[5];
    acStack_d8[6] = s_persist_vendor_radio_rilj_nw_raf_00258830[6];
    acStack_d8[7] = s_persist_vendor_radio_rilj_nw_raf_00258830[7];
    uStack_e0 = ram0x00258828;
    acStack_c8[0] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x10];
    acStack_c8[1] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x11];
    acStack_c8[2] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x12];
    acStack_c8[3] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x13];
    acStack_c8[4] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x14];
    acStack_c8[5] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x15];
    acStack_c8[6] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x16];
    acStack_c8[7] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x17];
    local_d0[0] = s_persist_vendor_radio_rilj_nw_raf_00258830[8];
    local_d0[1] = s_persist_vendor_radio_rilj_nw_raf_00258830[9];
    local_d0[2] = s_persist_vendor_radio_rilj_nw_raf_00258830[10];
    local_d0[3] = s_persist_vendor_radio_rilj_nw_raf_00258830[0xb];
    local_d0[4] = s_persist_vendor_radio_rilj_nw_raf_00258830[0xc];
    local_d0[5] = s_persist_vendor_radio_rilj_nw_raf_00258830[0xd];
    local_d0[6] = s_persist_vendor_radio_rilj_nw_raf_00258830[0xe];
    local_d0[7] = s_persist_vendor_radio_rilj_nw_raf_00258830[0xf];
    uStack_b8 = ram0x00258850;
    acStack_c0[0] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x18];
    acStack_c0[1] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x19];
    acStack_c0[2] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x1a];
    acStack_c0[3] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x1b];
    acStack_c0[4] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x1c];
    acStack_c0[5] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x1d];
    acStack_c0[6] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x1e];
    acStack_c0[7] = s_persist_vendor_radio_rilj_nw_raf_00258830[0x1f];
    acStack_128[0] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0];
    acStack_128[1] = s_persist_vendor_radio_rilj_nw_raf_002587e0[1];
    acStack_128[2] = s_persist_vendor_radio_rilj_nw_raf_002587e0[2];
    acStack_128[3] = s_persist_vendor_radio_rilj_nw_raf_002587e0[3];
    acStack_128[4] = s_persist_vendor_radio_rilj_nw_raf_002587e0[4];
    acStack_128[5] = s_persist_vendor_radio_rilj_nw_raf_002587e0[5];
    acStack_128[6] = s_persist_vendor_radio_rilj_nw_raf_002587e0[6];
    acStack_128[7] = s_persist_vendor_radio_rilj_nw_raf_002587e0[7];
    local_130 = ram0x002587d8;
    acStack_118[0] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x10];
    acStack_118[1] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x11];
    acStack_118[2] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x12];
    acStack_118[3] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x13];
    acStack_118[4] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x14];
    acStack_118[5] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x15];
    acStack_118[6] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x16];
    acStack_118[7] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x17];
    acStack_120[0] = s_persist_vendor_radio_rilj_nw_raf_002587e0[8];
    acStack_120[1] = s_persist_vendor_radio_rilj_nw_raf_002587e0[9];
    acStack_120[2] = s_persist_vendor_radio_rilj_nw_raf_002587e0[10];
    acStack_120[3] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0xb];
    acStack_120[4] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0xc];
    acStack_120[5] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0xd];
    acStack_120[6] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0xe];
    acStack_120[7] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0xf];
    uStack_108 = ram0x00258800;
    local_110[0] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x18];
    local_110[1] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x19];
    local_110[2] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x1a];
    local_110[3] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x1b];
    local_110[4] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x1c];
    local_110[5] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x1d];
    local_110[6] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x1e];
    local_110[7] = s_persist_vendor_radio_rilj_nw_raf_002587e0[0x1f];
    acStack_f8[0] = s_persist_vendor_radio_rilj_nw_raf_00258808[8];
    acStack_f8[1] = s_persist_vendor_radio_rilj_nw_raf_00258808[9];
    acStack_f8[2] = s_persist_vendor_radio_rilj_nw_raf_00258808[10];
    acStack_f8[3] = s_persist_vendor_radio_rilj_nw_raf_00258808[0xb];
    acStack_f8[4] = s_persist_vendor_radio_rilj_nw_raf_00258808[0xc];
    acStack_f8[5] = s_persist_vendor_radio_rilj_nw_raf_00258808[0xd];
    acStack_f8[6] = s_persist_vendor_radio_rilj_nw_raf_00258808[0xe];
    acStack_f8[7] = s_persist_vendor_radio_rilj_nw_raf_00258808[0xf];
    acStack_100[0] = s_persist_vendor_radio_rilj_nw_raf_00258808[0];
    acStack_100[1] = s_persist_vendor_radio_rilj_nw_raf_00258808[1];
    acStack_100[2] = s_persist_vendor_radio_rilj_nw_raf_00258808[2];
    acStack_100[3] = s_persist_vendor_radio_rilj_nw_raf_00258808[3];
    acStack_100[4] = s_persist_vendor_radio_rilj_nw_raf_00258808[4];
    acStack_100[5] = s_persist_vendor_radio_rilj_nw_raf_00258808[5];
    acStack_100[6] = s_persist_vendor_radio_rilj_nw_raf_00258808[6];
    acStack_100[7] = s_persist_vendor_radio_rilj_nw_raf_00258808[7];
    acStack_148[0] = s_persist_vendor_radio_rilj_nw_raf_002587b8[8];
    acStack_148[1] = s_persist_vendor_radio_rilj_nw_raf_002587b8[9];
    acStack_148[2] = s_persist_vendor_radio_rilj_nw_raf_002587b8[10];
    acStack_148[3] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0xb];
    acStack_148[4] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0xc];
    acStack_148[5] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0xd];
    acStack_148[6] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0xe];
    acStack_148[7] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0xf];
    local_150 = (long *)s_persist_vendor_radio_rilj_nw_raf_002587b8._0_8_;
    acStack_138[0] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x18];
    acStack_138[1] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x19];
    acStack_138[2] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x1a];
    acStack_138[3] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x1b];
    acStack_138[4] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x1c];
    acStack_138[5] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x1d];
    acStack_138[6] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x1e];
    acStack_138[7] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x1f];
    acStack_140[0] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x10];
    acStack_140[1] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x11];
    acStack_140[2] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x12];
    acStack_140[3] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x13];
    acStack_140[4] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x14];
    acStack_140[5] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x15];
    acStack_140[6] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x16];
    acStack_140[7] = s_persist_vendor_radio_rilj_nw_raf_002587b8[0x17];
    uVar4 = getPreferredNetWorkTypeFromSysProp(this,iVar3);
    if (uVar4 != uVar1) {
      local_b0[0x54] = '\0';
      local_b0[0x55] = '\0';
      local_b0[0x56] = '\0';
      local_b0[0x57] = '\0';
      local_b0[0x58] = '\0';
      local_b0[0x59] = '\0';
      local_b0[0x5a] = '\0';
      local_b0[0x5b] = '\0';
      local_b0[0x50] = '\0';
      local_b0[0x51] = '\0';
      local_b0[0x52] = '\0';
      local_b0[0x53] = '\0';
      local_b0[0x18] = '\0';
      local_b0[0x19] = '\0';
      local_b0[0x1a] = '\0';
      local_b0[0x1b] = '\0';
      local_b0[0x1c] = '\0';
      local_b0[0x1d] = '\0';
      local_b0[0x1e] = '\0';
      local_b0[0x1f] = '\0';
      local_b0[0x10] = '\0';
      local_b0[0x11] = '\0';
      local_b0[0x12] = '\0';
      local_b0[0x13] = '\0';
      local_b0[0x14] = '\0';
      local_b0[0x15] = '\0';
      local_b0[0x16] = '\0';
      local_b0[0x17] = '\0';
      local_b0[0x28] = '\0';
      local_b0[0x29] = '\0';
      local_b0[0x2a] = '\0';
      local_b0[0x2b] = '\0';
      local_b0[0x2c] = '\0';
      local_b0[0x2d] = '\0';
      local_b0[0x2e] = '\0';
      local_b0[0x2f] = '\0';
      local_b0[0x20] = '\0';
      local_b0[0x21] = '\0';
      local_b0[0x22] = '\0';
      local_b0[0x23] = '\0';
      local_b0[0x24] = '\0';
      local_b0[0x25] = '\0';
      local_b0[0x26] = '\0';
      local_b0[0x27] = '\0';
      local_b0[0x38] = '\0';
      local_b0[0x39] = '\0';
      local_b0[0x3a] = '\0';
      local_b0[0x3b] = '\0';
      local_b0[0x3c] = '\0';
      local_b0[0x3d] = '\0';
      local_b0[0x3e] = '\0';
      local_b0[0x3f] = '\0';
      local_b0[0x30] = '\0';
      local_b0[0x31] = '\0';
      local_b0[0x32] = '\0';
      local_b0[0x33] = '\0';
      local_b0[0x34] = '\0';
      local_b0[0x35] = '\0';
      local_b0[0x36] = '\0';
      local_b0[0x37] = '\0';
      local_b0[0x48] = '\0';
      local_b0[0x49] = '\0';
      local_b0[0x4a] = '\0';
      local_b0[0x4b] = '\0';
      local_b0[0x4c] = '\0';
      local_b0[0x4d] = '\0';
      local_b0[0x4e] = '\0';
      local_b0[0x4f] = '\0';
      local_b0[0x40] = '\0';
      local_b0[0x41] = '\0';
      local_b0[0x42] = '\0';
      local_b0[0x43] = '\0';
      local_b0[0x44] = '\0';
      local_b0[0x45] = '\0';
      local_b0[0x46] = '\0';
      local_b0[0x47] = '\0';
      local_b0[8] = '\0';
      local_b0[9] = '\0';
      local_b0[10] = '\0';
      local_b0[0xb] = '\0';
      local_b0[0xc] = '\0';
      local_b0[0xd] = '\0';
      local_b0[0xe] = '\0';
      local_b0[0xf] = '\0';
      local_b0[0] = '\0';
      local_b0[1] = '\0';
      local_b0[2] = '\0';
      local_b0[3] = '\0';
      local_b0[4] = '\0';
      local_b0[5] = '\0';
      local_b0[6] = '\0';
      local_b0[7] = '\0';
      iVar5 = sprintf(local_b0,extraout_x1);
      if (iVar5 < 0) {
        RfxController::logE((char *)this,"RtcRatSwCtrl","setIntSysProp sprintf fail");
      }
      rfx_property_set(&local_150 + (long)iVar3 * 5,local_b0);
    }
    if (this[0x110] != (RtcRatSwitchController)0x0) {
      RfxController::logV((char *)this,"RtcRatSwCtrl",
                          "[setPrefNwType] from RILJ, rilj_nw_type set to %d, mInRestrictedMode:%d",
                          (ulong)uVar1,1);
      this[0x110] = (RtcRatSwitchController)0x0;
      *(undefined4 *)(this + 0xe0) = 0xffffffff;
    }
    local_150 = (long *)0x0;
    switchNwRat(this,uVar1,2,&local_150,param_1);
  }
  if (*(long *)(lVar2 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


