
long FUN_ffff0000be01d19c(void)

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
  lVar4 = FUN_ffff0000be05f1bc(0xc0000);
  if (lVar4 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_common_init_c_ffff0000be0be146,100,s_dtb_addr_ffff0000be0a32f6);
  }
  if (DAT_ffff0000be0f5608 != 0) {
    FUN_ffff0000be01d1d8();
  }
  iVar2 = FUN_ffff0000be066394(&DAT_ffff0000be0c0584,s_lk_main_dtb_ffff0000be0a32ff,lVar4,0xc0000);
  if (iVar2 < 1) {
    FUN_ffff0000be05fd98(s_Error_load_main_dtb___d__ffff0000be0c1965,iVar2);
    thunk_FUN_ffff0000be09937c(lVar4);
    return 0;
  }
  lStack_38 = lVar4;
  iVar2 = FUN_ffff0000be065cb8
                    (&DAT_ffff0000be0c0584,s_lk_dtbo_ffff0000be0a330b,0,&local_28,&local_2c);
  FUN_ffff0000be05fd98(s_dtbo_size__u_ffff0000be0b2ce5,local_2c);
  uVar3 = (*(uint *)(lVar4 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lVar4 + 4) & 0xff00ff) << 8;
  uVar3 = uVar3 >> 0x10 | uVar3 << 0x10;
  if (iVar2 == 0) {
    uVar3 = FUN_ffff0000be065a4c(lVar4,uVar3,local_28,local_2c,&lStack_38);
    if (uVar3 == 0) goto LAB_ffff0000be01d294;
    pcVar5 = s_DT_overlay_fail___d__ffff0000be0aa270;
  }
  else {
    pcVar5 = s_load_dtbo_fail__fdt_size__d__ffff0000be0b3f7b;
  }
  FUN_ffff0000be05fd98(pcVar5,uVar3);
LAB_ffff0000be01d294:
  uVar3 = (*(uint *)(lStack_38 + 4) & 0xff00ff00) >> 8 | (*(uint *)(lStack_38 + 4) & 0xff00ff) << 8;
  lVar6 = FUN_ffff0000be01fde4(lStack_38,uVar3 >> 0x10 | uVar3 << 0x10,0);
  lVar1 = lStack_38;
  if ((lVar6 != 0) && (lVar1 = lVar6, lStack_38 != lVar4)) {
    thunk_FUN_ffff0000be09937c();
  }
  lStack_38 = lVar1;
  iVar2 = FUN_ffff0000be056b9c(lStack_38);
  if (-1 < iVar2) {
    if (lVar4 != lStack_38) {
      thunk_FUN_ffff0000be09937c(lVar4);
    }
    if (local_28 != 0) {
      thunk_FUN_ffff0000be09937c();
    }
    return lStack_38;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_common_init_c_ffff0000be0be146,0x96,s_ret_>__0_ffff0000be0ab406);
}


