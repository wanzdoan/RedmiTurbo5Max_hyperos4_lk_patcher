// File offset entry: 0xa9338

void FUN_020a9238(long param_1)

{
  uint uVar1;
  uint *in_x9;
  long in_x10;
  
  uVar1 = *(uint *)(in_x10 + (param_1 - 1U & 0xffffffff) * 4);
  uVar1 = (uVar1 & 0xff00ff00) >> 8 | (uVar1 & 0xff00ff) << 8;
  *in_x9 = uVar1 >> 0x10 | uVar1 << 0x10;
  return;
}


