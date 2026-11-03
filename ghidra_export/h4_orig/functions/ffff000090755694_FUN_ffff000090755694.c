// ffff000090755694  size=136  FUN_ffff000090755694

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090755694(uint param_1)

{
  if ((DAT_ffff00009087a998 & 1) == 0) {
    FUN_ffff00009077ee50(&DAT_ffff00009087a9a0,0,0xc50);
    _DAT_ffff00009087a9a4 = 0xf;
    DAT_ffff00009087a998 = 1;
  }
  if (*(long *)((ulong)param_1 * 8 + -0xffff6f785248) != 0) {
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0x67a,
             s_dp_handle____NULL_ffff0000907e81a2);
}


