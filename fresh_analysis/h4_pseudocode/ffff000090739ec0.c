
undefined8 FUN_ffff000090739ec0(uint param_1)

{
  if (DAT_ffff00009086dec0 != 0) {
    if (0x1fffe < param_1) {
      param_1 = 0x1ffff;
    }
    FUN_ffff000090739b00
              ((uint *)(DAT_ffff00009086dec0 + 0x50),
               param_1 | 0x95000000 | *(uint *)(DAT_ffff00009086dec0 + 0x50) & 0xfffe0000);
    return 0;
  }
  return 0xffffffff;
}


