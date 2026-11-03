// ffff00009075b554  size=196  FUN_ffff00009075b554

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff00009075b554(int param_1,uint param_2,int param_3)

{
  int iVar1;
  
  if (((param_1 != 10) && (param_1 != 0xf)) && (param_1 != 0x55)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_disp_dd_ffff0000907fa546,0x3c,&DAT_ffff0000907d2506);
  }
  if (param_3 < 1) {
    do {
    } while ((_DAT_ffff000032170004 & param_2) == 0);
    iVar1 = 1;
  }
  else {
    iVar1 = param_3 * -10 + 1;
    do {
      if ((_DAT_ffff000032170004 & param_2) != 0) {
        _DAT_ffff000032170004 = ~_DAT_ffff000032170004;
        return -iVar1;
      }
      FUN_ffff00009077cea0(100);
      iVar1 = iVar1 + 1;
    } while (iVar1 != 1);
    iVar1 = 0;
  }
  return iVar1;
}


