
void FUN_ffff0000b803ba3c(uint param_1,ulong param_2)

{
  int iVar1;
  long lVar2;
  long lVar3;
  long lVar4;
  
  lVar3 = 0;
  lVar4 = -0x100000000;
  lVar2 = 0x4a;
  do {
    if (*(int *)((long)&DAT_ffff0000b80eec4c + lVar3 + (param_2 & 0xffffffff) * 300) == -1) {
      lVar2 = lVar4 >> 0x20;
      if (3 < param_1) {
        return;
      }
      goto LAB_ffff0000b803ba9c;
    }
    lVar4 = lVar4 + 0x100000000;
    lVar3 = lVar3 + 4;
  } while (lVar3 != 300);
  if (param_1 < 4) {
LAB_ffff0000b803ba9c:
    iVar1 = (&DAT_ffff0000b80eec4c)[(param_2 & 0xffffffff) * 0x4b + lVar2];
    if ((2 < iVar1 - 0x2bU) && (iVar1 != 0xc)) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_disp_dd_ffff0000b808caf3,0x64b,&DAT_ffff0000b8086efb);
    }
  }
  return;
}


