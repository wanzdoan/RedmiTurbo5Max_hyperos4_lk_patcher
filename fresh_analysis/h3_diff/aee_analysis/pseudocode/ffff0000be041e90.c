
undefined8 FUN_ffff0000be041e90(int param_1)

{
  uint *puVar1;
  uint *puVar2;
  
  if (param_1 == 0x21) {
    puVar2 = (uint *)&DAT_ffff0000320b00a0;
  }
  else {
    puVar1 = (uint *)0xffff0000320900a0;
    if (param_1 == 0x13) {
      puVar1 = (uint *)&DAT_ffff0000320a00a0;
    }
    puVar2 = (uint *)0xffff0000320c00a0;
    if (param_1 != 0x20) {
      puVar2 = puVar1;
    }
  }
  *puVar2 = *puVar2 | 4;
  *puVar2 = *puVar2 | 1;
  return 0;
}


