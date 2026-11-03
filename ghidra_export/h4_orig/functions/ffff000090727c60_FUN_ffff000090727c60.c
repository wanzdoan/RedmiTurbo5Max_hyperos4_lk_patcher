// ffff000090727c60  size=48  FUN_ffff000090727c60

void FUN_ffff000090727c60(undefined4 *param_1,undefined4 *param_2,int param_3)

{
  uint uVar1;
  
  if (0 < param_3) {
    uVar1 = param_3 + 4;
    do {
      uVar1 = uVar1 - 4;
      *param_1 = *param_2;
      param_2 = param_2 + 1;
      param_1 = param_1 + 1;
    } while (4 < uVar1);
  }
  return;
}


