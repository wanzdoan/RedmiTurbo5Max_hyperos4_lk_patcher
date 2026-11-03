
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000be045df4(int param_1)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  
  uVar2 = _DAT_ffff000032170010;
  if (((param_1 != 10) && (param_1 != 0xf)) && (param_1 != 0x55)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c0fc1,0x3c,&DAT_ffff0000be0a553d);
  }
  uVar3 = 0;
  do {
    if ((_DAT_ffff000032170010 & 0x700) != 0x100) break;
    uVar1 = uVar3 >> 4;
    uVar3 = uVar3 + 1;
  } while (uVar1 < 0x271);
  _DAT_ffff000032170010 = _DAT_ffff000032170010 & 0xffffffef;
  uVar3 = 0;
  do {
    if ((uVar2 & 0x700) == 0x100) {
      return 0;
    }
    uVar1 = uVar3 >> 4;
    uVar3 = uVar3 + 1;
  } while (uVar1 < 0x271);
  return 0;
}


