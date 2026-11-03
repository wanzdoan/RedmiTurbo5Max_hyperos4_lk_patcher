// ffff00009078f564  size=100  FUN_ffff00009078f564

void FUN_ffff00009078f564(undefined4 *param_1,undefined4 param_2,undefined4 param_3)

{
  FUN_ffff00009077ee50((long)param_1 + 0xd,0,0x10);
  *param_1 = param_3;
  param_1[1] = param_2;
  param_1[8] = 0;
  param_1[2] = 3;
  *(undefined2 *)(param_1 + 3) = 0;
  *(undefined2 *)((long)param_1 + 0x1e) = 6;
  *(undefined8 *)(param_1 + 10) = 0;
  return;
}


