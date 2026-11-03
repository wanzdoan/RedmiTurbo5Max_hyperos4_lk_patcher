
undefined8 FUN_ffff0000be0247f8(int *param_1)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar2 = FUN_ffff0000be05df6c(s_expdb_ffff0000be0a79cf);
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98
              (s__s__Partition___s__is_not_exist__ffff0000be0c2ba1,s_LOG_STORE_LK_ffff0000be0a8eaf,
               s_expdb_ffff0000be0a79cf);
    return 0xffffffed;
  }
  FUN_ffff0000be05e000(lVar2,param_1,*(long *)(lVar2 + 0x20) - *(long *)(lVar2 + 0x28),0x34);
  if (*param_1 == 0x785690ef) {
    lVar3 = *(long *)(lVar2 + 0x28);
    lVar1 = 0x400000;
    if (DAT_ffff0000be117d08 == '\0') {
      lVar1 = 0x200000;
    }
    if ((ulong)(uint)param_1[1] < (ulong)(lVar1 - lVar3)) goto LAB_ffff0000be0248d4;
    param_1[1] = 0;
  }
  else {
    FUN_ffff0000be060ae0(param_1,0,0x34);
    lVar3 = *(long *)(lVar2 + 0x28);
    *param_1 = 0x785690ef;
  }
  FUN_ffff0000be05e088(lVar2,param_1,*(long *)(lVar2 + 0x20) - lVar3,0x34);
LAB_ffff0000be0248d4:
  FUN_ffff0000be05def0(lVar2);
  return 0;
}


