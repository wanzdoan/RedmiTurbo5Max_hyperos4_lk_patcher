// ffff0000907580d0  size=88  FUN_ffff0000907580d0

undefined8 FUN_ffff0000907580d0(uint param_1)

{
  undefined4 *puVar1;
  
  FUN_ffff0000907546d4();
  if ((param_1 & 0xfffffffe) == 0xe2) {
    puVar1 = (undefined4 *)&DAT_ffff000032800b30;
    if (param_1 != 0xe2) {
      puVar1 = (undefined4 *)&DAT_ffff000032800b40;
    }
    *puVar1 = 7;
  }
  return 0;
}


