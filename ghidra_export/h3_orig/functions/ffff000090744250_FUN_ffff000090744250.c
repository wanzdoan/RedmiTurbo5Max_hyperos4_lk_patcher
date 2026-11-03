// ffff000090744250  size=132  FUN_ffff000090744250

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090744250(void)

{
  ulong uVar1;
  uint uVar2;
  uint uVar3;
  int iVar4;
  
  uVar3 = _DAT_ffff00001c004e60;
  uVar1 = currentel;
  iVar4 = (int)(uVar1 >> 2);
  uVar2 = _DAT_1c004e60;
  if (iVar4 != 3) {
    if (iVar4 != 1) {
      return 0;
    }
    FUN_ffff00009077de54(s_VCP_pwr_sta__x__bit__d_ffff0000907fd661,_DAT_ffff00001c004e60,0x1e);
    uVar2 = uVar3;
  }
  if (uVar2 >> 0x1e == 0) {
    return 0;
  }
  return 1;
}


