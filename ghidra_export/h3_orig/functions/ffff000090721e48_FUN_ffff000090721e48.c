// ffff000090721e48  size=216  FUN_ffff000090721e48

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090721e48(undefined8 param_1,undefined8 param_2,int param_3)

{
  uint uVar1;
  int iVar2;
  
  if ((DAT_ffff0000908605f8 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_dconfig_ffff0000907febf4,0x134,
               s_g_env_init_done____DCONFIG_VALID_ffff0000907f95ca);
  }
  if ((DAT_ffff0000908605fc == '\x01') && (_DAT_ffff000090860628 != 0)) {
    if (_DAT_ffff000090860630 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff00009077cd04
                (s_platform_mediatek_common_dconfig_ffff0000907febf4,0x13a,
                 s_g_dconfig_dtb_size____0_ffff0000907fcdcc);
    }
    iVar2 = FUN_ffff000090784e78();
    if (iVar2 == 0) {
      uVar1 = (uRam0000000000000004 & 0xff00ff00) >> 8 | (uRam0000000000000004 & 0xff00ff) << 8;
      FUN_ffff00009077de54
                (s_dconfig_overlay_ok__merged_fdt_s_ffff0000907edd12,uVar1 >> 0x10 | uVar1 << 0x10);
    }
    if (param_3 != 0) {
      thunk_FUN_ffff0000907c2ad8(_DAT_ffff000090860628);
      _DAT_ffff000090860628 = 0;
      _DAT_ffff000090860630 = 0;
    }
  }
  return 0;
}


