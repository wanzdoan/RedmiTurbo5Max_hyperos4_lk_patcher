
undefined8 FUN_ffff0000b801c7cc(int *param_1)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar2 = FUN_ffff0000b804a99c(s_expdb_ffff0000b8088de7);
  if (lVar2 == 0) {
    FUN_ffff0000b804c33c
              (s__s__Partition___s__is_not_exist__ffff0000b809e47d,s_LOG_STORE_LK_ffff0000b8089911,
               s_expdb_ffff0000b8088de7);
    return 0xffffffed;
  }
  FUN_ffff0000b804aa30(lVar2,param_1,*(long *)(lVar2 + 0x20) - *(long *)(lVar2 + 0x28),0x34);
  if (*param_1 == 0x785690ef) {
    lVar3 = *(long *)(lVar2 + 0x28);
    lVar1 = 0x400000;
    if (DAT_ffff0000b811b4f0 == '\0') {
      lVar1 = 0x200000;
    }
    if ((ulong)(uint)param_1[1] < (ulong)(lVar1 - lVar3)) goto LAB_ffff0000b801c8a8;
    param_1[1] = 0;
  }
  else {
    FUN_ffff0000b804d070(param_1,0,0x34);
    lVar3 = *(long *)(lVar2 + 0x28);
    *param_1 = 0x785690ef;
  }
  FUN_ffff0000b804aab8(lVar2,param_1,*(long *)(lVar2 + 0x20) - lVar3,0x34);
LAB_ffff0000b801c8a8:
  FUN_ffff0000b804a920(lVar2);
  return 0;
}


