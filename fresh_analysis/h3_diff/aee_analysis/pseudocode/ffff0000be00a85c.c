
void FUN_ffff0000be00a85c(undefined8 param_1,int param_2,ulong param_3)

{
  int iVar1;
  long lVar2;
  ulong uVar3;
  
  lVar2 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar2 != 0) {
    uVar3 = *(ulong *)(lVar2 + 0x20);
    if (uVar3 < param_3) {
      FUN_ffff0000be05fd98
                (s__s__partition_size__llx__is_less_ffff0000be0baa5b,
                 s_mrdump_read_expdb_ffff0000be0b6073,uVar3,param_3);
    }
    else if (uVar3 - 0x2fc000 < (long)param_2 + param_3) {
      FUN_ffff0000be05fd98
                (s__s__log_size_0x_x__too_big__ffff0000be0baa92,s_mrdump_read_expdb_ffff0000be0b6073
                 ,param_2);
    }
    else {
      iVar1 = FUN_ffff0000be05e000(lVar2,param_1,param_3);
      if (iVar1 != param_2) {
        FUN_ffff0000be05fd98
                  (s__s__read_expdb_fail_ffff0000be0baaaf,s_mrdump_read_expdb_ffff0000be0b6073);
      }
    }
    FUN_ffff0000be05def0(lVar2);
    return;
  }
  FUN_ffff0000be05fd98(s_kedump__Partition___s__is_not_ex_ffff0000be0a68a4,s_expdb_ffff0000be0a79cf)
  ;
  return;
}


