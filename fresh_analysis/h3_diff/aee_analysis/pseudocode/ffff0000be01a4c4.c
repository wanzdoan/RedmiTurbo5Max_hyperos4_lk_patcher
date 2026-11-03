
void FUN_ffff0000be01a4c4(undefined4 *param_1,undefined1 param_2,undefined4 param_3)

{
  *(undefined1 *)(param_1 + 1) = param_2;
  param_1[2] = param_3;
  *param_1 = 0x65766e74;
  param_1[4] = 0x77616974;
  param_1[10] = 0;
  *(undefined4 **)(param_1 + 6) = param_1 + 6;
  *(undefined4 **)(param_1 + 8) = param_1 + 6;
  return;
}


