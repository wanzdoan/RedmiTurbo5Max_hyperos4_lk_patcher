// ffff000090734fe4  size=120  FUN_ffff000090734fe4

undefined4 FUN_ffff000090734fe4(void)

{
  int iVar1;
  long lVar2;
  undefined4 local_4;
  
  local_4 = 1;
  lVar2 = FUN_ffff00009077bc50(s_preloader_a_ffff0000907d7a1f);
  if (lVar2 == 0) {
    FUN_ffff00009077de54(s_Partition__preloader_a__is_not_e_ffff0000907f811f);
    local_4 = 1;
  }
  else {
    iVar1 = FUN_ffff00009077bf48(lVar2,0x102,&local_4);
    if (iVar1 != 0) {
      FUN_ffff00009077de54(s_bio_get_bootdev_type_fialed_ffff0000907f9201);
    }
    FUN_ffff00009077bce4(lVar2);
  }
  return local_4;
}


