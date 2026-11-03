
void FUN_ffff0000be044938(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff0000be0dbd1c;
  if (param_1 != 7) {
    if (param_1 == 0x12) {
      FUN_ffff0000be044998(&DAT_ffff0000be0dd234);
      FUN_ffff0000be044998(&DAT_ffff0000be0dd360);
      return;
    }
    puVar1 = &DAT_ffff0000be0dbd1c + (ulong)param_1 * 0x4b;
  }
  FUN_ffff0000be044998(puVar1);
  return;
}


