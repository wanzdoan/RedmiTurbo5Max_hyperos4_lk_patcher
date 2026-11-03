// ffff000090739c94  size=76  FUN_ffff000090739c94

undefined8 FUN_ffff000090739c94(long param_1,int param_2)

{
  uint uVar1;
  
  uVar1 = 0x59000000;
  if (param_2 != 0) {
    uVar1 = 0x59000080;
  }
  FUN_ffff000090739868((uint *)(param_1 + 0x30),uVar1 | *(uint *)(param_1 + 0x30) & 0xa6ffff7f);
  return 0;
}


