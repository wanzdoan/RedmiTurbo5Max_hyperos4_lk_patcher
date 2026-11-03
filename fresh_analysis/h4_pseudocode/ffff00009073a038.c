
undefined8 FUN_ffff00009073a038(int param_1)

{
  uint uVar1;
  
  if (DAT_ffff00009086dec0 != 0) {
    uVar1 = 0x77000000;
    if (param_1 != 0) {
      uVar1 = 0x77000002;
    }
    FUN_ffff000090739b00
              ((uint *)(DAT_ffff00009086dec0 + 0x60),
               uVar1 | *(uint *)(DAT_ffff00009086dec0 + 0x60) & 0x88fffffd);
    return 0;
  }
  return 0xffffffff;
}


