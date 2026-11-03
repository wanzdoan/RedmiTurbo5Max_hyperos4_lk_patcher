
void FUN_ffff00009070cc00(int *param_1,undefined8 param_2,int param_3)

{
  undefined8 uVar1;
  ulong uVar2;
  
  uVar1 = **(undefined8 **)(param_1 + 10);
  param_1[1] = param_3;
  *(undefined8 *)(param_1 + 4) = param_2;
  uVar2 = FUN_ffff00009077c144(uVar1,param_2,*(undefined8 *)(param_1 + 2),param_3);
  if (uVar2 == (uint)param_1[1]) {
    if (*param_1 == 0) {
      param_1[1] = 0;
      *(ulong *)(param_1 + 2) = *(long *)(param_1 + 2) + uVar2;
    }
  }
  else {
    *param_1 = -0x67;
  }
  return;
}


