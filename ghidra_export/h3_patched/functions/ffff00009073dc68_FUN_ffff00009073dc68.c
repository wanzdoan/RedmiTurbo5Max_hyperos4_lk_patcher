// ffff00009073dc68  size=156  FUN_ffff00009073dc68

void FUN_ffff00009073dc68(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  char *pcVar5;
  
  lVar2 = FUN_ffff00009077d24c(0xc0000);
  if (lVar2 == 0) {
    FUN_ffff00009077de54(s_dtb_ptr_malloc_failed_ffff0000907eacd4);
    return;
  }
  iVar1 = FUN_ffff000090785a50(&DAT_ffff0000907f91df,s_lk_main_dtb_ffff0000907cf349,lVar2,0xc0000);
  if (iVar1 < 0) {
    pcVar5 = s__DTB__load_dtb_img_fail_ffff0000907ff0a3;
  }
  else {
    uVar3 = FUN_ffff00009077b06c(lVar2);
    uVar4 = (*param_1)(uVar3,0xc0000);
    if ((uVar4 & 1) != 0) goto LAB_ffff00009073dcf0;
    pcVar5 = s_DTB_dump_failed__ffff0000907e14be;
  }
  FUN_ffff00009077de54(pcVar5);
LAB_ffff00009073dcf0:
  thunk_FUN_ffff0000907c2ad8(lVar2);
  return;
}


