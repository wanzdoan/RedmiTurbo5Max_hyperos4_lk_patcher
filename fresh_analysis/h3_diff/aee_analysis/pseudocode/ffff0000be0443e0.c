
void FUN_ffff0000be0443e0(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff0000be0dbd1c;
  if (param_1 != 7) {
    if (param_1 == 0xb) {
      FUN_ffff0000be044454(&DAT_ffff0000be0dc67c);
      puVar1 = (undefined4 *)&DAT_ffff0000be0dc8d4;
    }
    else if (param_1 == 0x11) {
      FUN_ffff0000be044454(&DAT_ffff0000be0dbd1c);
      puVar1 = (undefined4 *)&DAT_ffff0000be0dc1cc;
    }
    else {
      puVar1 = &DAT_ffff0000be0dbd1c + (ulong)param_1 * 0x4b;
    }
  }
  FUN_ffff0000be044454(puVar1);
  return;
}


