
void FUN_ffff00009070b534(undefined8 param_1,int param_2,undefined4 param_3)

{
  int iVar1;
  long lVar2;
  
  DAT_ffff00009083d2c4 = param_3;
  lVar2 = FUN_ffff00009077c028(s_expdb_ffff0000907d5780);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff00009077c0bc(lVar2,param_1,DAT_ffff00009083d2c4,(long)param_2);
    FUN_ffff00009077e11c
              (s_read_expdb__offset_src____llx__l_ffff0000907e5b1e,DAT_ffff00009083d2c4,
               (long)param_2);
    if (iVar1 != param_2) {
      FUN_ffff00009077e11c(s__s__read_expdb_fail_ffff0000907f0cfc,s_read_expdb_ffff0000907ebf48);
    }
    FUN_ffff00009077bfac(lVar2);
    return;
  }
  FUN_ffff00009077e11c(s_kedump__Partition___s__is_not_ex_ffff0000907d3e3f,s_expdb_ffff0000907d5780)
  ;
  return;
}


