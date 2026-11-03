// ffff0000907198dc  size=172  FUN_ffff0000907198dc

long FUN_ffff0000907198dc
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
               undefined4 param_5,undefined8 param_6)

{
  long lVar1;
  
  FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
  lVar1 = FUN_ffff000090719988(param_1,param_2,0,param_3,param_4,param_5,param_6);
  FUN_ffff0000907177d0(&LAB_ffff000090818208);
  if (lVar1 != 0) {
    return lVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s__s_failed_ffff0000907f7d7e,s_mblock_alloc_ffff00009080213b);
}


