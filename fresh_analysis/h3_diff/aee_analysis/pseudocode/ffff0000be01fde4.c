
undefined8 FUN_ffff0000be01fde4(undefined8 param_1,undefined8 param_2,int param_3)

{
  uint uVar1;
  int iVar2;
  
  if ((DAT_ffff0000be1176a0 & 1) == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_common_dconfig_ffff0000be0c3eb5,0x134,
               s_g_env_init_done____DCONFIG_VALID_ffff0000be0c0675);
  }
  if ((DAT_ffff0000be1176a4 == '\x01') && (DAT_ffff0000be1176d0 != 0)) {
    if (DAT_ffff0000be1176d8 == 0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_common_dconfig_ffff0000be0c3eb5,0x13a,
                 s_g_dconfig_dtb_size____0_ffff0000be0c2af0);
    }
    iVar2 = FUN_ffff0000be065a4c();
    if (iVar2 == 0) {
      uVar1 = (uRam0000000000000004 & 0xff00ff00) >> 8 | (uRam0000000000000004 & 0xff00ff) << 8;
      FUN_ffff0000be05fd98
                (s_dconfig_overlay_ok__merged_fdt_s_ffff0000be0b87ac,uVar1 >> 0x10 | uVar1 << 0x10);
    }
    if (param_3 != 0) {
      thunk_FUN_ffff0000be09937c(DAT_ffff0000be1176d0);
      DAT_ffff0000be1176d0 = 0;
      DAT_ffff0000be1176d8 = 0;
    }
  }
  return 0;
}


