// ffff0000907571b4  size=64  FUN_ffff0000907571b4

undefined8 FUN_ffff0000907571b4(int param_1)

{
  uint *puVar1;
  
  puVar1 = (uint *)&DAT_ffff0000320400f0;
  if (param_1 != 0x1a) {
    puVar1 = (uint *)&DAT_ffff0000320300f0;
  }
  *puVar1 = *puVar1 | 1;
  *puVar1 = *puVar1 | 4;
  return 0;
}


