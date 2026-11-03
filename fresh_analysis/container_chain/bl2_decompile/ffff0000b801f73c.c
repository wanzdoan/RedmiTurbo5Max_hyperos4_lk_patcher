
undefined4 FUN_ffff0000b801f73c(void)

{
  int iVar1;
  long lVar2;
  undefined4 local_4;
  
  local_4 = 1;
  lVar2 = FUN_ffff0000b804a88c(s_preloader_a_ffff0000b8089bb1);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c(s_Partition__preloader_a__is_not_e_ffff0000b809b83b);
    local_4 = 1;
  }
  else {
    iVar1 = FUN_ffff0000b804ab84(lVar2,0x102,&local_4);
    if (iVar1 != 0) {
      FUN_ffff0000b804c33c(s_bio_get_bootdev_type_fialed_ffff0000b809c72f);
    }
    FUN_ffff0000b804a920(lVar2);
  }
  return local_4;
}


