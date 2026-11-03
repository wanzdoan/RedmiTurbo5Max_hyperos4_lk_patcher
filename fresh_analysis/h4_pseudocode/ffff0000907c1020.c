
int FUN_ffff0000907c1020(ulong *param_1)

{
  ulong uVar1;
  ulong uVar2;
  
  uVar1 = *param_1;
  uVar2 = uVar1 / 10;
  *param_1 = uVar2;
  return (int)uVar1 + (int)uVar2 * -10;
}


