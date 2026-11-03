
undefined8 FUN_ffff0000be065a4c(long param_1,int param_2,long param_3,int param_4,long *param_5)

{
  uint uVar1;
  long lVar2;
  char *pcVar3;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_common_dtb_ops_ffff0000be0b7f88,0x29,
               s_main_dtb_addr_ffff0000be0c2417);
  }
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_common_dtb_ops_ffff0000be0b7f88,0x2a,
               s_main_dtb_size_ffff0000be0b04d2);
  }
  if (param_3 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_common_dtb_ops_ffff0000be0b7f88,0x2b,s_dtbo_addr_ffff0000be0a87b0
              );
  }
  if (param_4 != 0) {
    if (param_5 == (long *)0x0) {
                    /* WARNING: Subroutine does not return */
      FUN_ffff0000be05ee98
                (s_platform_mediatek_common_dtb_ops_ffff0000be0b7f88,0x2d,
                 s_merged_dtb_ffff0000be0a9993);
    }
    lVar2 = FUN_ffff0000be0987a4(param_1,param_2);
    if (lVar2 == 0) {
      pcVar3 = s_ufdt_install_blob___failed__ffff0000be0ba52b;
    }
    else {
      lVar2 = FUN_ffff0000be098868(lVar2,param_2,param_3,param_4);
      if (lVar2 != 0) {
        uVar1 = (*(uint *)(lVar2 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar2 + 4) & 0xff00ff) << 8;
        FUN_ffff0000be05fd98
                  (s_after_overlay__fdt_size__d__ffff0000be0c1240,uVar1 >> 0x10 | uVar1 << 0x10);
        *param_5 = lVar2;
        return 0;
      }
      pcVar3 = s_ufdt_apply_overlay___failed__ffff0000be0b6e1c;
    }
    FUN_ffff0000be05fd98(pcVar3);
    return 0xfffffff8;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_common_dtb_ops_ffff0000be0b7f88,0x2c,s_dtbo_size_ffff0000be0bc6c9);
}


