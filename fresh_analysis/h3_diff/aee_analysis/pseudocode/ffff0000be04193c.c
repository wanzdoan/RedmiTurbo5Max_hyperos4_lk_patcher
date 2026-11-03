
undefined8 FUN_ffff0000be04193c(long param_1,uint param_2,undefined4 param_3)

{
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x5e4,
               s_dp_handle____NULL_ffff0000be0b463c);
  }
  if (param_2 < 10) {
    *(undefined4 *)(param_1 + (ulong)param_2 * 4 + 0x14) = param_3;
    return 0;
  }
  return 0xffffffff;
}


