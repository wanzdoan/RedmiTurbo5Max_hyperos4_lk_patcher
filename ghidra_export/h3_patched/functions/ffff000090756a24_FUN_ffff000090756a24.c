// ffff000090756a24  size=64  FUN_ffff000090756a24

undefined8 FUN_ffff000090756a24(int param_1)

{
  uint *puVar1;
  
  puVar1 = (uint *)&DAT_ffff000032100cb0;
  if (param_1 != 0x12) {
    puVar1 = (uint *)&DAT_ffff0000320f0cb0;
  }
  *puVar1 = *puVar1 | 1;
  *puVar1 = *puVar1 | 4;
  return 0;
}


