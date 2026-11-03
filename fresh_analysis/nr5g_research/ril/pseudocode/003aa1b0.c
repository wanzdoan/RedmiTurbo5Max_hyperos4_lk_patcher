
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */
/* RtcRatSwitchController::setPreferredNetWorkTypeToSysProp(int, int) */

void __thiscall
RtcRatSwitchController::setPreferredNetWorkTypeToSysProp
          (RtcRatSwitchController *this,int param_1,int param_2)

{
  long lVar1;
  int iVar2;
  char *extraout_x1;
  char local_150 [8];
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
  
  lVar1 = tpidr_el0;
  local_48 = *(long *)(lVar1 + 0x28);
  acStack_e8 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_00258808._24_8_;
  local_f0 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_00258808._16_8_;
  acStack_d8 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_00258830._0_8_;
  uStack_e0 = ram0x00258828;
  acStack_c8 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_00258830._16_8_;
  local_d0 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_00258830._8_8_;
  uStack_b8 = ram0x00258850;
  acStack_c0 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_00258830._24_8_;
  acStack_128 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_002587e0._0_8_;
  local_130 = ram0x002587d8;
  acStack_118 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_002587e0._16_8_;
  acStack_120 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_002587e0._8_8_;
  uStack_108 = ram0x00258800;
  local_110 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_002587e0._24_8_;
  acStack_f8 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_00258808._8_8_;
  acStack_100 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_00258808._0_8_;
  acStack_148 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_002587b8._8_8_;
  local_150 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_002587b8._0_8_;
  acStack_138 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_002587b8._24_8_;
  acStack_140 = (char  [8])s_persist_vendor_radio_rilj_nw_raf_002587b8._16_8_;
  iVar2 = getPreferredNetWorkTypeFromSysProp(this,param_1);
  if (iVar2 != param_2) {
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
    iVar2 = sprintf(local_b0,extraout_x1);
    if (iVar2 < 0) {
      RfxController::logE((char *)this,"RtcRatSwCtrl","setIntSysProp sprintf fail");
    }
    rfx_property_set(local_150 + (long)param_1 * 0x28,local_b0);
  }
  if (*(long *)(lVar1 + 0x28) == local_48) {
    return;
  }
                    /* WARNING: Subroutine does not return */
  __stack_chk_fail();
}


