// ffff00009075aa08  size=64  FUN_ffff00009075aa08

undefined8 FUN_ffff00009075aa08(int param_1)

{
  uint *puVar1;
  
  puVar1 = (uint *)&DAT_ffff000032190004;
  if (param_1 != 0x1d) {
    puVar1 = (uint *)&DAT_ffff000032180004;
  }
  *puVar1 = *puVar1 | 1;
  *puVar1 = *puVar1 & 0xfffffffe;
  return 0;
}


