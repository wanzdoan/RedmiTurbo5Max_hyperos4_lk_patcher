// ffff0000907aa374  size=88  FUN_ffff0000907aa374

undefined8 FUN_ffff0000907aa374(long param_1,uint param_2)

{
  undefined4 uVar1;
  int in_w8;
  int iVar2;
  ulong uVar3;
  
  uVar3 = (ulong)(param_2 >> 2);
  iVar2 = in_w8 + 0xdc;
  param_1 = param_1 + 3;
  do {
    uVar1 = thunk_FUN_ffff00009074b22c(iVar2);
    iVar2 = iVar2 + 1;
    *(undefined4 *)(param_1 + -3) = uVar1;
    param_1 = param_1 + 4;
    uVar3 = uVar3 - 1;
  } while (uVar3 != 0);
  return 0;
}


