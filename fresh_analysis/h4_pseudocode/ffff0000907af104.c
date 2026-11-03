
void FUN_ffff0000907af104(undefined8 param_1,long *param_2)

{
  long lVar1;
  ulong uVar2;
  ulong uVar3;
  
  uVar2 = FUN_ffff0000907ad25c();
  FUN_ffff0000907aee00(3,uVar2,param_2);
  uVar3 = param_2[2];
  if (((~uVar3 < uVar2) || ((ulong)param_2[1] < uVar3)) || (param_2[1] - uVar3 < uVar2)) {
    lVar1 = uVar3 + uVar2;
    if (~uVar3 < uVar2) {
      lVar1 = -1;
    }
    param_2[2] = lVar1;
  }
  else {
    param_2[2] = uVar3 + uVar2;
    if ((uVar2 != 0) && (*param_2 != 0)) {
      FUN_ffff0000907ad234(*param_2 + uVar3,param_1,uVar2);
      return;
    }
  }
  return;
}


