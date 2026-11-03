
undefined4
FUN_ffff0000b80732d8(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  lVar2 = FUN_ffff0000b804a99c();
  if (lVar2 == 0) {
    uVar3 = 0x100008;
    FUN_ffff0000b804c33c
              (s___s__Partition___s__is_not_exist_ffff0000b8098847,&DAT_ffff0000b80987e6,param_1);
  }
  else {
    iVar1 = FUN_ffff0000b804aa30(lVar2,param_2,param_3,(long)param_4);
    if (iVar1 < param_4) {
      uVar3 = 0x100008;
      FUN_ffff0000b804c33c
                (s___s__Partition___s__read_fail__o_ffff0000b8099734,&DAT_ffff0000b80987e6,param_1,
                 param_3,param_4);
    }
    else {
      uVar3 = 0;
    }
    FUN_ffff0000b804a920(lVar2);
  }
  return uVar3;
}


