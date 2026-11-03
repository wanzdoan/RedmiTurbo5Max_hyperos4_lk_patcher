// ffff00009075786c  size=52  FUN_ffff00009075786c

undefined8 FUN_ffff00009075786c(int param_1)

{
  undefined4 *puVar1;
  undefined4 *puVar2;
  
  puVar1 = (undefined4 *)&DAT_ffff000032560000;
  if (param_1 != 0xf6) {
    puVar1 = (undefined4 *)&DAT_ffff000032550000;
  }
  puVar2 = (undefined4 *)&DAT_ffff000032550000;
  if (param_1 != 0xf5) {
    puVar2 = puVar1;
  }
  *puVar2 = *puVar2;
  return 0;
}


