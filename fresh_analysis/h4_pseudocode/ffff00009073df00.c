
void FUN_ffff00009073df00(code *param_1)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  ulong uVar4;
  char *pcVar5;
  
  lVar2 = FUN_ffff00009077d514(0xc0000);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c(s_dtb_ptr_malloc_failed_ffff0000907eae6e);
    return;
  }
  iVar1 = FUN_ffff000090785d18(&DAT_ffff0000907f937b,s_lk_main_dtb_ffff0000907cf368,lVar2,0xc0000);
  if (iVar1 < 0) {
    pcVar5 = s__DTB__load_dtb_img_fail_ffff0000907ff24b;
  }
  else {
    uVar3 = FUN_ffff00009077b334(lVar2);
    uVar4 = (*param_1)(uVar3,0xc0000);
    if ((uVar4 & 1) != 0) goto LAB_ffff00009073df88;
    pcVar5 = s_DTB_dump_failed__ffff0000907e15a6;
  }
  FUN_ffff00009077e11c(pcVar5);
LAB_ffff00009073df88:
  thunk_FUN_ffff0000907c314c(lVar2);
  return;
}


