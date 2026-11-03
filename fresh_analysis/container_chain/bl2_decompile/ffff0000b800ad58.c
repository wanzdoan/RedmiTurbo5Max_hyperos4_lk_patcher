
void FUN_ffff0000b800ad58(undefined4 *param_1)

{
  *(undefined8 *)(param_1 + 2) = 0;
  param_1[4] = 0;
  *param_1 = 0x6d757478;
  param_1[6] = 0x77616974;
  param_1[0xc] = 0;
  *(undefined4 **)(param_1 + 8) = param_1 + 8;
  *(undefined4 **)(param_1 + 10) = param_1 + 8;
  return;
}


