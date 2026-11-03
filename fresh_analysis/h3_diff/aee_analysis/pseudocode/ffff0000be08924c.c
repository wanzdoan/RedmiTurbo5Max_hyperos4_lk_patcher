
void FUN_ffff0000be08924c(ulong param_1,undefined8 param_2,long *param_3)

{
  long lVar1;
  ulong uVar2;
  
  FUN_ffff0000be088ff4(2,param_1);
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
      FUN_ffff0000be087428(*param_3 + uVar2,param_2,param_1);
      return;
    }
  }
  return;
}


