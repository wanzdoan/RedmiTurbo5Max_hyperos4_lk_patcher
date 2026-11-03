// ffff00009072ac00  size=252  FUN_ffff00009072ac00

undefined8 FUN_ffff00009072ac00(int *param_1)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar2 = FUN_ffff00009077bd60(s_expdb_ffff0000907d5730);
  if (lVar2 == 0) {
    FUN_ffff00009077de54
              (s__s__Partition___s__is_not_exist__ffff0000907fcfbd,s_LOG_STORE_LK_ffff0000907d7514,
               s_expdb_ffff0000907d5730);
    return 0xffffffed;
  }
  FUN_ffff00009077bdf4(lVar2,param_1,*(long *)(lVar2 + 0x20) - *(long *)(lVar2 + 0x28),0x34);
  if (*param_1 == 0x785690ef) {
    lVar3 = *(long *)(lVar2 + 0x28);
    lVar1 = 0x400000;
    if (DAT_ffff00009086d638 == '\0') {
      lVar1 = 0x200000;
    }
    if ((ulong)(uint)param_1[1] < (ulong)(lVar1 - lVar3)) goto LAB_ffff00009072acdc;
    param_1[1] = 0;
  }
  else {
    FUN_ffff00009077eb88(param_1,0,0x34);
    lVar3 = *(long *)(lVar2 + 0x28);
    *param_1 = 0x785690ef;
  }
  FUN_ffff00009077be7c(lVar2,param_1,*(long *)(lVar2 + 0x20) - lVar3,0x34);
LAB_ffff00009072acdc:
  FUN_ffff00009077bce4(lVar2);
  return 0;
}


