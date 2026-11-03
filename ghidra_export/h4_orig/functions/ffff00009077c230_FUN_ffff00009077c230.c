// ffff00009077c230  size=200  FUN_ffff00009077c230

void FUN_ffff00009077c230
               (undefined8 *param_1,undefined8 param_2,undefined8 param_3,uint param_4,
               undefined8 param_5,undefined8 param_6,undefined4 param_7)

{
  uint uVar1;
  undefined8 uVar2;
  
  *param_1 = 0;
  param_1[1] = 0;
  uVar2 = FUN_ffff00009077f0f4(param_2);
  uVar1 = 0;
  if ((int)param_3 != 0) {
    uVar1 = (uint)LZCOUNT((int)param_3) ^ 0x1f;
  }
  *(uint *)(param_1 + 7) = param_4;
  param_1[0xb] = param_5;
  param_1[0xc] = param_6;
  *(undefined1 *)(param_1 + 0xd) = 0;
  param_1[5] = param_3;
  param_1[6] = (ulong)uVar1;
  param_1[3] = uVar2;
  param_1[4] = (ulong)param_4 << (ulong)uVar1;
  *(undefined4 *)(param_1 + 2) = 0;
  param_1[8] = 0;
  *(undefined1 *)(param_1 + 10) = 0;
  param_1[0xe] = FUN_ffff00009077c2f8;
  param_1[0xf] = FUN_ffff00009077c5a8;
  *(undefined4 *)((long)param_1 + 0x6c) = param_7;
  param_1[0x14] = 0;
  param_1[0x10] = FUN_ffff00009077c5b8;
  param_1[0x11] = FUN_ffff00009077c8e0;
  param_1[0x12] = FUN_ffff00009077c8f0;
  return;
}


