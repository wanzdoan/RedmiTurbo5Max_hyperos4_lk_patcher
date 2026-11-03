
void FUN_ffff0000b806b9e0(long *param_1)

{
  ulong uVar1;
  
  uVar1 = param_1[2];
  if (uVar1 != 0xffffffffffffffff) {
    if (uVar1 < (ulong)param_1[1]) {
      *(undefined1 *)(*param_1 + uVar1) = 0xf6;
      uVar1 = param_1[2];
    }
    uVar1 = uVar1 + 1;
  }
  param_1[2] = uVar1;
  return;
}


