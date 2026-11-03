
void FUN_ffff0000be014e28(undefined8 param_1,int param_2,undefined4 param_3)

{
  int iVar1;
  long lVar2;
  
  DAT_ffff0000be107a30 = param_3;
  lVar2 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000be05e000(lVar2,param_1,DAT_ffff0000be107a30,(long)param_2);
    FUN_ffff0000be05fd98
              (s_read_expdb__offset_src____llx__l_ffff0000be0b2caa,DAT_ffff0000be107a30,
               (long)param_2);
    if (iVar1 != param_2) {
      FUN_ffff0000be05fd98(s__s__read_expdb_fail_ffff0000be0baaaf,s_read_expdb_ffff0000be0b7565);
    }
    FUN_ffff0000be05def0(lVar2);
    return;
  }
  FUN_ffff0000be05fd98(s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf)
  ;
  return;
}


