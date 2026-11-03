// ffff000090757b04  size=52  FUN_ffff000090757b04

undefined8 FUN_ffff000090757b04(int param_1)

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


