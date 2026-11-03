// ffff00009073527c  size=120  FUN_ffff00009073527c

undefined4 FUN_ffff00009073527c(void)

{
  int iVar1;
  long lVar2;
  undefined4 local_4;
  
  local_4 = 1;
  lVar2 = FUN_ffff00009077bf18(s_preloader_a_ffff0000907d7ab5);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c(s_Partition__preloader_a__is_not_e_ffff0000907f82bb);
    local_4 = 1;
  }
  else {
    iVar1 = FUN_ffff00009077c210(lVar2,0x102,&local_4);
    if (iVar1 != 0) {
      FUN_ffff00009077e11c(s_bio_get_bootdev_type_fialed_ffff0000907f939d);
    }
    FUN_ffff00009077bfac(lVar2);
  }
  return local_4;
}


