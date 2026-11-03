// ffff00009073a048  size=76  FUN_ffff00009073a048

undefined8 FUN_ffff00009073a048(long param_1,int param_2)

{
  uint uVar1;
  
  uVar1 = 0x77000000;
  if (param_2 != 0) {
    uVar1 = 0x77000002;
  }
  FUN_ffff000090739b00((uint *)(param_1 + 0x60),uVar1 | *(uint *)(param_1 + 0x60) & 0x88fffffd);
  return 0;
}


