// ffff00009071a09c  size=176  FUN_ffff00009071a09c

long FUN_ffff00009071a09c
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
               undefined8 param_5,undefined4 param_6,undefined8 param_7)

{
  long lVar1;
  
  FUN_ffff000090717738(&LAB_ffff000090818208,0xffffffff);
  lVar1 = FUN_ffff000090719988(param_1,param_2,param_3,param_4,param_5,param_6,param_7);
  FUN_ffff0000907177d0(&LAB_ffff000090818208);
  if (lVar1 != 0) {
    return lVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cee0(s__s_failed_ffff0000907f7d7e,s_mblock_alloc_range_ffff0000907df600);
}


