// ffff000090739f2c  size=76  FUN_ffff000090739f2c

undefined8 FUN_ffff000090739f2c(long param_1,int param_2)

{
  uint uVar1;
  
  uVar1 = 0x59000000;
  if (param_2 != 0) {
    uVar1 = 0x59000080;
  }
  FUN_ffff000090739b00((uint *)(param_1 + 0x30),uVar1 | *(uint *)(param_1 + 0x30) & 0xa6ffff7f);
  return 0;
}


