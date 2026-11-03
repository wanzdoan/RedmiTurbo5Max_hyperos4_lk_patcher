// ffff00009071c3b8  size=60  FUN_ffff00009071c3b8

long FUN_ffff00009071c3b8(void)

{
  long lVar1;
  int iVar2;
  uint uVar3;
  long lVar4;
  char *pcVar5;
  long lVar6;
  long lStack_38;
  undefined4 local_2c;
  long local_28;
  
  local_28 = 0;
  local_2c = 0;
  lVar4 = FUN_ffff00009077d24c(0xc0000);
  if (lVar4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_init_c_ffff0000907f5e15,0x5f,s_dtb_addr_ffff0000907cf340);
  }
  if (DAT_ffff000090839930 != 0) {
    FUN_ffff00009071c3f4();
  }
  iVar2 = FUN_ffff000090785a50(&DAT_ffff0000907f91df,s_lk_main_dtb_ffff0000907cf349,lVar4,0xc0000);
  if (iVar2 < 1) {
    FUN_ffff00009077de54(s_Error_load_main_dtb___d__ffff0000907fb25b,iVar2);
    thunk_FUN_ffff0000907c2ad8(lVar4);
    return 0;
  }
  lStack_38 = lVar4;
  iVar2 = FUN_ffff0000907851bc
                    (&DAT_ffff0000907f91df,s_lk_dtbo_ffff0000907cf355,0,&local_28,&local_2c);
  FUN_ffff00009077de54(s_dtbo_size__u_ffff0000907e5b35,local_2c);
  uVar3 = (*(uint *)(lVar4 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar4 + 4) & 0xff00ff) << 8;
  uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  if (iVar2 == 0) {
    uVar3 = FUN_ffff000090784e78(lVar4,uVar3,local_28,local_2c,&lStack_38);
    if (uVar3 == 0) goto LAB_ffff00009071c4b0;
    pcVar5 = s_DT_overlay_fail___d__ffff0000907d8ebf;
  }
  else {
    pcVar5 = s_load_dtbo_fail__fdt_size__d__ffff0000907e7496;
  }
  FUN_ffff00009077de54(pcVar5,uVar3);
LAB_ffff00009071c4b0:
  uVar3 = (*(uint *)(lStack_38 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lStack_38 + 4) & 0xff00ff) << 8;
  lVar6 = FUN_ffff000090721e48(lStack_38,uVar3 >> 0x10 | uVar3 << 0x10,0);
  lVar1 = lStack_38;
  if ((lVar6 != 0) && (lVar1 = lVar6, lStack_38 != lVar4)) {
    thunk_FUN_ffff0000907c2ad8();
  }
  lStack_38 = lVar1;
  iVar2 = FUN_ffff00009076ec98(lStack_38);
  if (-1 < iVar2) {
    if (lVar4 != lStack_38) {
      thunk_FUN_ffff0000907c2ad8(lVar4);
    }
    if (local_28 != 0) {
      thunk_FUN_ffff0000907c2ad8();
    }
    return lStack_38;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_init_c_ffff0000907f5e15,0x91,s_ret_>__0_ffff0000907da5a0);
}


