// ffff0000907352bc  size=228  FUN_ffff0000907352bc

undefined4 FUN_ffff0000907352bc(long param_1,int param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined4 uVar4;
  int local_24;
  
  local_24 = 1;
  if ((param_1 == 0) || (param_2 != 0x48)) {
    FUN_ffff00009077de54(s__Part__illegal_ptr__p_size__d_ffff0000907d45a0,param_1);
    uVar4 = 0xffffffea;
  }
  else {
    lVar2 = FUN_ffff00009077bc50(s_preloader_a_ffff0000907d7a1f);
    if (lVar2 == 0) {
      FUN_ffff00009077de54(s_Partition__preloader_a__is_not_e_ffff0000907f811f);
      uVar4 = 0xffffffed;
    }
    else {
      iVar1 = FUN_ffff00009077bf48(lVar2,0x103,&local_24);
      if (iVar1 == 0) {
        if (local_24 == 1) {
          pcVar3 = s_preloader_a_ffff0000907d7a1f;
        }
        else {
          pcVar3 = s_preloader_b_ffff0000907d9251;
        }
        FUN_ffff00009077efe4(param_1,pcVar3,0xc);
        uVar4 = 0;
      }
      else {
        FUN_ffff00009077de54(s__Part_WP__bio_get_fialed_ffff0000907d1166);
        uVar4 = 0xffffffe4;
      }
      FUN_ffff00009077bce4(lVar2);
    }
  }
  return uVar4;
}


