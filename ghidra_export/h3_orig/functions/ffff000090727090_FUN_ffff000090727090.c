// ffff000090727090  size=188  FUN_ffff000090727090

int FUN_ffff000090727090(long *param_1,int param_2)

{
  long lVar1;
  long lVar2;
  
  if ((param_1 != (long *)0x0) && (param_2 == 0)) {
    lVar2 = *param_1;
    lVar1 = FUN_ffff000090719798
                      (0x200000,0x200000,0xffffffffffffffff,lVar2 + -0x200000,0,
                       s_security_pvmfw_ffff0000907fb3fe);
    if (lVar1 == 0) {
      FUN_ffff00009077de54
                (s__GZ_INIT__MBLOCK___s___s_size_0x_ffff0000907f2759,
                 s_gz_mblock_info_post_process_ffff0000907d2914,s_security_pvmfw_ffff0000907fb3fe,
                 0x200000);
    }
    else {
      if (lVar1 == lVar2 + -0x200000) {
        return 0;
      }
      FUN_ffff00009077de54
                (s__GZ_INIT__MBLOCK___s___s_not_exp_ffff0000907ef597,
                 s_gz_mblock_info_post_process_ffff0000907d2914,s_security_pvmfw_ffff0000907fb3fe);
      FUN_ffff00009071a6ac(lVar1);
    }
    param_2 = -5;
  }
  return param_2;
}


