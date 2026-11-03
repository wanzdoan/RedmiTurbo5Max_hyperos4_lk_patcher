// File offset entry: 0x3598

uint FUN_02003498(uint param_1)

{
  uint uVar1;
  
  uVar1 = (param_1 & 0xff00ff00) >> 8 | (param_1 & 0xff00ff) << 8;
  return uVar1 >> 0x10 | uVar1 << 0x10;
}


