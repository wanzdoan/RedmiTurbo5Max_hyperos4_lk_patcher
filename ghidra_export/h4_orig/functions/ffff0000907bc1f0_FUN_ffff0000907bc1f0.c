// ffff0000907bc1f0  size=88  FUN_ffff0000907bc1f0

undefined8 FUN_ffff0000907bc1f0(undefined8 param_1,undefined4 *param_2)

{
  undefined4 uVar1;
  
  thunk_FUN_ffff00009077eb20(param_2,param_1,8);
  uVar1 = FUN_ffff0000907c1050(*param_2);
  *param_2 = uVar1;
  uVar1 = FUN_ffff0000907c1050(param_2[1]);
  param_2[1] = uVar1;
  return 1;
}


