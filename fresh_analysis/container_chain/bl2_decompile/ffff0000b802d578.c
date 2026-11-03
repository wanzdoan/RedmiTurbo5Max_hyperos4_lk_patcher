
undefined8 FUN_ffff0000b802d578(int param_1)

{
  uint *puVar1;
  
  if (param_1 == 0x1e) {
    puVar1 = (uint *)&DAT_ffff000032120100;
  }
  else {
    puVar1 = (uint *)&DAT_ffff000032110100;
    if ((param_1 != 0xb9) && (param_1 == 0xb8)) {
      puVar1 = (uint *)&DAT_ffff000032530100;
    }
  }
  *puVar1 = *puVar1 | 1;
  *puVar1 = *puVar1 | 4;
  return 0;
}


