
long FUN_ffff00009071c4fc(void)

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
  lVar4 = FUN_ffff00009077d514(0xc0000);
  if (lVar4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_init_c_ffff0000907f5f8c,100,s_dtb_addr_ffff0000907cf35f);
  }
  if (DAT_ffff000090839950 != 0) {
    FUN_ffff00009071c538();
  }
  iVar2 = FUN_ffff000090785d18(&DAT_ffff0000907f937b,s_lk_main_dtb_ffff0000907cf368,lVar4,0xc0000);
  if (iVar2 < 1) {
    FUN_ffff00009077e11c(s_Error_load_main_dtb___d__ffff0000907fb3f7,iVar2);
    thunk_FUN_ffff0000907c314c(lVar4);
    return 0;
  }
  lStack_38 = lVar4;
  iVar2 = FUN_ffff000090785484
                    (&DAT_ffff0000907f937b,s_lk_dtbo_ffff0000907cf374,0,&local_28,&local_2c);
  FUN_ffff00009077e11c(s_dtbo_size__u_ffff0000907e5c49,local_2c);
  uVar3 = (*(uint *)(lVar4 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar4 + 4) & 0xff00ff) << 8;
  uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  if (iVar2 == 0) {
    uVar3 = FUN_ffff000090785140(lVar4,uVar3,local_28,local_2c,&lStack_38);
    if (uVar3 == 0) goto LAB_ffff00009071c5f4;
    pcVar5 = s_DT_overlay_fail___d__ffff0000907d8f56;
  }
  else {
    pcVar5 = s_load_dtbo_fail__fdt_size__d__ffff0000907e75da;
  }
  FUN_ffff00009077e11c(pcVar5,uVar3);
LAB_ffff00009071c5f4:
  uVar3 = (*(uint *)(lStack_38 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lStack_38 + 4) & 0xff00ff) << 8;
  lVar6 = FUN_ffff000090722088(lStack_38,uVar3 >> 0x10 | uVar3 << 0x10,0);
  lVar1 = lStack_38;
  if ((lVar6 != 0) && (lVar1 = lVar6, lStack_38 != lVar4)) {
    thunk_FUN_ffff0000907c314c();
  }
  lStack_38 = lVar1;
  iVar2 = FUN_ffff00009076ef60(lStack_38);
  if (-1 < iVar2) {
    if (lVar4 != lStack_38) {
      thunk_FUN_ffff0000907c314c(lVar4);
    }
    if (local_28 != 0) {
      thunk_FUN_ffff0000907c314c();
    }
    return lStack_38;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_init_c_ffff0000907f5f8c,0x96,s_ret_>__0_ffff0000907da66e);
}


