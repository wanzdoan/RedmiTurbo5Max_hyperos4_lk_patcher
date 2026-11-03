// ffff00009075738c  size=64  FUN_ffff00009075738c

undefined8 FUN_ffff00009075738c(int param_1)

{
  uint *puVar1;
  
  puVar1 = (uint *)&DAT_ffff000032140034;
  if (param_1 != 0x1c) {
    puVar1 = (uint *)&DAT_ffff000032130034;
  }
  *puVar1 = *puVar1 | 2;
  *puVar1 = *puVar1 | 4;
  return 0;
}


