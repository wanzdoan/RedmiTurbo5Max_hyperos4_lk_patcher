
int FUN_ffff0000be047450(undefined8 *param_1)

{
  int iVar1;
  int *piVar2;
  
  if (((param_1 == (undefined8 *)0x0) || (piVar2 = (int *)*param_1, piVar2 == (int *)0x0)) ||
     (param_1[1] == 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_di_ffff0000be0acccf,0x455,&DAT_ffff0000be0a553d);
  }
  iVar1 = *piVar2;
  if ((iVar1 != 0) && (iVar1 != 1)) {
    if ((iVar1 != 2) || (3 < (uint)piVar2[0x5c])) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_mt6991_disp_di_ffff0000be0acccf,0x475,&DAT_ffff0000be0a553d);
    }
    iVar1 = *(int *)(&DAT_ffff0000be0c8290 + (long)piVar2[0x5c] * 4);
  }
  return iVar1;
}


