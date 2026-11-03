
void FUN_ffff0000b8023de8(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  char *pcVar5;
  
  lVar2 = FUN_ffff0000b804bccc(0xc0000);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c(s_dtb_ptr_malloc_failed_ffff0000b8094580);
    return;
  }
  iVar1 = FUN_ffff0000b8050eb8(&DAT_ffff0000b809c16c,s_lk_main_dtb_ffff0000b80850c0,lVar2,0xc0000);
  if (iVar1 < 0) {
    pcVar5 = s__DTB__load_dtb_img_fail_ffff0000b809f729;
  }
  else {
    uVar3 = FUN_ffff0000b8049ec8(lVar2);
    uVar4 = (*param_1)(uVar3,0xc0000);
    if ((uVar4 & 1) != 0) goto LAB_ffff0000b8023e70;
    pcVar5 = s_DTB_dump_failed__ffff0000b808edd8;
  }
  FUN_ffff0000b804c33c(pcVar5);
LAB_ffff0000b8023e70:
  thunk_FUN_ffff0000b8082558(lVar2);
  return;
}


