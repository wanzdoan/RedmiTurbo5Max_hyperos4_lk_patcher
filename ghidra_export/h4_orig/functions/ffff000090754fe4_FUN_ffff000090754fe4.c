// ffff000090754fe4  size=32  FUN_ffff000090754fe4

undefined8 FUN_ffff000090754fe4(long param_1,int param_2)

{
  if (param_1 != 0) {
    *(uint *)(param_1 + 0x10) = (uint)(param_2 == 0);
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_disp_dd_ffff0000907fc035,0xcb,
             s_dp_handle____NULL_ffff0000907e81a2);
}


