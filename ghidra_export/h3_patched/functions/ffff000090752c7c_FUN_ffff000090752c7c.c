// ffff000090752c7c  size=336  FUN_ffff000090752c7c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090752c7c(int param_1,undefined8 param_2)

{
  int iVar1;
  ulong uVar2;
  uint *puVar3;
  undefined4 local_14;
  
  iVar1 = 0x2d;
  if (_DAT_ffff000090878200 != 2) {
    iVar1 = param_1;
  }
  uVar2 = 0;
  if (iVar1 != 0x2d) {
    uVar2 = (ulong)(iVar1 != 0x2b);
  }
  if (*(int *)(&DAT_ffff0000908781d8 + uVar2 * 0xbd8) == 0) {
    local_14 = 0x2c3909;
    FUN_ffff000090750aa0(iVar1,param_2,&local_14,1,0);
    FUN_ffff00009077de54(s___s___set_2c3909_ffff0000907ff42f,s_ddp_dsi_trigger_ffff0000907d160f);
    if (iVar1 == 0x2d) {
      _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe;
      _DAT_ffff0000324a0000 = _DAT_ffff0000324a0000 & 0xfffffffe;
      _DAT_ffff0000324a0030 = _DAT_ffff0000324a0030 | 0x10;
      goto LAB_ffff000090752d48;
    }
  }
  else if (iVar1 == 0x2d) {
LAB_ffff000090752d48:
    _DAT_ffff000032490000 = _DAT_ffff000032490000 & 0xfffffffe | 1;
    if (*(int *)(&DAT_ffff0000908781d8 + uVar2 * 0xbd8) != 0) {
      return 0;
    }
    _DAT_ffff0000324a0030 = _DAT_ffff0000324a0030 & 0xffffffef;
    return 0;
  }
  puVar3 = *(uint **)(s_0123456789abcdef_ffff000090804a48 + (ulong)(iVar1 != 0x2b) * 8 + 0x10);
  *puVar3 = *puVar3 & 0xfffffffe;
  *puVar3 = *puVar3 | 1;
  return 0;
}


