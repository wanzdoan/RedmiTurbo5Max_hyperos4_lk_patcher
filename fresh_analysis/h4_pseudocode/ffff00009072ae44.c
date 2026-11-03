
undefined8 FUN_ffff00009072ae44(int *param_1)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar2 = FUN_ffff00009077c028(s_expdb_ffff0000907d5780);
  if (lVar2 == 0) {
    FUN_ffff00009077e11c
              (s__s__Partition___s__is_not_exist__ffff0000907fd165,s_LOG_STORE_LK_ffff0000907d75aa,
               s_expdb_ffff0000907d5780);
    return 0xffffffed;
  }
  FUN_ffff00009077c0bc(lVar2,param_1,*(long *)(lVar2 + 0x20) - *(long *)(lVar2 + 0x28),0x34);
  if (*param_1 == 0x785690ef) {
    lVar3 = *(long *)(lVar2 + 0x28);
    lVar1 = 0x400000;
    if (DAT_ffff00009086d6d0 == '\0') {
      lVar1 = 0x200000;
    }
    if ((ulong)(uint)param_1[1] < (ulong)(lVar1 - lVar3)) goto LAB_ffff00009072af20;
    param_1[1] = 0;
  }
  else {
    FUN_ffff00009077ee50(param_1,0,0x34);
    lVar3 = *(long *)(lVar2 + 0x28);
    *param_1 = 0x785690ef;
  }
  FUN_ffff00009077c144(lVar2,param_1,*(long *)(lVar2 + 0x20) - lVar3,0x34);
LAB_ffff00009072af20:
  FUN_ffff00009077bfac(lVar2);
  return 0;
}


