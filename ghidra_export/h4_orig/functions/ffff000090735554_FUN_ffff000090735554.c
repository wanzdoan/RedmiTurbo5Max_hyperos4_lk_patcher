// ffff000090735554  size=228  FUN_ffff000090735554

undefined4 FUN_ffff000090735554(long param_1,int param_2)

{
  int iVar1;
  long lVar2;
  char *pcVar3;
  undefined4 uVar4;
  int local_24;
  
  local_24 = 1;
  if ((param_1 == 0) || (param_2 != 0x48)) {
    FUN_ffff00009077e11c(s__Part__illegal_ptr__p_size__d_ffff0000907d45dd,param_1);
    uVar4 = 0xffffffea;
  }
  else {
    lVar2 = FUN_ffff00009077bf18(s_preloader_a_ffff0000907d7ab5);
    if (lVar2 == 0) {
      FUN_ffff00009077e11c(s_Partition__preloader_a__is_not_e_ffff0000907f82bb);
      uVar4 = 0xffffffed;
    }
    else {
      iVar1 = FUN_ffff00009077c210(lVar2,0x103,&local_24);
      if (iVar1 == 0) {
        if (local_24 == 1) {
          pcVar3 = s_preloader_a_ffff0000907d7ab5;
        }
        else {
          pcVar3 = s_preloader_b_ffff0000907d92e8;
        }
        FUN_ffff00009077f2ac(param_1,pcVar3,0xc);
        uVar4 = 0;
      }
      else {
        FUN_ffff00009077e11c(s__Part_WP__bio_get_fialed_ffff0000907d11a3);
        uVar4 = 0xffffffe4;
      }
      FUN_ffff00009077bfac(lVar2);
    }
  }
  return uVar4;
}


