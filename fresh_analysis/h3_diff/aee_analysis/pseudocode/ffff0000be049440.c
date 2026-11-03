
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000be049440(code *param_1)

{
  undefined8 uVar1;
  
  if ((_DAT_ffff000010001600 >> 0x13 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s__s__was_not_triggered__0x_08x__ffff0000be0a61d1,s__GPU_DFD__ffff0000be0b23c1);
    return;
  }
  if ((_DAT_ffff000010001600 >> 0x12 & 1) == 0) {
    FUN_ffff0000be05fd98
              (s__s__was_not_complete__0x_08x__ffff0000be0b1231,s__GPU_DFD__ffff0000be0b23c1);
  }
  else {
    _DAT_ffff000048500a00 = 0xf100011;
    uVar1 = FUN_ffff0000be05d228(0xffff000049000000);
    (*param_1)(uVar1,0x200000);
    uVar1 = FUN_ffff0000be05d228(0xffff000049400000);
    (*param_1)(uVar1,0x200000);
  }
  _DAT_ffff00004b800500 = _DAT_ffff00004b800500 & 0xfffffffe;
  FUN_ffff0000be05fd98
            (s__s__after_release_RST_B_MFG_RPC__ffff0000be0aec3f,s__GPU_DFD__ffff0000be0b23c1,
             _DAT_ffff00004b800500);
  _DAT_ffff00004b800500 = _DAT_ffff00004b800500 | 1;
  FUN_ffff0000be05fd98
            (s__s__after_set_RST_B_MFG_RPC_MFG1_ffff0000be0a737e,s__GPU_DFD__ffff0000be0b23c1,
             _DAT_ffff00004b800500);
  FUN_ffff0000be05fd98(s__s__disable_MFG_DFD_CON_0_ffff0000be0b23cb,s__GPU_DFD__ffff0000be0b23c1);
  _DAT_ffff000048500a00 = 0;
  return;
}


