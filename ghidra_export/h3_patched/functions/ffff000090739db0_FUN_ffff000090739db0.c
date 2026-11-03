// ffff000090739db0  size=76  FUN_ffff000090739db0

undefined8 FUN_ffff000090739db0(long param_1,int param_2)

{
  uint uVar1;
  
  uVar1 = 0x77000000;
  if (param_2 != 0) {
    uVar1 = 0x77000002;
  }
  FUN_ffff000090739868((uint *)(param_1 + 0x60),uVar1 | *(uint *)(param_1 + 0x60) & 0x88fffffd);
  return 0;
}


