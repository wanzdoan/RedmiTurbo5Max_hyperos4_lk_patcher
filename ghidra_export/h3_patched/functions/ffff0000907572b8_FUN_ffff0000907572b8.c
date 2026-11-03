// ffff0000907572b8  size=88  FUN_ffff0000907572b8

undefined8 FUN_ffff0000907572b8(int param_1)

{
  uint *puVar1;
  uint *puVar2;
  
  puVar2 = (uint *)&DAT_ffff000032060030;
  if (param_1 != 0x29) {
    puVar2 = (uint *)&DAT_ffff000032050030;
  }
  puVar1 = (uint *)&DAT_ffff000032070030;
  if (param_1 != 0xf2) {
    puVar1 = puVar2;
  }
  puVar2 = (uint *)&DAT_ffff000032080030;
  if (param_1 != 0xf3) {
    puVar2 = puVar1;
  }
  *puVar2 = *puVar2 | 1;
  *puVar2 = *puVar2 | 4;
  return 0;
}


