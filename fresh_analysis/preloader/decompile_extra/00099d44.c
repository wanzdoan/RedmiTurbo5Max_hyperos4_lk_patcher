// File offset entry: 0x99d44

void FUN_02099c44(uint param_1)

{
  uRam000000001c010024 = uRam000000001c010024 & 0xfffff80f | (param_1 & 0xff) << 4;
  return;
}


