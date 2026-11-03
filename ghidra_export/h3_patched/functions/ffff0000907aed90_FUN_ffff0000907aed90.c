// ffff0000907aed90  size=172  FUN_ffff0000907aed90

void FUN_ffff0000907aed90(ulong param_1,undefined8 param_2,long *param_3)

{
  long lVar1;
  ulong uVar2;
  
  FUN_ffff0000907aeb38(2,param_1);
  uVar2 = param_3[2];
  if (((~uVar2 < param_1) || ((ulong)param_3[1] < uVar2)) || (param_3[1] - uVar2 < param_1)) {
    lVar1 = uVar2 + param_1;
    if (~uVar2 < param_1) {
      lVar1 = -1;
    }
    param_3[2] = lVar1;
  }
  else {
    param_3[2] = uVar2 + param_1;
    if ((param_1 != 0) && (*param_3 != 0)) {
      FUN_ffff0000907acf74(*param_3 + uVar2,param_2,param_1);
      return;
    }
  }
  return;
}


