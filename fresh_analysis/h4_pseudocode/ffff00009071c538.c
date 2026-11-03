
/* WARNING: Removing unreachable block (ram,0xffff00009071c61c) */

long FUN_ffff00009071c538(void)

{
  int iVar1;
  uint uVar2;
  char *pcVar3;
  long lVar4;
  long unaff_x19;
  long lVar5;
  long unaff_x29;
  
  iVar1 = FUN_ffff000090785d18(&DAT_ffff0000907f937b,s_lk_main_dtb_ffff0000907cf368);
  if (iVar1 < 1) {
    FUN_ffff00009077e11c(s_Error_load_main_dtb___d__ffff0000907fb3f7,iVar1);
    thunk_FUN_ffff0000907c314c();
    return 0;
  }
  iVar1 = FUN_ffff000090785484
                    (&DAT_ffff0000907f937b,s_lk_dtbo_ffff0000907cf374,0,unaff_x29 + -8,
                     unaff_x29 + -0xc);
  FUN_ffff00009077e11c(s_dtbo_size__u_ffff0000907e5c49,*(undefined4 *)(unaff_x29 + -0xc));
  uVar2 = (*(uint *)(unaff_x19 + 4) & 0xff00ff00) >> 8 | (*(uint *)(unaff_x19 + 4) & 0xff00ff) << 8;
  uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
  if (iVar1 == 0) {
    uVar2 = FUN_ffff000090785140();
    if (uVar2 == 0) goto LAB_ffff00009071c5f4;
    pcVar3 = s_DT_overlay_fail___d__ffff0000907d8f56;
  }
  else {
    pcVar3 = s_load_dtbo_fail__fdt_size__d__ffff0000907e75da;
  }
  FUN_ffff00009077e11c(pcVar3,uVar2);
LAB_ffff00009071c5f4:
  uVar2 = (*(uint *)(unaff_x19 + 4) & 0xff00ff00) >> 8 | (*(uint *)(unaff_x19 + 4) & 0xff00ff) << 8;
  lVar4 = FUN_ffff000090722088(unaff_x19,uVar2 >> 0x10 | uVar2 << 0x10,0);
  lVar5 = unaff_x19;
  if (lVar4 != 0) {
    lVar5 = lVar4;
  }
  iVar1 = FUN_ffff00009076ef60(lVar5);
  if (-1 < iVar1) {
    if (unaff_x19 != lVar5) {
      thunk_FUN_ffff0000907c314c();
    }
    if (*(long *)(unaff_x29 + -8) != 0) {
      thunk_FUN_ffff0000907c314c();
    }
    return lVar5;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_init_c_ffff0000907f5f8c,0x96,s_ret_>__0_ffff0000907da66e);
}


