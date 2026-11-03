
void FUN_ffff0000be0446a4(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff0000be0dbd1c;
  if (param_1 != 7) {
    if (param_1 == 0xb) {
      FUN_ffff0000be044704(&DAT_ffff0000be0dc67c);
      FUN_ffff0000be044704(&DAT_ffff0000be0dc8d4);
      return;
    }
    puVar1 = &DAT_ffff0000be0dbd1c + (ulong)param_1 * 0x4b;
  }
  FUN_ffff0000be044704(puVar1);
  return;
}


