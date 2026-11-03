// ffff00009070b4bc  size=200  FUN_ffff00009070b4bc

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009070b4bc(undefined8 param_1,int param_2,undefined4 param_3)

{
  int iVar1;
  long lVar2;
  
  _DAT_ffff00009083d288 = param_3;
  lVar2 = FUN_ffff00009077bd60(s_expdb_ffff0000907d5730);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff00009077bdf4(lVar2,param_1,_DAT_ffff00009083d288,(long)param_2);
    FUN_ffff00009077de54
              (s_read_expdb__offset_src____llx__l_ffff0000907e59f3,_DAT_ffff00009083d288,
               (long)param_2);
    if (iVar1 != param_2) {
      FUN_ffff00009077de54(s__s__read_expdb_fail_ffff0000907f0b74,s_read_expdb_ffff0000907ebdae);
    }
    FUN_ffff00009077bce4(lVar2);
    return;
  }
  FUN_ffff00009077de54(s_kedump__Partition___s__is_not_ex_ffff0000907d3e02,s_expdb_ffff0000907d5730)
  ;
  return;
}


