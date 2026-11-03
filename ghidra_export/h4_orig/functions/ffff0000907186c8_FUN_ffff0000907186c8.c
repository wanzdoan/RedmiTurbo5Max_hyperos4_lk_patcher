// ffff0000907186c8  size=48  FUN_ffff0000907186c8

void FUN_ffff0000907186c8(undefined4 *param_1)

{
  *(undefined8 *)(param_1 + 1) = 0;
  *(undefined8 *)(param_1 + 3) = 0;
  *(undefined8 *)(param_1 + 5) = 0;
  *(undefined8 *)(param_1 + 7) = 0;
  *param_1 = 0x74696d72;
  *(undefined8 *)(param_1 + 9) = 0;
  param_1[0xb] = 0;
  return;
}


