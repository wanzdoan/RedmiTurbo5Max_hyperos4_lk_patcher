
undefined8 FUN_ffff000090739f1c(int param_1)

{
  uint uVar1;
  
  if (DAT_ffff00009086dec0 != 0) {
    uVar1 = 0x59000000;
    if (param_1 != 0) {
      uVar1 = 0x59000080;
    }
    FUN_ffff000090739b00
              ((uint *)(DAT_ffff00009086dec0 + 0x30),
               uVar1 | *(uint *)(DAT_ffff00009086dec0 + 0x30) & 0xa6ffff7f);
    return 0;
  }
  return 0xffffffff;
}


