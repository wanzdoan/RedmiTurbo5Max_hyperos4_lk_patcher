
void FUN_ffff0000be06d05c
               (undefined4 *param_1,undefined4 param_2,undefined4 param_3,undefined8 param_4,
               undefined1 param_5,undefined1 param_6)

{
  FUN_ffff0000be060ae0((long)param_1 + 0xd,0,0x10);
  *param_1 = param_3;
  param_1[1] = param_2;
  *(undefined1 *)((long)param_1 + 0xe) = param_5;
  param_1[2] = 2;
  param_1[8] = 0x24;
  *(undefined2 *)(param_1 + 3) = 0x1200;
  *(undefined2 *)((long)param_1 + 0x1e) = 6;
  *(undefined1 *)((long)param_1 + 0xf) = param_6;
  *(undefined1 *)((long)param_1 + 0x12) = 0;
  *(undefined2 *)(param_1 + 4) = 0x2400;
  *(undefined8 *)(param_1 + 10) = param_4;
  return;
}


