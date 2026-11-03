// ffff000090759b8c  size=208  FUN_ffff000090759b8c

void FUN_ffff000090759b8c(uint param_1,ulong param_2)

{
  int iVar1;
  long lVar2;
  long lVar3;
  long lVar4;
  
  lVar3 = 0;
  lVar4 = -0x100000000;
  lVar2 = 0x4a;
  do {
    if (*(int *)((long)&DAT_ffff00009081f3dc + lVar3 + (param_2 & 0xffffffff) * 300) == -1) {
      lVar2 = lVar4 >> 0x20;
      if (3 < param_1) {
        return;
      }
      goto LAB_ffff000090759bec;
    }
    lVar4 = lVar4 + 0x100000000;
    lVar3 = lVar3 + 4;
  } while (lVar3 != 300);
  if (param_1 < 4) {
LAB_ffff000090759bec:
    iVar1 = (&DAT_ffff00009081f3dc)[(param_2 & 0xffffffff) * 0x4b + lVar2];
    if ((2 < iVar1 - 0x2bU) && (iVar1 != 0xc)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_mt6991_disp_dd_ffff0000907dcd91,0x64b,&DAT_ffff0000907d24c9);
    }
  }
  return;
}


