
undefined8 FUN_ffff0000b8038518(long param_1,uint param_2)

{
  int *piVar1;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x5ce,
               s_dp_handle____NULL_ffff0000b8092b68);
  }
  piVar1 = (int *)(param_1 + (ulong)param_2 * 0x10 + 0x40);
  if (*piVar1 != 0) {
    return 0;
  }
  param_1 = param_1 + (ulong)param_2 * 0x10;
  *piVar1 = 1;
  *(undefined8 *)(param_1 + 0x48) = 0;
  *(uint *)(param_1 + 0x44) = param_2;
  return 0;
}


