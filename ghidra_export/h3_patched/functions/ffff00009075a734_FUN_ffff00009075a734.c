// ffff00009075a734  size=60  FUN_ffff00009075a734

undefined8 FUN_ffff00009075a734(int param_1)

{
  undefined4 *puVar1;
  
  puVar1 = (undefined4 *)((ulong)(param_1 == 0x1d) << 0x10 | 0xffff000032180008);
  *puVar1 = 0;
  puVar1[-2] = puVar1[-2] & 0xfffffffe;
  puVar1[1] = 0;
  return 0;
}


