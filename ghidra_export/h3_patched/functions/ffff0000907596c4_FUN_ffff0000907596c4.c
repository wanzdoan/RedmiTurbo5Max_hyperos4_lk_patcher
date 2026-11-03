// ffff0000907596c4  size=44  FUN_ffff0000907596c4

void FUN_ffff0000907596c4(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff00009081f3dc;
  if (param_1 != 7) {
    if (param_1 == 0x12) {
      FUN_ffff000090759724(&DAT_ffff0000908208f4);
      FUN_ffff000090759724(&DAT_ffff000090820a20);
      return;
    }
    puVar1 = &DAT_ffff00009081f3dc + (ulong)param_1 * 0x4b;
  }
  FUN_ffff000090759724(puVar1);
  return;
}


