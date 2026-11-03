// ffff0000907b7a9c  size=196  FUN_ffff0000907b7a9c

undefined4
FUN_ffff0000907b7a9c(undefined8 param_1,undefined8 param_2,undefined8 param_3,int param_4)

{
  int iVar1;
  long lVar2;
  undefined4 uVar3;
  
  lVar2 = FUN_ffff00009077bd60();
  if (lVar2 == 0) {
    uVar3 = 0x100008;
    FUN_ffff00009077de54
              (s___s__Partition___s__is_not_exist_ffff0000907f2148,&DAT_ffff0000907f20e7,param_1);
  }
  else {
    iVar1 = FUN_ffff00009077bdf4(lVar2,param_2,param_3,(long)param_4);
    if (iVar1 < param_4) {
      uVar3 = 0x100008;
      FUN_ffff00009077de54
                (s___s__Partition___s__read_fail__o_ffff0000907f3b02,&DAT_ffff0000907f20e7,param_1,
                 param_3,param_4);
    }
    else {
      uVar3 = 0;
    }
    FUN_ffff00009077bce4(lVar2);
  }
  return uVar3;
}


