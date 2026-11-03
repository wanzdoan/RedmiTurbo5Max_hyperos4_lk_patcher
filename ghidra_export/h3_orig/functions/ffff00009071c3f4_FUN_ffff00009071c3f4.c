// ffff00009071c3f4  size=336  FUN_ffff00009071c3f4

/* WARNING: Removing unreachable block (ram,0xffff00009071c4d8) */

long FUN_ffff00009071c3f4(void)

{
  int iVar1;
  uint uVar2;
  char *pcVar3;
  long lVar4;
  long unaff_x19;
  long lVar5;
  long unaff_x29;
  
  iVar1 = FUN_ffff000090785a50(&DAT_ffff0000907f91df,s_lk_main_dtb_ffff0000907cf349);
  if (iVar1 < 1) {
    FUN_ffff00009077de54(s_Error_load_main_dtb___d__ffff0000907fb25b,iVar1);
    thunk_FUN_ffff0000907c2ad8();
    return 0;
  }
  iVar1 = FUN_ffff0000907851bc
                    (&DAT_ffff0000907f91df,s_lk_dtbo_ffff0000907cf355,0,unaff_x29 + -8,
                     unaff_x29 + -0xc);
  FUN_ffff00009077de54(s_dtbo_size__u_ffff0000907e5b35,*(undefined4 *)(unaff_x29 + -0xc));
  uVar2 = (*(uint *)(unaff_x19 + 4) & 0xff00ff00) >> 8 | (*(uint *)(unaff_x19 + 4) & 0xff00ff) << 8;
  uVar2 = uVar2 >> 0x10 | uVar2 << 0x10;
  if (iVar1 == 0) {
    uVar2 = FUN_ffff000090784e78();
    if (uVar2 == 0) goto LAB_ffff00009071c4b0;
    pcVar3 = s_DT_overlay_fail___d__ffff0000907d8ebf;
  }
  else {
    pcVar3 = s_load_dtbo_fail__fdt_size__d__ffff0000907e7496;
  }
  FUN_ffff00009077de54(pcVar3,uVar2);
LAB_ffff00009071c4b0:
  uVar2 = (*(uint *)(unaff_x19 + 4) & 0xff00ff00) >> 8 | (*(uint *)(unaff_x19 + 4) & 0xff00ff) << 8;
  lVar4 = FUN_ffff000090721e48(unaff_x19,uVar2 >> 0x10 | uVar2 << 0x10,0);
  lVar5 = unaff_x19;
  if (lVar4 != 0) {
    lVar5 = lVar4;
  }
  iVar1 = FUN_ffff00009076ec98(lVar5);
  if (-1 < iVar1) {
    if (unaff_x19 != lVar5) {
      thunk_FUN_ffff0000907c2ad8();
    }
    if (*(long *)(unaff_x29 + -8) != 0) {
      thunk_FUN_ffff0000907c2ad8();
    }
    return lVar5;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_init_c_ffff0000907f5e15,0x91,s_ret_>__0_ffff0000907da5a0);
}


