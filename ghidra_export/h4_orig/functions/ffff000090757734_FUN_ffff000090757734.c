// ffff000090757734  size=64  FUN_ffff000090757734

undefined8 FUN_ffff000090757734(int param_1)

{
  uint *puVar1;
  
  puVar1 = (uint *)&DAT_ffff0000321f067c;
  if (param_1 != 0x27) {
    puVar1 = (uint *)&DAT_ffff0000321e067c;
  }
  *puVar1 = *puVar1 | 1;
  *puVar1 = *puVar1 | 4;
  return 0;
}


