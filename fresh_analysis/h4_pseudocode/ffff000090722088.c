
undefined8 FUN_ffff000090722088(undefined8 param_1,undefined8 param_2,int param_3)

{
  uint uVar1;
  int iVar2;
  
  if ((DAT_ffff000090860690 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_dconfig_ffff0000907fed9c,0x134,
               s_g_env_init_done____DCONFIG_VALID_ffff0000907f9766);
  }
  if ((DAT_ffff000090860694 == '\x01') && (DAT_ffff0000908606c0 != 0)) {
    if (DAT_ffff0000908606c8 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cfcc
                (s_platform_mediatek_common_dconfig_ffff0000907fed9c,0x13a,
                 s_g_dconfig_dtb_size____0_ffff0000907fcf74);
    }
    iVar2 = FUN_ffff000090785140();
    if (iVar2 == 0) {
      uVar1 = (uRam0000000000000004 & 0xff00ff00) >> 8 | (uRam0000000000000004 & 0xff00ff) << 8;
      FUN_ffff00009077e11c
                (s_dconfig_overlay_ok__merged_fdt_s_ffff0000907ede83,uVar1 >> 0x10 | uVar1 << 0x10);
    }
    if (param_3 != 0) {
      thunk_FUN_ffff0000907c314c(DAT_ffff0000908606c0);
      DAT_ffff0000908606c0 = 0;
      DAT_ffff0000908606c8 = 0;
    }
  }
  return 0;
}


