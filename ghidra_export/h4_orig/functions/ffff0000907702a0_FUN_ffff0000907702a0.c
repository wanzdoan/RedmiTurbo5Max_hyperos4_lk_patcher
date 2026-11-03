// ffff0000907702a0  size=32  FUN_ffff0000907702a0

uint FUN_ffff0000907702a0(uint *param_1,uint *param_2)

{
  uint uVar1;
  
  uVar1 = (uint)(*param_1 != *param_2);
  if (*param_1 < *param_2) {
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


