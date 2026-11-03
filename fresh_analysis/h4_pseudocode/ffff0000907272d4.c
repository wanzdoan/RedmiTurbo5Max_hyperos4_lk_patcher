
int FUN_ffff0000907272d4(long *param_1,int param_2)

{
  long lVar1;
  long lVar2;
  
  if ((param_1 != (long *)0x0) && (param_2 == 0)) {
    lVar2 = *param_1;
    lVar1 = FUN_ffff0000907198dc
                      (0x200000,0x200000,0xffffffffffffffff,lVar2 + -0x200000,0,
                       s_security_pvmfw_ffff0000907fb59a);
    if (lVar1 == 0) {
      FUN_ffff00009077e11c
                (s__GZ_INIT__MBLOCK___s___s_size_0x_ffff0000907f28e5,
                 s_gz_mblock_info_post_process_ffff0000907d2951,s_security_pvmfw_ffff0000907fb59a,
                 0x200000);
    }
    else {
      if (lVar1 == lVar2 + -0x200000) {
        return 0;
      }
      FUN_ffff00009077e11c
                (s__GZ_INIT__MBLOCK___s___s_not_exp_ffff0000907ef708,
                 s_gz_mblock_info_post_process_ffff0000907d2951,s_security_pvmfw_ffff0000907fb59a);
      FUN_ffff00009071a7f0(lVar1);
    }
    param_2 = -5;
  }
  return param_2;
}


