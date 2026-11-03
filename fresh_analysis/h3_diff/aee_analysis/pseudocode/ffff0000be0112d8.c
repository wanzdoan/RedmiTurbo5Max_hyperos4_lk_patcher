
void FUN_ffff0000be0112d8(byte param_1,char *param_2,undefined8 *param_3)

{
  int iVar1;
  byte *pbVar2;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  byte local_a0 [128];
  
  local_a0[0x70] = 0;
  local_a0[0x71] = 0;
  local_a0[0x72] = 0;
  local_a0[0x73] = 0;
  local_a0[0x74] = 0;
  local_a0[0x75] = 0;
  local_a0[0x76] = 0;
  local_a0[0x77] = 0;
  local_a0[0x78] = 0;
  local_a0[0x79] = 0;
  local_a0[0x7a] = 0;
  local_a0[0x7b] = 0;
  local_a0[0x7c] = 0;
  local_a0[0x7d] = 0;
  local_a0[0x7e] = 0;
  local_a0[0x7f] = 0;
  local_a0[0x60] = 0;
  local_a0[0x61] = 0;
  local_a0[0x62] = 0;
  local_a0[99] = 0;
  local_a0[100] = 0;
  local_a0[0x65] = 0;
  local_a0[0x66] = 0;
  local_a0[0x67] = 0;
  local_a0[0x68] = 0;
  local_a0[0x69] = 0;
  local_a0[0x6a] = 0;
  local_a0[0x6b] = 0;
  local_a0[0x6c] = 0;
  local_a0[0x6d] = 0;
  local_a0[0x6e] = 0;
  local_a0[0x6f] = 0;
  local_a0[0x50] = 0;
  local_a0[0x51] = 0;
  local_a0[0x52] = 0;
  local_a0[0x53] = 0;
  local_a0[0x54] = 0;
  local_a0[0x55] = 0;
  local_a0[0x56] = 0;
  local_a0[0x57] = 0;
  local_a0[0x58] = 0;
  local_a0[0x59] = 0;
  local_a0[0x5a] = 0;
  local_a0[0x5b] = 0;
  local_a0[0x5c] = 0;
  local_a0[0x5d] = 0;
  local_a0[0x5e] = 0;
  local_a0[0x5f] = 0;
  local_a0[0x40] = 0;
  local_a0[0x41] = 0;
  local_a0[0x42] = 0;
  local_a0[0x43] = 0;
  local_a0[0x44] = 0;
  local_a0[0x45] = 0;
  local_a0[0x46] = 0;
  local_a0[0x47] = 0;
  local_a0[0x48] = 0;
  local_a0[0x49] = 0;
  local_a0[0x4a] = 0;
  local_a0[0x4b] = 0;
  local_a0[0x4c] = 0;
  local_a0[0x4d] = 0;
  local_a0[0x4e] = 0;
  local_a0[0x4f] = 0;
  local_a0[0x30] = 0;
  local_a0[0x31] = 0;
  local_a0[0x32] = 0;
  local_a0[0x33] = 0;
  local_a0[0x34] = 0;
  local_a0[0x35] = 0;
  local_a0[0x36] = 0;
  local_a0[0x37] = 0;
  local_a0[0x38] = 0;
  local_a0[0x39] = 0;
  local_a0[0x3a] = 0;
  local_a0[0x3b] = 0;
  local_a0[0x3c] = 0;
  local_a0[0x3d] = 0;
  local_a0[0x3e] = 0;
  local_a0[0x3f] = 0;
  local_a0[0x20] = 0;
  local_a0[0x21] = 0;
  local_a0[0x22] = 0;
  local_a0[0x23] = 0;
  local_a0[0x24] = 0;
  local_a0[0x25] = 0;
  local_a0[0x26] = 0;
  local_a0[0x27] = 0;
  local_a0[0x28] = 0;
  local_a0[0x29] = 0;
  local_a0[0x2a] = 0;
  local_a0[0x2b] = 0;
  local_a0[0x2c] = 0;
  local_a0[0x2d] = 0;
  local_a0[0x2e] = 0;
  local_a0[0x2f] = 0;
  local_a0[0x10] = 0;
  local_a0[0x11] = 0;
  local_a0[0x12] = 0;
  local_a0[0x13] = 0;
  local_a0[0x14] = 0;
  local_a0[0x15] = 0;
  local_a0[0x16] = 0;
  local_a0[0x17] = 0;
  local_a0[0x18] = 0;
  local_a0[0x19] = 0;
  local_a0[0x1a] = 0;
  local_a0[0x1b] = 0;
  local_a0[0x1c] = 0;
  local_a0[0x1d] = 0;
  local_a0[0x1e] = 0;
  local_a0[0x1f] = 0;
  local_a0[0] = 0;
  local_a0[1] = 0;
  local_a0[2] = 0;
  local_a0[3] = 0;
  local_a0[4] = 0;
  local_a0[5] = 0;
  local_a0[6] = 0;
  local_a0[7] = 0;
  local_a0[8] = 0;
  local_a0[9] = 0;
  local_a0[10] = 0;
  local_a0[0xb] = 0;
  local_a0[0xc] = 0;
  local_a0[0xd] = 0;
  local_a0[0xe] = 0;
  local_a0[0xf] = 0;
  if (*param_2 == '\r') {
    pbVar2 = (byte *)((ulong)local_a0 | 1);
    param_2 = param_2 + 1;
    local_a0[0] = 0xd;
    local_a0[1] = 0;
    local_a0[2] = 0;
    local_a0[3] = 0;
    local_a0[4] = 0;
    local_a0[5] = 0;
    local_a0[6] = 0;
    local_a0[7] = 0;
  }
  else {
    pbVar2 = local_a0;
  }
  *pbVar2 = param_1;
  local_c0 = *param_3;
  uStack_b8 = param_3[1];
  pbVar2[1] = 0x3a;
  local_b0 = param_3[2];
  uStack_a8 = param_3[3];
  iVar1 = FUN_ffff0000be061230
                    (pbVar2 + 2,&stack0xffffffffffffffe0 + -(long)(pbVar2 + 2),param_2,&local_c0);
  if (0 < iVar1) {
    if ((param_1 - 0x45 < 0x13) && ((1 << (ulong)(param_1 - 0x45 & 0x1f) & 0x40011U) != 0)) {
      FUN_ffff0000be034f1c(&DAT_ffff0000be0ac42b,local_a0);
    }
    FUN_ffff0000be05fd98(s___s___s_ffff0000be0b3dba,s_MRDUMP11_ffff0000be0a7a51,local_a0);
    return;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98(s_app_aee_mrdump_log_c_ffff0000be0c0459,0x35,s_err_>_0_ffff0000be0bbb4e);
}


