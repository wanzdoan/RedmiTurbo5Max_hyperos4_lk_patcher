
undefined4
FUN_ffff0000be091f34(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  lVar2 = FUN_ffff0000be05df6c();
  if (lVar2 == 0) {
    uVar3 = 0x100008;
    FUN_ffff0000be05fd98
              (s___s__Partition___s__is_not_exist_ffff0000be0bb872,&DAT_ffff0000be0bb811,param_1);
  }
  else {
    iVar1 = FUN_ffff0000be05e000(lVar2,param_2,param_3,(long)param_4);
    if (iVar1 < param_4) {
      uVar3 = 0x100008;
      FUN_ffff0000be05fd98
                (s___s__Partition___s__read_fail__o_ffff0000be0bcad7,&DAT_ffff0000be0bb811,param_1,
                 param_3,param_4);
    }
    else {
      uVar3 = 0;
    }
    FUN_ffff0000be05def0(lVar2);
  }
  return uVar3;
}


