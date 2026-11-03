
void FUN_ffff0000b805335c(undefined4 *param_1,undefined4 param_2)

{
  FUN_ffff0000b804d070((long)param_1 + 0xd,0,0x10);
  param_1[8] = 0;
  *(undefined1 *)(param_1 + 3) = 0;
  *param_1 = 0xd0;
  param_1[1] = param_2;
  param_1[2] = 3;
  *(undefined4 *)((long)param_1 + 0xd) = 0x1b;
  *(undefined2 *)((long)param_1 + 0x1e) = 6;
  *(undefined2 *)((long)param_1 + 0x11) = 0x30;
  *(undefined8 *)(param_1 + 10) = 0;
  return;
}


