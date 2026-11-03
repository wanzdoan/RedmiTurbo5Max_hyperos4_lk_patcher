// ffff000090719f58  size=176  FUN_ffff000090719f58

long FUN_ffff000090719f58
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
               undefined8 param_5,undefined4 param_6,undefined8 param_7)

{
  long lVar1;
  
  FUN_ffff0000907175f4(&LAB_ffff0000908181f8,0xffffffff);
  lVar1 = FUN_ffff000090719844(param_1,param_2,param_3,param_4,param_5,param_6,param_7);
  FUN_ffff00009071768c(&LAB_ffff0000908181f8);
  if (lVar1 != 0) {
    return lVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(s__s_failed_ffff0000907f7be2,s_mblock_alloc_range_ffff0000907df55d);
}


