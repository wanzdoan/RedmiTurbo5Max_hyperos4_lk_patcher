
undefined8 FUN_ffff0000b8037504(uint param_1)

{
  if ((DAT_ffff0000b81282d8 & 1) == 0) {
    FUN_ffff0000b804d070(&DAT_ffff0000b81282e0,0,0xc50);
    DAT_ffff0000b81282e4 = 0xf;
    DAT_ffff0000b81282d8 = 1;
  }
  if (*(long *)(&DAT_ffff0000b81286f8 + (ulong)param_1 * 8) != 0) {
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_disp_dd_ffff0000b809dc74,0x67a,
             s_dp_handle____NULL_ffff0000b8092b68);
}


