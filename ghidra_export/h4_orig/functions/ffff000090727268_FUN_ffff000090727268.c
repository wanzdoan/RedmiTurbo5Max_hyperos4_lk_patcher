// ffff000090727268  size=100  FUN_ffff000090727268

undefined8 FUN_ffff000090727268(long param_1)

{
  ulong uVar1;
  
  uVar1 = FUN_ffff00009071b008();
  *(ulong *)(param_1 + 8) =
       ((uVar1 + 0x1fffff >> 0x15) + (uVar1 + 0x3fffffff >> 0x1e)) * 0x88 + (uVar1 >> 0xc) * 8 +
       0x80007f & 0x1ffffffffe00000;
  return 0;
}


