
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000b8040258(code *param_1)

{
  undefined8 uVar1;
  
  if ((_DAT_ffff000010001600 >> 0x13 & 1) == 0) {
    FUN_ffff0000b804c33c
              (s__s__was_not_triggered__0x_08x__ffff0000b8087862,s__GPU_DFD__ffff0000b8091105);
    return;
  }
  if ((_DAT_ffff000010001600 >> 0x12 & 1) == 0) {
    FUN_ffff0000b804c33c
              (s__s__was_not_complete__0x_08x__ffff0000b809020d,s__GPU_DFD__ffff0000b8091105);
  }
  else {
    _DAT_ffff000048500a00 = 0xf100011;
    uVar1 = FUN_ffff0000b8049ec8(0xffff000049000000);
    (*param_1)(uVar1,0x200000);
    uVar1 = FUN_ffff0000b8049ec8(0xffff000049400000);
    (*param_1)(uVar1,0x200000);
  }
  _DAT_ffff00004b800500 = _DAT_ffff00004b800500 & 0xfffffffe;
  FUN_ffff0000b804c33c
            (s__s__after_release_RST_B_MFG_RPC__ffff0000b808e357,s__GPU_DFD__ffff0000b8091105,
             _DAT_ffff00004b800500);
  _DAT_ffff00004b800500 = _DAT_ffff00004b800500 | 1;
  FUN_ffff0000b804c33c
            (s__s__after_set_RST_B_MFG_RPC_MFG1_ffff0000b80886ae,s__GPU_DFD__ffff0000b8091105,
             _DAT_ffff00004b800500);
  FUN_ffff0000b804c33c(s__s__disable_MFG_DFD_CON_0_ffff0000b809110f,s__GPU_DFD__ffff0000b8091105);
  _DAT_ffff000048500a00 = 0;
  return;
}


