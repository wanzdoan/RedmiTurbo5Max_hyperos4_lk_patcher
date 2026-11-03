
undefined8 FUN_ffff0000be043344(uint param_1)

{
  undefined4 *puVar1;
  
  FUN_ffff0000be03fba0();
  if ((param_1 & 0xfffffffe) == 0xe2) {
    puVar1 = (undefined4 *)&DAT_ffff000032800b30;
    if (param_1 != 0xe2) {
      puVar1 = (undefined4 *)&DAT_ffff000032800b40;
    }
    *puVar1 = 7;
  }
  return 0;
}


