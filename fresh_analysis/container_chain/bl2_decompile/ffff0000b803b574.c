
void FUN_ffff0000b803b574(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff0000b80eec4c;
  if (param_1 != 7) {
    if (param_1 == 0x12) {
      FUN_ffff0000b803b5d4(&DAT_ffff0000b80f0164);
      FUN_ffff0000b803b5d4(&DAT_ffff0000b80f0290);
      return;
    }
    puVar1 = &DAT_ffff0000b80eec4c + (ulong)param_1 * 0x4b;
  }
  FUN_ffff0000b803b5d4(puVar1);
  return;
}


