// ffff000090739f90  size=72  FUN_ffff000090739f90

undefined8 FUN_ffff000090739f90(long param_1,int param_2)

{
  uint uVar1;
  
  uVar1 = 0x95000000;
  if (param_2 != 0) {
    uVar1 = 0x95000080;
  }
  FUN_ffff000090739b00((uint *)(param_1 + 0x40),uVar1 | *(uint *)(param_1 + 0x40) & 0x6affff7f);
  return 0;
}


