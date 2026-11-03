
void FUN_ffff0000be02e1cc(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  char *pcVar5;
  
  lVar2 = FUN_ffff0000be05f1bc(0xc0000);
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98(s_dtb_ptr_malloc_failed_ffff0000be0b65cf);
    return;
  }
  iVar1 = FUN_ffff0000be066394(&DAT_ffff0000be0c0584,s_lk_main_dtb_ffff0000be0a32ff,lVar2,0xc0000);
  if (iVar1 < 0) {
    pcVar5 = s__DTB__load_dtb_img_fail_ffff0000be0c409e;
  }
  else {
    uVar3 = FUN_ffff0000be05d228(lVar2);
    uVar4 = (*param_1)(uVar3,0xc0000);
    if ((uVar4 & 1) != 0) goto LAB_ffff0000be02e254;
    pcVar5 = s_DTB_dump_failed__ffff0000be0afae6;
  }
  FUN_ffff0000be05fd98(pcVar5);
LAB_ffff0000be02e254:
  thunk_FUN_ffff0000be09937c(lVar2);
  return;
}


