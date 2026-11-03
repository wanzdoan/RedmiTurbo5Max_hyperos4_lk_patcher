// ffff000090799e88  size=4  FUN_ffff000090799e88

undefined8 FUN_ffff000090799e88(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    *(undefined4 *)(param_1 + 5) = 0;
    *param_1 = 0;
    param_1[1] = 0xbb67ae856a09e667;
    param_1[2] = 0xa54ff53a3c6ef372;
    param_1[3] = 0x9b05688c510e527f;
    param_1[4] = 0x5be0cd191f83d9ab;
    return 0;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000907991f0
            (s_md____NULL_ffff0000907ef0cd,s_platform_mediatek_mt6991_sboot_c_ffff0000907ed6e0,0xcf)
  ;
}


