// ffff0000907c09ac  size=48  FUN_ffff0000907c09ac

int FUN_ffff0000907c09ac(ulong *param_1)

{
  ulong uVar1;
  ulong uVar2;
  
  uVar1 = *param_1;
  uVar2 = uVar1 / 10;
  *param_1 = uVar2;
  return (int)uVar1 + (int)uVar2 * -10;
}


