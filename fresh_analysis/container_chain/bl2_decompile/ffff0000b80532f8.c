
void FUN_ffff0000b80532f8(undefined4 *param_1,undefined4 param_2,undefined4 param_3)

{
  FUN_ffff0000b804d070((long)param_1 + 0xd,0,0x10);
  *param_1 = param_3;
  param_1[1] = param_2;
  param_1[8] = 0;
  param_1[2] = 3;
  *(undefined2 *)(param_1 + 3) = 0;
  *(undefined2 *)((long)param_1 + 0x1e) = 6;
  *(undefined8 *)(param_1 + 10) = 0;
  return;
}


