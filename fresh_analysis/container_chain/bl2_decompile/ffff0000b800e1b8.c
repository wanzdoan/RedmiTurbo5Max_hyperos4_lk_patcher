
long FUN_ffff0000b800e1b8(void)

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
  lVar4 = FUN_ffff0000b804bccc(0xc0000);
  if (lVar4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_init_c_ffff0000b809a70f,100,s_dtb_addr_ffff0000b80850b7);
  }
  if (DAT_ffff0000b8108708 != 0) {
    FUN_ffff0000b800e1f4();
  }
  iVar2 = FUN_ffff0000b8050eb8(&DAT_ffff0000b809c16c,s_lk_main_dtb_ffff0000b80850c0,lVar4,0xc0000);
  if (iVar2 < 1) {
    FUN_ffff0000b804c33c(s_Error_load_main_dtb___d__ffff0000b809d462,iVar2);
    thunk_FUN_ffff0000b8082558(lVar4);
    return 0;
  }
  lStack_38 = lVar4;
  iVar2 = FUN_ffff0000b805076c
                    (&DAT_ffff0000b809c16c,s_lk_dtbo_ffff0000b80850cc,0,&local_28,&local_2c);
  FUN_ffff0000b804c33c(s_dtbo_size__u_ffff0000b8091531,local_2c);
  uVar3 = (*(uint *)(lVar4 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar4 + 4) & 0xff00ff) << 8;
  uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  if (iVar2 == 0) {
    uVar3 = FUN_ffff0000b8050500(lVar4,uVar3,local_28,local_2c,&lStack_38);
    if (uVar3 == 0) goto LAB_ffff0000b800e2b0;
    pcVar5 = s_DT_overlay_fail___d__ffff0000b808a7aa;
  }
  else {
    pcVar5 = s_load_dtbo_fail__fdt_size__d__ffff0000b8092378;
  }
  FUN_ffff0000b804c33c(pcVar5,uVar3);
LAB_ffff0000b800e2b0:
  uVar3 = (*(uint *)(lStack_38 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lStack_38 + 4) & 0xff00ff) << 8;
  lVar6 = FUN_ffff0000b801474c(lStack_38,uVar3 >> 0x10 | uVar3 << 0x10,0);
  lVar1 = lStack_38;
  if ((lVar6 != 0) && (lVar1 = lVar6, lStack_38 != lVar4)) {
    thunk_FUN_ffff0000b8082558();
  }
  lStack_38 = lVar1;
  iVar2 = FUN_ffff0000b8048ac8(lStack_38);
  if (-1 < iVar2) {
    if (lVar4 != lStack_38) {
      thunk_FUN_ffff0000b8082558(lVar4);
    }
    if (local_28 != 0) {
      thunk_FUN_ffff0000b8082558();
    }
    return lStack_38;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_common_init_c_ffff0000b809a70f,0x96,s_ret_>__0_ffff0000b808b514);
}


