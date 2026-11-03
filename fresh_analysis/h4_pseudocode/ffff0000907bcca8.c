
void FUN_ffff0000907bcca8(ulong *param_1)

{
  ulong uVar1;
  
  if (*param_1 != 0) {
    uVar1 = 0;
    do {
      FUN_ffff0000907c1014(param_1[uVar1 + 1]);
      FUN_ffff0000907c1014(param_1[uVar1 + 0xb]);
      uVar1 = uVar1 + 1;
    } while (uVar1 < *param_1);
  }
  *param_1 = 0;
  FUN_ffff0000907c1014(param_1);
  return;
}


