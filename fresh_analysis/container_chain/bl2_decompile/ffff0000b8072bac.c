
undefined8
FUN_ffff0000b8072bac
          (long param_1,int param_2,long param_3,int param_4,long param_5,int param_6,long param_7,
          int param_8)

{
  int iVar1;
  undefined8 uVar2;
  
  iVar1 = FUN_ffff0000b800e378();
  uVar2 = 0xffffffff;
  if (((((param_1 != 0) && (param_3 != 0)) && (param_5 != 0)) &&
      ((param_7 != 0 && (uVar2 = 0xffffffff, param_2 != 0)))) &&
     ((param_4 != 0 && ((param_6 != 0 && (param_8 != 0)))))) {
    if (iVar1 == 1) {
      uVar2 = thunk_FUN_ffff0000b8067fd4
                        (param_1,param_2,param_3,param_4,param_5,param_6,param_7,param_8);
      return uVar2;
    }
    uVar2 = 0xf;
  }
  return uVar2;
}


