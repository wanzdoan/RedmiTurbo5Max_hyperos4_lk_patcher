
undefined4 FUN_ffff00009073585c(void)

{
  int iVar1;
  long lVar2;
  undefined4 local_4;

  local_4 = 1;
  lVar2 = FUN_ffff00009077c880(s_preloader_a_ffff0000907cfd89);
  if (lVar2 == 0) {
    FUN_ffff00009077ea84(s_Partition__preloader_a__is_not_e_ffff0000907f0f89);
    local_4 = 1;
  }
  else {
    iVar1 = FUN_ffff00009077cb78(lVar2,0x102,&local_4);
    if (iVar1 == 0) {
      FUN_ffff00009077ea84(s_bootdev_type__d__ffff0000907c7825,local_4);
    }
    else {
      FUN_ffff00009077ea84(s_bio_get_bootdev_type_fialed_ffff0000907f2082);
    }
    FUN_ffff00009077c914(lVar2);
  }
  return local_4;
}
