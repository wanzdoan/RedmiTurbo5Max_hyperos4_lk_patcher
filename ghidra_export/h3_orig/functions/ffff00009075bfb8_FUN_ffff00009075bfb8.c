// ffff00009075bfb8  size=140  FUN_ffff00009075bfb8

int FUN_ffff00009075bfb8(undefined8 *param_1)

{
  int iVar1;
  int *piVar2;
  
  if (((param_1 == (undefined8 *)0x0) || (piVar2 = (int *)*param_1, piVar2 == (int *)0x0)) ||
     (param_1[1] == 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_disp_di_ffff0000907dcdd1,0x455,&DAT_ffff0000907d24c9);
  }
  iVar1 = *piVar2;
  if ((iVar1 != 0) && (iVar1 != 1)) {
    if ((iVar1 != 2) || (3 < (uint)piVar2[0x5c])) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_mt6991_disp_di_ffff0000907dcdd1,0x475,&DAT_ffff0000907d24c9);
    }
    iVar1 = *(int *)(&DAT_ffff000090804aa8 + (long)piVar2[0x5c] * 4);
  }
  return iVar1;
}


