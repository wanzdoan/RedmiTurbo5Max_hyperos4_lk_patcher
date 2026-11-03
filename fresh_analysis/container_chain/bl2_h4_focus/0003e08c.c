// File offset entry: 0x3e08c

int FUN_ffff0000b803e08c(undefined8 *param_1)

{
  int iVar1;
  int *piVar2;
  
  if (((param_1 == (undefined8 *)0x0) || (piVar2 = (int *)*param_1, piVar2 == (int *)0x0)) ||
     (param_1[1] == 0)) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_di_ffff0000b808cb1c,0x455,&DAT_ffff0000b8086efb);
  }
  iVar1 = *piVar2;
  if ((iVar1 != 0) && (iVar1 != 1)) {
    if ((iVar1 != 2) || (3 < (uint)piVar2[0x5c])) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_mt6991_disp_di_ffff0000b808cb1c,0x475,&DAT_ffff0000b8086efb);
    }
    iVar1 = *(int *)(&DAT_ffff0000b80a2ae0 + (long)piVar2[0x5c] * 4);
  }
  return iVar1;
}


