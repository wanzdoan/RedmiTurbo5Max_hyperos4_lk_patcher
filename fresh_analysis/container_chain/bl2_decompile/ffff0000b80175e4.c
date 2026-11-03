
int FUN_ffff0000b80175e4(long *param_1,int param_2)

{
  long lVar1;
  long lVar2;
  
  if ((param_1 != (long *)0x0) && (param_2 == 0)) {
    lVar2 = *param_1;
    lVar1 = FUN_ffff0000b800c9f4
                      (0x200000,0x200000,0xffffffffffffffff,lVar2 + -0x200000,0,
                       s_security_pvmfw_ffff0000b809d47c);
    if (lVar1 == 0) {
      FUN_ffff0000b804c33c
                (s__GZ_INIT__MBLOCK___s___s_size_0x_ffff0000b8098b2c,
                 s_gz_mblock_info_post_process_ffff0000b808708a,s_security_pvmfw_ffff0000b809d47c,
                 0x200000);
    }
    else {
      if (lVar1 == lVar2 + -0x200000) {
        return 0;
      }
      FUN_ffff0000b804c33c
                (s__GZ_INIT__MBLOCK___s___s_not_exp_ffff0000b8097023,
                 s_gz_mblock_info_post_process_ffff0000b808708a,s_security_pvmfw_ffff0000b809d47c);
      FUN_ffff0000b800d264(lVar1);
    }
    param_2 = -5;
  }
  return param_2;
}


