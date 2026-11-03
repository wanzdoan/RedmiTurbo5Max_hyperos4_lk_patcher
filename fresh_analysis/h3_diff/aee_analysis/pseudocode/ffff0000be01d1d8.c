
/* WARNING: Removing unreachable block (ram,0xffff0000be01d2bc) */

long FUN_ffff0000be01d1d8(void)

{
  int iVar1;
  uint uVar2;
  char *pcVar3;
  long lVar4;
  long unaff_x19;
  long lVar5;
  long unaff_x29;
  
  iVar1 = FUN_ffff0000be066394(&DAT_ffff0000be0c0584,s_lk_main_dtb_ffff0000be0a32ff);
  if (iVar1 < 1) {
    FUN_ffff0000be05fd98(s_Error_load_main_dtb___d__ffff0000be0c1965,iVar1);
    thunk_FUN_ffff0000be09937c();
    return 0;
  }
  iVar1 = FUN_ffff0000be065cb8
                    (&DAT_ffff0000be0c0584,s_lk_dtbo_ffff0000be0a330b,0,unaff_x29 + -8,
                     unaff_x29 + -0xc);
  FUN_ffff0000be05fd98(s_dtbo_size__u_ffff0000be0b2ce5,*(undefined4 *)(unaff_x29 + -0xc));
  uVar2 = (*(uint *)(unaff_x19 + 4) & 0xff00ff00) >> 8 | (*(uint *)(unaff_x19 + 4) & 0xff00ff) << 8;
  uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
  if (iVar1 == 0) {
    uVar2 = FUN_ffff0000be065a4c();
    if (uVar2 == 0) goto LAB_ffff0000be01d294;
    pcVar3 = s_DT_overlay_fail___d__ffff0000be0aa270;
  }
  else {
    pcVar3 = s_load_dtbo_fail__fdt_size__d__ffff0000be0b3f7b;
  }
  FUN_ffff0000be05fd98(pcVar3,uVar2);
LAB_ffff0000be01d294:
  uVar2 = (*(uint *)(unaff_x19 + 4) & 0xff00ff00) >> 8 | (*(uint *)(unaff_x19 + 4) & 0xff00ff) << 8;
  lVar4 = FUN_ffff0000be01fde4(unaff_x19,uVar2 >> 0x10 | uVar2 << 0x10,0);
  lVar5 = unaff_x19;
  if (lVar4 != 0) {
    lVar5 = lVar4;
  }
  iVar1 = FUN_ffff0000be056b9c(lVar5);
  if (-1 < iVar1) {
    if (unaff_x19 != lVar5) {
      thunk_FUN_ffff0000be09937c();
    }
    if (*(long *)(unaff_x29 + -8) != 0) {
      thunk_FUN_ffff0000be09937c();
    }
    return lVar5;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_common_init_c_ffff0000be0be146,0x96,s_ret_>__0_ffff0000be0ab406);
}


