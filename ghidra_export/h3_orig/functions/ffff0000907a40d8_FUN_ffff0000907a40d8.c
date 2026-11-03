// ffff0000907a40d8  size=16  FUN_ffff0000907a40d8

undefined8 FUN_ffff0000907a40d8(undefined8 *param_1)

{
  if (param_1 != (undefined8 *)0x0) {
    *param_1 = 0;
    param_1[1] = 0x6a09e667;
    param_1[2] = 0xbb67ae85;
    param_1[3] = 0x3c6ef372;
    param_1[4] = 0xa54ff53a;
    param_1[5] = 0x510e527f;
    param_1[6] = 0x9b05688c;
    param_1[7] = 0x1f83d9ab;
    param_1[8] = 0x5be0cd19;
    param_1[9] = 0;
    return 0;
  }
  return 0xffffffff;
}


