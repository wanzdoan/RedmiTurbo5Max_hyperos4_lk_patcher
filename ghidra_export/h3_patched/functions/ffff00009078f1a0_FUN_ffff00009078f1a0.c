// ffff00009078f1a0  size=112  FUN_ffff00009078f1a0

void FUN_ffff00009078f1a0
               (undefined4 *param_1,undefined4 param_2,undefined4 param_3,undefined8 param_4)

{
  FUN_ffff00009077eb88((long)param_1 + 0xd,0,0x10);
  *param_1 = param_3;
  param_1[1] = param_2;
  param_1[2] = 2;
  param_1[8] = 8;
  *(undefined2 *)(param_1 + 3) = 0x2500;
  *(undefined2 *)((long)param_1 + 0x1e) = 10;
  *(undefined8 *)(param_1 + 10) = param_4;
  return;
}


