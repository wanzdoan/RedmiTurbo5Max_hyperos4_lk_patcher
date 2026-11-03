// ffff0000907575f4  size=52  FUN_ffff0000907575f4

undefined8 FUN_ffff0000907575f4(int param_1)

{
  undefined4 *puVar1;
  
  puVar1 = (undefined4 *)&DAT_ffff0000320e00f0;
  if ((param_1 - 0x5bU & 0xfffffffd) != 0) {
    puVar1 = (undefined4 *)&DAT_ffff0000320d00f0;
  }
  *puVar1 = 1;
  return 0;
}


