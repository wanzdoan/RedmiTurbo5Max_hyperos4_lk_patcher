
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000b8034d84(int param_1,undefined8 param_2)

{
  int iVar1;
  ulong uVar2;
  uint *puVar3;
  undefined4 local_14;
  
  iVar1 = 0x2d;
  if (DAT_ffff0000b8125bd8 != 2) {
    iVar1 = param_1;
  }
  uVar2 = 0;
  if (iVar1 != 0x2d) {
    uVar2 = (ulong)(iVar1 != 0x2b);
  }
  if ((&DAT_ffff0000b8125bb0)[uVar2 * 0x2f6] == 0) {
    local_14 = 0x2c3909;
    FUN_ffff0000b8032ba8(iVar1,param_2,&local_14,1,0);
    FUN_ffff0000b804c33c(s___s___set_2c3909_ffff0000b809f96f,s_ddp_dsi_trigger_ffff0000b808661b);
    if (iVar1 == 0x2d) {
      _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe;
      _DAT_ffff0000324a0000 = _DAT_ffff0000324a0000 & 0xfffffffe;
      _DAT_ffff0000324a0030 = _DAT_ffff0000324a0030 | 0x10;
      goto LAB_ffff0000b8034e50;
    }
  }
  else if (iVar1 == 0x2d) {
LAB_ffff0000b8034e50:
    _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe | 1;
    if ((&DAT_ffff0000b8125bb0)[uVar2 * 0x2f6] != 0) {
      return 0;
    }
    _DAT_ffff0000324a0030 = _DAT_ffff0000324a0030 & 0xffffffef;
    return 0;
  }
  puVar3 = *(uint **)(s_0123456789abcdef_ffff0000b80a2a80 + (ulong)(iVar1 != 0x2b) * 8 + 0x10);
  *puVar3 = *puVar3 & 0xfffffffe;
  *puVar3 = *puVar3 | 1;
  return 0;
}


