// ffff000090719798  size=172  FUN_ffff000090719798

long FUN_ffff000090719798
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined8 param_4,
               undefined4 param_5,undefined8 param_6)

{
  long lVar1;
  
  FUN_ffff0000907175f4(&LAB_ffff0000908181f8,0xffffffff);
  lVar1 = FUN_ffff000090719844(param_1,param_2,0,param_3,param_4,param_5,param_6);
  FUN_ffff00009071768c(&LAB_ffff0000908181f8);
  if (lVar1 != 0) {
    return lVar1;
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cc18(s__s_failed_ffff0000907f7be2,s_mblock_alloc_ffff000090801f55);
}


