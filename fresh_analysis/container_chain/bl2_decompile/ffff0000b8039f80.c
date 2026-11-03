
undefined8 FUN_ffff0000b8039f80(uint param_1)

{
  undefined4 *puVar1;
  
  FUN_ffff0000b80367dc();
  if ((param_1 & 0xfffffffe) == 0xe2) {
    puVar1 = (undefined4 *)&DAT_ffff000032800b30;
    if (param_1 != 0xe2) {
      puVar1 = (undefined4 *)&DAT_ffff000032800b40;
    }
    *puVar1 = 7;
  }
  return 0;
}


