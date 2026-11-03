// ffff00009074b690  size=52  FUN_ffff00009074b690

undefined8 FUN_ffff00009074b690(int param_1)

{
  undefined8 uVar1;
  uint *puVar2;
  
  puVar2 = (uint *)&DAT_ffff000032110100;
  if (param_1 == 0x1e) {
    puVar2 = (uint *)&DAT_ffff000032120100;
  }
  else if (param_1 != 0xb9) {
    if (param_1 == 0xb8) {
      puVar2 = (uint *)&DAT_ffff000032530100;
    }
    uVar1 = FUN_ffff00009074b6c4(puVar2);
    return uVar1;
  }
  *puVar2 = *puVar2 | 1;
  *puVar2 = *puVar2 | 4;
  return 0;
}


