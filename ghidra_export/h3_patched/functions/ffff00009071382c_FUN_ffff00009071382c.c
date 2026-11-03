// ffff00009071382c  size=20  FUN_ffff00009071382c

uint FUN_ffff00009071382c(uint *param_1)

{
  uint uVar1;
  
  uVar1 = (*param_1 & 0xff00ff00) >> 8 | (*param_1 & 0xff00ff) << 8;
  return uVar1 >> 0x10 | uVar1 << 0x10;
}


