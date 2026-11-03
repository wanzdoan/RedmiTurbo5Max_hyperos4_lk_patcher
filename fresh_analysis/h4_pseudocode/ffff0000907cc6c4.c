
undefined8 FUN_ffff0000907cc6c4(short *param_1,long param_2)

{
  int iVar1;
  ulong uVar2;
  undefined8 uVar3;
  
  if (*(long *)(param_2 + 200) == 0) {
    uVar3 = FUN_ffff0000907c9170(param_1,param_1,param_2 + 8);
    return uVar3;
  }
  if (((-1 < *param_1) || (iVar1 = FUN_ffff0000907c7d34(param_1,0), iVar1 == 0)) &&
     (uVar2 = FUN_ffff0000907c7628(param_1), uVar2 <= (ulong)(*(long *)(param_2 + 0xb0) * 2))) {
    uVar3 = (**(code **)(param_2 + 200))(param_1);
    if ((int)uVar3 != 0) {
      return uVar3;
    }
    param_2 = param_2 + 8;
    while ((*param_1 < 0 && (iVar1 = FUN_ffff0000907c7d34(param_1,0), iVar1 != 0))) {
      uVar3 = FUN_ffff0000907c8248(param_1,param_1,param_2);
      if ((int)uVar3 != 0) {
        return uVar3;
      }
    }
    do {
      iVar1 = FUN_ffff0000907c7c44(param_1,param_2);
      if (iVar1 < 0) {
        return 0;
      }
      uVar3 = FUN_ffff0000907c8058(param_1,param_1,param_2);
    } while ((int)uVar3 == 0);
    return uVar3;
  }
  return 0xffffb080;
}


