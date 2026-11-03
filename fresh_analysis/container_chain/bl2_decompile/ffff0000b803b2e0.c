
void FUN_ffff0000b803b2e0(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff0000b80eec4c;
  if (param_1 != 7) {
    if (param_1 == 0xb) {
      FUN_ffff0000b803b340(&DAT_ffff0000b80ef5ac);
      FUN_ffff0000b803b340(&DAT_ffff0000b80ef804);
      return;
    }
    puVar1 = &DAT_ffff0000b80eec4c + (ulong)param_1 * 0x4b;
  }
  FUN_ffff0000b803b340(puVar1);
  return;
}


