// ffff000090759430  size=44  FUN_ffff000090759430

void FUN_ffff000090759430(uint param_1)

{
  undefined4 *puVar1;
  
  puVar1 = &DAT_ffff00009081f3dc;
  if (param_1 != 7) {
    if (param_1 == 0xb) {
      FUN_ffff000090759490(&DAT_ffff00009081fd3c);
      FUN_ffff000090759490(&DAT_ffff00009081ff94);
      return;
    }
    puVar1 = &DAT_ffff00009081f3dc + (ulong)param_1 * 0x4b;
  }
  FUN_ffff000090759490(puVar1);
  return;
}


