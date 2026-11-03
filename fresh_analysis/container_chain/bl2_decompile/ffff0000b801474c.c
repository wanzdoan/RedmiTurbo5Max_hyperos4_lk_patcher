
undefined8 FUN_ffff0000b801474c(undefined8 param_1,undefined8 param_2,int param_3)

{
  uint uVar1;
  int iVar2;
  
  if ((DAT_ffff0000b811af8c & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_dconfig_ffff0000b809f483,0x134,
               s_g_env_init_done____DCONFIG_VALID_ffff0000b809c302);
  }
  if ((DAT_ffff0000b811af90 == '\x01') && (DAT_ffff0000b811afc0 != 0)) {
    if (DAT_ffff0000b811afc8 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000b804b830
                (s_platform_mediatek_common_dconfig_ffff0000b809f483,0x13a,
                 s_g_dconfig_dtb_size____0_ffff0000b809e330);
    }
    iVar2 = FUN_ffff0000b8050500();
    if (iVar2 == 0) {
      uVar1 = (uRam0000000000000004 & 0xff00ff00) >> 8 | (uRam0000000000000004 & 0xff00ff) << 8;
      FUN_ffff0000b804c33c
                (s_dconfig_overlay_ok__merged_fdt_s_ffff0000b809611f,uVar1 >> 0x10 | uVar1 << 0x10);
    }
    if (param_3 != 0) {
      thunk_FUN_ffff0000b8082558(DAT_ffff0000b811afc0);
      DAT_ffff0000b811afc0 = 0;
      DAT_ffff0000b811afc8 = 0;
    }
  }
  return 0;
}


