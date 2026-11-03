// ffff00009075e34c  size=300  FUN_ffff00009075e34c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009075e34c(code *param_1)

{
  undefined8 uVar1;
  
  if ((_DAT_ffff000010001600 >> 0x13 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__was_not_triggered__0x_08x__ffff0000907d34f0,s__GPU_DFD__ffff0000907e5023);
    return;
  }
  if ((_DAT_ffff000010001600 >> 0x12 & 1) == 0) {
    FUN_ffff00009077de54
              (s__s__was_not_complete__0x_08x__ffff0000907e3521,s__GPU_DFD__ffff0000907e5023);
  }
  else {
    _DAT_ffff000048500a00 = 0xf100011;
    uVar1 = FUN_ffff00009077b06c(0xffff000049000000);
    (*param_1)(uVar1,0x200000);
    uVar1 = FUN_ffff00009077b06c(0xffff000049400000);
    (*param_1)(uVar1,0x200000);
  }
  _DAT_ffff00004b800500 = _DAT_ffff00004b800500 & 0xfffffffe;
  FUN_ffff00009077de54
            (s__s__after_release_RST_B_MFG_RPC__ffff0000907dff3a,s__GPU_DFD__ffff0000907e5023,
             _DAT_ffff00004b800500);
  _DAT_ffff00004b800500 = _DAT_ffff00004b800500 | 1;
  FUN_ffff00009077de54
            (s__s__after_set_RST_B_MFG_RPC_MFG1_ffff0000907d4ce5,s__GPU_DFD__ffff0000907e5023,
             _DAT_ffff00004b800500);
  FUN_ffff00009077de54(s__s__disable_MFG_DFD_CON_0_ffff0000907e508a,s__GPU_DFD__ffff0000907e5023);
  _DAT_ffff000048500a00 = 0;
  return;
}


