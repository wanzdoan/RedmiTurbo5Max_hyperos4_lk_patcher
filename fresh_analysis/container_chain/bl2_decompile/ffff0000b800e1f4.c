
/* WARNING: Removing unreachable block (ram,0xffff0000b800e2d8) */

long FUN_ffff0000b800e1f4(void)

{
  int iVar1;
  uint uVar2;
  char *pcVar3;
  long lVar4;
  long unaff_x19;
  long lVar5;
  long unaff_x29;
  
  iVar1 = FUN_ffff0000b8050eb8(&DAT_ffff0000b809c16c,s_lk_main_dtb_ffff0000b80850c0);
  if (iVar1 < 1) {
    FUN_ffff0000b804c33c(s_Error_load_main_dtb___d__ffff0000b809d462,iVar1);
    thunk_FUN_ffff0000b8082558();
    return 0;
  }
  iVar1 = FUN_ffff0000b805076c
                    (&DAT_ffff0000b809c16c,s_lk_dtbo_ffff0000b80850cc,0,unaff_x29 + -8,
                     unaff_x29 + -0xc);
  FUN_ffff0000b804c33c(s_dtbo_size__u_ffff0000b8091531,*(undefined4 *)(unaff_x29 + -0xc));
  uVar2 = (*(uint *)(unaff_x19 + 4) & 0xff00ff00) >> 8 | (*(uint *)(unaff_x19 + 4) & 0xff00ff) << 8;
  uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
  if (iVar1 == 0) {
    uVar2 = FUN_ffff0000b8050500();
    if (uVar2 == 0) goto LAB_ffff0000b800e2b0;
    pcVar3 = s_DT_overlay_fail___d__ffff0000b808a7aa;
  }
  else {
    pcVar3 = s_load_dtbo_fail__fdt_size__d__ffff0000b8092378;
  }
  FUN_ffff0000b804c33c(pcVar3,uVar2);
LAB_ffff0000b800e2b0:
  uVar2 = (*(uint *)(unaff_x19 + 4) & 0xff00ff00) >> 8 | (*(uint *)(unaff_x19 + 4) & 0xff00ff) << 8;
  lVar4 = FUN_ffff0000b801474c(unaff_x19,uVar2 >> 0x10 | uVar2 << 0x10,0);
  lVar5 = unaff_x19;
  if (lVar4 != 0) {
    lVar5 = lVar4;
  }
  iVar1 = FUN_ffff0000b8048ac8(lVar5);
  if (-1 < iVar1) {
    if (unaff_x19 != lVar5) {
      thunk_FUN_ffff0000b8082558();
    }
    if (*(long *)(unaff_x29 + -8) != 0) {
      thunk_FUN_ffff0000b8082558();
    }
    return lVar5;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_common_init_c_ffff0000b809a70f,0x96,s_ret_>__0_ffff0000b808b514);
}


