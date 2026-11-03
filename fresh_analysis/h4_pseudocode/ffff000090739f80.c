
undefined8 FUN_ffff000090739f80(int param_1)

{
  uint uVar1;
  
  if (DAT_ffff00009086dec0 != 0) {
    uVar1 = 0x95000000;
    if (param_1 != 0) {
      uVar1 = 0x95000080;
    }
    FUN_ffff000090739b00
              ((uint *)(DAT_ffff00009086dec0 + 0x40),
               uVar1 | *(uint *)(DAT_ffff00009086dec0 + 0x40) & 0x6affff7f);
    return 0;
  }
  return 0xffffffff;
}


