// ffff00009070edb4  size=68  FUN_ffff00009070edb4

void FUN_ffff00009070edb4(undefined8 *param_1,undefined8 param_2)

{
  FUN_ffff00009077eb88(param_1,0,0x30);
  *param_1 = 0;
  *(undefined4 *)(param_1 + 1) = 0;
  param_1[2] = 0;
  param_1[5] = param_2;
  return;
}


