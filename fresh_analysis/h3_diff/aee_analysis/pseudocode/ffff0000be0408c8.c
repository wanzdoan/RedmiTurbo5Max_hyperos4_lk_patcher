
undefined8 FUN_ffff0000be0408c8(uint param_1)

{
  if ((DAT_ffff0000be124ad8 & 1) == 0) {
    FUN_ffff0000be060ae0(&DAT_ffff0000be124ae0,0,0xc50);
    DAT_ffff0000be124ae4 = 0xf;
    DAT_ffff0000be124ad8 = 1;
  }
  if (*(long *)(&DAT_ffff0000be124ef8 + (ulong)param_1 * 8) != 0) {
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_disp_dd_ffff0000be0c2130,0x67a,
             s_dp_handle____NULL_ffff0000be0b463c);
}


