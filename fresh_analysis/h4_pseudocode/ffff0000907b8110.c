
undefined4
FUN_ffff0000907b8110(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  lVar2 = FUN_ffff00009077c028();
  if (lVar2 == 0) {
    uVar3 = 0x100008;
    FUN_ffff00009077e11c
              (s___s__Partition___s__is_not_exist_ffff0000907f22eb,&DAT_ffff0000907f228a,param_1);
  }
  else {
    iVar1 = FUN_ffff00009077c0bc(lVar2,param_2,param_3,(long)param_4);
    if (iVar1 < param_4) {
      uVar3 = 0x100008;
      FUN_ffff00009077e11c
                (s___s__Partition___s__read_fail__o_ffff0000907f3ca5,&DAT_ffff0000907f228a,param_1,
                 param_3,param_4);
    }
    else {
      uVar3 = 0;
    }
    FUN_ffff00009077bfac(lVar2);
  }
  return uVar3;
}


