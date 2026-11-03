
undefined4 FUN_ffff0000be028018(void)

{
  int iVar1;
  long lVar2;
  undefined4 local_4;
  
  local_4 = 1;
  lVar2 = FUN_ffff0000be05de5c(s_preloader_a_ffff0000be0a9083);
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98(s_Partition__preloader_a__is_not_e_ffff0000be0bf785);
    local_4 = 1;
  }
  else {
    iVar1 = FUN_ffff0000be05e154(lVar2,0x102,&local_4);
    if (iVar1 != 0) {
      FUN_ffff0000be05fd98(s_bio_get_bootdev_type_fialed_ffff0000be0c05a6);
    }
    FUN_ffff0000be05def0(lVar2);
  }
  return local_4;
}


