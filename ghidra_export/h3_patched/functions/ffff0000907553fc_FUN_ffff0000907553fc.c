// ffff0000907553fc  size=136  FUN_ffff0000907553fc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff0000907553fc(uint param_1)

{
  if ((DAT_ffff00009087a900 & 1) == 0) {
    FUN_ffff00009077eb88(&DAT_ffff00009087a908,0,0xc50);
    _DAT_ffff00009087a90c = 0xf;
    DAT_ffff00009087a900 = 1;
  }
  if (*(long *)((ulong)param_1 * 8 + -0xffff6f7852e0) != 0) {
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fbe99,0x67a,
             s_dp_handle____NULL_ffff0000907e8047);
}


