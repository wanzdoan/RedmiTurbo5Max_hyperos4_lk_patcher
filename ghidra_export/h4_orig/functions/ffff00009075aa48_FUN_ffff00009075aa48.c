// ffff00009075aa48  size=52  FUN_ffff00009075aa48

undefined8 FUN_ffff00009075aa48(int param_1)

{
  uint *puVar1;
  
  puVar1 = (uint *)&DAT_ffff000032190020;
  if (param_1 != 0x1d) {
    puVar1 = (uint *)&DAT_ffff000032180020;
  }
  *puVar1 = *puVar1 | 1;
  return 0;
}


