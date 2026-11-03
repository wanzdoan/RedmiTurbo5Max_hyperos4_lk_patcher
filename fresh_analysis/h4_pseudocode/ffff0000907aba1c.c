
uint FUN_ffff0000907aba1c(ulong param_1)

{
  uint uVar1;
  
  uVar1 = (int)param_1 - ((uint)(param_1 >> 1) & 0x55555555);
  uVar1 = (uVar1 & 0x33333333) + (uVar1 >> 2 & 0x33333333);
  return (uVar1 + (uVar1 >> 4) & 0xf0f0f0f) * 0x1010101 >> 0x18;
}


